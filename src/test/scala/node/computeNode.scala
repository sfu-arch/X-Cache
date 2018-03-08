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
class computeTester(df: ComputeNode)
                  (implicit p: config.Parameters) extends PeekPokeTester(df)  {

  poke(df.io.LeftIO.bits.data, 2.U)
  poke(df.io.LeftIO.valid, false.B)
  poke(df.io.LeftIO.bits.predicate, false.B)

  poke(df.io.RightIO.bits.data, 1.U)
  poke(df.io.RightIO.valid, false.B)
  poke(df.io.RightIO.bits.predicate, false.B)

  poke(df.io.enable.bits.control , false.B)
  poke(df.io.enable.valid, false.B)
  poke(df.io.Out(0).ready, false.B)
  println(s"Output: ${peek(df.io.Out(0))}\n")


  step(1)

  poke(df.io.enable.bits.control , true.B)
  poke(df.io.enable.valid, true.B)
  poke(df.io.Out(0).ready, true.B)


  poke(df.io.LeftIO.valid, true.B)
  poke(df.io.RightIO.valid, true.B)
  poke(df.io.LeftIO.bits.predicate, true.B)
  poke(df.io.RightIO.bits.predicate, true.B)

  println(s"Output: ${peek(df.io.Out(0))}\n")

  println(s"t: -1\n -------------------------------------")
  step(1)


  for( i <- 0 until 10){
    println(s"Output: ${peek(df.io.Out(0))}\n")

    println(s"t: ${i}\n -------------------------------------")
    step(1)
  }
 }


class CompTests extends  FlatSpec with Matchers {
   implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  it should "Dataflow tester" in {
     chisel3.iotesters.Driver(() => new ComputeNode(NumOuts = 1, ID = 0, opCode = "Add")(sign = false)) {
       c => new computeTester(c)
     } should be(true)
   }
 }



