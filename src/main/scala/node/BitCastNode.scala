package node

import chisel3._
import chisel3.Module
import config._
import interfaces.{ControlBundle, DataBundle}
import util._

class BitCastNodeIO(val NumOuts: Int)
                         (implicit p: Parameters) extends CoreBundle()(p) {

    //Input for Bitcast
    val Input = Flipped(Decoupled(new DataBundle()))
    //val Input = Flipped(Decoupled(UInt(src.W)))

    //Enabl signal
    val enable = Flipped(Decoupled(new ControlBundle()))

    //Output of the input (Zexted version)
    val Out = Vec(NumOuts, Decoupled(new DataBundle()))
    //val Out = Output(Vec(nout, Decoupled(UInt(des.W))))

  override def cloneType = new BitCastNodeIO(NumOuts).asInstanceOf[this.type]
}

class BitCastNode(val NumOuts: Int,val ID : Int)(implicit val p: Parameters)
  extends Module with CoreParams{

  lazy val io = IO(new BitCastNodeIO(NumOuts))

  for(i <- 0 until NumOuts){
    io.Out(i) <> io.Input
  }
  io.enable.ready := true.B

}

