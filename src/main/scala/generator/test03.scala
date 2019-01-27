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

abstract class test03DFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32,32,32))))
    val MemResp = Flipped(Valid(new MemResp))
    val MemReq = Decoupled(new MemReq)
    val out = Decoupled(new Call(List(32)))
  })
}

class test03DF(implicit p: Parameters) extends test03DFIO()(p) {


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

  val Loop_0 = Module(new LoopBlock(NumIns=List(2,1,1), NumOuts = 1, NumExits=1, ID = 0))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val entry = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 0))

  val for_cond = Module(new LoopHead(NumOuts = 5, NumPhi=2, BID = 1))

  val for_body = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 3, BID = 2))

  val for_inc = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 3, BID = 3))

  val for_end = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 4))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  br label %for.cond
  val br_0 = Module(new UBranchNode(ID = 0))

  //  %sum.0 = phi i32 [ %a, %entry ], [ %mul, %for.inc ]
  val sum_0 = Module(new PhiNode(NumInputs = 2, NumOuts = 2, ID = 1))

  //  %i.0 = phi i32 [ 0, %entry ], [ %inc, %for.inc ]
  val i_0 = Module(new PhiNode(NumInputs = 2, NumOuts = 2, ID = 2))

  //  %cmp = icmp slt i32 %i.0, %n
  val cmp = Module(new IcmpNode(NumOuts = 1, ID = 3, opCode = "ult")(sign=false))

  //  br i1 %cmp, label %for.body, label %for.end
  val br_4 = Module(new CBranchNode(ID = 4))

  //  %add = add i32 %sum.0, %a
  val add = Module(new ComputeNode(NumOuts = 1, ID = 5, opCode = "add")(sign=false))

  //  %mul = mul i32 %add, %b
  val mul = Module(new ComputeNode(NumOuts = 1, ID = 6, opCode = "mul")(sign=false))

  //  br label %for.inc
  val br_7 = Module(new UBranchNode(ID = 7))

  //  %inc = add nsw i32 %i.0, 1
  val inc = Module(new ComputeNode(NumOuts = 1, ID = 8, opCode = "add")(sign=false))

  //  br label %for.cond, !llvm.loop !7
  val br_9 = Module(new UBranchNode(NumOuts=2, ID = 9))

  //  ret i32 %sum.0
  val ret_10 = Module(new RetNode(retTypes=List(32), ID = 10))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i32 0
  val const0 = Module(new ConstNode(value = 0, NumOuts = 1, ID = 0))

  //i32 1
  val const1 = Module(new ConstNode(value = 1, NumOuts = 1, ID = 1))



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  entry.io.predicateIn <> InputSplitter.io.Out.enable

  for_cond.io.activate <> Loop_0.io.activate

  for_cond.io.loopBack <> br_9.io.Out(0)

  for_body.io.predicateIn <> br_4.io.Out(0)

  for_inc.io.predicateIn <> br_7.io.Out(0)

  for_end.io.predicateIn <> Loop_0.io.endEnable



  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */

  Loop_0.io.enable <> br_0.io.Out(0)

  Loop_0.io.latchEnable <> br_9.io.Out(1)

  Loop_0.io.loopExit(0) <> br_4.io.Out(1)



  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

  Loop_0.io.In(0) <> InputSplitter.io.Out.data("field0")(0)

  Loop_0.io.In(1) <> InputSplitter.io.Out.data("field2")(0)

  Loop_0.io.In(2) <> InputSplitter.io.Out.data("field1")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */

  sum_0.io.InData(0) <> Loop_0.io.liveIn.elements("field0")(0)

  add.io.RightIO <> Loop_0.io.liveIn.elements("field0")(1)

  cmp.io.RightIO <> Loop_0.io.liveIn.elements("field1")(0)

  mul.io.RightIO <> Loop_0.io.liveIn.elements("field2")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */

  Loop_0.io.liveOut(0) <> sum_0.io.Out(1)



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  br_0.io.enable <> entry.io.Out(0)


  const0.io.enable <> for_cond.io.Out(0)

  sum_0.io.enable <> for_cond.io.Out(1)

  i_0.io.enable <> for_cond.io.Out(2)

  cmp.io.enable <> for_cond.io.Out(3)

  br_4.io.enable <> for_cond.io.Out(4)


  add.io.enable <> for_body.io.Out(0)

  mul.io.enable <> for_body.io.Out(1)

  br_7.io.enable <> for_body.io.Out(2)


  const1.io.enable <> for_inc.io.Out(0)

  inc.io.enable <> for_inc.io.Out(1)

  br_9.io.enable <> for_inc.io.Out(2)


  ret_10.io.enable <> for_end.io.Out(0)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  sum_0.io.Mask <> for_cond.io.MaskBB(0)

  i_0.io.Mask <> for_cond.io.MaskBB(1)



  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  i_0.io.InData(0) <> const0.io.Out(0)

  inc.io.RightIO <> const1.io.Out(0)

  add.io.LeftIO <> sum_0.io.Out(0)

  cmp.io.LeftIO <> i_0.io.Out(0)

  inc.io.LeftIO <> i_0.io.Out(1)

  br_4.io.CmpIO <> cmp.io.Out(0)

  mul.io.LeftIO <> add.io.Out(0)

  sum_0.io.InData(1) <> mul.io.Out(0)

  i_0.io.InData(1) <> inc.io.Out(0)

  ret_10.io.In.data("field0") <> Loop_0.io.Out(0)



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_10.io.Out

}

import java.io.{File, FileWriter}
object test03Main extends App {
  val dir = new File("RTL/test03") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new test03DF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}
