package dataflow

import chisel3._
import chisel3.util._
import chisel3.Module
import config.{CoreParams, Parameters}
import interfaces.{ControlBundle, DataBundle}
import node._
import utility.UniformPrintfs

class SyncIO(NumOuts: Int, NumInc: Int, NumDec: Int)(implicit p: Parameters)
  extends HandShakingIONPS(NumOuts)(new ControlBundle)(p) {
  val incIn = Flipped(Vec(NumInc, Decoupled(new ControlBundle())))
  val decIn = Flipped(Vec(NumDec, Decoupled(new ControlBundle())))

  override def cloneType = new SyncIO(NumOuts, NumInc, NumDec).asInstanceOf[this.type]
}

class Sync(NumOuts: Int, NumInc: Int, NumDec: Int, ID: Int)
          (implicit p: Parameters,
           name: sourcecode.Name,
           file: sourcecode.File)
  extends HandShakingCtrlNPS(NumOuts, ID)(p) {
  override lazy val io = IO(new SyncIO(NumOuts, NumInc, NumDec)(p))
  // Printf debugging
  val node_name = name.value
  val module_name = file.value.split("/").tail.last.split("\\.").head.capitalize
  override val printfSigil = "[" + module_name + "] " + node_name + ": " + ID + " "
  val (cycleCount, _) = Counter(true.B, 32 * 1024)

  /*===========================================*
   *            Registers                      *
   *===========================================*/

  // State machine
  val s_IDLE :: s_COMPUTE :: s_DONE :: Nil = Enum(3)
  val state = RegInit(s_IDLE)

  //val enableID = RegInit(0.U(1 << tlen))
  val syncCount = RegInit(0.U(8.W))

  /*==========================================*
   *           Predicate Evaluation           *
   *==========================================*/

  val predicate = IsEnable()
  val start = IsEnableValid()

  io.enable.ready := (state === s_IDLE)

  /*============================================*
   *            ACTIONS (possibly dangerous)    *
   *============================================*/
  val incArb = Module(new Arbiter(new ControlBundle, NumInc))
  val decArb = Module(new Arbiter(new ControlBundle, NumDec))

  incArb.io.in <> io.incIn
  decArb.io.in <> io.decIn

  incArb.io.out.ready := true.B
  decArb.io.out.ready := true.B
  val inc = incArb.io.out.fire() && incArb.io.out.bits.control
  val dec = decArb.io.out.fire() && decArb.io.out.bits.control
  when(inc && !dec) {
    syncCount := syncCount + 1.U
  }.elsewhen(!inc && dec) {
    syncCount := syncCount - 1.U
  }

  for (i <- 0 until NumOuts) {
    io.Out(i).bits := enable_R
  }

  switch(state) {
    is(s_IDLE) {
      when(start) {
        when(predicate) {
          state := s_COMPUTE
        }.otherwise {
          //          Reset()
          printf("[LOG] " + "[" + module_name + "] " + node_name + ": Read value only @ %d, value: %d\n", cycleCount, syncCount)
          ValidOut()
          state := s_DONE
        }
      }
    }
    is(s_COMPUTE) {
      when(syncCount === 0.U) {
        ValidOut()
        printf("[LOG] " + "[" + module_name + "] " + node_name + ": Output fired @ %d\n", cycleCount)
        state := s_DONE
      }
    }
    is(s_DONE) {
      when(IsOutReady()) {
        Reset()
        syncCount := 0.U
        printf("[LOG] " + "[" + module_name + "] " + node_name + ": Output restarted @ %d\n", cycleCount)
        state := s_IDLE
      }
    }
  }


}


class SyncNodeIO(val NumOuts: Int)(implicit p: Parameters)
  extends Bundle {
  val incIn = Flipped(Decoupled(new ControlBundle()))
  val decIn = Flipped(Decoupled(new ControlBundle()))
  val Out = Vec(NumOuts, Decoupled(new ControlBundle()))
}

class SyncNode(NumOuts: Int, ID: Int,
               NumInc: Int = 1, NumDec: Int = 1)
              (implicit val p: Parameters,
               name: sourcecode.Name,
               file: sourcecode.File)
  extends Module with CoreParams with UniformPrintfs {
  override lazy val io = IO(new SyncNodeIO(NumOuts))

  // Printf debugging
  val node_name = name.value
  val module_name = file.value.split("/").tail.last.split("\\.").head.capitalize
  override val printfSigil = "[" + module_name + "] " + node_name + ": " + ID + " "
  val (cycleCount, _) = Counter(true.B, 32 * 1024)

  val out_fire_W = WireInit(false.B)

  val inc_R = RegInit(ControlBundle.default)
  val inc_valid_R = RegInit(false.B)

  val dec_R = RegInit(ControlBundle.default)
  val dec_valid_R = RegInit(false.B)

  // Output Handshaking
  val out_ready_R = RegInit(VecInit(Seq.fill(NumOuts)(false.B)))
  val out_valid_R = RegInit(VecInit(Seq.fill(NumOuts)(false.B)))
  val out_ready_W = WireInit(VecInit(Seq.fill(NumOuts)(false.B)))

  // Defining states
  val s_IDLE :: s_COUNT :: s_WAIT :: Nil = Enum(3)
  val state = RegInit(s_IDLE)

  val enableID = RegInit(0.U(1 << tlen))
  val sync_count = RegInit(0.U(8.W))

  // Latching inputs
  io.incIn.ready := ~inc_valid_R
  when(io.incIn.fire()) {
    inc_R <> io.incIn.bits
    inc_valid_R := true.B
  }

  io.decIn.ready := ~dec_valid_R
  when(io.decIn.fire()) {
    dec_R <> io.decIn.bits
    dec_valid_R := true.B
  }

  // Firing output
  for (i <- 0 until NumOuts) {
    io.Out(i).bits.control := out_fire_W
    io.Out(i).bits.taskID := enableID
  }

  // Wire up OUT READYs and VALIDs
  for (i <- 0 until NumOuts) {
    io.Out(i).valid := out_valid_R(i)
    out_ready_W(i) := io.Out(i).ready
    when(io.Out(i).fire()) {
      // Detecting when to reset
      out_ready_R(i) := io.Out(i).ready
      // Propagating output
      out_valid_R(i) := false.B
    }
  }


  // Output value
  switch(state) {
    is(s_IDLE) {
      out_fire_W := false.B
    }
    is(s_COUNT) {
      out_fire_W := false.B
    }
    is(s_WAIT) {
      out_fire_W := true.B
    }
  }

  // Transition state machine
  switch(state) {
    is(s_IDLE) {
      when((io.incIn.fire() && io.incIn.bits.control) ||
        (inc_valid_R && inc_R.control)) {
        sync_count := sync_count + 1.U
        state := s_IDLE

        inc_R := ControlBundle.default
        inc_valid_R := false.B

        enableID := inc_R.taskID

      }.elsewhen((io.incIn.fire() && (~io.incIn.bits.control).toBool) ||
        (inc_valid_R && (~inc_R.control).toBool)) {
        state := s_COUNT

      }.elsewhen((io.decIn.fire() && io.decIn.bits.control) ||
        (dec_valid_R && dec_R.control)) {
        assert(sync_count > 0.U, "Sync counter can not be zero when it's decrementing")
        sync_count := sync_count - 1.U
        state := s_IDLE

        dec_R := ControlBundle.default
        dec_valid_R := false.B

      }.elsewhen(io.decIn.fire() && (~io.decIn.bits.control).toBool ||
        (dec_valid_R && (~dec_R.control).toBool)) {

        //Valid the output
        //out_ready_R := VecInit(Seq.fill(NumOuts)(true.B))
        //out_valid_R := VecInit(Seq.fill(NumOuts)(true.B))
        assert(sync_count === 0.U, "Transition only happens if the counter is equal to zero")
        state := s_WAIT

        dec_R := ControlBundle.default
        dec_valid_R := false.B

      }.otherwise {
        state := s_IDLE
      }
    }

    is(s_COUNT) {
      when((io.decIn.fire() && io.decIn.bits.control) ||
        (dec_valid_R && dec_R.control)) {
        assert(sync_count > 0.U, "Sync counter can not be zero for decrement")
        sync_count := sync_count - 1.U
        state := s_COUNT

        dec_R := ControlBundle.default
        dec_valid_R := false.B
      }

      when(sync_count === 0.U) {
        out_valid_R := VecInit(Seq.fill(NumOuts)(true.B))
        state := s_WAIT

        dec_R := ControlBundle.default
        dec_valid_R := false.B

        inc_R := ControlBundle.default
        inc_valid_R := false.B

      }
    }

    is(s_WAIT) {
      when(out_ready_R.asUInt.andR | out_ready_W.asUInt.andR) {
        out_ready_R := VecInit(Seq.fill(NumOuts)(false.B))
        state := s_IDLE
        printf("[LOG] " + "[" + module_name + "] [TID->%d] " + node_name + ": Output fired @ %d\n", enableID, cycleCount)
      }
    }
  }

}

class SyncTC(NumOuts: Int, NumInc: Int, NumDec: Int, ID: Int)
            (implicit p: Parameters,
             name: sourcecode.Name,
             file: sourcecode.File)
  extends HandShakingCtrlNPS(NumOuts, ID)(p) {
  override lazy val io = IO(new SyncIO(NumOuts, NumInc, NumDec)(p))
  // Printf debugging
  val node_name = name.value
  val module_name = file.value.split("/").tail.last.split("\\.").head.capitalize
  override val printfSigil = "[" + module_name + "] " + node_name + ": " + ID + " "
  val (cycleCount, _) = Counter(true.B, 32 * 1024)


  /*===========================================*
   *            Registers                      *
   *===========================================*/

  // idState machine
  val s_IDLE :: s_COMPUTE :: s_DONE :: Nil = Enum(3)
  val state = RegInit(s_IDLE)
  val syncCount = RegInit(VecInit(Seq.fill(1 << tlen)(0.U(tlen.W))))

  /*==========================================*
   *           Predicate Evaluation           *
   *==========================================*/

  val predicate = IsEnable()
  val start = IsEnableValid()


  /*============================================*
   *            Update Counts                   *
   *============================================*/
  val incArb = Module(new Arbiter(new ControlBundle, NumInc))
  val decArb = Module(new Arbiter(new ControlBundle, NumDec))
  val updateArb = Module(new Arbiter(new ControlBundle, 2))

  incArb.io.in <> io.incIn
  decArb.io.in <> io.decIn

  updateArb.io.in(0) <> incArb.io.out // increments are higher priority
  updateArb.io.in(1) <> decArb.io.out // decrements lower priority

  updateArb.io.out.ready := true.B

  val update = RegNext(init = false.B, next = updateArb.io.out.fire() && updateArb.io.out.bits.control)
  val dec = RegNext(init = 0.U, next = updateArb.io.chosen)
  val updateID = RegNext(init = 0.U, next = updateArb.io.out.bits.taskID)

  when(update) {
    when(dec === 0.U) {
      syncCount(updateID) := syncCount(updateID) + 1.U
    }.otherwise {
      syncCount(updateID) := syncCount(updateID) - 1.U
    }
  }

  /*============================================*
   *            Output State Machine            *
   *============================================*/
  switch(state) {
    is(s_IDLE) {
      when(start) {
        when(predicate) {
          state := s_COMPUTE
        }.otherwise {
          printf("[LOG] " + "[" + module_name + "] " + node_name + ": Not predicated value -> reset\n")
          ValidOut()
          state := s_DONE
        }
      }
    }
    is(s_COMPUTE) {
      when(syncCount(enable_R.taskID) === 0.U) {
        ValidOut()
        state := s_DONE
      }
    }
    is(s_DONE) {
      when(IsOutReady()) {
        Reset()
        when(predicate) {
          printf("[LOG] " + "[" + module_name + "] " + node_name + ": Output fired @ %d\n", cycleCount)
        }
        state := s_IDLE
      }
    }
  }

  /*============================================*
   *            Connect outputs                 *
   *============================================*/
  for (i <- 0 until NumOuts) {
    io.Out(i).bits := enable_R
  }

}

class SyncTC2(NumOuts: Int, NumInc: Int, NumDec: Int, ID: Int)
             (implicit p: Parameters,
              name: sourcecode.Name,
              file: sourcecode.File)
  extends HandShakingCtrlNPS(NumOuts, ID)(p) {
  override lazy val io = IO(new SyncIO(NumOuts, NumInc, NumDec)(p))
  // Printf debugging
  val node_name = name.value
  val module_name = file.value.split("/").tail.last.split("\\.").head.capitalize
  override val printfSigil = "[" + module_name + "] " + node_name + ": " + ID + " "
  val (cycleCount, _) = Counter(true.B, 32 * 1024)

  /*==========================================*
   *          Init the Sync Counters          *
   *==========================================*/
  //  val syncCount = RegInit(VecInit(Seq.fill(1<<tlen)(0.U(tlen.W))))
  val countBits = BigInt(math.min(NumInc, NumDec)).bitLength
  val syncCount = SyncReadMem(1 << tlen, UInt(countBits.W))
  val initCounters = RegInit(true.B)
  val (initCount, initDone) = Counter(true.B, 1 << tlen)

  when(initDone) {
    when(initCounters) {
      printfInfo("[LOG] " + "[" + module_name + "] " + node_name + ":RAM counters initialized @ %d\n", cycleCount)
    }
    initCounters := false.B
  }

  /*============================================*
   *            Update the Counts               *
   *============================================*/

  val incArb = Module(new Arbiter(new ControlBundle, NumInc))
  val decArb = Module(new Arbiter(new ControlBundle, NumDec))
  val updateArb = Module(new Arbiter(new ControlBundle(), 2))
  val doneQueue = Module(new Queue(new ControlBundle(), 128))
  val dec_R = RegInit(0.U)
  val updateArb_R = RegInit(ControlBundle.default)
  val update_R = RegInit(false.B)

  val s_IDLE :: s_WRITE :: s_WAIT :: Nil = Enum(3)
  val state = RegInit(s_IDLE)

  incArb.io.in <> io.incIn
  decArb.io.in <> io.decIn
  updateArb.io.in(0) <> incArb.io.out // increments are higher priority
  updateArb.io.in(1) <> decArb.io.out // decrements lower priority

  when(updateArb.io.out.ready) {
    update_R := updateArb.io.out.valid && updateArb.io.out.bits.control
    dec_R := updateArb.io.chosen
    updateArb_R := updateArb.io.out.bits
  }

  doneQueue.io.enq.valid := false.B
  doneQueue.io.enq.bits := ControlBundle.default

  // Counter RAM
  val updateAddr = WireInit(0.U((1 << tlen).W))
  val updateCount = WireInit(0.U(countBits.W))

  // Mux write address and data
  val currCount = syncCount.read(updateArb_R.taskID)
  when(initCounters) {
    updateAddr := initCount
    updateCount := 0.U
  }.elsewhen(state === s_WRITE) {
    updateAddr := updateArb_R.taskID
    when(dec_R === 0.U) {
      updateCount := currCount + 1.U
    }.otherwise {
      assert(currCount =/= 0.U)
      updateCount := currCount - 1.U
    }
  }
  // Write enable
  when(initCounters || state === s_WRITE) {
    syncCount.write(updateAddr, updateCount)
  }


  updateArb.io.out.ready := false.B
  switch(state) {
    is(s_IDLE) {
      when(initCounters) {
        updateArb.io.out.ready := false.B
        state := s_IDLE
      }.elsewhen(update_R) {
        updateArb.io.out.ready := false.B
        state := s_WRITE
      }.otherwise {
        updateArb.io.out.ready := true.B
        state := s_IDLE
      }
    }
    is(s_WRITE) {
      // Update count
      // If decrementing to zero then last re-attach has arrived.
      when(updateCount === 0.U) {
        when(doneQueue.io.enq.ready) {
          doneQueue.io.enq.valid := true.B
          doneQueue.io.enq.bits := updateArb_R
          updateArb.io.out.ready := true.B
          state := s_IDLE
        }.otherwise {
          updateArb.io.out.ready := false.B
          state := s_WAIT
        }
      }.otherwise {
        updateArb.io.out.ready := true.B
        state := s_IDLE
      }
    }
    is(s_WAIT) {
      when(doneQueue.io.enq.ready) {
        doneQueue.io.enq.valid := true.B
        doneQueue.io.enq.bits := updateArb_R
        updateArb.io.out.ready := true.B
        state := s_IDLE
      }.otherwise {
        updateArb.io.out.ready := true.B
        state := s_WAIT
      }
    }
  }

  /*============================================*
   *            Connect outputs                 *
   *============================================*/


  val outPorts = Module(new ExpandNode(NumOuts = NumOuts, ID = 0)(new ControlBundle))

  outPorts.io.InData <> doneQueue.io.deq //outArb.io.out
  outPorts.io.enable.enq(ControlBundle.active())

  io.Out <> outPorts.io.Out

  io.enable.ready := true.B


}
