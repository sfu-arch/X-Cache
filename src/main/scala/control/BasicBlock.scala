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
 * @param fanIN   Number of fan ins
 * @param numInst Number of instructions
 * @param Active  Active signal which tells basic block to activate all the included instructions
 * @param InsVec  Output signal for activating child instructions inside each basic block
 * @param PredicateOut single bit to activate rest of the basic blocks
 */
abstract class BasicBlockIO(val fanIN : Int, val numInst : Int )(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle{

    //Input activate signals
    val Active        = Vec(fanIN,   Input(Bool()))

    //Activiation signal for all the consequence instructions
    val InsVec        = Vec(numInst, Output(Bool()))

    //Preciation output bit
    val PredicateOut  = Output(Bool())
  })
}


class BasicBlockCtrl(fanIN : Int = 1, numInst : Int = 1, var BID : Int = 0)(implicit p: Parameters)extends BasicBlockIO(fanIN, numInst)(p){

  // Extra information
  val token_reg   = RegInit(0.U(tlen.W))
  val nodeID      = RegInit(BID.U)

  //Check whether at least of the fan-ins is activated
  val fanNumber = fanIN.asUInt.orR

  io.PredicateOut := fanNumber

  //If orR's result is true then activate all the instructions
  when(fanNumber){
    io.InsVec := Vec(Seq.fill(numInst){true.B})
    token_reg := token_reg + 1.U
    }.otherwise{
      io.InsVec := Vec(Seq.fill(numInst){false.B})
    }
}
