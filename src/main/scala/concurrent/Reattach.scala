package dataflow

import chisel3._
import chisel3.util._
import chisel3.Module
import config._
import interfaces._
import node._
import utility.UniformPrintfs

class ReattachIO(val NumPredOps: Int)(implicit p: Parameters)
  extends HandShakingIONPS(NumOuts = 1)(new ControlBundle)(p) {
  val predicateIn = Vec(NumPredOps, Flipped(Decoupled(new DataBundle()(p))))
  override def cloneType = new ReattachIO(NumPredOps).asInstanceOf[this.type]
}

class Reattach(val NumPredOps: Int, ID: Int)
              (implicit p: Parameters,
               name: sourcecode.Name,
               file: sourcecode.File)
  extends HandShakingNPS(NumOuts = 1, ID)(new ControlBundle)(p) {
  override lazy val io = IO(new ReattachIO(NumPredOps))
  // Printf debugging
  val node_name = name.value
  val module_name = file.value.split("/").tail.last.split("\\.").head.capitalize
  override val printfSigil = "[" + module_name + "] " + node_name + ": " + ID + " "
  val (cycleCount, _) = Counter(true.B, 32 * 1024)

  /*===========================================*
   *            Registers                      *
   *===========================================*/

  val ctrlPredicate_R = RegInit(VecInit(Seq.fill(NumPredOps){DataBundle.default}))
  val ctrlReady_R = RegInit(VecInit(Seq.fill(NumPredOps){false.B}))
  for (i <- 0 until NumPredOps) {
    when(io.predicateIn(i).fire()) {
      ctrlReady_R(i) := true.B
      ctrlPredicate_R(i) := io.predicateIn(i).bits
    }
    io.predicateIn(i).ready := ~ctrlReady_R(i)
  }

  val s_idle :: s_COMPUTE :: Nil = Enum(2)
  val state = RegInit(s_idle)

  /*==========================================*
   *           Predicate Evaluation           *
   *==========================================*/

  val start = ctrlReady_R.asUInt.andR && IsEnableValid()
  val predicate = ctrlPredicate_R(0).predicate

  io.Out(0).bits.control := ctrlPredicate_R(0).predicate
  io.Out(0).bits.taskID := ctrlPredicate_R(0).taskID | enable_R.taskID

  /*============================================*
   *            ACTIONS (possibly dangerous)    *
   *============================================*/

  when(start & state =/= s_COMPUTE) {
    state := s_COMPUTE
    ValidOut()
  }

  /*==========================================*
   *            Output Handshaking and Reset  *
   *==========================================*/


  when(IsOutReady() & (state === s_COMPUTE)) {
    //printfInfo("Start restarting output \n")
    //Reset state
    state := s_idle
    Reset()
    enable_valid_R := false.B
    for (i <- 0 until NumPredOps) {
      ctrlReady_R(i) := false.B
      ctrlPredicate_R(i) := DataBundle.default
    }

    when(predicate) {
      if(log){
        printf("[LOG] " + "[" + module_name + "] " + node_name + ": Output fired @ %d\n", cycleCount)
      }
    }


  }


}


class ReattachNodeIO(val NumOuts: Int)(implicit p: Parameters)
  extends Bundle {
  val dataIn = Flipped(Decoupled(new DataBundle()))
  val Out = Vec(NumOuts, Decoupled(new ControlBundle()))
}

class ReattachNode(val NumOuts: Int = 1, ID: Int)
                    (implicit val p: Parameters,
                     name: sourcecode.Name,
                     file: sourcecode.File)
  extends Module with CoreParams with UniformPrintfs {
  override lazy val io = IO(new ReattachNodeIO(NumOuts))

  val node_name = name.value
  val module_name = file.value.split("/").tail.last.split("\\.").head.capitalize
  val (cycleCount, _) = Counter(true.B, 32 * 1024)
  override val printfSigil = "[" + module_name + "] " + node_name + ": " + ID + " "

  /*===========================================*
   *            Registers                      *
   *===========================================*/
  // Addr Inputs
  val data_R = RegInit(DataBundle.default)
  val data_valid_R = RegInit(false.B)

  // Output Handshaking
  val out_ready_R = RegInit(VecInit(Seq.fill(NumOuts)(false.B)))
  val out_valid_R = RegInit(VecInit(Seq.fill(NumOuts)(false.B)))

  // Wire
  val out_ready_W = WireInit(VecInit(Seq.fill(NumOuts)(false.B)))


  val s_IDLE :: s_COMPUTE :: Nil = Enum(2)
  val state = RegInit(s_IDLE)


  /*===============================================*
   *            Latch inputs. Wire up output       *
   *===============================================*/

  io.dataIn.ready := ~data_valid_R
  when(io.dataIn.fire()) {
    data_R <> io.dataIn.bits
    data_valid_R := true.B
  }

  // Wire up Outputs
  for (i <- 0 until NumOuts) {
    io.Out(i).bits.control := data_valid_R
    io.Out(i).bits.taskID := data_R.taskID
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


  /*============================================*
   *            STATES                          *
   *============================================*/

  switch(state) {
    is(s_IDLE) {
      when(io.dataIn.fire()) {
        out_valid_R := VecInit(Seq.fill(NumOuts)(true.B))
        state := s_COMPUTE
        printf("[LOG] " + "[" + module_name + "] " + node_name + ": Output fired @ %d\n", cycleCount)
      }

    }
    is(s_COMPUTE) {
      when(out_ready_R.asUInt.andR | out_ready_W.asUInt.andR) {
        // Reset output
        data_R := DataBundle.default
        data_valid_R := false.B
        // Reset state
        state := s_IDLE
        // Reset output
        out_ready_R := VecInit(Seq.fill(NumOuts)(false.B))
      }
    }
  }


}


class ReattachNodeSYNCIO(val NumPredIn: Int)(implicit p: Parameters)
  extends HandShakingIONPS(NumOuts = 1)(new ControlBundle)(p) {
  val dataIn = Flipped(Decoupled(new DataBundle()))
}

class ReattachNodeSYNC(val NumPredIn: Int = 1, ID: Int)
                  (implicit p: Parameters,
                   name: sourcecode.Name,
                   file: sourcecode.File)
  extends HandShakingNPS(NumOuts = 1, ID)(new ControlBundle)(p) {
  override lazy val io = IO(new ReattachNodeSYNCIO(NumOuts))
  // Printf debugging
  val node_name = name.value
  val module_name = file.value.split("/").tail.last.split("\\.").head.capitalize
  val (cycleCount, _) = Counter(true.B, 32 * 1024)
  override val printfSigil = "[" + module_name + "] " + node_name + ": " + ID + " "

  /*===========================================*
   *            Registers                      *
   *===========================================*/
  // Addr Inputs
  val data_R = RegInit(DataBundle.default)
  val data_valid_R = RegInit(false.B)


  val s_IDLE :: s_COMPUTE :: Nil = Enum(2)
  val state = RegInit(s_IDLE)


  /*===============================================*
   *            Latch inputs. Wire up output       *
   *===============================================*/

  io.dataIn.ready := ~data_valid_R
  when(io.dataIn.fire()) {
    data_R <> io.dataIn.bits
    data_valid_R := true.B
  }

  // Wire up Outputs
  for (i <- 0 until NumOuts) {
    io.Out(i).bits.control := enable_R.control
    io.Out(i).bits.taskID := data_R.taskID
  }


  /*============================================*
   *            STATES                          *
   *============================================*/

  switch(state) {
    is(s_IDLE) {
      when(enable_valid_R) {
        when((~enable_R.control).toBool) {
          //Reset()
          ValidOut()
          state := s_COMPUTE
          printf("[LOG] " + "[" + module_name + "] " + node_name + ": Not predicated value -> reset\n")
        }.elsewhen((data_valid_R)) {
          ValidOut()
          state := s_COMPUTE
          printf("[LOG] " + "[" + module_name + "] " + node_name + ": Output fired @ %d\n", cycleCount)
        }
      }

    }
    is(s_COMPUTE) {
      when(IsOutReady()) {
        // Reset output
        data_R := DataBundle.default
        data_valid_R := false.B
        // Reset state
        state := s_IDLE
        // Reset output
        Reset()
      }
    }
  }


}


