package dandelion.generator 

import chipsalliance.rocketchip.config._
import chisel3._
import dandelion.accel._
import dandelion.memory._

class test14RootDF(PtrsIn : Seq[Int] = List (64), ValsIn : Seq[Int] = List(64), Returns: Seq[Int] = List())
                  (implicit p: Parameters) extends DandelionAccelDCRModule(PtrsIn, ValsIn, Returns) {

  val NumKernels = 1
  val memory_arbiter = Module(new MemArbiter(NumKernels))

  /**
    * Local memories
    */
  val memory_0 = Module(new ScratchPadMemory(Size = 24))

  /**
    * Kernel Modules
    */
  val test14 =  Module(new test14DF(PtrsIn = List(64), ValsIn = List(64), Returns = List()))

  test14.io.in <> io.in
  io.out <> test14.io.out

  memory_0.io.req <> test14.temp_mem_req
  temp_mem_resp <> memory_0.io.resp

  memory_arbiter.io.cpu.MemReq(0) <> test14.io.MemReq
  test14.io.MemResp <> memory_arbiter.io.cpu.MemResp(0)

  io.MemReq <> memory_arbiter.io.cache.MemReq
  memory_arbiter.io.cache.MemResp <> io.MemResp

}
