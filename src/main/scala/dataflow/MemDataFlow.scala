package dataflow

import chisel3._
import chisel3.util._

import node._
import config._
import interfaces._
import arbiters._
import memory._

class UnTypMemDataFlow(val ops:Int)(implicit val p: Parameters) extends Module with CoreParams{

	val io = IO(new Bundle{
		val Out = Vec(ops, Decoupled(new DataBundle()))
	})

  // Fire enables and loads/stores after reset
  val init = RegNext(init=true.B, next=false.B)
	val fireEnables = RegNext(init=false.B, next=init)
  val fireLoadsStores = RegNext(init=false.B, next=fireEnables)

	val StackFile = Module(new TypeStackFile(ID=0,Size=32,NReads=ops*2,NWrites=ops*2)
		            (WControl=new WriteMemoryController(NumOps=ops*2,BaseSize=2,NumEntries=2))
		            (RControl=new ReadMemoryController(NumOps=ops*2,BaseSize=2,NumEntries=2)))
	// val Store     = Module(new UnTypStore(NumPredOps=0,NumSuccOps=1,NumOuts=1,ID=0,RouteID=0))
	// val Load      = Module(new UnTypLoad(NumPredOps=1,NumSuccOps=0,NumOuts=1,ID=0,RouteID=0))
	// val Store1    = Module(new TypStore(NumPredOps=0,NumSuccOps=1,NumOuts=1,ID=0,RouteID=1))
	// val Load1     = Module(new TypLoad(NumPredOps=1,NumSuccOps=0,NumOuts=2,ID=0,RouteID=1))


 val Stores = for (i <- 0 until ops*2) yield {
    val store = Module(new UnTypStore(NumPredOps=0,NumSuccOps=1,NumOuts=1,ID=i,RouteID=i))
    store
  }

 val Loads = for (i <- 0 until ops*2) yield {
    val load = Module(new UnTypLoad(NumPredOps=1,NumSuccOps=0,NumOuts=2,ID=i,RouteID=i))
    load
  }

  val Ops = for (i <- 0 until ops) yield {
    val op  = Module(new ComputeNode(NumOuts = 1, ID = 0, opCode = "Add")(sign = false))
    op
  }


 for (i <- 0 until ops*2) {
 	StackFile.io.ReadIn(i) <> Loads(i).io.memReq
 	Loads(i).io.memResp  <> StackFile.io.ReadOut(i)

 	StackFile.io.WriteIn(i) <> Stores(i).io.memReq
 	Stores(i).io.memResp  <> StackFile.io.WriteOut(i)


 	Stores(i).io.GepAddr.bits.data      := (8+(xlen/8)*i).U
 	Stores(i).io.GepAddr.bits.predicate := true.B
 	Stores(i).io.GepAddr.valid          := fireLoadsStores

 	Stores(i).io.inData.bits.data       := (i+1).U
 	Stores(i).io.inData.bits.predicate  := true.B
 	Stores(i).io.inData.valid           := fireLoadsStores

 	Stores(i).io.enable.bits  := true.B
 	Stores(i).io.enable.valid := fireEnables
 	Stores(i).io.Out(0).ready := true.B


 	Loads(i).io.GepAddr.bits.data      := (8+(xlen/8)*i).U
 	Loads(i).io.GepAddr.bits.predicate := true.B
 	Loads(i).io.GepAddr.valid          := fireLoadsStores

 	Loads(i).io.enable.bits  := true.B
 	Loads(i).io.enable.valid := fireEnables
 	Loads(i).io.Out(0).ready := true.B

 	Loads(i).io.PredOp(0) <> Stores(i).io.SuccOp(0)

 }

 for (i <- 0 until ops) {
   Ops(i).io.enable.bits := true.B
   Ops(i).io.enable.valid := true.B
   Ops(i).io.LeftIO <> Loads(2*i).io.Out(1)
   Ops(i).io.RightIO <> Loads(2*i + 1).io.Out(1)
   io.Out(i) <> Ops(i).io.Out(0)
 }

// StackFile.io.ReadIn(0) <> Load.io.memReq
// Load.io.memResp  <> StackFile.io.ReadOut(0)

// StackFile.io.WriteIn(0) <> Store.io.memReq
// Store.io.memResp  <> StackFile.io.WriteOut(0)


// Store.io.GepAddr.bits.data      := 8.U
// Store.io.GepAddr.bits.predicate := true.B
// Store.io.GepAddr.valid          := true.B

// Store.io.inData.bits.data       := 0x11112222.U
// Store.io.inData.bits.predicate  := true.B
// Store.io.inData.valid           := true.B

// Store.io.enable.bits  := true.B
// Store.io.enable.valid := true.B
// Store.io.Out(0).ready := true.B


// Load.io.GepAddr.bits.data      := 8.U
// Load.io.GepAddr.bits.predicate := true.B
// Load.io.GepAddr.valid          := true.B

// Load.io.enable.bits  := true.B
// Load.io.enable.valid := true.B
// Load.io.Out(0).ready := true.B

// Load.io.PredOp(0) <> Store.io.SuccOp(0)


/*   Connect up second pair of ops */

// StackFile.io.ReadIn(1) <> Load1.io.memReq
// Load1.io.memResp  <> StackFile.io.ReadOut(1)

// StackFile.io.WriteIn(1) <> Store1.io.memReq
// Store1.io.memResp  <> StackFile.io.WriteOut(1)


// Store1.io.GepAddr.bits.data      := 16.U
// Store1.io.GepAddr.bits.predicate := true.B
// Store1.io.GepAddr.valid          := true.B

// Store1.io.inData.bits.data       := 0x3333444433334444L.U
// Store1.io.inData.bits.predicate  := true.B
// Store1.io.inData.valid           := true.B

// Store1.io.enable.bits  := true.B
// Store1.io.enable.valid := true.B
// Store1.io.Out(0).ready := true.B

// Load1.io.GepAddr.bits.data      := 16.U
// Load1.io.GepAddr.bits.predicate := true.B
// Load1.io.GepAddr.valid          := true.B

// Load1.io.enable.bits  := true.B
// Load1.io.enable.valid := true.B
// Load1.io.Out(0).ready := true.B

// Load1.io.PredOp(0) <> Store1.io.SuccOp(0)

// val typadd = Module(new TypCompute(NumOuts=1,ID=0,"Add")(true)(new vec2))
// typadd.io.enable.bits  := true.B
// typadd.io.enable.valid := true.B
// typadd.io.Out(0).ready := true.B
// typadd.io.LeftIO    <>  Load.io.Out(1)
// typadd.io.RightIO   <>  Load1.io.Out(1)
}
