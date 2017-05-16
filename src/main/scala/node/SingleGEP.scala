package node

import chisel3._
import chisel3.util._

import config._
import interfaces._


abstract class GEPIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    // Inputs should be fed only when Ready is HIGH
    // Inputs are always latched.
    // If Ready is LOW; Do not change the inputs as this will cause a bug
    val baseAddress  = Flipped(Decoupled(UInt(xlen.W)))
    val index        = Flipped(Decoupled(UInt(xlen.W)))

    // The interface has to be prepared to latch the output on every cycle as long as ready is enabled
    // The output will appear only for one cycle and it has to be latched. 
    // The output WILL NOT BE HELD (not matter the state of ready/valid)
    // Ready simply ensures that no subsequent valid output will appear until Ready is HIGH
    val outAddr = Decoupled(UInt(xlen.W))
   })
}

class SingleGEP(implicit p: Parameters, val numbyte: Int, val ID: Int) extends GEPIO()(p){

  // Input 
  val baseOperand_R    = RegNext(io.baseAddress.bits, init = 0.U(xlen.W))
  val indexOperand_R   = RegNext(io.index.bits, init = 0.U(xlen.W))

  //Instantiate ALU with selected code
  io.outAddr.bits := baseOperand_R + (indexOperand_R * numbyte.U)

  // States of the combinatorial logic
  val s_init :: s_valid :: Nil = Enum(2)
  val state = RegInit(s_init)

  // Extra information
  val token  = RegInit(0.U)
  val nodeID = RegInit(ID.asUInt())

  when (state === s_init){
    io.outAddr.valid     := false.B
    io.baseAddress.ready := true.B
    io.index.ready       := true.B
  }
  when(state === s_valid){
    io.outAddr.valid      := true.B
    io.baseAddress.ready  := false.B
    io.index.ready        := false.B
  }
  
  when (state === s_init){
    when(io.baseAddress.valid && io.index.valid &&
      io.outAddr.ready) { state := s_valid}
    .otherwise{ state := s_init}

  }
  when (state === s_valid){
    state := s_init
    token := token + 1.U
  }
 
}

