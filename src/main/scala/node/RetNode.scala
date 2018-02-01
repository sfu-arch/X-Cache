package node

import chisel3._
import chisel3.Module
import junctions._

import config._
import interfaces._
import util._
import utility.UniformPrintfs

class RetNodeIO(val retTypes: Seq[Int])(implicit p: Parameters)
  extends Bundle
{
  val In      = Flipped(new CallDecoupled(retTypes))   // Data to be returned
  val Out     = Decoupled(new Call(retTypes))          // Returns to calling block(s)
}

class RetNode(ID: Int, retTypes: Seq[Int])(implicit p: Parameters) extends Module
  with UniformPrintfs {
  override lazy val io = IO(new RetNodeIO(retTypes)(p))
  override val printfSigil = "Node (Ret) ID: " + ID + " "

  // Combine individually decoupled enable and data into single decoupled call
  val CombineIn = Module(new CombineCall(retTypes))
  CombineIn.io.In <> io.In
  io.Out <> CombineIn.io.Out

  when(CombineIn.io.Out.fire) {
    printfInfo("Output fired")
  }
}
