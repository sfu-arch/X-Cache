package dataflow

import chisel3._
import chisel3.util._

import node._
import config._
import interfaces._
import arbiters._
import memory._

class TypeMemDataFlow(implicit val p: Parameters) extends Module with CoreParams{

	val io = IO(new Bundle{val dummy = Input(UInt{32.W})})

	val StackFile = Module(new TypeStackFile(ID=0,Size=32,NReads=2,NWrites=2)
		            (WControl=new WriteTypMemoryController(NumOps=2,BaseSize=2))
		            (RControl=new ReadTypMemoryController(NumOps=2,BaseSize=2)))
	val Store     = Module(new TypStore(NumPredOps=0,NumSuccOps=1,NumOuts=1,ID=0,RouteID=0))
	val Load      = Module(new TypLoad(NumPredOps=1,NumSuccOps=0,NumOuts=2,ID=0,RouteID=0))
	val Store1    = Module(new TypStore(NumPredOps=0,NumSuccOps=1,NumOuts=1,ID=0,RouteID=1))
	val Load1     = Module(new TypLoad(NumPredOps=1,NumSuccOps=0,NumOuts=2,ID=0,RouteID=1))


StackFile.io.ReadIn(0) <> Load.io.memReq
Load.io.memResp  <> StackFile.io.ReadOut(0)

StackFile.io.WriteIn(0) <> Store.io.memReq
Store.io.memResp  <> StackFile.io.WriteOut(0)


Store.io.GepAddr.bits.data      := 8.U
Store.io.GepAddr.bits.predicate := true.B
Store.io.GepAddr.valid          := true.B

Store.io.inData.bits.data       := 0x1111222211112222L.U
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

StackFile.io.ReadIn(1) <> Load1.io.memReq
Load1.io.memResp  <> StackFile.io.ReadOut(1)

StackFile.io.WriteIn(1) <> Store1.io.memReq
Store1.io.memResp  <> StackFile.io.WriteOut(1)


Store1.io.GepAddr.bits.data      := 16.U
Store1.io.GepAddr.bits.predicate := true.B
Store1.io.GepAddr.valid          := true.B

Store1.io.inData.bits.data       := 0x3333444433334444L.U
Store1.io.inData.bits.predicate  := true.B
Store1.io.inData.valid           := true.B

Store1.io.enable.bits  := true.B
Store1.io.enable.valid := true.B
Store1.io.Out(0).ready := true.B

Load1.io.GepAddr.bits.data      := 16.U
Load1.io.GepAddr.bits.predicate := true.B
Load1.io.GepAddr.valid          := true.B

Load1.io.enable.bits  := true.B
Load1.io.enable.valid := true.B
Load1.io.Out(0).ready := true.B

Load1.io.PredOp(0) <> Store1.io.SuccOp(0)

val typadd = Module(new TypCompute(NumOuts=1,ID=0,"Add")(true)(new vec2))
typadd.io.enable.bits  := true.B
typadd.io.enable.valid := true.B
typadd.io.Out(0).ready := true.B
typadd.io.LeftIO    <>  Load.io.Out(1)
typadd.io.RightIO   <>  Load1.io.Out(1)
}

