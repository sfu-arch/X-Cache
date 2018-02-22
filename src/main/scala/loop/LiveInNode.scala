package node

import chisel3._
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import chisel3.Module
import chisel3.testers._
import chisel3.util._
import org.scalatest.{Matchers, FlatSpec}

import config._
import interfaces._
import muxes._
import util._

class LiveInNodeIO(NumOuts: Int)
                  (implicit p: Parameters)
  extends HandShakingIONPS(NumOuts)(new DataBundle) {

  //Input data for live in
  val InData = Flipped(Decoupled(new DataBundle()))

  //Finish signal
//  val Finish = Flipped(Decoupled(new ControlBundle()))

}

class LiveInNode(NumOuts: Int, ID: Int)
                (implicit p: Parameters)
  extends HandShakingNPS(NumOuts, ID)(new DataBundle())(p) {
  override lazy val io = IO(new LiveInNodeIO(NumOuts))
  // Printf debugging
  override val printfSigil = "LiveIn ID: " + ID + " "

  /*===========================================*
   *            Registers                      *
   *===========================================*/
  // In data Input
  val indata_R = RegInit(DataBundle.default)
  val indata_valid_R = RegInit(false.B)

  // Finish data
//  val finish_R = RegInit(ControlBundle.default)
//  val finish_valid_R = RegInit(false.B)

  val s_IDLE :: s_LATCH :: s_VALIDOUT :: s_RESET :: Nil = Enum(4)

  val state = RegInit(s_IDLE)

  /*===============================================*
   *            LATCHING INPUTS                    *
   *===============================================*/

  io.InData.ready := ~indata_valid_R
  when(io.InData.fire()) {
    indata_R <> io.InData.bits
    indata_valid_R := true.B
  }

//  io.Finish.ready := ~finish_valid_R
//  when(io.Finish.fire()) {
//    finish_R <> io.Finish.bits
//    finish_valid_R := true.B
//  }

  /*===============================================*
   *            DEFINING STATES                    *
   *===============================================*/

  switch(state){
    is(s_IDLE){
      when(io.InData.fire()){
        state := s_LATCH
      }
    }
    is(s_LATCH){
      when(enable_valid_R && enable_R){
        state := s_RESET
      }.otherwise{
        ValidOut()
        state := s_VALIDOUT
      }
    }
    is(s_VALIDOUT){
      when(IsOutReady()){
        state := s_LATCH
        out_ready_R := VecInit(Seq.fill(NumOuts)(false.B))
      }
    }
    is(s_RESET){
      state := s_IDLE
      Reset()
      indata_R <> DataBundle.default
      indata_valid_R := false.B
    }
  }

//  switch(state) {
//
//    is(s_idle) {
//
//      //When the input is fire
//      when(io.InData.fire()) {
//
//        //If the predication has been latched
//        when(enable_valid_R) {
//          state := s_VALIDOUT
//          ValidOut()
//        }.otherwise {
//          state := s_LATCH
//        }
//      }
//    }
//    is(s_LATCH) {
//
//      //State transition
//      when(enable_valid_R) {
//        state := s_VALIDOUT
//        ValidOut()
//      }
//    }
//    is(s_VALIDOUT) {
//      when(IsOutReady() & finish_valid_R) {
//        when(finish_R.control) {
//          //Restart the states
//          state := s_idle
//          Reset()
//
//          indata_R := DataBundle.default
//          finish_R := ControlBundle.default
//
//          indata_valid_R := false.B
//          finish_valid_R := false.B
//        }.otherwise {
//          // Change the state to s_LATCH
//          state := s_LATCH
//
//          finish_R := ControlBundle.default
//          finish_valid_R := false.B
//
//          Reset()
//        }
//      }
//    }
//  }


  /*===============================================*
   *            CONNECTING OUTPUTS                 *
   *===============================================*/

  for (i <- 0 until NumOuts) {
    io.Out(i).bits <> indata_R
  }
}
