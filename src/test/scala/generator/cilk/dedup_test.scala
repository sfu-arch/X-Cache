package dandelion.generator.cilk

import chisel3._
import chisel3.Module
import dandelion.concurrent.{TaskController,TaskControllerIO}
import chisel3.iotesters._
import dandelion.config._
import util._
import dandelion.interfaces._
import memory._
import dandelion.accel._


class dedupMainIO(implicit val p: Parameters)  extends Module with CoreParams with CacheParams {
  val io = IO( new CoreBundle {
    val in = Flipped(Decoupled(new Call(List(32,32,32))))
    val addr = Input(UInt(nastiXAddrBits.W))  // Initialization address
    val din  = Input(UInt(nastiXDataBits.W))  // Initialization data
    val write = Input(Bool())                 // Initialization write strobe
    val dout = Output(UInt(nastiXDataBits.W))
    val out = Decoupled(new Call(List(32)))
  })
}


class dedupMainTM(tiles : Int)(implicit p: Parameters) extends dedupMainIO  {

  val cache = Module(new Cache) // Simple Nasti Cache
  val memModel = Module(new NastiMemSlave) // Model of DRAM to connect to Cache
  val memCopy = Mem(1024, UInt(32.W)) // Local memory just to keep track of writes to cache for validation

  // Store a copy of all data written to the cache.  This is done since the cache isn't
  // 'write through' to the memory model and we have no easy way of reading the
  // cache contents from the testbench.
  when(cache.io.cpu.req.valid && cache.io.cpu.req.bits.iswrite) {
    memCopy.write((cache.io.cpu.req.bits.addr >> 2).asUInt(), cache.io.cpu.req.bits.data)
  }
  io.dout := memCopy.read((io.addr >> 2).asUInt())

  // Connect the wrapper I/O to the memory model initialization interface so the
  // test bench can write contents at start.
  memModel.io.nasti <> cache.io.nasti
  memModel.io.init.bits.addr := io.addr
  memModel.io.init.bits.data := io.din
  memModel.io.init.valid := io.write
  cache.io.cpu.abort := false.B

  // Wire up the cache, TM, and modules under test.

  val S2Tiles = 1
  val S3Tiles = tiles // 1,2,4,8
  val S4Tiles = 1
  val dedup = Module(new dedupDF())
  val dedup_S2 = for (i <- 0 until S2Tiles) yield {
    val S2 = Module(new dedup_S2DF())
    S2
  }
  val dedup_S3 = for (i <- 0 until S3Tiles) yield {
    val S3 = Module(new dedup_S3DF())
    S3
  }
  val dedup_S4 = for (i <- 0 until S4Tiles) yield {
    val S4 = Module(new dedup_S4DF())
    S4
  }
  val S2TC = Module(new TaskController(List(32,32,32,32), List(32), 1, S2Tiles))
  val S3TC = Module(new TaskController(List(32,32,32,32), List(32), S2Tiles, S3Tiles))
  val S4TC = Module(new TaskController(List(32,32,32), List(32), 1, S4Tiles))

  // Connect cache interfaces to a cache arbiter
  val MemArbiter = Module(new MemArbiter(1+S2Tiles+S3Tiles+S4Tiles))
  MemArbiter.io.cpu.MemReq(0) <> dedup.io.MemReq
  dedup.io.MemResp <> MemArbiter.io.cpu.MemResp(0)
  for (i <- 0 until S2Tiles) {
    MemArbiter.io.cpu.MemReq(i+1) <> dedup_S2(i).io.MemReq
    dedup_S2(i).io.MemResp <> MemArbiter.io.cpu.MemResp(i+1)
  }
  for (i <- 0 until S3Tiles) {
    MemArbiter.io.cpu.MemReq(i+1+S2Tiles) <> dedup_S3(i).io.MemReq
    dedup_S3(i).io.MemResp <> MemArbiter.io.cpu.MemResp(i+1+S2Tiles)
  }
  for (i <- 0 until S4Tiles) {
    MemArbiter.io.cpu.MemReq(i+1+S2Tiles+S3Tiles) <> dedup_S4(i).io.MemReq
    dedup_S4(i).io.MemResp <> MemArbiter.io.cpu.MemResp(i+1+S2Tiles+S3Tiles)
  }
  cache.io.cpu.req <> MemArbiter.io.cache.MemReq
  MemArbiter.io.cache.MemResp <> cache.io.cpu.resp

  // tester to dedup
  dedup.io.in <> io.in

  // Task Controllers
  S4TC.io.parentIn(0) <> dedup.io.call_1_out
  dedup.io.call_1_in <> S4TC.io.parentOut(0)
  for(i <- 0 until S4Tiles) {
    dedup_S4(i).io.in <> S4TC.io.childOut(i)
    S4TC.io.childIn(i) <> dedup_S4(i).io.out
  }

  S2TC.io.parentIn(0) <> dedup.io.call_15_out
  dedup.io.call_15_in <> S2TC.io.parentOut(0)
  for(i <- 0 until S2Tiles) {
    dedup_S2(i).io.in <> S2TC.io.childOut(i)
    S2TC.io.childIn(i) <> dedup_S2(i).io.out
    S3TC.io.parentIn(i) <> dedup_S2(i).io.call_8_out
    dedup_S2(i).io.call_8_in <> S3TC.io.parentOut(i)
  }
  for(i <- 0 until S3Tiles) {
    dedup_S3(i).io.in <> S3TC.io.childOut(i)
    S3TC.io.childIn(i) <> dedup_S3(i).io.out
  }


  // dedup to tester
  io.out <> dedup.io.out

}

class dedupTest01[T <: dedupMainIO](c: T, tiles : Int) extends PeekPokeTester(c) {

  val queueBase = 0
  val inBase = 1000
  val outBase = 2000

  val queueAddrVec = List.range(queueBase, queueBase+4*256,4)
  val inDataVec = List(97,97,50,50,105,105,108,108,99,99,112,112,115,115,107,107,108,108,114,114,121,121,118,118,109,109,99,112,106,110,98,112,98)
  val inAddrVec = List.range(inBase, inBase+4*inDataVec.length, 4)
  val outDataVec = List(97,2,50,2,105,2,108,2,99,2,112,2,115,2,107,2,108,2,114,2,121,2,118,2,109,2,99,112,106,110,98,112,98)
  val outAddrVec = List.range(outBase, outBase+4*outDataVec.length, 4)

  poke(c.io.addr, 0.U)
  poke(c.io.din, 0.U)
  poke(c.io.write, false.B)


  // Initialize queue to invalid (999)
  for(i <- 0 until queueAddrVec.length) {
    poke(c.io.addr, queueAddrVec(i))
    poke(c.io.din, 999)
    poke(c.io.write, true.B)
    step(1)
  }

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
  poke(c.io.out.ready, false.B)
  step(1)
  poke(c.io.in.bits.enable.control, true.B)
  poke(c.io.in.valid, true.B)
  poke(c.io.in.bits.data("field0").data, inBase)     // x[] base address
  poke(c.io.in.bits.data("field0").predicate, true.B)
  poke(c.io.in.bits.data("field1").data, outBase)   // y[] base address
  poke(c.io.in.bits.data("field1").predicate, true.B)
  poke(c.io.in.bits.data("field2").data, queueBase)   // y[] base address
  poke(c.io.in.bits.data("field2").predicate, true.B)
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
  while (time < 10000 && !result) {
    time += 1
    step(1)
    if (peek(c.io.out.valid) == 1 &&
      peek(c.io.out.bits.data("field0").predicate) == 1
      ) {
        result = true
        println(Console.BLUE + s"*** Correct return result received. Tiles: $tiles. Run time: $time cycles." + Console.RESET)
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

//class dedupTester1 extends FlatSpec with Matchers {
//  implicit val p = Parameters.root((new MiniConfig).toInstance)
//  // iotester flags:
//  // -ll  = log level <Error|Warn|Info|Debug|Trace>
//  // -tbn = backend <firrtl|verilator|vcs>
//  // -td  = target directory
//  // -tts = seed for RNG
////  val tile_list = List(1,2,4,8)
//  val tile_list = List(1)
//  for (tile <- tile_list) {
//    it should s"Test: $tile tiles" in {
//      chisel3.iotesters.Driver.execute(
//        Array(
//          // "-ll", "Info",
//          "-tbn", "verilator",
//          "-td", s"test_run_dir/dedup_${tile}",
//          "-tts", "0001"),
//        () => new dedupMainTM(tile)(p.alterPartial({case TLEN => 8}))) {
//        c => new dedupTest01(c,tile)
//      } should be(true)
//    }
//  }
//}
