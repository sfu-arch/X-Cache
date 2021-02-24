package memGen.memory.cache

import chipsalliance.rocketchip.config._
import chisel3._
import chisel3.util._
import memGen.config._
import memGen.interfaces._
import memGen.interfaces.axi._
import memGen.memory.message._

class CacheInputBundle[T <: Bundle ](val gen: T) (implicit val p:Parameters) extends Bundle with HasCacheAccelParams{
  val cpu = Flipped(Decoupled(gen.cloneType))
  val memCtrl = Flipped(Decoupled(gen.cloneType))
}

class CacheNodeIO (implicit val p:Parameters) extends Bundle with HasCacheAccelParams{

  val in = new CacheInputBundle(new IntraNodeBundle())
  val out = Valid(new MessageBundle())
}

class CacheNode (val UniqueID : Int = 0)(implicit val p:Parameters) extends Module with HasCacheAccelParams{

  val io = IO(new CacheNodeIO())
  val ID = WireInit(UniqueID.U)

//  val packetizer = Module(new Packetizer(IntraNodeBundle.default))
//  val depacketizer = Module(new Depacketizer(IntraNodeBundle.default))

  val cache = Module(new programmableCache())

  cache.io.instruction.bits.addr := io.in.bits.addr
  cache.io.instruction.bits.data := io.in.bits.data
  cache.io.instruction.bits.event:= io.in.bits.inst // shouldn't be mapped directly
  cache.io.instruction.valid := io.in.valid
  io.in.ready := cache.io.instruction.ready

  io.out.valid := cache.io.out.valid
  io.out.bits.src := ID
  io.out.bits.dst := cache.io.out.bits.dst
  io.out.bits.inst:= cache.io.out.bits.req.inst
  io.out.bits.data := cache.io.out.bits.req.data
  io.out.bits.addr := cache.io.out.bits.req.addr

}