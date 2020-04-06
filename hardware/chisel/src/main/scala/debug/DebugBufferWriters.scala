package dandelion.generator

import chisel3._
import chipsalliance.rocketchip.config._
import dandelion.accel.DandelionAccelDebugModule
import dandelion.node._
import chisel3.MultiIOModule
import chisel3.util.experimental.BoringUtils
/**
 * DebugBufferWriters
 *
 * This module is responsible to connect nodes debug IOs to DME.
 * The connection between buffer nodes with the actual nodes is implicit using Bore feature
 * Bore_ID is the same as the ID of the node_to_be_logged and node_cnt for memory spaces from 0
 * @param numDebug number of debug nodes
 * @param boreIDsList list of node IDs
 * @param p
 */
class DebugBufferWriters(val numDebug: Int, val boreIDsList: Seq[Int])
                      (implicit p: Parameters) extends DandelionAccelDebugModule(numDebug, boreIDsList)(p)  {


  val buffers = Seq.tabulate(numDebug) {i => Module(new DebugVMEBufferNode(ID = i, Bore_ID = boreIDsList(i)))}

  val buf_data = for(i <- 0 until numDebug) yield {
    val data = Wire(UInt(xlen.W))
    data
  }

  val buf_valid = for(i <- 0 until numDebug) yield {
    val valid = Wire(Bool())
    valid
  }

  val buf_ready = for(i <- 0 until numDebug) yield {
    val ready = Wire(Bool())
    ready
  }

  buf_data.foreach(_ := 0.U)
  buf_valid.foreach(_ := false.B)
  buf_ready.foreach(_ := false.B)


  for(i <- 0 until numDebug){
    buffers(i).io.Enable := io.enableNode(i)
    buffers(i).io.addrDebug := io.addrDebug(i)
    io.vmeOut(i) <> buffers(i).io.vmeOut

    buffers(i).buf.port.bits := buf_data(i)
    buffers(i).buf.port.valid := buf_valid(i)
    buf_ready(i) := buffers(i).buf.port.ready

    BoringUtils.addSink(buf_data(i), s"data${boreIDsList(i)}")
    BoringUtils.addSink(buf_valid(i), s"valid${boreIDsList(i)}")
    BoringUtils.addSource(buf_ready(i), s"Buffer_ready${boreIDsList(i)}")
  }

}
