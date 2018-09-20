package dataflow

import java.io.PrintWriter
import java.io.File
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


class test05MainIO(implicit val p: Parameters)  extends Module with CoreParams with CacheParams {
  val io = IO( new CoreBundle {
    val in = Flipped(Decoupled(new Call(List(32,32,32))))
    val req = Flipped(Decoupled(new MemReq))
    val resp = Output(Valid(new MemResp))
    val out = Decoupled(new Call(List(32)))
  })
}

class test05Main(implicit p: Parameters) extends test05MainIO {

  val cache = Module(new Cache)            // Simple Nasti Cache
  val memModel = Module(new NastiMemSlave) // Model of DRAM to connect to Cache
  val memCopy = Mem(1024, UInt(32.W))      // Local memory just to keep track of writes to cache for validation


  // Connect the wrapper I/O to the memory model initialization interface so the
  // test bench can write contents at start.
  memModel.io.nasti <> cache.io.nasti
  memModel.io.init.bits.addr := 0.U
  memModel.io.init.bits.data := 0.U
  memModel.io.init.valid := false.B
  cache.io.cpu.abort := false.B

  // Wire up the cache and modules under test.
  val test05 = Module(new test05unDF())

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

class test05bMain(implicit p: Parameters) extends test05MainIO {

  val cache = Module(new Cache)            // Simple Nasti Cache
  val memModel = Module(new NastiMemSlave) // Model of DRAM to connect to Cache
  val memCopy = Mem(1024, UInt(32.W))      // Local memory just to keep track of writes to cache for validation

  // Store a copy of all data written to the cache.  This is done since the cache isn't
  // 'write through' to the memory model and we have no easy way of reading the
  // cache contents from the testbench.
  //  when(cache.io.cpu.req.valid && cache.io.cpu.req.bits.iswrite) {
  //    memCopy.write((cache.io.cpu.req.bits.addr>>2).asUInt(), cache.io.cpu.req.bits.data)
  //  }
  //  io.dout := memCopy.read((io.addr>>2).asUInt())

  // Connect the wrapper I/O to the memory model initialization interface so the
  // test bench can write contents at start.
  memModel.io.nasti <> cache.io.nasti
  memModel.io.init.bits.addr := 0.U
  memModel.io.init.bits.data := 0.U
  memModel.io.init.valid := false.B
  cache.io.cpu.abort := false.B

  // Wire up the cache and modules under test.
  val test05 = Module(new test05DF())
  val test05b = Module(new test05bDF())
  val MemArbiter = Module(new MemArbiter(2))

  MemArbiter.io.cpu.MemReq(0) <> test05b.io.MemReq
  test05b.io.MemResp <> MemArbiter.io.cpu.MemResp(0)
  MemArbiter.io.cpu.MemReq(1) <> test05.io.MemReq
  test05.io.MemResp <> MemArbiter.io.cpu.MemResp(1)

  cache.io.cpu.req <> MemArbiter.io.cache.MemReq
  MemArbiter.io.cache.MemResp <> cache.io.cpu.resp

  test05b.io.in <> io.in
  test05.io.in <> test05b.io.call4_out
  test05b.io.call4_in <> test05.io.out
  io.out <> test05b.io.out

}

class test05cMain(implicit p: Parameters) extends test05MainIO {

  val cache = Module(new Cache)            // Simple Nasti Cache
  val memModel = Module(new NastiMemSlave) // Model of DRAM to connect to Cache
  val memCopy = Mem(1024, UInt(32.W))      // Local memory just to keep track of writes to cache for validation

  // Connect the wrapper I/O to the memory model initialization interface so the
  // test bench can write contents at start.
  memModel.io.nasti <> cache.io.nasti
  memModel.io.init.bits.addr := 0.U
  memModel.io.init.bits.data := 0.U
  memModel.io.init.valid := false.B
  cache.io.cpu.abort := false.B

  // Wire up the cache and modules under test.
  val test05 = Module(new test05DF())
  val test05b = Module(new test05bDF())
  val test05c = Module(new test05cDF())
  val MemArbiter = Module(new MemArbiter(3))

  MemArbiter.io.cpu.MemReq(0) <> test05.io.MemReq
  test05.io.MemResp <> MemArbiter.io.cpu.MemResp(0)
  MemArbiter.io.cpu.MemReq(1) <> test05b.io.MemReq
  test05b.io.MemResp <> MemArbiter.io.cpu.MemResp(1)
  MemArbiter.io.cpu.MemReq(2) <> test05.io.MemReq
  test05.io.MemResp <> MemArbiter.io.cpu.MemResp(2)

  cache.io.cpu.req <> MemArbiter.io.cache.MemReq
  MemArbiter.io.cache.MemResp <> cache.io.cpu.resp

  test05c.io.in <> io.in
  test05b.io.in <> test05c.io.call5_out
  test05.io.in <> test05b.io.call4_out
  test05b.io.call4_in <> test05.io.out
  test05c.io.call5_in <> test05b.io.out
  io.out <> test05c.io.out

}

class test05Test01[T <: test05MainIO](c: T) extends PeekPokeTester(c) {


  def MemRead(addr: Int): BigInt = {
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

  def MemWrite(addr: Int, data: Int): BigInt = {
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

  def dumpMemory(path: String) = {
    //Writing mem states back to the file
    val pw = new PrintWriter(new File(path))
    for (i <- 0 until outDataVec.length) {
      val data = MemRead(outAddrVec(i))
      pw.write("0X" + outAddrVec(i).toHexString + " -> " + data + "\n")
    }
    pw.close

  }


  val inAddrVec = List.range(0, 4*10, 4)
  val inDataVec = List(0,1,2,3,4,0,0,0,0,0)
  val outAddrVec = List.range(0, 4*10, 4)
  val outDataVec = inDataVec.zipWithIndex.map{case(a,b) => if(b < 5) a else (b - 5) * 2}


  // Write initial contents to the memory model.
  for (i <- 0 until inDataVec.length) {
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
  poke(c.io.out.ready, false.B)
  step(1)
  poke(c.io.in.bits.enable.control, true.B)
  poke(c.io.in.valid, true.B)
  poke(c.io.in.bits.data("field0").data, 0)    // Array a[] base address
  poke(c.io.in.bits.data("field0").predicate, true.B)
  poke(c.io.in.bits.data("field1").data, 10)   // n
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
      if (data != 8) {
        println(Console.RED + s"*** Incorrect result received. Got $data. Hoping for 8" + Console.RESET)
        fail
      } else {
        println(Console.BLUE + s"*** Correct return result received. Run time: $time cycles." + Console.RESET)
      }
    }
  }

  step(1000)

  //  Peek into the CopyMem to see if the expected data is written back to the Cache
  var valid_data = true
  for(i <- 0 until outAddrVec.length) {
    val data = MemRead(outAddrVec(i))
    if (data != outDataVec(i).toInt) {
      println(Console.RED + s"*** Incorrect data received. Got $data. Hoping for ${outDataVec(i).toInt}" + Console.RESET)
      fail
      valid_data = false
    }
    else {
      println(Console.BLUE + s"[LOG] MEM[${outAddrVec(i).toInt}] :: $data" + Console.RESET)
    }
  }
  if (valid_data) {
    println(Console.BLUE + "*** Correct data written back." + Console.RESET)
    dumpMemory("memory.txt")
  }


  if(!result) {
    println(Console.RED + "*** Timeout." + Console.RESET)
    dumpMemory("memory.txt")
    fail
  }
}

//class test05Test02[T <: test05MainIO](c: T) extends PeekPokeTester(c) {
//
//  val inAddrVec = List.range(0, 4*10, 4)
//  val inDataVec = List(0,1,2,3,4,5,6,7,8,9)
//  val outAddrVec = List.range(0, 4*10, 4)
//  val outDataVec = List(0,1024,2048,3072,4096,5120,6144,7168,8192,10240)
//
//  poke(c.io.addr, 0.U)
//  poke(c.io.din, 0.U)
//  poke(c.io.write, false.B)
//  var i = 0
//
//  // Write initial contents to the memory model.
//  for(i <- 0 until inAddrVec.length) {
//    poke(c.io.addr, inAddrVec(i))
//    poke(c.io.din, inDataVec(i))
//    poke(c.io.write, true.B)
//    step(1)
//  }
//  poke(c.io.write, false.B)
//  step(1)
//
//  // Initializing the signals
//  poke(c.io.in.bits.enable.control, false.B)
//  poke(c.io.in.valid, false.B)
//  poke(c.io.in.bits.data("field0").data, 0.U)
//  poke(c.io.in.bits.data("field0").taskID, 5.U)
//  poke(c.io.in.bits.data("field0").predicate, false.B)
//  poke(c.io.in.bits.data("field1").data, 0.U)
//  poke(c.io.in.bits.data("field1").taskID, 5.U)
//  poke(c.io.in.bits.data("field1").predicate, false.B)
//  poke(c.io.out.ready, false.B)
//  step(1)
//  poke(c.io.in.bits.enable.control, true.B)
//  poke(c.io.in.valid, true.B)
//  poke(c.io.in.bits.data("field0").data, 0)    // Array a[] base address
//  poke(c.io.in.bits.data("field0").predicate, true.B)
//  poke(c.io.in.bits.data("field1").data, 10)   // n
//  poke(c.io.in.bits.data("field1").predicate, true.B)
//  poke(c.io.out.ready, true.B)
//  step(1)
//  poke(c.io.in.bits.enable.control, false.B)
//  poke(c.io.in.valid, false.B)
//  poke(c.io.in.bits.data("field0").data, 0)
//  poke(c.io.in.bits.data("field0").predicate, false.B)
//  poke(c.io.in.bits.data("field1").data, 0.U)
//  poke(c.io.in.bits.data("field1").predicate, false.B)
//
//  step(1)
//
//  // NOTE: Don't use assert().  It seems to terminate the writing of VCD files
//  // early (before the error) which makes debugging very difficult. Check results
//  // using if() and fail command.
//  var time = 0
//  var result = false
//  while (time < 4000) {
//    time += 1
//    step(1)
//    if (peek(c.io.out.valid) == 1 &&
//      peek(c.io.out.bits.data("field0").predicate) == 1
//    ) {
//      result = true
//      val data = peek(c.io.out.bits.data("field0").data)
//      if (data != 10240) {
//        println(Console.RED + s"*** Incorrect result received. Got $data. Hoping for 10240" + Console.RESET)
//        fail
//      } else {
//        println(Console.BLUE + s"*** Correct return result received. Run time: $time cycles." + Console.RESET)
//      }
//    }
//  }
//
//  //  Peek into the CopyMem to see if the expected data is written back to the Cache
//  var valid_data = true
//  for(i <- 0 until outAddrVec.length) {
//    poke(c.io.addr, outAddrVec(i))
//    step(1)
//    val data = peek(c.io.dout)
//    if (data != outDataVec(i).toInt) {
//      println(Console.RED + s"*** Incorrect data received addr=${outAddrVec(i)}. Got $data. Hoping for ${outDataVec(i).toInt}" + Console.RESET)
//      fail
//      valid_data = false
//    }
//  }
//  if (valid_data) {
//    println(Console.BLUE + "*** Correct data written back." + Console.RESET)
//  }
//
//
//  if(!result) {
//    println(Console.RED + "*** Timeout." + Console.RESET)
//    fail
//  }
//}
//
//class test05Test03[T <: test05MainIO](c: T) extends PeekPokeTester(c) {
//
//  val inAddrVec = List.range(0, 4*8, 4)
//  val inDataVec = List(0,1,2,3,4,5,6,7)
//  val outAddrVec = List.range(0, 4*8, 4)
//  val outDataVec = List(0,16777216,33554432,50331648,67108864,83886080,100663296,134283520)
//
//  poke(c.io.addr, 0.U)
//  poke(c.io.din, 0.U)
//  poke(c.io.write, false.B)
//  var i = 0
//
//  // Write initial contents to the memory model.
//  for(i <- 0 until inAddrVec.length) {
//    poke(c.io.addr, inAddrVec(i))
//    poke(c.io.din, inDataVec(i))
//    poke(c.io.write, true.B)
//    step(1)
//  }
//  poke(c.io.write, false.B)
//  step(1)
//
//  // Initializing the signals
//  poke(c.io.in.bits.enable.control, false.B)
//  poke(c.io.in.valid, false.B)
//  poke(c.io.in.bits.data("field0").data, 0.U)
//  poke(c.io.in.bits.data("field0").taskID, 5.U)
//  poke(c.io.in.bits.data("field0").predicate, false.B)
//  poke(c.io.in.bits.data("field1").data, 0.U)
//  poke(c.io.in.bits.data("field1").taskID, 5.U)
//  poke(c.io.in.bits.data("field1").predicate, false.B)
//  poke(c.io.out.ready, false.B)
//  step(1)
//  poke(c.io.in.bits.enable.control, true.B)
//  poke(c.io.in.valid, true.B)
//  poke(c.io.in.bits.data("field0").data, 0)    // Array a[] base address
//  poke(c.io.in.bits.data("field0").predicate, true.B)
//  poke(c.io.in.bits.data("field1").data, 8)   // n
//  poke(c.io.in.bits.data("field1").predicate, true.B)
//  poke(c.io.out.ready, true.B)
//  step(1)
//  poke(c.io.in.bits.enable.control, false.B)
//  poke(c.io.in.valid, false.B)
//  poke(c.io.in.bits.data("field0").data, 0)
//  poke(c.io.in.bits.data("field0").predicate, false.B)
//  poke(c.io.in.bits.data("field1").data, 0.U)
//  poke(c.io.in.bits.data("field1").predicate, false.B)
//
//  step(1)
//
//  // NOTE: Don't use assert().  It seems to terminate the writing of VCD files
//  // early (before the error) which makes debugging very difficult. Check results
//  // using if() and fail command.
//  var time = 0
//  var result = false
//  while (time < 8000) {
//    time += 1
//    step(1)
//    if (peek(c.io.out.valid) == 1 &&
//      peek(c.io.out.bits.data("field0").predicate) == 1
//    ) {
//      result = true
//      val data = peek(c.io.out.bits.data("field0").data)
//      if (data != 67405056) {
//        println(Console.RED + s"*** Incorrect result received. Got $data. Hoping for 67405056" + Console.RESET)
//        fail
//      } else {
//        println(Console.BLUE + s"*** Correct return result received. Run time: $time cycles." + Console.RESET)
//      }
//    }
//  }
//
//  //  Peek into the CopyMem to see if the expected data is written back to the Cache
//  var valid_data = true
//  for(i <- 0 until outAddrVec.length) {
//    poke(c.io.addr, outAddrVec(i))
//    step(1)
//    val data = peek(c.io.dout)
//    if (data != outDataVec(i).toInt) {
//      println(Console.RED + s"*** Incorrect data received addr=${outAddrVec(i)}. Got $data. Hoping for ${outDataVec(i).toInt}" + Console.RESET)
//      fail
//      valid_data = false
//    }
//  }
//  if (valid_data) {
//    println(Console.BLUE + "*** Correct data written back." + Console.RESET)
//  }
//
//
//  if(!result) {
//    println(Console.RED + "*** Timeout." + Console.RESET)
//    fail
//  }
//}

class test05Tester1 extends FlatSpec with Matchers {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  it should "Check that test05 works correctly." in {
    // iotester flags:
    // -ll  = log level <Error|Warn|Info|Debug|Trace>
    // -tbn = backend <firrtl|verilator|vcs>
    // -td  = target directory
    // -tts = seed for RNG
    chisel3.iotesters.Driver.execute(
      Array(
        // "-ll", "Info",
        "-tbn", "verilator",
        "-td", "test_run_dir/test05",
        "-tts", "0001"),
      () => new test05Main()) {
      c => new test05Test01(c)
    } should be(true)
  }
}

//class test05Tester2 extends FlatSpec with Matchers {
//  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
//  it should "Check that test05b works correctly." in {
//    // iotester flags:
//    // -ll  = log level <Error|Warn|Info|Debug|Trace>
//    // -tbn = backend <firrtl|verilator|vcs>
//    // -td  = target directory
//    // -tts = seed for RNG
//    chisel3.iotesters.Driver.execute(
//      Array(
//        // "-ll", "Info",
//        "-tbn", "verilator",
//        "-td", "test_run_dir",
//        "-tts", "0001"),
//      () => new test05bMain()) {
//      c => new test05Test02(c)
//    } should be(true)
//  }
//}
//
//class test05Tester3 extends FlatSpec with Matchers {
//  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
//  it should "Check that test05b works correctly." in {
//    // iotester flags:
//    // -ll  = log level <Error|Warn|Info|Debug|Trace>
//    // -tbn = backend <firrtl|verilator|vcs>
//    // -td  = target directory
//    // -tts = seed for RNG
//    chisel3.iotesters.Driver.execute(
//      Array(
//        // "-ll", "Info",
//        "-tbn", "verilator",
//        "-td", "test_run_dir",
//        "-tts", "0001"),
//      () => new test05cMain()) {
//      c => new test05Test03(c)
//    } should be(true)
//  }
//}
