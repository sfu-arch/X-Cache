package node

import chisel3._
import chisel3.Module

import util._

abstract class ZextNodeIO(val src: Int, val des: Int, val nout: Int) extends Module{
  val io = IO(new Bundle {

    //Input for Zext
    val Input = Flipped(Decoupled(UInt(src.W)))

    //Enabl signal
    val enable = Flipped(Decoupled(Bool()))

    //Output of the input (Zexted version)
    val Out = Output(Vec(nout, Decoupled(UInt(des.W))))

  })

}

class ZextNode(val SrcW: Int, val DesW: Int, val NumOuts: Int) extends ZextNodeIO(SrcW, DesW, NumOuts) {

  for(i <- 0 until NumOuts){

    when(io.enable.bits){
      io.Out(i).bits := Cat(Fill((DesW-SrcW), 0.U),io.Input.bits)
    }.otherwise{
      io.Out(i).bits := 0.U
    }
    io.Out(i).valid := io.Input.valid
    io.Input.ready := io.Out(i).ready
  }
}

