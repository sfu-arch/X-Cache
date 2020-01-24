// See LICENSE for license details.

package stack

import chisel3._

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import org.scalatest.{Matchers, FlatSpec}

import dandelion.config._
import dandelion.memory.stack._




// Tester.
class StackTester(df: Stack)
                  (implicit p: Parameters) extends PeekPokeTester(df)  {

  for( i <- 0 until 10){
    poke(df.io.InData(i).bits.RouteID, i)
//    poke(df.io.InData(i).bits.node, i)
    poke(df.io.InData(i).bits.size, 3)
    poke(df.io.InData(i).bits.numByte, 2)
    poke(df.io.InData(i).valid, false.B)
  }

  step(1)

  poke(df.io.InData(3).valid, true.B)
//  poke(df.io.OutData(3).ready, true.B)
  println(s"Output: ${peek(df.io.OutData(3))}\n")
  step(1)
  println(s"Output: ${peek(df.io.OutData(3))}\n")
  step(1)
  println(s"Output: ${peek(df.io.OutData(3))}\n")
  step(1)
  println(s"Output: ${peek(df.io.OutData(3))}\n")
  step(1)
  println(s"Output: ${peek(df.io.OutData(3))}\n")

//
//
//  step(1)
//
//  println(s"Output: ${peek(df.io.Out(0))}\n")
//
//  println(s"t: -1\n -------------------------------------")
//  step(1)
//
//
//  for( i <- 0 until 10){
//    println(s"Output: ${peek(df.io.Out(0))}\n")
//
//    println(s"t: ${i}\n -------------------------------------")
//    step(1)
//  }
//  poke(df.io.CmpIn.valid, false.B)
//  poke(df.io.CmpIn.bits, false.B)
//
//  println(s"Node input : ${peek(df.io.CmpIn)}")
//  println(s"Node output: ${peek(df.io.OutIO)}")
//
//  step(1)
//
 }




class StackTests extends  FlatSpec with Matchers {
   implicit val p = new WithAccelConfig
  it should "Dataflow tester" in {
     chisel3.iotesters.Driver(() => new Stack(NumOps = 10)) {
       c => new StackTester(c)
     } should be(true)
   }
 }



