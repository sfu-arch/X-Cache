package dandelion.memory.cache

import chipsalliance.rocketchip.config._
import chisel3._
import chisel3.util._
import dandelion.config._
import dandelion.interfaces._


class DUTIO (implicit val p:Parameters) extends Bundle
with HasCacheAccelParams
with HasAccelShellParams {

    val instruction = Flipped(Decoupled(new InstBundle))
}

class DUT (implicit val p:Parameters) extends Module
with HasCacheAccelParams
with HasAccelShellParams {

    val io = IO(new DUTIO())

    val dut = Module(new programmableCache())
    RegNext(io.instruction.bits) <> dut.io.instruction.bits
    dut.io.instruction.valid := RegNext(io.instruction.valid)
    io.instruction.ready := dut.io.instruction.ready
}
