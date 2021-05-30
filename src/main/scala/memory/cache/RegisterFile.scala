package memGen.memory.cache

import chisel3._
import chisel3.util._
import chisel3.util.experimental._

class RegisterFileIO(val addressWidth :Int = 16, val operandWidth :Int = 16) extends Bundle {
    val read_addr1 = Input(UInt(addressWidth.W))
    val read_en1 = Input(Bool())

    val read_addr2 = Input(UInt(addressWidth.W))
    val read_en2 = Input(Bool())

    val write_addr = Input(UInt(addressWidth.W))
    val write_data = Input(UInt(operandWidth.W))
    val write_en = Input(Bool())

    val output1 = Output(UInt(operandWidth.W))
    val output2 = Output(UInt(operandWidth.W))

}

class RegisterFile (val operandWidth :Int = 16, val regFileSize: Int = 16) extends Module {
    val io = IO (new RegisterFileIO())

    val result1 = Wire(UInt(operandWidth.W));
    val result2 = Wire(UInt(operandWidth.W));
    
    val reg_file = Mem(regFileSize, UInt(operandWidth.W));

    result1 := 0.U;
    result2 := 0.U;

    when (io.read_en1) { result1 := reg_file(io.read_addr1); }
    when (io.read_en2) { result2 := reg_file(io.read_addr2); }
    when (io.write_en) { reg_file(io.write_addr) := io.write_data; }

    io.output1 := result1;
    io.output2 := result2;

}
