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

import scala.util.Random


class saxpyMainIO(implicit val p: Parameters)  extends Module with CoreParams with CacheParams {
  val io = IO( new CoreBundle {
    val in = Flipped(Decoupled(new Call(List(32,32,32,32))))
    val addr = Input(UInt(nastiXAddrBits.W))  // Initialization address
    val din  = Input(UInt(nastiXDataBits.W))  // Initialization data
    val write = Input(Bool())                 // Initialization write strobe
    val dout = Output(UInt(nastiXDataBits.W))
    val out = Decoupled(new Call(List(32)))
  })
}

class saxpyMain(implicit p: Parameters) extends saxpyMainIO {

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
  val saxpy = Module(new cilk_saxpyDF())

  cache.io.cpu.req <> saxpy.io.MemReq
  saxpy.io.MemResp <> cache.io.cpu.resp
  saxpy.io.in <> io.in
  io.out <> saxpy.io.out

}


class saxpyTest01[T <: saxpyMainIO](c: T, n:Int, ch:Int) extends PeekPokeTester(c) {
  val dataLen = n;
  val a = 5 // 'a' of a*x[i]+y[i]
  val inAddrVec = List.range(0, 8*dataLen, 4)
  val inX = List.range(1, dataLen+1)  // array of uint32
  val inY = List.range(1, dataLen+1)  // array of uint32
  val inDataVec = inX++inY
  val outAddrVec = List.range(4*dataLen, 8*dataLen, 4)
  val outDataVec = inX.zip(inY).map { case (x, y) => a*x + y }

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
  poke(c.io.in.bits.data("field2").data, 0.U)
  poke(c.io.in.bits.data("field2").taskID, 5.U)
  poke(c.io.in.bits.data("field2").predicate, false.B)
  poke(c.io.in.bits.data("field3").data, 0.U)
  poke(c.io.in.bits.data("field3").taskID, 5.U)
  poke(c.io.in.bits.data("field3").predicate, false.B)
  poke(c.io.out.ready, false.B)
  step(1)
  poke(c.io.in.bits.enable.control, true.B)
  poke(c.io.in.valid, true.B)
  poke(c.io.in.bits.data("field0").data, n)    // Number of iterations
  poke(c.io.in.bits.data("field0").predicate, true.B)
  poke(c.io.in.bits.data("field1").data, a)   // Scale value
  poke(c.io.in.bits.data("field1").predicate, true.B)
  poke(c.io.in.bits.data("field2").data, 0)   // X[]
  poke(c.io.in.bits.data("field2").predicate, true.B)
  poke(c.io.in.bits.data("field3").data, 4*dataLen)   // Y[]
  poke(c.io.in.bits.data("field3").predicate, true.B)
  poke(c.io.out.ready, true.B)
  step(1)
  poke(c.io.in.bits.enable.control, false.B)
  poke(c.io.in.valid, false.B)
  poke(c.io.in.bits.data("field0").data, 0)
  poke(c.io.in.bits.data("field0").predicate, false.B)
  poke(c.io.in.bits.data("field1").data, 0.U)
  poke(c.io.in.bits.data("field1").predicate, false.B)
  poke(c.io.in.bits.data("field2").data, 0)
  poke(c.io.in.bits.data("field2").predicate, false.B)
  poke(c.io.in.bits.data("field3").data, 0.U)
  poke(c.io.in.bits.data("field3").predicate, false.B)

  step(1)

  // NOTE: Don't use assert().  It seems to terminate the writing of VCD files
  // early (before the error) which makes debugging very difficult. Check results
  // using if() and fail command.
  var time = 0
  var result = false
  while (time < 5000) {
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
        println(Console.BLUE + s"*** Correct return result received. Run time: $time cycles. Tiles=$ch" + Console.RESET)
      }
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

class saxpyTester1 extends FlatSpec with Matchers {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  // iotester flags:
  // -ll  = log level <Error|Warn|Info|Debug|Trace>
  // -tbn = backend <firrtl|verilator|vcs>
  // -td  = target directory
  // -tts = seed for RNG
  for (i <- 10 to 10) {
    it should s"Loop ${i} iterations." in {
      chisel3.iotesters.Driver.execute(
        Array(
          // "-ll", "Info",
          "-tbn", "verilator",
          "-td", "test_run_dir",
          "-tts", "0001"),
        () => new saxpyMain()) {
        c => new saxpyTest01(c,i, 1)
      } should be(true)
    }
  }
}

