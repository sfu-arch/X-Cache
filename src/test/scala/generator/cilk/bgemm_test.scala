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


class bgemmMainIO(implicit val p: Parameters)  extends Module with CoreParams with CacheParams {
  val io = IO( new CoreBundle {
    val in = Flipped(Decoupled(new Call(List(32,32,32))))
    val addr = Input(UInt(nastiXAddrBits.W))  // Initialization address
    val din  = Input(UInt(nastiXDataBits.W))  // Initialization data
    val write = Input(Bool())                 // Initialization write strobe
    val dout = Output(UInt(nastiXDataBits.W))
    val out = Decoupled(new Call(List(32)))
  })
}

class bgemmMainDirect(implicit p: Parameters) extends bgemmMainIO()(p) {
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

  val bgemm = Module(new bgemmDF())
  val bgemm_detach1 = Module(new bgemm_detach1DF())
  val bgemm_detach2 = Module(new bgemm_detach2DF())
  val bgemm_detach3 = Module(new bgemm_detach3DF())

  cache.io.cpu.req <> bgemm_detach2.io.CacheReq
  bgemm_detach2.io.CacheResp <> cache.io.cpu.resp
  bgemm.io.in <> io.in
  bgemm_detach1.io.in <> bgemm.io.call10_out
  bgemm_detach2.io.in <> bgemm_detach1.io.call13_out
  bgemm_detach3.io.in <> bgemm_detach2.io.call13_out
  bgemm_detach2.io.call13_in <> bgemm_detach3.io.out
  bgemm_detach1.io.call13_in <> bgemm_detach2.io.out
  bgemm.io.call10_in <> bgemm_detach1.io.out
  io.out <> bgemm.io.out

}

class bgemmMainTM(implicit p: Parameters) extends bgemmMainIO()(p) {

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

  val children = 3
  val TaskControllerModule = Module(new TaskController(List(32,32,32,32), List(32), 1, children))
  val bgemm = Module(new bgemmDF())

  val bgemm_detach1 = for (i <- 0 until children) yield {
    val detach = Module(new bgemm_detach1DF())
    detach
  }
  val bgemm_detach2 = for (i <- 0 until children) yield {
    val detach2 = Module(new bgemm_detach2DF)
    detach2
  }
  val bgemm_detach3 = for (i <- 0 until children) yield {
    val detach3 = Module(new bgemm_detach3DF)
    detach3
  }

  // Merge requests from two children.
  val CacheArbiter = Module(new CacheArbiter(children))
  for (i <- 0 until children) {
    CacheArbiter.io.cpu.CacheReq(i) <> bgemm_detach2(i).io.CacheReq
    bgemm_detach2(i).io.CacheResp <> CacheArbiter.io.cpu.CacheResp(i)
  }
  cache.io.cpu.req <> CacheArbiter.io.cache.CacheReq
  CacheArbiter.io.cache.CacheResp <> cache.io.cpu.resp

  // tester to cilk_for_test02
  bgemm.io.in <> io.in

  // cilk_for_test02 to task controller
  TaskControllerModule.io.parentIn(0) <> bgemm.io.call10_out

  // task controller to sub-task bgemm_detach
  for (i <- 0 until children ) {
    bgemm_detach1(i).io.in <> TaskControllerModule.io.childOut(i)
    bgemm_detach2(i).io.in <> bgemm_detach1(i).io.call13_out
    bgemm_detach3(i).io.in <> bgemm_detach2(i).io.call13_out
    bgemm_detach2(i).io.call13_in <> bgemm_detach3(i).io.out
    bgemm_detach1(i).io.call13_in <> bgemm_detach2(i).io.out
    TaskControllerModule.io.childIn(i) <> bgemm_detach1(i).io.out
  }

  // Task controller to cilk_for_test02
  bgemm.io.call10_in <> TaskControllerModule.io.parentOut(0)

  // cilk_for_test02 to tester
  io.out <> bgemm.io.out

}

class bgemmTest01[T <: bgemmMainIO](c: T) extends PeekPokeTester(c) {


  val inAddrVec = List.range(0, 200, 4)  // byte addresses
  val inA = List.range(0, 16)  // 4x4 array of uint32
  val inB = List.range(0, 16)  // 4x4 array of uint32
  val inDataVec = inA++inB
  val outAddrVec = List.range(256, 256+100, 4)
  val outDataVec = inA.zip(inB).map { case (x, y) => x + y }

  poke(c.io.addr, 0.U)
  poke(c.io.din, 0.U)
  poke(c.io.write, false.B)
  var i = 0

  // Write initial contents to the memory model.
  for(i <- 0 until inDataVec.length) {
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
  poke(c.io.in.bits.data("field0").predicate, false.B)
  poke(c.io.in.bits.data("field1").data, 0.U)
  poke(c.io.in.bits.data("field1").predicate, false.B)
  poke(c.io.in.bits.data("field2").data, 0.U)
  poke(c.io.in.bits.data("field2").predicate, false.B)
  poke(c.io.out.ready, false.B)
  step(1)
  poke(c.io.in.bits.enable.control, true.B)
  poke(c.io.in.valid, true.B)
  poke(c.io.in.bits.data("field0").data, 0)
  poke(c.io.in.bits.data("field0").predicate, true.B)
  poke(c.io.in.bits.data("field1").data, 100)
  poke(c.io.in.bits.data("field1").predicate, true.B)
  poke(c.io.in.bits.data("field2").data, 256)
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
  var time = 0 //Cycle counter
  var result = false
  while (time < 1000) {
    time += 1
    step(1)
    //println(s"Cycle: $time")
    if (peek(c.io.out.valid) == 1 &&
      peek(c.io.out.bits.data("field0").predicate) == 1
      ) {
      result = true
      val data = peek(c.io.out.bits.data("field0").data)
      if (data != 1) {
        println(Console.RED + s"*** Incorrect result received. Got $data. Hoping for 1" + Console.RESET)
        fail
      } else {
        println(Console.BLUE + s"*** Correct result received. Run time: $time cycles." + Console.RESET)
      }
    }
  }
  //  Peek into the CopyMem to see if the expected data is written back to the Cache
  var valid_data = true
  for(i <- 0 until outDataVec.length) {
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

  if(!result) {
    println(Console.RED + "*** Timeout." + Console.RESET)
    fail
  }
}

class bgemmTester1 extends FlatSpec with Matchers {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  it should "Check that bgemm works correctly." in {
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
     () => new bgemmMainDirect()) {
     c => new bgemmTest01(c)
    } should be(true)
  }
}

class bgemmTester2 extends FlatSpec with Matchers {
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
      () => new bgemmMainTM()) {
      c => new bgemmTest01(c)
    } should be(true)
  }
}
