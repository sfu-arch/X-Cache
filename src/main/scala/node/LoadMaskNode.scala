  package node

/**
  * Created by nvedula on 15/5/17.
  */

import chisel3._
import chisel3.util._
import org.scalacheck.Prop.False
import scala.util.control.Breaks._
import arbiters._
import config._
import interfaces._
import muxes._

trait MemoryOpConstants 
{
   val MT_X  = 0.U(3.W)
   val MT_B  = 1.U(3.W)
   val MT_H  = 2.U(3.W)
   val MT_W  = 3.U(3.W)
   val MT_D  = 4.U(3.W)
   val MT_BU = 5.U(3.W)
   val MT_HU = 6.U(3.W)
   val MT_WU = 7.U(3.W)
}

  object Constants  extends MemoryOpConstants 
{


}
  
import Constants._

abstract class LoadMaskIO(NumPredOps :Int = 1, NumSuccOps :Int = 1,
  val ID :Int = 0)(implicit val p: Parameters) extends Module with CoreParams{

  val io = IO(new Bundle {
    // gepAddr: The calculated address comming from GEP node
    val Gep = Flipped(Decoupled(UInt(xlen.W)))

    //Bool data from predecessor memory ops
    // using Handshaking protocols
    // predValid has to be size of atleast 1 and a true has to be wired to it
    // even if no predecessors exist.
    val PredOp = Vec(NumPredOps, Flipped(Decoupled(UInt(1.W))))

    //Memory interface
    val MemReq    = Decoupled(new ReadReq())
    val MemResp   = Flipped(Decoupled(new ReadResp()))

    // Data outs.

    // Successor Memory Op
    val SuccOp = Vec(NumPredOps, Decoupled(UInt(1.W))) 
    })
}


class LoadMaskNode(NumPredOps: Int = 1, NumSuccOps: Int = 1)(implicit p: Parameters) extends LoadMaskIO(NumPredOps)(p){



  // Extra information
  val token  = RegInit(0.U)
  val nodeID = RegInit(ID.U)


  // Gep address passed into load
  val GepOperand   = RegInit(0.U(xlen.W))
  val GepValid     = RegInit(false.B)

  // predessor memory ops. whether they are valid.
  val predValid =  RegInit(Vec(Seq.fill(NumPredOps)(false.B)))
  val succValid =  RegInit(Vec(Seq.fill(NumSuccOps)(false.B)))

  // Mask for final ANDing and output of data
  val bitmask  = RegInit(0.U((2*xlen).W))
  // Send word mask for tracking how many words need to be read
  val sendbytemask = RegInit(0.U((2*xlen).W))
 

  // Is the request valid and request to memory
  val ReqValid     = RegInit(false.B)
  val ReqAddress   = RegNext((GepOperand >> log2Ceil(xlen/8)) << log2Ceil(xlen/8), 0.U(xlen.W))


  // Incoming data valid and daata operand.
  val DataValid    = RegInit(false.B)
  val ptr          = RegInit(0.U((2*xlen).W))
  val linebuffer   = RegInit(Vec(Seq.fill(2)(0.U(xlen.W))))
 

  // Latch predecessor valid signals.
  for (i <- 0 until NumPredOps) {
    io.PredOp(i).ready := ~predValid(i)
    when(io.PredOp(i).fire()) {
      predValid(i) := io.PredOp(i).valid      
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

  }.elsewhen((state === s_SENDING) && (sendbytemask =/= 0.asUInt(16.W))) {
     printf("Requesting data %x", sendbytemask)
     ReqValid := 1.U          
    // io.MemReq.ready means arbitration succeeded and memory op has been passed on
    when(io.MemReq.ready === true.B && ReqValid === 1.U) {
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
   val y = (sendbytemask === 0.asUInt(16.W))
   state := Mux(y,s_Done,s_SENDING)

  }.elsewhen (state === s_Done) {

   // AND with bitmask, shift and output data.
   // Set valid to true.
   // 
   state := s_init
   val z = linebuffer.asUInt
   printf("linebuffer %x", (z & bitmask) >> Cat(GepOperand(1,0),UInt(0,3)))

 }

 val ArbiterReady    = RegInit(true.B)
 var prev     = Seq.fill(0){Module(new RRArbiter(UInt(32.W), 4)).io}
 var toplevel = Seq.fill(0){Module(new RRArbiter(UInt(32.W), 4)).io}
 var x = 20
 var y = (x + 4 - 1)/4
 while (y > 0) {
  val arbiters = Seq.fill(y){Module(new RRArbiter(UInt(32.W),4)).io}
  if (prev.length != 0)  {
     // println("Y:"+y +" Prev:"+ prev.length)
    for (i <- 0 until arbiters.length*4) {
      if (i < prev.length) {
        arbiters(i/4).in(i-((i/4)*4)) <>  prev(i).out
      }else {
       arbiters(i/4).in(i-((i/4)*4)).valid := false.B
      }
    }
  }

  if (prev.length == 0) {
    toplevel = arbiters
    for (i <- 0 until arbiters.length*4) {
      if (i < x) {
        arbiters(i/4).in(i-((i/4)*4)).bits :=  i.U;
        arbiters(i/4).in(i-((i/4)*4)).valid := true.B;
        }else {
         arbiters(i/4).in(i-((i/4)*4)).valid := false.B;
        }
    }
  }
    prev = arbiters
    if (y == 1) {
      y = 0
    } 
    else {
      y = (y + 4 -1)/4
    }
  }

 prev(0).out.ready := ArbiterReady
 when (state === s_Done) 
 {ArbiterReady := false.B}.otherwise{ArbiterReady := true.B}
 // printf(p"Chosen:  ${prev(0).chosen} Out: ${prev(0).out} \n")



 val Tree = Module(new ArbiterTree(BaseSize = 4, NumOps = 2, UInt(32.W)))
 val MuxTree = Module(new DeMuxTree(BaseSize = 32, NumOps = 100, new ReadResp()))
 MuxTree.io.enable := true.B
 MuxTree.io.input.data := 500.U
 MuxTree.io.input.RouteID := 10.U

 Tree.io.in(0).bits := 0.U
 Tree.io.in(0).valid := true.B
 Tree.io.in(1).bits := 1.U
 Tree.io.in(1).valid := true.B
 // Tree.io.in(2).bits := 2.U
 // Tree.io.in(2).valid := true.B
 // Tree.io.in(3).bits := 3.U
 // Tree.io.in(3).valid := false.B
 Tree.io.out.ready := true.B
 // printf(p"Tree Out: ${Tree.io.out} \n")
 printf(p"\n MuxTree Out: ${MuxTree.io.outputs} \n")


 // val L1_1 = Module(new RRArbiter(UInt(32.W),2))
 // val L1_2 = Module(new RRArbiter(UInt(32.W),2))
 // val L1_3   = Module(new RRArbiter(UInt(32.W),2))
 // val L2_1   = Module(new RRArbiter(UInt(32.W),2))
 // val L2_2   = Module(new RRArbiter(UInt(32.W),2))
 // val L3_1   = Module(new RRArbiter(UInt(32.W),2))


 // L2_1.io.in(0) <> L1_1.io.out
 // L2_1.io.in(1) <> L1_2.io.out
 // L2_2.io.in(0) <> L1_3.io.out
 // L3_1.io.in(0) <> L2_1.io.out
 // L3_1.io.in(1) <> L2_2.io.out

 // L3_1.io.out.ready := true.B
 
 // L1_1.io.in(0).bits := 1.U
 // L1_1.io.in(0).valid := true.B
 // L1_2.io.in(1).bits := 2.U
 // L1_2.io.in(1).valid := true.B

 // L1_2.io.in(0).bits := 3.U
 // L1_2.io.in(0).valid := true.B
 // L1_2.io.in(1).bits := 4.U
 // L1_2.io.in(1).valid := true.B

 // L1_3.io.in(0).bits := 5.U
 // L1_3.io.in(0).valid := true.B



 // printf(p"Arbiters Chosen : ${L2_2.io.out},${L2_2.io.chosen}\n")





  // val y = PriorityEncoder(0x4.U.toBools)
  //  printf("Priority: %x",y)


  // printf(p"State: $state, ${io.MemResp.valid}")
}



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

object Typ2BitMask
{
   def apply(sel: UInt): UInt = 
   {
      val mask = Mux(sel === MT_H.asUInt || sel === MT_HU.asUInt, Fill(16,1.U),
                 Mux(sel === MT_B.asUInt || sel === MT_BU.asUInt, Fill(8,1.U),
                 Mux(sel === MT_W.asUInt || sel === MT_WU.asUInt, Fill(32,1.U),
                                                    Fill(64,1.U))))
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
      val mask = Mux(sel === MT_H.asUInt || sel === MT_HU.asUInt, Fill(2,1.U),
                 Mux(sel === MT_B.asUInt || sel === MT_BU.asUInt, Fill(1,1.U),
                 Mux(sel === MT_W.asUInt || sel === MT_WU.asUInt, Fill(4,1.U),
                                                    Fill(8,1.U))))
      return mask
}

/**
 * @todo fix the xlen parameter for the apply value
 */
//object Data2Sign
//{
   //def apply(data: Bits, typ: Bits) : Bits =
   //{
     ////@todo check whether casting Bits to UInt doesn't introduce bug
      //val out = Mux(typ.asUInt === MT_H,  Cat(Fill(xlen-16, data(15)),  data(15,0)),
                //Mux(typ.asUInt === MT_HU, Cat(Fill(xlen-16, UInt(0x0)), data(15,0)),
                //Mux(typ.asUInt === MT_B,  Cat(Fill(xlen-8, data(7)),    data(7,0)),
                //Mux(typ.asUInt === MT_BU, Cat(Fill(xlen-8, UInt(0x0)), data(7,0)), 
                                    //data(xlen-1,0)))))
      
      //return out
   //}
//}



}
