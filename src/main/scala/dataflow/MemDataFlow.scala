package dataflow

import chisel3._
import chisel3.util._

import node._
import config._
import interfaces._
import arbiters._
import memory._

class UnTypMemDataFlow(implicit val p: Parameters) extends Module with CoreParams{

	val io = IO(new Bundle{val dummy = Input(UInt{32.W})})

	val StackFile = Module(new TypeStackFile(ID=0,Size=32,NReads=2,NWrites=2)
		            (WControl=new WriteMemoryController(NumOps=2,BaseSize=2,NumEntries=2))
		            (RControl=new ReadMemoryController(NumOps=2,BaseSize=2,NumEntries=2)))
	val Store     = Module(new UnTypStore(NumPredOps=0,NumSuccOps=1,NumOuts=1,ID=0,RouteID=0))
	val Load      = Module(new UnTypLoad(NumPredOps=1,NumSuccOps=0,NumOuts=1,ID=0,RouteID=0))
	// val Store1    = Module(new TypStore(NumPredOps=0,NumSuccOps=1,NumOuts=1,ID=0,RouteID=1))
	// val Load1     = Module(new TypLoad(NumPredOps=1,NumSuccOps=0,NumOuts=2,ID=0,RouteID=1))


StackFile.io.ReadIn(0) <> Load.io.memReq
Load.io.memResp  <> StackFile.io.ReadOut(0)

StackFile.io.WriteIn(0) <> Store.io.memReq
Store.io.memResp  <> StackFile.io.WriteOut(0)


Store.io.GepAddr.bits.data      := 8.U
Store.io.GepAddr.bits.predicate := true.B
Store.io.GepAddr.valid          := true.B

Store.io.inData.bits.data       := 0x11112222.U
Store.io.inData.bits.predicate  := true.B
Store.io.inData.valid           := true.B

Store.io.enable.bits  := true.B
Store.io.enable.valid := true.B
Store.io.Out(0).ready := true.B


Load.io.GepAddr.bits.data      := 8.U
Load.io.GepAddr.bits.predicate := true.B
Load.io.GepAddr.valid          := true.B

Load.io.enable.bits  := true.B
Load.io.enable.valid := true.B
Load.io.Out(0).ready := true.B

Load.io.PredOp(0) <> Store.io.SuccOp(0)


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

// package dataflow

// import chisel3._
// import chisel3.util._

// import node._
// import config._
// import interfaces._
// import arbiters._


// abstract class MemDFIO()(implicit val p: Parameters) extends Module with CoreParams{

//   val io = IO(new Bundle{
//     val memOpAck = Decoupled(UInt(1.W)) //TODO 0 bits
//   })
// }

// class MemDataFlow(implicit p: Parameters) extends MemDFIO()(p){


//   //class LoadNode(NumPredOps: Int,
//     //NumSuccOps: Int,
//     //NumOuts: Int,
//     //Typ: UInt = MT_W, ID: Int)(implicit p: Parameters)

//   val m0 = Module(new StoreNode(1,2,1,ID=0,RouteID=0))
//   val m1 = Module(new LoadNode(1,1,1,ID=1,RouteID=0))
//   val m2 = Module(new LoadNode(2,1,1,ID=2,RouteID=1))
//   val counter = RegInit(400.U(32.W))
//   counter := counter + 500.U
//   val RF = Module(new CentralizedStackRegFile(Size=32, NReads=2, NWrites=1))

//   RF.io.WriteIn(0) <> m0.io.memReq
//   m0.io.memResp    <> RF.io.WriteOut(0)

//   RF.io.ReadIn(0)  <> m1.io.memReq
//   m1.io.memResp    <> RF.io.ReadOut(0)

//   RF.io.ReadIn(1)  <> m2.io.memReq
//   m2.io.memResp    <> RF.io.ReadOut(1)

//   // m0.io.SuccOp(0) := true.B
//   // m0.io.SuccOp(1) := true.B
//   // m1.io.SuccOp(0) := true.B
//   // m1.io.PredOp(0).valid := true.B
//   // m1.io.SuccOp(0).ready := true.B
//   // m0.io.SuccOp(0).ready := true.B
//   // m0.io.SuccOp(1).ready := true.B
//   // m2.io.PredOp(0).valid := true.B
//   // m2.io.PredOp(1).valid := true.B

//   m1.io.PredOp(0) <> m0.io.SuccOp(0)
//   m2.io.PredOp(0) <> m0.io.SuccOp(1)
//   m2.io.PredOp(1) <> m1.io.SuccOp(0)

//   m0.io.enable.bits  := true.B
//   m0.io.enable.valid := true.B
//   m1.io.enable.bits  := true.B
//   m1.io.enable.valid := true.B
//   m2.io.enable.bits  := true.B
//   m2.io.enable.valid := true.B

//   // printf(p"RouteID : ${m2.io.memReq.bits.RouteID}")


//   m0.io.GepAddr.bits.data      := 12.U
//   m0.io.GepAddr.valid          := true.B
//   m0.io.GepAddr.bits.predicate := true.B
//   m0.io.inData.bits.data       := counter
//   m0.io.inData.valid           := true.B
//   m0.io.inData.bits.predicate  := true.B
  
//   m0.io.PredOp(0).valid        := true.B
//   m0.io.Out(0).ready           := true.B


//   m1.io.GepAddr.bits.data      := 12.U
//   m1.io.GepAddr.valid          := true.B
//   m1.io.GepAddr.bits.predicate := true.B
//   m1.io.Out(0).ready           := true.B



//   m2.io.GepAddr.bits.data      := 12.U
//   m2.io.GepAddr.valid          := true.B
//   m2.io.GepAddr.bits.predicate := true.B
//   m2.io.SuccOp(0).ready        := true.B
//   m2.io.Out(0).ready           := true.B


//   //m0.io.memResp.data  := m1.io.ReadOut(0).data

//   //m0.io.gepAddr       <> io.gepAddr
//   //m0.io.PredOp(0)  <> io.PredOp
//   //io.memOpAck         <> m0.io.memOpAck
// }
