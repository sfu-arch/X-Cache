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

  //Input data
  val InData = Flipped(Decoupled(new DataBundle()))

  //Finish signal
  val Finish = Flipped(Decoupled(new ControlBundle()))

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
  val indata_R      = RegInit(DataBundle.default)
  val indata_valid_R = RegInit(false.B)

  // Finish data
  val finish_R       = RegInit(ControlBundle.default)
  val finish_valid_R = RegInit(false.B)

  val s_idle :: s_LATCH :: s_VALIDOUT :: Nil = Enum(3)

  val state   = RegInit(s_idle)
  val f_state = RegInit(s_idle)

  /*==========================================*
   *           Predicate Evaluation           *
   *==========================================*/

  val predicate = indata_R.predicate & IsEnable()
  val start = indata_valid_R & IsEnableValid()

  val finish_start = finish_valid_R & IsEnableValid()

  /*===============================================*
   *            Latch inputs. Wire up output       *
   *===============================================*/

  io.InData.ready := ~indata_valid_R
  when(io.InData.fire()) {
    state := s_LATCH
    indata_R <> io.InData.bits
    indata_valid_R := true.B
  }

  io.Finish.ready := ~finish_valid_R
  when(io.Finish.fire()) {
    f_state := s_LATCH
    finish_R <> io.InData.bits
    finish_valid_R := true.B
  }


  // Wire up Outputs
  for (i <- 0 until NumOuts) {
    io.Out(i).bits <> indata_R
  }

  /*============================================*
   *            ACTIONS (possibly dangerous)    *
   *============================================*/

  when(start & state =/= s_VALIDOUT) {
    state := s_VALIDOUT 
    ValidOut()
  }

  when(finish_start & f_state =/= s_VALIDOUT){
    f_state := s_VALIDOUT
  }

  /*==========================================*
   *            Output Handshaking and Reset  *
   *==========================================*/

  when(IsOutReady() & (state === s_VALIDOUT) & 
    (f_state =/= s_VALIDOUT) & ~finish_R.control){
    ValidOut()


  }.elsewhen(IsOutReady() & (state === s_VALIDOUT) & 
    (f_state === s_VALIDOUT) & finish_R.control){

    // Reset data
    indata_R := DataBundle.default

    indata_valid_R := false.B

    //Reset state
    state   := s_idle
    f_state := s_idle
    //Reset output
    Reset()
  }

  //printfInfo(" Main State: %x", state)
  //printfInfo(" Finish Signal: %x", finish_start)
  //printfInfo(" Finish State: %x\n", f_state)


}
