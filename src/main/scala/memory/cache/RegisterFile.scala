package memGen.memory.cache

import chisel3._
import chisel3.util._
import chisel3.util.experimental._

class RegisterFileIO[T <: Data] (OperandType: T, val addressWidth :Int) extends Bundle {
    val read_addr1 = Input(UInt(addressWidth.W))
    val read_en1 = Input(Bool())

    val read_addr2 = Input(UInt(addressWidth.W))
    val read_en2 = Input(Bool())

    val write_addr = Input(UInt(addressWidth.W))
    val write_data = Input(OperandType.cloneType)
    val write_en = Input(Bool())

    val output1 = Output(OperandType.cloneType)
    val output2 = Output(OperandType.cloneType)

}

class RegisterFile[T <: Data] (OperandType: T, val addressWidth :Int, val regFileSize: Int) 
    extends Module {
        
    val io = IO (new RegisterFileIO(OperandType, addressWidth))

    val result1 = Wire(OperandType.cloneType);
    val result2 = Wire(OperandType.cloneType);
    
    val reg_file = Reg(Vec(regFileSize, OperandType.cloneType));

    result1 := 0.U;
    result2 := 0.U;

    when (io.read_en1) { result1 := reg_file(io.read_addr1); }
    when (io.read_en2) { result2 := reg_file(io.read_addr2); }
    when (io.write_en) { reg_file(io.write_addr) := io.write_data; }

    io.output1 := result1;
    io.output2 := result2;

}
