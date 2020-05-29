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


class test10RootDF(PtrsIn: Seq[Int] = List(64, 64, 64), ValsIn: Seq[Int] = List(), Returns: Seq[Int] = List())
                  (implicit p: Parameters) extends DandelionAccelDCRModule(PtrsIn, ValsIn, Returns) {

  val NumKernels = 2
  val memory_arbiter = Module(new MemArbiter(NumKernels))

  val test10_main = Module(new test10DF(PtrsIn, ValsIn, Returns))
  val test10_add = Module(new test10_addDF(List(), List(64, 64), List(64)))

  test10_main.io.in <> io.in
  io.out <> test10_main.io.out

  test10_add.io.in <> test10_main.call_7_out_io
  test10_main.call_7_in_io <> test10_add.io.out

  memory_arbiter.io.cpu.MemReq(0) <> test10_main.io.MemReq
  memory_arbiter.io.cpu.MemReq(1) <> test10_add.io.MemReq

  test10_main.io.MemResp <> memory_arbiter.io.cpu.MemResp(0)
  test10_add.io.MemResp <> memory_arbiter.io.cpu.MemResp(1)

  io.MemReq <> memory_arbiter.io.cache.MemReq
  memory_arbiter.io.cache.MemResp <> io.MemResp
}

