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

abstract class test11DFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32,32,32))))
    val call_8_out = Decoupled(new Call(List(32,32)))
    val call_8_in = Flipped(Decoupled(new Call(List(32))))
    val MemResp = Flipped(Valid(new MemResp))
    val MemReq = Decoupled(new MemReq)
    val out = Decoupled(new Call(List(32)))
  })
}

class test11DF(implicit p: Parameters) extends test11DFIO()(p) {


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  val MemCtrl = Module(new UnifiedController(ID=0, Size=32, NReads=2, NWrites=2)
		 (WControl=new WriteMemoryController(NumOps=2, BaseSize=2, NumEntries=2))
		 (RControl=new ReadMemoryController(NumOps=2, BaseSize=2, NumEntries=2))
		 (RWArbiter=new ReadWriteArbiter()))

  io.MemReq <> MemCtrl.io.MemReq
  MemCtrl.io.MemResp <> io.MemResp

  val InputSplitter = Module(new SplitCallNew(List(1,1,1)))
  InputSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val Loop_0 = Module(new LoopBlock(NumIns=List(1,1,1), NumOuts = 0, NumExits=1, ID = 0))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val entry = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 0))

  val for_cond = Module(new LoopHead(NumOuts = 5, NumPhi=1, BID = 1))

  val for_body = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 8, BID = 2))

  val for_inc = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 3, BID = 3))

  val for_end = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 2, BID = 4))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  br label %for.cond
  val br_0 = Module(new UBranchNode(ID = 0))

  //  %i.0 = phi i32 [ 0, %entry ], [ %inc, %for.inc ]
  val i_0 = Module(new PhiNode(NumInputs = 2, NumOuts = 5, ID = 1))

  //  %cmp = icmp ult i32 %i.0, 5
  val cmp = Module(new IcmpNode(NumOuts = 1, ID = 2, opCode = "ult")(sign=false))

  //  br i1 %cmp, label %for.body, label %for.end
  val br_3 = Module(new CBranchNode(ID = 3))

  //  %arrayidx = getelementptr inbounds i32, i32* %a, i32 %i.0
  val arrayidx = Module(new GepArrayOneNode(NumOuts=1, ID=4)(numByte=0)(size=0))

  //  %0 = load i32, i32* %arrayidx, align 4
  val ld_5 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=5, RouteID=0))

  //  %arrayidx1 = getelementptr inbounds i32, i32* %b, i32 %i.0
  val arrayidx1 = Module(new GepArrayOneNode(NumOuts=1, ID=6)(numByte=0)(size=0))

  //  %1 = load i32, i32* %arrayidx1, align 4
  val ld_7 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=7, RouteID=1))

  //  %call = call i32 @test11_add(i32 %0, i32 %1)
  val call_8_out = Module(new CallOutNode(ID = 8, NumSuccOps = 0, argTypes = List(32,32)))

  val call_8_in = Module(new CallInNode(ID = 8, argTypes = List(32)))

  //  %arrayidx2 = getelementptr inbounds i32, i32* %c, i32 %i.0
  val arrayidx2 = Module(new GepArrayOneNode(NumOuts=1, ID=9)(numByte=0)(size=0))

  //  store i32 %call, i32* %arrayidx2, align 4
  val st_10 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=1, ID=10, RouteID=0))

  //  br label %for.inc
  val br_11 = Module(new UBranchNode(NumPredOps=1, ID = 11))

  //  %inc = add i32 %i.0, 1
  val inc = Module(new ComputeNode(NumOuts = 1, ID = 12, opCode = "add")(sign=false))

  //  br label %for.cond, !llvm.loop !7
  val br_13 = Module(new UBranchNode(NumOuts=2, ID = 13))

  //  ret i32 1
  val ret_14 = Module(new RetNode(retTypes=List(32), ID = 14))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i32 0
  val const0 = Module(new ConstNode(value = 0, NumOuts = 1, ID = 0))

  //i32 5
  val const1 = Module(new ConstNode(value = 5, NumOuts = 1, ID = 1))

  //i32 1
  val const2 = Module(new ConstNode(value = 1, NumOuts = 1, ID = 2))

  //i32 1
  val const3 = Module(new ConstNode(value = 1, NumOuts = 1, ID = 3))



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  entry.io.predicateIn <> InputSplitter.io.Out.enable

  for_cond.io.activate <> Loop_0.io.activate

  for_cond.io.loopBack <> br_13.io.Out(0)

  for_body.io.predicateIn <> br_3.io.Out(0)

  for_inc.io.predicateIn <> br_11.io.Out(0)

  for_end.io.predicateIn <> Loop_0.io.endEnable



  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */

  Loop_0.io.enable <> br_0.io.Out(0)

  Loop_0.io.latchEnable <> br_13.io.Out(1)

  Loop_0.io.loopExit(0) <> br_3.io.Out(1)



  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */

  br_11.io.PredOp(0) <> st_10.io.SuccOp(0)



  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

  Loop_0.io.In(0) <> InputSplitter.io.Out.data.elements("field0")(0)

  Loop_0.io.In(1) <> InputSplitter.io.Out.data.elements("field1")(0)

  Loop_0.io.In(2) <> InputSplitter.io.Out.data.elements("field2")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */

  arrayidx.io.baseAddress <> Loop_0.io.liveIn.elements("field0")(0)

  arrayidx1.io.baseAddress <> Loop_0.io.liveIn.elements("field1")(0)

  arrayidx2.io.baseAddress <> Loop_0.io.liveIn.elements("field2")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  br_0.io.enable <> entry.io.Out(0)


  const0.io.enable <> for_cond.io.Out(0)

  const1.io.enable <> for_cond.io.Out(1)

  i_0.io.enable <> for_cond.io.Out(2)

  cmp.io.enable <> for_cond.io.Out(3)

  br_3.io.enable <> for_cond.io.Out(4)


  arrayidx.io.enable <> for_body.io.Out(0)

  ld_5.io.enable <> for_body.io.Out(1)

  arrayidx1.io.enable <> for_body.io.Out(2)

  ld_7.io.enable <> for_body.io.Out(3)

  call_8_in.io.enable.enq(ControlBundle.active())

  call_8_out.io.enable <> for_body.io.Out(4)

  arrayidx2.io.enable <> for_body.io.Out(5)

  st_10.io.enable <> for_body.io.Out(6)

  br_11.io.enable <> for_body.io.Out(7)


  const2.io.enable <> for_inc.io.Out(0)

  inc.io.enable <> for_inc.io.Out(1)

  br_13.io.enable <> for_inc.io.Out(2)


  const3.io.enable <> for_end.io.Out(0)

  ret_14.io.enable <> for_end.io.Out(1)




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

  MemCtrl.io.ReadIn(1) <> ld_7.io.memReq

  ld_7.io.memResp <> MemCtrl.io.ReadOut(1)

  MemCtrl.io.WriteIn(0) <> st_10.io.memReq

  st_10.io.memResp <> MemCtrl.io.WriteOut(0)



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  i_0.io.InData(0) <> const0.io.Out(0)

  cmp.io.RightIO <> const1.io.Out(0)

  inc.io.RightIO <> const2.io.Out(0)

  ret_14.io.In.elements("field0") <> const3.io.Out(0)

  cmp.io.LeftIO <> i_0.io.Out(0)

  arrayidx.io.idx1 <> i_0.io.Out(1)

  arrayidx1.io.idx1 <> i_0.io.Out(2)

  arrayidx2.io.idx1 <> i_0.io.Out(3)

  inc.io.LeftIO <> i_0.io.Out(4)

  br_3.io.CmpIO <> cmp.io.Out(0)

  ld_5.io.GepAddr <> arrayidx.io.Out(0)

  call_8_out.io.In("field0") <> ld_5.io.Out(0)

  ld_7.io.GepAddr <> arrayidx1.io.Out(0)

  call_8_out.io.In("field1") <> ld_7.io.Out(0)

  st_10.io.inData <> call_8_in.io.Out.data("field0")

  st_10.io.GepAddr <> arrayidx2.io.Out(0)

  i_0.io.InData(1) <> inc.io.Out(0)

  st_10.io.Out(0).ready := true.B



  /* ================================================================== *
   *                   PRINTING CALLIN AND CALLOUT INTERFACE            *
   * ================================================================== */

  call_8_in.io.In <> io.call_8_in

  io.call_8_out <> call_8_out.io.Out(0)

  call_8_in.io.Out.enable.ready := true.B

  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_14.io.Out

}

import java.io.{File, FileWriter}
object test11Main extends App {
  val dir = new File("RTL/test11") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new test11DF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}
