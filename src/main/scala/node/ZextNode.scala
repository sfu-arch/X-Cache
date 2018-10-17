package node

import chisel3._
import chisel3.Module
import config._
import interfaces.{ControlBundle, DataBundle}
import util._

class ZextNodeIO(val src: Int, val des: Int, val nout: Int)
                         (implicit p: Parameters) extends CoreBundle()(p) {

    //Input for Zext
    val Input = Flipped(Decoupled(new DataBundle()))
    //val Input = Flipped(Decoupled(UInt(src.W)))

    //Enabl signal
    val enable = Flipped(Decoupled(Bool()))

    //Output of the input (Zexted version)
    val Out = Vec(nout, Decoupled(new DataBundle()))
    //val Out = Output(Vec(nout, Decoupled(UInt(des.W))))

}

class ZextNode(val SrcW: Int = 0, val DesW: Int = 0, val NumOuts: Int=1)(implicit val p: Parameters)
  extends Module with CoreParams{

  lazy val io = IO(new ZextNodeIO(SrcW, DesW, NumOuts))

  for(i <- 0 until NumOuts){
    io.Out(i) <> io.Input
  }
  io.enable.ready := true.B

  //for(i <- 0 until NumOuts){

    //when(io.enable.bits){
      //io.Out(i).bits := Cat(Fill((DesW-SrcW), 0.U),io.Input.bits)
    //}.otherwise{
      //io.Out(i).bits := 0.U
    //}
    //io.Out(i).valid := io.Input.valid
    //io.Input.ready := io.Out(i).ready
  //}
}

