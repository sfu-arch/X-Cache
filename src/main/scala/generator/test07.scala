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

abstract class test07DFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(1))))
    val MemResp = Flipped(Valid(new MemResp))
    val MemReq = Decoupled(new MemReq)
    val out = Decoupled(new Call(List(32)))
  })
}

class test07DF(implicit p: Parameters) extends test07DFIO()(p) {


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  val MemCtrl = Module(new UnifiedController(ID=0, Size=32, NReads=2, NWrites=2)
		 (WControl=new WriteMemoryController(NumOps=2, BaseSize=2, NumEntries=2))
		 (RControl=new ReadMemoryController(NumOps=2, BaseSize=2, NumEntries=2))
		 (RWArbiter=new ReadWriteArbiter()))

  io.MemReq <> MemCtrl.io.MemReq
  MemCtrl.io.MemResp <> io.MemResp

  val StackPointer = Module(new Stack(NumOps = 1))

  val InputSplitter = Module(new SplitCallNew(List(1)))
  InputSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val Loop_0 = Module(new LoopBlock(NumIns=List(1), NumOuts = 1, NumExits=1, ID = 0))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val entry = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 0))

  val for_cond = Module(new LoopHead(NumOuts = 7, NumPhi=2, BID = 1))

  val for_body = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 3, BID = 2))

  val for_inc = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 3, BID = 3))

  val for_end = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 4))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  br label %for.cond
  val br_0 = Module(new UBranchNode(ID = 0))

  //  %i.0 = phi i32 [ 0, %entry ], [ %inc, %for.inc ]
  val i_0 = Module(new PhiNode(NumInputs = 2, NumOuts = 2, ID = 1))

  //  %j.0 = phi i32 [ 100, %entry ], [ %sub, %for.inc ]
  val j_0 = Module(new PhiNode(NumInputs = 2, NumOuts = 2, ID = 2))

  //  %cmp = icmp slt i32 %i.0, 5
  val cmp = Module(new IcmpNode(NumOuts = 1, ID = 3, opCode = "ult")(sign=false))

  //  br i1 %cmp, label %for.body, label %for.end
  val br_4 = Module(new CBranchNode(ID = 4))

  //  %sub = sub i32 %j.0, 1
  val sub = Module(new ComputeNode(NumOuts = 1, ID = 5, opCode = "sub")(sign=false))

  //  br label %for.inc
  val br_6 = Module(new UBranchNode(ID = 6))

  //  %inc = add nsw i32 %i.0, 1
  val inc = Module(new ComputeNode(NumOuts = 1, ID = 7, opCode = "add")(sign=false))

  //  br label %for.cond, !llvm.loop !7
  val br_8 = Module(new UBranchNode(NumOuts=2, ID = 8))

  //  ret i32 %j.0
  val ret_9 = Module(new RetNode(retTypes=List(32), ID = 9))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i32 0
  val const0 = Module(new ConstNode(value = 0, NumOuts = 1, ID = 0))

  //i32 100
  val const1 = Module(new ConstNode(value = 100, NumOuts = 1, ID = 1))

  //i32 5
  val const2 = Module(new ConstNode(value = 5, NumOuts = 1, ID = 2))

  //i32 1
  val const3 = Module(new ConstNode(value = 1, NumOuts = 1, ID = 3))

  //i32 1
  val const4 = Module(new ConstNode(value = 1, NumOuts = 1, ID = 4))



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  entry.io.predicateIn <> InputSplitter.io.Out.enable

  for_cond.io.activate <> Loop_0.io.activate

  for_cond.io.loopBack <> br_8.io.Out(0)

  for_body.io.predicateIn <> br_4.io.Out(0)

  for_inc.io.predicateIn <> br_6.io.Out(0)

  for_end.io.predicateIn <> Loop_0.io.endEnable



  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */

  Loop_0.io.enable <> br_0.io.Out(0)

  Loop_0.io.latchEnable <> br_8.io.Out(1)

  Loop_0.io.loopExit(0) <> br_4.io.Out(1)



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

  Loop_0.io.liveOut(0) <> j_0.io.Out(1)



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  br_0.io.enable <> entry.io.Out(0)


  const0.io.enable <> for_cond.io.Out(0)

  const1.io.enable <> for_cond.io.Out(1)

  const2.io.enable <> for_cond.io.Out(2)

  i_0.io.enable <> for_cond.io.Out(3)

  j_0.io.enable <> for_cond.io.Out(4)

  cmp.io.enable <> for_cond.io.Out(5)

  br_4.io.enable <> for_cond.io.Out(6)


  const3.io.enable <> for_body.io.Out(0)

  sub.io.enable <> for_body.io.Out(1)

  br_6.io.enable <> for_body.io.Out(2)


  const4.io.enable <> for_inc.io.Out(0)

  inc.io.enable <> for_inc.io.Out(1)

  br_8.io.enable <> for_inc.io.Out(2)


  ret_9.io.enable <> for_end.io.Out(0)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  i_0.io.Mask <> for_cond.io.MaskBB(0)

  j_0.io.Mask <> for_cond.io.MaskBB(1)



  /* ================================================================== *
   *                   PRINT ALLOCA OFFSET                              *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  i_0.io.InData(0) <> const0.io.Out(0)

  j_0.io.InData(0) <> const1.io.Out(0)

  cmp.io.RightIO <> const2.io.Out(0)

  sub.io.RightIO <> const3.io.Out(0)

  inc.io.RightIO <> const4.io.Out(0)

  cmp.io.LeftIO <> i_0.io.Out(0)

  inc.io.LeftIO <> i_0.io.Out(1)

  sub.io.LeftIO <> j_0.io.Out(0)

  br_4.io.CmpIO <> cmp.io.Out(0)

  j_0.io.InData(1) <> sub.io.Out(0)

  i_0.io.InData(1) <> inc.io.Out(0)

  ret_9.io.In.data("field0") <> Loop_0.io.Out(0)



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_9.io.Out

}

import java.io.{File, FileWriter}
object test07Main extends App {
  val dir = new File("RTL/test07") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new test07DF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}
