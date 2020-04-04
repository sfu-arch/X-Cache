package dandelion.accel


import java.io.PrintWriter
import java.io.File

import chisel3._
import chisel3.Module
import chisel3.iotesters._
import org.scalatest.{FlatSpec, Matchers}
import chipsalliance.rocketchip.config._
import dandelion.config._
import util._
import dandelion.interfaces._
import dandelion.memory._
import dandelion.dataflow.prefetchDF
import dandelion.memory.cache.{HasCacheAccelParams, ReferenceCache}


class prefetchDFMainIO(implicit val p: Parameters) extends Module with HasAccelParams
with HasAccelShellParams
  with HasCacheAccelParams{
  val io = IO( new Bundle {
    val in = Flipped(Decoupled(new Call(List(32))))
    val req = Flipped(Decoupled(new MemReq))
    val resp = Output(Valid(new MemResp))
    val out = Decoupled(new Call(List(32)))
  })
  def cloneType = new prefetchDFMainIO().asInstanceOf[this.type]
}

class prefetchDFMain(implicit p: Parameters) extends prefetchDFMainIO {

  val cache = Module(new ReferenceCache())            // Simple Nasti Cache
  val memModel = Module(new NastiMemSlave) // Model of DRAM to connect to Cache


  // Connect the wrapper I/O to the memory model initialization interface so the
  // test bench can write contents at start.
  memModel.io.nasti <> cache.io.mem
  memModel.io.init.bits.addr := 0.U
  memModel.io.init.bits.data := 0.U
  memModel.io.init.valid := false.B
  cache.io.cpu.abort := false.B


  // Wire up the cache and modules under test.
  val prefetch_dataflow = Module(new prefetchDF())

  //  prefetch_dataflow.io.PreReq.ready := true.B


  val CacheArbiter = Module(new MemArbiter(3))

  //Connection DF to cache arbiter
  CacheArbiter.io.cpu.MemReq(0) <> prefetch_dataflow.io.MemReq
  prefetch_dataflow.io.MemResp <> CacheArbiter.io.cpu.MemResp(0)

  //Connecting memory io to arbiter
  CacheArbiter.io.cpu.MemReq(1) <> io.req
  io.resp <> CacheArbiter.io.cpu.MemResp(1)

  CacheArbiter.io.cpu.MemReq(2) <> prefetch_dataflow.io.PreReq

  //Connection arbiter to cache
  cache.io.cpu.req <> CacheArbiter.io.cache.MemReq
  CacheArbiter.io.cache.MemResp <> cache.io.cpu.resp


  //Connecting module inputs to io
  prefetch_dataflow.io.in <> io.in
  io.out <> prefetch_dataflow.io.out

}


class basePrefetchTest01[T <: prefetchDFMainIO](c: T) extends PeekPokeTester(c) {


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
    poke(c.io.req.valid, 1.U)
    poke(c.io.req.bits.addr, addr.U)
    poke(c.io.req.bits.iswrite, 0.U)
    poke(c.io.req.bits.tag, 0.U)
    poke(c.io.req.bits.mask, 0.U)
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
    poke(c.io.req.valid, 1.U)
    poke(c.io.req.bits.addr, addr.U)
    poke(c.io.req.bits.data, data.U)
    poke(c.io.req.bits.iswrite, 1.U)
    poke(c.io.req.bits.tag, 0.U)
    poke(c.io.req.bits.mask, "hF".U((c.xlen/ 8).W))
    step(1)
    poke(c.io.req.valid, 0.U)
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

  val inAddrVec = List(0x0, 0x4, 0x8, 0xc, 0x10)
  val inDataVec = List(1, 20, 30, 40, 5)
  val outAddrVec = List(0x0, 0x4, 0x8, 0xc, 0x10)
  val outDataVec = List(1, 2, 3, 4, 5)


  var i = 0

  // Write initial contents to the memory model.
  for (i <- 0 until inDataVec.length) {
    MemWrite(inAddrVec(i), inDataVec(i))
  }
  step(10)


  // Initializing the signals

  //Field descriptions:
  // 1) field0 : BaseAddress
  // 2) field1 : index and a[i] = i
  // 3) field2 : true -> load, false -> store

  poke(c.io.in.valid, false.B)

  poke(c.io.in.bits.data("field0").data, 0.U)
  poke(c.io.in.bits.data("field0").predicate, false.B)
  poke(c.io.out.ready, false.B)
  step(1)

  poke(c.io.in.valid, true.B)
  poke(c.io.in.bits.enable.control, true.B)
  poke(c.io.in.bits.data("field0").data, 4.U)
  poke(c.io.in.bits.data("field0").predicate, true.B)
  poke(c.io.out.ready, true.B)
  step(1)

  poke(c.io.in.valid, false.B)
  poke(c.io.in.bits.enable.control, false.B)
  poke(c.io.in.bits.data("field0").data, 0.U)
  poke(c.io.in.bits.data("field0").predicate, false.B)
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
      if (data != 20) {
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

  step(1000)
  dumpMemory("final.mem")


}

class basePrefetchTester extends FlatSpec with Matchers {
  implicit val p = new WithAccelConfig ++ new WithTestConfig
  it should "Check that prefetchTest works correctly." in {
    // iotester flags:
    // -ll  = log level <Error|Warn|Info|Debug|Trace>
    // -tbn = backend <firrtl|verilator|vcs>
    // -td  = target directory
    // -tts = seed for RNG
    chisel3.iotesters.Driver.execute(
      Array(
        // "-ll", "Info",
        "-tbn", "verilator",
        "-td", "test_run_dir/prefetchTest",
        "-tts", "0001"),
      () => new prefetchDFMain()) {
      c => new basePrefetchTest01(c)
    } should be(true)
  }
}

