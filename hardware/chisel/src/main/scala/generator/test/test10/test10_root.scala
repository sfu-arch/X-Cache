package dandelion.generator

import chipsalliance.rocketchip.config._
import chisel3._
import dandelion.accel._
import dandelion.memory._

class test10RootDF(PtrsIn: Seq[Int] = List(64, 64, 64), ValsIn: Seq[Int] = List(), Returns: Seq[Int] = List())
                  (implicit p: Parameters) extends DandelionAccelDCRModule(PtrsIn, ValsIn, Returns) {

  val NumKernels = 2
  val memory_arbiter = Module(new MemArbiter(NumKernels))

  val test10_add = Module(new test10_addDF(List(), List(64, 64), List(64)))
  val test10 = Module(new test10DF(PtrsIn, ValsIn, Returns))

  test10.io.in <> io.in
  io.out <> test10.io.out

  test10_add.io.in <> test10.call_7_out_io
  test10.call_7_in_io <> test10_add.io.out

  memory_arbiter.io.cpu.MemReq(0) <> test10.io.MemReq
  test10.io.MemResp <> memory_arbiter.io.cpu.MemResp(0)

  memory_arbiter.io.cpu.MemReq(1) <> test10_add.io.MemReq
  test10_add.io.MemResp <> memory_arbiter.io.cpu.MemResp(1)

  io.MemReq <> memory_arbiter.io.cache.MemReq
  memory_arbiter.io.cache.MemResp <> io.MemResp
}

