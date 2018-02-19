package dataflow

import chisel3._
import chisel3.util._
import chisel3.Module
import config._
import interfaces._
import node._

class ReattachIO(val NumPredIn: Int)(implicit p: Parameters)
  extends HandShakingIONPS(NumOuts = 1)(new ControlBundle)(p) {
  val predicateIn  = Vec(NumPredIn, Flipped(Decoupled(new DataBundle()(p))))
}

class Reattach(val NumPredIn: Int, ID: Int)(implicit p: Parameters)
  extends HandShakingNPS(NumOuts=1, ID)(new ControlBundle)(p) {
  override lazy val io = IO(new ReattachIO(NumPredIn))
  // Printf debugging
  override val printfSigil = "Node (REAT) ID: " + ID + " "

  /*===========================================*
   *            Registers                      *
   *===========================================*/

  val ctrlPredicate_R = Reg(Vec(NumPredIn,Bool()))
  val ctrlReady_R = Reg(Vec(NumPredIn,Bool()))
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
  when (io.predicateIn(0).fire()) {
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


  when(IsOutReady() & (state === s_COMPUTE)){
    //printfInfo("Start restarting output \n")
    //Reset state
    state := s_idle
    Reset()
    enable_valid_R := false.B
    for (i <- 0 until NumPredIn) {
      ctrlReady_R(i) := false.B
      ctrlPredicate_R(i) := false.B
    }

    when (predicate) {printfInfo(s"Output fired")}

  }


}

