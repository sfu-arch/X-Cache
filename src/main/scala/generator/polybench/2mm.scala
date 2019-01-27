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

abstract class kernel_2mmDFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32))))
    val MemResp = Flipped(Valid(new MemResp))
    val MemReq = Decoupled(new MemReq)
    val out = Decoupled(new Call(List()))
  })
}

class kernel_2mmDF(implicit p: Parameters) extends kernel_2mmDFIO()(p) {


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  val MemCtrl = Module(new UnifiedController(ID=0, Size=32, NReads=7, NWrites=4)
		 (WControl=new WriteMemoryController(NumOps=4, BaseSize=2, NumEntries=2))
		 (RControl=new ReadMemoryController(NumOps=7, BaseSize=2, NumEntries=2))
		 (RWArbiter=new ReadWriteArbiter()))

  io.MemReq <> MemCtrl.io.MemReq
  MemCtrl.io.MemResp <> io.MemResp

  val InputSplitter = Module(new SplitCallNew(List(2,2,1,1,1,1,2,1,1,1,1)))
  InputSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val Loop_0 = Module(new LoopBlock(NumIns=List(1,1,2,1,2,1), NumOuts = 0, NumExits=1, ID = 0))

  val Loop_1 = Module(new LoopBlock(NumIns=List(1,2,2,1,1,1,1), NumOuts = 0, NumExits=1, ID = 1))

  val Loop_2 = Module(new LoopBlock(NumIns=List(1,1,1,1,1,1,1), NumOuts = 0, NumExits=1, ID = 2))

  val Loop_3 = Module(new LoopBlock(NumIns=List(1,1,2,1,1,2,1), NumOuts = 0, NumExits=1, ID = 3))

  val Loop_4 = Module(new LoopBlock(NumIns=List(1,2,2,1,1,1,1), NumOuts = 0, NumExits=1, ID = 4))

  val Loop_5 = Module(new LoopBlock(NumIns=List(1,1,1,1,1,1,1), NumOuts = 0, NumExits=1, ID = 5))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_entry0 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 0))

  val bb_for_cond1 = Module(new LoopHead(NumOuts = 4, NumPhi=1, BID = 1))

  val bb_for_body2 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 2))

  val bb_for_cond43 = Module(new LoopHead(NumOuts = 4, NumPhi=1, BID = 3))

  val bb_for_body64 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 6, BID = 4))

  val bb_for_cond85 = Module(new LoopHead(NumOuts = 4, NumPhi=1, BID = 5))

  val bb_for_inc6 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 19, BID = 6))

  val bb_for_inc187 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 3, BID = 7))

  val bb_for_inc218 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 3, BID = 8))

  val bb_for_end239 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 9))

  val bb_for_cond2410 = Module(new LoopHead(NumOuts = 4, NumPhi=1, BID = 10))

  val bb_for_body2611 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 11))

  val bb_for_cond2712 = Module(new LoopHead(NumOuts = 4, NumPhi=1, BID = 12))

  val bb_for_body2913 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 7, BID = 13))

  val bb_for_cond3314 = Module(new LoopHead(NumOuts = 4, NumPhi=1, BID = 14))

  val bb_for_inc4415 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 18, BID = 15))

  val bb_for_inc4716 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 3, BID = 16))

  val bb_for_inc5017 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 3, BID = 17))

  val bb_for_end5218 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 18))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  br label %for.cond
  val br_0 = Module(new UBranchFastNode(ID = 0))

  //  %i.0 = phi i32 [ 0, %entry ], [ %inc22, %for.inc21 ]
  val phi_i_01 = Module(new PhiFastNode2(NumInputs = 2, NumOutputs = 3, ID = 1))

  //  %cmp = icmp slt i32 %i.0, %ni
  val icmp_cmp2 = Module(new IcmpNode(NumOuts = 1, ID = 2, opCode = "ult")(sign=false))

  //  br i1 %cmp, label %for.body, label %for.end23
  val br_3 = Module(new CBranchFastNodeVariable(NumTrue = 1, NumFalse = 1, ID = 3))

  //  br label %for.cond4
  val br_4 = Module(new UBranchNode(ID = 4))

  //  %j.0 = phi i32 [ 0, %for.body ], [ %inc19, %for.inc18 ]
  val phi_j_05 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 4, ID = 5))

  //  %cmp5 = icmp slt i32 %j.0, %nj
  val icmp_cmp56 = Module(new IcmpNode(NumOuts = 1, ID = 6, opCode = "ult")(sign=false))

  //  br i1 %cmp5, label %for.body6, label %for.inc21
  val br_7 = Module(new CBranchFastNodeVariable(NumTrue = 1, NumFalse = 1, ID = 7))

  //  %arrayidx = getelementptr inbounds [1024 x double], [1024 x double]* %tmp, i32 %i.0
  val Gep_arrayidx8 = Module(new GepNode(NumIns = 1, NumOuts=1, ID=8)(ElementSize = 8, ArraySize = List()))

  //  %arrayidx7 = getelementptr inbounds [1024 x double], [1024 x double]* %arrayidx, i32 0, i32 %j.0
  val Gep_arrayidx79 = Module(new GepNode(NumIns = 2, NumOuts=1, ID=9)(ElementSize = 8, ArraySize = List()))

  //  store double 0.000000e+00, double* %arrayidx7, align 4
  val st_10 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=1, ID=10, RouteID=0))

  //  br label %for.cond8
  val br_11 = Module(new UBranchNode(NumPredOps=1, ID = 11))

  //  %k.0 = phi i32 [ 0, %for.body6 ], [ %inc, %for.inc ]
  val phi_k_012 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 4, ID = 12))

  //  %cmp9 = icmp slt i32 %k.0, %nk
  val icmp_cmp913 = Module(new IcmpNode(NumOuts = 1, ID = 13, opCode = "ult")(sign=false))

  //  br i1 %cmp9, label %for.inc, label %for.inc18
  val br_14 = Module(new CBranchFastNodeVariable(NumTrue = 1, NumFalse = 1, ID = 14))

  //  %arrayidx11 = getelementptr inbounds [1024 x double], [1024 x double]* %A, i32 %i.0
  val Gep_arrayidx1115 = Module(new GepNode(NumIns = 1, NumOuts=1, ID=15)(ElementSize = 8, ArraySize = List()))

  //  %arrayidx12 = getelementptr inbounds [1024 x double], [1024 x double]* %arrayidx11, i32 0, i32 %k.0
  val Gep_arrayidx1216 = Module(new GepNode(NumIns = 2, NumOuts=1, ID=16)(ElementSize = 8, ArraySize = List()))

  //  %0 = load double, double* %arrayidx12, align 4
  val ld_17 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=17, RouteID=0))

  //  %mul = fmul double %alpha, %0
  val binaryOp_mul18 = Module(new ComputeNode(NumOuts = 1, ID = 18, opCode = "fmul")(sign=false))

  //  %arrayidx13 = getelementptr inbounds [1024 x double], [1024 x double]* %B, i32 %k.0
  val Gep_arrayidx1319 = Module(new GepNode(NumIns = 1, NumOuts=1, ID=19)(ElementSize = 8, ArraySize = List()))

  //  %arrayidx14 = getelementptr inbounds [1024 x double], [1024 x double]* %arrayidx13, i32 0, i32 %j.0
  val Gep_arrayidx1420 = Module(new GepNode(NumIns = 2, NumOuts=1, ID=20)(ElementSize = 8, ArraySize = List()))

  //  %1 = load double, double* %arrayidx14, align 4
  val ld_21 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=21, RouteID=1))

  //  %mul15 = fmul double %mul, %1
  val binaryOp_mul1522 = Module(new ComputeNode(NumOuts = 1, ID = 22, opCode = "fmul")(sign=false))

  //  %arrayidx16 = getelementptr inbounds [1024 x double], [1024 x double]* %tmp, i32 %i.0
  val Gep_arrayidx1623 = Module(new GepNode(NumIns = 1, NumOuts=1, ID=23)(ElementSize = 8, ArraySize = List()))

  //  %arrayidx17 = getelementptr inbounds [1024 x double], [1024 x double]* %arrayidx16, i32 0, i32 %j.0
  val Gep_arrayidx1724 = Module(new GepNode(NumIns = 2, NumOuts=2, ID=24)(ElementSize = 8, ArraySize = List()))

  //  %2 = load double, double* %arrayidx17, align 4
  val ld_25 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=25, RouteID=2))

  //  %add = fadd double %2, %mul15
  val FP_add26 = Module(new FPComputeNode(NumOuts = 1, ID = 26, opCode = "fadd")(t = p(FTYP)))

  //  store double %add, double* %arrayidx17, align 4
  val st_27 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=1, ID=27, RouteID=1))

  //  %inc = add nsw i32 %k.0, 1
  val binaryOp_inc28 = Module(new ComputeNode(NumOuts = 1, ID = 28, opCode = "add")(sign=false))

  //  br label %for.cond8
  val br_29 = Module(new UBranchNode(NumPredOps=1, NumOuts=2, ID = 29))

  //  %inc19 = add nsw i32 %j.0, 1
  val binaryOp_inc1930 = Module(new ComputeNode(NumOuts = 1, ID = 30, opCode = "add")(sign=false))

  //  br label %for.cond4
  val br_31 = Module(new UBranchNode(NumOuts=2, ID = 31))

  //  %inc22 = add nsw i32 %i.0, 1
  val binaryOp_inc2232 = Module(new ComputeNode(NumOuts = 1, ID = 32, opCode = "add")(sign=false))

  //  br label %for.cond
  val br_33 = Module(new UBranchNode(NumOuts=2, ID = 33))

  //  br label %for.cond24
  val br_34 = Module(new UBranchNode(ID = 34))

  //  %i.1 = phi i32 [ 0, %for.end23 ], [ %inc51, %for.inc50 ]
  val phi_i_135 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 3, ID = 35))

  //  %cmp25 = icmp slt i32 %i.1, %ni
  val icmp_cmp2536 = Module(new IcmpNode(NumOuts = 1, ID = 36, opCode = "ult")(sign=false))

  //  br i1 %cmp25, label %for.body26, label %for.end52
  val br_37 = Module(new CBranchFastNodeVariable(NumTrue = 1, NumFalse = 1, ID = 37))

  //  br label %for.cond27
  val br_38 = Module(new UBranchNode(ID = 38))

  //  %j.1 = phi i32 [ 0, %for.body26 ], [ %inc48, %for.inc47 ]
  val phi_j_139 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 4, ID = 39))

  //  %cmp28 = icmp slt i32 %j.1, %nl
  val icmp_cmp2840 = Module(new IcmpNode(NumOuts = 1, ID = 40, opCode = "ult")(sign=false))

  //  br i1 %cmp28, label %for.body29, label %for.inc50
  val br_41 = Module(new CBranchFastNodeVariable(NumTrue = 1, NumFalse = 1, ID = 41))

  //  %arrayidx30 = getelementptr inbounds [1024 x double], [1024 x double]* %D, i32 %i.1
  val Gep_arrayidx3042 = Module(new GepNode(NumIns = 1, NumOuts=1, ID=42)(ElementSize = 8, ArraySize = List()))

  //  %arrayidx31 = getelementptr inbounds [1024 x double], [1024 x double]* %arrayidx30, i32 0, i32 %j.1
  val Gep_arrayidx3143 = Module(new GepNode(NumIns = 2, NumOuts=2, ID=43)(ElementSize = 8, ArraySize = List()))

  //  %3 = load double, double* %arrayidx31, align 4
  val ld_44 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=44, RouteID=3))

  //  %mul32 = fmul double %3, %beta
  val binaryOp_mul3245 = Module(new ComputeNode(NumOuts = 1, ID = 45, opCode = "fmul")(sign=false))

  //  store double %mul32, double* %arrayidx31, align 4
  val st_46 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=1, ID=46, RouteID=2))

  //  br label %for.cond33
  val br_47 = Module(new UBranchNode(NumPredOps=1, ID = 47))

  //  %k.1 = phi i32 [ 0, %for.body29 ], [ %inc45, %for.inc44 ]
  val phi_k_148 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 4, ID = 48))

  //  %cmp34 = icmp slt i32 %k.1, %nj
  val icmp_cmp3449 = Module(new IcmpNode(NumOuts = 1, ID = 49, opCode = "ult")(sign=false))

  //  br i1 %cmp34, label %for.inc44, label %for.inc47
  val br_50 = Module(new CBranchFastNodeVariable(NumTrue = 1, NumFalse = 1, ID = 50))

  //  %arrayidx36 = getelementptr inbounds [1024 x double], [1024 x double]* %tmp, i32 %i.1
  val Gep_arrayidx3651 = Module(new GepNode(NumIns = 1, NumOuts=1, ID=51)(ElementSize = 8, ArraySize = List()))

  //  %arrayidx37 = getelementptr inbounds [1024 x double], [1024 x double]* %arrayidx36, i32 0, i32 %k.1
  val Gep_arrayidx3752 = Module(new GepNode(NumIns = 2, NumOuts=1, ID=52)(ElementSize = 8, ArraySize = List()))

  //  %4 = load double, double* %arrayidx37, align 4
  val ld_53 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=53, RouteID=4))

  //  %arrayidx38 = getelementptr inbounds [1024 x double], [1024 x double]* %C, i32 %k.1
  val Gep_arrayidx3854 = Module(new GepNode(NumIns = 1, NumOuts=1, ID=54)(ElementSize = 8, ArraySize = List()))

  //  %arrayidx39 = getelementptr inbounds [1024 x double], [1024 x double]* %arrayidx38, i32 0, i32 %j.1
  val Gep_arrayidx3955 = Module(new GepNode(NumIns = 2, NumOuts=1, ID=55)(ElementSize = 8, ArraySize = List()))

  //  %5 = load double, double* %arrayidx39, align 4
  val ld_56 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=56, RouteID=5))

  //  %mul40 = fmul double %4, %5
  val binaryOp_mul4057 = Module(new ComputeNode(NumOuts = 1, ID = 57, opCode = "fmul")(sign=false))

  //  %arrayidx41 = getelementptr inbounds [1024 x double], [1024 x double]* %D, i32 %i.1
  val Gep_arrayidx4158 = Module(new GepNode(NumIns = 1, NumOuts=1, ID=58)(ElementSize = 8, ArraySize = List()))

  //  %arrayidx42 = getelementptr inbounds [1024 x double], [1024 x double]* %arrayidx41, i32 0, i32 %j.1
  val Gep_arrayidx4259 = Module(new GepNode(NumIns = 2, NumOuts=2, ID=59)(ElementSize = 8, ArraySize = List()))

  //  %6 = load double, double* %arrayidx42, align 4
  val ld_60 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=60, RouteID=6))

  //  %add43 = fadd double %6, %mul40
  val FP_add4361 = Module(new FPComputeNode(NumOuts = 1, ID = 61, opCode = "fadd")(t = p(FTYP)))

  //  store double %add43, double* %arrayidx42, align 4
  val st_62 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=1, ID=62, RouteID=3))

  //  %inc45 = add nsw i32 %k.1, 1
  val binaryOp_inc4563 = Module(new ComputeNode(NumOuts = 1, ID = 63, opCode = "add")(sign=false))

  //  br label %for.cond33
  val br_64 = Module(new UBranchNode(NumPredOps=1, NumOuts=2, ID = 64))

  //  %inc48 = add nsw i32 %j.1, 1
  val binaryOp_inc4865 = Module(new ComputeNode(NumOuts = 1, ID = 65, opCode = "add")(sign=false))

  //  br label %for.cond27
  val br_66 = Module(new UBranchNode(NumOuts=2, ID = 66))

  //  %inc51 = add nsw i32 %i.1, 1
  val binaryOp_inc5167 = Module(new ComputeNode(NumOuts = 1, ID = 67, opCode = "add")(sign=false))

  //  br label %for.cond24
  val br_68 = Module(new UBranchNode(NumOuts=2, ID = 68))

  //  ret void
  val ret_69 = Module(new RetNode2(retTypes=List(), ID = 69))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i32 0
  val const0 = Module(new ConstFastNode(value = 0, ID = 0))

  //i32 0
  val const1 = Module(new ConstFastNode(value = 0, ID = 1))

  //i32 0
  val const2 = Module(new ConstFastNode(value = 0, ID = 2))

  //i32 0
  val const3 = Module(new ConstFastNode(value = 0, ID = 3))

  //i32 0
  val const4 = Module(new ConstFastNode(value = 0, ID = 4))

  //i32 0
  val const5 = Module(new ConstFastNode(value = 0, ID = 5))

  //i32 0
  val const6 = Module(new ConstFastNode(value = 0, ID = 6))

  //i32 1
  val const7 = Module(new ConstFastNode(value = 1, ID = 7))

  //i32 1
  val const8 = Module(new ConstFastNode(value = 1, ID = 8))

  //i32 1
  val const9 = Module(new ConstFastNode(value = 1, ID = 9))

  //i32 0
  val const10 = Module(new ConstFastNode(value = 0, ID = 10))

  //i32 0
  val const11 = Module(new ConstFastNode(value = 0, ID = 11))

  //i32 0
  val const12 = Module(new ConstFastNode(value = 0, ID = 12))

  //i32 0
  val const13 = Module(new ConstFastNode(value = 0, ID = 13))

  //i32 0
  val const14 = Module(new ConstFastNode(value = 0, ID = 14))

  //i32 0
  val const15 = Module(new ConstFastNode(value = 0, ID = 15))

  //i32 0
  val const16 = Module(new ConstFastNode(value = 0, ID = 16))

  //i32 1
  val const17 = Module(new ConstFastNode(value = 1, ID = 17))

  //i32 1
  val const18 = Module(new ConstFastNode(value = 1, ID = 18))

  //i32 1
  val const19 = Module(new ConstFastNode(value = 1, ID = 19))

  //double 0.000000e+00
  val constf0 = Module(new ConstNode(value = 0x0, ID = 0))



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_entry0.io.predicateIn <> InputSplitter.io.Out.enable

  bb_for_cond1.io.activate <> Loop_5.io.activate

  bb_for_cond1.io.loopBack <> br_33.io.Out(0)

  bb_for_body2.io.predicateIn <> br_3.io.TrueOutput(0)

  bb_for_cond43.io.activate <> Loop_4.io.activate

  bb_for_cond43.io.loopBack <> br_31.io.Out(0)

  bb_for_body64.io.predicateIn <> br_7.io.TrueOutput(0)

  bb_for_cond85.io.activate <> Loop_3.io.activate

  bb_for_cond85.io.loopBack <> br_29.io.Out(0)

  bb_for_inc6.io.predicateIn <> br_14.io.TrueOutput(0)

  bb_for_inc187.io.predicateIn <> Loop_3.io.endEnable

  bb_for_inc218.io.predicateIn <> Loop_4.io.endEnable

  bb_for_end239.io.predicateIn <> Loop_5.io.endEnable

  bb_for_cond2410.io.activate <> Loop_2.io.activate

  bb_for_cond2410.io.loopBack <> br_68.io.Out(0)

  bb_for_body2611.io.predicateIn <> br_37.io.TrueOutput(0)

  bb_for_cond2712.io.activate <> Loop_1.io.activate

  bb_for_cond2712.io.loopBack <> br_66.io.Out(0)

  bb_for_body2913.io.predicateIn <> br_41.io.TrueOutput(0)

  bb_for_cond3314.io.activate <> Loop_0.io.activate

  bb_for_cond3314.io.loopBack <> br_64.io.Out(0)

  bb_for_inc4415.io.predicateIn <> br_50.io.TrueOutput(0)

  bb_for_inc4716.io.predicateIn <> Loop_0.io.endEnable

  bb_for_inc5017.io.predicateIn <> Loop_1.io.endEnable

  bb_for_end5218.io.predicateIn <> Loop_2.io.endEnable



  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */

  Loop_0.io.enable <> br_47.io.Out(0)

  Loop_0.io.latchEnable <> br_64.io.Out(1)

  Loop_0.io.loopExit(0) <> br_50.io.FalseOutput(0)

  Loop_1.io.enable <> br_38.io.Out(0)

  Loop_1.io.latchEnable <> br_66.io.Out(1)

  Loop_1.io.loopExit(0) <> br_41.io.FalseOutput(0)

  Loop_2.io.enable <> br_34.io.Out(0)

  Loop_2.io.latchEnable <> br_68.io.Out(1)

  Loop_2.io.loopExit(0) <> br_37.io.FalseOutput(0)

  Loop_3.io.enable <> br_11.io.Out(0)

  Loop_3.io.latchEnable <> br_29.io.Out(1)

  Loop_3.io.loopExit(0) <> br_14.io.FalseOutput(0)

  Loop_4.io.enable <> br_4.io.Out(0)

  Loop_4.io.latchEnable <> br_31.io.Out(1)

  Loop_4.io.loopExit(0) <> br_7.io.FalseOutput(0)

  Loop_5.io.enable <> br_0.io.Out(0)

  Loop_5.io.latchEnable <> br_33.io.Out(1)

  Loop_5.io.loopExit(0) <> br_3.io.FalseOutput(0)



  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */

  br_47.io.PredOp(0) <> st_46.io.SuccOp(0)

  br_64.io.PredOp(0) <> st_62.io.SuccOp(0)

  br_11.io.PredOp(0) <> st_10.io.SuccOp(0)

  br_29.io.PredOp(0) <> st_27.io.SuccOp(0)



  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

  Loop_0.io.In(0) <> Loop_1.io.liveIn.elements("field4")(0)

  Loop_0.io.In(1) <> Loop_1.io.liveIn.elements("field5")(0)

  Loop_0.io.In(2) <> Loop_1.io.liveIn.elements("field2")(1)

  Loop_0.io.In(3) <> Loop_1.io.liveIn.elements("field6")(0)

  Loop_0.io.In(4) <> phi_j_139.io.Out(2)

  Loop_0.io.In(5) <> Loop_1.io.liveIn.elements("field1")(1)

  Loop_1.io.In(0) <> Loop_2.io.liveIn.elements("field1")(0)

  Loop_1.io.In(1) <> Loop_2.io.liveIn.elements("field2")(0)

  Loop_1.io.In(2) <> phi_i_135.io.Out(1)

  Loop_1.io.In(3) <> Loop_2.io.liveIn.elements("field3")(0)

  Loop_1.io.In(4) <> Loop_2.io.liveIn.elements("field4")(0)

  Loop_1.io.In(5) <> Loop_2.io.liveIn.elements("field5")(0)

  Loop_1.io.In(6) <> Loop_2.io.liveIn.elements("field6")(0)

  Loop_2.io.In(0) <> InputSplitter.io.Out.data("field0")(1)

  Loop_2.io.In(1) <> InputSplitter.io.Out.data("field3")(0)

  Loop_2.io.In(2) <> InputSplitter.io.Out.data("field10")(0)

  Loop_2.io.In(3) <> InputSplitter.io.Out.data("field5")(0)

  Loop_2.io.In(4) <> InputSplitter.io.Out.data("field1")(1)

  Loop_2.io.In(5) <> InputSplitter.io.Out.data("field6")(1)

  Loop_2.io.In(6) <> InputSplitter.io.Out.data("field9")(0)

  Loop_3.io.In(0) <> Loop_4.io.liveIn.elements("field3")(0)

  Loop_3.io.In(1) <> Loop_4.io.liveIn.elements("field4")(0)

  Loop_3.io.In(2) <> Loop_4.io.liveIn.elements("field2")(1)

  Loop_3.io.In(3) <> Loop_4.io.liveIn.elements("field5")(0)

  Loop_3.io.In(4) <> Loop_4.io.liveIn.elements("field6")(0)

  Loop_3.io.In(5) <> phi_j_05.io.Out(2)

  Loop_3.io.In(6) <> Loop_4.io.liveIn.elements("field1")(1)

  Loop_4.io.In(0) <> Loop_5.io.liveIn.elements("field1")(0)

  Loop_4.io.In(1) <> Loop_5.io.liveIn.elements("field2")(0)

  Loop_4.io.In(2) <> phi_i_01.io.Out(1)

  Loop_4.io.In(3) <> Loop_5.io.liveIn.elements("field3")(0)

  Loop_4.io.In(4) <> Loop_5.io.liveIn.elements("field4")(0)

  Loop_4.io.In(5) <> Loop_5.io.liveIn.elements("field5")(0)

  Loop_4.io.In(6) <> Loop_5.io.liveIn.elements("field6")(0)

  Loop_5.io.In(0) <> InputSplitter.io.Out.data("field0")(0)

  Loop_5.io.In(1) <> InputSplitter.io.Out.data("field1")(0)

  Loop_5.io.In(2) <> InputSplitter.io.Out.data("field6")(0)

  Loop_5.io.In(3) <> InputSplitter.io.Out.data("field2")(0)

  Loop_5.io.In(4) <> InputSplitter.io.Out.data("field7")(0)

  Loop_5.io.In(5) <> InputSplitter.io.Out.data("field4")(0)

  Loop_5.io.In(6) <> InputSplitter.io.Out.data("field8")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */

  icmp_cmp3449.io.RightIO <> Loop_0.io.liveIn.elements("field0")(0)

  Gep_arrayidx3651.io.baseAddress <> Loop_0.io.liveIn.elements("field1")(0)

  Gep_arrayidx3651.io.idx(0) <> Loop_0.io.liveIn.elements("field2")(0)

  Gep_arrayidx4158.io.idx(0) <> Loop_0.io.liveIn.elements("field2")(1)

  Gep_arrayidx3854.io.baseAddress <> Loop_0.io.liveIn.elements("field3")(0)

  Gep_arrayidx3955.io.idx(1) <> Loop_0.io.liveIn.elements("field4")(0)

  Gep_arrayidx4259.io.idx(1) <> Loop_0.io.liveIn.elements("field4")(1)

  Gep_arrayidx4158.io.baseAddress <> Loop_0.io.liveIn.elements("field5")(0)

  icmp_cmp2840.io.RightIO <> Loop_1.io.liveIn.elements("field0")(0)

  Gep_arrayidx3042.io.baseAddress <> Loop_1.io.liveIn.elements("field1")(0)

  Gep_arrayidx3042.io.idx(0) <> Loop_1.io.liveIn.elements("field2")(0)

  binaryOp_mul3245.io.RightIO <> Loop_1.io.liveIn.elements("field3")(0)

  icmp_cmp2536.io.RightIO <> Loop_2.io.liveIn.elements("field0")(0)

  icmp_cmp913.io.RightIO <> Loop_3.io.liveIn.elements("field0")(0)

  Gep_arrayidx1115.io.baseAddress <> Loop_3.io.liveIn.elements("field1")(0)

  Gep_arrayidx1115.io.idx(0) <> Loop_3.io.liveIn.elements("field2")(0)

  Gep_arrayidx1623.io.idx(0) <> Loop_3.io.liveIn.elements("field2")(1)

  binaryOp_mul18.io.LeftIO <> Loop_3.io.liveIn.elements("field3")(0)

  Gep_arrayidx1319.io.baseAddress <> Loop_3.io.liveIn.elements("field4")(0)

  Gep_arrayidx1420.io.idx(1) <> Loop_3.io.liveIn.elements("field5")(0)

  Gep_arrayidx1724.io.idx(1) <> Loop_3.io.liveIn.elements("field5")(1)

  Gep_arrayidx1623.io.baseAddress <> Loop_3.io.liveIn.elements("field6")(0)

  icmp_cmp56.io.RightIO <> Loop_4.io.liveIn.elements("field0")(0)

  Gep_arrayidx8.io.baseAddress <> Loop_4.io.liveIn.elements("field1")(0)

  Gep_arrayidx8.io.idx(0) <> Loop_4.io.liveIn.elements("field2")(0)

  icmp_cmp2.io.RightIO <> Loop_5.io.liveIn.elements("field0")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  br_0.io.enable <> bb_entry0.io.Out(0)


  const0.io.enable <> bb_for_cond1.io.Out(0)

  phi_i_01.io.enable <> bb_for_cond1.io.Out(1)

  icmp_cmp2.io.enable <> bb_for_cond1.io.Out(2)

  br_3.io.enable <> bb_for_cond1.io.Out(3)


  br_4.io.enable <> bb_for_body2.io.Out(0)


  const1.io.enable <> bb_for_cond43.io.Out(0)

  phi_j_05.io.enable <> bb_for_cond43.io.Out(1)

  icmp_cmp56.io.enable <> bb_for_cond43.io.Out(2)

  br_7.io.enable <> bb_for_cond43.io.Out(3)


  constf0.io.enable <> bb_for_body64.io.Out(1)

  const2.io.enable <> bb_for_body64.io.Out(0)

  Gep_arrayidx8.io.enable <> bb_for_body64.io.Out(2)

  Gep_arrayidx79.io.enable <> bb_for_body64.io.Out(3)

  st_10.io.enable <> bb_for_body64.io.Out(4)

  br_11.io.enable <> bb_for_body64.io.Out(5)


  const3.io.enable <> bb_for_cond85.io.Out(0)

  phi_k_012.io.enable <> bb_for_cond85.io.Out(1)

  icmp_cmp913.io.enable <> bb_for_cond85.io.Out(2)

  br_14.io.enable <> bb_for_cond85.io.Out(3)


  const4.io.enable <> bb_for_inc6.io.Out(0)

  const5.io.enable <> bb_for_inc6.io.Out(1)

  const6.io.enable <> bb_for_inc6.io.Out(2)

  const7.io.enable <> bb_for_inc6.io.Out(3)

  Gep_arrayidx1115.io.enable <> bb_for_inc6.io.Out(4)

  Gep_arrayidx1216.io.enable <> bb_for_inc6.io.Out(5)

  ld_17.io.enable <> bb_for_inc6.io.Out(6)

  binaryOp_mul18.io.enable <> bb_for_inc6.io.Out(7)

  Gep_arrayidx1319.io.enable <> bb_for_inc6.io.Out(8)

  Gep_arrayidx1420.io.enable <> bb_for_inc6.io.Out(9)

  ld_21.io.enable <> bb_for_inc6.io.Out(10)

  binaryOp_mul1522.io.enable <> bb_for_inc6.io.Out(11)

  Gep_arrayidx1623.io.enable <> bb_for_inc6.io.Out(12)

  Gep_arrayidx1724.io.enable <> bb_for_inc6.io.Out(13)

  ld_25.io.enable <> bb_for_inc6.io.Out(14)

  FP_add26.io.enable <> bb_for_inc6.io.Out(15)

  st_27.io.enable <> bb_for_inc6.io.Out(16)

  binaryOp_inc28.io.enable <> bb_for_inc6.io.Out(17)

  br_29.io.enable <> bb_for_inc6.io.Out(18)


  const8.io.enable <> bb_for_inc187.io.Out(0)

  binaryOp_inc1930.io.enable <> bb_for_inc187.io.Out(1)

  br_31.io.enable <> bb_for_inc187.io.Out(2)


  const9.io.enable <> bb_for_inc218.io.Out(0)

  binaryOp_inc2232.io.enable <> bb_for_inc218.io.Out(1)

  br_33.io.enable <> bb_for_inc218.io.Out(2)


  br_34.io.enable <> bb_for_end239.io.Out(0)


  const10.io.enable <> bb_for_cond2410.io.Out(0)

  phi_i_135.io.enable <> bb_for_cond2410.io.Out(1)

  icmp_cmp2536.io.enable <> bb_for_cond2410.io.Out(2)

  br_37.io.enable <> bb_for_cond2410.io.Out(3)


  br_38.io.enable <> bb_for_body2611.io.Out(0)


  const11.io.enable <> bb_for_cond2712.io.Out(0)

  phi_j_139.io.enable <> bb_for_cond2712.io.Out(1)

  icmp_cmp2840.io.enable <> bb_for_cond2712.io.Out(2)

  br_41.io.enable <> bb_for_cond2712.io.Out(3)


  const12.io.enable <> bb_for_body2913.io.Out(0)

  Gep_arrayidx3042.io.enable <> bb_for_body2913.io.Out(1)

  Gep_arrayidx3143.io.enable <> bb_for_body2913.io.Out(2)

  ld_44.io.enable <> bb_for_body2913.io.Out(3)

  binaryOp_mul3245.io.enable <> bb_for_body2913.io.Out(4)

  st_46.io.enable <> bb_for_body2913.io.Out(5)

  br_47.io.enable <> bb_for_body2913.io.Out(6)


  const13.io.enable <> bb_for_cond3314.io.Out(0)

  phi_k_148.io.enable <> bb_for_cond3314.io.Out(1)

  icmp_cmp3449.io.enable <> bb_for_cond3314.io.Out(2)

  br_50.io.enable <> bb_for_cond3314.io.Out(3)


  const14.io.enable <> bb_for_inc4415.io.Out(0)

  const15.io.enable <> bb_for_inc4415.io.Out(1)

  const16.io.enable <> bb_for_inc4415.io.Out(2)

  const17.io.enable <> bb_for_inc4415.io.Out(3)

  Gep_arrayidx3651.io.enable <> bb_for_inc4415.io.Out(4)

  Gep_arrayidx3752.io.enable <> bb_for_inc4415.io.Out(5)

  ld_53.io.enable <> bb_for_inc4415.io.Out(6)

  Gep_arrayidx3854.io.enable <> bb_for_inc4415.io.Out(7)

  Gep_arrayidx3955.io.enable <> bb_for_inc4415.io.Out(8)

  ld_56.io.enable <> bb_for_inc4415.io.Out(9)

  binaryOp_mul4057.io.enable <> bb_for_inc4415.io.Out(10)

  Gep_arrayidx4158.io.enable <> bb_for_inc4415.io.Out(11)

  Gep_arrayidx4259.io.enable <> bb_for_inc4415.io.Out(12)

  ld_60.io.enable <> bb_for_inc4415.io.Out(13)

  FP_add4361.io.enable <> bb_for_inc4415.io.Out(14)

  st_62.io.enable <> bb_for_inc4415.io.Out(15)

  binaryOp_inc4563.io.enable <> bb_for_inc4415.io.Out(16)

  br_64.io.enable <> bb_for_inc4415.io.Out(17)


  const18.io.enable <> bb_for_inc4716.io.Out(0)

  binaryOp_inc4865.io.enable <> bb_for_inc4716.io.Out(1)

  br_66.io.enable <> bb_for_inc4716.io.Out(2)


  const19.io.enable <> bb_for_inc5017.io.Out(0)

  binaryOp_inc5167.io.enable <> bb_for_inc5017.io.Out(1)

  br_68.io.enable <> bb_for_inc5017.io.Out(2)


  ret_69.io.In.enable <> bb_for_end5218.io.Out(0)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  phi_i_01.io.Mask <> bb_for_cond1.io.MaskBB(0)

  phi_j_05.io.Mask <> bb_for_cond43.io.MaskBB(0)

  phi_k_012.io.Mask <> bb_for_cond85.io.MaskBB(0)

  phi_i_135.io.Mask <> bb_for_cond2410.io.MaskBB(0)

  phi_j_139.io.Mask <> bb_for_cond2712.io.MaskBB(0)

  phi_k_148.io.Mask <> bb_for_cond3314.io.MaskBB(0)



  /* ================================================================== *
   *                   PRINT ALLOCA OFFSET                              *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */

  MemCtrl.io.WriteIn(0) <> st_10.io.memReq

  st_10.io.memResp <> MemCtrl.io.WriteOut(0)

  MemCtrl.io.ReadIn(0) <> ld_17.io.memReq

  ld_17.io.memResp <> MemCtrl.io.ReadOut(0)

  MemCtrl.io.ReadIn(1) <> ld_21.io.memReq

  ld_21.io.memResp <> MemCtrl.io.ReadOut(1)

  MemCtrl.io.ReadIn(2) <> ld_25.io.memReq

  ld_25.io.memResp <> MemCtrl.io.ReadOut(2)

  MemCtrl.io.WriteIn(1) <> st_27.io.memReq

  st_27.io.memResp <> MemCtrl.io.WriteOut(1)

  MemCtrl.io.ReadIn(3) <> ld_44.io.memReq

  ld_44.io.memResp <> MemCtrl.io.ReadOut(3)

  MemCtrl.io.WriteIn(2) <> st_46.io.memReq

  st_46.io.memResp <> MemCtrl.io.WriteOut(2)

  MemCtrl.io.ReadIn(4) <> ld_53.io.memReq

  ld_53.io.memResp <> MemCtrl.io.ReadOut(4)

  MemCtrl.io.ReadIn(5) <> ld_56.io.memReq

  ld_56.io.memResp <> MemCtrl.io.ReadOut(5)

  MemCtrl.io.ReadIn(6) <> ld_60.io.memReq

  ld_60.io.memResp <> MemCtrl.io.ReadOut(6)

  MemCtrl.io.WriteIn(3) <> st_62.io.memReq

  st_62.io.memResp <> MemCtrl.io.WriteOut(3)



  /* ================================================================== *
   *                   PRINT SHARED CONNECTIONS                         *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  phi_i_01.io.InData(0) <> const0.io.Out

  phi_j_05.io.InData(0) <> const1.io.Out

  Gep_arrayidx79.io.idx(0) <> const2.io.Out

  phi_k_012.io.InData(0) <> const3.io.Out

  Gep_arrayidx1216.io.idx(0) <> const4.io.Out

  Gep_arrayidx1420.io.idx(0) <> const5.io.Out

  Gep_arrayidx1724.io.idx(0) <> const6.io.Out

  binaryOp_inc28.io.RightIO <> const7.io.Out

  binaryOp_inc1930.io.RightIO <> const8.io.Out

  binaryOp_inc2232.io.RightIO <> const9.io.Out

  phi_i_135.io.InData(0) <> const10.io.Out

  phi_j_139.io.InData(0) <> const11.io.Out

  Gep_arrayidx3143.io.idx(0) <> const12.io.Out

  phi_k_148.io.InData(0) <> const13.io.Out

  Gep_arrayidx3752.io.idx(0) <> const14.io.Out

  Gep_arrayidx3955.io.idx(0) <> const15.io.Out

  Gep_arrayidx4259.io.idx(0) <> const16.io.Out

  binaryOp_inc4563.io.RightIO <> const17.io.Out

  binaryOp_inc4865.io.RightIO <> const18.io.Out

  binaryOp_inc5167.io.RightIO <> const19.io.Out

  st_10.io.inData <> constf0.io.Out(0)

  icmp_cmp2.io.LeftIO <> phi_i_01.io.Out(0)

  binaryOp_inc2232.io.LeftIO <> phi_i_01.io.Out(2)

  br_3.io.CmpIO <> icmp_cmp2.io.Out(0)

  icmp_cmp56.io.LeftIO <> phi_j_05.io.Out(0)

  Gep_arrayidx79.io.idx(1) <> phi_j_05.io.Out(1)

  binaryOp_inc1930.io.LeftIO <> phi_j_05.io.Out(3)

  br_7.io.CmpIO <> icmp_cmp56.io.Out(0)

  Gep_arrayidx79.io.baseAddress <> Gep_arrayidx8.io.Out(0)

  st_10.io.GepAddr <> Gep_arrayidx79.io.Out(0)

  icmp_cmp913.io.LeftIO <> phi_k_012.io.Out(0)

  Gep_arrayidx1216.io.idx(1) <> phi_k_012.io.Out(1)

  Gep_arrayidx1319.io.idx(0) <> phi_k_012.io.Out(2)

  binaryOp_inc28.io.LeftIO <> phi_k_012.io.Out(3)

  br_14.io.CmpIO <> icmp_cmp913.io.Out(0)

  Gep_arrayidx1216.io.baseAddress <> Gep_arrayidx1115.io.Out(0)

  ld_17.io.GepAddr <> Gep_arrayidx1216.io.Out(0)

  binaryOp_mul18.io.RightIO <> ld_17.io.Out(0)

  binaryOp_mul1522.io.LeftIO <> binaryOp_mul18.io.Out(0)

  Gep_arrayidx1420.io.baseAddress <> Gep_arrayidx1319.io.Out(0)

  ld_21.io.GepAddr <> Gep_arrayidx1420.io.Out(0)

  binaryOp_mul1522.io.RightIO <> ld_21.io.Out(0)

  FP_add26.io.RightIO <> binaryOp_mul1522.io.Out(0)

  Gep_arrayidx1724.io.baseAddress <> Gep_arrayidx1623.io.Out(0)

  ld_25.io.GepAddr <> Gep_arrayidx1724.io.Out(0)

  st_27.io.GepAddr <> Gep_arrayidx1724.io.Out(1)

  FP_add26.io.LeftIO <> ld_25.io.Out(0)

  st_27.io.inData <> FP_add26.io.Out(0)

  phi_k_012.io.InData(1) <> binaryOp_inc28.io.Out(0)

  phi_j_05.io.InData(1) <> binaryOp_inc1930.io.Out(0)

  phi_i_01.io.InData(1) <> binaryOp_inc2232.io.Out(0)

  icmp_cmp2536.io.LeftIO <> phi_i_135.io.Out(0)

  binaryOp_inc5167.io.LeftIO <> phi_i_135.io.Out(2)

  br_37.io.CmpIO <> icmp_cmp2536.io.Out(0)

  icmp_cmp2840.io.LeftIO <> phi_j_139.io.Out(0)

  Gep_arrayidx3143.io.idx(1) <> phi_j_139.io.Out(1)

  binaryOp_inc4865.io.LeftIO <> phi_j_139.io.Out(3)

  br_41.io.CmpIO <> icmp_cmp2840.io.Out(0)

  Gep_arrayidx3143.io.baseAddress <> Gep_arrayidx3042.io.Out(0)

  ld_44.io.GepAddr <> Gep_arrayidx3143.io.Out(0)

  st_46.io.GepAddr <> Gep_arrayidx3143.io.Out(1)

  binaryOp_mul3245.io.LeftIO <> ld_44.io.Out(0)

  st_46.io.inData <> binaryOp_mul3245.io.Out(0)

  icmp_cmp3449.io.LeftIO <> phi_k_148.io.Out(0)

  Gep_arrayidx3752.io.idx(1) <> phi_k_148.io.Out(1)

  Gep_arrayidx3854.io.idx(0) <> phi_k_148.io.Out(2)

  binaryOp_inc4563.io.LeftIO <> phi_k_148.io.Out(3)

  br_50.io.CmpIO <> icmp_cmp3449.io.Out(0)

  Gep_arrayidx3752.io.baseAddress <> Gep_arrayidx3651.io.Out(0)

  ld_53.io.GepAddr <> Gep_arrayidx3752.io.Out(0)

  binaryOp_mul4057.io.LeftIO <> ld_53.io.Out(0)

  Gep_arrayidx3955.io.baseAddress <> Gep_arrayidx3854.io.Out(0)

  ld_56.io.GepAddr <> Gep_arrayidx3955.io.Out(0)

  binaryOp_mul4057.io.RightIO <> ld_56.io.Out(0)

  FP_add4361.io.RightIO <> binaryOp_mul4057.io.Out(0)

  Gep_arrayidx4259.io.baseAddress <> Gep_arrayidx4158.io.Out(0)

  ld_60.io.GepAddr <> Gep_arrayidx4259.io.Out(0)

  st_62.io.GepAddr <> Gep_arrayidx4259.io.Out(1)

  FP_add4361.io.LeftIO <> ld_60.io.Out(0)

  st_62.io.inData <> FP_add4361.io.Out(0)

  phi_k_148.io.InData(1) <> binaryOp_inc4563.io.Out(0)

  phi_j_139.io.InData(1) <> binaryOp_inc4865.io.Out(0)

  phi_i_135.io.InData(1) <> binaryOp_inc5167.io.Out(0)

  st_10.io.Out(0).ready := true.B

  st_27.io.Out(0).ready := true.B

  st_46.io.Out(0).ready := true.B

  st_62.io.Out(0).ready := true.B



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_69.io.Out

}

import java.io.{File, FileWriter}
object kernel_2mmMain extends App {
  val dir = new File("RTL/kernel_2mm") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new kernel_2mmDF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}
