package memGen.memory.cache

import chipsalliance.rocketchip.config._
import chisel3._
import chisel3.util._
import memGen.config._
import memGen.interfaces._
import memGen.interfaces.axi._
import memGen.memory.message.IntraNodeBundle



class memGenTopLevelIO(implicit val p:Parameters) extends Bundle
with HasCacheAccelParams
with HasAccelShellParams {

    val instruction = Flipped(Decoupled(new IntraNodeBundle()))
    val resp = Decoupled(new IntraNodeBundle())
    val mem = new AXIMaster(memParams)
}

class memGenTopLevel(implicit val p:Parameters) extends Module
with HasCacheAccelParams
with HasAccelShellParams {

    val io = IO(new memGenTopLevelIO())

    // io.mem <> DontCare
    val cacheNode = Module(new CacheNode())
    val memCtrl = Module(new memoryWrapper())
//    RegNext(io.instruction.bits) <> dut.io.instruction.bits
//    dut.io.instruction.valid := RegNext(io.instruction.valid)
//    io.instruction.ready := dut.io.instruction.ready

    (io.instruction.bits) <> cacheNode.io.in.cpu.bits
    cacheNode.io.in.cpu.valid:= (io.instruction.valid)
    io.instruction.ready := cacheNode.io.in.cpu.ready

    io.mem <> memCtrl.io.mem

        val memCtrlInputQueue = Module(new Queue(new IntraNodeBundle(), entries = 5))
        memCtrlInputQueue.io.enq.bits.data <> cacheNode.io.out.network.bits.data
        memCtrlInputQueue.io.enq.bits.addr <> cacheNode.io.out.network.bits.addr
        memCtrlInputQueue.io.enq.bits.inst <> cacheNode.io.out.network.bits.inst
        memCtrlInputQueue.io.enq.valid := cacheNode.io.out.network.valid

        io.resp.bits.addr := cacheNode.io.out.cpu.bits.addr
        io.resp.bits.data := cacheNode.io.out.cpu.bits.data
        io.resp.bits.inst := cacheNode.io.out.cpu.bits.inst
        io.resp.valid := cacheNode.io.out.cpu.valid
        cacheNode.io.out.cpu.ready := io.resp.ready

        memCtrl.io.in.bits <> memCtrlInputQueue.io.deq.bits
        memCtrl.io.in.valid := memCtrlInputQueue.io.deq.valid
        memCtrlInputQueue.io.deq.ready :=  memCtrl.io.in.ready
//    memCtrl.io.in <> cacheNode.io.out
    // printf(p" valid :${memCtrl.io.in.valid} , addr: ${memCtrl.io.in.bits.addr} \n")

//    memCtrl
      cacheNode.io.in.memCtrl.bits.inst  := memCtrl.io.out.bits.inst
      cacheNode.io.in.memCtrl.bits.addr := memCtrl.io.out.bits.addr
      cacheNode.io.in.memCtrl.bits.data := memCtrl.io.out.bits.data
      cacheNode.io.in.memCtrl.valid     :=  memCtrl.io.out.valid


//        cacheNode.io.in.memCtrl <> DontCare

//    memCtrl.io.in := cacheNode.




}
