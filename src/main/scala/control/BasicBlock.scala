package control

import chisel3._
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import chisel3.Module
import chisel3.testers._
import chisel3.util._
import org.scalatest.{Matchers, FlatSpec}

import node._
import config._
import interfaces._
import muxes._
import util._

class BasicBlockIO(NumInputs: Int, NumOuts: Int)
  (implicit p: Parameters)
  extends HandShakingCtrlIO(NumOuts) {
    // LeftIO: Left input data for computation
    val predicateIn = Vec(NumInputs, Flipped(Decoupled(Bool())))

}

class BasicBlockNode(NumInputs: Int,
                 NumOuts: Int, BID: Int)
  (implicit p: Parameters)
  extends HandShakingCtrl(NumOuts, BID)(p) {
    override lazy val io = IO(new BasicBlockIO(NumInputs,NumOuts))
    // Printf debugging
    override val printfSigil = "BasicBlock ID: " + BID + " "

    /*===========================================*
     *            Registers                      *
     *===========================================*/
      // OP Inputs
      val predicate_in_R    = RegInit(Vec(Seq.fill(NumInputs)(false.B)))
      val predicate_valid_R = RegInit(Vec(Seq.fill(NumInputs)(false.B)))
    
      val s_idle :: s_LATCH :: s_COMPUTE :: Nil = Enum(3)
      val state  = RegInit(s_idle)

    /*===========================================*
     *            Valids                         *
     *===========================================*/

      val predicate = predicate_in_R.asUInt().orR
      val start     = predicate_valid_R.asUInt().andR
    
    /*===============================================*
     *            Latch inputs. Wire up output       *
     *===============================================*/
    
      //printfInfo("start: %x\n", start)

      val pred_R = RegInit(init=false.B)

      for ( i <- 0 until NumInputs ){
        io.predicateIn(i).ready := ~predicate_valid_R(i)
        when(io.predicateIn(i).fire()) {
          //printfInfo("Latch predicate %x\n", i)
          state := s_LATCH
          predicate_in_R(i)    := io.predicateIn(i).bits
          predicate_valid_R(i) := true.B
        }

      }

      // Wire up Outputs
      for (i <- 0 until NumOuts) {
        io.Out(i).bits      := pred_R
      }
    
    
    /*============================================*
     *            ACTIONS (possibly dangerous)    *
     *============================================*/

    when(start) {
      state := s_COMPUTE
      pred_R:= predicate
      ValidOut()
    }

    /*==========================================*
     *            Output Handshaking and Reset  *
     *==========================================*/


    val out_ready_W = out_ready_R.asUInt.andR
    val out_valid_W = out_valid_R.asUInt.andR

    printfInfo("out_ready: %x\n", out_ready_W)
    printfInfo("out_valid: %x\n", out_valid_W)

    when(out_ready_W & out_valid_W){
      //printfInfo("Start restarting output \n")
      // Reset data
      predicate_in_R    := Vec(Seq.fill(NumInputs){false.B})
      predicate_valid_R := Vec(Seq.fill(NumInputs){false.B})

      // Reset output
      out_ready_R := Vec(Seq.fill(NumOuts) { false.B })

      //Reset state
      state := s_idle
      //Restart predicate bit
      pred_R := false.B
    }

  //printfInfo(" State: %x\n", state)


  }
