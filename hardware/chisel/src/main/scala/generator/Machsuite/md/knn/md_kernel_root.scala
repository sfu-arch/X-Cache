package dandelion.generator 

import chipsalliance.rocketchip.config._
import chisel3._
import dandelion.accel._
import dandelion.memory._

class md_kernelRootDF(PtrsIn : Seq[Int] = List (64, 64, 64, 64, 64, 64, 64), ValsIn : Seq[Int] = List(), Returns: Seq[Int] = List())
                  (implicit p: Parameters) extends DandelionAccelDCRModule(PtrsIn, ValsIn, Returns) {

  val NumKernels = 1
  val memory_arbiter = Module(new MemArbiter(NumKernels))

  /**
    * Local memories
    */

  /**
    * Kernel Modules
    */
  val md_kernel =  Module(new md_kernelDF(PtrsIn = List(64, 64, 64, 64, 64, 64, 64), ValsIn = List(), Returns = List()))

  md_kernel.io.in <> io.in
  io.out <> md_kernel.io.out


  memory_arbiter.io.cpu.MemReq(0) <> md_kernel.io.MemReq
  md_kernel.io.MemResp <> memory_arbiter.io.cpu.MemResp(0)

  io.MemReq <> memory_arbiter.io.cache.MemReq
  memory_arbiter.io.cache.MemResp <> io.MemResp

}
