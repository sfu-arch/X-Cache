package dandelion.generator 

import chipsalliance.rocketchip.config._
import chisel3._
import dandelion.accel._
import dandelion.memory._

class stencil2dRootDF(PtrsIn : Seq[Int] = List (64, 64, 64), ValsIn : Seq[Int] = List(), Returns: Seq[Int] = List())
                  (implicit p: Parameters) extends DandelionAccelDCRModule(PtrsIn, ValsIn, Returns) {

  val NumKernels = 1
  val memory_arbiter = Module(new MemArbiter(NumKernels))

  /**
    * Local memories
    */

  /**
    * Kernel Modules
    */
  val stencil2d =  Module(new stencil2dDF(PtrsIn = List(64, 64, 64), ValsIn = List(), Returns = List()))

  stencil2d.io.in <> io.in
  io.out <> stencil2d.io.out


  memory_arbiter.io.cpu.MemReq(0) <> stencil2d.io.MemReq
  stencil2d.io.MemResp <> memory_arbiter.io.cpu.MemResp(0)

  io.MemReq <> memory_arbiter.io.cache.MemReq
  memory_arbiter.io.cache.MemResp <> io.MemResp

}
