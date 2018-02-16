package dataflow

import chisel3._
import chisel3.util._
import chisel3.Module
import config.{CoreParams, Parameters}
import control.BasicBlockNoMaskIO
import interfaces.{ControlBundle, DataBundle}
import node.{HandShakingCtrlNPS, HandShakingCtrlNoMaskIO, HandShakingIONPS}

class Sync2IO(NumOuts : Int)(implicit p: Parameters)
extends HandShakingIONPS(NumOuts)(new ControlBundle)(p)
{
  val incIn = Flipped(Decoupled(new ControlBundle()))
  val decIn = Flipped(Decoupled(new ControlBundle()))
}

class Sync2(NumOuts : Int, ID: Int)(implicit p: Parameters)
  extends HandShakingCtrlNPS(NumOuts, ID)(p) {
  override lazy val io = IO(new Sync2IO(NumOuts)(p))
  // Printf debugging
  override val printfSigil = "Node (SYNC) ID: " + ID + " "

  /*===========================================*
   *            Registers                      *
   *===========================================*/

  // State machine
  val s_IDLE :: s_COMPUTE :: Nil = Enum(2)
  val state = RegInit(s_IDLE)
  val enableID = RegInit(0.U(1<<tlen))
  val syncCount = RegInit(0.U(8.W))

  /*==========================================*
   *           Predicate Evaluation           *
   *==========================================*/

  val predicate = IsEnable()
  val start     = IsEnableValid()

  io.enable.ready := (state === s_IDLE)
  when (io.enable.fire()){
    enableID := io.enable.bits.taskID
  }

  /*============================================*
   *            ACTIONS (possibly dangerous)    *
   *============================================*/

  io.incIn.ready := true.B
  io.decIn.ready := true.B
  val inc = io.incIn.fire() && io.incIn.bits.control
  val dec = io.decIn.fire() && io.decIn.bits.control
  when(inc && !dec) {
    syncCount := syncCount + 1.U
  }.elsewhen(!inc && dec) {
    syncCount := syncCount - 1.U
  }

  for (i <- 0 until NumOuts) {
//    io.Out(i).valid := false.B
    io.Out(i).bits.control := predicate
    io.Out(i).bits.taskID := enableID
  }
  switch (state) {
    is (s_IDLE) {
      when(start && predicate) {
        state := s_COMPUTE
        printfInfo("Input fired")
        ValidOut()
      }
    }
    is (s_COMPUTE) {
      when(IsOutReady() && (syncCount === 0.U)) {
        Reset()
        when (predicate) {printfInfo("Output fired")}
        state := s_IDLE
      }
    }
  }

  io.Out(0).bits.control := predicate

}

