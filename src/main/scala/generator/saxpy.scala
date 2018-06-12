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
//import org.scalatest.FlatSpec._
import org.scalatest.Matchers._
import regfile._
import stack._
import util._


  /* ================================================================== *
   *                   PRINTING PORTS DEFINITION                        *
   * ================================================================== */

abstract class saxpyDFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32,32,32,32))))
    val MemResp = Flipped(Valid(new MemResp))
    val MemReq = Decoupled(new MemReq)
    val out = Decoupled(new Call(List(32)))
  })
}

class saxpyDF(implicit p: Parameters) extends saxpyDFIO()(p) {


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  val MemCtrl = Module(new UnifiedController(ID=0, Size=32, NReads=2, NWrites=2)
		 (WControl=new WriteMemoryController(NumOps=2, BaseSize=2, NumEntries=2))
		 (RControl=new ReadMemoryController(NumOps=2, BaseSize=2, NumEntries=2))
		 (RWArbiter=new ReadWriteArbiter()))

  io.MemReq <> MemCtrl.io.MemReq
  MemCtrl.io.MemResp <> io.MemResp

  //val StackPointer = Module(new Stack(NumOps = 0))  // Manual fix

  val InputSplitter = Module(new SplitCallNew(List(1,1,1,1)))
  InputSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val Loop_0 = Module(new LoopBlock(NumIns=List(1,1,1,2), NumOuts = 0, NumExits=1, ID = 0))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val entry = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 0))

  val for_cond = Module(new LoopHead(NumOuts = 4, NumPhi=1, BID = 1))

  val for_body = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 9, BID = 2))

  val for_inc = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 3, BID = 3))

  val for_end = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 2, BID = 4))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  br label %for.cond
  val br_0 = Module(new UBranchNode(ID = 0))

  //  %i.0 = phi i32 [ 0, %entry ], [ %inc, %for.inc ]
  val i_0 = Module(new PhiNode(NumInputs = 2, NumOuts = 5, ID = 1))

  //  %cmp = icmp slt i32 %i.0, %n
  val cmp = Module(new IcmpNode(NumOuts = 1, ID = 2, opCode = "ult")(sign=false))

  //  br i1 %cmp, label %for.body, label %for.end
  val br_3 = Module(new CBranchNode(ID = 3))

  //  %arrayidx = getelementptr inbounds i32, i32* %x, i32 %i.0
  val arrayidx = Module(new GepArrayOneNode(NumOuts=1, ID=4)(numByte=4)(size=1)) // Manual fix

  //  %0 = load i32, i32* %arrayidx, align 4
  val ld_5 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=5, RouteID=0))

  //  %mul = mul nsw i32 %a, %0
  val mul = Module(new ComputeNode(NumOuts = 1, ID = 6, opCode = "mul")(sign=false))

  //  %arrayidx1 = getelementptr inbounds i32, i32* %y, i32 %i.0
  val arrayidx1 = Module(new GepArrayOneNode(NumOuts=1, ID=7)(numByte=4)(size=1)) // Manual fix

  //  %1 = load i32, i32* %arrayidx1, align 4
  val ld_8 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=8, RouteID=1))

  //  %add = add nsw i32 %mul, %1
  val add = Module(new ComputeNode(NumOuts = 1, ID = 9, opCode = "add")(sign=false))

  //  %arrayidx2 = getelementptr inbounds i32, i32* %y, i32 %i.0
  val arrayidx2 = Module(new GepArrayOneNode(NumOuts=1, ID=10)(numByte=4)(size=1)) // Manual fix

  //  store i32 %add, i32* %arrayidx2, align 4
  val st_11 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=1, ID=11, RouteID=0))

  //  br label %for.inc
  val br_12 = Module(new UBranchNode(NumPredOps=1, ID = 12))

  //  %inc = add nsw i32 %i.0, 1
  val inc = Module(new ComputeNode(NumOuts = 1, ID = 13, opCode = "add")(sign=false))

  //  br label %for.cond, !llvm.loop !6
  val br_14 = Module(new UBranchNode(NumOuts=2, ID = 14))

  //  ret i32 1
  val ret_15 = Module(new RetNode(retTypes=List(32), ID = 15))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i32 0
  val const0 = Module(new ConstNode(value = 0, NumOuts = 1, ID = 0))

  //i32 1
  val const1 = Module(new ConstNode(value = 1, NumOuts = 1, ID = 1))

  //i32 1
  val const2 = Module(new ConstNode(value = 1, NumOuts = 1, ID = 2))



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  entry.io.predicateIn <> InputSplitter.io.Out.enable

  for_cond.io.activate <> Loop_0.io.activate

  for_cond.io.loopBack <> br_14.io.Out(0)

  for_body.io.predicateIn <> br_3.io.Out(0)

  for_inc.io.predicateIn <> br_12.io.Out(0)

  for_end.io.predicateIn <> Loop_0.io.endEnable



  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */

  Loop_0.io.enable <> br_0.io.Out(0)

  Loop_0.io.latchEnable <> br_14.io.Out(1)

  Loop_0.io.loopExit(0) <> br_3.io.Out(1)



  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */

  br_12.io.PredOp(0) <> st_11.io.SuccOp(0)



  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

  Loop_0.io.In(0) <> InputSplitter.io.Out.data("field0")(0)

  Loop_0.io.In(1) <> InputSplitter.io.Out.data("field2")(0)

  Loop_0.io.In(2) <> InputSplitter.io.Out.data("field1")(0)

  Loop_0.io.In(3) <> InputSplitter.io.Out.data("field3")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */

  cmp.io.RightIO <> Loop_0.io.liveIn.data("field0")(0)

  arrayidx.io.baseAddress <> Loop_0.io.liveIn.data("field1")(0)

  mul.io.LeftIO <> Loop_0.io.liveIn.data("field2")(0)

  arrayidx1.io.baseAddress <> Loop_0.io.liveIn.data("field3")(0)

  arrayidx2.io.baseAddress <> Loop_0.io.liveIn.data("field3")(1)



  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  br_0.io.enable <> entry.io.Out(0)


  const0.io.enable <> for_cond.io.Out(0)

  i_0.io.enable <> for_cond.io.Out(1)

  cmp.io.enable <> for_cond.io.Out(2)

  br_3.io.enable <> for_cond.io.Out(3)


  arrayidx.io.enable <> for_body.io.Out(0)

  ld_5.io.enable <> for_body.io.Out(1)

  mul.io.enable <> for_body.io.Out(2)

  arrayidx1.io.enable <> for_body.io.Out(3)

  ld_8.io.enable <> for_body.io.Out(4)

  add.io.enable <> for_body.io.Out(5)

  arrayidx2.io.enable <> for_body.io.Out(6)

  st_11.io.enable <> for_body.io.Out(7)

  br_12.io.enable <> for_body.io.Out(8)


  const1.io.enable <> for_inc.io.Out(0)

  inc.io.enable <> for_inc.io.Out(1)

  br_14.io.enable <> for_inc.io.Out(2)


  const2.io.enable <> for_end.io.Out(0)

  ret_15.io.enable <> for_end.io.Out(1)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  i_0.io.Mask <> for_cond.io.MaskBB(0)



  /* ================================================================== *
   *                   PRINT ALLOCA OFFSET                              *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */

  MemCtrl.io.ReadIn(0) <> ld_5.io.memReq

  ld_5.io.memResp <> MemCtrl.io.ReadOut(0)

  MemCtrl.io.ReadIn(1) <> ld_8.io.memReq

  ld_8.io.memResp <> MemCtrl.io.ReadOut(1)

  MemCtrl.io.WriteIn(0) <> st_11.io.memReq

  st_11.io.memResp <> MemCtrl.io.WriteOut(0)



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  i_0.io.InData(0) <> const0.io.Out(0)

  inc.io.RightIO <> const1.io.Out(0)

  ret_15.io.In.data("field0") <> const2.io.Out(0)

  cmp.io.LeftIO <> i_0.io.Out(0)

  arrayidx.io.idx1 <> i_0.io.Out(1)

  arrayidx1.io.idx1 <> i_0.io.Out(2)

  arrayidx2.io.idx1 <> i_0.io.Out(3)

  inc.io.LeftIO <> i_0.io.Out(4)

  br_3.io.CmpIO <> cmp.io.Out(0)

  ld_5.io.GepAddr <> arrayidx.io.Out.data(0)

  mul.io.RightIO <> ld_5.io.Out.data(0)

  add.io.LeftIO <> mul.io.Out(0)

  ld_8.io.GepAddr <> arrayidx1.io.Out.data(0)

  add.io.RightIO <> ld_8.io.Out.data(0)

  st_11.io.inData <> add.io.Out(0)

  st_11.io.GepAddr <> arrayidx2.io.Out.data(0)

  i_0.io.InData(1) <> inc.io.Out(0)

  st_11.io.Out(0).ready := true.B



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_15.io.Out

}

import java.io.{File, FileWriter}
object saxpyMain extends App {
  val dir = new File("RTL/saxpy") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new saxpyDF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}
