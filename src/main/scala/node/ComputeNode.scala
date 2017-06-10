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

class ComputeNodeIO(NumOuts: Int)
  (implicit p: Parameters)
  extends HandShakingIONPS(NumOuts) {
    // LeftIO: Left input data for computation
    val LeftIO  = Flipped(Decoupled(new DataBundle))

    // RightIO: Right input data for computation
    val RightIO = Flipped(Decoupled(new DataBundle))
}

class ComputeNode(NumOuts: Int, ID: Int, opCode: Int)
  (implicit p: Parameters)
  extends HandShakingNPS(NumOuts, ID)(p) {
    override lazy val io = IO(new ComputeNodeIO(NumOuts))
    // Printf debugging
    override val printfSigil = "Node ID: " + ID + " "

    /*===========================================*
     *            Registers                      *
     *===========================================*/
      // OP Inputs
      val left_R  = RegInit(DataBundle.default)
    
      // Memory Response
      val right_R = RegInit(DataBundle.default)

      // Output register
      val data_R = RegInit(0.U(xlen.W))
    
    /*==========================================*
     *           Predicate Evaluation           *
     *==========================================*/
    
      val predicate = left_R.predicate & right_R.predicate & IsEnable()
      val start     = left_R.valid & right_R.valid & IsEnableValid()
    
    /*===============================================*
     *            Latch inputs. Wire up output       *
     *===============================================*/
    
      io.LeftIO.ready := ~left_R.valid
      when(io.LeftIO.fire()) {
        left_R.data  := io.LeftIO.bits.data
        left_R.valid := true.B
      }

      io.RightIO.ready := ~right_R.valid
      when(io.RightIO.fire()) {
        right_R.data  := io.RightIO.bits.data
        right_R.valid := true.B
      }


      //Instantiate ALU with selected code
      val FU = Module(new ALU(xlen, opCode))

      FU.io.in1 := left_R.data
      FU.io.in2 := right_R.data
    
      // Wire up Outputs
      for (i <- 0 until NumOuts) {
        io.Out(i).bits.data      := data_R.data
        io.Out(i).bits.predicate := predicate
      }
    
    
    /*============================================*
     *            ACTIONS (possibly dangerous)    *
     *============================================*/
    when(start && predicate) {
      data_R := FU.io.out
      ValidOut()
    }.elsewhen(start && ~predicate) {
      ValidOut()
    }

    /*==========================================*
     *            Output Handshaking and Reset  *
     *==========================================*/

    val complete = IsOutValid() && IsOutReady()
    when(complete){
      // Reset data
      left_R  := DataBundle.default
      right_R := DataBundle.default
      // Reset output
      Reset()
    }

  printfInfo("\n")
  printf(p"Left; ${left_R}\n")
  printf(p"Right, ${right_R}\n")


  }
