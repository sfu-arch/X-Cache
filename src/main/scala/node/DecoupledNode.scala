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

abstract class NodeSingleIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    // Inputs should be fed only when Ready is HIGH
    // Inputs are always latched.
    // If Ready is LOW; Do not change the inputs as this will cause a bug
    val LeftIO    = Flipped(Decoupled(UInt(xlen.W)))
    val RightIO   = Flipped(Decoupled(UInt(xlen.W))) 

    //Predicate bit comming from basic block bits
    val PredicateIN = Input(Bool())

    // The interface has to be prepared to latch the output on every cycle as long as ready is enabled
    // The output will appear only for one cycle and it has to be latched. 
    // The output WILL NOT BE HELD (not matter the state of ready/valid)
    // Ready simply ensures that no subsequent valid output will appear until Ready is HIGH
    //val OutIO = Decoupled(new DecoupledNodeOut(xlen))
    val OutIO   = Decoupled(UInt(xlen.W))

    val PredicateOUT = Output(Bool())

    })

}



abstract class NodeTwoIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    // Inputs should be fed only when Ready is HIGH
    // Inputs are always latched.
    // If Ready is LOW; Do not change the inputs as this will cause a bug
    val LeftIO   = Flipped(Decoupled(UInt(xlen.W)))
    val RightIO  = Flipped(Decoupled(UInt(xlen.W))) 

    //Predicate bit comming from basic block bits
    val PredicateIN = Input(Bool())

    // The interface has to be prepared to latch the output on every cycle as long as ready is enabled
    // The output will appear only for one cycle and it has to be latched. 
    // The output WILL NOT BE HELD (not matter the state of ready/valid)
    // Ready simply ensures that no subsequent valid output will appear until Ready is HIGH
    //val OutIO = Decoupled(new DecoupledNodeOut(xlen))
    val OutIoFirst   = Decoupled(UInt(xlen.W))
    val OutIoSecond  = Decoupled(UInt(xlen.W))

    val PredicateOUT = Output(Bool())


    })

}


/**
 * Decoupled node with single input
 * Decoupled node do the computation base on the inputs.
 * Inputs for computation nodes are decoupled so that the node
 * should follow the ready/valid handshaking signalling.
 *
 * @param opCode  Opcode code comming from doc
 * @param ID      Node ID from dot graph file
 */
class DecoupledNodeSingle(val opCode: Int, val ID: Int = 0)(implicit p: Parameters) extends NodeSingleIO()(p){

  // Extra information
  val token_reg  = RegInit(0.U(tlen.W))
  val nodeID = RegInit(ID.U)

  io.PredicateOUT := io.PredicateIN

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

  io.OutIO.valid  := outValid

  // Connect operands to ALU.
  FU.io.in1 := LeftOperand
  FU.io.in2 := RightOperand

  // Connect output to ALU
  io.OutIO.bits:= FU.io.out

  io.LeftIO.ready   := ~LeftValid
  io.RightIO.ready  := ~RightValid

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
  when(outValid && io.OutIO.ready ){
    RightOperand := 0.U
    LeftOperand  := 0.U
    RightValid   := false.B
    LeftValid    := false.B
    token_reg := token_reg + 1.U
  }

}



class DecoupledNodeTwo(val opCode: Int, val ID: Int = 0)(implicit p: Parameters) extends NodeTwoIO()(p){

  // Extra information
  val token_reg  = RegInit(0.U(tlen.W))
  val nodeID = RegInit(ID.U)

  io.PredicateOUT := io.PredicateIN

  //Instantiate ALU with selected code
  val FU = Module(new ALU(xlen, opCode))

  // Input data
  val LeftOperand   = RegInit(0.U(xlen.W))
  val RightOperand  = RegInit(0.U(xlen.W))

  //Input valid signals
  val LeftValid  = RegInit(false.B)
  val RightValid = RegInit(false.B)

  // Connect operands to ALU.
  FU.io.in1 := LeftOperand
  FU.io.in2 := RightOperand

  // Connect output to ALU
  io.OutIoFirst.bits:= FU.io.out
  io.OutIoSecond.bits:= FU.io.out

  io.LeftIO.ready   := ~LeftValid
  io.RightIO.ready  := ~RightValid

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

  /**
    * @todo Add predicate bit here
    */
  //output valid signal
  val outValid   = LeftValid & RightValid

  io.OutIoFirst.valid  := outValid
  io.OutIoSecond.valid := outValid

  //Reset the latches if we make sure that
  //consumer has consumed the output
  when(outValid && io.OutIoFirst.ready && io.OutIoSecond.ready){
    RightOperand := 0.U
    LeftOperand  := 0.U
    RightValid   := false.B
    LeftValid    := false.B
    token_reg := token_reg + 1.U
  }

}

