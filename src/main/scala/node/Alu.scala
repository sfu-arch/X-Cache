package node

import chisel3._
import chisel3.util._


/**
 * List of operations which we can support
 */
object AluOpCode {
  val Add                   = 1.U
  val Sub                   = 2.U
  val AND                   = 3.U
  val OR                    = 4.U
  val XOR                   = 5.U
  val XNOR                  = 6.U
  val ShiftLeft             = 7.U
  val ShiftRightLogical     = 8.U
  val ShiftRightArithmetic  = 9.U
  val SetLessThan           = 10.U
  val SetLessThanUnsigned   = 11.U
  val PassA                 = 12.U
  val PassB                 = 13.U 
  val length = 13
}


object CustomAlu {
  /** @param key a key to search for
    * @param default a default value if nothing is found
    * @param mapping a sequence to search of keys and values
    * @return the value found or the default if not
    */
  def apply[S <: UInt, T <: Data] (mapping: (S, T)): T = {
    var res = mapping._2
    res
  }
}


/**
 * ALU class supports all the computation operations exist in LLVM
 * to use the class you only need to specify the length of inputs
 * and opCode of your alu.
 *
 * @param opCode  opcode which indicates ALU operation
 * @param xlen    bit width of the inputs
 */
class ALU (val xlen: Int, val opCode: Int) extends Module {
  val io = IO(new Bundle {
    val in1 = Input(UInt(width=xlen))
    val in2 = Input(UInt(width=xlen))
    val out = Output(UInt(width=xlen))
  })


  val aluOp = Array(
      AluOpCode.Add -> (io.in1 + io.in2),
      AluOpCode.Sub -> (io.in1 - io.in2),
      AluOpCode.AND -> (io.in1 & io.in2),
      AluOpCode.OR -> (io.in1 | io.in2),
      AluOpCode.XOR -> (io.in1 ^ io.in2),
      AluOpCode.XNOR -> (~(io.in1 ^ io.in2)),
      AluOpCode.ShiftLeft -> (io.in1 << io.in2(4, 0)),
      AluOpCode.ShiftRightLogical -> (io.in1 >> io.in2(4, 0)),

      //BUG ALERT does not convert back to UInt properly !????
      AluOpCode.ShiftRightArithmetic -> (io.in1.asSInt >> io.in2(4, 0)).asUInt, // Chisel only performs arithmetic right-shift on SInt
      AluOpCode.SetLessThan -> (io.in1.asSInt < io.in2.asSInt),
      AluOpCode.SetLessThanUnsigned -> (io.in1 < io.in2),
      AluOpCode.PassA -> io.in1,
      AluOpCode.PassB -> io.in2
    )

  io.out := CustomAlu(aluOp(opCode))

}



