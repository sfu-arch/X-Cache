package accel

import chisel3._
import dandelion.dpi._

/** Add-by-one accelerator.
  *
  * ___________      ___________
  * |         |      |         |
  * | HostDPI | <--> | RegFile | <->|
  * |_________|      |_________|    |
  *                                 |
  * ___________      ___________    |
  * |         |      |         |    |
  * | MemDPI  | <--> | Compute | <->|
  * |_________|      |_________|
  *
  */
class AccelConfig() {
  val nCtrl = 1
  val nECnt = 1
  val nVals = 2
  val nPtrs = 2
  val regBits = 32
  val ptrBits = 2*regBits
}

class Accel extends Module {
  val io = IO(new Bundle {
    val host = new VTAHostDPIClient
    val mem = new VTAMemDPIMaster
  })
  implicit val config: AccelConfig= new AccelConfig()
  val rf = Module(new RegFile)
  val ce = Module(new Compute)
  rf.io.host <> io.host
  io.mem <> ce.io.mem
  ce.io.launch := rf.io.launch
  rf.io.finish := ce.io.finish
  rf.io.ecnt <> ce.io.ecnt
  ce.io.vals <> rf.io.vals
  ce.io.ptrs <> rf.io.ptrs
}
