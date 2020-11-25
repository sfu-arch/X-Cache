package dandelion.generator 

import chipsalliance.rocketchip.config._
import chisel3._
import dandelion.accel._
import dandelion.memory._

class conv2dRootDF(PtrsIn : Seq[Int] = List (64, 64, 64), ValsIn : Seq[Int] = List(64, 64, 64, 64), Returns: Seq[Int] = List())
                  (implicit p: Parameters) extends DandelionAccelDCRModule(PtrsIn, ValsIn, Returns) {

  val NumKernels = 1
  val memory_arbiter = Module(new MemArbiter(NumKernels))

  /**
    * Local memories
    */

  /**
    * Kernel Modules
    */
  val conv2d =  Module(new conv2dDF(PtrsIn = List(64, 64, 64), ValsIn = List(64, 64, 64, 64), Returns = List()))

  conv2d.io.in <> io.in
  io.out <> conv2d.io.out


  memory_arbiter.io.cpu.MemReq(0) <> conv2d.io.MemReq
  conv2d.io.MemResp <> memory_arbiter.io.cpu.MemResp(0)

  io.MemReq <> memory_arbiter.io.cache.MemReq
  memory_arbiter.io.cache.MemResp <> io.MemResp

}
