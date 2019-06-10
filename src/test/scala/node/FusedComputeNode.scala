// See LICENSE for license details.

package dandelion.node

import chisel3._

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import org.scalatest.{Matchers, FlatSpec}

import dandelion.config._



// Tester.
class ChainTester(df: Chain)
                  (implicit p: Parameters) extends PeekPokeTester(df)  {

  poke(df.io.In(0).bits.data, 9.U)
  poke(df.io.In(0).valid, false.B)
  poke(df.io.In(0).bits.predicate, false.B)

  poke(df.io.In(1).bits.data, 5.U)
  poke(df.io.In(1).valid, false.B)
  poke(df.io.In(1).bits.predicate, false.B)

  poke(df.io.In(2).bits.data, 10.U)
  poke(df.io.In(2).valid, false.B)
  poke(df.io.In(2).bits.predicate, false.B)

  poke(df.io.In(2).bits.data, 12.U)
  poke(df.io.In(2).valid, false.B)
  poke(df.io.In(2).bits.predicate, false.B)


  poke(df.io.enable.bits.control , false.B)
  poke(df.io.enable.valid, false.B)
  poke(df.io.Out(0).ready, false.B)
  poke(df.io.Out(1).ready, false.B)
  println(s"Output: ${peek(df.io.Out(0))}\n")


  step(1)

  poke(df.io.enable.bits.control , true.B)
  poke(df.io.enable.valid, true.B)
  poke(df.io.Out(0).ready, true.B)
  poke(df.io.Out(1).ready, true.B)

  poke(df.io.In(0).valid, true.B)
  poke(df.io.In(1).valid, true.B)
  poke(df.io.In(2).valid, true.B)

  poke(df.io.In(0).bits.predicate, true.B)
  poke(df.io.In(1).bits.predicate, true.B)
  poke(df.io.In(2).bits.predicate, true.B)
  

  println(s"Output: ${peek(df.io.Out(1))}\n")

  println(s"t: -1\n -------------------------------------")
  step(1)


  for( i <- 0 until 10){
    println(s"Output: ${peek(df.io.Out(1))}\n")

    println(s"t: ${i}\n -------------------------------------")
    step(1)
  }
}

	class FusedCompTests extends  FlatSpec with Matchers {
   implicit val p = Parameters.root((new MiniConfig).toInstance)
  it should "Dataflow tester" in {
     chisel3.iotesters.Driver(() => new Chain(NumOps = 2, ID = 0, OpCodes = Array("add","add"))(sign = false)) {
       c => new ChainTester(c)
     } should be(true)
   }
 }



