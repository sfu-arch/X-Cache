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
    // gepAddr: The calculated address comming from GEP node
    val gepAddr = Flipped(Decoupled(UInt(xlen.W)))
    val inData = Flipped(Decoupled(UInt(xlen.W)))
    //Bool data from other memory ops
    // using Handshaking protocols
    val predMemOp = Vec(NumMemOP, Flipped(Decoupled(UInt(1.W))))
    val memOpAck = Decoupled(UInt(1.W)) //TODO 0 bits

    //Memory interface
    val memReq  = Decoupled(new WriteReq())
    val memResp = Flipped(Decoupled(new WriteResp()))

  })
}


class StoreNode(implicit p: Parameters) extends StoreIO()(p){

  val nodeID_reg = RegInit(ID.U)
  val mask_reg = RegInit(mask.U)
  val addr_reg = RegInit(init = 0.U(xlen.W))
  val addr_valid_reg = RegInit(init = false.B)

  val data_reg = RegInit(init = 0.U(xlen.W))
  val data_valid_reg = RegInit(init = false.B)

  // Status Register - If src mem-ops done execution
  val in3_done_reg = RegInit(init = false.B)

  // Initialization registers to send ready signals
  // to all inputs to accept input nodes
  val init1_reg = RegInit(true.B)
  val init2_reg = RegInit(true.B)

  //TODO Make init3_reg a Vector of registers activated based on input predMemOp
  val init3_reg = RegInit(true.B)


  //Mem ready for response
  val memresp_ready_reg = RegInit(false.B)
  val ack_reg = RegInit(false.B  )



  //-----------------------------------
  //  //NOTE: Whenever Memreq_addr_ready is true Memreq_data should be true
  //  when(io.Memreq_addr.ready) {
  //    io.Memreq_data.ready := true.B
  //  }

  //Initialization
  when(init1_reg) {

    io.gepAddr.ready := true.B
  }
    .otherwise( io.gepAddr.nodeq())

  when(init2_reg) {
    io.inData.ready := true.B
  }

    .otherwise( io.inData.nodeq())

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


  //Rules for inData
  when(io.inData.fire()) {

    printf("\n inData. fire \n")
    data_valid_reg := true.B
    data_reg := io.inData.bits
    init2_reg := false.B
  }


  //Rules for predMemOp(0)
  when(io.predMemOp(0).fire()) {

    printf("\n predMemOp(0). fire \n")
    in3_done_reg := true.B
    init3_reg := false.B
  }

  //-----------------------------------

  when(data_valid_reg && addr_valid_reg) {

    io.memReq.valid := true.B
    io.memReq.bits.address := addr_reg
    io.memReq.bits.node := nodeID_reg
    io.memReq.bits.mask := mask_reg
    io.memReq.bits.data := data_reg
  }

    .otherwise( io.memReq.valid := false.B)

  //Rules for Sending address and data to Memory
  // Note Memreq_addr.ready and Memreq_data.ready are connected
  // Store Node cannot send the data to memory unless all its predecessors are done: in3 in this case
  when(io.memReq.ready && io.memReq.valid  && in3_done_reg ) {
    memresp_ready_reg := true.B
    addr_valid_reg := false.B
    data_valid_reg := false.B
    printf("\n Mem Request Sent \n")
  }

  //Once the request is sent to memory, be ready to receive the response back
  when(memresp_ready_reg ) {

//    printf("\n Memresp_Ready_reg is true \n")
    io.memResp.ready := true.B
  }
    .otherwise( io.memResp.ready := false.B )

  when( io.memResp.valid && io.memResp.ready) {
    ack_reg := true.B

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

  when(ack_reg ) {
    //keep sending valid && bits
    io.memOpAck.enq(1.U)
  }
    .otherwise( io.memOpAck.valid := false.B)

  when(io.memOpAck.fire()) {

    //In Case of pipelining StoreNode
    //    //TODO Once you know you need to reset StoreNode
        ack_reg := false.B
        init1_reg := true.B
        init2_reg := true.B
        init3_reg := true.B
  }


}
