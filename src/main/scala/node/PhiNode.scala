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
                 ID: Int, Desc : String = "PhiNode")
                (implicit p: Parameters)
  extends HandShakingNPS(NumOuts, ID)(new DataBundle)(p) {
  override lazy val io = IO(new PhiNodeIO(NumInputs, NumOuts))
  // Printf debugging
  override val printfSigil = "PHI ID: " + ID + " "
  val (cycleCount,_) = Counter(true.B,32*1024)

  /*===========================================*
   *            Registers                      *
   *===========================================*/
  // Data Inputs
  val in_data_R       = RegInit(VecInit(Seq.fill(NumInputs)(DataBundle.default)))
  val in_data_valid_R = RegInit(VecInit(Seq.fill(NumInputs)(false.B)))

  val in_data_W = WireInit(DataBundle.default)

  // Mask Input
  val mask_R = RegInit(0.U(NumInputs.W))
  val mask_valid_R = RegInit(false.B)


  // Output register
  //val data_R = RegInit(0.U(xlen.W))

  val s_idle :: s_MASKLATCH :: s_DATALATCH :: s_COMPUTE :: Nil = Enum(4)
  val state = RegInit(s_idle)
  val m_state = RegInit(s_idle)

  /*==========================================*
   *           Predicate Evaluation           *
   *==========================================*/

  var predicate = in_data_W.predicate & IsEnable()
  var start = mask_valid_R & IsEnableValid()


  /*===============================================*
   *            Latch inputs. Wire up output       *
   *===============================================*/

  //Instantiating a MUX
  val sel = OHToUInt(mask_R)

  in_data_W := in_data_R(sel)

  // If the mask value is eqaul to zero we don't proceed
  val mask_valid = mask_R.asUInt.orR

  //wire up mask
  io.Mask.ready := ~mask_valid_R
  when(io.Mask.fire()) {
    m_state := s_MASKLATCH
    mask_R := io.Mask.bits
    mask_valid_R := true.B
  }

  //Wire up inputs
  for (i <- 0 until NumInputs) {

    io.InData(i).ready := ~in_data_valid_R(i)
    when(io.InData(i).fire()) {
      state := s_DATALATCH
      in_data_R(i) <> io.InData(i).bits
      in_data_valid_R(i) := true.B
    }

  }

  // Wire up Outputs
  for (i <- 0 until NumOuts) {
    io.Out(i).bits <> in_data_W
  }


  /*============================================*
   *            ACTIONS                         *
   *============================================*/


  when(start & (state === s_DATALATCH) & (in_data_valid_R(sel) === true.B)) {
    state := s_COMPUTE
    ValidOut()
  }

  /*==========================================*
   *            Output Handshaking and Reset  *
   *==========================================*/

   when(IsOutReady() & (state === s_COMPUTE) & (m_state === s_MASKLATCH)) {
    // Reset data
    mask_R := 0.U
    mask_valid_R := false.B

     in_data_R := VecInit(Seq.fill(NumInputs)(DataBundle.default))
     in_data_valid_R := VecInit(Seq.fill(NumInputs)(false.B))

    //Reset state
    state := s_idle
    m_state := s_idle
    //Reset output
    Reset()
     when (predicate) {printf("[LOG] " + Desc+": Output fired @ %d\n",cycleCount)}

   }

}
