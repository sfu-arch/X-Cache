package dataflow

import chisel3._
import chisel3.util._
import chisel3.Module
import chisel3.testers._
import chisel3.iotesters.{ChiselFlatSpec, Driver, OrderedDecoupledHWIOTester, PeekPokeTester}
import org.scalatest.{FlatSpec, Matchers}
import muxes._
import config._
import control.BasicBlockNode
import util._
import interfaces._
import regfile._
import node._


//TODO uncomment if you remove StackCentral.scala file
//
abstract class AddDFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val Data0   = Flipped(Decoupled(new DataBundle))
    val pred    = Decoupled(new Bool())
    val result  = Decoupled(new DataBundle)
   })
}

class AddDF(implicit p: Parameters) extends AddDFIO()(p){
  

  //BasicBlock
  val b0_entry = Module(new BasicBlockNode(NumInputs = 1, NumOuts = 2, BID = 0)(p))
  val b1_then  = Module(new BasicBlockNode(NumInputs = 1, NumOuts = 1, BID = 1)(p))
  val b2_end   = Module(new BasicBlockNode(NumInputs = 1, NumOuts = 2, BID = 2)(p))

  //Compute
  val m0 = Module(new IcmpNode(NumOuts = 1, ID = 0, opCode = 1)(p))
  val m1 = Module(new CBranchNode(ID = 1))

  val m2 = Module(new ComputeNode(NumOuts = 1, ID = 2, opCode = 1)(p))
  //val m3 = Module(new UBranchNode(ID = 3)(p))

  //Wiring
  //predicate signal

  b0_entry.io.predicateIn(0).bits := true.B
  b0_entry.io.predicateIn(0).valid := true.B

  m1.io.Out(0).ready := b1_then.io.predicateIn(0).ready
  b1_then.io.predicateIn(0).bits  := m1.io.Out(0).bits.data.orR.toBool
  b1_then.io.predicateIn(0).valid := m1.io.Out(0).valid


  m0.io.enable <> b0_entry.io.Out(0)
  m1.io.enable <> b0_entry.io.Out(1)

  m2.io.enable <> b1_then.io.Out(0)
  //m3.io.enable <> b1_then.io.Out(1)

  io.pred <> b1_then.io.Out(0)

  //dataflow signal
  m0.io.LeftIO <> io.Data0
  m1.io.CmpIO <> m0.io.Out(0)
  m2.io.LeftIO <> io.Data0


  //Constant
  m0.io.RightIO.bits.data       := 9.U
  m0.io.RightIO.bits.predicate  := true.B
  m0.io.RightIO.bits.valid      := true.B
  m0.io.RightIO.valid           := true.B

  m2.io.RightIO.bits.data       := 5.U
  m2.io.RightIO.bits.predicate  := true.B
  m2.io.RightIO.bits.valid      := true.B
  m2.io.RightIO.valid           := true.B


  //Output
  io.result <> m2.io.Out(0)

}
