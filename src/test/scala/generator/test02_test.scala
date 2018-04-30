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


class test02CacheWrapper()(implicit p: Parameters) extends test02DF()(p)
  with CacheParams {

  // Instantiate the AXI Cache
  val cache = Module(new Cache)
  cache.io.cpu.req <> CacheMem.io.MemReq
  CacheMem.io.MemResp <> cache.io.cpu.resp
  cache.io.cpu.abort := false.B
  // Instantiate a memory model with AXI slave interface for cache
  val memModel = Module(new NastiMemSlave)
  memModel.io.nasti <> cache.io.nasti

}

class test02Test01(c: test02CacheWrapper) extends PeekPokeTester(c) {


  /**
    * test02DF interface:
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
  poke(c.io.in.bits.data("field0").data, 8.U)
  poke(c.io.in.bits.data("field0").predicate, true.B)
  poke(c.io.in.bits.data("field1").data, 3.U)
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
      if (data != 11) {
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


}

class test02Tester extends FlatSpec with Matchers {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  it should "Check that test02 works correctly." in {
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
      () => new test02CacheWrapper()) {
      c => new test02Test01(c)
    } should be(true)
  }
}

