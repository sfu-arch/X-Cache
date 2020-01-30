package dandelion.generator

import chisel3._
import chisel3.Module
import org.scalatest.{FlatSpec, Matchers}
import chipsalliance.rocketchip.config._
import dandelion.config._
import dandelion.memory._
import dandelion.accel._
import dandelion.interfaces.NastiMemSlave
import helpers._

class test13MainDirect(implicit p: Parameters) extends AccelIO(List(32, 32, 32), List(32)) {

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
  if (log == false) {
    println(Console.RED + "****** Trace option is off. *********" + Console.RESET)
  }
  else
    println(Console.BLUE + "****** Trace option is on. *********" + Console.RESET)


}


class test13Test01[T <: AccelIO](c: T)
                                (inAddrVec: List[Int], inDataVec: List[Int],
                                 outAddrVec: List[Int], outDataVec: List[Int])
  extends AccelTesterLocal(c)(inAddrVec, inDataVec, outAddrVec, outDataVec) {

  val addr_range = 0x0


  initMemory()

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
        println(Console.BLUE + s"*** Correct return result received. Run time: $time cycles." + Console.RESET)
      }
    }
  }

  if (!result) {
    println("*** Timeout.")
    fail
  }

  checkMemory()

  if (!result) {
    println(Console.RED + "*** Timeout." + Console.RESET)
    fail
  }

}

class test13Tester extends FlatSpec with Matchers {

  val addr_range = 0x0
  val inAddrVec = List.range(addr_range, addr_range + (4 * 24), 4)

  val inDataVec = List(0x0, 0x3f800000, 0x40000000, 0x40400000, 0x40800000,
    0x40a00000, 0x40c00000, 0x40e00000, 0x41000000, 0x41100000, 0x41200000,
    0x41300000, 0x41400000, 0x41500000, 0x41600000, 0x41700000, 0x41800000,
    0x41880000, 0x41900000, 0x41980000, 0x41a00000, 0x41a80000, 0x41b00000,
    0x41b80000)

  val outAddrVec = List.range(addr_range, addr_range + (4 * 24), 4)
  val outDataVec = List(0x0, 0x3db21643, 0x3e321643, 0x3e8590b3,
    0x3eb21643, 0x3ede9bd3, 0x3f0590b3, 0x3f1bd37b, 0x3f321643, 0x3f48590b,
    0x3f5e9bd3, 0x3f74de9b, 0x3f8590b3, 0x3f90b217, 0x3f9bd37b, 0x3fa6f4df,
    0x3fb21643, 0x3fbd37a7, 0x3fc8590b, 0x3fd37a6f, 0x3fde9bd3, 0x3fe9bd37,
    0x3ff4de9b, 0x40000000)


  implicit val p = new WithAccelConfig
  it should "Check that test11 works correctly." in {
    // iotester flags:
    // -ll  = log level <Error|Warn|Info|Debug|Trace>
    // -tbn = backend <firrtl|verilator|vcs>
    // -td  = target directory
    // -tts = seed for RNG
    chisel3.iotesters.Driver.execute(
      Array(
        "-ll", "Warn",
        "-tn", "test13Main",
        "-tbn", "verilator",
        "-td", "test_run_dir/test13",
        "-tts", "0001"),
      () => new test13MainDirect()) {
      c => new test13Test01(c)(inAddrVec, inDataVec, outAddrVec, outDataVec)
    } should be(true)
  }
}

