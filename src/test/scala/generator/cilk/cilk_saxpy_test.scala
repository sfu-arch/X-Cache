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
    val addr = Input(UInt(nastiXAddrBits.W))  // Initialization address
    val din  = Input(UInt(nastiXDataBits.W))  // Initialization data
    val write = Input(Bool())                 // Initialization write strobe
    val dout = Output(UInt(nastiXDataBits.W))
    val out = Decoupled(new Call(List(32)))
  })
}

class cilk_saxpyMainDirect(implicit p: Parameters) extends saxpyMainIO {

  val cache = Module(new Cache)            // Simple Nasti Cache
  val memModel = Module(new NastiMemSlave) // Model of DRAM to connect to Cache
  val memCopy = Mem(16*1024, UInt(32.W))      // Local memory just to keep track of writes to cache for validation

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

  // Wire up the cache and modules under test.
  val saxpy_detach = Module(new cilk_saxpy_detach1DF())
  val saxpy = Module(new cilk_saxpyDF())

  cache.io.cpu.req <> saxpy_detach.io.MemReq
  saxpy_detach.io.MemResp <> cache.io.cpu.resp
  saxpy.io.in <> io.in
  saxpy_detach.io.in <> saxpy.io.call_9_out
  saxpy.io.call_9_in <> saxpy_detach.io.out
  io.out <> saxpy.io.out

}

class cilk_saxpyMainTM(children :Int=5)(implicit p: Parameters) extends saxpyMainIO  {

  val cache = Module(new Cache)            // Simple Nasti Cache
  val memModel = Module(new NastiMemSlave) // Model of DRAM to connect to Cache
  val memCopy = Mem(16*1024, UInt(32.W))      // Local memory just to keep track of writes to cache for validation

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

  // Wire up the cache, TM, and modules under test.


  val TaskControllerModule = Module(new TaskController(List(32,32,32,32), List(32), 1, children))
  val saxpy = Module(new cilk_saxpyDF())

  val saxpy_detach = for (i <- 0 until children) yield {
    val foo = Module(new cilk_saxpy_detach1DF())
    foo
  }

  // Ugly hack to merge requests from two children.  "ReadWriteArbiter" merges two
  // requests ports of any type.  Read or write is irrelevant.
  val CacheArbiter = Module(new MemArbiter(children))
  for (i <- 0 until children) {
    CacheArbiter.io.cpu.MemReq(i) <> saxpy_detach(i).io.MemReq
    saxpy_detach(i).io.MemResp <> CacheArbiter.io.cpu.MemResp(i)
  }
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
/*
class saxpyMainTMStack(children :Int)(implicit p: Parameters) extends saxpyMainIO  {

  val Stack = Module(new RFile(1024))            // Simple Nasti Cache

  io.dout := 0.U
  // Wire up the cache, TM, and modules under test.


  val TaskControllerModule = Module(new TaskController(List(32,32,32,32), List(32), 1, children))
  val saxpy = Module(new saxpyDF())

  val saxpy_detach = for (i <- 0 until children) yield {
    val foo = Module(new saxpy_detach1DF())
    foo
  }

  val StackArbiter = Module(new TypeStackFile(ID=0,Size=32,NReads=children*2,NWrites=children*2)
  (WControl=new WriteMemoryController(NumOps=children*2,BaseSize=2,NumEntries=2))
  (RControl=new ReadMemoryController(NumOps=children*2,BaseSize=2,NumEntries=2)))

  for (i<-0 until children) {
    for(j<-0 until 2) {
      StackArbiter.io.ReadIn(2 * i + j) <> saxpy_detach(i).io.ReadIn(j)
      StackArbiter.io.WriteIn(2 * i + j) <> saxpy_detach(i).io.WriteIn(j)
      saxpy_detach(i).io.ReadOut(j) <> StackArbiter.io.ReadOut(2 * i + j)
      saxpy_detach(i).io.WriteOut(j) <> StackArbiter.io.WriteOut(2 * i + j)
    }
  }

  // tester to saxpy
  saxpy.io.in <> io.in

  // saxpy to task controller
  TaskControllerModule.io.parentIn(0) <> saxpy.io.call11_out

  // task controller to sub-task saxpy_detach
  for (i <- 0 until children ) {
    saxpy_detach(i).io.in <> TaskControllerModule.io.childOut(i)
    TaskControllerModule.io.childIn(i) <> saxpy_detach(i).io.out
  }

  // Task controller to saxpy
  saxpy.io.call11_in <> TaskControllerModule.io.parentOut(0)

  // saxpy to tester
  io.out <> saxpy.io.out

}
*/
class cilk_saxpyTest01[T <: saxpyMainIO](c: T, n:Int, ch:Int) extends PeekPokeTester(c) {
  val dataLen = n;
  val a = 5 // 'a' of a*x[i]+y[i]
  val inAddrVec = List.range(0, 8*dataLen, 4)
  val inX = List.range(1, dataLen+1)  // array of uint32
  val inY = List.range(1, dataLen+1)  // array of uint32
  val inDataVec = inX++inY
  val outAddrVec = List.range(4*dataLen, 8*dataLen, 4)
  val outDataVec = inX.zip(inY).map { case (x, y) => a*x + y }

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

class cilk_saxpyTester1 extends FlatSpec with Matchers {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  // iotester flags:
  // -ll  = log level <Error|Warn|Info|Debug|Trace>
  // -tbn = backend <firrtl|verilator|vcs>
  // -td  = target directory
  // -tts = seed for RNG
  for (i <- 100 to 100) {
    it should s"Loop ${i} iterations." in {
      chisel3.iotesters.Driver.execute(
        Array(
          // "-ll", "Info",
          "-tbn", "verilator",
          "-td", "test_run_dir",
          "-tts", "0001"),
        () => new cilk_saxpyMainDirect()) {
        c => new saxpyTest01(c,i, 1)
      } should be(true)
    }
  }
}

class cilk_saxpyTester2 extends FlatSpec with Matchers {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  // iotester flags:
  // -ll  = log level <Error|Warn|Info|Debug|Trace>
  // -tbn = backend <firrtl|verilator|vcs>
  // -td  = target directory
  // -tts = seed for RNG
  val tile_list = List(3)

  for (tile <- tile_list) {
    it should s"Test: $tile tiles" in {
      chisel3.iotesters.Driver.execute(
        Array(
          // "-ll", "Info",
          "-tbn", "verilator",
          "-td", "test_run_dir",
          "-tts", "0001"),
        () => new cilk_saxpyMainTM(tile)(p.alterPartial({case TLEN => 4}))) {
        c => new cilk_saxpyTest01(c, 400, tile)
      } should be(true)
    }
  }
}
