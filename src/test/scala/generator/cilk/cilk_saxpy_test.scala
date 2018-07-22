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

import scala.util.Random


class cilk_saxpyMainIO(implicit val p: Parameters)  extends Module with CoreParams with CacheParams {
  val io = IO( new CoreBundle {
    val in = Flipped(Decoupled(new Call(List(32,32,32,32))))
    val req  = Flipped(Decoupled(new MemReq))
    val resp = Output(Valid(new MemResp))
    val out = Decoupled(new Call(List(32)))
  })
}

class cilk_saxpyMainTM(children :Int)(implicit p: Parameters) extends cilk_saxpyMainIO  {

  val cache = Module(new Cache)            // Simple Nasti Cache
  val memModel = Module(new NastiMemSlave) // Model of DRAM to connect to Cache

  // Connect the wrapper I/O to the memory model initialization interface so the
  // test bench can write contents at start.
  memModel.io.nasti <> cache.io.nasti
  memModel.io.init.bits.addr := 0.U
  memModel.io.init.bits.data := 0.U
  memModel.io.init.valid := false.B
  cache.io.cpu.abort := false.B

  // Wire up the cache, TM, and modules under test.

  val TaskControllerModule = Module(new TaskController(List(32,32,32,32), List(), 1, children))
  val saxpy = Module(new cilk_saxpyDF())

  val saxpy_detach = for (i <- 0 until children) yield {
    val detach1 = Module(new cilk_saxpy_detach1DF())
    detach1
  }

  // Ugly hack to merge requests from two children.  "ReadWriteArbiter" merges two
  // requests ports of any type.  Read or write is irrelevant.
  val CacheArbiter = Module(new MemArbiter(children+1))
  for (i <- 0 until children) {
    CacheArbiter.io.cpu.MemReq(i) <> saxpy_detach(i).io.MemReq
    saxpy_detach(i).io.MemResp <> CacheArbiter.io.cpu.MemResp(i)
  }
  CacheArbiter.io.cpu.MemReq(children) <> io.req
  io.resp <> CacheArbiter.io.cpu.MemResp(children)

  cache.io.cpu.req <> CacheArbiter.io.cache.MemReq
  CacheArbiter.io.cache.MemResp <> cache.io.cpu.resp


  // tester to saxpy
  saxpy.io.in <> io.in

  // saxpy to task controller
  TaskControllerModule.io.parentIn(0) <> saxpy.io.call_9_out

  // task controller to sub-task saxpy_detach
  for (i <- 0 until children ) {
    saxpy_detach(i).io.in <> TaskControllerModule.io.childOut(i)
    TaskControllerModule.io.childIn(i) <> saxpy_detach(i).io.out
  }

  // Task controller to saxpy
  saxpy.io.call_9_in <> TaskControllerModule.io.parentOut(0)

  // saxpy to tester
  io.out <> saxpy.io.out

}

class cilk_saxpyTest01[T <: cilk_saxpyMainIO](c: T, n:Int, ch:Int) extends PeekPokeTester(c) {

  def MemRead(addr:Int):BigInt = {
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

  def MemWrite(addr:Int, data:Int):BigInt = {
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

  val dataLen = n;
  val a = 5 // 'a' of a*x[i]+y[i]
  val inAddrVec = List.range(0, 8*dataLen, 4)
  val inX = List.range(1, dataLen+1)  // array of uint32
  val inY = List.range(1, dataLen+1)  // array of uint32
  val inDataVec = inX++inY
  val outAddrVec = List.range(4*dataLen, 8*dataLen, 4)
  val outDataVec = inX.zip(inY).map { case (x, y) => a*x + y }

  var i = 0

  // Write initial contents to the memory model.
  for(i <- 0 until inDataVec.length) {
    MemWrite(inAddrVec(i), inDataVec(i))
  }
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
  poke(c.io.in.bits.data("field0").data, n)    // Number of iterations
  poke(c.io.in.bits.data("field0").predicate, true.B)
  poke(c.io.in.bits.data("field1").data, a)   // Scale value
  poke(c.io.in.bits.data("field1").predicate, true.B)
  poke(c.io.in.bits.data("field2").data, 0)   // X[]
  poke(c.io.in.bits.data("field2").predicate, true.B)
  poke(c.io.in.bits.data("field3").data, 4*dataLen)   // Y[]
  poke(c.io.in.bits.data("field3").predicate, true.B)
  poke(c.io.out.ready, true.B)
  step(1)
  poke(c.io.in.bits.enable.control, false.B)
  poke(c.io.in.valid, false.B)
  poke(c.io.in.bits.data("field0").data, 0)
  poke(c.io.in.bits.data("field0").predicate, false.B)
  poke(c.io.in.bits.data("field1").data, 0.U)
  poke(c.io.in.bits.data("field1").predicate, false.B)
  poke(c.io.in.bits.data("field2").data, 0)
  poke(c.io.in.bits.data("field2").predicate, false.B)
  poke(c.io.in.bits.data("field3").data, 0.U)
  poke(c.io.in.bits.data("field3").predicate, false.B)

  step(1)

  // NOTE: Don't use assert().  It seems to terminate the writing of VCD files
  // early (before the error) which makes debugging very difficult. Check results
  // using if() and fail command.
  var time = 0
  var result = false
  while (time < 100000 && !result) {
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
        println(Console.BLUE + s"*** Correct return result received. Run time: $time cycles. Tiles=$ch" + Console.RESET)
      }
    }
  }

  //  Peek into the CopyMem to see if the expected data is written back to the Cache
  var valid_data = true
  for(i <- 0 until outDataVec.length) {
    val data = MemRead(outAddrVec(i))
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

class cilk_saxpyTester1 extends FlatSpec with Matchers {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val testParams = p.alterPartial({
    case TLEN => 5
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
        () => new cilk_saxpyMainTM(tile)(testParams)) {
        c => new cilk_saxpyTest01(c, 400, tile)
      } should be(true)
    }
  }
}
