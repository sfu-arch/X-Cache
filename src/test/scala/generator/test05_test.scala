package dataflow

import chisel3._
import chisel3.Module
import org.scalatest.{FlatSpec, Matchers}
import dandelion.config._
import memory._
import dandelion.accel._
import dandelion.interfaces.NastiMemSlave
import helpers._


class test05Main(implicit p: Parameters) extends AccelIO(List(32), List(32)) {

  val cache = Module(new Cache) // Simple Nasti Cache
  //val memModel = Module(new NastiInitMemSlave()()) // Model of DRAM to connect to Cache
  val memModel = Module(new NastiMemSlave()) // Model of DRAM to connect to Cache


  // Connect the wrapper I/O to the memory model initialization interface so the
  // test bench can write contents at start.
  memModel.io.nasti <> cache.io.nasti
  memModel.io.init.bits.addr := 0.U
  memModel.io.init.bits.data := 0.U
  memModel.io.init.valid := false.B
  cache.io.cpu.abort := false.B

  // Wire up the cache and modules under test.
  val test05 = Module(new test05DF())

  //Put an arbiter infront of cache
  val CacheArbiter = Module(new MemArbiter(2))

  // Connect input signals to cache
  CacheArbiter.io.cpu.MemReq(0) <> test05.io.MemReq
  test05.io.MemResp <> CacheArbiter.io.cpu.MemResp(0)

  //Connect main module to cache arbiter
  CacheArbiter.io.cpu.MemReq(1) <> io.req
  io.resp <> CacheArbiter.io.cpu.MemResp(1)

  //Connect cache to the arbiter
  cache.io.cpu.req <> CacheArbiter.io.cache.MemReq
  CacheArbiter.io.cache.MemResp <> cache.io.cpu.resp

  //Connect in/out ports
  test05.io.in <> io.in
  io.out <> test05.io.out

}


class test05Test01[T <: AccelIO](c: T)
                                (inAddrVec: List[Int], inDataVec: List[Int],
                                 outAddrVec: List[Int], outDataVec: List[Int])
  extends AccelTesterLocal(c)(inAddrVec, inDataVec, outAddrVec, outDataVec) {



  //  val inAddrVec = List.range(0x0037957020, 0x000037957020 + (4 * 10), 4)
  val addr_range = 0x0

  initMemory()

  // Initializing the signals
  poke(c.io.in.bits.enable.control, false)
  poke(c.io.in.valid, false)
  poke(c.io.in.bits.data("field0").data, 0)
  poke(c.io.in.bits.data("field0").taskID, 0)
  poke(c.io.in.bits.data("field0").predicate, false)
  poke(c.io.out.ready, false)
  step(1)
  poke(c.io.in.bits.enable.control, true)
  poke(c.io.in.valid, true)
  poke(c.io.in.bits.data("field0").data, addr_range) // Array a[] base address
  poke(c.io.in.bits.data("field0").predicate, true)
  poke(c.io.out.ready, true)
  step(1)
  poke(c.io.in.bits.enable.control, false)
  poke(c.io.in.valid, false)
  poke(c.io.in.bits.data("field0").data, 0)
  poke(c.io.in.bits.data("field0").predicate, false)

  step(1)

  // NOTE: Don't use assert().  It seems to terminate the writing of VCD files
  // early (before the error) which makes debugging very difficult. Check results
  // using if() and fail command.
  var time = 0
  var result = false
  while (time < 500) {
    time += 1
    step(1)
    if (peek(c.io.out.valid) == 1 &&
      peek(c.io.out.bits.data("field0").predicate) == 1
    ) {
      result = true
      val data = peek(c.io.out.bits.data("field0").data)
      if (data != 8) {
        println(Console.RED + s"*** Incorrect result received. Got $data. Hoping for 8" + Console.RESET)
        fail
      } else {
        println(Console.BLUE + s"*** Correct return result received. Run time: $time cycles." + Console.RESET)
      }
    }
  }

  checkMemory()

  if (!result) {
    println(Console.RED + "*** Timeout." + Console.RESET)
    fail
  }
}


class test05Tester1 extends FlatSpec with Matchers {

  val addr_range = 0x0
  val inAddrVec = List.range(addr_range, addr_range + (4 * 10), 4)
  val inDataVec = List(0, 10, 2, 3, 4, 0, 0, 0, 0, 0)
  val outAddrVec = List.range(addr_range, addr_range + (4 * 10), 4)
  val outDataVec = inDataVec.zipWithIndex.map { case (a, b) => if (b < 5) a else inDataVec(b - 5) * 2 }


  implicit val p = Parameters.root((new MiniConfig).toInstance)
  it should "Check that test05 works correctly." in {
    // iotester flags:
    // -ll  = log level <Error|Warn|Info|Debug|Trace>
    // -tbn = backend <firrtl|verilator|vcs>
    // -td  = target directory
    // -tts = seed for RNG
    chisel3.iotesters.Driver.execute(
      Array(
        // "-ll", "Info",
        "-tn", "test05Main",
        "-tbn", "verilator",
        "-td", "test_run_dir/test05",
        "-tts", "0001"),
      () => new test05Main()) {
      c => new test05Test01(c)(inAddrVec, inDataVec, outAddrVec, outDataVec)
    } should be(true)
  }
}

