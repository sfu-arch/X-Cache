package dandelion.generator 

import chipsalliance.rocketchip.config._
import chisel3._
import dandelion.accel._
import dandelion.memory._

class test13RootDF(PtrsIn : Seq[Int] = List (64, 64), ValsIn : Seq[Int] = List(64), Returns: Seq[Int] = List())
                  (implicit p: Parameters) extends DandelionAccelDCRModule(PtrsIn, ValsIn, Returns) {

  val NumKernels = 1
  val memory_arbiter = Module(new MemArbiter(NumKernels))

  val test13 =  Module(new test13DF(PtrsIn = List(64, 64), ValsIn = List(64), Returns = List()))

  test13.io.in <> io.in
  io.out <> test13.io.out

  memory_arbiter.io.cpu.MemReq(0) <> test13.io.MemReq
  test13.io.MemResp <> memory_arbiter.io.cpu.MemResp(0)

  io.MemReq <> memory_arbiter.io.cache.MemReq
  memory_arbiter.io.cache.MemResp <> io.MemResp

}
