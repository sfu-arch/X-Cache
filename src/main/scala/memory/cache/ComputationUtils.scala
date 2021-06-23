package memGen.memory.cache

import chisel3._
import chisel3.util._
import chisel3.util.experimental._

class ComputationALUInput[T <: Data](val OperandType: T) extends Bundle {
    val hardCoded = OperandType.cloneType
    val data = OperandType.cloneType
    val tbe = OperandType.cloneType
    val select = UInt(2.W)
}

class Mux3 [T <: Data] (val OperandType: T) extends Module {
    val io = IO( new Bundle {
        val in = Input(new ComputationALUInput(OperandType))
        val out = Output(OperandType.cloneType)
    })

    val result = Wire(OperandType.cloneType);
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

