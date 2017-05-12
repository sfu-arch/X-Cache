package amin
/**
  * Created by nvedula on 11/5/17.
  */

import chisel3._
import chisel3.util._
import org.scalacheck.Prop.False

//class MemIO (xLen : Int) extends Bundle {
//  val memout = Decoupled(UInt(xLen.W))
//  val memin = Flipped(Decoupled(UInt(0.W)))
//}

class StoreNode (xLen: Int) extends Module {
  val io = IO(new Bundle {
    val In1 = Flipped(Decoupled(UInt(xLen.W)))
    val In2 = Flipped(Decoupled(UInt(xLen.W)))
    //Bool data from other memory ops
    // using Handshaking protocols
    val In3 = Flipped(Decoupled(UInt(0.W)))
    val Out1 = Decoupled(UInt(1.W))

//    val Mem = MemIO(xLen)
    //Decoupled = ready(I), valid(O), bits(O)
  })

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


//  val in1_fire = Wire(io.In1.fire())
//  val in2_fire = Wire(io.In2.fire())
//  val in3_fire = Wire(io.In3.fire())
//  val out1_fire = io.Out1.fire()

  //-----------------------------------
  //Initialization
  when(init1_reg) {

    io.In1.ready := true.B
  }
    .otherwise( io.In1.ready := false.B)

  when(init2_reg) {
    io.In2.ready := true.B
  }

    .otherwise( io.In2.ready := false.B)

  when(init3_reg) {
    io.In3.ready := true.B
  }

    .otherwise( io.In3.ready := false.B)





  //-----------------------------------
  //Rules for In1
  when(io.In1.fire()) {
    printf("\n In1. fire \n")
    addr_valid_reg := true.B
    init1_reg := false.B
  }


  //Rules for In2
  when(io.In2.fire()) {

    printf("\n In2. fire \n")
    data_valid_reg := true.B
    init2_reg := false.B
  }


  //Rules for In3
  when(io.In3.fire()) {

    printf("\n In3. fire \n")
    in3_done_reg := true.B
    init3_reg := false.B
  }

  //-----------------------------------


  //-----------------------------------
  // Once data_valid_reg is true -> set MEMIO->OUT->VALID and DATA
  // Once MEMIO->IN->sends ack (i.e READY == TRUE)
  // When the ack is received && all Inputs from other memory ops are true set Out1 := true
  // For the time being just send output when data_valid is true
  //-----------------------------------
  when( io.Out1.ready && data_valid_reg && in3_done_reg ) {
//    io.Out1.enq(1)
    io.Out1.valid := true.B
    io.Out1.bits := 1.U
  }

  //-----------------------------------


}



//class GEPNode (xLen: Int) extends Module {
//    val io = IO(new Bundle {
//    val Out1 = Decoupled(UInt(xLen.W))
//    //Decoupled = ready(I), valid(O), bits(O)
//  })
//
//}
//
//
//class DataNode (xLen: Int) extends Module {
//    val io = IO(new Bundle {
//    val Out1 = Decoupled(UInt(xLen.W))
//    //Decoupled = ready(I), valid(O), bits(O)
//  })
//

