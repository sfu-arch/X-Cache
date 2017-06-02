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
class BranchTester(df: CBranchNode)(implicit p: config.Parameters) extends PeekPokeTester(df)  {

  poke(df.io.CmpIn.valid, false.B)
  poke(df.io.CmpIn.bits, false.B)

  println(s"Node input : ${peek(df.io.CmpIn)}")
  println(s"Node output: ${peek(df.io.OutIO)}")

  step(1)

  poke(df.io.CmpIn.bits, true.B)
  println(s"Node input : ${peek(df.io.CmpIn)}")
  println(s"Node output: ${peek(df.io.OutIO)}")

  poke(df.io.CmpIn.valid, true.B)
  step(1)
  println(s"Node input : ${peek(df.io.CmpIn)}")
  println(s"Node output: ${peek(df.io.OutIO)}")

  step(1)
  println(s"Node input : ${peek(df.io.CmpIn)}")
  println(s"Node output: ${peek(df.io.OutIO)}")
  step(1)

  poke(df.io.CmpIn.bits, false.B)
  println(s"Node input : ${peek(df.io.CmpIn)}")
  println(s"Node output: ${peek(df.io.OutIO)}")
  step(1)
  println(s"Node input : ${peek(df.io.CmpIn)}")
  println(s"Node output: ${peek(df.io.OutIO)}")
 }




class BrTests extends  FlatSpec with Matchers {
   implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  it should "Dataflow tester" in {
     chisel3.iotesters.Driver(() => new CBranchNode(0)(p)) { c =>
       new BranchTester(c)
     } should be(true)
   }
 }



