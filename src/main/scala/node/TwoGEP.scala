package node

import chisel3._
import chisel3.util._


/* A new Bus for GEPs.
 * It should include Base address and Number of byte.
 */
//class DecoupledGEPAddr(val xLen: Int) extends Bundle {
  //override def cloneType = new DecoupledNodeOut(xLen = xLen).asInstanceOf[this.type]
  //val Index       = Output(UInt(xLen.W))
  //val NumByte     = Output(UInt(xLen.W))
//}


class TwoGEP(val xLen: Int, val opCode: Int, val ID: Int) extends Module {
  val io = IO(new Bundle {
    // Inputs should be fed only when Ready is HIGH
    // Inputs are always latched.
    // If Ready is LOW; Do not change the inputs as this will cause a bug
    val Address         = Flipped(Decoupled(UInt(xLen.W))) 
    val FirstIndex      = Flipped(Decoupled(new DecoupledGEPAddr((xLen))))
    val SecondIndex     = Flipped(Decoupled(new DecoupledGEPAddr((xLen))))

    // The interface has to be prepared to latch the output on every cycle as long as ready is enabled
    // The output will appear only for one cycle and it has to be latched. 
    // The output WILL NOT BE HELD (not matter the state of ready/valid)
    // Ready simply ensures that no subsequent valid output will appear until Ready is HIGH
    //val OutIO = Decoupled(new DecoupledNodeOut(xLen))
    val OutIO = Decoupled(UInt(xLen.W))
    })


  // Input 
  val BAOperand           = Reg (UInt(xLen.W),next=io.Address.bits)

  val FirstIndexOperand  = Reg (UInt(xLen.W),next=io.FirstIndex.bits.Index)
  val FirstNByteOperand  = Reg (UInt(xLen.W),next=io.FirstIndex.bits.NumByte)

  val SecondIndexOperand  = Reg (UInt(xLen.W),next=io.SecondIndex.bits.Index)
  val SecondNByteOperand  = Reg (UInt(xLen.W),next=io.SecondIndex.bits.NumByte)

  //Instantiate ALU with selected code
  io.OutIO.bits := BAOperand +
      (FirstIndexOperand*FirstNByteOperand) +
      (SecondIndexOperand + SecondNByteOperand)

  // States of the combinatorial logic
  val s_init :: s_valid :: Nil = Enum(2)
  val state = Reg(init = s_init)

  // Extra information
  val token  = Reg(init = 0.U)
  val nodeID = Reg(init = ID.asUInt())

  when (state === s_init){
    io.OutIO.valid        := false.B
    io.Address.ready      := true.B
    io.FirstIndex.ready   := true.B
    io.SecondIndex.ready  := true.B
  }
  when(state === s_valid){
    io.OutIO.valid        := true.B
    io.Address.ready      := false.B
    io.FirstIndex.ready   := false.B
    io.SecondIndex.ready  := false.B
  }
  
  when (state === s_init){
    when(io.Address.valid && io.FirstIndex.valid &&
      io.SecondIndex.valid && io.OutIO.ready) { state := s_valid}
    .otherwise{ state := s_init}

  }
  when (state === s_valid){
    state := s_init
  }
 
}

