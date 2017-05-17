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
    val addr         = Flipped(Decoupled(new GepAddr()))

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
  val baseAddress_reg   = RegInit(0.U(xlen.W))
  val index_reg         = RegInit(0.U(xlen.W))

  val addrValid_reg = RegInit(false.B)

  io.outAddr.valid := addrValid_reg

  io.outAddr.bits := baseAddress_reg + (index_reg * numbyte.U)

  io.addr.ready  := ~addrValid_reg

  //Latch Addr input if it's fire
  when(io.addr.fire()){
    baseAddress_reg := io.addr.bits.baseAddress
    index_reg       := io.addr.bits.index
    addrValid_reg   := io.addr.valid
  }

  //Reset the latches if we make sure that 
  //consumer has consumed the output
  when(addrValid_reg && io.outAddr.ready){
    baseAddress_reg := 0.U
    index_reg       := 0.U
    addrValid_reg   := false.B
    token := token + 1.U
  }


}

