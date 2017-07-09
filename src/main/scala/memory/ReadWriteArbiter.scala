package memory

/**
  * Created by vnaveen0 on 9/7/17.
  */
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


abstract class RWController(NumOps: Int, BaseSize: Int)(implicit val p: Parameters)
  extends Module with CoreParams {
  val io = IO(new Bundle {
    val ReadCacheReq = Decoupled(new CacheReq)
    val ReadCacheResp = Flipped(Valid(new CacheResp))
    val WriteCacheReq = Decoupled(new CacheReq)
    val WriteCacheResp = Flipped(Valid(new CacheResp))
    val CacheReq = Decoupled(new CacheReq)
    val CacheResp = Flipped(Valid(new CacheRespT))

  })
}


class ReadWriteArbiter
  (NumOps: Int, BaseSize: Int)
  (implicit p: Parameters)
  extends RWController(NumOps,BaseSize)(p) {

  val MLPSize = 2
  val RdIdx = 0
  val WrIdx = 1
    // Memory request
  val cachereq_arb = Module(new Arbiter(new CacheReq, MLPSize))
  // Memory response Demux
  val cacheresp_demux = Module(new Demux(new CacheResp, MLPSize))

  // Table entries -> CacheReq arbiter.
  cachereq_arb.io.in(RdIdx) <> io.ReadCacheReq
  cachereq_arb.io.in(WrIdx) <> io.WriteCacheReq

  // Cache request arbiter
  // cachereq_arb.io.out.ready := io.CacheReq.ready
  io.CacheReq <> cachereq_arb.io.out


  // CacheResp -> Table entries Demux
  // cacheresp_demux.io.outputs.bits.isSt is an extra field not in Rd/WrCacheResp
  io.ReadCacheResp <> cacheresp_demux.io.outputs(RdIdx)
  io.WriteCacheResp <> cacheresp_demux.io.outputs(WrIdx)

  // Cache response Demux
  cacheresp_demux.io.en := io.CacheResp.valid
  cacheresp_demux.io.input := io.CacheResp.bits
  //Note RdIdx == 0 , so is isSt for Loads
  cacheresp_demux.io.sel := io.CacheResp.bits.isSt

}
