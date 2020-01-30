package dandelion.generator.cilk


import chisel3._
import chisel3.Module
import org.scalatest.{FlatSpec, Matchers}
import dandelion.concurrent.{TaskController,TaskControllerIO}
import chipsalliance.rocketchip.config._
import dandelion.config._
import dandelion.memory._
import dandelion.accel._
import dandelion.interfaces.NastiMemSlave
import helpers._


class cilk_for_test05MainTM(tiles: Int)(implicit p: Parameters)
  extends AccelIO(List(32, 32, 32), List(32))(p) {

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
  val cilk_for_test05 = Module(new cilk_for_test05DF())
  cilk_for_test05.io.MemResp <> DontCare
  cilk_for_test05.io.MemReq <> DontCare

  val cilk_for_test05_detach1 = for (i <- 0 until children) yield {
    val detach1 = Module(new cilk_for_test05_detach1DF())
    detach1.io.MemResp <> DontCare
    detach1.io.MemReq <> DontCare
    detach1
  }
  val cilk_for_test05_detach2 = for (i <- 0 until children) yield {
    val detach2 = Module(new cilk_for_test05_detach2DF)
    detach2
  }

  // Ugly hack to merge requests from two children.  "ReadWriteArbiter" merges two
  // requests ports of any type.  Read or write is irrelevant.
  val MemArbiter = Module(new MemArbiter(children + 1))
  for (i <- 0 until children) {
    MemArbiter.io.cpu.MemReq(i) <> cilk_for_test05_detach2(i).io.MemReq
    cilk_for_test05_detach2(i).io.MemResp <> MemArbiter.io.cpu.MemResp(i)
  }
  MemArbiter.io.cpu.MemReq(children) <> io.req
  io.resp <> MemArbiter.io.cpu.MemResp(children)

  cache.io.cpu.req <> MemArbiter.io.cache.MemReq
  MemArbiter.io.cache.MemResp <> cache.io.cpu.resp

  // tester to cilk_for_test02
  cilk_for_test05.io.in <> io.in

  // cilk_for_test02 to task controller
  TaskControllerModule.io.parentIn(0) <> cilk_for_test05.io.call_8_out

  // task controller to sub-task cilk_for_test05_detach
  for (i <- 0 until children) {
    cilk_for_test05_detach1(i).io.in <> TaskControllerModule.io.childOut(i)
    cilk_for_test05_detach2(i).io.in <> cilk_for_test05_detach1(i).io.call_8_out
    cilk_for_test05_detach1(i).io.call_8_in <> cilk_for_test05_detach2(i).io.out
    TaskControllerModule.io.childIn(i) <> cilk_for_test05_detach1(i).io.out
  }

  // Task controller to cilk_for_test02
  cilk_for_test05.io.call_8_in <> TaskControllerModule.io.parentOut(0)

  // cilk_for_test02 to tester
  io.out <> cilk_for_test05.io.out

}

class cilk_for_test05Test01[T <: AccelIO](c: T, tiles: Int)
                                         (inAddrVec: List[Int], inDataVec: List[Int],
                                          outAddrVec: List[Int], outDataVec: List[Int])
  extends AccelTesterLocal(c)(inAddrVec, inDataVec, outAddrVec, outDataVec) {

  // Write initial contents to the memory model.
  initMemory()


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

  checkMemory()

  if (!result) {
    println(Console.RED + "*** Timeout." + Console.RESET)
    fail
  }
}


class cilk_for_test05Tester1 extends FlatSpec with Matchers {

  val inAddrVec = List.range(0, (4 * 50), 4) // byte addresses
  val inA = List.range(1, 6) ::: List.range(11, 16) ::: List.range(21, 26) ::: List.range(31, 36) ::: List.range(41, 46) // 5x5 array of uint32
  val inB = List.range(1, 6) ::: List.range(11, 16) ::: List.range(21, 26) ::: List.range(31, 36) ::: List.range(41, 46) // 5x5 array of uint32
  val inDataVec = inA ++ inB
  val outAddrVec = List.range(256, 256 + 100, 4)
  val outDataVec = inA.zip(inB).map { case (x, y) => x + y }


  implicit val p = new WithAccelConfig
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
          "-ll", "Error",
          "-tbn", "verilator",
          "-td", s"test_run_dir/cilk_for_test05_${tile}",
          "-tts", "0001"),
        () => new cilk_for_test05MainTM(tile)(p)) {
        c => new cilk_for_test05Test01(c, tile)(inAddrVec, inDataVec, outAddrVec, outDataVec)
      } should be(true)
    }
  }
}
