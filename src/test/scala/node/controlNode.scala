// See LICENSE for license details.

package node

import chisel3._
import chisel3.util._

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import org.scalatest.{Matchers, FlatSpec}

import node._
import control._
import dataflow._
import muxes._
import dandelion.config._
import util._
import interfaces._


// Tester.
class controlTester(df: BasicBlockNode)
                   (implicit p: Parameters) extends PeekPokeTester(df) {

  poke(df.io.predicateIn(0).valid, false.B)
  poke(df.io.predicateIn(1).valid, false.B)
  poke(df.io.Out(0).ready, false.B)
  poke(df.io.Out(1).ready, false.B)
  poke(df.io.MaskBB(0).ready, false.B)

  println(s"Output 0: ${peek(df.io.Out(0))}")
  println(s"Output 1: ${peek(df.io.Out(1))}")
  println(s"Mask   0: ${peek(df.io.MaskBB(0))}")
  println(s"###############")
  step(1)

  poke(df.io.predicateIn(0).bits.control, false.B)
  poke(df.io.predicateIn(0).valid, false.B)
  poke(df.io.predicateIn(1).bits.control, false.B)
  poke(df.io.predicateIn(1).valid, true.B)

  poke(df.io.Out(0).ready, true.B)
  poke(df.io.Out(1).ready, true.B)
  poke(df.io.MaskBB(0).ready, true.B)

  println(s"Output 0: ${peek(df.io.Out(0))}")
  println(s"Output 1: ${peek(df.io.Out(1))}")
  println(s"Mask   0: ${peek(df.io.MaskBB(0))}")
  println(s"###############")
  step(1)

  poke(df.io.predicateIn(1).valid, false.B)

  println(s"Output 0: ${peek(df.io.Out(0))}")
  println(s"Output 1: ${peek(df.io.Out(1))}")
  println(s"Mask   0: ${peek(df.io.MaskBB(0))}")
  println(s"###############")
  step(1)


  println(s"Output 0: ${peek(df.io.Out(0))}")
  println(s"Output 1: ${peek(df.io.Out(1))}")
  println(s"Mask   0: ${peek(df.io.MaskBB(0))}")
  println(s"###############")
  step(1)

  poke(df.io.predicateIn(0).bits.control, true.B)
  poke(df.io.predicateIn(0).valid, true.B)


  println(s"Output 0: ${peek(df.io.Out(0))}")
  println(s"Output 1: ${peek(df.io.Out(1))}")
  println(s"Mask   0: ${peek(df.io.MaskBB(0))}")
  println(s"###############")
  step(1)

  println(s"Output 0: ${peek(df.io.Out(0))}")
  println(s"Output 1: ${peek(df.io.Out(1))}")
  println(s"Mask   0: ${peek(df.io.MaskBB(0))}")
  println(s"###############")
  step(1)

  println(s"Output 0: ${peek(df.io.Out(0))}")
  println(s"Output 1: ${peek(df.io.Out(1))}")
  println(s"Mask   0: ${peek(df.io.MaskBB(0))}")
  println(s"###############")
  step(1)

  println(s"Output 0: ${peek(df.io.Out(0))}")
  println(s"Output 1: ${peek(df.io.Out(1))}")
  println(s"Mask   0: ${peek(df.io.MaskBB(0))}")
  println(s"###############")
  step(1)


  //  for( i <- 0 until 10){
  //    println(s"Output 0: ${peek(df.io.Out(0))}")
  //    println(s"Output 1: ${peek(df.io.Out(1))}")
  //    println("")
  //    step(1)
  //  }
}


class ControlTests extends FlatSpec with Matchers {
  implicit val p = Parameters.root((new MiniConfig).toInstance)
  it should "BasicBlock tester" in {
    chisel3.iotesters.Driver.execute(
     Array(
       // "-ll", "Info",
       "-tbn", "verilator",
       "-td", "test_run_dir",
       "-tts", "0001"),
      () => new BasicBlockNode(NumInputs = 2, NumOuts = 2, NumPhi = 1, BID = 0)){
       c => new controlTester(c)
    } should be(true)
  }
}
