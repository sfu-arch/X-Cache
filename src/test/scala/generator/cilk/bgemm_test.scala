package dandelion.generator.cilk


import chisel3._
import chisel3.Module
import org.scalatest.{FlatSpec, Matchers}
import dandelion.concurrent.{TaskController,TaskControllerIO}
import dandelion.config._
import memory._
import accel._
import scala.util.Random
import helpers._


class bgemmMainDirect(implicit p: Parameters) extends
  AccelIO(List(32, 32, 32), List())(p) {

  val cache = Module(new Cache) // Simple Nasti Cache
  val memModel = Module(new NastiMemSlave(latency = 80)) // Model of DRAM to connect to Cache

  // Connect the wrapper I/O to the memory model initialization interface so the
  // test bench can write contents at start.
  memModel.io.nasti <> cache.io.nasti
  memModel.io.init.bits.addr := 0.U
  memModel.io.init.bits.data := 0.U
  memModel.io.init.valid := false.B
  cache.io.cpu.abort := false.B


  val bgemm = Module(new bgemmDF())
  val bgemm_detach1 = Module(new bgemm_detach1DF())
  val bgemm_detach2 = Module(new bgemm_detach2DF())
  val bgemm_detach3 = Module(new bgemm_detach3DF())


  bgemm.io.MemResp <> DontCare
  bgemm.io.MemReq <> DontCare

  bgemm_detach1.io.MemResp <> DontCare
  bgemm_detach1.io.MemReq <> DontCare

  bgemm_detach2.io.MemResp <> DontCare
  bgemm_detach2.io.MemReq <> DontCare

  val MemArbiter = Module(new MemArbiter(2))

  MemArbiter.io.cpu.MemReq(0) <> bgemm_detach3.io.MemReq
  bgemm_detach3.io.MemResp <> MemArbiter.io.cpu.MemResp(0)

  MemArbiter.io.cpu.MemReq(1) <> io.req
  io.resp <> MemArbiter.io.cpu.MemResp(1)

  cache.io.cpu.req <> MemArbiter.io.cache.MemReq
  MemArbiter.io.cache.MemResp <> cache.io.cpu.resp

  bgemm_detach1.io.in <> bgemm.io.call_9_out
  bgemm_detach2.io.in <> bgemm_detach1.io.call_9_out
  bgemm_detach3.io.in <> bgemm_detach2.io.call_8_out
  bgemm_detach2.io.call_8_in <> bgemm_detach3.io.out
  bgemm_detach1.io.call_9_in <> bgemm_detach2.io.out
  bgemm.io.call_9_in <> bgemm_detach1.io.out

  bgemm.io.in <> io.in
  io.out <> bgemm.io.out

}

class bgemmMainTM(val Tile: Int = 1)(implicit p: Parameters)
  extends AccelIO(List(32, 32, 32), List())(p) {

  val cache = Module(new Cache) // Simple Nasti Cache
  val memModel = Module(new NastiMemSlave(latency = 80)) // Model of DRAM to connect to Cache

  // Connect the wrapper I/O to the memory model initialization interface so the
  // test bench can write contents at start.
  memModel.io.nasti <> cache.io.nasti
  memModel.io.init.bits.addr := 0.U
  memModel.io.init.bits.data := 0.U
  memModel.io.init.valid := false.B
  cache.io.cpu.abort := false.B


  val children = Tile
  val TaskControllerModule = Module(new TaskController(List(32, 32, 32, 32), List(), 1, children))


  val bgemm = Module(new bgemmDF())

  val bgemm_detach1 = for (i <- 0 until children) yield {
    val detach = Module(new bgemm_detach1DF())
    detach
  }
  val bgemm_detach2 = for (i <- 0 until children) yield {
    val detach2 = Module(new bgemm_detach2DF)
    detach2
  }
  val bgemm_detach3 = for (i <- 0 until children) yield {
    val detach3 = Module(new bgemm_detach3DF)
    detach3
  }

  // Merge requests from two children.
  val MemArbiter = Module(new MemArbiter(children + 1))
  for (i <- 0 until children) {
    MemArbiter.io.cpu.MemReq(i) <> bgemm_detach3(i).io.MemReq
    bgemm_detach3(i).io.MemResp <> MemArbiter.io.cpu.MemResp(i)
  }

  for (i <- 0 until children) {
    bgemm_detach1(i).io.MemReq <> DontCare
    bgemm_detach1(i).io.MemResp <> DontCare

    bgemm_detach2(i).io.MemReq <> DontCare
    bgemm_detach2(i).io.MemResp <> DontCare

  }

  bgemm.io.MemResp <> DontCare
  bgemm.io.MemReq <> DontCare

  MemArbiter.io.cpu.MemReq(children) <> io.req
  io.resp <> MemArbiter.io.cpu.MemResp(children)

  cache.io.cpu.req <> MemArbiter.io.cache.MemReq
  MemArbiter.io.cache.MemResp <> cache.io.cpu.resp

  // tester to cilk_for_test02
  bgemm.io.in <> io.in

  // cilk_for_test02 to task controller
  TaskControllerModule.io.parentIn(0) <> bgemm.io.call_9_out

  // task controller to sub-task bgemm_detach
  for (i <- 0 until children) {
    bgemm_detach1(i).io.in <> TaskControllerModule.io.childOut(i)
    bgemm_detach2(i).io.in <> bgemm_detach1(i).io.call_9_out
    bgemm_detach3(i).io.in <> bgemm_detach2(i).io.call_8_out
    bgemm_detach2(i).io.call_8_in <> bgemm_detach3(i).io.out
    bgemm_detach1(i).io.call_9_in <> bgemm_detach2(i).io.out
    TaskControllerModule.io.childIn(i) <> bgemm_detach1(i).io.out
  }

  // Task controller to cilk_for_test02
  bgemm.io.call_9_in <> TaskControllerModule.io.parentOut(0)

  // cilk_for_test02 to tester
  io.out <> bgemm.io.out

}

class bgemmTest01[T <: AccelIO](c: T)
                               (inAddrVec: List[Int], inDataVec: List[Int],
                                outAddrVec: List[Int], outDataVec: List[Int])
  extends AccelTesterLocal(c)(inAddrVec, inDataVec, outAddrVec, outDataVec) {

  initMemory()

  // Initializing the signals
  poke(c.io.in.bits.enable.control, false)
  poke(c.io.in.bits.enable.taskID, 0)
  poke(c.io.in.valid, false)
  poke(c.io.in.bits.data("field0").data, 0)
  poke(c.io.in.bits.data("field0").predicate, false)
  poke(c.io.in.bits.data("field1").data, 0)
  poke(c.io.in.bits.data("field1").predicate, false)
  poke(c.io.in.bits.data("field2").data, 0)
  poke(c.io.in.bits.data("field2").predicate, false)
  poke(c.io.out.ready, false)
  step(1)
  poke(c.io.in.bits.enable.control, true)
  poke(c.io.in.valid, true)
  poke(c.io.in.bits.data("field0").data, 0)
  poke(c.io.in.bits.data("field0").predicate, true)
  poke(c.io.in.bits.data("field1").data, 64)
  poke(c.io.in.bits.data("field1").predicate, true)
  poke(c.io.in.bits.data("field2").data, 256)
  poke(c.io.in.bits.data("field2").predicate, true)
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

  step(1)

  // NOTE: Don't use assert().  It seems to terminate the writing of VCD files
  // early (before the error) which makes debugging very difficult. Check results
  // using if() and fail command.
  var time = 0 //Cycle counter
  var result = false
  while (time < 8000) {
    time += 1
    step(1)
    if (peek(c.io.out.valid) == 1) {
      result = true
      println(Console.BLUE + s"*** Bgemm finished. Run time: $time cycles." + Console.RESET)
    }
  }
  //  Peek into the CopyMem to see if the expected data is written back to the Cache

  checkMemory()

  if (!result) {
    println(Console.RED + "*** Timeout." + Console.RESET)
    fail
  }
}

class bgemmTester1 extends FlatSpec with Matchers {

  val inAddrVec = List.range(0, 4 * 32, 4) // byte addresses
  val inA = List.range(0, 16) // 4x4 array of uint32
  val inB = List.range(0, 16) // 4x4 array of uint32
  val inDataVec = inA ++ inB
  val outAddrVec = List.range(256, 256 + (4 * 16), 4)
  val outDataVec = List(
    56, 62, 68, 74
    , 152, 174, 196, 218
    , 248, 286, 324, 362
    , 344, 398, 452, 506
  )


  implicit val p = Parameters.root((new MiniConfig).toInstance)
  it should "Check that bgemm works correctly." in {
    // iotester flags:
    // -ll  = log level <Error|Warn|Info|Debug|Trace>
    // -tbn = backend <firrtl|verilator|vcs>
    // -td  = target directory
    // -tts = seed for RNG
    chisel3.iotesters.Driver.execute(
      Array(
        // "-ll", "Info",
        "-tn", "bgemmDirect",
        "-tbn", "verilator",
        "-td", "test_run_dir/bgemmDirect",
        "-tts", "0001"),
      () => new bgemmMainDirect()) {
      c => new bgemmTest01(c)(inAddrVec, inDataVec, outAddrVec, outDataVec)
    } should be(true)
  }
}

class bgemmTester2 extends FlatSpec with Matchers {
  val inAddrVec = List.range(0, 4 * 32 , 4) // byte addresses
  val inA = List.range(0, 16) // 4x4 array of uint32
  val inB = List.range(0, 16) // 4x4 array of uint32

  val inDataVec = inA ++ inB
  val outAddrVec = List.range(256, 256 + (4 * 16), 4)
  val outDataVec = List(
    56, 62, 68, 74
    , 152, 174, 196, 218
    , 248, 286, 324, 362
    , 344, 398, 452, 506
  )


  implicit val p = Parameters.root((new MiniConfig).toInstance)
  it should "Check that bgemm works correctly." in {
    // iotester flags:
    // -ll  = log level <Error|Warn|Info|Debug|Trace>
    // -tbn = backend <firrtl|verilator|vcs>
    // -td  = target directory
    // -tts = seed for RNG
    chisel3.iotesters.Driver.execute(
      Array(
        // "-ll", "Info",
        "-tn", "bgemmTM",
        "-tbn", "verilator",
        "-td", "test_run_dir/bgemmTM",
        "-tts", "0001"),
      () => new bgemmMainTM(Tile = 1)) {
      c => new bgemmTest01(c)(inAddrVec, inDataVec, outAddrVec, outDataVec)
    } should be(true)
  }
}

