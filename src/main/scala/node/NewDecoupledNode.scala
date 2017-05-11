package node

import chisel3._
import chisel3.util._


/**
  * Implements a Decoupled Node that used decoupledIO for both input and output
  */

class NewDecoupledNode(val xLen: Int, val opCode: Int, val ID: Int) extends Module {
  val io = IO(new Bundle {
    // Inputs should be fed only when Ready is HIGH
    // Inputs are always latched.
    // If Ready is LOW; Do not change the inputs as this will cause a bug
    val LeftIO   = Flipped(Decoupled(UInt(xLen.W)))
    val RightIO  = Flipped(Decoupled(UInt(xLen.W))) 

    // The interface has to be prepared to latch the output on every cycle as long as ready is enabled
    // The output will appear only for one cycle and it has to be latched. 
    // The output WILL NOT BE HELD (not matter the state of ready/valid)
    // Ready simply ensures that no subsequent valid output will appear until Ready is HIGH
    //val OutIO = Decoupled(new DecoupledNodeOut(xLen))
    val OutIO = Decoupled(UInt(xLen.W))
    })


  //Instantiate ALU with selected code
  val FU = Module(new ALU(xLen, opCode))

  // Input 
  val LeftOperand   = Reg (UInt(xLen.W),next=io.LeftIO.bits)
  val RightOperand  = Reg (UInt(xLen.W),next=io.RightIO.bits)

  val validLeft   = Reg(init  = false.B,next=io.LeftIO.valid)
  val validRight  = Reg(init  = false.B,next=io.RightIO.valid)
  val done        = Reg(init  = false.B)

  // States of the combinatorial logic
  val s_init :: s_exe  :: s_valid :: Nil = Enum(3)
  val state = Reg(init = s_init)

  // Extra information
  val token  = Reg(init = 0.U)
  val nodeID = Reg(init = ID.asUInt())

  // Connect operands to ALU.
  FU.io.in1 := LeftOperand
  FU.io.in2 := RightOperand

 // Connect output to ALU
  io.OutIO.bits:= FU.io.out

  
  when (state === s_init){
    io.OutIO.valid   := false.B
    io.LeftIO.ready  := true.B
    io.RightIO.ready := true.B
  }
  when(state === s_exe){
    io.OutIO.valid   := false.B
    io.LeftIO.ready  := false.B
    io.RightIO.ready := false.B
  }
  when(state === s_valid){
    io.OutIO.valid   := true.B
    io.LeftIO.ready  := true.B
    io.RightIO.ready := true.B
  }


  when (state === s_init){
    when(io.LeftIO.valid && io.RightIO.valid) { state := s_exe}
    .otherwise{ state := s_init}

  }
  when (state === s_exe){
    //TODO We can add wait signal here if we want to wait for functional unit to finish
    when(io.OutIO.ready){ state := s_valid}
    .otherwise{ state := s_exe }
  }
  when (state === s_valid){
    token := token + 1.U
    state := s_init
  }
  
}

