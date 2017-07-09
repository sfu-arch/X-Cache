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

// Cache requests
import accel._



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