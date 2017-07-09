package memory

// Generic Packages
import chisel3._
import chisel3.Module
import chisel3.util._

// Modules needed
import arbiters._
import muxes._

// Config
import config._
import utility._
import interfaces._
import node._

// Cache requests
import accel._

abstract class ReadEntryIO()(implicit val p: Parameters)
  extends Module
  with CoreParams {

  val io = IO(new Bundle {
    // Read Request Type
    val NodeReq = Flipped(Decoupled(Input(new ReadReq)))
    val NodeResp = Decoupled(new ReadResp)

    //Memory interface
    val MemReq = Decoupled(new CacheReq)
    val MemResp = Input(new CacheResp)

    // val Output
    val output = Decoupled(new ReadResp)

    val free = Output(Bool())
    val done = Output(Bool())
  })
}
/**
 * @brief Read Table Entry
 * @details [long description]
 *
 * @param ID [Read table IDs]
 * @return [description]
 */
class ReadTableEntry(id: Int)(implicit p: Parameters) extends ReadEntryIO()(p) with UniformPrintfs {

  val ID = RegInit(id.U)
  val request_R = RegInit(ReadReq.default)
  val request_valid_R = RegInit(false.B)
  // Data buffers for misaligned accesses

   // Mask for final ANDing and output of data
  val bitmask = RegInit(0.U(((2) * xlen).W))
  // Send word mask for tracking how many words need to be read
  val sendbytemask = RegInit(0.U(((2) * xlen/8).W))

  // Is the request valid and request to memory
  val ReqValid = RegInit(false.B)
  val ReqAddress = RegInit(0.U(xlen.W))

  // Incoming data valid and data operand.
  val DataValid = RegInit(false.B)
  val ptr = RegInit(0.U(log2Ceil(2).W))
  val linebuffer = RegInit(Vec(Seq.fill(2)(0.U(xlen.W))))
  val xlen_bytes = xlen / 8
  val output = Wire(0.U(xlen.W))

  // State machine
  val s_idle :: s_SENDING :: s_RECEIVING :: s_Done :: Nil = Enum(4)
  val state = RegInit(s_idle)

 // Check if entry free.
/*================================================
=            Indicate Table State                =
=================================================*/


  // Table entry indicates free to outside world
  io.free := (state === s_idle)
  // Table entry ready to latch new requests
  io.NodeReq.ready := (state === s_idle)
  // Table entry to output demux
  io.done := (state === s_Done)

/*=================================================================
=            Default values for external communication            =
==================================================================*/
  io.output.valid := 0.U
  io.MemReq.valid := 0.U


/*=======================================================
=            Latch Inputs. Calculate masks              =
========================================================*/
  when(io.NodeReq.fire()) {
    request_R := io.NodeReq.bits
    // Calculate things to start the sending process
    // Base word address
    ReqAddress := (io.NodeReq.bits.address >> log2Ceil(xlen_bytes)) << log2Ceil(xlen_bytes)
    // Bitmask of data  for final ANDing
    bitmask := ReadBitMask(io.NodeReq.bits.Typ, io.NodeReq.bits.address,xlen)
    // Bytemask of bytes within words that need to be fetched.
    sendbytemask := ReadByteMask(io.NodeReq.bits.Typ, io.NodeReq.bits.address,xlen)
    // Next State
    state := s_SENDING
  }

  // printf("\nMSHR %d: Inputs are Ready %d", ID, request_R.address)
  // printf("\n MSHR %d State :%d RouteID %d ", ID, state, request_R.RouteID)
  // printf("\n  linebuffer %x & bitmask: %x", linebuffer.asUInt, bitmask)

/*===========================================================
=            Sending values to the cache request            =
===========================================================*/

  when((state === s_SENDING) && (sendbytemask =/= 0.asUInt((xlen/4).W))) {
    io.MemReq.bits.addr := ReqAddress + Cat(ptr,0.U(log2Ceil(xlen_bytes).W))
    io.MemReq.bits.tag := ID
    io.MemReq.valid := 1.U
    // io.MemReq.ready means arbitration succeeded and memory op has been passed on
    when(io.MemReq.fire()) {
      // Shift right by word length on machine.
      sendbytemask := sendbytemask >> (xlen / 8)
      // Move to receiving data
      state := s_RECEIVING
    }
  }

/*============================================================
=            Receiving values from cache response            =
=============================================================*/

  when((state === s_RECEIVING) && (io.MemResp.valid === true.B)) {
    // Received data; concatenate into linebuffer
    linebuffer(ptr) := io.MemResp.data
    // Increment ptr to next entry in linebuffer (for next read)
    ptr := ptr + 1.U
    // Check if more data needs to be sent
    val y = (sendbytemask === 0.asUInt((xlen/4).W))
    state := Mux(y, s_Done, s_SENDING)
  }

/*============================================================
=            Cleanup and send output                         =
=============================================================*/

  when(state === s_Done) {
    output := (linebuffer.asUInt & bitmask) >> Cat(request_R.address(log2Ceil(xlen_bytes) - 1, 0), 0.U(3.W))
    io.output.valid := 1.U
    // @error: To handle doubles this has to change.
    io.output.bits.data := Data2Sign(output,request_R.Typ)
    io.output.bits.RouteID := request_R.RouteID
    io.output.bits.valid := true.B
    // Output driver demux tree has forwarded output (may not have reached receiving node yet)
    when(io.output.fire()) {
      state := s_idle
      request_valid_R := false.B
    }
  }


   override val printfSigil = "UnTyp RD MSHR(" + ID + ")"
  if ((log == true) && (comp contains "RDMSHR")) {
    val x = RegInit(0.U(xlen.W))
    x     := x + 1.U

    verb match {
      case "high"  => { printf(p"\nUNTYP RD MSHR Time $x: Nodereq: $request_R "); printf(p"linebuffer: ${linebuffer}") }
      case "med"   => { printf(p"\nUNTYP RD MSHR Time $x: $io.MemReq"); printf(p"linebuffer: ${linebuffer}") }
      case "low"   => { printf(p"\nUNTYP RD MSHR Time $x: ") ; printf(p"Output bits : ${io.output.bits} Output Valid : ${io.output.valid}") }
    }
  }
}

abstract class RController(NumOps: Int, BaseSize: Int)(implicit val p: Parameters)
extends Module with CoreParams {
  val io = IO(new Bundle {
    val ReadIn = Vec(NumOps, Flipped(Decoupled(new ReadReq())))
    val ReadOut = Vec(NumOps, Output(new ReadResp()))
    val CacheReq = Decoupled(new CacheReq)
    val CacheResp = Flipped(Valid(new CacheResp))
  })
}


class ReadMemoryController
  (NumOps: Int,
  BaseSize: Int)
  (implicit p: Parameters)
  extends RController(NumOps,BaseSize)(p) {

  require(rdmshrlen >= 0)
  // Number of MLP entries
  val MLPSize = 1 << rdmshrlen
  // Input arbiter
  val in_arb = Module(new ArbiterTree(BaseSize = BaseSize, NumOps = NumOps, new ReadReq(), Locks = 1))
  // MSHR allocator
  val alloc_arb = Module(new Arbiter(Bool(), MLPSize))

  // Memory request
  val cachereq_arb = Module(new Arbiter(new CacheReq, MLPSize))
  // Memory response Demux
  val cacheresp_demux = Module(new Demux(new CacheResp, MLPSize))

  // Output arbiter and demuxes
  val out_arb = Module(new RRArbiter(new ReadResp, MLPSize))
  val out_demux = Module(new DeMuxTree(BaseSize = BaseSize, NumOps = NumOps, new ReadResp()))

/*=====================================================================
=            Wire up incoming reads from nodes to ReadMSHR            =
=====================================================================*/

  // Wire up input with in_arb
  for (i <- 0 until NumOps) {
    in_arb.io.in(i) <> io.ReadIn(i)
  }

/*=============================================
=           Declare Read Table                =
=============================================*/

  // Create ReadTable
  val ReadTable = for (i <- 0 until MLPSize) yield {
    val read_entry = Module(new ReadTableEntry(i))
    read_entry
  }

/*=========================================================================
=            Wire up arbiters and demux to read table entries
             1. Allocator arbiter
             2. Output arbiter
             3. Output demux
             4. Cache request arbiter
             5. Cache response demux                                                             =
=========================================================================*/

  for (i <- 0 until MLPSize) {
    // val MSHR = Module(new ReadTableEntry(i))
    // Allocator wireup with table entries
    alloc_arb.io.in(i).valid := ReadTable(i).io.free
    ReadTable(i).io.NodeReq.valid := alloc_arb.io.in(i).ready
    ReadTable(i).io.NodeReq.bits := in_arb.io.out.bits

    // Table entries -> CacheReq arbiter.
    cachereq_arb.io.in(i) <> ReadTable(i).io.MemReq

    // CacheResp -> Table entries Demux
    ReadTable(i).io.MemResp <> cacheresp_demux.io.outputs(i)

    // Table entries -> Output arbiter
    out_arb.io.in(i) <> ReadTable(i).io.output
  }

  //  Handshaking input arbiter with allocator
  in_arb.io.out.ready := alloc_arb.io.out.valid
  alloc_arb.io.out.ready := in_arb.io.out.valid

  // Cache request arbiter
  // cachereq_arb.io.out.ready := io.CacheReq.ready
  io.CacheReq <> cachereq_arb.io.out

  // Cache response Demux
  cacheresp_demux.io.en := io.CacheResp.valid
  cacheresp_demux.io.input := io.CacheResp.bits
  cacheresp_demux.io.sel := io.CacheResp.bits.tag

  // Output arbiter -> Demux
  out_arb.io.out.ready := true.B
  out_demux.io.enable := out_arb.io.out.fire()
  out_demux.io.input := out_arb.io.out.bits

  // printf(p"\n Demux Out: ${out_demux.io.outputs}")

}