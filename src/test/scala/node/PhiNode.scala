// See LICENSE for license details.

package node

import chisel3._
import chisel3.util._
import chisel3.util.Reverse

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

  poke(df.io.InData(0).valid, false.B)
// //   poke(df.io.InData(0).bits.valid, true.B)
  poke(df.io.InData(0).bits.predicate, true.B)
  poke(df.io.InData(0).bits.data, 5.U)

  poke(df.io.InData(1).valid, false.B)
// //   poke(df.io.InData(1).bits.valid, true.B)
  poke(df.io.InData(1).bits.predicate, true.B)
  poke(df.io.InData(1).bits.data, 3.U)

  poke(df.io.enable.bits.control, true.B)
  poke(df.io.enable.valid, false.B)

  poke(df.io.Mask.bits, 0.U)
  poke(df.io.Mask.valid, false.B)
  //poke(df.io.Mask.valid, true.B)

  poke(df.io.Out(0).ready, false.B)

  println()
  println(s"Node input 0: ${peek(df.io.InData(0))}")
  println(s"Node input 1: ${peek(df.io.InData(1))}")
  println(s"Node output : ${peek(df.io.Out(0))}")
  step(1)
  var time = 1

  poke(df.io.InData(0).valid, true.B)
  poke(df.io.InData(1).valid, true.B)

  poke(df.io.enable.valid, true.B)

  poke(df.io.Mask.valid, true.B)
  poke(df.io.Mask.bits, 2.U)


  //while(peek(df.io.Out(0).valid) == BigInt(0)){
    //println()
    //println(s"Node input 0: ${peek(df.io.InData(0))}")
    //println(s"Node input 1: ${peek(df.io.InData(1))}")
    //println(s"Node output : ${peek(df.io.Out(0))}")

    //println(s"Time: $time")
    //step(1)
    //time = time + 1
  //}

  for(i <- 0 until 10 ){

    println()
    println(s"Node input 0: ${peek(df.io.InData(0))}")
    println(s"Node input 1: ${peek(df.io.InData(1))}")
    println(s"Node output : ${peek(df.io.Out(0))}")

    println(s"Time: $time")
    step(1)
    time = time + 1
  }


  //step(1)

  println()
  println(s"Node input 0: ${peek(df.io.InData(0))}")
  println(s"Node input 1: ${peek(df.io.InData(1))}")
  println(s"Node output : ${peek(df.io.Out(0))}")

  println(s"Time: $time")
  step(1)


  println()
  println(s"Node input 0: ${peek(df.io.InData(0))}")
  println(s"Node input 1: ${peek(df.io.InData(1))}")
  println(s"Node output : ${peek(df.io.Out(0))}")

  println(s"Time: $time")
  step(1)

  println()
  println(s"Node input 0: ${peek(df.io.InData(0))}")
  println(s"Node input 1: ${peek(df.io.InData(1))}")
  println(s"Node output : ${peek(df.io.Out(0))}")

  println(s"Time: $time")
  step(1)

  //while(peek(df.io.Out(0).valid) == BigInt(0)){
    //println()
    //println(s"Node input 0: ${peek(df.io.InData(0))}")
    //println(s"Node input 1: ${peek(df.io.InData(1))}")
    //println(s"Node output : ${peek(df.io.Out(0))}")

    //println(s"Time: $time")
    //step(1)
    //time = time + 1
  //}


  println(s"Time: $time")
  println(s"Result of the DF: ${peek(df.io.Out(0))}")
  //expect(df.io.Out(0).bits.data, 5.U)


 }




class PHITests extends  FlatSpec with Matchers {
   implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  it should "Dataflow tester" in {
     chisel3.iotesters.Driver(() => new PhiNode(NumInputs = 2, NumOuts = 1, ID = 0)(p)) { c =>
       new PhiTester(c)
     } should be(true)
   }
 }



