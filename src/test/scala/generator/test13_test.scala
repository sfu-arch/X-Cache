package dataflow


import java.io.PrintWriter
import java.io.File

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

class test13MainIO(implicit val p: Parameters) extends Module with CoreParams with CacheParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32, 32, 32))))
    val req = Flipped(Decoupled(new MemReq))
    val resp = Output(Valid(new MemResp))
    val out = Decoupled(new Call(List(32)))
  })

  def cloneType = new test13MainIO().asInstanceOf[this.type]
}

class test13MainDirect(implicit p: Parameters) extends test13MainIO {

  val cache = Module(new Cache) // Simple Nasti Cache
  val memModel = Module(new NastiMemSlave) // Model of DRAM to connect to Cache

  // Connect the wrapper I/O to the memory model initialization interface so the
  // test bench can write contents at start.
  memModel.io.nasti <> cache.io.nasti
  memModel.io.init.bits.addr := 0.U
  memModel.io.init.bits.data := 0.U
  memModel.io.init.valid := false.B
  cache.io.cpu.abort := false.B


  // Wire up the cache and modules under test.
  //  val test04 = Module(new test04DF())
  val test13 = Module(new test13DF())

  //Put an arbiter infront of cache
  val CacheArbiter = Module(new MemArbiter(2))

  // Connect input signals to cache
  CacheArbiter.io.cpu.MemReq(0) <> test13.io.MemReq
  test13.io.MemResp <> CacheArbiter.io.cpu.MemResp(0)

  //Connect main module to cache arbiter
  CacheArbiter.io.cpu.MemReq(1) <> io.req
  io.resp <> CacheArbiter.io.cpu.MemResp(1)

  //Connect cache to the arbiter
  cache.io.cpu.req <> CacheArbiter.io.cache.MemReq
  CacheArbiter.io.cache.MemResp <> cache.io.cpu.resp

  //Connect in/out ports
  test13.io.in <> io.in
  io.out <> test13.io.out

  // Check if trace option is on or off
  if (p(TRACE) == false) {
    println(Console.RED + "****** Trace option is off. *********" + Console.RESET)
  }
  else
    println(Console.BLUE + "****** Trace option is on. *********" + Console.RESET)


}


class test13Test01[T <: test13MainDirect](c: T) extends PeekPokeTester(c) {

  def MemRead(addr: Int): BigInt = {
    while (peek(c.io.req.ready) == 0) {
      step(1)
    }
    poke(c.io.req.valid, 1)
    poke(c.io.req.bits.addr, addr)
    poke(c.io.req.bits.iswrite, 0)
    poke(c.io.req.bits.tag, 0)
    poke(c.io.req.bits.mask, 0)
    poke(c.io.req.bits.mask, -1)
    step(1)
    while (peek(c.io.resp.valid) == 0) {
      step(1)
    }
    val result = peek(c.io.resp.bits.data)
    result
  }

  def MemWrite(addr: Int, data: Int): BigInt = {
    while (peek(c.io.req.ready) == 0) {
      step(1)
    }
    poke(c.io.req.valid, 1)
    poke(c.io.req.bits.addr, addr)
    poke(c.io.req.bits.data, data)
    poke(c.io.req.bits.iswrite, 1)
    poke(c.io.req.bits.tag, 0)
    poke(c.io.req.bits.mask, 0)
    poke(c.io.req.bits.mask, -1)
    step(1)
    poke(c.io.req.valid, 0)
    1
  }

  def dumpMemory(path: String) = {
    //Writing mem states back to the file
    val pw = new PrintWriter(new File(path))
    for (i <- 0 until outDataVec.length) {
      val data = MemRead(outAddrVec(i))
      pw.write("0X" + outAddrVec(i).toHexString + " -> " + data + "\n")
    }
    pw.close

  }


  val addr_range = 0x0
  val inAddrVec = List.range(addr_range, addr_range + (4 * 24), 4)

  val inDataVec = List(0x0, 0x3f800000, 0x40000000, 0x40400000, 0x40800000,
    0x40a00000, 0x40c00000, 0x40e00000, 0x41000000, 0x41100000, 0x41200000,
    0x41300000, 0x41400000, 0x41500000, 0x41600000, 0x41700000, 0x41800000,
    0x41880000, 0x41900000, 0x41980000, 0x41a00000, 0x41a80000, 0x41b00000,
    0x41b80000)

  val outAddrVec = List.range(addr_range, addr_range + (4 * 24), 4)
  val outDataVec = List(0x0, 0x3db21643, 0x3e321643, 0x3e8590b2,
    0x3eb21643, 0x3ede9bd3, 0x3f0590b2, 0x3f1bd37a, 0x3f321643, 0x3f48590b,
    0x3f5e9bd3, 0x3f74de9c, 0x3f8590b2, 0x3f90b216, 0x3f9bd37a, 0x3fa6f4df,
    0x3fb21643, 0x3fbd37a7, 0x3fc8590b, 0x3fd37a6f, 0x3fde9bd3, 0x3fe9bd38,
    0x3ff4de9c, 0x40000000)


  //Write initial contents to the memory model.
  for (i <- 0 until inDataVec.length) {
    MemWrite(inAddrVec(i), inDataVec(i))
  }

  step(10)
  dumpMemory("memory.txt")

  step(1)


  /**
    * test11DF interface:
    *
    * in = Flipped(new CallDecoupled(List(...)))
    * out = new CallDecoupled(List(32))
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
  poke(c.io.in.bits.data("field1").data, 24.U)
  poke(c.io.in.bits.data("field1").predicate, true.B)
  poke(c.io.in.bits.data("field2").data, 0x41380000.U)
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
  var time = 1 //Cycle counter
  var result = false
  while (time < 6000) {
    time += 1
    step(1)
    //println(s"Cycle: $time")
    if (peek(c.io.out.valid) == 1 &&
      peek(c.io.out.bits.data("field0").predicate) == 1) {
      result = true
      val data = peek(c.io.out.bits.data("field0").data)
      if (data != 1102577664) {
        println(Console.RED + s"[LOG] *** Incorrect result received. Got $data. Hoping for 40000000" + Console.RESET)
        fail
      } else {
        println(Console.BLUE + s"[LOG] *** Correct result received." + Console.RESET)
      }
    }
  }

  if (!result) {
    println("*** Timeout.")
    fail
  }

  step(100)

  //  Peek into the CopyMem to see if the expected data is written back to the Cache
  var valid_data = true
  for (i <- 0 until outAddrVec.length) {
    val data = MemRead(outAddrVec(i))
    if (!(outDataVec(i).toInt - 2 < data) && (data < outDataVec(i).toInt + 2)) {
      println(Console.RED + s"*** Incorrect data received. Got $data. Hoping for ${outDataVec(i).toInt}" + Console.RESET)
      fail
      valid_data = false
    }
    else {
      println(Console.BLUE + s"[LOG] MEM[${outAddrVec(i).toInt}] :: $data" + Console.RESET)
    }
  }
  if (valid_data) {
    println(Console.BLUE + "*** Correct data written back." + Console.RESET)
    dumpMemory("memory.txt")
  }


  if (!result) {
    println(Console.RED + "*** Timeout." + Console.RESET)
    dumpMemory("memory.txt")
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

