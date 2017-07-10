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

  val InData = Vec(NumInputs, Flipped(Decoupled(new DataBundle)))

  val Mask = Flipped(Decoupled(UInt(NumInputs.W)))
}

class PhiNode(NumInputs: Int,
                 NumOuts: Int,
                 ID: Int)
                (implicit p: Parameters)
  extends HandShakingNPS(NumOuts, ID)(new DataBundle)(p) {
  override lazy val io = IO(new PhiNodeIO(NumInputs, NumOuts))
  // Printf debugging
  override val printfSigil = "Node(PHI) ID: " + ID + " "

  /*===========================================*
   *            Registers                      *
   *===========================================*/
  // Data Inputs
  val in_data_R = RegInit(Vec(Seq.fill(NumInputs)(DataBundle.default)))

  // Mask Input
  val mask_R = RegInit(0.U(NumInputs.W))
  val mask_valid_R = RegInit(false.B)


  // Output register
  val data_R = RegInit(0.U(xlen.W))

  val s_idle :: s_LATCH :: s_COMPUTE :: Nil = Enum(3)
  val state = RegInit(s_idle)

  /*==========================================*
   *           Predicate Evaluation           *
   *==========================================*/

  val acc_predicate = Vec(Seq.fill(NumInputs)(false.B))
  for(i <- 0 until NumInputs){
    acc_predicate(i) := in_data_R(i).predicate
  }

  val acc_start = Vec(Seq.fill(NumInputs)(false.B))
  for(i <- 0 until NumInputs){
    acc_start(i) := in_data_R(i).valid
  }

  var predicate = acc_predicate.asUInt.andR & IsEnable()
  var start = mask_valid_R & acc_start.asUInt.andR & IsEnableValid()

  //  val predicate = left_R.predicate & right_R.predicate & IsEnable()
  //  val start = left_R.valid & right_R.valid & IsEnableValid()

  /*===============================================*
   *            Latch inputs. Wire up output       *
   *===============================================*/

  // Predicate register
  val valid_R = RegInit(false.B)

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
    io.InData(i).ready := ~in_data_R(i).valid
    when(io.InData(i).fire()) {
      //printfInfo("Latch left data\n")
      state := s_LATCH
      in_data_R(i).data := io.InData(i).bits.data
      in_data_R(i).valid := true.B
      in_data_R(i).predicate := io.InData(i).bits.predicate
    }

  }

  // Wire up Outputs
  for (i <- 0 until NumOuts) {
    io.Out(i).bits.data := data_R
    io.Out(i).bits.predicate := predicate
    io.Out(i).bits.valid := valid_R
  }


  /*============================================*
   *            ACTIONS                         *
   *============================================*/

  //Instantiating a MUX
  val sel = OHToUInt(mask_R)

  printfInfo("sel: %x\n", sel)

  data_R := in_data_R(sel).data
  valid_R := true.B

  when(start & predicate) {
    state := s_COMPUTE
    ValidOut()
  }.elsewhen(start & !predicate) {
    //printfInfo("Start sending data to output INVALID\n")
    state := s_COMPUTE
    ValidOut()
  }

  /*==========================================*
   *            Output Handshaking and Reset  *
   *==========================================*/

   when(IsOutReady()) {
    //printfInfo("Start restarting output \n")
    // Reset data
    mask_R := 0.U
    mask_valid_R := false.B

    for (i <- 0 until NumInputs) {
      in_data_R(i) := DataBundle.default
    }
    // Reset output
    data_R := 0.U
    //Reset state
    state := s_idle
    //Reset output
    Reset()
  }

  printfInfo(" State: %x\n", state)
}
