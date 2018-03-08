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

class ExpandNodeIO[T <: Data](NumOuts: Int)(gen: T)
                   (implicit p: Parameters)
  extends HandShakingIONPS(NumOuts)(gen) {
  val InData = Flipped(Decoupled(gen))
}

class ExpandNode[T <: Data](NumOuts: Int, ID: Int)(gen: T)
                 (implicit p: Parameters)
  extends HandShakingNPS(NumOuts, ID)(gen)(p) {
  override lazy val io = IO(new ExpandNodeIO(NumOuts)(gen))
  // Printf debugging

  /*===========================================*
   *            Registers                      *
   *===========================================*/
  // Left Input
  val indata_R = RegInit(0.U.asTypeOf(gen))
  val indata_valid_R = RegInit(false.B)

  val s_idle :: s_LATCH :: s_COMPUTE :: Nil = Enum(3)
  val state = RegInit(s_idle)

  /*==========================================*
   *           Predicate Evaluation           *
   *==========================================*/

  val predicate = IsEnable()
  val start = indata_valid_R & IsEnableValid()

  /*===============================================*
   *            Latch inputs. Wire up output       *
   *===============================================*/


  io.InData.ready := ~indata_valid_R
  when(io.InData.fire()) {
    state := s_LATCH
    indata_R <> io.InData.bits
    indata_valid_R := true.B
  }

  // Wire up Outputs
  for (i <- 0 until NumOuts) {
    io.Out(i).bits <> indata_R
  }

  /*============================================*
   *            ACTIONS (possibly dangerous)    *
   *============================================*/

  when(start & state =/= s_COMPUTE) {
    state := s_COMPUTE
    ValidOut()
  }

  /*==========================================*
   *            Output Handshaking and Reset  *
   *==========================================*/

  when(IsOutReady() & (state === s_COMPUTE)) {

    // Reset data
//    indata_R := gen.default

    indata_valid_R := false.B

    //Reset state
    state := s_idle
    //Reset output
    Reset()
  }

}
