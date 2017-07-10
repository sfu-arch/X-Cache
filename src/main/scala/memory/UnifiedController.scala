package memory

/**
  * Created by vnaveen0 on 10/7/17.
  */



package arbiters
import scala.math._
import chisel3._
import chisel3.util._
import chisel3.Module
import chisel3.testers._
import chisel3.iotesters.{ ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester }
import org.scalatest.{ Matchers, FlatSpec }

import regfile._
import config._
import util._
import interfaces._
import muxes._
import accel._
import memory._
import utility.Constants._
import utility.UniformPrintfs

//XXX
//TODO put VEC insid Outputs
//OUTPUT(VECXXX)
//
//TODO Make readOut and readValid as a bundle
//

/**
 * @param Size    : Size of Register file to be allocated and managed
 * @param NReads  : Number of static reads to be connected. Controls size of arbiter and Demux
 * @param NWrites : Number of static writes to be connected. Controls size of arbiter and Demux
 */

class UnifiedController (ID: Int,
  Size: Int,
  NReads: Int,
  NWrites: Int)(WControl: => WriteTypMemoryController)(RControl: => ReadTypMemoryController)(RWArbiter: => ReadWriteArbiter )(implicit val p: Parameters)
  extends Module
  with CoreParams
  with UniformPrintfs {

  val io = IO(new Bundle {
    val WriteIn = Vec(NWrites, Flipped(Decoupled(new WriteReq())))
    val WriteOut = Vec(NWrites, Output(new WriteResp()))
    val ReadIn = Vec(NReads, Flipped(Decoupled(new ReadReq())))
    val ReadOut = Vec(NReads, Output(new ReadResp()))

    //orig
    val CacheResp = Flipped(Valid(new CacheRespT))
    val CacheReq = Decoupled(new CacheReq)

  })

  require(Size > 0)
  require(isPow2(Size))

/*====================================
 =            Declarations            =
 ====================================*/
  val cacheReq_R = RegInit(CacheReq.default)
  val cacheResp_R = RegInit(CacheRespT.default)

  // Initialize a vector of register files (as wide as type).
//  val RegFile = Module(new RFile(Size)(p))
  val WriteController = Module(WControl)
  val ReadController = Module(RControl)
  val ReadWriteArbiter = Module(RWArbiter)

  // Write registers
  val WriteReq     = RegNext(next = WriteController.io.CacheReq.bits)
  val WriteValid   = RegNext(init  = false.B,next=WriteController.io.CacheReq.fire())

  val ReadReq     = RegNext(next = ReadController.io.CacheReq.bits)
  val ReadValid   = RegNext(init  = false.B, next=ReadController.io.CacheReq.fire())


  val xlen_bytes = xlen / 8
  val wordindex = log2Ceil(xlen_bytes)

/*================================================
=            Wiring up input arbiters            =
================================================*/

  // Connect up Write ins with arbiters
  for (i <- 0 until NWrites) {
    WriteController.io.WriteIn(i) <> io.WriteIn(i)
    io.WriteOut(i) <> WriteController.io.WriteOut(i)
  }

  // Connect up Read ins with arbiters
  for (i <- 0 until NReads) {
    ReadController.io.ReadIn(i) <> io.ReadIn(i)
    io.ReadOut(i) <> ReadController.io.ReadOut(i)
  }

  // Connect Read/Write Controllers to ReadWrite Arbiter
  ReadController.io.CacheReq <> ReadWriteArbiter.io.ReadCacheReq
  ReadController.io.CacheResp <> ReadWriteArbiter.io.ReadCacheResp

  WriteController.io.CacheReq <> ReadWriteArbiter.io.WriteCacheReq
  WriteController.io.CacheResp <> ReadWriteArbiter.io.WriteCacheResp

  // Connecting CacheReq/Resp
  val (sIdle :: sReq :: sResp :: sDone :: Nil) = Enum(4)
  val state = RegInit(init = sIdle)

  switch(state) {
    is(sIdle){
      when(ReadWriteArbiter.io.CacheReq.fire()) {
        cacheReq_R := ReadWriteArbiter.io.CacheReq.bits
        state := sReq
      }
    }

    is(sReq) {

      ReadWriteArbiter.io.CacheReq.ready := false.B
      when(io.CacheReq.fire()) {
        state := sResp
      }
    }

    is(sResp){
      when(io.CacheResp.valid){
        cacheResp_R := io.CacheResp.bits
        state := sDone
      }
    }

    is(sDone) {
      when(ReadWriteArbiter.io.CacheResp.fire()) {
        state := sIdle
      }
    }
  }

  ReadWriteArbiter.io.CacheReq.ready := state === sIdle
  io.CacheReq.valid  := state === sReq
  io.CacheReq.bits  := cacheReq_R

  ReadWriteArbiter.io.CacheResp.valid := state === sDone
  ReadWriteArbiter.io.CacheResp.bits := cacheResp_R


  //--------------------------

  //------------------------------------------------------------------------------------
  /// Printf debugging
  override val printfSigil = "RFile: " + ID + " Type " + (Typ_SZ)


  // printf(p"\n : ${ReadController.io.CacheReq.fire()} Tag: ${ReadReq.tag} ")
  // printf(p"\n Cache Request ${WriteController.io.CacheReq}")
  //  printf(p"Demux out:  ${io.WriteOut(0)}")

}
