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


class conv2dDF(PtrsIn: Seq[Int] = List(32, 32, 32), ValsIn: Seq[Int] = List(32, 32, 32, 32), Returns: Seq[Int] = List())
			(implicit p: Parameters) extends DandelionAccelDCRModule(PtrsIn, ValsIn, Returns){


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  val MemCtrl = Module(new CacheMemoryEngine(ID = 0, NumRead = 2, NumWrite = 1))

  io.MemReq <> MemCtrl.io.cache.MemReq
  MemCtrl.io.cache.MemResp <> io.MemResp
  val ArgSplitter = Module(new SplitCallDCR(ptrsArgTypes = List(1, 1, 1), valsArgTypes = List(4, 1, 4, 1)))
  ArgSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val Loop_0 = Module(new LoopBlockNode(NumIns = List(1, 1, 1, 1, 1, 1), NumOuts = List(1), NumCarry = List(1, 1, 1), NumExits = 1, ID = 0))

  val Loop_1 = Module(new LoopBlockNode(NumIns = List(1, 1, 1, 1, 1, 1, 1, 1, 1), NumOuts = List(1), NumCarry = List(1, 1, 1, 1), NumExits = 1, ID = 1))

  val Loop_2 = Module(new LoopBlockNode(NumIns = List(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 2))

  val Loop_3 = Module(new LoopBlockNode(NumIns = List(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1), NumOuts = List(), NumCarry = List(1, 1), NumExits = 1, ID = 3))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_entry0 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 6, BID = 0))

  val bb_for_body_lr_ph1 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 18, BID = 1))

  val bb_for_cond_cleanup_loopexit2 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 2))

  val bb_for_cond_cleanup3 = Module(new BasicBlockNoMaskFastNode(NumInputs = 2, NumOuts = 1, BID = 3))

  val bb_for_body4 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 3, NumPhi = 2, BID = 4))

  val bb_for_body5_lr_ph5 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 3, BID = 5))

  val bb_for_cond_cleanup4_loopexit6 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 6))

  val bb_for_cond_cleanup47 = Module(new BasicBlockNoMaskFastNode(NumInputs = 2, NumOuts = 5, BID = 7))

  val bb_for_body58 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 2, NumPhi = 1, BID = 8))

  val bb_for_body12_lr_ph9 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 4, BID = 9))

  val bb_for_cond_cleanup11_loopexit10 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 10))

  val bb_for_cond_cleanup1111 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 10, NumPhi = 1, BID = 11))

  val bb_for_body1212 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 8, NumPhi = 4, BID = 12))

  val bb_for_body17_lr_ph13 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 3, BID = 13))

  val bb_for_cond_cleanup16_loopexit14 = Module(new BasicBlockNode(NumInputs = 1, NumOuts = 3, NumPhi = 1, BID = 14))

  val bb_for_cond_cleanup1615 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 7, NumPhi = 2, BID = 15))

  val bb_for_body1716 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 19, NumPhi = 3, BID = 16))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  %shr = ashr i32 %K, 1, !dbg !73, !UID !74
  val binaryOp_shr0 = Module(new ComputeNode(NumOuts = 7, ID = 0, opCode = "ashr")(sign = false, Debug = false))

  //  %mul = mul nsw i32 %shr, %W, !dbg !76, !UID !77
  val binaryOp_mul1 = Module(new ComputeNode(NumOuts = 2, ID = 1, opCode = "mul")(sign = false, Debug = false))

  //  %sub = sub nsw i32 %H, %shr, !dbg !80, !UID !81
  val binaryOp_sub2 = Module(new ComputeNode(NumOuts = 2, ID = 2, opCode = "sub")(sign = false, Debug = false))

  //  %cmp84 = icmp slt i32 %shr, %sub, !dbg !82, !UID !83
  val icmp_cmp843 = Module(new ComputeNode(NumOuts = 1, ID = 3, opCode = "slt")(sign = true, Debug = false))

  //  br i1 %cmp84, label %for.body.lr.ph, label %for.cond.cleanup, !dbg !84, !UID !85, !BB_UID !86
  val br_4 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 4))

  //  %sub2 = sub nsw i32 %W, %shr, !UID !87
  val binaryOp_sub25 = Module(new ComputeNode(NumOuts = 2, ID = 5, opCode = "sub")(sign = false, Debug = false))

  //  %cmp382 = icmp slt i32 %shr, %sub2, !UID !88
  val icmp_cmp3826 = Module(new ComputeNode(NumOuts = 1, ID = 6, opCode = "slt")(sign = true, Debug = false))

  //  %mul9 = and i32 %K, -2, !UID !89
  val binaryOp_mul97 = Module(new ComputeNode(NumOuts = 2, ID = 7, opCode = "and")(sign = false, Debug = false))

  //  %cmp1076 = icmp slt i32 %mul9, 0, !UID !90
  val icmp_cmp10768 = Module(new ComputeNode(NumOuts = 1, ID = 8, opCode = "slt")(sign = true, Debug = false))

  //  %cmp1571 = icmp slt i32 %mul9, 0, !UID !91
  val icmp_cmp15719 = Module(new ComputeNode(NumOuts = 1, ID = 9, opCode = "slt")(sign = true, Debug = false))

  //  %0 = or i32 %K, 1, !dbg !92, !UID !93
  val binaryOp_10 = Module(new ComputeNode(NumOuts = 1, ID = 10, opCode = "or")(sign = false, Debug = false))

  //  %1 = or i32 %K, 1, !dbg !84, !UID !94
  val binaryOp_11 = Module(new ComputeNode(NumOuts = 2, ID = 11, opCode = "or")(sign = false, Debug = false))

  //  %2 = sext i32 %shr to i64, !dbg !84, !UID !95
  val sext12 = Module(new SextNode(NumOuts = 1))

  //  %3 = sext i32 %mul to i64, !dbg !84, !UID !96
  val sext13 = Module(new SextNode(NumOuts = 1))

  //  %4 = sext i32 %W to i64, !dbg !84, !UID !97
  val sext14 = Module(new SextNode(NumOuts = 1))

  //  %wide.trip.count94 = sext i32 %sub2 to i64, !UID !98
  val sextwide_trip_count9415 = Module(new SextNode(NumOuts = 1))

  //  %wide.trip.count = zext i32 %1 to i64, !UID !99
  val sextwide_trip_count16 = Module(new ZextNode(NumOuts = 1))

  //  br label %for.body, !dbg !84, !UID !100, !BB_UID !101
  val br_17 = Module(new UBranchNode(ID = 17))

  //  br label %for.cond.cleanup, !dbg !102
  val br_18 = Module(new UBranchNode(ID = 18))

  //  ret void, !dbg !102, !UID !103, !BB_UID !104
  val ret_19 = Module(new RetNode2(retTypes = List(), ID = 19))

  //  %indvars.iv96 = phi i64 [ %3, %for.body.lr.ph ], [ %indvars.iv.next97, %for.cond.cleanup4 ], !UID !105
  val phiindvars_iv9620 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 3, ID = 20, Res = true))

  //  %j.087 = phi i32 [ %shr, %for.body.lr.ph ], [ %inc38, %for.cond.cleanup4 ], !UID !106
  val phij_08721 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 1, ID = 21, Res = true))

  //  br i1 %cmp382, label %for.body5.lr.ph, label %for.cond.cleanup4, !dbg !108, !UID !109, !BB_UID !110
  val br_22 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 22))

  //  %5 = trunc i64 %indvars.iv96 to i32, !dbg !108, !UID !111
  val trunc23 = Module(new TruncNode(NumOuts = 1))

  //  %6 = sub i32 %5, %mul, !dbg !108, !UID !112
  val binaryOp_24 = Module(new ComputeNode(NumOuts = 1, ID = 24, opCode = "sub")(sign = false, Debug = false))

  //  br label %for.body5, !dbg !108, !UID !113, !BB_UID !114
  val br_25 = Module(new UBranchNode(ID = 25))

  //  br label %for.cond.cleanup4, !dbg !115
  val br_26 = Module(new UBranchNode(ID = 26))

  //  %indvars.iv.next97 = add i64 %indvars.iv96, %4, !dbg !115, !UID !116
  val binaryOp_indvars_iv_next9727 = Module(new ComputeNode(NumOuts = 1, ID = 27, opCode = "add")(sign = false, Debug = false))

  //  %inc38 = add nsw i32 %j.087, 1, !dbg !117, !UID !118
  val binaryOp_inc3828 = Module(new ComputeNode(NumOuts = 2, ID = 28, opCode = "add")(sign = false, Debug = false))

  //  %exitcond99 = icmp eq i32 %inc38, %sub, !dbg !82, !UID !119
  val icmp_exitcond9929 = Module(new ComputeNode(NumOuts = 1, ID = 29, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond99, label %for.cond.cleanup.loopexit, label %for.body, !dbg !84, !llvm.loop !120, !UID !122, !BB_UID !123
  val br_30 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 30))

  //  %indvars.iv91 = phi i64 [ %2, %for.body5.lr.ph ], [ %indvars.iv.next92, %for.cond.cleanup11 ], !UID !124
  val phiindvars_iv9131 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 3, ID = 31, Res = true))

  //  br i1 %cmp1076, label %for.cond.cleanup11, label %for.body12.lr.ph, !dbg !128, !UID !129, !BB_UID !130
  val br_32 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 32))

  //  %7 = trunc i64 %indvars.iv91 to i32, !UID !131
  val trunc33 = Module(new TruncNode(NumOuts = 1))

  //  %sub18 = add i32 %7, -2, !UID !132
  val binaryOp_sub1834 = Module(new ComputeNode(NumOuts = 1, ID = 34, opCode = "add")(sign = false, Debug = false))

  //  br label %for.body12, !dbg !128, !UID !133, !BB_UID !134
  val br_35 = Module(new UBranchNode(ID = 35))

  //  br label %for.cond.cleanup11, !dbg !135
  val br_36 = Module(new UBranchNode(ID = 36))

  //  %val.0.lcssa = phi i32 [ 0, %for.body5 ], [ %val.1.lcssa, %for.cond.cleanup11.loopexit ], !UID !136
  val phival_0_lcssa37 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 1, ID = 37, Res = true))

  //  %shr29 = ashr i32 %val.0.lcssa, %scf, !dbg !135, !UID !137
  val binaryOp_shr2938 = Module(new ComputeNode(NumOuts = 1, ID = 38, opCode = "ashr")(sign = false, Debug = false))

  //  %8 = add nsw i64 %indvars.iv91, %indvars.iv96, !dbg !138, !UID !139
  val binaryOp_39 = Module(new ComputeNode(NumOuts = 1, ID = 39, opCode = "add")(sign = false, Debug = false))

  //  %arrayidx32 = getelementptr inbounds i32, i32* %res, i64 %8, !dbg !140, !UID !141
  val Gep_arrayidx3240 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 40)(ElementSize = 8, ArraySize = List()))

  //  store i32 %shr29, i32* %arrayidx32, align 4, !dbg !142, !tbaa !143, !UID !147
  val st_41 = Module(new UnTypStoreCache(NumPredOps = 0, NumSuccOps = 1, ID = 41, RouteID = 2))

  //  %indvars.iv.next92 = add nsw i64 %indvars.iv91, 1, !dbg !148, !UID !149
  val binaryOp_indvars_iv_next9242 = Module(new ComputeNode(NumOuts = 2, ID = 42, opCode = "add")(sign = false, Debug = false))

  //  %exitcond95 = icmp eq i64 %indvars.iv.next92, %wide.trip.count94, !dbg !150, !UID !151
  val icmp_exitcond9543 = Module(new ComputeNode(NumOuts = 1, ID = 43, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond95, label %for.cond.cleanup4.loopexit, label %for.body5, !dbg !108, !llvm.loop !152, !UID !154, !BB_UID !155
  val br_44 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 1, ID = 44))

  //  %y.080 = phi i32 [ 0, %for.body12.lr.ph ], [ %inc27, %for.cond.cleanup16 ], !UID !156
  val phiy_08045 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 1, ID = 45, Res = true))

  //  %val.079 = phi i32 [ 0, %for.body12.lr.ph ], [ %val.1.lcssa, %for.cond.cleanup16 ], !UID !157
  val phival_07946 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 46, Res = true))

  //  %c.078 = phi i32 [ 0, %for.body12.lr.ph ], [ %c.1.lcssa, %for.cond.cleanup16 ], !UID !158
  val phic_07847 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 3, ID = 47, Res = true))

  //  %index2.077 = phi i32 [ %6, %for.body12.lr.ph ], [ %add25, %for.cond.cleanup16 ], !UID !159
  val phiindex2_07748 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 48, Res = true))

  //  br i1 %cmp1571, label %for.cond.cleanup16, label %for.body17.lr.ph, !dbg !92, !UID !162, !BB_UID !163
  val br_49 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 49))

  //  %add = add i32 %sub18, %index2.077, !UID !164
  val binaryOp_add50 = Module(new ComputeNode(NumOuts = 1, ID = 50, opCode = "add")(sign = false, Debug = false))

  //  %9 = sext i32 %c.078 to i64, !dbg !92, !UID !165
  val sext51 = Module(new SextNode(NumOuts = 1))

  //  br label %for.body17, !dbg !92, !UID !166, !BB_UID !167
  val br_52 = Module(new UBranchNode(ID = 52))

  //  %add23.lcssa = phi i32 [ %add23, %for.body17 ], !UID !168
  val phiadd23_lcssa53 = Module(new PhiFastNode(NumInputs = 1, NumOutputs = 1, ID = 53, Res = false))

  //  %10 = add i32 %0, %c.078, !dbg !92, !UID !169
  val binaryOp_54 = Module(new ComputeNode(NumOuts = 1, ID = 54, opCode = "add")(sign = false, Debug = false))

  //  br label %for.cond.cleanup16, !dbg !170, !UID !171, !BB_UID !172
  val br_55 = Module(new UBranchNode(ID = 55))

  //  %c.1.lcssa = phi i32 [ %c.078, %for.body12 ], [ %10, %for.cond.cleanup16.loopexit ], !UID !173
  val phic_1_lcssa56 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 1, ID = 56, Res = true))

  //  %val.1.lcssa = phi i32 [ %val.079, %for.body12 ], [ %add23.lcssa, %for.cond.cleanup16.loopexit ], !UID !174
  val phival_1_lcssa57 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 57, Res = true))

  //  %add25 = add nsw i32 %index2.077, %W, !dbg !170, !UID !175
  val binaryOp_add2558 = Module(new ComputeNode(NumOuts = 1, ID = 58, opCode = "add")(sign = false, Debug = false))

  //  %inc27 = add nuw nsw i32 %y.080, 1, !dbg !176, !UID !177
  val binaryOp_inc2759 = Module(new ComputeNode(NumOuts = 2, ID = 59, opCode = "add")(sign = false, Debug = false))

  //  %exitcond90 = icmp eq i32 %inc27, %1, !dbg !178, !UID !179
  val icmp_exitcond9060 = Module(new ComputeNode(NumOuts = 1, ID = 60, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond90, label %for.cond.cleanup11.loopexit, label %for.body12, !dbg !128, !llvm.loop !180, !UID !182, !BB_UID !183
  val br_61 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 61))

  //  %indvars.iv88 = phi i64 [ %indvars.iv.next89, %for.body17 ], [ %9, %for.body17.lr.ph ], !UID !184
  val phiindvars_iv8862 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 62, Res = false))

  //  %indvars.iv = phi i64 [ %indvars.iv.next, %for.body17 ], [ 0, %for.body17.lr.ph ], !UID !185
  val phiindvars_iv63 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 63, Res = false))

  //  %val.173 = phi i32 [ %add23, %for.body17 ], [ %val.079, %for.body17.lr.ph ], !UID !186
  val phival_17364 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 1, ID = 64, Res = false))

  //  %indvars.iv.next89 = add nsw i64 %indvars.iv88, 1, !dbg !187, !UID !190
  val binaryOp_indvars_iv_next8965 = Module(new ComputeNode(NumOuts = 1, ID = 65, opCode = "add")(sign = false, Debug = false))

  //  %arrayidx = getelementptr inbounds i32, i32* %coeffs, i64 %indvars.iv88, !dbg !191, !UID !192
  val Gep_arrayidx66 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 66)(ElementSize = 8, ArraySize = List()))

  //  %11 = load i32, i32* %arrayidx, align 4, !dbg !191, !tbaa !143, !UID !193
  val ld_67 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 67, RouteID = 0))

  //  %12 = trunc i64 %indvars.iv to i32, !dbg !194, !UID !195
  val trunc68 = Module(new TruncNode(NumOuts = 1))

  //  %add19 = add i32 %add, %12, !dbg !194, !UID !196
  val binaryOp_add1969 = Module(new ComputeNode(NumOuts = 1, ID = 69, opCode = "add")(sign = false, Debug = false))

  //  %idxprom20 = sext i32 %add19 to i64, !dbg !197, !UID !198
  val sextidxprom2070 = Module(new SextNode(NumOuts = 1))

  //  %arrayidx21 = getelementptr inbounds i32, i32* %mat, i64 %idxprom20, !dbg !197, !UID !199
  val Gep_arrayidx2171 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 71)(ElementSize = 8, ArraySize = List()))

  //  %13 = load i32, i32* %arrayidx21, align 4, !dbg !197, !tbaa !143, !UID !200
  val ld_72 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 72, RouteID = 1))

  //  %mul22 = mul nsw i32 %13, %11, !dbg !201, !UID !202
  val binaryOp_mul2273 = Module(new ComputeNode(NumOuts = 1, ID = 73, opCode = "mul")(sign = false, Debug = false))

  //  %add23 = add nsw i32 %mul22, %val.173, !dbg !203, !UID !204
  val binaryOp_add2374 = Module(new ComputeNode(NumOuts = 2, ID = 74, opCode = "add")(sign = false, Debug = false))

  //  %indvars.iv.next = add nuw nsw i64 %indvars.iv, 1, !dbg !205, !UID !206
  val binaryOp_indvars_iv_next75 = Module(new ComputeNode(NumOuts = 2, ID = 75, opCode = "add")(sign = false, Debug = false))

  //  %exitcond = icmp eq i64 %indvars.iv.next, %wide.trip.count, !dbg !207, !UID !208
  val icmp_exitcond76 = Module(new ComputeNode(NumOuts = 1, ID = 76, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond, label %for.cond.cleanup16.loopexit, label %for.body17, !dbg !92, !llvm.loop !209, !UID !211, !BB_UID !212
  val br_77 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 77))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i32 1
  val const0 = Module(new ConstFastNode(value = 1, ID = 0))

  //i32 -2
  val const1 = Module(new ConstFastNode(value = -2, ID = 1))

  //i32 0
  val const2 = Module(new ConstFastNode(value = 0, ID = 2))

  //i32 0
  val const3 = Module(new ConstFastNode(value = 0, ID = 3))

  //i32 1
  val const4 = Module(new ConstFastNode(value = 1, ID = 4))

  //i32 1
  val const5 = Module(new ConstFastNode(value = 1, ID = 5))

  //i32 1
  val const6 = Module(new ConstFastNode(value = 1, ID = 6))

  //i32 -2
  val const7 = Module(new ConstFastNode(value = -2, ID = 7))

  //i32 0
  val const8 = Module(new ConstFastNode(value = 0, ID = 8))

  //i64 1
  val const9 = Module(new ConstFastNode(value = 1, ID = 9))

  //i32 0
  val const10 = Module(new ConstFastNode(value = 0, ID = 10))

  //i32 0
  val const11 = Module(new ConstFastNode(value = 0, ID = 11))

  //i32 0
  val const12 = Module(new ConstFastNode(value = 0, ID = 12))

  //i32 1
  val const13 = Module(new ConstFastNode(value = 1, ID = 13))

  //i64 0
  val const14 = Module(new ConstFastNode(value = 0, ID = 14))

  //i64 1
  val const15 = Module(new ConstFastNode(value = 1, ID = 15))

  //i64 1
  val const16 = Module(new ConstFastNode(value = 1, ID = 16))



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_entry0.io.predicateIn(0) <> ArgSplitter.io.Out.enable

  bb_for_body_lr_ph1.io.predicateIn(0) <> br_4.io.TrueOutput(0)

  bb_for_cond_cleanup3.io.predicateIn(1) <> br_4.io.FalseOutput(0)

  bb_for_cond_cleanup3.io.predicateIn(0) <> br_18.io.Out(0)

  bb_for_body5_lr_ph5.io.predicateIn(0) <> br_22.io.TrueOutput(0)

  bb_for_cond_cleanup47.io.predicateIn(1) <> br_22.io.FalseOutput(0)

  bb_for_cond_cleanup47.io.predicateIn(0) <> br_26.io.Out(0)

  bb_for_body12_lr_ph9.io.predicateIn(0) <> br_32.io.FalseOutput(0)

  bb_for_cond_cleanup1111.io.predicateIn(1) <> br_32.io.TrueOutput(0)

  bb_for_cond_cleanup1111.io.predicateIn(0) <> br_36.io.Out(0)

  bb_for_body17_lr_ph13.io.predicateIn(0) <> br_49.io.FalseOutput(0)

  bb_for_cond_cleanup1615.io.predicateIn(1) <> br_49.io.TrueOutput(0)

  bb_for_cond_cleanup1615.io.predicateIn(0) <> br_55.io.Out(0)



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE LOOP                     *
   * ================================================================== */

  bb_for_cond_cleanup_loopexit2.io.predicateIn(0) <> Loop_3.io.loopExit(0)

  bb_for_body4.io.predicateIn(1) <> Loop_3.io.activate_loop_start

  bb_for_body4.io.predicateIn(0) <> Loop_3.io.activate_loop_back

  bb_for_cond_cleanup4_loopexit6.io.predicateIn(0) <> Loop_2.io.loopExit(0)

  bb_for_body58.io.predicateIn(1) <> Loop_2.io.activate_loop_start

  bb_for_body58.io.predicateIn(0) <> Loop_2.io.activate_loop_back

  bb_for_cond_cleanup11_loopexit10.io.predicateIn(0) <> Loop_1.io.loopExit(0)

  bb_for_body1212.io.predicateIn(1) <> Loop_1.io.activate_loop_start

  bb_for_body1212.io.predicateIn(0) <> Loop_1.io.activate_loop_back

  bb_for_cond_cleanup16_loopexit14.io.predicateIn(0) <> Loop_0.io.loopExit(0)

  bb_for_body1716.io.predicateIn(1) <> Loop_0.io.activate_loop_start

  bb_for_body1716.io.predicateIn(0) <> Loop_0.io.activate_loop_back



  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */

  Loop_0.io.enable <> br_52.io.Out(0)

  Loop_0.io.loopBack(0) <> br_77.io.FalseOutput(0)

  Loop_0.io.loopFinish(0) <> br_77.io.TrueOutput(0)

  Loop_1.io.enable <> br_35.io.Out(0)

  Loop_1.io.loopBack(0) <> br_61.io.FalseOutput(0)

  Loop_1.io.loopFinish(0) <> br_61.io.TrueOutput(0)

  Loop_2.io.enable <> br_25.io.Out(0)

  Loop_2.io.loopBack(0) <> br_44.io.FalseOutput(0)

  Loop_2.io.loopFinish(0) <> br_44.io.TrueOutput(0)

  Loop_3.io.enable <> br_17.io.Out(0)

  Loop_3.io.loopBack(0) <> br_30.io.FalseOutput(0)

  Loop_3.io.loopFinish(0) <> br_30.io.TrueOutput(0)



  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

  Loop_0.io.InLiveIn(0) <> sext51.io.Out(0)

  Loop_0.io.InLiveIn(1) <> phival_07946.io.Out(0)

  Loop_0.io.InLiveIn(2) <> binaryOp_add50.io.Out(0)

  Loop_0.io.InLiveIn(3) <> Loop_1.io.OutLiveIn.elements("field1")(0)

  Loop_0.io.InLiveIn(4) <> Loop_1.io.OutLiveIn.elements("field2")(0)

  Loop_0.io.InLiveIn(5) <> Loop_1.io.OutLiveIn.elements("field3")(0)

  Loop_1.io.InLiveIn(0) <> binaryOp_sub1834.io.Out(0)

  Loop_1.io.InLiveIn(1) <> Loop_2.io.OutLiveIn.elements("field3")(0)

  Loop_1.io.InLiveIn(2) <> Loop_2.io.OutLiveIn.elements("field4")(0)

  Loop_1.io.InLiveIn(3) <> Loop_2.io.OutLiveIn.elements("field7")(0)

  Loop_1.io.InLiveIn(4) <> Loop_2.io.OutLiveIn.elements("field0")(0)

  Loop_1.io.InLiveIn(5) <> Loop_2.io.OutLiveIn.elements("field2")(0)

  Loop_1.io.InLiveIn(6) <> Loop_2.io.OutLiveIn.elements("field5")(0)

  Loop_1.io.InLiveIn(7) <> Loop_2.io.OutLiveIn.elements("field6")(0)

  Loop_1.io.InLiveIn(8) <> Loop_2.io.OutLiveIn.elements("field8")(0)

  Loop_2.io.InLiveIn(0) <> binaryOp_24.io.Out(0)

  Loop_2.io.InLiveIn(1) <> phiindvars_iv9620.io.Out(0)

  Loop_2.io.InLiveIn(2) <> Loop_3.io.OutLiveIn.elements("field12")(0)

  Loop_2.io.InLiveIn(3) <> Loop_3.io.OutLiveIn.elements("field9")(0)

  Loop_2.io.InLiveIn(4) <> Loop_3.io.OutLiveIn.elements("field7")(0)

  Loop_2.io.InLiveIn(5) <> Loop_3.io.OutLiveIn.elements("field11")(0)

  Loop_2.io.InLiveIn(6) <> Loop_3.io.OutLiveIn.elements("field6")(0)

  Loop_2.io.InLiveIn(7) <> Loop_3.io.OutLiveIn.elements("field8")(0)

  Loop_2.io.InLiveIn(8) <> Loop_3.io.OutLiveIn.elements("field10")(0)

  Loop_2.io.InLiveIn(9) <> Loop_3.io.OutLiveIn.elements("field5")(0)

  Loop_2.io.InLiveIn(10) <> Loop_3.io.OutLiveIn.elements("field15")(0)

  Loop_2.io.InLiveIn(11) <> Loop_3.io.OutLiveIn.elements("field4")(0)

  Loop_2.io.InLiveIn(12) <> Loop_3.io.OutLiveIn.elements("field13")(0)

  Loop_2.io.InLiveIn(13) <> Loop_3.io.OutLiveIn.elements("field14")(0)

  Loop_3.io.InLiveIn(0) <> sext13.io.Out(0)

  Loop_3.io.InLiveIn(1) <> binaryOp_shr0.io.Out(0)

  Loop_3.io.InLiveIn(2) <> icmp_cmp3826.io.Out(0)

  Loop_3.io.InLiveIn(3) <> binaryOp_mul1.io.Out(0)

  Loop_3.io.InLiveIn(4) <> sext12.io.Out(0)

  Loop_3.io.InLiveIn(5) <> icmp_cmp10768.io.Out(0)

  Loop_3.io.InLiveIn(6) <> icmp_cmp15719.io.Out(0)

  Loop_3.io.InLiveIn(7) <> ArgSplitter.io.Out.dataPtrs.elements("field2")(0)

  Loop_3.io.InLiveIn(8) <> ArgSplitter.io.Out.dataPtrs.elements("field0")(0)

  Loop_3.io.InLiveIn(9) <> sextwide_trip_count16.io.Out(0)

  Loop_3.io.InLiveIn(10) <> binaryOp_10.io.Out(0)

  Loop_3.io.InLiveIn(11) <> ArgSplitter.io.Out.dataVals.elements("field0")(0)

  Loop_3.io.InLiveIn(12) <> binaryOp_11.io.Out(0)

  Loop_3.io.InLiveIn(13) <> ArgSplitter.io.Out.dataVals.elements("field3")(0)

  Loop_3.io.InLiveIn(14) <> ArgSplitter.io.Out.dataPtrs.elements("field1")(0)

  Loop_3.io.InLiveIn(15) <> sextwide_trip_count9415.io.Out(0)

  Loop_3.io.InLiveIn(16) <> sext14.io.Out(0)

  Loop_3.io.InLiveIn(17) <> binaryOp_sub2.io.Out(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */

  phiindvars_iv8862.io.InData(1) <> Loop_0.io.OutLiveIn.elements("field0")(0)

  phival_17364.io.InData(1) <> Loop_0.io.OutLiveIn.elements("field1")(0)

  binaryOp_add1969.io.LeftIO <> Loop_0.io.OutLiveIn.elements("field2")(0)

  icmp_exitcond76.io.RightIO <> Loop_0.io.OutLiveIn.elements("field3")(0)

  Gep_arrayidx66.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field4")(0)

  Gep_arrayidx2171.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field5")(0)

  binaryOp_add50.io.LeftIO <> Loop_1.io.OutLiveIn.elements("field0")(0)

  phiindex2_07748.io.InData(0) <> Loop_1.io.OutLiveIn.elements("field4")(0)

  icmp_exitcond9060.io.RightIO <> Loop_1.io.OutLiveIn.elements("field5")(0)

  binaryOp_add2558.io.RightIO <> Loop_1.io.OutLiveIn.elements("field6")(0)

  br_49.io.CmpIO <> Loop_1.io.OutLiveIn.elements("field7")(0)

  binaryOp_54.io.LeftIO <> Loop_1.io.OutLiveIn.elements("field8")(0)

  binaryOp_39.io.RightIO <> Loop_2.io.OutLiveIn.elements("field1")(0)

  br_32.io.CmpIO <> Loop_2.io.OutLiveIn.elements("field9")(0)

  icmp_exitcond9543.io.RightIO <> Loop_2.io.OutLiveIn.elements("field10")(0)

  phiindvars_iv9131.io.InData(0) <> Loop_2.io.OutLiveIn.elements("field11")(0)

  binaryOp_shr2938.io.RightIO <> Loop_2.io.OutLiveIn.elements("field12")(0)

  Gep_arrayidx3240.io.baseAddress <> Loop_2.io.OutLiveIn.elements("field13")(0)

  phiindvars_iv9620.io.InData(0) <> Loop_3.io.OutLiveIn.elements("field0")(0)

  phij_08721.io.InData(0) <> Loop_3.io.OutLiveIn.elements("field1")(0)

  br_22.io.CmpIO <> Loop_3.io.OutLiveIn.elements("field2")(0)

  binaryOp_24.io.RightIO <> Loop_3.io.OutLiveIn.elements("field3")(0)

  binaryOp_indvars_iv_next9727.io.RightIO <> Loop_3.io.OutLiveIn.elements("field16")(0)

  icmp_exitcond9929.io.RightIO <> Loop_3.io.OutLiveIn.elements("field17")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */

  Loop_0.io.InLiveOut(0) <> binaryOp_add2374.io.Out(0)

  Loop_1.io.InLiveOut(0) <> phival_1_lcssa57.io.Out(0)



  /* ================================================================== *
   *                   LOOP LIVE OUT DEPENDENCIES                       *
   * ================================================================== */

  phiadd23_lcssa53.io.InData(0) <> Loop_0.io.OutLiveOut.elements("field0")(0)

  phival_0_lcssa37.io.InData(1) <> Loop_1.io.OutLiveOut.elements("field0")(0)



  /* ================================================================== *
   *                   LOOP CARRY DEPENDENCIES                          *
   * ================================================================== */

  Loop_0.io.CarryDepenIn(0) <> binaryOp_indvars_iv_next8965.io.Out(0)

  Loop_0.io.CarryDepenIn(1) <> binaryOp_add2374.io.Out(1)

  Loop_0.io.CarryDepenIn(2) <> binaryOp_indvars_iv_next75.io.Out(0)

  Loop_1.io.CarryDepenIn(0) <> phic_1_lcssa56.io.Out(0)

  Loop_1.io.CarryDepenIn(1) <> binaryOp_inc2759.io.Out(0)

  Loop_1.io.CarryDepenIn(2) <> phival_1_lcssa57.io.Out(1)

  Loop_1.io.CarryDepenIn(3) <> binaryOp_add2558.io.Out(0)

  Loop_2.io.CarryDepenIn(0) <> binaryOp_indvars_iv_next9242.io.Out(0)

  Loop_3.io.CarryDepenIn(0) <> binaryOp_indvars_iv_next9727.io.Out(0)

  Loop_3.io.CarryDepenIn(1) <> binaryOp_inc3828.io.Out(0)



  /* ================================================================== *
   *                   LOOP DATA CARRY DEPENDENCIES                     *
   * ================================================================== */

  phiindvars_iv8862.io.InData(0) <> Loop_0.io.CarryDepenOut.elements("field0")(0)

  phival_17364.io.InData(0) <> Loop_0.io.CarryDepenOut.elements("field1")(0)

  phiindvars_iv63.io.InData(0) <> Loop_0.io.CarryDepenOut.elements("field2")(0)

  phic_07847.io.InData(1) <> Loop_1.io.CarryDepenOut.elements("field0")(0)

  phiy_08045.io.InData(1) <> Loop_1.io.CarryDepenOut.elements("field1")(0)

  phival_07946.io.InData(1) <> Loop_1.io.CarryDepenOut.elements("field2")(0)

  phiindex2_07748.io.InData(1) <> Loop_1.io.CarryDepenOut.elements("field3")(0)

  phiindvars_iv9131.io.InData(1) <> Loop_2.io.CarryDepenOut.elements("field0")(0)

  phiindvars_iv9620.io.InData(1) <> Loop_3.io.CarryDepenOut.elements("field0")(0)

  phij_08721.io.InData(1) <> Loop_3.io.CarryDepenOut.elements("field1")(0)



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  const0.io.enable <> bb_entry0.io.Out(0)

  binaryOp_shr0.io.enable <> bb_entry0.io.Out(1)


  binaryOp_mul1.io.enable <> bb_entry0.io.Out(2)


  binaryOp_sub2.io.enable <> bb_entry0.io.Out(3)


  icmp_cmp843.io.enable <> bb_entry0.io.Out(4)


  br_4.io.enable <> bb_entry0.io.Out(5)


  const1.io.enable <> bb_for_body_lr_ph1.io.Out(0)

  const2.io.enable <> bb_for_body_lr_ph1.io.Out(1)

  const3.io.enable <> bb_for_body_lr_ph1.io.Out(2)

  const4.io.enable <> bb_for_body_lr_ph1.io.Out(3)

  const5.io.enable <> bb_for_body_lr_ph1.io.Out(4)

  binaryOp_sub25.io.enable <> bb_for_body_lr_ph1.io.Out(5)


  icmp_cmp3826.io.enable <> bb_for_body_lr_ph1.io.Out(6)


  binaryOp_mul97.io.enable <> bb_for_body_lr_ph1.io.Out(7)


  icmp_cmp10768.io.enable <> bb_for_body_lr_ph1.io.Out(8)


  icmp_cmp15719.io.enable <> bb_for_body_lr_ph1.io.Out(9)


  binaryOp_10.io.enable <> bb_for_body_lr_ph1.io.Out(10)


  binaryOp_11.io.enable <> bb_for_body_lr_ph1.io.Out(11)


  sext12.io.enable <> bb_for_body_lr_ph1.io.Out(12)


  sext13.io.enable <> bb_for_body_lr_ph1.io.Out(13)


  sext14.io.enable <> bb_for_body_lr_ph1.io.Out(14)


  sextwide_trip_count9415.io.enable <> bb_for_body_lr_ph1.io.Out(15)


  sextwide_trip_count16.io.enable <> bb_for_body_lr_ph1.io.Out(16)


  br_17.io.enable <> bb_for_body_lr_ph1.io.Out(17)


  br_18.io.enable <> bb_for_cond_cleanup_loopexit2.io.Out(0)


  ret_19.io.In.enable <> bb_for_cond_cleanup3.io.Out(0)


  phiindvars_iv9620.io.enable <> bb_for_body4.io.Out(0)


  phij_08721.io.enable <> bb_for_body4.io.Out(1)


  br_22.io.enable <> bb_for_body4.io.Out(2)


  trunc23.io.enable <> bb_for_body5_lr_ph5.io.Out(0)


  binaryOp_24.io.enable <> bb_for_body5_lr_ph5.io.Out(1)


  br_25.io.enable <> bb_for_body5_lr_ph5.io.Out(2)


  br_26.io.enable <> bb_for_cond_cleanup4_loopexit6.io.Out(0)


  const6.io.enable <> bb_for_cond_cleanup47.io.Out(0)

  binaryOp_indvars_iv_next9727.io.enable <> bb_for_cond_cleanup47.io.Out(1)


  binaryOp_inc3828.io.enable <> bb_for_cond_cleanup47.io.Out(2)


  icmp_exitcond9929.io.enable <> bb_for_cond_cleanup47.io.Out(3)


  br_30.io.enable <> bb_for_cond_cleanup47.io.Out(4)


  phiindvars_iv9131.io.enable <> bb_for_body58.io.Out(0)


  br_32.io.enable <> bb_for_body58.io.Out(1)


  const7.io.enable <> bb_for_body12_lr_ph9.io.Out(0)

  trunc33.io.enable <> bb_for_body12_lr_ph9.io.Out(1)


  binaryOp_sub1834.io.enable <> bb_for_body12_lr_ph9.io.Out(2)


  br_35.io.enable <> bb_for_body12_lr_ph9.io.Out(3)


  br_36.io.enable <> bb_for_cond_cleanup11_loopexit10.io.Out(0)


  const8.io.enable <> bb_for_cond_cleanup1111.io.Out(0)

  const9.io.enable <> bb_for_cond_cleanup1111.io.Out(1)

  phival_0_lcssa37.io.enable <> bb_for_cond_cleanup1111.io.Out(2)


  binaryOp_shr2938.io.enable <> bb_for_cond_cleanup1111.io.Out(3)


  binaryOp_39.io.enable <> bb_for_cond_cleanup1111.io.Out(4)


  Gep_arrayidx3240.io.enable <> bb_for_cond_cleanup1111.io.Out(5)


  st_41.io.enable <> bb_for_cond_cleanup1111.io.Out(6)


  binaryOp_indvars_iv_next9242.io.enable <> bb_for_cond_cleanup1111.io.Out(7)


  icmp_exitcond9543.io.enable <> bb_for_cond_cleanup1111.io.Out(8)


  br_44.io.enable <> bb_for_cond_cleanup1111.io.Out(9)


  const10.io.enable <> bb_for_body1212.io.Out(0)

  const11.io.enable <> bb_for_body1212.io.Out(1)

  const12.io.enable <> bb_for_body1212.io.Out(2)

  phiy_08045.io.enable <> bb_for_body1212.io.Out(3)


  phival_07946.io.enable <> bb_for_body1212.io.Out(4)


  phic_07847.io.enable <> bb_for_body1212.io.Out(5)


  phiindex2_07748.io.enable <> bb_for_body1212.io.Out(6)


  br_49.io.enable <> bb_for_body1212.io.Out(7)


  binaryOp_add50.io.enable <> bb_for_body17_lr_ph13.io.Out(0)


  sext51.io.enable <> bb_for_body17_lr_ph13.io.Out(1)


  br_52.io.enable <> bb_for_body17_lr_ph13.io.Out(2)


  phiadd23_lcssa53.io.enable <> bb_for_cond_cleanup16_loopexit14.io.Out(0)


  binaryOp_54.io.enable <> bb_for_cond_cleanup16_loopexit14.io.Out(1)


  br_55.io.enable <> bb_for_cond_cleanup16_loopexit14.io.Out(2)


  const13.io.enable <> bb_for_cond_cleanup1615.io.Out(0)

  phic_1_lcssa56.io.enable <> bb_for_cond_cleanup1615.io.Out(1)


  phival_1_lcssa57.io.enable <> bb_for_cond_cleanup1615.io.Out(2)


  binaryOp_add2558.io.enable <> bb_for_cond_cleanup1615.io.Out(3)


  binaryOp_inc2759.io.enable <> bb_for_cond_cleanup1615.io.Out(4)


  icmp_exitcond9060.io.enable <> bb_for_cond_cleanup1615.io.Out(5)


  br_61.io.enable <> bb_for_cond_cleanup1615.io.Out(6)


  const14.io.enable <> bb_for_body1716.io.Out(0)

  const15.io.enable <> bb_for_body1716.io.Out(1)

  const16.io.enable <> bb_for_body1716.io.Out(2)

  phiindvars_iv8862.io.enable <> bb_for_body1716.io.Out(3)


  phiindvars_iv63.io.enable <> bb_for_body1716.io.Out(4)


  phival_17364.io.enable <> bb_for_body1716.io.Out(5)


  binaryOp_indvars_iv_next8965.io.enable <> bb_for_body1716.io.Out(6)


  Gep_arrayidx66.io.enable <> bb_for_body1716.io.Out(7)


  ld_67.io.enable <> bb_for_body1716.io.Out(8)


  trunc68.io.enable <> bb_for_body1716.io.Out(9)


  binaryOp_add1969.io.enable <> bb_for_body1716.io.Out(10)


  sextidxprom2070.io.enable <> bb_for_body1716.io.Out(11)


  Gep_arrayidx2171.io.enable <> bb_for_body1716.io.Out(12)


  ld_72.io.enable <> bb_for_body1716.io.Out(13)


  binaryOp_mul2273.io.enable <> bb_for_body1716.io.Out(14)


  binaryOp_add2374.io.enable <> bb_for_body1716.io.Out(15)


  binaryOp_indvars_iv_next75.io.enable <> bb_for_body1716.io.Out(16)


  icmp_exitcond76.io.enable <> bb_for_body1716.io.Out(17)


  br_77.io.enable <> bb_for_body1716.io.Out(18)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  phiindvars_iv9620.io.Mask <> bb_for_body4.io.MaskBB(0)

  phij_08721.io.Mask <> bb_for_body4.io.MaskBB(1)

  phiindvars_iv9131.io.Mask <> bb_for_body58.io.MaskBB(0)

  phival_0_lcssa37.io.Mask <> bb_for_cond_cleanup1111.io.MaskBB(0)

  phiy_08045.io.Mask <> bb_for_body1212.io.MaskBB(0)

  phival_07946.io.Mask <> bb_for_body1212.io.MaskBB(1)

  phic_07847.io.Mask <> bb_for_body1212.io.MaskBB(2)

  phiindex2_07748.io.Mask <> bb_for_body1212.io.MaskBB(3)

  phiadd23_lcssa53.io.Mask <> bb_for_cond_cleanup16_loopexit14.io.MaskBB(0)

  phic_1_lcssa56.io.Mask <> bb_for_cond_cleanup1615.io.MaskBB(0)

  phival_1_lcssa57.io.Mask <> bb_for_cond_cleanup1615.io.MaskBB(1)

  phiindvars_iv8862.io.Mask <> bb_for_body1716.io.MaskBB(0)

  phiindvars_iv63.io.Mask <> bb_for_body1716.io.MaskBB(1)

  phival_17364.io.Mask <> bb_for_body1716.io.MaskBB(2)



  /* ================================================================== *
   *                   PRINT ALLOCA OFFSET                              *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */

  MemCtrl.io.wr.mem(0).MemReq <> st_41.io.MemReq

  st_41.io.MemResp <> MemCtrl.io.wr.mem(0).MemResp

  MemCtrl.io.rd.mem(0).MemReq <> ld_67.io.MemReq

  ld_67.io.MemResp <> MemCtrl.io.rd.mem(0).MemResp

  MemCtrl.io.rd.mem(1).MemReq <> ld_72.io.MemReq

  ld_72.io.MemResp <> MemCtrl.io.rd.mem(1).MemResp



  /* ================================================================== *
   *                   PRINT SHARED CONNECTIONS                         *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  binaryOp_shr0.io.RightIO <> const0.io.Out

  binaryOp_mul97.io.RightIO <> const1.io.Out

  icmp_cmp10768.io.RightIO <> const2.io.Out

  icmp_cmp15719.io.RightIO <> const3.io.Out

  binaryOp_10.io.RightIO <> const4.io.Out

  binaryOp_11.io.RightIO <> const5.io.Out

  binaryOp_inc3828.io.RightIO <> const6.io.Out

  binaryOp_sub1834.io.RightIO <> const7.io.Out

  phival_0_lcssa37.io.InData(0) <> const8.io.Out

  binaryOp_indvars_iv_next9242.io.RightIO <> const9.io.Out

  phiy_08045.io.InData(0) <> const10.io.Out

  phival_07946.io.InData(0) <> const11.io.Out

  phic_07847.io.InData(0) <> const12.io.Out

  binaryOp_inc2759.io.RightIO <> const13.io.Out

  phiindvars_iv63.io.InData(1) <> const14.io.Out

  binaryOp_indvars_iv_next8965.io.RightIO <> const15.io.Out

  binaryOp_indvars_iv_next75.io.RightIO <> const16.io.Out

  binaryOp_mul1.io.LeftIO <> binaryOp_shr0.io.Out(1)

  binaryOp_sub2.io.RightIO <> binaryOp_shr0.io.Out(2)

  icmp_cmp843.io.LeftIO <> binaryOp_shr0.io.Out(3)

  binaryOp_sub25.io.RightIO <> binaryOp_shr0.io.Out(4)

  icmp_cmp3826.io.LeftIO <> binaryOp_shr0.io.Out(5)

  sext12.io.Input <> binaryOp_shr0.io.Out(6)

  sext13.io.Input <> binaryOp_mul1.io.Out(1)

  icmp_cmp843.io.RightIO <> binaryOp_sub2.io.Out(1)

  br_4.io.CmpIO <> icmp_cmp843.io.Out(0)

  icmp_cmp3826.io.RightIO <> binaryOp_sub25.io.Out(0)

  sextwide_trip_count9415.io.Input <> binaryOp_sub25.io.Out(1)

  icmp_cmp10768.io.LeftIO <> binaryOp_mul97.io.Out(0)

  icmp_cmp15719.io.LeftIO <> binaryOp_mul97.io.Out(1)

  sextwide_trip_count16.io.Input <> binaryOp_11.io.Out(1)

  trunc23.io.Input <> phiindvars_iv9620.io.Out(1)

  binaryOp_indvars_iv_next9727.io.LeftIO <> phiindvars_iv9620.io.Out(2)

  binaryOp_inc3828.io.LeftIO <> phij_08721.io.Out(0)

  binaryOp_24.io.LeftIO <> trunc23.io.Out(0)

  icmp_exitcond9929.io.LeftIO <> binaryOp_inc3828.io.Out(1)

  br_30.io.CmpIO <> icmp_exitcond9929.io.Out(0)

  trunc33.io.Input <> phiindvars_iv9131.io.Out(0)

  binaryOp_39.io.LeftIO <> phiindvars_iv9131.io.Out(1)

  binaryOp_indvars_iv_next9242.io.LeftIO <> phiindvars_iv9131.io.Out(2)

  binaryOp_sub1834.io.LeftIO <> trunc33.io.Out(0)

  binaryOp_shr2938.io.LeftIO <> phival_0_lcssa37.io.Out(0)

  st_41.io.inData <> binaryOp_shr2938.io.Out(0)

  Gep_arrayidx3240.io.idx(0) <> binaryOp_39.io.Out(0)

  st_41.io.GepAddr <> Gep_arrayidx3240.io.Out(0)

  icmp_exitcond9543.io.LeftIO <> binaryOp_indvars_iv_next9242.io.Out(1)

  br_44.io.CmpIO <> icmp_exitcond9543.io.Out(0)

  binaryOp_inc2759.io.LeftIO <> phiy_08045.io.Out(0)

  phival_1_lcssa57.io.InData(0) <> phival_07946.io.Out(1)

  sext51.io.Input <> phic_07847.io.Out(0)

  binaryOp_54.io.RightIO <> phic_07847.io.Out(1)

  phic_1_lcssa56.io.InData(0) <> phic_07847.io.Out(2)

  binaryOp_add50.io.RightIO <> phiindex2_07748.io.Out(0)

  binaryOp_add2558.io.LeftIO <> phiindex2_07748.io.Out(1)

  phival_1_lcssa57.io.InData(1) <> phiadd23_lcssa53.io.Out(0)

  phic_1_lcssa56.io.InData(1) <> binaryOp_54.io.Out(0)

  icmp_exitcond9060.io.LeftIO <> binaryOp_inc2759.io.Out(1)

  br_61.io.CmpIO <> icmp_exitcond9060.io.Out(0)

  binaryOp_indvars_iv_next8965.io.LeftIO <> phiindvars_iv8862.io.Out(0)

  Gep_arrayidx66.io.idx(0) <> phiindvars_iv8862.io.Out(1)

  trunc68.io.Input <> phiindvars_iv63.io.Out(0)

  binaryOp_indvars_iv_next75.io.LeftIO <> phiindvars_iv63.io.Out(1)

  binaryOp_add2374.io.RightIO <> phival_17364.io.Out(0)

  ld_67.io.GepAddr <> Gep_arrayidx66.io.Out(0)

  binaryOp_mul2273.io.RightIO <> ld_67.io.Out(0)

  binaryOp_add1969.io.RightIO <> trunc68.io.Out(0)

  sextidxprom2070.io.Input <> binaryOp_add1969.io.Out(0)

  Gep_arrayidx2171.io.idx(0) <> sextidxprom2070.io.Out(0)

  ld_72.io.GepAddr <> Gep_arrayidx2171.io.Out(0)

  binaryOp_mul2273.io.LeftIO <> ld_72.io.Out(0)

  binaryOp_add2374.io.LeftIO <> binaryOp_mul2273.io.Out(0)

  icmp_exitcond76.io.LeftIO <> binaryOp_indvars_iv_next75.io.Out(1)

  br_77.io.CmpIO <> icmp_exitcond76.io.Out(0)

  binaryOp_mul1.io.RightIO <> ArgSplitter.io.Out.dataVals.elements("field0")(1)

  binaryOp_sub25.io.LeftIO <> ArgSplitter.io.Out.dataVals.elements("field0")(2)

  sext14.io.Input <> ArgSplitter.io.Out.dataVals.elements("field0")(3)

  binaryOp_sub2.io.LeftIO <> ArgSplitter.io.Out.dataVals.elements("field1")(0)

  binaryOp_shr0.io.LeftIO <> ArgSplitter.io.Out.dataVals.elements("field2")(0)

  binaryOp_mul97.io.LeftIO <> ArgSplitter.io.Out.dataVals.elements("field2")(1)

  binaryOp_10.io.LeftIO <> ArgSplitter.io.Out.dataVals.elements("field2")(2)

  binaryOp_11.io.LeftIO <> ArgSplitter.io.Out.dataVals.elements("field2")(3)

  st_41.io.Out(0).ready := true.B



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  br_44.io.PredOp(0) <> st_41.io.SuccOp(0)



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_19.io.Out

}

