/**
  * @author Amirali Sharifian
  */

package loop

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


class LoopTests(c: LoopHeader) extends PeekPokeTester(c) {

  poke(c.io.inputArg(0).valid, false)
  poke(c.io.inputArg(1).valid, false)
  poke(c.io.inputArg(0).bits.data, 0.U)
  poke(c.io.inputArg(1).bits.data, 0.U)
  poke(c.io.outputVal(0).ready, false)
  poke(c.io.outputVal(1).ready, false)

  poke(c.io.enable.bits, false)
  poke(c.io.enable.valid, false)

  step(1)
  println(s"t: ${t}, Output(0): ${peek(c.io.start)}\n")

  poke(c.io.inputArg(0).valid, false)
  poke(c.io.inputArg(1).valid, false)
  poke(c.io.enable.bits , true)
  poke(c.io.enable.valid, true)


  for(t <- 0 until 20){

    step(1)
    println(s"t: ${t}, Output(0): ${peek(c.io.outputVal(0))}")
    println(s"t: ${t}, Output(1): ${peek(c.io.outputVal(1))}")
    println(s"t: ${t} Start: ${peek(c.io.start)}")

    if(t == 5){
      poke(c.io.inputArg(0).valid, true)
      poke(c.io.inputArg(1).valid, true)
      poke(c.io.inputArg(0).bits.predicate, true)
      poke(c.io.inputArg(1).bits.predicate, true)
      poke(c.io.inputArg(0).bits.data, 5.U)
      poke(c.io.inputArg(1).bits.data, 8.U)
    }

    if(t == 3){
      poke(c.io.outputVal(0).ready, true)
      poke(c.io.outputVal(1).ready, true)
    }


  }
}



class LoopHeadTester extends  FlatSpec with Matchers {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  it should "not do something stupid" in {
    // iotestester flags:
    // -ll  = log level <Error|Warn|Info|Debug|Trace>
    // -tbn = backend <firrtl|verilator|vcs>
    // -td  = target directory
    // -tts = seed for RNG
    chisel3.iotesters.Driver.execute(
      Array(
//        "-ll", "Info",
        "-tbn", "verilator",
        "-td", "test_run_dir",
        "-tts", "0001"),
      () => new LoopHeader(NumInputs = 2, NumOuts = 2, ID = 0)(nodeOut = Seq(1,1))){
      c => new LoopTests(c)
    } should be(true)
  }

}

