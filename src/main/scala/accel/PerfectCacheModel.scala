// See LICENSE for license details.

package accel

import chisel3._
import chisel3.util._

import junctions._
import config._
import interfaces._
import NastiConstants._
import accel._

class PerfectCacheIO(implicit p: Parameters) extends CoreBundle()(p) with CacheParams {
  val init  = Flipped(Valid(new InitParams()(p)))
  val cache = new CacheIO()
}

class PerfectCacheModel(val depth : Int = 1<<16)(implicit val p: Parameters) extends Module with CacheParams {
  val io = IO(new PerfectCacheIO)

  val size  = log2Ceil(nastiXDataBits / 8).U
  val len   = (dataBeats - 1).U

  val mem = Mem(depth, UInt(nastiXDataBits.W))

  io.cache.req.ready := true.B

  when(io.cache.req.fire() && io.cache.req.bits.iswrite) {
    mem.write((io.cache.req.bits.addr >> size).asUInt(), io.cache.req.bits.data)
  }.elsewhen(io.init.valid){
    mem.write((io.init.bits.addr >> size).asUInt(), io.init.bits.data)
  }

  val resp_R = Reg(io.cache.resp.bits.cloneType)
  resp_R.data := mem.read((io.cache.req.bits.addr >> size).asUInt())
  resp_R.iswrite := io.cache.req.bits.iswrite
  resp_R.tag := io.cache.req.bits.tag
  resp_R.valid := io.cache.req.valid
  io.cache.resp.valid := resp_R.valid
  io.cache.resp.bits := resp_R

}
