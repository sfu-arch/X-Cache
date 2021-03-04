package memGen.memory.cache

import chipsalliance.rocketchip.config._
import chisel3._
import chisel3.util._
import memGen.config._
import memGen.interfaces._
import memGen.interfaces.axi._
import memGen.memory.message._

//class CacheInputBundle[T <: Bundle ](val gen: T) (implicit val p:Parameters) extends Bundle with HasCacheAccelParams{
//  val cpu = gen.cloneType
//  val memCtrl = Flipped(Decoupled(gen.cloneType))
//}

class CacheNodeIO (implicit val p:Parameters) extends Bundle with HasCacheAccelParams{

  val in = new Bundle{
    val cpu = Flipped(Decoupled((new IntraNodeBundle())))
    val memCtrl = Flipped(Decoupled((new IntraNodeBundle())))
  }
  val out = new Bundle{
    val network = Valid(new MessageBundle())
    val cpu = Decoupled(new IntraNodeBundle())
  }
}

class CacheNode (val UniqueID : Int = 0)(implicit val p:Parameters) extends Module with HasCacheAccelParams{

  val io = IO(new CacheNodeIO())
  val ID = WireInit(UniqueID.U)

//  val packetizer = Module(new Packetizer(IntraNodeBundle.default))
//  val depacketizer = Module(new Depacketizer(IntraNodeBundle.default))

  val cache = Module(new programmableCache())

  val cpuQueue = Module(new Queue(new IntraNodeBundle(), entries = 1, pipe = true))
  val memCtrlQueue= Module(new Queue(new IntraNodeBundle(), entries = 1, pipe = true))

  cpuQueue.io.enq <> io.in.cpu
  memCtrlQueue.io.enq <> io.in.memCtrl

  cache.io.in.cpu.bits.event := cpuQueue.io.deq.bits.inst
  cache.io.in.cpu.bits.addr  := cpuQueue.io.deq.bits.addr
  cache.io.in.cpu.bits.data  := cpuQueue.io.deq.bits.data
  cache.io.in.cpu.valid := cpuQueue.io.deq.valid
  cpuQueue.io.deq.ready := cache.io.in.cpu.ready

  cache.io.in.memCtrl.bits.event := memCtrlQueue.io.deq.bits.inst
  cache.io.in.memCtrl.bits.addr  := memCtrlQueue.io.deq.bits.addr
  cache.io.in.memCtrl.bits.data  := memCtrlQueue.io.deq.bits.data
  cache.io.in.memCtrl.valid := memCtrlQueue.io.deq.valid
  memCtrlQueue.io.deq.ready := cache.io.in.memCtrl.ready

  io.out.valid := cache.io.out.valid
  io.out.bits.src := ID
  io.out.bits.dst := cache.io.out.bits.dst
  io.out.bits.inst:= cache.io.out.bits.req.inst
  io.out.bits.data := cache.io.out.bits.req.data
  io.out.bits.addr := cache.io.out.bits.req.addr

}