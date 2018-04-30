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


class cilk_for_test12MainIO(implicit val p: Parameters)  extends Module with CoreParams with CacheParams {
  val io = IO( new CoreBundle {
    val in = Flipped(Decoupled(new Call(List(32,32,32))))
    val addr = Input(UInt(nastiXAddrBits.W))
    val din  = Input(UInt(nastiXDataBits.W))
    val write = Input(Bool())
    val dout = Output(UInt(nastiXDataBits.W))
    val out = Decoupled(new Call(List(32)))
  })
}

class cilk_for_test12Main(implicit p: Parameters) extends cilk_for_test12MainIO {

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

  // Wire up the cache and modules under test.
  val children1 = 1
  val children2 = 1
  val children3 = 8
  val TaskControllerModule1 = Module(new TaskController(List(32,32,32), List(32), 1, children1,depth=2))
  val TaskControllerModule2 = Module(new TaskController(List(32,32), List(32), children1, children2,depth=2))
  val TaskControllerModule3 = Module(new TaskController(List(32,32), List(32), children2, children3,depth=2))
  val cilk_for_test12 = Module(new cilk_for_test12DF())

  val cilk_for_test12_detach1 = for (i <- 0 until children1) yield {
    val detach = Module(new cilk_for_test12_detach1DF())
    detach
  }
  val cilk_for_test12_detach2 = for (i <- 0 until children2) yield {
    val detach2 = Module(new cilk_for_test12_detach2DF)
    detach2
  }
  val cilk_for_test12_detach3 = for (i <- 0 until children3) yield {
    val detach3 = Module(new cilk_for_test12_detach3DF)
    detach3
  }

  // Merge requests from two children.
  val CacheArbiter = Module(new CacheArbiter(children1+children2+children3+1))
  for (i <- 0 until children1) {
    CacheArbiter.io.cpu.MemReq(i) <> cilk_for_test12_detach1(i).io.MemReq
    cilk_for_test12_detach1(i).io.MemResp <> CacheArbiter.io.cpu.MemResp(i)
  }
  for (i <- 0 until children2) {
    CacheArbiter.io.cpu.MemReq(children1+i) <> cilk_for_test12_detach2(i).io.MemReq
    cilk_for_test12_detach2(i).io.MemResp <> CacheArbiter.io.cpu.MemResp(children1+i)
  }
  for (i <- 0 until children3) {
    CacheArbiter.io.cpu.MemReq(children2+children1+i) <> cilk_for_test12_detach3(i).io.MemReq
    cilk_for_test12_detach3(i).io.MemResp <> CacheArbiter.io.cpu.MemResp(children2+children1+i)
  }
  CacheArbiter.io.cpu.MemReq(children1+children2+children3) <> cilk_for_test12.io.MemReq
  cilk_for_test12.io.MemResp <> CacheArbiter.io.cpu.MemResp(children1+children2+children3)
  cache.io.cpu.req <> CacheArbiter.io.cache.MemReq
  CacheArbiter.io.cache.MemResp <> cache.io.cpu.resp

  // tester to cilk_for_test02
  cilk_for_test12.io.in <> io.in

  // cilk_for_test02 to task controller
  TaskControllerModule1.io.parentIn(0) <> cilk_for_test12.io.call15_out

  // task controller to sub-task cilk_for_test12_detach
  for (i <- 0 until children1 ) {
    // TC1 to Detach1
    TaskControllerModule1.io.childIn(i) <> cilk_for_test12_detach1(i).io.out
    cilk_for_test12_detach1(i).io.in <> TaskControllerModule1.io.childOut(i)
    // Detach1 to TC2
    TaskControllerModule2.io.parentIn(i) <> cilk_for_test12_detach1(i).io.call18_out
    cilk_for_test12_detach1(i).io.call18_in <> TaskControllerModule2.io.parentOut(i)
  }
  for (i <- 0 until children2 ) {
    // TC2 to Detach2
    TaskControllerModule2.io.childIn(i) <> cilk_for_test12_detach2(i).io.out
    cilk_for_test12_detach2(i).io.in <> TaskControllerModule2.io.childOut(i)
    // Detach2 to TC3
    TaskControllerModule3.io.parentIn(i) <> cilk_for_test12_detach2(i).io.call15_out
    cilk_for_test12_detach2(i).io.call15_in <> TaskControllerModule3.io.parentOut(i)
  }

  for (i <- 0 until children3) {
    // TC3 to Detach3
    TaskControllerModule3.io.childIn(i) <> cilk_for_test12_detach3(i).io.out
    cilk_for_test12_detach3(i).io.in <> TaskControllerModule3.io.childOut(i)
  }

  // Task controller to cilk_for_test02
  cilk_for_test12.io.call15_in <> TaskControllerModule1.io.parentOut(0)

  // cilk_for_test02 to tester
  io.out <> cilk_for_test12.io.out

}



class cilk_for_test12Test01[T <: cilk_for_test12MainIO](c: T) extends PeekPokeTester(c) {

  val inAddrVec = List.range(64, 64+4*8, 4)
  val inDataVec = List(0,1,2,3,4,5,6,7)
  val outAddrVec = List.range(64, 64+4*8, 4)
  val outDataVec = List(0,16777216,33554432,50331648,67108864,83886080,100663296,134283520)

  poke(c.io.addr, 0.U)
  poke(c.io.din, 0.U)
  poke(c.io.write, false.B)
  var i = 0

  // Write initial contents to the memory model.
  for(i <- 0 until inAddrVec.length) {
    poke(c.io.addr, inAddrVec(i))
    poke(c.io.din, inDataVec(i))
    poke(c.io.write, true.B)
    step(1)
  }
  poke(c.io.write, false.B)
  step(1)

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
  poke(c.io.in.bits.data("field0").data, 64)    // Array a[] base address
  poke(c.io.in.bits.data("field0").predicate, true.B)
  poke(c.io.in.bits.data("field1").data, 8)   // n
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
  while (time < 4000) {
    time += 1
    step(1)
    if (peek(c.io.out.valid) == 1 &&
      peek(c.io.out.bits.data("field0").predicate) == 1
    ) {
      result = true
      val data = peek(c.io.out.bits.data("field0").data)
      if (data != 67405054) {
        println(Console.RED + s"*** Incorrect result received. Got $data. Hoping for 67405054" + Console.RESET)
        fail
      } else {
        println(Console.BLUE + s"*** Correct return result received. Run time: $time cycles." + Console.RESET)
      }
    }
  }

  //  Peek into the CopyMem to see if the expected data is written back to the Cache
  var valid_data = true
  for(i <- 0 until outAddrVec.length) {
    poke(c.io.addr, outAddrVec(i))
    step(1)
    val data = peek(c.io.dout)
    if (data != outDataVec(i).toInt) {
      println(Console.RED + s"*** Incorrect data received addr=${outAddrVec(i)}. Got $data. Hoping for ${outDataVec(i).toInt}" + Console.RESET)
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

class cilk_for_test12Tester1 extends FlatSpec with Matchers {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  it should "Check that cilk_for_test12 works correctly." in {
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
      () => new cilk_for_test12Main()) {
      c => new cilk_for_test12Test01(c)
    } should be(true)
  }
}
