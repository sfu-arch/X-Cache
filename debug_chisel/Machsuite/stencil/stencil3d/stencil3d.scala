package dandelion.generator

import chipsalliance.rocketchip.config._
import chisel3._
import chisel3.util._
import chisel3.Module._
import chisel3.testers._
import chisel3.iotesters._
import dandelion.accel._
import dandelion.arbiters._
import dandelion.config._
import dandelion.control._
import dandelion.fpu._
import dandelion.interfaces._
import dandelion.junctions._
import dandelion.loop._
import dandelion.memory._
import dandelion.memory.stack._
import dandelion.node._
import muxes._
import org.scalatest._
import regfile._
import util._


class stencil3dDF(PtrsIn: Seq[Int] = List(64, 64, 64), ValsIn: Seq[Int] = List(), Returns: Seq[Int] = List())
			(implicit p: Parameters) extends DandelionAccelDCRModule(PtrsIn, ValsIn, Returns){


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  //Cache
  val mem_ctrl_cache = Module(new CacheMemoryEngine(ID = 0, NumRead = 15, NumWrite = 7))

  io.MemReq <> mem_ctrl_cache.io.cache.MemReq
  mem_ctrl_cache.io.cache.MemResp <> io.MemResp

  val ArgSplitter = Module(new SplitCallDCR(ptrsArgTypes = List(2, 4, 4), valsArgTypes = List()))
  ArgSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val Loop_0 = Module(new LoopBlockNode(NumIns = List(3, 1, 1, 1, 1, 1, 7, 1, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 0))

  val Loop_1 = Module(new LoopBlockNode(NumIns = List(5, 1, 1, 1, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 1))

  val Loop_2 = Module(new LoopBlockNode(NumIns = List(1, 1, 1, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 2))

  val Loop_3 = Module(new LoopBlockNode(NumIns = List(1, 2, 2), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 3))

  val Loop_4 = Module(new LoopBlockNode(NumIns = List(1, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 4))

  val Loop_5 = Module(new LoopBlockNode(NumIns = List(1, 1, 2, 2), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 5))

  val Loop_6 = Module(new LoopBlockNode(NumIns = List(1, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 6))

  val Loop_7 = Module(new LoopBlockNode(NumIns = List(2, 2, 2), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 7))

  val Loop_8 = Module(new LoopBlockNode(NumIns = List(1, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 8))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_entry1 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 1))

  val bb_height_bound_row3 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 5, NumPhi = 1, BID = 3))

  val bb_for_body37 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 19, NumPhi = 1, BID = 7))

  val bb_for_inc2023 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 5, BID = 23))

  val bb_col_bound_row_preheader27 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 27))

  val bb_col_bound_row29 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 7, NumPhi = 1, BID = 29))

  val bb_for_body2834 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 17, NumPhi = 1, BID = 34))

  val bb_for_inc5649 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 5, BID = 49))

  val bb_row_bound_col_preheader53 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 53))

  val bb_row_bound_col55 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 5, NumPhi = 1, BID = 55))

  val bb_for_body6459 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 20, NumPhi = 1, BID = 59))

  val bb_for_inc9275 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 5, BID = 75))

  val bb_loop_height79 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 3, BID = 79))

  val bb_loop_col82 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 5, NumPhi = 1, BID = 82))

  val bb_loop_row86 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 26, NumPhi = 1, BID = 86))

  val bb_for_body103103 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 42, NumPhi = 1, BID = 103))

  val bb_for_inc170142 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 5, BID = 142))

  val bb_for_inc173146 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 5, BID = 146))

  val bb_for_end175150 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 150))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  br label %height_bound_row, !UID !3, !BB_UID !4
  val br_2 = Module(new UBranchNode(ID = 2))

  //  %indvar = phi i64 [ 0, %entry ], [ %indvar.next, %for.inc20 ], !UID !5
  val phiindvar4 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 4, Res = true))

  //  %0 = shl i64 %indvar, 4, !UID !6
  val binaryOp_5 = Module(new ComputeNode(NumOuts = 1, ID = 5, opCode = "shl")(sign = false, Debug = false))

  //  br label %for.body3, !UID !7, !BB_UID !8
  val br_6 = Module(new UBranchNode(ID = 6))

  //  %indvars.iv325 = phi i64 [ 0, %height_bound_row ], [ %indvars.iv.next326, %for.body3 ], !UID !9
  val phiindvars_iv3258 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 3, ID = 8, Res = true))

  //  %1 = add nuw nsw i64 %indvars.iv325, %0, !UID !10
  val binaryOp_9 = Module(new ComputeNode(NumOuts = 2, ID = 9, opCode = "add")(sign = false, Debug = false))

  //  %arrayidx = getelementptr inbounds i32, i32* %orig, i64 %1, !UID !11
  val Gep_arrayidx10 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 10)(ElementSize = 8, ArraySize = List()))

  //  %2 = load i32, i32* %arrayidx, align 4, !tbaa !12, !UID !16
  val ld_11 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 11, RouteID = 0))

  //  %arrayidx9 = getelementptr inbounds i32, i32* %sol, i64 %1, !UID !17
  val Gep_arrayidx912 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 12)(ElementSize = 8, ArraySize = List()))

  //  store i32 %2, i32* %arrayidx9, align 4, !tbaa !12, !UID !18
  val st_13 = Module(new UnTypStoreCache(NumPredOps = 0, NumSuccOps = 1, ID = 13, RouteID = 15))

  //  %3 = add nuw nsw i64 %indvars.iv325, %0, !UID !19
  val binaryOp_14 = Module(new ComputeNode(NumOuts = 1, ID = 14, opCode = "add")(sign = false, Debug = false))

  //  %4 = add nuw nsw i64 %3, 15872, !UID !20
  val binaryOp_15 = Module(new ComputeNode(NumOuts = 2, ID = 15, opCode = "add")(sign = false, Debug = false))

  //  %arrayidx14 = getelementptr inbounds i32, i32* %orig, i64 %4, !UID !21
  val Gep_arrayidx1416 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 16)(ElementSize = 8, ArraySize = List()))

  //  %5 = load i32, i32* %arrayidx14, align 4, !tbaa !12, !UID !22
  val ld_17 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 17, RouteID = 1))

  //  %arrayidx19 = getelementptr inbounds i32, i32* %sol, i64 %4, !UID !23
  val Gep_arrayidx1918 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 18)(ElementSize = 8, ArraySize = List()))

  //  store i32 %5, i32* %arrayidx19, align 4, !tbaa !12, !UID !24
  val st_19 = Module(new UnTypStoreCache(NumPredOps = 0, NumSuccOps = 1, ID = 19, RouteID = 16))

  //  %indvars.iv.next326 = add nuw nsw i64 %indvars.iv325, 1, !UID !25
  val binaryOp_indvars_iv_next32620 = Module(new ComputeNode(NumOuts = 2, ID = 20, opCode = "add")(sign = false, Debug = false))

  //  %exitcond330 = icmp eq i64 %indvars.iv.next326, 16, !UID !26
  val icmp_exitcond33021 = Module(new ComputeNode(NumOuts = 1, ID = 21, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond330, label %for.inc20, label %for.body3, !UID !27, !BB_UID !28
  val br_22 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 2, ID = 22))

  //  %indvar.next = add nuw nsw i64 %indvar, 1, !UID !29
  val binaryOp_indvar_next24 = Module(new ComputeNode(NumOuts = 2, ID = 24, opCode = "add")(sign = false, Debug = false))

  //  %exitcond334 = icmp eq i64 %indvar.next, 32, !UID !30
  val icmp_exitcond33425 = Module(new ComputeNode(NumOuts = 1, ID = 25, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond334, label %col_bound_row.preheader, label %height_bound_row, !UID !31, !BB_UID !32
  val br_26 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 26))

  //  br label %col_bound_row, !UID !33, !BB_UID !34
  val br_28 = Module(new UBranchNode(ID = 28))

  //  %indvars.iv320 = phi i64 [ %indvars.iv.next321, %for.inc56 ], [ 1, %col_bound_row.preheader ], !UID !35
  val phiindvars_iv32030 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 30, Res = true))

  //  %6 = shl i64 %indvars.iv320, 9, !UID !36
  val binaryOp_31 = Module(new ComputeNode(NumOuts = 2, ID = 31, opCode = "shl")(sign = false, Debug = false))

  //  %7 = or i64 %6, 496, !UID !37
  val binaryOp_32 = Module(new ComputeNode(NumOuts = 1, ID = 32, opCode = "or")(sign = false, Debug = false))

  //  br label %for.body28, !UID !38, !BB_UID !39
  val br_33 = Module(new UBranchNode(ID = 33))

  //  %indvars.iv313 = phi i64 [ 0, %col_bound_row ], [ %indvars.iv.next314, %for.body28 ], !UID !40
  val phiindvars_iv31335 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 3, ID = 35, Res = true))

  //  %8 = add nuw nsw i64 %indvars.iv313, %6, !UID !41
  val binaryOp_36 = Module(new ComputeNode(NumOuts = 2, ID = 36, opCode = "add")(sign = false, Debug = false))

  //  %arrayidx34 = getelementptr inbounds i32, i32* %orig, i64 %8, !UID !42
  val Gep_arrayidx3437 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 37)(ElementSize = 8, ArraySize = List()))

  //  %9 = load i32, i32* %arrayidx34, align 4, !tbaa !12, !UID !43
  val ld_38 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 38, RouteID = 2))

  //  %arrayidx40 = getelementptr inbounds i32, i32* %sol, i64 %8, !UID !44
  val Gep_arrayidx4039 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 39)(ElementSize = 8, ArraySize = List()))

  //  store i32 %9, i32* %arrayidx40, align 4, !tbaa !12, !UID !45
  val st_40 = Module(new UnTypStoreCache(NumPredOps = 0, NumSuccOps = 1, ID = 40, RouteID = 17))

  //  %10 = add nuw nsw i64 %indvars.iv313, %7, !UID !46
  val binaryOp_41 = Module(new ComputeNode(NumOuts = 2, ID = 41, opCode = "add")(sign = false, Debug = false))

  //  %arrayidx46 = getelementptr inbounds i32, i32* %orig, i64 %10, !UID !47
  val Gep_arrayidx4642 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 42)(ElementSize = 8, ArraySize = List()))

  //  %11 = load i32, i32* %arrayidx46, align 4, !tbaa !12, !UID !48
  val ld_43 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 43, RouteID = 3))

  //  %arrayidx52 = getelementptr inbounds i32, i32* %sol, i64 %10, !UID !49
  val Gep_arrayidx5244 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 44)(ElementSize = 8, ArraySize = List()))

  //  store i32 %11, i32* %arrayidx52, align 4, !tbaa !12, !UID !50
  val st_45 = Module(new UnTypStoreCache(NumPredOps = 0, NumSuccOps = 1, ID = 45, RouteID = 18))

  //  %indvars.iv.next314 = add nuw nsw i64 %indvars.iv313, 1, !UID !51
  val binaryOp_indvars_iv_next31446 = Module(new ComputeNode(NumOuts = 2, ID = 46, opCode = "add")(sign = false, Debug = false))

  //  %exitcond317 = icmp eq i64 %indvars.iv.next314, 16, !UID !52
  val icmp_exitcond31747 = Module(new ComputeNode(NumOuts = 1, ID = 47, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond317, label %for.inc56, label %for.body28, !UID !53, !BB_UID !54
  val br_48 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 2, ID = 48))

  //  %indvars.iv.next321 = add nuw nsw i64 %indvars.iv320, 1, !UID !55
  val binaryOp_indvars_iv_next32150 = Module(new ComputeNode(NumOuts = 2, ID = 50, opCode = "add")(sign = false, Debug = false))

  //  %exitcond324 = icmp eq i64 %indvars.iv.next321, 31, !UID !56
  val icmp_exitcond32451 = Module(new ComputeNode(NumOuts = 1, ID = 51, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond324, label %row_bound_col.preheader, label %col_bound_row, !UID !57, !BB_UID !58
  val br_52 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 52))

  //  br label %row_bound_col, !UID !59, !BB_UID !60
  val br_54 = Module(new UBranchNode(ID = 54))

  //  %indvars.iv309 = phi i64 [ %indvars.iv.next310, %for.inc92 ], [ 1, %row_bound_col.preheader ], !UID !61
  val phiindvars_iv30956 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 56, Res = true))

  //  %12 = shl i64 %indvars.iv309, 5, !UID !62
  val binaryOp_57 = Module(new ComputeNode(NumOuts = 1, ID = 57, opCode = "shl")(sign = false, Debug = false))

  //  br label %for.body64, !UID !63, !BB_UID !64
  val br_58 = Module(new UBranchNode(ID = 58))

  //  %indvars.iv303 = phi i64 [ 1, %row_bound_col ], [ %indvars.iv.next304, %for.body64 ], !UID !65
  val phiindvars_iv30360 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 60, Res = true))

  //  %13 = add nuw nsw i64 %indvars.iv303, %12, !UID !66
  val binaryOp_61 = Module(new ComputeNode(NumOuts = 1, ID = 61, opCode = "add")(sign = false, Debug = false))

  //  %14 = shl nsw i64 %13, 4, !UID !67
  val binaryOp_62 = Module(new ComputeNode(NumOuts = 3, ID = 62, opCode = "shl")(sign = false, Debug = false))

  //  %arrayidx70 = getelementptr inbounds i32, i32* %orig, i64 %14, !UID !68
  val Gep_arrayidx7063 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 63)(ElementSize = 8, ArraySize = List()))

  //  %15 = load i32, i32* %arrayidx70, align 4, !tbaa !12, !UID !69
  val ld_64 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 64, RouteID = 4))

  //  %arrayidx76 = getelementptr inbounds i32, i32* %sol, i64 %14, !UID !70
  val Gep_arrayidx7665 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 65)(ElementSize = 8, ArraySize = List()))

  //  store i32 %15, i32* %arrayidx76, align 4, !tbaa !12, !UID !71
  val st_66 = Module(new UnTypStoreCache(NumPredOps = 0, NumSuccOps = 1, ID = 66, RouteID = 19))

  //  %16 = or i64 %14, 15, !UID !72
  val binaryOp_67 = Module(new ComputeNode(NumOuts = 2, ID = 67, opCode = "or")(sign = false, Debug = false))

  //  %arrayidx82 = getelementptr inbounds i32, i32* %orig, i64 %16, !UID !73
  val Gep_arrayidx8268 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 68)(ElementSize = 8, ArraySize = List()))

  //  %17 = load i32, i32* %arrayidx82, align 4, !tbaa !12, !UID !74
  val ld_69 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 69, RouteID = 5))

  //  %arrayidx88 = getelementptr inbounds i32, i32* %sol, i64 %16, !UID !75
  val Gep_arrayidx8870 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 70)(ElementSize = 8, ArraySize = List()))

  //  store i32 %17, i32* %arrayidx88, align 4, !tbaa !12, !UID !76
  val st_71 = Module(new UnTypStoreCache(NumPredOps = 0, NumSuccOps = 1, ID = 71, RouteID = 20))

  //  %indvars.iv.next304 = add nuw nsw i64 %indvars.iv303, 1, !UID !77
  val binaryOp_indvars_iv_next30472 = Module(new ComputeNode(NumOuts = 2, ID = 72, opCode = "add")(sign = false, Debug = false))

  //  %exitcond308 = icmp eq i64 %indvars.iv.next304, 31, !UID !78
  val icmp_exitcond30873 = Module(new ComputeNode(NumOuts = 1, ID = 73, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond308, label %for.inc92, label %for.body64, !UID !79, !BB_UID !80
  val br_74 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 2, ID = 74))

  //  %indvars.iv.next310 = add nuw nsw i64 %indvars.iv309, 1, !UID !81
  val binaryOp_indvars_iv_next31076 = Module(new ComputeNode(NumOuts = 2, ID = 76, opCode = "add")(sign = false, Debug = false))

  //  %exitcond312 = icmp eq i64 %indvars.iv.next310, 31, !UID !82
  val icmp_exitcond31277 = Module(new ComputeNode(NumOuts = 1, ID = 77, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond312, label %loop_height, label %row_bound_col, !UID !83, !BB_UID !84
  val br_78 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 78))

  //  %arrayidx158 = getelementptr inbounds i32, i32* %C, i64 1, !UID !85
  val Gep_arrayidx15880 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 80)(ElementSize = 8, ArraySize = List()))

  //  br label %loop_col, !UID !86, !BB_UID !87
  val br_81 = Module(new UBranchNode(ID = 81))

  //  %indvars.iv299 = phi i64 [ 1, %loop_height ], [ %indvars.iv.next300, %for.inc173 ], !UID !88
  val phiindvars_iv29983 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 83, Res = true))

  //  %18 = shl i64 %indvars.iv299, 5, !UID !89
  val binaryOp_84 = Module(new ComputeNode(NumOuts = 1, ID = 84, opCode = "shl")(sign = false, Debug = false))

  //  br label %loop_row, !UID !90, !BB_UID !91
  val br_85 = Module(new UBranchNode(ID = 85))

  //  %indvars.iv282 = phi i64 [ 1, %loop_col ], [ %indvars.iv.next283, %for.inc170 ], !UID !92
  val phiindvars_iv28287 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 6, ID = 87, Res = true))

  //  %19 = add nuw nsw i64 %indvars.iv282, %18, !UID !93
  val binaryOp_88 = Module(new ComputeNode(NumOuts = 1, ID = 88, opCode = "add")(sign = false, Debug = false))

  //  %20 = shl nsw i64 %19, 4, !UID !94
  val binaryOp_89 = Module(new ComputeNode(NumOuts = 1, ID = 89, opCode = "shl")(sign = false, Debug = false))

  //  %21 = add nuw nsw i64 %indvars.iv282, %18, !UID !95
  val binaryOp_90 = Module(new ComputeNode(NumOuts = 1, ID = 90, opCode = "add")(sign = false, Debug = false))

  //  %22 = shl i64 %21, 4, !UID !96
  val binaryOp_91 = Module(new ComputeNode(NumOuts = 1, ID = 91, opCode = "shl")(sign = false, Debug = false))

  //  %23 = add nuw nsw i64 %22, 512, !UID !97
  val binaryOp_92 = Module(new ComputeNode(NumOuts = 1, ID = 92, opCode = "add")(sign = false, Debug = false))

  //  %24 = add nuw nsw i64 %indvars.iv282, %18, !UID !98
  val binaryOp_93 = Module(new ComputeNode(NumOuts = 1, ID = 93, opCode = "add")(sign = false, Debug = false))

  //  %25 = shl i64 %24, 4, !UID !99
  val binaryOp_94 = Module(new ComputeNode(NumOuts = 1, ID = 94, opCode = "shl")(sign = false, Debug = false))

  //  %26 = add nsw i64 %25, -512, !UID !100
  val binaryOp_95 = Module(new ComputeNode(NumOuts = 1, ID = 95, opCode = "add")(sign = false, Debug = false))

  //  %27 = add nuw nsw i64 %indvars.iv282, %18, !UID !101
  val binaryOp_96 = Module(new ComputeNode(NumOuts = 1, ID = 96, opCode = "add")(sign = false, Debug = false))

  //  %28 = shl i64 %27, 4, !UID !102
  val binaryOp_97 = Module(new ComputeNode(NumOuts = 1, ID = 97, opCode = "shl")(sign = false, Debug = false))

  //  %29 = add nuw nsw i64 %28, 16, !UID !103
  val binaryOp_98 = Module(new ComputeNode(NumOuts = 1, ID = 98, opCode = "add")(sign = false, Debug = false))

  //  %30 = add nuw nsw i64 %indvars.iv282, %18, !UID !104
  val binaryOp_99 = Module(new ComputeNode(NumOuts = 1, ID = 99, opCode = "add")(sign = false, Debug = false))

  //  %31 = shl i64 %30, 4, !UID !105
  val binaryOp_100 = Module(new ComputeNode(NumOuts = 1, ID = 100, opCode = "shl")(sign = false, Debug = false))

  //  %32 = add nsw i64 %31, -16, !UID !106
  val binaryOp_101 = Module(new ComputeNode(NumOuts = 1, ID = 101, opCode = "add")(sign = false, Debug = false))

  //  br label %for.body103, !UID !107, !BB_UID !108
  val br_102 = Module(new UBranchNode(ID = 102))

  //  %indvars.iv = phi i64 [ 1, %loop_row ], [ %indvars.iv.next, %for.body103 ], !UID !109
  val phiindvars_iv104 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 7, ID = 104, Res = true))

  //  %33 = add nuw nsw i64 %indvars.iv, %20, !UID !110
  val binaryOp_105 = Module(new ComputeNode(NumOuts = 2, ID = 105, opCode = "add")(sign = false, Debug = false))

  //  %arrayidx109 = getelementptr inbounds i32, i32* %orig, i64 %33, !UID !111
  val Gep_arrayidx109106 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 106)(ElementSize = 8, ArraySize = List()))

  //  %34 = load i32, i32* %arrayidx109, align 4, !tbaa !12, !UID !112
  val ld_107 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 107, RouteID = 6))

  //  %35 = add nuw nsw i64 %indvars.iv, %23, !UID !113
  val binaryOp_108 = Module(new ComputeNode(NumOuts = 1, ID = 108, opCode = "add")(sign = false, Debug = false))

  //  %arrayidx116 = getelementptr inbounds i32, i32* %orig, i64 %35, !UID !114
  val Gep_arrayidx116109 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 109)(ElementSize = 8, ArraySize = List()))

  //  %36 = load i32, i32* %arrayidx116, align 4, !tbaa !12, !UID !115
  val ld_110 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 110, RouteID = 7))

  //  %37 = add nuw nsw i64 %indvars.iv, %26, !UID !116
  val binaryOp_111 = Module(new ComputeNode(NumOuts = 1, ID = 111, opCode = "add")(sign = false, Debug = false))

  //  %arrayidx122 = getelementptr inbounds i32, i32* %orig, i64 %37, !UID !117
  val Gep_arrayidx122112 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 112)(ElementSize = 8, ArraySize = List()))

  //  %38 = load i32, i32* %arrayidx122, align 4, !tbaa !12, !UID !118
  val ld_113 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 113, RouteID = 8))

  //  %add123 = add nsw i32 %38, %36, !UID !119
  val binaryOp_add123114 = Module(new ComputeNode(NumOuts = 1, ID = 114, opCode = "add")(sign = false, Debug = false))

  //  %39 = add nuw nsw i64 %indvars.iv, %29, !UID !120
  val binaryOp_115 = Module(new ComputeNode(NumOuts = 1, ID = 115, opCode = "add")(sign = false, Debug = false))

  //  %arrayidx130 = getelementptr inbounds i32, i32* %orig, i64 %39, !UID !121
  val Gep_arrayidx130116 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 116)(ElementSize = 8, ArraySize = List()))

  //  %40 = load i32, i32* %arrayidx130, align 4, !tbaa !12, !UID !122
  val ld_117 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 117, RouteID = 9))

  //  %add131 = add nsw i32 %add123, %40, !UID !123
  val binaryOp_add131118 = Module(new ComputeNode(NumOuts = 1, ID = 118, opCode = "add")(sign = false, Debug = false))

  //  %41 = add nuw nsw i64 %indvars.iv, %32, !UID !124
  val binaryOp_119 = Module(new ComputeNode(NumOuts = 1, ID = 119, opCode = "add")(sign = false, Debug = false))

  //  %arrayidx138 = getelementptr inbounds i32, i32* %orig, i64 %41, !UID !125
  val Gep_arrayidx138120 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 120)(ElementSize = 8, ArraySize = List()))

  //  %42 = load i32, i32* %arrayidx138, align 4, !tbaa !12, !UID !126
  val ld_121 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 121, RouteID = 10))

  //  %add139 = add nsw i32 %add131, %42, !UID !127
  val binaryOp_add139122 = Module(new ComputeNode(NumOuts = 1, ID = 122, opCode = "add")(sign = false, Debug = false))

  //  %indvars.iv.next = add nuw nsw i64 %indvars.iv, 1, !UID !128
  val binaryOp_indvars_iv_next123 = Module(new ComputeNode(NumOuts = 3, ID = 123, opCode = "add")(sign = false, Debug = false))

  //  %43 = add nuw nsw i64 %indvars.iv.next, %20, !UID !129
  val binaryOp_124 = Module(new ComputeNode(NumOuts = 1, ID = 124, opCode = "add")(sign = false, Debug = false))

  //  %arrayidx146 = getelementptr inbounds i32, i32* %orig, i64 %43, !UID !130
  val Gep_arrayidx146125 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 125)(ElementSize = 8, ArraySize = List()))

  //  %44 = load i32, i32* %arrayidx146, align 4, !tbaa !12, !UID !131
  val ld_126 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 126, RouteID = 11))

  //  %add147 = add nsw i32 %add139, %44, !UID !132
  val binaryOp_add147127 = Module(new ComputeNode(NumOuts = 1, ID = 127, opCode = "add")(sign = false, Debug = false))

  //  %45 = add nuw nsw i64 %indvars.iv, %20, !UID !133
  val binaryOp_128 = Module(new ComputeNode(NumOuts = 1, ID = 128, opCode = "add")(sign = false, Debug = false))

  //  %46 = add nsw i64 %45, -1, !UID !134
  val binaryOp_129 = Module(new ComputeNode(NumOuts = 1, ID = 129, opCode = "add")(sign = false, Debug = false))

  //  %arrayidx154 = getelementptr inbounds i32, i32* %orig, i64 %46, !UID !135
  val Gep_arrayidx154130 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 130)(ElementSize = 8, ArraySize = List()))

  //  %47 = load i32, i32* %arrayidx154, align 4, !tbaa !12, !UID !136
  val ld_131 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 131, RouteID = 12))

  //  %add155 = add nsw i32 %add147, %47, !UID !137
  val binaryOp_add155132 = Module(new ComputeNode(NumOuts = 1, ID = 132, opCode = "add")(sign = false, Debug = false))

  //  %48 = load i32, i32* %C, align 4, !tbaa !12, !UID !138
  val ld_133 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 133, RouteID = 13))

  //  %mul157 = mul nsw i32 %48, %34, !UID !139
  val binaryOp_mul157134 = Module(new ComputeNode(NumOuts = 1, ID = 134, opCode = "mul")(sign = false, Debug = false))

  //  %49 = load i32, i32* %arrayidx158, align 4, !tbaa !12, !UID !140
  val ld_135 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 135, RouteID = 14))

  //  %mul159 = mul nsw i32 %49, %add155, !UID !141
  val binaryOp_mul159136 = Module(new ComputeNode(NumOuts = 1, ID = 136, opCode = "mul")(sign = false, Debug = false))

  //  %add160 = add nsw i32 %mul159, %mul157, !UID !142
  val binaryOp_add160137 = Module(new ComputeNode(NumOuts = 1, ID = 137, opCode = "add")(sign = false, Debug = false))

  //  %arrayidx166 = getelementptr inbounds i32, i32* %sol, i64 %33, !UID !143
  val Gep_arrayidx166138 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 138)(ElementSize = 8, ArraySize = List()))

  //  store i32 %add160, i32* %arrayidx166, align 4, !tbaa !12, !UID !144
  val st_139 = Module(new UnTypStoreCache(NumPredOps = 0, NumSuccOps = 1, ID = 139, RouteID = 21))

  //  %exitcond = icmp eq i64 %indvars.iv.next, 15, !UID !145
  val icmp_exitcond140 = Module(new ComputeNode(NumOuts = 1, ID = 140, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond, label %for.inc170, label %for.body103, !UID !146, !BB_UID !147
  val br_141 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 1, ID = 141))

  //  %indvars.iv.next283 = add nuw nsw i64 %indvars.iv282, 1, !UID !148
  val binaryOp_indvars_iv_next283143 = Module(new ComputeNode(NumOuts = 2, ID = 143, opCode = "add")(sign = false, Debug = false))

  //  %exitcond298 = icmp eq i64 %indvars.iv.next283, 31, !UID !149
  val icmp_exitcond298144 = Module(new ComputeNode(NumOuts = 1, ID = 144, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond298, label %for.inc173, label %loop_row, !UID !150, !BB_UID !151
  val br_145 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 145))

  //  %indvars.iv.next300 = add nuw nsw i64 %indvars.iv299, 1, !UID !152
  val binaryOp_indvars_iv_next300147 = Module(new ComputeNode(NumOuts = 2, ID = 147, opCode = "add")(sign = false, Debug = false))

  //  %exitcond302 = icmp eq i64 %indvars.iv.next300, 31, !UID !153
  val icmp_exitcond302148 = Module(new ComputeNode(NumOuts = 1, ID = 148, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond302, label %for.end175, label %loop_col, !UID !154, !BB_UID !155
  val br_149 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 149))

  //  ret void, !UID !156, !BB_UID !157
  val ret_151 = Module(new RetNode2(retTypes = List(), ID = 151))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i64 0
  val const0 = Module(new ConstFastNode(value = 0, ID = 0))

  //i64 4
  val const1 = Module(new ConstFastNode(value = 4, ID = 1))

  //i64 0
  val const2 = Module(new ConstFastNode(value = 0, ID = 2))

  //i64 15872
  val const3 = Module(new ConstFastNode(value = 15872, ID = 3))

  //i64 1
  val const4 = Module(new ConstFastNode(value = 1, ID = 4))

  //i64 16
  val const5 = Module(new ConstFastNode(value = 16, ID = 5))

  //i64 1
  val const6 = Module(new ConstFastNode(value = 1, ID = 6))

  //i64 32
  val const7 = Module(new ConstFastNode(value = 32, ID = 7))

  //i64 1
  val const8 = Module(new ConstFastNode(value = 1, ID = 8))

  //i64 9
  val const9 = Module(new ConstFastNode(value = 9, ID = 9))

  //i64 496
  val const10 = Module(new ConstFastNode(value = 496, ID = 10))

  //i64 0
  val const11 = Module(new ConstFastNode(value = 0, ID = 11))

  //i64 1
  val const12 = Module(new ConstFastNode(value = 1, ID = 12))

  //i64 16
  val const13 = Module(new ConstFastNode(value = 16, ID = 13))

  //i64 1
  val const14 = Module(new ConstFastNode(value = 1, ID = 14))

  //i64 31
  val const15 = Module(new ConstFastNode(value = 31, ID = 15))

  //i64 1
  val const16 = Module(new ConstFastNode(value = 1, ID = 16))

  //i64 5
  val const17 = Module(new ConstFastNode(value = 5, ID = 17))

  //i64 1
  val const18 = Module(new ConstFastNode(value = 1, ID = 18))

  //i64 4
  val const19 = Module(new ConstFastNode(value = 4, ID = 19))

  //i64 15
  val const20 = Module(new ConstFastNode(value = 15, ID = 20))

  //i64 1
  val const21 = Module(new ConstFastNode(value = 1, ID = 21))

  //i64 31
  val const22 = Module(new ConstFastNode(value = 31, ID = 22))

  //i64 1
  val const23 = Module(new ConstFastNode(value = 1, ID = 23))

  //i64 31
  val const24 = Module(new ConstFastNode(value = 31, ID = 24))

  //i64 1
  val const25 = Module(new ConstFastNode(value = 1, ID = 25))

  //i64 1
  val const26 = Module(new ConstFastNode(value = 1, ID = 26))

  //i64 5
  val const27 = Module(new ConstFastNode(value = 5, ID = 27))

  //i64 1
  val const28 = Module(new ConstFastNode(value = 1, ID = 28))

  //i64 4
  val const29 = Module(new ConstFastNode(value = 4, ID = 29))

  //i64 4
  val const30 = Module(new ConstFastNode(value = 4, ID = 30))

  //i64 512
  val const31 = Module(new ConstFastNode(value = 512, ID = 31))

  //i64 4
  val const32 = Module(new ConstFastNode(value = 4, ID = 32))

  //i64 -512
  val const33 = Module(new ConstFastNode(value = -512, ID = 33))

  //i64 4
  val const34 = Module(new ConstFastNode(value = 4, ID = 34))

  //i64 16
  val const35 = Module(new ConstFastNode(value = 16, ID = 35))

  //i64 4
  val const36 = Module(new ConstFastNode(value = 4, ID = 36))

  //i64 -16
  val const37 = Module(new ConstFastNode(value = -16, ID = 37))

  //i64 1
  val const38 = Module(new ConstFastNode(value = 1, ID = 38))

  //i64 1
  val const39 = Module(new ConstFastNode(value = 1, ID = 39))

  //i64 -1
  val const40 = Module(new ConstFastNode(value = -1, ID = 40))

  //i64 15
  val const41 = Module(new ConstFastNode(value = 15, ID = 41))

  //i64 1
  val const42 = Module(new ConstFastNode(value = 1, ID = 42))

  //i64 31
  val const43 = Module(new ConstFastNode(value = 31, ID = 43))

  //i64 1
  val const44 = Module(new ConstFastNode(value = 1, ID = 44))

  //i64 31
  val const45 = Module(new ConstFastNode(value = 31, ID = 45))



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_entry1.io.predicateIn(0) <> ArgSplitter.io.Out.enable



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE LOOP                     *
   * ================================================================== */

  bb_height_bound_row3.io.predicateIn(1) <> Loop_8.io.activate_loop_start

  bb_height_bound_row3.io.predicateIn(0) <> Loop_8.io.activate_loop_back

  bb_for_body37.io.predicateIn(1) <> Loop_7.io.activate_loop_start

  bb_for_body37.io.predicateIn(0) <> Loop_7.io.activate_loop_back

  bb_for_inc2023.io.predicateIn(0) <> Loop_7.io.loopExit(0)

  bb_col_bound_row_preheader27.io.predicateIn(0) <> Loop_8.io.loopExit(0)

  bb_col_bound_row29.io.predicateIn(0) <> Loop_6.io.activate_loop_start

  bb_col_bound_row29.io.predicateIn(1) <> Loop_6.io.activate_loop_back

  bb_for_body2834.io.predicateIn(1) <> Loop_5.io.activate_loop_start

  bb_for_body2834.io.predicateIn(0) <> Loop_5.io.activate_loop_back

  bb_for_inc5649.io.predicateIn(0) <> Loop_5.io.loopExit(0)

  bb_row_bound_col_preheader53.io.predicateIn(0) <> Loop_6.io.loopExit(0)

  bb_row_bound_col55.io.predicateIn(0) <> Loop_4.io.activate_loop_start

  bb_row_bound_col55.io.predicateIn(1) <> Loop_4.io.activate_loop_back

  bb_for_body6459.io.predicateIn(1) <> Loop_3.io.activate_loop_start

  bb_for_body6459.io.predicateIn(0) <> Loop_3.io.activate_loop_back

  bb_for_inc9275.io.predicateIn(0) <> Loop_3.io.loopExit(0)

  bb_loop_height79.io.predicateIn(0) <> Loop_4.io.loopExit(0)

  bb_loop_col82.io.predicateIn(1) <> Loop_2.io.activate_loop_start

  bb_loop_col82.io.predicateIn(0) <> Loop_2.io.activate_loop_back

  bb_loop_row86.io.predicateIn(1) <> Loop_1.io.activate_loop_start

  bb_loop_row86.io.predicateIn(0) <> Loop_1.io.activate_loop_back

  bb_for_body103103.io.predicateIn(1) <> Loop_0.io.activate_loop_start

  bb_for_body103103.io.predicateIn(0) <> Loop_0.io.activate_loop_back

  bb_for_inc170142.io.predicateIn(0) <> Loop_0.io.loopExit(0)

  bb_for_inc173146.io.predicateIn(0) <> Loop_1.io.loopExit(0)

  bb_for_end175150.io.predicateIn(0) <> Loop_2.io.loopExit(0)



  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */

  Loop_0.io.enable <> br_102.io.Out(0)

  Loop_0.io.loopBack(0) <> br_141.io.FalseOutput(0)

  Loop_0.io.loopFinish(0) <> br_141.io.TrueOutput(0)

  Loop_1.io.enable <> br_85.io.Out(0)

  Loop_1.io.loopBack(0) <> br_145.io.FalseOutput(0)

  Loop_1.io.loopFinish(0) <> br_145.io.TrueOutput(0)

  Loop_2.io.enable <> br_81.io.Out(0)

  Loop_2.io.loopBack(0) <> br_149.io.FalseOutput(0)

  Loop_2.io.loopFinish(0) <> br_149.io.TrueOutput(0)

  Loop_3.io.enable <> br_58.io.Out(0)

  Loop_3.io.loopBack(0) <> br_74.io.FalseOutput(0)

  Loop_3.io.loopFinish(0) <> br_74.io.TrueOutput(0)

  Loop_4.io.enable <> br_54.io.Out(0)

  Loop_4.io.loopBack(0) <> br_78.io.FalseOutput(0)

  Loop_4.io.loopFinish(0) <> br_78.io.TrueOutput(0)

  Loop_5.io.enable <> br_33.io.Out(0)

  Loop_5.io.loopBack(0) <> br_48.io.FalseOutput(0)

  Loop_5.io.loopFinish(0) <> br_48.io.TrueOutput(0)

  Loop_6.io.enable <> br_28.io.Out(0)

  Loop_6.io.loopBack(0) <> br_52.io.FalseOutput(0)

  Loop_6.io.loopFinish(0) <> br_52.io.TrueOutput(0)

  Loop_7.io.enable <> br_6.io.Out(0)

  Loop_7.io.loopBack(0) <> br_22.io.FalseOutput(0)

  Loop_7.io.loopFinish(0) <> br_22.io.TrueOutput(0)

  Loop_8.io.enable <> br_2.io.Out(0)

  Loop_8.io.loopBack(0) <> br_26.io.FalseOutput(0)

  Loop_8.io.loopFinish(0) <> br_26.io.TrueOutput(0)



  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

  Loop_0.io.InLiveIn(0) <> binaryOp_89.io.Out(0)

  Loop_0.io.InLiveIn(1) <> binaryOp_92.io.Out(0)

  Loop_0.io.InLiveIn(2) <> binaryOp_95.io.Out(0)

  Loop_0.io.InLiveIn(3) <> binaryOp_98.io.Out(0)

  Loop_0.io.InLiveIn(4) <> binaryOp_101.io.Out(0)

  Loop_0.io.InLiveIn(5) <> Loop_1.io.OutLiveIn.elements("field1")(0)

  Loop_0.io.InLiveIn(6) <> Loop_1.io.OutLiveIn.elements("field2")(0)

  Loop_0.io.InLiveIn(7) <> Loop_1.io.OutLiveIn.elements("field3")(0)

  Loop_0.io.InLiveIn(8) <> Loop_1.io.OutLiveIn.elements("field4")(0)

  Loop_1.io.InLiveIn(0) <> binaryOp_84.io.Out(0)

  Loop_1.io.InLiveIn(1) <> Loop_2.io.OutLiveIn.elements("field2")(0)

  Loop_1.io.InLiveIn(2) <> Loop_2.io.OutLiveIn.elements("field0")(0)

  Loop_1.io.InLiveIn(3) <> Loop_2.io.OutLiveIn.elements("field3")(0)

  Loop_1.io.InLiveIn(4) <> Loop_2.io.OutLiveIn.elements("field1")(0)

  Loop_2.io.InLiveIn(0) <> ArgSplitter.io.Out.dataPtrs.elements("field1")(0)

  Loop_2.io.InLiveIn(1) <> ArgSplitter.io.Out.dataPtrs.elements("field0")(0)

  Loop_2.io.InLiveIn(2) <> Gep_arrayidx15880.io.Out(0)

  Loop_2.io.InLiveIn(3) <> ArgSplitter.io.Out.dataPtrs.elements("field2")(0)

  Loop_3.io.InLiveIn(0) <> binaryOp_57.io.Out(0)

  Loop_3.io.InLiveIn(1) <> Loop_4.io.OutLiveIn.elements("field0")(0)

  Loop_3.io.InLiveIn(2) <> Loop_4.io.OutLiveIn.elements("field1")(0)

  Loop_4.io.InLiveIn(0) <> ArgSplitter.io.Out.dataPtrs.elements("field1")(1)

  Loop_4.io.InLiveIn(1) <> ArgSplitter.io.Out.dataPtrs.elements("field2")(1)

  Loop_5.io.InLiveIn(0) <> binaryOp_31.io.Out(0)

  Loop_5.io.InLiveIn(1) <> binaryOp_32.io.Out(0)

  Loop_5.io.InLiveIn(2) <> Loop_6.io.OutLiveIn.elements("field0")(0)

  Loop_5.io.InLiveIn(3) <> Loop_6.io.OutLiveIn.elements("field1")(0)

  Loop_6.io.InLiveIn(0) <> ArgSplitter.io.Out.dataPtrs.elements("field1")(2)

  Loop_6.io.InLiveIn(1) <> ArgSplitter.io.Out.dataPtrs.elements("field2")(2)

  Loop_7.io.InLiveIn(0) <> binaryOp_5.io.Out(0)

  Loop_7.io.InLiveIn(1) <> Loop_8.io.OutLiveIn.elements("field0")(0)

  Loop_7.io.InLiveIn(2) <> Loop_8.io.OutLiveIn.elements("field1")(0)

  Loop_8.io.InLiveIn(0) <> ArgSplitter.io.Out.dataPtrs.elements("field1")(3)

  Loop_8.io.InLiveIn(1) <> ArgSplitter.io.Out.dataPtrs.elements("field2")(3)



  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */

  binaryOp_105.io.RightIO <> Loop_0.io.OutLiveIn.elements("field0")(0)

  binaryOp_124.io.RightIO <> Loop_0.io.OutLiveIn.elements("field0")(1)

  binaryOp_128.io.RightIO <> Loop_0.io.OutLiveIn.elements("field0")(2)

  binaryOp_108.io.RightIO <> Loop_0.io.OutLiveIn.elements("field1")(0)

  binaryOp_111.io.RightIO <> Loop_0.io.OutLiveIn.elements("field2")(0)

  binaryOp_115.io.RightIO <> Loop_0.io.OutLiveIn.elements("field3")(0)

  binaryOp_119.io.RightIO <> Loop_0.io.OutLiveIn.elements("field4")(0)

  ld_135.io.GepAddr <> Loop_0.io.OutLiveIn.elements("field5")(0)

  Gep_arrayidx109106.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field6")(0)

  Gep_arrayidx116109.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field6")(1)

  Gep_arrayidx122112.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field6")(2)

  Gep_arrayidx130116.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field6")(3)

  Gep_arrayidx138120.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field6")(4)

  Gep_arrayidx146125.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field6")(5)

  Gep_arrayidx154130.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field6")(6)

  Gep_arrayidx166138.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field7")(0)

  ld_133.io.GepAddr <> Loop_0.io.OutLiveIn.elements("field8")(0)

  binaryOp_88.io.RightIO <> Loop_1.io.OutLiveIn.elements("field0")(0)

  binaryOp_90.io.RightIO <> Loop_1.io.OutLiveIn.elements("field0")(1)

  binaryOp_93.io.RightIO <> Loop_1.io.OutLiveIn.elements("field0")(2)

  binaryOp_96.io.RightIO <> Loop_1.io.OutLiveIn.elements("field0")(3)

  binaryOp_99.io.RightIO <> Loop_1.io.OutLiveIn.elements("field0")(4)

  binaryOp_61.io.RightIO <> Loop_3.io.OutLiveIn.elements("field0")(0)

  Gep_arrayidx7063.io.baseAddress <> Loop_3.io.OutLiveIn.elements("field1")(0)

  Gep_arrayidx8268.io.baseAddress <> Loop_3.io.OutLiveIn.elements("field1")(1)

  Gep_arrayidx7665.io.baseAddress <> Loop_3.io.OutLiveIn.elements("field2")(0)

  Gep_arrayidx8870.io.baseAddress <> Loop_3.io.OutLiveIn.elements("field2")(1)

  binaryOp_36.io.RightIO <> Loop_5.io.OutLiveIn.elements("field0")(0)

  binaryOp_41.io.RightIO <> Loop_5.io.OutLiveIn.elements("field1")(0)

  Gep_arrayidx3437.io.baseAddress <> Loop_5.io.OutLiveIn.elements("field2")(0)

  Gep_arrayidx4642.io.baseAddress <> Loop_5.io.OutLiveIn.elements("field2")(1)

  Gep_arrayidx4039.io.baseAddress <> Loop_5.io.OutLiveIn.elements("field3")(0)

  Gep_arrayidx5244.io.baseAddress <> Loop_5.io.OutLiveIn.elements("field3")(1)

  binaryOp_9.io.RightIO <> Loop_7.io.OutLiveIn.elements("field0")(0)

  binaryOp_14.io.RightIO <> Loop_7.io.OutLiveIn.elements("field0")(1)

  Gep_arrayidx10.io.baseAddress <> Loop_7.io.OutLiveIn.elements("field1")(0)

  Gep_arrayidx1416.io.baseAddress <> Loop_7.io.OutLiveIn.elements("field1")(1)

  Gep_arrayidx912.io.baseAddress <> Loop_7.io.OutLiveIn.elements("field2")(0)

  Gep_arrayidx1918.io.baseAddress <> Loop_7.io.OutLiveIn.elements("field2")(1)



  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP LIVE OUT DEPENDENCIES                       *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP CARRY DEPENDENCIES                          *
   * ================================================================== */

  Loop_0.io.CarryDepenIn(0) <> binaryOp_indvars_iv_next123.io.Out(0)

  Loop_1.io.CarryDepenIn(0) <> binaryOp_indvars_iv_next283143.io.Out(0)

  Loop_2.io.CarryDepenIn(0) <> binaryOp_indvars_iv_next300147.io.Out(0)

  Loop_3.io.CarryDepenIn(0) <> binaryOp_indvars_iv_next30472.io.Out(0)

  Loop_4.io.CarryDepenIn(0) <> binaryOp_indvars_iv_next31076.io.Out(0)

  Loop_5.io.CarryDepenIn(0) <> binaryOp_indvars_iv_next31446.io.Out(0)

  Loop_6.io.CarryDepenIn(0) <> binaryOp_indvars_iv_next32150.io.Out(0)

  Loop_7.io.CarryDepenIn(0) <> binaryOp_indvars_iv_next32620.io.Out(0)

  Loop_8.io.CarryDepenIn(0) <> binaryOp_indvar_next24.io.Out(0)



  /* ================================================================== *
   *                   LOOP DATA CARRY DEPENDENCIES                     *
   * ================================================================== */

  phiindvars_iv104.io.InData(1) <> Loop_0.io.CarryDepenOut.elements("field0")(0)

  phiindvars_iv28287.io.InData(1) <> Loop_1.io.CarryDepenOut.elements("field0")(0)

  phiindvars_iv29983.io.InData(1) <> Loop_2.io.CarryDepenOut.elements("field0")(0)

  phiindvars_iv30360.io.InData(1) <> Loop_3.io.CarryDepenOut.elements("field0")(0)

  phiindvars_iv30956.io.InData(0) <> Loop_4.io.CarryDepenOut.elements("field0")(0)

  phiindvars_iv31335.io.InData(1) <> Loop_5.io.CarryDepenOut.elements("field0")(0)

  phiindvars_iv32030.io.InData(0) <> Loop_6.io.CarryDepenOut.elements("field0")(0)

  phiindvars_iv3258.io.InData(1) <> Loop_7.io.CarryDepenOut.elements("field0")(0)

  phiindvar4.io.InData(1) <> Loop_8.io.CarryDepenOut.elements("field0")(0)



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  br_2.io.enable <> bb_entry1.io.Out(0)


  const0.io.enable <> bb_height_bound_row3.io.Out(0)

  const1.io.enable <> bb_height_bound_row3.io.Out(1)

  phiindvar4.io.enable <> bb_height_bound_row3.io.Out(2)


  binaryOp_5.io.enable <> bb_height_bound_row3.io.Out(3)


  br_6.io.enable <> bb_height_bound_row3.io.Out(4)


  const2.io.enable <> bb_for_body37.io.Out(0)

  const3.io.enable <> bb_for_body37.io.Out(1)

  const4.io.enable <> bb_for_body37.io.Out(2)

  const5.io.enable <> bb_for_body37.io.Out(3)

  phiindvars_iv3258.io.enable <> bb_for_body37.io.Out(4)


  binaryOp_9.io.enable <> bb_for_body37.io.Out(5)


  Gep_arrayidx10.io.enable <> bb_for_body37.io.Out(6)


  ld_11.io.enable <> bb_for_body37.io.Out(7)


  Gep_arrayidx912.io.enable <> bb_for_body37.io.Out(8)


  st_13.io.enable <> bb_for_body37.io.Out(9)


  binaryOp_14.io.enable <> bb_for_body37.io.Out(10)


  binaryOp_15.io.enable <> bb_for_body37.io.Out(11)


  Gep_arrayidx1416.io.enable <> bb_for_body37.io.Out(12)


  ld_17.io.enable <> bb_for_body37.io.Out(13)


  Gep_arrayidx1918.io.enable <> bb_for_body37.io.Out(14)


  st_19.io.enable <> bb_for_body37.io.Out(15)


  binaryOp_indvars_iv_next32620.io.enable <> bb_for_body37.io.Out(16)


  icmp_exitcond33021.io.enable <> bb_for_body37.io.Out(17)


  br_22.io.enable <> bb_for_body37.io.Out(18)


  const6.io.enable <> bb_for_inc2023.io.Out(0)

  const7.io.enable <> bb_for_inc2023.io.Out(1)

  binaryOp_indvar_next24.io.enable <> bb_for_inc2023.io.Out(2)


  icmp_exitcond33425.io.enable <> bb_for_inc2023.io.Out(3)


  br_26.io.enable <> bb_for_inc2023.io.Out(4)


  br_28.io.enable <> bb_col_bound_row_preheader27.io.Out(0)


  const8.io.enable <> bb_col_bound_row29.io.Out(0)

  const9.io.enable <> bb_col_bound_row29.io.Out(1)

  const10.io.enable <> bb_col_bound_row29.io.Out(2)

  phiindvars_iv32030.io.enable <> bb_col_bound_row29.io.Out(3)


  binaryOp_31.io.enable <> bb_col_bound_row29.io.Out(4)


  binaryOp_32.io.enable <> bb_col_bound_row29.io.Out(5)


  br_33.io.enable <> bb_col_bound_row29.io.Out(6)


  const11.io.enable <> bb_for_body2834.io.Out(0)

  const12.io.enable <> bb_for_body2834.io.Out(1)

  const13.io.enable <> bb_for_body2834.io.Out(2)

  phiindvars_iv31335.io.enable <> bb_for_body2834.io.Out(3)


  binaryOp_36.io.enable <> bb_for_body2834.io.Out(4)


  Gep_arrayidx3437.io.enable <> bb_for_body2834.io.Out(5)


  ld_38.io.enable <> bb_for_body2834.io.Out(6)


  Gep_arrayidx4039.io.enable <> bb_for_body2834.io.Out(7)


  st_40.io.enable <> bb_for_body2834.io.Out(8)


  binaryOp_41.io.enable <> bb_for_body2834.io.Out(9)


  Gep_arrayidx4642.io.enable <> bb_for_body2834.io.Out(10)


  ld_43.io.enable <> bb_for_body2834.io.Out(11)


  Gep_arrayidx5244.io.enable <> bb_for_body2834.io.Out(12)


  st_45.io.enable <> bb_for_body2834.io.Out(13)


  binaryOp_indvars_iv_next31446.io.enable <> bb_for_body2834.io.Out(14)


  icmp_exitcond31747.io.enable <> bb_for_body2834.io.Out(15)


  br_48.io.enable <> bb_for_body2834.io.Out(16)


  const14.io.enable <> bb_for_inc5649.io.Out(0)

  const15.io.enable <> bb_for_inc5649.io.Out(1)

  binaryOp_indvars_iv_next32150.io.enable <> bb_for_inc5649.io.Out(2)


  icmp_exitcond32451.io.enable <> bb_for_inc5649.io.Out(3)


  br_52.io.enable <> bb_for_inc5649.io.Out(4)


  br_54.io.enable <> bb_row_bound_col_preheader53.io.Out(0)


  const16.io.enable <> bb_row_bound_col55.io.Out(0)

  const17.io.enable <> bb_row_bound_col55.io.Out(1)

  phiindvars_iv30956.io.enable <> bb_row_bound_col55.io.Out(2)


  binaryOp_57.io.enable <> bb_row_bound_col55.io.Out(3)


  br_58.io.enable <> bb_row_bound_col55.io.Out(4)


  const18.io.enable <> bb_for_body6459.io.Out(0)

  const19.io.enable <> bb_for_body6459.io.Out(1)

  const20.io.enable <> bb_for_body6459.io.Out(2)

  const21.io.enable <> bb_for_body6459.io.Out(3)

  const22.io.enable <> bb_for_body6459.io.Out(4)

  phiindvars_iv30360.io.enable <> bb_for_body6459.io.Out(5)


  binaryOp_61.io.enable <> bb_for_body6459.io.Out(6)


  binaryOp_62.io.enable <> bb_for_body6459.io.Out(7)


  Gep_arrayidx7063.io.enable <> bb_for_body6459.io.Out(8)


  ld_64.io.enable <> bb_for_body6459.io.Out(9)


  Gep_arrayidx7665.io.enable <> bb_for_body6459.io.Out(10)


  st_66.io.enable <> bb_for_body6459.io.Out(11)


  binaryOp_67.io.enable <> bb_for_body6459.io.Out(12)


  Gep_arrayidx8268.io.enable <> bb_for_body6459.io.Out(13)


  ld_69.io.enable <> bb_for_body6459.io.Out(14)


  Gep_arrayidx8870.io.enable <> bb_for_body6459.io.Out(15)


  st_71.io.enable <> bb_for_body6459.io.Out(16)


  binaryOp_indvars_iv_next30472.io.enable <> bb_for_body6459.io.Out(17)


  icmp_exitcond30873.io.enable <> bb_for_body6459.io.Out(18)


  br_74.io.enable <> bb_for_body6459.io.Out(19)


  const23.io.enable <> bb_for_inc9275.io.Out(0)

  const24.io.enable <> bb_for_inc9275.io.Out(1)

  binaryOp_indvars_iv_next31076.io.enable <> bb_for_inc9275.io.Out(2)


  icmp_exitcond31277.io.enable <> bb_for_inc9275.io.Out(3)


  br_78.io.enable <> bb_for_inc9275.io.Out(4)


  const25.io.enable <> bb_loop_height79.io.Out(0)

  Gep_arrayidx15880.io.enable <> bb_loop_height79.io.Out(1)


  br_81.io.enable <> bb_loop_height79.io.Out(2)


  const26.io.enable <> bb_loop_col82.io.Out(0)

  const27.io.enable <> bb_loop_col82.io.Out(1)

  phiindvars_iv29983.io.enable <> bb_loop_col82.io.Out(2)


  binaryOp_84.io.enable <> bb_loop_col82.io.Out(3)


  br_85.io.enable <> bb_loop_col82.io.Out(4)


  const28.io.enable <> bb_loop_row86.io.Out(0)

  const29.io.enable <> bb_loop_row86.io.Out(1)

  const30.io.enable <> bb_loop_row86.io.Out(2)

  const31.io.enable <> bb_loop_row86.io.Out(3)

  const32.io.enable <> bb_loop_row86.io.Out(4)

  const33.io.enable <> bb_loop_row86.io.Out(5)

  const34.io.enable <> bb_loop_row86.io.Out(6)

  const35.io.enable <> bb_loop_row86.io.Out(7)

  const36.io.enable <> bb_loop_row86.io.Out(8)

  const37.io.enable <> bb_loop_row86.io.Out(9)

  phiindvars_iv28287.io.enable <> bb_loop_row86.io.Out(10)


  binaryOp_88.io.enable <> bb_loop_row86.io.Out(11)


  binaryOp_89.io.enable <> bb_loop_row86.io.Out(12)


  binaryOp_90.io.enable <> bb_loop_row86.io.Out(13)


  binaryOp_91.io.enable <> bb_loop_row86.io.Out(14)


  binaryOp_92.io.enable <> bb_loop_row86.io.Out(15)


  binaryOp_93.io.enable <> bb_loop_row86.io.Out(16)


  binaryOp_94.io.enable <> bb_loop_row86.io.Out(17)


  binaryOp_95.io.enable <> bb_loop_row86.io.Out(18)


  binaryOp_96.io.enable <> bb_loop_row86.io.Out(19)


  binaryOp_97.io.enable <> bb_loop_row86.io.Out(20)


  binaryOp_98.io.enable <> bb_loop_row86.io.Out(21)


  binaryOp_99.io.enable <> bb_loop_row86.io.Out(22)


  binaryOp_100.io.enable <> bb_loop_row86.io.Out(23)


  binaryOp_101.io.enable <> bb_loop_row86.io.Out(24)


  br_102.io.enable <> bb_loop_row86.io.Out(25)


  const38.io.enable <> bb_for_body103103.io.Out(0)

  const39.io.enable <> bb_for_body103103.io.Out(1)

  const40.io.enable <> bb_for_body103103.io.Out(2)

  const41.io.enable <> bb_for_body103103.io.Out(3)

  phiindvars_iv104.io.enable <> bb_for_body103103.io.Out(4)


  binaryOp_105.io.enable <> bb_for_body103103.io.Out(5)


  Gep_arrayidx109106.io.enable <> bb_for_body103103.io.Out(6)


  ld_107.io.enable <> bb_for_body103103.io.Out(7)


  binaryOp_108.io.enable <> bb_for_body103103.io.Out(8)


  Gep_arrayidx116109.io.enable <> bb_for_body103103.io.Out(9)


  ld_110.io.enable <> bb_for_body103103.io.Out(10)


  binaryOp_111.io.enable <> bb_for_body103103.io.Out(11)


  Gep_arrayidx122112.io.enable <> bb_for_body103103.io.Out(12)


  ld_113.io.enable <> bb_for_body103103.io.Out(13)


  binaryOp_add123114.io.enable <> bb_for_body103103.io.Out(14)


  binaryOp_115.io.enable <> bb_for_body103103.io.Out(15)


  Gep_arrayidx130116.io.enable <> bb_for_body103103.io.Out(16)


  ld_117.io.enable <> bb_for_body103103.io.Out(17)


  binaryOp_add131118.io.enable <> bb_for_body103103.io.Out(18)


  binaryOp_119.io.enable <> bb_for_body103103.io.Out(19)


  Gep_arrayidx138120.io.enable <> bb_for_body103103.io.Out(20)


  ld_121.io.enable <> bb_for_body103103.io.Out(21)


  binaryOp_add139122.io.enable <> bb_for_body103103.io.Out(22)


  binaryOp_indvars_iv_next123.io.enable <> bb_for_body103103.io.Out(23)


  binaryOp_124.io.enable <> bb_for_body103103.io.Out(24)


  Gep_arrayidx146125.io.enable <> bb_for_body103103.io.Out(25)


  ld_126.io.enable <> bb_for_body103103.io.Out(26)


  binaryOp_add147127.io.enable <> bb_for_body103103.io.Out(27)


  binaryOp_128.io.enable <> bb_for_body103103.io.Out(28)


  binaryOp_129.io.enable <> bb_for_body103103.io.Out(29)


  Gep_arrayidx154130.io.enable <> bb_for_body103103.io.Out(30)


  ld_131.io.enable <> bb_for_body103103.io.Out(31)


  binaryOp_add155132.io.enable <> bb_for_body103103.io.Out(32)


  ld_133.io.enable <> bb_for_body103103.io.Out(33)


  binaryOp_mul157134.io.enable <> bb_for_body103103.io.Out(34)


  ld_135.io.enable <> bb_for_body103103.io.Out(35)


  binaryOp_mul159136.io.enable <> bb_for_body103103.io.Out(36)


  binaryOp_add160137.io.enable <> bb_for_body103103.io.Out(37)


  Gep_arrayidx166138.io.enable <> bb_for_body103103.io.Out(38)


  st_139.io.enable <> bb_for_body103103.io.Out(39)


  icmp_exitcond140.io.enable <> bb_for_body103103.io.Out(40)


  br_141.io.enable <> bb_for_body103103.io.Out(41)


  const42.io.enable <> bb_for_inc170142.io.Out(0)

  const43.io.enable <> bb_for_inc170142.io.Out(1)

  binaryOp_indvars_iv_next283143.io.enable <> bb_for_inc170142.io.Out(2)


  icmp_exitcond298144.io.enable <> bb_for_inc170142.io.Out(3)


  br_145.io.enable <> bb_for_inc170142.io.Out(4)


  const44.io.enable <> bb_for_inc173146.io.Out(0)

  const45.io.enable <> bb_for_inc173146.io.Out(1)

  binaryOp_indvars_iv_next300147.io.enable <> bb_for_inc173146.io.Out(2)


  icmp_exitcond302148.io.enable <> bb_for_inc173146.io.Out(3)


  br_149.io.enable <> bb_for_inc173146.io.Out(4)


  ret_151.io.In.enable <> bb_for_end175150.io.Out(0)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  phiindvar4.io.Mask <> bb_height_bound_row3.io.MaskBB(0)

  phiindvars_iv3258.io.Mask <> bb_for_body37.io.MaskBB(0)

  phiindvars_iv32030.io.Mask <> bb_col_bound_row29.io.MaskBB(0)

  phiindvars_iv31335.io.Mask <> bb_for_body2834.io.MaskBB(0)

  phiindvars_iv30956.io.Mask <> bb_row_bound_col55.io.MaskBB(0)

  phiindvars_iv30360.io.Mask <> bb_for_body6459.io.MaskBB(0)

  phiindvars_iv29983.io.Mask <> bb_loop_col82.io.MaskBB(0)

  phiindvars_iv28287.io.Mask <> bb_loop_row86.io.MaskBB(0)

  phiindvars_iv104.io.Mask <> bb_for_body103103.io.MaskBB(0)



  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */

  mem_ctrl_cache.io.rd.mem(0).MemReq <> ld_11.io.MemReq
  ld_11.io.MemResp <> mem_ctrl_cache.io.rd.mem(0).MemResp
  mem_ctrl_cache.io.rd.mem(1).MemReq <> ld_17.io.MemReq
  ld_17.io.MemResp <> mem_ctrl_cache.io.rd.mem(1).MemResp
  mem_ctrl_cache.io.rd.mem(2).MemReq <> ld_38.io.MemReq
  ld_38.io.MemResp <> mem_ctrl_cache.io.rd.mem(2).MemResp
  mem_ctrl_cache.io.rd.mem(3).MemReq <> ld_43.io.MemReq
  ld_43.io.MemResp <> mem_ctrl_cache.io.rd.mem(3).MemResp
  mem_ctrl_cache.io.rd.mem(4).MemReq <> ld_64.io.MemReq
  ld_64.io.MemResp <> mem_ctrl_cache.io.rd.mem(4).MemResp
  mem_ctrl_cache.io.rd.mem(5).MemReq <> ld_69.io.MemReq
  ld_69.io.MemResp <> mem_ctrl_cache.io.rd.mem(5).MemResp
  mem_ctrl_cache.io.rd.mem(6).MemReq <> ld_107.io.MemReq
  ld_107.io.MemResp <> mem_ctrl_cache.io.rd.mem(6).MemResp
  mem_ctrl_cache.io.rd.mem(7).MemReq <> ld_110.io.MemReq
  ld_110.io.MemResp <> mem_ctrl_cache.io.rd.mem(7).MemResp
  mem_ctrl_cache.io.rd.mem(8).MemReq <> ld_113.io.MemReq
  ld_113.io.MemResp <> mem_ctrl_cache.io.rd.mem(8).MemResp
  mem_ctrl_cache.io.rd.mem(9).MemReq <> ld_117.io.MemReq
  ld_117.io.MemResp <> mem_ctrl_cache.io.rd.mem(9).MemResp
  mem_ctrl_cache.io.rd.mem(10).MemReq <> ld_121.io.MemReq
  ld_121.io.MemResp <> mem_ctrl_cache.io.rd.mem(10).MemResp
  mem_ctrl_cache.io.rd.mem(11).MemReq <> ld_126.io.MemReq
  ld_126.io.MemResp <> mem_ctrl_cache.io.rd.mem(11).MemResp
  mem_ctrl_cache.io.rd.mem(12).MemReq <> ld_131.io.MemReq
  ld_131.io.MemResp <> mem_ctrl_cache.io.rd.mem(12).MemResp
  mem_ctrl_cache.io.rd.mem(13).MemReq <> ld_133.io.MemReq
  ld_133.io.MemResp <> mem_ctrl_cache.io.rd.mem(13).MemResp
  mem_ctrl_cache.io.rd.mem(14).MemReq <> ld_135.io.MemReq
  ld_135.io.MemResp <> mem_ctrl_cache.io.rd.mem(14).MemResp
  mem_ctrl_cache.io.wr.mem(0).MemReq <> st_13.io.MemReq
  st_13.io.MemResp <> mem_ctrl_cache.io.wr.mem(0).MemResp

  mem_ctrl_cache.io.wr.mem(1).MemReq <> st_19.io.MemReq
  st_19.io.MemResp <> mem_ctrl_cache.io.wr.mem(1).MemResp

  mem_ctrl_cache.io.wr.mem(2).MemReq <> st_40.io.MemReq
  st_40.io.MemResp <> mem_ctrl_cache.io.wr.mem(2).MemResp

  mem_ctrl_cache.io.wr.mem(3).MemReq <> st_45.io.MemReq
  st_45.io.MemResp <> mem_ctrl_cache.io.wr.mem(3).MemResp

  mem_ctrl_cache.io.wr.mem(4).MemReq <> st_66.io.MemReq
  st_66.io.MemResp <> mem_ctrl_cache.io.wr.mem(4).MemResp

  mem_ctrl_cache.io.wr.mem(5).MemReq <> st_71.io.MemReq
  st_71.io.MemResp <> mem_ctrl_cache.io.wr.mem(5).MemResp

  mem_ctrl_cache.io.wr.mem(6).MemReq <> st_139.io.MemReq
  st_139.io.MemResp <> mem_ctrl_cache.io.wr.mem(6).MemResp



  /* ================================================================== *
   *                   PRINT SHARED CONNECTIONS                         *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  phiindvar4.io.InData(0) <> const0.io.Out

  binaryOp_5.io.RightIO <> const1.io.Out

  phiindvars_iv3258.io.InData(0) <> const2.io.Out

  binaryOp_15.io.RightIO <> const3.io.Out

  binaryOp_indvars_iv_next32620.io.RightIO <> const4.io.Out

  icmp_exitcond33021.io.RightIO <> const5.io.Out

  binaryOp_indvar_next24.io.RightIO <> const6.io.Out

  icmp_exitcond33425.io.RightIO <> const7.io.Out

  phiindvars_iv32030.io.InData(1) <> const8.io.Out

  binaryOp_31.io.RightIO <> const9.io.Out

  binaryOp_32.io.RightIO <> const10.io.Out

  phiindvars_iv31335.io.InData(0) <> const11.io.Out

  binaryOp_indvars_iv_next31446.io.RightIO <> const12.io.Out

  icmp_exitcond31747.io.RightIO <> const13.io.Out

  binaryOp_indvars_iv_next32150.io.RightIO <> const14.io.Out

  icmp_exitcond32451.io.RightIO <> const15.io.Out

  phiindvars_iv30956.io.InData(1) <> const16.io.Out

  binaryOp_57.io.RightIO <> const17.io.Out

  phiindvars_iv30360.io.InData(0) <> const18.io.Out

  binaryOp_62.io.RightIO <> const19.io.Out

  binaryOp_67.io.RightIO <> const20.io.Out

  binaryOp_indvars_iv_next30472.io.RightIO <> const21.io.Out

  icmp_exitcond30873.io.RightIO <> const22.io.Out

  binaryOp_indvars_iv_next31076.io.RightIO <> const23.io.Out

  icmp_exitcond31277.io.RightIO <> const24.io.Out

  Gep_arrayidx15880.io.idx(0) <> const25.io.Out

  phiindvars_iv29983.io.InData(0) <> const26.io.Out

  binaryOp_84.io.RightIO <> const27.io.Out

  phiindvars_iv28287.io.InData(0) <> const28.io.Out

  binaryOp_89.io.RightIO <> const29.io.Out

  binaryOp_91.io.RightIO <> const30.io.Out

  binaryOp_92.io.RightIO <> const31.io.Out

  binaryOp_94.io.RightIO <> const32.io.Out

  binaryOp_95.io.RightIO <> const33.io.Out

  binaryOp_97.io.RightIO <> const34.io.Out

  binaryOp_98.io.RightIO <> const35.io.Out

  binaryOp_100.io.RightIO <> const36.io.Out

  binaryOp_101.io.RightIO <> const37.io.Out

  phiindvars_iv104.io.InData(0) <> const38.io.Out

  binaryOp_indvars_iv_next123.io.RightIO <> const39.io.Out

  binaryOp_129.io.RightIO <> const40.io.Out

  icmp_exitcond140.io.RightIO <> const41.io.Out

  binaryOp_indvars_iv_next283143.io.RightIO <> const42.io.Out

  icmp_exitcond298144.io.RightIO <> const43.io.Out

  binaryOp_indvars_iv_next300147.io.RightIO <> const44.io.Out

  icmp_exitcond302148.io.RightIO <> const45.io.Out

  binaryOp_5.io.LeftIO <> phiindvar4.io.Out(0)

  binaryOp_indvar_next24.io.LeftIO <> phiindvar4.io.Out(1)

  binaryOp_9.io.LeftIO <> phiindvars_iv3258.io.Out(0)

  binaryOp_14.io.LeftIO <> phiindvars_iv3258.io.Out(1)

  binaryOp_indvars_iv_next32620.io.LeftIO <> phiindvars_iv3258.io.Out(2)

  Gep_arrayidx10.io.idx(0) <> binaryOp_9.io.Out(0)

  Gep_arrayidx912.io.idx(0) <> binaryOp_9.io.Out(1)

  ld_11.io.GepAddr <> Gep_arrayidx10.io.Out(0)

  st_13.io.inData <> ld_11.io.Out(0)

  st_13.io.GepAddr <> Gep_arrayidx912.io.Out(0)

  binaryOp_15.io.LeftIO <> binaryOp_14.io.Out(0)

  Gep_arrayidx1416.io.idx(0) <> binaryOp_15.io.Out(0)

  Gep_arrayidx1918.io.idx(0) <> binaryOp_15.io.Out(1)

  ld_17.io.GepAddr <> Gep_arrayidx1416.io.Out(0)

  st_19.io.inData <> ld_17.io.Out(0)

  st_19.io.GepAddr <> Gep_arrayidx1918.io.Out(0)

  icmp_exitcond33021.io.LeftIO <> binaryOp_indvars_iv_next32620.io.Out(1)

  br_22.io.CmpIO <> icmp_exitcond33021.io.Out(0)

  icmp_exitcond33425.io.LeftIO <> binaryOp_indvar_next24.io.Out(1)

  br_26.io.CmpIO <> icmp_exitcond33425.io.Out(0)

  binaryOp_31.io.LeftIO <> phiindvars_iv32030.io.Out(0)

  binaryOp_indvars_iv_next32150.io.LeftIO <> phiindvars_iv32030.io.Out(1)

  binaryOp_32.io.LeftIO <> binaryOp_31.io.Out(1)

  binaryOp_36.io.LeftIO <> phiindvars_iv31335.io.Out(0)

  binaryOp_41.io.LeftIO <> phiindvars_iv31335.io.Out(1)

  binaryOp_indvars_iv_next31446.io.LeftIO <> phiindvars_iv31335.io.Out(2)

  Gep_arrayidx3437.io.idx(0) <> binaryOp_36.io.Out(0)

  Gep_arrayidx4039.io.idx(0) <> binaryOp_36.io.Out(1)

  ld_38.io.GepAddr <> Gep_arrayidx3437.io.Out(0)

  st_40.io.inData <> ld_38.io.Out(0)

  st_40.io.GepAddr <> Gep_arrayidx4039.io.Out(0)

  Gep_arrayidx4642.io.idx(0) <> binaryOp_41.io.Out(0)

  Gep_arrayidx5244.io.idx(0) <> binaryOp_41.io.Out(1)

  ld_43.io.GepAddr <> Gep_arrayidx4642.io.Out(0)

  st_45.io.inData <> ld_43.io.Out(0)

  st_45.io.GepAddr <> Gep_arrayidx5244.io.Out(0)

  icmp_exitcond31747.io.LeftIO <> binaryOp_indvars_iv_next31446.io.Out(1)

  br_48.io.CmpIO <> icmp_exitcond31747.io.Out(0)

  icmp_exitcond32451.io.LeftIO <> binaryOp_indvars_iv_next32150.io.Out(1)

  br_52.io.CmpIO <> icmp_exitcond32451.io.Out(0)

  binaryOp_57.io.LeftIO <> phiindvars_iv30956.io.Out(0)

  binaryOp_indvars_iv_next31076.io.LeftIO <> phiindvars_iv30956.io.Out(1)

  binaryOp_61.io.LeftIO <> phiindvars_iv30360.io.Out(0)

  binaryOp_indvars_iv_next30472.io.LeftIO <> phiindvars_iv30360.io.Out(1)

  binaryOp_62.io.LeftIO <> binaryOp_61.io.Out(0)

  Gep_arrayidx7063.io.idx(0) <> binaryOp_62.io.Out(0)

  Gep_arrayidx7665.io.idx(0) <> binaryOp_62.io.Out(1)

  binaryOp_67.io.LeftIO <> binaryOp_62.io.Out(2)

  ld_64.io.GepAddr <> Gep_arrayidx7063.io.Out(0)

  st_66.io.inData <> ld_64.io.Out(0)

  st_66.io.GepAddr <> Gep_arrayidx7665.io.Out(0)

  Gep_arrayidx8268.io.idx(0) <> binaryOp_67.io.Out(0)

  Gep_arrayidx8870.io.idx(0) <> binaryOp_67.io.Out(1)

  ld_69.io.GepAddr <> Gep_arrayidx8268.io.Out(0)

  st_71.io.inData <> ld_69.io.Out(0)

  st_71.io.GepAddr <> Gep_arrayidx8870.io.Out(0)

  icmp_exitcond30873.io.LeftIO <> binaryOp_indvars_iv_next30472.io.Out(1)

  br_74.io.CmpIO <> icmp_exitcond30873.io.Out(0)

  icmp_exitcond31277.io.LeftIO <> binaryOp_indvars_iv_next31076.io.Out(1)

  br_78.io.CmpIO <> icmp_exitcond31277.io.Out(0)

  binaryOp_84.io.LeftIO <> phiindvars_iv29983.io.Out(0)

  binaryOp_indvars_iv_next300147.io.LeftIO <> phiindvars_iv29983.io.Out(1)

  binaryOp_88.io.LeftIO <> phiindvars_iv28287.io.Out(0)

  binaryOp_90.io.LeftIO <> phiindvars_iv28287.io.Out(1)

  binaryOp_93.io.LeftIO <> phiindvars_iv28287.io.Out(2)

  binaryOp_96.io.LeftIO <> phiindvars_iv28287.io.Out(3)

  binaryOp_99.io.LeftIO <> phiindvars_iv28287.io.Out(4)

  binaryOp_indvars_iv_next283143.io.LeftIO <> phiindvars_iv28287.io.Out(5)

  binaryOp_89.io.LeftIO <> binaryOp_88.io.Out(0)

  binaryOp_91.io.LeftIO <> binaryOp_90.io.Out(0)

  binaryOp_92.io.LeftIO <> binaryOp_91.io.Out(0)

  binaryOp_94.io.LeftIO <> binaryOp_93.io.Out(0)

  binaryOp_95.io.LeftIO <> binaryOp_94.io.Out(0)

  binaryOp_97.io.LeftIO <> binaryOp_96.io.Out(0)

  binaryOp_98.io.LeftIO <> binaryOp_97.io.Out(0)

  binaryOp_100.io.LeftIO <> binaryOp_99.io.Out(0)

  binaryOp_101.io.LeftIO <> binaryOp_100.io.Out(0)

  binaryOp_105.io.LeftIO <> phiindvars_iv104.io.Out(0)

  binaryOp_108.io.LeftIO <> phiindvars_iv104.io.Out(1)

  binaryOp_111.io.LeftIO <> phiindvars_iv104.io.Out(2)

  binaryOp_115.io.LeftIO <> phiindvars_iv104.io.Out(3)

  binaryOp_119.io.LeftIO <> phiindvars_iv104.io.Out(4)

  binaryOp_indvars_iv_next123.io.LeftIO <> phiindvars_iv104.io.Out(5)

  binaryOp_128.io.LeftIO <> phiindvars_iv104.io.Out(6)

  Gep_arrayidx109106.io.idx(0) <> binaryOp_105.io.Out(0)

  Gep_arrayidx166138.io.idx(0) <> binaryOp_105.io.Out(1)

  ld_107.io.GepAddr <> Gep_arrayidx109106.io.Out(0)

  binaryOp_mul157134.io.RightIO <> ld_107.io.Out(0)

  Gep_arrayidx116109.io.idx(0) <> binaryOp_108.io.Out(0)

  ld_110.io.GepAddr <> Gep_arrayidx116109.io.Out(0)

  binaryOp_add123114.io.RightIO <> ld_110.io.Out(0)

  Gep_arrayidx122112.io.idx(0) <> binaryOp_111.io.Out(0)

  ld_113.io.GepAddr <> Gep_arrayidx122112.io.Out(0)

  binaryOp_add123114.io.LeftIO <> ld_113.io.Out(0)

  binaryOp_add131118.io.LeftIO <> binaryOp_add123114.io.Out(0)

  Gep_arrayidx130116.io.idx(0) <> binaryOp_115.io.Out(0)

  ld_117.io.GepAddr <> Gep_arrayidx130116.io.Out(0)

  binaryOp_add131118.io.RightIO <> ld_117.io.Out(0)

  binaryOp_add139122.io.LeftIO <> binaryOp_add131118.io.Out(0)

  Gep_arrayidx138120.io.idx(0) <> binaryOp_119.io.Out(0)

  ld_121.io.GepAddr <> Gep_arrayidx138120.io.Out(0)

  binaryOp_add139122.io.RightIO <> ld_121.io.Out(0)

  binaryOp_add147127.io.LeftIO <> binaryOp_add139122.io.Out(0)

  binaryOp_124.io.LeftIO <> binaryOp_indvars_iv_next123.io.Out(1)

  icmp_exitcond140.io.LeftIO <> binaryOp_indvars_iv_next123.io.Out(2)

  Gep_arrayidx146125.io.idx(0) <> binaryOp_124.io.Out(0)

  ld_126.io.GepAddr <> Gep_arrayidx146125.io.Out(0)

  binaryOp_add147127.io.RightIO <> ld_126.io.Out(0)

  binaryOp_add155132.io.LeftIO <> binaryOp_add147127.io.Out(0)

  binaryOp_129.io.LeftIO <> binaryOp_128.io.Out(0)

  Gep_arrayidx154130.io.idx(0) <> binaryOp_129.io.Out(0)

  ld_131.io.GepAddr <> Gep_arrayidx154130.io.Out(0)

  binaryOp_add155132.io.RightIO <> ld_131.io.Out(0)

  binaryOp_mul159136.io.RightIO <> binaryOp_add155132.io.Out(0)

  binaryOp_mul157134.io.LeftIO <> ld_133.io.Out(0)

  binaryOp_add160137.io.RightIO <> binaryOp_mul157134.io.Out(0)

  binaryOp_mul159136.io.LeftIO <> ld_135.io.Out(0)

  binaryOp_add160137.io.LeftIO <> binaryOp_mul159136.io.Out(0)

  st_139.io.inData <> binaryOp_add160137.io.Out(0)

  st_139.io.GepAddr <> Gep_arrayidx166138.io.Out(0)

  br_141.io.CmpIO <> icmp_exitcond140.io.Out(0)

  icmp_exitcond298144.io.LeftIO <> binaryOp_indvars_iv_next283143.io.Out(1)

  br_145.io.CmpIO <> icmp_exitcond298144.io.Out(0)

  icmp_exitcond302148.io.LeftIO <> binaryOp_indvars_iv_next300147.io.Out(1)

  br_149.io.CmpIO <> icmp_exitcond302148.io.Out(0)

  Gep_arrayidx15880.io.baseAddress <> ArgSplitter.io.Out.dataPtrs.elements("field0")(1)

  st_13.io.Out(0).ready := true.B

  st_19.io.Out(0).ready := true.B

  st_40.io.Out(0).ready := true.B

  st_45.io.Out(0).ready := true.B

  st_66.io.Out(0).ready := true.B

  st_71.io.Out(0).ready := true.B

  st_139.io.Out(0).ready := true.B



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  br_22.io.PredOp(0) <> st_13.io.SuccOp(0)

  br_22.io.PredOp(1) <> st_19.io.SuccOp(0)

  br_48.io.PredOp(0) <> st_40.io.SuccOp(0)

  br_48.io.PredOp(1) <> st_45.io.SuccOp(0)

  br_74.io.PredOp(0) <> st_66.io.SuccOp(0)

  br_74.io.PredOp(1) <> st_71.io.SuccOp(0)

  br_141.io.PredOp(0) <> st_139.io.SuccOp(0)



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_151.io.Out

}

