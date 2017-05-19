package node
/**
  * Created by nvedula on 11/5/17.
  */
import chisel3._
import chisel3.util._
import org.scalacheck.Prop.False

import config._
import interfaces._

//TODO parametrize NumMemOp, ID, and mask

abstract class StoreIO(val NumMemOP :Int = 1, val ID :Int = 0, val mask :Int = 1
                      )(implicit val p: Parameters) extends Module with CoreParams{

  val io = IO(new Bundle {
    // gepAddr: The calculated address coming from GEP node
    val gepAddr = Flipped(Decoupled(UInt(xlen.W)))
    val inData = Flipped(Decoupled(UInt(xlen.W)))
    //Bool data from other memory ops
    // using Handshaking protocols
    val predMemOp = Vec(NumMemOP, Flipped(Decoupled(UInt(1.W))))
    val memOpAck = Decoupled(UInt(1.W)) //TODO 0 bits

    //Memory interface
    val memReq  = Decoupled(new WriteReq())
    val memResp = Flipped(new WriteResp())

  })
}


class StoreNode(implicit p: Parameters) extends StoreIO()(p){

  val nodeID_reg = RegInit(ID.U)
  val mask_reg = RegInit(mask.U)
  val addr_reg = RegInit(0.U(xlen.W))
  val addr_valid_reg = RegInit(false.B)

  val data_reg = RegInit(0.U(xlen.W))
  val data_valid_reg = RegInit(false.B)

  // Status Register - If src mem-ops done execution
  val in3_done_reg = RegInit(false.B)

  // Initialization registers to send ready signals
  // to all inputs to accept input nodes
  val init1_reg = RegInit(false.B)
  val init2_reg = RegInit(false.B)

  //TODO Make init3_reg a Vector of registers activated based on input predMemOp
  val init3_reg = RegInit(false.B)


  //Mem ready for response
  val memresp_ready_reg = RegInit(false.B)
  val ack_reg = RegInit(false.B  )



  //Initialization
  io.gepAddr.ready      := ~init1_reg
  io.inData.ready       := ~init2_reg
  io.predMemOp(0).ready := ~init3_reg



  printf( p"\n StNODE: gepAddrValid ${io.gepAddr.valid} \n")
  printf( p"\n StNODE: gepAddrReady ${io.gepAddr.ready} \n")

  //-----------------------------------
  //Rules for gepAddr
  when(io.gepAddr.fire()) {
    printf(p"\n --------------- gepAddr. fire  --------------------\n")
    addr_valid_reg := true.B
    addr_reg := io.gepAddr.bits
    init1_reg := true.B
  }


  //Rules for inData
  when(io.inData.fire()) {

    printf(p"\n --------------- inData. fire  -------------------\n")
    data_valid_reg := true.B
    data_reg := io.inData.bits
    init2_reg := true.B
  }


  //Rules for predMemOp(0)
  when(io.predMemOp(0).fire()) {

    printf(p"\n ---------------- predMemOp(0). fire -------------------\n")
    in3_done_reg := true.B
    init3_reg := true.B
  }

  //-----------------------------------

  //Rules for Sending address and data to Memory
  // Note Memreq_addr.ready and Memreq_data.ready are connected
  // Store Node cannot send the data to memory unless all its predecessors are done: in3 in this case

  val mem_req_fire = data_valid_reg & addr_valid_reg & in3_done_reg & init1_reg & init2_reg & init3_reg
  io.memReq.valid := mem_req_fire
  io.memReq.bits.address := addr_reg
  io.memReq.bits.node := nodeID_reg
  io.memReq.bits.mask := mask_reg
  io.memReq.bits.data := data_reg

  when(io.memReq.fire()) {

    memresp_ready_reg := true.B
    addr_valid_reg := false.B
    data_valid_reg := false.B
    in3_done_reg := false.B
    printf(p"\n ------------------ Mem Request Sent ------------------------- \n")
  }

  //Once the request is sent to memory, be ready to receive the response back
  ack_reg := io.memResp.valid & memresp_ready_reg
  val resp_fire = io.memResp.valid & memresp_ready_reg

  when( resp_fire) {

    printf(p"\n ------------------- Mem Response Received ---------------------\n")
    //Reset other registers
    memresp_ready_reg := false.B
  }



  //-----------------------------------
  //when all source memops are done executing - in3_done_reg
  // and ack is received from memory and next node is ready
  // then send Output as valid


  io.memOpAck.valid := ack_reg
  io.memOpAck.bits := 1.U

  //TODO In Case of pipelining StoreNode
  //TODO Once you know you need to reset StoreNode Uncomment below rule
  when(io.memOpAck.fire()) {

    ack_reg := false.B
    init1_reg := false.B
    init2_reg := false.B
    init3_reg := false.B

    printf(p"\n ------------------- Iteration Over ---------------------\n")
  }


}
