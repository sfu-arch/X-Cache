package memGen.memory.cache

import chisel3._
import chisel3.util._
import chipsalliance.rocketchip.config._
import memGen.config._
import memGen.util._
import memGen.interfaces._
import memGen.interfaces.axi._
import memGen.memory.message._
import chisel3.util.experimental.loadMemoryFromFile
import memGen.junctions._
import memGen.shell._



class memoryWrapperIO (implicit val p:Parameters) extends Bundle
with HasAccelShellParams
with HasCacheAccelParams {

    val in = Flipped(Decoupled( new IntraNodeBundle()))
    val mem = new AXIMaster(memParams)
    val out = Valid(new IntraNodeBundle())

//    mem.tieoff()

}

class memoryWrapper ()(implicit val p:Parameters) extends Module
with HasCacheAccelParams
with HasAccelShellParams{
    val io = IO(new memoryWrapperIO)
    io.mem <> DontCare

    val addrReg = RegInit(0.U(addrLen.W))
    val dataRegRead = RegInit(VecInit(Seq.fill(nData)(0.U(xlen.W))))
    val dataRegWrite = RegInit(VecInit(Seq.fill(nData)(0.U(xlen.W))))


    val start = Wire(Bool())
    start := io.in.fire()

    when (start){
        addrReg := io.in.bits.addr
    }

    val (rd :: wr_back :: Nil ) = Enum(2)
    val (stIdle :: stWriteAddr :: stWriteData :: stReadAddr :: stReadData :: stCmdIssue :: Nil) = Enum(6)
    val stReg = RegInit(stIdle)

    io.in.ready := stReg === stIdle

    val writeInst = WireInit(start & io.in.bits.inst === wr_back)

    // Reading
    val (readCount, readWrapped) = Counter (io.mem.r.fire(), nData)
    val (writeCount, writeWrapped) = Counter(io.mem.w.fire(), nData)

    io.mem.ar.bits.addr := Mux(stReg === stReadAddr, addrReg , 0.U(addrLen))

    when(writeInst){
        dataRegWrite := io.in.bits.data
    }

    when(io.mem.r.fire()) {
        dataRegRead(readCount) := io.mem.r.bits.data
    }
    io.mem.aw.bits.addr := Mux(stReg === stWriteAddr, addrReg, 0.U(addrLen.W))
    io.mem.w.bits.data := Mux(stReg === stWriteData, dataRegWrite(writeCount), 0.U(xlen.W))

    val issueCmd = Wire(Bool())
    issueCmd := false.B


    io.out.bits.data := Cat(dataRegRead)
    io.out.bits.addr := addrReg
    io.out.bits.inst := Events.EventArray("DATA").U
    io.out.valid := false.B


    switch(stReg){
        is(stIdle){
            when (start){
                when(writeInst){
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
                stReg := stCmdIssue
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
        is(stCmdIssue){
            io.out.valid := true.B
            stReg := stIdle
        }
    }

}