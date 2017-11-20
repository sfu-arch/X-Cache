package node

import chisel3._
import chisel3.Module
import config._
import interfaces.{ControlBundle, DataBundle}
import util._

class SextNodeIO(val src: Int, val des: Int, val nout: Int)
                         (implicit p: Parameters) extends CoreBundle()(p) {

    //Input for Sext
    val Input = Flipped(Decoupled(new DataBundle()))
    //val Input = Flipped(Decoupled(UInt(src.W)))

    //Enabl signal
    val enable = Flipped(Decoupled(Bool()))

    //Output of the input (Sexted version)
    val Out = Vec(nout, Decoupled(new DataBundle()))
    //val Out = Output(Vec(nout, Decoupled(UInt(des.W))))

}

class SextNode(val SrcW: Int, val DesW: Int, val NumOuts: Int)(implicit val p: Parameters)
  extends Module with CoreParams{

  lazy val io = IO(new SextNodeIO(SrcW, DesW, NumOuts))

  io.enable.ready := true.B
  for(i <- 0 until NumOuts){
    io.Out(i) <> io.Input
  }

//    when(io.enable.bits){
//      io.Out(i).bits := Cat(Fill((DesW-SrcW), io.Input.bits(SrcW-1)),io.Input.bits)
//    }.otherwise{
//      io.Out(i).bits := 0.U
//    }
//    io.Out(i).valid := io.Input.valid
//    io.Input.ready := io.Out(i).ready
//  }
}

