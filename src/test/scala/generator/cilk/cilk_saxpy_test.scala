package dandelion.generator.cilk

import chisel3._
import chisel3.Module
import org.scalatest.{FlatSpec, Matchers}
import chipsalliance.rocketchip.config._
import dandelion.config._
import dandelion.concurrent.{TaskController, TaskControllerIO}
import dandelion.memory._
import dandelion.accel._
import dandelion.interfaces.NastiMemSlave
import dandelion.memory.cache.ReferenceCache
import helpers._

class cilk_saxpyMainTM(children: Int)(implicit p: Parameters)
  extends AccelIO(List(32, 32, 32, 32), List(32)) {

  val cache = Module(new ReferenceCache) // Simple Nasti Cache
  val memModel = Module(new NastiMemSlave(latency = 10)) // Model of DRAM to connect to Cache

  // Connect the wrapper I/O to the memory model initialization interface so the
  // test bench can write contents at start.
  memModel.io.nasti <> cache.io.mem
  memModel.io.init.bits.addr := 0.U
  memModel.io.init.bits.data := 0.U
  memModel.io.init.valid := false.B
  cache.io.cpu.abort := false.B

  // Wire up the cache, TM, and modules under test.

  val TaskControllerModule = Module(new TaskController(List(32, 32, 32, 32), List(), 1, children))
  val saxpy = Module(new cilk_saxpyDF())

  val saxpy_detach = for (i <- 0 until children) yield {
    val detach1 = Module(new cilk_saxpy_detach1DF())
    detach1
  }

  // Ugly hack to merge requests from two children.  "ReadWriteArbiter" merges two
  // requests ports of any type.  Read or write is irrelevant.
  // saxpy to task controller
  val CacheArbiter = Module(new MemArbiter(children + 2))

  for (i <- 0 until children) {
    saxpy_detach(i).io.in <> TaskControllerModule.io.childOut(i)
    TaskControllerModule.io.childIn(i) <> saxpy_detach(i).io.out

    CacheArbiter.io.cpu.MemReq(i) <> saxpy_detach(i).io.MemReq
    saxpy_detach(i).io.MemResp <> CacheArbiter.io.cpu.MemResp(i)
  }

  CacheArbiter.io.cpu.MemReq(children) <> io.req
  io.resp <> CacheArbiter.io.cpu.MemResp(children)

  CacheArbiter.io.cpu.MemReq(children + 1) <> saxpy.io.MemReq
  saxpy.io.MemResp <> CacheArbiter.io.cpu.MemResp(children + 1)

  TaskControllerModule.io.parentIn(0) <> saxpy.io.call_11_out
  saxpy.io.call_11_in <> TaskControllerModule.io.parentOut(0)


  cache.io.cpu.req <> CacheArbiter.io.cache.MemReq
  CacheArbiter.io.cache.MemResp <> cache.io.cpu.resp


  // tester to saxpy
  saxpy.io.in <> io.in
  io.out <> saxpy.io.out

}

class cilk_saxpyTest01[T <: AccelIO](c: T, n: Int, ch: Int)
                                    (inAddrVec: List[Int], inDataVec: List[Int],
                                     outAddrVec: List[Int], outDataVec: List[Int])
  extends AccelTesterLocal(c)(inAddrVec, inDataVec, outAddrVec, outDataVec) {


  val dataLen = n;
  val a = 5 // 'a' of a*x[i]+y[i]

  initMemory()

  // Initializing the signals
  poke(c.io.in.bits.enable.control, false)
  poke(c.io.in.valid, false)
  poke(c.io.in.bits.data("field0").data, 0)
  poke(c.io.in.bits.data("field0").taskID, 1)
  poke(c.io.in.bits.data("field0").predicate, false)
  poke(c.io.in.bits.data("field1").data, 0)
  poke(c.io.in.bits.data("field1").taskID, 1)
  poke(c.io.in.bits.data("field1").predicate, false)
  poke(c.io.in.bits.data("field2").data, 0)
  poke(c.io.in.bits.data("field2").taskID, 1)
  poke(c.io.in.bits.data("field2").predicate, false)
  poke(c.io.in.bits.data("field3").data, 0)
  poke(c.io.in.bits.data("field3").taskID, 1)
  poke(c.io.in.bits.data("field3").predicate, false)
  poke(c.io.out.ready, false)
  step(1)
  poke(c.io.in.bits.enable.control, true)
  poke(c.io.in.valid, true)
  poke(c.io.in.bits.data("field0").data, n) // Number of iterations
  poke(c.io.in.bits.data("field0").predicate, true)
  poke(c.io.in.bits.data("field1").data, a) // Scale value
  poke(c.io.in.bits.data("field1").predicate, true)
  poke(c.io.in.bits.data("field2").data, 0) // X[]
  poke(c.io.in.bits.data("field2").predicate, true)
  poke(c.io.in.bits.data("field3").data, 4 * dataLen) // Y[]
  poke(c.io.in.bits.data("field3").predicate, true.B)
  poke(c.io.out.ready, true)
  step(1)
  poke(c.io.in.bits.enable.control, false)
  poke(c.io.in.valid, false)
  poke(c.io.in.bits.data("field0").data, 0)
  poke(c.io.in.bits.data("field0").predicate, false)
  poke(c.io.in.bits.data("field1").data, 0)
  poke(c.io.in.bits.data("field1").predicate, false)
  poke(c.io.in.bits.data("field2").data, 0)
  poke(c.io.in.bits.data("field2").predicate, false)
  poke(c.io.in.bits.data("field3").data, 0)
  poke(c.io.in.bits.data("field3").predicate, false)

  step(1)

  // NOTE: Don't use assert().  It seems to terminate the writing of VCD files
  // early (before the error) which makes debugging very difficult. Check results
  // using if() and fail command.
  var time = 0
  var result = false
  while (time < 90000 && !result) {
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

  checkMemory()

  if (!result) {
    println(Console.RED + "*** Timeout." + Console.RESET)
    fail
  }
}

class cilk_saxpyTester1 extends FlatSpec with Matchers {

  val dataLen = 500 // 500 for intel FPGA test
  val cof_a = 5 // 'a' of a*x[i]+y[i]
  val inAddrVec = List.range(0, 2 * (4 * dataLen), 4)
  val inX = List.range(1, dataLen + 1) // array of int 32
  val inY = List.range(1, dataLen + 1) // array of int 32
  val inDataVec = inX ++ inY
  val outAddrVec = List.range(4 * dataLen, 8 * dataLen, 4)
  val outDataVec = inX.zip(inY).map { case (x, y) => cof_a * x + y }

  implicit val p = new WithAccelConfig ++ new WithTestConfig
  // iotester flags:
  // -ll  = log level <Error|Warn|Info|Debug|Trace>
  // -tbn = backend <firrtl|verilator|vcs>
  // -td  = target directory
  // -tts = seed for RNG
  //  val tile_list = List(1,2,4,8)
  val tile_list = List(2)
  for (tile <- tile_list) {
    it should s"Test: $tile tiles" in {
      chisel3.iotesters.Driver.execute(
        Array(
          // "-ll", "Info",
          "-tbn", "verilator",
          "-td", s"test_run_dir/cilk_saxpy_${tile}",
          "-tts", "0001"),
        () => new cilk_saxpyMainTM(tile)(p)) {
        c => new cilk_saxpyTest01(c, dataLen, tile)(inAddrVec, inDataVec, outAddrVec, outDataVec)
      } should be(true)
    }
  }
}
