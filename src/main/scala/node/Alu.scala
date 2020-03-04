package dandelion.node

import chisel3._
import dsptools._
import dsptools.numbers.{RealBits}
import dsptools.numbers.implicits._
import dsptools.DspContext


/**
  * List of compute operations which we can support
  */
object AluOpCode {
  val Add                  = 1
  val Sub                  = 2
  val And                  = 3
  val Or                   = 4
  val Xor                  = 5
  val Xnor                 = 6
  val ShiftLeft            = 7
  val ShiftRight           = 8
  val ShiftRightLogical    = 9
  val ShiftRightArithmetic = 10
  val EQ                   = 11
  val NE                   = 12
  val LT                   = 13
  val GT                   = 14
  val LTE                  = 15
  val GTE                  = 16
  val PassA                = 17
  val PassB                = 18
  val Mul                  = 19
  val Div                  = 20
  val Mod                  = 21
  val Max                  = 22
  val Min                  = 23
  val Mac                  = 24

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
    "ashr" -> ShiftRightArithmetic,
    "ShiftRightArithmetic" -> ShiftRightArithmetic,
    "lshr" -> ShiftRightLogical,

    // Comparision opCodes
    "EQ" -> EQ,
    "eq" -> EQ,
    "NE" -> NE,
    "ne" -> NE,
    "LT" -> LT,
    "lt" -> LT,
    "ult" -> LT,
    "slt" -> LT,
    "GT" -> GT,
    "gt" -> GT,
    "sgt" -> GT,
    "ugt" -> GT,
    "LTE" -> LTE,
    "lte" -> LTE,
    "GTE" -> GTE,
    "gte" -> GTE,

    //DSP opCodes
    "PassA" -> PassA,
    "PassB" -> PassB,
    "Mul" -> Mul,
    "mul" -> Mul,
    "Udiv" -> Div,
    "udiv" -> Div,
    "sdiv" -> Div,
    "Urem" -> Mod,
    "urem" -> Mod,
    "max" -> Max,
    "Max" -> Max,
    "Min" -> Min,
    "Min" -> Min,
    "Mac" -> Mac,
    "mac" -> Mac
  )

  val DSPopMap = Map(
    "Add" -> Add,
    "add" -> Add,
    "Sub" -> Sub,
    "sub" -> Sub,
    "LT" -> LT,
    "lt" -> LT,
    "GT" -> GT,
    "gt" -> GT,
    "EQ" -> EQ,
    "eq" -> EQ,
    "NE" -> NE,
    "ne" -> NE,
    "LTE" -> LTE,
    "lte" -> LTE,
    "GTE" -> GTE,
    "gte" -> GTE,
    "PassA" -> PassA,
    "PassB" -> PassB,
    "Mul" -> Mul,
    "mul" -> Mul,
    "max" -> Max,
    "Max" -> Max,
    "Min" -> Min,
    "Min" -> Min,
    "Mac" -> Mac,
    "mac" -> Mac
  )


  val length = 24
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

class UALUIO(xlen: Int, val opCode: String) extends Bundle {
  val in1 = Input(UInt(xlen.W))
  val in2 = Input(UInt(xlen.W))
  val in3 = if (AluOpCode.opMap(opCode) == AluOpCode.Mac)
    Some(Input(UInt(xlen.W))) else None
  val out = Output(UInt(xlen.W))

  override def cloneType: this.type = new UALUIO(xlen, opCode).asInstanceOf[this.type]
}

class UALU(val xlen: Int, val opCode: String, val issign: Boolean = false) extends Module {
  val io = IO(new UALUIO(xlen, opCode))

  val in1S = io.in1.asSInt
  val in2S = io.in2.asSInt


  var aluOp = if (!issign) {
    Array(
      AluOpCode.Add -> (io.in1 + io.in2),
      AluOpCode.Sub -> (io.in1 - io.in2),
      AluOpCode.And -> (io.in1 & io.in2),
      AluOpCode.Or -> (io.in1 | io.in2),
      AluOpCode.Xor -> (io.in1 ^ io.in2),
      AluOpCode.Xnor -> (~(io.in1 ^ io.in2)),
      AluOpCode.ShiftLeft -> (io.in1 << io.in2(math.min(in2S.getWidth, 19) - 1, 0)),
      AluOpCode.ShiftRight -> (io.in1 >> io.in2(math.min(in2S.getWidth, 19) - 1, 0)),
      AluOpCode.ShiftRightLogical -> (io.in1.asUInt >> io.in2(math.min(in2S.getWidth, 19) - 1, 0)).asUInt, // Chisel only performs arithmetic right-shift on SInt
      AluOpCode.ShiftRightArithmetic -> (io.in1.asSInt >> io.in2(math.min(in2S.getWidth, 19) - 1, 0)).asUInt, // Chisel only performs arithmetic right-shift on SInt
      AluOpCode.LT -> (io.in1.asUInt < io.in2.asUInt),
      AluOpCode.GT -> (io.in1.asUInt > io.in2.asUInt),
      AluOpCode.EQ -> (io.in1.asUInt === io.in2.asUInt),
      AluOpCode.LTE -> (io.in1.asUInt <= io.in2.asUInt),
      AluOpCode.GTE -> (io.in1.asUInt >= io.in2.asUInt),
      AluOpCode.PassA -> io.in1,
      AluOpCode.PassB -> io.in2,
      AluOpCode.Mul -> (io.in1 * io.in2),
      AluOpCode.Div -> (io.in1 / io.in2),
      AluOpCode.Mod -> (io.in1 % io.in2),
      AluOpCode.Max -> (Mux(io.in1 > io.in2, io.in1, io.in2)),
      AluOpCode.Min -> (Mux(io.in1 < io.in2, io.in1, io.in2))
    )
  } else {
    Array(
      AluOpCode.Add -> (in1S + in2S),
      AluOpCode.Sub -> (in1S - in2S),
      AluOpCode.And -> (in1S & in2S),
      AluOpCode.Or -> (in1S | in2S),
      AluOpCode.Xor -> (in1S ^ in2S),
      AluOpCode.Xnor -> (~(in1S ^ in2S)),
      AluOpCode.ShiftLeft -> (in1S << in2S(math.min(in2S.getWidth, 19) - 1, 0)),
      AluOpCode.ShiftRight -> (in1S >> in2S(math.min(in2S.getWidth, 19) - 1, 0)),
      AluOpCode.ShiftRightLogical -> (in1S.asUInt >> in2S(math.min(in2S.getWidth, 19),0)).asUInt, // Chisel only performs arithmetic right-shift on SInt
      AluOpCode.ShiftRightArithmetic -> (in1S.asSInt >> in2S(math.min(in2S.getWidth, 19), 0)).asUInt, // Chisel only performs arithmetic right-shift on SInt
      AluOpCode.LT -> (io.in1.asSInt < io.in2.asSInt),
      AluOpCode.GT -> (io.in1.asSInt > io.in2.asSInt),
      AluOpCode.EQ -> (io.in1.asSInt === io.in2.asSInt),
      AluOpCode.LTE -> (io.in1.asSInt <= io.in2.asSInt),
      AluOpCode.GTE -> (io.in1.asSInt >= io.in2.asSInt),
      AluOpCode.PassA -> in1S,
      AluOpCode.PassB -> in2S,
      AluOpCode.Mul -> (in1S * in2S),
      AluOpCode.Div -> (in1S / in2S),
      AluOpCode.Mod -> (in1S % in2S),
      AluOpCode.Max -> (Mux(in1S > in2S, in1S, in2S)),
      AluOpCode.Min -> (Mux(in1S < in2S, in1S, in2S))
    )

  }

  if (AluOpCode.opMap(opCode) == AluOpCode.Mac) {
    if (!issign)
      aluOp = aluOp :+ AluOpCode.Mac -> ((io.in1 * io.in2).+(io.in3.get))
    else
      aluOp = aluOp :+ AluOpCode.Mac -> ((in1S * in2S).+(io.in3.get.asSInt))
  }
  assert(!AluOpCode.opMap.get(opCode).isEmpty, "Wrong ALU OP!")
  io.out := AluGenerator(AluOpCode.opMap(opCode), aluOp).asUInt
}


class DSPIO[T <: Data : RealBits](gen: T, val opCode: String) extends Bundle {
  val in1 = Input(gen)
  val in2 = Input(gen)

  val in3 = if (AluOpCode.DSPopMap(opCode) == AluOpCode.Mac) {
    Some(Input(gen.cloneType))
  }
  else None

  val out = Output(UInt(gen.getWidth.W))

  override def cloneType: this.type

  = new DSPIO(gen, opCode).asInstanceOf[this.type]
}

// Parameterized Chisel Module; takes in type parameters as explained above
class DSPALU[T <: Data : RealBits](gen: T, val opCode: String) extends Module {
  // This is how you declare an IO with parameters
  val io = IO(new DSPIO(gen, opCode))
  // Output will be current x + y addPipes clock cycles later
  // Note that this relies on the fact that type classes have a special + that
  // add addPipes # of ShiftRegister after the sum. If you don't wrap the sum in
  // DspContext.withNumAddPipes(addPiPes), the default # of addPipes is used.

  val in2U = io.in2.asUInt

  DspContext.alter(DspContext.current.copy(trimType = RoundDown, binaryPointGrowth = 0, numMulPipes = 0)) {
    var aluOp = Array(
      AluOpCode.Add -> (io.in1 context_+ io.in2),
      AluOpCode.Sub -> (io.in1 context_- io.in2),
      AluOpCode.LT -> (io.in1 < io.in2),
      AluOpCode.GT -> (io.in1 < io.in2),
      AluOpCode.EQ -> (io.in1 === io.in2),
      AluOpCode.NE -> (io.in1 =/= io.in2),
      AluOpCode.LTE -> (io.in1 <= io.in2),
      AluOpCode.GTE -> (io.in1 >= io.in2),
      AluOpCode.PassA -> io.in1,
      AluOpCode.PassB -> io.in2,
      AluOpCode.Mul -> (io.in1 context_* io.in2),
      AluOpCode.Max -> (Mux(io.in1 > io.in2, io.in1, io.in2)),
      AluOpCode.Min -> (Mux(io.in1 < io.in2, io.in1, io.in2))
    )

    if (AluOpCode.DSPopMap(opCode) == AluOpCode.Mac) {
      aluOp = aluOp :+ AluOpCode.Mac -> ((io.in1 context_* io.in2).+(io.in3.get))
    }

    assert(!AluOpCode.DSPopMap.get(opCode).isEmpty, "Wrong ALU OP!")
    io.out := AluGenerator(AluOpCode.DSPopMap(opCode), aluOp).asUInt
  }

}


// Parameterized Chisel Module; takes in type parameters as explained above
class UALUcompatibleDSPALU[T <: Data : RealBits](gen: T, val opCode: String) extends Module {
  // This is how you declare an IO with parameters
  val io = IO(new UALUIO(gen.getWidth, opCode))
  // Output will be current x + y addPipes clock cycles later
  // Note that this relies on the fact that type classes have a special + that
  // add addPipes # of ShiftRegister after the sum. If you don't wrap the sum in
  // DspContext.withNumAddPipes(addPiPes), the default # of addPipes is used.

  val in1gen = io.in1.asTypeOf(gen)
  val in2gen = io.in2.asTypeOf(gen)

  DspContext.alter(DspContext.current.copy(trimType = RoundDown, binaryPointGrowth = 0, numMulPipes = 0)) {
    var aluOp = Array(
      AluOpCode.Add -> (in1gen context_+ in2gen),
      AluOpCode.Sub -> (in1gen context_- in2gen),
      AluOpCode.LT -> (in1gen < in2gen),
      AluOpCode.GT -> (io.in1 > io.in2),
      AluOpCode.EQ -> (io.in1 === io.in2),
      AluOpCode.LTE -> (io.in1 <= io.in2),
      AluOpCode.GTE -> (io.in1 >= io.in2),
      AluOpCode.PassA -> in1gen,
      AluOpCode.PassB -> in2gen,
      AluOpCode.Mul -> (in1gen context_* in2gen),
      AluOpCode.Max -> (Mux(in1gen > in2gen, in1gen, in2gen)),
      AluOpCode.Min -> (Mux(in1gen < in2gen, in1gen, in2gen))
    )

    if (AluOpCode.DSPopMap(opCode) == AluOpCode.Mac) {
      aluOp = aluOp :+ AluOpCode.Mac -> ((in1gen context_* in2gen).context_+(io.in3.get.asTypeOf(gen)))
    }

    assert(!AluOpCode.DSPopMap.get(opCode).isEmpty, "Wrong ALU OP!")
    io.out := AluGenerator(AluOpCode.DSPopMap(opCode), aluOp).asUInt

  }

}

