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

class test10CacheWrapper()(implicit p: Parameters) extends test10DF()(p)
  with CacheParams {

  // Instantiate the AXI Cache
  val cache = Module(new Cache)
  //cache.io.cpu.req <> io.CacheReq
  CacheMem.io.CacheResp <> cache.io.cpu.resp
  cache.io.cpu.abort := false.B
  // Instantiate a memory model with AXI slave interface for cache
  val memModel = Module(new NastiMemSlave)
  memModel.io.nasti <> cache.io.nasti

}

class test10Test01(c: test10CacheWrapper) extends PeekPokeTester(c) {


  /**
  *  test10DF interface:
  *
  *    data_0 = Flipped(Decoupled(new DataBundle))
   *    val pred = Decoupled(new Bool())
   *    val start = Input(new Bool())
   *    val result = Decoupled(new DataBundle)
   */


  // Initializing the signals

  poke(c.io.entry.bits.control, false.B)
  poke(c.io.entry.valid, false.B)

  poke(c.io.data_0.bits.data, 0.U)
  poke(c.io.data_0.bits.predicate, false.B)
  poke(c.io.data_0.valid, false.B)
  poke(c.io.data_1.bits.data, 0.U)
  poke(c.io.data_1.bits.predicate, false.B)
  poke(c.io.data_1.valid, false.B)
  poke(c.io.data_2.bits.data, 0.U)
  poke(c.io.data_2.bits.predicate, false.B)
  poke(c.io.data_2.valid, false.B)

  poke(c.io.result.ready, false.B)


  step(1)
  poke(c.io.entry.bits.control, true.B)
  poke(c.io.entry.valid, true.B)
  poke(c.io.data_0.bits.data, 0.U)
  poke(c.io.data_0.bits.predicate, true.B)
  poke(c.io.data_0.valid, true.B)
  poke(c.io.data_1.bits.data, 4.U)
  poke(c.io.data_1.bits.predicate, true.B)
  poke(c.io.data_1.valid, true.B)
  poke(c.io.data_2.bits.data, 8.U)
  poke(c.io.data_2.bits.predicate, true.B)
  poke(c.io.data_2.valid, true.B)
  poke(c.io.result.ready, true.B)
  step(1)
  poke(c.io.entry.bits.control, false.B)
  poke(c.io.entry.valid, false.B)
  poke(c.io.data_0.bits.data, 0.U)
  poke(c.io.data_0.bits.predicate, false.B)
  poke(c.io.data_0.valid, false.B)
  poke(c.io.data_1.bits.data, 0.U)
  poke(c.io.data_1.bits.predicate, false.B)
  poke(c.io.data_1.valid, false.B)
  poke(c.io.data_2.bits.data, 0.U)
  poke(c.io.data_2.bits.predicate, false.B)
  poke(c.io.data_2.valid, false.B)

  step(1)
  var time = 1  //Cycle counter
  var result = false
  while (time < 150) {
    time += 1
    step(1)
    println(s"Cycle: $time")
    if (peek(c.io.result.valid) == 1 && peek(c.io.result.bits.predicate) == 1) {
      result = true
      assert(peek(c.io.result.bits.data) == 105, "Incorrect result received.")
    }
  }

  assert(result, "*** Timeout.")
}

class test10Tester extends FlatSpec with Matchers {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  it should "Check that test10 works correctly." in {
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
     () => new test10CacheWrapper()) {
     c => new test10Test01(c)
    } should be(true)
  }
}

