package node

import chisel3._
import chisel3.util._
import org.scalacheck.Prop.False

import config._
import interfaces._
import Constants._
import utility.UniformPrintfs


/**
 * @note
 * There are three types of handshaking:
 * 1)   There is no ordering -> (No PredOp/ No SuccOp)
 *    it has only vectorized output
 *    @note HandshakingIONPS
 *    @todo Put special case for singl output vs two outputs
 *
 *  2)  There is ordering    -> (PredOp/ SuccOp)
 *    vectorized output/succ/pred
 *    @note HandshakingIOPS
 *
 *  3)  There is ordering + vectorized input
 *    @note HandshakingIO
 *
 */


/**
 * @note Type1
 * Handshaking IO with no ordering.
 * @note IO Bundle for Handshaking
 * @param NumPredOps  Number of parents
 * @param NumSuccOps  Number of successors
 * @param NumOuts       Number of outputs
 *
 */
class HandShakingIONPS(val NumOuts: Int)
  (implicit p: Parameters)
    extends CoreBundle()(p) {
      // Predicate enable
      val enable = Flipped(Decoupled(Bool()))
      // Output IO
      val Out    = Vec(NumOuts   , Decoupled(DataBundle.default))
}



/**
 * @note Type2
 * Handshaking IO.
 * @note IO Bundle for Handshaking
 * PredOp: Vector of RvAckIOs
 * SuccOp: Vector of RvAckIOs
 * Out      : Vector of Outputs
 * @param NumPredOps  Number of parents
 * @param NumSuccOps  Number of successors
 * @param NumOuts       Number of outputs
 *
 *
 */
class HandShakingIOPS(val NumPredOps: Int,
  val NumSuccOps: Int,
  val NumOuts: Int)
  (implicit p: Parameters)
  extends CoreBundle()(p) {
    // Predicate enable
    val enable = Flipped(Decoupled(Bool()))
    // Predecessor Ordering
    val PredOp = Vec(NumPredOps, Flipped(Decoupled(new AckBundle)))
    // Successor Ordering
    val SuccOp = Vec(NumSuccOps, Decoupled(new AckBundle()))
    // Output IO
    val Out    = Vec(NumOuts   , Decoupled(DataBundle.default))
}


/**
 * @brief Handshaking between data nodes with no ordering.
 * @details Sets up base registers and hand shaking registers
 * @param NumOuts Number of outputs
 * @param ID      Node id
 * @return        Module
 */

class HandShakingNPS(val NumOuts: Int,
  val ID: Int)
  (implicit val p: Parameters)
  extends Module with CoreParams with UniformPrintfs {

    lazy val io = IO(new HandShakingIONPS(NumOuts))

    /*=================================
    =            Registers            =
    =================================*/
    // Extra information
    val token     = RegInit(0.U)
    val nodeID_R  = RegInit(ID.U)

    // Enable
    val enable_R       = RegInit(true.B)
    val enable_valid_R = RegInit(false.B)

    // Output Handshaking
    val out_ready_R = RegInit(Vec(Seq.fill(NumOuts)(false.B)))
    val out_valid_R = RegInit(Vec(Seq.fill(NumOuts)(false.B)))


    /*============================*
     *           Wiring           *
     *============================*/

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

    // Wire up enable READY and VALIDs
    io.enable.ready := ~enable_valid_R
    when(io.enable.fire()) {
      enable_valid_R := io.enable.valid
      enable_R       := io.enable.bits
    }

    /*===================================*
     *            Helper Checks          *
     *===================================*/
    def IsEnable(): Bool = {
      enable_R
    }
    def IsEnableValid(): Bool = {
      enable_valid_R 
    }
    def ResetEnable() = {
      enable_valid_R := false.B
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
    def Reset() = {
    	out_ready_R    := Vec(Seq.fill(NumOuts) { false.B })
    	enable_valid_R := false.B
    }
}


/**
 * @brief Handshaking between data nodes.
 * @details Sets up base registers and hand shaking registers
 * @param NumPredOps Number of parents
 * @param NumSuccOps Number of successors
 * @param NumOuts Number of outputs
 * @param ID Node id
 * @return Module
 */

class HandShaking(val NumPredOps: Int,
  val NumSuccOps: Int,
  val NumOuts: Int,
  val ID: Int)
  (implicit val p: Parameters)
  extends Module with CoreParams with UniformPrintfs {

  lazy val io = IO(new HandShakingIOPS(NumPredOps, NumSuccOps, NumOuts))

  /*=================================
  =            Registers            =
  =================================*/
  // Extra information
  val token     = RegInit(0.U)
  val nodeID_R  = RegInit(ID.U)

  // Enable
  val enable_R       = RegInit(true.B)
  val enable_valid_R = RegInit(false.B)

  // Predecessor Handshaking
  val pred_valid_R  = RegInit(Vec(Seq.fill(NumPredOps)(false.B)))
  val pred_bundle_R = RegInit(Vec(Seq.fill(NumPredOps)(AckBundle.default)))

  // Successor Handshaking. Registers needed
  val succ_ready_R  = RegInit(Vec(Seq.fill(NumSuccOps)(false.B)))
  val succ_valid_R  = RegInit(Vec(Seq.fill(NumSuccOps)(false.B)))
  val succ_bundle_R = RegInit(Vec(Seq.fill(NumSuccOps)(AckBundle.default)))

  // Output Handshaking
  val out_ready_R = RegInit(Vec(Seq.fill(NumOuts)(false.B)))
  val out_valid_R = RegInit(Vec(Seq.fill(NumOuts)(false.B)))


  /*==============================
  =            Wiring            =
  ==============================*/
  // Wire up Successors READYs and VALIDs
  for (i <- 0 until NumSuccOps) {
    io.SuccOp(i).valid  := succ_valid_R(i)
    io.SuccOp(i).bits   := succ_bundle_R(i)
    when(io.SuccOp(i).fire()) {
      succ_ready_R(i) := io.SuccOp(i).ready
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
  for (i <- 0 until NumPredOps) {
    io.PredOp(i).ready := ~pred_valid_R(i)
    when(io.PredOp(i).fire()) {
      pred_valid_R(i)  := io.PredOp(i).valid
      pred_bundle_R(i) := io.PredOp(i).bits
    }
  }

  // Wire up enable READY and VALIDs
  io.enable.ready := ~enable_valid_R
  when(io.enable.fire()) {
    enable_valid_R  := io.enable.valid
    enable_R := io.enable.bits
  }

  /*=====================================
  =            Helper Checks            =
  =====================================*/
  def IsEnable(): Bool = {
    enable_R
  }
  def IsEnableValid(): Bool = {
    enable_valid_R 
  }
  def ResetEnable() = {
    enable_valid_R := false.B
  }

  // Check if Predecssors have fired
  def IsPredValid(): Bool = {
    pred_valid_R.asUInt.andR
  }
  // Fire Predecessors
  def ValidPred() = {
    pred_valid_R := Vec(Seq.fill(NumPredOps) { true.B })
  }
  // Clear predessors
  def InvalidPred() = {
    pred_valid_R := Vec(Seq.fill(NumPredOps) { false.B })
  }
  // Successors
  def IsSuccReady(): Bool = {
    succ_ready_R.asUInt.andR
  }
  def ValidSucc() = {
    succ_valid_R := Vec(Seq.fill(NumSuccOps) { true.B })
  }
  def InvalidSucc() = {
    succ_valid_R := Vec(Seq.fill(NumSuccOps) { false.B })
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
  def Reset() = {
  	pred_valid_R   := Vec(Seq.fill(NumPredOps) { false.B })
  	succ_ready_R   := Vec(Seq.fill(NumSuccOps) { false.B })
  	out_ready_R    := Vec(Seq.fill(NumOuts) { false.B })
  	enable_valid_R := false.B
  }
}

