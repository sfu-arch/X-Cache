// See LICENSE for license details.

package node

import chisel3._
import chisel3.util._

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import org.scalatest.{Matchers, FlatSpec}

import node._
import dataflow._
import muxes._
import config._
import util._
import interfaces._





// Tester.
class PhiTester(df: PhiNodeNew)(implicit p: config.Parameters) extends PeekPokeTester(df)  {

  poke(df.io.InData(0).valid, false.B)
  poke(df.io.InData(0).bits.valid, true.B)
  poke(df.io.InData(0).bits.predicate, true.B)
  poke(df.io.InData(0).bits.data, 5.U)

  poke(df.io.InData(1).valid, false.B)
  poke(df.io.InData(1).bits.valid, true.B)
  poke(df.io.InData(1).bits.predicate, true.B)
  poke(df.io.InData(1).bits.data, 3.U)

  poke(df.io.enable.bits, true.B)
  poke(df.io.enable.valid, false.B)

  poke(df.io.Mask.valid, false.B)
  poke(df.io.Mask.bits, 2.U)

  poke(df.io.Out(0).ready, false.B)

  println()
  println(s"Node input 0: ${peek(df.io.InData(0))}")
  println(s"Node input 1: ${peek(df.io.InData(1))}")
  println(s"Node output : ${peek(df.io.Out(0))}")
  step(1)

  poke(df.io.InData(0).valid, true.B)
  poke(df.io.InData(1).valid, true.B)
  poke(df.io.enable.valid, true.B)
  poke(df.io.Mask.valid, true.B)
  poke(df.io.Out(0).ready, true.B)

  println()
  println(s"Node input 0: ${peek(df.io.InData(0))}")
  println(s"Node input 1: ${peek(df.io.InData(1))}")
  println(s"Node output : ${peek(df.io.Out(0))}")
  step(1)


  println()
  println(s"Node input 0: ${peek(df.io.InData(0))}")
  println(s"Node input 1: ${peek(df.io.InData(1))}")
  println(s"Node output : ${peek(df.io.Out(0))}")
  step(1)

  println()
  println(s"Node input 0: ${peek(df.io.InData(0))}")
  println(s"Node input 1: ${peek(df.io.InData(1))}")
  println(s"Node output : ${peek(df.io.Out(0))}")
  step(1)

  println()
  println(s"Node input 0: ${peek(df.io.InData(0))}")
  println(s"Node input 1: ${peek(df.io.InData(1))}")
  println(s"Node output : ${peek(df.io.Out(0))}")
  step(1)
 }




class PHITests extends  FlatSpec with Matchers {
   implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  it should "Dataflow tester" in {
     chisel3.iotesters.Driver(() => new PhiNodeNew(NumInputs = 2, NumOuts = 1, ID = 0)(p)) { c =>
       new PhiTester(c)
     } should be(true)
   }
 }



