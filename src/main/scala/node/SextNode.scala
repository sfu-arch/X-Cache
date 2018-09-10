package node

import chisel3._
import chisel3.Module
import config._
import interfaces.{ControlBundle, DataBundle}
import util._

class SextNode()(implicit val p: Parameters)
  extends Module with CoreParams {

  val io = IO(
    new Bundle {
      //Input for Sext
      val Input = Flipped(Decoupled(new DataBundle()))

      //Enabl signal
      val enable = Flipped(Decoupled(Bool()))

      //Output of the input (Sexted version)
      val Out = Decoupled(new DataBundle())

    }
  )

  io.enable.ready := true.B
  io.Out <> io.Input
}

