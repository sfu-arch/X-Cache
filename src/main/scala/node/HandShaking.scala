package node

import chisel3._
import chisel3.util._
import org.scalacheck.Prop.False

import config._
import interfaces._
import Constants._
import utility.UniformPrintfs

/**
 * Handshaking IO.
 * @note IO Bundle for Handshaking
 * PredMemOp: Vector of RvAckIOs
 * SuccMemOp: Vector of RvAckIOs
 * Out      : Vector of Outputs
 * @param NumPredMemOps  Number of parents
 * @param NumSuccMemOps  Number of successors
 * @param NumOuts       Number of outputs
 *
 */
class HandShakingIO(val NumPredMemOps: Int,
  val NumSuccMemOps: Int,
  val NumOuts: Int)
  (implicit p: Parameters)
  extends CoreBundle()(p) {
  // Predicate enable
  val enable = Flipped(Decoupled(Bool()))
  // Predecessor Ordering
  val PredMemOp = Vec(NumPredMemOps, Flipped(Decoupled(new AckBundle)))
  // Successor Ordering
  val SuccMemOp = Vec(NumSuccMemOps, Decoupled(new AckBundle()))
  // Output IO
  val Out = Vec(NumOuts, Decoupled(DataBundle.default))
}

/**
 * @brief Handshaking between data nodes.
 * @details Sets up base registers and hand shaking registers
 * @param NumPredMemOps Number of parents
 * @param NumSuccMemOps Number of successors
 * @param NumOuts Number of outputs
 * @param ID Node id
 * @return Module
 */

class HandShaking(val NumPredMemOps: Int,
  val NumSuccMemOps: Int,
  val NumOuts: Int,
  val ID: Int)
  (implicit val p: Parameters)
  extends Module with CoreParams with UniformPrintfs {

  lazy val io = IO(new HandShakingIO(NumPredMemOps, NumSuccMemOps, NumOuts))

  /*=================================
  =            Registers            =
  =================================*/
  // Extra information
  val token = RegInit(0.U)
  val nodeID_R = RegInit(ID.U)
  val enable = RegInit(true.B)

  // Predecessor Handshaking
  val pred_valid_R = RegInit(Vec(Seq.fill(NumPredMemOps)(false.B)))
  val pred_bundle_R = RegInit(Vec(Seq.fill(NumPredMemOps)(AckBundle.default)))

  // Successor Handshaking. Registers needed
  val succ_ready_R = RegInit(Vec(Seq.fill(NumSuccMemOps)(false.B)))
  val succ_valid_R = RegInit(Vec(Seq.fill(NumSuccMemOps)(false.B)))
  val succ_bundle_R = RegInit(Vec(Seq.fill(NumSuccMemOps)(AckBundle.default)))

  // Output Handshaking
  val out_ready_R = RegInit(Vec(Seq.fill(NumOuts)(false.B)))
  val out_valid_R = RegInit(Vec(Seq.fill(NumOuts)(false.B)))


  /*==============================
  =            Wiring            =
  ==============================*/
  // Wire up Successors READYs and VALIDs
  for (i <- 0 until NumSuccMemOps) {
    io.SuccMemOp(i).valid  := succ_valid_R(i)
    io.SuccMemOp(i).bits   := succ_bundle_R(i)
    when(io.SuccMemOp(i).fire()) {
      succ_ready_R(i) := io.SuccMemOp(i).ready
      succ_valid_R(i) := false.B
    }
  }

  // Wire up OUT READYs and VALIDs
  for (i <- 0 until NumOuts) {
    io.Out(i).valid := out_valid_R(i)
    when(io.Out(i).fire()) {
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
      pred_valid_R(i)  := io.PredMemOp(i).valid
      pred_bundle_R(i) := io.PredMemOp(i).bits
    }
  }

  /*=====================================
  =            Helper Checks            =
  =====================================*/
  def Isenable(): Bool = {
    enable
  }

  // Check if Predecssors have fired
  def IsPredValid(): Bool = {
    pred_valid_R.asUInt.andR
  }
  // Fire Predecessors
  def ValidPred() = {
    pred_valid_R := Vec(Seq.fill(NumPredMemOps) { true.B })
  }
  // Clear predessors
  def InvalidPred() = {
    pred_valid_R := Vec(Seq.fill(NumPredMemOps) { false.B })
  }
  // Successors
  def IsSuccReady(): Bool = {
    succ_ready_R.asUInt.andR
  }
  def ValidSucc() = {
    succ_valid_R := Vec(Seq.fill(NumSuccMemOps) { true.B })
  }
  def InvalidSucc() = {
    succ_valid_R := Vec(Seq.fill(NumSuccMemOps) { false.B })
  }
  // OUTs
  def IsOutReady(): Bool = {
    out_ready_R.asUInt.andR
  }
  def ValidOut() = {
    out_valid_R := Vec(Seq.fill(NumOuts) { true.B })
  }
  def InvalidOut() = {
    out_valid_R := Vec(Seq.fill(NumOuts) { false.B })
  }
  def ResetSuccAndOutReadys() = {
    succ_ready_R := Vec(Seq.fill(NumSuccMemOps) { false.B })
    out_ready_R := Vec(Seq.fill(NumOuts) { true.B })
  }
}
