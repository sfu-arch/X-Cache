package dataflow

import chisel3._
import chisel3.util._

import node._
import config._
import interfaces._
import arbiters._
import memory._
import accel._

class UnTypMemDataFlow(val ops:Int)(implicit val p: Parameters) extends Module with CoreParams{

	val io = IO(new Bundle{
		val CacheResp = Flipped(Valid(new CacheRespT))
		val CacheReq = Decoupled(new CacheReq)
		val Out = Vec(ops, Decoupled(new DataBundle()))
	})

  // Fire enables and loads/stores after reset
  val init = RegNext(init=true.B, next=false.B)
	val fireEnables = RegNext(init=false.B, next=init)
  val fireLoadsStores = RegNext(init=false.B, next=fireEnables)

	val StackFile = Module(new TypeStackFile(ID=0,Size=32,NReads=ops*2,NWrites=ops*2)
			          (WControl=new WriteMemoryController(NumOps=ops*2,BaseSize=2,NumEntries=2))
		            (RControl=new ReadMemoryController(NumOps=ops*2,BaseSize=2,NumEntries=2)))
	val CacheMem = Module(new UnifiedController(ID=0,Size=32,NReads=ops*2,NWrites=ops*2)
		            (WControl=new WriteMemoryController(NumOps=ops*2,BaseSize=2,NumEntries=2))
	              (RControl=new ReadMemoryController(NumOps=ops*2,BaseSize=2,NumEntries=2))
	              (RWArbiter=new ReadWriteArbiter()))

	io.CacheReq <> CacheMem.io.CacheReq
	CacheMem.io.CacheResp <> io.CacheResp

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
 	CacheMem.io.ReadIn(i) <> Loads(i).io.memReq
 	Loads(i).io.memResp  <> CacheMem.io.ReadOut(i)

 	CacheMem.io.WriteIn(i) <> Stores(i).io.memReq
 	Stores(i).io.memResp  <> CacheMem.io.WriteOut(i)


 	Stores(i).io.GepAddr.bits.data      := (8+(xlen/8)*i).U
 	Stores(i).io.GepAddr.bits.predicate := true.B
 	Stores(i).io.GepAddr.valid          := fireLoadsStores

 	Stores(i).io.inData.bits.data       := (i+1).U
 	Stores(i).io.inData.bits.predicate  := true.B
 	Stores(i).io.inData.valid           := fireLoadsStores

 	Stores(i).io.enable.bits.control  := true.B
 	Stores(i).io.enable.valid := fireEnables
 	Stores(i).io.Out(0).ready := true.B


 	Loads(i).io.GepAddr.bits.data      := (8+(xlen/8)*i).U
 	Loads(i).io.GepAddr.bits.predicate := true.B
 	Loads(i).io.GepAddr.valid          := fireLoadsStores

 	Loads(i).io.enable.bits.control  := true.B
 	Loads(i).io.enable.valid := fireEnables
 	Loads(i).io.Out(0).ready := true.B

 	Loads(i).io.PredOp(0) <> Stores(i).io.SuccOp(0)

 }

 for (i <- 0 until ops) {
   Ops(i).io.enable.bits.control := true.B
   Ops(i).io.enable.valid := true.B
   Ops(i).io.LeftIO <> Loads(2*i).io.Out(1)
   Ops(i).io.RightIO <> Loads(2*i + 1).io.Out(1)
   io.Out(i) <> Ops(i).io.Out(0)
 }

}
