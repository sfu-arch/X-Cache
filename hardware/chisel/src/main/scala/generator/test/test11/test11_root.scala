package dandelion.generator 

import chipsalliance.rocketchip.config._
import chisel3._
import dandelion.accel._
import dandelion.memory._

class test11RootDF(PtrsIn: Seq[Int] = List (), ValsIn : Seq[Int] = List(64), Returns: Seq[Int] = List(64))
                  (implicit p: Parameters) extends DandelionAccelDCRModule(PtrsIn, ValsIn, Returns) {

  val NumKernels = 2
  val memory_arbiter = Module(new MemArbiter(NumKernels))

  val test11_inner= Module(new test11_innerDF(PtrsIn = List(), ValsIn = List(64), Returns = List(64)))
  val test11= Module(new test11DF(PtrsIn = List(), ValsIn = List(64), Returns = List(64)))

  test11.io.in <> io.in
  io.out <> test11.io.out

  test11_inner.io.in <> test11.call_5_out_io
  test11.call_5_in_io <> test11_inner.io.out

  memory_arbiter.io.cpu.MemReq(0) <> test11_inner.io.MemReq
  test11_inner.io.MemResp <> memory_arbiter.io.cpu.MemResp(0)

  memory_arbiter.io.cpu.MemReq(1) <> test11.io.MemReq
  test11.io.MemResp <> memory_arbiter.io.cpu.MemResp(1)

  io.MemReq <> memory_arbiter.io.cache.MemReq
  memory_arbiter.io.cache.MemResp <> io.MemResp

}
