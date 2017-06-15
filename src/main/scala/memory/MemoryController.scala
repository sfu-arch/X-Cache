package memory

// Generic Packages
import chisel3._
import chisel3.Module
import chisel3.util._
import org.scalacheck.Prop.False


// Modules needed
import arbiters._
import muxes._

// Config
import config._
import utility._
import interfaces._


// Memory constants
import Constants._


abstract class ReadEntryIO()(implicit val p: Parameters) 
  extends Module 
  with CoreParams{

  val io = IO(new Bundle {
    // Read Request Type
    val NodeReq   = Flipped(Decoupled(Input(new ReadReq)))
    val NodeResp  = Decoupled(new ReadResp)
    //Memory interface
    val MemReq    = Decoupled(new ReadReq)
    val MemResp   = Input(new ReadResp)

    // val Output 
    val output       = Decoupled(new ReadResp)

    val free      = Output(Bool())
    val done      = Output(Bool())
    })
}

class ReadTableEntry(id: Int)(implicit p: Parameters) extends ReadEntryIO()(p){

  val ID = RegInit(id.U)
  val request_R = RegInit(ReadReq.default)
  val request_valid_R = RegInit(false.B)
  // Data buffers for misaligned accesses

  // Mask for final ANDing and output of data
  val bitmask  = RegInit(0.U((2*xlen).W))
  // Send word mask for tracking how many words need to be read
  val sendbytemask = RegInit(0.U((2*xlen).W))

  // Is the request valid and request to memory
  val ReqValid     = RegInit(false.B)
  val ReqAddress   = RegInit(0.U(xlen.W))

  // Incoming data valid and daata operand.
  val DataValid    = RegInit(false.B)
  val ptr          = RegInit(0.U((2*xlen).W))
  val linebuffer   = RegInit(Vec(Seq.fill(2)(0.U(xlen.W))))
  val count        = RegInit(5.U(32.W))
  val xlen_bytes  =  xlen/8

  // State machine
  val s_idle :: s_Input :: s_SENDING :: s_RECEIVING  :: s_Done :: Nil = Enum(5)
  val state = RegInit(s_idle)

  // // Check if entry free. 
  // io.free := (state === s_idle) 
  // io.NodeReq.ready := (state === s_idle)
  // io.done := (state === s_Done)
  // when(io.NodeReq.fire()) {
  //   request_R := io.NodeReq.bits
  //   // Calculate things to start the sending process
  //   // Base word address
  //   ReqAddress     := (io.NodeReq.bits.address >> log2Ceil(xlen_bytes)) << log2Ceil(xlen_bytes)
  //   // Bitmask of data  for final ANDing
  //   bitmask        := ReadBitMask(io.NodeReq.Typ,io.NodeReq.address)
  //   // Bytemask of bytes within words that need to be fetched.
  //   sendbytemask   := ReadByteMask(io.NodeReq.Typ,io.NodeReq.address)
  //   // Next State
  //   state := s_SENDING
  // }

  // printf("\nMSHR %d: Inputs are Ready %d", ID, request_R.address)  
  // // when(start && state === s_idle) 
  // // {

  //   printf(p"State : $state")
  // //   // bitmask        := ReadBitMask(type_word,GepOperand)
  // //   // Two masks needed for keeping track of what is sent and received. 
  // //   // Could also use count. Going from mask to word count is difficult
  // //   // We simply use shifts in the mask to indicate if we have requested all words required
  // //   // sendbytemask   := ReadByteMask(type_word,GepOperand)
  
  // //    state      := s_SENDING
  // //   // Set the state to send
  // //   // Generate send mask
  // //   // Generate the based load addresses

  // // }
  // io.output.valid := 0.U
  // io.MemReq.valid := 0.U

  // when((state === s_SENDING) && (sendbytemask =/= 0.asUInt(16.W))) {
  //    printf("Requesting data %x", sendbytemask)
  //    io.MemReq.valid := 1.U          
  //    // io.MemReq.ready means arbitration succeeded and memory op has been passed on
  //    when(io.MemReq.fire()) {
  //     // Next word to be fetched
  //     ReqAddress   := ReqAddress + 1.U
  //     // Shift right by word length on machine. 
  //     sendbytemask := sendbytemask >> (xlen/8) 
  //     // Move to receiving data
  //     state := s_RECEIVING
  //   }
  // }

  // when ((state === s_RECEIVING) && (io.MemResp.valid === true.B)) {
  //  // Received data; concatenate into linebuffer 
  //  linebuffer(ptr) := io.MemResp.bits.data
  //  // Increment ptr to next entry in linebuffer (for next read)
  //  ptr := ptr + 1.U
  //  // Check if more data needs to be sent 
  //  val y = (sendbytemask === 0.asUInt(16.W))
  //  state := Mux(y,s_Done,s_SENDING)
  // }


  // when(state === s_SENDING) 
  // {
    
    
  //   // Set the state to send
  //   // Generate send mask
  //   // Generate the based load addresses

  // }.elsewhen((state === s_SENDING) && (sendbytemask =/= 0.asUInt(16.W))) {



  // }
  // when ((state === s_SENDING) && (count =/= 0.U))
  // {
  //   count := count - 1.U
  // }

  // when ((state === s_SENDING) && (count === 0.U))
  // {
  //   state := s_Done
  // }
  // when (state === s_Done)
  // {
  //   io.output.valid := 1.U
  //   io.output.bits.data := request_R.address
  //   io.output.bits.RouteID := request_R.RouteID
  //   io.output.bits.valid   := true.B
  //   // request_R.address
  //   when (io.output.fire())
  //   {
  //     state := s_idle
  //     count := 5.U
  //   }
  // }

}
  // abstract class ReadMMU(Nops: Int)(implicit p: Parameters) 
// extends Module with CoreParams {
//   val io = IO(new Bundle {
//     val in = Vec(Nops, Flipped(Decoupled(ReadReq)))
//     val out = Decoupled(Output(ReadResp))
//   })
// }

//   class ReadMMU[T <: Data](BaseSize: Int, NumOps: Int, gen: T)(implicit val p: Parameters) 
// extends AbstractArbiterTree(NumOps, gen)(p) {



// class MmuTestIO[Req <: ValidT with RouteID, Resp <: ValidT with RouteID](gen: T, n: Int) extends Bundle {

//   override def cloneType = new MmuTestIO(gen,n).asInstanceOf[this.type]
//   val ready = Output(Bool())
//   val valid = Output(Bool())
//   val bits = Output(gen)
//   val chosen = Output(UInt(log2Ceil(n).W))
// }

class MemoryController()(implicit val p: Parameters) extends
 Module with CoreParams{
   val io = IO(new Bundle {
     val ReadIn    = Vec(10,Flipped(Decoupled(new ReadReq())))
   })

   val MLP = 3

   // Input arbiter 
   val in_arb      = Module(new ArbiterTree(BaseSize = 2, NumOps = 2, new ReadReq()))
   // MSHR allocator
   val alloc_arb = Module(new Arbiter(Bool(), MLP))
   // Memory request

   // Memory response Demux
   // val memresp_demux = Module(new Demux(MemResp, MLP))




   val out_arb  = Module(new RRArbiter(new ReadResp, 3))
   val out_demux = Module(new DeMuxTree(BaseSize = 2, NumOps = 2, new ReadResp()))


   // val ReadMSHR1 = Module(new ReadTableEntry(1))
   // val ReadMSHR2 = Module(new ReadTableEntry(2))
   // val ReadMSHR3 = Module(new ReadTableEntry(3))

   val Counter  = RegInit(0.U(32.W))
   Counter := Counter + 1.U

    in_arb.io.in(0).bits.address := 100.U + Counter
    in_arb.io.in(0).bits.RouteID := 0.U
    in_arb.io.in(0).valid := true.B
    in_arb.io.in(1).bits.address := 200.U + Counter
    in_arb.io.in(1).bits.RouteID := 1.U
    in_arb.io.in(1).valid := true.B
    
    val valid = RegInit(false.B)
    valid := true.B
    // Tree.io.out.ready := true.B
    // alloc_arb.io.out.ready := true.B
    // ReadMSHR.io.NodeReq <> Tree.io.out

    // printf(p"\nTree Out: ${Tree.io.out} Alloc Out: {alloc_arb.io.out}")

    // val my_args = Seq(1,2,3)




    for (i <- 0 until 2) {
      val MSHR = Module(new ReadTableEntry(i))
      alloc_arb.io.in(i).valid := MSHR.io.free
      MSHR.io.NodeReq.valid := alloc_arb.io.in(i).ready
      MSHR.io.NodeReq.bits  := in_arb.io.out.bits
      out_arb.io.in(i)     <> MSHR.io.output

      // demux.io.input  <> MSHR.io.output
      // demux.io.sel  := MSHR.io.output.RouteID
      // demux.io.en   := MSHR.io.done
    }

    in_arb.io.out.ready := alloc_arb.io.out.valid
    alloc_arb.io.out.ready := in_arb.io.out.valid
    out_arb.io.out.ready  := true.B
    out_demux.io.enable      := out_arb.io.out.fire()
    out_demux.io.input       := out_arb.io.out.bits



    printf(p"demux: ${out_demux.io.outputs}")












    // alloc_arb.io.in(0).valid   := ReadMSHR1.io.free
    // alloc_arb.io.in(1).valid   := ReadMSHR2.io.free
    // alloc_arb.io.in(2).valid   := ReadMSHR3.io.free

    

    // ReadMSHR1.io.NodeReq.valid := alloc_arb.io.in(0).ready
    // ReadMSHR2.io.NodeReq.valid := alloc_arb.io.in(1).ready
    // ReadMSHR3.io.NodeReq.valid := alloc_arb.io.in(2).ready

    // If space is valid and incoming node has a valid request.
    // when (Tree.io.out.fire() && alloc_arb.io.out.fire())
    // {
    // alloc_arb.io.out.ready     := Tree.io.out.valid
    // Tree.io.out.ready          := alloc_arb.io.out.valid

    // ReadMSHR1.io.NodeReq.valid  := alloc_arb.io.in(0).fire() & Tree.io.out.fire()
    // ReadMSHR2.io.NodeReq.valid  := alloc_arb.io.in(1).fire() & Tree.io.out.fire()
    // ReadMSHR3.io.NodeReq.valid  := alloc_arb.io.in(2).fire() & Tree.io.out.fire()

    // ReadMSHR1.io.NodeReq.bits   := Tree.io.out.bits    
    // ReadMSHR2.io.NodeReq.bits   := Tree.io.out.bits    
    // ReadMSHR3.io.NodeReq.bits   := Tree.io.out.bits    
  // }
 }
//     val ReadOut   = Vec(NReads,Output(new ReadResp()))
//     val WriteIn   = Vec(NWrites,Flipped(Decoupled(new WriteReq())))
//     val WriteOut  = Vec(NWrites,Output(new WriteResp()))

//     //  Todo Create individual Test case for Demux
//     //  Todo To Test Demux
//     //    val testInput = Input(new ReadResp())
//     //    val testSel = Input(UInt(log2Ceil(NReads).W))
//     //    val testEn = Input(Bool())

//     // To test MMU
//     val testReadReq   = new MmuTestIO(new ReadReq(), NReads)
//   })

//   //----------------------------------------------------------------------------------------
//   // Connections
//   // ReadIn --> ReadArbiter -> mmu
//   // mmu --> Demux --> ReadOut

//   // Declaring Modules

//   //---------------------------------------------------------------------------------------
//   // Purpose of readInReady_R register
//   //---------------------------------------------------------------------------------------
//   // The Arbiter.In.Ready Signal is not always activated and
//   // cannot be depended upon. So "readInReady_R" register is used.
//   // This  register sends ready signal to ReadIn. The ReadIn will
//   // not send valid data if its input ready signal is false.
//   //
//   // By default this ready signal is true. Once, the arbiter
//   // selects a Node it is stored inside MMU. and the corresponding
//   // arbiter channel is switched off (by sending "false" to the ready
//   // signal of corresponding ReadIN)
//   // TODO Do not send false to ReadIn untill MMU has received data from the arbiter
//   // Todo By default Arbiter does not wait for the ready signal of MMU
//   // The switched off node will not send any valid data to the arbiter
//   // after this event.
//   //
//   // Once, the data/ack signal for the selected Node comes back from the memory
//   // MMU will send the response to that node.
//   // Note :Before the start of the next iteration a RESET signal should set all bits
//   // Note :corresponding to this node to false.


//   val readInReady_R = RegInit(Vec(Seq.fill(NReads)(false.B)))

//   //---------------------------------------------------------------------------------------
//   // MMU
//   val mmu = Module(new MMU(NReads,NWrites))

//   // ReadArbiter
//   //TODO We do not need nodeid's for ReadReq or WriteReq
//   // Since, Arbiters are already hardwired as of now to each Ld/St Node

//   // TODO: Do not depend on RRArbiter.in.ready to be true
//   val readArbiter  = Module(new RRArbiter(new ReadReq(),NReads))


//   // Demux
//   val readDemux   = Module(new Demux(new ReadResp(),NReads))

//   //----------------------------------------------------------------------------------------
//   // Connecting ReadIn with Read Arbiter

//   for (i <- 0 until NReads) {
//     //TODO readArbiter.io.in(i).ready signal is not always active
//     // and gets valid randomly.
//     // So make sure readInReady_R is used to connect to ReadIn(i).ready signal
//     //io.ReadIn(i).ready := readInReady_R(i)

//     readArbiter.io.in(i).bits := io.ReadIn(i).bits
//     readArbiter.io.in(i).valid := io.ReadIn(i).valid
//   }

//   for (i <- 0 until NReads) {
//     io.ReadIn(i).ready := ~readInReady_R(i)
//   }


//   //Signal Node that Data is received by the MMU only when
//   // MMU is ready to receive, Since Arbiter does not wait
//   // for any ready Signal.
//   when(readArbiter.io.out.valid && mmu.io.readReq.in.ready) {
//     readInReady_R(readArbiter.io.chosen) := true.B
//   }

//   //----------------------------------------------------------------------------------------
//   // Connection between Read Arbiter and mmu

//   mmu.io.readReq.in.bits <> readArbiter.io.out.bits
//   mmu.io.readReq.chosen <> readArbiter.io.chosen
//   mmu.io.readReq.in.ready <> readArbiter.io.out.ready
//   mmu.io.readReq.in.valid <> readArbiter.io.out.valid

//   //----------------------------------------------------------------------------------------
//   //Read Arbiter Test Circuit
//   io.testReadReq.bits := readArbiter.io.out.bits
//   io.testReadReq.chosen <> readArbiter.io.chosen
//   io.testReadReq.ready := mmu.io.readReq.in.ready
//   io.testReadReq.valid <> readArbiter.io.out.valid
//   //----------------------------------------------------------------------------------------
//   // ReadResponse - Connection between mmu and readDemux
//   // ToDo  Note Demux is not handshaking signal
//   // ToDo mmu.io.readResponse.ready signal unused
//   // Once decided --> need to fix ready

//   readDemux.io.sel        := mmu.io.readResp.chosen
//   // TODO ReadResponse valid is redundant
//   // Todo mmu.io.readResp.out.bits.valid is unused
//   readDemux.io.en         := mmu. io.readResp.out.valid
//   //    readDemux.io.en := false.B
//   readDemux.io.input:= mmu.io.readResp.out.bits
//   mmu.io.readResp.out.ready := true.B

//   //-----------------------------------------------------------------------------------------
//   // Todo Create separate Test case for Demux
//   // Testing Demux
//   //      readDemux.io.sel  := io.testSel
//   //      readDemux.io.en   := io.testEn
//   //      readDemux.io.input := io.testInput



//   //----------------------------------------------------------------------------------------
//   // Connection between readDemux and ReadOut

//   for (i <- 0 until NReads) {
//     //    io.ReadOut(i) <> readDemux.io.outputs(i)
//     io.ReadOut(i).valid <> readDemux.io.valids(i)
//     io.ReadOut(i).data <> readDemux.io.outputs(i).data
//   }


//   //  //----------------------------------------------------------------------------------------
//   //  // Write Arbiter
//   //
//   //  val WriteReqArbiter  = Module(new RRArbiter(new WriteReq(),NWrites));
//   //
//   //  // Connect up Write ins with arbiters
//   //  for (i <- 0 until NWrites) {
//   //    WriteReqArbiter.io.in(i) <> io.WriteIn(i)
//   //  }
//   //
//   //  val writeInArbiterAddress_R = RegInit(0.U(xlen.W))
//   //  val writeInArbiterId_R = RegInit(0.U(log2Up(NWrites).W))
//   //  val writeInArbiterValid_R = RegInit(false.B)
//   //
//   //  WriteReqArbiter.io.out.ready := true.B
//   //
//   //  writeInArbiterAddress_R := WriteReqArbiter.io.out.bits
//   //  writeInArbiterId_R := WriteReqArbiter.io.chosen
//   //  writeInArbiterValid_R := WriteReqArbiter.io.out.valid
//   //
//   //  mmu.io.writeReq.chosen := writeInArbiterId_R
//   //  mmu.io.write.valid  := writeInArbiterValid_R
//   //  mmu.In.write.data   := writeInArbiterAddress_R


//   //----------------------------------------------------------------------------------------
//   //  // Demux WriteResp
//   //
//   //  val writeRespDemux   = Module(new Demux(new WriteResp(),NWrites));
//   //
//   //  val writeOutArbiter_R = RegInit(0.U(xlen.W))
//   //  val writeOutChosen_R = RegInit(0.U(log2Up(NReads).W) )
//   //  val writeOutputValid_R = RegInit(false.B)
//   //
//   //  writeOutputValid_R    := mmu.Out.write.valid
//   //  writeOutChosen_R      := mmu.Out.write.chosen
//   //  writeOutArbiter_R     := mmu.Out.write.data
//   //
//   //
//   //
//   //  writeRespDemux.io.en    := writeOutputValid_R
//   //  writeRespDemux.io.sel   := writeOutChosen_R
//   //  writeRespDemux.io.input := writeOutArbiter_R
//   //
//   //  for (i <- 0 until NWrites) {
//   //    io.WriteOut(i) <> writeRespDemux.io.outputs(i)
//   //  }

//   // Feed arbiter output to Regfile input port.
//   //  RegFile.io.raddr1 := ReadArbiterReg.address
//   // Feed Regfile output port to Demux port
//   //  ReadRespDemux.io.input.data   := RegFile.io.rdata1
//   //  RegFile.io.wen := WriteInputValid
//   //  RegFile.io.waddr := WriteArbiterReg.address
//   //  RegFile.io.wdata := WriteArbiterReg.data
//   //  RegFile.io.wmask := WriteArbiterReg.mask

// }
