package dandelion.generator


import java.io.PrintWriter
import java.io.File
import chisel3._
import chisel3.Module
import chisel3.iotesters._
import org.scalatest.{FlatSpec, Matchers}
import dandelion.config._
import util._
import dandelion.interfaces._
import dandelion.memory._
import dandelion.accel._

class test11MainIO(implicit val p: Parameters)  extends Module with CoreParams with CacheParams {
  val io = IO( new Bundle {
    val in = Flipped(Decoupled(new Call(List(32))))
    val req = Flipped(Decoupled(new MemReq))
    val resp = Output(Valid(new MemResp))
    val out = Decoupled(new Call(List(32)))
  })

  def cloneType = new test11MainIO().asInstanceOf[this.type]
}

class test11MainDirect(implicit p: Parameters) extends test11MainIO{


  val cache = Module(new Cache) // Simple Nasti Cache
  val memModel = Module(new NastiMemSlave) // Model of DRAM to connect to Cache
  val memCopy = Mem(1024, UInt(32.W)) // Local memory just to keep track of writes to cache for validation

  // Connect the wrapper I/O to the memory model initialization interface so the
  // test bench can write contents at start.
  memModel.io.nasti <> cache.io.nasti
  memModel.io.init.bits.addr := 0.U
  memModel.io.init.bits.data := 0.U
  memModel.io.init.valid := true.B
  cache.io.cpu.abort := false.B


  // Wire up the cache and modules under test.
  val test11 = Module(new test11DF())
  val test11_inner = Module(new test11_innerDF())


  val MemArbiter = Module(new MemArbiter(3))


  cache.io.cpu.req <> MemArbiter.io.cache.MemReq
  MemArbiter.io.cache.MemResp <> cache.io.cpu.resp


  MemArbiter.io.cpu.MemReq(0) <> test11_inner.io.MemReq
  test11_inner.io.MemResp <> MemArbiter.io.cpu.MemResp(0)

  MemArbiter.io.cpu.MemReq(1) <> test11.io.MemReq
  test11.io.MemResp <> MemArbiter.io.cpu.MemResp(1)

  MemArbiter.io.cpu.MemReq(2) <> io.req
  io.resp <> MemArbiter.io.cpu.MemResp(2)

  test11.io.call_5_in <> test11_inner.io.out
  test11_inner.io.in <> test11.io.call_5_out

  test11.io.in <> io.in
  io.out <> test11.io.out

}



class test11Test01[T <: test11MainDirect](c: T) extends PeekPokeTester(c) {


  /**
  *  test11DF interface:
  *
  *    in = Flipped(new CallDecoupled(List(...)))
  *    out = new CallDecoupled(List(32))
  */

  // Initializing the signals
  poke(c.io.in.bits.enable.control, false.B)
  poke(c.io.in.valid, false.B)
  poke(c.io.in.bits.data("field0").data, 0.U)
  poke(c.io.in.bits.data("field0").predicate, false.B)
  poke(c.io.out.ready, false.B)
  step(1)
  poke(c.io.in.bits.enable.control, true.B)
  poke(c.io.in.valid, true.B)
  poke(c.io.in.bits.data("field0").data, 10.U)
  poke(c.io.in.bits.data("field0").predicate, true.B)
  poke(c.io.out.ready, true.B)
  step(1)
  poke(c.io.in.bits.enable.control, false.B)
  poke(c.io.in.valid, false.B)
  poke(c.io.in.bits.data("field0").data, 0.U)
  poke(c.io.in.bits.data("field0").predicate, false.B)

  step(1)

  // NOTE: Don't use assert().  It seems to terminate the writing of VCD files
  // early (before the error) which makes debugging very difficult. Check results
  // using if() and fail command.
  var time = 1  //Cycle counter
  var result = false
  while (time < 400) {
    time += 1
    step(1)
    //println(s"Cycle: $time")
    if (peek(c.io.out.valid) == 1 &&
      peek(c.io.out.bits.data("field0").predicate) == 1){
      result = true
      val data = peek(c.io.out.bits.data("field0").data)
      if (data != 475) {
        println(Console.RED + s"[LOG] *** Incorrect result received. Got $data. Hoping for 1" + Console.RESET)
        fail
      } else {
        println(Console.BLUE + s"*** Correct return result received ($data). Run time: $time cycles." + Console.RESET)

      }
    }
  }

  if(!result) {
    println("*** Timeout.")
    fail
  }

}

class test11Tester1 extends FlatSpec with Matchers {
  implicit val p = Parameters.root((new MiniConfig).toInstance)
  it should "Check that test11 works correctly." in {
    // iotester flags:
    // -ll  = log level <Error|Warn|Info|Debug|Trace>
    // -tbn = backend <firrtl|verilator|vcs>
    // -td  = target directory
    // -tts = seed for RNG
    chisel3.iotesters.Driver.execute(
     Array(
//       "-ll", "Info",
       "-tbn", "verilator",
       "-td", "test_run_dir",
       "-tts", "0001"),
     () => new test11MainDirect()) {
     c => new test11Test01(c)
    } should be(true)
  }
}

