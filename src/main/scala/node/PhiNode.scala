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
  val in_data_R = RegInit(Vec(Seq.fill(NumInputs)(DataBundle.default)))
  val in_data_valid_R = RegInit(Vec(Seq.fill(NumInputs)(false.B)))

  val in_data_W = WireInit(DataBundle.default)

  // Mask Input
  val mask_R = RegInit(0.U(NumInputs.W))
  val mask_valid_R = RegInit(false.B)


  // Output register
  //val data_R = RegInit(0.U(xlen.W))

  val s_idle :: s_LATCH :: s_COMPUTE :: Nil = Enum(3)
  val state = RegInit(s_idle)

  /*==========================================*
   *           Predicate Evaluation           *
   *==========================================*/

  //val acc_predicate = Vec(Seq.fill(NumInputs)(false.B))
  //for(i <- 0 until NumInputs){
    //acc_predicate(i) := in_data_R(i).predicate
  //}

  //val acc_start = Vec(Seq.fill(NumInputs)(false.B))
  //for(i <- 0 until NumInputs){
    //acc_start(i) := in_data_R(i).valid
  //}

  var predicate = in_data_W.predicate & IsEnable()
  var start = mask_valid_R & IsEnableValid()


  /*===============================================*
   *            Latch inputs. Wire up output       *
   *===============================================*/

  // Predicate register
  //val valid_R = RegInit(false.B)

  //printfInfo("start: %x\n", start)

  //wire up mask
  io.Mask.ready := ~mask_valid_R
  when(io.Mask.fire()) {
    state := s_LATCH
    mask_R := io.Mask.bits
    mask_valid_R := true.B
  }


  //Wire up inputs
  for (i <- 0 until NumInputs) {
    io.InData(i).ready := ~in_data_valid_R(i)
    when(io.InData(i).fire()) {
      state := s_LATCH
      in_data_R(i).data := io.InData(i).bits.data
      in_data_R(i).valid := true.B
      in_data_R(i).predicate := io.InData(i).bits.predicate
    }

  }

  // Wire up Outputs
  for (i <- 0 until NumOuts) {
    io.Out(i).bits <> in_data_W
  }


  /*============================================*
   *            ACTIONS                         *
   *============================================*/

  //Instantiating a MUX
  val sel = OHToUInt(mask_R)

  in_data_W := in_data_R(sel)

  // If the mask value is eqaul to zero we don't proceed
  val mask_valid = mask_R.asUInt.orR

  when(start & mask_valid & state =/= s_COMPUTE) {
    state := s_COMPUTE
    ValidOut()
  }

  /*==========================================*
   *            Output Handshaking and Reset  *
   *==========================================*/

   when(IsOutReady() & (state === s_COMPUTE)) {
    // Reset data
    mask_R := 0.U
    mask_valid_R := false.B

    for (i <- 0 until NumInputs) {
      in_data_R(i) := DataBundle.default
    }
    //Reset state
    state := s_idle
    //Reset output
    Reset()
  }

}
