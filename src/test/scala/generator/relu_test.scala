package dandelion.generator


import chisel3._
import chisel3.Module
import org.scalatest.{FlatSpec, Matchers}
import dandelion.concurrent.{TaskController, TaskControllerIO}
import chipsalliance.rocketchip.config._
import dandelion.config._
import dandelion.memory._
import dandelion.accel._
import dandelion.interfaces.NastiMemSlave
import dandelion.memory.cache.ReferenceCache
import helpers._


class reluMainDirect()(implicit p: Parameters)
  extends AccelIO(List(32, 32, 32), List())(p) {

  val cache = Module(new ReferenceCache) // Simple Nasti Cache
  val memModel = Module(new NastiMemSlave) // Model of DRAM to connect to Cache

  // Connect the wrapper I/O to the memory model initialization interface so the
  // test bench can write contents at start.
  memModel.io.nasti <> cache.io.mem
  memModel.io.init.bits.addr := 0.U
  memModel.io.init.bits.data := 0.U
  memModel.io.init.valid := false.B
  cache.io.cpu.abort := false.B


  val testDF = Module(new reluDF())


  val CacheArb = Module(new MemArbiter(2))

  // Merge the memory interfaces and connect to the stack memory
  // Connect to stack memory interface
  CacheArb.io.cpu.MemReq(0) <> testDF.io.MemReq
  testDF.io.MemResp <> CacheArb.io.cpu.MemResp(0)

  CacheArb.io.cpu.MemReq(1) <> io.req
  io.resp <> CacheArb.io.cpu.MemResp(1)

  cache.io.cpu.req <> CacheArb.io.cache.MemReq
  CacheArb.io.cache.MemResp <> cache.io.cpu.resp

  testDF.io.in <> io.in
  io.out <> testDF.io.out


}

class reluTest01[T <: AccelIO](c: T)
                                    (inAddrVec: List[Int], inDataVec: List[Int],
                                     outAddrVec: List[Int], outDataVec: List[Int],
                                     inAddrBegin: Int, outAddrBegin: Int)
  extends AccelTesterLocal(c)(inAddrVec, inDataVec, outAddrVec, outDataVec) {

  initMemory()

  // Initializing the signals
  poke(c.io.in.bits.enable.control, false)
  poke(c.io.in.bits.enable.taskID, 0)
  poke(c.io.in.valid, false)
  poke(c.io.in.bits.data("field0").data, 0)
  poke(c.io.in.bits.data("field0").taskID, 0)
  poke(c.io.in.bits.data("field0").predicate, false)
  poke(c.io.in.bits.data("field1").data, 0)
  poke(c.io.in.bits.data("field1").taskID, 0)
  poke(c.io.in.bits.data("field1").predicate, false)
  poke(c.io.in.bits.data("field2").data, 0)
  poke(c.io.in.bits.data("field2").taskID, 0)
  poke(c.io.in.bits.data("field2").predicate, false)
  poke(c.io.out.ready, false)
  step(1)
  poke(c.io.in.bits.enable.control, true)
  poke(c.io.in.valid, true)
  poke(c.io.in.bits.data("field0").data, inAddrBegin) // Array a[] base address
  poke(c.io.in.bits.data("field0").predicate, true)
  poke(c.io.in.bits.data("field1").data, outAddrBegin) // Array b[] base address
  poke(c.io.in.bits.data("field1").predicate, true)
  poke(c.io.in.bits.data("field2").data, 8.U) // scale value
  poke(c.io.in.bits.data("field2").predicate, true)
  poke(c.io.out.ready, true.B)
  step(1)
  poke(c.io.in.bits.enable.control, false.B)
  poke(c.io.in.valid, false.B)
  poke(c.io.in.bits.data("field0").data, 0)
  poke(c.io.in.bits.data("field0").predicate, false)
  poke(c.io.in.bits.data("field1").data, 0.U)
  poke(c.io.in.bits.data("field1").predicate, false)
  poke(c.io.in.bits.data("field2").data, 0.U)
  poke(c.io.in.bits.data("field2").predicate, false)

  step(1)

  // NOTE: Don't use assert().  It seems to terminate the writing of VCD files
  // early (before the error) which makes debugging very difficult. Check results
  // using if() and fail command.
  var time = 0
  var result = false
  while (time < 10000) {
    time += 1
    step(1)
    if (peek(c.io.out.valid) == 1) {
      result = true
      println(Console.BLUE + s"*** Return received. Run time: $time cycles." + Console.RESET)
    }
  }


  checkMemory()

  if (!result) {
    println(Console.RED + "*** Timeout." + Console.RESET)
    fail
  }
}


class reluTester1 extends FlatSpec with Matchers {

  val TEST_SIZE = 64
  val BYTE_SIZE = 4

  val inAddrBegin = 0
  val inDataVec = List.range(0, TEST_SIZE).map( (x) => if( x < TEST_SIZE / 2)  -x else x )
  val inAddrVec = List.range(inAddrBegin, BYTE_SIZE * inDataVec.length, BYTE_SIZE)

  val outAddrBegin = inAddrBegin + (BYTE_SIZE * inDataVec.length)
  val outAddrVec = List.range(outAddrBegin, outAddrBegin + (BYTE_SIZE * inDataVec.length), BYTE_SIZE)
  val outDataVec = inDataVec.map((x) => if(x < 0) 0 else x)


  implicit val p = new WithAccelConfig ++ new WithTestConfig
  // iotester flags:
  // -ll  = log level <Error|Warn|Info|Debug|Trace>
  // -tbn = backend <firrtl|verilator|vcs>
  // -td  = target directory
  // -tts = seed for RNG
  it should s"Test: direct connection" in {
    chisel3.iotesters.Driver.execute(
      Array(
        // "-ll", "Info",
        "-tn", "relu_Direct",
        "-tbn", "verilator",
        "-td", s"test_run_dir/relu_direct",
        "-tts", "0001"),
      () => new reluMainDirect()(p)) {
      c => new reluTest01(c)(inAddrVec, inDataVec, outAddrVec, outDataVec, inAddrBegin, outAddrBegin)
    } should be(true)
  }
}
