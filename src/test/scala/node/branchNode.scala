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
class BranchTester(df: CBranchNode)(implicit p: Parameters) extends PeekPokeTester(df)  {

  poke(df.io.CmpIO.bits.data, 9.U)
  poke(df.io.CmpIO.valid, false.B)
  poke(df.io.CmpIO.bits.predicate, false.B)

  poke(df.io.enable.bits.control , false.B)
  poke(df.io.enable.valid, false.B)
  poke(df.io.Out(0).ready, false.B)
  println(s"Output: ${peek(df.io.Out(0))}\n")
  println(s"Output: ${peek(df.io.Out(1))}\n")


  step(1)

  poke(df.io.enable.bits.control , true.B)
  poke(df.io.enable.valid, true.B)
  poke(df.io.Out(0).ready, true.B)


  poke(df.io.CmpIO.valid, true.B)
  poke(df.io.CmpIO.bits.predicate, true.B)

  println(s"Output: ${peek(df.io.Out(0))}\n")
  println(s"Output: ${peek(df.io.Out(1))}\n")
  step(1)


  for( i <- 0 until 10){
    println(s"Output: ${peek(df.io.Out(0))}\n")
    println(s"Output: ${peek(df.io.Out(1))}\n")
    step(1)
  }
 }




class BrTests extends  FlatSpec with Matchers {
   implicit val p = new WithAccelConfig ++ new WithTestConfig
  it should "Dataflow tester" in {
     chisel3.iotesters.Driver(() => new CBranchNode(ID = 0)) {
       c => new BranchTester(c)
     } should be(true)
   }
 }



