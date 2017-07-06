package stack

import chisel3._
import chisel3.util._
import chisel3.Module
import chisel3.testers._
import chisel3.iotesters.{ChiselFlatSpec, Driver, OrderedDecoupledHWIOTester, PeekPokeTester}
import org.scalatest.{FlatSpec, Matchers}
import config._
import interfaces._
import arbiters._
import util._
import utility.UniformPrintfs
import muxes._
import node._

class StackIO(NumOps: Int)
             (implicit p: Parameters) extends CoreBundle()(p) {
  val InData = Vec(NumOps, Flipped(Decoupled(new AllocaReq)))
  val OutData = Vec(NumOps, Decoupled(new AllocaResp))
}

class Stack(NumOps: Int)
           (implicit val p: Parameters) extends Module with CoreParams with UniformPrintfs{
  override lazy val io = IO(new StackIO(NumOps))
  // Printf debugging

  /**
    * Instantiating Arbiter module and connecting inputs to the output
    * @note we fix the base size to 8
    */
  val in_arb = Module(new ArbiterTree(BaseSize = 8, NumOps = NumOps, new AllocaReq()))
  for( i <- 0 until NumOps){
    in_arb.io.in(i) <> io.InData(i)
  }

  /**
    * Instantiating Demux
    */
  val out_demux = Module(new DeMuxTree(BaseSize = 8, NumOps = NumOps, new AllocaResp()))
  out_demux.io.enable := true.B
  for( i <- 0 until NumOps){
    io.OutData(i).bits <> out_demux.io.outputs(i)
    io.OutData(i).valid <> out_demux.io.outputs(i).valid
  }


  /**
    * Stack pointer computation
    */

  val SP = RegInit(0.U)
  //Check if SP is grater than 16MB
  assert(SP <= (1 << 24).U)

  val new_sp = (in_arb.io.out.bits.numByte * in_arb.io.out.bits.size) + SP

  when(in_arb.io.out.valid){
    /**
      * Send result to the Demux
      */
    out_demux.io.input.nodeID := in_arb.io.out.bits.nodeID
    out_demux.io.input.ptr := new_sp
    out_demux.io.input.valid := true.B
    out_demux.io.input.RouteID := in_arb.io.out.bits.nodeID
    out_demux.io.sel := in_arb.io.out.bits.nodeID

    /**
      * Update the stack pointer
      */
    SP := new_sp
  }

}

