package dummy_nodes

/**
  * Created by nvedula on 15/5/17.
  */
import chisel3._
import chisel3.util._

class DummyGEP(xLen: Int) extends Module {

  val io = IO(new Bundle {
    val In1 = Flipped(Decoupled(UInt(xLen.W)))
    val Out1 = Decoupled(UInt(xLen.W))
  })

//  io.In1.ready := true.B
//  io.Out1.valid := true.B

  io.In1 <> io.Out1
}
