package memGen.memory.cache

import chipsalliance.rocketchip.config._
import chisel3._
import chisel3.util._
import memGen.config._
import chisel3.util.experimental._

import memGen.interfaces._
import memGen.interfaces.axi._
import memGen.noc._
import memGen.memory.message._


class Bore(val ID:Int =0)(implicit p: Parameters) extends Module  {

    val nEvents = 17
    
    val io = IO(new Bundle{
        val events = Output (Vec(nEvents - 1, UInt(32.W)))
    })

    val names = Vector(s"missLD_$ID","hitLD", "instCount", "cpuReq", "memCtrlReq", "ldReq" )
    io.events := DontCare

    val boreWire = WireInit(VecInit(Seq.fill(nEvents - 1)(false.B)))

    val cntWire = for (i <- 0 until nEvents - 1) yield {
       Counter(boreWire(i),10000000 )
    }

    io.events <> (cntWire.map(i => i._1)).toVector

    for(i <- 0 until names.size){
         BoringUtils.addSink(boreWire(i), names(i))
    }
  

}

class memGenTopLevelIO(implicit val p:Parameters) extends Bundle
with HasCacheAccelParams
with HasAccelShellParams {

    val instruction = Vec(nCache, Flipped(Decoupled(new IntraNodeBundle())))
    val resp = Vec(nCache, Decoupled(new IntraNodeBundle()))
    val events = Valid(Vec(16, UInt(32.W)))
    val mem = new AXIMaster(memParams)
}

class memGenTopLevel(val numMemCtrl:Int = 1) (implicit val p:Parameters) extends Module
with HasCacheAccelParams
with HasAccelShellParams {


    val io = IO(new memGenTopLevelIO())
    val numCache = nCache

    val memCtrl = Module(new memoryWrapper(ID = (nCache + numMemCtrl - 1))(p))
    val memCtrlInputQueue = Module(new Queue(new Flit(), entries = 256))
    val routerNode = for (i <- 0 until nCache + numMemCtrl) yield {
        val Router = Module(new Router(ID = i))
        Router
    }
    val cacheNode = for (i <- 0 until nCache) yield {
        val Cache = Module (new CacheNode(UniqueID = i))
        Cache
    }

    val bore = Module(new Bore())


    io.events.valid := true.B
    io.events.bits <> DontCare


    for (i <- 0 until nCache) {
        (io.instruction(i).bits) <> cacheNode(i).io.in.cpu.bits
        cacheNode(i).io.in.cpu.valid := (io.instruction(i).valid)
        io.instruction(i).ready := cacheNode(i).io.in.cpu.ready
        io.resp(i) <>  cacheNode(i).io.out.cpu
    }

    for (i <- 0 until nCache){
        routerNode(i).io.cacheIn <> cacheNode(i).io.out.network
        cacheNode(i).io.in.network <> routerNode(i).io.cacheOut
    }

    memCtrlInputQueue.io.enq <> routerNode(nCache + numMemCtrl - 1).io.cacheOut
    memCtrl.io.in <> memCtrlInputQueue.io.deq
    routerNode(nCache + numMemCtrl - 1).io.cacheIn <> memCtrl.io.out
    io.mem <> memCtrl.io.mem

    for (i <- 0 until nCache + numMemCtrl - 1){
         routerNode(i+1).io.in <> routerNode(i).io.out
    }
    routerNode(0).io.in <> routerNode(nCache + numMemCtrl - 1).io.out

}
