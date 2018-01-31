package node

import chisel3._
import chisel3.Module
import junctions._

import config._
import interfaces._
import util._
import utility.UniformPrintfs

class CallNodeIO(val argTypes: Seq[Int], val retTypes: Seq[Int])(implicit p: Parameters)
  extends Bundle
{
  val In      = Flipped(new CallDecoupled(argTypes))   // Requests from calling block(s)
  val callOut = Decoupled(new Call(argTypes))          // To task
  val retIn   = Flipped(Decoupled(new Call(retTypes))) // From task
  val Out     = new CallDecoupled(retTypes)            // Returns to calling block(s)
}

class CallNode(ID: Int, argTypes: Seq[Int], retTypes: Seq[Int])(implicit p: Parameters) extends Module
  with UniformPrintfs {
  override lazy val io = IO(new CallNodeIO(argTypes, retTypes)(p))
  override val printfSigil = "Node (Call) ID: " + ID + " "

  // Combine individually decoupled enable and data into single decoupled call
  val CombineIn = Module(new CombineCall(argTypes))
  CombineIn.io.In <> io.In
  io.Out <> CombineIn.io.Out

  // Split return enable and arguments into individually decoupled enable and data
  val SplitOut = Module(new SplitCall(retTypes))
  SplitOut.io.In <> io.retIn
  io.callOut <> SplitOut.io.Out

  when(CombineIn.io.Out.fire) {
    printfInfo("Output fired")
  }
}
