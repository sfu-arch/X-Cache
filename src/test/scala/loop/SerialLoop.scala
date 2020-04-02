// See LICENSE for license details.
package dandelion.loop

import chisel3._
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import org.scalatest.{Matchers, FlatSpec}

import chipsalliance.rocketchip.config._
import dandelion.config._


class LoopTests(c: LoopStart) extends PeekPokeTester(c) {

  poke(c.io.inputArg(0).valid, false)
  poke(c.io.inputArg(1).valid, false)
  poke(c.io.inputArg(0).bits.data, 0.U)
  poke(c.io.inputArg(1).bits.data, 0.U)

  poke(c.io.Finish(0).bits.control, false.B)
  poke(c.io.Finish(0).valid , false.B)
  poke(c.io.Finish(1).bits.control, false.B)
  poke(c.io.Finish(1).valid , false.B)

  poke(c.io.enableSignal(0).bits.control, false.B)
  poke(c.io.enableSignal(0).valid , false.B)
  poke(c.io.enableSignal(1).bits.control, false.B)
  poke(c.io.enableSignal(1).valid , false.B)

  poke(c.io.outputArg(0).ready, false.B)
  poke(c.io.outputArg(1).ready, false.B)


  step(1)
  println(s"t: ${t}, Output(0): ${peek(c.io.outputArg(0))}\n")
  println(s"t: ${t}, Output(1): ${peek(c.io.outputArg(1))}\n")

  for(t <- 0 until 20){

    step(1)

    println(s"t: ${t}, Output(0): ${peek(c.io.outputArg(0))}\n")
    println(s"t: ${t}, Output(1): ${peek(c.io.outputArg(1))}\n")

    if(t == 5){
      poke(c.io.inputArg(0).valid, true)
      poke(c.io.inputArg(1).valid, true)
      poke(c.io.inputArg(0).bits.predicate, true)
      poke(c.io.inputArg(1).bits.predicate, true)
      poke(c.io.inputArg(0).bits.data, 5.U)
      poke(c.io.inputArg(1).bits.data, 8.U)

      poke(c.io.enableSignal(0).valid, true.B)
      poke(c.io.enableSignal(1).valid, true.B)

      poke(c.io.Finish(0).valid , true.B)
      poke(c.io.Finish(1).valid , true.B)
    }

    if(t == 10){
      poke(c.io.Finish(0).bits.control, true)
      poke(c.io.Finish(0).valid, true)
      poke(c.io.Finish(1).bits.control, true)
      poke(c.io.Finish(0).valid, true)
    }


  }
}



class LoopHeadTester extends  FlatSpec with Matchers {
  implicit val p = new WithAccelConfig ++ new WithTestConfig
  it should "not do something stupid" in {
    // iotestester flags:
    // -ll  = log level <Error|Warn|Info|Debug|Trace>
    // -tbn = backend <firrtl|verilator|vcs>
    // -td  = target directory
    // -tts = seed for RNG
    chisel3.iotesters.Driver.execute(
      Array(
        // "-ll", "Info",
        "-tbn", "verilator",
        "-td", "test_run_dir",
        "-tts", "0001"),
      () => new LoopStart(NumInputs = 2, NumOuts = 2, ID = 0)){
      c => new LoopTests(c)
    } should be(true)
  }

}

