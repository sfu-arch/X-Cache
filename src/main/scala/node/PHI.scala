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
 * @param NumProd Number of consumer
 * @param DataIN    Input data from previous node
 * @param TokenIn   Input token from previous node
 */

abstract class PhiIO(NumProd: Int)(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    // Inputs should be fed only when Ready is HIGH
    // Inputs are always latched.
    // If Ready is LOW; Do not change the inputs as this will cause a bug
    val DataIn    = Vec(NumProd, Flipped(Decoupled(UInt(xlen.W))))

    val Predicates    = Vec(NumProd, Input(Bool()))

    val PredicateIN = Input(Bool())

    // The interface has to be prepared to latch the output on every cycle as long as ready is enabled
    // The output will appear only for one cycle and it has to be latched. 
    // The output WILL NOT BE HELD (not matter the state of ready/valid)
    // Ready simply ensures that no subsequent valid output will appear until Ready is HIGH
    val OutIO = Decoupled(UInt(xlen.W))

    val PredicateOUT = Output(Bool())
    })

}

class PhiNode(val NumProd: Int = 2)(implicit p : Parameters) extends PhiIO(NumProd)(p){

  io.PredicateOUT := io.PredicateIN

  var array_in_bit = Array(0.U -> 0.U)
  //Generate an array of input bits
  for(i <- 0 until NumProd){
    array_in_bit = array_in_bit :+ ((i+1).U -> io.DataIn(i).bits )
  }

  var array_in_valid= Array(0.U -> false.B)
  //Generate an array of input valid
  for(i <- 0 until NumProd){
    array_in_valid = array_in_valid :+ ((i+1).U -> io.DataIn(i).valid)
  }

  //Initialize ready signal to false
  for(i <- 0 until NumProd){
    io.DataIn(i).ready := false.B
  }

  when(io.OutIO.ready){
    io.DataIn(io.Predicates.asUInt + 1.U).ready := true.B
  }

  val rs_data  = MuxLookup(io.Predicates.asUInt, 0.U, array_in_bit)
  val rs_valid = MuxLookup(io.Predicates.asUInt, false.B, array_in_valid)

  //printf(p"Predicates: ${io.Predicates}\n")
  //printf(p"Valid : ${io.Predicates.asUInt}\n")
  //printf(p"Result: ${rs_data}\n")

  io.OutIO.bits := rs_data
  io.OutIO.valid:= rs_valid

}
