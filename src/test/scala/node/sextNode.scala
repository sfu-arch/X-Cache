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
class sextTester(df: SextNode) extends PeekPokeTester(df)  {

  poke(df.io.Input.bits, (-16).S)
  poke(df.io.Input.valid, true.B)
  poke(df.io.Out(0).ready, true.B)

  println(s"Output: ${peek(df.io.Out(0))}\n")
  step(1)

  println(s"Output: ${peek(df.io.Out(0))}\n")

 }




class sextTests extends  FlatSpec with Matchers {
  it should "Dataflow tester" in {
     chisel3.iotesters.Driver(() => new SextNode(SrcW = 32, DesW = 64, NumOuts=1)) {
       c => new sextTester(c)
     } should be(true)
   }
 }



