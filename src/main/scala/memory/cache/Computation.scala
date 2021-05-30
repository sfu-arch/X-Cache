package memGen.memory.cache

import chisel3._
import chisel3.util._
import chisel3.util.experimental._


// Opcodes: add: 0, sub: 1, mult: 2, shift_r: 3, shift_l: 4 


class Computation (val operandWidth :Int = 16, val addressWidth :Int = 16, val opcodeWidth :Int = 6, val regFileSize: Int = 16) extends Module {
    def ALU(opcode: UInt, op1: UInt, op2: UInt) = {
        val result = Wire(UInt(operandWidth.W));
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

    def RegIOConnect(
        reg_file: RegisterFile, 
        
        read_addr1: UInt, 
        read_en1: Bool, 
        read_addr2: UInt, 
        read_en2: Bool, 

        write_addr: UInt,
        write_data: UInt,
        write_en: Bool,

        output1: UInt,
        output2: UInt
    ) {
        reg_file.io.read_addr1 <> read_addr1;
        reg_file.io.read_en1 <> read_en1;

        reg_file.io.read_addr2 <> read_addr2;
        reg_file.io.read_en2 <> read_en2;

        reg_file.io.write_data <> write_data;
        reg_file.io.write_addr <> write_addr;
        reg_file.io.write_en <> write_en;

        reg_file.io.output1 <> output1;
        reg_file.io.output2 <> output2;

    }

    val io = IO (new Bundle {
        val operand1 =  Input(new ComputationALUInput(operandWidth))
        val operand2 =  Input(new ComputationALUInput(operandWidth))
        val opcode = Input(UInt(opcodeWidth.W))
        
        val write_addr = Input(UInt(addressWidth.W))
        val write_en = Input(Bool())

        val read_en1 = Input(Bool())
        val read_addr1 = Input(UInt(addressWidth.W))
        
        val read_en2 = Input(Bool())
        val read_addr2 = Input(UInt(addressWidth.W))
        
        val output = Output(UInt(operandWidth.W))
    })

    val add :: sub :: mult :: shift_r :: shift_l :: Nil = Enum (5)

    val operand1_mux = Module(new Mux3(operandWidth));
    val operand2_mux = Module(new Mux3(operandWidth));

    val result = Wire(UInt(operandWidth.W));
    
    val op1 = Wire(UInt(operandWidth.W));
    val op2 = Wire(UInt(operandWidth.W));
    
    val alu_in1 = Wire(UInt(operandWidth.W));
    val alu_in2 = Wire(UInt(operandWidth.W));

    val reg_out1 = Wire(UInt(operandWidth.W));
    val reg_out2 = Wire(UInt(operandWidth.W));

    val reg_file = Module(new RegisterFile());
    reg_file.io <> DontCare;
    
    RegIOConnect(
        reg_file = reg_file, 

        read_addr1 = io.read_addr1, 
        read_en1 = io.read_en1, 
        read_addr2 = io.read_addr2, 
        read_en2 = io.read_en2, 

        write_addr = io.write_addr,
        write_data = result,
        write_en = io.write_en,

        output1 = reg_out1,
        output2 = reg_out2
    );

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
  (new chisel3.stage.ChiselStage).emitVerilog(new Computation())
}
