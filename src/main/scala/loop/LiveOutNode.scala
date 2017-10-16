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

}

class LiveOutNode(NumOuts: Int, ID: Int)
                 (implicit p: Parameters)
  extends HandShakingNPS(NumOuts, ID)(new DataBundle())(p) {
  override lazy val io = IO(new LiveOutNodeIO(NumOuts))
  // Printf debugging

  // Printf debugging
  override val printfSigil = "LiveOut ID: " + ID + " "

  /*===========================================*
   *            Registers                      *
   *===========================================*/
  // Left Input
  val indata_R = RegInit(DataBundle.default)
  val indata_valid_R = RegInit(false.B)

  val s_idle :: s_LATCH :: s_VALIDOUT :: Nil = Enum(3)
  val state = RegInit(s_idle)

  /*==========================================*
   *           Predicate Evaluation           *
   *==========================================*/

  val predicate = indata_R.predicate & IsEnable()
  val start = indata_valid_R & enable_valid_R

  /*===============================================*
   *            Latch inputs. Wire up output       *
   *===============================================*/


  val pred_R = RegInit(init = false.B)

  io.InData.ready := ~indata_valid_R

  when(io.InData.fire()) {
    //printfInfo("Latch left data\n")
    state := s_LATCH
    indata_R <> io.InData.bits
    indata_valid_R := true.B
  }

  // Wire up Outputs
  for (i <- 0 until NumOuts) {
    io.Out(i).bits <> indata_R
  }

  /*============================================*
   *            ACTIONS (possibly dangerous)    *
   *============================================*/

  when(start & ~enable_R){
    //When the Loop hasn't done, we only restart the states
    //and we don't make the output valid
    
    // Reset data
    indata_R := DataBundle.default
    indata_valid_R := false.B

    //Reset state
    state := s_idle
    //Reset output
    Reset()
  } .elsewhen(start & enable_R & state =/= s_VALIDOUT) {
    //If the input is valid, start is activated and 
    //enalbe signal is true then we make the output valid
    state := s_VALIDOUT
    ValidOut()
  }

  /*==========================================*
   *            Output Handshaking and Reset  *
   *==========================================*/

  when(IsOutReady() & (state === s_VALIDOUT)) {
    // Reset data
    indata_R := DataBundle.default
    indata_valid_R := false.B

    //Reset state
    state := s_idle
    //Reset output
    Reset()
  }

  //printfInfo(" State: %x\n", state)

}
