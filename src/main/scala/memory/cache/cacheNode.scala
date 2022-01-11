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
    val network = Flipped(Decoupled(new Flit()))
  }
  val out = new Bundle{
    val network = Decoupled(new Flit())
    val cpu = Decoupled(new IntraNodeBundle())
  }
}

class CacheNode ( UniqueID : Int = 0)(implicit val p:Parameters) extends Module with HasCacheAccelParams{

  val io = IO(new CacheNodeIO())

  val ID = WireInit (UniqueID.U)
  val cache = Module(new programmableCache(UniqueID))
  val cpuQueue = Module(new Queue(new IntraNodeBundle(), entries = 1, pipe = true))
  val memCtrlQueue = Module(new Queue(new IntraNodeBundle(), entries = 1, pipe = true))
  val otherNodesQueue = Module(new Queue(new IntraNodeBundle(), entries = 1, pipe = true))

  cpuQueue.io.enq <> io.in.cpu
  memCtrlQueue.io.enq.bits.addr :=  io.in.network.bits.addr
  memCtrlQueue.io.enq.bits.data :=  io.in.network.bits.data
  memCtrlQueue.io.enq.bits.inst :=  io.in.network.bits.inst

  otherNodesQueue.io.enq.bits.addr :=  io.in.network.bits.addr
  otherNodesQueue.io.enq.bits.data :=  io.in.network.bits.data
  otherNodesQueue.io.enq.bits.inst :=  io.in.network.bits.inst

  io.in.network.ready := memCtrlQueue.io.enq.ready && otherNodesQueue.io.enq.ready

  memCtrlQueue.io.enq.valid := false.B
  otherNodesQueue.io.enq.valid := false.B

  when(io.in.network.fire()){
    // should be generic with enum, but it causes bug!
    when(io.in.network.bits.msgType === 0.U){
      memCtrlQueue.io.enq.valid := true.B
    }.elsewhen(io.in.network.bits.msgType === 1.U){
      otherNodesQueue.io.enq.valid := true.B
    }
  }

  cache.io.in.cpu.bits.event := cpuQueue.io.deq.bits.inst
  cache.io.in.cpu.bits.addr  := cpuQueue.io.deq.bits.addr
  cache.io.in.cpu.bits.data  := cpuQueue.io.deq.bits.data
  cache.io.in.cpu.valid      := cpuQueue.io.deq.valid
  cpuQueue.io.deq.ready := cache.io.in.cpu.ready

  cache.io.in.memCtrl.bits.event := memCtrlQueue.io.deq.bits.inst
  cache.io.in.memCtrl.bits.addr  := memCtrlQueue.io.deq.bits.addr
  cache.io.in.memCtrl.bits.data  := memCtrlQueue.io.deq.bits.data
  cache.io.in.memCtrl.valid      := memCtrlQueue.io.deq.valid
  memCtrlQueue.io.deq.ready := cache.io.in.memCtrl.ready

  cache.io.in.otherNodes.bits.event := otherNodesQueue.io.deq.bits.inst
  cache.io.in.otherNodes.bits.addr  := otherNodesQueue.io.deq.bits.addr
  cache.io.in.otherNodes.bits.data  := otherNodesQueue.io.deq.bits.data
  cache.io.in.otherNodes.valid      := otherNodesQueue.io.deq.valid
  otherNodesQueue.io.deq.ready := cache.io.in.otherNodes.ready



  io.out.network.valid := cache.io.out.req.valid
  io.out.network.bits.src := ID
  io.out.network.bits.dst := cache.io.out.req.bits.dst
  io.out.network.bits.inst:= cache.io.out.req.bits.req.inst
  io.out.network.bits.data := cache.io.out.req.bits.req.data
  io.out.network.bits.addr := cache.io.out.req.bits.req.addr
  io.out.network.bits.msgType := 0.U 

  io.out.cpu.bits.data := cache.io.out.resp.bits.data
  io.out.cpu.bits.addr := cache.io.out.resp.bits.addr
  io.out.cpu.bits.inst := cache.io.out.resp.bits.inst
  io.out.cpu.valid := cache.io.out.resp.valid
  cache.io.out.resp.ready := true.B

}
