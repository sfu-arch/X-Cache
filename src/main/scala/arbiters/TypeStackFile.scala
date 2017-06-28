package arbiters
import scala.math._
import chisel3._
import chisel3.util._
import chisel3.Module
import chisel3.testers._
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import org.scalatest.{Matchers, FlatSpec}

import regfile._
import config._
import util._
import interfaces._
import muxes._
import accel._
import memory._
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

class  TypeStackFile(Size: Int, NReads: Int, NWrites: Int)(implicit val p: Parameters) extends Module with CoreParams {

  val io = IO(new Bundle {
    val WriteIn  = Vec(NWrites, Flipped(Decoupled(new WriteReq())))
    val WriteOut = Vec(NWrites, Output(new WriteResp()))
    val ReadIn   = Vec(NReads, Flipped(Decoupled(new ReadReq())))
    val ReadOut  = Vec(NReads, Flipped(Decoupled(new ReadReq())))
  })
  require(Size > 0)
  require(isPow2(Size))
  // Initialize a vector of register files (as wide as type).
  val RegFile          = Module(new RFile(Size)(p))
  val WriteController  = Module(new WriteNMemoryController(NWrites,2))
  val ReadController   = Module(new ReadMemoryController(NReads,2))

  // State machine
  val write_valid_R = RegInit(false.B)
  val writereq         = Reg(new CacheReq())

  // State machine
  val read_valid_R = RegInit(false.B)
  val readreq         = Reg(new CacheReq())

  val xlen_bytes = xlen/8
  val wordindex  = log2Ceil(xlen_bytes)

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

 WriteController.io.CacheResp.valid  := false.B
 ReadController.io.CacheResp.valid   := false.B  

  WriteController.io.CacheReq.ready := ~write_valid_R
  when (WriteController.io.CacheReq.fire())
  {
    write_valid_R    := true.B
    writereq         := WriteController.io.CacheReq.bits
    RegFile.io.wen   := true.B
    RegFile.io.waddr := WriteController.io.CacheReq.bits.addr(wordindex+log2Ceil(Size)-1,wordindex)
    RegFile.io.wdata := WriteController.io.CacheReq.bits.data
    RegFile.io.wmask := WriteController.io.CacheReq.bits.mask
  }

  when (write_valid_R)
  {
    WriteController.io.CacheResp.valid       := true.B
    WriteController.io.CacheResp.bits.tag    := writereq.tag
    write_valid_R                         := false.B
  }

/*==============================================
=            Read Memory Controller            =
==============================================*/
  WriteController.io.CacheReq.ready := ~read_valid_R
  when (ReadController.io.CacheReq.fire())
  {
    read_valid_R   := true.B
    readreq           := ReadController.io.CacheReq.bits
    RegFile.io.raddr1 := WriteController.io.CacheReq.bits.addr(wordindex+log2Ceil(Size)-1,wordindex)
   }

  when (read_valid_R)
  {
    ReadController.io.CacheResp.valid       := true.B
    ReadController.io.CacheResp.bits.tag    := readreq.tag
    ReadController.io.CacheResp.bits.data   := RegFile.io.rdata1
    read_valid_R                            := false.B
  }

  // RegFile.io.raddr1 := 2.U
  RegFile.io.raddr1 := 2.U

  printf(p"RegFile Input: ${Hexadecimal(RegFile.io.rdata1)}")

  // Read in parallel after shifting.
  // seq
  // for (i <- 0 until Typ_SZ)
  // {

}
