package FPU

import hardfloat._
import chisel3.Module
import chisel3._
import chisel3.util._ 
import FType._ 

case class FType(exp: Int, sig: Int) {
  def ieeeWidth = exp + sig
  def expWidth  = exp
  def sigWidth  = sig
  def recodedWidth = ieeeWidth + 1

  def qNaN = UInt((BigInt(7) << (exp + sig - 3)) + (BigInt(1) << (sig - 2)), exp + sig + 1)
  def isNaN(x: UInt) = x(sig + exp - 1, sig + exp - 3).andR
  def isSNaN(x: UInt) = isNaN(x) && !x(sig - 2)

  def classify(x: UInt) = {
    val sign = x(sig + exp)
    val code = x(exp + sig - 1, exp + sig - 3)
    val codeHi = code(2, 1)
    val isSpecial = codeHi === 3.U

    val isHighSubnormalIn = x(exp + sig - 3, sig - 1) < 2.U
    val isSubnormal = code === 1.U || codeHi === 1.U && isHighSubnormalIn
    val isNormal = codeHi === 1.U && !isHighSubnormalIn || codeHi === 2.U
    val isZero = code === 0.U
    val isInf = isSpecial && !code(0)
    val isNaN = code.andR
    val isSNaN = isNaN && !x(sig-2)
    val isQNaN = isNaN && x(sig-2)

    Cat(isQNaN, isSNaN, isInf && !sign, isNormal && !sign,
        isSubnormal && !sign, isZero && !sign, isZero && sign,
        isSubnormal && sign, isNormal && sign, isInf && sign)
  }

  // convert between formats, ignoring rounding, range, NaN
  def unsafeConvert(x: UInt, to: FType) = if (this == to) x else {
    val sign = x(sig + exp)
    val fractIn = x(sig - 2, 0)
    val expIn = x(sig + exp - 1, sig - 1)
    val fractOut = fractIn << to.sig >> sig
    val expOut = {
      val expCode = expIn(exp, exp - 2)
      val commonCase = (expIn + (1 << to.exp).U) - (1 << exp).U
      Mux(expCode === 0.U || expCode >= 6.U, Cat(expCode, commonCase(to.exp - 3,0)), commonCase(to.exp,0))
    }
    Cat(sign, expOut, fractOut)
  }

  def recode(x: UInt) = hardfloat.recFNFromFN(exp, sig, x)
  def ieee(x: UInt) = hardfloat.fNFromRecFN(exp, sig, x)
}

object FType {
  val S = new FType(8, 24)
  val D = new FType(11, 53)
  val H  = new FType(5, 11)
}

/**
  * List of compute operations which we can support
  */
object FPAluOpCode {
  val Add = 1
  val Sub = 2
  val Mul = 3
  val Mac = 4

  val opMap = Map(
    "Add" -> Add,
    "add" -> Add,
    "Sub" -> Sub,
    "sub" -> Sub,
    "Mul" -> Mul,
    "mul" -> Mul,
    "Mac" -> Mac,
    "mac" -> Mac
   )
  val length = 8
}


/** @param key     a key to search for
  * @param default a default value if nothing is found
  * @param mapping a sequence to search of keys and values
  * @return the value found or the default if not
  */
object FPAluGenerator {
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
class FPUALU(val xlen: Int, val opCode: String, t: FType) extends Module {
  val io = IO(new Bundle {
    val in1 = Input(UInt(xlen.W))
    val in2 = Input(UInt(xlen.W))
    val out = Output(UInt(xlen.W))
  })

  /* We are hardcoding the signals at compile time to enable
     the backend synthesis tools to optimize. This is important 
     from a dataflow perspective to ensure we get the most optimal node
     for each element in the dataflow.
     If we decode, then a MUX would be needed within the hardware.
  */
  def FPUControl(): Unit = { 
    FPAluOpCode.opMap(opCode) match {
      case FPAluOpCode.Add  =>  { // b + c
                        mulAddRecFN.io.op := 0.U
                        mulAddRecFN.io.a := dummy1.io.out
                        mulAddRecFN.io.b := in1RecFN
                        mulAddRecFN.io.c := in2RecFN
                      }
      case FPAluOpCode.Sub  =>  { // b - c
                        mulAddRecFN.io.op := 1.U
                        mulAddRecFN.io.a := dummy1.io.out
                        mulAddRecFN.io.b := in1RecFN
                        mulAddRecFN.io.c := in2RecFN
                      }
      case FPAluOpCode.Mul  =>  { // a*b
                        mulAddRecFN.io.op := 0.U
                        mulAddRecFN.io.a := in1RecFN
                        mulAddRecFN.io.b := in2RecFN
                        mulAddRecFN.io.c := dummy0.io.out
                      }
      case FPAluOpCode.Mac  =>  { // a*b + c
                        mulAddRecFN.io.op := 0.U
                        mulAddRecFN.io.a := in1RecFN
                        mulAddRecFN.io.b := in2RecFN
                        mulAddRecFN.io.c := dummy0.io.out
                      }
    }
  }

  /* 1 and 0 encoded in berkley hardfloat format. 
    This is useful for hardwiring some of the inputs e.g., 
    use implement b + c as 1 * b + c on a MAC.
  */
  val dummy1 = Module(new INToRecFN(t.ieeeWidth, t.expWidth, t.sigWidth))
  dummy1.io.signedIn := Bool(false)
  dummy1.io.in := 1.U((t.ieeeWidth).W)
  dummy1.io.roundingMode :=  "b110".U(3.W)
  dummy1.io.detectTininess := 0.U(1.W)

  val dummy0 = Module(new INToRecFN(t.ieeeWidth, t.expWidth, t.sigWidth))
  dummy0.io.signedIn := Bool(false)
  dummy0.io.in := 0.U((t.ieeeWidth).W)
  dummy0.io.roundingMode :=  "b110".U(3.W)
  dummy0.io.detectTininess := 0.U(1.W)

  /* Recode inputs into ieee format */
  val in1RecFN = t.recode(io.in1)
  val in2RecFN = t.recode(io.in2)

  val mulAddRecFN = Module(new MulAddRecFN(t.expWidth, t.sigWidth))
  mulAddRecFN.io.roundingMode   := "b110".U(3.W)
  mulAddRecFN.io.detectTininess := 0.U(1.W)

  assert(!FPAluOpCode.opMap.get(opCode).isEmpty, "Wrong ALU OP!")

  FPUControl()
  io.out := t.ieee(mulAddRecFN.io.out)

  printf(p"${Hexadecimal(io.out)}")
}

