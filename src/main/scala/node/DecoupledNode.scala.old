package node

import chisel3._
import chisel3.util._


/**
  * Implements a Decoupled Node that used decoupledIO for both input and output
  */

class DecoupledNodeOut(val xLen: Int) extends Bundle {
  override def cloneType = new DecoupledNodeOut(xLen = xLen).asInstanceOf[this.type]
  val data  = Output(UInt(xLen.W))
  val state = Output(UInt(xLen.W))
}

class DecoupledNode(val xLen: Int, val opCode: Int) extends Module {
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
  val s_init :: s_input :: s_exe  :: Nil = Enum(3)
  val state = Reg(init = s_init)


  io.LeftIO.ready   := !validLeft
  io.RightIO.ready  := !validRight

  io.OutIO.valid :=  done

  // Connect operands to ALU.
  FU.io.in1 := LeftOperand
  FU.io.in2 := RightOperand

 // Connect output to ALU
  io.OutIO.bits:= FU.io.out

  //io.OutIO.bits.state:= state


  switch (state){
    is(s_init) {
      done := false.B
      state := s_input
    }
    is (s_input){
      when(validLeft && validRight)
      {
        state := s_exe
      }
    }
    // Execution completed state.
    // Set done to true to set io.out.ready
    // The valid bits need to be set to false here to enable the ALU to read the data in the next cycle itself
    // 
    is (s_exe){ 
      when(io.OutIO.ready){
        done  := true.B
        validLeft := false.B
        validRight := false.B
        state := s_init
      }
    }
  }
}

