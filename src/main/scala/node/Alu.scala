package node

import chisel3._
import chisel3.util._


/**
 * List of compute operations which we can support
 */
object AluOpCode {
  val Add                   = 1
  val Sub                   = 2
  val AND                   = 3
  val OR                    = 4
  val XOR                   = 5
  val XNOR                  = 6
  val ShiftLeft             = 7
  val ShiftRightLogical     = 8
  val ShiftRightArithmetic  = 9
  val SetLessThan           = 10
  val SetLessThanUnsigned   = 11
  val PassA                 = 12
  val PassB                 = 13 
  val length = 13
}


/** @param key a key to search for
  * @param default a default value if nothing is found
  * @param mapping a sequence to search of keys and values
  * @return the value found or the default if not
  */
object AluGenerator {
  def apply[S <: Int, T <: Data] (key: S, mapping: Seq[(S, T)]): T = {

    //Assign default to first element
    var res= mapping(0)._2
    for((k,v) <- mapping){
      if(k == key)
        res = v
    }

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
    val in1 = Input(UInt(xlen.W))
    val in2 = Input(UInt(xlen.W))
    val out = Output(UInt(xlen.W))
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

  io.out := AluGenerator(opCode, aluOp)

}



