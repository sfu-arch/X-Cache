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


class cilk_for_test05MainIO(implicit val p: Parameters) extends Module with CoreParams with CacheParams {
  val io = IO(new CoreBundle {
    val in = Flipped(Decoupled(new Call(List(32, 32, 32))))
    val req = Flipped(Decoupled(new MemReq))
    val resp = Output(Valid(new MemResp))
    val out = Decoupled(new Call(List(32)))
  })
}


class cilk_for_test05Main(tiles: Int)(implicit p: Parameters) extends cilk_for_test05MainIO {

  val cache = Module(new Cache) // Simple Nasti Cache
  val memModel = Module(new NastiMemSlave) // Model of DRAM to connect to Cache

  // Connect the wrapper I/O to the memory model initialization interface so the
  // test bench can write contents at start.
  memModel.io.nasti <> cache.io.nasti
  memModel.io.init.bits.addr := 0.U
  memModel.io.init.bits.data := 0.U
  memModel.io.init.valid := false.B
  cache.io.cpu.abort := false.B

  val cilk_for_testDF = Module(new cilk_for_test05DF())

  val NumTiles = tiles
  val cilk_for_tiles = for (i <- 0 until NumTiles) yield {
    val cilk05 = Module(new cilk_for_test05_detach1DF())
    cilk05
  }

  val TC = Module(new TaskController(List(32, 32, 32, 32), List(32), 1, numChild = NumTiles))
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

  TC.io.parentIn(0) <> cilk_for_testDF.io.call_9_out
  cilk_for_testDF.io.call_9_in <> TC.io.parentOut(0)


  CacheArb.io.cpu.MemReq(NumTiles) <> io.req
  io.resp <> CacheArb.io.cpu.MemResp(NumTiles)

  cache.io.cpu.req <> CacheArb.io.cache.MemReq
  CacheArb.io.cache.MemResp <> cache.io.cpu.resp

  cilk_for_testDF.io.in <> io.in
  io.out <> cilk_for_testDF.io.out

}


//class cilk_for_test05CacheWrapper()(implicit p: Parameters) extends cilk_for_test05DF()(p)
//  with CacheParams {
//  /*  val io2 = IO(new Bundle {
//      val init  = Flipped(Valid(new InitParams()(p)))
//    })
//    */
//  // Instantiate the AXI Cache
//  val cache = Module(new Cache)
//  cache.io.cpu.req <> CacheMem.io.MemReq
//  CacheMem.io.MemResp <> cache.io.cpu.resp
//  cache.io.cpu.abort := false.B
//  // Instantiate a memory model with AXI slave interface for cache
//  val memModel = Module(new NastiMemSlave)
//  memModel.io.nasti <> cache.io.nasti
//  //memModel.io.init <> io2.init
//
//  val raminit = RegInit(true.B)
//  val addrVec = VecInit(0.U, 1.U, 2.U, 3.U, 4.U, 5.U, 6.U, 7.U, 8.U, 9.U,
//    10.U, 11.U, 12.U, 13.U, 14.U, 15.U, 16.U, 17.U, 18.U, 19.U,
//    32.U, 33.U, 34.U, 35.U, 36.U, 37.U, 38.U, 39.U, 40.U, 41.U,
//    42.U, 43.U, 44.U, 45.U, 46.U, 47.U, 48.U, 49.U, 50.U, 51.U)
//  val dataVec = VecInit(0.U, 1.U, 2.U, 3.U, 4.U, 5.U, 6.U, 7.U, 8.U, 9.U,
//    10.U, 11.U, 12.U, 13.U, 14.U, 15.U, 16.U, 17.U, 18.U, 19.U,
//    10.U, 11.U, 12.U, 13.U, 14.U, 15.U, 16.U, 17.U, 18.U, 19.U,
//    0.U, 1.U, 2.U, 3.U, 4.U, 5.U, 6.U, 7.U, 8.U, 9.U)
//  val (count_out, count_done) = Counter(raminit, addrVec.length)
//  memModel.io.init.bits.addr := addrVec(count_out)
//  memModel.io.init.bits.data := dataVec(count_out)
//  when(!count_done) {
//    memModel.io.init.valid := true.B
//  }.otherwise {
//    memModel.io.init.valid := false.B
//    raminit := false.B
//  }
//
//}

//abstract class cilk_for_test05MainIO(implicit val p: Parameters) extends Module with CoreParams {
//  val io = IO(new Bundle {
//    val in = Flipped(Decoupled(new Call(List(32, 32, 32))))
//    val out = Decoupled(new Call(List(32)))
//  })
//}

//class cilk_for_test05MainDirect(implicit p: Parameters) extends cilk_for_test05MainIO()(p) {
//
//  val cilk_for_test05_detach = Module(new cilk_for_test05_detachDF())
//  val cilk_for_test05 = Module(new cilk_for_test05CacheWrapper())
//
//  cilk_for_test05.io.in <> io.in
//  cilk_for_test05_detach.io.in <> cilk_for_test05.io.call9_out
//  cilk_for_test05.io.call9_in <> cilk_for_test05_detach.io.out
//  io.out <> cilk_for_test05.io.out
//
//}
//
//class cilk_for_test05MainTM(implicit p: Parameters) extends cilk_for_test05MainIO()(p) {
//
//  val children = 2
//  val TaskControllerModule = Module(new TaskController(List(32,32,32,32), List(32), 1, children))
//  val cilk_for_test05 = Module(new cilk_for_test05CacheWrapper())
//
//  val cilk_for_test05_detach = for (i <- 0 until children) yield {
//    val foo = Module(new cilk_for_test05_detachDF())
//    foo
//  }
//
//  // tester to cilk_for_test02
//  cilk_for_test05.io.in <> io.in
//
//  // cilk_for_test02 to task controller
//  TaskControllerModule.io.parentIn(0) <> cilk_for_test05.io.call9_out
//
//  // task controller to sub-task cilk_for_test05_detach
//  for (i <- 0 until children ) {
//    cilk_for_test05_detach(i).io.in <> TaskControllerModule.io.childOut(i)
//    TaskControllerModule.io.childIn(i) <> cilk_for_test05_detach(i).io.out
//  }
//
//  // Task controller to cilk_for_test02
//  cilk_for_test05.io.call9_in <> TaskControllerModule.io.parentOut(0)
//
//  // cilk_for_test02 to tester
//  io.out <> cilk_for_test05.io.out
//
//}

class cilk_for_test05Test01[T <: cilk_for_test05MainIO](c: T, n: Int, tiles: Int) extends PeekPokeTester(c) {

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


  //  val addrVec = VecInit(0.U, 1.U, 2.U, 3.U, 4.U, 5.U, 6.U, 7.U, 8.U, 9.U,
  //    10.U, 11.U, 12.U, 13.U, 14.U, 15.U, 16.U, 17.U, 18.U, 19.U,
  //    32.U, 33.U, 34.U, 35.U, 36.U, 37.U, 38.U, 39.U, 40.U, 41.U,
  //    42.U, 43.U, 44.U, 45.U, 46.U, 47.U, 48.U, 49.U, 50.U, 51.U)

  //  val dataVec = VecInit(0.U, 1.U, 2.U, 3.U, 4.U, 5.U, 6.U, 7.U, 8.U, 9.U,
  //    10.U, 11.U, 12.U, 13.U, 14.U, 15.U, 16.U, 17.U, 18.U, 19.U,
  //    10.U, 11.U, 12.U, 13.U, 14.U, 15.U, 16.U, 17.U, 18.U, 19.U,
  //    0.U, 1.U, 2.U, 3.U, 4.U, 5.U, 6.U, 7.U, 8.U, 9.U)

  val inAddrVec = Range(0, 160, 4)
  val inDataVec = List(0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
    10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 10, 11,
    12, 13, 14, 15, 16, 17, 18, 19, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9)


  // Write initial contents to the memory model.
  for (i <- 0 until inAddrVec.length) {
    MemWrite(inAddrVec(i), inDataVec(i))
  }


  /**
    * cilk_for_test05DF interface:
    *
    * data_0 = Flipped(Decoupled(new DataBundle))
    * val pred = Decoupled(new Bool())
    * val start = Input(new Bool())
    * val result = Decoupled(new DataBundle)
    */


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
  poke(c.io.in.bits.data("field0").data, 0.U)
  poke(c.io.in.bits.data("field0").predicate, true.B)
//  poke(c.io.in.bits.data("field1").data, 128.U)
  poke(c.io.in.bits.data("field1").data, 80.U)
  poke(c.io.in.bits.data("field1").predicate, true.B)
  poke(c.io.in.bits.data("field2").data, 160.U)
//  poke(c.io.in.bits.data("field2").data, 256.U)
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
  while (time < 20000 && !result) {
    time += 1
    step(1)
    //println(s"Cycle: $time")
    if (peek(c.io.out.valid) == 1 &&
      peek(c.io.out.bits.data("field0").predicate) == 1
    ) {
      result = true
      val data = peek(c.io.out.bits.data("field0").data)
      if (data != 10) {
        println(Console.RED + s"*** Incorrect result received. Got $data. Hoping for 1" + Console.RESET)
        fail
      } else {
        println(Console.BLUE + s"*** Correct result received. Run time: $time cycles." + Console.RESET)
      }
    }
  }

  if (!result) {
    println(Console.RED + "*** Timeout." + Console.RESET)
    fail
  }
}

class cilk_for_test05Tester1 extends FlatSpec with Matchers {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  it should "Check that cilk_for_test05 works correctly." in {
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
      () => new cilk_for_test05Main(tiles = 1)(p.alterPartial({case TLEN => 6}))) {
      c => new cilk_for_test05Test01(c, 5, 1)
    } should be(true)
  }
}

