// See LICENSE for license details.
package dataflow

import chisel3._
import chisel3.util._

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import org.scalatest.{Matchers, FlatSpec}

import dandelion.config._


// Tester.
class Sample02Tester(df: StackDF)
                  (implicit p: Parameters) extends PeekPokeTester(df)  {

  poke(df.io.Data0.bits.size, 3.U)
  poke(df.io.Data0.bits.numByte, 4.U)
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
  poke(df.io.Data0.valid, true.B)

  println(s"t:-1 -------------------------\n")
  poke(df.io.Data0.valid, true.B)
  poke(df.io.Data0.bits.predicate, true.B)
  poke(df.io.result.ready, false.B)


  //poke(df.io.enable.bits , true.B)
  //poke(df.io.enable.valid, true.B)
  //poke(df.io.Out(0).ready, true.B)


  //poke(df.io.LeftIO.valid, true.B)
  //poke(df.io.RightIO.valid, true.B)
  //poke(df.io.LeftIO.bits.predicate, true.B)
  //poke(df.io.RightIO.bits.predicate, true.B)

  //println(s"Output: ${peek(df.io.Out(0))}\n")
  //step(1)


  for( i <- 0 until 5){
    println(s"Output: ${peek(df.io.result)}")
    println(s"Pred  : ${peek(df.io.pred)}")
    step(1)

    println(s"t:${i} -------------------------")
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




class Sample02Tests extends  FlatSpec with Matchers {
   implicit val p = Parameters.root((new MiniConfig).toInstance)
  it should "Dataflow sample 01 tester" in {
     chisel3.iotesters.Driver(() => new StackDF()(p)) {
       c => new Sample02Tester(c)
     } should be(true)
   }
 }



