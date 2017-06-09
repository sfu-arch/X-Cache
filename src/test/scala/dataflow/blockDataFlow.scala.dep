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
import control._





// Tester.
class blockDataFlowTester(df: incDataFlow)(implicit p: config.Parameters) extends PeekPokeTester(df)  {

  //poke(df.io.resultReady, false.B)
  println(s" ")
  println(s"Node output: ${peek(df.io.result)}")
  println(s" ")
  step(1)
  println(s"Node output: ${peek(df.io.result)}")
  println(s" ")
  step(1)
  println(s"Node output: ${peek(df.io.result)}")
  println(s" ")
  step(1)
  println(s"Node output: ${peek(df.io.result)}")
  println(s" ")
  step(1)
  println(s"Node output: ${peek(df.io.result)}")
  println(s" ")
  step(1)
  println(s"Node output: ${peek(df.io.result)}")
  println(s" ")
  step(1)
  println(s"Node output: ${peek(df.io.result)}")
  println(s" ")
  step(1)
  //poke(df.io.resultReady, true.B)
 }




class blockTests extends  FlatSpec with Matchers {
   implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  it should "Dataflow tester" in {
     chisel3.iotesters.Driver(() => new incDataFlow()(p)) { c =>
       new blockDataFlowTester(c)
     } should be(true)
   }
 }



