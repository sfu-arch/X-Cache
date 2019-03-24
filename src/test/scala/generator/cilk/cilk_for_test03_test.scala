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


class cilk_for_test03MainIO(implicit val p: Parameters) extends Module with CoreParams with CacheParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32, 32, 32))))
    val req = Flipped(Decoupled(new MemReq))
    val resp = Output(Valid(new MemResp))
    val out = Decoupled(new Call(List(32)))
  })

  def cloneType = new cilk_for_test03MainIO().asInstanceOf[this.type]
}


class cilk_for_test03Main1(tiles: Int)(implicit p: Parameters) extends cilk_for_test03MainIO {

  val cache = Module(new Cache) // Simple Nasti Cache
  val memModel = Module(new NastiMemSlave) // Model of DRAM to connect to Cache

  // Connect the wrapper I/O to the memory model initialization interface so the
  // test bench can write contents at start.
  memModel.io.nasti <> cache.io.nasti
  memModel.io.init.bits.addr := 0.U
  memModel.io.init.bits.data := 0.U
  memModel.io.init.valid := false.B
  cache.io.cpu.abort := false.B

  val cilk_for_testDF = Module(new cilk_for_test03DF())
  cilk_for_testDF.io.MemReq <> DontCare
  cilk_for_testDF.io.MemResp <> DontCare

  val NumTiles = tiles
  val cilk_for_tiles = for (i <- 0 until NumTiles) yield {
    val cilk03 = Module(new cilk_for_test03_detach1DF())
    cilk03
  }

  val TC = Module(new TaskController(List(32, 32, 32, 32), List(), 1, numChild = NumTiles))
  val CacheArb = Module(new MemArbiter(NumTiles + 1))


  // Merge the memory interfaces and connect to the stack memory
  for (i <- 0 until NumTiles) {
    // Connect to stack memory interface
    CacheArb.io.cpu.MemReq(i) <> cilk_for_tiles(i).io.MemReq
    cilk_for_tiles(i).io.MemResp <> CacheArb.io.cpu.MemResp(i)


    // Connect to task controller
    cilk_for_tiles(i).io.in <> TC.io.childOut(i)
    TC.io.childIn(i) <> cilk_for_tiles(i).io.out
  }

  TC.io.parentIn(0) <> cilk_for_testDF.io.call_8_out
  cilk_for_testDF.io.call_8_in <> TC.io.parentOut(0)


  CacheArb.io.cpu.MemReq(NumTiles) <> io.req
  io.resp <> CacheArb.io.cpu.MemResp(NumTiles)

  cache.io.cpu.req <> CacheArb.io.cache.MemReq
  CacheArb.io.cache.MemResp <> cache.io.cpu.resp

  cilk_for_testDF.io.in <> io.in
  io.out <> cilk_for_testDF.io.out

}

class cilk_for_test03SuperCache(NumTiles: Int = 1, NumBanks: Int = 2)(implicit p: Parameters) extends cilk_for_test03MainIO {

  val cache = Module(new NParallelCache(NumTiles = NumTiles + 1, NumBanks)) // Simple Nasti Cache

  val memModels = for (i <- 0 until NumBanks) yield {
    val mem = Module(new NastiMemSlave) // Model of DRAM to connect to Cache
    mem
  }

  // Connect the wrapper I/O to the memory model initialization interface so the
  // test bench can write contents at start.
  for (i <- 0 until NumBanks) {
    memModels(i).io.nasti <> cache.io.nasti(i)
    memModels(i).io.init.bits.addr := 0.U
    memModels(i).io.init.bits.data := 0.U
    memModels(i).io.init.valid := false.B
  }

  val cilk_for_testDF = Module(new cilk_for_test03DF())
  cilk_for_testDF.io.MemReq <> DontCare
  cilk_for_testDF.io.MemResp <> DontCare

  val cilk_for_tiles = for (i <- 0 until NumTiles) yield {
    val cilk03 = Module(new cilk_for_test03_detach1DF())
    cilk03
  }


  val TC = Module(new TaskController(List(32, 32, 32, 32), List(), 1, numChild = NumTiles))

  // Merge the memory interfaces and connect to the stack memory
  for (i <- 0 until NumTiles) {
    // Connect to stack memory interface
    cache.io.cpu.MemReq(i) <> cilk_for_tiles(i).io.MemReq
    cilk_for_tiles(i).io.MemResp <> cache.io.cpu.MemResp(i)


    // Connect to task controller
    cilk_for_tiles(i).io.in <> TC.io.childOut(i)
    TC.io.childIn(i) <> cilk_for_tiles(i).io.out
  }

  TC.io.parentIn(0) <> cilk_for_testDF.io.call_8_out
  cilk_for_testDF.io.call_8_in <> TC.io.parentOut(0)


  cache.io.cpu.MemReq(NumTiles) <> io.req
  io.resp <> cache.io.cpu.MemResp(NumTiles)

  cilk_for_testDF.io.in <> io.in
  io.out <> cilk_for_testDF.io.out

}


class cilk_for_test03Test01[T <: cilk_for_test03MainIO](c: T, tiles: Int) extends PeekPokeTester(c) {

  def MemRead(addr: Int): BigInt = {
    while (peek(c.io.req.ready) == 0) {
      step(1)
    }
    poke(c.io.req.valid, 1)
    poke(c.io.req.bits.addr, addr)
    poke(c.io.req.bits.iswrite, 0)
    poke(c.io.req.bits.tag, 0)
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


  val inAddrVec = List.range(0, 4 * 20, 4)
  val inDataVec = List.range(1, 11, 1) ++ List.range(1, 11, 1)
  val outAddrVec = List.range(80, 80 + (4 * 10), 4)
  val outDataVec = List.range(1, 11, 1).map(_ * 2)

  // Write initial contents to the memory model.
  for (i <- 0 until inAddrVec.length) {
    MemWrite(inAddrVec(i), inDataVec(i))
  }

  step(1)
  dumpMemory("init.mem")
  step(20)


  // Initializing the signals
  poke(c.io.in.bits.enable.control, false)
  poke(c.io.in.bits.enable.taskID, 0)
  poke(c.io.in.valid, false)
  poke(c.io.in.bits.data("field0").data, 0)
  poke(c.io.in.bits.data("field0").taskID, 0)
  poke(c.io.in.bits.data("field0").predicate, false)
  poke(c.io.in.bits.data("field1").data, 0)
  poke(c.io.in.bits.data("field1").taskID, 0)
  poke(c.io.in.bits.data("field1").predicate, false)
  poke(c.io.in.bits.data("field2").data, 0)
  poke(c.io.in.bits.data("field2").taskID, 0)
  poke(c.io.in.bits.data("field2").predicate, false)
  poke(c.io.out.ready, false.B)
  step(1)
  poke(c.io.in.bits.enable.control, true.B)
  poke(c.io.in.valid, true.B)
  poke(c.io.in.bits.data("field0").data, 0) // Array a[] base address
  poke(c.io.in.bits.data("field0").predicate, true)
  poke(c.io.in.bits.data("field1").data, 40) // Array b[] base address
  poke(c.io.in.bits.data("field1").predicate, true)
  poke(c.io.in.bits.data("field2").data, 80) // Array c[] base address
  poke(c.io.in.bits.data("field2").predicate, true)
  poke(c.io.out.ready, true.B)
  step(1)
  poke(c.io.in.bits.enable.control, false.B)
  poke(c.io.in.valid, false.B)
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
  while (time < 20000 && !result) {
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

  step(10)

  var valid_data = true
  for (i <- 0 until outDataVec.length) {
    val data = MemRead(outAddrVec(i))
    if (data != outDataVec(i)) {
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

  dumpMemory("finish.mem")

  if (!result) {
    println(Console.RED + "*** Timeout." + Console.RESET)
    fail
  }
}



class cilk_for_test03Test02Cache[T <: cilk_for_test03MainIO](c: T, tiles: Int) extends PeekPokeTester(c) {

  var writesync = 0

  /**
    * cacheDF interface:
    *
    * in = Flipped(Decoupled(new Call(List(...))))
    * out = Decoupled(new Call(List(32)))
    */

  def MemReadBlocking(addr: Int): BigInt = {
    while (peek(c.io.req.ready) == 0) {
      step(1)
    }
    poke(c.io.req.valid, 1)
    poke(c.io.req.bits.addr, addr)
    poke(c.io.req.bits.iswrite, 0)
    poke(c.io.req.bits.tag, 10)
    poke(c.io.req.bits.mask, (1 << (c.io.req.bits.mask.getWidth)) - 1)
    step(1)
    poke(c.io.req.valid, 0)
    while (peek(c.io.resp.valid) == 0) {
      step(1)
    }
    val result = peek(c.io.resp.bits.data)
    result
  }

  def MemWriteNonBlocking(addr: Int, data: Int, tag: Int) = {
    while (peek(c.io.req.ready) == 0) {
      step(1)
      MemWriteSyncCount( )
    }
    poke(c.io.req.valid, 1)
    poke(c.io.req.bits.addr, addr)
    poke(c.io.req.bits.data, data)
    poke(c.io.req.bits.iswrite, 1)
    poke(c.io.req.bits.tag, 20 + tag)
    //    [HACK]. Chisel and/or Verilator has a bug where it does not model 8 bit values correctly.
    // If you set this to a value exceeding the bitwidth; it will overwrite the neighboring fields in the msg.
    poke(c.io.req.bits.mask, (1 << (c.io.req.bits.mask.getWidth)) - 1)
    //poke(c.io.req.bits.mask, -1)
    step(1)
    MemWriteSyncCount( )
    poke(c.io.req.valid, 0)
  }


  def MemWriteSyncCount() = {
    if (peek(c.io.resp.valid) == 1) {
      writesync += 1
    }
  }


  def dumpMemory(path: String) = {
    //Writing mem states back to the file
    val pw = new PrintWriter(new File(path))
    for (i <- 0 until outDataVec.length) {
      //      for (i <- 0 until outDataVec.length) {
      val data = MemReadBlocking(outAddrVec(i))
      pw.write("0X" + outAddrVec(i).toHexString + " -> " + data + "\n")
    }
    pw.close

  }


  val inAddrVec = List.range(0, 4 * 10, 4) ++ List.range(0x1000, 0x1000 + (4 * 10), 4)

  val inDataVec = List.range(1, 11, 1) ++ List.range(1, 11, 1)
  val outAddrVec = List.range(80, 80 + (4 * 10), 4)
  val outDataVec = List.range(1, 11, 1).map(_ * 2)

  var i = 0
  poke(c.io.req.valid, 0)
  // Write initial contents to the memory model. All writes are non blocking
  for (i <- 0 until inDataVec.length) {
    MemWriteNonBlocking(inAddrVec(i), inDataVec(i), i)
  }

  //  Write fence. Ensures all initial writes are done.
  while (writesync != inAddrVec.length) {
    step(1)
    MemWriteSyncCount( )
  }

  println("WriteSync" + writesync)


  dumpMemory("init.mem")

  step(10)



  // Initializing the signals
  poke(c.io.in.bits.enable.control, false)
  poke(c.io.in.bits.enable.taskID, 0)
  poke(c.io.in.valid, false)
  poke(c.io.in.bits.data("field0").data, 0)
  poke(c.io.in.bits.data("field0").taskID, 0)
  poke(c.io.in.bits.data("field0").predicate, false)
  poke(c.io.in.bits.data("field1").data, 0)
  poke(c.io.in.bits.data("field1").taskID, 0)
  poke(c.io.in.bits.data("field1").predicate, false)
  poke(c.io.in.bits.data("field2").data, 0)
  poke(c.io.in.bits.data("field2").taskID, 0)
  poke(c.io.in.bits.data("field2").predicate, false)
  poke(c.io.out.ready, false.B)
  step(1)
  poke(c.io.in.bits.enable.control, true.B)
  poke(c.io.in.valid, true.B)
  poke(c.io.in.bits.data("field0").data, 0) // Array a[] base address
  poke(c.io.in.bits.data("field0").predicate, true)
  poke(c.io.in.bits.data("field1").data, 0x1000) // Array b[] base address
  poke(c.io.in.bits.data("field1").predicate, true)
  poke(c.io.in.bits.data("field2").data, 80) // Array c[] base address
  poke(c.io.in.bits.data("field2").predicate, true)
  poke(c.io.out.ready, true.B)
  step(1)
  poke(c.io.in.bits.enable.control, false.B)
  poke(c.io.in.valid, false.B)
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
  while (time < 20000 && !result) {
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

  step(10)

  var valid_data = true
  for (i <- 0 until outDataVec.length) {
    val data = MemReadBlocking(outAddrVec(i))
    if (data != outDataVec(i)) {
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

  dumpMemory("finish.mem")

  if (!result) {
    println(Console.RED + "*** Timeout." + Console.RESET)
    fail
  }
}



class cilk_for_test02Tester1 extends FlatSpec with Matchers {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  it should "Check that cilk_for_test03 works correctly." in {
    // iotester flags:
    // -ll  = log level <Error|Warn|Info|Debug|Trace>
    // -tbn = backend <firrtl|verilator|vcs>
    // -td  = target directory
    // -tts = seed for RNG
    val Tiles = List(2)
    for (tile <- Tiles) {
      chisel3.iotesters.Driver.execute(
        Array(
          // "-ll", "Info",
          "-tbn", "verilator",
          "-td", "test_run_dir/cilk_for_test03",
          "-tts", "0001"),
        () => new cilk_for_test03Main1(tile)(p.alterPartial({ case TLEN => 6 }))) {
        c => new cilk_for_test03Test01(c, tile)
      } should be(true)
    }
  }
}

//class cilk_for_test03Tester2 extends FlatSpec with Matchers {
//  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
//  it should "Check that cilk_for_test03 works correctly." in {
//    // iotester flags:
//    // -ll  = log level <Error|Warn|Info|Debug|Trace>
//    // -tbn = backend <firrtl|verilator|vcs>
//    // -td  = target directory
//    // -tts = seed for RNG
//    val Tiles = List(2)
//    for (tile <- Tiles) {
//      chisel3.iotesters.Driver.execute(
//        Array(
//          // "-ll", "Info",
//          "-tbn", "verilator",
//          "-td", "test_run_dir/cilk_for_test03",
//          "-tts", "0001"),
//        () => new cilk_for_test03SuperCache(tile)(p.alterPartial({ case TLEN => 6 }))) {
//        c => new cilk_for_test03Test02Cache(c, tile)
//      } should be(true)
//    }
//  }
//}
