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
  val Finish = Flipped(Decoupled(new ControlBundle()))

}

class LiveInNode(NumOuts: Int, ID: Int)
                (implicit p: Parameters, name: sourcecode.Name)
  extends HandShakingNPS(NumOuts, ID)(new DataBundle())(p) {
  override lazy val io = IO(new LiveInNodeIO(NumOuts))

  var NodeName = name.value

  // Printf debugging
  override val printfSigil = NodeName + ID + " "
  val (cycleCount,_) = Counter(true.B,32*1024)

  /*===========================================*
   *            Registers                      *
   *===========================================*/
  // In data Input
  val indata_R = RegInit(DataBundle.default)
  val indata_valid_R = RegInit(false.B)

  // In finish control signal
  val finish_R = RegInit(ControlBundle.default)
  val finish_valid_R = RegInit(false.B)

  val s_IDLE :: s_LATCH :: s_VALIDOUT :: s_CLEAN :: s_RESET :: Nil = Enum(5)

  val state = RegInit(s_IDLE)

  /*===============================================*
   *            LATCHING INPUTS                    *
   *===============================================*/

  io.InData.ready := ~indata_valid_R
  when(io.InData.fire()) {
    indata_R <> io.InData.bits
    indata_valid_R := true.B
  }

  io.Finish.ready := ~finish_valid_R
  when(io.Finish.fire()) {
    finish_R <> io.Finish.bits
    finish_valid_R := true.B
  }

  /*===============================================*
   *            DEFINING STATES                    *
   *===============================================*/

  switch(state){
    is(s_IDLE){
      when(io.InData.fire()){
        state := s_LATCH
        printf("[LOG] " + NodeName + ": Latch fired @ %d, Value:%d\n",cycleCount, io.InData.bits.data.asUInt())
      }
    }
    is(s_LATCH){
      when(enable_R && enable_valid_R){
        state := s_VALIDOUT
        ValidOut()
      }.elsewhen(finish_R.control && finish_valid_R){
        state := s_RESET
      }
    }
    is(s_VALIDOUT){
      when(IsOutReady()){
        state := s_LATCH
        Reset()
      }
    }
    is(s_RESET){
      printf("[LOG] " + NodeName + ": Latch reset @ %d\n",cycleCount)
      state := s_IDLE
      indata_R <> DataBundle.default
      indata_valid_R := false.B

      finish_R <> ControlBundle.default
      finish_valid_R := false.B
    }
  }
//  switch(state){
//    is(s_IDLE){
//      when(io.InData.fire()){
//        state := s_LATCH
//        ValidOut()
//        printf("[LOG] " + NodeName + ": Latch fired @ %d, Value:%d\n",cycleCount, io.InData.bits.data.asUInt())
//      }
//    }
//    is(s_LATCH){
//      when(enable_valid_R && enable_R){
//        state := s_RESET
//      }.elsewhen(IsOutReady()){
//        state := s_CLEAN
//        Reset()
//      }
//    }
//    is(s_CLEAN){
//      out_ready_R := VecInit(Seq.fill(NumOuts)(false.B))
//      ValidOut()
//      state := s_LATCH
//    }
//    is(s_RESET){
//      printf("[LOG] " + NodeName + ": Latch reset @ %d\n",cycleCount)
//      state := s_IDLE
//      indata_R <> DataBundle.default
//      indata_valid_R := false.B
//    }
//  }


  /*===============================================*
   *            CONNECTING OUTPUTS                 *
   *===============================================*/

  for (i <- 0 until NumOuts) {
    io.Out(i).bits <> indata_R
  }
}
