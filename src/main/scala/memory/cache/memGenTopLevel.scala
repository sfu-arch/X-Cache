package memGen.memory.cache

import chipsalliance.rocketchip.config._
import chisel3._
import chisel3.util._
import memGen.config._
import memGen.interfaces._
import memGen.interfaces.axi._



class memGenTopLevelIO(implicit val p:Parameters) extends Bundle
with HasCacheAccelParams
with HasAccelShellParams {

    val instruction = Flipped(Decoupled(new InstBundle))
    val mem = new AXIMaster(memParams)
}

class memGenTopLevel(implicit val p:Parameters) extends Module
with HasCacheAccelParams
with HasAccelShellParams {

    val io = IO(new memGenTopLevelIO())

    io.mem <> DontCare
    val dut = Module(new programmableCache())
//    RegNext(io.instruction.bits) <> dut.io.instruction.bits
//    dut.io.instruction.valid := RegNext(io.instruction.valid)
//    io.instruction.ready := dut.io.instruction.ready

    (io.instruction.bits) <> dut.io.instruction.bits
    dut.io.instruction.valid := (io.instruction.valid)
    io.instruction.ready := dut.io.instruction.ready


}
