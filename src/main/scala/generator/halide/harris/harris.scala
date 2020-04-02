package dandelion.generator

import chisel3._
import chisel3.Module
import chipsalliance.rocketchip.config._
import dandelion.config._
import util._
import dandelion.interfaces._
import dandelion.memory._
import dandelion.accel._
import dandelion.memory.cache.HasCacheAccelParams

class harrisMainIO(implicit val p: Parameters) extends Module with HasAccelParams with HasAccelShellParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32))))

    val MemResp = Flipped(Valid(new MemResp))
    val MemReq = Decoupled(new MemReq)

    val out = Decoupled(new Call(List()))
  })

  def cloneType = new harrisMainIO().asInstanceOf[this.type]
}

class harrisMain(implicit p: Parameters) extends harrisMainIO {


  // Wire up the cache and modules under test.
  val harris_f1 = Module(new extract_function_harris_f1DF())
  val harris_f2 = Module(new extract_function_harris_f2DF())
  val harris_f3 = Module(new extract_function_harris_f3DF())

  val max_f1 = Module(new maxDF())
  val max_f2 = Module(new maxDF())
  val max_f22 = Module(new maxDF())
  val max_f3 = Module(new maxDF())

  val min_f1 = Module(new minDF())
  val min_f2 = Module(new minDF())
  val min_f22 = Module(new minDF())
  val min_f3 = Module(new minDF())

  //Put an arbiter infront of cache
  val CacheArbiter = Module(new MemArbiter(11))

  // Connect input signals to cache
  CacheArbiter.io.cpu.MemReq(0) <> harris_f1.io.MemReq
  harris_f1.io.MemResp <> CacheArbiter.io.cpu.MemResp(0)

  CacheArbiter.io.cpu.MemReq(1) <> harris_f2.io.MemReq
  harris_f2.io.MemResp <> CacheArbiter.io.cpu.MemResp(1)

  CacheArbiter.io.cpu.MemReq(2) <> harris_f3.io.MemReq
  harris_f3.io.MemResp <> CacheArbiter.io.cpu.MemResp(2)

  CacheArbiter.io.cpu.MemReq(3) <> max_f1.io.MemReq
  max_f1.io.MemResp <> CacheArbiter.io.cpu.MemResp(3)

  CacheArbiter.io.cpu.MemReq(4) <> max_f2.io.MemReq
  max_f2.io.MemResp <> CacheArbiter.io.cpu.MemResp(4)

  CacheArbiter.io.cpu.MemReq(5) <> max_f22.io.MemReq
  max_f22.io.MemResp <> CacheArbiter.io.cpu.MemResp(5)

  CacheArbiter.io.cpu.MemReq(6) <> max_f3.io.MemReq
  max_f3.io.MemResp <> CacheArbiter.io.cpu.MemResp(6)

  CacheArbiter.io.cpu.MemReq(7) <> min_f1.io.MemReq
  min_f1.io.MemResp <> CacheArbiter.io.cpu.MemResp(7)

  CacheArbiter.io.cpu.MemReq(8) <> min_f2.io.MemReq
  min_f2.io.MemResp <> CacheArbiter.io.cpu.MemResp(8)

  CacheArbiter.io.cpu.MemReq(9) <> min_f22.io.MemReq
  min_f22.io.MemResp <> CacheArbiter.io.cpu.MemResp(9)

  CacheArbiter.io.cpu.MemReq(10) <> min_f3.io.MemReq
  min_f3.io.MemResp <> CacheArbiter.io.cpu.MemResp(10)

  //Connect main module to cache arbiter
  //  CacheArbiter.io.cpu.MemReq(3) <> io.req
  //  io.resp <> CacheArbiter.io.cpu.MemResp(3)

  //Connect cache to the arbiter

  io.MemReq <> CacheArbiter.io.cache.MemReq
  CacheArbiter.io.cache.MemResp <> io.MemResp

  min_f1.io.in <> harris_f1.io.call_73_out
  harris_f1.io.call_73_in <> min_f1.io.out

  max_f1.io.in <> harris_f1.io.call_76_out
  harris_f1.io.call_76_in <> max_f1.io.out

  min_f2.io.in <> harris_f2.io.call_78_out
  harris_f2.io.call_78_in <> min_f2.io.out

  max_f2.io.in <> harris_f2.io.call_81_out
  harris_f2.io.call_81_in <> max_f2.io.out

  min_f22.io.in <> harris_f2.io.call_100_out
  harris_f2.io.call_100_in <> min_f22.io.out

  max_f22.io.in <> harris_f2.io.call_102_out
  harris_f2.io.call_102_in <> max_f22.io.out

  min_f3.io.in <> harris_f3.io.call_73_out
  harris_f3.io.call_73_in <> min_f3.io.out

  max_f3.io.in <> harris_f3.io.call_76_out
  harris_f3.io.call_76_in <> max_f3.io.out


  //Connect in/out ports
  harris_f1.io.in <> io.in
  harris_f2.io.in <> harris_f1.io.call_f2_out
  harris_f1.io.call_f2_in <> harris_f2.io.out
  harris_f3.io.in <> harris_f2.io.call_f3_out
  harris_f2.io.call_f3_in <> harris_f3.io.out
  io.out <> harris_f1.io.out

}


import java.io.{File, FileWriter}

object harrisTop extends App {
  val dir = new File("RTL/harrisTop");
  dir.mkdirs
  implicit val p = new WithAccelConfig
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new harrisMain()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}
