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

//class test10CacheWrapper()(implicit p: Parameters) extends test10DF()(p)
  //with CacheParams {
///*  val io2 = IO(new Bundle {
    //val init  = Flipped(Valid(new InitParams()(p)))
  //})
  //*/
  //// Instantiate the AXI Cache
  //val cache = Module(new Cache)
  //cache.io.cpu.req <> CacheMem.io.MemReq
  //CacheMem.io.MemResp <> cache.io.cpu.resp
  //cache.io.cpu.abort := false.B
  //// Instantiate a memory model with AXI slave interface for cache
  //val memModel = Module(new NastiMemSlave)
  //memModel.io.nasti <> cache.io.nasti
  ////memModel.io.init <> io2.init

  //val raminit = RegInit(true.B)
  //val addrVec = VecInit(0.U,1.U,2.U,3.U)
  //val dataVec = VecInit(1.U,2.U,3.U,4.U)
  //val (count_out, count_done) = Counter(raminit, addrVec.length)
  //memModel.io.init.bits.addr := addrVec(count_out)
  //memModel.io.init.bits.data := dataVec(count_out)
  //when (!count_done) {
    //memModel.io.init.valid := true.B
  //}.otherwise {
    //memModel.io.init.valid := false.B
    //raminit := false.B
  //}

//}

class test10MainIO(implicit val p: Parameters)  extends Module with CoreParams with CacheParams {
  val io = IO( new CoreBundle {
    val in = Flipped(Decoupled(new Call(List(32,32,32))))
    val addr = Input(UInt(nastiXAddrBits.W))
    val din  = Input(UInt(nastiXDataBits.W))
    val write = Input(Bool())
    val dout = Output(UInt(nastiXDataBits.W))
    val out = Decoupled(new Call(List(32)))
  })
}

class test10Main(implicit p: Parameters) extends test10MainIO {

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
  val test10 = Module(new test10DF())

  cache.io.cpu.req <> test10.io.MemReq
  test10.io.MemResp <> cache.io.cpu.resp
  test10.io.in <> io.in
  io.out <> test10.io.out

}




//class test10Test01(c: test10CacheWrapper) extends PeekPokeTester(c) {
class test10Test01[T <: test10MainIO](c: T) extends PeekPokeTester(c) {

  /**
  *  test10DF interface:
  *
  *    data_0 = Flipped(Decoupled(new DataBundle))
   *    val pred = Decoupled(new Bool())
   *    val start = Input(new Bool())
   *    val result = Decoupled(new DataBundle)
   */


/*
  val initList = List((0,1), (1,2), (2,3), (3,4))
  for ((addr,data) <- initList) {
    poke(c.io2.init.bits.addr, addr)
    poke(c.io2.init.bits.data, data)
    poke(c.io2.init.valid, true.B)
    step(1)
  }
  poke(c.io2.init.valid, false.B)
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
  poke(c.io.in.bits.data("field1").data, 4.U)
  poke(c.io.in.bits.data("field1").predicate, true.B)
  poke(c.io.in.bits.data("field2").data, 8.U)
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
  var time = 1  //Cycle counter
  var result = false
  while (time < 500) {
    time += 1
    step(1)
    //println(s"Cycle: $time")
    if (peek(c.io.out.valid) == 1 &&
      peek(c.io.out.bits.data("field0").predicate) == 1){
      result = true
      val data = peek(c.io.out.bits.data("field0").data)
      if (data != 1) {
        println(Console.RED + s"*** Incorrect result received. Got $data. Hoping for 1")
        fail
      } else {
        println(Console.BLUE + "*** Correct result received.")
      }
    }
  }

  if(!result) {
    println("*** Timeout.")
    fail
  }
}

class test10Tester extends FlatSpec with Matchers {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  it should "Check that test10 works correctly." in {
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
     () => new test10Main()) {
     c => new test10Test01(c)
    } should be(true)
  }
}

