// See LICENSE for license details.

package dandelion.node

import chisel3._
import chisel3.util._
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import org.scalatest.{Matchers, FlatSpec}

import chipsalliance.rocketchip.config._
import dandelion.config._
import dandelion.interfaces._





// Tester.
class AllocaTester(df: AllocaNode)(implicit p: Parameters) extends PeekPokeTester(df)  {

  poke(df.io.allocaInputIO.bits.size, 3.U)
  poke(df.io.allocaInputIO.bits.numByte, 4.U)
  poke(df.io.allocaInputIO.bits.predicate, false.B)
// //   poke(df.io.allocaInputIO.bits.valid, false.B)
  poke(df.io.allocaInputIO.valid, false.B)

  poke(df.io.enable.bits.control , false.B)
  poke(df.io.enable.valid, false.B)
  poke(df.io.Out(0).ready, false.B)

  poke(df.io.allocaReqIO.ready, true.B)
  poke(df.io.allocaRespIO.valid, false.B)

  println(s"Output: ${peek(df.io.Out(0))}\n")


  step(1)

  poke(df.io.enable.bits.control , true.B)
  poke(df.io.enable.valid, true.B)
  poke(df.io.Out(0).ready, false.B)


  poke(df.io.allocaInputIO.valid, true.B)
// //   poke(df.io.allocaInputIO.bits.valid, true.B)
  poke(df.io.allocaInputIO.bits.predicate, true.B)

  println(s"Output: ${peek(df.io.Out(0))}\n")
  step(1)


  for( i <- 0 until 5){
    println(s"Output: ${peek(df.io.Out(0))}\n")
    println(s"t:${i} -------------------------")
    step(1)
  }

  poke(df.io.allocaRespIO.valid, true.B)
  poke(df.io.allocaRespIO.ptr, 4.U)
  poke(df.io.allocaRespIO.RouteID, 0.U)

  for( i <- 0 until 5){
    println(s"Output: ${peek(df.io.Out(0))}\n")
    println(s"t:${i} -------------------------")
    step(1)
  }

 }




class AllocaTests extends  FlatSpec with Matchers {
   implicit val p = new WithAccelConfig
  it should "Dataflow tester" in {
     chisel3.iotesters.Driver(() => new AllocaNode(ID = 0, NumOuts = 1, RouteID = 0)) {
       c => new AllocaTester(c)
     } should be(true)
   }
 }



