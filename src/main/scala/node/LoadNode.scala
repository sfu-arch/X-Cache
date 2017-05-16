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
abstract class LoadIO(val NumMemOP :Int = 1, val ID :Int = 0)(implicit val p: Parameters) extends Module with CoreParams{

  val io = IO(new Bundle {
    // gepAddr: The calculated address comming from GEP node
    val gepAddr = Flipped(Decoupled(UInt(xlen.W)))

    //Bool data from other memory ops
    // using Handshaking protocols
    val predMemOp = Vec(NumMemOP, Flipped(Decoupled(UInt(1.W))))

    //Memory interface
    val memReq  = Decoupled(new ReadReq())
    val memResp = Decoupled(new ReadResp())

    //val memLDIO = new MemLdIO(xlen)

    val memOpAck = Decoupled(UInt(1.W)) //TODO 0 bits
  
  })
}


class LoadNode(implicit p: Parameters) extends LoadIO()(p){

  val nodeID_reg = RegInit(ID.U)

  val addr_reg       = RegInit(0.U(xlen.W))
  val addr_valid_reg = RegInit(false.B)

  val data_reg = RegInit(0.U(xlen.W))

  // Status Register - If src mem-ops done execution
  val in3_done_reg = RegInit(init = false.B)

  // Initialization registers to send ready signals
  // to all inputs to accept input nodes
  val init1_reg = RegInit(true.B)
  val init3_reg = RegInit(true.B)


  //Mem ready for response
  val memresp_ready_reg = RegInit(false.B)
  val data_resp_valid   = RegInit(false.B)



  //-----------------------------------

  //Initialization
  when(init1_reg) {

    io.gepAddr.ready := true.B
  }
    .otherwise( io.gepAddr.nodeq())

  when(init3_reg) {
    io.predMemOp(0).ready := true.B
  }

    .otherwise( io.predMemOp(0).nodeq())





  //-----------------------------------
  //Rules for gepAddr
  when(io.gepAddr.fire()) {
    printf("\n gepAddr. fire \n")
    addr_valid_reg := true.B
    addr_reg := io.gepAddr.bits
    init1_reg := false.B
  }



  //Rules for predMemOp
  when(io.predMemOp(0).fire()) {

    printf("\n predMemOp. fire \n")
    in3_done_reg := true.B
    init3_reg := false.B
  }

  //-----------------------------------
  when(addr_valid_reg) {
    io.memReq.bits.address := addr_reg
    io.memReq.bits.node := nodeID_reg
    io.memReq.valid := true.B
  }
    .otherwise( io.memReq.valid := false.B)


  //Rules for Sending address and data to Memory
  // Note memLDIO.Memreq_addr.ready and Memreq_data.ready are connected
  // Store Node cannot send the data to memory unless all its predecessors are done: in3 in this case
  when(io.memReq.ready && io.memReq.valid && in3_done_reg ) {
    memresp_ready_reg := true.B
    addr_valid_reg := false.B
    printf("\n Mem Request Sent \n")
  }

  //Once the request is sent to memory, be ready to receive the response back
  when(memresp_ready_reg ) {

//    printf("\n Memresp_Ready_reg is true \n")
    io.memResp.ready := true.B
  }
    .otherwise( io.memResp.valid := false.B)

  when( io.memResp.ready && io.memResp.valid) {
    data_resp_valid := true.B
    data_reg := io.memResp.bits.data

    printf("\n Mem Response Received \n")

    //Reset other registers
    memresp_ready_reg := false.B
    in3_done_reg := false.B

  }





  //-----------------------------------
  // Once data_valid_reg is true -> set MEMIO->OUT->VALID and DATA
  // Once MEMIO->IN->sends ack (i.e READY == TRUE)
  // When the ack is received && all Inputs from other memory ops are true set memOpAck := true
  // For the time being just send output when data_valid is true
  //-----------------------------------
  //  when( io.memOpAck.ready && data_valid_reg && in3_done_reg ) {
  //    io.memOpAck.valid := true.B
  ////    io.memOpAck.bits := 1.U
  //  }

  //-----------------------------------
  //when all source memops are done executing - in3_done_reg
  // and ack is received from memory and next node is ready
  // then send Output as valid

  when(io.memOpAck.ready && data_resp_valid ) {
    io.memOpAck.enq(1.U)

    //In Case of pipelining StoreNode
    //    //TODO Once you know you need to reset StoreNode
    //    data_resp_valid := false.B
    //    init1_reg := true.B
    //    init3_reg := true.B
  }
    .otherwise( io.memOpAck.noenq())


}
