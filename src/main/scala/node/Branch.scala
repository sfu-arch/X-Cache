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

/** This nodes recieve an input from a node and then relay it to other nodes. Nodes using their token ID can register their request inside relay node.  And base on token ID relay node understand when it's ready to recieve a new data 
 * @param CMPIn   Result of CMP instruction
 * @param BasicB0 Activition basic block 0
 * @param BasicB1 Activition basic block 1
 */

abstract class UBranchNodeIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {

    val PredicateIN = Input(Bool())

    val PredicateOUT = Output(Bool())

  })
}

abstract class CBranchNodeIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {

    val CmpIn   = Flipped(Decoupled(Bool()))
    val PredicateIN = Input(Bool())

    val OutIO   = Output(UInt(2.W))
    val PredicateOUT = Output(Bool())
  })
}
class CBranchNode(val ID: Int = 0)(implicit p : Parameters) extends CBranchNodeIO()(p){

  // Extra information
  val token_reg = RegInit(0.U(tlen.W))
  val nodeID    = RegInit(ID.U)

  io.PredicateOUT := io.PredicateIN

  // Input data
  val operand_reg = Reg(UInt(2.W))

  //Branch node is always ready to accept data
  io.CmpIn.ready := true.B

  /**
   * Combination of bits and valid signal from CmpIn whill result the output value:
   *    valid == 0  ->  output = 0
   *    valid == 1  ->  cmp = false then 1
   *    valid == 1  ->  cmp = true  then 2
   */

  when(io.CmpIn.valid === false.B){
    io.OutIO := 0.U

    } .elsewhen(io.CmpIn.valid === true.B && io.CmpIn.bits === false.B){
      io.OutIO := 1.U

    } .elsewhen(io.CmpIn.valid === true.B && io.CmpIn.bits === true.B){
      io.OutIO := 2.U

    } .otherwise {
      io.OutIO := 0.U
    }


}


class UBranchNode(val ID: Int = 0)(implicit p : Parameters) extends UBranchNodeIO()(p){

  // Extra information
  val token_reg = RegInit(0.U(tlen.W))
  val nodeID    = RegInit(ID.U)

  io.PredicateOUT := io.PredicateIN

}


