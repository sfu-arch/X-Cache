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

abstract class test15DFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32, 32))))
    val MemResp = Flipped(Valid(new MemResp))
    val MemReq = Decoupled(new MemReq)
    val out = Decoupled(new Call(List(32)))
  })
}

class test15DF(implicit p: Parameters) extends test15DFIO()(p) {


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  val MemCtrl = Module(new UnifiedController(ID=0, Size=32, NReads=3, NWrites=3)
		 (WControl=new WriteMemoryController(NumOps=3, BaseSize=2, NumEntries=2))
		 (RControl=new ReadMemoryController(NumOps=3, BaseSize=2, NumEntries=2))
		 (RWArbiter=new ReadWriteArbiter()))

  io.MemReq <> MemCtrl.io.MemReq
  MemCtrl.io.MemResp <> io.MemResp

  val InputSplitter = Module(new SplitCallNew(List(1,1)))
  InputSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val Loop_0 = Module(new LoopBlock(NumIns=List(1,2), NumOuts = 0, NumExits=1, ID = 0))

  val Loop_1 = Module(new LoopBlock(NumIns=List(3,2), NumOuts = 0, NumExits=1, ID = 1))

  val Loop_2 = Module(new LoopBlock(NumIns=List(2,2), NumOuts = 1, NumExits=1, ID = 2))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_entry0 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 0))

  val bb_for_cond1 = Module(new LoopHead(NumOuts = 7, NumPhi=2, BID = 1))

  val bb_for_body2 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 2))

  val bb_for_cond13 = Module(new LoopHead(NumOuts = 4, NumPhi=1, BID = 3))

  val bb_for_body34 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 4))

  val bb_for_cond45 = Module(new LoopHead(NumOuts = 4, NumPhi=1, BID = 5))

  val bb_for_inc6 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 9, BID = 6))

  val bb_for_inc107 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 10, BID = 7))

  val bb_for_inc168 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 11, BID = 8))

  val bb_for_end189 = Module(new BasicBlockNode(NumInputs = 1, NumOuts = 4, NumPhi=1, BID = 9))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  br label %for.cond
  val br_0 = Module(new UBranchNode(ID = 0))

  //  %i.0 = phi i32 [ 0, %entry ], [ %inc17, %for.inc16 ]
  val phi_i_01 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 1))

  //  %result.0 = phi i32 [ 0, %entry ], [ %add, %for.inc16 ]
  val phi_result_02 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 2))

  //  %cmp = icmp ult i32 %i.0, 3
  val icmp_cmp3 = Module(new IcmpNode(NumOuts = 1, ID = 3, opCode = "ult")(sign=false))

  //  br i1 %cmp, label %for.body, label %for.end18
  val br_4 = Module(new CBranchFastNodeVariable(NumTrue = 1, NumFalse = 1, ID = 4))

  //  br label %for.cond1
  val br_5 = Module(new UBranchNode(ID = 5))

  //  %j.0 = phi i32 [ 0, %for.body ], [ %inc11, %for.inc10 ]
  val phi_j_06 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 6))

  //  %cmp2 = icmp ult i32 %j.0, %n
  val icmp_cmp27 = Module(new IcmpNode(NumOuts = 1, ID = 7, opCode = "ult")(sign=false))

  //  br i1 %cmp2, label %for.body3, label %for.inc16
  val br_8 = Module(new CBranchFastNodeVariable(NumTrue = 1, NumFalse = 1, ID = 8))

  //  br label %for.cond4
  val br_9 = Module(new UBranchNode(ID = 9))

  //  %k.0 = phi i32 [ 0, %for.body3 ], [ %inc, %for.inc ]
  val phi_k_010 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 4, ID = 10))

  //  %cmp5 = icmp ult i32 %k.0, %n
  val icmp_cmp511 = Module(new IcmpNode(NumOuts = 1, ID = 11, opCode = "ult")(sign=false))

  //  br i1 %cmp5, label %for.inc, label %for.inc10
  val br_12 = Module(new CBranchFastNodeVariable(NumTrue = 1, NumFalse = 1, ID = 12))

  //  %arrayidx = getelementptr inbounds i32, i32* %a, i32 %k.0
  val Gep_arrayidx13 = Module(new GepNode(NumIns = 1, NumOuts=1, ID=13)(ElementSize = 4, ArraySize = List()))

  //  %0 = load i32, i32* %arrayidx, align 4
  val ld_14 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=14, RouteID=0))

  //  %mul = mul i32 2, %0
  val binaryOp_mul15 = Module(new ComputeNode(NumOuts = 1, ID = 15, opCode = "mul")(sign=false))

  //  %arrayidx7 = getelementptr inbounds i32, i32* %a, i32 %k.0
  val Gep_arrayidx716 = Module(new GepNode(NumIns = 1, NumOuts=1, ID=16)(ElementSize = 4, ArraySize = List()))

  //  store i32 %mul, i32* %arrayidx7, align 4
  val st_17 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=1, ID=17, RouteID=0))

  //  %inc = add i32 %k.0, 1
  val binaryOp_inc18 = Module(new ComputeNode(NumOuts = 1, ID = 18, opCode = "add")(sign=false))

  //  br label %for.cond4
  val br_19 = Module(new UBranchNode(NumPredOps=1, NumOuts=2, ID = 19))

  //  %sub = sub i32 %n, 1
  val binaryOp_sub20 = Module(new ComputeNode(NumOuts = 1, ID = 20, opCode = "sub")(sign=false))

  //  %arrayidx8 = getelementptr inbounds i32, i32* %a, i32 %sub
  val Gep_arrayidx821 = Module(new GepNode(NumIns = 1, NumOuts=2, ID=21)(ElementSize = 4, ArraySize = List()))

  //  %1 = load i32, i32* %arrayidx8, align 4
  val ld_22 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=22, RouteID=1))

  //  %inc9 = add i32 %1, 1
  val binaryOp_inc923 = Module(new ComputeNode(NumOuts = 1, ID = 23, opCode = "add")(sign=false))

  //  store i32 %inc9, i32* %arrayidx8, align 4
  val st_24 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=1, ID=24, RouteID=1))

  //  %inc11 = add i32 %j.0, 1
  val binaryOp_inc1125 = Module(new ComputeNode(NumOuts = 1, ID = 25, opCode = "add")(sign=false))

  //  br label %for.cond1
  val br_26 = Module(new UBranchNode(NumPredOps=1, NumOuts=2, ID = 26))

  //  %sub13 = sub i32 %n, 1
  val binaryOp_sub1327 = Module(new ComputeNode(NumOuts = 1, ID = 27, opCode = "sub")(sign=false))

  //  %arrayidx14 = getelementptr inbounds i32, i32* %a, i32 %sub13
  val Gep_arrayidx1428 = Module(new GepNode(NumIns = 1, NumOuts=2, ID=28)(ElementSize = 4, ArraySize = List()))

  //  %2 = load i32, i32* %arrayidx14, align 4
  val ld_29 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=2, ID=29, RouteID=2))

  //  %inc15 = add i32 %2, 1
  val binaryOp_inc1530 = Module(new ComputeNode(NumOuts = 1, ID = 30, opCode = "add")(sign=false))

  //  store i32 %inc15, i32* %arrayidx14, align 4
  val st_31 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=1, ID=31, RouteID=2))

  //  %add = add i32 %result.0, %2
  val binaryOp_add32 = Module(new ComputeNode(NumOuts = 1, ID = 32, opCode = "add")(sign=false))

  //  %inc17 = add i32 %i.0, 1
  val binaryOp_inc1733 = Module(new ComputeNode(NumOuts = 1, ID = 33, opCode = "add")(sign=false))

  //  br label %for.cond
  val br_34 = Module(new UBranchNode(NumPredOps=1, NumOuts=2, ID = 34))

  //  %result.0.lcssa = phi i32 [ %result.0, %for.cond ]
  val phi_result_0_lcssa35 = Module(new PhiFastNode(NumInputs = 1, NumOutputs = 1, ID = 35))

  //  %div = sdiv i32 %result.0.lcssa, 2
  val binaryOp_div36 = Module(new ComputeNode(NumOuts = 1, ID = 36, opCode = "udiv")(sign=false))

  //  ret i32 %div
  val ret_37 = Module(new RetNode2(retTypes=List(32), ID = 37))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i32 0
  val const0 = Module(new ConstFastNode(value = 0, ID = 0))

  //i32 0
  val const1 = Module(new ConstFastNode(value = 0, ID = 1))

  //i32 3
  val const2 = Module(new ConstFastNode(value = 3, ID = 2))

  //i32 0
  val const3 = Module(new ConstFastNode(value = 0, ID = 3))

  //i32 0
  val const4 = Module(new ConstFastNode(value = 0, ID = 4))

  //i32 2
  val const5 = Module(new ConstFastNode(value = 2, ID = 5))

  //i32 1
  val const6 = Module(new ConstFastNode(value = 1, ID = 6))

  //i32 1
  val const7 = Module(new ConstFastNode(value = 1, ID = 7))

  //i32 1
  val const8 = Module(new ConstFastNode(value = 1, ID = 8))

  //i32 1
  val const9 = Module(new ConstFastNode(value = 1, ID = 9))

  //i32 1
  val const10 = Module(new ConstFastNode(value = 1, ID = 10))

  //i32 1
  val const11 = Module(new ConstFastNode(value = 1, ID = 11))

  //i32 1
  val const12 = Module(new ConstFastNode(value = 1, ID = 12))

  //i32 2
  val const13 = Module(new ConstFastNode(value = 2, ID = 13))



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_entry0.io.predicateIn <> InputSplitter.io.Out.enable

  bb_for_cond1.io.activate <> Loop_2.io.activate

  bb_for_cond1.io.loopBack <> br_34.io.Out(0)

  bb_for_body2.io.predicateIn <> br_4.io.TrueOutput(0)

  bb_for_cond13.io.activate <> Loop_1.io.activate

  bb_for_cond13.io.loopBack <> br_26.io.Out(0)

  bb_for_body34.io.predicateIn <> br_8.io.TrueOutput(0)

  bb_for_cond45.io.activate <> Loop_0.io.activate

  bb_for_cond45.io.loopBack <> br_19.io.Out(0)

  bb_for_inc6.io.predicateIn <> br_12.io.TrueOutput(0)

  bb_for_inc107.io.predicateIn <> Loop_0.io.endEnable

  bb_for_inc168.io.predicateIn <> Loop_1.io.endEnable

  bb_for_end189.io.predicateIn(0) <> Loop_2.io.endEnable



  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */

  Loop_0.io.enable <> br_9.io.Out(0)

  Loop_0.io.latchEnable <> br_19.io.Out(1)

  Loop_0.io.loopExit(0) <> br_12.io.FalseOutput(0)

  Loop_1.io.enable <> br_5.io.Out(0)

  Loop_1.io.latchEnable <> br_26.io.Out(1)

  Loop_1.io.loopExit(0) <> br_8.io.FalseOutput(0)

  Loop_2.io.enable <> br_0.io.Out(0)

  Loop_2.io.latchEnable <> br_34.io.Out(1)

  Loop_2.io.loopExit(0) <> br_4.io.FalseOutput(0)



  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */

  br_34.io.PredOp(0) <> st_31.io.SuccOp(0)

  br_26.io.PredOp(0) <> st_24.io.SuccOp(0)

  br_19.io.PredOp(0) <> st_17.io.SuccOp(0)



  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

  Loop_0.io.In(0) <> Loop_1.io.liveIn.data("field0")(1)

  Loop_0.io.In(1) <> Loop_1.io.liveIn.data("field1")(1)

  Loop_1.io.In(0) <> Loop_2.io.liveIn.data("field0")(0)

  Loop_1.io.In(1) <> Loop_2.io.liveIn.data("field1")(1)

  Loop_2.io.In(0) <> InputSplitter.io.Out.data("field1")(0)

  Loop_2.io.In(1) <> InputSplitter.io.Out.data("field0")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */

  icmp_cmp511.io.RightIO <> Loop_0.io.liveIn.data("field0")(0)

  Gep_arrayidx13.io.baseAddress <> Loop_0.io.liveIn.data("field1")(0)

  Gep_arrayidx716.io.baseAddress <> Loop_0.io.liveIn.data("field1")(1)

  icmp_cmp27.io.RightIO <> Loop_1.io.liveIn.data("field0")(0)

  binaryOp_sub20.io.LeftIO <> Loop_1.io.liveIn.data("field0")(2)

  Gep_arrayidx821.io.baseAddress <> Loop_1.io.liveIn.data("field1")(0)

  binaryOp_sub1327.io.LeftIO <> Loop_2.io.liveIn.data("field0")(1)

  Gep_arrayidx1428.io.baseAddress <> Loop_2.io.liveIn.data("field1")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */

  Loop_2.io.liveOut(0) <> phi_result_02.io.Out(1)



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  br_0.io.enable <> bb_entry0.io.Out(0)


  const0.io.enable <> bb_for_cond1.io.Out(0)

  const1.io.enable <> bb_for_cond1.io.Out(1)

  const2.io.enable <> bb_for_cond1.io.Out(2)

  phi_i_01.io.enable <> bb_for_cond1.io.Out(3)

  phi_result_02.io.enable <> bb_for_cond1.io.Out(4)

  icmp_cmp3.io.enable <> bb_for_cond1.io.Out(5)

  br_4.io.enable <> bb_for_cond1.io.Out(6)


  br_5.io.enable <> bb_for_body2.io.Out(0)


  const3.io.enable <> bb_for_cond13.io.Out(0)

  phi_j_06.io.enable <> bb_for_cond13.io.Out(1)

  icmp_cmp27.io.enable <> bb_for_cond13.io.Out(2)

  br_8.io.enable <> bb_for_cond13.io.Out(3)


  br_9.io.enable <> bb_for_body34.io.Out(0)


  const4.io.enable <> bb_for_cond45.io.Out(0)

  phi_k_010.io.enable <> bb_for_cond45.io.Out(1)

  icmp_cmp511.io.enable <> bb_for_cond45.io.Out(2)

  br_12.io.enable <> bb_for_cond45.io.Out(3)


  const5.io.enable <> bb_for_inc6.io.Out(0)

  const6.io.enable <> bb_for_inc6.io.Out(1)

  Gep_arrayidx13.io.enable <> bb_for_inc6.io.Out(2)

  ld_14.io.enable <> bb_for_inc6.io.Out(3)

  binaryOp_mul15.io.enable <> bb_for_inc6.io.Out(4)

  Gep_arrayidx716.io.enable <> bb_for_inc6.io.Out(5)

  st_17.io.enable <> bb_for_inc6.io.Out(6)

  binaryOp_inc18.io.enable <> bb_for_inc6.io.Out(7)

  br_19.io.enable <> bb_for_inc6.io.Out(8)


  const7.io.enable <> bb_for_inc107.io.Out(0)

  const8.io.enable <> bb_for_inc107.io.Out(1)

  const9.io.enable <> bb_for_inc107.io.Out(2)

  binaryOp_sub20.io.enable <> bb_for_inc107.io.Out(3)

  Gep_arrayidx821.io.enable <> bb_for_inc107.io.Out(4)

  ld_22.io.enable <> bb_for_inc107.io.Out(5)

  binaryOp_inc923.io.enable <> bb_for_inc107.io.Out(6)

  st_24.io.enable <> bb_for_inc107.io.Out(7)

  binaryOp_inc1125.io.enable <> bb_for_inc107.io.Out(8)

  br_26.io.enable <> bb_for_inc107.io.Out(9)


  const10.io.enable <> bb_for_inc168.io.Out(0)

  const11.io.enable <> bb_for_inc168.io.Out(1)

  const12.io.enable <> bb_for_inc168.io.Out(2)

  binaryOp_sub1327.io.enable <> bb_for_inc168.io.Out(3)

  Gep_arrayidx1428.io.enable <> bb_for_inc168.io.Out(4)

  ld_29.io.enable <> bb_for_inc168.io.Out(5)

  binaryOp_inc1530.io.enable <> bb_for_inc168.io.Out(6)

  st_31.io.enable <> bb_for_inc168.io.Out(7)

  binaryOp_add32.io.enable <> bb_for_inc168.io.Out(8)

  binaryOp_inc1733.io.enable <> bb_for_inc168.io.Out(9)

  br_34.io.enable <> bb_for_inc168.io.Out(10)


  const13.io.enable <> bb_for_end189.io.Out(0)

  phi_result_0_lcssa35.io.enable <> bb_for_end189.io.Out(1)

  binaryOp_div36.io.enable <> bb_for_end189.io.Out(2)

  ret_37.io.In.enable <> bb_for_end189.io.Out(3)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  phi_i_01.io.Mask <> bb_for_cond1.io.MaskBB(0)

  phi_result_02.io.Mask <> bb_for_cond1.io.MaskBB(1)

  phi_j_06.io.Mask <> bb_for_cond13.io.MaskBB(0)

  phi_k_010.io.Mask <> bb_for_cond45.io.MaskBB(0)

  phi_result_0_lcssa35.io.Mask <> bb_for_end189.io.MaskBB(0)



  /* ================================================================== *
   *                   PRINT ALLOCA OFFSET                              *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */

  MemCtrl.io.ReadIn(0) <> ld_14.io.memReq

  ld_14.io.memResp <> MemCtrl.io.ReadOut(0)

  MemCtrl.io.WriteIn(0) <> st_17.io.memReq

  st_17.io.memResp <> MemCtrl.io.WriteOut(0)

  MemCtrl.io.ReadIn(1) <> ld_22.io.memReq

  ld_22.io.memResp <> MemCtrl.io.ReadOut(1)

  MemCtrl.io.WriteIn(1) <> st_24.io.memReq

  st_24.io.memResp <> MemCtrl.io.WriteOut(1)

  MemCtrl.io.ReadIn(2) <> ld_29.io.memReq

  ld_29.io.memResp <> MemCtrl.io.ReadOut(2)

  MemCtrl.io.WriteIn(2) <> st_31.io.memReq

  st_31.io.memResp <> MemCtrl.io.WriteOut(2)



  /* ================================================================== *
   *                   PRINT SHARED CONNECTIONS                         *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  phi_i_01.io.InData(0) <> const0.io.Out

  phi_result_02.io.InData(0) <> const1.io.Out

  icmp_cmp3.io.RightIO <> const2.io.Out

  phi_j_06.io.InData(0) <> const3.io.Out

  phi_k_010.io.InData(0) <> const4.io.Out

  binaryOp_mul15.io.LeftIO <> const5.io.Out

  binaryOp_inc18.io.RightIO <> const6.io.Out

  binaryOp_sub20.io.RightIO <> const7.io.Out

  binaryOp_inc923.io.RightIO <> const8.io.Out

  binaryOp_inc1125.io.RightIO <> const9.io.Out

  binaryOp_sub1327.io.RightIO <> const10.io.Out

  binaryOp_inc1530.io.RightIO <> const11.io.Out

  binaryOp_inc1733.io.RightIO <> const12.io.Out

  binaryOp_div36.io.RightIO <> const13.io.Out

  icmp_cmp3.io.LeftIO <> phi_i_01.io.Out(0)

  binaryOp_inc1733.io.LeftIO <> phi_i_01.io.Out(1)

  binaryOp_add32.io.LeftIO <> phi_result_02.io.Out(0)

  br_4.io.CmpIO <> icmp_cmp3.io.Out(0)

  icmp_cmp27.io.LeftIO <> phi_j_06.io.Out(0)

  binaryOp_inc1125.io.LeftIO <> phi_j_06.io.Out(1)

  br_8.io.CmpIO <> icmp_cmp27.io.Out(0)

  icmp_cmp511.io.LeftIO <> phi_k_010.io.Out(0)

  Gep_arrayidx13.io.idx(0) <> phi_k_010.io.Out(1)

  Gep_arrayidx716.io.idx(0) <> phi_k_010.io.Out(2)

  binaryOp_inc18.io.LeftIO <> phi_k_010.io.Out(3)

  br_12.io.CmpIO <> icmp_cmp511.io.Out(0)

  ld_14.io.GepAddr <> Gep_arrayidx13.io.Out.data(0)

  binaryOp_mul15.io.RightIO <> ld_14.io.Out.data(0)

  st_17.io.inData <> binaryOp_mul15.io.Out(0)

  st_17.io.GepAddr <> Gep_arrayidx716.io.Out.data(0)

  phi_k_010.io.InData(1) <> binaryOp_inc18.io.Out(0)

  Gep_arrayidx821.io.idx(0) <> binaryOp_sub20.io.Out(0)

  ld_22.io.GepAddr <> Gep_arrayidx821.io.Out.data(0)

  st_24.io.GepAddr <> Gep_arrayidx821.io.Out.data(1)

  binaryOp_inc923.io.LeftIO <> ld_22.io.Out.data(0)

  st_24.io.inData <> binaryOp_inc923.io.Out(0)

  phi_j_06.io.InData(1) <> binaryOp_inc1125.io.Out(0)

  Gep_arrayidx1428.io.idx(0) <> binaryOp_sub1327.io.Out(0)

  ld_29.io.GepAddr <> Gep_arrayidx1428.io.Out.data(0)

  st_31.io.GepAddr <> Gep_arrayidx1428.io.Out.data(1)

  binaryOp_inc1530.io.LeftIO <> ld_29.io.Out.data(0)

  binaryOp_add32.io.RightIO <> ld_29.io.Out.data(1)

  st_31.io.inData <> binaryOp_inc1530.io.Out(0)

  phi_result_02.io.InData(1) <> binaryOp_add32.io.Out(0)

  phi_i_01.io.InData(1) <> binaryOp_inc1733.io.Out(0)

  binaryOp_div36.io.LeftIO <> phi_result_0_lcssa35.io.Out(0)

  ret_37.io.In.data("field0") <> binaryOp_div36.io.Out(0)

  phi_result_0_lcssa35.io.InData(0) <> Loop_2.io.Out(0)

  st_17.io.Out(0).ready := true.B

  st_24.io.Out(0).ready := true.B

  st_31.io.Out(0).ready := true.B



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_37.io.Out

}

import java.io.{File, FileWriter}
object test15Main extends App {
  val dir = new File("RTL/test15") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new test15DF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}
