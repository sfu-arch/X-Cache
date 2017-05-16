package amin
/**
  * Created by nvedula on 11/5/17.
  */

import chisel3._
import chisel3.util._

class MemStIO (xLen : Int) extends Bundle {
  val Memreq_addr = Decoupled(UInt(xLen.W))
  val Memreq_data = Decoupled(UInt(xLen.W))
  val Memresp_ack = Flipped(Decoupled(UInt(0.W)))
  //  override def cloneType: this.type = new MemIO(xLen).asInstanceOf[this.type]
  //Apparantly not allowed
  //  Memreq_addr.ready <> Memreq_data.ready // Connect the ready signals for Memreq_addr and Memreq_data
}

class StoreIO(xLen: Int ) extends MemStIO(xLen) {
  //    val Mem = new MemIO(xLen)
  val In1 = Flipped(Decoupled(UInt(xLen.W)))
  val In2 = Flipped(Decoupled(UInt(xLen.W)))
  //Bool data from other memory ops
  // using Handshaking protocols
  val In3 = Flipped(Decoupled(UInt(1.W)))
  val Out1 = Decoupled(UInt(1.W)) //TODO 0 bits
  //    val Mem = MemIO(xLen)
  //Decoupled = ready(I), valid(O), bits(O)
}


class StoreNode (xLen: Int) extends Module {
  val io = IO(new StoreIO(xLen))

  val addr_reg = RegInit(init = 0.U(xLen.W))
  val addr_valid_reg = RegInit(init = false.B)

  val data_reg = RegInit(init = 0.U(xLen.W))
  val data_valid_reg = RegInit(init = false.B)

  // Status Register - If src mem-ops done execution
  val in3_done_reg = RegInit(init = false.B)

  // Initialization registers to send ready signals
  // to all inputs to accept input nodes
  val init1_reg = RegInit(true.B)
  val init2_reg = RegInit(true.B)
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

    io.In1.ready := true.B
  }
    .otherwise( io.In1.nodeq())

  when(init2_reg) {
    io.In2.ready := true.B
  }

    .otherwise( io.In2.nodeq())

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


  //Rules for In2
  when(io.In2.fire()) {

    printf("\n In2. fire \n")
    data_valid_reg := true.B
    data_reg := io.In2.bits
    init2_reg := false.B
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

  when(data_valid_reg){
    io.Memreq_data.enq(true.B)
  }

    .otherwise( io.Memreq_data.noenq())

  //Rules for Sending address and data to Memory
  // Note Memreq_addr.ready and Memreq_data.ready are connected
  // Store Node cannot send the data to memory unless all its predecessors are done: in3 in this case
  when(io.Memreq_addr.fire() && io.Memreq_data.fire() && in3_done_reg ) {
    memresp_ready_reg := true.B
    addr_valid_reg := false.B
    data_valid_reg := false.B
    printf("\n Mem Request Sent \n")
  }

  //Once the request is sent to memory, be ready to receive the response back
  when(memresp_ready_reg ) {

//    printf("\n Memresp_Ready_reg is true \n")
    io.Memresp_ack.ready := true.B
  }
    .otherwise( io.Memresp_ack.nodeq())

  when( io.Memresp_ack.fire()) {
    ack_reg := true.B

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

  when(io.Out1.ready && ack_reg ) {
    io.Out1.enq(1.U)

    //In Case of pipelining StoreNode
    //    //TODO Once you know you need to reset StoreNode
    //    ack_reg := false.B
    //    init1_reg := true.B
    //    init2_reg := true.B
    //    init3_reg := true.B
  }
    .otherwise( io.Out1.noenq())


}
