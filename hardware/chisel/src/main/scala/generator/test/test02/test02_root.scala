package dandelion.generator 

import chipsalliance.rocketchip.config._
import chisel3._
import dandelion.accel._
import dandelion.memory._

class test02RootDF(PtrsIn : Seq[Int] = List (), ValsIn : Seq[Int] = List(64, 64), Returns: Seq[Int] = List(64))
                  (implicit p: Parameters) extends DandelionAccelDCRModule(PtrsIn, ValsIn, Returns) {

  val NumKernels = 1
  val memory_arbiter = Module(new MemArbiter(NumKernels))

  /**
    * Local memories
    */

  /**
    * Kernel Modules
    */
  val test02 =  Module(new test02DF(PtrsIn = List(), ValsIn = List(64, 64), Returns = List(64)))

  test02.io.in <> io.in
  io.out <> test02.io.out


  memory_arbiter.io.cpu.MemReq(0) <> test02.io.MemReq
  test02.io.MemResp <> memory_arbiter.io.cpu.MemResp(0)

  io.MemReq <> memory_arbiter.io.cache.MemReq
  memory_arbiter.io.cache.MemResp <> io.MemResp

}
