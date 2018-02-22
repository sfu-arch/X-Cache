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

  val s_IDLE :: s_MASKLATCH :: s_DATALATCH :: s_COMPUTE :: Nil = Enum(4)
  val state = RegInit(s_IDLE)

  /*==========================================*
   *           Predicate Evaluation           *
   *==========================================*/

  var predicate = in_data_W.predicate & IsEnable()
  var start = IsEnableValid()
//  var start = mask_valid_R & IsEnableValid()


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
    mask_R := io.Mask.bits
    mask_valid_R := true.B
  }

  //Wire up inputs
  for (i <- 0 until NumInputs) {
    io.InData(i).ready := ~in_data_valid_R(i)
    when(io.InData(i).fire()) {
      in_data_R(i) <> io.InData(i).bits
      in_data_valid_R(i) := true.B
    }
  }

  // Wire up Outputs
  for (i <- 0 until NumOuts) {
    io.Out(i).bits <> in_data_W
  }


  /*============================================*
   *            STATE MACHINE                   *
   *============================================*/
  switch(state){
    is(s_IDLE){
      when(io.Mask.fire()){
        state := s_MASKLATCH
      }
    }
    is(s_MASKLATCH){
      when(in_data_valid_R(sel)){
        state := s_DATALATCH
      }
    }
    is(s_DATALATCH){
      when(start){
        state := s_COMPUTE
        ValidOut()
      }
    }
    is(s_COMPUTE){
      when(IsOutReady()){
        mask_R := 0.U
        mask_valid_R := false.B

        in_data_R := VecInit(Seq.fill(NumInputs)(DataBundle.default))
        in_data_valid_R := VecInit(Seq.fill(NumInputs)(false.B))

        //Reset state
        state := s_IDLE
        //Reset output
        Reset()

        //Print output
        when (predicate) {
          printf("[LOG] " + Desc+": Output fired @ %d\n",cycleCount)
        }


      }
    }
  }

  /*============================================*
   *            ACTIONS                         *
   *============================================*/


//  when(start && (state === s_DATALATCH) && (in_data_valid_R(sel) === true.B)) {
//    state := s_COMPUTE
//    ValidOut()
//  }

  /*==========================================*
   *            Output Handshaking and Reset  *
   *==========================================*/

//   when(IsOutReady() && (state === s_COMPUTE) && (m_state === s_DATALATCH)) {
//    // Reset data
//    mask_R := 0.U
//    mask_valid_R := false.B
//
//     in_data_R := VecInit(Seq.fill(NumInputs)(DataBundle.default))
//     in_data_valid_R := VecInit(Seq.fill(NumInputs)(false.B))
//
//    //Reset state
//    state := s_IDLE
//    //Reset output
//    Reset()
//     when (predicate) {printf("[LOG] " + Desc+": Output fired @ %d\n",cycleCount)}
//
//   }

}
