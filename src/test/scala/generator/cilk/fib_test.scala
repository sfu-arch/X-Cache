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


class fibMainIO(implicit val p: Parameters)  extends Module with CoreParams with CacheParams {
  val io = IO( new CoreBundle {
    val in = Flipped(Decoupled(new Call(List(32,32))))
    val req  = Flipped(Decoupled(new MemReq))
    val resp = Output(Valid(new MemResp))
    val out = Decoupled(new Call(List(32)))
  })
}

class fibMain(tiles : Int)(implicit p: Parameters) extends fibMainIO {


//  val fib = Module(new fibDF())
//  val fib_continue = Module(new fib_continueDF())
  val NumFibs = tiles
  val fib = for (i <- 0 until NumFibs) yield {
    val fibby = Module(new fibDF())
    fibby
  }
  val fib_continue = for (i <- 0 until NumFibs) yield {
    val fibby_continue = Module(new fib_continueDF())
    fibby_continue
  }
  val TC = Module(new TaskController(List(32,32), List(32), 1+(2*NumFibs), NumFibs))
  val StackArb = Module(new MemArbiter((2*NumFibs)+1))
  val Stack = Module(new StackMem((1 << tlen)*4))


  // Merge the memory interfaces and connect to the stack memory
  for (i <- 0 until NumFibs) {
    // Connect to memory interface
    StackArb.io.cpu.MemReq(2*i) <> fib(i).io.MemReq
    fib(i).io.MemResp <> StackArb.io.cpu.MemResp(2*i)
    StackArb.io.cpu.MemReq(2*i+1) <> fib_continue(i).io.MemReq
    fib_continue(i).io.MemResp <> StackArb.io.cpu.MemResp(2*i+1)

    // Connect fib to continuation
    fib_continue(i).io.in <> fib(i).io.call17_out
    fib(i).io.call17_in <> fib_continue(i).io.out

    // Connect to task controller
    TC.io.parentIn(2*i) <> fib(i).io.call10_out
    fib(i).io.call10_in <> TC.io.parentOut(2*i)
    TC.io.parentIn(2*i+1) <> fib(i).io.call14_out
    fib(i).io.call14_in <> TC.io.parentOut(2*i+1)
    fib(i).io.in <> TC.io.childOut(i)
    TC.io.childIn(i) <> fib(i).io.out
  }

  StackArb.io.cpu.MemReq(2*NumFibs) <> io.req
  io.resp <> StackArb.io.cpu.MemResp(2*NumFibs)

  Stack.io.req <> StackArb.io.cache.MemReq
  StackArb.io.cache.MemResp <> Stack.io.resp
  TC.io.parentIn(2*NumFibs) <> io.in
  io.out <> TC.io.parentOut(2*NumFibs)

}


class fibTest01[T <: fibMainIO](c : T, n : Int, tiles: Int) extends PeekPokeTester(c) {
  def fib( n : Int) : Int = n match {
    case 0 | 1 => n
    case _ => fib( n-1 ) + fib( n-2 )
  }
  def MemRead(addr:Int):BigInt = {
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

  def MemWrite(addr:Int, data:Int):BigInt = {
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

  val taskID = 0
  // Initializing the signals
  poke(c.io.in.bits.enable.control, false.B)
  poke(c.io.in.bits.enable.taskID, taskID)
  poke(c.io.in.valid, false.B)
  poke(c.io.in.bits.data("field0").data, 0.U)
  poke(c.io.in.bits.data("field0").taskID, taskID)
  poke(c.io.in.bits.data("field0").predicate, false.B)
  poke(c.io.in.bits.data("field1").data, 0.U)
  poke(c.io.in.bits.data("field1").taskID, taskID)
  poke(c.io.in.bits.data("field1").predicate, false.B)
  poke(c.io.out.ready, false.B)
  step(1)
  poke(c.io.in.bits.enable.control, true.B)
  poke(c.io.in.valid, true.B)
  poke(c.io.in.bits.data("field0").data, n)    // n
  poke(c.io.in.bits.data("field0").predicate, true.B)
  poke(c.io.in.bits.data("field1").data, 0)   // &r
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
  while (time < 100000 && !result) {
    time += 1
    step(1)
    if (peek(c.io.out.valid) == 1 &&
      peek(c.io.out.bits.data("field0").predicate) == 1
    ) {
      result = true
      println(Console.BLUE + s"*** Result returned for n=$n, t=$tiles. Run time: $time cycles." + Console.RESET)
    }
  }

//  step(100)

  val data = MemRead(0)
  val expected = fib(n)
  var valid_data = true
  if (data != expected) {
    println(Console.RED + s"*** Incorrect data received. Got $data. Hoping for $expected" + Console.RESET)
    fail
    valid_data = false
  }

  if (valid_data) {
    println(Console.BLUE + "*** Correct data written back." + Console.RESET)
  }

  if(!result) {
    println(Console.RED + "*** Timeout." + Console.RESET)
    fail
  }
}

object fibTesterParams {
//  val tile_list = List(1,2,4,8)
  val tile_list = List(4)
  val n_list = List(15)
}

class fibTester1 extends FlatSpec with Matchers {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  it should "Check that fib works correctly." in {
    for (tiles <- fibTesterParams.tile_list) {
      for (n <- fibTesterParams.n_list) {
        // iotester flags:
        // -ll  = log level <Error|Warn|Info|Debug|Trace>
        // -tbn = backend <firrtl|verilator|vcs>
        // -td  = target directory
        // -tts = seed for RNG
        chisel3.iotesters.Driver.execute(
          Array(
            // "-ll", "Info",
            "-tbn", "verilator",
            "-td", s"test_run_dir/fib_${tiles}_n${n}",
            "-tts", "0001"),
          () => new fibMain(tiles)(p.alterPartial({case TLEN => 11 case TRACE => false}))) {
          c => new fibTest01(c, n, tiles)
        } should be(true)
      }
    }
  }
}
