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


class CallNode(ID: Int)(implicit p: Parameters) extends HandShakingCtrlNPS(NumOuts = 1, ID)(p) {
  override lazy val io = IO(new HandShakingIONPS(NumOuts = 1)(new ControlBundle)(p))
  // Printf debugging
  override val printfSigil = "Node (Call) ID: " + ID + " "

  /*===========================================*
   *            Registers                      *
   *===========================================*/

  // Output register
  val data_R = RegInit(0.U(xlen.W))

  val s_idle :: s_OUTPUT :: Nil = Enum(2)
  val state = RegInit(s_idle)

  /*==========================================*
   *           Predicate Evaluation           *
   *==========================================*/

  val predicate = IsEnable()
  val start = IsEnableValid()

  /*===============================================*
   *            Latch inputs. Wire up output       *
   *===============================================*/

  /**
    * Combination of bits and valid signal from CmpIn whill result the output value:
    * valid == 0  ->  output = 0
    * valid == 1  ->  cmp = true  then 1
    * valid == 1  ->  cmp = false then 2
    * @note data_R value is equale to predicate bit
    */
  // Wire up Outputs
  io.Out(0).bits.control := predicate

  /*============================================*
   *            ACTIONS (possibly dangerous)    *
   *============================================*/

  when(start & (state === s_idle)) {
    state := s_OUTPUT
    ValidOut()
  }

  /*==========================================*
   *            Output Handshaking and Reset  *
   *==========================================*/


  val out_ready_W = out_ready_R.asUInt.andR
  val out_valid_W = out_valid_R.asUInt.andR

  when(out_ready_W & (state === s_OUTPUT)) {
    //printfInfo("Start restarting output \n")
    //
    Reset()

    //Output predication
    data_R := 0.U

    //Reset state
    state := s_idle
    printfInfo("Output fired")

  }

}
