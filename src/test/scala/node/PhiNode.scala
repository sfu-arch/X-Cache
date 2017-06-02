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
class PhiTester(df: PhiNode)(implicit p: config.Parameters) extends PeekPokeTester(df)  {

  poke(df.io.DataIn(0).valid, false.B)
  poke(df.io.DataIn(1).valid, false.B)

  poke(df.io.Predicates(0), false.B)
  poke(df.io.Predicates(1), false.B)

  poke(df.io.DataIn(0).bits , 5.U)
  poke(df.io.DataIn(1).bits , 3.U)
  //poke(df.io.CmpIn.bits, false.B)

  println()
  println(s"Node input 0: ${peek(df.io.DataIn(0))}")
  println(s"Node input 1: ${peek(df.io.DataIn(1))}")
  println(s"Node output : ${peek(df.io.OutIO)}")

  step(1)

  poke(df.io.DataIn(0).valid, false.B)
  poke(df.io.DataIn(1).valid, true.B)

  poke(df.io.Predicates(0), true.B)
  poke(df.io.Predicates(1), false.B)

  poke(df.io.OutIO.ready, true.B)
  println()
  println(s"Node input 0: ${peek(df.io.DataIn(0))}")
  println(s"Node input 1: ${peek(df.io.DataIn(1))}")
  println(s"Node output : ${peek(df.io.OutIO)}")
  step(1)

  poke(df.io.Predicates(0), false.B)
  poke(df.io.Predicates(1), true.B)
  println()
  println(s"Node input 0: ${peek(df.io.DataIn(0))}")
  println(s"Node input 1: ${peek(df.io.DataIn(1))}")
  println(s"Node output : ${peek(df.io.OutIO)}")
  step(1)

  //poke(df.io.CmpIn.bits, true.B)
  //println(s"Node input : ${peek(df.io.CmpIn)}")
  //println(s"Node output: ${peek(df.io.OutIO)}")

  //poke(df.io.CmpIn.valid, true.B)
  //step(1)
  //println(s"Node input : ${peek(df.io.CmpIn)}")
  //println(s"Node output: ${peek(df.io.OutIO)}")

  //step(1)
  //println(s"Node input : ${peek(df.io.CmpIn)}")
  //println(s"Node output: ${peek(df.io.OutIO)}")
  //step(1)

  //poke(df.io.CmpIn.bits, false.B)
  //println(s"Node input : ${peek(df.io.CmpIn)}")
  //println(s"Node output: ${peek(df.io.OutIO)}")
  //step(1)
  //println(s"Node input : ${peek(df.io.CmpIn)}")
  //println(s"Node output: ${peek(df.io.OutIO)}")
 }




class PHITests extends  FlatSpec with Matchers {
   implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  it should "Dataflow tester" in {
     chisel3.iotesters.Driver(() => new PhiNode(2)(p)) { c =>
       new PhiTester(c)
     } should be(true)
   }
 }



