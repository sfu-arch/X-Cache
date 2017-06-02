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
import control._


//TODO uncomment if you remove StackCentral.scala file
//
abstract class dataflowIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val result  = Output(xlen.U)
    val resultReady = Input(Bool())
    val resultValid = Output(Bool())
   })
}

class simpleDataFlow(implicit p: Parameters) extends dataflowIO()(p){
  

  //Dataflow graph
  val reg1 = Module(new InputRegFile(Array(1.U))(p))  //a
  val reg2 = Module(new InputRegFile(Array(4.U))(p))  //b
  val reg3 = Module(new InputRegFile(Array(7.U))(p))  //c
  val reg4 = Module(new InputRegFile(Array(2.U))(p))  //d

  val reg5 = Module(new InputRegFile(Array(5.U))(p))  //e
  val reg6 = Module(new InputRegFile(Array(7.U))(p))  //f

  val m0 = Module(new DecoupledNodeSingle(0, 0)(p))
  val m1 = Module(new DecoupledNodeSingle(0, 0)(p))
  val m2 = Module(new PhiNode()(p))
  val m3 = Module(new DecoupledNodeSingle(0, 0)(p))

  val m4 = Module(new IcmpNode(1, 0)(p))

  val m5 = Module(new CBranchNode(0)(p))

  val m6 = Module(new UBranchNode()(p))
  val m7 = Module(new UBranchNode()(p))

  m0.io.LeftIO  <> reg3.io.Data
  m0.io.RightIO <> reg1.io.Data

  m1.io.LeftIO  <> reg3.io.Data
  m1.io.RightIO <> reg2.io.Data

  m4.io.LeftIO  <> reg5.io.Data
  m4.io.RightIO <> reg6.io.Data

  m5.io.CmpIn <> m4.io.OutIO

  m2.io.DataIn(0) <> m0.io.OutIO
  m2.io.DataIn(1) <> m1.io.OutIO

  m2.io.Predicates(0) := m0.io.PredicateOUT
  m2.io.Predicates(1) := m1.io.PredicateOUT

  m3.io.LeftIO  <> m2.io.OutIO
  m3.io.RightIO <> reg4.io.Data

  m3.io.OutIO.ready := io.resultReady
  io.result := m3.io.OutIO.bits
  io.resultValid := m3.io.OutIO.valid


  //Controlflow Graph
  //entry
  val c0 = Module(new BasicBlockCtrl(1,2,0)(p))
  //ifthen
  val c1 = Module(new BasicBlockCtrl(1,2,0)(p))
  //ifelse
  val c2 = Module(new BasicBlockCtrl(1,2,0)(p))
  //ifend
  val c3 = Module(new BasicBlockCtrl(2,2,0)(p))

  //Entry basic block is awlays active
  c0.io.Active(0) := true.B
  m4.io.PredicateIN := c0.io.InsVec(0)
  m5.io.PredicateIN := c0.io.InsVec(1)
  c1.io.Active(0) := m5.io.PredicateOUT
  c2.io.Active(0) := m5.io.PredicateOUT

  //If then BB
  m0.io.PredicateIN := c1.io.InsVec(0)
  m6.io.PredicateIN := c1.io.InsVec(1)

  //If then BB
  m1.io.PredicateIN := c2.io.InsVec(0)
  m7.io.PredicateIN := c2.io.InsVec(1)

  //If end BB
  m2.io.Predicates(0) := m0.io.PredicateOUT
  m2.io.Predicates(1) := m1.io.PredicateOUT


  m2.io.PredicateIN := c3.io.InsVec(0)
  m3.io.PredicateIN := c3.io.InsVec(1)

  c3.io.Active(0)   := m6.io.PredicateOUT
  c3.io.Active(1)   := m7.io.PredicateOUT

}
