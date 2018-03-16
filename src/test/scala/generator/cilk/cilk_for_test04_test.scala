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

/*
class cilk_for_test04CacheWrapper()(implicit p: Parameters) extends cilk_for_test04DF()(p)
  with CacheParams {
/*  val io2 = IO(new Bundle {
    val init  = Flipped(Valid(new InitParams()(p)))
  })
  */
  // Instantiate the AXI Cache
  val cache = Module(new Cache)
  cache.io.cpu.req <> CacheMem.io.CacheReq
  CacheMem.io.CacheResp <> cache.io.cpu.resp
  cache.io.cpu.abort := false.B
  // Instantiate a memory model with AXI slave interface for cache
  val memModel = Module(new NastiMemSlave)
  memModel.io.nasti <> cache.io.nasti
  //memModel.io.init <> io2.init

  val raminit = RegInit(true.B)
  val addrVec = VecInit(0.U,1.U,1.U,3.U,4.U,
                        8.U,9.U,10.U,11.U,12.U)
  val dataVec = VecInit(1.U,2.U,3.U,4.U,5.U,
                        1.U,2.U,3.U,4.U,5.U)
  val (count_out, count_done) = Counter(raminit, addrVec.length)
  memModel.io.init.bits.addr := addrVec(count_out)
  memModel.io.init.bits.data := dataVec(count_out)
  when (!count_done) {
    memModel.io.init.valid := true.B
  }.otherwise {
    memModel.io.init.valid := false.B
    raminit := false.B
  }

}
*/

class cilk_for_test04MainIO(implicit p: Parameters) extends CoreBundle()(p) with CacheParams {
    val in = Flipped(Decoupled(new Call(List(32,32,32))))
    val addr = Input(UInt(nastiXAddrBits.W))
    val din  = Input(UInt(nastiXDataBits.W))
    val write = Input(Bool())
    val dout = Output(UInt(nastiXDataBits.W))
    val out = Decoupled(new Call(List(32)))
}

class cilk_for_test04MainDirect(implicit val p: Parameters) extends Module with CoreParams with CacheParams {
  val io = IO(new cilk_for_test04MainIO)
  val cache = Module(new Cache)
  val memModel = Module(new NastiMemSlave)
  val memCopy = Mem(1024, UInt(32.W))

  when(cache.io.cpu.req.valid && cache.io.cpu.req.bits.iswrite) {
    memCopy.write((cache.io.cpu.req.bits.addr>>2).asUInt(), cache.io.cpu.req.bits.data)
  }
  io.dout := memCopy.read((io.addr>>2).asUInt())

  memModel.io.nasti <> cache.io.nasti
  memModel.io.init.bits.addr := io.addr
  memModel.io.init.bits.data := io.din
  memModel.io.init.valid := io.write
  cache.io.cpu.abort := false.B

  val cilk_for_test04_detach = Module(new cilk_for_test04_detachDF())
  val cilk_for_test04 = Module(new cilk_for_test04DF())

  cache.io.cpu.req <> cilk_for_test04_detach.io.CacheReq
  cilk_for_test04_detach.io.CacheResp <> cache.io.cpu.resp
  cilk_for_test04.io.in <> io.in
  cilk_for_test04_detach.io.in <> cilk_for_test04.io.call9_out
  cilk_for_test04.io.call9_in <> cilk_for_test04_detach.io.out
  io.out <> cilk_for_test04.io.out

}
/*
class cilk_for_test04MainTM(implicit p: Parameters) extends cilk_for_test04MainIO()(p) {

  val cache = Module(new Cache)
  cache.io.cpu.abort := false.B
  // Instantiate a memory model with AXI slave interface for cache
  val memModel = Module(new NastiMemSlave)
  memModel.io.nasti <> cache.io.nasti
  //memModel.io.init <> io2.init

  val raminit = RegInit(true.B)
  val addrVec = VecInit(0.U,1.U,1.U,3.U,4.U,
    8.U,9.U,10.U,11.U,12.U)
  val dataVec = VecInit(1.U,2.U,3.U,4.U,5.U,
    1.U,2.U,3.U,4.U,5.U)
  val (count_out, count_done) = Counter(raminit, addrVec.length)
  memModel.io.init.bits.addr := addrVec(count_out)
  memModel.io.init.bits.data := dataVec(count_out)
  when (!count_done) {
    memModel.io.init.valid := true.B
  }.otherwise {
    memModel.io.init.valid := false.B
    raminit := false.B
  }



  val children = 2
  val TaskControllerModule = Module(new TaskController(List(32,32), List(32), 1, children))
  val cilk_for_test04 = Module(new cilk_for_test04DF())

  val cilk_for_test04_detach = for (i <- 0 until children) yield {
    val foo = Module(new cilk_for_test04_detachDF())
    foo
  }

  // tester to cilk_for_test02
  cilk_for_test04.io.in <> io.in

  // cilk_for_test02 to task controller
  TaskControllerModule.io.parentIn(0) <> cilk_for_test04.io.call9_out

  // task controller to sub-task cilk_for_test04_detach
  for (i <- 0 until children ) {
    cilk_for_test04_detach(i).io.in <> TaskControllerModule.io.childOut(i)
    TaskControllerModule.io.childIn(i) <> cilk_for_test04_detach(i).io.out
  }

  // Task controller to cilk_for_test02
  cilk_for_test04.io.call9_in <> TaskControllerModule.io.parentOut(0)

  // cilk_for_test02 to tester
  io.out <> cilk_for_test04.io.out

}
*/
class cilk_for_test04Test01[T <: cilk_for_test04MainDirect](c: T) extends PeekPokeTester(c) {


  val inAddrVec = List("h0".U,"h4".U,"h8".U,"hC".U,"h10".U,
    "h20".U,"h24".U,"h28".U,"h2C".U,"h30".U)
  val inDataVec = List(1.U,2.U,3.U,4.U,5.U,
    1.U,2.U,3.U,4.U,5.U)
  val outAddrVec = List("h40".U,"h44".U,"h48".U,"h4C".U,"h50".U)
  val outDataVec = List(2.U,4.U,6.U,8.U,10.U)

  // Intialize memory model
  poke(c.io.addr, 0.U)
  poke(c.io.din, 0.U)
  poke(c.io.write, false.B)
  var i = 0

  for(i <- 0 until 10) {
    poke(c.io.addr, inAddrVec(i))
    poke(c.io.din, inDataVec(i))
    poke(c.io.write, true.B)
    step(1)
  }
  poke(c.io.write, false.B)
  step(1)

  // Initializing the signals
  poke(c.io.in.bits.enable.control, false.B)
  poke(c.io.in.valid, false.B)
  poke(c.io.in.bits.data("field0").data, 0.U)
  poke(c.io.in.bits.data("field0").taskID, 5.U)
  poke(c.io.in.bits.data("field0").predicate, false.B)
  poke(c.io.in.bits.data("field1").data, 0.U)
  poke(c.io.in.bits.data("field1").taskID, 5.U)
  poke(c.io.in.bits.data("field1").predicate, false.B)
  poke(c.io.in.bits.data("field2").data, 0.U)
  poke(c.io.in.bits.data("field1").taskID, 5.U)
  poke(c.io.in.bits.data("field2").predicate, false.B)
  poke(c.io.out.ready, false.B)
  step(1)
  poke(c.io.in.bits.enable.control, true.B)
  poke(c.io.in.valid, true.B)
  poke(c.io.in.bits.data("field0").data, "h0".U)    // Array a[] base address
  poke(c.io.in.bits.data("field0").predicate, true.B)
  poke(c.io.in.bits.data("field1").data, "h20".U)   // Array b[] base address
  poke(c.io.in.bits.data("field1").predicate, true.B)
  poke(c.io.in.bits.data("field2").data, "h40".U)   // Array c[] base address
  poke(c.io.in.bits.data("field2").predicate, true.B)
  poke(c.io.out.ready, true.B)
  step(1)
  poke(c.io.in.bits.enable.control, false.B)
  poke(c.io.in.valid, false.B)
  poke(c.io.in.bits.data("field0").data, 0)
  poke(c.io.in.bits.data("field0").predicate, false.B)
  poke(c.io.in.bits.data("field1").data, 0.U)
  poke(c.io.in.bits.data("field1").predicate, false.B)
  poke(c.io.in.bits.data("field2").data, 0.U)
  poke(c.io.in.bits.data("field2").predicate, false.B)

  step(1)

  // NOTE: Don't use assert().  It seems to terminate the writing of VCD files
  // early (before the error) which makes debugging very difficult. Check results
  // using if() and fail command.
  var time = 0
  var result = false
  while (time < 200) {
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
        println(Console.BLUE + s"*** Correct return result received @ cycle: $time." + Console.RESET)
      }
    }
  }

  var valid_data = true
  for(i <- 0 until 5) {
    poke(c.io.addr, outAddrVec(i))
    step(1)
    val data = peek(c.io.dout)
    if (data != outDataVec(i).toInt) {
      println(Console.RED + s"*** Incorrect data received. Got $data. Hoping for ${outDataVec(i).toInt}" + Console.RESET)
      fail
      valid_data = false
    }
  }
  if (valid_data) {
    println(Console.BLUE + s"*** Correct data written back." + Console.RESET)
  }


  if(!result) {
    println(Console.RED + "*** Timeout." + Console.RESET)
    fail
  }
}

class cilk_for_test04Tester1 extends FlatSpec with Matchers {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  it should "Check that cilk_for_test04 works correctly." in {
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
     () => new cilk_for_test04MainDirect()) {
     c => new cilk_for_test04Test01(c)
    } should be(true)
  }
}
/*
class cilk_for_test04Tester2 extends FlatSpec with Matchers {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  // iotester flags:
  // -ll  = log level <Error|Warn|Info|Debug|Trace>
  // -tbn = backend <firrtl|verilator|vcs>
  // -td  = target directory
  // -tts = seed for RNG
  it should "Check that cilk_for_test02 works when called via task manager." in {
    chisel3.iotesters.Driver.execute(
      Array(
        // "-ll", "Info",
        "-tbn", "verilator",
        "-td", "test_run_dir",
        "-tts", "0001"),
      () => new cilk_for_test04MainTM()) {
      c => new cilk_for_test04Test01(c)
    } should be(true)
  }
}
*/