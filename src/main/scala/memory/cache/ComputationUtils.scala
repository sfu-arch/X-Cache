package memGen.memory.cache

import chisel3._
import chisel3.util._
import chisel3.util.experimental._

class ComputationALUInput[T <: UInt](val OperandType: T) extends Bundle {
    val hardCoded = OperandType.cloneType
    val data = OperandType.cloneType
    val tbe = OperandType.cloneType
    val select = UInt(2.W)
}


// reg = 0, tbe = 1, data = 2, hard = 3
class Mux3 [T <: UInt] (OperandType: T) extends Module {
    val io = IO( new Bundle {
        val in = Input(new ComputationALUInput(OperandType))
        val out = Valid(OperandType.cloneType)
    })


    val result = Wire(OperandType.cloneType);
    when (io.in.select === 3.U) {
        result := io.in.hardCoded;
    } .elsewhen (io.in.select === 2.U) {
        result := io.in.data;
    } .elsewhen (io.in.select === 1.U) {
        result := io.in.tbe;
    }.otherwise{
        result := 0.U
    }

    io.out.bits := result
    io.out.valid := (result =/= 0.U)
}

