package dataflow

import java.io.BufferedWriter
import java.io.FileWriter
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


class cilk_for_test06MainIO(implicit val p: Parameters) extends Module with CoreParams with CacheParams {
  val io = IO(new CoreBundle {
    val in = Flipped(Decoupled(new Call(List(32, 32, 32))))
    val req  = Flipped(Decoupled(new MemReq))
    val resp = Output(Valid(new MemResp))
    val out = Decoupled(new Call(List(32)))
  })
}

class cilk_for_test06MainTM(tiles: Int)(implicit p: Parameters) extends cilk_for_test06MainIO()(p) {

  val cache = Module(new Cache) // Simple Nasti Cache
  val memModel = Module(new NastiMemSlave) // Model of DRAM to connect to Cache

  // Connect the wrapper I/O to the memory model initialization interface so the
  // test bench can write contents at start.
  memModel.io.nasti <> cache.io.nasti
  memModel.io.init.bits.addr := 0.U
  memModel.io.init.bits.data := 0.U
  memModel.io.init.valid := false.B
  cache.io.cpu.abort := false.B

  val children = tiles
  val TaskControllerModule = Module(new TaskController(List(32, 32, 32, 32), List(), 1, children))
  val cilk_for_test06 = Module(new cilk_for_test06DF())

  val cilk_for_test06_detach1 = for (i <- 0 until children) yield {
    val detach1 = Module(new cilk_for_test06_detach1DF())
    detach1
  }
  val cilk_for_test06_detach2 = for (i <- 0 until children) yield {
    val detach2 = Module(new cilk_for_test06_detach2DF)
    detach2
  }

  // Ugly hack to merge requests from two children.  "ReadWriteArbiter" merges two
  // requests ports of any type.  Read or write is irrelevant.
  val MemArbiter = Module(new MemArbiter(children+1))
  for (i <- 0 until children) {
    MemArbiter.io.cpu.MemReq(i) <> cilk_for_test06_detach2(i).io.MemReq
    cilk_for_test06_detach2(i).io.MemResp <> MemArbiter.io.cpu.MemResp(i)
  }
  MemArbiter.io.cpu.MemReq(children) <> io.req
  io.resp <> MemArbiter.io.cpu.MemResp(children)

  cache.io.cpu.req <> MemArbiter.io.cache.MemReq
  MemArbiter.io.cache.MemResp <> cache.io.cpu.resp

  // tester to cilk_for_test02
  cilk_for_test06.io.in <> io.in

  // cilk_for_test02 to task controller
  TaskControllerModule.io.parentIn(0) <> cilk_for_test06.io.call_9_out

  // task controller to sub-task cilk_for_test06_detach
  for (i <- 0 until children) {
    cilk_for_test06_detach1(i).io.in <> TaskControllerModule.io.childOut(i)
    cilk_for_test06_detach2(i).io.in <> cilk_for_test06_detach1(i).io.call_10_out
    cilk_for_test06_detach1(i).io.call_10_in <> cilk_for_test06_detach2(i).io.out
    TaskControllerModule.io.childIn(i) <> cilk_for_test06_detach1(i).io.out
  }

  // Task controller to cilk_for_test02
  cilk_for_test06.io.call_9_in <> TaskControllerModule.io.parentOut(0)

  // cilk_for_test02 to tester
  io.out <> cilk_for_test06.io.out

}

class cilk_for_test06Test01[T <: cilk_for_test06MainIO](c: T, tiles : Int) extends PeekPokeTester(c) {

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

  val inAddrVec = List.range(0, 200, 4) // byte addresses
  val inA = List.range(0, 25) // 5x5 array of uint32
  val inB = List.range(0, 25) // 5x5 array of uint32
  val inDataVec = inA ++ inB
  val outAddrVec = List.range(256, 256 + 100, 4)
  val outDataVec = inA.zip(inB).map { case (x, y) => x + y }

  val file = "test.txt"
  val writer = new BufferedWriter(new FileWriter(file))
  //  outDataVec.map(_.toString + "\n").foreach(writer.write)
  val writeResult = inA.zip(inB).zip(outDataVec)
  writeResult.map(_.toString() + "\n").foreach(writer.write)
  writer.close()

  var i = 0

  // Write initial contents to the memory model.
  for(i <- 0 until inDataVec.length) {
    MemWrite(inAddrVec(i), inDataVec(i))
  }
  step(1)


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
  poke(c.io.in.bits.data("field0").data, 0)
  poke(c.io.in.bits.data("field0").predicate, true.B)
  poke(c.io.in.bits.data("field1").data, 100)
  poke(c.io.in.bits.data("field1").predicate, true.B)
  poke(c.io.in.bits.data("field2").data, 256)
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
  while (time < 5000 && !result) {
    time += 1
    step(1)
    //println(s"Cycle: $time")
    if (peek(c.io.out.valid) == 1 &&
      peek(c.io.out.bits.data("field0").predicate) == 1
    ) {
      result = true
      val data = peek(c.io.out.bits.data("field0").data)
      if (data != 1) {
        println(Console.RED + s"*** Incorrect return result received. Got $data. Hoping for 1" + Console.RESET)
        fail
      } else {
        println(Console.BLUE + s"*** Return received for t=$tiles. $time cycles." + Console.RESET)
      }
    }
  }

  // Pause to make sure all writes have made it through arbiter to memory
  step(100)

  //  Read back expected results
  var valid_data = true
  for (i <- 0 until outDataVec.length) {
    val data = MemRead(outAddrVec(i))
    if (data != outDataVec(i).toInt) {
      println(Console.RED + s"*** Incorrect data received. Got $data. Hoping for ${outDataVec(i).toInt}" + Console.RESET)
      fail
      valid_data = false
    }
  }
  if (valid_data) {
    println(Console.BLUE + "*** Correct data written back." + Console.RESET)
  }

  if (!result) {
    println(Console.RED + "*** Timeout." + Console.RESET)
    fail
  }
}


class cilk_for_test06Tester1 extends FlatSpec with Matchers {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val testParams = p.alterPartial({
    case TLEN => 5
    case TRACE => false
  })
  // iotester flags:
  // -ll  = log level <Error|Warn|Info|Debug|Trace>
  // -tbn = backend <firrtl|verilator|vcs>
  // -td  = target directory
  // -tts = seed for RNG
//  val tile_list = List(1,2,4,8)
  val tile_list = List(8)
  for (tile <- tile_list) {
    it should s"Test: $tile tiles" in {
      chisel3.iotesters.Driver.execute(
        Array(
          "-ll", "Error",
          "-tbn", "verilator",
          "-td", s"test_run_dir/cilk_for_test06_${tile}",
          "-tts", "0001"),
        () => new cilk_for_test06MainTM(tile)(testParams)) {
        c => new cilk_for_test06Test01(c,tile)
      } should be(true)
    }
  }
}
