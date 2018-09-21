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

  val MemCtrl = Module(new UnifiedController(ID=0, Size=32, NReads=17, NWrites=15)
		 (WControl=new WriteMemoryController(NumOps=15, BaseSize=2, NumEntries=2))
		 (RControl=new ReadMemoryController(NumOps=17, BaseSize=2, NumEntries=2))
		 (RWArbiter=new ReadWriteArbiter()))

  io.MemReq <> MemCtrl.io.MemReq
  MemCtrl.io.MemResp <> io.MemResp

  val SharedFPU = Module(new SharedFPU(NumOps = 1, PipeDepth = 32)(t = p(FTYP)))

  val InputSplitter = Module(new SplitCallNew(List(1,3,1,2)))
  InputSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val Loop_0 = Module(new LoopBlock(NumIns=List(4,2,2,2), NumOuts = 1, NumExits=1, ID = 0))

  val Loop_1 = Module(new LoopBlock(NumIns=List(4,2,1), NumOuts = 0, NumExits=1, ID = 1))

  val Loop_2 = Module(new LoopBlock(NumIns=List(1,1), NumOuts = 0, NumExits=1, ID = 2))

  val Loop_3 = Module(new LoopBlock(NumIns=List(4,4,4), NumOuts = 0, NumExits=1, ID = 3))

  val Loop_4 = Module(new LoopBlock(NumIns=List(1,1), NumOuts = 0, NumExits=1, ID = 4))

  val Loop_5 = Module(new LoopBlock(NumIns=List(5,5,5), NumOuts = 1, NumExits=1, ID = 5))

  val Loop_6 = Module(new LoopBlock(NumIns=List(1,1,1), NumOuts = 0, NumExits=1, ID = 6))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_entry0 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 0))

  val bb_for_body1 = Module(new LoopHead(NumOuts = 6, NumPhi=1, BID = 1))

  val bb_for_body32 = Module(new LoopHead(NumOuts = 47, NumPhi=2, BID = 2))

  val bb_for_end3 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 7, BID = 3))

  val bb_for_cond18_preheader_preheader4 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 4))

  val bb_for_cond18_preheader5 = Module(new LoopHead(NumOuts = 3, NumPhi=1, BID = 5))

  val bb_for_body206 = Module(new LoopHead(NumOuts = 45, NumPhi=1, BID = 6))

  val bb_for_inc307 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 5, BID = 7))

  val bb_for_body38_lr_ph_preheader8 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 8))

  val bb_for_body38_lr_ph9 = Module(new LoopHead(NumOuts = 3, NumPhi=1, BID = 9))

  val bb_for_body3810 = Module(new LoopHead(NumOuts = 7, NumPhi=1, BID = 10))

  val bb_for_body4511 = Module(new LoopHead(NumOuts = 33, NumPhi=2, BID = 11))

  val bb_for_end6112 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 9, BID = 12))

  val bb_for_inc7313 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 5, BID = 13))

  val bb_for_end7514 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 14))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  br label %for.body
  val br_0 = Module(new UBranchNode(ID = 0))

  //  %indvars.iv28 = phi i64 [ 0, %entry ], [ %indvars.iv.next29, %for.end ]
  val phi_indvars_iv281 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 3, ID = 1))

  //  %arrayidx = getelementptr inbounds double, double* %mean, i64 %indvars.iv28
  val Gep_arrayidx2 = Module(new GepNode(NumIns = 1, NumOuts=3, ID=2)(ElementSize = 8, ArraySize = List()))

  //  store double 0.000000e+00, double* %arrayidx, align 8, !tbaa !17
  val st_3 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=1, ID=3, RouteID=0))

  //  br label %for.body3
  val br_4 = Module(new UBranchNode(NumPredOps=1, ID = 4))

  //  %indvars.iv25 = phi i64 [ 0, %for.body ], [ %indvars.iv.next26.4, %for.body3 ]
  val phi_indvars_iv255 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 6, ID = 5))

  //  %0 = phi double [ 0.000000e+00, %for.body ], [ %add.4, %for.body3 ]
  val phi_6 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 1, ID = 6))

  //  %tmp = getelementptr [1000 x double], [1000 x double]* %data, i64 %indvars.iv25
  val Gep_tmp7 = Module(new GepNode(NumIns = 1, NumOuts=1, ID=7)(ElementSize = 8, ArraySize = List()))

  //  %tmp1 = getelementptr [1000 x double], [1000 x double]* %tmp, i64 0, i64 %indvars.iv28
  val Gep_tmp18 = Module(new GepNode(NumIns = 2, NumOuts=1, ID=8)(ElementSize = 8, ArraySize = List()))

  //  %1 = load double, double* %tmp1, align 8, !tbaa !17
  val ld_9 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=9, RouteID=0))

  //  %add = fadd double %0, %1
  val FP_add10 = Module(new FPComputeNode(NumOuts = 2, ID = 10, opCode = "fadd")(t = p(FTYP)))

  //  store double %add, double* %arrayidx, align 8, !tbaa !17
  val st_11 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, ID=11, RouteID=1))

  //  %indvars.iv.next26 = add nuw nsw i64 %indvars.iv25, 1
  val binaryOp_indvars_iv_next2612 = Module(new ComputeNode(NumOuts = 1, ID = 12, opCode = "add")(sign=false))

  //  %tmp2 = getelementptr [1000 x double], [1000 x double]* %data, i64 %indvars.iv.next26
  val Gep_tmp213 = Module(new GepNode(NumIns = 1, NumOuts=1, ID=13)(ElementSize = 8, ArraySize = List()))

  //  %tmp3 = getelementptr [1000 x double], [1000 x double]* %tmp2, i64 0, i64 %indvars.iv28
  val Gep_tmp314 = Module(new GepNode(NumIns = 2, NumOuts=1, ID=14)(ElementSize = 8, ArraySize = List()))

  //  %2 = load double, double* %tmp3, align 8, !tbaa !17
  val ld_15 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=15, RouteID=1))

  //  %add.1 = fadd double %add, %2
  val FP_add_116 = Module(new FPComputeNode(NumOuts = 2, ID = 16, opCode = "fadd")(t = p(FTYP)))

  //  store double %add.1, double* %arrayidx, align 8, !tbaa !17
  val st_17 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, ID=17, RouteID=2))

  //  %indvars.iv.next26.1 = add nsw i64 %indvars.iv25, 2
  val binaryOp_indvars_iv_next26_118 = Module(new ComputeNode(NumOuts = 1, ID = 18, opCode = "add")(sign=false))

  //  %tmp4 = getelementptr [1000 x double], [1000 x double]* %data, i64 %indvars.iv.next26.1
  val Gep_tmp419 = Module(new GepNode(NumIns = 1, NumOuts=1, ID=19)(ElementSize = 8, ArraySize = List()))

  //  %tmp5 = getelementptr [1000 x double], [1000 x double]* %tmp4, i64 0, i64 %indvars.iv28
  val Gep_tmp520 = Module(new GepNode(NumIns = 2, NumOuts=1, ID=20)(ElementSize = 8, ArraySize = List()))

  //  %3 = load double, double* %tmp5, align 8, !tbaa !17
  val ld_21 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=21, RouteID=2))

  //  %add.2 = fadd double %add.1, %3
  val FP_add_222 = Module(new FPComputeNode(NumOuts = 2, ID = 22, opCode = "fadd")(t = p(FTYP)))

  //  store double %add.2, double* %arrayidx, align 8, !tbaa !17
  val st_23 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, ID=23, RouteID=3))

  //  %indvars.iv.next26.2 = add nsw i64 %indvars.iv25, 3
  val binaryOp_indvars_iv_next26_224 = Module(new ComputeNode(NumOuts = 1, ID = 24, opCode = "add")(sign=false))

  //  %tmp6 = getelementptr [1000 x double], [1000 x double]* %data, i64 %indvars.iv.next26.2
  val Gep_tmp625 = Module(new GepNode(NumIns = 1, NumOuts=1, ID=25)(ElementSize = 8, ArraySize = List()))

  //  %tmp7 = getelementptr [1000 x double], [1000 x double]* %tmp6, i64 0, i64 %indvars.iv28
  val Gep_tmp726 = Module(new GepNode(NumIns = 2, NumOuts=1, ID=26)(ElementSize = 8, ArraySize = List()))

  //  %4 = load double, double* %tmp7, align 8, !tbaa !17
  val ld_27 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=27, RouteID=3))

  //  %add.3 = fadd double %add.2, %4
  val FP_add_328 = Module(new FPComputeNode(NumOuts = 2, ID = 28, opCode = "fadd")(t = p(FTYP)))

  //  store double %add.3, double* %arrayidx, align 8, !tbaa !17
  val st_29 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, ID=29, RouteID=4))

  //  %indvars.iv.next26.3 = add nsw i64 %indvars.iv25, 4
  val binaryOp_indvars_iv_next26_330 = Module(new ComputeNode(NumOuts = 1, ID = 30, opCode = "add")(sign=false))

  //  %tmp8 = getelementptr [1000 x double], [1000 x double]* %data, i64 %indvars.iv.next26.3
  val Gep_tmp831 = Module(new GepNode(NumIns = 1, NumOuts=1, ID=31)(ElementSize = 8, ArraySize = List()))

  //  %tmp9 = getelementptr [1000 x double], [1000 x double]* %tmp8, i64 0, i64 %indvars.iv28
  val Gep_tmp932 = Module(new GepNode(NumIns = 2, NumOuts=1, ID=32)(ElementSize = 8, ArraySize = List()))

  //  %5 = load double, double* %tmp9, align 8, !tbaa !17
  val ld_33 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=33, RouteID=4))

  //  %add.4 = fadd double %add.3, %5
  val FP_add_434 = Module(new FPComputeNode(NumOuts = 3, ID = 34, opCode = "fadd")(t = p(FTYP)))

  //  store double %add.4, double* %arrayidx, align 8, !tbaa !17
  val st_35 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, ID=35, RouteID=5))

  //  %indvars.iv.next26.4 = add nsw i64 %indvars.iv25, 5
  val binaryOp_indvars_iv_next26_436 = Module(new ComputeNode(NumOuts = 2, ID = 36, opCode = "add")(sign=false))

  //  %exitcond27.4 = icmp eq i64 %indvars.iv.next26.4, 1000
  val icmp_exitcond27_437 = Module(new IcmpNode(NumOuts = 1, ID = 37, opCode = "eq")(sign=false))

  //  br i1 %exitcond27.4, label %for.end, label %for.body3, !llvm.loop !21
  val br_38 = Module(new CBranchNode(ID = 38))

  //  %div = fdiv double %add.4, %float_n
  val FP_div39 = Module(new FPDivSqrtNode(NumOuts = 1, ID = 39, RouteID = 0, opCode = "fdiv")(t = p(FTYP)))

  //  store double %div, double* %arrayidx, align 8, !tbaa !17
  val st_40 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, ID=40, RouteID=6))

  //  %indvars.iv.next29 = add nuw nsw i64 %indvars.iv28, 1
  val binaryOp_indvars_iv_next2941 = Module(new ComputeNode(NumOuts = 2, ID = 41, opCode = "add")(sign=false))

  //  %exitcond30 = icmp eq i64 %indvars.iv.next29, 1000
  val icmp_exitcond3042 = Module(new IcmpNode(NumOuts = 1, ID = 42, opCode = "eq")(sign=false))

  //  br i1 %exitcond30, label %for.cond18.preheader.preheader, label %for.body, !llvm.loop !44
  val br_43 = Module(new CBranchNode(ID = 43))

  //  br label %for.cond18.preheader
  val br_44 = Module(new UBranchNode(ID = 44))

  //  %indvars.iv22 = phi i64 [ %indvars.iv.next23, %for.inc30 ], [ 0, %for.cond18.preheader.preheader ]
  val phi_indvars_iv2245 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 45))

  //  br label %for.body20
  val br_46 = Module(new UBranchNode(ID = 46))

  //  %indvars.iv19 = phi i64 [ 0, %for.cond18.preheader ], [ %indvars.iv.next20.3, %for.body20 ]
  val phi_indvars_iv1947 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 6, ID = 47))

  //  %arrayidx22 = getelementptr inbounds double, double* %mean, i64 %indvars.iv19
  val Gep_arrayidx2248 = Module(new GepNode(NumIns = 1, NumOuts=1, ID=48)(ElementSize = 8, ArraySize = List()))

  //  %6 = load double, double* %arrayidx22, align 8, !tbaa !17
  val ld_49 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=49, RouteID=5))

  //  %tmp10 = getelementptr [1000 x double], [1000 x double]* %data, i64 %indvars.iv22
  val Gep_tmp1050 = Module(new GepNode(NumIns = 1, NumOuts=1, ID=50)(ElementSize = 8, ArraySize = List()))

  //  %tmp11 = getelementptr [1000 x double], [1000 x double]* %tmp10, i64 0, i64 %indvars.iv19
  val Gep_tmp1151 = Module(new GepNode(NumIns = 2, NumOuts=2, ID=51)(ElementSize = 8, ArraySize = List()))

  //  %7 = load double, double* %tmp11, align 8, !tbaa !17
  val ld_52 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=52, RouteID=6))

  //  %sub = fsub double %7, %6
  val binaryOp_sub53 = Module(new ComputeNode(NumOuts = 1, ID = 53, opCode = "fsub")(sign=false))

  //  store double %sub, double* %tmp11, align 8, !tbaa !17
  val st_54 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, ID=54, RouteID=7))

  //  %indvars.iv.next20 = or i64 %indvars.iv19, 1
  val binaryOp_indvars_iv_next2055 = Module(new ComputeNode(NumOuts = 2, ID = 55, opCode = "or")(sign=false))

  //  %arrayidx22.1 = getelementptr inbounds double, double* %mean, i64 %indvars.iv.next20
  val Gep_arrayidx22_156 = Module(new GepNode(NumIns = 1, NumOuts=1, ID=56)(ElementSize = 8, ArraySize = List()))

  //  %8 = load double, double* %arrayidx22.1, align 8, !tbaa !17
  val ld_57 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=57, RouteID=7))

  //  %tmp12 = getelementptr [1000 x double], [1000 x double]* %data, i64 %indvars.iv22
  val Gep_tmp1258 = Module(new GepNode(NumIns = 1, NumOuts=1, ID=58)(ElementSize = 8, ArraySize = List()))

  //  %tmp13 = getelementptr [1000 x double], [1000 x double]* %tmp12, i64 0, i64 %indvars.iv.next20
  val Gep_tmp1359 = Module(new GepNode(NumIns = 2, NumOuts=2, ID=59)(ElementSize = 8, ArraySize = List()))

  //  %9 = load double, double* %tmp13, align 8, !tbaa !17
  val ld_60 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=60, RouteID=8))

  //  %sub.1 = fsub double %9, %8
  val binaryOp_sub_161 = Module(new ComputeNode(NumOuts = 1, ID = 61, opCode = "fsub")(sign=false))

  //  store double %sub.1, double* %tmp13, align 8, !tbaa !17
  val st_62 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, ID=62, RouteID=8))

  //  %indvars.iv.next20.1 = or i64 %indvars.iv19, 2
  val binaryOp_indvars_iv_next20_163 = Module(new ComputeNode(NumOuts = 2, ID = 63, opCode = "or")(sign=false))

  //  %arrayidx22.2 = getelementptr inbounds double, double* %mean, i64 %indvars.iv.next20.1
  val Gep_arrayidx22_264 = Module(new GepNode(NumIns = 1, NumOuts=1, ID=64)(ElementSize = 8, ArraySize = List()))

  //  %10 = load double, double* %arrayidx22.2, align 8, !tbaa !17
  val ld_65 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=65, RouteID=9))

  //  %tmp14 = getelementptr [1000 x double], [1000 x double]* %data, i64 %indvars.iv22
  val Gep_tmp1466 = Module(new GepNode(NumIns = 1, NumOuts=1, ID=66)(ElementSize = 8, ArraySize = List()))

  //  %tmp15 = getelementptr [1000 x double], [1000 x double]* %tmp14, i64 0, i64 %indvars.iv.next20.1
  val Gep_tmp1567 = Module(new GepNode(NumIns = 2, NumOuts=2, ID=67)(ElementSize = 8, ArraySize = List()))

  //  %11 = load double, double* %tmp15, align 8, !tbaa !17
  val ld_68 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=68, RouteID=10))

  //  %sub.2 = fsub double %11, %10
  val binaryOp_sub_269 = Module(new ComputeNode(NumOuts = 1, ID = 69, opCode = "fsub")(sign=false))

  //  store double %sub.2, double* %tmp15, align 8, !tbaa !17
  val st_70 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, ID=70, RouteID=9))

  //  %indvars.iv.next20.2 = or i64 %indvars.iv19, 3
  val binaryOp_indvars_iv_next20_271 = Module(new ComputeNode(NumOuts = 2, ID = 71, opCode = "or")(sign=false))

  //  %arrayidx22.3 = getelementptr inbounds double, double* %mean, i64 %indvars.iv.next20.2
  val Gep_arrayidx22_372 = Module(new GepNode(NumIns = 1, NumOuts=1, ID=72)(ElementSize = 8, ArraySize = List()))

  //  %12 = load double, double* %arrayidx22.3, align 8, !tbaa !17
  val ld_73 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=73, RouteID=11))

  //  %tmp16 = getelementptr [1000 x double], [1000 x double]* %data, i64 %indvars.iv22
  val Gep_tmp1674 = Module(new GepNode(NumIns = 1, NumOuts=1, ID=74)(ElementSize = 8, ArraySize = List()))

  //  %tmp17 = getelementptr [1000 x double], [1000 x double]* %tmp16, i64 0, i64 %indvars.iv.next20.2
  val Gep_tmp1775 = Module(new GepNode(NumIns = 2, NumOuts=2, ID=75)(ElementSize = 8, ArraySize = List()))

  //  %13 = load double, double* %tmp17, align 8, !tbaa !17
  val ld_76 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=76, RouteID=12))

  //  %sub.3 = fsub double %13, %12
  val binaryOp_sub_377 = Module(new ComputeNode(NumOuts = 1, ID = 77, opCode = "fsub")(sign=false))

  //  store double %sub.3, double* %tmp17, align 8, !tbaa !17
  val st_78 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, ID=78, RouteID=10))

  //  %indvars.iv.next20.3 = add nsw i64 %indvars.iv19, 4
  val binaryOp_indvars_iv_next20_379 = Module(new ComputeNode(NumOuts = 2, ID = 79, opCode = "add")(sign=false))

  //  %exitcond21.3 = icmp eq i64 %indvars.iv.next20.3, 1000
  val icmp_exitcond21_380 = Module(new IcmpNode(NumOuts = 1, ID = 80, opCode = "eq")(sign=false))

  //  br i1 %exitcond21.3, label %for.inc30, label %for.body20, !llvm.loop !47
  val br_81 = Module(new CBranchNode(ID = 81))

  //  %indvars.iv.next23 = add nuw nsw i64 %indvars.iv22, 1
  val binaryOp_indvars_iv_next2382 = Module(new ComputeNode(NumOuts = 2, ID = 82, opCode = "add")(sign=false))

  //  %exitcond24 = icmp eq i64 %indvars.iv.next23, 1000
  val icmp_exitcond2483 = Module(new IcmpNode(NumOuts = 1, ID = 83, opCode = "eq")(sign=false))

  //  br i1 %exitcond24, label %for.body38.lr.ph.preheader, label %for.cond18.preheader, !llvm.loop !53
  val br_84 = Module(new CBranchNode(ID = 84))

  //  br label %for.body38.lr.ph
  val br_85 = Module(new UBranchNode(ID = 85))

  //  %indvars.iv16 = phi i64 [ %indvars.iv.next17, %for.inc73 ], [ 0, %for.body38.lr.ph.preheader ]
  val phi_indvars_iv1686 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 86))

  //  br label %for.body38
  val br_87 = Module(new UBranchNode(ID = 87))

  //  %indvars.iv12 = phi i64 [ %indvars.iv16, %for.body38.lr.ph ], [ %indvars.iv.next13, %for.end61 ]
  val phi_indvars_iv1288 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 4, ID = 88))

  //  %tmp18 = getelementptr [1000 x double], [1000 x double]* %symmat, i64 %indvars.iv16
  val Gep_tmp1889 = Module(new GepNode(NumIns = 1, NumOuts=1, ID=89)(ElementSize = 8, ArraySize = List()))

  //  %tmp19 = getelementptr [1000 x double], [1000 x double]* %tmp18, i64 0, i64 %indvars.iv12
  val Gep_tmp1990 = Module(new GepNode(NumIns = 2, NumOuts=2, ID=90)(ElementSize = 8, ArraySize = List()))

  //  store double 0.000000e+00, double* %tmp19, align 8, !tbaa !17
  val st_91 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=1, ID=91, RouteID=11))

  //  br label %for.body45
  val br_92 = Module(new UBranchNode(NumPredOps=1, ID = 92))

  //  %indvars.iv = phi i64 [ 0, %for.body38 ], [ %indvars.iv.next.1, %for.body45 ]
  val phi_indvars_iv93 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 4, ID = 93))

  //  %14 = phi double [ 0.000000e+00, %for.body38 ], [ %add58.1, %for.body45 ]
  val phi_94 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 1, ID = 94))

  //  %tmp20 = getelementptr [1000 x double], [1000 x double]* %data, i64 %indvars.iv
  val Gep_tmp2095 = Module(new GepNode(NumIns = 1, NumOuts=1, ID=95)(ElementSize = 8, ArraySize = List()))

  //  %tmp21 = getelementptr [1000 x double], [1000 x double]* %tmp20, i64 0, i64 %indvars.iv16
  val Gep_tmp2196 = Module(new GepNode(NumIns = 2, NumOuts=1, ID=96)(ElementSize = 8, ArraySize = List()))

  //  %15 = load double, double* %tmp21, align 8, !tbaa !17
  val ld_97 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=97, RouteID=13))

  //  %tmp22 = getelementptr [1000 x double], [1000 x double]* %data, i64 %indvars.iv
  val Gep_tmp2298 = Module(new GepNode(NumIns = 1, NumOuts=1, ID=98)(ElementSize = 8, ArraySize = List()))

  //  %tmp23 = getelementptr [1000 x double], [1000 x double]* %tmp22, i64 0, i64 %indvars.iv12
  val Gep_tmp2399 = Module(new GepNode(NumIns = 2, NumOuts=1, ID=99)(ElementSize = 8, ArraySize = List()))

  //  %16 = load double, double* %tmp23, align 8, !tbaa !17
  val ld_100 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=100, RouteID=14))

  //  %mul = fmul double %15, %16
  val binaryOp_mul101 = Module(new ComputeNode(NumOuts = 1, ID = 101, opCode = "fmul")(sign=false))

  //  %add58 = fadd double %14, %mul
  val FP_add58102 = Module(new FPComputeNode(NumOuts = 2, ID = 102, opCode = "fadd")(t = p(FTYP)))

  //  store double %add58, double* %tmp19, align 8, !tbaa !17
  val st_103 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, ID=103, RouteID=12))

  //  %indvars.iv.next = or i64 %indvars.iv, 1
  val binaryOp_indvars_iv_next104 = Module(new ComputeNode(NumOuts = 2, ID = 104, opCode = "or")(sign=false))

  //  %tmp24 = getelementptr [1000 x double], [1000 x double]* %data, i64 %indvars.iv.next
  val Gep_tmp24105 = Module(new GepNode(NumIns = 1, NumOuts=1, ID=105)(ElementSize = 8, ArraySize = List()))

  //  %tmp25 = getelementptr [1000 x double], [1000 x double]* %tmp24, i64 0, i64 %indvars.iv16
  val Gep_tmp25106 = Module(new GepNode(NumIns = 2, NumOuts=1, ID=106)(ElementSize = 8, ArraySize = List()))

  //  %17 = load double, double* %tmp25, align 8, !tbaa !17
  val ld_107 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=107, RouteID=15))

  //  %tmp26 = getelementptr [1000 x double], [1000 x double]* %data, i64 %indvars.iv.next
  val Gep_tmp26108 = Module(new GepNode(NumIns = 1, NumOuts=1, ID=108)(ElementSize = 8, ArraySize = List()))

  //  %tmp27 = getelementptr [1000 x double], [1000 x double]* %tmp26, i64 0, i64 %indvars.iv12
  val Gep_tmp27109 = Module(new GepNode(NumIns = 2, NumOuts=1, ID=109)(ElementSize = 8, ArraySize = List()))

  //  %18 = load double, double* %tmp27, align 8, !tbaa !17
  val ld_110 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=110, RouteID=16))

  //  %mul.1 = fmul double %17, %18
  val binaryOp_mul_1111 = Module(new ComputeNode(NumOuts = 1, ID = 111, opCode = "fmul")(sign=false))

  //  %add58.1 = fadd double %add58, %mul.1
  val FP_add58_1112 = Module(new FPComputeNode(NumOuts = 3, ID = 112, opCode = "fadd")(t = p(FTYP)))

  //  store double %add58.1, double* %tmp19, align 8, !tbaa !17
  val st_113 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, ID=113, RouteID=13))

  //  %indvars.iv.next.1 = add nsw i64 %indvars.iv, 2
  val binaryOp_indvars_iv_next_1114 = Module(new ComputeNode(NumOuts = 2, ID = 114, opCode = "add")(sign=false))

  //  %exitcond.1 = icmp eq i64 %indvars.iv.next.1, 1000
  val icmp_exitcond_1115 = Module(new IcmpNode(NumOuts = 1, ID = 115, opCode = "eq")(sign=false))

  //  br i1 %exitcond.1, label %for.end61, label %for.body45, !llvm.loop !56
  val br_116 = Module(new CBranchNode(ID = 116))

  //  %tmp28 = getelementptr [1000 x double], [1000 x double]* %symmat, i64 %indvars.iv12
  val Gep_tmp28117 = Module(new GepNode(NumIns = 1, NumOuts=1, ID=117)(ElementSize = 8, ArraySize = List()))

  //  %tmp29 = getelementptr [1000 x double], [1000 x double]* %tmp28, i64 0, i64 %indvars.iv16
  val Gep_tmp29118 = Module(new GepNode(NumIns = 2, NumOuts=1, ID=118)(ElementSize = 8, ArraySize = List()))

  //  store double %add58.1, double* %tmp29, align 8, !tbaa !17
  val st_119 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, ID=119, RouteID=14))

  //  %indvars.iv.next13 = add nuw nsw i64 %indvars.iv12, 1
  val binaryOp_indvars_iv_next13120 = Module(new ComputeNode(NumOuts = 2, ID = 120, opCode = "add")(sign=false))

  //  %exitcond14 = icmp eq i64 %indvars.iv.next13, 1000
  val icmp_exitcond14121 = Module(new IcmpNode(NumOuts = 1, ID = 121, opCode = "eq")(sign=false))

  //  br i1 %exitcond14, label %for.inc73, label %for.body38, !llvm.loop !65
  val br_122 = Module(new CBranchNode(ID = 122))

  //  %indvars.iv.next17 = add nuw nsw i64 %indvars.iv16, 1
  val binaryOp_indvars_iv_next17123 = Module(new ComputeNode(NumOuts = 2, ID = 123, opCode = "add")(sign=false))

  //  %exitcond18 = icmp eq i64 %indvars.iv.next17, 1000
  val icmp_exitcond18124 = Module(new IcmpNode(NumOuts = 1, ID = 124, opCode = "eq")(sign=false))

  //  br i1 %exitcond18, label %for.end75, label %for.body38.lr.ph, !llvm.loop !68
  val br_125 = Module(new CBranchNode(ID = 125))

  //  ret void
  val ret_126 = Module(new RetNode2(retTypes=List(), ID = 126))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i64 0
  val const0 = Module(new ConstFastNode(value = 0, ID = 0))

  //i64 0
  val const1 = Module(new ConstFastNode(value = 0, ID = 1))

  //i64 0
  val const2 = Module(new ConstFastNode(value = 0, ID = 2))

  //i64 1
  val const3 = Module(new ConstFastNode(value = 1, ID = 3))

  //i64 0
  val const4 = Module(new ConstFastNode(value = 0, ID = 4))

  //i64 2
  val const5 = Module(new ConstFastNode(value = 2, ID = 5))

  //i64 0
  val const6 = Module(new ConstFastNode(value = 0, ID = 6))

  //i64 3
  val const7 = Module(new ConstFastNode(value = 3, ID = 7))

  //i64 0
  val const8 = Module(new ConstFastNode(value = 0, ID = 8))

  //i64 4
  val const9 = Module(new ConstFastNode(value = 4, ID = 9))

  //i64 0
  val const10 = Module(new ConstFastNode(value = 0, ID = 10))

  //i64 5
  val const11 = Module(new ConstFastNode(value = 5, ID = 11))

  //i64 1000
  val const12 = Module(new ConstFastNode(value = 1000, ID = 12))

  //i64 1
  val const13 = Module(new ConstFastNode(value = 1, ID = 13))

  //i64 1000
  val const14 = Module(new ConstFastNode(value = 1000, ID = 14))

  //i64 0
  val const15 = Module(new ConstFastNode(value = 0, ID = 15))

  //i64 0
  val const16 = Module(new ConstFastNode(value = 0, ID = 16))

  //i64 0
  val const17 = Module(new ConstFastNode(value = 0, ID = 17))

  //i64 1
  val const18 = Module(new ConstFastNode(value = 1, ID = 18))

  //i64 0
  val const19 = Module(new ConstFastNode(value = 0, ID = 19))

  //i64 2
  val const20 = Module(new ConstFastNode(value = 2, ID = 20))

  //i64 0
  val const21 = Module(new ConstFastNode(value = 0, ID = 21))

  //i64 3
  val const22 = Module(new ConstFastNode(value = 3, ID = 22))

  //i64 0
  val const23 = Module(new ConstFastNode(value = 0, ID = 23))

  //i64 4
  val const24 = Module(new ConstFastNode(value = 4, ID = 24))

  //i64 1000
  val const25 = Module(new ConstFastNode(value = 1000, ID = 25))

  //i64 1
  val const26 = Module(new ConstFastNode(value = 1, ID = 26))

  //i64 1000
  val const27 = Module(new ConstFastNode(value = 1000, ID = 27))

  //i64 0
  val const28 = Module(new ConstFastNode(value = 0, ID = 28))

  //i64 0
  val const29 = Module(new ConstFastNode(value = 0, ID = 29))

  //i64 0
  val const30 = Module(new ConstFastNode(value = 0, ID = 30))

  //i64 0
  val const31 = Module(new ConstFastNode(value = 0, ID = 31))

  //i64 0
  val const32 = Module(new ConstFastNode(value = 0, ID = 32))

  //i64 1
  val const33 = Module(new ConstFastNode(value = 1, ID = 33))

  //i64 0
  val const34 = Module(new ConstFastNode(value = 0, ID = 34))

  //i64 0
  val const35 = Module(new ConstFastNode(value = 0, ID = 35))

  //i64 2
  val const36 = Module(new ConstFastNode(value = 2, ID = 36))

  //i64 1000
  val const37 = Module(new ConstFastNode(value = 1000, ID = 37))

  //i64 0
  val const38 = Module(new ConstFastNode(value = 0, ID = 38))

  //i64 1
  val const39 = Module(new ConstFastNode(value = 1, ID = 39))

  //i64 1000
  val const40 = Module(new ConstFastNode(value = 1000, ID = 40))

  //i64 1
  val const41 = Module(new ConstFastNode(value = 1, ID = 41))

  //i64 1000
  val const42 = Module(new ConstFastNode(value = 1000, ID = 42))

  //double 0.000000e+00
  val constf0 = Module(new ConstNode(value = 0x0, ID = 0))

  //double 0.000000e+00
  val constf1 = Module(new ConstNode(value = 0x0, ID = 1))

  //double 0.000000e+00
  val constf2 = Module(new ConstNode(value = 0x0, ID = 2))

  //double 0.000000e+00
  val constf3 = Module(new ConstNode(value = 0x0, ID = 3))



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_entry0.io.predicateIn <> InputSplitter.io.Out.enable

  bb_for_body1.io.activate <> Loop_6.io.endEnable

  bb_for_body1.io.loopBack <> br_43.io.Out(0)

  bb_for_body32.io.activate <> Loop_5.io.endEnable

  bb_for_body32.io.loopBack <> br_38.io.Out(0)

  bb_for_end3.io.predicateIn <> Loop_5.io.endEnable

  bb_for_cond18_preheader_preheader4.io.predicateIn <> Loop_6.io.endEnable

  bb_for_cond18_preheader5.io.activate <> Loop_4.io.endEnable

  bb_for_cond18_preheader5.io.loopBack <> br_84.io.Out(0)

  bb_for_body206.io.activate <> Loop_3.io.endEnable

  bb_for_body206.io.loopBack <> br_81.io.Out(0)

  bb_for_inc307.io.predicateIn <> Loop_3.io.endEnable

  bb_for_body38_lr_ph_preheader8.io.predicateIn <> Loop_4.io.endEnable

  bb_for_body38_lr_ph9.io.activate <> Loop_2.io.endEnable

  bb_for_body38_lr_ph9.io.loopBack <> br_125.io.Out(0)

  bb_for_body3810.io.activate <> Loop_1.io.endEnable

  bb_for_body3810.io.loopBack <> br_122.io.Out(0)

  bb_for_body4511.io.activate <> Loop_0.io.endEnable

  bb_for_body4511.io.loopBack <> br_116.io.Out(0)

  bb_for_end6112.io.predicateIn <> Loop_0.io.endEnable

  bb_for_inc7313.io.predicateIn <> Loop_1.io.endEnable

  bb_for_end7514.io.predicateIn <> Loop_2.io.endEnable



  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */

  Loop_0.io.enable <> br_92.io.Out(0)

  Loop_0.io.latchEnable <> br_116.io.Out(1)

  Loop_0.io.loopExit(0) <> br_116.io.Out(1)

  Loop_1.io.enable <> br_87.io.Out(0)

  Loop_1.io.latchEnable <> br_122.io.Out(1)

  Loop_1.io.loopExit(0) <> br_122.io.Out(1)

  Loop_2.io.enable <> br_85.io.Out(0)

  Loop_2.io.latchEnable <> br_125.io.Out(1)

  Loop_2.io.loopExit(0) <> br_125.io.Out(1)

  Loop_3.io.enable <> br_46.io.Out(0)

  Loop_3.io.latchEnable <> br_81.io.Out(1)

  Loop_3.io.loopExit(0) <> br_81.io.Out(1)

  Loop_4.io.enable <> br_44.io.Out(0)

  Loop_4.io.latchEnable <> br_84.io.Out(1)

  Loop_4.io.loopExit(0) <> br_84.io.Out(1)

  Loop_5.io.enable <> br_4.io.Out(0)

  Loop_5.io.latchEnable <> br_38.io.Out(1)

  Loop_5.io.loopExit(0) <> br_38.io.Out(1)

  Loop_6.io.enable <> br_0.io.Out(0)

  Loop_6.io.latchEnable <> br_43.io.Out(1)

  Loop_6.io.loopExit(0) <> br_43.io.Out(1)



  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */

  br_92.io.PredOp(0) <> st_91.io.SuccOp(0)

  br_4.io.PredOp(0) <> st_3.io.SuccOp(0)



  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

  Loop_0.io.In(0) <> Loop_1.io.liveIn.data("field2")(0)

  Loop_0.io.In(1) <> Loop_1.io.liveIn.data("field0")(2)

  Loop_0.io.In(2) <> phi_indvars_iv1288.io.Out(2)

  Loop_0.io.In(3) <> Gep_tmp1990.io.Out.data(1)

  Loop_1.io.In(0) <> phi_indvars_iv1686.io.Out(0)

  Loop_1.io.In(1) <> Loop_2.io.liveIn.data("field0")(0)

  Loop_1.io.In(2) <> Loop_2.io.liveIn.data("field1")(0)

  Loop_2.io.In(0) <> InputSplitter.io.Out.data("field2")(0)

  Loop_2.io.In(1) <> InputSplitter.io.Out.data("field1")(0)

  Loop_3.io.In(0) <> Loop_4.io.liveIn.data("field0")(0)

  Loop_3.io.In(1) <> Loop_4.io.liveIn.data("field1")(0)

  Loop_3.io.In(2) <> phi_indvars_iv2245.io.Out(1)

  Loop_4.io.In(0) <> InputSplitter.io.Out.data("field3")(0)

  Loop_4.io.In(1) <> InputSplitter.io.Out.data("field1")(0)

  Loop_5.io.In(0) <> Loop_6.io.liveIn.data("field1")(0)

  Loop_5.io.In(1) <> phi_indvars_iv281.io.Out(2)

  Loop_5.io.In(2) <> Gep_arrayidx2.io.Out.data(1)

  Loop_6.io.In(0) <> InputSplitter.io.Out.data("field3")(0)

  Loop_6.io.In(1) <> InputSplitter.io.Out.data("field1")(0)

  Loop_6.io.In(2) <> InputSplitter.io.Out.data("field0")(1)



  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */

  Gep_tmp2095.io.baseAddress <> Loop_0.io.liveIn.data("field0")(0)

  Gep_tmp2298.io.baseAddress <> Loop_0.io.liveIn.data("field0")(1)

  Gep_tmp24105.io.baseAddress <> Loop_0.io.liveIn.data("field0")(2)

  Gep_tmp26108.io.baseAddress <> Loop_0.io.liveIn.data("field0")(3)

  Gep_tmp2196.io.idx(1) <> Loop_0.io.liveIn.data("field1")(0)

  Gep_tmp25106.io.idx(1) <> Loop_0.io.liveIn.data("field1")(1)

  Gep_tmp2399.io.idx(1) <> Loop_0.io.liveIn.data("field2")(0)

  Gep_tmp27109.io.idx(1) <> Loop_0.io.liveIn.data("field2")(1)

  st_103.io.GepAddr <> Loop_0.io.liveIn.data("field3")(0)

  st_113.io.GepAddr <> Loop_0.io.liveIn.data("field3")(1)

  phi_indvars_iv1288.io.InData(0) <> Loop_1.io.liveIn.data("field0")(0)

  Gep_tmp1889.io.idx(0) <> Loop_1.io.liveIn.data("field0")(1)

  Gep_tmp29118.io.idx(1) <> Loop_1.io.liveIn.data("field0")(4)

  Gep_tmp1889.io.baseAddress <> Loop_1.io.liveIn.data("field1")(0)

  Gep_tmp28117.io.baseAddress <> Loop_1.io.liveIn.data("field1")(1)

  Gep_arrayidx2248.io.baseAddress <> Loop_3.io.liveIn.data("field0")(0)

  Gep_arrayidx22_156.io.baseAddress <> Loop_3.io.liveIn.data("field0")(1)

  Gep_arrayidx22_264.io.baseAddress <> Loop_3.io.liveIn.data("field0")(2)

  Gep_arrayidx22_372.io.baseAddress <> Loop_3.io.liveIn.data("field0")(3)

  Gep_tmp1050.io.baseAddress <> Loop_3.io.liveIn.data("field1")(0)

  Gep_tmp1258.io.baseAddress <> Loop_3.io.liveIn.data("field1")(1)

  Gep_tmp1466.io.baseAddress <> Loop_3.io.liveIn.data("field1")(2)

  Gep_tmp1674.io.baseAddress <> Loop_3.io.liveIn.data("field1")(3)

  Gep_tmp1050.io.idx(0) <> Loop_3.io.liveIn.data("field2")(0)

  Gep_tmp1258.io.idx(0) <> Loop_3.io.liveIn.data("field2")(1)

  Gep_tmp1466.io.idx(0) <> Loop_3.io.liveIn.data("field2")(2)

  Gep_tmp1674.io.idx(0) <> Loop_3.io.liveIn.data("field2")(3)

  Gep_tmp7.io.baseAddress <> Loop_5.io.liveIn.data("field0")(0)

  Gep_tmp213.io.baseAddress <> Loop_5.io.liveIn.data("field0")(1)

  Gep_tmp419.io.baseAddress <> Loop_5.io.liveIn.data("field0")(2)

  Gep_tmp625.io.baseAddress <> Loop_5.io.liveIn.data("field0")(3)

  Gep_tmp831.io.baseAddress <> Loop_5.io.liveIn.data("field0")(4)

  Gep_tmp18.io.idx(1) <> Loop_5.io.liveIn.data("field1")(0)

  Gep_tmp314.io.idx(1) <> Loop_5.io.liveIn.data("field1")(1)

  Gep_tmp520.io.idx(1) <> Loop_5.io.liveIn.data("field1")(2)

  Gep_tmp726.io.idx(1) <> Loop_5.io.liveIn.data("field1")(3)

  Gep_tmp932.io.idx(1) <> Loop_5.io.liveIn.data("field1")(4)

  st_11.io.GepAddr <> Loop_5.io.liveIn.data("field2")(0)

  st_17.io.GepAddr <> Loop_5.io.liveIn.data("field2")(1)

  st_23.io.GepAddr <> Loop_5.io.liveIn.data("field2")(2)

  st_29.io.GepAddr <> Loop_5.io.liveIn.data("field2")(3)

  st_35.io.GepAddr <> Loop_5.io.liveIn.data("field2")(4)

  Gep_arrayidx2.io.baseAddress <> Loop_6.io.liveIn.data("field0")(0)

  FP_div39.io.b <> Loop_6.io.liveIn.data("field2")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */

  Loop_0.io.liveOut(0) <> FP_add58_1112.io.Out(0)

  Loop_5.io.liveOut(0) <> FP_add_434.io.Out(0)



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  br_0.io.enable <> bb_entry0.io.Out(0)


  constf0.io.enable <> bb_for_body1.io.Out(1)

  const0.io.enable <> bb_for_body1.io.Out(0)

  phi_indvars_iv281.io.enable <> bb_for_body1.io.Out(2)

  Gep_arrayidx2.io.enable <> bb_for_body1.io.Out(3)

  st_3.io.enable <> bb_for_body1.io.Out(4)

  br_4.io.enable <> bb_for_body1.io.Out(5)


  constf1.io.enable <> bb_for_body32.io.Out(1)

  const1.io.enable <> bb_for_body32.io.Out(0)

  const2.io.enable <> bb_for_body32.io.Out(2)

  const3.io.enable <> bb_for_body32.io.Out(3)

  const4.io.enable <> bb_for_body32.io.Out(4)

  const5.io.enable <> bb_for_body32.io.Out(5)

  const6.io.enable <> bb_for_body32.io.Out(6)

  const7.io.enable <> bb_for_body32.io.Out(7)

  const8.io.enable <> bb_for_body32.io.Out(8)

  const9.io.enable <> bb_for_body32.io.Out(9)

  const10.io.enable <> bb_for_body32.io.Out(10)

  const11.io.enable <> bb_for_body32.io.Out(11)

  const12.io.enable <> bb_for_body32.io.Out(12)

  phi_indvars_iv255.io.enable <> bb_for_body32.io.Out(13)

  phi_6.io.enable <> bb_for_body32.io.Out(14)

  Gep_tmp7.io.enable <> bb_for_body32.io.Out(15)

  Gep_tmp18.io.enable <> bb_for_body32.io.Out(16)

  ld_9.io.enable <> bb_for_body32.io.Out(17)

  FP_add10.io.enable <> bb_for_body32.io.Out(18)

  st_11.io.enable <> bb_for_body32.io.Out(19)

  binaryOp_indvars_iv_next2612.io.enable <> bb_for_body32.io.Out(20)

  Gep_tmp213.io.enable <> bb_for_body32.io.Out(21)

  Gep_tmp314.io.enable <> bb_for_body32.io.Out(22)

  ld_15.io.enable <> bb_for_body32.io.Out(23)

  FP_add_116.io.enable <> bb_for_body32.io.Out(24)

  st_17.io.enable <> bb_for_body32.io.Out(25)

  binaryOp_indvars_iv_next26_118.io.enable <> bb_for_body32.io.Out(26)

  Gep_tmp419.io.enable <> bb_for_body32.io.Out(27)

  Gep_tmp520.io.enable <> bb_for_body32.io.Out(28)

  ld_21.io.enable <> bb_for_body32.io.Out(29)

  FP_add_222.io.enable <> bb_for_body32.io.Out(30)

  st_23.io.enable <> bb_for_body32.io.Out(31)

  binaryOp_indvars_iv_next26_224.io.enable <> bb_for_body32.io.Out(32)

  Gep_tmp625.io.enable <> bb_for_body32.io.Out(33)

  Gep_tmp726.io.enable <> bb_for_body32.io.Out(34)

  ld_27.io.enable <> bb_for_body32.io.Out(35)

  FP_add_328.io.enable <> bb_for_body32.io.Out(36)

  st_29.io.enable <> bb_for_body32.io.Out(37)

  binaryOp_indvars_iv_next26_330.io.enable <> bb_for_body32.io.Out(38)

  Gep_tmp831.io.enable <> bb_for_body32.io.Out(39)

  Gep_tmp932.io.enable <> bb_for_body32.io.Out(40)

  ld_33.io.enable <> bb_for_body32.io.Out(41)

  FP_add_434.io.enable <> bb_for_body32.io.Out(42)

  st_35.io.enable <> bb_for_body32.io.Out(43)

  binaryOp_indvars_iv_next26_436.io.enable <> bb_for_body32.io.Out(44)

  icmp_exitcond27_437.io.enable <> bb_for_body32.io.Out(45)

  br_38.io.enable <> bb_for_body32.io.Out(46)


  const13.io.enable <> bb_for_end3.io.Out(0)

  const14.io.enable <> bb_for_end3.io.Out(1)

  FP_div39.io.enable <> bb_for_end3.io.Out(2)

  st_40.io.enable <> bb_for_end3.io.Out(3)

  binaryOp_indvars_iv_next2941.io.enable <> bb_for_end3.io.Out(4)

  icmp_exitcond3042.io.enable <> bb_for_end3.io.Out(5)

  br_43.io.enable <> bb_for_end3.io.Out(6)


  br_44.io.enable <> bb_for_cond18_preheader_preheader4.io.Out(0)


  const15.io.enable <> bb_for_cond18_preheader5.io.Out(0)

  phi_indvars_iv2245.io.enable <> bb_for_cond18_preheader5.io.Out(1)

  br_46.io.enable <> bb_for_cond18_preheader5.io.Out(2)


  const16.io.enable <> bb_for_body206.io.Out(0)

  const17.io.enable <> bb_for_body206.io.Out(1)

  const18.io.enable <> bb_for_body206.io.Out(2)

  const19.io.enable <> bb_for_body206.io.Out(3)

  const20.io.enable <> bb_for_body206.io.Out(4)

  const21.io.enable <> bb_for_body206.io.Out(5)

  const22.io.enable <> bb_for_body206.io.Out(6)

  const23.io.enable <> bb_for_body206.io.Out(7)

  const24.io.enable <> bb_for_body206.io.Out(8)

  const25.io.enable <> bb_for_body206.io.Out(9)

  phi_indvars_iv1947.io.enable <> bb_for_body206.io.Out(10)

  Gep_arrayidx2248.io.enable <> bb_for_body206.io.Out(11)

  ld_49.io.enable <> bb_for_body206.io.Out(12)

  Gep_tmp1050.io.enable <> bb_for_body206.io.Out(13)

  Gep_tmp1151.io.enable <> bb_for_body206.io.Out(14)

  ld_52.io.enable <> bb_for_body206.io.Out(15)

  binaryOp_sub53.io.enable <> bb_for_body206.io.Out(16)

  st_54.io.enable <> bb_for_body206.io.Out(17)

  binaryOp_indvars_iv_next2055.io.enable <> bb_for_body206.io.Out(18)

  Gep_arrayidx22_156.io.enable <> bb_for_body206.io.Out(19)

  ld_57.io.enable <> bb_for_body206.io.Out(20)

  Gep_tmp1258.io.enable <> bb_for_body206.io.Out(21)

  Gep_tmp1359.io.enable <> bb_for_body206.io.Out(22)

  ld_60.io.enable <> bb_for_body206.io.Out(23)

  binaryOp_sub_161.io.enable <> bb_for_body206.io.Out(24)

  st_62.io.enable <> bb_for_body206.io.Out(25)

  binaryOp_indvars_iv_next20_163.io.enable <> bb_for_body206.io.Out(26)

  Gep_arrayidx22_264.io.enable <> bb_for_body206.io.Out(27)

  ld_65.io.enable <> bb_for_body206.io.Out(28)

  Gep_tmp1466.io.enable <> bb_for_body206.io.Out(29)

  Gep_tmp1567.io.enable <> bb_for_body206.io.Out(30)

  ld_68.io.enable <> bb_for_body206.io.Out(31)

  binaryOp_sub_269.io.enable <> bb_for_body206.io.Out(32)

  st_70.io.enable <> bb_for_body206.io.Out(33)

  binaryOp_indvars_iv_next20_271.io.enable <> bb_for_body206.io.Out(34)

  Gep_arrayidx22_372.io.enable <> bb_for_body206.io.Out(35)

  ld_73.io.enable <> bb_for_body206.io.Out(36)

  Gep_tmp1674.io.enable <> bb_for_body206.io.Out(37)

  Gep_tmp1775.io.enable <> bb_for_body206.io.Out(38)

  ld_76.io.enable <> bb_for_body206.io.Out(39)

  binaryOp_sub_377.io.enable <> bb_for_body206.io.Out(40)

  st_78.io.enable <> bb_for_body206.io.Out(41)

  binaryOp_indvars_iv_next20_379.io.enable <> bb_for_body206.io.Out(42)

  icmp_exitcond21_380.io.enable <> bb_for_body206.io.Out(43)

  br_81.io.enable <> bb_for_body206.io.Out(44)


  const26.io.enable <> bb_for_inc307.io.Out(0)

  const27.io.enable <> bb_for_inc307.io.Out(1)

  binaryOp_indvars_iv_next2382.io.enable <> bb_for_inc307.io.Out(2)

  icmp_exitcond2483.io.enable <> bb_for_inc307.io.Out(3)

  br_84.io.enable <> bb_for_inc307.io.Out(4)


  br_85.io.enable <> bb_for_body38_lr_ph_preheader8.io.Out(0)


  const28.io.enable <> bb_for_body38_lr_ph9.io.Out(0)

  phi_indvars_iv1686.io.enable <> bb_for_body38_lr_ph9.io.Out(1)

  br_87.io.enable <> bb_for_body38_lr_ph9.io.Out(2)


  constf2.io.enable <> bb_for_body3810.io.Out(1)

  const29.io.enable <> bb_for_body3810.io.Out(0)

  phi_indvars_iv1288.io.enable <> bb_for_body3810.io.Out(2)

  Gep_tmp1889.io.enable <> bb_for_body3810.io.Out(3)

  Gep_tmp1990.io.enable <> bb_for_body3810.io.Out(4)

  st_91.io.enable <> bb_for_body3810.io.Out(5)

  br_92.io.enable <> bb_for_body3810.io.Out(6)


  constf3.io.enable <> bb_for_body4511.io.Out(1)

  const30.io.enable <> bb_for_body4511.io.Out(0)

  const31.io.enable <> bb_for_body4511.io.Out(2)

  const32.io.enable <> bb_for_body4511.io.Out(3)

  const33.io.enable <> bb_for_body4511.io.Out(4)

  const34.io.enable <> bb_for_body4511.io.Out(5)

  const35.io.enable <> bb_for_body4511.io.Out(6)

  const36.io.enable <> bb_for_body4511.io.Out(7)

  const37.io.enable <> bb_for_body4511.io.Out(8)

  phi_indvars_iv93.io.enable <> bb_for_body4511.io.Out(9)

  phi_94.io.enable <> bb_for_body4511.io.Out(10)

  Gep_tmp2095.io.enable <> bb_for_body4511.io.Out(11)

  Gep_tmp2196.io.enable <> bb_for_body4511.io.Out(12)

  ld_97.io.enable <> bb_for_body4511.io.Out(13)

  Gep_tmp2298.io.enable <> bb_for_body4511.io.Out(14)

  Gep_tmp2399.io.enable <> bb_for_body4511.io.Out(15)

  ld_100.io.enable <> bb_for_body4511.io.Out(16)

  binaryOp_mul101.io.enable <> bb_for_body4511.io.Out(17)

  FP_add58102.io.enable <> bb_for_body4511.io.Out(18)

  st_103.io.enable <> bb_for_body4511.io.Out(19)

  binaryOp_indvars_iv_next104.io.enable <> bb_for_body4511.io.Out(20)

  Gep_tmp24105.io.enable <> bb_for_body4511.io.Out(21)

  Gep_tmp25106.io.enable <> bb_for_body4511.io.Out(22)

  ld_107.io.enable <> bb_for_body4511.io.Out(23)

  Gep_tmp26108.io.enable <> bb_for_body4511.io.Out(24)

  Gep_tmp27109.io.enable <> bb_for_body4511.io.Out(25)

  ld_110.io.enable <> bb_for_body4511.io.Out(26)

  binaryOp_mul_1111.io.enable <> bb_for_body4511.io.Out(27)

  FP_add58_1112.io.enable <> bb_for_body4511.io.Out(28)

  st_113.io.enable <> bb_for_body4511.io.Out(29)

  binaryOp_indvars_iv_next_1114.io.enable <> bb_for_body4511.io.Out(30)

  icmp_exitcond_1115.io.enable <> bb_for_body4511.io.Out(31)

  br_116.io.enable <> bb_for_body4511.io.Out(32)


  const38.io.enable <> bb_for_end6112.io.Out(0)

  const39.io.enable <> bb_for_end6112.io.Out(1)

  const40.io.enable <> bb_for_end6112.io.Out(2)

  Gep_tmp28117.io.enable <> bb_for_end6112.io.Out(3)

  Gep_tmp29118.io.enable <> bb_for_end6112.io.Out(4)

  st_119.io.enable <> bb_for_end6112.io.Out(5)

  binaryOp_indvars_iv_next13120.io.enable <> bb_for_end6112.io.Out(6)

  icmp_exitcond14121.io.enable <> bb_for_end6112.io.Out(7)

  br_122.io.enable <> bb_for_end6112.io.Out(8)


  const41.io.enable <> bb_for_inc7313.io.Out(0)

  const42.io.enable <> bb_for_inc7313.io.Out(1)

  binaryOp_indvars_iv_next17123.io.enable <> bb_for_inc7313.io.Out(2)

  icmp_exitcond18124.io.enable <> bb_for_inc7313.io.Out(3)

  br_125.io.enable <> bb_for_inc7313.io.Out(4)


  ret_126.io.In.enable <> bb_for_end7514.io.Out(0)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  phi_indvars_iv281.io.Mask <> bb_for_body1.io.MaskBB(0)

  phi_indvars_iv255.io.Mask <> bb_for_body32.io.MaskBB(0)

  phi_6.io.Mask <> bb_for_body32.io.MaskBB(1)

  phi_indvars_iv2245.io.Mask <> bb_for_cond18_preheader5.io.MaskBB(0)

  phi_indvars_iv1947.io.Mask <> bb_for_body206.io.MaskBB(0)

  phi_indvars_iv1686.io.Mask <> bb_for_body38_lr_ph9.io.MaskBB(0)

  phi_indvars_iv1288.io.Mask <> bb_for_body3810.io.MaskBB(0)

  phi_indvars_iv93.io.Mask <> bb_for_body4511.io.MaskBB(0)

  phi_94.io.Mask <> bb_for_body4511.io.MaskBB(1)



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

  MemCtrl.io.ReadIn(1) <> ld_15.io.memReq

  ld_15.io.memResp <> MemCtrl.io.ReadOut(1)

  MemCtrl.io.WriteIn(2) <> st_17.io.memReq

  st_17.io.memResp <> MemCtrl.io.WriteOut(2)

  MemCtrl.io.ReadIn(2) <> ld_21.io.memReq

  ld_21.io.memResp <> MemCtrl.io.ReadOut(2)

  MemCtrl.io.WriteIn(3) <> st_23.io.memReq

  st_23.io.memResp <> MemCtrl.io.WriteOut(3)

  MemCtrl.io.ReadIn(3) <> ld_27.io.memReq

  ld_27.io.memResp <> MemCtrl.io.ReadOut(3)

  MemCtrl.io.WriteIn(4) <> st_29.io.memReq

  st_29.io.memResp <> MemCtrl.io.WriteOut(4)

  MemCtrl.io.ReadIn(4) <> ld_33.io.memReq

  ld_33.io.memResp <> MemCtrl.io.ReadOut(4)

  MemCtrl.io.WriteIn(5) <> st_35.io.memReq

  st_35.io.memResp <> MemCtrl.io.WriteOut(5)

  MemCtrl.io.WriteIn(6) <> st_40.io.memReq

  st_40.io.memResp <> MemCtrl.io.WriteOut(6)

  MemCtrl.io.ReadIn(5) <> ld_49.io.memReq

  ld_49.io.memResp <> MemCtrl.io.ReadOut(5)

  MemCtrl.io.ReadIn(6) <> ld_52.io.memReq

  ld_52.io.memResp <> MemCtrl.io.ReadOut(6)

  MemCtrl.io.WriteIn(7) <> st_54.io.memReq

  st_54.io.memResp <> MemCtrl.io.WriteOut(7)

  MemCtrl.io.ReadIn(7) <> ld_57.io.memReq

  ld_57.io.memResp <> MemCtrl.io.ReadOut(7)

  MemCtrl.io.ReadIn(8) <> ld_60.io.memReq

  ld_60.io.memResp <> MemCtrl.io.ReadOut(8)

  MemCtrl.io.WriteIn(8) <> st_62.io.memReq

  st_62.io.memResp <> MemCtrl.io.WriteOut(8)

  MemCtrl.io.ReadIn(9) <> ld_65.io.memReq

  ld_65.io.memResp <> MemCtrl.io.ReadOut(9)

  MemCtrl.io.ReadIn(10) <> ld_68.io.memReq

  ld_68.io.memResp <> MemCtrl.io.ReadOut(10)

  MemCtrl.io.WriteIn(9) <> st_70.io.memReq

  st_70.io.memResp <> MemCtrl.io.WriteOut(9)

  MemCtrl.io.ReadIn(11) <> ld_73.io.memReq

  ld_73.io.memResp <> MemCtrl.io.ReadOut(11)

  MemCtrl.io.ReadIn(12) <> ld_76.io.memReq

  ld_76.io.memResp <> MemCtrl.io.ReadOut(12)

  MemCtrl.io.WriteIn(10) <> st_78.io.memReq

  st_78.io.memResp <> MemCtrl.io.WriteOut(10)

  MemCtrl.io.WriteIn(11) <> st_91.io.memReq

  st_91.io.memResp <> MemCtrl.io.WriteOut(11)

  MemCtrl.io.ReadIn(13) <> ld_97.io.memReq

  ld_97.io.memResp <> MemCtrl.io.ReadOut(13)

  MemCtrl.io.ReadIn(14) <> ld_100.io.memReq

  ld_100.io.memResp <> MemCtrl.io.ReadOut(14)

  MemCtrl.io.WriteIn(12) <> st_103.io.memReq

  st_103.io.memResp <> MemCtrl.io.WriteOut(12)

  MemCtrl.io.ReadIn(15) <> ld_107.io.memReq

  ld_107.io.memResp <> MemCtrl.io.ReadOut(15)

  MemCtrl.io.ReadIn(16) <> ld_110.io.memReq

  ld_110.io.memResp <> MemCtrl.io.ReadOut(16)

  MemCtrl.io.WriteIn(13) <> st_113.io.memReq

  st_113.io.memResp <> MemCtrl.io.WriteOut(13)

  MemCtrl.io.WriteIn(14) <> st_119.io.memReq

  st_119.io.memResp <> MemCtrl.io.WriteOut(14)



  /* ================================================================== *
   *                   PRINT SHARED CONNECTIONS                         *
   * ================================================================== */

  SharedFPU.io.InData(0) <> FP_div39.io.FUReq
  FP_div39.io.FUResp <> SharedFPU.io.OutData(0)

  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  phi_indvars_iv281.io.InData(0) <> const0.io.Out

  phi_indvars_iv255.io.InData(0) <> const1.io.Out

  Gep_tmp18.io.idx(0) <> const2.io.Out

  binaryOp_indvars_iv_next2612.io.RightIO <> const3.io.Out

  Gep_tmp314.io.idx(0) <> const4.io.Out

  binaryOp_indvars_iv_next26_118.io.RightIO <> const5.io.Out

  Gep_tmp520.io.idx(0) <> const6.io.Out

  binaryOp_indvars_iv_next26_224.io.RightIO <> const7.io.Out

  Gep_tmp726.io.idx(0) <> const8.io.Out

  binaryOp_indvars_iv_next26_330.io.RightIO <> const9.io.Out

  Gep_tmp932.io.idx(0) <> const10.io.Out

  binaryOp_indvars_iv_next26_436.io.RightIO <> const11.io.Out

  icmp_exitcond27_437.io.RightIO <> const12.io.Out

  binaryOp_indvars_iv_next2941.io.RightIO <> const13.io.Out

  icmp_exitcond3042.io.RightIO <> const14.io.Out

  phi_indvars_iv2245.io.InData(1) <> const15.io.Out

  phi_indvars_iv1947.io.InData(0) <> const16.io.Out

  Gep_tmp1151.io.idx(0) <> const17.io.Out

  binaryOp_indvars_iv_next2055.io.RightIO <> const18.io.Out

  Gep_tmp1359.io.idx(0) <> const19.io.Out

  binaryOp_indvars_iv_next20_163.io.RightIO <> const20.io.Out

  Gep_tmp1567.io.idx(0) <> const21.io.Out

  binaryOp_indvars_iv_next20_271.io.RightIO <> const22.io.Out

  Gep_tmp1775.io.idx(0) <> const23.io.Out

  binaryOp_indvars_iv_next20_379.io.RightIO <> const24.io.Out

  icmp_exitcond21_380.io.RightIO <> const25.io.Out

  binaryOp_indvars_iv_next2382.io.RightIO <> const26.io.Out

  icmp_exitcond2483.io.RightIO <> const27.io.Out

  phi_indvars_iv1686.io.InData(1) <> const28.io.Out

  Gep_tmp1990.io.idx(0) <> const29.io.Out

  phi_indvars_iv93.io.InData(0) <> const30.io.Out

  Gep_tmp2196.io.idx(0) <> const31.io.Out

  Gep_tmp2399.io.idx(0) <> const32.io.Out

  binaryOp_indvars_iv_next104.io.RightIO <> const33.io.Out

  Gep_tmp25106.io.idx(0) <> const34.io.Out

  Gep_tmp27109.io.idx(0) <> const35.io.Out

  binaryOp_indvars_iv_next_1114.io.RightIO <> const36.io.Out

  icmp_exitcond_1115.io.RightIO <> const37.io.Out

  Gep_tmp29118.io.idx(0) <> const38.io.Out

  binaryOp_indvars_iv_next13120.io.RightIO <> const39.io.Out

  icmp_exitcond14121.io.RightIO <> const40.io.Out

  binaryOp_indvars_iv_next17123.io.RightIO <> const41.io.Out

  icmp_exitcond18124.io.RightIO <> const42.io.Out

  st_3.io.inData <> constf0.io.Out(0)

  phi_6.io.InData(0) <> constf1.io.Out(0)

  st_91.io.inData <> constf2.io.Out(0)

  phi_94.io.InData(0) <> constf3.io.Out(0)

  Gep_arrayidx2.io.idx(0) <> phi_indvars_iv281.io.Out(1)

  binaryOp_indvars_iv_next2941.io.LeftIO <> phi_indvars_iv281.io.Out(0)

  st_3.io.GepAddr <> Gep_arrayidx2.io.Out.data(1)

  st_40.io.GepAddr <> Gep_arrayidx2.io.Out.data(1)

  Gep_tmp7.io.idx(0) <> phi_indvars_iv255.io.Out(1)

  binaryOp_indvars_iv_next2612.io.LeftIO <> phi_indvars_iv255.io.Out(0)

  binaryOp_indvars_iv_next26_118.io.LeftIO <> phi_indvars_iv255.io.Out(0)

  binaryOp_indvars_iv_next26_224.io.LeftIO <> phi_indvars_iv255.io.Out(0)

  binaryOp_indvars_iv_next26_330.io.LeftIO <> phi_indvars_iv255.io.Out(0)

  binaryOp_indvars_iv_next26_436.io.LeftIO <> phi_indvars_iv255.io.Out(0)

  FP_add10.io.LeftIO <> phi_6.io.Out(0)

  Gep_tmp18.io.baseAddress <> Gep_tmp7.io.Out.data(0)

  ld_9.io.GepAddr <> Gep_tmp18.io.Out.data(0)

  FP_add10.io.RightIO <> ld_9.io.Out.data(1)

  st_11.io.inData <> FP_add10.io.Out(0)

  FP_add_116.io.LeftIO <> FP_add10.io.Out(0)

  Gep_tmp213.io.idx(0) <> binaryOp_indvars_iv_next2612.io.Out(1)

  Gep_tmp314.io.baseAddress <> Gep_tmp213.io.Out.data(0)

  ld_15.io.GepAddr <> Gep_tmp314.io.Out.data(0)

  FP_add_116.io.RightIO <> ld_15.io.Out.data(1)

  st_17.io.inData <> FP_add_116.io.Out(0)

  FP_add_222.io.LeftIO <> FP_add_116.io.Out(0)

  Gep_tmp419.io.idx(0) <> binaryOp_indvars_iv_next26_118.io.Out(1)

  Gep_tmp520.io.baseAddress <> Gep_tmp419.io.Out.data(0)

  ld_21.io.GepAddr <> Gep_tmp520.io.Out.data(0)

  FP_add_222.io.RightIO <> ld_21.io.Out.data(1)

  st_23.io.inData <> FP_add_222.io.Out(0)

  FP_add_328.io.LeftIO <> FP_add_222.io.Out(0)

  Gep_tmp625.io.idx(0) <> binaryOp_indvars_iv_next26_224.io.Out(1)

  Gep_tmp726.io.baseAddress <> Gep_tmp625.io.Out.data(0)

  ld_27.io.GepAddr <> Gep_tmp726.io.Out.data(0)

  FP_add_328.io.RightIO <> ld_27.io.Out.data(1)

  st_29.io.inData <> FP_add_328.io.Out(0)

  FP_add_434.io.LeftIO <> FP_add_328.io.Out(0)

  Gep_tmp831.io.idx(0) <> binaryOp_indvars_iv_next26_330.io.Out(1)

  Gep_tmp932.io.baseAddress <> Gep_tmp831.io.Out.data(0)

  ld_33.io.GepAddr <> Gep_tmp932.io.Out.data(0)

  FP_add_434.io.RightIO <> ld_33.io.Out.data(1)

  phi_6.io.InData(1) <> FP_add_434.io.Out(1)

  st_35.io.inData <> FP_add_434.io.Out(0)

  phi_indvars_iv255.io.InData(1) <> binaryOp_indvars_iv_next26_436.io.Out(1)

  icmp_exitcond27_437.io.LeftIO <> binaryOp_indvars_iv_next26_436.io.Out(0)

  br_38.io.CmpIO <> icmp_exitcond27_437.io.Out(0)

  st_40.io.inData <> FP_div39.io.Out(0)

  phi_indvars_iv281.io.InData(1) <> binaryOp_indvars_iv_next2941.io.Out(1)

  icmp_exitcond3042.io.LeftIO <> binaryOp_indvars_iv_next2941.io.Out(0)

  br_43.io.CmpIO <> icmp_exitcond3042.io.Out(0)

  binaryOp_indvars_iv_next2382.io.LeftIO <> phi_indvars_iv2245.io.Out(0)

  Gep_arrayidx2248.io.idx(0) <> phi_indvars_iv1947.io.Out(1)

  Gep_tmp1151.io.idx(1) <> phi_indvars_iv1947.io.Out(2)

  binaryOp_indvars_iv_next2055.io.LeftIO <> phi_indvars_iv1947.io.Out(0)

  binaryOp_indvars_iv_next20_163.io.LeftIO <> phi_indvars_iv1947.io.Out(0)

  binaryOp_indvars_iv_next20_271.io.LeftIO <> phi_indvars_iv1947.io.Out(0)

  binaryOp_indvars_iv_next20_379.io.LeftIO <> phi_indvars_iv1947.io.Out(0)

  ld_49.io.GepAddr <> Gep_arrayidx2248.io.Out.data(0)

  binaryOp_sub53.io.RightIO <> ld_49.io.Out.data(1)

  Gep_tmp1151.io.baseAddress <> Gep_tmp1050.io.Out.data(0)

  ld_52.io.GepAddr <> Gep_tmp1151.io.Out.data(0)

  st_54.io.GepAddr <> Gep_tmp1151.io.Out.data(1)

  binaryOp_sub53.io.LeftIO <> ld_52.io.Out.data(0)

  st_54.io.inData <> binaryOp_sub53.io.Out(0)

  Gep_arrayidx22_156.io.idx(0) <> binaryOp_indvars_iv_next2055.io.Out(1)

  Gep_tmp1359.io.idx(1) <> binaryOp_indvars_iv_next2055.io.Out(2)

  ld_57.io.GepAddr <> Gep_arrayidx22_156.io.Out.data(0)

  binaryOp_sub_161.io.RightIO <> ld_57.io.Out.data(1)

  Gep_tmp1359.io.baseAddress <> Gep_tmp1258.io.Out.data(0)

  ld_60.io.GepAddr <> Gep_tmp1359.io.Out.data(0)

  st_62.io.GepAddr <> Gep_tmp1359.io.Out.data(1)

  binaryOp_sub_161.io.LeftIO <> ld_60.io.Out.data(0)

  st_62.io.inData <> binaryOp_sub_161.io.Out(0)

  Gep_arrayidx22_264.io.idx(0) <> binaryOp_indvars_iv_next20_163.io.Out(1)

  Gep_tmp1567.io.idx(1) <> binaryOp_indvars_iv_next20_163.io.Out(2)

  ld_65.io.GepAddr <> Gep_arrayidx22_264.io.Out.data(0)

  binaryOp_sub_269.io.RightIO <> ld_65.io.Out.data(1)

  Gep_tmp1567.io.baseAddress <> Gep_tmp1466.io.Out.data(0)

  ld_68.io.GepAddr <> Gep_tmp1567.io.Out.data(0)

  st_70.io.GepAddr <> Gep_tmp1567.io.Out.data(1)

  binaryOp_sub_269.io.LeftIO <> ld_68.io.Out.data(0)

  st_70.io.inData <> binaryOp_sub_269.io.Out(0)

  Gep_arrayidx22_372.io.idx(0) <> binaryOp_indvars_iv_next20_271.io.Out(1)

  Gep_tmp1775.io.idx(1) <> binaryOp_indvars_iv_next20_271.io.Out(2)

  ld_73.io.GepAddr <> Gep_arrayidx22_372.io.Out.data(0)

  binaryOp_sub_377.io.RightIO <> ld_73.io.Out.data(1)

  Gep_tmp1775.io.baseAddress <> Gep_tmp1674.io.Out.data(0)

  ld_76.io.GepAddr <> Gep_tmp1775.io.Out.data(0)

  st_78.io.GepAddr <> Gep_tmp1775.io.Out.data(1)

  binaryOp_sub_377.io.LeftIO <> ld_76.io.Out.data(0)

  st_78.io.inData <> binaryOp_sub_377.io.Out(0)

  phi_indvars_iv1947.io.InData(1) <> binaryOp_indvars_iv_next20_379.io.Out(1)

  icmp_exitcond21_380.io.LeftIO <> binaryOp_indvars_iv_next20_379.io.Out(0)

  br_81.io.CmpIO <> icmp_exitcond21_380.io.Out(0)

  phi_indvars_iv2245.io.InData(0) <> binaryOp_indvars_iv_next2382.io.Out(0)

  icmp_exitcond2483.io.LeftIO <> binaryOp_indvars_iv_next2382.io.Out(0)

  br_84.io.CmpIO <> icmp_exitcond2483.io.Out(0)

  binaryOp_indvars_iv_next17123.io.LeftIO <> phi_indvars_iv1686.io.Out(0)

  Gep_tmp1990.io.idx(1) <> phi_indvars_iv1288.io.Out(2)

  Gep_tmp28117.io.idx(0) <> phi_indvars_iv1288.io.Out(1)

  binaryOp_indvars_iv_next13120.io.LeftIO <> phi_indvars_iv1288.io.Out(0)

  Gep_tmp1990.io.baseAddress <> Gep_tmp1889.io.Out.data(0)

  st_91.io.GepAddr <> Gep_tmp1990.io.Out.data(1)

  Gep_tmp2095.io.idx(0) <> phi_indvars_iv93.io.Out(1)

  Gep_tmp2298.io.idx(0) <> phi_indvars_iv93.io.Out(1)

  binaryOp_indvars_iv_next104.io.LeftIO <> phi_indvars_iv93.io.Out(0)

  binaryOp_indvars_iv_next_1114.io.LeftIO <> phi_indvars_iv93.io.Out(0)

  FP_add58102.io.LeftIO <> phi_94.io.Out(0)

  Gep_tmp2196.io.baseAddress <> Gep_tmp2095.io.Out.data(0)

  ld_97.io.GepAddr <> Gep_tmp2196.io.Out.data(0)

  binaryOp_mul101.io.LeftIO <> ld_97.io.Out.data(0)

  Gep_tmp2399.io.baseAddress <> Gep_tmp2298.io.Out.data(0)

  ld_100.io.GepAddr <> Gep_tmp2399.io.Out.data(0)

  binaryOp_mul101.io.RightIO <> ld_100.io.Out.data(1)

  FP_add58102.io.RightIO <> binaryOp_mul101.io.Out(1)

  st_103.io.inData <> FP_add58102.io.Out(0)

  FP_add58_1112.io.LeftIO <> FP_add58102.io.Out(0)

  Gep_tmp24105.io.idx(0) <> binaryOp_indvars_iv_next104.io.Out(1)

  Gep_tmp26108.io.idx(0) <> binaryOp_indvars_iv_next104.io.Out(1)

  Gep_tmp25106.io.baseAddress <> Gep_tmp24105.io.Out.data(0)

  ld_107.io.GepAddr <> Gep_tmp25106.io.Out.data(0)

  binaryOp_mul_1111.io.LeftIO <> ld_107.io.Out.data(0)

  Gep_tmp27109.io.baseAddress <> Gep_tmp26108.io.Out.data(0)

  ld_110.io.GepAddr <> Gep_tmp27109.io.Out.data(0)

  binaryOp_mul_1111.io.RightIO <> ld_110.io.Out.data(1)

  FP_add58_1112.io.RightIO <> binaryOp_mul_1111.io.Out(1)

  phi_94.io.InData(1) <> FP_add58_1112.io.Out(1)

  st_113.io.inData <> FP_add58_1112.io.Out(0)

  phi_indvars_iv93.io.InData(1) <> binaryOp_indvars_iv_next_1114.io.Out(1)

  icmp_exitcond_1115.io.LeftIO <> binaryOp_indvars_iv_next_1114.io.Out(0)

  br_116.io.CmpIO <> icmp_exitcond_1115.io.Out(0)

  Gep_tmp29118.io.baseAddress <> Gep_tmp28117.io.Out.data(0)

  st_119.io.GepAddr <> Gep_tmp29118.io.Out.data(1)

  phi_indvars_iv1288.io.InData(1) <> binaryOp_indvars_iv_next13120.io.Out(1)

  icmp_exitcond14121.io.LeftIO <> binaryOp_indvars_iv_next13120.io.Out(0)

  br_122.io.CmpIO <> icmp_exitcond14121.io.Out(0)

  phi_indvars_iv1686.io.InData(0) <> binaryOp_indvars_iv_next17123.io.Out(0)

  icmp_exitcond18124.io.LeftIO <> binaryOp_indvars_iv_next17123.io.Out(0)

  br_125.io.CmpIO <> icmp_exitcond18124.io.Out(0)

  st_119.io.inData <> Loop_0.io.Out(0)

  FP_div39.io.a <> Loop_5.io.Out(0)

  st_3.io.Out(0).ready := true.B

  st_11.io.Out(0).ready := true.B

  st_17.io.Out(0).ready := true.B

  st_23.io.Out(0).ready := true.B

  st_29.io.Out(0).ready := true.B

  st_35.io.Out(0).ready := true.B

  st_40.io.Out(0).ready := true.B

  st_54.io.Out(0).ready := true.B

  st_62.io.Out(0).ready := true.B

  st_70.io.Out(0).ready := true.B

  st_78.io.Out(0).ready := true.B

  st_91.io.Out(0).ready := true.B

  st_103.io.Out(0).ready := true.B

  st_113.io.Out(0).ready := true.B

  st_119.io.Out(0).ready := true.B



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_126.io.Out

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
