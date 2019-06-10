// See LICENSE for license details.

package node

import chisel3._
import chisel3.util._

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import org.scalatest.{Matchers, FlatSpec}

import node._
import dataflow._
import muxes._
import dandelion.config._
import util._
import interfaces._





// Tester.
class sextTester(df: SextNode)
                      (implicit p: Parameters)extends PeekPokeTester(df)  {

  //poke(df.io.Input.bits, (-16).S)
  //poke(df.io.Input.valid, true.B)
  //poke(df.io.Out(0).ready, true.B)

  println(s"Output: ${peek(df.io.Out)}\n")
  step(1)

  println(s"Output: ${peek(df.io.Out)}\n")

 }




class sextTests extends  FlatSpec with Matchers {
  implicit val p = Parameters.root((new MiniConfig).toInstance)
  it should "Dataflow tester" in {
    chisel3.iotesters.Driver(() => new SextNode()) {
       c => new sextTester(c)
     } should be(true)
   }
 }



