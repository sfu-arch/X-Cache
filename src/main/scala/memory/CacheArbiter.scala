package memory


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


abstract class CacheArbiterIO(NumPorts:Int)(implicit val p: Parameters)
  extends Module with CoreParams  with UniformPrintfs {
  val io = IO(new Bundle {
    val cpu = new Bundle {
      val CacheReq   = Vec(NumPorts, Flipped(Decoupled(new CacheReq)))
      val CacheResp  = Vec(NumPorts, Output(Valid(new CacheResp)))
    }
    val cache = new Bundle {
      val CacheReq   = Decoupled(new CacheReq)
      val CacheResp  = Input(Valid(new CacheResp))
    }
  })
}

class CacheArbiter(NumPorts:Int)(implicit p: Parameters) extends CacheArbiterIO(NumPorts)(p) {

  val reqArb  = Module(new RRArbiter(new CacheReq, NumPorts))
  reqArb.io.in <> io.cpu.CacheReq
  val chosen_reg = RegInit(0.U)
  when (reqArb.io.out.fire()) {
    chosen_reg := reqArb.io.chosen
  }
  io.cache.CacheReq <> reqArb.io.out

  // Response Demux
  for(i <- 0 until NumPorts) {
    io.cpu.CacheResp(i) := io.cache.CacheResp
    io.cpu.CacheResp(i).valid := false.B  // default
  }
  io.cpu.CacheResp(chosen_reg).valid := io.cache.CacheResp.valid

}
