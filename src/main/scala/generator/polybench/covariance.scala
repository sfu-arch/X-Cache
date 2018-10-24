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

abstract class kernel_covarianceDFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32, 32, 32, 32))))
    val MemResp = Flipped(Valid(new MemResp))
    val MemReq = Decoupled(new MemReq)
    val out = Decoupled(new Call(List()))
  })
}

class kernel_covarianceDF(implicit p: Parameters) extends kernel_covarianceDFIO()(p) {


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  val MemCtrl = Module(new UnifiedController(ID = 0, Size = 32, NReads = 5, NWrites = 8)
  (WControl = new WriteMemoryController(NumOps = 8, BaseSize = 2, NumEntries = 2))
  (RControl = new ReadMemoryController(NumOps = 5, BaseSize = 2, NumEntries = 2))
  (RWArbiter = new ReadWriteArbiter()))

  io.MemReq <> MemCtrl.io.MemReq
  MemCtrl.io.MemResp <> io.MemResp

  val SharedFPU = Module(new SharedFPU(NumOps = 2, PipeDepth = 32)(t = p(FTYP)))

  val InputSplitter = Module(new SplitCallNew(List(2, 3, 1, 2)))
  InputSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val Loop_0 = Module(new LoopBlockO1(NumIns = List(2, 1, 1, 1), NumOuts = 1, NumExits = 1, ID = 0))

  val Loop_1 = Module(new LoopBlockO1(NumIns = List(4, 2, 1, 1), NumOuts = 0, NumExits = 1, ID = 1))

  val Loop_2 = Module(new LoopBlockO1(NumIns = List(1, 1, 1), NumOuts = 0, NumExits = 1, ID = 2))

  val Loop_3 = Module(new LoopBlockO1(NumIns = List(1, 1, 1), NumOuts = 0, NumExits = 1, ID = 3))

  val Loop_4 = Module(new LoopBlockO1(NumIns = List(1, 1), NumOuts = 0, NumExits = 1, ID = 4))

  val Loop_5 = Module(new LoopBlockO1(NumIns = List(1, 1, 1), NumOuts = 1, NumExits = 1, ID = 5))

  val Loop_6 = Module(new LoopBlockO1(NumIns = List(1, 1, 1), NumOuts = 0, NumExits = 1, ID = 6))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_entry0 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 2, BID = 0))

  val bb_for_body1 = Module(new LoopFastHead(NumOuts = 7, NumPhi = 1, BID = 1))

  val bb_for_body32 = Module(new LoopFastHead(NumOuts = 13, NumPhi = 2, BID = 2))

  val bb_for_end3 = Module(new BasicBlockNode(NumInputs = 1, NumOuts = 8, NumPhi = 1, BID = 3))

  val bb_for_cond14_preheader_preheader4 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 2, BID = 4))

  val bb_for_cond14_preheader5 = Module(new LoopFastHead(NumOuts = 3, NumPhi = 1, BID = 5))

  val bb_for_cond26_preheader6 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 4, BID = 6))

  val bb_for_body167 = Module(new LoopFastHead(NumOuts = 14, NumPhi = 1, BID = 7))

  val bb_for_inc238 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 5, BID = 8))

  val bb_for_body31_preheader9 = Module(new LoopFastHead(NumOuts = 2, NumPhi = 1, BID = 9))

  val bb_for_body3110 = Module(new LoopFastHead(NumOuts = 9, NumPhi = 1, BID = 10))

  val bb_for_body3611 = Module(new LoopFastHead(NumOuts = 20, NumPhi = 2, BID = 11))

  val bb_for_end4612 = Module(new BasicBlockNode(NumInputs = 1, NumOuts = 12, NumPhi = 1, BID = 12))

  val bb_for_inc5813 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 5, BID = 13))

  val bb_for_end6014 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 14))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  br label %for.body
  val br_0 = Module(new UBranchNode(ID = 0))

  //  %j.010 = phi i32 [ 0, %entry ], [ %inc9, %for.end ]
  val phi_j_0101 = Module(new PhiFastNode2(NumInputs = 2, NumOutputs = 3, ID = 1))

  //  %arrayidx = getelementptr inbounds double, double* %mean, i32 %j.010
  val Gep_arrayidx2 = Module(new GepNode(NumIns = 1, NumOuts = 3, ID = 2)(ElementSize = 8, ArraySize = List()))

  //  store double 0.000000e+00, double* %arrayidx, align 4, !tbaa !2
  val st_3 = Module(new UnTypStore(NumPredOps = 0, NumSuccOps = 0, ID = 3, RouteID = 0))

  //  br label %for.body3
  val br_4 = Module(new UBranchNode(ID = 4))

  //  %0 = phi double [ 0.000000e+00, %for.body ], [ %add, %for.body3 ]
  val phi_5 = Module(new PhiFastNode2(NumInputs = 2, NumOutputs = 1, ID = 5))

  //  %i.09 = phi i32 [ 0, %for.body ], [ %inc, %for.body3 ]
  val phi_i_096 = Module(new PhiFastNode2(NumInputs = 2, NumOutputs = 2, ID = 6))

  //  %tmp = getelementptr [1200 x double], [1200 x double]* %data, i32 %i.09
  val Gep_tmp7 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 7)(ElementSize = 8, ArraySize = List()))

  //  %tmp1 = getelementptr [1200 x double], [1200 x double]* %tmp, i64 0, i32 %j.010
  val Gep_tmp18 = Module(new GepNode(NumIns = 2, NumOuts = 1, ID = 8)(ElementSize = 8, ArraySize = List()))

  //  %1 = load double, double* %tmp1, align 4, !tbaa !2
  val ld_9 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=9, RouteID=0))

  //  %add = fadd double %0, %1
  val FP_add10 = Module(new FPComputeNode(NumOuts = 3, ID = 10, opCode = "Add")(t = p(FTYP)))

  //  store double %add, double* %arrayidx, align 4, !tbaa !2
  val st_11 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, ID=11, RouteID=1))

  //  %inc = add nuw nsw i32 %i.09, 1
  val binaryOp_inc12 = Module(new ComputeNode(NumOuts = 2, ID = 12, opCode = "add")(sign = false))

  //  %exitcond16 = icmp eq i32 %inc, 1400
  val icmp_exitcond1613 = Module(new IcmpNode(NumOuts = 1, ID = 13, opCode = "eq")(sign = false))

  //  br i1 %exitcond16, label %for.end, label %for.body3
  val br_14 = Module(new CBranchFastNodeVariable2(NumFalse = 2, NumTrue = 1, ID = 14))

  //  %add.lcssa = phi double [ %add, %for.body3 ]
  //  val phi_add_lcssa15 = Module(new PhiFastNode2(NumInputs = 1, NumOutputs = 1, ID = 15))

  //  %div = fdiv double %add.lcssa, %float_n
    val FP_div16 = Module(new FPDivSqrtNode(NumOuts = 1, ID = 16, RouteID = 0, opCode = "DIV")(t = p(FTYP)))
//  val FP_div16 = Module(new ComputeNode(NumOuts = 1, ID = 16, opCode = "add")(sign = false))

  //  store double %div, double* %arrayidx, align 4, !tbaa !2
  val st_17 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, ID=17, RouteID=2))

  //  %inc9 = add nuw nsw i32 %j.010, 1
  val binaryOp_inc918 = Module(new ComputeNode(NumOuts = 2, ID = 18, opCode = "add")(sign = false))

  //  %exitcond17 = icmp eq i32 %inc9, 1200
  val icmp_exitcond1719 = Module(new IcmpNode(NumOuts = 1, ID = 19, opCode = "eq")(sign = false))

  //  br i1 %exitcond17, label %for.cond14.preheader.preheader, label %for.body
  val br_20 = Module(new CBranchFastNodeVariable2(NumTrue = 1, NumFalse = 2, ID = 20))

  //  br label %for.cond14.preheader
  val br_21 = Module(new UBranchNode(ID = 21))

  //  %i.18 = phi i32 [ %inc24, %for.inc23 ], [ 0, %for.cond14.preheader.preheader ]
  val phi_i_1822 = Module(new PhiFastNode2(NumInputs = 2, NumOutputs = 2, ID = 22))

  //  br label %for.body16
  val br_23 = Module(new UBranchNode(ID = 23))

  //  %sub47 = fadd double %float_n, -1.000000e+00
  val FP_sub4724 = Module(new FPComputeNode(NumOuts = 1, ID = 24, opCode = "Add")(t = p(FTYP)))

  //  br label %for.body31.preheader
  val br_25 = Module(new UBranchNode(ID = 25))

  //  %j.17 = phi i32 [ 0, %for.cond14.preheader ], [ %inc21, %for.body16 ]
  val phi_j_1726 = Module(new PhiFastNode2(NumInputs = 2, NumOutputs = 3, ID = 26))

  //  %arrayidx17 = getelementptr inbounds double, double* %mean, i32 %j.17
  val Gep_arrayidx1727 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 27)(ElementSize = 8, ArraySize = List()))

  //  %2 = load double, double* %arrayidx17, align 4, !tbaa !2
  val ld_28 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 28, RouteID = 1))

  //  %tmp2 = getelementptr [1200 x double], [1200 x double]* %data, i32 %i.18
  val Gep_tmp229 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 29)(ElementSize = 8, ArraySize = List()))

  //  %tmp3 = getelementptr [1200 x double], [1200 x double]* %tmp2, i64 0, i32 %j.17
  val Gep_tmp330 = Module(new GepNode(NumIns = 2, NumOuts = 2, ID = 30)(ElementSize = 8, ArraySize = List()))

  //  %3 = load double, double* %tmp3, align 4, !tbaa !2
  val ld_31 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 31, RouteID = 2))

  //  %sub = fsub double %3, %2
  val binaryOp_sub32 = Module(new ComputeNode(NumOuts = 1, ID = 32, opCode = "Sub")(sign = false))

  //  store double %sub, double* %tmp3, align 4, !tbaa !2
  val st_33 = Module(new UnTypStore(NumPredOps = 0, NumSuccOps = 0, ID = 33, RouteID = 3))

  //  %inc21 = add nuw nsw i32 %j.17, 1
  val binaryOp_inc2134 = Module(new ComputeNode(NumOuts = 2, ID = 34, opCode = "add")(sign = false))

  //  %exitcond14 = icmp eq i32 %inc21, 1200
  val icmp_exitcond1435 = Module(new IcmpNode(NumOuts = 1, ID = 35, opCode = "eq")(sign = false))

  //  br i1 %exitcond14, label %for.inc23, label %for.body16
  val br_36 = Module(new CBranchFastNodeVariable2(NumTrue = 1, NumFalse = 2, ID = 36))

  //  %inc24 = add nuw nsw i32 %i.18, 1
  val binaryOp_inc2437 = Module(new ComputeNode(NumOuts = 2, ID = 37, opCode = "add")(sign = false))

  //  %exitcond15 = icmp eq i32 %inc24, 1400
  val icmp_exitcond1538 = Module(new IcmpNode(NumOuts = 1, ID = 38, opCode = "eq")(sign = false))

  //  br i1 %exitcond15, label %for.cond26.preheader, label %for.cond14.preheader
  val br_39 = Module(new CBranchFastNodeVariable2(NumTrue = 1, NumFalse = 2, ID = 39))

  //  %i.25 = phi i32 [ 0, %for.cond26.preheader ], [ %inc59, %for.inc58 ]
  val phi_i_2540 = Module(new PhiFastNode2(NumInputs = 2, NumOutputs = 2, ID = 40))

  //  br label %for.body31
  val br_41 = Module(new UBranchNode(ID = 41))

  //  %j.24 = phi i32 [ %inc56, %for.end46 ], [ %i.25, %for.body31.preheader ]
  val phi_j_2442 = Module(new PhiFastNode2(NumInputs = 2, NumOutputs = 4, ID = 42))

  //  %tmp4 = getelementptr [1200 x double], [1200 x double]* %cov, i32 %i.25
  val Gep_tmp443 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 43)(ElementSize = 8, ArraySize = List()))

  //  %tmp5 = getelementptr [1200 x double], [1200 x double]* %tmp4, i64 0, i32 %j.24
  val Gep_tmp544 = Module(new GepNode(NumIns = 2, NumOuts = 3, ID = 44)(ElementSize = 8, ArraySize = List()))

  //  store double 0.000000e+00, double* %tmp5, align 4, !tbaa !2
  val st_45 = Module(new UnTypStore(NumPredOps = 0, NumSuccOps = 0, ID = 45, RouteID = 4))

  //  br label %for.body36
  val br_46 = Module(new UBranchNode(ID = 46))

  //  %4 = phi double [ 0.000000e+00, %for.body31 ], [ %add43, %for.body36 ]
  val phi_47 = Module(new PhiFastNode2(NumInputs = 2, NumOutputs = 1, ID = 47))

  //  %k.02 = phi i32 [ 0, %for.body31 ], [ %inc45, %for.body36 ]
  val phi_k_0248 = Module(new PhiFastNode2(NumInputs = 2, NumOutputs = 3, ID = 48))

  //  %tmp6 = getelementptr [1200 x double], [1200 x double]* %data, i32 %k.02
  val Gep_tmp649 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 49)(ElementSize = 8, ArraySize = List()))

  //  %tmp7 = getelementptr [1200 x double], [1200 x double]* %tmp6, i64 0, i32 %i.25
  val Gep_tmp750 = Module(new GepNode(NumIns = 2, NumOuts = 1, ID = 50)(ElementSize = 8, ArraySize = List()))

  //  %5 = load double, double* %tmp7, align 4, !tbaa !2
  val ld_51 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 51, RouteID = 3))

  //  %tmp8 = getelementptr [1200 x double], [1200 x double]* %data, i32 %k.02
  val Gep_tmp852 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 52)(ElementSize = 8, ArraySize = List()))

  //  %tmp9 = getelementptr [1200 x double], [1200 x double]* %tmp8, i64 0, i32 %j.24
  val Gep_tmp953 = Module(new GepNode(NumIns = 2, NumOuts = 1, ID = 53)(ElementSize = 8, ArraySize = List()))

  //  %6 = load double, double* %tmp9, align 4, !tbaa !2
  val ld_54 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 54, RouteID = 4))

  //  %mul = fmul double %5, %6
  val binaryOp_mul55 = Module(new ComputeNode(NumOuts = 1, ID = 55, opCode = "Mul")(sign = false))

  //  %add43 = fadd double %4, %mul
  val FP_add4356 = Module(new FPComputeNode(NumOuts = 3, ID = 56, opCode = "Add")(t = p(FTYP)))

  //  store double %add43, double* %tmp5, align 4, !tbaa !2
  val st_57 = Module(new UnTypStore(NumPredOps = 0, NumSuccOps = 0, ID = 57, RouteID = 5))

  //  %inc45 = add nuw nsw i32 %k.02, 1
  val binaryOp_inc4558 = Module(new ComputeNode(NumOuts = 2, ID = 58, opCode = "add")(sign = false))

  //  %exitcond = icmp eq i32 %inc45, 1400
  val icmp_exitcond59 = Module(new IcmpNode(NumOuts = 1, ID = 59, opCode = "eq")(sign = false))

  //  br i1 %exitcond, label %for.end46, label %for.body36
  val br_60 = Module(new CBranchFastNodeVariable2(NumTrue = 1, NumFalse = 2, ID = 60))

  //  %add43.lcssa = phi double [ %add43, %for.body36 ]
  //  val phi_add43_lcssa61 = Module(new PhiFastNode2(NumInputs = 1, NumOutputs = 1, ID = 61))

  //  %div50 = fdiv double %add43.lcssa, %sub47
  val FP_div5062 = Module(new FPDivSqrtNode(NumOuts = 2, ID = 62, RouteID = 0, opCode = "DIV")(t = p(FTYP)))

  //  store double %div50, double* %tmp5, align 4, !tbaa !2
  val st_63 = Module(new UnTypStore(NumPredOps = 0, NumSuccOps = 0, ID = 63, RouteID = 6))

  //  %tmp10 = getelementptr [1200 x double], [1200 x double]* %cov, i32 %j.24
  val Gep_tmp1064 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 64)(ElementSize = 8, ArraySize = List()))

  //  %tmp11 = getelementptr [1200 x double], [1200 x double]* %tmp10, i64 0, i32 %i.25
  val Gep_tmp1165 = Module(new GepNode(NumIns = 2, NumOuts = 1, ID = 65)(ElementSize = 8, ArraySize = List()))

  //  store double %div50, double* %tmp11, align 4, !tbaa !2
  val st_66 = Module(new UnTypStore(NumPredOps = 0, NumSuccOps = 0, ID = 66, RouteID = 7))

  //  %inc56 = add nuw nsw i32 %j.24, 1
  val binaryOp_inc5667 = Module(new ComputeNode(NumOuts = 2, ID = 67, opCode = "add")(sign = false))

  //  %exitcond11 = icmp eq i32 %inc56, 1200
  val icmp_exitcond1168 = Module(new IcmpNode(NumOuts = 1, ID = 68, opCode = "eq")(sign = false))

  //  br i1 %exitcond11, label %for.inc58, label %for.body31
  val br_69 = Module(new CBranchFastNodeVariable2(NumTrue = 1, NumFalse = 2, ID = 69))

  //  %inc59 = add nuw nsw i32 %i.25, 1
  val binaryOp_inc5970 = Module(new ComputeNode(NumOuts = 2, ID = 70, opCode = "add")(sign = false))

  //  %exitcond13 = icmp eq i32 %inc59, 1200
  val icmp_exitcond1371 = Module(new IcmpNode(NumOuts = 1, ID = 71, opCode = "eq")(sign = false))

  //  br i1 %exitcond13, label %for.end60, label %for.body31.preheader
  val br_72 = Module(new CBranchFastNodeVariable2(NumTrue = 1, NumFalse = 2, ID = 72))

  //  ret void
  val ret_73 = Module(new RetNode2(retTypes = List(), ID = 73))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i32 0
  val const0 = Module(new ConstFastNode(value = 0, ID = 0))

  //i32 0
  val const1 = Module(new ConstFastNode(value = 0, ID = 1))

  //i64 0
  val const2 = Module(new ConstFastNode(value = 0, ID = 2))

  //i32 1
  val const3 = Module(new ConstFastNode(value = 1, ID = 3))

  //i32 1400
//  val const4 = Module(new ConstFastNode(value = 1400, ID = 4))
  val const4 = Module(new ConstFastNode(value = 14, ID = 4))

  //i32 1
  val const5 = Module(new ConstFastNode(value = 1, ID = 5))

  //i32 1200
//  val const6 = Module(new ConstFastNode(value = 1200, ID = 6))
  val const6 = Module(new ConstFastNode(value = 12, ID = 6))

  //i32 0
  val const7 = Module(new ConstFastNode(value = 0, ID = 7))

  //i32 0
  val const8 = Module(new ConstFastNode(value = 0, ID = 8))

  //i64 0
  val const9 = Module(new ConstFastNode(value = 0, ID = 9))

  //i32 1
  val const10 = Module(new ConstFastNode(value = 1, ID = 10))

  //i32 1200
//  val const11 = Module(new ConstFastNode(value = 1200, ID = 11))
  val const11 = Module(new ConstFastNode(value = 12, ID = 11))

  //i32 1
  val const12 = Module(new ConstFastNode(value = 1, ID = 12))

  //i32 1400
//  val const13 = Module(new ConstFastNode(value = 1400, ID = 13))
  val const13 = Module(new ConstFastNode(value = 14, ID = 13))

  //i32 0
  val const14 = Module(new ConstFastNode(value = 0, ID = 14))

  //i64 0
  val const15 = Module(new ConstFastNode(value = 0, ID = 15))

  //i32 0
  val const16 = Module(new ConstFastNode(value = 0, ID = 16))

  //i64 0
  val const17 = Module(new ConstFastNode(value = 0, ID = 17))

  //i64 0
  val const18 = Module(new ConstFastNode(value = 0, ID = 18))

  //i32 1
  val const19 = Module(new ConstFastNode(value = 1, ID = 19))

  //i32 1400
//  val const20 = Module(new ConstFastNode(value = 1400, ID = 20))
  val const20 = Module(new ConstFastNode(value = 14, ID = 20))

  //i64 0
  val const21 = Module(new ConstFastNode(value = 0, ID = 21))

  //i32 1
  val const22 = Module(new ConstFastNode(value = 1, ID = 22))

  //i32 1200
//  val const23 = Module(new ConstFastNode(value = 1200, ID = 23))
  val const23 = Module(new ConstFastNode(value = 12, ID = 23))

  //i32 1
  val const24 = Module(new ConstFastNode(value = 1, ID = 24))

  //i32 1200
//  val const25 = Module(new ConstFastNode(value = 1200, ID = 25))
  val const25 = Module(new ConstFastNode(value = 12, ID = 25))

  //double 0.000000e+00
  val constf0 = Module(new ConstNode(value = 0x0, ID = 0))

  //double 0.000000e+00
  val constf1 = Module(new ConstNode(value = 0x0, ID = 1))

  //double -1.000000e+00
  val constf2 = Module(new ConstNode(value = 0xbf800000, ID = 2))

  //double 0.000000e+00
  val constf3 = Module(new ConstNode(value = 0x0, ID = 3))

  //double 0.000000e+00
  val constf4 = Module(new ConstNode(value = 0x0, ID = 4))



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_entry0.io.predicateIn(0) <> InputSplitter.io.Out.enable

  bb_for_body1.io.activate <> Loop_6.io.activate

  bb_for_body1.io.loopBack <> br_20.io.FalseOutput(0)

  bb_for_body32.io.activate <> Loop_5.io.activate

  bb_for_body32.io.loopBack <> br_14.io.FalseOutput(0)

  bb_for_end3.io.predicateIn(0) <> Loop_5.io.endEnable

  bb_for_cond14_preheader_preheader4.io.predicateIn(0) <> Loop_6.io.endEnable

  bb_for_cond14_preheader5.io.activate <> Loop_4.io.activate

  bb_for_cond14_preheader5.io.loopBack <> br_39.io.FalseOutput(0)

  bb_for_cond26_preheader6.io.predicateIn(0) <> Loop_4.io.endEnable

  bb_for_body167.io.activate <> Loop_3.io.activate

  bb_for_body167.io.loopBack <> br_36.io.FalseOutput(0)

  bb_for_inc238.io.predicateIn(0) <> Loop_3.io.endEnable

  bb_for_body31_preheader9.io.activate <> Loop_2.io.activate

  bb_for_body31_preheader9.io.loopBack <> br_72.io.FalseOutput(0)

  bb_for_body3110.io.activate <> Loop_1.io.activate

  bb_for_body3110.io.loopBack <> br_69.io.FalseOutput(0)

  bb_for_body3611.io.activate <> Loop_0.io.activate

  bb_for_body3611.io.loopBack <> br_60.io.FalseOutput(0)

  bb_for_end4612.io.predicateIn(0) <> Loop_0.io.endEnable

  bb_for_inc5813.io.predicateIn(0) <> Loop_1.io.endEnable

  bb_for_end6014.io.predicateIn(0) <> Loop_2.io.endEnable



  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */

  Loop_0.io.enable <> br_46.io.Out(0)

  Loop_0.io.latchEnable <> br_60.io.FalseOutput(1)

  Loop_0.io.loopExit(0) <> br_60.io.TrueOutput(0)

  Loop_1.io.enable <> br_41.io.Out(0)

  Loop_1.io.latchEnable <> br_69.io.FalseOutput(1)

  Loop_1.io.loopExit(0) <> br_69.io.TrueOutput(0)

  Loop_2.io.enable <> br_25.io.Out(0)

  Loop_2.io.latchEnable <> br_72.io.FalseOutput(1)

  Loop_2.io.loopExit(0) <> br_72.io.TrueOutput(0)

  Loop_3.io.enable <> br_23.io.Out(0)

  Loop_3.io.latchEnable <> br_36.io.FalseOutput(1)

  Loop_3.io.loopExit(0) <> br_36.io.TrueOutput(0)

  Loop_4.io.enable <> br_21.io.Out(0)

  Loop_4.io.latchEnable <> br_39.io.FalseOutput(1)

  Loop_4.io.loopExit(0) <> br_39.io.TrueOutput(0)

  Loop_5.io.enable <> br_4.io.Out(0)

  Loop_5.io.latchEnable <> br_14.io.FalseOutput(1)

  Loop_5.io.loopExit(0) <> br_14.io.TrueOutput(0)

  Loop_6.io.enable <> br_0.io.Out(0)

  Loop_6.io.latchEnable <> br_20.io.FalseOutput(1)

  Loop_6.io.loopExit(0) <> br_20.io.TrueOutput(0)



  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */

  //  br_46.io.PredOp(0) <> st_45.io.SuccOp(0)

  //  br_4.io.PredOp(0) <> st_3.io.SuccOp(0)



  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

  Loop_0.io.In(0) <> Loop_1.io.liveIn.data("field2")(0)

  Loop_0.io.In(1) <> Loop_1.io.liveIn.data("field0")(2)

  Loop_0.io.In(2) <> phi_j_2442.io.Out(1)

  Loop_0.io.In(3) <> Gep_tmp544.io.Out(1)

  Loop_1.io.In(0) <> phi_i_2540.io.Out(0)

  Loop_1.io.In(1) <> Loop_2.io.liveIn.data("field0")(0)

  Loop_1.io.In(2) <> Loop_2.io.liveIn.data("field1")(0)

  Loop_1.io.In(3) <> Loop_2.io.liveIn.data("field2")(0)

  Loop_2.io.In(0) <> InputSplitter.io.Out.data("field2")(0)

  Loop_2.io.In(1) <> InputSplitter.io.Out.data("field1")(2)

  Loop_2.io.In(2) <> FP_sub4724.io.Out(0)

  Loop_3.io.In(0) <> Loop_4.io.liveIn.data("field0")(0)

  Loop_3.io.In(1) <> Loop_4.io.liveIn.data("field1")(0)

  Loop_3.io.In(2) <> phi_i_1822.io.Out(0)

  Loop_4.io.In(0) <> InputSplitter.io.Out.data("field3")(1)

  Loop_4.io.In(1) <> InputSplitter.io.Out.data("field1")(1)

  Loop_5.io.In(0) <> Loop_6.io.liveIn.data("field1")(0)

  Loop_5.io.In(1) <> phi_j_0101.io.Out(1)

  Loop_5.io.In(2) <> Gep_arrayidx2.io.Out(1)

  Loop_6.io.In(0) <> InputSplitter.io.Out.data("field3")(0)

  Loop_6.io.In(1) <> InputSplitter.io.Out.data("field1")(0)

  Loop_6.io.In(2) <> InputSplitter.io.Out.data("field0")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */

  Gep_tmp649.io.baseAddress <> Loop_0.io.liveIn.data("field0")(0)

  Gep_tmp852.io.baseAddress <> Loop_0.io.liveIn.data("field0")(1)

  Gep_tmp750.io.idx(1) <> Loop_0.io.liveIn.data("field1")(0)

  Gep_tmp953.io.idx(1) <> Loop_0.io.liveIn.data("field2")(0)

  st_57.io.GepAddr <> Loop_0.io.liveIn.data("field3")(0)

  phi_j_2442.io.InData(0) <> Loop_1.io.liveIn.data("field0")(0)

  Gep_tmp443.io.idx(0) <> Loop_1.io.liveIn.data("field0")(1)

  Gep_tmp1165.io.idx(1) <> Loop_1.io.liveIn.data("field0")(3)

  Gep_tmp443.io.baseAddress <> Loop_1.io.liveIn.data("field1")(0)

  Gep_tmp1064.io.baseAddress <> Loop_1.io.liveIn.data("field1")(1)

  FP_div5062.io.b <> Loop_1.io.liveIn.data("field3")(0)

  Gep_arrayidx1727.io.baseAddress <> Loop_3.io.liveIn.data("field0")(0)

  Gep_tmp229.io.baseAddress <> Loop_3.io.liveIn.data("field1")(0)

  Gep_tmp229.io.idx(0) <> Loop_3.io.liveIn.data("field2")(0)

  Gep_tmp7.io.baseAddress <> Loop_5.io.liveIn.data("field0")(0)

  Gep_tmp18.io.idx(1) <> Loop_5.io.liveIn.data("field1")(0)

  st_11.io.GepAddr <> Loop_5.io.liveIn.data("field2")(0)

  Gep_arrayidx2.io.baseAddress <> Loop_6.io.liveIn.data("field0")(0)

  FP_div16.io.b <> Loop_6.io.liveIn.data("field2")(0)
//  FP_div16.io.RightIO <> Loop_6.io.liveIn.data("field2")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */

  Loop_0.io.liveOut(0) <> FP_add4356.io.Out(2)

  Loop_5.io.liveOut(0) <> FP_add10.io.Out(2)



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  br_0.io.enable <> bb_entry0.io.Out(0)
  const0.io.enable <> bb_entry0.io.Out(1)


  constf0.io.enable <> bb_for_body1.io.Out(0)

  phi_j_0101.io.enable <> bb_for_body1.io.Out(1)

  Gep_arrayidx2.io.enable <> bb_for_body1.io.Out(2)

  st_3.io.enable <> bb_for_body1.io.Out(3)

  br_4.io.enable <> bb_for_body1.io.Out(4)

  constf1.io.enable <> bb_for_body1.io.Out(5)

  const1.io.enable <> bb_for_body1.io.Out(6)

  const2.io.enable <> bb_for_body32.io.Out(0)

  const3.io.enable <> bb_for_body32.io.Out(1)

  const4.io.enable <> bb_for_body32.io.Out(2)

  phi_5.io.enable <> bb_for_body32.io.Out(3)

  phi_i_096.io.enable <> bb_for_body32.io.Out(4)

  Gep_tmp7.io.enable <> bb_for_body32.io.Out(5)

  Gep_tmp18.io.enable <> bb_for_body32.io.Out(6)

  ld_9.io.enable <> bb_for_body32.io.Out(7)

  FP_add10.io.enable <> bb_for_body32.io.Out(8)

  st_11.io.enable <> bb_for_body32.io.Out(9)

  binaryOp_inc12.io.enable <> bb_for_body32.io.Out(10)

  icmp_exitcond1613.io.enable <> bb_for_body32.io.Out(11)

  br_14.io.enable <> bb_for_body32.io.Out(12)


  const5.io.enable <> bb_for_end3.io.Out(0)

  const6.io.enable <> bb_for_end3.io.Out(1)

//  phi_add_lcssa15.io.enable <> bb_for_end3.io.Out(2)
  bb_for_end3.io.Out(2).ready := true.B

  FP_div16.io.enable <> bb_for_end3.io.Out(3)

  st_17.io.enable <> bb_for_end3.io.Out(4)

  binaryOp_inc918.io.enable <> bb_for_end3.io.Out(5)

  icmp_exitcond1719.io.enable <> bb_for_end3.io.Out(6)

  br_20.io.enable <> bb_for_end3.io.Out(7)


  br_21.io.enable <> bb_for_cond14_preheader_preheader4.io.Out(0)

  const7.io.enable <> bb_for_cond14_preheader_preheader4.io.Out(1)

  phi_i_1822.io.enable <> bb_for_cond14_preheader5.io.Out(0)

  br_23.io.enable <> bb_for_cond14_preheader5.io.Out(1)


  constf2.io.enable <> bb_for_cond26_preheader6.io.Out(0)

  const14.io.enable <> bb_for_cond26_preheader6.io.Out(1)

  FP_sub4724.io.enable <> bb_for_cond26_preheader6.io.Out(2)

  br_25.io.enable <> bb_for_cond26_preheader6.io.Out(3)

  const8.io.enable <> bb_for_cond14_preheader5.io.Out(2)

  const9.io.enable <> bb_for_body167.io.Out(0)

  const10.io.enable <> bb_for_body167.io.Out(1)

  const11.io.enable <> bb_for_body167.io.Out(2)

  phi_j_1726.io.enable <> bb_for_body167.io.Out(3)

  Gep_arrayidx1727.io.enable <> bb_for_body167.io.Out(4)

  ld_28.io.enable <> bb_for_body167.io.Out(5)

  Gep_tmp229.io.enable <> bb_for_body167.io.Out(6)

  Gep_tmp330.io.enable <> bb_for_body167.io.Out(7)

  ld_31.io.enable <> bb_for_body167.io.Out(8)

  binaryOp_sub32.io.enable <> bb_for_body167.io.Out(9)

  st_33.io.enable <> bb_for_body167.io.Out(10)

  binaryOp_inc2134.io.enable <> bb_for_body167.io.Out(11)

  icmp_exitcond1435.io.enable <> bb_for_body167.io.Out(12)

  br_36.io.enable <> bb_for_body167.io.Out(13)


  const12.io.enable <> bb_for_inc238.io.Out(0)

  const13.io.enable <> bb_for_inc238.io.Out(1)

  binaryOp_inc2437.io.enable <> bb_for_inc238.io.Out(2)

  icmp_exitcond1538.io.enable <> bb_for_inc238.io.Out(3)

  br_39.io.enable <> bb_for_inc238.io.Out(4)


  phi_i_2540.io.enable <> bb_for_body31_preheader9.io.Out(0)

  br_41.io.enable <> bb_for_body31_preheader9.io.Out(1)


  constf3.io.enable <> bb_for_body3110.io.Out(1)

  const15.io.enable <> bb_for_body3110.io.Out(0)

  phi_j_2442.io.enable <> bb_for_body3110.io.Out(2)

  Gep_tmp443.io.enable <> bb_for_body3110.io.Out(3)

  Gep_tmp544.io.enable <> bb_for_body3110.io.Out(4)

  st_45.io.enable <> bb_for_body3110.io.Out(5)

  br_46.io.enable <> bb_for_body3110.io.Out(6)


//  constf4.io.enable <> bb_for_body3611.io.Out(0)
  constf4.io.enable <> bb_for_body3110.io.Out(7)
  bb_for_body3611.io.Out(0).ready := true.B

//  const16.io.enable <> bb_for_body3611.io.Out(1)
  const16.io.enable <> bb_for_body3110.io.Out(8)
  bb_for_body3611.io.Out(1).ready := true.B

  const17.io.enable <> bb_for_body3611.io.Out(2)

  const18.io.enable <> bb_for_body3611.io.Out(3)

  const19.io.enable <> bb_for_body3611.io.Out(4)

  const20.io.enable <> bb_for_body3611.io.Out(5)

  phi_47.io.enable <> bb_for_body3611.io.Out(6)

  phi_k_0248.io.enable <> bb_for_body3611.io.Out(7)

  Gep_tmp649.io.enable <> bb_for_body3611.io.Out(8)

  Gep_tmp750.io.enable <> bb_for_body3611.io.Out(9)

  ld_51.io.enable <> bb_for_body3611.io.Out(10)

  Gep_tmp852.io.enable <> bb_for_body3611.io.Out(11)

  Gep_tmp953.io.enable <> bb_for_body3611.io.Out(12)

  ld_54.io.enable <> bb_for_body3611.io.Out(13)

  binaryOp_mul55.io.enable <> bb_for_body3611.io.Out(14)

  FP_add4356.io.enable <> bb_for_body3611.io.Out(15)

  st_57.io.enable <> bb_for_body3611.io.Out(16)

  binaryOp_inc4558.io.enable <> bb_for_body3611.io.Out(17)

  icmp_exitcond59.io.enable <> bb_for_body3611.io.Out(18)

  br_60.io.enable <> bb_for_body3611.io.Out(19)


  const21.io.enable <> bb_for_end4612.io.Out(0)

  const22.io.enable <> bb_for_end4612.io.Out(1)

  const23.io.enable <> bb_for_end4612.io.Out(2)

//  phi_add43_lcssa61.io.enable <> bb_for_end4612.io.Out(3)
  bb_for_end4612.io.Out(3).ready := true.B

  FP_div5062.io.enable <> bb_for_end4612.io.Out(4)

  st_63.io.enable <> bb_for_end4612.io.Out(5)

  Gep_tmp1064.io.enable <> bb_for_end4612.io.Out(6)

  Gep_tmp1165.io.enable <> bb_for_end4612.io.Out(7)

  st_66.io.enable <> bb_for_end4612.io.Out(8)

  binaryOp_inc5667.io.enable <> bb_for_end4612.io.Out(9)

  icmp_exitcond1168.io.enable <> bb_for_end4612.io.Out(10)

  br_69.io.enable <> bb_for_end4612.io.Out(11)


  const24.io.enable <> bb_for_inc5813.io.Out(0)

  const25.io.enable <> bb_for_inc5813.io.Out(1)

  binaryOp_inc5970.io.enable <> bb_for_inc5813.io.Out(2)

  icmp_exitcond1371.io.enable <> bb_for_inc5813.io.Out(3)

  br_72.io.enable <> bb_for_inc5813.io.Out(4)


  ret_73.io.In.enable <> bb_for_end6014.io.Out(0)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  phi_j_0101.io.Mask <> bb_for_body1.io.MaskBB(0)

  phi_5.io.Mask <> bb_for_body32.io.MaskBB(0)

  phi_i_096.io.Mask <> bb_for_body32.io.MaskBB(1)

//  phi_add_lcssa15.io.Mask <> bb_for_end3.io.MaskBB(0)
  bb_for_end3.io.MaskBB(0).ready := true.B

  phi_i_1822.io.Mask <> bb_for_cond14_preheader5.io.MaskBB(0)

  phi_j_1726.io.Mask <> bb_for_body167.io.MaskBB(0)

  phi_i_2540.io.Mask <> bb_for_body31_preheader9.io.MaskBB(0)

  phi_j_2442.io.Mask <> bb_for_body3110.io.MaskBB(0)

  phi_47.io.Mask <> bb_for_body3611.io.MaskBB(0)

  phi_k_0248.io.Mask <> bb_for_body3611.io.MaskBB(1)

  //phi_add43_lcssa61.io.Mask <> bb_for_end4612.io.MaskBB(0)
  bb_for_end4612.io.MaskBB(0).ready := true.B



  /* ================================================================== *
   *                   PRINT ALLOCA OFFSET                              *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */

  MemCtrl.io.WriteIn(0) <> st_3.io.memReq

  st_3.io.memResp <> MemCtrl.io.WriteOut(0)

  MemCtrl.io.ReadIn(0) <> ld_9.io.memReq

  ld_9.io.memResp <> MemCtrl.io.ReadOut(0)

  MemCtrl.io.WriteIn(1) <> st_11.io.memReq

  st_11.io.memResp <> MemCtrl.io.WriteOut(1)

  MemCtrl.io.WriteIn(2) <> st_17.io.memReq

  st_17.io.memResp <> MemCtrl.io.WriteOut(2)

  MemCtrl.io.ReadIn(1) <> ld_28.io.memReq

  ld_28.io.memResp <> MemCtrl.io.ReadOut(1)

  MemCtrl.io.ReadIn(2) <> ld_31.io.memReq

  ld_31.io.memResp <> MemCtrl.io.ReadOut(2)

  MemCtrl.io.WriteIn(3) <> st_33.io.memReq

  st_33.io.memResp <> MemCtrl.io.WriteOut(3)

  MemCtrl.io.WriteIn(4) <> st_45.io.memReq

  st_45.io.memResp <> MemCtrl.io.WriteOut(4)

  MemCtrl.io.ReadIn(3) <> ld_51.io.memReq

  ld_51.io.memResp <> MemCtrl.io.ReadOut(3)

  MemCtrl.io.ReadIn(4) <> ld_54.io.memReq

  ld_54.io.memResp <> MemCtrl.io.ReadOut(4)

  MemCtrl.io.WriteIn(5) <> st_57.io.memReq

  st_57.io.memResp <> MemCtrl.io.WriteOut(5)

  MemCtrl.io.WriteIn(6) <> st_63.io.memReq

  st_63.io.memResp <> MemCtrl.io.WriteOut(6)

  MemCtrl.io.WriteIn(7) <> st_66.io.memReq

  st_66.io.memResp <> MemCtrl.io.WriteOut(7)



  /* ================================================================== *
   *                   PRINT SHARED CONNECTIONS                         *
   * ================================================================== */

  SharedFPU.io.InData(0) <> FP_div16.io.FUReq
  FP_div16.io.FUResp <> SharedFPU.io.OutData(0)
  SharedFPU.io.InData(1) <> FP_div5062.io.FUReq
  FP_div5062.io.FUResp <> SharedFPU.io.OutData(1)

  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  phi_j_0101.io.InData(0) <> const0.io.Out

  phi_i_096.io.InData(0) <> const1.io.Out

  Gep_tmp18.io.idx(0) <> const2.io.Out

  binaryOp_inc12.io.RightIO <> const3.io.Out

  icmp_exitcond1613.io.RightIO <> const4.io.Out

  binaryOp_inc918.io.RightIO <> const5.io.Out

  icmp_exitcond1719.io.RightIO <> const6.io.Out

  phi_i_1822.io.InData(0) <> const7.io.Out

  phi_j_1726.io.InData(0) <> const8.io.Out

  Gep_tmp330.io.idx(0) <> const9.io.Out

  binaryOp_inc2134.io.RightIO <> const10.io.Out

  icmp_exitcond1435.io.RightIO <> const11.io.Out

  binaryOp_inc2437.io.RightIO <> const12.io.Out

  icmp_exitcond1538.io.RightIO <> const13.io.Out

  phi_i_2540.io.InData(0) <> const14.io.Out

  Gep_tmp544.io.idx(0) <> const15.io.Out

  phi_k_0248.io.InData(0) <> const16.io.Out

  Gep_tmp750.io.idx(0) <> const17.io.Out

  Gep_tmp953.io.idx(0) <> const18.io.Out

  binaryOp_inc4558.io.RightIO <> const19.io.Out

  icmp_exitcond59.io.RightIO <> const20.io.Out

  Gep_tmp1165.io.idx(0) <> const21.io.Out

  binaryOp_inc5667.io.RightIO <> const22.io.Out

  icmp_exitcond1168.io.RightIO <> const23.io.Out

  binaryOp_inc5970.io.RightIO <> const24.io.Out

  icmp_exitcond1371.io.RightIO <> const25.io.Out

  st_3.io.inData <> constf0.io.Out(0)

  phi_5.io.InData(0) <> constf1.io.Out(0)

  FP_sub4724.io.RightIO <> constf2.io.Out(0)

  st_45.io.inData <> constf3.io.Out(0)

  phi_47.io.InData(0) <> constf4.io.Out(0)

  Gep_arrayidx2.io.idx(0) <> phi_j_0101.io.Out(0)

  binaryOp_inc918.io.LeftIO <> phi_j_0101.io.Out(2)

  st_3.io.GepAddr <> Gep_arrayidx2.io.Out(0)

  st_17.io.GepAddr <> Gep_arrayidx2.io.Out(2)

  FP_add10.io.LeftIO <> phi_5.io.Out(0)

  Gep_tmp7.io.idx(0) <> phi_i_096.io.Out(0)

  binaryOp_inc12.io.LeftIO <> phi_i_096.io.Out(1)

  Gep_tmp18.io.baseAddress <> Gep_tmp7.io.Out(0)

  ld_9.io.GepAddr <> Gep_tmp18.io.Out(0)

  FP_add10.io.RightIO <> ld_9.io.Out(0)

  phi_5.io.InData(1) <> FP_add10.io.Out(0)

  st_11.io.inData <> FP_add10.io.Out(1)

  phi_i_096.io.InData(1) <> binaryOp_inc12.io.Out(0)

  icmp_exitcond1613.io.LeftIO <> binaryOp_inc12.io.Out(1)

  br_14.io.CmpIO <> icmp_exitcond1613.io.Out(0)

//  FP_div16.io.a <> phi_add_lcssa15.io.Out(0)
  FP_div16.io.a <> Loop_5.io.Out(0)
//  FP_div16.io.LeftIO <> phi_add_lcssa15.io.Out(0)
//  phi_add_lcssa15.io.InData(0) <> Loop_5.io.Out(0)
//  FP_div16.io.LeftIO <> Loop_5.io.Out(0)

  st_17.io.inData <> FP_div16.io.Out(0)

  phi_j_0101.io.InData(1) <> binaryOp_inc918.io.Out(0)

  icmp_exitcond1719.io.LeftIO <> binaryOp_inc918.io.Out(1)

  br_20.io.CmpIO <> icmp_exitcond1719.io.Out(0)

  binaryOp_inc2437.io.LeftIO <> phi_i_1822.io.Out(1)

  Gep_arrayidx1727.io.idx(0) <> phi_j_1726.io.Out(0)

  Gep_tmp330.io.idx(1) <> phi_j_1726.io.Out(1)

  binaryOp_inc2134.io.LeftIO <> phi_j_1726.io.Out(2)

  ld_28.io.GepAddr <> Gep_arrayidx1727.io.Out(0)

  binaryOp_sub32.io.RightIO <> ld_28.io.Out(0)

  Gep_tmp330.io.baseAddress <> Gep_tmp229.io.Out(0)

  ld_31.io.GepAddr <> Gep_tmp330.io.Out(0)

  st_33.io.GepAddr <> Gep_tmp330.io.Out(1)

  binaryOp_sub32.io.LeftIO <> ld_31.io.Out(0)

  st_33.io.inData <> binaryOp_sub32.io.Out(0)

  phi_j_1726.io.InData(1) <> binaryOp_inc2134.io.Out(0)

  icmp_exitcond1435.io.LeftIO <> binaryOp_inc2134.io.Out(1)

  br_36.io.CmpIO <> icmp_exitcond1435.io.Out(0)

  phi_i_1822.io.InData(1) <> binaryOp_inc2437.io.Out(0)

  icmp_exitcond1538.io.LeftIO <> binaryOp_inc2437.io.Out(1)

  br_39.io.CmpIO <> icmp_exitcond1538.io.Out(0)

  binaryOp_inc5970.io.LeftIO <> phi_i_2540.io.Out(1)

  Gep_tmp544.io.idx(1) <> phi_j_2442.io.Out(0)

  Gep_tmp1064.io.idx(0) <> phi_j_2442.io.Out(2)

  binaryOp_inc5667.io.LeftIO <> phi_j_2442.io.Out(3)

  Gep_tmp544.io.baseAddress <> Gep_tmp443.io.Out(0)

  st_45.io.GepAddr <> Gep_tmp544.io.Out(0)

  st_63.io.GepAddr <> Gep_tmp544.io.Out(2)

  FP_add4356.io.LeftIO <> phi_47.io.Out(0)

  Gep_tmp649.io.idx(0) <> phi_k_0248.io.Out(0)

  Gep_tmp852.io.idx(0) <> phi_k_0248.io.Out(1)

  binaryOp_inc4558.io.LeftIO <> phi_k_0248.io.Out(2)

  Gep_tmp750.io.baseAddress <> Gep_tmp649.io.Out(0)

  ld_51.io.GepAddr <> Gep_tmp750.io.Out(0)

  binaryOp_mul55.io.LeftIO <> ld_51.io.Out(0)

  Gep_tmp953.io.baseAddress <> Gep_tmp852.io.Out(0)

  ld_54.io.GepAddr <> Gep_tmp953.io.Out(0)

  binaryOp_mul55.io.RightIO <> ld_54.io.Out(0)

  FP_add4356.io.RightIO <> binaryOp_mul55.io.Out(0)

  phi_47.io.InData(1) <> FP_add4356.io.Out(0)

  st_57.io.inData <> FP_add4356.io.Out(1)

  phi_k_0248.io.InData(1) <> binaryOp_inc4558.io.Out(0)

  icmp_exitcond59.io.LeftIO <> binaryOp_inc4558.io.Out(1)

  br_60.io.CmpIO <> icmp_exitcond59.io.Out(0)

  //FP_div5062.io.a <> phi_add43_lcssa61.io.Out(0)

  FP_div5062.io.a <> Loop_0.io.Out(0)

  st_63.io.inData <> FP_div5062.io.Out(0)

  st_66.io.inData <> FP_div5062.io.Out(1)

  Gep_tmp1165.io.baseAddress <> Gep_tmp1064.io.Out(0)

  st_66.io.GepAddr <> Gep_tmp1165.io.Out(0)

  phi_j_2442.io.InData(1) <> binaryOp_inc5667.io.Out(0)

  icmp_exitcond1168.io.LeftIO <> binaryOp_inc5667.io.Out(1)

  br_69.io.CmpIO <> icmp_exitcond1168.io.Out(0)

  phi_i_2540.io.InData(1) <> binaryOp_inc5970.io.Out(0)

  icmp_exitcond1371.io.LeftIO <> binaryOp_inc5970.io.Out(1)

  br_72.io.CmpIO <> icmp_exitcond1371.io.Out(0)

  //phi_add43_lcssa61.io.InData(0) <> Loop_0.io.Out(0)

//  phi_add_lcssa15.io.InData(0) <> Loop_5.io.Out(0)

  FP_sub4724.io.LeftIO <> InputSplitter.io.Out.data("field0")(1)

  st_3.io.Out(0).ready := true.B

  st_11.io.Out(0).ready := true.B

  st_17.io.Out(0).ready := true.B

  st_33.io.Out(0).ready := true.B

  st_45.io.Out(0).ready := true.B

  st_57.io.Out(0).ready := true.B

  st_63.io.Out(0).ready := true.B

  st_66.io.Out(0).ready := true.B



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_73.io.Out

}

import java.io.{File, FileWriter}
object kernel_covarianceMain extends App {
  val dir = new File("RTL/kernel_covariance") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new kernel_covarianceDF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}
