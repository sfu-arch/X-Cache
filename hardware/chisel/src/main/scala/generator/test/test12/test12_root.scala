package dandelion.generator 

import chipsalliance.rocketchip.config._
import chisel3._
import dandelion.accel._
import dandelion.memory._

class test12RootDF(PtrsIn : Seq[Int] = List (), ValsIn : Seq[Int] = List(64, 64, 64), Returns: Seq[Int] = List(64))
                  (implicit p: Parameters) extends DandelionAccelDCRModule(PtrsIn, ValsIn, Returns) {

  val NumKernels = 1
  val memory_arbiter = Module(new MemArbiter(NumKernels))

  val test12 =  Module(new test12DF(PtrsIn = List(), ValsIn = List(64, 64, 64), Returns = List(64)))

  test12.io.in <> io.in
  io.out <> test12.io.out

  memory_arbiter.io.cpu.MemReq(0) <> test12.io.MemReq
  test12.io.MemResp <> memory_arbiter.io.cpu.MemResp(0)

  io.MemReq <> memory_arbiter.io.cache.MemReq
  memory_arbiter.io.cache.MemResp <> io.MemResp

}
