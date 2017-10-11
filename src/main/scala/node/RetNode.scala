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

class RetNodeIO(NumOuts: Int)
                   (implicit p: Parameters)
extends HandShakingIONPS (NumOuts)(new DataBundle) {
  // Input data
  val InputIO = Flipped (Decoupled (new DataBundle) )
}

class RetNode(NumOuts: Int, ID: Int)
                 (implicit p: Parameters)
  extends HandShakingNPS(NumOuts, ID)(new DataBundle)(p) {
  override lazy val io = IO(new RetNodeIO(NumOuts))

  /*===========================================*
   *            Registers                      *
   *===========================================*/
  // Left Input
  val input_R = RegInit(DataBundle.default)

  val s_idle :: s_LATCH :: s_COMPUTE :: Nil = Enum(3)
  val state = RegInit(s_idle)

  /*==========================================*
   *           Predicate Evaluation           *
   *==========================================*/

  val predicate = input_R.predicate & IsEnable()
  val start = input_R.valid & IsEnableValid()

  /*===============================================*
   *            Latch inputs. Wire up output       *
   *===============================================*/

  //printfInfo("start: %x\n", start)

  io.InputIO.ready := ~input_R.valid
  when(io.InputIO.fire()) {
    //printfInfo("Latch left data\n")
    state := s_LATCH
    input_R.data := io.InputIO.bits.data
    input_R.valid := true.B
    input_R.predicate := io.InputIO.bits.predicate
  }

  /*============================================*
   *            ACTIONS (possibly dangerous)    *
   *============================================*/

  // Wire up Outputs
  for (i <- 0 until NumOuts) {
    io.Out(i).bits.data := input_R.data
    io.Out(i).bits.predicate := predicate 
  }

  when(start & state =/= s_COMPUTE) {
    state := s_COMPUTE
    ValidOut()
  }
  /*==========================================*
   *            Output Handshaking and Reset  *
   *==========================================*/

  when(IsOutReady() & (state === s_COMPUTE)) {
    // Reset data
    input_R := DataBundle.default
    //Reset state
    state := s_idle
    //Reset output
    Reset()
  }
}
