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


/**
  * @note
  * For Conditional Branch output is always equal to two!
  * Since your branch output wire to two different basic block only
  */

class CBranchNodeIO(NumOuts: Int = 2)
                   (implicit p: Parameters)
  extends HandShakingCtrlIONPS(NumOuts) {

  // RightIO: Right input data for computation
  val CmpIO = Flipped(Decoupled(new DataBundle))
}

class CBranchNode(ID: Int)
                 (implicit p: Parameters)
  extends HandShakingCtrlNPS(2, ID)(p) {
  override lazy val io = IO(new CBranchNodeIO())
  // Printf debugging
  override val printfSigil = "Node ID: " + ID + " "

  /*===========================================*
   *            Registers                      *
   *===========================================*/
  // OP Inputs
  val cmp_R = RegInit(DataBundle.default)


  // Output register
  val data0_R = RegInit(0.U(xlen.W))
  val data1_R = RegInit(0.U(xlen.W))

  val s_idle :: s_LATCH :: s_COMPUTE :: Nil = Enum(3)
  val state = RegInit(s_idle)

  /*==========================================*
   *           Predicate Evaluation           *
   *==========================================*/

  val predicate = cmp_R.predicate & IsEnable()
  val start = cmp_R.valid & IsEnableValid()

  /*===============================================*
   *            Latch inputs. Wire up output       *
   *===============================================*/

  // Predicate register
  val pred_R = RegInit(init = false.B)


  io.CmpIO.ready := ~cmp_R.valid
  when(io.CmpIO.fire()) {
    //printfInfo("Latch left data\n")
    state := s_LATCH
    cmp_R.data := io.CmpIO.bits.data
    cmp_R.valid := true.B
    cmp_R.predicate := io.CmpIO.bits.predicate
  }

  // Wire up Outputs
  io.Out(0).bits.control := data0_R.orR.toBool
//  io.Out(0).bits.predicate := pred_R

  io.Out(1).bits.control := data1_R.orR.toBool
//  io.Out(1).bits.predicate := pred_R

  //printf(p"cmp_R: ${cmp_R}\n")

  /*============================================*
   *            ACTIONS (possibly dangerous)    *
   *============================================*/

  /**
    * Combination of bits and valid signal from CmpIn whill result the output value:
    * valid == 0  ->  output = 0
    * valid == 1  ->  cmp = true  then 1
    * valid == 1  ->  cmp = false then 2
    */

  data0_R := ~cmp_R.data.asUInt.orR
  data1_R := cmp_R.data.asUInt.orR
  pred_R := predicate

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


  val out_ready_W = out_ready_R.asUInt.andR
  val out_valid_W = out_valid_R.asUInt.andR

  printfInfo("out_ready: %x\n", out_ready_W)
  printfInfo("out_valid: %x\n", out_valid_W)

  //printfInfo(" Start restarting\n")
  when(out_ready_W & out_valid_W) {
    //printfInfo("Start restarting output \n")
    // Reset data
    cmp_R := DataBundle.default
    // Reset output
    data0_R := 0.U
    data1_R := 0.U
    out_ready_R := Vec(Seq.fill(NumOuts) {
      false.B
    })
    //Reset state
    state := s_idle
    //Restart predicate bit
    pred_R := false.B
  }

  printfInfo(" State: %x\n", state)


}

class UBranchNode(ID: Int)
                 (implicit p: Parameters)
  extends HandShakingCtrlNPS(NumOuts = 1, ID)(p) {
  override lazy val io = IO(new HandShakingCtrlIONPS(NumOuts = 1)(p))
  // Printf debugging
  override val printfSigil = "Node (CBR) ID: " + ID + " "

  /*===========================================*
   *            Registers                      *
   *===========================================*/
  // OP Inputs
  val cmp_R = RegInit(DataBundle.default)


  // Output register
  val data_R = RegInit(0.U(xlen.W))

  val s_idle :: s_LATCH :: s_COMPUTE :: Nil = Enum(3)
  val state = RegInit(s_idle)

  /*==========================================*
   *           Predicate Evaluation           *
   *==========================================*/

  val predicate = IsEnable()
  val start = IsEnableValid()

  /*===============================================*
   *            Latch inputs. Wire up output       *
   *===============================================*/

  // Predicate register
  val pred_R = RegInit(false.B)
  val valid_R = RegInit(false.B)


  // Wire up Outputs
  io.Out(0).bits.control := data_R.orR.toBool

  //printf(p"cmp_R: ${cmp_R}\n")

  /*============================================*
   *            ACTIONS (possibly dangerous)    *
   *============================================*/

  /**
    * Combination of bits and valid signal from CmpIn whill result the output value:
    * valid == 0  ->  output = 0
    * valid == 1  ->  cmp = true  then 1
    * valid == 1  ->  cmp = false then 2
    * @note data_R value is equale to predicate bit
    */

  data_R := predicate
  pred_R := predicate
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


  val out_ready_W = out_ready_R.asUInt.andR
  val out_valid_W = out_valid_R.asUInt.andR

  printfInfo("out_ready: %x\n", out_ready_W)
  printfInfo("out_valid: %x\n", out_valid_W)

  //printfInfo(" Start restarting\n")
  when(out_ready_W & out_valid_W) {
    //printfInfo("Start restarting output \n")
    // Reset data
    cmp_R := DataBundle.default
    // Reset output
    data_R := 0.U
    out_ready_R := Vec(Seq.fill(NumOuts) {
      false.B
    })
    //Reset state
    state := s_idle
    //Restart predicate bit
    pred_R := false.B
  }

  printfInfo(" State: %x\n", state)


}
