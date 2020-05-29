package dandelion.generator

import chipsalliance.rocketchip.config._
import chisel3._
import chisel3.util._
import chisel3.Module._
import chisel3.testers._
import chisel3.iotesters._
import dandelion.accel._
import dandelion.arbiters._
import dandelion.config._
import dandelion.control._
import dandelion.fpu._
import dandelion.interfaces._
import dandelion.junctions._
import dandelion.loop._
import dandelion.memory._
import dandelion.memory.stack._
import dandelion.node._
import muxes._
import org.scalatest._
import regfile._
import util._


class test15RootDF(PtrsIn: Seq[Int] = List(64, 64, 64), ValsIn: Seq[Int] = List(), Returns: Seq[Int] = List(64))
                  (implicit p: Parameters) extends DandelionAccelDCRModule(PtrsIn, ValsIn, Returns) {

  val NumKernels = 3
  val memory_arbiter = Module(new MemArbiter(NumKernels))

  val test15_main = Module(new test15DF(PtrsIn, ValsIn, Returns))
  val test15_mul = Module(new test15_mulDF(List(64, 64, 64), List(), List()))
  val test15_reduce = Module(new test15_reduceDF(List(64), List(), List(64)))

  test15_main.io.in <> io.in
  io.out <> test15_main.io.out

  test15_mul.io.in <> test15_main.call_0_out_io
  test15_main.call_0_in_io <> test15_mul.io.out

  test15_reduce.io.in <> test15_main.call_1_out_io
  test15_main.call_1_in_io <> test15_reduce.io.out

  memory_arbiter.io.cpu.MemReq(0) <> test15_main.io.MemReq
  memory_arbiter.io.cpu.MemReq(1) <> test15_mul.io.MemReq
  memory_arbiter.io.cpu.MemReq(2) <> test15_reduce.io.MemReq

  test15_main.io.MemResp <> memory_arbiter.io.cpu.MemResp(0)
  test15_mul.io.MemResp <> memory_arbiter.io.cpu.MemResp(1)
  test15_reduce.io.MemResp <> memory_arbiter.io.cpu.MemResp(2)

  io.MemReq <> memory_arbiter.io.cache.MemReq
  memory_arbiter.io.cache.MemResp <> io.MemResp
}

