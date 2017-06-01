package node

import chisel3._
import chisel3.util._
import org.scalacheck.Prop.False

import config._
import interfaces._
import Constants._


class HandShaking (val NumPredMemOps :Int, val NumSuccMemOps : Int, val NumOuts : Int, val ID :Int) (implicit val p: Parameters) 
 extends Module with CoreParams {

  lazy val io = IO(new HandShakingIO(NumPredMemOps,NumSuccMemOps,NumOuts))

  // Extra information
  val token  = RegInit(0.U)
  val nodeID_R = RegInit(ID.U)

  // Predessor Handshaking
  val pred_valid_R =  RegInit(Vec(Seq.fill(NumPredMemOps)(false.B)))
 
  // Successor Handshaking. Registers needed
  val succ_ready_R =  RegInit(Vec(Seq.fill(NumSuccMemOps)(false.B)))
  val succ_valid_R =  RegInit(Vec(Seq.fill(NumSuccMemOps)(false.B)))
  // Output Handshaking  
  val out_ready_R  =  RegInit(Vec(Seq.fill(NumOuts)(false.B)))
  val out_valid_R  =  RegInit(Vec(Seq.fill(NumOuts)(false.B)))

////////////////////////////////////  WIRING /////////////////////////////////

  // Wire up Successors READYs and VALIDs
  for (i <- 0 until NumSuccMemOps) {
    io.SuccMemOp(i).valid := succ_valid_R(i)
    when(io.SuccMemOp(i).fire())
    {
      succ_ready_R(i) := io.SuccMemOp(i).ready 
      succ_valid_R(i) := false.B
    }
  }

  // Wire up OUT READYs and VALIDs
  for (i <- 0 until NumOuts) {
    io.Out(i).valid := out_valid_R(i)
    when(io.Out(i).fire())
    {
      // Detecting when to reset
      out_ready_R(i) := io.Out(i).ready 
      // Propagating output
      out_valid_R(i) := false.B
    }
  }
  // Wire up Predecessor READY and VALIDs
  for (i <- 0 until NumPredMemOps) {
    io.PredMemOp(i).ready := ~pred_valid_R(i)
    when(io.PredMemOp(i).fire()) {
      pred_valid_R(i) := io.PredMemOp(i).valid      
    }
  }

////////////////////////////////////  Actions and Checks /////////////////////////////////
  // Check if Predecssors have fired
  def IsPredFire(): Bool = { 
    pred_valid_R.asUInt.andR 
  }
  // Fire Predecessors
  def FirePred() = { 
    pred_valid_R :=  Fill(NumPredMemOps,1.U).toBools
  }
  // Clear predessors
  def ClearPred() = { 
    pred_valid_R :=  Fill(NumPredMemOps,0.U).toBools
  }
  // Successors
  def IsSuccFire(): Bool = { 
    succ_valid_R.asUInt.andR
  }
  def FireSucc() = { 
    succ_valid_R :=  Fill(NumSuccMemOps,1.U).toBools
  }
  def ClearSucc() = { 
    succ_valid_R :=  Fill(NumSuccMemOps,0.U).toBools
  }
  // OUTs
   def IsOutFire(): Bool = { 
    out_valid_R.asUInt.andR
  }
  def FireOut()  = {
   out_valid_R  :=  Fill(NumOuts,1.U).toBools
  }
  def ClearOut()  = {
   out_valid_R  :=  Fill(NumOuts,0.U).toBools
  }
}