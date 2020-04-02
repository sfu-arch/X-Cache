package dandelion.generator.cilk

import chisel3._
import chisel3.util._
import chisel3.Module
import chisel3.testers._
import dandelion.concurrent.{TaskController, TaskControllerIO}
import chisel3.iotesters._
import org.scalatest.{FlatSpec, Matchers}

import scala.util.control.Breaks._
import muxes._
import chipsalliance.rocketchip.config._
import dandelion.config._
import dandelion.control._
import util._
import dandelion.interfaces._
import regfile._
import dandelion.memory._
import dandelion.memory.stack._
import dandelion.arbiters._
import dandelion.loop._
import dandelion.accel._
import dandelion.memory.cache.{HasCacheAccelParams, ReferenceCache}


class mergesortMainIO(implicit val p: Parameters) extends Module with HasAccelParams
  with HasAccelShellParams
  with HasCacheAccelParams {
  val io = IO(new AccelBundle {
    val in = Flipped(Decoupled(new Call(List(32, 32, 32, 32))))
    val req = Flipped(Decoupled(new MemReq))
    val resp = Output(Valid(new MemResp))
    val out = Decoupled(new Call(List(32)))
  })
}


class mergesortMain1(tiles: Int)(implicit p: Parameters) extends mergesortMainIO {

  val cache = Module(new ReferenceCache) // Simple Nasti Cache
  val memModel = Module(new NastiMemSlave) // Model of DRAM to connect to Cache

  // Connect the wrapper I/O to the memory model initialization interface so the
  // test bench can write contents at start.
  memModel.io.nasti <> cache.io.mem
  memModel.io.init.bits.addr := 0.U
  memModel.io.init.bits.data := 0.U
  memModel.io.init.valid := false.B
  cache.io.cpu.abort := false.B

  val NumMergesorts = tiles
  val mergesort = for (i <- 0 until NumMergesorts) yield {
    val mergesortby = Module(new mergesortDF())
    mergesortby
  }
  val mergesort_merge = for (i <- 0 until NumMergesorts) yield {
    val mergesortby_continue = Module(new mergesort_mergeDF())
    mergesortby_continue
  }
  val TC = Module(new TaskController(List(32, 32, 32, 32), List(32), 1 + (2 * NumMergesorts), NumMergesorts))
  val CacheArb = Module(new MemArbiter(NumMergesorts + 1))
  val StackArb = Module(new MemArbiter(2 * NumMergesorts))
  val Stack = Module(new StackMem((1 << tlen) * 4))


  // Merge the memory interfaces and connect to the stack memory
  for (i <- 0 until NumMergesorts) {
    // Connect to stack memory interface
    StackArb.io.cpu.MemReq(2 * i) <> mergesort(i).io.StackReq
    mergesort(i).io.StackResp <> StackArb.io.cpu.MemResp(2 * i)
    StackArb.io.cpu.MemReq(2 * i + 1) <> mergesort_merge(i).io.StackReq
    mergesort_merge(i).io.StackResp <> StackArb.io.cpu.MemResp(2 * i + 1)

    // Connect to cache memory
    CacheArb.io.cpu.MemReq(i) <> mergesort_merge(i).io.GlblReq
    mergesort_merge(i).io.GlblResp <> CacheArb.io.cpu.MemResp(i)

    // Connect mergesort to continuation
    mergesort_merge(i).io.in <> mergesort(i).io.call24_out
    mergesort(i).io.call24_in <> mergesort_merge(i).io.out

    // Connect to task controller
    TC.io.parentIn(2 * i) <> mergesort(i).io.call18_out
    mergesort(i).io.call18_in <> TC.io.parentOut(2 * i)
    TC.io.parentIn(2 * i + 1) <> mergesort(i).io.call21_out
    mergesort(i).io.call21_in <> TC.io.parentOut(2 * i + 1)
    mergesort(i).io.in <> TC.io.childOut(i)
    TC.io.childIn(i) <> mergesort(i).io.out
  }

  CacheArb.io.cpu.MemReq(NumMergesorts) <> io.req
  io.resp <> CacheArb.io.cpu.MemResp(NumMergesorts)

  cache.io.cpu.req <> CacheArb.io.cache.MemReq
  CacheArb.io.cache.MemResp <> cache.io.cpu.resp

  Stack.io.req <> StackArb.io.cache.MemReq
  StackArb.io.cache.MemResp <> Stack.io.resp
  TC.io.parentIn(2 * NumMergesorts) <> io.in
  io.out <> TC.io.parentOut(2 * NumMergesorts)

}

class mergesortMain2(tiles: Int)(implicit p: Parameters) extends mergesortMainIO {

  val NumMergesorts = tiles
  val mergesort = for (i <- 0 until NumMergesorts) yield {
    val mergesortby = Module(new mergesortDF())
    mergesortby
  }
  val mergesort_merge = for (i <- 0 until NumMergesorts) yield {
    val mergesortby_continue = Module(new mergesort_mergeDF())
    mergesortby_continue
  }
  val TC = Module(new TaskController(List(32, 32, 32, 32), List(32), 1 + (2 * NumMergesorts), NumMergesorts))
  val SortArb = Module(new MemArbiter(NumMergesorts + 1))
  val StackArb = Module(new MemArbiter(2 * NumMergesorts))
  val Stack = Module(new StackMem((1 << tlen) * 4))
  val SortMem = Module(new StackMem(2048))


  // Merge the memory interfaces and connect to the stack memory
  for (i <- 0 until NumMergesorts) {
    // Connect to stack memory interface
    StackArb.io.cpu.MemReq(2 * i) <> mergesort(i).io.StackReq
    mergesort(i).io.StackResp <> StackArb.io.cpu.MemResp(2 * i)
    StackArb.io.cpu.MemReq(2 * i + 1) <> mergesort_merge(i).io.StackReq
    mergesort_merge(i).io.StackResp <> StackArb.io.cpu.MemResp(2 * i + 1)

    // Connect to cache memory
    SortArb.io.cpu.MemReq(i) <> mergesort_merge(i).io.GlblReq
    mergesort_merge(i).io.GlblResp <> SortArb.io.cpu.MemResp(i)

    // Connect mergesort to continuation
    mergesort_merge(i).io.in <> mergesort(i).io.call24_out
    mergesort(i).io.call24_in <> mergesort_merge(i).io.out

    // Connect to task controller
    TC.io.parentIn(2 * i) <> mergesort(i).io.call18_out
    mergesort(i).io.call18_in <> TC.io.parentOut(2 * i)
    TC.io.parentIn(2 * i + 1) <> mergesort(i).io.call21_out
    mergesort(i).io.call21_in <> TC.io.parentOut(2 * i + 1)
    mergesort(i).io.in <> TC.io.childOut(i)
    TC.io.childIn(i) <> mergesort(i).io.out
  }

  SortArb.io.cpu.MemReq(NumMergesorts) <> io.req
  io.resp <> SortArb.io.cpu.MemResp(NumMergesorts)

  SortMem.io.req <> SortArb.io.cache.MemReq
  SortArb.io.cache.MemResp <> SortMem.io.resp

  Stack.io.req <> StackArb.io.cache.MemReq
  StackArb.io.cache.MemResp <> Stack.io.resp
  TC.io.parentIn(2 * NumMergesorts) <> io.in
  io.out <> TC.io.parentOut(2 * NumMergesorts)

}

class mergesortMain3(tiles: Int)(implicit p: Parameters) extends mergesortMainIO {

  val cache = Module(new Cache) // Simple Nasti Cache
  val memModel = Module(new NastiMemSlave) // Model of DRAM to connect to Cache

  // Connect the wrapper I/O to the memory model initialization interface so the
  // test bench can write contents at start.
  memModel.io.nasti <> cache.io.nasti
  memModel.io.init.bits.addr := 0.U
  memModel.io.init.bits.data := 0.U
  memModel.io.init.valid := false.B
  cache.io.cpu.abort := false.B

  val NumMergesorts = tiles
  val mergesort = for (i <- 0 until NumMergesorts) yield {
    val mergesortby = Module(new mergesortDF())
    mergesortby
  }
  val mergesort_merge = for (i <- 0 until NumMergesorts) yield {
    val mergesortby_continue = Module(new mergesort_mergeDF())
    mergesortby_continue
  }
  val TC = Module(new TaskController(List(32, 32, 32, 32), List(32), 1 + (2 * NumMergesorts), NumMergesorts))
  val CacheArb = Module(new MemArbiter(NumMergesorts + 1))
  //  val StackArb = Module(new MemArbiter(2*NumMergesorts))
  //  val Stack = Module(new StackMem((1 << tlen)*4))
  val Stack = Module(new InterleavedStack((1 << tlen) * 4, List(3, 2), 2 * NumMergesorts))

  // Merge the memory interfaces and connect to the stack memory
  for (i <- 0 until NumMergesorts) {
    // Connect to stack memory interface
    Stack.io.MemReq(2 * i) <> mergesort(i).io.StackReq
    mergesort(i).io.StackResp <> Stack.io.MemResp(2 * i)
    Stack.io.MemReq(2 * i + 1) <> mergesort_merge(i).io.StackReq
    mergesort_merge(i).io.StackResp <> Stack.io.MemResp(2 * i + 1)

    // Connect to cache memory
    CacheArb.io.cpu.MemReq(i) <> mergesort_merge(i).io.GlblReq
    mergesort_merge(i).io.GlblResp <> CacheArb.io.cpu.MemResp(i)

    // Connect mergesort to continuation
    mergesort_merge(i).io.in <> mergesort(i).io.call24_out
    mergesort(i).io.call24_in <> mergesort_merge(i).io.out

    // Connect to task controller
    TC.io.parentIn(2 * i) <> mergesort(i).io.call18_out
    mergesort(i).io.call18_in <> TC.io.parentOut(2 * i)
    TC.io.parentIn(2 * i + 1) <> mergesort(i).io.call21_out
    mergesort(i).io.call21_in <> TC.io.parentOut(2 * i + 1)
    mergesort(i).io.in <> TC.io.childOut(i)
    TC.io.childIn(i) <> mergesort(i).io.out
  }

  CacheArb.io.cpu.MemReq(NumMergesorts) <> io.req
  io.resp <> CacheArb.io.cpu.MemResp(NumMergesorts)

  cache.io.cpu.req <> CacheArb.io.cache.MemReq
  CacheArb.io.cache.MemResp <> cache.io.cpu.resp

  //  Stack.io.req <> StackArb.io.cache.MemReq
  //  StackArb.io.cache.MemResp <> Stack.io.resp
  TC.io.parentIn(2 * NumMergesorts) <> io.in
  io.out <> TC.io.parentOut(2 * NumMergesorts)

}

class mergesortTest01[T <: mergesortMainIO](c: T, len: Int) extends PeekPokeTester(c) {

  // recursive merge of 2 sorted lists
  def merge(left: List[Int], right: List[Int]): List[Int] =
    (left, right) match {
      case (left, Nil) => left
      case (Nil, right) => right
      case (leftHead :: leftTail, rightHead :: rightTail) =>
        if (leftHead < rightHead) leftHead :: merge(leftTail, right)
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

  scala.util.Random.setSeed(1234)
  val inDataVec = List.fill(len)(scala.util.Random.nextInt(256))
  val inAddrVec = List.range(0, 4 * inDataVec.length, 4)
  val outDataVec = mergeSort(inDataVec)
  val outAddrVec = List.range(4 * inDataVec.length, 4 * inDataVec.length * 2, 4)


  // Write initial contents to the memory model.
  for (i <- 0 until inAddrVec.length) {
    MemWrite(inAddrVec(i), inDataVec(i))
  }
  for (i <- 0 until outAddrVec.length) {
    MemWrite(outAddrVec(i), inDataVec(i))
  }

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
  poke(c.io.in.bits.data("field0").data, 4 * inDataVec.length) // B[]
  poke(c.io.in.bits.data("field0").predicate, true.B)
  poke(c.io.in.bits.data("field1").data, 0) // iBegin
  poke(c.io.in.bits.data("field1").predicate, true.B)
  poke(c.io.in.bits.data("field2").data, inDataVec.length) // iEnd
  poke(c.io.in.bits.data("field2").predicate, true.B)
  poke(c.io.in.bits.data("field3").data, 0) // A[]
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
  while (time < 100000 && !result) {
    time += 1
    step(1)
    if (peek(c.io.out.valid) == 1 &&
      peek(c.io.out.bits.data("field0").predicate) == 1
    ) {
      result = true
      val expected = 1
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
  for (i <- 0 until outDataVec.length) {
    val data = MemRead(inAddrVec(i))
    if (data != outDataVec(i)) {
      println(Console.RED + s"*** Incorrect data received addr=${inAddrVec(i)}. Got $data. Hoping for ${outDataVec(i)}" + Console.RESET)
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

object mergesortTesterParams {
  //  val tile_list = List(1,2,4,8)
  val tile_list = List(4)
  val len_list = List(100)
}

//class mergesortTester1 extends FlatSpec with Matchers {
//  implicit val p = new WithAccelConfig
//  val testParams = p.alterPartial({
//    case TLEN => 11
//    case TRACE => false
//  })
//  it should "Check that mergesort works correctly." in {
//    // iotester flags:
//    // -ll  = log level <Error|Warn|Info|Debug|Trace>
//    // -tbn = backend <firrtl|verilator|vcs>
//    // -td  = target directory
//    // -tts = seed for RNG
//    for (tiles <- mergesortTesterParams.tile_list) {
//      for (len <- mergesortTesterParams.len_list) {
//        chisel3.iotesters.Driver.execute(
//          Array(
//            // "-ll", "Info",
//            s"-tbn", "verilator",
//            "-td", s"test_run_dir/mergesort_${tiles}_l${len}",
//            "-tts", "0001"),
//          () => new mergesortMain1(tiles)(testParams)) {
//          c => new mergesortTest01(c, len)
//        } should be(true)
//      }
//    }
//  }
//}

