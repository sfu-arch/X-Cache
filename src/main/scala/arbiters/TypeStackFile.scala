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
    // val ReadIn   = Vec(NWrites, Flipped(Decoupled(new ReadReq())))
    // val ReadOut  = Vec(NWrites, Flipped(Decoupled(new ReadReq())))
  })
  require(Size > 0)
  require(isPow2(Size))
  // Initialize a vector of register files (as wide as type).
  val RegFile     = Module(new RFile(Size)(p))
  val Controller  = Module(new WriteNMemoryController(NWrites,2))
  // State machine
  val request_valid_R = RegInit(false.B)
  val request         = Reg(new CacheReq())

   // Connect up Write ins with arbiters
  for (i <- 0 until NWrites) {
    Controller.io.WriteIn(i) <> io.WriteIn(i)
  }
  Controller.io.CacheReq.ready := ~request_valid_R
  val xlen_bytes = xlen/8
  val wordindex  = log2Ceil(xlen_bytes)
  when (Controller.io.CacheReq.fire())
  {
    request_valid_R  := true.B
    request          := Controller.io.CacheReq.bits
    RegFile.io.wen   := true.B
    RegFile.io.waddr := Controller.io.CacheReq.bits.addr(wordindex+log2Ceil(Size)-1,wordindex)
    RegFile.io.wdata := Controller.io.CacheReq.bits.data
    RegFile.io.wmask := Controller.io.CacheReq.bits.mask
  }
  Controller.io.CacheResp.valid  := false.B

  when (request_valid_R)
  {
    Controller.io.CacheResp.valid  := true.B
    Controller.io.CacheResp.bits.tag    := request.tag
    Controller.io.CacheResp.bits.data   := 0x1eadbeef.U
    request_valid_R := false.B
  }

  // RegFile.io.raddr1 := 2.U
  RegFile.io.raddr1 := 1.U

  printf(p"RegFile Input: ${Hexadecimal(RegFile.io.rdata1)}")

  // Read in parallel after shifting.
  // seq
  // for (i <- 0 until Typ_SZ)
  // {


  // }

  //DEBUG prinln
  //TODO make them as a flag
  // printf("Write Data:%x\n", {WriteArbiterReg.data})
}
