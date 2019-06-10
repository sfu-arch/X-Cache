package cache


import java.io.PrintWriter
import java.io.File
import chisel3._
import chisel3.util._
import chisel3.Module
import chisel3.testers._
import chisel3.iotesters._
import org.scalatest.{FlatSpec, Matchers}
import muxes._
import dandelion.config._
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
import dandelion.junctions._


class cacheDFMainIO(implicit val p: Parameters) extends Module with CoreParams with CacheParams {
  val io = IO(new Bundle {
    val in   = Flipped(Decoupled(new Call(List(32, 32, 32))))
    val req  = Flipped(Decoupled(new MemReq))
    val resp = Output(Valid(new MemResp))
    val out  = Decoupled(new Call(List(32)))
  })
  def cloneType = new cacheDFMainIO().asInstanceOf[this.type]
}

class cacheDFMain(implicit p: Parameters) extends cacheDFMainIO {

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
  val cache_dataflow = Module(new cacheDF( ))

  val CacheArbiter = Module(new MemArbiter(2))

  //Connection DF to cache arbiter
  CacheArbiter.io.cpu.MemReq(0) <> cache_dataflow.io.MemReq
  cache_dataflow.io.MemResp <> CacheArbiter.io.cpu.MemResp(0)

  //Connecting memory io to arbiter
  CacheArbiter.io.cpu.MemReq(1) <> io.req
  io.resp <> CacheArbiter.io.cpu.MemResp(1)

  //Connection arbiter to cache
  cache.io.cpu.req <> CacheArbiter.io.cache.MemReq
  CacheArbiter.io.cache.MemResp <> cache.io.cpu.resp


  //Connecting module inputs to io
  cache_dataflow.io.in <> io.in
  io.out <> cache_dataflow.io.out

}


class basecacheTest01[T <: cacheDFMainIO](c: T) extends PeekPokeTester(c) {


  /**
    * cacheDF interface:
    *
    * in = Flipped(Decoupled(new Call(List(...))))
    * out = Decoupled(new Call(List(32)))
    */

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
      //      for (i <- 0 until outDataVec.length) {
      val data = MemRead(outAddrVec(i))
      pw.write("0X" + outAddrVec(i).toHexString + " -> " + data + "\n")
    }
    pw.close

  }

  val inAddrVec  = List(0x0, 0x4, 0x8, 0xc, 0x10)
  val inDataVec  = List(1, 20, 30, 40, 5)
  val outAddrVec = List(0x0, 0x4, 0x8, 0xc, 0x10)
  val outDataVec = List(1, 2, 3, 4, 5)


  var i = 0

  // Write initial contents to the memory model.
  for (i <- 0 until inDataVec.length) {
    MemWrite(inAddrVec(i), inDataVec(i))
  }
  step(1)

  dumpMemory("init.mem")


  // Initializing the signals

  //Field descriptions:
  // 1) field0 : BaseAddress
  // 2) field1 : index and a[i] = i
  // 3) field2 : true -> load, false -> store

  poke(c.io.in.valid, false.B)

  poke(c.io.in.bits.data("field0").data, 0.U)
  poke(c.io.in.bits.data("field0").predicate, false.B)
  poke(c.io.in.bits.data("field1").data, 0.U)
  poke(c.io.in.bits.data("field1").predicate, false.B)
  poke(c.io.in.bits.data("field2").data, false.B)
  poke(c.io.in.bits.data("field2").predicate, false.B)
  poke(c.io.out.ready, false.B)
  step(1)

  poke(c.io.in.valid, true.B)
  poke(c.io.in.bits.enable.control, true.B)
  poke(c.io.in.bits.data("field0").data, 0.U)   // Address of %a
  poke(c.io.in.bits.data("field0").predicate, true.B)
  poke(c.io.in.bits.data("field1").data, 2.U)  // index of a, %i
  poke(c.io.in.bits.data("field1").predicate, true.B)
  poke(c.io.in.bits.data("field2").data, 1.U) // Flag: Read(1), Write(0)
  poke(c.io.in.bits.data("field2").predicate, true.B)
  poke(c.io.out.ready, true.B)
  step(1)

  poke(c.io.in.valid, false.B)
  poke(c.io.in.bits.enable.control, false.B)
  poke(c.io.in.bits.data("field0").data, 0.U)
  poke(c.io.in.bits.data("field0").predicate, false.B)
  poke(c.io.in.bits.data("field1").data, 0.U)
  poke(c.io.in.bits.data("field1").predicate, false.B)
  poke(c.io.in.bits.data("field2").data, false.B)
  poke(c.io.in.bits.data("field2").predicate, false.B)
  poke(c.io.out.ready, true.B)
  step(1)

  var time   = 1
  var result = false

  while (time < 100) {
    time += 1
    step(1)
    //println(s"Cycle: $time")
    if (peek(c.io.out.valid) == 1 &&
      peek(c.io.out.bits.data("field0").predicate) == 1) {
      result = true
      val data = peek(c.io.out.bits.data("field0").data)
      if (data != 30 ) {
        println(Console.RED + s"*** Incorrect result received. Got $data. Hoping for 0")
        fail
      } else {
        println(Console.BLUE + s"*** Correct result received @ cycle: $time, MEM[2] = 30.")
      }
    }
  }
  if (!result) {
    println("*** Timeout.")
    fail
  }

  step(100)
  dumpMemory("final.mem")


}

class baseCacheTester extends FlatSpec with Matchers {
  implicit val p = Parameters.root((new MiniConfig).toInstance)
  it should "Check that cacheTest works correctly." in {
    // iotester flags:
    // -ll  = log level <Error|Warn|Info|Debug|Trace>
    // -tbn = backend <firrtl|verilator|vcs>
    // -td  = target directory
    // -tts = seed for RNG
    chisel3.iotesters.Driver.execute(
      Array(
        // "-ll", "Info",
        "-tbn", "verilator",
        "-td", "test_run_dir/cacheTest",
        "-tts", "0001"),
      () => new cacheDFMain( )) {
      c => new basecacheTest01(c)
    } should be(true)
  }
}
