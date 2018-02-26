package dataflow

import chisel3.{Flipped, Module, _}
import chisel3.util.{Decoupled, _}
import config._
import config.Parameters
import interfaces._
import utility.Constants._
import junctions._


class ParentBundle()(implicit p: Parameters) extends CoreBundle  {
  val sid = UInt(tlen.W)  // Static ID (e.g. parent identifier)
  val did = UInt(tlen.W)  // Dynamic ID (e.g. parent's task #)
//  override def cloneType: this.type = new ParentBundle(argTypes).asInstanceOf[this.type]
}

class TaskControllerIO(val argTypes: Seq[Int], val retTypes: Seq[Int], NumParent: Int, NumChild: Int)(implicit p: Parameters)
  extends Bundle
{
  val parentIn  = Vec(NumParent, Flipped(Decoupled(new Call(argTypes))))  // Requests from calling block(s)
  val parentOut = Vec(NumParent, Decoupled(new Call(retTypes)))           // Returns to calling block(s)
  val childIn   = Vec(NumChild, Flipped(Decoupled(new Call(retTypes))))   // Returns from sub-block
  val childOut  = Vec(NumChild, Decoupled(new Call(argTypes)))            // Requests to sub-block
}

class TaskController(val argTypes: Seq[Int], val retTypes: Seq[Int], NumParent: Int, NumChild: Int)
                    (implicit val p: Parameters) extends Module with CoreParams {

  // Instantiate TaskController I/O signals
  val io = IO(new TaskControllerIO(argTypes, retTypes, NumParent, NumChild))

  /** *************************************************************************
    * Input Arbiter
    * Instantiate a round-robin arbiter to select from multiple possible inputs
    * *************************************************************************/
  val taskArb = Module(new RRArbiter(new Call(argTypes), NumParent))
  val freeList = Module(new Queue(UInt(), 1 << tlen))
  val exeList = Module(new Queue(new Call(argTypes), 1 << tlen))
  val parentTable = SyncReadMem(1 << tlen, new ParentBundle())

  for (i <- 0 until NumParent) {
    taskArb.io.in(i) <> io.parentIn(i)
  }
  taskArb.io.out.ready := exeList.io.enq.ready && freeList.io.deq.valid

  // Replace parent TID with local TID
  var parentID = Wire(new ParentBundle())
  parentID.sid := taskArb.io.chosen
  parentID.did := taskArb.io.out.bits.data("field0").taskID

  var parentCall = Wire(new Call(argTypes))
  parentCall := taskArb.io.out.bits
  for (i <- argTypes.indices) {
    parentCall.data(s"field$i").taskID := freeList.io.deq.bits.data
  }

  /** *************************************************************************
    * Free List
    * Instantiate a queue to hold the unused table task entries. Initialize
    * it on startup
    * *************************************************************************/
  val initQueue = RegInit(true.B)
  val (initCount, initDone) = Counter(true.B, 1 << tlen)
  when(initDone) {
    initQueue := false.B
  }
  val retReg = RegInit(0.U.asTypeOf(Decoupled(new Call(retTypes))))
  when(initQueue) {
    freeList.io.enq.bits.data := initCount.asUInt()
    freeList.io.enq.valid := true.B
  }.otherwise {
    freeList.io.enq.bits.data := retReg.bits.data("field0").taskID
    freeList.io.enq.valid := retReg.fire()
  }
  freeList.io.deq.ready := exeList.io.enq.ready && taskArb.io.out.valid

  /***************************************************************************
    * Task Request Queue
    * Instantiate a buffer to decouple input requests from execution.
    **************************************************************************/
  exeList.io.enq.valid := freeList.io.deq.valid && taskArb.io.out.valid
  exeList.io.enq.bits := taskArb.io.out.bits
  exeList.io.enq.bits := taskArb.io.out.bits
  for (i <- argTypes.indices) {
    exeList.io.enq.bits.data(s"field$i").taskID := freeList.io.deq.bits.data
  }
  exeList.io.enq.bits.enable.taskID := freeList.io.deq.bits.data

  when (exeList.io.enq.fire) {
    parentTable.write(freeList.io.deq.bits, parentID)
  }


  /***************************************************************************
    * Spawn task to an available child
    **************************************************************************/
  val readyBits = WireInit(VecInit(Seq.fill(NumChild)(false.B)))
  for (i <- 0 until NumChild) {
    io.childOut(i).bits := exeList.io.deq.bits
    io.childOut(i).valid := false.B
    readyBits(i) := io.childOut(i).ready
  }
  val sel = PriorityEncoder(readyBits)
  io.childOut(sel).valid := exeList.io.deq.valid
  exeList.io.deq.ready := io.childOut(sel).ready

  /***************************************************************************
    * Return Result ID Lookup
    * Delay the result by one clock cycle to look up the parent ID and TID
    * Replace the PID and TID with the original from the parent request
    **************************************************************************/
  val ChildArb = Module(new RRArbiter(new Call(retTypes), NumChild))
  ChildArb.io.in <> io.childIn
  retReg <> ChildArb.io.out

  // Lookup the original PID and TID
  val taskEntry = parentTable(io.childIn(0).bits.data("field0").taskID)

  // Restore the original TID and PID in the return value.
  val finalReturn = Wire(Decoupled(new Call(retTypes)))
  finalReturn.valid := retReg.valid
  finalReturn.bits := retReg.bits
  for (i <- retTypes.indices) {
    finalReturn.bits.data(s"field$i").taskID := taskEntry.did
    finalReturn.bits.enable.taskID := taskEntry.did
  }
  retReg.ready := finalReturn.ready

  /***************************************************************************
    * Output Demux
    * Send the return value back to its parent
    **************************************************************************/
  for (i <- 0 until io.parentOut.length) {
    io.parentOut(i).valid := false.B
    io.parentOut(i).bits := finalReturn.bits
  }
  finalReturn.ready :=  io.parentOut(taskEntry.sid).ready
  io.parentOut(taskEntry.sid).valid := finalReturn.valid

}




