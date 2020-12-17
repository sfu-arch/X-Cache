package dandelion.memory.cache

import chisel3._
import chisel3.util._
import chipsalliance.rocketchip.config._
import dandelion.config._
import dandelion.util._
import dandelion.interfaces._
import dandelion.interfaces.axi._
import chisel3.util.experimental.loadMemoryFromFile

import dandelion.junctions._
import dandelion.shell._



class memoryWrapperIO (implicit val p:Parameters) extends Bundle
with HasAccelShellParams
with HasCacheAccelParams {

    val address = Flipped(Decoupled(UInt(addrLen.W)))
    val data = Flipped(Decoupled(Vec(nData,UInt(xlen.W))))
    val mem = new AXIMaster(memParams)
    val event = Output(UInt(eventLen.W)) // eventLen should be replaced with proper Parameter which defines length of event

    mem.tieoff()

}

class memoryWrapper ()(implicit val p:Parameters) extends Module
with HasCacheAccelParams
with HasAccelShellParams{
    val io = IO(new memoryWrapperIO)

    val addrReg = RegInit(0.U(addrLen.W))
    val dataReg = Reg(VecInit(Seq.fill(nData)(0.U(xlen.W))))



    val start = Wire(Bool())

    start := io.address.fire()

    when (io.address.fire()){
        addrReg := io.address.bits.asUInt()
    }

    val (stIdle :: stWriteAddr :: stWriteData :: stReadAddr :: stReadData :: stCmdIssue :: Nil) = Enum(6)
    val stReg = RegInit(stIdle)

    // Reading
    val (readCount, readWrapped) = Counter (io.mem.r.fire(), nData)

    io.mem.ar.bits.addr := Mux(stReg === stReadAddr, addrReg , 0.U(addrLen))

    when(io.mem.r.fire()) {
        dataReg(readCount) := io.mem.r.bits.data
    }


    // Writing
    val (writeCount, writeWrapped) = Counter(io.mem.w.fire(), nData)
    when(io.data.fire()){
        dataReg := io.data
    }
    io.mem.aw.bits.addr := Mux(stReg === stWriteAddr, addrReg, 0.U(addrLen.W))
    io.mem.w.bits.data := Mux(stReg === stWriteData, dataReg(writeCount), 0.U(xlen.W))


    switch(stReg){
        is(stIdle){
            when (start){
                when(io.data.fire()){
                    stReg := stWriteAddr
                }.otherwise{
                    stReg := stReadData
                }
            }
        }
        is(stReadAddr){
            io.mem.ar.valid := true.B
            when(io.mem.ar.fire()){
                stReg := stReadData
            }
        }
        is(stReadData) {
            io.mem.r.ready := true.B
            when(readWrapped) {
                stReg := stIdle
            }
        }
        is(stWriteAddr){
            io.mem.aw.valid := true.B
            when(io.mem.aw.fire()){
                stReg := stWriteData
            }
        }
        is (stWriteData){
            io.mem.w.valid := true.B
            when(writeWrapped){
                stReg := stIdle
            }
        }
        // data must be put on network and issue Data routine event
    }

}