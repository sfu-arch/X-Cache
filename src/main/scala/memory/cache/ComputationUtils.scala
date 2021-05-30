package memGen.memory.cache

import chisel3._
import chisel3.util._
import chisel3.util.experimental._

class ComputationALUInput (val operandWidth:Int = 16) extends Bundle {
    val hardCoded = UInt(operandWidth.W)
    val data = UInt(operandWidth.W)
    val tbe = UInt(operandWidth.W)
    val select = UInt(2.W)
}

class ComputationMemInput (val addressWidth:Int = 16) extends Bundle {
    val addr1 = UInt(addressWidth.W)
    val addr2 = UInt(addressWidth.W)
}

class Mux3 (val operandWidth :Int = 16) extends Module {
    val io = IO( new Bundle {
        val in = Input( new ComputationALUInput(operandWidth))
        val out = Output(UInt(operandWidth.W))
    })

    val result = Wire(UInt(operandWidth.W));
    result := 0.U;
    when (io.in.select === 0.U) {
        result := io.in.hardCoded;
    } .elsewhen (io.in.select === 1.U) {
        result := io.in.data;
    } .elsewhen (io.in.select === 2.U) {
        result := io.in.tbe;
    }

    io.out := result;
}