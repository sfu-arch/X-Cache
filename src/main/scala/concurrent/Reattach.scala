package dataflow

import chisel3._
import chisel3.util._
import chisel3.Module
import config._
import interfaces._
import node._

class ReattachIO(val NumPredIn: Int)(implicit p: Parameters)
  extends HandShakingIONPS(NumOuts = 1)(new ControlBundle)(p) {
  val predicateIn = Vec(NumPredIn, Flipped(Decoupled(new DataBundle()(p))))
}

class Reattach(val NumPredIn: Int, ID: Int)
              (implicit p: Parameters,
               name: sourcecode.Name,
               file: sourcecode.File)
  extends HandShakingNPS(NumOuts = 1, ID)(new ControlBundle)(p) {
  override lazy val io = IO(new ReattachIO(NumPredIn))
  // Printf debugging
  val node_name = name.value
  val module_name = file.value.split("/").tail.last.split("\\.").head.capitalize
  override val printfSigil = "[" + module_name + "] " + node_name + ": " + ID + " "
  val (cycleCount, _) = Counter(true.B, 32 * 1024)

  /*===========================================*
   *            Registers                      *
   *===========================================*/

  val ctrlPredicate_R = Reg(Vec(NumPredIn, Bool()))
  val ctrlReady_R = Reg(Vec(NumPredIn, Bool()))
  val ctrlTaskID_R = RegInit(0.U(tlen.W))
  for (i <- 0 until NumPredIn) {
    when(io.predicateIn(i).fire()) {
      ctrlReady_R(i) := true.B
      ctrlPredicate_R(i) := io.predicateIn(i).bits.predicate
    }
    io.predicateIn(i).ready := ~ctrlReady_R(i)
  }

  val s_idle :: s_COMPUTE :: Nil = Enum(2)
  val state = RegInit(s_idle)

  /*==========================================*
   *           Predicate Evaluation           *
   *==========================================*/

  val start = ctrlReady_R.asUInt.andR && IsEnableValid()
  val predicate = ctrlPredicate_R.asUInt.andR && IsEnable()

  io.Out(0).bits.control := predicate

  /*===============================================*
   *            Latch inputs. Wire up output       *
   *===============================================*/


  // Wire up Outputs
  when(io.predicateIn(0).fire()) {
    ctrlTaskID_R := io.predicateIn(0).bits.taskID
  }
  io.Out(0).bits.taskID := ctrlTaskID_R

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
    for (i <- 0 until NumPredIn) {
      ctrlReady_R(i) := false.B
      ctrlPredicate_R(i) := false.B
    }

    when(predicate) {
      printf("[LOG] " + "[" + module_name + "] " + node_name + ": Output fired @ %d\n", cycleCount)
    }


  }


}

class ReattachNodeIO(val NumPredIn: Int)(implicit p: Parameters)
  extends HandShakingIONPS(NumOuts = 1)(new ControlBundle)(p) {
  val dataIn = Flipped(Decoupled(new DataBundle()))
}

class ReattachNode(val NumPredIn: Int, ID: Int)
                  (implicit p: Parameters,
                   name: sourcecode.Name,
                   file: sourcecode.File)
  extends HandShakingNPS(NumOuts = 1, ID)(new ControlBundle)(p) {
  override lazy val io = IO(new ReattachNodeIO(NumOuts))
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
    io.Out(i).bits.control := enable_R
    io.Out(i).bits.taskID := data_R.taskID
  }


  /*============================================*
   *            STATES                          *
   *============================================*/

  switch(state) {
    is(s_IDLE) {
      when(enable_valid_R) {
        when((~enable_R).toBool) {
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


