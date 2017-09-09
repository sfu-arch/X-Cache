package switches



import chisel3._
import chisel3.util._
import config._
import muxes._
import scala.math._
import interfaces._
import muxes._
import node._
import util._


//class SwitchInBundle(implicit p: Parameters) extends ValidT {
//  val data = UInt(xlen.W)
//}
//
//
//object SwitchInBundle {
//  def default(implicit p: Parameters): SwitchInBundle = {
//    val wire = Wire(new SwitchInBundle)
//    wire.data := 0.U
//    wire.valid := false.B
//    wire
//  }
//
//}

abstract class SwitchInControlIO (implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new DataBundle()))
    val out = Decoupled(new DataBundle())
    val ack = Flipped(Decoupled(new DataBundle()))
  })
}


class SwitchInControl (implicit p: Parameters) extends SwitchInControlIO()(p) {
  val switchControl_R = RegInit(DataBundle.default )

  io.in.ready := ~switchControl_R.valid

  // Note: Be wary of io.in.valid and io.in.bits.valid
  when(io.in.fire()){
    switchControl_R :=  io.in.bits
  }

  io.ack.ready := switchControl_R.valid

  when (io.ack.fire()) {
    switchControl_R.valid := false.B
  }

  io.out.bits := switchControl_R
  io.out.valid := switchControl_R.valid
}



//// 4:1 Mux
//class DyserMux extends DyserMuxIO {
//
//  val DMux = new Mux[DataBundle]()
//  val DSwitch = Vec(4,new SwitchInControl())
//
//  DMux(0).in := DSwitch(0).out
//  DMux.sel := FF
//  DSwitch(sel).ack := (DMux.fire())
//
//}
