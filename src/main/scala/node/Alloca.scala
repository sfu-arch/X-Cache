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

class AllocaNodeIO(NumOuts: Int) (implicit p: Parameters)
  extends HandShakingIONPS(NumOuts)(new DataBundle) {
  /**
    * @note requested size for address
    */
  val allocaInputIO = Flipped(Decoupled(new AllocaIO()))

  /**
    * @note Alloca interface to talk with stack
    */
  val allocaReqIO = Decoupled(new AllocaReq())
  val allocaRespIO = Input(Flipped(new AllocaResp()))

}

class AllocaNode(NumOuts: Int, ID: Int, RouteID: Int)
                (implicit p: Parameters)
  extends HandShakingNPS(NumOuts, ID)(new DataBundle)(p) {
  override lazy val io = IO(new AllocaNodeIO(NumOuts))
  // Printf debugging
  override val printfSigil = "Node ID: " + ID + " "

  /*===========================================*
   *            Registers                      *
   *===========================================*/
  // OP Inputs
  val alloca_R = RegInit(AllocaIO.default)

  // Output register
  val data_R = RegInit(0.U(xlen.W))
  val pred_R = RegInit(false.B)

  // Alloca req
  val alloca_req_R = RegInit(AllocaReq.default)

  // Alloca resp
  val alloca_resp_R = RegInit(AllocaResp.default)

  /*==========================================*
   *           Predicate Evaluation           *
   *==========================================*/

  val predicate = alloca_R.predicate & IsEnable()
  val start = alloca_R.valid & IsEnableValid()

  /*===============================================*
   *            Latch inputs. Wire up output       *
   *===============================================*/

  // Predicate register
  io.allocaInputIO.ready := ~alloca_R.valid

  // Input Register
  when(io.allocaInputIO.fire()) {
    alloca_R.size := io.allocaInputIO.bits.size
    alloca_R.numByte := io.allocaInputIO.bits.numByte
    alloca_R.valid := true.B
    alloca_R.predicate := io.allocaInputIO.bits.predicate
  }

  /**
    * Defaults assignments
    */

  io.allocaReqIO.bits.size := alloca_R.size
  io.allocaReqIO.bits.numByte := alloca_R.numByte
  io.allocaReqIO.bits.node    := nodeID_R
  io.allocaReqIO.bits.RouteID := RouteID.U
  io.allocaReqIO.valid := false.B

  /**
    * State Machine
    */
  val s_idle :: s_req :: s_done :: Nil = Enum(3)
  val state = RegInit(s_idle)

  switch (state) {
    is (s_idle) {
      when (start & predicate) {
        io.allocaReqIO.valid := true.B
        state := s_req
      }
    }
    is (s_req) {
      when (io.allocaRespIO.valid) {
        data_R       := io.allocaRespIO.ptr
        ValidOut()
        // Completion state.
        state := s_done
      }
    }
    is (s_done) {
      when(IsOutReady()) {
        io.allocaReqIO.valid := false.B
        alloca_R := AllocaIO.default
        data_R := 0.U
        pred_R := false.B
        state := s_idle
        Reset()
      }
    }
  }

  // Wire up Outputs.
  for (i <- 0 until NumOuts) {
    io.Out(i).bits.data := data_R
    io.Out(i).bits.predicate := predicate
    io.Out(i).bits.valid := true.B
  }


  // printf(p"State: ${state}\n")
  // printf(p"Alloca reg: ${alloca_R}\n")
  // printf(p"Alloca input: ${io.allocaInputIO}\n")
  // printf(p"Alloca req:   ${io.allocaReqIO}\n")
  // printf(p"Alloca res:   ${io.allocaRespIO}\n")

  
}
