package dandelion.generator


import chisel3._
import chisel3.Module
import org.scalatest.{FlatSpec, Matchers}
import dandelion.concurrent.{TaskController,TaskControllerIO}
import dandelion.config._
import dandelion.memory._
import dandelion.accel._
import dandelion.interfaces.NastiMemSlave
import helpers._


class conv2DSerialSerialMainDirect(NumTiles: Int = 1)(implicit p: Parameters)
  extends AccelIO(List(32, 32, 32, 32), List(32))(p) {

  val cache = Module(new Cache) // Simple Nasti Cache
  val memModel = Module(new NastiMemSlave) // Model of DRAM to connect to Cache

  // Connect the wrapper I/O to the memory model initialization interface so the
  // test bench can write contents at start.
  memModel.io.nasti <> cache.io.nasti
  memModel.io.init.bits.addr := 0.U
  memModel.io.init.bits.data := 0.U
  memModel.io.init.valid := false.B
  cache.io.cpu.abort := false.B


  val testDF = Module(new conv2DSerialSerialDF())


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

class conv2DSerialSerialTest01[T <: AccelIO](c: T, tiles: Int)
                                      (inAddrVec: List[Int], inDataVec: List[Int],
                                       outAddrVec: List[Int], outDataVec: List[Int])
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
  poke(c.io.in.bits.data("field3").data, 0)
  poke(c.io.in.bits.data("field3").taskID, 0)
  poke(c.io.in.bits.data("field3").predicate, false)
  poke(c.io.out.ready, false)
  step(1)
  poke(c.io.in.bits.enable.control, true)
  poke(c.io.in.valid, true)
  poke(c.io.in.bits.data("field0").data, 0) // Array a[] base address
  poke(c.io.in.bits.data("field0").predicate, true)
  poke(c.io.in.bits.data("field1").data, 1024) // Array b[] base address
  poke(c.io.in.bits.data("field1").predicate, true)
  poke(c.io.in.bits.data("field2").data, 800) // scale value
  poke(c.io.in.bits.data("field2").predicate, true)
  poke(c.io.in.bits.data("field3").data, 100)
  poke(c.io.in.bits.data("field3").predicate, false)
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
  poke(c.io.in.bits.data("field3").data, 0.U)
  poke(c.io.in.bits.data("field3").predicate, false)

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
      peek(c.io.out.bits.data("field0").predicate) == 1) {
      result = true
      val data = peek(c.io.out.bits.data("field0").data)
      if (data != 1) {
        println(Console.RED + s"*** Incorrect result received. Got $data. Hoping for 1" + Console.RESET)
        fail
      } else {
        println(Console.BLUE + s"*** Return received for t=$tiles. Run time: $time cycles." + Console.RESET)
      }
    }
  }


  checkMemory()

  if (!result) {
    println(Console.RED + "*** Timeout." + Console.RESET)
    fail
  }
}


class conv2DSerialSerialTester1 extends FlatSpec with Matchers {

  val inDataVec = List(0, 0, 9, 221, 178, 152, 80, 98, 163, 40, 239, 169, 89, 179, 98, 69, 117, 196, 16, 44, 12, 59,
    23, 14, 68, 13, 57, 137, 175, 195, 165, 68, 199, 71, 34, 0, 249, 139, 118, 157, 76, 254, 71, 190, 179, 66, 157, 193, 7,
    198, 0, 44, 2, 182, 236, 247, 221, 190, 129, 141, 131, 192, 106, 227, 8, 166, 246, 154, 202, 0, 56, 24, 0, 25,
    111, 57, 0, 13, 0, 21, 108, 154, 242, 7, 82, 95, 0, 200, 31, 26, 238, 59, 115, 89, 183, 21, 152, 174, 200, 100)
  val inAddrVec = List.range(0, 4 * inDataVec.length, 4)

  val outAddrVec = List.range(1024, 1024 + (4 * inDataVec.length), 4)
  val outDataVec = List(0, 0, 28, 255, 255, 255, 250, 255, 255, 125, 255, 255, 255, 255, 255, 215, 255, 255, 50,
    137, 37, 184, 71, 43, 212, 40, 178, 255, 255, 255, 255, 212, 255, 221, 106, 0, 255, 255, 255, 255, 237, 255, 221,
    255, 255, 206, 255, 255, 21, 255, 0, 137, 6, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 25, 255, 255,
    255, 255, 0, 175, 75, 0, 78, 255, 178, 0, 40, 0, 65, 255, 255, 255, 21, 255, 255, 0, 255, 96, 81, 255, 184, 255, 255,
    255, 65, 255, 255, 255, 255)


  implicit val p = Parameters.root((new MiniConfig).toInstance)
  val testParams = p.alterPartial({
    case TLEN => 6
    case TRACE => true
  })
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
      () => new conv2DSerialSerialMainDirect(1)(testParams)) {
      c => new conv2DSerialSerialTest01(c, 1)(inAddrVec, inDataVec, outAddrVec, outDataVec)
    } should be(true)
  }
}
