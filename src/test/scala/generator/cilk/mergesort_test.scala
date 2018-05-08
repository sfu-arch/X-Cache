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


class mergesortMainIO(implicit val p: Parameters)  extends Module with CoreParams with CacheParams {
  val io = IO( new CoreBundle {
    val in = Flipped(Decoupled(new Call(List(32,32))))
    val addr = Input(UInt(nastiXAddrBits.W))
    val din  = Input(UInt(nastiXDataBits.W))
    val write = Input(Bool())
    val dout = Output(UInt(nastiXDataBits.W))
    val out = Decoupled(new Call(List(32)))
  })
}

class mergesortMain(implicit p: Parameters) extends mergesortMainIO {

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
  io.dout := 0.U

  val NumMergesorts = 1
  val mergesort = for (i <- 0 until NumMergesorts) yield {
    val mergesortby = Module(new mergesortDF())
    mergesortby
  }
  val mergesort_merge = for (i <- 0 until NumMergesorts) yield {
    val mergesortby_continue = Module(new mergesort_mergeDF())
    mergesortby_continue
  }
  val TC = Module(new TaskController(List(32,32), List(32), 1+(2*NumMergesorts), NumMergesorts))
  val CacheArb = Module(new CacheArbiter(NumMergesorts))
  val StackArb = Module(new CacheArbiter(2*NumMergesorts))
  val Stack = Module(new StackMem((1 << tlen)*4))


  // Merge the memory interfaces and connect to the stack memory
  for (i <- 0 until NumMergesorts) {
    // Connect to stack memory interface
    StackArb.io.cpu.MemReq(2*i) <> mergesort(i).io.StackReq
    mergesort(i).io.StackResp <> StackArb.io.cpu.MemResp(2*i)
    StackArb.io.cpu.MemReq(2*i+1) <> mergesort_merge(i).io.StackReq
    mergesort_merge(i).io.StackResp <> StackArb.io.cpu.MemResp(2*i+1)

    // Connect to cache memory
    CacheArb.io.cpu.MemReq(i) <> mergesort_merge(i).io.GlblReq
    mergesort_merge(i).io.GlblResp <> CacheArb.io.cpu.MemResp(i)

    // Connect mergesort to continuation
    mergesort_merge(i).io.in <> mergesort(i).io.call24_out
    mergesort(i).io.call24_in <> mergesort_merge(i).io.out

    // Connect to task controller
    TC.io.parentIn(2*i) <> mergesort(i).io.call18_out
    mergesort(i).io.call18_in <> TC.io.parentOut(2*i)
    TC.io.parentIn(2*i+1) <> mergesort(i).io.call21_out
    mergesort(i).io.call21_in <> TC.io.parentOut(2*i+1)
    mergesort(i).io.in <> TC.io.childOut(i)
    TC.io.childIn(i) <> mergesort(i).io.out
  }

  cache.io.cpu.req <> CacheArb.io.cache.MemReq
  CacheArb.io.cache.MemResp <> cache.io.cpu.resp

  Stack.io.req <> StackArb.io.cache.MemReq
  StackArb.io.cache.MemResp <> Stack.io.resp
  TC.io.parentIn(2*NumMergesorts) <> io.in
  io.out <> TC.io.parentOut(2*NumMergesorts)

}

class mergesortTest01[T <: mergesortMainIO](c: T) extends PeekPokeTester(c) {

    // recursive merge of 2 sorted lists
    def merge(left: List[Int], right: List[Int]): List[Int] =
      (left, right) match {
        case(left, Nil) => left
        case(Nil, right) => right
        case(leftHead :: leftTail, rightHead :: rightTail) =>
          if (leftHead < rightHead) leftHead::merge(leftTail, right)
          else rightHead :: merge(left, rightTail)
      }

    def mergeSort(list: List[Int]): List[Int] = {
      val n = list.length / 2
      if (n == 0) list // i.e. if list is empty or single value, no sorting needed
      else {
        val (left, right) = list.splitAt(n)
        merge(mergeSort(left), mergeSort(right))
      }
    }

  val inDataVec = List(99, 41, 18, 66, 88, 27, 74, 25, 35, 68,
    20, 64, 39, 62, 62, 27, 76, 97, 60)
  val outDataVec = mergeSort(inDataVec)
  val addrVec = List.range(0, 4*inDataVec.length*2, 4)

  poke(c.io.addr, 0.U)
  poke(c.io.din, 0.U)
  poke(c.io.write, false.B)

  // Write initial contents to the memory model.
  for(i <- 0 until addrVec.length) {
    poke(c.io.addr, addrVec(i))
    poke(c.io.din, inDataVec(i))
    poke(c.io.write, true.B)
    step(1)
  }
  poke(c.io.write, false.B)
  step(1)

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
  poke(c.io.in.bits.data("field2").data, 0.U)
  poke(c.io.in.bits.data("field2").taskID, taskID)
  poke(c.io.in.bits.data("field2").predicate, false.B)
  poke(c.io.in.bits.data("field3").data, 0.U)
  poke(c.io.in.bits.data("field3").taskID, taskID)
  poke(c.io.in.bits.data("field3").predicate, false.B)
  poke(c.io.out.ready, false.B)
  step(1)
  poke(c.io.in.bits.enable.control, true.B)
  poke(c.io.in.valid, true.B)
  poke(c.io.in.bits.data("field0").data, 4*inDataVec.length)  // B[]
  poke(c.io.in.bits.data("field0").predicate, true.B)
  poke(c.io.in.bits.data("field1").data, 0)                   // iBegin
  poke(c.io.in.bits.data("field1").predicate, true.B)
  poke(c.io.in.bits.data("field2").data, inDataVec.length)    // iEnd
  poke(c.io.in.bits.data("field2").predicate, true.B)
  poke(c.io.in.bits.data("field3").data, 0)                   // A[]
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
  while (time < 10000) {
    time += 1
    step(1)
    if (peek(c.io.out.valid) == 1 &&
      peek(c.io.out.bits.data("field0").predicate) == 1
    ) {
      result = true
      val expected = 99
      val data = peek(c.io.out.bits.data("field0").data)
      if (data != expected) {
        println(Console.RED + s"*** Incorrect result received. Got $data. Hoping for $expected" + Console.RESET)
        fail
      } else {
        println(Console.BLUE + s"*** Correct result. Run time: $time cycles." + Console.RESET)
      }
    }
  }

  //  Peek into the CopyMem to see if the expected data is written back to the Cache
  var valid_data = true
  for(i <- 0 until addrVec.length) {
    poke(c.io.addr, addrVec(i))
    step(1)
    val data = peek(c.io.dout)
    if (data != outDataVec(i).toInt) {
      println(Console.RED + s"*** Incorrect data received addr=${addrVec(i)}. Got $data. Hoping for ${outDataVec(i).toInt}" + Console.RESET)
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

class mergesortTester1 extends FlatSpec with Matchers {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  it should "Check that mergesort works correctly." in {
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
      () => new mergesortMain()) {
      c => new mergesortTest01(c)
    } should be(true)
  }
}
