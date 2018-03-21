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
                (implicit p: Parameters,
                 name: sourcecode.Name,
                 file: sourcecode.File)
  extends HandShakingNPS(NumOuts, ID)(new DataBundle)(p) {
  override lazy val io = IO(new PhiNodeIO(NumInputs, NumOuts))
  // Printf debugging
  val node_name = name.value
  val module_name = file.value.split("/").tail.last.split("\\.").head.capitalize

  override val printfSigil = "[" + module_name + "] " + node_name + ": " + ID + " "
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

  val s_IDLE :: s_COMPUTE :: Nil = Enum(2)
  val state = RegInit(s_IDLE)

  /*==========================================*
   *           Predicate Evaluation           *
   *==========================================*/

  var predicate = in_data_W.predicate & IsEnable()

  /*===============================================*
   *            Latch inputs. Wire up output       *
   *===============================================*/

  // If the mask value is eqaul to zero we don't proceed
  val mask_valid = mask_R.asUInt.orR

  //Instantiating a MUX
  val sel = OHToUInt(mask_R)

  in_data_W := in_data_R(sel)


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
      when(mask_valid_R && enable_valid_R && in_data_valid_R(sel)){
        state := s_COMPUTE
        ValidOut()
      }
    }
    is(s_COMPUTE){
//      when(IsOutReady()){
      when(in_data_valid_R(sel)){
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
          printf("[LOG] " + "[" + module_name + "] " + node_name + ": Output fired @ %d, Value: %d\n",cycleCount, in_data_W.data)
        }


      }
    }
  }

}
