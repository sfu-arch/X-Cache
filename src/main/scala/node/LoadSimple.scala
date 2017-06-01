package node

/**
  * Created by nvedula on 15/5/17.
  */

import chisel3._
import chisel3.util._
import org.scalacheck.Prop.False

import config._
import interfaces._
import Constants._

// Design Doc
//////////
/// DRIVER ///
/// 1. Memory response only available atleast 1 cycle after request
//  2. Handshaking has to be done with registers.
//////////

class LoadSimpleIO()(implicit p: Parameters) extends HandShakingIO {
    // Node specific IO
    // GepAddr: The calculated address comming from GEP node
    val GepAddr = Flipped(Decoupled(UInt(xlen.W)))

    // Memory request
    val memReq  = Decoupled(new ReadReq())

    // Memory response.
    val memResp = Input(Flipped(new ReadResp()))

}

class HandShaking (val NumPredMemOps :Int = 1, val NumSuccMemOps : Int = 1, val NumOuts : Int = 1, val ID :Int = 0) (implicit val p: Parameters) 
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

class LoadSimpleNode(NumPredMemOps :Int = 1, 
                NumSuccMemOps : Int = 1, 
                NumOuts:Int = 1, 
                Typ:UInt = MT_W, ID :Int)
                (implicit p: Parameters) 
              extends HandShaking(NumPredMemOps,NumSuccMemOps,NumOuts,ID)(p) {

  override lazy val io = IO(new LoadSimpleIO())

  // OP Inputs
  val addr_R = RegInit(0.U(xlen.W))
  val addr_valid_R = RegInit(false.B)
  // Memory Response
  val data_R = RegInit(0.U(xlen.W))
  val data_valid_R = RegInit(0.U(xlen.W))

  // State machine
  val s_idle :: s_RECEIVING  :: s_Done :: Nil = Enum(3)
  val state = RegInit(s_idle)

  val ReqValid     = RegInit(false.B)


  io.GepAddr.ready      := ~addr_valid_R
  when(io.GepAddr.fire()) {
    addr_valid_R := io.GepAddr.valid
    addr_R := io.GepAddr.bits
    //    printf(p"\n --------------- GepAddr Fire.  --------------------\n")
    // printf(p"Ld Node: GepAddr.valid: ${io.GepAddr.valid} " +
    // p" GepAddr.ready: ${io.GepAddr.ready} GepAddr.bits: ${io.GepAddr.bits} \n")
    // printf(p"Ld Node: addr_R: ${addr_R} \n")    
  }

  // Wire up Outputs
  for (i <- 0 until NumOuts) {
     io.Out(i).bits  := data_R
   }

  // ACTION:  Memory request
  //  Check if address is valid and predecessors have completed. 
  val mem_req_fire = addr_valid_R & IsPredFire()
  // If idle, and mem-req is ready to fire. Fire it to memory system! Deactivate if state changes
  io.memReq.valid := (state === s_idle) & mem_req_fire

  // ACTION: Outgoing Address Req -> 
  io.memReq.bits.address := addr_R
  io.memReq.bits.node := nodeID_R

  //  ACTION: Arbitration ready
  //   <- Incoming memory arbitration  
  when((state === s_idle) && (io.memReq.ready === true.B))
  {
    state := s_RECEIVING
  }

  // Data detected only one cycle later. 
  // Memory should supply only one cycle after arbitration.
  //  ACTION:  <- Incoming Data  
  when(state === s_RECEIVING && io.memResp.valid)
  {
    // Set data output registers 
   data_R       := io.memResp.data
   FireSucc()
   FireOut()
   // Completion state.
   state := s_Done

  }

  //  ACTION: <- Check Out READY and Successors READY 
  when (state === s_Done)
  {
    // When successors are complete and outputs are ready you can reset.
    // data already valid and would be latched in this cycle.
    val complete = IsSuccFire() & IsOutFire()
    when(complete)
    {
      // Clear all the valid states.
      // Reset address
       addr_valid_R := false.B
      // Reset data.
       data_valid_R := false.B
      // Reset state.
       state := s_idle
      // indicate completion to predecessors. 
       ClearPred()
     // Clear all other state.

    }
  }
   printf(p"State: ${state} Output: ${io.Out(0)} Valid: ${out_valid_R} \n")  

}
