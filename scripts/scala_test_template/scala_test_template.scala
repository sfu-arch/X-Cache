package dataflow

import java.io.PrintWriter
import java.io.File
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


class {{module_name}}MainIO(implicit val p: Parameters) extends Module with CoreParams with CacheParams {
  val io = IO(new CoreBundle {
    val in = Flipped(Decoupled(new Call(List({% for n in in_args -%} 
    {{32}} {{ "," if not loop.last}}
    {%- endfor %}))))
    val req = Flipped(Decoupled(new MemReq))
    val resp = Output(Valid(new MemResp))
    val out = Decoupled(new Call(List({% for n in return_args -%} {{n}} {{ "," if not loop.last }}
    {%- endfor %})))
  })
}

class {{module_name}}Main(implicit p: Parameters) extends {{module_name}}MainIO {

  val cache = Module(new Cache) // Simple Nasti Cache
  val memModel = Module(new NastiCMemSlave) // Model of DRAM to connect to Cache

  // Connect the wrapper I/O to the memory model initialization interface so the
  // test bench can write contents at start.
  memModel.io.nasti <> cache.io.nasti
  memModel.io.init.bits.addr := 0.U
  memModel.io.init.bits.data := 0.U
  memModel.io.init.valid := false.B
  cache.io.cpu.abort := false.B

  // Wire up the cache and modules under test.
  val {{module_name}}= Module(new {{module_name}}DF())

  //Put an arbiter infront of cache
  val CacheArbiter = Module(new MemArbiter(2))

  // Connect input signals to cache
  CacheArbiter.io.cpu.MemReq(0) <> {{module_name}}.io.MemReq
  {{module_name}}.io.MemResp <> CacheArbiter.io.cpu.MemResp(0)

  //Connect main module to cache arbiter
  CacheArbiter.io.cpu.MemReq(1) <> io.req
  io.resp <> CacheArbiter.io.cpu.MemResp(1)

  //Connect cache to the arbiter
  cache.io.cpu.req <> CacheArbiter.io.cache.MemReq
  CacheArbiter.io.cache.MemResp <> cache.io.cpu.resp

  //Connect in/out ports
  {{module_name}}.io.in <> io.in
  io.out <> {{module_name}}.io.out

  // Check if trace option is on or off
  if (p(TRACE) == false) {
    println(Console.RED + "****** Trace option is off. *********" + Console.RESET)
  }
  else
    println(Console.BLUE + "****** Trace option is on. *********" + Console.RESET)


}


class {{module_name}}Test01[T <: {{module_name}}MainIO](c: T) extends PeekPokeTester(c) {

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

  def dumpMemory(path: String) = {
    //Writing mem states back to the file
    val pw = new PrintWriter(new File(path))
    for (i <- 0 until outDataVec.length) {
      val data = MemRead(outAddrVec(i))
      pw.write("0X" + outAddrVec(i).toHexString + " -> " + data.toInt.toHexString + "\n")
      //pw.write(data.toInt.toHexString + "\n")
    }
    pw.close

  }


  // Initializing the signals
  poke(c.io.in.bits.enable.control, false.B)
  poke(c.io.in.valid, false.B)
  {% for n in range(0,in_args|length) %}
  poke(c.io.in.bits.data("field{{n}}").data, 0.U)
  poke(c.io.in.bits.data("field{{n}}").taskID, 0.U)
  poke(c.io.in.bits.data("field{{n}}").predicate, false.B)
  {% endfor %}
  poke(c.io.out.ready, false.B)
  step(1)

  poke(c.io.in.bits.enable.control, true.B)
  poke(c.io.in.valid, true.B)
  {% for n in range(0,in_args|length) %}
  poke(c.io.in.bits.data("field{{n}}").data, {{in_args[n]}}.U)
  poke(c.io.in.bits.data("field{{n}}").predicate, false.B)
  {% endfor %}
  poke(c.io.out.ready, true.B)

  step(1)

  poke(c.io.in.bits.enable.control, false.B)
  poke(c.io.in.valid, false.B)
  {% for n in range(0,in_args|length) %}
  poke(c.io.in.bits.data("field{{n}}").data, 0.U)
  poke(c.io.in.bits.data("field{{n}}").predicate, false.B)
  {% endfor %}

  step(1)

  // NOTE: Don't use assert().  It seems to terminate the writing of VCD files
  // early (before the error) which makes debugging very difficult. Check results
  // using if() and fail command.
  var time = 0
  var result = false
  while (time < 9000) {
    time += 1
    step(1)
    if (peek(c.io.out.valid) == 1 &&
      peek(c.io.out.bits.data("field0").predicate) == 1
    ) {
      result = true
      val data = peek(c.io.out.bits.data("field0").data)
      if (data != 14) {
        println(Console.RED + s"*** Incorrect result received. Got $data. Hoping for 15" + Console.RESET)
        fail
      } else {
        println(Console.BLUE + s"*** Correct return result received. Run time: $time cycles." + Console.RESET)
      }
    }
  }

  step(1000)

  //  Peek into the CopyMem to see
  //  if the expected data is written back to the Cache
  var valid_data = true
  for (i <- 0 until outAddrVec.length) {
    val data = MemRead(outAddrVec(i))
    if (data != outDataVec(i).toInt) {
      println(Console.RED + s"*** Incorrect data received addr=${outAddrVec(i)}. Got $data. Hoping for ${outDataVec(i).toInt}" + Console.RESET)
      fail
      valid_data = false
    }
    else {
      println(Console.BLUE + s"[LOG] MEM[${outAddrVec(i).toInt}] :: $data" + Console.RESET)
    }
  }
  if (valid_data) {
    println(Console.BLUE + "*** Correct data written back." + Console.RESET)
    dumpMemory("memory.txt")
  }


  if (!result) {
    println(Console.RED + "*** Timeout." + Console.RESET)
    dumpMemory("memory.txt")
    fail
  }
}

class {{module_name}}Tester1 extends FlatSpec with Matchers {
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  it should "Check that {{module_name}} works correctly." in {
    // iotester flags:
    // -ll  = log level <Error|Warn|Info|Debug|Trace>
    // -tbn = backend <firrtl|verilator|vcs>
    // -td  = target directory
    // -tts = seed for RNG
    chisel3.iotesters.Driver.execute(
      Array(
        // "-ll", "Info",
        "-tn", "{{module_name}}Main",
        "-tbn", "verilator",
        "-td", "test_run_dir/{{module_name}}/",
        "-tts", "0001"),
      () => new {{module_name}}Main()) {
      c => new {{module_name}}Test01(c)
    } should be(true)
  }
}
