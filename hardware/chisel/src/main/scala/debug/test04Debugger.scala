package dandelion.generator

import chisel3._
import chipsalliance.rocketchip.config._
import dandelion.accel.DandelionAccelDebugModule
import dandelion.node._


class test04DebugVMEDF(val numDebug: Int, val boreIDsList: Seq[Int])
                      (implicit p: Parameters) extends DandelionAccelDebugModule(numDebug, boreIDsList)(p)  {

  /**
    * Debug node for BoreID = 4
    */
  //ID from 0, Bore_ID same as the ID of the node_to_be_logged and node_cnt for memory spaces from 0

  val buffers = Seq.tabulate(numDebug) {i => Module(new DebugVMEBufferNode(ID = i, Bore_ID = boreIDsList(i)))}
  for(i <- 0 until numDebug){
    buffers(i).io.Enable := io.enableNode(i)
    buffers(i).io.addrDebug := io.addrDebug(i)
    io.vmeOut(i) <> buffers(i).io.vmeOut
  }

}
