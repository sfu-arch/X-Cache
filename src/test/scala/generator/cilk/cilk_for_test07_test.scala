package dandelion.generator.cilk


import chisel3._
import chisel3.Module
import org.scalatest.{FlatSpec, Matchers}
import dandelion.concurrent.{TaskController, TaskControllerIO}
import chipsalliance.rocketchip.config._
import dandelion.config._
import dandelion.memory._
import dandelion.accel._
import dandelion.interfaces.NastiMemSlave
import dandelion.memory.cache.ReferenceCache
import helpers._


class cilk_for_test07MainDirect(implicit p: Parameters)
  extends AccelIO(List(32, 32), List(32)) {

  val cache = Module(new ReferenceCache) // Simple Nasti Cache
  val memModel = Module(new NastiMemSlave) // Model of DRAM to connect to Cache

  // Connect the wrapper I/O to the memory model initialization interface so the
  // test bench can write contents at start.
  memModel.io.nasti <> cache.io.mem
  memModel.io.init.bits.addr := 0.U
  memModel.io.init.bits.data := 0.U
  memModel.io.init.valid := false.B
    cache.io.cpu.abort := false.B
  cache.io.cpu.flush := false.B

  // Wire up the cache and modules under test.
  val cilk_for_test07_detach = Module(new cilk_for_test07_detach1DF())
  val cilk_for_test07 = Module(new cilk_for_test07DF())

  val MemArbiter = Module(new MemArbiter(2))

  MemArbiter.io.cpu.MemReq(0) <> cilk_for_test07_detach.io.MemReq
  cilk_for_test07_detach.io.MemResp <> MemArbiter.io.cpu.MemResp(0)

  MemArbiter.io.cpu.MemReq(1) <> io.req
  io.resp <> MemArbiter.io.cpu.MemResp(1)

  cache.io.cpu.req <> MemArbiter.io.cache.MemReq
  MemArbiter.io.cache.MemResp <> cache.io.cpu.resp


  cilk_for_test07.io.MemResp <> DontCare
  cilk_for_test07.io.MemReq <> DontCare

  cilk_for_test07.io.in <> io.in
  io.out <> cilk_for_test07.io.out

  cilk_for_test07_detach.io.in <> cilk_for_test07.io.call_8_out
  cilk_for_test07.io.call_8_in <> cilk_for_test07_detach.io.out


}

class cilk_for_test07MainTM(tiles: Int)(implicit p: Parameters)
  extends AccelIO(List(32, 32), List(32)) {

  val cache = Module(new ReferenceCache) // Simple Nasti Cache
  val memModel = Module(new NastiMemSlave) // Model of DRAM to connect to Cache

  // Connect the wrapper I/O to the memory model initialization interface so the
  // test bench can write contents at start.
  memModel.io.nasti <> cache.io.mem
  memModel.io.init.bits.addr := 0.U
  memModel.io.init.bits.data := 0.U
  memModel.io.init.valid := false.B
    cache.io.cpu.abort := false.B
  cache.io.cpu.flush := false.B

  val cilk_for_testDF = Module(new cilk_for_test07DF())

  val NumTiles = tiles
  val cilk_for_tiles = for (i <- 0 until NumTiles) yield {
    val cilk01 = Module(new cilk_for_test07_detach1DF())
    cilk01
  }

  val TC = Module(new TaskController(List(32, 32, 32), List(), 1, numChild = NumTiles))
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


  CacheArb.io.cpu.MemReq(NumTiles) <> cilk_for_testDF.io.MemReq
  cilk_for_testDF.io.MemResp <> CacheArb.io.cpu.MemResp(NumTiles)

  TC.io.parentIn(0) <> cilk_for_testDF.io.call_8_out
  cilk_for_testDF.io.call_8_in <> TC.io.parentOut(0)


  CacheArb.io.cpu.MemReq(NumTiles + 1) <> io.req
  io.resp <> CacheArb.io.cpu.MemResp(NumTiles + 1)

  cache.io.cpu.req <> CacheArb.io.cache.MemReq
  CacheArb.io.cache.MemResp <> cache.io.cpu.resp

  cilk_for_testDF.io.in <> io.in
  io.out <> cilk_for_testDF.io.out

}


class cilk_for_test07Test01[T <: AccelIO](c: T, tiles: Int)
                                         (inAddrVec: List[Int], inDataVec: List[Int],
                                          outAddrVec: List[Int], outDataVec: List[Int])
  extends AccelTesterLocal(c)(inAddrVec, inDataVec, outAddrVec, outDataVec) {


  val pixels = 8

  initMemory()

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
  poke(c.io.out.ready, false)
  step(1)
  poke(c.io.in.bits.enable.control, true)
  poke(c.io.in.valid, true)
  poke(c.io.in.bits.data("field0").data, 0) // Array rgb[] base address
  poke(c.io.in.bits.data("field0").predicate, true)
  poke(c.io.in.bits.data("field1").data, 256) // Array xyz[] base address
  poke(c.io.in.bits.data("field1").predicate, true)
  poke(c.io.out.ready, true)
  step(1)
  poke(c.io.in.bits.enable.control, false)
  poke(c.io.in.valid, false)
  poke(c.io.in.bits.data("field0").data, 0)
  poke(c.io.in.bits.data("field0").predicate, false)
  poke(c.io.in.bits.data("field1").data, 0.U)
  poke(c.io.in.bits.data("field1").predicate, false)

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

  checkMemory()

  if (!result) {
    println(Console.RED + "*** Timeout." + Console.RESET)
    fail
  }
}

class cilk_for_test07Tester1 extends FlatSpec with Matchers {
  val pixels = 8
  val inAddrVec = List.range(0, pixels * 3 * 4, 4)
  val inDataVec = List(1, 1, 1, 0, 10, 3, 0, 2, 5,
    255, 192, 32, 52, 71, 98, 31, 27, 99,
    12, 77, 52, 128, 7, 7)
  val outAddrVec = List.range(256, 256 + (pixels * 3 * 4), 4)
  val outDataVec = List(0, 0, 1, 4, 7, 4, 1, 1, 4,
    179, 193, 58, 64, 68, 102, 40, 33, 97,
    41, 61, 58, 56, 32, 9)

  implicit val p = new WithAccelConfig ++ new WithTestConfig
  it should "Check that cilk_for_test07 works correctly." in {
    // iotester flags:
    // -ll  = log level <Error|Warn|Info|Debug|Trace>
    // -tbn = backend <firrtl|verilator|vcs>
    // -td  = target directory
    // -tts = seed for RNG
    chisel3.iotesters.Driver.execute(
      Array(
        // "-ll", "Info",
        "-tbn", "verilator",
        "-td", "test_run_dir/cilk_for_test07_1",
        "-tts", "0001"),
      () => new cilk_for_test07MainDirect()) {
      c => new cilk_for_test07Test01(c, 1)(inAddrVec, inDataVec, outAddrVec, outDataVec)
    } should be(true)
  }
}

class cilk_for_test07Tester2 extends FlatSpec with Matchers {
  val pixels = 8
  val inAddrVec = List.range(0, pixels * 3 * 4, 4)
  val inDataVec = List(1, 1, 1, 0, 10, 3, 0, 2, 5,
    255, 192, 32, 52, 71, 98, 31, 27, 99,
    12, 77, 52, 128, 7, 7)
  val outAddrVec = List.range(256, 256 + (pixels * 3 * 4), 4)
  val outDataVec = List(0, 0, 1, 4, 7, 4, 1, 1, 4,
    179, 193, 58, 64, 68, 102, 40, 33, 97,
    41, 61, 58, 56, 32, 9)

  implicit val p = new WithAccelConfig ++ new WithTestConfig
  // iotester flags:
  // -ll  = log level <Error|Warn|Info|Debug|Trace>
  // -tbn = backend <firrtl|verilator|vcs>
  // -td  = target directory
  // -tts = seed for RNG
  it should "Check that cilk_for_test02 works when called via task manager." in {

    val Tiles = List(4)
    for (tile <- Tiles) {
      chisel3.iotesters.Driver.execute(
        Array(
          // "-ll", "Info",
          "-tbn", "verilator",
          "-td", s"test_run_dir/cilk_for_test07_${tile}",
          "-tts", "0001"),
        () => new cilk_for_test07MainTM(tiles = tile)) {
        c => new cilk_for_test07Test01(c, tile)(inAddrVec, inDataVec, outAddrVec, outDataVec)
      } should be(true)
    }
  }
}
