package dataflow

import chisel3._
import chisel3.util._

import utility._
import node._
import config._
import interfaces._
import arbiters._
import memory._

import utility.Constants._

class MixedDataFlow(implicit val p: Parameters) extends Module with CoreParams {

  val io = IO(new Bundle { val dummy = Input(UInt { 32.W }) })

/*=====================================================
=             16 bit operations                       =
=====================================================*/

  val StackFile = Module(new TypeStackFile(ID = 0, Size = 32, NReads = 2, NWrites = 2)(WControl = new WriteMemoryController(NumOps = 2, BaseSize = 2, NumEntries = 2)(p))(RControl = new ReadMemoryController(NumOps = 2, BaseSize = 2, NumEntries = 2)(p)))

  val Stores = for (i <- 0 until 2) yield {
    val store = Module(new UnTypStore(NumPredOps = 0, NumSuccOps = 1, NumOuts = 1, Typ = MT_HU, ID = i, RouteID = i)(p))
    store
  }

  val Loads = for (i <- 0 until 2) yield {
    val load = Module(new UnTypLoad(NumPredOps = 1, NumSuccOps = 0, NumOuts = 2, Typ = MT_HU, ID = i, RouteID = i)(p))
    load
  }

  val Ops = for (i <- 0 until 2 / 2) yield {
    val op = Module(new ComputeNode(NumOuts = 1, ID = 0, opCode = "Add")(sign = false)(p))
    op
  }

  for (i <- 0 until 2) {
    StackFile.io.ReadIn(i) <> Loads(i).io.memReq
    Loads(i).io.memResp <> StackFile.io.ReadOut(i)

    StackFile.io.WriteIn(i) <> Stores(i).io.memReq
    Stores(i).io.memResp <> StackFile.io.WriteOut(i)

    Stores(i).io.GepAddr.bits.data := (4 + (xlen / 8) * i).U
    Stores(i).io.GepAddr.bits.predicate := true.B
    Stores(i).io.GepAddr.valid := true.B

    Stores(i).io.inData.bits.data := (0x2222 + i).U
    Stores(i).io.inData.bits.predicate := true.B
    Stores(i).io.inData.valid := true.B

    Stores(i).io.enable.bits.control := true.B
    Stores(i).io.enable.bits.taskID := 0.U
    Stores(i).io.enable.valid := true.B
    Stores(i).io.Out(0).ready := true.B

    Loads(i).io.GepAddr.bits.data := (4 + (xlen / 8) * i).U
    Loads(i).io.GepAddr.bits.predicate := true.B
    Loads(i).io.GepAddr.valid := true.B

    Loads(i).io.enable.bits.control := true.B
    Loads(i).io.enable.bits.taskID := 0.U
    Loads(i).io.enable.valid := true.B
    Loads(i).io.Out(0).ready := true.B

    Loads(i).io.PredOp(0) <> Stores(i).io.SuccOp(0)

  }

  for (i <- 0 until 2 / 2) {
    Ops(i).io.enable.bits.control := true.B
    Ops(i).io.enable.bits.taskID := 0.U
    Ops(i).io.enable.valid := true.B
    // Ops(i).io.Out(0).ready := true.B
    Ops(i).io.LeftIO <> Loads(i).io.Out(1)
    Ops(i).io.RightIO <> Loads(i + 1).io.Out(1)
  }

/*========================================================
=              32 bit operations                         =
========================================================*/
 val config32b = p.alterPartial({case XLEN => 32})

 val StackFile32b = Module(new TypeStackFile(ID = 0, Size = 32, NReads = 2, NWrites = 2)(WControl = new WriteMemoryController(NumOps = 2, BaseSize = 2, NumEntries = 2)(config32b))(RControl = new ReadMemoryController(NumOps = 2, BaseSize = 2, NumEntries = 2)(config32b))
                     (config32b))

  val Stores32b = for (i <- 0 until 2) yield {
    val store32b = Module(new UnTypStore(NumPredOps = 0, NumSuccOps = 1, NumOuts = 1, Typ = MT_WU, ID = i, RouteID = i)
                (config32b))
    store32b
  }

  val Loads32b = for (i <- 0 until 2) yield {
    val load32b = Module(new UnTypLoad(NumPredOps = 1, NumSuccOps = 0, NumOuts = 2, Typ = MT_WU, ID = i, RouteID = i)
               (config32b))
    load32b
  }

  val Ops32b = for (i <- 0 until 2 / 2) yield {
    val op32b = Module(new ComputeNode(NumOuts = 1, ID = 0, opCode = "Add")(sign = false)
             (config32b))
    op32b
  }

  for (i <- 0 until 2) {
    StackFile32b.io.ReadIn(i) <> Loads32b(i).io.memReq
    Loads32b(i).io.memResp <> StackFile32b.io.ReadOut(i)

    StackFile32b.io.WriteIn(i) <> Stores32b(i).io.memReq
    Stores32b(i).io.memResp <> StackFile32b.io.WriteOut(i)

    Stores32b(i).io.GepAddr.bits.data := (8 + (32 / 8) * i).U
    Stores32b(i).io.GepAddr.bits.predicate := true.B
    Stores32b(i).io.GepAddr.valid := true.B

    Stores32b(i).io.inData.bits.data := (0x11112222 + i).U
    Stores32b(i).io.inData.bits.predicate := true.B
    Stores32b(i).io.inData.valid := true.B

    Stores32b(i).io.enable.bits.control := true.B
    Stores32b(i).io.enable.bits.taskID := 0.U
    Stores32b(i).io.enable.valid := true.B
    Stores32b(i).io.Out(0).ready := true.B

    Loads32b(i).io.GepAddr.bits.data := (8 + (32 / 8) * i).U
    Loads32b(i).io.GepAddr.bits.predicate := true.B
    Loads32b(i).io.GepAddr.valid := true.B

    Loads32b(i).io.enable.bits.control := true.B
    Loads32b(i).io.enable.bits.taskID := 0.U
    Loads32b(i).io.enable.valid := true.B
    Loads32b(i).io.Out(0).ready := true.B

    Loads32b(i).io.PredOp(0) <> Stores32b(i).io.SuccOp(0)

  }

  for (i <- 0 until 2 / 2) {
    Ops32b(i).io.enable.bits.control := true.B
    Ops32b(i).io.enable.bits.taskID := 0.U
    Ops32b(i).io.enable.valid := true.B
    // Ops32b(i).io.Out(0).ready := true.B
    Ops32b(i).io.LeftIO <> Loads32b(i).io.Out(1)
    Ops32b(i).io.RightIO <> Loads32b(i + 1).io.Out(1)
  }

  val config64b = p.alterPartial({case XLEN => 64})
  val Ops64b = for (i <- 0 until 2 / 2) yield {
     val op64b = Module(new ComputeNode(NumOuts = 1, ID = 0, opCode = "Add")(sign = false)
              (config64b))
     op64b
  }

  for (i <- 0 until 2 / 2) {
    Ops64b(i).io.enable.bits.control := true.B
    Ops64b(i).io.enable.bits.taskID := 0.U
    Ops64b(i).io.enable.valid := true.B
    Ops64b(i).io.Out(0).ready := true.B
    Ops64b(i).io.LeftIO <> Ops32b(i).io.Out(0)
    Ops64b(i).io.RightIO <> Ops(i).io.Out(0)
  }
}
