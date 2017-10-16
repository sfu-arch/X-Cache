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
class LiveOutTester(df: LiveOutNode)
                  (implicit p: config.Parameters) extends PeekPokeTester(df)  {

  poke(df.io.InData.bits.data, 2.U)
  poke(df.io.InData.bits.valid, true.B)
  poke(df.io.InData.bits.predicate, false.B)

  poke(df.io.InData.valid, false.B)

  poke(df.io.enable.bits , false.B)
  poke(df.io.enable.valid, false.B)

  poke(df.io.Out(0).ready, false.B)

  println(s"Output: ${peek(df.io.Out(0))}\n")


  step(1)

  poke(df.io.enable.bits , false.B)
  poke(df.io.enable.valid, true.B)

  poke(df.io.Out(0).ready, true.B)


  poke(df.io.InData.valid, true.B)
  poke(df.io.InData.bits.predicate, true.B)

  println(s"Output: ${peek(df.io.Out(0))}\n")

  println(s"t: -1\n -------------------------------------")
  step(1)


  for( i <- 0 until 10){
    println(s"Output: ${peek(df.io.Out(0))}\n")

    println(s"t: ${i}\n -------------------------------------")
    step(1)
  }


  println(s"Make the enable valid")
  poke(df.io.enable.valid, false.B)
  poke(df.io.enable.bits, true.B)

  step(1)
  poke(df.io.enable.valid, true.B)

  for( i <- 0 until 10){
    println(s"Output: ${peek(df.io.Out(0))}\n")

    println(s"t: ${i}\n -------------------------------------")
    step(1)
  }

 }


class LiveOutTests extends  FlatSpec with Matchers {
   implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  it should "Dataflow tester" in {
     chisel3.iotesters.Driver(() => new LiveOutNode(NumOuts = 1, ID = 0)) {
       c => new LiveOutTester(c)
     } should be(true)
   }
 }



