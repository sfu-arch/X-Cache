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

abstract class test05DFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32,32))))
    val MemResp = Flipped(Valid(new MemResp))
    val MemReq = Decoupled(new MemReq)
    val out = Decoupled(new Call(List(32)))
  })
}

class test05DF(implicit p: Parameters) extends test05DFIO()(p) {


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  val MemCtrl = Module(new UnifiedController(ID=0, Size=32, NReads=3, NWrites=2)
		 (WControl=new WriteMemoryController(NumOps=2, BaseSize=2, NumEntries=2))
		 (RControl=new ReadMemoryController(NumOps=3, BaseSize=2, NumEntries=2))
		 (RWArbiter=new ReadWriteArbiter()))

  io.MemReq <> MemCtrl.io.MemReq
  MemCtrl.io.MemResp <> io.MemResp

  val InputSplitter = Module(new SplitCallNew(List(3,3)))
  InputSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val Loop_0 = Module(new LoopBlock(NumIns=List(1,2), NumOuts = 0, NumExits=1, ID = 0))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val entry = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 0))

  val for_cond = Module(new LoopHead(NumOuts = 4, NumPhi=1, BID = 1))

  val for_body = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 7, BID = 2))

  val for_inc = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 3, BID = 3))

  val for_end = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 12, BID = 4))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  br label %for.cond
  val br_0 = Module(new UBranchNode(ID = 0))

  //  %i.0 = phi i32 [ 0, %entry ], [ %inc, %for.inc ]
  val i_0 = Module(new PhiNode(NumInputs = 2, NumOuts = 4, ID = 1))

  //  %cmp = icmp ult i32 %i.0, %n
  val cmp = Module(new IcmpNode(NumOuts = 1, ID = 2, opCode = "ULT")(sign=false))

  //  br i1 %cmp, label %for.body, label %for.end
  val br_3 = Module(new CBranchNode(ID = 3))

  //  %arrayidx = getelementptr inbounds i32, i32* %a, i32 %i.0
  val arrayidx = Module(new GepOneNode(NumOuts=1, ID=4)(numByte1=4))

  //  %0 = load i32, i32* %arrayidx, align 4
  val ld_5 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=5, RouteID=0))

  //  %mul = mul i32 2, %0
  val mul = Module(new ComputeNode(NumOuts = 1, ID = 6, opCode = "mul")(sign=false))

  //  %arrayidx1 = getelementptr inbounds i32, i32* %a, i32 %i.0
  val arrayidx1 = Module(new GepOneNode(NumOuts=1, ID=7)(numByte1=4))

  //  store i32 %mul, i32* %arrayidx1, align 4
  val st_8 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=1, ID=8, RouteID=0))

  //  br label %for.inc
  val br_9 = Module(new UBranchNode(NumPredOps=1, ID = 9))

  //  %inc = add i32 %i.0, 1
  val inc = Module(new ComputeNode(NumOuts = 1, ID = 10, opCode = "add")(sign=false))

  //  br label %for.cond, !llvm.loop !7
  val br_11 = Module(new UBranchNode(NumOuts=2, ID = 11))

  //  %sub = sub i32 %n, 1
  val sub = Module(new ComputeNode(NumOuts = 1, ID = 12, opCode = "sub")(sign=false))

  //  %arrayidx2 = getelementptr inbounds i32, i32* %a, i32 %sub
  val arrayidx2 = Module(new GepOneNode(NumOuts=2, ID=13)(numByte1=4))

  //  %1 = load i32, i32* %arrayidx2, align 4
  val ld_14 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=14, RouteID=1))

  //  %inc3 = add i32 %1, 1
  val inc3 = Module(new ComputeNode(NumOuts = 1, ID = 15, opCode = "add")(sign=false))

  //  store i32 %inc3, i32* %arrayidx2, align 4
  val st_16 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=1, ID=16, RouteID=1))

  //  %sub4 = sub i32 %n, 1
  val sub4 = Module(new ComputeNode(NumOuts = 1, ID = 17, opCode = "sub")(sign=false))

  //  %arrayidx5 = getelementptr inbounds i32, i32* %a, i32 %sub4
  val arrayidx5 = Module(new GepOneNode(NumOuts=1, ID=18)(numByte1=4))

  //  %2 = load i32, i32* %arrayidx5, align 4
  val ld_19 = Module(new UnTypLoad(NumPredOps=1, NumSuccOps=0, NumOuts=1, ID=19, RouteID=2))

  //  ret i32 %2
  val ret_20 = Module(new RetNode(retTypes=List(32), ID = 20))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i32 0
  val const0 = Module(new ConstNode(value = 0, NumOuts = 1, ID = 0))

  //i32 2
  val const1 = Module(new ConstNode(value = 2, NumOuts = 1, ID = 1))

  //i32 1
  val const2 = Module(new ConstNode(value = 1, NumOuts = 1, ID = 2))

  //i32 1
  val const3 = Module(new ConstNode(value = 1, NumOuts = 1, ID = 3))

  //i32 1
  val const4 = Module(new ConstNode(value = 1, NumOuts = 1, ID = 4))

  //i32 1
  val const5 = Module(new ConstNode(value = 1, NumOuts = 1, ID = 5))



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  entry.io.predicateIn <> InputSplitter.io.Out.enable

  for_cond.io.activate <> Loop_0.io.activate

  for_cond.io.loopBack <> br_11.io.Out(0)

  for_body.io.predicateIn <> br_3.io.Out(0)

  for_inc.io.predicateIn <> br_9.io.Out(0)

  for_end.io.predicateIn <> Loop_0.io.endEnable



  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */

  Loop_0.io.enable <> br_0.io.Out(0)

  Loop_0.io.latchEnable <> br_11.io.Out(1)

  Loop_0.io.loopExit(0) <> br_3.io.Out(1)



  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */

  br_9.io.PredOp(0) <> st_8.io.SuccOp(0)



  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

  Loop_0.io.In(0) <> InputSplitter.io.Out.data.elements("field1")(0)

  Loop_0.io.In(1) <> InputSplitter.io.Out.data.elements("field0")(0)



  /* ================================================================== *
   *                   LOOP DATA OUTPUT DEPENDENCIES                    *
   * ================================================================== */

  cmp.io.RightIO <> Loop_0.io.liveIn.elements("field0")(0)

  arrayidx.io.baseAddress <> Loop_0.io.liveIn.elements("field1")(0)

  arrayidx1.io.baseAddress <> Loop_0.io.liveIn.elements("field1")(1)



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  br_0.io.enable <> entry.io.Out(0)


  const0.io.enable <> for_cond.io.Out(0)

  i_0.io.enable <> for_cond.io.Out(1)

  cmp.io.enable <> for_cond.io.Out(2)

  br_3.io.enable <> for_cond.io.Out(3)


  const1.io.enable <> for_body.io.Out(0)

  arrayidx.io.enable <> for_body.io.Out(1)

  ld_5.io.enable <> for_body.io.Out(2)

  mul.io.enable <> for_body.io.Out(3)

  arrayidx1.io.enable <> for_body.io.Out(4)

  st_8.io.enable <> for_body.io.Out(5)

  br_9.io.enable <> for_body.io.Out(6)


  const2.io.enable <> for_inc.io.Out(0)

  inc.io.enable <> for_inc.io.Out(1)

  br_11.io.enable <> for_inc.io.Out(2)


  const3.io.enable <> for_end.io.Out(0)

  const4.io.enable <> for_end.io.Out(1)

  const5.io.enable <> for_end.io.Out(2)

  sub.io.enable <> for_end.io.Out(3)

  arrayidx2.io.enable <> for_end.io.Out(4)

  ld_14.io.enable <> for_end.io.Out(5)

  inc3.io.enable <> for_end.io.Out(6)

  st_16.io.enable <> for_end.io.Out(7)

  sub4.io.enable <> for_end.io.Out(8)

  arrayidx5.io.enable <> for_end.io.Out(9)

  ld_19.io.enable <> for_end.io.Out(10)

  ret_20.io.enable <> for_end.io.Out(11)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  i_0.io.Mask <> for_cond.io.MaskBB(0)



  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */

  MemCtrl.io.ReadIn(0) <> ld_5.io.memReq

  ld_5.io.memResp <> MemCtrl.io.ReadOut(0)

  MemCtrl.io.WriteIn(0) <> st_8.io.memReq

  st_8.io.memResp <> MemCtrl.io.WriteOut(0)

  MemCtrl.io.ReadIn(1) <> ld_14.io.memReq

  ld_14.io.memResp <> MemCtrl.io.ReadOut(1)

  MemCtrl.io.WriteIn(1) <> st_16.io.memReq

  st_16.io.memResp <> MemCtrl.io.WriteOut(1)

  MemCtrl.io.ReadIn(2) <> ld_19.io.memReq

  ld_19.io.memResp <> MemCtrl.io.ReadOut(2)



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  i_0.io.InData(0) <> const0.io.Out(0)

  mul.io.LeftIO <> const1.io.Out(0)

  inc.io.RightIO <> const2.io.Out(0)

  sub.io.RightIO <> const3.io.Out(0)

  inc3.io.RightIO <> const4.io.Out(0)

  sub4.io.RightIO <> const5.io.Out(0)

  cmp.io.LeftIO <> i_0.io.Out(0)

  arrayidx.io.idx1 <> i_0.io.Out(1)

  arrayidx1.io.idx1 <> i_0.io.Out(2)

  inc.io.LeftIO <> i_0.io.Out(3)

  br_3.io.CmpIO <> cmp.io.Out(0)

  ld_5.io.GepAddr <> arrayidx.io.Out(0)

  mul.io.RightIO <> ld_5.io.Out(0)

  st_8.io.inData <> mul.io.Out(0)

  st_8.io.GepAddr <> arrayidx1.io.Out(0)

  i_0.io.InData(1) <> inc.io.Out(0)

  arrayidx2.io.idx1 <> sub.io.Out(0)

  ld_14.io.GepAddr <> arrayidx2.io.Out(0)

  st_16.io.GepAddr <> arrayidx2.io.Out(1)

  inc3.io.LeftIO <> ld_14.io.Out(0)

  st_16.io.inData <> inc3.io.Out(0)

  arrayidx5.io.idx1 <> sub4.io.Out(0)

  ld_19.io.GepAddr <> arrayidx5.io.Out(0)

  ret_20.io.In.elements("field0") <> ld_19.io.Out(0)

  arrayidx2.io.baseAddress <> InputSplitter.io.Out.data.elements("field0")(1)

  arrayidx5.io.baseAddress <> InputSplitter.io.Out.data.elements("field0")(2)

  sub.io.LeftIO <> InputSplitter.io.Out.data.elements("field1")(1)

  sub4.io.LeftIO <> InputSplitter.io.Out.data.elements("field1")(2)

  st_8.io.Out(0).ready := true.B

  st_16.io.Out(0).ready := true.B



  ld_19.io.PredOp(0) <> st_16.io.SuccOp(0)

  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_20.io.Out

}

import java.io.{File, FileWriter}
object test05Main extends App {
  val dir = new File("RTL/test05") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new test05DF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}


