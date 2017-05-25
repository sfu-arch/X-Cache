package node

/**
  * Created by nvedula on 15/5/17.
  */

import chisel3._
import chisel3.util._
import org.scalacheck.Prop.False

import config._
import interfaces._


//TODO parametrize NumMemOp and ID
//
abstract class LoadIO(val NumMemOP :Int = 1, val ID :Int = 0)
                     (implicit val p: Parameters) extends Module with CoreParams{

  val io = IO(new Bundle {
    // gepAddr: The calculated address comming from GEP node
    val gepAddr = Flipped(Decoupled(UInt(xlen.W)))

    //Bool data from other memory ops
    // using Handshaking protocols
    val predMemOp = Vec(NumMemOP, Flipped(Decoupled(UInt(1.W))))

    //Memory interface
    val memReq  = Decoupled(new ReadReq())
    val memResp = Input(Flipped(new ReadResp()))

    val memOpAck  = new RvAckIO()

  })
}


class   LoadNode(implicit p: Parameters) extends LoadIO()(p){

  //---------------------------------------------------------------------------------
  val nodeID_reg = RegInit(ID.U)
  val addr_reg = RegInit(0.U(xlen.W))
  val addr_valid_reg = RegInit(false.B)

  // Initialization registers to send ready signals
  // to all inputs to accept input nodes
  val init1_reg = RegInit(false.B)

  //TODO Make init3_reg a Vector of registers activated based on input predMemOp
  //  val init3_reg = RegInit(false.B)
  val init3_reg = RegInit(Vec(Seq.fill(NumMemOP)(false.B)))

  // Status Register - If src mem-ops done execution
  //  val in3_done_reg = RegInit(false.B)
  val in3_done_reg = RegInit(Vec(Seq.fill(NumMemOP)(false.B)))


  //Mem ready for response
  val memresp_ready_reg = RegInit(false.B)
  val data_resp_valid   = RegInit(false.B)
  val data_reg = RegInit(0.U(xlen.W))


  //Initialization
  io.gepAddr.ready      := ~init1_reg
//  io.predMemOp(0).ready := ~init3_reg
  for( w <- 0 until NumMemOP) {
    io.predMemOp(w).ready := ~init3_reg(w)
  }

  //---------------------------------------------------------------------------------


  printf(p"Ld Node: GepAddr.valid: ${io.gepAddr.valid} " +
    p" GepAddr.ready: ${io.gepAddr.ready} GepAddr.bits: ${io.gepAddr.bits} \n")
  printf(p"Ld Node: addr_reg: ${addr_reg} \n")

  //Rules for gepAddr
  when(io.gepAddr.fire()) {
    //    printf(p"\n --------------- gepAddr. fire  --------------------\n")
    addr_valid_reg := true.B
    addr_reg := io.gepAddr.bits
    init1_reg := true.B
  }


  //Rules for predMemOp(0)
  for( i <- 0 until NumMemOP) {
    when(io.predMemOp(i).fire()) {

      //    printf(p"\n ---------------- predMemOp(0). fire -------------------\n")
      in3_done_reg(i) := true.B
      init3_reg(i) := true.B
    }
  }

  //-----------------------------------

  //Rules for Sending address and data to Memory
  // Note Memreq_addr.ready and Memreq_data.ready are connected
  // Store Node cannot send the data to memory unless all its predecessors are done: in3 in this case

  val mem_req_fire = addr_valid_reg &  init1_reg & in3_done_reg.asUInt.andR & init1_reg.asUInt.andR
  io.memReq.valid := mem_req_fire
  io.memReq.bits.address := addr_reg
  io.memReq.bits.node := nodeID_reg



  when(io.memReq.fire()) {

    memresp_ready_reg := true.B
    addr_valid_reg := false.B

    for(i <- 0 until NumMemOP) {
      in3_done_reg(0) := false.B
    }
    //    printf(p"\n ------------------ Mem Request Sent ------------------------- \n")
  }

  //Once the request is sent to memory, be ready to receive the response back
  val resp_fire = io.memResp.valid & memresp_ready_reg

  when( resp_fire) {

    data_resp_valid := true.B
    data_reg := io.memResp.data
    //    printf(p"\n ------------------- Mem Response Received ---------------------\n")
    //Reset other registers
    memresp_ready_reg := false.B
  }



  //-----------------------------------
  //when all source memops are done executing - in3_done_reg
  // and ack is received from memory and next node is ready
  // then send Output as valid


  io.memOpAck.valid := data_resp_valid
  //  io.memOpAck.bits := 1.U

  //TODO In Case of pipelining StoreNode
  //TODO Once you know you need to reset StoreNode Uncomment below rule

  val memopack_fire = io.memOpAck.ready & io.memOpAck.valid
  when(memopack_fire) {

    data_resp_valid := false.B
    init1_reg := false.B
    for ( w <- 0 until NumMemOP) {
      init3_reg(w) := false.B
    }

    //    printf(p"\n ------------------- Iteration Over ---------------------\n")
  }


}
