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


class cilk_for_test06MainIO(implicit val p: Parameters) extends Module with CoreParams with CacheParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32, 32, 32))))
    val req = Flipped(Decoupled(new MemReq))
    val resp = Output(Valid(new MemResp))
    val out = Decoupled(new Call(List(32)))
  })

  def cloneType = new cilk_for_test06MainIO().asInstanceOf[this.type]
}

class cilk_for_test06MainDirect(implicit p: Parameters) extends cilk_for_test06MainIO {

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
  val cilk_for_test06_detach = Module(new cilk_for_test06_detach1DF())
  val cilk_for_test06 = Module(new cilk_for_test06DF())


  val MemArbiter = Module(new MemArbiter(2))

  MemArbiter.io.cpu.MemReq(0) <> cilk_for_test06_detach.io.MemReq
  cilk_for_test06_detach.io.MemResp <> MemArbiter.io.cpu.MemResp(0)

  MemArbiter.io.cpu.MemReq(1) <> io.req
  io.resp <> MemArbiter.io.cpu.MemResp(1)

  cache.io.cpu.req <> MemArbiter.io.cache.MemReq
  MemArbiter.io.cache.MemResp <> cache.io.cpu.resp


  cilk_for_test06.io.MemResp <> DontCare
  cilk_for_test06.io.MemReq <> DontCare

  cilk_for_test06.io.in <> io.in
  io.out <> cilk_for_test06.io.out

  cilk_for_test06_detach.io.in <> cilk_for_test06.io.call_8_out
  cilk_for_test06.io.call_8_in <> cilk_for_test06_detach.io.out

}

class cilk_for_test06MainTM(tiles: Int)(implicit p: Parameters) extends cilk_for_test06MainIO {

  val cache = Module(new Cache) // Simple Nasti Cache
  val memModel = Module(new NastiMemSlave) // Model of DRAM to connect to Cache

  // Connect the wrapper I/O to the memory model initialization interface so the
  // test bench can write contents at start.
  memModel.io.nasti <> cache.io.nasti
  memModel.io.init.bits.addr := 0.U
  memModel.io.init.bits.data := 0.U
  memModel.io.init.valid := false.B
  cache.io.cpu.abort := false.B

  val cilk_for_testDF = Module(new cilk_for_test06DF())

  val NumTiles = tiles
  val cilk_for_tiles = for (i <- 0 until NumTiles) yield {
    val cilk01 = Module(new cilk_for_test06_detach1DF())
    cilk01
  }

  val TC = Module(new TaskController(List(32, 32, 32, 32), List(), 1, numChild = NumTiles))
  val CacheArb = Module(new MemArbiter(NumTiles + 2))


  // Merge the memory interfaces and connect to the stack memory
  for (i <- 0 until NumTiles) {
    // Connect to stack memory interface
    CacheArb.io.cpu.MemReq(i) <> cilk_for_tiles(i).io.MemReq
    cilk_for_tiles(i).io.MemResp <> CacheArb.io.cpu.MemResp(i)


    // Connect to task controller
    cilk_for_tiles(i).io.in <> TC.io.childOut(i)
    TC.io.childIn(i) <> cilk_for_tiles(i).io.out
  }


  CacheArb.io.cpu.MemReq(NumTiles + 1) <> cilk_for_testDF.io.MemReq
  cilk_for_testDF.io.MemResp <> CacheArb.io.cpu.MemResp(NumTiles + 1)

  TC.io.parentIn(0) <> cilk_for_testDF.io.call_8_out
  cilk_for_testDF.io.call_8_in <> TC.io.parentOut(0)


  CacheArb.io.cpu.MemReq(NumTiles) <> io.req
  io.resp <> CacheArb.io.cpu.MemResp(NumTiles)

  cache.io.cpu.req <> CacheArb.io.cache.MemReq
  CacheArb.io.cache.MemResp <> cache.io.cpu.resp

  cilk_for_testDF.io.in <> io.in
  io.out <> cilk_for_testDF.io.out

}


class cilk_for_test06Test01[T <: cilk_for_test06MainIO](c: T) extends PeekPokeTester(c) {
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


  def dumpMemoryInit(path: String) = {
    //Writing mem states back to the file
    val pw = new PrintWriter(new File(path))
    for (i <- 0 until inDataVec.length) {
      val data = MemRead(inAddrVec(i))
      pw.write("0X" + inAddrVec(i).toHexString + " -> " + data + "\n")
    }
    pw.close

  }


  val inAddrVec = List.range(0, (2*18)*4, 4)
  val inDataVec = List(0, 0, 0, 1, 0, 2, 1, 0, 1, 1, 1, 2, 2, 0, 2, 1, 2, 2,
    7, 5, 1, 1, 3, 5, 2, 1, 9, 2, 4, 3, 3, 3, 0, 0, 6, 2)
  val outAddrVec = List.range(0x100, 0x100 + 0x24, 4)
  val outDataVec = List(74, 1, 18, 2, 65, 10, 10, 5, 16)

  // Write initial contents to the memory model.
  for (i <- 0 until inDataVec.length) {
    MemWrite(inAddrVec(i), inDataVec(i))
  }

  step(10)
  dumpMemoryInit("memory.txt")

  step(1)


  // Initializing the signals
  poke(c.io.in.bits.enable.control, false.B)
  poke(c.io.in.bits.enable.taskID, 0)
  poke(c.io.in.valid, false.B)
  poke(c.io.in.bits.data("field0").data, 0)
  poke(c.io.in.bits.data("field0").taskID, 0)
  poke(c.io.in.bits.data("field0").predicate, false.B)
  poke(c.io.in.bits.data("field1").data, 0)
  poke(c.io.in.bits.data("field1").taskID, 0)
  poke(c.io.in.bits.data("field1").predicate, false.B)
  poke(c.io.in.bits.data("field2").data, 0)
  poke(c.io.in.bits.data("field2").taskID, 0)
  poke(c.io.in.bits.data("field2").predicate, false.B)
  poke(c.io.out.ready, false.B)
  step(1)
  poke(c.io.in.bits.enable.control, true.B)
  poke(c.io.in.valid, true.B)
  poke(c.io.in.bits.data("field0").data, 0) // Array a[] base address
  poke(c.io.in.bits.data("field0").predicate, true)
  poke(c.io.in.bits.data("field1").data, 0x48) // Array b[] base address
  poke(c.io.in.bits.data("field1").predicate, true)
  poke(c.io.in.bits.data("field2").data, 0x100) // Array c[] base address
  poke(c.io.in.bits.data("field2").predicate, true)
  poke(c.io.out.ready, true)
  step(1)
  poke(c.io.in.bits.enable.control, false)
  poke(c.io.in.valid, false)
  poke(c.io.in.bits.data("field0").data, 0)
  poke(c.io.in.bits.data("field0").predicate, false)
  poke(c.io.in.bits.data("field1").data, 0)
  poke(c.io.in.bits.data("field1").predicate, false)
  poke(c.io.in.bits.data("field2").data, 0)
  poke(c.io.in.bits.data("field2").predicate, false)

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
  }
  if (!result) {
    println(Console.RED + "*** Timeout." + Console.RESET)
    fail
  }
}

class cilk_for_test06Tester1 extends FlatSpec with Matchers {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  it should "Check that cilk_for_test06 works correctly." in {
    // iotester flags:
    // -ll  = log level <Error|Warn|Info|Debug|Trace>
    // -tbn = backend <firrtl|verilator|vcs>
    // -td  = target directory
    // -tts = seed for RNG
    chisel3.iotesters.Driver.execute(
      Array(
        // "-ll", "Info",
        "-tbn", "verilator",
        "-td", "test_run_dir/cilk_for_test06/test2/",
        "-tts", "0001"),
      () => new cilk_for_test06MainDirect()) {
      c => new cilk_for_test06Test01(c)
    } should be(true)
  }
}

class cilk_for_test06Tester2 extends FlatSpec with Matchers {
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
        "-td", "test_run_dir/cilk_for_test06/test2/",
        "-tts", "0001"),
      () => new cilk_for_test06MainTM(1)) {
      c => new cilk_for_test06Test01(c)
    } should be(true)
  }
}
