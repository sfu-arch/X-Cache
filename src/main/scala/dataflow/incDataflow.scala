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
abstract class incDataFlowIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    //val result  = Output(xlen.U)
    val result = Decoupled(UInt(xlen.W))
    val out   = Output(UInt(2.W))
   })
}

class incDataFlow(implicit p: Parameters) extends incDataFlowIO()(p){
  

  val reg1 = Module(new InputRegFile(Array(5.U))(p))
  val reg2 = Module(new InputRegFile(Array(7.U))(p))
  val reg3 = Module(new InputRegFile(Array(3.U))(p))
  val reg4 = Module(new InputRegFile(Array(4.U))(p))
  val reg5 = Module(new InputRegFile(Array(9.U))(p))
  val reg6 = Module(new InputRegFile(Array(2.U))(p))

  val m0 = Module(new IcmpNode(1, 0)(p))

  val b0 = Module(new CBranchNode(1)(p))
  val b1 = Module(new UBranchNode()(p))
  val b2 = Module(new UBranchNode()(p))

  val m1 = Module(new DecoupledNodeSingle(0, 0)(p))
  val m2 = Module(new DecoupledNodeSingle(0, 0)(p))
  val m3 = Module(new DecoupledNodeSingle(0, 0)(p))
  val m4 = Module(new PhiNode()(p))

  val c0 = Module(new BasicBlockCtrl(numInst=2)(p))
  val c1 = Module(new BasicBlockCtrl(numInst=2)(p))
  val c2 = Module(new BasicBlockCtrl(numInst=2)(p))
  val c3 = Module(new BasicBlockCtrl(fanIN = 2, numInst=2)(p))

  //Data flow


  m0.io.LeftIO  <> reg1.io.Data
  m0.io.RightIO <> reg2.io.Data

  m1.io.LeftIO.valid := true.B
  m1.io.LeftIO.bits := 9.U
  m1.io.RightIO.valid := true.B
  m1.io.RightIO.bits := 4.U

  m2.io.LeftIO  <> reg3.io.Data
  m2.io.RightIO <> reg4.io.Data

  //printf(p"m4.io : ${m4.io}\n")
  m3.io.LeftIO.valid := true.B
  m3.io.LeftIO.bits := 9.U
  m3.io.RightIO <> m4.io.OutIO

  b0.io.CmpIn <> m0.io.OutIO

  m4.io.DataIn(0) <> m1.io.OutIO
  m4.io.Predicates(0) <> c1.io.PredicateOUT

  //printf(p"m4 : ${m4.io}\n")

  //printf(p"c1 predicate: ${c1.io.PredicateOUT}\n")
  //printf(p"c2 predicate: ${c2.io.PredicateOUT}\n")

  m4.io.DataIn(1) <> m2.io.OutIO
  m4.io.Predicates(1) <> c2.io.PredicateOUT


  //Control flow
  c0.io.Active(0) := true.B


  c1.io.Active(0) := b0.io.OutIO(0)
  c2.io.Active(0) := b0.io.OutIO(1)

  c3.io.Active(0) := b1.io.PredicateOUT
  c3.io.Active(1) := b2.io.PredicateOUT

  //Connecting Predicate bits
  //Entry bb:
  m0.io.PredicateIN := c0.io.InsVec
  b0.io.PredicateIN := c0.io.InsVec

  //If.then
  m1.io.PredicateIN := c1.io.InsVec
  //printf(p"m1 Output: ${m1.io}\n")
  b1.io.PredicateIN := c1.io.InsVec

  //If.else
  m2.io.PredicateIN := c2.io.InsVec
  b2.io.PredicateIN := c2.io.InsVec

  //If.end
  
  m3.io.PredicateIN := c3.io.InsVec
  m4.io.PredicateIN := c3.io.InsVec

  //printf(p"m4.io.out: ${m4.io.OutIO}\n")
  io.result <> m4.io.OutIO

}
