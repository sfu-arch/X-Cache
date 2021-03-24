package memGen.memory.cache

import chipsalliance.rocketchip.config._
import chisel3._
import chisel3.util._
import memGen.config._
import memGen.interfaces._
import memGen.interfaces.axi._
import memGen.noc._
import memGen.memory.message._



class memGenTopLevelIO( implicit val p:Parameters) extends Bundle
with HasCacheAccelParams
with HasAccelShellParams {

    val instruction = Flipped(Decoupled(new IntraNodeBundle()))
    val resp = Decoupled(new IntraNodeBundle())
    val mem = new AXIMaster(memParams)
}

class memGenTopLevel(val numCache:Int =1, val numMemCtrl:Int = 1, implicit val p:Parameters) extends Module
with HasCacheAccelParams
with HasAccelShellParams {

    val io = IO(new memGenTopLevelIO())

    val cacheNode = for (i <- 0 until numCache) yield {
        val Cache = Module (new CacheNode(UniqueID = i))
        Cache
    }
    val routerNode = for (i <- 0 until numCache + numMemCtrl) yield {
        val Router = Module(new Router(ID = i))
        Router
    }
    val memCtrl = Module(new memoryWrapper(ID = (numCache + numMemCtrl - 1))(p))
    val memCtrlInputQueue = Module(new Queue(new Flit(), entries = 16))

    for (i <- 0 until numCache) {
        (io.instruction.bits) <> cacheNode(i).io.in.cpu.bits
        cacheNode(i).io.in.cpu.valid := (io.instruction.valid)
        io.instruction.ready := cacheNode(i).io.in.cpu.ready
        io.resp <>  cacheNode(i).io.out.cpu
    }

    for (i <- 0 until numCache){
        routerNode(i).io.cacheIn <> cacheNode(i).io.out.network
        cacheNode(i).io.in.network <> routerNode(i).io.cacheOut
    }

    memCtrlInputQueue.io.enq <> routerNode(numCache + numMemCtrl).io.cacheOut
    memCtrl.io.in <> memCtrlInputQueue.io.deq
    routerNode(numCache + numMemCtrl).io.cacheIn <> memCtrl.io.out
    io.mem <> memCtrl.io.mem

    for (i <- 0 until numCache + numMemCtrl - 1){
         routerNode(i+1).io.in <> routerNode(i).io.out
    }
    routerNode(0).io.in <> routerNode(numCache + numMemCtrl).io.out

}
