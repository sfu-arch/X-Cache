package node

/**
  * Created by nvedula on 15/5/17.
  */

import chisel3._
import chisel3.util._
import org.scalacheck.Prop.False

import config._
import interfaces._

trait MemoryOpConstants 
{
   val MT_X  = Bits(0, 3)
   val MT_B  = Bits(1, 3)
   val MT_H  = Bits(2, 3)
   val MT_W  = Bits(3, 3)
   val MT_D  = Bits(4, 3)
   val MT_BU = Bits(5, 3)
   val MT_HU = Bits(6, 3)
   val MT_WU = Bits(7, 3)

   val M_X   = Bits("b0", 1)
   val M_XRD = Bits("b0", 1) // int load
   val M_XWR = Bits("b1", 1) // int store
}

  object Constants  extends MemoryOpConstants 
{


}
  
import Constants._

abstract class LoadMaskIO(NumPredMemOps :Int = 1, val ID :Int = 0)(implicit val p: Parameters) extends Module with CoreParams{

  val io = IO(new Bundle {
    // gepAddr: The calculated address comming from GEP node
    val Gep = Flipped(Decoupled(UInt(xlen.W)))

    //Bool data from predecessor memory ops
    // using Handshaking protocols
    // predValid has to be size of atleast 1 and a true has to be wired to it
    // even if no predecessors exist.
    val PredMemOp = Vec(NumPredMemOps, Flipped(Decoupled(UInt(1.W))))

    //Memory interface
    val MemReq    = Decoupled(new ReadReq())
    val MemResp   = Flipped(Decoupled(new ReadResp()))

    // Data outs.

    // Successor Memory Op
    val SuccMemOp = Vec(NumPredMemOps, Decoupled(UInt(1.W))) 
    })
}


class LoadMaskNode(NumPredMemOps: Int)(implicit p: Parameters) extends LoadMaskIO(NumPredMemOps)(p){



  // Extra information
  val token  = RegInit(0.U)
  val nodeID = RegInit(ID.U)


  // Gep address passed into load
  val GepOperand   = RegInit(0.U(xlen.W))
  val GepValid     = RegInit(false.B)

  // predessor memory ops. whether they are valid.
  val predValid =  RegInit(Vec(Seq.fill(NumPredMemOps)(false.B)))
  val succValid =  RegInit(Vec(Seq.fill(NumSuccMemOps)(false.B)))

  // Mask for final ANDing and output of data
  val bitmask  = RegInit(0.U((2*xlen).W))
  // Send word mask for tracking how many words need to be read
  val sendbytemask = RegInit(0.U((2*xlen).W))
 

  // Is the request valid and request to memory
  val ReqValid     = RegInit(false.B)
  val ReqAddress   = RegNext((GepOperand >> log2Up(xlen/8)) << log2Up(xlen/8), 0.U(xlen.W))


  // Incoming data valid and daata operand.
  val DataValid    = RegInit(false.B)
  val ptr          = RegInit(0.U((2*xlen).W))
  val linebuffer   = RegInit(Vec(Seq.fill(2)(0.U(xlen.W))))
 

  // Latch predecessor valid signals.
  for (i <- 0 until NumPredMemOps) {
    io.PredMemOp(i).ready := ~predValid(i)
    when(io.PredMemOp(i).fire()) {
    predValid(i) := io.PredMemOp(i).valid      
  }
}


  //Latch GEP input if it's fire
  io.Gep.ready   := ~GepValid
  when(io.Gep.fire()) {
    GepOperand := io.Gep.bits
    GepValid   := io.Gep.valid
  }

  // Because of this statement. predValid vec has to be size of atleast 1 and a true has to be wired to it
  // even if no predecessors exist.
  val predValidInt = predValid.asUInt
  val inputValid   = GepValid & predValidInt.andR
  io.MemReq.valid := ReqValid
 
 val s_init :: s_SENDING :: s_RECEIVING  :: s_Done :: Nil = Enum(4)
 val state = Reg(init = s_init)

 val data = Fill(4,0xffbe.U)
 val type_word = MT_W


  // Now I need a state machine to drive this. This would make it a lot easier when sending multiple requests.
  when(inputValid && state === s_init) 
  {
    printf("Inputs are Ready %d", GepOperand)

    bitmask        := ReadBitMask(type_word,GepOperand)
    // Two masks needed for keeping track of what is sent and received. 
    // Could also use count. Going from mask to word count is difficult
    // We simply use shifts in the mask to indicate if we have requested all words required
    sendbytemask   := ReadByteMask(type_word,GepOperand)
  
    state      := s_SENDING
    // Set the state to send
    // Generate send mask
    // Generate the based load addresses

  }.elsewhen((state === s_SENDING) && (sendbytemask =/= 0.asUInt(64.W))) {
     printf("Requesting data %x", sendbytemask)
     ReqValid := 1.U          
    // io.MemReq.ready means arbitration succeeded and memory op has been passed on
    when(io.MemReq.ready === true.B) {
      // Next word to be fetched
      ReqAddress   := ReqAddress + 1.U
      // Shift right by word length on machine. 
      sendbytemask := sendbytemask >> (xlen/8) 
      // Disable request
      ReqValid := 0.U
      // Move to receiving data
      state := s_RECEIVING
    }
  
  }.elsewhen ((state === s_RECEIVING) && (io.MemResp.valid === true.B)) {
   // Received data; concatenate into linebuffer 
   linebuffer(ptr) := io.MemResp.bits.data
   // Increment ptr to next entry in linebuffer (for next read)
   ptr := ptr + 1.U
   // Check if more data needs to be sent 
   val y = (sendbytemask === 0.asUInt(64.W))
   state := Mux(y,s_Done,s_SENDING)

  }.elsewhen (state === s_Done) {

   // AND with bitmask, shift and output data.
   // Set valid to true.
   // 
   val z = linebuffer.asUInt
   printf("linebuffer %x", z)

 }


  printf(p"State: $state, ${io.MemResp.valid}")



  // }.elsewhen (state === s_RECEIVING && sendbytemask === 0.U) {
  //   // No more data left to receive. Waiting for final word.

  //   // when there is incoming valid. change state to done.
  //   // next state = done.
   // }
   // .elsewhen (state === s_Done) {

  // // when done and io.out.ready
  // // send data.
  // }

  // Set the validity of the output signal. 
  // Connect Mem Resp to Output

  //-----------------------------------
  // Once data_valid_reg is true -> set MEMIO->OUT->VALID and DATA
  // Once MEMIO->IN->sends ack (i.e READY == TRUE)
  // When the ack is received && all Inputs from other memory ops are true set memOpAck := true
  // For the time being just send output when data_valid is true
  //-----------------------------------
 object WriteOut
{

   def apply(dataout: UInt, sel: UInt, address: UInt): UInt = 
   {
      val byte_alignment = address(1,0)
      val bit_alignment  =  Cat(byte_alignment,UInt(0,3))
      val out = dataout << bit_alignment
      printf("%d",out)
      return out
   }
}

object ReadByteMask
{
   def apply(sel: UInt, address: UInt): UInt = 
   {
      val wordmask = Typ2ByteMask(sel)
      val alignment = address(1,0)
      val mask = (wordmask << alignment)
      return mask
   }
}

object ReadBitMask
{
   def apply(sel: UInt, address: UInt): UInt = 
   {
      val wordmask = Typ2BitMask(sel)
      val alignment = Cat(address(1,0),UInt(0,3))
      val mask = (wordmask << alignment)
      return mask
   }
}


object Typ2ByteMask
{
   def apply(sel: UInt): UInt = 
   {
      val mask = Mux(sel === MT_H || sel === MT_HU, Fill(2,1.U),
                 Mux(sel === MT_B || sel === MT_BU, Fill(1,1.U),
                 Mux(sel === MT_W || sel === MT_WU, Fill(4,1.U),
                                                    Fill(8,1.U))))
}

object Typ2BitMask
{
   def apply(sel: UInt): UInt = 
   {
      val mask = Mux(sel === MT_H || sel === MT_HU, Fill(16,1.U),
                 Mux(sel === MT_B || sel === MT_BU, Fill(8,1.U),
                 Mux(sel === MT_W || sel === MT_WU, Fill(32,1.U),
                                                    Fill(64,1.U))))
      return mask
   }
}

object Data2Sign
{
   def apply(data: Bits, typ: Bits) : Bits =
   {
      val out = Mux(typ === MT_H,  Cat(Fill(xlen-16, data(15)),  data(15,0)),
                Mux(typ === MT_HU, Cat(Fill(xlen-16, UInt(0x0)), data(15,0)),
                Mux(typ === MT_B,  Cat(Fill(xlen-8, data(7)),    data(7,0)),
                Mux(typ === MT_BU, Cat(Fill(xlen-8, UInt(0x0)), data(7,0)), 
                                    data(xlen-1,0)))))
      
      return out
   }
}



}
