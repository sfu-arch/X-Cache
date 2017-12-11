package node

/**
  * Created by nvedula on 15/5/17.
  */

import chisel3._
import chisel3.util._
import org.scalacheck.Prop.False

import config._
import interfaces._
import utility.Constants._
import utility.UniformPrintfs


// Design Doc
//////////
/// DRIVER ///
/// 1. Memory response only available atleast 1 cycle after request
//  2. Handshaking has to be done with registers.
// @todo : This node will only receive one word. To handle doubles. Change handshaking logic
//////////

class LoadIO(NumPredOps: Int,
             NumSuccOps: Int,
             NumOuts: Int)(implicit p: Parameters)
  extends HandShakingIOPS(NumPredOps, NumSuccOps, NumOuts)(new DataBundle) {
  // GepAddr: The calculated address comming from GEP node
  val GepAddr = Flipped(Decoupled(new DataBundle))
  // Memory request
  val memReq = Decoupled(new ReadReq())
  // Memory response.
  val memResp = Input(Flipped(new ReadResp()))
}

class UnTypLoad(NumPredOps: Int,
                NumSuccOps: Int,
                NumOuts: Int,
                Typ: UInt = MT_W, ID: Int, RouteID: Int)(implicit p: Parameters)
  extends HandShaking(NumPredOps, NumSuccOps, NumOuts, ID)(new DataBundle)(p) {

  override lazy val io = IO(new LoadIO(NumPredOps, NumSuccOps, NumOuts))
  // Printf debugging
  override val printfSigil = "Node (LOAD) ID: " + ID + " "


  /*=============================================
  =            Registers                        =
  =============================================*/
  // OP Inputs
  val addr_R = RegInit(DataBundle.default)
  val addr_valid_R = RegInit(false.B)

  // Memory Response
  val data_R = RegInit(DataBundle.default)
  val data_valid_R = RegInit(false.B)

  // State machine
  val s_idle :: s_RECEIVING :: s_Done :: Nil = Enum(3)
  val state = RegInit(s_idle)

  /*============================================
  =            Predicate Evaluation            =
  ============================================*/

  val predicate = IsEnable()
  val start = addr_valid_R & IsPredValid & IsEnableValid()

  /*================================================
  =            Latch inputs. Wire up output            =
  ================================================*/

  io.GepAddr.ready := ~addr_valid_R
  when(io.GepAddr.fire()) {
    addr_R.data := io.GepAddr.bits.data
    //addr_R.valid := true.B
    addr_valid_R := true.B
  }

  // Wire up Outputs
  for (i <- 0 until NumOuts) {
    io.Out(i).bits := data_R
    io.Out(i).bits.predicate := predicate
  }

  io.memReq.valid := false.B
  io.memReq.bits.address := addr_R.data
  io.memReq.bits.node := nodeID_R
  io.memReq.bits.Typ := Typ
  io.memReq.bits.RouteID := RouteID.U
  /*=============================================
  =            ACTIONS (possibly dangerous)     =
  =============================================*/

  when(start & predicate) {
    // ACTION:  Memory request
    //  Check if address is valid and predecessors have completed.
    val mem_req_fire = addr_valid_R & IsPredValid()

    // ACTION: Outgoing Address Req ->
    when((state === s_idle) && (mem_req_fire)) {
      io.memReq.valid := true.B
    }

    //  ACTION: Arbitration ready
    //   <- Incoming memory arbitration
    when((state === s_idle) && (io.memReq.ready === true.B) && (io.memReq.valid === true.B)) {
      state := s_RECEIVING
    }

    // Data detected only one cycle later.
    // Memory should supply only one cycle after arbitration.
    //  ACTION:  <- Incoming Data
    when(state === s_RECEIVING && io.memResp.valid) {
      // Set data output registers
      data_R.data := io.memResp.data
      data_R.predicate := predicate
      //data_R.valid := true.B
      ValidSucc()
      ValidOut()
      // Completion state.
      state := s_Done
    }
  }.elsewhen(start && !predicate && state =/= s_Done) {
    ValidSucc()
    ValidOut()
    state := s_Done
  }
  /*===========================================
  =            Output Handshaking and Reset   =
  ===========================================*/

  //  ACTION: <- Check Out READY and Successors READY
  when(state === s_Done) {
    // When successors are complete and outputs are ready you can reset.
    // data already valid and would be latched in this cycle.
    val complete = IsSuccReady() & IsOutReady()
    when(complete) {
      // Clear all the valid states.
      // Reset address
      addr_R := DataBundle.default
      addr_valid_R := false.B
      // Reset data
      data_R := DataBundle.default
      data_valid_R := false.B
      // Reset state.
      Reset()
      // Reset state.
      state := s_idle
      printfInfo("Output fired")
    }
  }
  /*
  if (log == true && (comp contains "LOAD")) {
    val x = RegInit(0.U(xlen.W))
    x     := x + 1.U
  
    verb match {
      case "high"  => { }
      case "med"   => { }
      case "low"   => {
        printfInfo("Cycle %d : { \"Inputs\": {\"GepAddr\": %x},",x, (addr_R.valid))
        printf("\"State\": {\"State\": \"%x\", \"data_R(Valid,Data,Pred)\": \"%x,%x,%x\" },",state,data_R.valid,data_R.data,data_R.predicate)
        printf("\"Outputs\": {\"Out\": %x}",io.Out(0).fire())
        printf("}")
       }
      case everythingElse => {}
    }
  }
  */
}
