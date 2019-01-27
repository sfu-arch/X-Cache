package dataflow

import FPU._
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

abstract class test18DFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32, 32))))
    val MemResp = Flipped(Valid(new MemResp))
    val MemReq = Decoupled(new MemReq)
    val out = Decoupled(new Call(List(32)))
  })
}

class test18DF(implicit p: Parameters) extends test18DFIO()(p) {


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

  val bb_entry0 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 0))

  val bb_for_cond1 = Module(new LoopHead(NumOuts = 4, NumPhi=1, BID = 1))

  val bb_for_inc2 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 9, BID = 2))

  val bb_for_end3 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 12, BID = 3))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  br label %for.cond
  val br_0 = Module(new UBranchNode(ID = 0))

  //  %k.0 = phi i32 [ 0, %entry ], [ %inc, %for.inc ]
  val phi_k_01 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 4, ID = 1))

  //  %cmp = icmp ult i32 %k.0, %n
  val icmp_cmp2 = Module(new IcmpNode(NumOuts = 1, ID = 2, opCode = "ult")(sign=false))

  //  br i1 %cmp, label %for.inc, label %for.end
  val br_3 = Module(new CBranchFastNodeVariable(NumTrue = 1, NumFalse = 1, ID = 3))

  //  %arrayidx = getelementptr inbounds i32, i32* %a, i32 %k.0
  val Gep_arrayidx4 = Module(new GepNode(NumIns = 1, NumOuts=1, ID=4)(ElementSize = 4, ArraySize = List()))

  //  %0 = load i32, i32* %arrayidx, align 4
  val ld_5 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=5, RouteID=0))

  //  %mul = mul i32 2, %0
  val binaryOp_mul6 = Module(new ComputeNode(NumOuts = 1, ID = 6, opCode = "mul")(sign=false))

  //  %arrayidx1 = getelementptr inbounds i32, i32* %a, i32 %k.0
  val Gep_arrayidx17 = Module(new GepNode(NumIns = 1, NumOuts=1, ID=7)(ElementSize = 4, ArraySize = List()))

  //  store i32 %mul, i32* %arrayidx1, align 4
  val st_8 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=1, ID=8, RouteID=0))

  //  %inc = add i32 %k.0, 1
  val binaryOp_inc9 = Module(new ComputeNode(NumOuts = 1, ID = 9, opCode = "add")(sign=false))

  //  br label %for.cond
  val br_10 = Module(new UBranchNode(NumPredOps=1, NumOuts=2, ID = 10))

  //  %sub = sub i32 %n, 1
  val binaryOp_sub11 = Module(new ComputeNode(NumOuts = 1, ID = 11, opCode = "sub")(sign=false))

  //  %arrayidx2 = getelementptr inbounds i32, i32* %a, i32 %sub
  val Gep_arrayidx212 = Module(new GepNode(NumIns = 1, NumOuts=2, ID=12)(ElementSize = 4, ArraySize = List()))

  //  %1 = load i32, i32* %arrayidx2, align 4
  val ld_13 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=13, RouteID=1))

  //  %inc3 = add i32 %1, 1
  val binaryOp_inc314 = Module(new ComputeNode(NumOuts = 1, ID = 14, opCode = "add")(sign=false))

  //  store i32 %inc3, i32* %arrayidx2, align 4
  val st_15 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, ID=15, RouteID=1))

  //  %sub4 = sub i32 %n, 1
  val binaryOp_sub416 = Module(new ComputeNode(NumOuts = 1, ID = 16, opCode = "sub")(sign=false))

  //  %arrayidx5 = getelementptr inbounds i32, i32* %a, i32 %sub4
  val Gep_arrayidx517 = Module(new GepNode(NumIns = 1, NumOuts=1, ID=17)(ElementSize = 4, ArraySize = List()))

  //  %2 = load i32, i32* %arrayidx5, align 4
  val ld_18 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=18, RouteID=2))

  //  ret i32 %2
  val ret_19 = Module(new RetNode2(retTypes=List(32), ID = 19))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i32 0
  val const0 = Module(new ConstFastNode(value = 0, ID = 0))

  //i32 2
  val const1 = Module(new ConstFastNode(value = 2, ID = 1))

  //i32 1
  val const2 = Module(new ConstFastNode(value = 1, ID = 2))

  //i32 1
  val const3 = Module(new ConstFastNode(value = 1, ID = 3))

  //i32 1
  val const4 = Module(new ConstFastNode(value = 1, ID = 4))

  //i32 1
  val const5 = Module(new ConstFastNode(value = 1, ID = 5))



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_entry0.io.predicateIn <> InputSplitter.io.Out.enable

  bb_for_cond1.io.activate <> Loop_0.io.activate

  bb_for_cond1.io.loopBack <> br_10.io.Out(0)

  bb_for_inc2.io.predicateIn <> br_3.io.TrueOutput(0)

  bb_for_end3.io.predicateIn <> Loop_0.io.endEnable



  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */

  Loop_0.io.enable <> br_0.io.Out(0)

  Loop_0.io.latchEnable <> br_10.io.Out(1)

  Loop_0.io.loopExit(0) <> br_3.io.FalseOutput(0)



  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */

  br_10.io.PredOp(0) <> st_8.io.SuccOp(0)



  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

  Loop_0.io.In(0) <> InputSplitter.io.Out.data("field1")(0)

  Loop_0.io.In(1) <> InputSplitter.io.Out.data("field0")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */

  icmp_cmp2.io.RightIO <> Loop_0.io.liveIn.elements("field0")(0)

  Gep_arrayidx4.io.baseAddress <> Loop_0.io.liveIn.elements("field1")(0)

  Gep_arrayidx17.io.baseAddress <> Loop_0.io.liveIn.elements("field1")(1)



  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  br_0.io.enable <> bb_entry0.io.Out(0)


  const0.io.enable <> bb_for_cond1.io.Out(0)

  phi_k_01.io.enable <> bb_for_cond1.io.Out(1)

  icmp_cmp2.io.enable <> bb_for_cond1.io.Out(2)

  br_3.io.enable <> bb_for_cond1.io.Out(3)


  const1.io.enable <> bb_for_inc2.io.Out(0)

  const2.io.enable <> bb_for_inc2.io.Out(1)

  Gep_arrayidx4.io.enable <> bb_for_inc2.io.Out(2)

  ld_5.io.enable <> bb_for_inc2.io.Out(3)

  binaryOp_mul6.io.enable <> bb_for_inc2.io.Out(4)

  Gep_arrayidx17.io.enable <> bb_for_inc2.io.Out(5)

  st_8.io.enable <> bb_for_inc2.io.Out(6)

  binaryOp_inc9.io.enable <> bb_for_inc2.io.Out(7)

  br_10.io.enable <> bb_for_inc2.io.Out(8)


  const3.io.enable <> bb_for_end3.io.Out(0)

  const4.io.enable <> bb_for_end3.io.Out(1)

  const5.io.enable <> bb_for_end3.io.Out(2)

  binaryOp_sub11.io.enable <> bb_for_end3.io.Out(3)

  Gep_arrayidx212.io.enable <> bb_for_end3.io.Out(4)

  ld_13.io.enable <> bb_for_end3.io.Out(5)

  binaryOp_inc314.io.enable <> bb_for_end3.io.Out(6)

  st_15.io.enable <> bb_for_end3.io.Out(7)

  binaryOp_sub416.io.enable <> bb_for_end3.io.Out(8)

  Gep_arrayidx517.io.enable <> bb_for_end3.io.Out(9)

  ld_18.io.enable <> bb_for_end3.io.Out(10)

  ret_19.io.In.enable <> bb_for_end3.io.Out(11)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  phi_k_01.io.Mask <> bb_for_cond1.io.MaskBB(0)



  /* ================================================================== *
   *                   PRINT ALLOCA OFFSET                              *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */

  MemCtrl.io.ReadIn(0) <> ld_5.io.memReq

  ld_5.io.memResp <> MemCtrl.io.ReadOut(0)

  MemCtrl.io.WriteIn(0) <> st_8.io.memReq

  st_8.io.memResp <> MemCtrl.io.WriteOut(0)

  MemCtrl.io.ReadIn(1) <> ld_13.io.memReq

  ld_13.io.memResp <> MemCtrl.io.ReadOut(1)

  MemCtrl.io.WriteIn(1) <> st_15.io.memReq

  st_15.io.memResp <> MemCtrl.io.WriteOut(1)

  MemCtrl.io.ReadIn(2) <> ld_18.io.memReq

  ld_18.io.memResp <> MemCtrl.io.ReadOut(2)



  /* ================================================================== *
   *                   PRINT SHARED CONNECTIONS                         *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  phi_k_01.io.InData(0) <> const0.io.Out

  binaryOp_mul6.io.LeftIO <> const1.io.Out

  binaryOp_inc9.io.RightIO <> const2.io.Out

  binaryOp_sub11.io.RightIO <> const3.io.Out

  binaryOp_inc314.io.RightIO <> const4.io.Out

  binaryOp_sub416.io.RightIO <> const5.io.Out

  icmp_cmp2.io.LeftIO <> phi_k_01.io.Out(0)

  Gep_arrayidx4.io.idx(0) <> phi_k_01.io.Out(1)

  Gep_arrayidx17.io.idx(0) <> phi_k_01.io.Out(2)

  binaryOp_inc9.io.LeftIO <> phi_k_01.io.Out(3)

  br_3.io.CmpIO <> icmp_cmp2.io.Out(0)

  ld_5.io.GepAddr <> Gep_arrayidx4.io.Out(0)

  binaryOp_mul6.io.RightIO <> ld_5.io.Out(0)

  st_8.io.inData <> binaryOp_mul6.io.Out(0)

  st_8.io.GepAddr <> Gep_arrayidx17.io.Out(0)

  phi_k_01.io.InData(1) <> binaryOp_inc9.io.Out(0)

  Gep_arrayidx212.io.idx(0) <> binaryOp_sub11.io.Out(0)

  ld_13.io.GepAddr <> Gep_arrayidx212.io.Out(0)

  st_15.io.GepAddr <> Gep_arrayidx212.io.Out(1)

  binaryOp_inc314.io.LeftIO <> ld_13.io.Out(0)

  st_15.io.inData <> binaryOp_inc314.io.Out(0)

  Gep_arrayidx517.io.idx(0) <> binaryOp_sub416.io.Out(0)

  ld_18.io.GepAddr <> Gep_arrayidx517.io.Out(0)

  ret_19.io.In.data("field0") <> ld_18.io.Out(0)

  Gep_arrayidx212.io.baseAddress <> InputSplitter.io.Out.data("field0")(1)

  Gep_arrayidx517.io.baseAddress <> InputSplitter.io.Out.data("field0")(2)

  binaryOp_sub11.io.LeftIO <> InputSplitter.io.Out.data("field1")(1)

  binaryOp_sub416.io.LeftIO <> InputSplitter.io.Out.data("field1")(2)

  st_8.io.Out(0).ready := true.B

  st_15.io.Out(0).ready := true.B



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_19.io.Out

}

import java.io.{File, FileWriter}
object test18Main extends App {
  val dir = new File("RTL/test18") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new test18DF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}
