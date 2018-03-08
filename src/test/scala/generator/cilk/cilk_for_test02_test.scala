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


class cilk_for_test02CacheWrapper()(implicit p: Parameters) extends cilk_for_test02DF()(p)
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

abstract class cilk_for_test02MainIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32,32))))
    val out = Decoupled(new Call(List(32)))
  })
}

class cilk_for_test02MainDirect(implicit p: Parameters) extends cilk_for_test02MainIO()(p) {

  val cilk_for_test02_mul = Module(new cilk_for_test02_mulDF())
  val cilk_for_test02 = Module(new cilk_for_test02CacheWrapper())

  cilk_for_test02.io.in <> io.in
  cilk_for_test02_mul.io.in <> cilk_for_test02.io.call5_out
  cilk_for_test02.io.call5_in <> cilk_for_test02_mul.io.out
  io.out <> cilk_for_test02.io.out

}

class cilk_for_test02MainTM(implicit p: Parameters) extends cilk_for_test02MainIO()(p) {

  val TaskControllerModule = Module(new TaskController(List(32,32), List(32), 1, 1))
  val cilk_for_test02_mul = Module(new cilk_for_test02_mulDF())
  val cilk_for_test02 = Module(new cilk_for_test02CacheWrapper())

  // tester to cilk_for_test02
  cilk_for_test02.io.in <> io.in

  // cilk_for_test02 to task controller
  TaskControllerModule.io.parentIn(0) <> cilk_for_test02.io.call5_out

  // task controller to sub-task cilk_for_test02_mul
  cilk_for_test02_mul.io.in <> TaskControllerModule.io.childOut(0)

  // sub-task to task controller
  TaskControllerModule.io.childIn(0) <> cilk_for_test02_mul.io.out

  // Task controller to cilk_for_test02
  cilk_for_test02.io.call5_in <> TaskControllerModule.io.parentOut(0)

  // cilk_for_test02 to tester
  io.out <> cilk_for_test02.io.out

}

class cilk_for_test02Test01[T <: cilk_for_test02MainIO](c: T) extends PeekPokeTester(c) {


  /**
  *  cilk_for_test02DF interface:
  *
  *    in = Flipped(Decoupled(new Call(List(...))))
  */


  // Initializing the signals
  poke(c.io.in.bits.enable.control, false.B)
  poke(c.io.in.valid, false.B)
  poke(c.io.in.bits.data("field0").data, 0.U)
  poke(c.io.in.bits.data("field0").predicate, false.B)
  poke(c.io.in.bits.data("field1").data, 0.U)
  poke(c.io.in.bits.data("field1").predicate, false.B)
  poke(c.io.out.ready, false.B)
  step(1)
  poke(c.io.in.bits.enable.control, true.B)
  poke(c.io.in.valid, true.B)
  poke(c.io.in.bits.data("field0").data, 4.U)
  poke(c.io.in.bits.data("field0").predicate, false.B)
  poke(c.io.in.bits.data("field1").data, 8.U)
  poke(c.io.in.bits.data("field1").predicate, false.B)
  poke(c.io.out.ready, true.B)
  step(1)
  poke(c.io.in.bits.enable.control, false.B)
  poke(c.io.in.valid, false.B)
  poke(c.io.in.bits.data("field0").data, 0.U)
  poke(c.io.in.bits.data("field0").predicate, false.B)
  poke(c.io.in.bits.data("field1").data, 0.U)
  poke(c.io.in.bits.data("field1").predicate, false.B)

  var time = 1  //Cycle counter
  var result = false
  while (time < 200) {
    time += 1
    step(1)
//    println(s"[INFO] Cycle: $time")
    if (peek(c.io.out.valid) == 1 &&
      peek(c.io.out.bits.data("field0").predicate) == 1
      ) {
      result = true
      val data = peek(c.io.out.bits.data("field0").data)
      if (data != 1) {
        println(s"*** Incorrect result received. Got $data. Hoping for 1")
        fail
      } else {
        println(s"*** Correct result received.")
      }
    }
  }

  if(!result) {
    println("*** Timeout.")
    fail
  }
}

class cilk_for_test02Tester1 extends FlatSpec with Matchers {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
    // iotester flags:
    // -ll  = log level <Error|Warn|Info|Debug|Trace>
    // -tbn = backend <firrtl|verilator|vcs>
    // -td  = target directory
    // -tts = seed for RNG
  it should "Check that cilk_for_test02 works when called directly." in {
    chisel3.iotesters.Driver.execute(
      Array(
        // "-ll", "Info",
        "-tbn", "verilator",
        "-td", "test_run_dir",
        "-tts", "0001"),
      () => new cilk_for_test02MainDirect()) {
      c => new cilk_for_test02Test01(c)
    } should be(true)
  }
}

class cilk_for_test02Tester2 extends FlatSpec with Matchers {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  // iotester flags:
  // -ll  = log level <Error|Warn|Info|Debug|Trace>
  // -tbn = backend <firrtl|verilator|vcs>
  // -td  = target directory
  // -tts = seed for RNG
  it should "Check that cilk_for_test02 works when called via task manager." in {
    chisel3.iotesters.Driver.execute(
      Array(
        // "-ll", "Info",
        "-tbn", "verilator",
        "-td", "test_run_dir",
        "-tts", "0001"),
      () => new cilk_for_test02MainTM()) {
      c => new cilk_for_test02Test01(c)
    } should be(true)
  }
}
