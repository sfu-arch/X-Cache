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


/**
 * This nodes recieve an input from a node and then
 * relay it to other nodes. Nodes using their token ID
 * can register their request inside relay node.
 * And base on token ID relay node understand when 
 * it's ready to recieve a new data
 *
 * @param NumConsum Number of consumer
 * @param DataIN    Input data from previous node
 * @param TokenIn   Input token from previous node
 */

abstract class RelayNode(NumConsum: Int)(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    // Inputs should be fed only when Ready is HIGH
    // Inputs are always latched.
    // If Ready is LOW; Do not change the inputs as this will cause a bug
    val DataIn    = Flipped(Decoupled(UInt(xlen.W)))
    val TokenIn   = Input(UInt(tlen.W))

    // The interface has to be prepared to latch the output on every cycle as long as ready is enabled
    // The output will appear only for one cycle and it has to be latched. 
    // The output WILL NOT BE HELD (not matter the state of ready/valid)
    // Ready simply ensures that no subsequent valid output will appear until Ready is HIGH
    val OutIO = Vec(NumConsum, new RelayOutput())
    //val OutIO  = Output(Vec(NumConsum, new Bundle{
        //val DataNode = Decoupled(UInt(xlen.W))
        //val TokenNode = Input(UInt(tlen.W))
      //}))
    })

}

class RelayDecoupledNode(val NumConsum: Int = 2)(implicit p : Parameters) extends RelayNode(NumConsum)(p){

  //Latch input data from parent node
  val dataIn_reg  = RegInit(0.U(tlen.W))
  val dataV_reg   = RegInit(false.B)
  val tokenIn_reg = RegInit(0.U(tlen.W))

  printf(p"dataIn reg:  ${dataIn_reg}\n")
  printf(p"dataV_reg:   ${dataV_reg}\n")
  printf(p"tokenIn_reg: ${tokenIn_reg}\n")


  val tokenConsum_reg = RegInit(Vec(Seq.fill(NumConsum)(0.U(tlen.W))))

  //val tokenCMP_w  = Wire(Vec(NumConsum, Bool()))
  val tokenCMP_w  = VecInit(Seq.fill(NumConsum){false.B})

  val res = tokenCMP_w.asUInt.andR

  io.DataIn.ready := ~dataV_reg

  when(io.DataIn.fire()){
    dataV_reg  := true.B
    dataIn_reg := io.DataIn.bits
    tokenIn_reg  := io.TokenIn
  }

  for(i <- 0 until NumConsum){
    io.OutIO(i).DataNode.bits   := dataIn_reg
    io.OutIO(i).DataNode.valid  := dataV_reg
  }

  printf(p"io.OutIO.DataNode: ${io.OutIO}\n")
  printf(p"TokenCMP: ${tokenCMP_w.asUInt}\n")

  for(i <- 0 until NumConsum){
    when(io.OutIO(i).DataNode.ready){
      tokenConsum_reg(i) := io.OutIO(i).TokenNode
    }
  }

  for(i <- 0 until NumConsum){
    when(tokenIn_reg =/= tokenConsum_reg(i)){
      tokenCMP_w(i) := 1.U
    }.otherwise{
      tokenCMP_w(i) := 0.U
    }
  }
  //
  //tokenCMP_w(0) := false.B

  when(res){
    dataV_reg := false.B
  }

}

//class DecoupledNode(val opCode: Int, val ID: Int = 0)(implicit p: Parameters) extends Node()(p){

  //// Extra information
  //val token_reg  = RegInit(0.U(tlen.W))
  //val nodeID = RegInit(ID.U)

  ////Instantiate ALU with selected code
  //val FU = Module(new ALU(xlen, opCode))

  //// Input data
  //val LeftOperand   = RegInit(0.U(xlen.W))
  //val RightOperand  = RegInit(0.U(xlen.W))

  ////Input valid signals
  //val LeftValid  = RegInit(false.B)
  //val RightValid = RegInit(false.B)

  ////output valid signal
  //val outValid   = LeftValid & RightValid

  //io.OutIO.valid := outValid

  //// Connect operands to ALU.
  //FU.io.in1 := LeftOperand
  //FU.io.in2 := RightOperand

  //// Connect output to ALU
  //io.OutIO.bits:= FU.io.out

  //io.LeftIO.ready   := ~LeftValid
  //io.RightIO.ready  := ~RightValid

  //io.TokenIO := token_reg

  ////Latch Left input if it's fire
  //when(io.LeftIO.fire()){
    //LeftOperand := io.LeftIO.bits
    //LeftValid   := io.LeftIO.valid
  //}

  ////Latch Righ input if it's fire
  //when(io.RightIO.fire()){
    //RightOperand := io.RightIO.bits
    //RightValid   := io.RightIO.valid
  //}

  ////Reset the latches if we make sure that 
  ////consumer has consumed the output
  //when(outValid && io.OutIO.ready){
    //RightOperand := 0.U
    //LeftOperand  := 0.U
    //RightValid   := false.B
    //LeftValid    := false.B
    //token_reg := token_reg + 1.U
  //}

//}

