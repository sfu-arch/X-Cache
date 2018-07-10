package dataflow

import java.io.BufferedWriter
import java.io.FileWriter
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


class stencilMainIO(implicit val p: Parameters) extends Module with CoreParams with CacheParams {
  val io = IO(new CoreBundle {
    val in = Flipped(Decoupled(new Call(List(32, 32))))
    val addr = Input(UInt(nastiXAddrBits.W))
    val din = Input(UInt(nastiXDataBits.W))
    val write = Input(Bool())
    val dout = Output(UInt(nastiXDataBits.W))
    val out = Decoupled(new Call(List(32)))
  })
}

class stencilMainDirect(implicit p: Parameters) extends stencilMainIO {

  val cache = Module(new Cache) // Simple Nasti Cache
  val memModel = Module(new NastiMemSlave) // Model of DRAM to connect to Cache
  val memCopy = Mem(1024, UInt(32.W)) // Local memory just to keep track of writes to cache for validation

  // Store a copy of all data written to the cache.  This is done since the cache isn't
  // 'write through' to the memory model and we have no easy way of reading the
  // cache contents from the testbench.
  when(cache.io.cpu.req.valid && cache.io.cpu.req.bits.iswrite) {
    memCopy.write((cache.io.cpu.req.bits.addr >> 2).asUInt(), cache.io.cpu.req.bits.data)
  }
  io.dout := memCopy.read((io.addr >> 2).asUInt())

  // Connect the wrapper I/O to the memory model initialization interface so the
  // test bench can write contents at start.
  memModel.io.nasti <> cache.io.nasti
  memModel.io.init.bits.addr := io.addr
  memModel.io.init.bits.data := io.din
  memModel.io.init.valid := io.write
  cache.io.cpu.abort := false.B

  // Wire up the cache and modules under test.
  val stencil = Module(new stencilDF())
  val stencil_detach1 = Module(new stencil_detach1DF())
  val stencil_inner = Module(new stencil_innerDF())

  val MemArbiter = Module(new MemArbiter(2))
  MemArbiter.io.cpu.MemReq(0) <> stencil_inner.io.MemReq
  stencil_inner.io.MemResp <> MemArbiter.io.cpu.MemResp(0)
  MemArbiter.io.cpu.MemReq(1) <> stencil_detach1.io.MemReq
  stencil_detach1.io.MemResp <> MemArbiter.io.cpu.MemResp(1)

  cache.io.cpu.req <> MemArbiter.io.cache.MemReq
  MemArbiter.io.cache.MemResp <> cache.io.cpu.resp


  stencil.io.in <> io.in
  stencil_detach1.io.in <> stencil.io.call_9_out
  stencil_inner.io.in <> stencil_detach1.io.call_6_out
  stencil_detach1.io.call_6_in <> stencil_inner.io.out
  stencil.io.call_9_in <> stencil_detach1.io.out
  io.out <> stencil.io.out

}

class stencilMainTM(implicit p: Parameters) extends stencilMainIO {

  val cache = Module(new Cache) // Simple Nasti Cache
  val memModel = Module(new NastiMemSlave) // Model of DRAM to connect to Cache
  val memCopy = Mem(1024, UInt(32.W)) // Local memory just to keep track of writes to cache for validation

  // Store a copy of all data written to the cache.  This is done since the cache isn't
  // 'write through' to the memory model and we have no easy way of reading the
  // cache contents from the testbench.
  when(cache.io.cpu.req.valid && cache.io.cpu.req.bits.iswrite) {
    memCopy.write((cache.io.cpu.req.bits.addr >> 2).asUInt(), cache.io.cpu.req.bits.data)
  }
  io.dout := memCopy.read((io.addr >> 2).asUInt())

  // Connect the wrapper I/O to the memory model initialization interface so the
  // test bench can write contents at start.
  memModel.io.nasti <> cache.io.nasti
  memModel.io.init.bits.addr := io.addr
  memModel.io.init.bits.data := io.din
  memModel.io.init.valid := io.write
  cache.io.cpu.abort := false.B

  // Wire up the cache, TM, and modules under test.

  val children = 4
  val TaskControllerModule = Module(new TaskController(List(32, 32, 32), List(32), 1, children))
  val stencil = Module(new stencilDF())

  val stencil_detach1 = for (i <- 0 until children) yield {
    val detach1 = Module(new stencil_detach1DF())
    detach1
  }
  val stencil_inner = for (i <- 0 until children) yield {
    val inner = Module(new stencil_innerDF())
    inner
  }

  val MemArbiter = Module(new MemArbiter(2 * children))
  for (i <- 0 until children) {
    MemArbiter.io.cpu.MemReq(i) <> stencil_inner(i).io.MemReq
    stencil_inner(i).io.MemResp <> MemArbiter.io.cpu.MemResp(i)
    MemArbiter.io.cpu.MemReq(children + i) <> stencil_detach1(i).io.MemReq
    stencil_detach1(i).io.MemResp <> MemArbiter.io.cpu.MemResp(children + i)
  }

  cache.io.cpu.req <> MemArbiter.io.cache.MemReq
  MemArbiter.io.cache.MemResp <> cache.io.cpu.resp

  // tester to cilk_for_test02
  stencil.io.in <> io.in

  // cilk_for_test02 to task controller
  TaskControllerModule.io.parentIn(0) <> stencil.io.call_9_out

  // task controller to sub-task stencil_detach1
  for (i <- 0 until children) {
    stencil_detach1(i).io.in <> TaskControllerModule.io.childOut(i)
    stencil_inner(i).io.in <> stencil_detach1(i).io.call_6_out
    stencil_detach1(i).io.call_6_in <> stencil_inner(i).io.out
    TaskControllerModule.io.childIn(i) <> stencil_detach1(i).io.out
  }

  // Task controller to cilk_for_test02
  stencil.io.call_9_in <> TaskControllerModule.io.parentOut(0)

  // cilk_for_test02 to tester
  io.out <> stencil.io.out

}

class stencilTest01[T <: stencilMainIO](c: T) extends PeekPokeTester(c) {

  val inDataVec = List(
    3, 6, 7, 5,
    3, 5, 6, 2,
    9, 1, 2, 7,
    0, 9, 3, 6)
  val inAddrVec = List.range(0, 4 * inDataVec.length, 4)
  val outAddrVec = List.range(4 * inDataVec.length, 2 * 4 * inDataVec.length, 4)
  val outDataVec = List(
    3, 4, 4, 3,
    4, 6, 6, 4,
    4, 5, 6, 4,
    3, 4, 4, 3
  )
  poke(c.io.addr, 0.U)
  poke(c.io.din, 0.U)
  poke(c.io.write, false.B)
  var i = 0

  // Write initial contents to the memory model.
  for (i <- 0 until inDataVec.length) {
    poke(c.io.addr, inAddrVec(i))
    poke(c.io.din, inDataVec(i))
    poke(c.io.write, true.B)
    step(1)
  }
  poke(c.io.write, false.B)
  step(1)

  // Initializing the signals
  poke(c.io.in.bits.enable.control, false.B)
  poke(c.io.in.valid, false.B)
  poke(c.io.in.bits.data("field0").data, 0.U)
  poke(c.io.in.bits.data("field0").taskID, 5.U)
  poke(c.io.in.bits.data("field0").predicate, false.B)
  poke(c.io.in.bits.data("field1").data, 0.U)
  poke(c.io.in.bits.data("field1").taskID, 5.U)
  poke(c.io.in.bits.data("field1").predicate, false.B)
  poke(c.io.out.ready, false.B)
  step(1)
  poke(c.io.in.bits.enable.control, true.B)
  poke(c.io.in.valid, true.B)
  poke(c.io.in.bits.data("field0").data, 0) // Array a[] base address
  poke(c.io.in.bits.data("field0").predicate, true.B)
  poke(c.io.in.bits.data("field1").data, 4 * inDataVec.length) // Array b[] base address
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

  //  val file = "test.txt"
  //  val writer = new BufferedWriter(new FileWriter(file))
  //  val writeResult = inA.zip(inB).zip(outDataVec)
  //  writeResult.map(_.toString() + "\n").foreach(writer.write)
  //  writer.close()

  poke(c.io.addr, 0.U)
  poke(c.io.din, 0.U)
  poke(c.io.write, false.B)
  //  var i = 0

  // Write initial contents to the memory model.
  for (i <- 0 until inDataVec.length) {
    poke(c.io.addr, inAddrVec(i))
    poke(c.io.din, inDataVec(i))
    poke(c.io.write, true.B)
    step(1)
  }
  poke(c.io.write, false.B)
  step(1)


  // Initializing the signals
  poke(c.io.in.bits.enable.control, false.B)
  poke(c.io.in.valid, false.B)

  var time = 0
  var result = false
  while (time < 6000) {
    time += 1
    step(1)
    if (peek(c.io.out.valid) == 1 &&
      peek(c.io.out.bits.data("field0").predicate) == 1
    ) {
      result = true
      val data = peek(c.io.out.bits.data("field0").data)
      if (data != 1) {
        println(Console.RED + s"*** Incorrect result received. Got $data. Hoping for 1" + Console.RESET)
        fail
      } else {
        println(Console.BLUE + s"*** Correct return result received. Run time: $time cycles." + Console.RESET)
      }
    }
  }

  //  Peek into the CopyMem to see if the expected data is written back to the Cache
  var valid_data = true
  for (i <- 0 until outDataVec.length) {
    poke(c.io.addr, outAddrVec(i))
    step(1)
    val data = peek(c.io.dout)
    if (data != outDataVec(i).toInt) {
      println(Console.RED + s"*** Incorrect data received. Got $data. Hoping for ${outDataVec(i).toInt}" + Console.RESET)
      fail
      valid_data = false
    }
  }
  if (valid_data) {
    println(Console.BLUE + "*** Correct data written back." + Console.RESET)
  }


  if (!result) {
    println(Console.RED + "*** Timeout." + Console.RESET)
    fail
  }
}

class stencilTester1 extends FlatSpec with Matchers {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  it should "Check that stencil works correctly." in {
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
      () => new stencilMainDirect()) {
      c => new stencilTest01(c)
    } should be(true)
  }
}

class stencilTester2 extends FlatSpec with Matchers {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  // iotester flags:
  // -ll  = log level <Error|Warn|Info|Debug|Trace>
  // -tbn = backend <firrtl|verilator|vcs>
  // -td  = target directory
  // -tts = seed for RNG
  it should "Check that cilk_for_test02 works when called via task manager." in {
    chisel3.iotesters.Driver.execute(
      Array(
        // "-ll", "Info",
        "-tbn", "verilator",
        "-td", "test_run_dir",
        "-tts", "0001"),
      () => new stencilMainTM()) {
      c => new stencilTest01(c)
    } should be(true)
  }
}
