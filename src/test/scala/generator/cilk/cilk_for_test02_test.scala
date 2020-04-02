package dandelion.generator.cilk

import chisel3._
import chisel3.Module
import org.scalatest.{FlatSpec, Matchers}
import chipsalliance.rocketchip.config._
import dandelion.config._
import dandelion.concurrent.{TaskController, TaskControllerIO}
import dandelion.memory._
import dandelion.accel._
import dandelion.interfaces.NastiMemSlave
import dandelion.memory.cache.ReferenceCache
import helpers.AccelTesterLocal
import helpers.AccelIO


class cilk_for_test02Main(tiles: Int)(implicit p: Parameters) extends AccelIO(List(32), List(32)) {

  val cache = Module(new ReferenceCache) // Simple Nasti Cache
  val memModel = Module(new NastiMemSlave) // Model of DRAM to connect to Cache

  // Connect the wrapper I/O to the memory model initialization interface so the
  // test bench can write contents at start.
  memModel.io.nasti <> cache.io.mem
  memModel.io.init.bits.addr := 0.U
  memModel.io.init.bits.data := 0.U
  memModel.io.init.valid := false.B
  cache.io.cpu.abort := false.B

  val cilk_for_testDF = Module(new cilk_for_test02DF())

  val NumTiles = tiles
  val cilk_for_outter_detach1 = for (i <- 0 until NumTiles) yield {
    val cilk02 = Module(new cilk_for_test02_detach1DF())
    cilk02
  }
  val cilk_for_inner_detach2 = for (i <- 0 until NumTiles) yield {
    val cilk02 = Module(new cilk_for_test02_detach2DF())
    cilk02
  }


  val TC = Module(new TaskController(List(32), List(), 1, numChild = NumTiles))
  val CacheArb = Module(new MemArbiter((NumTiles * 2) + 2))


  // Merge the memory interfaces and connect to the stack memory
  for (i <- 0 until NumTiles) {
    // Connect to stack memory interface
    CacheArb.io.cpu.MemReq(i) <> cilk_for_outter_detach1(i).io.MemReq
    cilk_for_outter_detach1(i).io.MemResp <> CacheArb.io.cpu.MemResp(i)


    // Connect to task controller
    cilk_for_outter_detach1(i).io.in <> TC.io.childOut(i)
    TC.io.childIn(i) <> cilk_for_outter_detach1(i).io.out

    cilk_for_inner_detach2(i).io.in <> cilk_for_outter_detach1(i).io.call_8_out
    cilk_for_outter_detach1(i).io.call_8_in <> cilk_for_inner_detach2(i).io.out

    CacheArb.io.cpu.MemReq(i + NumTiles) <> cilk_for_inner_detach2(i).io.MemReq
    cilk_for_inner_detach2(i).io.MemResp <> CacheArb.io.cpu.MemResp(i + NumTiles)
  }


  CacheArb.io.cpu.MemReq((NumTiles * 2) + 1) <> cilk_for_testDF.io.MemReq
  cilk_for_testDF.io.MemResp <> CacheArb.io.cpu.MemResp((NumTiles * 2) + 1)

  TC.io.parentIn(0) <> cilk_for_testDF.io.call_12_out
  cilk_for_testDF.io.call_12_in <> TC.io.parentOut(0)


  CacheArb.io.cpu.MemReq(NumTiles * 2) <> io.req
  io.resp <> CacheArb.io.cpu.MemResp(NumTiles * 2)

  cache.io.cpu.req <> CacheArb.io.cache.MemReq
  CacheArb.io.cache.MemResp <> cache.io.cpu.resp

  cilk_for_testDF.io.in <> io.in
  io.out <> cilk_for_testDF.io.out

}


class cilk_for_test02Test02[T <: AccelIO](c: T, n: Int, tiles: Int)
                                         (inAddrVec: List[Int], inDataVec: List[Int],
                                          outAddrVec: List[Int], outDataVec: List[Int])
  extends AccelTesterLocal(c)(inAddrVec, inDataVec, outAddrVec, outDataVec) {


  initMemory()

  // Initializing the signals
  poke(c.io.in.bits.enable.control, false)
  poke(c.io.in.valid, false)
  poke(c.io.in.bits.data("field0").data, 0)
  poke(c.io.in.bits.data("field0").taskID, 0)
  poke(c.io.in.bits.data("field0").predicate, false)
  poke(c.io.out.ready, false)
  step(1)
  poke(c.io.in.bits.enable.control, true)
  poke(c.io.in.valid, true)
  poke(c.io.in.bits.data("field0").data, 100) //unsigned j = 100. Initial value
  poke(c.io.in.bits.data("field0").predicate, true)
  poke(c.io.out.ready, true)
  step(1)
  poke(c.io.in.bits.enable.control, false)
  poke(c.io.in.valid, false)
  poke(c.io.in.bits.data("field0").data, 0)
  poke(c.io.in.bits.data("field0").predicate, false)

  step(1)

  // NOTE: Don't use assert().  It seems to terminate the writing of VCD files
  // early (before the error) which makes debugging very difficult. Check results
  // using if() and fail command.
  var time = 0
  var result = false
  while (time < 2000 && !result) {
    time += 1
    step(1)
    if (peek(c.io.out.valid) == 1 &&
      peek(c.io.out.bits.data("field0").predicate) == 1
    ) {
      result = true
      val data = peek(c.io.out.bits.data("field0").data)
      if (data != 125) {
        println(Console.RED + s"*** Incorrect result received. Got $data. Hoping for 125" + Console.RESET)
        fail
      } else {
        println(Console.BLUE + s"*** Correct return result received. Run time: $time cycles." + Console.RESET)
      }
    }
  }

  //The kernel is a computation only kernel
  //checkMemory()
  //  Peek into the CopyMem to see if the expected data is written back to the Cache

  if (!result) {
    println(Console.RED + "*** Timeout." + Console.RESET)
    fail
  }
}

class cilk_for_test02Tester1 extends FlatSpec with Matchers {

  val inAddrVec = List(0)
  val inDataVec = List(0)
  val outAddrVec = List(0)
  val outDataVec = List(0)

  implicit val p = new WithAccelConfig ++ new WithTestConfig


  val tile_list = List(1)
  for (tile <- tile_list) {
    it should "Check that cilk_for_test02 works correctly." in {


      // iotester flags:
      // -ll  = log level <Error|Warn|Info|Debug|Trace>
      // -tbn = backend <firrtl|verilator|vcs>
      // -td  = target directory
      // -tts = seed for RNG
      chisel3.iotesters.Driver.execute(
        Array(
          "-ll", "Error",
          "-tn", "cilk_for_test02Main",
          "-tbn", "verilator",
          "-td", "test_run_dir/cilk_for_test02",
          "-tts", "0002"),
        () => new cilk_for_test02Main(tiles = tile)(p)) {
        c => new cilk_for_test02Test02(c, 5, tile)(inAddrVec, inDataVec, outAddrVec, outDataVec)
      } should be(true)
    }
  }
}
