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


class fibMainIO(implicit val p: Parameters)  extends Module with CoreParams with CacheParams {
  val io = IO( new CoreBundle {
    val in = Flipped(Decoupled(new Call(List(32,32))))
    val addr = Input(UInt(nastiXAddrBits.W))
    val din  = Input(UInt(nastiXDataBits.W))
    val write = Input(Bool())
    val dout = Output(UInt(nastiXDataBits.W))
    val out = Decoupled(new Call(List(32)))
  })
}

class fibMain(implicit p: Parameters) extends fibMainIO {

  val memCopy = Mem(1024, UInt(32.W))      // Local memory just to keep track of writes to cache for validation

  // Store a copy of all data written to the cache.  This is done since the cache isn't
  // 'write through' to the memory model and we have no easy way of reading the
  // cache contents from the testbench.
/*  when(stackArb.io.cache.MemReq.valid && stackArb.io.cache.MemReq.bits.iswrite) {
    memCopy.write((stackArb.io.cache.MemReq.bits.addr>>2).asUInt(), stackArb.io.cache.MemReq.bits.data)
  }*/
  io.dout := memCopy.read((io.addr>>2).asUInt())

  val fib = Module(new fibDF())
  val fib_continue = Module(new fib_continueDF())
  val TC = Module(new TaskController(List(32,32), List(32), 3, 1, depth = 1024))
  val stackArb = Module(new CacheArbiter(2))
  val stack = Module(new StackMem(4096))

  // Merge the memory interfaces and connect to the stack memory
  stackArb.io.cpu.MemReq(0) <> fib.io.MemReq
  fib.io.MemResp <> stackArb.io.cpu.MemResp(0)
  stackArb.io.cpu.MemReq(1) <> fib_continue.io.MemReq
  fib_continue.io.MemResp <> stackArb.io.cpu.MemResp(1)
  stack.io.req <> stackArb.io.cache.MemReq
  stackArb.io.cache.MemResp <> stack.io.resp

  // Continuation
  fib_continue.io.in <> fib.io.call17_out
  fib.io.call17_in <> fib_continue.io.out

  // Task controller
  TC.io.parentIn(0) <> io.in
  io.out <> TC.io.parentOut(0)
  TC.io.parentIn(1) <> fib.io.call10_out
  fib.io.call10_in <> TC.io.parentOut(1)
  TC.io.parentIn(2) <> fib.io.call14_out
  fib.io.call14_in <> TC.io.parentOut(2)
  fib.io.in <> TC.io.childOut(0)
  TC.io.childIn(0) <> fib.io.out

}



class fibTest01[T <: fibMainIO](c: T) extends PeekPokeTester(c) {

  val taskID = 0
  // Initializing the signals
  poke(c.io.in.bits.enable.control, false.B)
  poke(c.io.in.bits.enable.taskID, taskID)
  poke(c.io.in.valid, false.B)
  poke(c.io.in.bits.data("field0").data, 0.U)
  poke(c.io.in.bits.data("field0").taskID, taskID)
  poke(c.io.in.bits.data("field0").predicate, false.B)
  poke(c.io.in.bits.data("field1").data, 0.U)
  poke(c.io.in.bits.data("field1").taskID, taskID)
  poke(c.io.in.bits.data("field1").predicate, false.B)
  poke(c.io.out.ready, false.B)
  step(1)
  poke(c.io.in.bits.enable.control, true.B)
  poke(c.io.in.valid, true.B)
  poke(c.io.in.bits.data("field0").data, 5)    // n
  poke(c.io.in.bits.data("field0").predicate, true.B)
  poke(c.io.in.bits.data("field1").data, 1000)   // &r
  poke(c.io.in.bits.data("field1").predicate, true.B)
  poke(c.io.out.ready, true.B)
  step(1)
  poke(c.io.in.bits.enable.control, false.B)
  poke(c.io.in.valid, false.B)
  poke(c.io.in.bits.data("field0").data, 0)
  poke(c.io.in.bits.data("field0").predicate, false.B)
  poke(c.io.in.bits.data("field1").data, 0.U)
  poke(c.io.in.bits.data("field1").predicate, false.B)

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
      if (data != 55) {
        println(Console.RED + s"*** Incorrect result received. Got $data. Hoping for 67405054" + Console.RESET)
        fail
      } else {
        println(Console.BLUE + s"*** Correct return result received. Run time: $time cycles." + Console.RESET)
      }
    }
  }

  if(!result) {
    println(Console.RED + "*** Timeout." + Console.RESET)
    fail
  }
}

class fibTester1 extends FlatSpec with Matchers {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  it should "Check that fib works correctly." in {
    // iotester flags:
    // -ll  = log level <Error|Warn|Info|Debug|Trace>
    // -tbn = backend <firrtl|verilator|vcs>
    // -td  = target directory
    // -tts = seed for RNG
    chisel3.iotesters.Driver.execute(
      Array(
        // "-ll", "Info",
        "-tbn", "verilator",
        "-td", "test_run_dir",
        "-tts", "0001"),
      () => new fibMain()) {
      c => new fibTest01(c)
    } should be(true)
  }
}
