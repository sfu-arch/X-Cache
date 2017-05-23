// See LICENSE for license details.

package dataflow

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
class AllocaTester(df: AllocaTest)(implicit p: config.Parameters) extends PeekPokeTester(df)  {

  poke(df.io.resultReady, false.B)
  println(s" ")
  println(s"Node output: ${peek(df.io.result)}")
  println(s"Node valid : ${peek(df.io.resultValid)}")
  println(s" ")
  step(1)
  poke(df.io.resultReady, true.B)
  println(s"Node output: ${peek(df.io.result)}")
  println(s"Node valid : ${peek(df.io.resultValid)}")
  println(s" ")
  step(1)
  println(s"Node output: ${peek(df.io.result)}")
  println(s"Node valid : ${peek(df.io.resultValid)}")
  println(s" ")
  step(1)
  println(s"Node output: ${peek(df.io.result)}")
  println(s"Node valid : ${peek(df.io.resultValid)}")
  println(s" ")
  step(1)
  println(s"Node output: ${peek(df.io.result)}")
  println(s"Node valid : ${peek(df.io.resultValid)}")
  println(s" ")
  step(1)
  println(s"Node output: ${peek(df.io.result)}")
  println(s"Node valid : ${peek(df.io.resultValid)}")
  println(s" ")
  step(1)
  poke(df.io.resultReady, false.B)
  println(s" ")
  println(s"Node output: ${peek(df.io.result)}")
  println(s"Node valid : ${peek(df.io.resultValid)}")
  println(s" ")
  step(1)
  println(s"Node output: ${peek(df.io.result)}")
  println(s"Node valid : ${peek(df.io.resultValid)}")
  println(s" ")
  step(1)
  println(s"Node output: ${peek(df.io.result)}")
  println(s"Node valid : ${peek(df.io.resultValid)}")
  println(s" ")
  step(1)
  println(s"Node output: ${peek(df.io.result)}")
  println(s"Node valid : ${peek(df.io.resultValid)}")
  println(s" ")
  step(1)
  println(s"Node output: ${peek(df.io.result)}")
  println(s"Node valid : ${peek(df.io.resultValid)}")
  println(s" ")
  step(1)
  poke(df.io.resultReady, true.B)

  println(s"Node output: ${peek(df.io.result)}")
  println(s"Node valid : ${peek(df.io.resultValid)}")
  println(s" ")
  step(1)
  println(s"Node output: ${peek(df.io.result)}")
  println(s"Node valid : ${peek(df.io.resultValid)}")
  println(s" ")
  step(1)
  println(s"Node output: ${peek(df.io.result)}")
  println(s"Node valid : ${peek(df.io.resultValid)}")
  println(s" ")
  step(1)
  println(s"Node output: ${peek(df.io.result)}")
  println(s"Node valid : ${peek(df.io.resultValid)}")
  println(s" ")
  step(1)
  println(s"Node output: ${peek(df.io.result)}")
  println(s"Node valid : ${peek(df.io.resultValid)}")
  println(s" ")
  step(1)
  println(s"Node output: ${peek(df.io.result)}")
  println(s"Node valid : ${peek(df.io.resultValid)}")
  println(s" ")
  step(1)
  println(s"Node output: ${peek(df.io.result)}")
  println(s"Node valid : ${peek(df.io.resultValid)}")
  println(s" ")
  step(1)
  println(s"Node output: ${peek(df.io.result)}")
  println(s"Node valid : ${peek(df.io.resultValid)}")
  println(s" ")
  step(1)
  println(s"Node output: ${peek(df.io.result)}")
  println(s"Node valid : ${peek(df.io.resultValid)}")
  println(s" ")
  step(1)
 }




class allocaTests extends  FlatSpec with Matchers {
   implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  it should "Dataflow tester" in {
     chisel3.iotesters.Driver(() => new AllocaTest()(p)) { c =>
       new AllocaTester(c)
     } should be(true)
   }
 }



