package control

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
 * BasicBlock is class which impliments notion of basic block in our chisel dataflow
 * project.
 * 
 * @param Active  Active signal which tells basic block to activate all the included instructions
 * @param InsVec  Output signal for activating child instructions inside each basic block
 */
abstract class BasicBlock(val numInst : Int)(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle{
    val Active = Input(Bool())
    val InsVec = Vec(numInst, Output(Bool()))
  })
}


class ControlFlowNode(numInst : Int)(implicit p: Parameters) extends BasicBlock(numInst)(p){

}
