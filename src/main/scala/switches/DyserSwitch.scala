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


class SwitchInControlIO (implicit val p: Parameters) extends Module with CoreParams {
  //    override def cloneType(): this.type = this.getClass.newInstance.asInstanceOf[this.type]
  //      new SwitchInControlIO()(p).asInstanceOf[this.type]
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new DataBundle()))
    val out = Decoupled(new DataBundle())
    val ack = Flipped(Decoupled(new AckBundle()))
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


abstract class DyserMuxIO(NInputs : Int) (implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Vec(NInputs, Flipped(Decoupled(new DataBundle())))
    val out = Decoupled(new DataBundle())
    //    val ack = Flipped(Decoupled(new AckBundle()))
  })
}

//// 4:1 Mux and 5:1 Mux

class DyserMux (NInputs : Int, Sel: Int, En: Bool)(implicit p: Parameters) extends DyserMuxIO(NInputs)(p) {

  val mux   = Module(new Mux(new DataBundle(),NInputs))
  //  val switchIn =  Vec(Seq.fill(NInputs){ Module(new SwitchInControl()(p)).io })
  //  val manysuchmodules = Array.fill( 5 ) (Module( new somemodule( 10 ) ).io)

  val switchIn = Array.fill(NInputs) (Module(new SwitchInControl()(p)).io)

  val config_R = RegInit(Sel.U(max(1, log2Ceil(NInputs)).W))
  val ack_R = RegInit(false.B)

  for ( i <- 0 until NInputs) {
    //TODO fix this
    // inputs are simply wired together
    //    switchIn(i).in := io.in(i)
    // does not work. Since, it considers as source and sink
    // and ready signals cannot be connected

    switchIn(i).in.valid := io.in(i).valid
    switchIn(i).in.bits := io.in(i).bits
    io.in(i).ready := switchIn(i).in.ready


    mux.io.inputs(i) := switchIn(i).out.bits
  }

  mux.io.sel := config_R
  mux.io.en := En

  io.out.bits := mux.io.output
  io.out.valid := switchIn(Sel).out.valid

  when (io.out.fire() ) {
    ack_R := true.B
  }
    .otherwise(ack_R := false.B)


  switchIn(Sel).ack.valid := ack_R

}



