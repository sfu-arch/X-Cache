package dataflow

import accel._
import arbiters._
import chisel3._
import chisel3.util._
import chisel3.Module._
import chisel3.testers._
import chisel3.iotesters._
import config._
import control._
import interfaces._
import junctions._
import loop._
import memory._
import muxes._
import node._
import org.scalatest._
import regfile._
import stack._
import util._


  /* ================================================================== *
   *                   PRINTING PORTS DEFINITION                        *
   * ================================================================== */

abstract class test06DFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32,32))))
    val MemResp = Flipped(Valid(new MemResp))
    val MemReq = Decoupled(new MemReq)
    val out = Decoupled(new Call(List(32)))
  })
}

class test06DF(implicit p: Parameters) extends test06DFIO()(p) {


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  val MemCtrl = Module(new UnifiedController(ID=0, Size=32, NReads=3, NWrites=3)
		 (WControl=new WriteMemoryController(NumOps=3, BaseSize=2, NumEntries=2))
		 (RControl=new ReadMemoryController(NumOps=3, BaseSize=2, NumEntries=2))
		 (RWArbiter=new ReadWriteArbiter()))

  io.MemReq <> MemCtrl.io.MemReq
  MemCtrl.io.MemResp <> io.MemResp

  val StackPointer = Module(new Stack(NumOps = 2))

  val InputSplitter = Module(new SplitCallNew(List(1,1)))
  InputSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val entry = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 28, BID = 0))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  %alloc0 = alloca [2 x i32], align 4
  //val alloc0 = Module(new AllocaNode(ID = 0, RouteID=0, NumOuts=4))
  val alloc0 = Module(new AllocaNode(NumOuts=4, RouteID=0, ID=0, FrameSize=12))

  //  %alloc1 = alloca [1 x i32], align 4
  //val alloc1 = Module(new AllocaNode(ID = 1, RouteID=1, NumOuts=2))
  val alloc1 = Module(new AllocaNode(NumOuts=2, RouteID=1, ID=1, FrameSize=12))

  //  %arrayidx = getelementptr inbounds [2 x i32], [2 x i32]* %alloc0, i32 0, i32 0
  //  val arrayidx = Module (new GepTwoNode(NumOuts = 1, ID = 2)(numByte1=8, numByte2=4))
  val arrayidx = Module (new GepArrayTwoNode(NumOuts = 1, ID = 2)(numByte = 4)(size = 2))

  //  store i32 %a, i32* %arrayidx, align 4
  val st_3 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=1, ID=3, RouteID=0))

  //  %arrayidx1 = getelementptr inbounds [2 x i32], [2 x i32]* %alloc0, i32 0, i32 1
  val arrayidx1 = Module(new GepArrayTwoNode(NumOuts=1, ID=4)(numByte = 4)(size = 2))

  //  store i32 %b, i32* %arrayidx1, align 4
  val st_5 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=1, ID=5, RouteID=1))

  //  %arrayidx2 = getelementptr inbounds [2 x i32], [2 x i32]* %alloc0, i32 0, i32 0
  val arrayidx2 = Module(new GepArrayTwoNode(NumOuts=1, ID=6)(numByte = 4)(size = 2))

  //  %0 = load i32, i32* %arrayidx2, align 4
  val ld_7 = Module(new UnTypLoad(NumPredOps=1, NumSuccOps=0, NumOuts=1, ID=7, RouteID=0))

  //  %arrayidx3 = getelementptr inbounds [2 x i32], [2 x i32]* %alloc0, i32 0, i32 1
  val arrayidx3 = Module(new GepTwoNode(NumOuts=1, ID=8)(numByte1=8, numByte2=4))

  //  %1 = load i32, i32* %arrayidx3, align 4
  val ld_9 = Module(new UnTypLoad(NumPredOps=1, NumSuccOps=0, NumOuts=1, ID=9, RouteID=1))

  //  %add = add i32 %0, %1
  val add = Module(new ComputeNode(NumOuts = 1, ID = 10, opCode = "add")(sign=false))

  //  %arrayidx4 = getelementptr inbounds [1 x i32], [1 x i32]* %alloc1, i32 0, i32 0
  val arrayidx4 = Module(new GepArrayTwoNode(NumOuts=1, ID=11)(numByte=4)(size = 1))

  //  store i32 %add, i32* %arrayidx4, align 4
  val st_12 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=1, ID=12, RouteID=2))

  //  %arrayidx5 = getelementptr inbounds [1 x i32], [1 x i32]* %alloc1, i32 0, i32 0
  val arrayidx5 = Module(new GepArrayTwoNode(NumOuts=1, ID=13)(numByte=4)(size = 1))

  //  %2 = load i32, i32* %arrayidx5, align 4
  val ld_14 = Module(new UnTypLoad(NumPredOps=1, NumSuccOps=0, NumOuts=1, ID=14, RouteID=2))

  //  ret i32 %2
  val ret_15 = Module(new RetNode(retTypes=List(32), ID = 15))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i32 0
  val const0 = Module(new ConstNode(value = 0, NumOuts = 1, ID = 0))

  //i32 0
  val const1 = Module(new ConstNode(value = 0, NumOuts = 1, ID = 1))

  //i32 0

  val const2 = Module(new ConstNode(value = 0, NumOuts = 1, ID = 2))

  //i32 1
  val const3 = Module(new ConstNode(value = 1, NumOuts = 1, ID = 3))

  //i32 0
  val const4 = Module(new ConstNode(value = 0, NumOuts = 1, ID = 4))

  //i32 0
  val const5 = Module(new ConstNode(value = 0, NumOuts = 1, ID = 5))

  //i32 0
  val const6 = Module(new ConstNode(value = 0, NumOuts = 1, ID = 6))

  //i32 1
  val const7 = Module(new ConstNode(value = 1, NumOuts = 1, ID = 7))

  //i32 0
  val const8 = Module(new ConstNode(value = 0, NumOuts = 1, ID = 8))

  //i32 0
  val const9 = Module(new ConstNode(value = 0, NumOuts = 1, ID = 9))

  //i32 0
  val const10 = Module(new ConstNode(value = 0, NumOuts = 1, ID = 10))

  //i32 0
  val const11 = Module(new ConstNode(value = 0, NumOuts = 1, ID = 11))



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  entry.io.predicateIn <> InputSplitter.io.Out.enable



  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */



  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  const0.io.enable <> entry.io.Out(0)

  const1.io.enable <> entry.io.Out(1)

  const2.io.enable <> entry.io.Out(2)

  const3.io.enable <> entry.io.Out(3)

  const4.io.enable <> entry.io.Out(4)

  const5.io.enable <> entry.io.Out(5)

  const6.io.enable <> entry.io.Out(6)

  const7.io.enable <> entry.io.Out(7)

  const8.io.enable <> entry.io.Out(8)

  const9.io.enable <> entry.io.Out(9)

  const10.io.enable <> entry.io.Out(10)

  const11.io.enable <> entry.io.Out(11)

  alloc0.io.enable <> entry.io.Out(12)

  alloc1.io.enable <> entry.io.Out(13)

  arrayidx.io.enable <> entry.io.Out(14)

  st_3.io.enable <> entry.io.Out(15)

  arrayidx1.io.enable <> entry.io.Out(16)

  st_5.io.enable <> entry.io.Out(17)

  arrayidx2.io.enable <> entry.io.Out(18)

  ld_7.io.enable <> entry.io.Out(19)

  arrayidx3.io.enable <> entry.io.Out(20)

  ld_9.io.enable <> entry.io.Out(21)

  add.io.enable <> entry.io.Out(22)

  arrayidx4.io.enable <> entry.io.Out(23)

  st_12.io.enable <> entry.io.Out(24)

  arrayidx5.io.enable <> entry.io.Out(25)

  ld_14.io.enable <> entry.io.Out(26)

  ret_15.io.enable <> entry.io.Out(27)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */



  /* ================================================================== *
   *                   PRINT ALLOCA OFFSET                              *
   * ================================================================== */

  alloc0.io.allocaInputIO.bits.size      := 1.U
  alloc0.io.allocaInputIO.bits.numByte   := 8.U
  alloc0.io.allocaInputIO.bits.predicate := true.B
  alloc0.io.allocaInputIO.bits.valid     := true.B
  alloc0.io.allocaInputIO.valid          := true.B



  alloc1.io.allocaInputIO.bits.size      := 1.U
  alloc1.io.allocaInputIO.bits.numByte   := 4.U
  alloc1.io.allocaInputIO.bits.predicate := true.B
  alloc1.io.allocaInputIO.bits.valid     := true.B
  alloc1.io.allocaInputIO.valid          := true.B





  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */

  StackPointer.io.InData(0) <> alloc0.io.allocaReqIO

  alloc0.io.allocaRespIO <> StackPointer.io.OutData(0)

  StackPointer.io.InData(1) <> alloc1.io.allocaReqIO

  alloc1.io.allocaRespIO <> StackPointer.io.OutData(1)

  MemCtrl.io.WriteIn(0) <> st_3.io.memReq

  st_3.io.memResp <> MemCtrl.io.WriteOut(0)

  MemCtrl.io.WriteIn(1) <> st_5.io.memReq

  st_5.io.memResp <> MemCtrl.io.WriteOut(1)

  MemCtrl.io.ReadIn(0) <> ld_7.io.memReq

  ld_7.io.memResp <> MemCtrl.io.ReadOut(0)

  MemCtrl.io.ReadIn(1) <> ld_9.io.memReq

  ld_9.io.memResp <> MemCtrl.io.ReadOut(1)

  MemCtrl.io.WriteIn(2) <> st_12.io.memReq

  st_12.io.memResp <> MemCtrl.io.WriteOut(2)

  MemCtrl.io.ReadIn(2) <> ld_14.io.memReq

  ld_14.io.memResp <> MemCtrl.io.ReadOut(2)



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  arrayidx.io.idx1 <> const0.io.Out(0)

  arrayidx.io.idx2 <> const1.io.Out(0)

  arrayidx1.io.idx1 <> const2.io.Out(0)

  arrayidx1.io.idx2 <> const3.io.Out(0)

  arrayidx2.io.idx1 <> const4.io.Out(0)

  arrayidx2.io.idx2 <> const5.io.Out(0)

  arrayidx3.io.idx1 <> const6.io.Out(0)

  arrayidx3.io.idx2 <> const7.io.Out(0)

  arrayidx4.io.idx1 <> const8.io.Out(0)

  arrayidx4.io.idx2 <> const9.io.Out(0)

  arrayidx5.io.idx1 <> const10.io.Out(0)

  arrayidx5.io.idx2 <> const11.io.Out(0)

  arrayidx.io.baseAddress <> alloc0.io.Out(0)

  arrayidx1.io.baseAddress <> alloc0.io.Out(1)

  arrayidx2.io.baseAddress <> alloc0.io.Out(2)

  arrayidx3.io.baseAddress <> alloc0.io.Out(3)

  arrayidx4.io.baseAddress <> alloc1.io.Out(0)

  arrayidx5.io.baseAddress <> alloc1.io.Out(1)

  st_3.io.GepAddr <> arrayidx.io.Out.data(0)

  st_5.io.GepAddr <> arrayidx1.io.Out.data(0)

  ld_7.io.GepAddr <> arrayidx2.io.Out.data(0)

  add.io.LeftIO <> ld_7.io.Out.data(0)

  ld_9.io.GepAddr <> arrayidx3.io.Out.data(0)

  add.io.RightIO <> ld_9.io.Out.data(0)

  st_12.io.inData <> add.io.Out(0)

  st_12.io.GepAddr <> arrayidx4.io.Out.data(0)

  ld_14.io.GepAddr <> arrayidx5.io.Out.data(0)

  ret_15.io.In.data("field0") <> ld_14.io.Out.data(0)

  st_3.io.inData <> InputSplitter.io.Out.data("field0")(0)

  st_5.io.inData <> InputSplitter.io.Out.data("field1")(0)

  st_3.io.Out(0).ready := true.B

  st_5.io.Out(0).ready := true.B

  st_12.io.Out(0).ready := true.B


  ld_7.io.PredOp(0) <> st_3.io.SuccOp(0)  // manually added
  ld_9.io.PredOp(0) <> st_5.io.SuccOp(0)  // manually added
  ld_14.io.PredOp(0) <> st_12.io.SuccOp(0)  // manually added

  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_15.io.Out

}

import java.io.{File, FileWriter}
object test06Main extends App {
  val dir = new File("RTL/test06") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new test06DF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}
