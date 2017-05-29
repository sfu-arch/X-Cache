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
//  2. 
//////////


//TODO parametrize NumPredMemOps and ID
//
abstract class LoadSimpleIO(val NumPredMemOps :Int = 1, val NumSuccMemOps : Int = 1, val NumOuts : Int = 1, val Typ:UInt = MT_W, val ID :Int = 0)
                     (implicit val p: Parameters) extends Module with CoreParams{

  val io = IO(new Bundle {
    // GepAddr: The calculated address comming from GEP node
    val GepAddr = Flipped(Decoupled(UInt(xlen.W)))

    //Bool data from other memory ops
    // using Handshaking protocols
    // The valid and ready signals cannot be floating.
    val PredMemOp = Vec(NumPredMemOps, Flipped(new RvAckIO()))
    val SuccMemOp = Vec(NumSuccMemOps, new RvAckIO())

    //Memory interface
    // Memory request
    val memReq  = Decoupled(new ReadReq())
    // Memory response.
    // Response should always be delayed by atleast one cycle
    val memResp = Input(Flipped(new ReadResp()))

    val Out   = Vec(NumOuts, Decoupled(UInt(xlen.W))) 
    })
}


class LoadSimpleNode(NumPredMemOps :Int ,NumSuccMemOps : Int,NumOuts:Int,Typ:UInt, ID :Int)(implicit p: Parameters) extends LoadSimpleIO(NumPredMemOps,NumSuccMemOps,NumOuts,Typ,ID)(p){

  // Extra information
  val token  = RegInit(0.U)
  val nodeID_reg = RegInit(ID.U)
 
  val addr_reg = RegInit(0.U(xlen.W))
  val addr_valid_R = RegInit(false.B)

  // predessor memory ops. whether they are valid.
  val pred_valid_R =  RegInit(Vec(Seq.fill(NumPredMemOps)(false.B)))
  val succ_ready_R =  RegInit(Vec(Seq.fill(NumSuccMemOps)(false.B)))
  val out_ready_R        =  RegInit(Vec(Seq.fill(NumOuts)(false.B)))

  val s_idle :: s_RECEIVING  :: s_Done :: Nil = Enum(3)
  val state = Reg(init = s_idle)

  val ReqValid     = RegInit(false.B)


  //Mem ready for response
  val memresp_ready_reg = RegInit(false.B)
  val data_R = RegInit(0.U(xlen.W))
  val data_valid_R = RegInit(0.U(xlen.W))


  //Initialization READY-VALIDs for GepAddr and Predecessor memory ops
  io.GepAddr.ready      := ~addr_valid_R

  for (i <- 0 until NumPredMemOps) {
    io.PredMemOp(i).ready := ~pred_valid_R(i)
    when(io.PredMemOp(i).fire()) {
      pred_valid_R(i) := io.PredMemOp(i).valid      
    }
  }
 


  /*
     Registers needed here cause the outputs are vectors.
     We need to wire up the io.out(i).ready to a ready_R(i)
     Need to perform a andR unfortuntely can't do io.out(i).ready.andR
   */

  // Wire up Successors READYs
  for (i <- 0 until NumSuccMemOps) {
    succ_ready_R(i) := io.SuccMemOp(i).ready 
  }

  // Wire up OUT READYs
  for (i <- 0 until NumOuts) {
    out_ready_R(i) := io.Out(i).ready 
  }

  // Wire up Sucessor VALIDs
  for (i <- 0 until NumSuccMemOps) {
    io.SuccMemOp(i).valid := data_valid_R
  }

  // Wire up Output VALIDs
  for (i <- 0 until NumOuts) {
    io.Out(i).bits  := data_R
    io.Out(i).valid := data_valid_R
  }

  // ACTIONS

  // ACTION: GepAddr
  when(io.GepAddr.fire()) {
    
    addr_valid_R := io.GepAddr.valid
    addr_reg := io.GepAddr.bits

    //    printf(p"\n --------------- GepAddr Fire.  --------------------\n")
    // printf(p"Ld Node: GepAddr.valid: ${io.GepAddr.valid} " +
    // p" GepAddr.ready: ${io.GepAddr.ready} GepAddr.bits: ${io.GepAddr.bits} \n")
    // printf(p"Ld Node: addr_reg: ${addr_reg} \n")    
  }

  // ACTION:  Memory request
  //  Check if address is valid and predecessors have completed. 
  val mem_req_fire = addr_valid_R & pred_valid_R.asUInt.andR 
  // If idle, and mem-req is ready to fire. Fire it to memory system! Deactivate if state changes
  io.memReq.valid := (state === s_idle) & mem_req_fire

  // Outgoing Address Req -> 
  io.memReq.bits.address := addr_reg
  io.memReq.bits.node := nodeID_reg

   
  //  ACTION: Arbitration ready
  //   <- Incoming memory arbitration  
  when((state === s_idle) && (io.memReq.ready === true.B))
  {
    // This may not print. For some reason print only prints in the next cycle
    // after a condition is true i.e.,
    // Request has been arbitrated start receiving data.
    state := s_RECEIVING

     // printf(p"s_SENDING ${state} \n")    
  }
 
  // Data detected only one cycle later. 
  // Memory should supply only one cycle after arbitration.
  //  ACTION:  <- Incoming Data  
  when(state === s_RECEIVING && io.memResp.valid)
  {
    // Set data output registers 
   data_R       := io.memResp.data
   data_valid_R := true.B
   // Completion state.
   state := s_Done
   // printf(p"s_RECEIVING: Mem Resp: ${io.memResp.data} \n")    
  }

  //  ACTION: <- Check Out READY and Successors READY 
  when (state === s_Done)
  {

    // When successors are complete and outputs are ready you can reset.
    // data already valid and would be latched in this cycle.
    val complete = succ_ready_R.asUInt.andR & out_ready_R.asUInt.andR
  
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
      for ( w <- 0 until NumPredMemOps) {
       pred_valid_R(w) := false.B
     }
     // Clear all other state.

    }
  }
}

