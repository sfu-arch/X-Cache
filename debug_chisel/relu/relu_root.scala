package dandelion.generator 

import chipsalliance.rocketchip.config._
import chisel3._
import dandelion.accel._
import dandelion.memory._

class reluRootDF(PtrsIn : Seq[Int] = List (64, 64), ValsIn : Seq[Int] = List(64), Returns: Seq[Int] = List())
                  (implicit p: Parameters) extends DandelionAccelDCRModule(PtrsIn, ValsIn, Returns) {

  val NumKernels = 1
  val memory_arbiter = Module(new MemArbiter(NumKernels))

  /**
    * Local memories
    */

  /**
    * Kernel Modules
    */
  val relu =  Module(new reluDF(PtrsIn = List(64, 64), ValsIn = List(64), Returns = List()))

  relu.io.in <> io.in
  io.out <> relu.io.out


  memory_arbiter.io.cpu.MemReq(0) <> relu.io.MemReq
  relu.io.MemResp <> memory_arbiter.io.cpu.MemResp(0)

  io.MemReq <> memory_arbiter.io.cache.MemReq
  memory_arbiter.io.cache.MemResp <> io.MemResp

}
