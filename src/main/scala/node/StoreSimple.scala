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

import utility.UniformPrintfs


// Design Doc
//////////
/// DRIVER ///
/// 1. Memory response only available atleast 1 cycle after request
//  2. Need registers for pipeline handshaking e.g., _valid, 
// _ready need to latch ready and valid signals. 
//////////



class StoreSimpleIO(NumPredMemOps :Int, 
                NumSuccMemOps : Int, 
                NumOuts:Int)(implicit p: Parameters) extends HandShakingIO(NumPredMemOps, NumSuccMemOps, NumOuts) {
   // Node specific IO
    // GepAddr: The calculated address comming from GEP node
    val GepAddr = Flipped(Decoupled(new DataBundle))
    // Store data.
    val inData = Flipped(Decoupled(UInt(xlen.W)))
    // Memory request
    val memReq  = Decoupled(new WriteReq())
    // Memory response.
    val memResp = Input(Flipped(new WriteResp()))
}

/**
 * @brief Store Node. Implements store operations
 * @details [long description]
 * 
 * @param NumPredMemOps [Number of predicate memory operations]
 */
class StoreSimpleNode(NumPredMemOps :Int, 
                NumSuccMemOps : Int, 
                NumOuts:Int, 
                Typ:UInt = MT_W, ID :Int)
                (implicit p: Parameters) 
              extends HandShaking(NumPredMemOps,NumSuccMemOps,NumOuts,ID)(p) {

  // Set up StoreSimpleIO
  override lazy val io = IO(new StoreSimpleIO(NumPredMemOps,NumSuccMemOps,NumOuts))
  
  // Printf debugging
  override val printfSigil = "Store ID: " + ID
  

/*=============================================
=            Register declarations            =
=============================================*/

  // OP Inputs
  val addr_R = RegInit(DataBundle.default)
  

  val data_R = RegInit(0.U(xlen.W))
  val data_valid_R = RegInit(false.B)


  // State machine
  val s_idle :: s_RECEIVING  :: s_Done :: Nil = Enum(3)
  val state = RegInit(s_idle)



   

  //Initialization READY-VALIDs for GepAddr and Predecessor memory ops
  io.GepAddr.ready      := ~addr_R.valid
  io.inData.ready       := ~data_valid_R


  // ACTION: GepAddr
  io.GepAddr.ready      := ~addr_R.valid
  when(io.GepAddr.fire()) {
    addr_R.valid := io.GepAddr.valid
    addr_R.data := io.GepAddr.bits.data
    addr_R.predicate := io.GepAddr.bits.predicate
  }

  // ACTION: inData
  when(io.inData.fire()) {
    // Latch the data
    data_R       := io.inData.bits
    // Set data valid
    data_valid_R := true.B
  }

    // Wire up Outputs
  for (i <- 0 until NumOuts) {
     io.Out(i).bits  := data_R
   }

  // ACTION:  Memory request
  //  Check if address is valid and data has arrive and predecessors have completed. 
  val mem_req_fire = addr_R.valid & IsPredValid() & data_valid_R

  // Outgoing Address Req -> 
  io.memReq.bits.address := addr_R.data
  io.memReq.bits.node := nodeID_R
  io.memReq.bits.data := data_R
  io.memReq.bits.Typ  := Typ

  // ACTION: Memory Request
  // -> Send memory request
  when((state === s_idle) && (mem_req_fire))
  {
    io.memReq.valid := true.B
  }

  //  ACTION: Arbitration ready
  when((state === s_idle) && (io.memReq.ready === true.B))
  {
    state := s_RECEIVING
  }
 
  //  ACTION:  <- Incoming Data  
  when(state === s_RECEIVING && io.memResp.valid)
  {
    // Set output to valid
    ValidSucc()
    ValidOut()
    state := s_Done
  }

    printfInfo("State : %x Dat %x", state, data_R)

  //  ACTION: <- Check Out READY and Successors READY 
  when (state === s_Done)
  {
    // When successors are complete and outputs are ready you can reset.
    // data already valid and would be latched in this cycle.
    val complete = IsSuccReady() & IsOutReady()

    when(complete)
    {
      // Clear all the valid states.
      // Reset address
       addr_R.valid := false.B
      // Reset data.
       data_valid_R := false.B
      // Clear all other state
      InvalidPred()
      // Reset state.
       state := s_idle

    }
  }
}

