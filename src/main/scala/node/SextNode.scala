package node

import chisel3._
import chisel3.Module

import util._

abstract class SextNodeIO(val src: Int, val des: Int, val nout: Int) extends Module{
  val io = IO(new Bundle {

    //Input for Sext
    val Input = Flipped(Decoupled(SInt(src.W)))

    //Enabl signal
    val enable = Flipped(Decoupled(Bool()))

    //Output of the input (Sexted version)
    val Out = Output(Vec(nout, Decoupled(UInt(des.W))))

  })

}

class SextNode(val SrcW: Int, val DesW: Int, val NumOuts: Int) extends SextNodeIO(SrcW, DesW, NumOuts) {

  for(i <- 0 until NumOuts){

    when(io.enable.bits){
      io.Out(i).bits := Cat(Fill((DesW-SrcW), io.Input.bits(SrcW-1)),io.Input.bits)
    }.otherwise{
      io.Out(i).bits := 0.U
    }
    io.Out(i).valid := io.Input.valid
    io.Input.ready := io.Out(i).ready
  }
}

