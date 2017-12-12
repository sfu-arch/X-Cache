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


class LoopRegTests(c: LoopElement) extends PeekPokeTester(c) {

  poke(c.io.inData.valid, false.B)
  poke(c.io.Finish, false.B)
  poke(c.io.inData.bits.data, 5.U)
//  poke(c.io.inData.bits.predicate, false.B)

  step(1)
  println(s"t: ${t} Output      : ${peek(c.io.outData)}")

  for(t <- 0 until 20){

    if(t == 3){
      poke(c.io.inData.valid, true.B)
// //       poke(c.io.inData.bits.valid, true.B)
    }

    if(t == 10){
      poke(c.io.Finish, true.B)
    }

    step(1)
    println(s"t: ${t} Output      : ${peek(c.io.outData)}")

  }

}



class LoopRegTester extends  FlatSpec with Matchers {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  it should "not do something stupid" in {
    // iotestester flags:
    // -ll  = log level <Error|Warn|Info|Debug|Trace>
    // -tbn = backend <firrtl|verilator|vcs>
    // -td  = target directory
    // -tts = seed for RNG
    chisel3.iotesters.Driver.execute(
      Array(
        //"-ll", "Info",
        "-tbn", "verilator",
        "-td", "test_run_dir",
        "-tts", "0001"),
      () => new LoopElement(ID = 1)){
      c => new LoopRegTests(c)
    } should be(true)
  }

}

