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

class test03MainIO(implicit val p: Parameters) extends Module with CoreParams with CacheParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32, 32))))
    val req = Flipped(Decoupled(new MemReq))
    val resp = Output(Valid(new MemResp))
    val out = Decoupled(new Call(List(32)))
  })

  def cloneType = new test03MainIO().asInstanceOf[this.type]
}

class test03Main(implicit p: Parameters) extends test03MainIO {

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
  val test03 = Module(new test03DF())

  //Put an arbiter infront of cache
  val CacheArbiter = Module(new MemArbiter(2))

  // Connect input signals to cache
  CacheArbiter.io.cpu.MemReq(0) <> test03.io.MemReq
  test03.io.MemResp <> CacheArbiter.io.cpu.MemResp(0)

  //Connect main module to cache arbiter
  CacheArbiter.io.cpu.MemReq(1) <> io.req
  io.resp <> CacheArbiter.io.cpu.MemResp(1)

  //Connect cache to the arbiter
  cache.io.cpu.req <> CacheArbiter.io.cache.MemReq
  CacheArbiter.io.cache.MemResp <> cache.io.cpu.resp

  //Connect in/out ports
  test03.io.in <> io.in
  io.out <> test03.io.out

  // Check if trace option is on or off
  if (p(TRACE) == false) {
    println(Console.RED + "****** Trace option is off. *********" + Console.RESET)
  }
  else
    println(Console.BLUE + "****** Trace option is on. *********" + Console.RESET)


}


//class test04Test01(c: test04CacheWrapper) extends PeekPokeTester(c) {
class test03Test01[T <: test03MainIO](c: T) extends PeekPokeTester(c) {

  poke(c.io.in.bits.enable.control, false.B)
  poke(c.io.in.bits.enable.taskID, 0.U)
  poke(c.io.in.valid, false.B)
  poke(c.io.in.bits.data("field0").data, 0.U)
  poke(c.io.in.bits.data("field0").predicate, false.B)
  poke(c.io.in.bits.data("field0").taskID, 0.U)
  poke(c.io.in.bits.data("field1").data, 0.U)
  poke(c.io.in.bits.data("field1").predicate, false.B)
  poke(c.io.in.bits.data("field1").taskID, 0.U)
  poke(c.io.out.ready, false.B)
  step(1)
  poke(c.io.in.bits.enable.control, true.B)
  poke(c.io.in.bits.enable.taskID, 3.U)
  poke(c.io.in.valid, true.B)
  poke(c.io.in.bits.data("field0").data, 50.U)
  poke(c.io.in.bits.data("field0").predicate, true.B)
  poke(c.io.in.bits.data("field0").taskID, 3.U)
  poke(c.io.in.bits.data("field1").data, 5.U)
  poke(c.io.in.bits.data("field1").predicate, true.B)
  poke(c.io.in.bits.data("field1").taskID, 3.U)
  poke(c.io.out.ready, true.B)
  step(1)
  poke(c.io.in.bits.enable.control, false.B)
  poke(c.io.in.valid, false.B)
  poke(c.io.in.bits.data("field0").data, 0.U)
  poke(c.io.in.bits.data("field0").predicate, false.B)
  poke(c.io.in.bits.data("field1").data, 0.U)
  poke(c.io.in.bits.data("field1").predicate, false.B)
  step(1)
  var time = 1 //Cycle counter
  var result = false
  while (time < 1500) {
    time += 1
    step(1)
    if (peek(c.io.out.valid) == 1 &&
      peek(c.io.out.bits.data("field0").predicate) == 1
    ) {
      result = true
      val data = peek(c.io.out.bits.data("field0").data)
      val expected = 225
      if (data != expected) {
        println(s"*** Incorrect result received. Got $data. Hoping for $expected")
        fail
      } else {
        println(Console.BLUE + s"*** Correct result received @ cycle: $time." + Console.RESET)
      }
    }
  }

  if (!result) {
    println("*** Timeout.")
    fail
  }

}

class test03Tester extends FlatSpec with Matchers {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  it should "Check that test04 works correctly." in {
    // iotester flags:
    // -ll  = log level <Error|Warn|Info|Debug|Trace>
    // -tbn = backend <firrtl|verilator|vcs>
    // -td  = target directory
    // -tts = seed for RNG
    chisel3.iotesters.Driver.execute(
      Array(
        // "-ll", "Info",
        "-tbn", "verilator",
        "-td", "test_run_dir/test04",
        "-tts", "0001"),
      () => new test03Main()) {
      c => new test03Test01(c)
    } should be(true)
  }
}

