package memGen.memory.cache

import chisel3._
import chisel3.util._
import chisel3.util.experimental._


// Opcodes: add: 0, sub: 1, mult: 2, shift_r: 3, shift_l: 4 

class Computation (val operandWidth :Int = 16, val opcodeWidth :Int = 6) extends Module {
    val io = IO (new Bundle {
        val operand1 =  Input(new ComputationInput(operandWidth))
        val operand2 =  Input(new ComputationInput(operandWidth))
        val opcode = Input(UInt(opcodeWidth.W))

        val output = Output(UInt(operandWidth.W))
    })

    val add :: sub :: mult :: shift_r :: shift_l :: Nil = Enum (5)

    val operand1_mux = Module(new Mux3(operandWidth));
    val operand2_mux = Module(new Mux3(operandWidth));

    val result = Wire(UInt(operandWidth.W));
    
    val op1 = Wire(UInt(operandWidth.W));
    val op2 = Wire(UInt(operandWidth.W));

    result := 0.U;

    operand1_mux.io.in <> io.operand1;
    operand2_mux.io.in <> io.operand2;

    op1 <> operand1_mux.io.out;
    op2 <> operand2_mux.io.out;

    switch (io.opcode) {
        is (add) { result := op1 + op2; }
        is (sub) { result := op1 - op2; }
        is (mult) { result := op1 * op2; }
        is (shift_r) { result := op1 >> op2; }
        is (shift_l) { result := op1 << op2; }
    }

    io.output := result;
}



object Computation extends App {
  (new chisel3.stage.ChiselStage).emitVerilog(new Computation())
}
