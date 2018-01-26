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

class test09CacheWrapper()(implicit p: Parameters) extends test09DF()(p)
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

class test09Test01(c: test09CacheWrapper) extends PeekPokeTester(c) {


  /**
  *  test09DF interface:
  *
  *    data_0 = Flipped(Decoupled(new DataBundle))
   *    val pred = Decoupled(new Bool())
   *    val start = Input(new Bool())
   *    val result = Decoupled(new DataBundle)
   */


  // Initializing the signals

  poke(c.io.in.enable.bits.control, false.B)
  poke(c.io.in.enable.valid, false.B)
  poke(c.io.in.data("field0").bits.data, 0.U)
  poke(c.io.in.data("field0").bits.predicate, false.B)
  poke(c.io.in.data("field0").valid, false.B)
  poke(c.io.out.data("field0").ready, false.B)
  poke(c.io.out.enable.ready, true.B)
  step(1)
  poke(c.io.in.enable.bits.control, true.B)
  poke(c.io.in.enable.valid, true.B)
  poke(c.io.in.data("field0").bits.data, 100.U)
  poke(c.io.in.data("field0").bits.predicate, true.B)
  poke(c.io.in.data("field0").valid, true.B)
  poke(c.io.out.data("field0").ready, true.B)
  poke(c.io.out.enable.ready, true.B)
  step(1)
  poke(c.io.in.enable.bits.control, false.B)
  poke(c.io.in.enable.valid, false.B)
  poke(c.io.in.data("field0").bits.data, 0.U)
  poke(c.io.in.data("field0").bits.predicate, false.B)
  poke(c.io.in.data("field0").valid, false.B)

  step(1)
  var time = 1  //Cycle counter
  var result = false
  while (time < 1000) {
    time += 1
    step(1)
    //println(s"Cycle: $time")
    if (peek(c.io.out.data("field0").valid) == 1 && peek(c.io.out.data("field0").bits.predicate) == 1) {
      result = true
      val data = peek(c.io.out.data("field0").bits.data)
      if (data != 5) {
        println(s"*** Incorrect result received. Got $data. Hoping for 5")
        fail
      } else {
        println("*** Correct result received.")
      }
    }
  }

  if(!result) {
    println("*** Timeout.")
    fail
  }

}

class test09Tester extends FlatSpec with Matchers {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  it should "Check that test09 works correctly." in {
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
     () => new test09CacheWrapper()) {
     c => new test09Test01(c)
    } should be(true)
  }
}

