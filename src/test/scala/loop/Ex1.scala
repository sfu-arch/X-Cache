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


class LoopEx1(c: LoopExample) extends PeekPokeTester(c) {

  poke(c.io.Input1.valid, false)
  poke(c.io.Input2.valid, false)
  poke(c.io.Input3.valid, false)
  poke(c.io.Input4.valid, false)

  poke(c.io.Input1.bits.data, 3.U)
  poke(c.io.Input2.bits.data, 5.U)
  poke(c.io.Input3.bits.data, 7.U)
  poke(c.io.Input4.bits.data, 2.U)

  poke(c.io.Enable.valid, false)
  poke(c.io.Enable.bits, true)
  poke(c.io.Result.ready, false)

  poke(c.io.Finish, false.B)

//  step(1)
//  println(s"t: ${t}, Output(0): ${peek(c.io.Start)}\n")
//
//  poke(c.io.inputArg(0).valid, false)
//  poke(c.io.inputArg(1).valid, false)


  for(t <- 0 until 20){

    step(1)
    println(s"t: ${t}, Output(0): ${peek(c.io.Result)}")

    if(t == 1){
      poke(c.io.Enable.valid, true)
    }

    if(t == 5){
      poke(c.io.Input1.valid, true)
      poke(c.io.Input2.valid, true)
      poke(c.io.Input1.bits.predicate, true)
      poke(c.io.Input2.bits.predicate, true)
      poke(c.io.Input3.valid, true)
      poke(c.io.Input4.valid, true)
      poke(c.io.Input3.bits.predicate, true)
      poke(c.io.Input4.bits.predicate, true)
    }

//    if(t == 10){
//      poke(c.io.Finish, true)
//    }


  }
}



class LoopEx1Tester extends  FlatSpec with Matchers {
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
      () => new LoopExample(NumInputs = 4, ID = 0)){
      c => new LoopEx1(c)
    } should be(true)
  }

}

