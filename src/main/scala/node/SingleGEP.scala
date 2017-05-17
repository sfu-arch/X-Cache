package node

import chisel3._
import chisel3.util._

import config._
import interfaces._


abstract class SingleGepIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    // Inputs should be fed only when Ready is HIGH
    // Inputs are always latched.
    // If Ready is LOW; Do not change the inputs as this will cause a bug
    val baseAddress = Flipped(Decoupled(UInt(xlen.W)))
    val idx         = Flipped(Decoupled(UInt(xlen.W)))

    // The interface has to be prepared to latch the output on every cycle as long as ready is enabled
    // The output will appear only for one cycle and it has to be latched. 
    // The output WILL NOT BE HELD (not matter the state of ready/valid)
    // Ready simply ensures that no subsequent valid output will appear until Ready is HIGH
    val outAddr = Decoupled(UInt(xlen.W))
   })
}


abstract class TwoGepIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    // Inputs should be fed only when Ready is HIGH
    // Inputs are always latched.
    // If Ready is LOW; Do not change the inputs as this will cause a bug
    val baseAddress = Flipped(Decoupled(UInt(xlen.W)))
    val idx1         = Flipped(Decoupled(UInt(xlen.W)))
    val idx2         = Flipped(Decoupled(UInt(xlen.W)))

    // The interface has to be prepared to latch the output on every cycle as long as ready is enabled
    // The output will appear only for one cycle and it has to be latched. 
    // The output WILL NOT BE HELD (not matter the state of ready/valid)
    // Ready simply ensures that no subsequent valid output will appear until Ready is HIGH
    val outAddr = Decoupled(UInt(xlen.W))
   })
}


class SingleGEP(val numbyte: Int, val ID: Int)(implicit p: Parameters) extends SingleGepIO()(p){

  // Extra information
  val token  = RegInit(0.U)
  val nodeID = RegInit(ID.U)

  // Input 
  val baseAddress_reg  = RegInit(0.U(xlen.W))
  val index_reg         = RegInit(0.U(xlen.W))


  val baseAddrValid_reg = RegInit(false.B)
  val indexValid_reg    = RegInit(false.B)

  val outValid = baseAddrValid_reg && indexValid_reg

  io.outAddr.valid := outValid

  io.outAddr.bits := baseAddress_reg + (index_reg * numbyte.U)

  io.baseAddress.ready := ~baseAddrValid_reg
  io.idx.ready         := ~indexValid_reg

  //base address input if it's fire
  when(io.baseAddress.fire()){
    baseAddress_reg   := io.baseAddress.bits
    baseAddrValid_reg := io.baseAddress.valid
  }

  //index input if it's fire
  when(io.idx.fire()){
    index_reg       := io.idx.bits
    indexValid_reg  := io.idx.valid
  }

  //Reset the latches if we make sure that 
  //consumer has consumed the output
  when(outValid && io.outAddr.ready){
    baseAddress_reg := 0.U
    index_reg       := 0.U
    baseAddrValid_reg := false.B
    indexValid_reg    := false.B
    token := token + 1.U
  }


}

class TwoGEP(val numbyte1: Int, val numbyte2: Int, val ID: Int)(implicit p: Parameters) extends TwoGepIO()(p){

  // Extra information
  val token  = RegInit(0.U)
  val nodeID = RegInit(ID.U)

  // Input 
  val baseAddress_reg = RegInit(0.U(xlen.W))
  val index1_reg      = RegInit(0.U(xlen.W))
  val index2_reg      = RegInit(0.U(xlen.W))

  val baseAddrValid_reg = RegInit(false.B)
  val indexValid1_reg    = RegInit(false.B)
  val indexValid2_reg    = RegInit(false.B)

  val outValid = baseAddrValid_reg &&
    (indexValid1_reg && indexValid2_reg)

  io.outAddr.valid := outValid

  io.outAddr.bits := baseAddress_reg + 
          (index1_reg * numbyte1.U)
          (index2_reg * numbyte2.U)

  io.baseAddress.ready := ~baseAddrValid_reg
  io.idx1.ready         := ~indexValid1_reg
  io.idx2.ready         := ~indexValid2_reg

  //base address input if it's fire
  when(io.baseAddress.fire()){
    baseAddress_reg   := io.baseAddress.bits
    baseAddrValid_reg := io.baseAddress.valid
  }

  //index input if it's fire
  when(io.idx1.fire()){
    index1_reg       := io.idx1.bits
    indexValid1_reg  := io.idx1.valid
  }

  when(io.idx2.fire()){
    index2_reg       := io.idx2.bits
    indexValid2_reg  := io.idx2.valid
  }

  //Reset the latches if we make sure that 
  //consumer has consumed the output
  when(outValid && io.outAddr.ready){
    baseAddress_reg := 0.U
    index1_reg       := 0.U
    index2_reg       := 0.U
    baseAddrValid_reg := false.B
    indexValid1_reg    := false.B
    indexValid2_reg    := false.B
    token := token + 1.U
  }


}

