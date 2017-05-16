package amin

/**
  * Created by nvedula on 15/5/17.
  */

import chisel3._
import chisel3.util._
import org.scalacheck.Prop.False

class MemLdIO (xLen : Int) extends Bundle {
  val Memreq_addr = Decoupled(UInt(xLen.W))
  val Memresp_data = Flipped(Decoupled(UInt(xLen.W)))
}

class LoadIO(xLen: Int ) extends MemLdIO(xLen) {
  val In1 = Flipped(Decoupled(UInt(xLen.W)))
  //Bool data from other memory ops
  // using Handshaking protocols
  val In3 = Flipped(Decoupled(UInt(0.W)))
  val Out1 = Decoupled(UInt(1.W)) //TODO 0 bits
  //Decoupled = ready(I), valid(O), bits(O)
}


class LoadNode (xLen: Int) extends Module {
  val io = IO(new LoadIO(xLen))

  val addr_reg = RegInit(init = 0.U(xLen.W))
  val addr_valid_reg = RegInit(init = false.B)

  val data_reg = RegInit(init = 0.U(xLen.W))

  // Status Register - If src mem-ops done execution
  val in3_done_reg = RegInit(init = false.B)

  // Initialization registers to send ready signals
  // to all inputs to accept input nodes
  val init1_reg = RegInit(true.B)
  val init3_reg = RegInit(true.B)


  //Mem ready for response
  val memresp_ready_reg = RegInit(false.B)
  val data_resp_valid = RegInit(false.B  )



  //-----------------------------------

  //Initialization
  when(init1_reg) {

    io.In1.ready := true.B
  }
    .otherwise( io.In1.nodeq())

  when(init3_reg) {
    io.In3.ready := true.B
  }

    .otherwise( io.In3.nodeq())





  //-----------------------------------
  //Rules for In1
  when(io.In1.fire()) {
    printf("\n In1. fire \n")
    addr_valid_reg := true.B
    addr_reg := io.In1.bits
    init1_reg := false.B
  }



  //Rules for In3
  when(io.In3.fire()) {

    printf("\n In3. fire \n")
    in3_done_reg := true.B
    init3_reg := false.B
  }

  //-----------------------------------
  when(addr_valid_reg) {
    io.Memreq_addr.enq(true.B)
  }
    .otherwise( io.Memreq_addr.noenq())


  //Rules for Sending address and data to Memory
  // Note Memreq_addr.ready and Memreq_data.ready are connected
  // Store Node cannot send the data to memory unless all its predecessors are done: in3 in this case
  when(io.Memreq_addr.fire() && in3_done_reg ) {
    memresp_ready_reg := true.B
    addr_valid_reg := false.B
    printf("\n Mem Request Sent \n")
  }

  //Once the request is sent to memory, be ready to receive the response back
  when(memresp_ready_reg ) {

//    printf("\n Memresp_Ready_reg is true \n")
    io.Memresp_data.ready := true.B
  }
    .otherwise( io.Memresp_data.nodeq())

  when( io.Memresp_data.fire()) {
    data_resp_valid := true.B
    data_reg := io.Memresp_data.bits

    printf("\n Mem Response Received \n")

    //Reset other registers
    memresp_ready_reg := false.B
    in3_done_reg := false.B

  }





  //-----------------------------------
  // Once data_valid_reg is true -> set MEMIO->OUT->VALID and DATA
  // Once MEMIO->IN->sends ack (i.e READY == TRUE)
  // When the ack is received && all Inputs from other memory ops are true set Out1 := true
  // For the time being just send output when data_valid is true
  //-----------------------------------
  //  when( io.Out1.ready && data_valid_reg && in3_done_reg ) {
  //    io.Out1.valid := true.B
  ////    io.Out1.bits := 1.U
  //  }

  //-----------------------------------
  //when all source memops are done executing - in3_done_reg
  // and ack is received from memory and next node is ready
  // then send Output as valid

  when(io.Out1.ready && data_resp_valid ) {
    io.Out1.enq(1.U)

    //In Case of pipelining StoreNode
    //    //TODO Once you know you need to reset StoreNode
    //    data_resp_valid := false.B
    //    init1_reg := true.B
    //    init3_reg := true.B
  }
    .otherwise( io.Out1.noenq())


}
