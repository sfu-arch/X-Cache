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
import dandelion.control._
import util._
import dandelion.interfaces._
import regfile._
import dandelion.memory._
import dandelion.memory.stack._
import dandelion.arbiters._
import dandelion.loop._
import dandelion.accel._
import dandelion.junctions._


class SuperCacheDFMainIO(implicit val p: Parameters) extends Module with HasAccelParams with CacheParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32, 32, 32))))
    val req = Flipped(Decoupled(new MemReq))
    val resp = Output(Valid(new MemResp))
    val out = Decoupled(new Call(List(32)))
  })

  def cloneType = new SuperCacheDFMainIO().asInstanceOf[this.type]
}

class SuperCacheDFMain(implicit p: Parameters) extends SuperCacheDFMainIO {

  val cache = Module(new NCache(2, 2)) // Simple Nasti Cache
  val memModel = Module(new NastiMemSlave(latency = 5)) // Model of DRAM to connect to Cache


  // Connect the wrapper I/O to the memory model initialization interface so the
  // test bench can write contents at start.
  memModel.io.nasti <> cache.io.nasti
  memModel.io.init.bits.addr := 0.U
  memModel.io.init.bits.data := 0.U
  memModel.io.init.valid := false.B


  // Wire up the cache and modules under test.
  val cache_dataflow = Module(new cacheDF())

  //Connection DF to cache arbiter
  cache.io.cpu.MemReq(0) <> cache_dataflow.io.MemReq
  cache_dataflow.io.MemResp <> cache.io.cpu.MemResp(0)

  //Connecting memory io to arbiter
  cache.io.cpu.MemReq(1) <> io.req
  io.resp <> cache.io.cpu.MemResp(1)

  //Connecting module inputs to io
  cache_dataflow.io.in <> io.in
  io.out <> cache_dataflow.io.out

}


class SuperCacheTest01[T <: SuperCacheDFMainIO](c: T) extends PeekPokeTester(c) {


  /**
    * cacheDF interface:
    *
    * in = Flipped(Decoupled(new Call(List(...))))
    * out = Decoupled(new Call(List(32)))
    */


  /**
    * cacheDF interface:
    *
    * in = Flipped(Decoupled(new Call(List(...))))
    * out = Decoupled(new Call(List(32)))
    */

  var writesync = 0

  def MemReadBlocking(addr: Int): BigInt = {
    while (peek(c.io.req.ready) == 0) {
      step(1)
    }
    poke(c.io.req.valid, 1)
    poke(c.io.req.bits.addr, addr)
    poke(c.io.req.bits.iswrite, 0)
    poke(c.io.req.bits.tag, 10)
    poke(c.io.req.bits.mask, (1 << (c.io.req.bits.mask.getWidth)) - 1)
    step(1)
    poke(c.io.req.valid, 0)
    while (peek(c.io.resp.valid) == 0) {
      step(1)
    }
    val result = peek(c.io.resp.bits.data)
    result
  }

  def MemWriteNonBlocking(addr: Int, data: Int, tag: Int) = {
    while (peek(c.io.req.ready) == 0) {
      step(1)
      MemWriteSyncCount()
    }
    poke(c.io.req.valid, 1)
    poke(c.io.req.bits.addr, addr)
    poke(c.io.req.bits.data, data)
    poke(c.io.req.bits.iswrite, 1)
    poke(c.io.req.bits.tag, 20 + tag)
    //    [HACK]. Chisel and/or Verilator has a bug where it does not model 8 bit values correctly.
    // If you set this to a value exceeding the bitwidth; it will overwrite the neighboring fields in the msg.
    poke(c.io.req.bits.mask, (1 << (c.io.req.bits.mask.getWidth)) - 1)
    //poke(c.io.req.bits.mask, -1)
    step(1)
    MemWriteSyncCount()
    poke(c.io.req.valid, 0)
  }


  def MemWriteSyncCount() = {
    if (peek(c.io.resp.valid) == 1) {
      writesync += 1
    }
  }


  def dumpMemory(path: String) = {
    //Writing mem states back to the file
    val pw = new PrintWriter(new File(path))
    for (i <- 0 until outDataVec.length) {
      //      for (i <- 0 until outDataVec.length) {
      val data = MemReadBlocking(outAddrVec(i))
      pw.write("0X" + outAddrVec(i).toHexString + " -> " + data + "\n")
    }
    pw.close

  }


  val inAddrVec = List(0x0, 0xB000, 0xA000, 0xD000)
  val inDataVec = List(10, 20, 30, 40)
  val outAddrVec = List(0x0, 0xB000, 0xA000, 0xD000)
  val outDataVec = List(10, 20, 30, 40)


  var i = 0

  // Write initial contents to the memory model.
  for (i <- 0 until inDataVec.length) {
    MemWriteNonBlocking(inAddrVec(i), inDataVec(i), i)
  }
  step(200)

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
  poke(c.io.in.bits.data("field0").data, 0.U)
  poke(c.io.in.bits.data("field0").predicate, true.B)
  poke(c.io.in.bits.data("field1").data, 2.U)
  poke(c.io.in.bits.data("field1").predicate, true.B)
  poke(c.io.in.bits.data("field2").data, false.B)
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
      if (data != 0) {
        println(Console.RED + s"*** Incorrect result received. Got $data. Hoping for 0")
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

  //dumpMemory("final.mem")

}

class SuperCacheDFTester extends FlatSpec with Matchers {
  implicit val p = new WithAccelConfig
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
      () => new SuperCacheDFMain()) {
      c => new SuperCacheTest01(c)
    } should be(true)
  }
}
