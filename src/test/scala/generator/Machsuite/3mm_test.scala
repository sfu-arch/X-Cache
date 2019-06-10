package dataflow

import java.io.{File, PrintWriter}

import accel._
import chisel3.iotesters._
import chisel3.util._
import chisel3.{Module, _}
import dandelion.config._
import interfaces._
import memory._
import org.scalatest.{FlatSpec, Matchers}


class k3mmMainIO(implicit val p: Parameters) extends Module with CoreParams with CacheParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32, 32, 32, 32, 32, 32, 32))))
    val req = Flipped(Decoupled(new MemReq))
    val resp = Output(Valid(new MemResp))
    val out = Decoupled(new Call(List()))
  })

  def cloneType = new k3mmMainIO().asInstanceOf[this.type]
}

class k3mmMain(implicit p: Parameters) extends k3mmMainIO {

  val cache = Module(new Cache) // Simple Nasti Cache
  val memModel = Module(new NastiInitMemSlave(latency=10)()) // Model of DRAM to connect to Cache
//  val memModel = Module(new NastiMemSlave) // Model of DRAM to connect to Cache


  // Connect the wrapper I/O to the memory model initialization interface so the
  // test bench can write contents at start.
  memModel.io.nasti <> cache.io.nasti
  memModel.io.init.bits.addr := 0.U
  memModel.io.init.bits.data := 0.U
  memModel.io.init.valid := false.B
  cache.io.cpu.abort := false.B

  // Wire up the cache and modules under test.
  val k3mm = Module(new k3mmDF())

  //Put an arbiter infront of cache
  val CacheArbiter = Module(new MemArbiter(2))

  // Connect input signals to cache
  CacheArbiter.io.cpu.MemReq(0) <> k3mm.io.MemReq
  k3mm.io.MemResp <> CacheArbiter.io.cpu.MemResp(0)

  //Connect main module to cache arbiter
  CacheArbiter.io.cpu.MemReq(1) <> io.req
  io.resp <> CacheArbiter.io.cpu.MemResp(1)

  //Connect cache to the arbiter
  cache.io.cpu.req <> CacheArbiter.io.cache.MemReq
  CacheArbiter.io.cache.MemResp <> cache.io.cpu.resp

  //Connect in/out ports
  k3mm.io.in <> io.in
  io.out <> k3mm.io.out

}


class k3mmTest01[T <: k3mmMainIO](c: T) extends PeekPokeTester(c) {


  // Initializing the signals
  poke(c.io.in.bits.enable.control, false.B)
  poke(c.io.in.bits.enable.taskID, 0)

  poke(c.io.in.valid, false.B)
  poke(c.io.in.bits.data("field0").data, 0)
  poke(c.io.in.bits.data("field0").taskID, 0)
  poke(c.io.in.bits.data("field0").predicate, false)
  poke(c.io.in.bits.data("field1").data, 0)
  poke(c.io.in.bits.data("field1").taskID, 0)
  poke(c.io.in.bits.data("field1").predicate, false)
  poke(c.io.in.bits.data("field2").data, 0)
  poke(c.io.in.bits.data("field2").taskID, 0)
  poke(c.io.in.bits.data("field2").predicate, false)
  poke(c.io.in.bits.data("field3").data, 0)
  poke(c.io.in.bits.data("field3").taskID, 0)
  poke(c.io.in.bits.data("field3").predicate, false)
  poke(c.io.in.bits.data("field4").data, 0)
  poke(c.io.in.bits.data("field4").taskID, 0)
  poke(c.io.in.bits.data("field4").predicate, false)
  poke(c.io.in.bits.data("field5").data, 0)
  poke(c.io.in.bits.data("field5").taskID, 0)
  poke(c.io.in.bits.data("field5").predicate, false)
  poke(c.io.in.bits.data("field6").data, 0)
  poke(c.io.in.bits.data("field6").taskID, 0)
  poke(c.io.in.bits.data("field6").predicate, false)
  poke(c.io.out.ready, false)
  step(1)
  poke(c.io.in.bits.enable.control, true.B)
  poke(c.io.in.valid, true.B)
  poke(c.io.in.bits.data("field0").data, 0) //
  poke(c.io.in.bits.data("field0").predicate, true.B)
  poke(c.io.in.bits.data("field1").data, 0x100) //
  poke(c.io.in.bits.data("field1").predicate, true.B)
  poke(c.io.in.bits.data("field2").data, 0x200) //
  poke(c.io.in.bits.data("field2").predicate, true.B)
  poke(c.io.in.bits.data("field3").data, 0x300) //
  poke(c.io.in.bits.data("field3").predicate, true.B)
  poke(c.io.in.bits.data("field4").data, 0x400) //
  poke(c.io.in.bits.data("field4").predicate, true.B)
  poke(c.io.in.bits.data("field5").data, 0x500) //
  poke(c.io.in.bits.data("field5").predicate, true.B)
  poke(c.io.in.bits.data("field6").data, 0x700) //
  poke(c.io.in.bits.data("field6").predicate, true.B)
  poke(c.io.out.ready, true.B)
  step(1)
  poke(c.io.in.bits.enable.control, false.B)
  poke(c.io.in.valid, false.B)
  poke(c.io.in.bits.data("field0").data, 0)
  poke(c.io.in.bits.data("field0").predicate, false.B)
  poke(c.io.in.bits.data("field1").data, 0)
  poke(c.io.in.bits.data("field1").predicate, false.B)
  poke(c.io.in.bits.data("field2").data, 0)
  poke(c.io.in.bits.data("field2").predicate, false.B)
  poke(c.io.in.bits.data("field3").data, 0)
  poke(c.io.in.bits.data("field3").predicate, false.B)
  poke(c.io.in.bits.data("field4").data, 0)
  poke(c.io.in.bits.data("field4").predicate, false.B)
  poke(c.io.in.bits.data("field5").data, 0)
  poke(c.io.in.bits.data("field5").predicate, false.B)
  poke(c.io.in.bits.data("field6").data, 0)
  poke(c.io.in.bits.data("field6").predicate, false.B)

  step(1)

  // NOTE: Don't use assert().  It seems to terminate the writing of VCD files
  // early (before the error) which makes debugging very difficult. Check results
  // using if() and fail command.
  var time = 0
  var result = false
  while (time < 800000) {
    time += 1
    step(1)
    if (peek(c.io.out.valid) == 1) {
      result = true
      println(Console.BLUE + s"*** Correct return result received. Run time: $time cycles." + Console.RESET)
    }
  }
  //println(Console.BLUE + s"*** Correct return result received. Run time: $time cycles." + Console.RESET)

  step(100)

  //  Peek into the CopyMem to see if the expected data is written back to the Cache
  /*var valid_data = true
  for (i <- 0 until outAddrVec.length) {
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
    dumpMemory("memory.txt")
  }*/


  if (!result) {
    println(Console.RED + s"*** Timeout after $time cycles." + Console.RESET)
    fail
  }
}


class k3mmTester1 extends FlatSpec with Matchers {
  implicit val p = Parameters.root((new MiniConfig).toInstance)
  val testParams = p.alterPartial({
    case XLEN => 32
    case TRACE => true
  })
  //it should "Check that k3mm works correctly." in {
    //// iotester flags:
    //// -ll  = log level <Error|Warn|Info|Debug|Trace>
    //// -tbn = backend <firrtl|verilator|vcs>
    //// -td  = target directory
    //// -tts = seed for RNG
    //chisel3.iotesters.Driver.execute(
      //Array(
        //// "-ll", "Info",
        //"-tn", "k3mmMain",
        //"-tbn", "verilator",
        //"-td", "test_run_dir/k3mm",
        //"-tts", "0001"),
      //() => new k3mmMain()(testParams)) {
      //c => new k3mmTest01(c)
    //} should be(true)
  //}
}
