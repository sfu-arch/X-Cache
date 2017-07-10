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

  /**
    * Instantiating Arbiter module and connecting inputs to the output
    * @note we fix the base size to 8
    */
  val in_arbiter = Module(new Arbiter(new AllocaReq, NumOps))
  for( i <- 0 until NumOps){
    in_arbiter.io.in(i) <> io.InData(i)
  }

  /**
    * Instantiating Demux
    */
  val out_demux = Module(new Demux(new AllocaResp(), Nops = NumOps))
  out_demux.io.en := true.B
  for( i <- 0 until NumOps){
    io.OutData(i).bits <> out_demux.io.outputs(i)
    io.OutData(i).valid <> out_demux.io.valids(i)
  }

  out_demux.io.sel := in_arbiter.io.chosen

  /**
    * Arbiter's output is always ready
    */
  in_arbiter.io.out.ready := true.B

  /**
    * Stack pointer Update
    */
  val SP = RegInit(0.U)
  val old_SP = RegInit(0.U)

  when(in_arbiter.io.out.fire){
    old_SP := SP
    SP := SP + (in_arbiter.io.out.bits.numByte * in_arbiter.io.out.bits.size)
  }

  /**
    * Connecting SP to the demux
    */

  out_demux.io.input.ptr := old_SP
  out_demux.io.input.nodeID := in_arbiter.io.out.bits.nodeID

}

