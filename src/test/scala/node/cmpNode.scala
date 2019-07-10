// See LICENSE for license details.

package dandelion.node

import chisel3._
import chisel3.util._
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import org.scalatest.{Matchers, FlatSpec}

import dandelion.config._
import dandelion.interfaces._





// Tester.
class cmpTester(df: IcmpNode)
                  (implicit p: Parameters) extends PeekPokeTester(df)  {

  poke(df.io.LeftIO.bits.data, 9.U)
  poke(df.io.LeftIO.valid, false.B)
  poke(df.io.LeftIO.bits.predicate, false.B)

  poke(df.io.RightIO.bits.data, 5.U)
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
  step(1)


  for( i <- 0 until 10){
    println(s"Output: ${peek(df.io.Out(0))}\n")
    step(1)
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




class cmpTests extends  FlatSpec with Matchers {
   implicit val p = Parameters.root((new MiniConfig).toInstance)
  it should "Dataflow tester" in {
     chisel3.iotesters.Driver(() => new IcmpNode(NumOuts = 1, ID = 0, opCode = "SLT")(sign = true)) {
       c => new cmpTester(c)
     } should be(true)
   }
 }



