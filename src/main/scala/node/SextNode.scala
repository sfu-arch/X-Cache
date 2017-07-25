package node

import chisel3._
import chisel3.Module

import util._

abstract class SextNodeIO(val src: Int, val des: Int) extends Module{
  val io = IO(new Bundle {

    //Input for Sext
    val Input = Flipped(Decoupled(SInt(src.W)))

    //Output of the input (Sexted version)
    val OutIO = Output(Decoupled(UInt(des.W)))

  })

}

class SextNode(val SrcW: Int, val DesW: Int) extends SextNodeIO(SrcW, DesW) {

  io.OutIO.bits := Cat(Fill((DesW-SrcW), io.Input.bits(SrcW-1)),io.Input.bits)
  io.OutIO.valid := io.Input.valid
  io.Input.ready := io.OutIO.ready
}

