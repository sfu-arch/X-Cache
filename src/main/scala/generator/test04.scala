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

abstract class test04DFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32,32))))
    val MemResp = Flipped(Valid(new MemResp))
    val MemReq = Decoupled(new MemReq)
    val out = Decoupled(new Call(List(32)))
  })
}

class test04DF(implicit p: Parameters) extends test04DFIO()(p) {


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  val MemCtrl = Module(new UnifiedController(ID=0, Size=32, NReads=2, NWrites=2)
		 (WControl=new WriteMemoryController(NumOps=2, BaseSize=2, NumEntries=2))
		 (RControl=new ReadMemoryController(NumOps=2, BaseSize=2, NumEntries=2))
		 (RWArbiter=new ReadWriteArbiter()))

  io.MemReq <> MemCtrl.io.MemReq
  MemCtrl.io.MemResp <> io.MemResp

  val InputSplitter = Module(new SplitCallNew(List(1,1)))
  InputSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val Loop_0 = Module(new LoopBlock(NumIns=List(1,1), NumOuts = 0, NumExits=1, ID = 0))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val entry = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 0))

  val Minim_Loop = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 1))

  val while_cond = Module(new LoopHead(NumOuts = 4, NumPhi=2, BID = 2))

  val while_body = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 2, BID = 3))

  val if_then = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 2, BID = 4))

  val if_else = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 2, BID = 5))

  val if_end = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 3, NumPhi=2, BID = 6))

  val while_end = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 7))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  br label %Minim_Loop
  val br_0 = Module(new UBranchNode(ID = 0))

  //  br label %while.cond
  val br_1 = Module(new UBranchNode(ID = 1))

  //  %b.addr.0 = phi i32 [ %b, %Minim_Loop ], [ %b.addr.1, %if.end ]
  val b_addr_0 = Module(new PhiNode(NumInputs = 2, NumOuts = 5, ID = 2))

  //  %a.addr.0 = phi i32 [ %a, %Minim_Loop ], [ %a.addr.1, %if.end ]
  val a_addr_0 = Module(new PhiNode(NumInputs = 2, NumOuts = 5, ID = 3))

  //  %cmp = icmp ne i32 %a.addr.0, %b.addr.0
  val cmp = Module(new IcmpNode(NumOuts = 1, ID = 4, opCode = "ne")(sign=false))

  //  br i1 %cmp, label %while.body, label %while.end
  val br_5 = Module(new CBranchNode(ID = 5))

  //  %cmp1 = icmp sgt i32 %a.addr.0, %b.addr.0
  val cmp1 = Module(new IcmpNode(NumOuts = 1, ID = 6, opCode = "ugt")(sign=false))

  //  br i1 %cmp1, label %if.then, label %if.else
  val br_7 = Module(new CBranchNode(ID = 7))

  //  %sub = sub nsw i32 %a.addr.0, %b.addr.0
  val sub = Module(new ComputeNode(NumOuts = 1, ID = 8, opCode = "sub")(sign=false))

  //  br label %if.end
  val br_9 = Module(new UBranchNode(ID = 9))

  //  %sub2 = sub nsw i32 %b.addr.0, %a.addr.0
  val sub2 = Module(new ComputeNode(NumOuts = 1, ID = 10, opCode = "sub")(sign=false))

  //  br label %if.end
  val br_11 = Module(new UBranchNode(ID = 11))

  //  %b.addr.1 = phi i32 [ %b.addr.0, %if.then ], [ %sub2, %if.else ]
  val b_addr_1 = Module(new PhiNode(NumInputs = 2, NumOuts = 1, ID = 12))

  //  %a.addr.1 = phi i32 [ %sub, %if.then ], [ %a.addr.0, %if.else ]
  val a_addr_1 = Module(new PhiNode(NumInputs = 2, NumOuts = 1, ID = 13))

  //  br label %while.cond, !llvm.loop !7
  val br_14 = Module(new UBranchNode(NumOuts=2, ID = 14))

  //  ret void
  val ret_15 = Module(new RetNode(retTypes=List(32), ID = 15))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  entry.io.predicateIn <> InputSplitter.io.Out.enable

  Minim_Loop.io.predicateIn <> br_0.io.Out(0)

  while_cond.io.activate <> Loop_0.io.activate

  while_cond.io.loopBack <> br_14.io.Out(0)

  while_body.io.predicateIn <> br_5.io.Out(0)

  if_then.io.predicateIn <> br_7.io.Out(0)

  if_else.io.predicateIn <> br_7.io.Out(1)

  if_end.io.predicateIn(0) <> br_9.io.Out(0)

  if_end.io.predicateIn(1) <> br_11.io.Out(0)

  while_end.io.predicateIn <> Loop_0.io.endEnable



  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */

  Loop_0.io.enable <> br_1.io.Out(0)

  Loop_0.io.latchEnable <> br_14.io.Out(1)

  Loop_0.io.loopExit(0) <> br_5.io.Out(1)



  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

  Loop_0.io.In(0) <> InputSplitter.io.Out.data("field1")(0)

  Loop_0.io.In(1) <> InputSplitter.io.Out.data("field0")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */

  b_addr_0.io.InData(0) <> Loop_0.io.liveIn.data("field0")(0)

  a_addr_0.io.InData(0) <> Loop_0.io.liveIn.data("field1")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  br_0.io.enable <> entry.io.Out(0)


  br_1.io.enable <> Minim_Loop.io.Out(0)


  b_addr_0.io.enable <> while_cond.io.Out(0)

  a_addr_0.io.enable <> while_cond.io.Out(1)

  cmp.io.enable <> while_cond.io.Out(2)

  br_5.io.enable <> while_cond.io.Out(3)


  cmp1.io.enable <> while_body.io.Out(0)

  br_7.io.enable <> while_body.io.Out(1)


  sub.io.enable <> if_then.io.Out(0)

  br_9.io.enable <> if_then.io.Out(1)


  sub2.io.enable <> if_else.io.Out(0)

  br_11.io.enable <> if_else.io.Out(1)


  b_addr_1.io.enable <> if_end.io.Out(0)

  a_addr_1.io.enable <> if_end.io.Out(1)

  br_14.io.enable <> if_end.io.Out(2)


  ret_15.io.enable <> while_end.io.Out(0)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  b_addr_0.io.Mask <> while_cond.io.MaskBB(0)

  a_addr_0.io.Mask <> while_cond.io.MaskBB(1)

  b_addr_1.io.Mask <> if_end.io.MaskBB(0)

  a_addr_1.io.Mask <> if_end.io.MaskBB(1)



  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  cmp.io.RightIO <> b_addr_0.io.Out(0)

  cmp1.io.RightIO <> b_addr_0.io.Out(1)

  sub.io.RightIO <> b_addr_0.io.Out(2)

  sub2.io.LeftIO <> b_addr_0.io.Out(3)

  b_addr_1.io.InData(0) <> b_addr_0.io.Out(4)

  cmp.io.LeftIO <> a_addr_0.io.Out(0)

  cmp1.io.LeftIO <> a_addr_0.io.Out(1)

  sub.io.LeftIO <> a_addr_0.io.Out(2)

  sub2.io.RightIO <> a_addr_0.io.Out(3)

  a_addr_1.io.InData(1) <> a_addr_0.io.Out(4)

  br_5.io.CmpIO <> cmp.io.Out(0)

  br_7.io.CmpIO <> cmp1.io.Out(0)

  a_addr_1.io.InData(0) <> sub.io.Out(0)

  b_addr_1.io.InData(1) <> sub2.io.Out(0)

  b_addr_0.io.InData(1) <> b_addr_1.io.Out(0)

  a_addr_0.io.InData(1) <> a_addr_1.io.Out(0)



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_15.io.Out

}

import java.io.{File, FileWriter}
object test04Main extends App {
  val dir = new File("RTL/test04") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new test04DF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}
