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
  NWrites: Int)(WControl: => WriteMemoryController)(RControl: => ReadMemoryController)(RWArbiter: => ReadWriteArbiter )(implicit val p: Parameters)
  extends Module
  with CoreParams
  with UniformPrintfs {

  val io = IO(new Bundle {
    val WriteIn = Vec(NWrites, Flipped(Decoupled(new WriteReq())))
    val WriteOut = Vec(NWrites, Output(new WriteResp()))
    val ReadIn = Vec(NReads, Flipped(Decoupled(new ReadReq())))
    val ReadOut = Vec(NReads, Output(new ReadResp()))

    //orig
    val CacheResp = Flipped(new CacheRespT)
    val CacheReq = Decoupled(new CacheReq)

  })

  require(Size > 0)
  require(isPow2(Size))

/*====================================
 =            Declarations            =
 ====================================*/

  // Initialize a vector of register files (as wide as type).
  val RegFile = Module(new RFile(Size)(p))
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


//  //-- Connect to RW Arbiter
//  WriteController.io.CacheReq.ready := true.B
//  ReadController.io.CacheReq.ready := true.B
//
//
//  WriteController.io.CacheResp.valid := false.B
//  ReadController.io.CacheResp.valid := false.B

/*==========================================
//=            Write Controller.             =
//==========================================*/
//
//  RegFile.io.wen := WriteController.io.CacheReq.fire()
//  RegFile.io.waddr := WriteController.io.CacheReq.bits.addr(wordindex + log2Ceil(Size) - 1, wordindex)
//  RegFile.io.wdata := WriteController.io.CacheReq.bits.data
//  RegFile.io.wmask := WriteController.io.CacheReq.bits.mask
//
//
//  WriteController.io.CacheResp.valid    := WriteValid
//  WriteController.io.CacheResp.bits.tag := WriteReq.tag
//
//
///*==============================================
//=            Read Memory Controller            =
//==============================================*/
//
//  when(ReadController.io.CacheReq.fire()) {
//    RegFile.io.raddr1 := ReadController.io.CacheReq.bits.addr(wordindex + log2Ceil(Size) - 1, wordindex)
//  }
//
//  ReadController.io.CacheResp.valid     := ReadValid
//  ReadController.io.CacheResp.bits.tag  := ReadReq.tag
//  ReadController.io.CacheResp.bits.data := RegFile.io.rdata1


  //------------------------------------------------------------------------------------
  /// Printf debugging
  override val printfSigil = "RFile: " + ID + " Type " + (Typ_SZ)


  // printf(p"\n : ${ReadController.io.CacheReq.fire()} Tag: ${ReadReq.tag} ")
  // printf(p"\n Cache Request ${WriteController.io.CacheReq}")
  //  printf(p"Demux out:  ${io.WriteOut(0)}")

}
