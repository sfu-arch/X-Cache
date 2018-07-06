package node

import chisel3._
import chisel3.util._

/**
  * List of compute operations which we can support
  */
object AluOpCode {
  val Add = 1
  val Sub = 2
  val And = 3
  val Or = 4
  val Xor = 5
  val Xnor = 6
  val ShiftLeft = 7
  val ShiftRight = 8
  val ShiftRightLogical = 9
  val ShiftRightArithmetic = 10
  val SetLessThan = 11
  val SetLessThanUnsigned = 12
  val PassA = 13
  val PassB = 14
  val Mul = 15
  val Div = 16
  val Mod = 17

  val opMap = Map(
    "Add" -> Add,
    "add" -> Add,
    "Sub" -> Sub,
    "sub" -> Sub,
    "And" -> And,
    "and" -> And,
    "Or" -> Or,
    "or" -> Or,
    "Xor" -> Xor,
    "xor" -> Xor,
    "Xnor" -> Xnor,
    "xnor" -> Xnor,
    "ShiftLeft" -> ShiftLeft,
    "shl" -> ShiftLeft,
    "ShiftRight" -> ShiftRight,
    "ShiftRightLogical" -> ShiftRightLogical,
    "ashr" -> ShiftRightLogical,
    "ShiftRightArithmetic" -> ShiftRightArithmetic,
    "lshr" -> ShiftRightArithmetic,
    "SetLessThan" -> SetLessThan,
    "SetLessThanUnsigned" -> SetLessThanUnsigned,
    "PassA" -> PassA,
    "PassB" -> PassB,
    "Mul" -> Mul,
    "mul" -> Mul,
    "Udiv" -> Div,
    "udiv" -> Div,
    "Urem" -> Mod,
    "urem" -> Mod)


  val length = 14
}


/** @param key     a key to search for
  * @param default a default value if nothing is found
  * @param mapping a sequence to search of keys and values
  * @return the value found or the default if not
  */
object AluGenerator {
  def apply[S <: Int, T <: Data](key: S, mapping: Seq[(S, T)]): T = {

    //Assign default to first element
    var res = mapping(0)._2
    for ((k, v) <- mapping) {
      if (k == key)
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
  * @param opCode opcode which indicates ALU operation
  * @param xlen   bit width of the inputs
  */
class UALU(val xlen: Int, val opCode: String) extends Module {
  val io = IO(new Bundle {
    val in1 = Input(UInt(xlen.W))
    val in2 = Input(UInt(xlen.W))
    val out = Output(UInt(xlen.W))
  })


  val aluOp = Array(
    AluOpCode.Add -> (io.in1 + io.in2),
    AluOpCode.Sub -> (io.in1 - io.in2),
    AluOpCode.And -> (io.in1 & io.in2),
    AluOpCode.Or -> (io.in1 | io.in2),
    AluOpCode.Xor -> (io.in1 ^ io.in2),
    AluOpCode.Xnor -> (~(io.in1 ^ io.in2)),
    AluOpCode.ShiftLeft -> (io.in1 << io.in2(4,0)),
    AluOpCode.ShiftRight -> (io.in1 >> io.in2(4,0)),
    AluOpCode.ShiftRightArithmetic -> (io.in1.asSInt >> io.in2(4, 0)).asUInt, // Chisel only performs arithmetic right-shift on SInt
    AluOpCode.SetLessThan -> (io.in1.asSInt < io.in2.asSInt),
    AluOpCode.SetLessThanUnsigned -> (io.in1 < io.in2),
    AluOpCode.PassA -> io.in1,
    AluOpCode.PassB -> io.in2,
    AluOpCode.Mul -> (io.in1 * io.in2),
    AluOpCode.Div -> (io.in1 / io.in2),
    AluOpCode.Mod -> (io.in1 % io.in2)
  )

  assert(!AluOpCode.opMap.get(opCode).isEmpty, "Wrong ALU OP!")

  io.out := AluGenerator(AluOpCode.opMap(opCode), aluOp)


}



/**
  * SALU class supports all the computation operations exist in LLVM
  * to use the class you only need to specify the length of inputs
  * and opCode of your alu.
  *
  * @param opCode opcode which indicates ALU operation
  * @param xlen   bit width of the inputs
  */


class CALU[T <: Data](gen : T)(val xlen: Int, val opCode: String) extends Module {
  val io = IO(new Bundle {
    val in1 = Input(gen)
    val in2 = Input(gen)
    val out = Output(gen)
  })

  val input_1 =
    if(io.in1.isInstanceOf[UInt])
      io.in1.asUInt()
  else
      io.in1.asTypeOf(SInt)

  val input_2 =
    if(io.in2.isInstanceOf[SInt])
      io.in2.asUInt()
  else
      io.in2.asTypeOf(SInt)

    val aluOp = Array(
    AluOpCode.Add -> (input_1 + input_2),
    AluOpCode.Sub -> (io.in1 - io.in2),
    AluOpCode.And -> (io.in1 & io.in2),
    AluOpCode.Or -> (io.in1 | io.in2),
    AluOpCode.Xor -> (io.in1 ^ io.in2),
    AluOpCode.Xnor -> (~(io.in1 ^ io.in2)),
    AluOpCode.ShiftLeft -> (io.in1 << io.in2(4, 0).asUInt),
    AluOpCode.ShiftRight -> (io.in1 >> io.in2.asUInt),
    AluOpCode.ShiftRightLogical -> (io.in1 >> io.in2(4, 0).asUInt),
    AluOpCode.SetLessThan -> (io.in1.asSInt < io.in2.asSInt),
    AluOpCode.SetLessThanUnsigned -> (io.in1 < io.in2),
    AluOpCode.PassA -> io.in1,
    AluOpCode.PassB -> io.in2
  )

  assert(!AluOpCode.opMap.get(opCode).isEmpty, "Wrong ALU OP!")

  io.out := AluGenerator(AluOpCode.opMap(opCode), aluOp)


}

