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


abstract class RWController(implicit val p: Parameters)
  extends Module with CoreParams  with UniformPrintfs {
  val io = IO(new Bundle {
    val ReadCacheReq = Flipped(Decoupled(new CacheReq))
    val WriteCacheReq = Flipped(Decoupled(new CacheReq))
//    val CacheResp = new CacheRespT
    val CacheResp = Flipped(new CacheRespT)

    val ReadCacheResp = new CacheResp
    val WriteCacheResp = new CacheResp
    val CacheReq = Decoupled(new CacheReq)

  })
}


class ReadWriteArbiter()
  (implicit p: Parameters)
  extends RWController()(p) {

  //ToDo : Need to remove this 
  val MLPSize = 2
  val RdIdx = 0
  val WrIdx = 1


    // Memory request
  val cachereq_arb = Module(new Arbiter(new CacheReq, MLPSize))
  // Memory response Demux
  val cacheresp_demux = Module(new Demux(new CacheResp, MLPSize))

  override val printfSigil = "ReadWriteArbiter: "

  //-----------------------------------
  // Driver Circuit
  io.ReadCacheReq.ready := io.CacheReq.ready
  io.WriteCacheReq.ready := io.CacheReq.ready


  //-----------------------------------

  // Table entries -> CacheReq arbiter.
  cachereq_arb.io.in(RdIdx) <> io.ReadCacheReq
  cachereq_arb.io.in(WrIdx) <> io.WriteCacheReq

  // Cache request arbiter
  cachereq_arb.io.out.ready := io.CacheReq.ready
  io.CacheReq.bits :=  cachereq_arb.io.out.bits
  io.CacheReq.valid := io.ReadCacheReq.valid | io.WriteCacheReq.valid



  //-----------------------------------
  // CacheResp -> Table entries Demux
  // cacheresp_demux.io.outputs.bits.isSt is an extra field not in Rd/WrCacheResp
  io.ReadCacheResp <> cacheresp_demux.io.outputs(RdIdx)
  io.WriteCacheResp <> cacheresp_demux.io.outputs(WrIdx)

  //-----------------------------------
  // Driver Circuit
  // Cache response Demux
  cacheresp_demux.io.en := io.CacheResp.valid
  cacheresp_demux.io.input := io.CacheResp
  //Note RdIdx == 0 , so is isSt for Loads
  //ToDO this could be dangerous - fix this
  cacheresp_demux.io.sel := io.CacheResp.isSt
  //-----------------------------------


//  assert(!io.CacheResp.valid, " CACHE RESPONSE IS VALID ")
  printf(s" io.Cache Resp valid: %x isSt: %x  tag: %x \n", io.CacheResp.valid ,io.CacheResp.isSt, io.CacheResp.tag )

}
