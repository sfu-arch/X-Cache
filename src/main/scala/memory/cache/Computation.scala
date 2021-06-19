package memGen.memory.cache

import chisel3._
import chisel3.util._
import chisel3.util.experimental._

// Opcodes: add: 0, sub: 1, mult: 2, shift_r: 3, shift_l: 4 
class Computation [T <: UInt] (val OperandType: T, val addressWidth :Int = 16, val opcodeWidth :Int = 6, val regFileSize: Int = 16) extends Module {
    def ALU(opcode: UInt, op1: T, op2: T) = {
        val result = Wire(OperandType.cloneType);
        result := 0.U;

        switch (io.opcode) {
            is (add) { result := op1 + op2; }
            is (sub) { result := op1 - op2; }
            is (mult) { result := op1 * op2; }
            is (shift_r) { result := op1 >> op2; }
            is (shift_l) { result := op1 << op2; }
        }
        result
    }

    val io = IO (new Bundle {
        val operand1 =  Input(new ComputationALUInput(OperandType))
        val operand2 =  Input(new ComputationALUInput(OperandType))
        val opcode = Input(UInt(opcodeWidth.W))
        
        val write = Input(Valid(OperandType.cloneType))
        
        val read_en1 = Input(Bool())
        val read_addr1 = Input(UInt(addressWidth.W))
        
        val read_en2 = Input(Bool())
        val read_addr2 = Input(UInt(addressWidth.W))
        
        val output = Output(OperandType.cloneType)
        val reg_file = Output(Vec(regFileSize, OperandType.cloneType))
    })

    val add :: sub :: mult :: shift_r :: shift_l :: Nil = Enum (5)
    val result = Wire(OperandType.cloneType);


    // *******************************************  Register File IO  *******************************************
    val reg_out1 = Wire(OperandType.cloneType);
    val reg_out2 = Wire(OperandType.cloneType);
    val reg_file = Reg(Vec(regFileSize, OperandType.cloneType));
    when (io.write.fire()) { reg_file(io.write.bits) := result; }
    reg_out1 := reg_file(io.read_addr1);
    reg_out2 := reg_file(io.read_addr2);
    io.reg_file := reg_file;
    
    // *******************************************  ALU IO  *******************************************
    
    val operand1_mux = Module(new Mux3(OperandType));
    val operand2_mux = Module(new Mux3(OperandType));
    
    val op1 = Wire(OperandType.cloneType);
    val op2 = Wire(OperandType.cloneType);
    val alu_in1 = Wire(OperandType.cloneType);
    val alu_in2 = Wire(OperandType.cloneType);
    
    operand1_mux.io.in <> io.operand1;
    operand2_mux.io.in <> io.operand2;

    op1 <> operand1_mux.io.out;
    op2 <> operand2_mux.io.out;

    alu_in1 := op1;
    alu_in2 := op2;

    when (io.read_en1) { alu_in1 := reg_out1; }
    when (io.read_en2) { alu_in2 := reg_out2; }

    result := ALU(io.opcode, alu_in1, alu_in2)
    
    io.output := result;
}

object Computation extends App {
  (new chisel3.stage.ChiselStage).emitVerilog(new Computation(UInt(16.W)))
}
