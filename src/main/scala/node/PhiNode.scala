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

class PhiNodeIO(NumInputs: Int, NumOuts: Int)
               (implicit p: Parameters)
  extends HandShakingIONPS(NumOuts)(new DataBundle) {

  // Vector input
  val InData = Vec(NumInputs, Flipped(Decoupled(new DataBundle)))

  // Predicate mask comming from the basic block
  val Mask = Flipped(Decoupled(UInt(NumInputs.W)))
}

class PhiNode(NumInputs: Int,
                 NumOuts: Int,
                 ID: Int)
                (implicit p: Parameters)
  extends HandShakingNPS(NumOuts, ID)(new DataBundle)(p) {
  override lazy val io = IO(new PhiNodeIO(NumInputs, NumOuts))
  // Printf debugging
  override val printfSigil = "PHI ID: " + ID + " "

  /*===========================================*
   *            Registers                      *
   *===========================================*/
  // Data Inputs
  val in_data_R = RegInit(DataBundle.default)
  val in_data_valid_R = RegInit(false.B)
  //val in_data_R = RegInit(Vec(Seq.fill(NumInputs)(DataBundle.default)))

  // Mask Input
  val mask_R = RegInit(0.U(NumInputs.W))
  val mask_valid_R = RegInit(false.B)

  val s_idle :: s_LATCH :: s_COMPUTE :: Nil = Enum(3)
  val state = RegInit(s_idle)

  /*==========================================*
   *           Predicate Evaluation           *
   *==========================================*/

  var predicate = in_data_R.predicate & IsEnable()
  var start = mask_valid_R & in_data_valid_R & IsEnableValid()


  /*===============================================*
   *            Latch inputs. Wire up output       *
   *===============================================*/

  //wire up mask
  io.Mask.ready := ~mask_valid_R
  when(io.Mask.fire()) {
    mask_R <> io.Mask.bits
    mask_valid_R := true.B
  }

  //Instantiating a MUX
  val sel = OHToUInt(mask_R)
  val mask_valid = mask_R.asUInt.orR
  val mask_input_W = io.InData(sel)

  in_data_R <> mask_input_W.bits

  when(mask_valid === 0.U){
    in_data_valid_R := false.B
  }

  mask_input_W.ready := ~in_data_valid_R
  when(mask_input_W.fire()) {
    state := s_LATCH
    in_data_R <> mask_input_W.bits

    in_data_valid_R := true.B
  }

  // Wire up Outputs
  for (i <- 0 until NumOuts) {
    io.Out(i).bits <> in_data_R
  }


  /*============================================*
   *            ACTIONS                         *
   *============================================*/


  when(start & predicate & state =/= s_COMPUTE) {
    state := s_COMPUTE
    ValidOut()
  }

  /*==========================================*
   *            Output Handshaking and Reset  *
   *==========================================*/

   when(IsOutReady() & (state === s_COMPUTE)) {
    //printfInfo("Start restarting output \n")
    // Reset data
    mask_R := 0.U
    mask_valid_R := false.B

    in_data_R := DataBundle.default

    //Reset state
    state := s_idle
    //Reset output
    Reset()
  }

  //printfInfo("SEl Value: %x\n", sel)

}
