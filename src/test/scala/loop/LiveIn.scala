// See LICENSE for license details.
package dandelion.loop

import chisel3._
import chisel3.util._
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import org.scalatest.{Matchers, FlatSpec}

import chipsalliance.rocketchip.config._
import dandelion.config._
import dandelion.interfaces._





// Tester.
class LiveInTester(df: LiveInNode)
                  (implicit p: Parameters) extends PeekPokeTester(df)  {

  poke(df.io.InData.bits.data, 2.U)
// //   poke(df.io.InData.bits.valid, true.B)
  poke(df.io.InData.bits.predicate, false.B)

  poke(df.io.InData.valid, false.B)

  poke(df.io.enable.bits.control , false.B)
  poke(df.io.enable.valid, false.B)


  //poke(df.io.Finish.bits.control , false.B)
  //poke(df.io.Finish.valid, false.B)

  poke(df.io.Out(0).ready, false.B)

  println(s"Output: ${peek(df.io.Out(0))}\n")


  step(1)

  poke(df.io.enable.bits.control , false.B)
  poke(df.io.enable.valid, true.B)

  poke(df.io.Out(0).ready, true.B)

  println(s"Input : ${peek(df.io.InData)}\n")
  println(s"Output: ${peek(df.io.Out(0))}\n")
  step(1)

  poke(df.io.InData.valid, true.B)
  poke(df.io.InData.bits.predicate, true.B)

  println(s"Input : ${peek(df.io.InData)}\n")
  println(s"Output: ${peek(df.io.Out(0))}\n")

  println(s"t: -1\n -------------------------------------")
  step(1)


  for( i <- 0 until 10){
    println(s"Input : ${peek(df.io.InData)}\n")
    println(s"Output: ${peek(df.io.Out(0))}\n")

    println(s"t: ${i}\n -------------------------------------")
    step(1)
  }


  println(s"Make the enable valid")
  poke(df.io.enable.valid, false.B)
  poke(df.io.enable.bits.control, true.B)

  step(1)

  poke(df.io.enable.valid, true.B)
  poke(df.io.InData.valid, false.B)

  for( i <- 0 until 5){
    println(s"Input : ${peek(df.io.InData)}\n")
    println(s"Output: ${peek(df.io.Out(0))}\n")

    println(s"t: ${i}\n -------------------------------------")
    step(1)
  }

  poke(df.io.Out(0).ready, false.B)
  for( i <- 0 until 5){
    println(s"Input : ${peek(df.io.InData)}\n")
    println(s"Output: ${peek(df.io.Out(0))}\n")

    println(s"t: ${i}\n -------------------------------------")
    step(1)
  }

  poke(df.io.Out(0).ready, true.B)
  for( i <- 0 until 5){
    println(s"Input : ${peek(df.io.InData)}\n")
    println(s"Output: ${peek(df.io.Out(0))}\n")

    println(s"t: ${i}\n -------------------------------------")
    step(1)
  }

  poke(df.io.InData.bits.data, 7.U)
  //poke(df.io.Finish.bits.control , true.B)
  //poke(df.io.Finish.valid, true.B)
  for( i <- 0 until 5){
    println(s"Input : ${peek(df.io.InData)}\n")
    println(s"Output: ${peek(df.io.Out(0))}\n")

    println(s"t: ${i}\n -------------------------------------")
    step(1)
  }



 }


class LiveInTests extends  FlatSpec with Matchers {
   implicit val p = new WithAccelConfig
  it should "Dataflow tester" in {
    chisel3.iotesters.Driver.execute(
     Array(
       // "-ll", "Info",
       "-tbn", "verilator",
       "-td", "test_run_dir",
       "-tts", "0001"),
      () => new LiveInNode(NumOuts = 1, ID = 0)) {
       c => new LiveInTester(c)
     } should be(true)
   }
 }



