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
    val CacheResp = Flipped(Valid(new CacheResp))

    val ReadCacheResp = Valid(new CacheResp)
    val WriteCacheResp = Valid(new CacheResp)
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
  val cachereq_arb = Module(new RRArbiter(new CacheReq, MLPSize))
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
  io.ReadCacheResp.bits <> cacheresp_demux.io.outputs(RdIdx)
  io.ReadCacheResp.valid := cacheresp_demux.io.outputs(RdIdx).valid
  io.WriteCacheResp.bits <> cacheresp_demux.io.outputs(WrIdx)
  io.WriteCacheResp.valid := cacheresp_demux.io.outputs(WrIdx).valid

  //-----------------------------------
  // Driver Circuit
  // Cache response Demux
  cacheresp_demux.io.en := io.CacheResp.valid
  cacheresp_demux.io.input := io.CacheResp.bits
  //Note RdIdx == 0 , so is isSt for Loads
  //ToDO this could be dangerous - fix this
  cacheresp_demux.io.sel := io.CacheResp.bits.iswrite
  //-----------------------------------

//  assert(!io.CacheResp.valid, " CACHE RESPONSE IS VALID ")


//  verb match {
//      case "high"  => {
//
//        printfInfo(s" INPUT.READREQ: valid: %d ready: %d addr: %d data: %d, iswrite: %d \n", io.ReadCacheReq.valid,
//          io.ReadCacheReq.ready, io.ReadCacheReq.bits.addr, io.ReadCacheReq.bits.data, io.ReadCacheReq.bits.iswrite)
//        printfInfo(s"INPUT.WRITEREQ: valid: %d ready:%d addr: %d data:%d iswrite:%d \n", io.WriteCacheReq.valid,
//          io.WriteCacheReq.ready, io.WriteCacheReq.bits.addr, io.WriteCacheReq.bits.data, io.WriteCacheReq.bits.iswrite)
//
//        printfInfo(s" OUTPUT Req valid: %d addr: %d data:%d  tag: %d  ready:%d iswrite:%d \n", io.CacheReq.valid ,
//          io.CacheReq.bits.addr,io.CacheReq.bits.data ,io.CacheReq.bits.tag, io.CacheReq.ready, io.CacheReq.bits.iswrite )
//
//
//        printfInfo(s" OUTPUT Resp valid: %d isSt: %d  tag: %d \n", io.CacheResp.valid ,io.CacheResp.bits.isSt, io.CacheResp.bits.tag )
//
//
//
//      }
//    }

}
