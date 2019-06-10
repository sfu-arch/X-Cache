package dandelion.generator.dftype

import chisel3._
import chisel3.util._
import chisel3.Module
import chisel3.testers._
import chisel3.iotesters._
import org.scalatest.{FlatSpec, Matchers}
import muxes._
import dandelion.config._
import control._
import util._
import dandelion.interfaces._
import regfile._
import memory._
import stack._
import arbiters._
import dandelion.loop._
import accel._


class mataddMainIO(implicit val p: Parameters) extends Module with CoreParams with CacheParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32, 32, 32))))
    val req = Flipped(Decoupled(new MemReq))
    val resp = Output(Valid(new MemResp))
    val out = Decoupled(new Call(List()))
  })

  def cloneType = new mataddMainIO().asInstanceOf[this.type]
}


class mataddMain(implicit p: Parameters) extends mataddMainIO {

  val cache = Module(new Cache) // Simple Nasti Cache
  val memModel = Module(new NastiMemSlave) // Model of DRAM to connect to Cache

  // Connect the wrapper I/O to the memory model initialization interface so the
  // test bench can write contents at start.
  memModel.io.nasti <> cache.io.nasti
  memModel.io.init.bits.addr := 0.U
  memModel.io.init.bits.data := 0.U
  memModel.io.init.valid := false.B
  cache.io.cpu.abort := false.B

  val matadd_df = Module(new mataddDF())

  val CacheArb = Module(new MemArbiter(2))

  CacheArb.io.cpu.MemReq(0) <> matadd_df.io.MemReq
  matadd_df.io.MemResp <> CacheArb.io.cpu.MemResp(0)

  CacheArb.io.cpu.MemReq(1) <> io.req
  io.resp <> CacheArb.io.cpu.MemResp(1)


  cache.io.cpu.req <> CacheArb.io.cache.MemReq
  CacheArb.io.cache.MemResp <> cache.io.cpu.resp

  matadd_df.io.in <> io.in
  io.out <> matadd_df.io.out

}


class matadd_Test01[T <: mataddMainIO](c: T) extends PeekPokeTester(c) {

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


  val inAddrVec = List(0x0, 0x4, 0x8, 0xc, 0x10, 0x20, 0x24, 0x28, 0x2c, 0x30)
  val inDataVec = List(1, 2, 3, 4, 5, 1, 2, 3, 4, 5)
  val outAddrVec = List(0x40, 0x44, 0x48, 0x4c, 0x50)
  val outDataVec = List(2, 4, 6, 8, 10)

  // Write initial contents to the memory model.
  for (i <- 0 until 10) {
    MemWrite(inAddrVec(i), inDataVec(i))
  }
  for (i <- 0 until 5) {
    MemWrite(outAddrVec(i), 0)
  }

  //  step(1)


  step(1)

  // Initializing the signals
  poke(c.io.in.bits.enable.control, false.B)
  poke(c.io.in.valid, false.B)
  poke(c.io.in.bits.data("field0").data, 0.U)
  poke(c.io.in.bits.data("field0").taskID, 0.U)
  poke(c.io.in.bits.data("field0").predicate, false.B)
  poke(c.io.in.bits.data("field1").data, 0.U)
  poke(c.io.in.bits.data("field1").taskID, 0.U)
  poke(c.io.in.bits.data("field1").predicate, false.B)
  poke(c.io.in.bits.data("field2").data, 0.U)
  poke(c.io.in.bits.data("field2").taskID, 0.U)
  poke(c.io.in.bits.data("field2").predicate, false.B)
  poke(c.io.out.ready, false.B)
  step(1)
  poke(c.io.in.bits.enable.control, true.B)
  poke(c.io.in.valid, true.B)
  poke(c.io.in.bits.data("field0").data, "h0".U) // Array a[] base address
  poke(c.io.in.bits.data("field0").predicate, true.B)
  poke(c.io.in.bits.data("field1").data, "h200".U) // Array b[] base address
  poke(c.io.in.bits.data("field1").predicate, true.B)
  poke(c.io.in.bits.data("field2").data, "h400".U) // Array c[] base address
  poke(c.io.in.bits.data("field2").predicate, true.B)
  poke(c.io.out.ready, true.B)
  step(1)
  poke(c.io.in.bits.enable.control, false.B)
  poke(c.io.in.valid, false.B)
  poke(c.io.in.bits.data("field0").data, 0)
  poke(c.io.in.bits.data("field0").predicate, false.B)
  poke(c.io.in.bits.data("field1").data, 0.U)
  poke(c.io.in.bits.data("field1").predicate, false.B)
  poke(c.io.in.bits.data("field2").data, 0.U)
  poke(c.io.in.bits.data("field2").predicate, false.B)

  step(1)

  // NOTE: Don't use assert().  It seems to terminate the writing of VCD files
  // early (before the error) which makes debugging very difficult. Check results
  // using if() and fail command.
  var time = 0
  var result = false
  while (time < 20000 && !result) {
    time += 1
    step(1)
    if (peek(c.io.out.valid) == 1 ) {
      result = true
      println(Console.BLUE + s"*** Correct return result received. Run time: $time cycles." + Console.RESET)
    }
  }

  /**
    * Self checking needs to be done
    */

  //  Peek into the CopyMem to see if the expected data is written back to the Cache

//  var valid_data = true
//  for (i <- 0 until outDataVec.length) {
//    val data = MemRead(outAddrVec(i))
//    if (data != outDataVec(i)) {
//      println(Console.RED + s"*** Incorrect data received. Got $data. Hoping for ${outDataVec(i).toInt}" + Console.RESET)
//      fail
//      valid_data = false
//    }
//  }
//  if (valid_data) {
//    println(Console.BLUE + "*** Correct data written back." + Console.RESET)
//  }
//
//
//  if (!result) {
//    println(Console.RED + "*** Timeout." + Console.RESET)
//    fail
//  }
}

class mataddTester1 extends FlatSpec with Matchers {
  implicit val p = Parameters.root((new MiniConfig).toInstance)
  it should "Check that matadd works correctly." in {
    // iotester flags:
    // -ll  = log level <Error|Warn|Info|Debug|Trace>
    // -tbn = backend <firrtl|verilator|vcs>
    // -td  = target directory
    // -tts = seed for RNG
    chisel3.iotesters.Driver.execute(
      Array(
        // "-ll", "Info",
        "-tbn", "verilator",
        "-td", "test_run_dir/typeadd",
        "-tts", "0001"),
      () => new mataddMain()) {
      c => new matadd_Test01(c)
    } should be(true)
  }
}
