package dataflow

import chisel3._
import chisel3.util._
import chisel3.Module
import config._
import interfaces._
import node._

class ReattachIO(val NumCtrlIn: Int, val NumDataIn: Int)(implicit p: Parameters)
  extends HandShakingIONPS(NumOuts = 1)(new ControlBundle)(p) {
  val InCtrl  = Vec(NumCtrlIn, Flipped(Decoupled(new ControlBundle()(p))))
  val InData  = Vec(NumDataIn, Flipped(Decoupled(new DataBundle()(p))))
}

class Reattach(val NumCtrlIn: Int, val NumDataIn: Int, ID: Int)(implicit p: Parameters)
  extends HandShakingNPS(NumOuts=1, ID)(new ControlBundle)(p) {
  override lazy val io = IO(new ReattachIO(NumCtrlIn, NumDataIn))
  // Printf debugging
  override val printfSigil = "Node (REATTACH) ID: " + ID + " "
  assert(NumCtrlIn != 0 || NumDataIn !=0, "Either NumCtrlIn or NumDataIn must be nonzero.")

  /*===========================================*
   *            Registers                      *
   *===========================================*/

  val ctrlPredicate_R = Reg(Vec(NumCtrlIn,Bool()))
  val ctrlReady_R = Reg(Vec(NumCtrlIn,Bool()))
  val ctrlTaskID_R = RegInit(0.U(tlen.W))
  for (i <- 0 until NumCtrlIn) {
    when(io.InCtrl(i).fire()) {
      ctrlReady_R(i) := true.B
      ctrlPredicate_R(i) := io.InCtrl(i).bits.control
    }
    io.InCtrl(i).ready := ~ctrlReady_R(i)
  }

  val dataPredicate_R = Reg(Vec(NumDataIn,Bool()))
  val dataReady_R = Reg(Vec(NumDataIn,Bool()))
  val dataTaskID_R = RegInit(0.U(tlen.W))
  for (i <- 0 until NumDataIn) {
    when(io.InData(i).fire()) {
      dataReady_R(i) := true.B
      dataPredicate_R(i) := io.InData(i).bits.predicate
    }
    io.InData(i).ready := ~dataReady_R(i)
  }


  val s_idle :: s_COMPUTE :: Nil = Enum(2)
  val state = RegInit(s_idle)

  /*==========================================*
   *           Predicate Evaluation           *
   *==========================================*/

  val start = Wire(Bool())
  if (NumCtrlIn == 0) {
    start := dataReady_R.asUInt.andR && IsEnableValid()
    io.Out(0).bits.control := dataPredicate_R.asUInt.andR && IsEnable()
  } else if (NumDataIn == 0) {
    start := ctrlReady_R.asUInt.andR && IsEnableValid()
    io.Out(0).bits.control := ctrlPredicate_R.asUInt.andR && IsEnable()
  } else {
    start := ctrlReady_R.asUInt.andR && dataReady_R.asUInt.andR && IsEnableValid()
    io.Out(0).bits.control := ctrlPredicate_R.asUInt.andR && dataPredicate_R.asUInt.andR && IsEnable()
  }

  /*===============================================*
   *            Latch inputs. Wire up output       *
   *===============================================*/


  // Wire up Outputs
  if (NumCtrlIn != 0) {
    when (io.InCtrl(0).fire()) {
      ctrlTaskID_R := io.InCtrl(0).bits.taskID
    }
    io.Out(0).bits.taskID := ctrlTaskID_R
  } else {
    when (io.InData(0).fire()) {
      dataTaskID_R := io.InData(0).bits.taskID
    }
    io.Out(0).bits.taskID := dataTaskID_R
  }

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
    for (i <- 0 until NumCtrlIn) {
      ctrlReady_R(i) := false.B
      ctrlPredicate_R(i) := false.B
    }
    for (i <- 0 until NumDataIn) {
      dataReady_R(i) := false.B
      dataPredicate_R(i) := false.B
    }

    printfInfo("Output fired\n")

  }


}

