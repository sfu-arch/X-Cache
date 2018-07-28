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


class vector_scaleMainIO(implicit val p: Parameters)  extends Module with CoreParams with CacheParams {
  val io = IO( new CoreBundle {
    val in = Flipped(Decoupled(new Call(List(32,32,32,32,32))))
    val addr = Input(UInt(nastiXAddrBits.W))  // Initialization address
    val din  = Input(UInt(nastiXDataBits.W))  // Initialization data
    val write = Input(Bool())                 // Initialization write strobe
    val dout = Output(UInt(nastiXDataBits.W))
    val out = Decoupled(new Call(List(32)))
  })
}


class vector_scaleMainTM(children :Int=3)(implicit p: Parameters) extends vector_scaleMainIO  {

  val cache = Module(new Cache)            // Simple Nasti Cache
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
  val memModel = Module(new NastiMemSlave(latency = 0)) // Model of DRAM to connect to Cache
  memModel.io.nasti <> cache.io.nasti
  memModel.io.init.bits.addr := io.addr
  memModel.io.init.bits.data := io.din
  memModel.io.init.valid := io.write
  cache.io.cpu.abort := false.B

  // Wire up the cache, TM, and modules under test.

  val TaskControllerModule = Module(new TaskController(List(32,32,32,32), List(), 1, children))
  val vector_scale = Module(new vector_scaleDF())

  val vector_scale_detach = for (i <- 0 until children) yield {
    val foo = Module(new vector_scale_detach1DF())
    foo
  }

  // Ugly hack to merge requests from two children.  "ReadWriteArbiter" merges two
  // requests ports of any type.  Read or write is irrelevant.
  val CacheArbiter = Module(new MemArbiter(children))
  for (i <- 0 until children) {
    CacheArbiter.io.cpu.MemReq(i) <> vector_scale_detach(i).io.MemReq
    vector_scale_detach(i).io.MemResp <> CacheArbiter.io.cpu.MemResp(i)
  }
  cache.io.cpu.req <> CacheArbiter.io.cache.MemReq
  CacheArbiter.io.cache.MemResp <> cache.io.cpu.resp

  // tester to vector_scale
  vector_scale.io.in <> io.in

  // vector_scale to task controller
  TaskControllerModule.io.parentIn(0) <> vector_scale.io.call_9_out

  // task controller to sub-task vector_scale_detach
  for (i <- 0 until children ) {
    vector_scale_detach(i).io.in <> TaskControllerModule.io.childOut(i)
    TaskControllerModule.io.childIn(i) <> vector_scale_detach(i).io.out
  }

  // Task controller to vector_scale
  vector_scale.io.call_9_in <> TaskControllerModule.io.parentOut(0)

  // vector_scale to tester
  io.out <> vector_scale.io.out

}

class vector_scaleTest01[T <: vector_scaleMainIO](c: T, tiles: Int) extends PeekPokeTester(c) {
//  val inDataVec = List(-4,-22,9,221,178,152,80,98,163,40,239,169,89,179,98,69,117,196,16,44,12,59,
//    23,14,68,13,57,137,175,195,165,68,199,71,34,-6,249,139,118,157,76,254,71,190,179,66,157,193,7,
//    198,-18,44,2,182,236,247,221,190,129,141,131,192,106,227,8,166,246,154,202,-19,56,24,-19,25,
//    111,57,-12,13,-5,21,108,154,242,7,82,95,0,200,31,26,238,59,115,89,183,21,152,174,200,100)
  val inDataVec = List(0,0,9,221,178,152,80,98,163,40,239,169,89,179,98,69,117,196,16,44,12,59,
    23,14,68,13,57,137,175,195,165,68,199,71,34,0,249,139,118,157,76,254,71,190,179,66,157,193,7,
    198,0,44,2,182,236,247,221,190,129,141,131,192,106,227,8,166,246,154,202,0,56,24,0,25,
    111,57,0,13,0,21,108,154,242,7,82,95,0,200,31,26,238,59,115,89,183,21,152,174,200,100)
  val inAddrVec = List.range(0, 4*inDataVec.length, 4)
  val outAddrVec = List.range(1024, 1024+(4*inDataVec.length), 4)
  val outDataVec = List(0,0,28,255,255,255,250,255,255,125,255,255,255,255,255,215,255,255,50,
    137,37,184,71,43,212,40,178,255,255,255,255,212,255,221,106,0,255,255,255,255,237,255,221,
    255,255,206,255,255,21,255,0,137,6,255,255,255,255,255,255,255,255,255,255,255,25,255,255,
    255,255,0,175,75,0,78,255,178,0,40,0,65,255,255,255,21,255,255,0,255,96,81,255,184,255,255,
    255,65,255,255,255,255)

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
  poke(c.io.in.bits.data("field0").taskID, 5.U)
  poke(c.io.in.bits.data("field0").predicate, false.B)
  poke(c.io.in.bits.data("field1").data, 0.U)
  poke(c.io.in.bits.data("field1").taskID, 5.U)
  poke(c.io.in.bits.data("field1").predicate, false.B)
  poke(c.io.in.bits.data("field2").data, 0.U)
  poke(c.io.in.bits.data("field2").taskID, 5.U)
  poke(c.io.in.bits.data("field2").predicate, false.B)
  poke(c.io.in.bits.data("field3").data, 0.U)
  poke(c.io.in.bits.data("field3").taskID, 5.U)
  poke(c.io.in.bits.data("field3").predicate, false.B)
  poke(c.io.out.ready, false.B)
  step(1)
  poke(c.io.in.bits.enable.control, true.B)
  poke(c.io.in.valid, true.B)
  poke(c.io.in.bits.data("field0").data, 0.U)    // Array a[] base address
  poke(c.io.in.bits.data("field0").predicate, true.B)
  poke(c.io.in.bits.data("field1").data, 1024.U)   // Array b[] base address
  poke(c.io.in.bits.data("field1").predicate, true.B)
  poke(c.io.in.bits.data("field2").data, 800.U)   // scale value
  poke(c.io.in.bits.data("field2").predicate, true.B)
  poke(c.io.in.bits.data("field3").data, 100.U)
  poke(c.io.in.bits.data("field3").predicate, false.B)
  poke(c.io.out.ready, true.B)
  step(1)
  poke(c.io.in.bits.enable.control, false.B)
  poke(c.io.in.valid, false.B)
  poke(c.io.in.bits.data("field0").data, 0)
  poke(c.io.in.bits.data("field0").predicate, false.B)
  poke(c.io.in.bits.data("field1").data, 0.U)
  poke(c.io.in.bits.data("field1").predicate, false.B)
  poke(c.io.in.bits.data("field2").data, 0.U)
  poke(c.io.in.bits.data("field2").predicate, false.B)
  poke(c.io.in.bits.data("field3").data, 0.U)
  poke(c.io.in.bits.data("field3").predicate, false.B)

  step(1)

  // NOTE: Don't use assert().  It seems to terminate the writing of VCD files
  // early (before the error) which makes debugging very difficult. Check results
  // using if() and fail command.
  var time = 0
  var result = false
  while (time < 10000 && !result) {
    time += 1
    step(1)
    if (peek(c.io.out.valid) == 1 && peek(c.io.out.bits.enable.control) == 1) {
      result = true
      println(Console.BLUE + s"*** Return received for t=$tiles. Run time: $time cycles." + Console.RESET)
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


class vector_scaleTester1 extends FlatSpec with Matchers {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val testParams = p.alterPartial({
    case TLEN => 6
    case TRACE => false
  })
  // iotester flags:
  // -ll  = log level <Error|Warn|Info|Debug|Trace>
  // -tbn = backend <firrtl|verilator|vcs>
  // -td  = target directory
  // -tts = seed for RNG
  val tile_list = List(1,2,4,8)
  for (tile <- tile_list) {
    it should s"Test: $tile tiles" in {
      chisel3.iotesters.Driver.execute(
        Array(
          // "-ll", "Info",
          "-tbn", "verilator",
          "-td", "test_run_dir",
          "-tts", "0001"),
        () => new vector_scaleMainTM(tile)(testParams)) {
        c => new vector_scaleTest01(c,tile)
      } should be(true)
    }
  }
}
