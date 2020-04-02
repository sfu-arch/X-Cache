package dandelion.generator

import chisel3._
import chipsalliance.rocketchip.config._
import dandelion.accel.DandelionAccelDebugModule
import dandelion.node._

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

  for(i <- 0 until numDebug){
    buffers(i).io.Enable := io.enableNode(i)
    buffers(i).io.addrDebug := io.addrDebug(i)
    io.vmeOut(i) <> buffers(i).io.vmeOut
  }

}
