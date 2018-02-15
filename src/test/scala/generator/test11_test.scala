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

class test11CacheWrapper()(implicit p: Parameters) extends test11DF()(p)
  with CacheParams {
  /*  val io2 = IO(new Bundle {
      val init  = Flipped(Valid(new InitParams()(p)))
    })
    */
  // Instantiate the AXI Cache
  val cache = Module(new Cache)
  cache.io.cpu.req <> CacheMem.io.CacheReq
  CacheMem.io.CacheResp <> cache.io.cpu.resp
  cache.io.cpu.abort := false.B
  // Instantiate a memory model with AXI slave interface for cache
  val memModel = Module(new NastiMemSlave)
  memModel.io.nasti <> cache.io.nasti
  //memModel.io.init <> io2.init

  val raminit = RegInit(true.B)
  val addrVec = VecInit(0.U,1.U,2.U,3.U)
  val dataVec = VecInit(1.U,2.U,3.U,4.U)
  val (count_out, count_done) = Counter(raminit, addrVec.length)
  when (!count_done) {
    memModel.io.init.bits.addr := addrVec(count_out)
    memModel.io.init.bits.data := dataVec(count_out)
    memModel.io.init.valid := true.B
  }.otherwise {
    memModel.io.init.valid := false.B
    raminit := false.B
  }

}

abstract class test11MainIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32,32,32))))
    val out = Decoupled(new Call(List(32)))
  })
}

class test11Main(implicit p: Parameters) extends test11MainIO()(p) {

  val test11_add = Module(new test11_addDF())
  val test11 = Module(new test11CacheWrapper())

  test11.io.in <> io.in
  io.out <> test11.io.out

  test11_add.io.in <> test11.io.call8_out
  test11.io.call8_in <> test11_add.io.out

}

class test11Test01(c: test11Main) extends PeekPokeTester(c) {


  /**
  *  test11DF interface:
  *
  *    in = Flipped(new CallDecoupled(List(...)))
  *    out = new CallDecoupled(List(32))
  */

  // Initializing the signals
  poke(c.io.in.bits.enable.control, false.B)
  poke(c.io.in.valid, false.B)
  poke(c.io.in.bits.data("field0").data, 0.U)
  poke(c.io.in.bits.data("field0").predicate, false.B)
  poke(c.io.in.bits.data("field1").data, 0.U)
  poke(c.io.in.bits.data("field1").predicate, false.B)
  poke(c.io.in.bits.data("field2").data, 0.U)
  poke(c.io.in.bits.data("field2").predicate, false.B)
  poke(c.io.out.ready, false.B)
  step(1)
  poke(c.io.in.bits.enable.control, true.B)
  poke(c.io.in.valid, true.B)
  poke(c.io.in.bits.data("field0").data, 0.U)
  poke(c.io.in.bits.data("field0").predicate, true.B)
  poke(c.io.in.bits.data("field1").data, 4.U)
  poke(c.io.in.bits.data("field1").predicate, true.B)
  poke(c.io.in.bits.data("field2").data, 8.U)
  poke(c.io.in.bits.data("field2").predicate, true.B)
  poke(c.io.out.ready, true.B)
  step(1)
  poke(c.io.in.bits.enable.control, false.B)
  poke(c.io.in.valid, false.B)
  poke(c.io.in.bits.data("field0").data, 0.U)
  poke(c.io.in.bits.data("field0").predicate, false.B)
  poke(c.io.in.bits.data("field1").data, 0.U)
  poke(c.io.in.bits.data("field1").predicate, false.B)
  poke(c.io.in.bits.data("field2").data, 0.U)
  poke(c.io.in.bits.data("field2").predicate, false.B)

  step(1)

  // NOTE: Don't use assert().  It seems to terminate the writing of VCD files
  // early (before the error) which makes debugging very difficult. Check results
  // using if() and fail command.
  var time = 1  //Cycle counter
  var result = false
  while (time < 200) {
    time += 1
    step(1)
    //println(s"Cycle: $time")
    if (peek(c.io.out.valid) == 1 &&
      peek(c.io.out.bits.data("field0").predicate) == 1 &&
      peek(c.io.out.bits.enable.control) == 1) {
      result = true
      val data = peek(c.io.out.bits.data("field0").data)
      if (data != 1) {
        println(s"*** Incorrect result received. Got $data. Hoping for 1")
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

class test11Tester extends FlatSpec with Matchers {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  it should "Check that test11 works correctly." in {
    // iotester flags:
    // -ll  = log level <Error|Warn|Info|Debug|Trace>
    // -tbn = backend <firrtl|verilator|vcs>
    // -td  = target directory
    // -tts = seed for RNG
    chisel3.iotesters.Driver.execute(
     Array(
//       "-ll", "Info",
       "-tbn", "verilator",
       "-td", "test_run_dir",
       "-tts", "0001"),
     () => new test11Main()) {
     c => new test11Test01(c)
    } should be(true)
  }
}

