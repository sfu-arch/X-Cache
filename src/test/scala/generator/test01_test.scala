package dataflow

import chisel3._
import chisel3.util._
import chisel3.Module
import chisel3.testers._
import chisel3.iotesters._
import org.scalatest.{FlatSpec, Matchers}
import muxes._
import config._
import control._
import util._
import interfaces._
import regfile._
import memory._
import stack._
import arbiters._
import loop._
import accel._
import node._
import junctions._


class test01CacheWrapper()(implicit p: Parameters) extends test01DF()(p)
  with CacheParams {

  // Instantiate the AXI Cache
  val cache = Module(new Cache)
  cache.io.cpu.req <> CacheMem.io.CacheReq
  CacheMem.io.CacheResp <> cache.io.cpu.resp
  cache.io.cpu.abort := false.B
  // Instantiate a memory model with AXI slave interface for cache
  val memModel = Module(new NastiMemSlave)
  memModel.io.nasti <> cache.io.nasti

}

class test01Test01(c: test01CacheWrapper) extends PeekPokeTester(c) {


  /**
    * test01DF interface:
    *
    * in = Flipped(Decoupled(new Call(List(...))))
    * out = Decoupled(new Call(List(32)))
    */


  // Initializing the signals

  poke(c.io.in.valid, false.B)

  poke(c.io.in.bits.data("field0").data, 0.U)
  poke(c.io.in.bits.data("field0").predicate, false.B)
  poke(c.io.in.bits.data("field1").data, 0.U)
  poke(c.io.in.bits.data("field1").predicate, false.B)
  poke(c.io.out.ready, false.B)
  step(1)

  poke(c.io.in.valid, true.B)
  poke(c.io.in.bits.enable.control, true.B)
  poke(c.io.in.bits.data("field0").data, 3.U)
  poke(c.io.in.bits.data("field0").predicate, true.B)
  poke(c.io.in.bits.data("field1").data, 5.U)
  poke(c.io.in.bits.data("field1").predicate, true.B)
  poke(c.io.out.ready, true.B)
  step(1)

  poke(c.io.in.valid, false.B)
  poke(c.io.in.bits.enable.control, false.B)
  poke(c.io.in.bits.data("field0").data, 0.U)
  poke(c.io.in.bits.data("field0").predicate, false.B)
  poke(c.io.in.bits.data("field1").data, 0.U)
  poke(c.io.in.bits.data("field1").predicate, false.B)
  poke(c.io.out.ready, true.B)
  step(1)

  var time = 1
  var result = false

  while (time < 100) {
    time += 1
    step(1)
    //println(s"Cycle: $time")
    if (peek(c.io.out.valid) == 1 &&
      peek(c.io.out.bits.data("field0").predicate) == 1) {
      result = true
      val data = peek(c.io.out.bits.data("field0").data)
      if (data != 15) {
        println(Console.RED + s"*** Incorrect result received. Got $data. Hoping for 105")
        fail
      } else {
        println(Console.BLUE + s"*** Correct result received @ cycle: $time.")
      }
    }
  }
  if (!result) {
    println("*** Timeout.")
    fail
  }


  /**
    *
    * @todo Add your test cases here
    *
    *       The test harness API allows 4 interactions with the DUT:
    *  1. To set the DUT'S inputs: poke
    *  2. To look at the DUT'S outputs: peek
    *  3. To test one of the DUT's outputs: expect
    *  4. To advance the clock of the DUT: step
    *
    *       Conditions:
    *  1. while(peek(c.io.XXX) == UInt(0))
    *  2. for(i <- 1 to 10)
    *  3. for{ i <- 1 to 10
    *       j <- 1 to 10
    *       }
    *
    *       Print Statement:
    *       println(s"Waited $count cycles on gcd inputs $i, $j, giving up")
    *
    */

}

class test01Tester extends FlatSpec with Matchers {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  it should "Check that test01 works correctly." in {
    // iotester flags:
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
      () => new test01CacheWrapper()) {
      c => new test01Test01(c)
    } should be(true)
  }
}

