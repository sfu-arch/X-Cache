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

class LiveOutNodeIO(NumOuts: Int)
                   (implicit p: Parameters)
  extends HandShakingIONPS(NumOuts)(new DataBundle) {

  // Inputdata for Live out element
  val InData = Flipped(Decoupled(new DataBundle()))

  //Finish signal
  val Finish = Flipped(Decoupled(new ControlBundle()))

}

class LiveOutNode(NumOuts: Int, ID: Int)
                 (implicit p: Parameters, name: sourcecode.Name)
  extends HandShakingNPS(NumOuts, ID)(new DataBundle())(p) {
  override lazy val io = IO(new LiveOutNodeIO(NumOuts))

  val node_name = name.value

  // Printf debugging
  override val printfSigil = node_name + ID + " "
  val (cycleCount,_) = Counter(true.B,32*1024)

  /*===========================================*
   *            Registers                      *
   *===========================================*/
  // In data Input
  val indata_R = RegInit(DataBundle.default)
  val indata_valid_R = RegInit(false.B)

  val finish_R = RegInit(ControlBundle.default)
  val finish_valid_R = RegInit(false.B)

  val s_IDLE :: s_LATCH :: s_VALIDOUT :: Nil = Enum(3)
  val state = RegInit(s_IDLE)

  /*===============================================*
   *            LATCHING INPUTS                    *
   *===============================================*/

  io.InData.ready := ~indata_valid_R
  when(io.InData.fire()){
    //Latch the data
    indata_R <> io.InData.bits
    indata_valid_R := true.B
  }

  io.Finish.ready := ~finish_valid_R
  when(io.Finish.fire()){
    finish_R <> io.Finish.bits
    finish_valid_R := true.B
  }

  /*===============================================*
   *            DEFINING STATES                    *
   *===============================================*/

  switch(state) {

    is(s_IDLE) {
      //When the input is fired
      when(io.InData.fire()) {
        state := s_LATCH
      }
    }
    is(s_LATCH){
      when(enable_R && enable_valid_R){
        // In this case we have to invalidate the input
        state := s_IDLE

        indata_R := DataBundle.default
        indata_valid_R := false.B

        finish_R := ControlBundle.default
        finish_valid_R := false.B

        Reset()
      }.elsewhen(finish_R.control && finish_valid_R){
        ValidOut()
        state := s_VALIDOUT
      }
    }

    is(s_VALIDOUT){
      when(IsOutReady()){
        state := s_IDLE

        indata_R := DataBundle.default
        indata_valid_R := false.B

        finish_R := ControlBundle.default
        finish_valid_R := false.B

        Reset()
      }
    }

  }

  /*==========================================*
   *             WIRING OUTPUT                *
   *==========================================*/

  // Wire up Outputs
  for (i <- 0 until NumOuts) {
    io.Out(i).bits <> indata_R
  }

}
