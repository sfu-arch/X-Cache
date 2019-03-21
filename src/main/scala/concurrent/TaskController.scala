package dataflow

import chisel3.{Flipped, Module, _}
import chisel3.util._
import config._
import config.Parameters
import interfaces._
import node._
import util._
import utility.UniformPrintfs

class ParentBundle()(implicit p: Parameters) extends CoreBundle {
  val sid = UInt(tlen.W) // Static ID (e.g. parent identifier)
  val did = UInt(tlen.W) // Dynamic ID (e.g. parent's task #)
  //  override def cloneType: this.type = new ParentBundle(argTypes).asInstanceOf[this.type]
}

class TaskControllerIO(val argTypes: Seq[Int], val retTypes: Seq[Int], numParent: Int, numChild: Int)(implicit p: Parameters)
  extends CoreBundle {
  val parentIn = Vec(numParent, Flipped(Decoupled(new Call(argTypes)))) // Requests from calling block(s)
  val parentOut = Vec(numParent, Decoupled(new Call(retTypes))) // Returns to calling block(s)
  val childIn = Vec(numChild, Flipped(Decoupled(new Call(retTypes)))) // Returns from sub-block
  val childOut = Vec(numChild, Decoupled(new Call(argTypes))) // Requests to sub-block

  // 3.1
  override def cloneType = new TaskControllerIO(argTypes, retTypes, numParent, numChild).asInstanceOf[this.type]
}

class TaskController(val argTypes: Seq[Int], val retTypes: Seq[Int], numParent: Int, numChild: Int)
                    (implicit val p: Parameters)
  extends Module with CoreParams with UniformPrintfs {

  // Instantiate TaskController I/O signals
  val io = IO(new TaskControllerIO(argTypes, retTypes, numParent, numChild))
  // Printf debugging

  /** *************************************************************************
    * Input Arbiter
    * Instantiate a round-robin arbiter to select from multiple possible inputs
    * *************************************************************************/
  val taskArb               = Module(new RRArbiter(new Call(argTypes), numParent))
  val freeList              = Module(new Queue(UInt(tlen.W), 1 << tlen))
  val exeList               = Module(new Queue(new Call(argTypes), 1 << tlen))
  val parentTable           = SyncReadMem(1 << tlen, new ParentBundle( ))
  val retExpand             = Module(new ExpandNode(NumOuts = 2, ID = 0)(new Call(retTypes)))
  val (initCount, initDone) = Counter(true.B, 1 << tlen)
  val initQueue             = RegInit(true.B)

  //  val parentTable =RegInit(VecInit(Seq.fill(1<<tlen)(0.U.asTypeOf(new ParentBundle()))))

  for (i <- 0 until numParent) {
    taskArb.io.in(i) <> io.parentIn(i)
  }
  taskArb.io.out.ready := exeList.io.enq.ready && freeList.io.deq.valid

  // Replace parent TID with local TID
  var parentID = Wire(new ParentBundle( ))
  parentID.sid := taskArb.io.chosen
  parentID.did := taskArb.io.out.bits.enable.taskID

  /** *************************************************************************
    * Free List
    * Instantiate a queue to hold the unused table task entries. Initialize
    * it on startup
    * *************************************************************************/
  when(initDone) {
    initQueue := false.B
  }

  /**
      @TODO: Making retExpand output.ready false at initialization looks wrong
      It needs more investigation
    */

  when(initQueue) {
    freeList.io.enq.bits := initCount.asUInt( )
    freeList.io.enq.valid := true.B
    //retExpand.io.Out(0).ready := false.B
  }.otherwise {
    freeList.io.enq.bits := retExpand.io.Out(0).bits.enable.taskID
    freeList.io.enq.valid := retExpand.io.Out(0).valid
    //retExpand.io.Out(0).ready := freeList.io.enq.ready
  }
  retExpand.io.Out(0).ready := freeList.io.enq.ready
  freeList.io.deq.ready := exeList.io.enq.ready & taskArb.io.out.valid

  /** *************************************************************************
    * Task Request Queue
    * Instantiate a buffer to decouple input requests from execution.
    * *************************************************************************/
  exeList.io.enq.valid := freeList.io.deq.valid && taskArb.io.out.valid
  exeList.io.enq.bits := taskArb.io.out.bits
  exeList.io.enq.bits := taskArb.io.out.bits
  for (i <- argTypes.indices) {
    exeList.io.enq.bits.data(s"field$i").taskID := freeList.io.deq.bits
  }
  exeList.io.enq.bits.enable.taskID := freeList.io.deq.bits

  when(exeList.io.enq.fire) {
    parentTable.write(freeList.io.deq.bits, parentID)
    //    parentTable(freeList.io.deq.bits) := parentID
  }


  /** *************************************************************************
    * Spawn task to an available child
    * *************************************************************************/
  val readyBits = WireInit(VecInit(Seq.fill(numChild)(false.B)))
  for (i <- 0 until numChild) {
    io.childOut(i).bits := exeList.io.deq.bits
    io.childOut(i).valid := false.B
    readyBits(i) := io.childOut(i).ready
  }

  val sel = PriorityEncoder(readyBits)
  io.childOut(sel).valid := exeList.io.deq.valid
  exeList.io.deq.ready := io.childOut(sel).ready

  /** *************************************************************************
    * Return Result ID Lookup
    * Delay the result by one clock cycle to look up the parent ID and TID
    * Replace the PID and TID with the original from the parent request
    * *************************************************************************/
  val s_IDLE :: s_COMPUTE :: Nil = Enum(2)
  val state                      = RegInit(s_IDLE)
  val ChildArb                   = Module(new RRArbiter(new Call(retTypes), numChild))

  ChildArb.io.in <> io.childIn
  retExpand.io.InData <> ChildArb.io.out
  retExpand.io.enable.enq(ControlBundle.active(true.B))

  // Lookup the original PID and TID
  val taskEntryReg = RegInit(0.U.asTypeOf(new ParentBundle( )))
  val latchEntry   = RegNext(init = false.B, next = ChildArb.io.out.fire( ))
  val taskEntry    = parentTable.read(ChildArb.io.out.bits.enable.taskID)
  when(latchEntry) {
    taskEntryReg := taskEntry
  }
  // Restore the original TID and PID in the return value.

  /** *************************************************************************
    * Output Demux
    * Send the return value back to its parent
    * *************************************************************************/
  for (i <- 0 until io.parentOut.length) {
    io.parentOut(i).valid := false.B
    io.parentOut(i).bits := retExpand.io.Out(1).bits
    io.parentOut(i).bits.enable.taskID := taskEntryReg.did
    for (j <- retTypes.indices) {
      io.parentOut(i).bits.data(s"field$j").taskID := taskEntryReg.did
    }
  }
  retExpand.io.Out(1).ready := io.parentOut(taskEntryReg.sid).ready
  io.parentOut(taskEntryReg.sid).valid := retExpand.io.Out(1).valid

  /** *************************************************************************
    * Debug
    * *************************************************************************/

  val maxActive  = RegInit(0.U(24.W))
  val error_flag = RegInit(false.B)

  val numActive   = (1 << tlen).U - freeList.io.count - 1.U
  val numActive_R = RegNext(numActive)
  when(!initQueue && numActive > maxActive) {
    maxActive := numActive
  }
  when(numActive_R === 1.U && numActive === 0.U) {
    printf("*** maxActive: %d\n", maxActive)
  }

  /*
  val activeID = RegInit(VecInit(Seq.fill(1<<tlen)(false.B)))
  when(exeList.io.deq.fire()) {
    when(activeID(exeList.io.deq.bits.enable.taskID) === true.B) {
      error_flag := true.B
      printf("*** ID re-used active ID error %d\n", error_flag)
    }
    activeID(exeList.io.deq.bits.enable.taskID) := true.B
  }
  when(ChildArb.io.out.fire()) {
    when(activeID(ChildArb.io.out.bits.enable.taskID) === false.B) {
      error_flag := true.B
      printf("*** Error: Duplicate ID returned %d\n", error_flag)
    }
    activeID(ChildArb.io.out.bits.enable.taskID) := false.B
  }
  val numOut = RegInit(0.U(16.W))
  when(ChildArb.io.out.fire() && !retExpand.io.Out(1).fire()) {
    when(numOut === 1.U) {
      error_flag := true.B
      printf("*** Error: Lost return in output %d\n", error_flag)
    }
    numOut := numOut + 1.U;
  }.elsewhen(!ChildArb.io.out.fire() && retExpand.io.Out(1).fire()){
    when(numOut === 0.U) {
      error_flag := true.B
      printf("*** Error: numOut under-run %d\n", error_flag)
    }
    numOut := numOut - 1.U;
  }
  */
}




