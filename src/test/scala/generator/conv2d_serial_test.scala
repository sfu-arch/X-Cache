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


class conv2DSerialMainDirect()(implicit p: Parameters)
  extends AccelIO(List(32, 32, 32, 32, 32, 32, 32), List(32))(p) {

  val cache = Module(new ReferenceCache) // Simple Nasti Cache
  val memModel = Module(new NastiMemSlave) // Model of DRAM to connect to Cache

  // Connect the wrapper I/O to the memory model initialization interface so the
  // test bench can write contents at start.
  memModel.io.nasti <> cache.io.mem
  memModel.io.init.bits.addr := 0.U
  memModel.io.init.bits.data := 0.U
  memModel.io.init.valid := false.B
  cache.io.cpu.abort := false.B


  val testDF = Module(new conv2dSerialDF())


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

class conv2DSerialTest01[T <: AccelIO](c: T)
                                      (inAddrVec: List[Int], inDataVec: List[Int],
                                       outAddrVec: List[Int], outDataVec: List[Int],
                                       coeffsAddrVec: List[Int], coeffsDataVec: List[Int],
                                       inAddrBegin: Int, outAddrBegin: Int, coeffsAddrBegin: Int
                                      )
  extends AccelTesterLocal(c)(inAddrVec, inDataVec, outAddrVec ++ coeffsAddrVec, outDataVec ++ coeffsDataVec) {

  initMemory()

  // Initializing the signals
  poke(c.io.in.bits.enable.control, false)
  poke(c.io.in.bits.enable.taskID, 0.U)
  poke(c.io.in.valid, false)
  poke(c.io.in.bits.data("field0").data, 0.U)
  poke(c.io.in.bits.data("field0").taskID, 0.U)
  poke(c.io.in.bits.data("field0").predicate, false)
  poke(c.io.in.bits.data("field1").data, 0.U)
  poke(c.io.in.bits.data("field1").taskID, 0.U)
  poke(c.io.in.bits.data("field1").predicate, false)
  poke(c.io.in.bits.data("field2").data, 0.U)
  poke(c.io.in.bits.data("field2").taskID, 0.U)
  poke(c.io.in.bits.data("field2").predicate, false)

  poke(c.io.in.bits.data("field3").data, 0.U)
  poke(c.io.in.bits.data("field3").taskID, 0.U)
  poke(c.io.in.bits.data("field3").predicate, false)

  poke(c.io.in.bits.data("field4").data, 0.U)
  poke(c.io.in.bits.data("field4").taskID, 0.U)
  poke(c.io.in.bits.data("field4").predicate, false)

  poke(c.io.in.bits.data("field5").data, 0.U)
  poke(c.io.in.bits.data("field5").taskID, 0.U)
  poke(c.io.in.bits.data("field5").predicate, false)

  poke(c.io.in.bits.data("field6").data, 0.U)
  poke(c.io.in.bits.data("field6").taskID, 0.U)
  poke(c.io.in.bits.data("field6").predicate, false)

  poke(c.io.out.ready, false)
  step(1)
  poke(c.io.in.bits.enable.control, true)
  poke(c.io.in.valid, true)
  poke(c.io.in.bits.data("field0").data, inAddrBegin.U) // Array a[] base address
  poke(c.io.in.bits.data("field0").predicate, true)
  poke(c.io.in.bits.data("field1").data, outAddrBegin.U) // Array out[] base address
  poke(c.io.in.bits.data("field1").predicate, true)
  poke(c.io.in.bits.data("field2").data, coeffsAddrBegin.U) // Array out[] base address
  poke(c.io.in.bits.data("field2").predicate, true)

  poke(c.io.in.bits.data("field3").data, 10.U) // scale value
  poke(c.io.in.bits.data("field3").predicate, true)

  poke(c.io.in.bits.data("field4").data, 10.U)
  poke(c.io.in.bits.data("field4").predicate, false)

  poke(c.io.in.bits.data("field5").data, 5.U)
  poke(c.io.in.bits.data("field5").predicate, false)

  poke(c.io.in.bits.data("field6").data, 8.U)
  poke(c.io.in.bits.data("field6").predicate, false)

  poke(c.io.out.ready, true.B)

  step(1)
  poke(c.io.in.bits.enable.control, false.B)
  poke(c.io.in.valid, false.B)

  poke(c.io.in.bits.data("field0").data, 0.U)
  poke(c.io.in.bits.data("field0").predicate, false)
  poke(c.io.in.bits.data("field1").data, 0.U)
  poke(c.io.in.bits.data("field1").predicate, false)
  poke(c.io.in.bits.data("field2").data, 0.U)
  poke(c.io.in.bits.data("field2").predicate, false)
  poke(c.io.in.bits.data("field3").data, 0.U)
  poke(c.io.in.bits.data("field3").predicate, false)
  poke(c.io.in.bits.data("field4").data, 0.U)
  poke(c.io.in.bits.data("field4").predicate, false)
  poke(c.io.in.bits.data("field5").data, 0.U)
  poke(c.io.in.bits.data("field5").predicate, false)
  poke(c.io.in.bits.data("field6").data, 0.U)
  poke(c.io.in.bits.data("field6").predicate, false)

  step(1)

  // NOTE: Don't use assert().  It seems to terminate the writing of VCD files
  // early (before the error) which makes debugging very difficult. Check results
  // using if() and fail command.
  var time = 0
  var result = false
  while (time < 100000) {
    time += 1
    step(1)
    if (peek(c.io.out.valid) == 1 &&
      peek(c.io.out.bits.data("field0").predicate) == 1) {
      result = true
      val data = peek(c.io.out.bits.data("field0").data)
      if (data != 80) {
        println(Console.RED + s"*** Incorrect result received. Got $data. Hoping for 1" + Console.RESET)
        fail
      } else {
        println(Console.BLUE + s"*** Return received.  Run time: $time cycles." + Console.RESET)
      }
    }
  }


  //checkMemory()

  if (!result) {
    println(Console.RED + "*** Timeout." + Console.RESET)
    fail
  }
}


class conv2DSerialSerialTester1 extends FlatSpec with Matchers {

  val IMG_SIZE = 10
  val COE_SIZE = IMG_SIZE/2
  val BYTE_SIZE = 4

  val inAddrBegin = 0
  val inDataVec = List.range(inAddrBegin, IMG_SIZE * IMG_SIZE)
  val inAddrVec = List.range(inAddrBegin, BYTE_SIZE * inDataVec.length, BYTE_SIZE)

  val outDataVec = List(
    0, 0, 0,  0,  0,  0,  0,  0,  0, 0, 0, 0, 0,  0,  0,  0,  0,  0,  0, 0,
    0, 0, 22, 23, 24, 25, 26, 27, 0, 0, 0, 0, 32, 33, 34, 35, 36, 37, 0, 0,
    0, 0, 42, 43, 44, 45, 46, 47, 0, 0, 0, 0, 52, 53, 54, 55, 56, 57, 0, 0,
    0, 0, 62, 63, 64, 65, 66, 67, 0, 0, 0, 0, 72, 73, 74, 75, 76, 77, 0, 0,
    0, 0, 0,  0,  0,  0,  0,  0,  0, 0, 0, 0, 0,  0,  0,  0,  0,  0,  0, 0)
  val outAddrBegin = BYTE_SIZE * (IMG_SIZE * IMG_SIZE)
  val outAddrVec = List.range(outAddrBegin, outAddrBegin + (BYTE_SIZE * outDataVec.length), BYTE_SIZE)

  val coeffsData = List(1, 4, 6, 4, 1, 4, 16, 24, 16, 4, 6,
    24, 36, 24, 6, 4, 16, 24, 16, 4, 1, 4, 6, 4, 1)
  val coeffsAddrBegin = 2 * BYTE_SIZE * (IMG_SIZE * IMG_SIZE)
  val coeffsAddr = List.range(coeffsAddrBegin, coeffsAddrBegin + (BYTE_SIZE * coeffsData.length), BYTE_SIZE)

  implicit val p = new WithAccelConfig(DandelionAccelParams(taskLen= 6))
  // iotester flags:
  // -ll  = log level <Error|Warn|Info|Debug|Trace>
  // -tbn = backend <firrtl|verilator|vcs>
  // -td  = target directory
  // -tts = seed for RNG
  it should s"Test: direct connection" in {
    chisel3.iotesters.Driver.execute(
      Array(
        // "-ll", "Info",
        "-tn", "conv2DSerialSerial_Direct",
        "-tbn", "verilator",
        "-td", s"test_run_dir/conv2DSerialSerial_direct",
        "-tts", "0001"),
      () => new conv2DSerialMainDirect()(p)) {
      c => new conv2DSerialTest01(c)(inAddrVec, inDataVec, outAddrVec, outDataVec, coeffsAddr, coeffsData,
        inAddrBegin, outAddrBegin, coeffsAddrBegin )
    } should be(true)
  }
}
