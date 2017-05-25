package node

import chisel3._
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import chisel3.Module
import chisel3.testers._
import chisel3.util._
import org.scalatest.{Matchers, FlatSpec} 

import config._
import interfaces._
import muxes._
import util._

abstract class Node()(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    // Inputs should be fed only when Ready is HIGH
    // Inputs are always latched.
    // If Ready is LOW; Do not change the inputs as this will cause a bug
    val LeftIO   = Flipped(Decoupled(UInt(xlen.W)))
    val RightIO  = Flipped(Decoupled(UInt(xlen.W))) 

    // The interface has to be prepared to latch the output on every cycle as long as ready is enabled
    // The output will appear only for one cycle and it has to be latched. 
    // The output WILL NOT BE HELD (not matter the state of ready/valid)
    // Ready simply ensures that no subsequent valid output will appear until Ready is HIGH
    //val OutIO = Decoupled(new DecoupledNodeOut(xlen))
    val OutIO   = Decoupled(UInt(xlen.W))

    val TokenIO = Output(UInt(tlen.W))

    })

}


class DecoupledNode(val opCode: Int, val ID: Int = 0)(implicit p: Parameters) extends Node()(p){

  // Extra information
  val token_reg  = RegInit(0.U(tlen.W))
  val nodeID = RegInit(ID.U)

  //Instantiate ALU with selected code
  val FU = Module(new ALU(xlen, opCode))

  // Input data
  val LeftOperand   = RegInit(0.U(xlen.W))
  val RightOperand  = RegInit(0.U(xlen.W))

  //Input valid signals
  val LeftValid  = RegInit(false.B)
  val RightValid = RegInit(false.B)

  //output valid signal
  val outValid   = LeftValid & RightValid

  io.OutIO.valid := outValid

  // Connect operands to ALU.
  FU.io.in1 := LeftOperand
  FU.io.in2 := RightOperand

  // Connect output to ALU
  io.OutIO.bits:= FU.io.out

  io.LeftIO.ready   := ~LeftValid
  io.RightIO.ready  := ~RightValid

  io.TokenIO := token_reg

  //Latch Left input if it's fire
  when(io.LeftIO.fire()){
    LeftOperand := io.LeftIO.bits
    LeftValid   := io.LeftIO.valid
  }

  //Latch Righ input if it's fire
  when(io.RightIO.fire()){
    RightOperand := io.RightIO.bits
    RightValid   := io.RightIO.valid
  }

  //Reset the latches if we make sure that 
  //consumer has consumed the output
  when(outValid && io.OutIO.ready){
    RightOperand := 0.U
    LeftOperand  := 0.U
    RightValid   := false.B
    LeftValid    := false.B
    token_reg := token_reg + 1.U
  }

}

