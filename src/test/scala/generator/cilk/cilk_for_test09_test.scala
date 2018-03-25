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


class cilk_for_test09MainIO(implicit val p: Parameters)  extends Module with CoreParams with CacheParams {
  val io = IO( new CoreBundle {
    val in = Flipped(Decoupled(new Call(List(32,32))))
    val addr = Input(UInt(nastiXAddrBits.W))  // Initialization address
    val din  = Input(UInt(nastiXDataBits.W))  // Initialization data
    val write = Input(Bool())                 // Initialization write strobe
    val dout = Output(UInt(nastiXDataBits.W))
    val out = Decoupled(new Call(List(32)))
  })
}

class cilk_for_test09MainDirect(implicit p: Parameters) extends cilk_for_test09MainIO {

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
  val cilk_for_test09_detach = Module(new cilk_for_test09_detachDF())
  val cilk_for_test09 = Module(new cilk_for_test09DF())

//  cache.io.cpu.req <> cilk_for_test09_detach.io.CacheReq
//  cilk_for_test09_detach.io.CacheResp <> cache.io.cpu.resp
  cilk_for_test09.io.in <> io.in
  cilk_for_test09_detach.io.in <> cilk_for_test09.io.call12_out
  cilk_for_test09.io.call12_in <> cilk_for_test09_detach.io.out
  io.out <> cilk_for_test09.io.out

}

class cilk_for_test09MainTM(ch:Int)(implicit p: Parameters) extends cilk_for_test09MainIO  {

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

  // Wire up the cache, TM, and modules under test.

  val children = ch
  val TaskControllerModule = Module(new TaskController(List(32,32), List(32), 1, children))
  val cilk_for_test09 = Module(new cilk_for_test09DF())

  val cilk_for_test09_detach = for (i <- 0 until children) yield {
    val foo = Module(new cilk_for_test09_detachDF())
    foo
  }

  // Ugly hack to merge requests from two children.  "ReadWriteArbiter" merges two
  // requests ports of any type.  Read or write is irrelevant.
  val CacheArbiter = Module(new CacheArbiter(children))
  for (i <- 0 until children) {
    //CacheArbiter.io.cpu.CacheReq(i) <> cilk_for_test09_detach(i).io.CacheReq
    //cilk_for_test09_detach(i).io.CacheResp <> CacheArbiter.io.cpu.CacheResp(i)
  }
  cache.io.cpu.req <> CacheArbiter.io.cache.CacheReq
  CacheArbiter.io.cache.CacheResp <> cache.io.cpu.resp

  // tester to cilk_for_test09
  cilk_for_test09.io.in <> io.in

  // cilk_for_test09 to task controller
  TaskControllerModule.io.parentIn(0) <> cilk_for_test09.io.call12_out

  // task controller to sub-task cilk_for_test09_detach
  for (i <- 0 until children ) {
    cilk_for_test09_detach(i).io.in <> TaskControllerModule.io.childOut(i)
    TaskControllerModule.io.childIn(i) <> cilk_for_test09_detach(i).io.out
  }

  // Task controller to cilk_for_test09
  cilk_for_test09.io.call12_in <> TaskControllerModule.io.parentOut(0)

  // cilk_for_test09 to tester
  io.out <> cilk_for_test09.io.out

}

class cilk_for_test09Test01[T <: cilk_for_test09MainIO](c: T, m:Int, n:Int) extends PeekPokeTester(c) {
  val pixels = 8
  val inAddrVec = List.range(0, pixels*3*4, 4)
  val inDataVec = List(1,1,1, 0,10,3, 0,2,5,
    255,192,32, 52,71,98, 31,27,99,
    12,77,52, 128,7,7)
  val outAddrVec = List.range(256, 256+(pixels*3*4), 4)
  val outDataVec = List(0,0,1,  4,7,4,  1,1,4,
    179,193,58,  64,68,102,  40,33,97,
    41,61,58,  56,32,9d)

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
  poke(c.io.out.ready, false.B)
  step(1)
  poke(c.io.in.bits.enable.control, true.B)
  poke(c.io.in.valid, true.B)
  poke(c.io.in.bits.data("field0").data, m.U)     // Array rgb[] base address
  poke(c.io.in.bits.data("field0").predicate, true.B)
  poke(c.io.in.bits.data("field1").data, n.U)   // Array xyz[] base address
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
  while (time < 100000) {
    time += 1
    step(1)
    if (peek(c.io.out.valid) == 1 &&
      peek(c.io.out.bits.data("field0").predicate) == 1
    ) {
      result = true
      val data = peek(c.io.out.bits.data("field0").data)
      if (data != 0) {
        println(Console.RED + s"*** Incorrect result received. Got $data. Hoping for 50" + Console.RESET)
        fail
      } else {
        println(Console.BLUE + s"*** Correct return result received. Run time: $time cycles." + Console.RESET)
      }
    }
  }

  //  Peek into the CopyMem to see if the expected data is written back to the Cache
/*
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
*/

  if(!result) {
    println(Console.RED + "*** Timeout." + Console.RESET)
    fail
  }
}

class cilk_for_test09Tester1(m:Int, n:Int) extends FlatSpec with Matchers {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)

  it should "Check that cilk_for_test09 works correctly." in {
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
      () => new cilk_for_test09MainDirect()) {
      c => new cilk_for_test09Test01(c,5,5)
    } should be(true)
  }
}

/*
 
class cilk_for_test09Tester2 extends FlatSpec with Matchers {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  // iotester flags:
  // -ll  = log level <Error|Warn|Info|Debug|Trace>
  // -tbn = backend <firrtl|verilator|vcs>
  // -td  = target directory
  // -tts = seed for RNG
  // -td", s"test_run_dir/test09-$ch-$m-$n",
  for (m <- 50 to 50 by 5) {
    for (n <- 0 to 20 by 5) {
      for (ch <- 1 to 10) {
        it should s"$ch, $m, $n" in {
          chisel3.iotesters.Driver.execute(
            Array(
              "-tbn", "verilator",
	      "-td", s"test_run_dir/test09-$ch-$m-$n",
              "-tts", "0001"),
            () => new cilk_for_test09MainTM(ch)) {
            c => new cilk_for_test09Test01(c, m, n)
          } should be(true)
        }
      }
    }
  }
}

*/
