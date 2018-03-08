package dataflow

import chisel3._
import chisel3.util._
import chisel3.Module
import config.{CoreParams, Parameters}
import interfaces.{ControlBundle, DataBundle}
import node.{HandShakingCtrlNPS, HandShakingIONPS}

class SyncIO()(implicit p: Parameters)
  extends HandShakingIONPS(NumOuts = 1)(new ControlBundle)(p)
{
  val syncStatus = Input(Vec(1<<tlen,Bool()))
}

class Sync(ID: Int)(implicit p: Parameters)
  extends HandShakingCtrlNPS(NumOuts = 1, ID)(p) {
  override lazy val io = IO(new SyncIO()(p))
  // Printf debugging
  override val printfSigil = "Node (SYNC) ID: " + ID + " "

  /*===========================================*
   *            Registers                      *
   *===========================================*/

  // State machine
  val s_IDLE :: s_COMPUTE :: Nil = Enum(2)
  val state = RegInit(s_IDLE)
  val enableID = RegInit(0.U(1<<tlen))

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


  io.Out(0).valid := false.B
  switch (state) {
    is (s_IDLE) {
      when(start && predicate) {
        state := s_COMPUTE
        printfInfo("Input fired")
      }
    }
    is (s_COMPUTE) {
      when(io.Out(0).ready && io.syncStatus(enableID)) {
        state := s_IDLE
        io.Out(0).valid := true.B
        io.Out(0).bits.control := true.B
        io.Out(0).bits.taskID  := enableID
        Reset()
        printfInfo("Output fired")
      }
    }
  }

  io.Out(0).bits.control := predicate

}

