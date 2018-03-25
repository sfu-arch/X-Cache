package dataflow

import chisel3._
import chisel3.util._
import chisel3.Module
import config.{CoreParams, Parameters}
import control.BasicBlockNoMaskIO
import interfaces.{ControlBundle, DataBundle}
import node.{HandShakingCtrlNPS, HandShakingCtrlNoMaskIO, HandShakingIONPS}
import utility.UniformPrintfs

class SyncIO(NumOuts: Int, NumInc: Int, NumDec: Int)(implicit p: Parameters)
  extends HandShakingIONPS(NumOuts)(new ControlBundle)(p) {
  val incIn = Flipped(Vec(NumInc, Decoupled(new ControlBundle())))
  val decIn = Flipped(Vec(NumDec, Decoupled(new ControlBundle())))
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

  val enableID = RegInit(0.U(1 << tlen))
  val syncCount = RegInit(0.U(8.W))

  /*==========================================*
   *           Predicate Evaluation           *
   *==========================================*/

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
    io.Out(i).bits.control := true.B
    io.Out(i).bits.taskID := enableID
  }

  switch(state) {
    is(s_IDLE) {
      when(enable_valid_R) {
        when(enable_R.control) {
          state := s_COMPUTE
        }.otherwise {
          Reset()
          printf("[LOG] " + "[" + module_name + "] " + node_name + ": Read value only @ %d, value: %d\n", cycleCount, syncCount)
          state := s_IDLE
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

class SyncNode(NumOuts: Int, ID: Int)
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
  val out_ready_W = WireInit(VecInit(Seq.fill(NumOuts) {
    false.B
  }))

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

      }.elsewhen((io.incIn.fire() && (~io.incIn.bits.control).toBool) ||
        (inc_valid_R && (~inc_R.control).toBool)) {
        state := s_COUNT

        inc_R := ControlBundle.default
        inc_valid_R := false.B

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
        out_valid_R := VecInit(Seq.fill(NumOuts)(true.B))
        //out_ready_R := VecInit(Seq.fill(NumOuts)(true.B))
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

      }.elsewhen((io.decIn.fire() && (~io.decIn.bits.control).toBool) ||
        (dec_valid_R && (~dec_R.control).toBool)) {
        assert(sync_count === 0.U, "Transition only happens if the counter is equal to zero")
        out_valid_R := VecInit(Seq.fill(NumOuts)(true.B))
        state := s_WAIT

        dec_R := ControlBundle.default
        dec_valid_R := false.B
      }.otherwise {
        state := s_COUNT
      }
    }

    is(s_WAIT) {
      when(out_ready_R.asUInt.andR | out_ready_W.asUInt.andR) {
        out_ready_R := VecInit(Seq.fill(NumOuts)(false.B))
        state := s_IDLE
        printf("[LOG] " + "[" + module_name + "] " + node_name + ": Output fired @ %d\n", cycleCount)
      }
    }
  }


}

