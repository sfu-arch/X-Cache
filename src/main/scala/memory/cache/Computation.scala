package memGen.memory.cache

import chisel3._
import chisel3.util._
import chisel3.util.experimental._

// functions: add: 0, sub: 1, mult: 2, shift_r: 3, shift_l: 4 
//
// opcodes: 
//  ---------------------------
// | 00 | operand1, operand2   |
// | 01 | operand1, address_2  |
// | 10 | address_1, address_2 |
//  ---------------------------
//
// Instruction:
//  ----------------------- ---------------------- ------------ ---------- -------
// |  address_2/ operand_2 | address_1/ operand1 | write_addr | function | opcode |
//  ----------------------- ---------------------- ------------ ---------- -------
// 

class Computation [T <: UInt] (
    val OperandType: T, 
    val opcodeWidth :Int = 2, 
    val funcWidth: Int = 3
    val regFileSize: Int = 16,
    val addressWidth :Int = 16, 
    val instructionWidth: Int = 41,
) 
extends Module {
    def ALU(function: UInt, op1: T, op2: T) = {
        val result = Wire(OperandType.cloneType);
        result := 0.U;

        switch (function) {
            is (add) { result := op1 + op2; }
            is (sub) { result := op1 - op2; }
            is (mult) { result := op1 * op2; }
            is (shift_r) { result := op1 >> op2; }
            is (shift_l) { result := op1 << op2; }
        }
        result
    }

    def OpcodeEnd() = opcodeWidth;
    def FunctionEnd() = OpcodeEnd() + funcWidth;
    def DestEnd() = FunctionEnd() + log2Ceil(regFileSize);
    def Operand1End() = DestEnd() + OperandType.cloneType.getWidth;
    def Operand2End() = Operand1End() + OperandType.cloneType.getWidth;
    def Addr1End() =  DestEnd() + addressWidth;

    val io = IO (new Bundle {
        val instruction = Input(UInt(instructionWidth.W))        
        val output = Output(OperandType.cloneType)
        val reg_file = Output(Vec(regFileSize, OperandType.cloneType))
    })

    val add :: sub :: mult :: shift_r :: shift_l :: Nil = Enum (5)
    val result = Wire(OperandType.cloneType);
    val reg_out1 = Wire(OperandType.cloneType);
    val reg_out2 = Wire(OperandType.cloneType);
    
    // *******************************************  FETCH  *******************************************
    val opcode      = io.instruction(OpcodeEnd()-1, 0);
    val function    = io.instruction(FunctionEnd()-1, OpcodeEnd());
    val write_addr  = io.instruction(DestEnd()-1, FunctionEnd());
    val operand1    = io.instruction(Operand1End()-1, DestEnd());
    val operand2    = io.instruction(Operand2End()-1, Operand1End());
    val read_addr1  = io.instruction(Addr1End()-1, DestEnd());
    val read_addr2  = io.instruction(instructionWidth-1, instructionWidth - addressWidth);
    printf(s"opcode: %d\nfunction: %d\nwrite_addr: %d\noperand1: %d\n, operand2: %d\n, read_addr1: %d\n, read_addr2: %d\n",
     opcode, function, write_addr, operand1, operand2, read_addr1, read_addr2);
    val alu_in1 = Wire(OperandType.cloneType);
    val alu_in2 = Wire(OperandType.cloneType);

    alu_in1 := operand1;
    alu_in2 := operand2;

    when (opcode === 1.U) { 
        alu_in1 := operand1; 
        alu_in2 := reg_out2; 
    } .elsewhen (opcode === 2.U) {
        alu_in1 := reg_out1; 
        alu_in2 := reg_out2; 
    }

    // *******************************************  Register File IO  *******************************************
    val reg_file = Reg(Vec(regFileSize, OperandType.cloneType));
    val write_en = write_addr =/= 0.U;
    when (write_en) { reg_file(write_addr) := result; }
    reg_out1 := reg_file(read_addr1);
    reg_out2 := reg_file(read_addr2);
    io.reg_file := reg_file;
    
    // *******************************************  ALU  *******************************************
    result := ALU(function, alu_in1, alu_in2)
    io.output := result;

}

object Computation extends App {
  (new chisel3.stage.ChiselStage).emitVerilog(new Computation(UInt(16.W)))
}
