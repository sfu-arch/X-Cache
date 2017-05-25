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
class RelayTester(df: RelayDecoupledNode)(implicit p: config.Parameters) extends PeekPokeTester(df)  {

  poke(df.io.DataIn.valid, false.B)
  poke(df.io.DataIn.bits, 10)
  poke(df.io.TokenIn, 1)

  poke(df.io.OutIO(0).TokenNode, 1)
  poke(df.io.OutIO(1).TokenNode, 1)

  poke(df.io.OutIO(0).DataNode.ready, false)
  poke(df.io.OutIO(1).DataNode.ready, false)

  println(s"Node Input : ${peek(df.io.DataIn)}")
  println(s"Node Token : ${peek(df.io.TokenIn)}")
  step(1)
  poke(df.io.DataIn.valid, true.B)
  poke(df.io.OutIO(1).DataNode.ready, true)

  println(s"Node Input : ${peek(df.io.DataIn)}")
  println(s"Node Token : ${peek(df.io.TokenIn)}")
  step(1)


  poke(df.io.OutIO(0).DataNode.ready, true)
  println(s"Node Input : ${peek(df.io.DataIn)}")
  println(s"Node Token : ${peek(df.io.TokenIn)}")
  step(1)

  println(s"Node Input : ${peek(df.io.DataIn)}")
  println(s"Node Token : ${peek(df.io.TokenIn)}")
  step(1)

  //println(s" ")
  //println(s"Node output: ${peek(df.io.result)}")
  //println(s"Node valid : ${peek(df.io.resultValid)}")
  //println(s" ")
  //step(1)
  //poke(df.io.resultReady, true.B)
  //println(s"Node output: ${peek(df.io.result)}")
  //println(s"Node valid : ${peek(df.io.resultValid)}")
  //println(s" ")
  //step(1)
  //println(s"Node output: ${peek(df.io.result)}")
  //println(s"Node valid : ${peek(df.io.resultValid)}")
  //println(s" ")
  //step(1)
  //println(s"Node output: ${peek(df.io.result)}")
  //println(s"Node valid : ${peek(df.io.resultValid)}")
  //println(s" ")
  //step(1)
  //println(s"Node output: ${peek(df.io.result)}")
  //println(s"Node valid : ${peek(df.io.resultValid)}")
  //println(s" ")
  //step(1)
  //println(s"Node output: ${peek(df.io.result)}")
  //println(s"Node valid : ${peek(df.io.resultValid)}")
  //println(s" ")
  //step(1)
  //poke(df.io.resultReady, false.B)
  //println(s" ")
  //println(s"Node output: ${peek(df.io.result)}")
  //println(s"Node valid : ${peek(df.io.resultValid)}")
  //println(s" ")
  //step(1)
  //println(s"Node output: ${peek(df.io.result)}")
  //println(s"Node valid : ${peek(df.io.resultValid)}")
  //println(s" ")
  //step(1)
  //println(s"Node output: ${peek(df.io.result)}")
  //println(s"Node valid : ${peek(df.io.resultValid)}")
  //println(s" ")
  //step(1)
  //println(s"Node output: ${peek(df.io.result)}")
  //println(s"Node valid : ${peek(df.io.resultValid)}")
  //println(s" ")
  //step(1)
  //println(s"Node output: ${peek(df.io.result)}")
  //println(s"Node valid : ${peek(df.io.resultValid)}")
  //println(s" ")
  //step(1)
  //poke(df.io.resultReady, true.B)

  //println(s"Node output: ${peek(df.io.result)}")
  //println(s"Node valid : ${peek(df.io.resultValid)}")
  //println(s" ")
  //step(1)
  //println(s"Node output: ${peek(df.io.result)}")
  //println(s"Node valid : ${peek(df.io.resultValid)}")
  //println(s" ")
  //step(1)
  //println(s"Node output: ${peek(df.io.result)}")
  //println(s"Node valid : ${peek(df.io.resultValid)}")
  //println(s" ")
  //step(1)
  //println(s"Node output: ${peek(df.io.result)}")
  //println(s"Node valid : ${peek(df.io.resultValid)}")
  //println(s" ")
  //step(1)
  //println(s"Node output: ${peek(df.io.result)}")
  //println(s"Node valid : ${peek(df.io.resultValid)}")
  //println(s" ")
  //step(1)
  //println(s"Node output: ${peek(df.io.result)}")
  //println(s"Node valid : ${peek(df.io.resultValid)}")
  //println(s" ")
  //step(1)
  //println(s"Node output: ${peek(df.io.result)}")
  //println(s"Node valid : ${peek(df.io.resultValid)}")
  //println(s" ")
  //step(1)
  //println(s"Node output: ${peek(df.io.result)}")
  //println(s"Node valid : ${peek(df.io.resultValid)}")
  //println(s" ")
  //step(1)
  //println(s"Node output: ${peek(df.io.result)}")
  //println(s"Node valid : ${peek(df.io.resultValid)}")
  //println(s" ")
  //step(1)
 }




class RelTests extends  FlatSpec with Matchers {
   implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  it should "Dataflow tester" in {
     chisel3.iotesters.Driver(() => new RelayDecoupledNode(2)(p)) { c =>
       new RelayTester(c)
     } should be(true)
   }
 }



