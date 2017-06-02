package node

import chisel3._
import chisel3.util._

/**
 * List of comparision operations
 */
object CmpOpCode {
  val EQ      = 1.U
  val NE      = 2.U
  val UGT     = 3.U
  val UGE     = 4.U
  val ULT     = 5.U
  val ULE     = 6.U
  val SGT     = 7.U
  val SGE     = 8.U
  val SLT     = 9.U
  val SLE     = 10.U
  val length  = 11.U
}


/**
 * This way you can not pick our operation correctly!!
 */
object CustomCMP {
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


class UCMP(val xlen: Int, val opCode: Int) extends Module {
  val io = IO(new Bundle {
    val in1 = Input(UInt(xlen.W))
    val in2 = Input(UInt(xlen.W))
    val out = Output(UInt(xlen.W))
  })

  //printf(p"OPCODE: ${opCode}\n")

  val cmpOp = Array(
      CmpOpCode.EQ  -> (io.in1 === io.in2),
      CmpOpCode.NE  -> (io.in1 =/= io.in2),
      CmpOpCode.UGT -> (io.in1  >  io.in2),
      CmpOpCode.UGE -> (io.in1  >=  io.in2),
      CmpOpCode.ULT -> (io.in1  <  io.in2),
      CmpOpCode.ULE -> (io.in1  <= io.in2)
    )

  io.out := CustomCMP(cmpOp(opCode))

}


class SCMP(val xlen: Int, val opCode: Int) extends Module {
  val io = IO(new Bundle {
    val in1 = Input(SInt(xlen.W))
    val in2 = Input(SInt(xlen.W))
    val out = Output(SInt(xlen.W))
  })


  val aluOp = Array(
      CmpOpCode.SGT -> (io.in1  >  io.in2),
      CmpOpCode.SGE -> (io.in1  >=  io.in2),
      CmpOpCode.SLT -> (io.in1  <  io.in2),
      CmpOpCode.SLE -> (io.in1  <= io.in2)

    )

  io.out := CustomCMP(aluOp(opCode))

}


