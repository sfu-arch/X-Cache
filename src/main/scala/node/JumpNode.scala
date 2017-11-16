package node

import chisel3._
import chisel3.Module
import config._
import interfaces.{ControlBundle, DataBundle}
import util._

class JumpNodeIO(val nout: Int)
                         (implicit p: Parameters) extends CoreBundle()(p) {

    //Input for Sext
    val Input = Flipped(Decoupled(new DataBundle()))

    //Enabl signal
    val enable = Flipped(Decoupled(Bool()))

    //Output of the input (Sexted version)
    val Out = Vec(nout, Decoupled(new DataBundle()))


}

class JumpNode(val NumOuts: Int, val ID: Int)(implicit val p: Parameters)
  extends Module with CoreParams{

  lazy val io = IO(new JumpNodeIO(NumOuts))


  val enable_R = RegInit(false.B)
  val enable_valid_R = RegInit(false.B)

  io.enable.ready := ~enable_valid_R
  when(io.enable.fire()) {
    //printfInfo("Latch left data\n")
    state := s_LATCH
    enable_R <> io.enable.bits
    enable_valid_R := true.B
  }

  when(enable_valid_R){
    enable_valid_R := false.B
  }


  for(i <- 0 until NumOuts){
    io.Out(i) <> io.Input
  }

}

