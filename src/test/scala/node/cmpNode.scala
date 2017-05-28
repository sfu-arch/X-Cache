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
class cmpTester(df: IcmpNode)(implicit p: config.Parameters) extends PeekPokeTester(df)  {

  poke(df.io.LeftIO.bits, 5.U)
  poke(df.io.LeftIO.valid, false.B)

  poke(df.io.RightIO.bits, 7.U)
  poke(df.io.RightIO.valid, false.B)

  println(s"Node Rinput : ${peek(df.io.LeftIO)}")
  println(s"Node Linput : ${peek(df.io.RightIO)}")
  println(s"Node output : ${peek(df.io.OutIO)}")
  println(s"")

  step(1)

  println(s"Node Rinput : ${peek(df.io.LeftIO)}")
  println(s"Node Linput : ${peek(df.io.RightIO)}")
  println(s"Node output : ${peek(df.io.OutIO)}")
  println(s"")

  step(1)

  poke(df.io.LeftIO.valid, true.B)
  poke(df.io.RightIO.valid, true.B)
  println(s"Node Rinput : ${peek(df.io.LeftIO)}")
  println(s"Node Linput : ${peek(df.io.RightIO)}")
  println(s"Node output : ${peek(df.io.OutIO)}")
  println(s"")

  step(1)
  println(s"Node Rinput : ${peek(df.io.LeftIO)}")
  println(s"Node Linput : ${peek(df.io.RightIO)}")
  println(s"Node output : ${peek(df.io.OutIO)}")
  println(s"")

 }




class cmpTests extends  FlatSpec with Matchers {
   implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  it should "Dataflow tester" in {
     chisel3.iotesters.Driver(() => new IcmpNode(4)(p)) { c =>
       new cmpTester(c)
     } should be(true)
   }
 }



