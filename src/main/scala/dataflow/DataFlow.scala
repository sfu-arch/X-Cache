package dataflow

import chisel3._
import chisel3.util._
import chisel3.Module
import chisel3.testers._
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import org.scalatest.{Matchers, FlatSpec} 

import muxes._
import config._
import util._
import interfaces._
import regfile._
import node._


//TODO uncomment if you remove StackCentral.scala file
//
abstract class myDataFlow(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val result  = Output(xlen.U)
    val resultReady = Input(Bool())
    val resultValid = Output(Bool())
   })
}

class newDataFlow(implicit p: Parameters) extends myDataFlow()(p){
  

  val reg1 = Module(new InputRegFile(Array(1.U, 4.U, 3.U, 4.U))(p))
  val reg2 = Module(new InputRegFile(Array(4.U, 4.U, 7.U, 8.U))(p))
  val reg3 = Module(new InputRegFile(Array(2.U, 2.U, 5.U, 8.U))(p))
  val reg4 = Module(new InputRegFile(Array(3.U, 6.U, 2.U, 8.U))(p))
  val reg5 = Module(new InputRegFile(Array(6.U, 8.U, 2.U, 8.U))(p))
  val reg6 = Module(new InputRegFile(Array(7.U, 5.U, 3.U, 8.U))(p))
  val reg7 = Module(new InputRegFile(Array(6.U, 1.U, 4.U, 8.U))(p))
  val reg8 = Module(new InputRegFile(Array(9.U, 2.U, 5.U, 8.U))(p))

  val m0 = Module(new DecoupledNode(0, 0)(p))
  val m1 = Module(new DecoupledNode(0, 1)(p))
  val m2 = Module(new DecoupledNode(0, 2)(p))
  val m3 = Module(new DecoupledNode(0, 3)(p))
  val m4 = Module(new DecoupledNode(0, 4)(p))
  val m5 = Module(new DecoupledNode(0, 5)(p))
  val m6 = Module(new DecoupledNode(0, 6)(p))


  m0.io.LeftIO  <> reg1.io.Data
  m0.io.RightIO <> reg2.io.Data

  m1.io.LeftIO  <> reg3.io.Data
  m1.io.RightIO <> reg4.io.Data

  m4.io.LeftIO  <> reg5.io.Data
  m4.io.RightIO <> reg6.io.Data

  m6.io.LeftIO  <> reg7.io.Data
  m6.io.RightIO <> reg8.io.Data

  m2.io.LeftIO  <> m0.io.OutIO
  m2.io.RightIO <> m1.io.OutIO

  m3.io.LeftIO  <> m2.io.OutIO
  m3.io.RightIO <> m4.io.OutIO

  m5.io.LeftIO  <> m3.io.OutIO
  m5.io.RightIO <> m6.io.OutIO

  m5.io.OutIO.ready := io.resultReady
  io.result := m5.io.OutIO.bits
  io.resultValid := m5.io.OutIO.valid

}
