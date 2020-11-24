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


class conv2dDF(PtrsIn: Seq[Int] = List(64, 64, 64), ValsIn: Seq[Int] = List(64, 64, 64, 64), Returns: Seq[Int] = List())
			(implicit p: Parameters) extends DandelionAccelDCRModule(PtrsIn, ValsIn, Returns){


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  //Cache
  val mem_ctrl_cache = Module(new CacheMemoryEngine(ID = 0, NumRead = 2, NumWrite = 1))

  io.MemReq <> mem_ctrl_cache.io.cache.MemReq
  mem_ctrl_cache.io.cache.MemResp <> io.MemResp

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

  val bb_entry1 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 6, BID = 1))

  val bb_for_body_lr_ph7 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 18, BID = 7))

  val bb_for_cond_cleanup_loopexit21 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 21))

  val bb_for_cond_cleanup23 = Module(new BasicBlockNoMaskFastNode(NumInputs = 2, NumOuts = 1, BID = 23))

  val bb_for_body25 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 3, NumPhi = 2, BID = 25))

  val bb_for_body5_lr_ph29 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 3, BID = 29))

  val bb_for_cond_cleanup4_loopexit33 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 33))

  val bb_for_cond_cleanup435 = Module(new BasicBlockNoMaskFastNode(NumInputs = 2, NumOuts = 5, BID = 35))

  val bb_for_body540 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 2, NumPhi = 1, BID = 40))

  val bb_for_body12_lr_ph43 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 4, BID = 43))

  val bb_for_cond_cleanup11_loopexit47 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 47))

  val bb_for_cond_cleanup1149 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 10, NumPhi = 1, BID = 49))

  val bb_for_body1258 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 8, NumPhi = 4, BID = 58))

  val bb_for_body17_lr_ph64 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 3, BID = 64))

  val bb_for_cond_cleanup16_loopexit68 = Module(new BasicBlockNode(NumInputs = 1, NumOuts = 3, NumPhi = 1, BID = 68))

  val bb_for_cond_cleanup1672 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 7, NumPhi = 2, BID = 72))

  val bb_for_body1779 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 19, NumPhi = 3, BID = 79))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  %shr = ashr i32 %K, 1, !dbg !70, !UID !71
  val binaryOp_shr2 = Module(new ComputeNode(NumOuts = 7, ID = 2, opCode = "ashr")(sign = false, Debug = true, GuardVals=Seq.tabulate(100)(n => n)))

  //  %mul = mul nsw i32 %shr, %W, !dbg !73, !UID !74
  val binaryOp_mul3 = Module(new ComputeNode(NumOuts = 2, ID = 3, opCode = "mul")(sign = false,  Debug = true, GuardVals=Seq.tabulate(100)(n => n)))

  //  %sub = sub nsw i32 %H, %shr, !dbg !77, !UID !78
  val binaryOp_sub4 = Module(new ComputeNode(NumOuts = 2, ID = 4, opCode = "sub")(sign = false,  Debug = true, GuardVals=Seq.tabulate(100)(n => n)))

  //  %cmp84 = icmp slt i32 %shr, %sub, !dbg !79, !UID !80
  val icmp_cmp843 = Module(new ComputeNode(NumOuts = 1, ID = 33, opCode = "slt")(sign = true,  Debug = true, GuardVals=Seq.tabulate(100)(n => n)))

  //  br i1 %cmp84, label %for.body.lr.ph, label %for.cond.cleanup, !dbg !81, !UID !82, !BB_UID !83
  val br_6 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 6))

  //  %sub2 = sub nsw i32 %W, %shr, !UID !84
  val binaryOp_sub28 = Module(new ComputeNode(NumOuts = 2, ID = 8, opCode = "sub")(sign = false, Debug = false))

  //  %cmp382 = icmp slt i32 %shr, %sub2, !UID !85
  val icmp_cmp3826 = Module(new ComputeNode(NumOuts = 1, ID = 6, opCode = "slt")(sign = true, Debug = false))

  //  %mul9 = and i32 %K, -2, !UID !86
  val binaryOp_mul910 = Module(new ComputeNode(NumOuts = 2, ID = 10, opCode = "and")(sign = false, Debug = false))

  //  %cmp1076 = icmp slt i32 %mul9, 0, !UID !87
  val icmp_cmp10768 = Module(new ComputeNode(NumOuts = 1, ID = 8, opCode = "slt")(sign = true, Debug = false))

  //  %cmp1571 = icmp slt i32 %mul9, 0, !UID !88
  val icmp_cmp15719 = Module(new ComputeNode(NumOuts = 1, ID = 9, opCode = "slt")(sign = true, Debug = false))

  //  %0 = or i32 %K, 1, !dbg !89, !UID !90
  val binaryOp_13 = Module(new ComputeNode(NumOuts = 1, ID = 13, opCode = "or")(sign = false, Debug = false))

  //  %1 = or i32 %K, 1, !dbg !81, !UID !91
  val binaryOp_14 = Module(new ComputeNode(NumOuts = 2, ID = 14, opCode = "or")(sign = false, Debug = false))

  //  %2 = sext i32 %shr to i64, !dbg !81, !UID !92
  val sext15 = Module(new SextNode(NumOuts = 1))

  //  %3 = sext i32 %mul to i64, !dbg !81, !UID !93
  val sext16 = Module(new SextNode(NumOuts = 1))

  //  %4 = sext i32 %W to i64, !dbg !81, !UID !94
  val sext17 = Module(new SextNode(NumOuts = 1))

  //  %wide.trip.count94 = sext i32 %sub2 to i64, !UID !95
  val sextwide_trip_count9418 = Module(new SextNode(NumOuts = 1))

  //  %wide.trip.count = zext i32 %1 to i64, !UID !96
  val sextwide_trip_count19 = Module(new ZextNode(NumOuts = 1))

  //  br label %for.body, !dbg !81, !UID !97, !BB_UID !98
  val br_20 = Module(new UBranchNode(ID = 20))

  //  br label %for.cond.cleanup, !dbg !99, !UID !100, !BB_UID !101
  val br_22 = Module(new UBranchNode(ID = 22))

  //  ret void, !dbg !99, !UID !102, !BB_UID !103
  val ret_24 = Module(new RetNode2(retTypes = List(), ID = 24))

  //  %indvars.iv96 = phi i64 [ %3, %for.body.lr.ph ], [ %indvars.iv.next97, %for.cond.cleanup4 ], !UID !104
  val phiindvars_iv9626 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 3, ID = 26, Res = true))

  //  %j.087 = phi i32 [ %shr, %for.body.lr.ph ], [ %inc38, %for.cond.cleanup4 ], !UID !105
  val phij_08727 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 1, ID = 27, Res = true))

  //  br i1 %cmp382, label %for.body5.lr.ph, label %for.cond.cleanup4, !dbg !107, !UID !108, !BB_UID !109
  val br_28 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 28))

  //  %5 = trunc i64 %indvars.iv96 to i32, !dbg !107, !UID !110
  val trunc30 = Module(new TruncNode(NumOuts = 1))

  //  %6 = sub i32 %5, %mul, !dbg !107, !UID !111
  val binaryOp_31 = Module(new ComputeNode(NumOuts = 1, ID = 31, opCode = "sub")(sign = false, Debug = false))

  //  br label %for.body5, !dbg !107, !UID !112, !BB_UID !113
  val br_32 = Module(new UBranchNode(ID = 32))

  //  br label %for.cond.cleanup4, !dbg !114, !UID !115, !BB_UID !116
  val br_34 = Module(new UBranchNode(ID = 34))

  //  %indvars.iv.next97 = add i64 %indvars.iv96, %4, !dbg !114, !UID !117
  val binaryOp_indvars_iv_next9736 = Module(new ComputeNode(NumOuts = 1, ID = 36, opCode = "add")(sign = false, Debug = false))

  //  %inc38 = add nsw i32 %j.087, 1, !dbg !118, !UID !119
  val binaryOp_inc3837 = Module(new ComputeNode(NumOuts = 2, ID = 37, opCode = "add")(sign = false, Debug = false))

  //  %exitcond99 = icmp eq i32 %inc38, %sub, !dbg !79, !UID !120
  val icmp_exitcond9929 = Module(new ComputeNode(NumOuts = 1, ID = 29, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond99, label %for.cond.cleanup.loopexit, label %for.body, !dbg !81, !llvm.loop !121, !UID !123, !BB_UID !124
  val br_39 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 39))

  //  %indvars.iv91 = phi i64 [ %2, %for.body5.lr.ph ], [ %indvars.iv.next92, %for.cond.cleanup11 ], !UID !125
  val phiindvars_iv9141 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 3, ID = 41, Res = true))

  //  br i1 %cmp1076, label %for.cond.cleanup11, label %for.body12.lr.ph, !dbg !129, !UID !130, !BB_UID !131
  val br_42 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 42))

  //  %7 = trunc i64 %indvars.iv91 to i32, !UID !132
  val trunc44 = Module(new TruncNode(NumOuts = 1))

  //  %sub18 = add i32 %7, -2, !UID !133
  val binaryOp_sub1845 = Module(new ComputeNode(NumOuts = 1, ID = 45, opCode = "add")(sign = false, Debug = false))

  //  br label %for.body12, !dbg !129, !UID !134, !BB_UID !135
  val br_46 = Module(new UBranchNode(ID = 46))

  //  br label %for.cond.cleanup11, !dbg !136, !UID !137, !BB_UID !138
  val br_48 = Module(new UBranchNode(ID = 48))

  //  %val.0.lcssa = phi i32 [ 0, %for.body5 ], [ %val.1.lcssa, %for.cond.cleanup11.loopexit ], !UID !139
  val phival_0_lcssa50 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 1, ID = 50, Res = true))

  //  %shr29 = ashr i32 %val.0.lcssa, %scf, !dbg !136, !UID !140
  val binaryOp_shr2951 = Module(new ComputeNode(NumOuts = 1, ID = 51, opCode = "ashr")(sign = false, Debug = false))

  //  %8 = add nsw i64 %indvars.iv91, %indvars.iv96, !dbg !141, !UID !142
  val binaryOp_52 = Module(new ComputeNode(NumOuts = 1, ID = 52, opCode = "add")(sign = false, Debug = false))

  //  %arrayidx32 = getelementptr inbounds i32, i32* %res, i64 %8, !dbg !143, !UID !144
  val Gep_arrayidx3253 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 53)(ElementSize = 8, ArraySize = List()))

  //  store i32 %shr29, i32* %arrayidx32, align 4, !dbg !145, !tbaa !146, !UID !150
  val st_54 = Module(new UnTypStoreCache(NumPredOps = 0, NumSuccOps = 1, ID = 54, RouteID = 2))

  //  %indvars.iv.next92 = add nsw i64 %indvars.iv91, 1, !dbg !151, !UID !152
  val binaryOp_indvars_iv_next9255 = Module(new ComputeNode(NumOuts = 2, ID = 55, opCode = "add")(sign = false, Debug = false))

  //  %exitcond95 = icmp eq i64 %indvars.iv.next92, %wide.trip.count94, !dbg !153, !UID !154
  val icmp_exitcond9543 = Module(new ComputeNode(NumOuts = 1, ID = 43, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond95, label %for.cond.cleanup4.loopexit, label %for.body5, !dbg !107, !llvm.loop !155, !UID !157, !BB_UID !158
  val br_57 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 1, ID = 57))

  //  %y.080 = phi i32 [ 0, %for.body12.lr.ph ], [ %inc27, %for.cond.cleanup16 ], !UID !159
  val phiy_08059 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 1, ID = 59, Res = true))

  //  %val.079 = phi i32 [ 0, %for.body12.lr.ph ], [ %val.1.lcssa, %for.cond.cleanup16 ], !UID !160
  val phival_07960 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 60, Res = true))

  //  %c.078 = phi i32 [ 0, %for.body12.lr.ph ], [ %c.1.lcssa, %for.cond.cleanup16 ], !UID !161
  val phic_07861 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 3, ID = 61, Res = true))

  //  %index2.077 = phi i32 [ %6, %for.body12.lr.ph ], [ %add25, %for.cond.cleanup16 ], !UID !162
  val phiindex2_07762 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 62, Res = true))

  //  br i1 %cmp1571, label %for.cond.cleanup16, label %for.body17.lr.ph, !dbg !89, !UID !165, !BB_UID !166
  val br_63 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 63))

  //  %add = add i32 %sub18, %index2.077, !UID !167
  val binaryOp_add65 = Module(new ComputeNode(NumOuts = 1, ID = 65, opCode = "add")(sign = false, Debug = false))

  //  %9 = sext i32 %c.078 to i64, !dbg !89, !UID !168
  val sext66 = Module(new SextNode(NumOuts = 1))

  //  br label %for.body17, !dbg !89, !UID !169, !BB_UID !170
  val br_67 = Module(new UBranchNode(ID = 67))

  //  %add23.lcssa = phi i32 [ %add23, %for.body17 ], !UID !171
  val phiadd23_lcssa69 = Module(new PhiFastNode(NumInputs = 1, NumOutputs = 1, ID = 69, Res = false))

  //  %10 = add i32 %0, %c.078, !dbg !89, !UID !172
  val binaryOp_70 = Module(new ComputeNode(NumOuts = 1, ID = 70, opCode = "add")(sign = false, Debug = false))

  //  br label %for.cond.cleanup16, !dbg !173, !UID !174, !BB_UID !175
  val br_71 = Module(new UBranchNode(ID = 71))

  //  %c.1.lcssa = phi i32 [ %c.078, %for.body12 ], [ %10, %for.cond.cleanup16.loopexit ], !UID !176
  val phic_1_lcssa73 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 1, ID = 73, Res = true))

  //  %val.1.lcssa = phi i32 [ %val.079, %for.body12 ], [ %add23.lcssa, %for.cond.cleanup16.loopexit ], !UID !177
  val phival_1_lcssa74 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 74, Res = true))

  //  %add25 = add nsw i32 %index2.077, %W, !dbg !173, !UID !178
  val binaryOp_add2575 = Module(new ComputeNode(NumOuts = 1, ID = 75, opCode = "add")(sign = false, Debug = false))

  //  %inc27 = add nuw nsw i32 %y.080, 1, !dbg !179, !UID !180
  val binaryOp_inc2776 = Module(new ComputeNode(NumOuts = 2, ID = 76, opCode = "add")(sign = false, Debug = false))

  //  %exitcond90 = icmp eq i32 %inc27, %1, !dbg !181, !UID !182
  val icmp_exitcond9060 = Module(new ComputeNode(NumOuts = 1, ID = 60, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond90, label %for.cond.cleanup11.loopexit, label %for.body12, !dbg !129, !llvm.loop !183, !UID !185, !BB_UID !186
  val br_78 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 78))

  //  %indvars.iv88 = phi i64 [ %indvars.iv.next89, %for.body17 ], [ %9, %for.body17.lr.ph ], !UID !187
  val phiindvars_iv8880 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 80, Res = false))

  //  %indvars.iv = phi i64 [ %indvars.iv.next, %for.body17 ], [ 0, %for.body17.lr.ph ], !UID !188
  val phiindvars_iv81 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 81, Res = false))

  //  %val.173 = phi i32 [ %add23, %for.body17 ], [ %val.079, %for.body17.lr.ph ], !UID !189
  val phival_17382 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 1, ID = 82, Res = false))

  //  %indvars.iv.next89 = add nsw i64 %indvars.iv88, 1, !dbg !190, !UID !193
  val binaryOp_indvars_iv_next8983 = Module(new ComputeNode(NumOuts = 1, ID = 83, opCode = "add")(sign = false, Debug = false))

  //  %arrayidx = getelementptr inbounds i32, i32* %coeffs, i64 %indvars.iv88, !dbg !194, !UID !195
  val Gep_arrayidx84 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 84)(ElementSize = 8, ArraySize = List()))

  //  %11 = load i32, i32* %arrayidx, align 4, !dbg !194, !tbaa !146, !UID !196
  val ld_85 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 85, RouteID = 0))

  //  %12 = trunc i64 %indvars.iv to i32, !dbg !197, !UID !198
  val trunc86 = Module(new TruncNode(NumOuts = 1))

  //  %add19 = add i32 %add, %12, !dbg !197, !UID !199
  val binaryOp_add1987 = Module(new ComputeNode(NumOuts = 1, ID = 87, opCode = "add")(sign = false, Debug = false))

  //  %idxprom20 = sext i32 %add19 to i64, !dbg !200, !UID !201
  val sextidxprom2088 = Module(new SextNode(NumOuts = 1))

  //  %arrayidx21 = getelementptr inbounds i32, i32* %mat, i64 %idxprom20, !dbg !200, !UID !202
  val Gep_arrayidx2189 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 89)(ElementSize = 8, ArraySize = List()))

  //  %13 = load i32, i32* %arrayidx21, align 4, !dbg !200, !tbaa !146, !UID !203
  val ld_90 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 90, RouteID = 1))

  //  %mul22 = mul nsw i32 %13, %11, !dbg !204, !UID !205
  val binaryOp_mul2291 = Module(new ComputeNode(NumOuts = 1, ID = 91, opCode = "mul")(sign = false, Debug = false))

  //  %add23 = add nsw i32 %mul22, %val.173, !dbg !206, !UID !207
  val binaryOp_add2392 = Module(new ComputeNode(NumOuts = 2, ID = 92, opCode = "add")(sign = false, Debug = false))

  //  %indvars.iv.next = add nuw nsw i64 %indvars.iv, 1, !dbg !208, !UID !209
  val binaryOp_indvars_iv_next93 = Module(new ComputeNode(NumOuts = 2, ID = 93, opCode = "add")(sign = false, Debug = false))

  //  %exitcond = icmp eq i64 %indvars.iv.next, %wide.trip.count, !dbg !210, !UID !211
  val icmp_exitcond76 = Module(new ComputeNode(NumOuts = 1, ID = 76, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond, label %for.cond.cleanup16.loopexit, label %for.body17, !dbg !89, !llvm.loop !212, !UID !214, !BB_UID !215
  val br_95 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 95))



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

  bb_entry1.io.predicateIn(0) <> ArgSplitter.io.Out.enable

  bb_for_body_lr_ph7.io.predicateIn(0) <> br_6.io.TrueOutput(0)

  bb_for_cond_cleanup23.io.predicateIn(1) <> br_6.io.FalseOutput(0)

  bb_for_cond_cleanup23.io.predicateIn(0) <> br_22.io.Out(0)

  bb_for_body5_lr_ph29.io.predicateIn(0) <> br_28.io.TrueOutput(0)

  bb_for_cond_cleanup435.io.predicateIn(1) <> br_28.io.FalseOutput(0)

  bb_for_cond_cleanup435.io.predicateIn(0) <> br_34.io.Out(0)

  bb_for_body12_lr_ph43.io.predicateIn(0) <> br_42.io.FalseOutput(0)

  bb_for_cond_cleanup1149.io.predicateIn(1) <> br_42.io.TrueOutput(0)

  bb_for_cond_cleanup1149.io.predicateIn(0) <> br_48.io.Out(0)

  bb_for_body17_lr_ph64.io.predicateIn(0) <> br_63.io.FalseOutput(0)

  bb_for_cond_cleanup1672.io.predicateIn(1) <> br_63.io.TrueOutput(0)

  bb_for_cond_cleanup1672.io.predicateIn(0) <> br_71.io.Out(0)



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE LOOP                     *
   * ================================================================== */

  bb_for_cond_cleanup_loopexit21.io.predicateIn(0) <> Loop_3.io.loopExit(0)

  bb_for_body25.io.predicateIn(1) <> Loop_3.io.activate_loop_start

  bb_for_body25.io.predicateIn(0) <> Loop_3.io.activate_loop_back

  bb_for_cond_cleanup4_loopexit33.io.predicateIn(0) <> Loop_2.io.loopExit(0)

  bb_for_body540.io.predicateIn(1) <> Loop_2.io.activate_loop_start

  bb_for_body540.io.predicateIn(0) <> Loop_2.io.activate_loop_back

  bb_for_cond_cleanup11_loopexit47.io.predicateIn(0) <> Loop_1.io.loopExit(0)

  bb_for_body1258.io.predicateIn(1) <> Loop_1.io.activate_loop_start

  bb_for_body1258.io.predicateIn(0) <> Loop_1.io.activate_loop_back

  bb_for_cond_cleanup16_loopexit68.io.predicateIn(0) <> Loop_0.io.loopExit(0)

  bb_for_body1779.io.predicateIn(1) <> Loop_0.io.activate_loop_start

  bb_for_body1779.io.predicateIn(0) <> Loop_0.io.activate_loop_back



  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */

  Loop_0.io.enable <> br_67.io.Out(0)

  Loop_0.io.loopBack(0) <> br_95.io.FalseOutput(0)

  Loop_0.io.loopFinish(0) <> br_95.io.TrueOutput(0)

  Loop_1.io.enable <> br_46.io.Out(0)

  Loop_1.io.loopBack(0) <> br_78.io.FalseOutput(0)

  Loop_1.io.loopFinish(0) <> br_78.io.TrueOutput(0)

  Loop_2.io.enable <> br_32.io.Out(0)

  Loop_2.io.loopBack(0) <> br_57.io.FalseOutput(0)

  Loop_2.io.loopFinish(0) <> br_57.io.TrueOutput(0)

  Loop_3.io.enable <> br_20.io.Out(0)

  Loop_3.io.loopBack(0) <> br_39.io.FalseOutput(0)

  Loop_3.io.loopFinish(0) <> br_39.io.TrueOutput(0)



  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

  Loop_0.io.InLiveIn(0) <> sext66.io.Out(0)

  Loop_0.io.InLiveIn(1) <> phival_07960.io.Out(0)

  Loop_0.io.InLiveIn(2) <> binaryOp_add65.io.Out(0)

  Loop_0.io.InLiveIn(3) <> Loop_1.io.OutLiveIn.elements("field1")(0)

  Loop_0.io.InLiveIn(4) <> Loop_1.io.OutLiveIn.elements("field2")(0)

  Loop_0.io.InLiveIn(5) <> Loop_1.io.OutLiveIn.elements("field3")(0)

  Loop_1.io.InLiveIn(0) <> binaryOp_sub1845.io.Out(0)

  Loop_1.io.InLiveIn(1) <> Loop_2.io.OutLiveIn.elements("field2")(0)

  Loop_1.io.InLiveIn(2) <> Loop_2.io.OutLiveIn.elements("field5")(0)

  Loop_1.io.InLiveIn(3) <> Loop_2.io.OutLiveIn.elements("field7")(0)

  Loop_1.io.InLiveIn(4) <> Loop_2.io.OutLiveIn.elements("field3")(0)

  Loop_1.io.InLiveIn(5) <> Loop_2.io.OutLiveIn.elements("field4")(0)

  Loop_1.io.InLiveIn(6) <> Loop_2.io.OutLiveIn.elements("field6")(0)

  Loop_1.io.InLiveIn(7) <> Loop_2.io.OutLiveIn.elements("field8")(0)

  Loop_1.io.InLiveIn(8) <> Loop_2.io.OutLiveIn.elements("field0")(0)

  Loop_2.io.InLiveIn(0) <> binaryOp_31.io.Out(0)

  Loop_2.io.InLiveIn(1) <> phiindvars_iv9626.io.Out(0)

  Loop_2.io.InLiveIn(2) <> Loop_3.io.OutLiveIn.elements("field8")(0)

  Loop_2.io.InLiveIn(3) <> Loop_3.io.OutLiveIn.elements("field10")(0)

  Loop_2.io.InLiveIn(4) <> Loop_3.io.OutLiveIn.elements("field11")(0)

  Loop_2.io.InLiveIn(5) <> Loop_3.io.OutLiveIn.elements("field7")(0)

  Loop_2.io.InLiveIn(6) <> Loop_3.io.OutLiveIn.elements("field6")(0)

  Loop_2.io.InLiveIn(7) <> Loop_3.io.OutLiveIn.elements("field9")(0)

  Loop_2.io.InLiveIn(8) <> Loop_3.io.OutLiveIn.elements("field12")(0)

  Loop_2.io.InLiveIn(9) <> Loop_3.io.OutLiveIn.elements("field14")(0)

  Loop_2.io.InLiveIn(10) <> Loop_3.io.OutLiveIn.elements("field5")(0)

  Loop_2.io.InLiveIn(11) <> Loop_3.io.OutLiveIn.elements("field4")(0)

  Loop_2.io.InLiveIn(12) <> Loop_3.io.OutLiveIn.elements("field13")(0)

  Loop_2.io.InLiveIn(13) <> Loop_3.io.OutLiveIn.elements("field15")(0)

  Loop_3.io.InLiveIn(0) <> sext16.io.Out(0)

  Loop_3.io.InLiveIn(1) <> binaryOp_shr2.io.Out(0)

  Loop_3.io.InLiveIn(2) <> icmp_cmp3826.io.Out(0)

  Loop_3.io.InLiveIn(3) <> binaryOp_mul3.io.Out(0)

  Loop_3.io.InLiveIn(4) <> sext15.io.Out(0)

  Loop_3.io.InLiveIn(5) <> icmp_cmp10768.io.Out(0)

  Loop_3.io.InLiveIn(6) <> icmp_cmp15719.io.Out(0)

  Loop_3.io.InLiveIn(7) <> ArgSplitter.io.Out.dataPtrs.elements("field2")(0)

  Loop_3.io.InLiveIn(8) <> ArgSplitter.io.Out.dataPtrs.elements("field0")(0)

  Loop_3.io.InLiveIn(9) <> sextwide_trip_count19.io.Out(0)

  Loop_3.io.InLiveIn(10) <> binaryOp_13.io.Out(0)

  Loop_3.io.InLiveIn(11) <> ArgSplitter.io.Out.dataVals.elements("field0")(0)

  Loop_3.io.InLiveIn(12) <> binaryOp_14.io.Out(0)

  Loop_3.io.InLiveIn(13) <> ArgSplitter.io.Out.dataVals.elements("field3")(0)

  Loop_3.io.InLiveIn(14) <> ArgSplitter.io.Out.dataPtrs.elements("field1")(0)

  Loop_3.io.InLiveIn(15) <> sextwide_trip_count9418.io.Out(0)

  Loop_3.io.InLiveIn(16) <> sext17.io.Out(0)

  Loop_3.io.InLiveIn(17) <> binaryOp_sub4.io.Out(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */

  phiindvars_iv8880.io.InData(1) <> Loop_0.io.OutLiveIn.elements("field0")(0)

  phival_17382.io.InData(1) <> Loop_0.io.OutLiveIn.elements("field1")(0)

  binaryOp_add1987.io.LeftIO <> Loop_0.io.OutLiveIn.elements("field2")(0)

  Gep_arrayidx2189.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field3")(0)

  Gep_arrayidx84.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field4")(0)

  icmp_exitcond76.io.RightIO <> Loop_0.io.OutLiveIn.elements("field5")(0)

  binaryOp_add65.io.LeftIO <> Loop_1.io.OutLiveIn.elements("field0")(0)

  binaryOp_70.io.LeftIO <> Loop_1.io.OutLiveIn.elements("field4")(0)

  binaryOp_add2575.io.RightIO <> Loop_1.io.OutLiveIn.elements("field5")(0)

  br_63.io.CmpIO <> Loop_1.io.OutLiveIn.elements("field6")(0)

  icmp_exitcond9060.io.RightIO <> Loop_1.io.OutLiveIn.elements("field7")(0)

  phiindex2_07762.io.InData(0) <> Loop_1.io.OutLiveIn.elements("field8")(0)

  binaryOp_52.io.RightIO <> Loop_2.io.OutLiveIn.elements("field1")(0)

  Gep_arrayidx3253.io.baseAddress <> Loop_2.io.OutLiveIn.elements("field9")(0)

  br_42.io.CmpIO <> Loop_2.io.OutLiveIn.elements("field10")(0)

  phiindvars_iv9141.io.InData(0) <> Loop_2.io.OutLiveIn.elements("field11")(0)

  binaryOp_shr2951.io.RightIO <> Loop_2.io.OutLiveIn.elements("field12")(0)

  icmp_exitcond9543.io.RightIO <> Loop_2.io.OutLiveIn.elements("field13")(0)

  phiindvars_iv9626.io.InData(0) <> Loop_3.io.OutLiveIn.elements("field0")(0)

  phij_08727.io.InData(0) <> Loop_3.io.OutLiveIn.elements("field1")(0)

  br_28.io.CmpIO <> Loop_3.io.OutLiveIn.elements("field2")(0)

  binaryOp_31.io.RightIO <> Loop_3.io.OutLiveIn.elements("field3")(0)

  binaryOp_indvars_iv_next9736.io.RightIO <> Loop_3.io.OutLiveIn.elements("field16")(0)

  icmp_exitcond9929.io.RightIO <> Loop_3.io.OutLiveIn.elements("field17")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */

  Loop_0.io.InLiveOut(0) <> binaryOp_add2392.io.Out(0)

  Loop_1.io.InLiveOut(0) <> phival_1_lcssa74.io.Out(0)



  /* ================================================================== *
   *                   LOOP LIVE OUT DEPENDENCIES                       *
   * ================================================================== */

  phiadd23_lcssa69.io.InData(0) <> Loop_0.io.OutLiveOut.elements("field0")(0)

  phival_0_lcssa50.io.InData(1) <> Loop_1.io.OutLiveOut.elements("field0")(0)



  /* ================================================================== *
   *                   LOOP CARRY DEPENDENCIES                          *
   * ================================================================== */

  Loop_0.io.CarryDepenIn(0) <> binaryOp_indvars_iv_next93.io.Out(0)

  Loop_0.io.CarryDepenIn(1) <> binaryOp_add2392.io.Out(1)

  Loop_0.io.CarryDepenIn(2) <> binaryOp_indvars_iv_next8983.io.Out(0)

  Loop_1.io.CarryDepenIn(0) <> binaryOp_inc2776.io.Out(0)

  Loop_1.io.CarryDepenIn(1) <> phic_1_lcssa73.io.Out(0)

  Loop_1.io.CarryDepenIn(2) <> binaryOp_add2575.io.Out(0)

  Loop_1.io.CarryDepenIn(3) <> phival_1_lcssa74.io.Out(1)

  Loop_2.io.CarryDepenIn(0) <> binaryOp_indvars_iv_next9255.io.Out(0)

  Loop_3.io.CarryDepenIn(0) <> binaryOp_indvars_iv_next9736.io.Out(0)

  Loop_3.io.CarryDepenIn(1) <> binaryOp_inc3837.io.Out(0)



  /* ================================================================== *
   *                   LOOP DATA CARRY DEPENDENCIES                     *
   * ================================================================== */

  phiindvars_iv81.io.InData(0) <> Loop_0.io.CarryDepenOut.elements("field0")(0)

  phival_17382.io.InData(0) <> Loop_0.io.CarryDepenOut.elements("field1")(0)

  phiindvars_iv8880.io.InData(0) <> Loop_0.io.CarryDepenOut.elements("field2")(0)

  phiy_08059.io.InData(1) <> Loop_1.io.CarryDepenOut.elements("field0")(0)

  phic_07861.io.InData(1) <> Loop_1.io.CarryDepenOut.elements("field1")(0)

  phiindex2_07762.io.InData(1) <> Loop_1.io.CarryDepenOut.elements("field2")(0)

  phival_07960.io.InData(1) <> Loop_1.io.CarryDepenOut.elements("field3")(0)

  phiindvars_iv9141.io.InData(1) <> Loop_2.io.CarryDepenOut.elements("field0")(0)

  phiindvars_iv9626.io.InData(1) <> Loop_3.io.CarryDepenOut.elements("field0")(0)

  phij_08727.io.InData(1) <> Loop_3.io.CarryDepenOut.elements("field1")(0)



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  const0.io.enable <> bb_entry1.io.Out(0)

  binaryOp_shr2.io.enable <> bb_entry1.io.Out(1)


  binaryOp_mul3.io.enable <> bb_entry1.io.Out(2)


  binaryOp_sub4.io.enable <> bb_entry1.io.Out(3)


  icmp_cmp843.io.enable <> bb_entry1.io.Out(4)


  br_6.io.enable <> bb_entry1.io.Out(5)


  const1.io.enable <> bb_for_body_lr_ph7.io.Out(0)

  const2.io.enable <> bb_for_body_lr_ph7.io.Out(1)

  const3.io.enable <> bb_for_body_lr_ph7.io.Out(2)

  const4.io.enable <> bb_for_body_lr_ph7.io.Out(3)

  const5.io.enable <> bb_for_body_lr_ph7.io.Out(4)

  binaryOp_sub28.io.enable <> bb_for_body_lr_ph7.io.Out(5)


  icmp_cmp3826.io.enable <> bb_for_body_lr_ph7.io.Out(6)


  binaryOp_mul910.io.enable <> bb_for_body_lr_ph7.io.Out(7)


  icmp_cmp10768.io.enable <> bb_for_body_lr_ph7.io.Out(8)


  icmp_cmp15719.io.enable <> bb_for_body_lr_ph7.io.Out(9)


  binaryOp_13.io.enable <> bb_for_body_lr_ph7.io.Out(10)


  binaryOp_14.io.enable <> bb_for_body_lr_ph7.io.Out(11)


  sext15.io.enable <> bb_for_body_lr_ph7.io.Out(12)


  sext16.io.enable <> bb_for_body_lr_ph7.io.Out(13)


  sext17.io.enable <> bb_for_body_lr_ph7.io.Out(14)


  sextwide_trip_count9418.io.enable <> bb_for_body_lr_ph7.io.Out(15)


  sextwide_trip_count19.io.enable <> bb_for_body_lr_ph7.io.Out(16)


  br_20.io.enable <> bb_for_body_lr_ph7.io.Out(17)


  br_22.io.enable <> bb_for_cond_cleanup_loopexit21.io.Out(0)


  ret_24.io.In.enable <> bb_for_cond_cleanup23.io.Out(0)


  phiindvars_iv9626.io.enable <> bb_for_body25.io.Out(0)


  phij_08727.io.enable <> bb_for_body25.io.Out(1)


  br_28.io.enable <> bb_for_body25.io.Out(2)


  trunc30.io.enable <> bb_for_body5_lr_ph29.io.Out(0)


  binaryOp_31.io.enable <> bb_for_body5_lr_ph29.io.Out(1)


  br_32.io.enable <> bb_for_body5_lr_ph29.io.Out(2)


  br_34.io.enable <> bb_for_cond_cleanup4_loopexit33.io.Out(0)


  const6.io.enable <> bb_for_cond_cleanup435.io.Out(0)

  binaryOp_indvars_iv_next9736.io.enable <> bb_for_cond_cleanup435.io.Out(1)


  binaryOp_inc3837.io.enable <> bb_for_cond_cleanup435.io.Out(2)


  icmp_exitcond9929.io.enable <> bb_for_cond_cleanup435.io.Out(3)


  br_39.io.enable <> bb_for_cond_cleanup435.io.Out(4)


  phiindvars_iv9141.io.enable <> bb_for_body540.io.Out(0)


  br_42.io.enable <> bb_for_body540.io.Out(1)


  const7.io.enable <> bb_for_body12_lr_ph43.io.Out(0)

  trunc44.io.enable <> bb_for_body12_lr_ph43.io.Out(1)


  binaryOp_sub1845.io.enable <> bb_for_body12_lr_ph43.io.Out(2)


  br_46.io.enable <> bb_for_body12_lr_ph43.io.Out(3)


  br_48.io.enable <> bb_for_cond_cleanup11_loopexit47.io.Out(0)


  const8.io.enable <> bb_for_cond_cleanup1149.io.Out(0)

  const9.io.enable <> bb_for_cond_cleanup1149.io.Out(1)

  phival_0_lcssa50.io.enable <> bb_for_cond_cleanup1149.io.Out(2)


  binaryOp_shr2951.io.enable <> bb_for_cond_cleanup1149.io.Out(3)


  binaryOp_52.io.enable <> bb_for_cond_cleanup1149.io.Out(4)


  Gep_arrayidx3253.io.enable <> bb_for_cond_cleanup1149.io.Out(5)


  st_54.io.enable <> bb_for_cond_cleanup1149.io.Out(6)


  binaryOp_indvars_iv_next9255.io.enable <> bb_for_cond_cleanup1149.io.Out(7)


  icmp_exitcond9543.io.enable <> bb_for_cond_cleanup1149.io.Out(8)


  br_57.io.enable <> bb_for_cond_cleanup1149.io.Out(9)


  const10.io.enable <> bb_for_body1258.io.Out(0)

  const11.io.enable <> bb_for_body1258.io.Out(1)

  const12.io.enable <> bb_for_body1258.io.Out(2)

  phiy_08059.io.enable <> bb_for_body1258.io.Out(3)


  phival_07960.io.enable <> bb_for_body1258.io.Out(4)


  phic_07861.io.enable <> bb_for_body1258.io.Out(5)


  phiindex2_07762.io.enable <> bb_for_body1258.io.Out(6)


  br_63.io.enable <> bb_for_body1258.io.Out(7)


  binaryOp_add65.io.enable <> bb_for_body17_lr_ph64.io.Out(0)


  sext66.io.enable <> bb_for_body17_lr_ph64.io.Out(1)


  br_67.io.enable <> bb_for_body17_lr_ph64.io.Out(2)


  phiadd23_lcssa69.io.enable <> bb_for_cond_cleanup16_loopexit68.io.Out(0)


  binaryOp_70.io.enable <> bb_for_cond_cleanup16_loopexit68.io.Out(1)


  br_71.io.enable <> bb_for_cond_cleanup16_loopexit68.io.Out(2)


  const13.io.enable <> bb_for_cond_cleanup1672.io.Out(0)

  phic_1_lcssa73.io.enable <> bb_for_cond_cleanup1672.io.Out(1)


  phival_1_lcssa74.io.enable <> bb_for_cond_cleanup1672.io.Out(2)


  binaryOp_add2575.io.enable <> bb_for_cond_cleanup1672.io.Out(3)


  binaryOp_inc2776.io.enable <> bb_for_cond_cleanup1672.io.Out(4)


  icmp_exitcond9060.io.enable <> bb_for_cond_cleanup1672.io.Out(5)


  br_78.io.enable <> bb_for_cond_cleanup1672.io.Out(6)


  const14.io.enable <> bb_for_body1779.io.Out(0)

  const15.io.enable <> bb_for_body1779.io.Out(1)

  const16.io.enable <> bb_for_body1779.io.Out(2)

  phiindvars_iv8880.io.enable <> bb_for_body1779.io.Out(3)


  phiindvars_iv81.io.enable <> bb_for_body1779.io.Out(4)


  phival_17382.io.enable <> bb_for_body1779.io.Out(5)


  binaryOp_indvars_iv_next8983.io.enable <> bb_for_body1779.io.Out(6)


  Gep_arrayidx84.io.enable <> bb_for_body1779.io.Out(7)


  ld_85.io.enable <> bb_for_body1779.io.Out(8)


  trunc86.io.enable <> bb_for_body1779.io.Out(9)


  binaryOp_add1987.io.enable <> bb_for_body1779.io.Out(10)


  sextidxprom2088.io.enable <> bb_for_body1779.io.Out(11)


  Gep_arrayidx2189.io.enable <> bb_for_body1779.io.Out(12)


  ld_90.io.enable <> bb_for_body1779.io.Out(13)


  binaryOp_mul2291.io.enable <> bb_for_body1779.io.Out(14)


  binaryOp_add2392.io.enable <> bb_for_body1779.io.Out(15)


  binaryOp_indvars_iv_next93.io.enable <> bb_for_body1779.io.Out(16)


  icmp_exitcond76.io.enable <> bb_for_body1779.io.Out(17)


  br_95.io.enable <> bb_for_body1779.io.Out(18)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  phiindvars_iv9626.io.Mask <> bb_for_body25.io.MaskBB(0)

  phij_08727.io.Mask <> bb_for_body25.io.MaskBB(1)

  phiindvars_iv9141.io.Mask <> bb_for_body540.io.MaskBB(0)

  phival_0_lcssa50.io.Mask <> bb_for_cond_cleanup1149.io.MaskBB(0)

  phiy_08059.io.Mask <> bb_for_body1258.io.MaskBB(0)

  phival_07960.io.Mask <> bb_for_body1258.io.MaskBB(1)

  phic_07861.io.Mask <> bb_for_body1258.io.MaskBB(2)

  phiindex2_07762.io.Mask <> bb_for_body1258.io.MaskBB(3)

  phiadd23_lcssa69.io.Mask <> bb_for_cond_cleanup16_loopexit68.io.MaskBB(0)

  phic_1_lcssa73.io.Mask <> bb_for_cond_cleanup1672.io.MaskBB(0)

  phival_1_lcssa74.io.Mask <> bb_for_cond_cleanup1672.io.MaskBB(1)

  phiindvars_iv8880.io.Mask <> bb_for_body1779.io.MaskBB(0)

  phiindvars_iv81.io.Mask <> bb_for_body1779.io.MaskBB(1)

  phival_17382.io.Mask <> bb_for_body1779.io.MaskBB(2)



  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */

  mem_ctrl_cache.io.rd.mem(0).MemReq <> ld_85.io.MemReq
  ld_85.io.MemResp <> mem_ctrl_cache.io.rd.mem(0).MemResp
  mem_ctrl_cache.io.rd.mem(1).MemReq <> ld_90.io.MemReq
  ld_90.io.MemResp <> mem_ctrl_cache.io.rd.mem(1).MemResp
  mem_ctrl_cache.io.wr.mem(0).MemReq <> st_54.io.MemReq
  st_54.io.MemResp <> mem_ctrl_cache.io.wr.mem(0).MemResp



  /* ================================================================== *
   *                   PRINT SHARED CONNECTIONS                         *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  binaryOp_shr2.io.RightIO <> const0.io.Out

  binaryOp_mul910.io.RightIO <> const1.io.Out

  icmp_cmp10768.io.RightIO <> const2.io.Out

  icmp_cmp15719.io.RightIO <> const3.io.Out

  binaryOp_13.io.RightIO <> const4.io.Out

  binaryOp_14.io.RightIO <> const5.io.Out

  binaryOp_inc3837.io.RightIO <> const6.io.Out

  binaryOp_sub1845.io.RightIO <> const7.io.Out

  phival_0_lcssa50.io.InData(0) <> const8.io.Out

  binaryOp_indvars_iv_next9255.io.RightIO <> const9.io.Out

  phiy_08059.io.InData(0) <> const10.io.Out

  phival_07960.io.InData(0) <> const11.io.Out

  phic_07861.io.InData(0) <> const12.io.Out

  binaryOp_inc2776.io.RightIO <> const13.io.Out

  phiindvars_iv81.io.InData(1) <> const14.io.Out

  binaryOp_indvars_iv_next8983.io.RightIO <> const15.io.Out

  binaryOp_indvars_iv_next93.io.RightIO <> const16.io.Out

  binaryOp_mul3.io.LeftIO <> binaryOp_shr2.io.Out(1)

  binaryOp_sub4.io.RightIO <> binaryOp_shr2.io.Out(2)

  icmp_cmp843.io.LeftIO <> binaryOp_shr2.io.Out(3)

  binaryOp_sub28.io.RightIO <> binaryOp_shr2.io.Out(4)

  icmp_cmp3826.io.LeftIO <> binaryOp_shr2.io.Out(5)

  sext15.io.Input <> binaryOp_shr2.io.Out(6)

  sext16.io.Input <> binaryOp_mul3.io.Out(1)

  icmp_cmp843.io.RightIO <> binaryOp_sub4.io.Out(1)

  br_6.io.CmpIO <> icmp_cmp843.io.Out(0)

  icmp_cmp3826.io.RightIO <> binaryOp_sub28.io.Out(0)

  sextwide_trip_count9418.io.Input <> binaryOp_sub28.io.Out(1)

  icmp_cmp10768.io.LeftIO <> binaryOp_mul910.io.Out(0)

  icmp_cmp15719.io.LeftIO <> binaryOp_mul910.io.Out(1)

  sextwide_trip_count19.io.Input <> binaryOp_14.io.Out(1)

  trunc30.io.Input <> phiindvars_iv9626.io.Out(1)

  binaryOp_indvars_iv_next9736.io.LeftIO <> phiindvars_iv9626.io.Out(2)

  binaryOp_inc3837.io.LeftIO <> phij_08727.io.Out(0)

  binaryOp_31.io.LeftIO <> trunc30.io.Out(0)

  icmp_exitcond9929.io.LeftIO <> binaryOp_inc3837.io.Out(1)

  br_39.io.CmpIO <> icmp_exitcond9929.io.Out(0)

  trunc44.io.Input <> phiindvars_iv9141.io.Out(0)

  binaryOp_52.io.LeftIO <> phiindvars_iv9141.io.Out(1)

  binaryOp_indvars_iv_next9255.io.LeftIO <> phiindvars_iv9141.io.Out(2)

  binaryOp_sub1845.io.LeftIO <> trunc44.io.Out(0)

  binaryOp_shr2951.io.LeftIO <> phival_0_lcssa50.io.Out(0)

  st_54.io.inData <> binaryOp_shr2951.io.Out(0)

  Gep_arrayidx3253.io.idx(0) <> binaryOp_52.io.Out(0)

  st_54.io.GepAddr <> Gep_arrayidx3253.io.Out(0)

  icmp_exitcond9543.io.LeftIO <> binaryOp_indvars_iv_next9255.io.Out(1)

  br_57.io.CmpIO <> icmp_exitcond9543.io.Out(0)

  binaryOp_inc2776.io.LeftIO <> phiy_08059.io.Out(0)

  phival_1_lcssa74.io.InData(0) <> phival_07960.io.Out(1)

  sext66.io.Input <> phic_07861.io.Out(0)

  binaryOp_70.io.RightIO <> phic_07861.io.Out(1)

  phic_1_lcssa73.io.InData(0) <> phic_07861.io.Out(2)

  binaryOp_add65.io.RightIO <> phiindex2_07762.io.Out(0)

  binaryOp_add2575.io.LeftIO <> phiindex2_07762.io.Out(1)

  phival_1_lcssa74.io.InData(1) <> phiadd23_lcssa69.io.Out(0)

  phic_1_lcssa73.io.InData(1) <> binaryOp_70.io.Out(0)

  icmp_exitcond9060.io.LeftIO <> binaryOp_inc2776.io.Out(1)

  br_78.io.CmpIO <> icmp_exitcond9060.io.Out(0)

  binaryOp_indvars_iv_next8983.io.LeftIO <> phiindvars_iv8880.io.Out(0)

  Gep_arrayidx84.io.idx(0) <> phiindvars_iv8880.io.Out(1)

  trunc86.io.Input <> phiindvars_iv81.io.Out(0)

  binaryOp_indvars_iv_next93.io.LeftIO <> phiindvars_iv81.io.Out(1)

  binaryOp_add2392.io.RightIO <> phival_17382.io.Out(0)

  ld_85.io.GepAddr <> Gep_arrayidx84.io.Out(0)

  binaryOp_mul2291.io.RightIO <> ld_85.io.Out(0)

  binaryOp_add1987.io.RightIO <> trunc86.io.Out(0)

  sextidxprom2088.io.Input <> binaryOp_add1987.io.Out(0)

  Gep_arrayidx2189.io.idx(0) <> sextidxprom2088.io.Out(0)

  ld_90.io.GepAddr <> Gep_arrayidx2189.io.Out(0)

  binaryOp_mul2291.io.LeftIO <> ld_90.io.Out(0)

  binaryOp_add2392.io.LeftIO <> binaryOp_mul2291.io.Out(0)

  icmp_exitcond76.io.LeftIO <> binaryOp_indvars_iv_next93.io.Out(1)

  br_95.io.CmpIO <> icmp_exitcond76.io.Out(0)

  binaryOp_mul3.io.RightIO <> ArgSplitter.io.Out.dataVals.elements("field0")(1)

  binaryOp_sub28.io.LeftIO <> ArgSplitter.io.Out.dataVals.elements("field0")(2)

  sext17.io.Input <> ArgSplitter.io.Out.dataVals.elements("field0")(3)

  binaryOp_sub4.io.LeftIO <> ArgSplitter.io.Out.dataVals.elements("field1")(0)

  binaryOp_shr2.io.LeftIO <> ArgSplitter.io.Out.dataVals.elements("field2")(0)

  binaryOp_mul910.io.LeftIO <> ArgSplitter.io.Out.dataVals.elements("field2")(1)

  binaryOp_13.io.LeftIO <> ArgSplitter.io.Out.dataVals.elements("field2")(2)

  binaryOp_14.io.LeftIO <> ArgSplitter.io.Out.dataVals.elements("field2")(3)

  st_54.io.Out(0).ready := true.B



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  br_57.io.PredOp(0) <> st_54.io.SuccOp(0)



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_24.io.Out

}

