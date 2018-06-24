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

class test13MainIO(implicit val p: Parameters)  extends Module with CoreParams with CacheParams {
  val io = IO( new CoreBundle {
    val in = Flipped(Decoupled(new Call(List(32))))
    val addr = Input(UInt(nastiXAddrBits.W))
    val din  = Input(UInt(nastiXDataBits.W))
    val write = Input(Bool())
    val dout = Output(UInt(nastiXDataBits.W))
    val out = Decoupled(new Call(List(32)))

  })
}

class test13MainDirect(implicit p: Parameters) extends test13MainIO{

  val cache = Module(new Cache)            // Simple Nasti Cache
  val memModel = Module(new NastiMemSlave) // Model of DRAM to connect to Cache
  val memCopy = Mem(1024, UInt(32.W))      // Local memory just to keep track of writes to cache for validation

  // Store a copy of all data written to the cache.  This is done since the cache isn't
  // 'write through' to the memory model and we have no easy way of reading the
  // cache contents from the testbench.
  when(cache.io.cpu.req.valid && cache.io.cpu.req.bits.iswrite) {
    memCopy.write((cache.io.cpu.req.bits.addr>>2).asUInt(), cache.io.cpu.req.bits.data)
  }
  io.dout := memCopy.read((io.addr>>2).asUInt())

  // Connect the wrapper I/O to the memory model initialization interface so the
  // test bench can write contents at start.
  memModel.io.nasti <> cache.io.nasti
  memModel.io.init.bits.addr := io.addr
  memModel.io.init.bits.data := io.din
  memModel.io.init.valid := io.write
  cache.io.cpu.abort := false.B

  // Wire up the cache and modules under test.
  val test13 = Module(new test13DF())


  cache.io.cpu.req <> MemArbiter.io.cache.MemReq
  MemArbiter.io.cache.MemResp <> cache.io.cpu.resp


  MemArbiter.io.cpu.MemReq(0) <> test13_inner.io.MemReq
  test13_inner.io.MemResp <> MemArbiter.io.cpu.MemResp(0)

  MemArbiter.io.cpu.MemReq(1) <> test13.io.MemReq
  test13.io.MemResp <> MemArbiter.io.cpu.MemResp(1)

  test13.io.call_5_in <> test13_inner.io.out
  test13_inner.io.in <> test13.io.call_5_out

  test13.io.in <> io.in
  io.out <> test13.io.out

}



class test13Test01[T <: test13MainDirect](c: T) extends PeekPokeTester(c) {


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
//  poke(c.io.in.bits.data("field1").data, 0.U)
//  poke(c.io.in.bits.data("field1").predicate, false.B)
//  poke(c.io.in.bits.data("field2").data, 0.U)
//  poke(c.io.in.bits.data("field2").predicate, false.B)
  poke(c.io.out.ready, false.B)
  step(1)
  poke(c.io.in.bits.enable.control, true.B)
  poke(c.io.in.valid, true.B)
  poke(c.io.in.bits.data("field0").data, 10.U)
  poke(c.io.in.bits.data("field0").predicate, true.B)
//  poke(c.io.in.bits.data("field1").data, 4.U)
//  poke(c.io.in.bits.data("field1").predicate, true.B)
//  poke(c.io.in.bits.data("field2").data, 8.U)
//  poke(c.io.in.bits.data("field2").predicate, true.B)
  poke(c.io.out.ready, true.B)
  step(1)
  poke(c.io.in.bits.enable.control, false.B)
  poke(c.io.in.valid, false.B)
  poke(c.io.in.bits.data("field0").data, 0.U)
  poke(c.io.in.bits.data("field0").predicate, false.B)
//  poke(c.io.in.bits.data("field1").data, 0.U)
//  poke(c.io.in.bits.data("field1").predicate, false.B)
//  poke(c.io.in.bits.data("field2").data, 0.U)
//  poke(c.io.in.bits.data("field2").predicate, false.B)

  step(1)

  // NOTE: Don't use assert().  It seems to terminate the writing of VCD files
  // early (before the error) which makes debugging very difficult. Check results
  // using if() and fail command.
  var time = 1  //Cycle counter
  var result = false
  while (time < 400) {
    time += 1
    step(1)
    //println(s"Cycle: $time")
    if (peek(c.io.out.valid) == 1 &&
      peek(c.io.out.bits.data("field0").predicate) == 1){
      result = true
      val data = peek(c.io.out.bits.data("field0").data)
      if (data != 475) {
        println(Console.RED + s"[LOG] *** Incorrect result received. Got $data. Hoping for 1" + Console.RESET)
        fail
      } else {
        println(Console.BLUE + s"[LOG] *** Correct result received." + Console.RESET)
      }
    }
  }

  if(!result) {
    println("*** Timeout.")
    fail
  }

}

class test13Tester extends FlatSpec with Matchers {
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
     () => new test13MainDirect()) {
     c => new test13Test01(c)
    } should be(true)
  }
}

