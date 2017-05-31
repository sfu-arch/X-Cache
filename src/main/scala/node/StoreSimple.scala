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
//  2. Need registers for pipeline handshaking e.g., _valid, 
// _ready need to latch ready and valid signals. 
//////////


//TODO parametrize NumPredMemOps and ID
//
abstract class StoreSimpleIO(val NumPredMemOps :Int = 1, val NumSuccMemOps : Int = 1, val NumOuts : Int = 1, val Typ:UInt = MT_W, val ID :Int = 0)
                     (implicit val p: Parameters) extends Module with CoreParams{

  val io = IO(new Bundle {
    // GepAddr: The calculated address comming from GEP node
    val GepAddr = Flipped(Decoupled(UInt(xlen.W)))

    // Store data.
    val inData = Flipped(Decoupled(UInt(xlen.W)))


    //Bool data from other memory ops
    // using Handshaking protocols
    // The valid and ready signals cannot be floating.
    val PredMemOp = Vec(NumPredMemOps, Flipped(new RvAckIO()))
    val SuccMemOp = Vec(NumSuccMemOps, new RvAckIO())

    //Memory interface
    // Memory request
    val memReq  = Decoupled(new WriteReq())
    // Memory response.
    // Response should always be delayed by atleast one cycle
    val memResp = Input(Flipped(new WriteResp()))

    val Out   = Vec(NumOuts, Decoupled(UInt(xlen.W))) 
    })
}


class StoreSimpleNode(NumPredMemOps :Int = 1, NumSuccMemOps : Int = 1, NumOuts:Int = 1, Typ:UInt = MT_W, ID :Int)(implicit p: Parameters) extends StoreSimpleIO(NumPredMemOps,NumSuccMemOps,NumOuts,Typ,ID)(p){

  // Extra information
  val token  = RegInit(0.U)
  val nodeID_reg = RegInit(ID.U)
  // OP Inputs
  val addr_reg = RegInit(0.U(xlen.W))
  val addr_valid_R = RegInit(false.B)

  val data_R = RegInit(0.U(xlen.W))
  val data_valid_R = RegInit(false.B)


  // predessor memory ops. whether they are valid.
  val pred_valid_R =  RegInit(Vec(Seq.fill(NumPredMemOps)(false.B)))
 
  // Output Handshaking
  val succ_ready_R =  RegInit(Vec(Seq.fill(NumSuccMemOps)(false.B)))
  val succ_valid_R =  RegInit(Vec(Seq.fill(NumSuccMemOps)(false.B)))
  
  val out_ready_R  =  RegInit(Vec(Seq.fill(NumOuts)(false.B)))
  val out_valid_R  =  RegInit(Vec(Seq.fill(NumOuts)(false.B)))
  // State machine
  val s_idle :: s_RECEIVING  :: s_Done :: Nil = Enum(3)
  val state = RegInit(s_idle)

 
  //Initialization READY-VALIDs for GepAddr and Predecessor memory ops
  io.GepAddr.ready      := ~addr_valid_R
  io.inData.ready       := ~data_valid_R

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

  // Wire up Successors READYs and VALIDs
  for (i <- 0 until NumSuccMemOps) {
    succ_ready_R(i) := io.SuccMemOp(i).ready 
    io.SuccMemOp(i).valid := succ_valid_R(i)
    when(io.SuccMemOp(i).fire())
    {
      succ_valid_R(i) := false.B
    }
  }

  // Wire up OUT READYs and VALIDs
  for (i <- 0 until NumOuts) {
    // Detecting when to reset.
    out_ready_R(i) := io.Out(i).ready 
  
    // Propagating output
    io.Out(i).bits  := data_R
    io.Out(i).valid := out_valid_R(i)
    when(io.Out(i).fire())
    {
      out_valid_R(i) := false.B
    }
  }


  // ACTIONS
  // printf(p"State: ${state} Output: ${io.Out(0)}\n")    

  // ACTION: GepAddr
  when(io.GepAddr.fire()) {
    addr_valid_R := io.GepAddr.valid
    addr_reg := io.GepAddr.bits
    //    printf(p"\n --------------- GepAddr Fire.  --------------------\n")
    // printf(p"Ld Node: GepAddr.valid: ${io.GepAddr.valid} " +
    // p" GepAddr.ready: ${io.GepAddr.ready} GepAddr.bits: ${io.GepAddr.bits} \n")
    // printf(p"Ld Node: addr_reg: ${addr_reg} \n")    
  }

  // ACTION: inData
  when(io.inData.fire()) {
    //    printf(p"\n --------------- inData. fire  -------------------\n")
    // Latch the data
    data_R       := io.inData.bits
    // Set data valid
    data_valid_R := true.B
  }

  // ACTION:  Memory request
  //  Check if address is valid and data has arrive and predecessors have completed. 
  val mem_req_fire = addr_valid_R & pred_valid_R.asUInt.andR & data_valid_R
  // If idle, and mem-req is ready to fire. Fire it to memory system! Deactivate if state changes
  // io.memReq.valid := (state === s_idle) & mem_req_fire

  // Outgoing Address Req -> 
  io.memReq.bits.address := addr_reg
  io.memReq.bits.node := nodeID_reg
  io.memReq.bits.data := data_R
  io.memReq.bits.Typ  := Typ

  // ACTION: Memory Request
  // -> Send memory request
  when((state === s_idle) && (mem_req_fire))
  {
    io.memReq.valid := true.B
  }

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
 
   printf(p"State: ${state} Output: ${io.Out(0)}\n")    

  // Data detected only one cycle later. 
  // Memory should supply only one cycle after arbitration.
  //  ACTION:  <- Incoming Data  
  when(state === s_RECEIVING && io.memResp.valid)
  {
    // Move to Done state
    state := s_Done

    // printf(p"s_RECEIVING: Mem Resp: ${io.memResp.data} \n")    
    succ_valid_R :=  Fill(NumSuccMemOps,1.U).toBools
    out_valid_R  :=  Fill(NumOuts,1.U).toBools
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
      pred_valid_R :=  Fill(NumPredMemOps,0.U).toBools
     // Clear all other state.
    }
  }
}

