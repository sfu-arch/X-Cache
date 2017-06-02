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

class LoadSimpleIO(NumPredMemOps :Int, 
                NumSuccMemOps : Int, 
                NumOuts:Int)(implicit p: Parameters) extends HandShakingIO(NumPredMemOps, NumSuccMemOps, NumOuts) {
    // Node specific IO
    // GepAddr: The calculated address comming from GEP node
    val GepAddr = Flipped(Decoupled(UInt(xlen.W)))

    // Memory request
    val memReq  = Decoupled(new ReadReq())

    // Memory response.
    val memResp = Input(Flipped(new ReadResp()))

}



class LoadSimpleNode(NumPredMemOps :Int, 
                NumSuccMemOps : Int, 
                NumOuts:Int, 
                Typ:UInt = MT_W, ID :Int)
                (implicit p: Parameters) 
              extends HandShaking(NumPredMemOps,NumSuccMemOps,NumOuts,ID)(p) {

  override lazy val io = IO(new LoadSimpleIO(NumPredMemOps,NumSuccMemOps,NumOuts))

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
