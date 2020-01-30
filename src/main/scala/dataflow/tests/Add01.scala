package dandelion.dataflow.tests

import chisel3._
import chisel3.util._
import chisel3.Module
import chipsalliance.rocketchip.config._
import dandelion.control.{BasicBlockNoMaskFastNode, BasicBlockNode}
import util._
import dandelion.interfaces._
import dandelion.node._
import dandelion.config._



abstract class Add01DFIO(implicit val p: Parameters) extends Module with HasAccelParams {
  val io = IO(new Bundle {
    val Data0 = Flipped(Decoupled(new DataBundle))
    val Data1 = Flipped(Decoupled(new DataBundle))
    val pred = Decoupled(new ControlBundle)
    val start = Input(new Bool())
    val result = Decoupled(new DataBundle)
  })
}


class Add01DF(implicit p: Parameters) extends Add01DFIO() {

  val b0_entry = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 0))
  val m0 = Module(new ComputeNode(NumOuts = 1, ID = 0, opCode = "Add")(sign = false))

  //Setting b0_entry predicates to be true
  // will start immediately
  b0_entry.io.predicateIn(0).bits.control := true.B
  b0_entry.io.predicateIn(0).bits.taskID := 0.U

  //ALU will start only if the basic block enables adds
  m0.io.enable <> b0_entry.io.Out(0)

//IO connections

  m0.io.LeftIO <> io.Data0
  m0.io.RightIO <> io.Data1
  b0_entry.io.predicateIn(0).valid := io.start


  io.pred <> b0_entry.io.Out(0)
  io.result <> m0.io.Out(0)
}
