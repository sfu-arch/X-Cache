package dataflow

import chisel3._
import chisel3.util._
import node._


class DataFlow(xLen: Int) extends Module {

  val io = IO(new Bundle {
    val In1   =  Flipped(Decoupled(UInt(xLen.W))) 
    val In2   =  Flipped(Decoupled(UInt(xLen.W))) 
    val In3   =  Flipped(Decoupled(UInt(xLen.W))) 
    val Out1  =  Decoupled(UInt(xLen.W))
    val e  = Input(Bool())
  })


  //val m0 = Module(new NewDecoupledNode(xLen, 0, 0))
  //m0.io.LeftIO  <> io.In1
  //m0.io.RightIO <> io.In2

  //val m1 = Module(new NewDecoupledNode(xLen, 1, 1))
  //m1.io.LeftIO <> m0.io.OutIO
  //m1.io.RightIO <> io.In3

  //io.Out1 <> m1.io.OutIO
}
