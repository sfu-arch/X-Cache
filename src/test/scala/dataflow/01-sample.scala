// See LICENSE for license details.

package dataflow

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
class Sample01Tester(df: AddDF)
                  (implicit p: Parameters) extends PeekPokeTester(df)  {

  poke(df.io.Data0.bits.data, 10.U)
  poke(df.io.Data0.valid, false.B)
  poke(df.io.Data0.bits.predicate, false.B)

  //poke(df.io.RightIO.bits.data, 5.U)
  //poke(df.io.RightIO.valid, false.B)
  //poke(df.io.RightIO.bits.predicate, false.B)

  //poke(df.io.enable.bits , false.B)
  //poke(df.io.enable.valid, false.B)
  //poke(df.io.result.ready, false.B)
  
  println(s"Output: ${peek(df.io.result)}\n")
  println(s"Pred  : ${peek(df.io.pred)}\n")

  println(s"t:-2 -------------------------")
  step(1)

  println(s"t:-1 -------------------------\n")
  poke(df.io.Data0.valid, true.B)
  poke(df.io.Data0.bits.predicate, true.B)
  poke(df.io.result.ready, true.B)


  //poke(df.io.enable.bits , true.B)
  //poke(df.io.enable.valid, true.B)
  //poke(df.io.Out(0).ready, true.B)


  //poke(df.io.LeftIO.valid, true.B)
  //poke(df.io.RightIO.valid, true.B)
  //poke(df.io.LeftIO.bits.predicate, true.B)
  //poke(df.io.RightIO.bits.predicate, true.B)

  //println(s"Output: ${peek(df.io.Out(0))}\n")
  //step(1)


  for( i <- 0 until 20){
    println(s"Output: ${peek(df.io.result)}\n")
    println(s"Pred  : ${peek(df.io.pred)}\n")
    step(1)

    println(s"t:${i} -------------------------\n")
  }
//  poke(df.io.CmpIn.valid, false.B)
//  poke(df.io.CmpIn.bits, false.B)
//
//  println(s"Node input : ${peek(df.io.CmpIn)}")
//  println(s"Node output: ${peek(df.io.OutIO)}")
//
//  step(1)
//
//  poke(df.io.CmpIn.bits, true.B)
//  println(s"Node input : ${peek(df.io.CmpIn)}")
//  println(s"Node output: ${peek(df.io.OutIO)}")
//
//  poke(df.io.CmpIn.valid, true.B)
//  step(1)
//  println(s"Node input : ${peek(df.io.CmpIn)}")
//  println(s"Node output: ${peek(df.io.OutIO)}")
//
//  step(1)
//  println(s"Node input : ${peek(df.io.CmpIn)}")
//  println(s"Node output: ${peek(df.io.OutIO)}")
//  step(1)
//
//  poke(df.io.CmpIn.bits, false.B)
//  println(s"Node input : ${peek(df.io.CmpIn)}")
//  println(s"Node output: ${peek(df.io.OutIO)}")
//  step(1)
//  println(s"Node input : ${peek(df.io.CmpIn)}")
//  println(s"Node output: ${peek(df.io.OutIO)}")
 }




class Sample01Tests extends  FlatSpec with Matchers {
   implicit val p = Parameters.root((new MiniConfig).toInstance)
  it should "Dataflow sample 01 tester" in {
     chisel3.iotesters.Driver(() => new AddDF()(p)) {
       c => new Sample01Tester(c)
     } should be(true)
   }
 }



