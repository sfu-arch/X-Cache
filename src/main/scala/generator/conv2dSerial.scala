package dandelion.generator

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


  /* ================================================================== *
   *                   PRINTING PORTS DEFINITION                        *
   * ================================================================== */

abstract class conv2dSerialDFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32, 32, 32, 32, 32, 32, 32))))
    val MemResp = Flipped(Valid(new MemResp))
    val MemReq = Decoupled(new MemReq)
    val out = Decoupled(new Call(List(32)))
  })
}

class conv2dSerialDF(implicit p: Parameters) extends conv2dSerialDFIO()(p) {


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  val MemCtrl = Module(new UnifiedController(ID = 0, Size = 32, NReads = 2, NWrites = 1)
  (WControl = new WriteMemoryController(NumOps = 1, BaseSize = 2, NumEntries = 2))
  (RControl = new ReadMemoryController(NumOps = 2, BaseSize = 2, NumEntries = 2))
  (RWArbiter = new ReadWriteArbiter()))

  io.MemReq <> MemCtrl.io.MemReq
  MemCtrl.io.MemResp <> io.MemResp

  val InputSplitter = Module(new SplitCallNew(List(1, 1, 1, 4, 1, 3, 1)))
  InputSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val Loop_0 = Module(new LoopBlockNode(NumIns = List(1, 1, 1, 1, 1, 1), NumOuts = List(1), NumCarry = List(1, 1, 1), NumExits = 1, ID = 0))

  val Loop_1 = Module(new LoopBlockNode(NumIns = List(1, 1, 1, 3, 1, 1, 1), NumOuts = List(1), NumCarry = List(1, 1, 1, 1), NumExits = 1, ID = 1))

  val Loop_2 = Module(new LoopBlockNode(NumIns = List(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 2))

  val Loop_3 = Module(new LoopBlockNode(NumIns = List(2, 2, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1), NumOuts = List(), NumCarry = List(1, 1), NumExits = 1, ID = 3))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_entry0 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 6, BID = 0))

  val bb_for_body_lr_ph1 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 11, BID = 1))

  val bb_for_cond_cleanup_loopexit2 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 2, BID = 2))

  val bb_for_cond_cleanup3 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 2, NumPhi = 1, BID = 3))

  val bb_for_body4 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 3, NumPhi = 2, BID = 4))

  val bb_for_body5_lr_ph5 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 2, BID = 5))

  val bb_for_cond_cleanup4_loopexit6 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 6))

  val bb_for_cond_cleanup47 = Module(new BasicBlockNoMaskFastNode(NumInputs = 2, NumOuts = 5, BID = 7))

  val bb_for_body58 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 2, NumPhi = 1, BID = 8))

  val bb_for_body12_lr_ph9 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 3, BID = 9))

  val bb_for_cond_cleanup11_loopexit10 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 10))

  val bb_for_cond_cleanup1111 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 10, NumPhi = 1, BID = 11))

  val bb_for_body1212 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 8, NumPhi = 4, BID = 12))

  val bb_for_body17_lr_ph13 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 2, BID = 13))

  val bb_for_cond_cleanup16_loopexit14 = Module(new BasicBlockNode(NumInputs = 1, NumOuts = 3, NumPhi = 1, BID = 14))

  val bb_for_cond_cleanup1615 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 7, NumPhi = 2, BID = 15))

  val bb_for_body1716 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 17, NumPhi = 3, BID = 16))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  %shr = ashr i32 %K, 1, !dbg !70, !UID !71
  val binaryOp_shr0 = Module(new ComputeNode(NumOuts = 6, ID = 0, opCode = "ashr")(sign = false, Debug = false))

  //  %mul = mul nsw i32 %shr, %W, !dbg !73, !UID !74
  val binaryOp_mul1 = Module(new ComputeNode(NumOuts = 2, ID = 1, opCode = "mul")(sign = false, Debug = false))

  //  %sub = sub nsw i32 %H, %shr, !dbg !77, !UID !78
  val binaryOp_sub2 = Module(new ComputeNode(NumOuts = 3, ID = 2, opCode = "sub")(sign = false, Debug = false))

  //  %cmp83 = icmp slt i32 %shr, %sub, !dbg !79, !UID !80
  val icmp_cmp833 = Module(new ComputeNode(NumOuts = 1, ID = 3, opCode = "ult")(sign = false, Debug = false))

  //  br i1 %cmp83, label %for.body.lr.ph, label %for.cond.cleanup, !dbg !81, !UID !82, !BB_UID !83
  val br_4 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 4))

  //  %sub2 = sub nsw i32 %W, %shr, !UID !84
  val binaryOp_sub25 = Module(new ComputeNode(NumOuts = 2, ID = 5, opCode = "sub")(sign = false, Debug = false))

  //  %cmp381 = icmp slt i32 %shr, %sub2, !UID !85
  val icmp_cmp3816 = Module(new ComputeNode(NumOuts = 1, ID = 6, opCode = "ult")(sign = false, Debug = false))

  //  %mul9 = and i32 %K, -2, !UID !86
  val binaryOp_mul97 = Module(new ComputeNode(NumOuts = 2, ID = 7, opCode = "and")(sign = false, Debug = false))

  //  %cmp1075 = icmp slt i32 %mul9, 0, !UID !87
  val icmp_cmp10758 = Module(new ComputeNode(NumOuts = 1, ID = 8, opCode = "ult")(sign = false, Debug = false))

  //  %cmp1570 = icmp slt i32 %mul9, 0, !UID !88
  val icmp_cmp15709 = Module(new ComputeNode(NumOuts = 1, ID = 9, opCode = "ult")(sign = false, Debug = false))

  //  %0 = or i32 %K, 1, !dbg !89, !UID !90
  val binaryOp_10 = Module(new ComputeNode(NumOuts = 1, ID = 10, opCode = "or")(sign = false, Debug = false))

  //  br label %for.body, !dbg !81, !UID !91, !BB_UID !92
  val br_11 = Module(new UBranchNode(ID = 11))

  //  %1 = mul i32 %sub, %W, !dbg !81, !UID !93
  val binaryOp_12 = Module(new ComputeNode(NumOuts = 1, ID = 12, opCode = "mul")(sign = false, Debug = false))

  //  br label %for.cond.cleanup, !dbg !94, !UID !95, !BB_UID !96
  val br_13 = Module(new UBranchNode(ID = 13))

  //  %index.0.lcssa = phi i32 [ %mul, %entry ], [ %1, %for.cond.cleanup.loopexit ], !UID !97
  val phiindex_0_lcssa14 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 1, ID = 14, Res = true))

  //  ret i32 %index.0.lcssa, !dbg !94, !UID !98, !BB_UID !99
  val ret_15 = Module(new RetNode2(retTypes = List(32), ID = 15))

  //  %j.086 = phi i32 [ %shr, %for.body.lr.ph ], [ %inc36, %for.cond.cleanup4 ], !UID !100
  val phij_08616 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 1, ID = 16, Res = true))

  //  %index.084 = phi i32 [ %mul, %for.body.lr.ph ], [ %add34, %for.cond.cleanup4 ], !UID !101
  val phiindex_08417 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 3, ID = 17, Res = true))

  //  br i1 %cmp381, label %for.body5.lr.ph, label %for.cond.cleanup4, !dbg !103, !UID !104, !BB_UID !105
  val br_18 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 18))

  //  %sub7 = sub nsw i32 %index.084, %mul, !UID !106
  val binaryOp_sub719 = Module(new ComputeNode(NumOuts = 1, ID = 19, opCode = "sub")(sign = false, Debug = false))

  //  br label %for.body5, !dbg !103, !UID !107, !BB_UID !108
  val br_20 = Module(new UBranchNode(ID = 20))

  //  br label %for.cond.cleanup4, !dbg !109
  val br_21 = Module(new UBranchNode(ID = 21))

  //  %add34 = add nsw i32 %index.084, %W, !dbg !109, !UID !110
  val binaryOp_add3422 = Module(new ComputeNode(NumOuts = 1, ID = 22, opCode = "add")(sign = false, Debug = false))

  //  %inc36 = add nsw i32 %j.086, 1, !dbg !111, !UID !112
  val binaryOp_inc3623 = Module(new ComputeNode(NumOuts = 2, ID = 23, opCode = "add")(sign = false, Debug = false))

  //  %exitcond90 = icmp eq i32 %inc36, %sub, !dbg !79, !UID !113
  val icmp_exitcond9024 = Module(new ComputeNode(NumOuts = 1, ID = 24, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond90, label %for.cond.cleanup.loopexit, label %for.body, !dbg !81, !llvm.loop !114, !UID !116, !BB_UID !117
  val br_25 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 25))

  //  %i.082 = phi i32 [ %shr, %for.body5.lr.ph ], [ %inc32, %for.cond.cleanup11 ], !UID !118
  val phii_08226 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 3, ID = 26, Res = true))

  //  br i1 %cmp1075, label %for.cond.cleanup11, label %for.body12.lr.ph, !dbg !123, !UID !124, !BB_UID !125
  val br_27 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 27))

  //  %sub18 = add i32 %i.082, -2, !UID !126
  val binaryOp_sub1828 = Module(new ComputeNode(NumOuts = 1, ID = 28, opCode = "add")(sign = false, Debug = false))

  //  br label %for.body12, !dbg !123, !UID !127, !BB_UID !128
  val br_29 = Module(new UBranchNode(ID = 29))

  //  br label %for.cond.cleanup11, !dbg !129
  val br_30 = Module(new UBranchNode(ID = 30))

  //  %val.0.lcssa = phi i32 [ 0, %for.body5 ], [ %val.1.lcssa, %for.cond.cleanup11.loopexit ], !UID !130
  val phival_0_lcssa31 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 1, ID = 31, Res = true))

  //  %shr28 = ashr i32 %val.0.lcssa, %scf, !dbg !129, !UID !131
  val binaryOp_shr2832 = Module(new ComputeNode(NumOuts = 1, ID = 32, opCode = "ashr")(sign = false, Debug = false))

  //  %add29 = add nsw i32 %i.082, %index.084, !dbg !132, !UID !133
  val binaryOp_add2933 = Module(new ComputeNode(NumOuts = 1, ID = 33, opCode = "add")(sign = false, Debug = false))

  //  %arrayidx30 = getelementptr inbounds i32, i32* %res, i32 %add29, !dbg !134, !UID !135
  val Gep_arrayidx3034 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 34)(ElementSize = 4, ArraySize = List()))

  //  store i32 %shr28, i32* %arrayidx30, align 4, !dbg !136, !tbaa !137, !UID !141
  val st_35 = Module(new UnTypStore(NumPredOps = 0, NumSuccOps = 0, ID = 35, RouteID = 0))

  //  %inc32 = add nsw i32 %i.082, 1, !dbg !142, !UID !143
  val binaryOp_inc3236 = Module(new ComputeNode(NumOuts = 2, ID = 36, opCode = "add")(sign = false, Debug = false))

  //  %exitcond89 = icmp eq i32 %inc32, %sub2, !dbg !144, !UID !145
  val icmp_exitcond8937 = Module(new ComputeNode(NumOuts = 1, ID = 37, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond89, label %for.cond.cleanup4.loopexit, label %for.body5, !dbg !103, !llvm.loop !146, !UID !148, !BB_UID !149
  val br_38 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 38))

  //  %y.079 = phi i32 [ 0, %for.body12.lr.ph ], [ %inc26, %for.cond.cleanup16 ], !UID !150
  val phiy_07939 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 1, ID = 39, Res = true))

  //  %val.078 = phi i32 [ 0, %for.body12.lr.ph ], [ %val.1.lcssa, %for.cond.cleanup16 ], !UID !151
  val phival_07840 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 40, Res = true))

  //  %c.077 = phi i32 [ 0, %for.body12.lr.ph ], [ %c.1.lcssa, %for.cond.cleanup16 ], !UID !152
  val phic_07741 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 3, ID = 41, Res = true))

  //  %index2.076 = phi i32 [ %sub7, %for.body12.lr.ph ], [ %add24, %for.cond.cleanup16 ], !UID !153
  val phiindex2_07642 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 42, Res = true))

  //  br i1 %cmp1570, label %for.cond.cleanup16, label %for.body17.lr.ph, !dbg !89, !UID !155, !BB_UID !156
  val br_43 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 43))

  //  %add = add i32 %sub18, %index2.076, !UID !157
  val binaryOp_add44 = Module(new ComputeNode(NumOuts = 1, ID = 44, opCode = "add")(sign = false, Debug = false))

  //  br label %for.body17, !dbg !89, !UID !158, !BB_UID !159
  val br_45 = Module(new UBranchNode(ID = 45))

  //  %add22.lcssa = phi i32 [ %add22, %for.body17 ], !UID !160
  val phiadd22_lcssa46 = Module(new PhiFastNode(NumInputs = 1, NumOutputs = 1, ID = 46, Res = false))

  //  %2 = add i32 %0, %c.077, !dbg !89, !UID !161
  val binaryOp_47 = Module(new ComputeNode(NumOuts = 1, ID = 47, opCode = "add")(sign = false, Debug = false))

  //  br label %for.cond.cleanup16, !dbg !162, !UID !163, !BB_UID !164
  val br_48 = Module(new UBranchNode(ID = 48))

  //  %c.1.lcssa = phi i32 [ %c.077, %for.body12 ], [ %2, %for.cond.cleanup16.loopexit ], !UID !165
  val phic_1_lcssa49 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 1, ID = 49, Res = true))

  //  %val.1.lcssa = phi i32 [ %val.078, %for.body12 ], [ %add22.lcssa, %for.cond.cleanup16.loopexit ], !UID !166
  val phival_1_lcssa50 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 50, Res = true))

  //  %add24 = add nsw i32 %index2.076, %W, !dbg !162, !UID !167
  val binaryOp_add2451 = Module(new ComputeNode(NumOuts = 1, ID = 51, opCode = "add")(sign = false, Debug = false))

  //  %inc26 = add nuw nsw i32 %y.079, 1, !dbg !168, !UID !169
  val binaryOp_inc2652 = Module(new ComputeNode(NumOuts = 2, ID = 52, opCode = "add")(sign = false, Debug = false))

  //  %exitcond88 = icmp eq i32 %inc26, %0, !dbg !170, !UID !171
  val icmp_exitcond8853 = Module(new ComputeNode(NumOuts = 1, ID = 53, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond88, label %for.cond.cleanup11.loopexit, label %for.body12, !dbg !123, !llvm.loop !172, !UID !174, !BB_UID !175
  val br_54 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 54))

  //  %x.073 = phi i32 [ 0, %for.body17.lr.ph ], [ %inc23, %for.body17 ], !UID !176
  val phix_07355 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 55, Res = true))

  //  %val.172 = phi i32 [ %val.078, %for.body17.lr.ph ], [ %add22, %for.body17 ], !UID !177
  val phival_17256 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 1, ID = 56, Res = true))

  //  %c.171 = phi i32 [ %c.077, %for.body17.lr.ph ], [ %inc, %for.body17 ], !UID !178
  val phic_17157 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 57, Res = true))

  //  %inc = add nsw i32 %c.171, 1, !dbg !179, !UID !182
  val binaryOp_inc58 = Module(new ComputeNode(NumOuts = 1, ID = 58, opCode = "add")(sign = false, Debug = false))

  //  %arrayidx = getelementptr inbounds i32, i32* %coeffs, i32 %c.171, !dbg !183, !UID !184
  val Gep_arrayidx59 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 59)(ElementSize = 4, ArraySize = List()))

  //  %3 = load i32, i32* %arrayidx, align 4, !dbg !183, !tbaa !137, !UID !185
  val ld_60 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 60, RouteID = 0))

  //  %add19 = add i32 %add, %x.073, !dbg !186, !UID !187
  val binaryOp_add1961 = Module(new ComputeNode(NumOuts = 1, ID = 61, opCode = "add")(sign = false, Debug = false))

  //  %arrayidx20 = getelementptr inbounds i32, i32* %mat, i32 %add19, !dbg !188, !UID !189
  val Gep_arrayidx2062 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 62)(ElementSize = 4, ArraySize = List()))

  //  %4 = load i32, i32* %arrayidx20, align 4, !dbg !188, !tbaa !137, !UID !190
  val ld_63 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 63, RouteID = 1))

  //  %mul21 = mul nsw i32 %4, %3, !dbg !191, !UID !192
  val binaryOp_mul2164 = Module(new ComputeNode(NumOuts = 1, ID = 64, opCode = "mul")(sign = false, Debug = false))

  //  %add22 = add nsw i32 %mul21, %val.172, !dbg !193, !UID !194
  val binaryOp_add2265 = Module(new ComputeNode(NumOuts = 2, ID = 65, opCode = "add")(sign = false, Debug = false))

  //  %inc23 = add nuw nsw i32 %x.073, 1, !dbg !195, !UID !196
  val binaryOp_inc2366 = Module(new ComputeNode(NumOuts = 2, ID = 66, opCode = "add")(sign = false, Debug = false))

  //  %exitcond = icmp eq i32 %inc23, %0, !dbg !197, !UID !198
  val icmp_exitcond67 = Module(new ComputeNode(NumOuts = 1, ID = 67, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond, label %for.cond.cleanup16.loopexit, label %for.body17, !dbg !89, !llvm.loop !199, !UID !201, !BB_UID !202
  val br_68 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 68))



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

  //i32 -2
  val const6 = Module(new ConstFastNode(value = -2, ID = 6))

  //i32 0
  val const7 = Module(new ConstFastNode(value = 0, ID = 7))

  //i32 1
  val const8 = Module(new ConstFastNode(value = 1, ID = 8))

  //i32 0
  val const9 = Module(new ConstFastNode(value = 0, ID = 9))

  //i32 0
  val const10 = Module(new ConstFastNode(value = 0, ID = 10))

  //i32 0
  val const11 = Module(new ConstFastNode(value = 0, ID = 11))

  //i32 1
  val const12 = Module(new ConstFastNode(value = 1, ID = 12))

  //i32 0
  val const13 = Module(new ConstFastNode(value = 0, ID = 13))

  //i32 1
  val const14 = Module(new ConstFastNode(value = 1, ID = 14))

  //i32 1
  val const15 = Module(new ConstFastNode(value = 1, ID = 15))



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_entry0.io.predicateIn(0) <> InputSplitter.io.Out.enable

  bb_for_body_lr_ph1.io.predicateIn(0) <> br_4.io.TrueOutput(0)

  bb_for_cond_cleanup3.io.predicateIn(1) <> br_4.io.FalseOutput(0)

  bb_for_cond_cleanup3.io.predicateIn(0) <> br_13.io.Out(0)

  bb_for_body5_lr_ph5.io.predicateIn(0) <> br_18.io.TrueOutput(0)

  bb_for_cond_cleanup47.io.predicateIn(1) <> br_18.io.FalseOutput(0)

  bb_for_cond_cleanup47.io.predicateIn(0) <> br_21.io.Out(0)

  bb_for_body12_lr_ph9.io.predicateIn(0) <> br_27.io.FalseOutput(0)

  bb_for_cond_cleanup1111.io.predicateIn(1) <> br_27.io.TrueOutput(0)

  bb_for_cond_cleanup1111.io.predicateIn(0) <> br_30.io.Out(0)

  bb_for_body17_lr_ph13.io.predicateIn(0) <> br_43.io.FalseOutput(0)

  bb_for_cond_cleanup1615.io.predicateIn(1) <> br_43.io.TrueOutput(0)

  bb_for_cond_cleanup1615.io.predicateIn(0) <> br_48.io.Out(0)



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

  Loop_0.io.enable <> br_45.io.Out(0)

  Loop_0.io.loopBack(0) <> br_68.io.FalseOutput(0)

  Loop_0.io.loopFinish(0) <> br_68.io.TrueOutput(0)

  Loop_1.io.enable <> br_29.io.Out(0)

  Loop_1.io.loopBack(0) <> br_54.io.FalseOutput(0)

  Loop_1.io.loopFinish(0) <> br_54.io.TrueOutput(0)

  Loop_2.io.enable <> br_20.io.Out(0)

  Loop_2.io.loopBack(0) <> br_38.io.FalseOutput(0)

  Loop_2.io.loopFinish(0) <> br_38.io.TrueOutput(0)

  Loop_3.io.enable <> br_11.io.Out(0)

  Loop_3.io.loopBack(0) <> br_25.io.FalseOutput(0)

  Loop_3.io.loopFinish(0) <> br_25.io.TrueOutput(0)



  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

  Loop_0.io.InLiveIn(0) <> phival_07840.io.Out(0)

  Loop_0.io.InLiveIn(1) <> phic_07741.io.Out(0)

  Loop_0.io.InLiveIn(2) <> binaryOp_add44.io.Out(0)

  Loop_0.io.InLiveIn(3) <> Loop_1.io.OutLiveIn.elements("field1")(0)

  Loop_0.io.InLiveIn(4) <> Loop_1.io.OutLiveIn.elements("field2")(0)

  Loop_0.io.InLiveIn(5) <> Loop_1.io.OutLiveIn.elements("field3")(0)

  Loop_1.io.InLiveIn(0) <> binaryOp_sub1828.io.Out(0)

  Loop_1.io.InLiveIn(1) <> Loop_2.io.OutLiveIn.elements("field3")(0)

  Loop_1.io.InLiveIn(2) <> Loop_2.io.OutLiveIn.elements("field5")(0)

  Loop_1.io.InLiveIn(3) <> Loop_2.io.OutLiveIn.elements("field6")(0)

  Loop_1.io.InLiveIn(4) <> Loop_2.io.OutLiveIn.elements("field2")(0)

  Loop_1.io.InLiveIn(5) <> Loop_2.io.OutLiveIn.elements("field0")(0)

  Loop_1.io.InLiveIn(6) <> Loop_2.io.OutLiveIn.elements("field4")(0)

  Loop_2.io.InLiveIn(0) <> binaryOp_sub719.io.Out(0)

  Loop_2.io.InLiveIn(1) <> phiindex_08417.io.Out(0)

  Loop_2.io.InLiveIn(2) <> Loop_3.io.OutLiveIn.elements("field4")(0)

  Loop_2.io.InLiveIn(3) <> Loop_3.io.OutLiveIn.elements("field6")(0)

  Loop_2.io.InLiveIn(4) <> Loop_3.io.OutLiveIn.elements("field8")(0)

  Loop_2.io.InLiveIn(5) <> Loop_3.io.OutLiveIn.elements("field5")(0)

  Loop_2.io.InLiveIn(6) <> Loop_3.io.OutLiveIn.elements("field7")(0)

  Loop_2.io.InLiveIn(7) <> Loop_3.io.OutLiveIn.elements("field3")(0)

  Loop_2.io.InLiveIn(8) <> Loop_3.io.OutLiveIn.elements("field9")(0)

  Loop_2.io.InLiveIn(9) <> Loop_3.io.OutLiveIn.elements("field10")(0)

  Loop_2.io.InLiveIn(10) <> Loop_3.io.OutLiveIn.elements("field11")(0)

  Loop_2.io.InLiveIn(11) <> Loop_3.io.OutLiveIn.elements("field0")(0)

  Loop_3.io.InLiveIn(0) <> binaryOp_shr0.io.Out(0)

  Loop_3.io.InLiveIn(1) <> binaryOp_mul1.io.Out(0)

  Loop_3.io.InLiveIn(2) <> icmp_cmp3816.io.Out(0)

  Loop_3.io.InLiveIn(3) <> icmp_cmp10758.io.Out(0)

  Loop_3.io.InLiveIn(4) <> icmp_cmp15709.io.Out(0)

  Loop_3.io.InLiveIn(5) <> InputSplitter.io.Out.data.elements("field2")(0)

  Loop_3.io.InLiveIn(6) <> InputSplitter.io.Out.data.elements("field0")(0)

  Loop_3.io.InLiveIn(7) <> binaryOp_10.io.Out(0)

  Loop_3.io.InLiveIn(8) <> InputSplitter.io.Out.data.elements("field3")(0)

  Loop_3.io.InLiveIn(9) <> InputSplitter.io.Out.data.elements("field6")(0)

  Loop_3.io.InLiveIn(10) <> InputSplitter.io.Out.data.elements("field1")(0)

  Loop_3.io.InLiveIn(11) <> binaryOp_sub25.io.Out(0)

  Loop_3.io.InLiveIn(12) <> binaryOp_sub2.io.Out(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */

  phival_17256.io.InData(0) <> Loop_0.io.OutLiveIn.elements("field0")(0)

  phic_17157.io.InData(0) <> Loop_0.io.OutLiveIn.elements("field1")(0)

  binaryOp_add1961.io.LeftIO <> Loop_0.io.OutLiveIn.elements("field2")(0)

  Gep_arrayidx2062.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field3")(0)

  Gep_arrayidx59.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field4")(0)

  icmp_exitcond67.io.RightIO <> Loop_0.io.OutLiveIn.elements("field5")(0)

  binaryOp_add44.io.LeftIO <> Loop_1.io.OutLiveIn.elements("field0")(0)

  binaryOp_47.io.LeftIO <> Loop_1.io.OutLiveIn.elements("field3")(1)

  icmp_exitcond8853.io.RightIO <> Loop_1.io.OutLiveIn.elements("field3")(2)

  br_43.io.CmpIO <> Loop_1.io.OutLiveIn.elements("field4")(0)

  phiindex2_07642.io.InData(0) <> Loop_1.io.OutLiveIn.elements("field5")(0)

  binaryOp_add2451.io.RightIO <> Loop_1.io.OutLiveIn.elements("field6")(0)

  binaryOp_add2933.io.RightIO <> Loop_2.io.OutLiveIn.elements("field1")(0)

  br_27.io.CmpIO <> Loop_2.io.OutLiveIn.elements("field7")(0)

  binaryOp_shr2832.io.RightIO <> Loop_2.io.OutLiveIn.elements("field8")(0)

  Gep_arrayidx3034.io.baseAddress <> Loop_2.io.OutLiveIn.elements("field9")(0)

  icmp_exitcond8937.io.RightIO <> Loop_2.io.OutLiveIn.elements("field10")(0)

  phii_08226.io.InData(0) <> Loop_2.io.OutLiveIn.elements("field11")(0)

  phij_08616.io.InData(0) <> Loop_3.io.OutLiveIn.elements("field0")(1)

  phiindex_08417.io.InData(0) <> Loop_3.io.OutLiveIn.elements("field1")(0)

  binaryOp_sub719.io.RightIO <> Loop_3.io.OutLiveIn.elements("field1")(1)

  br_18.io.CmpIO <> Loop_3.io.OutLiveIn.elements("field2")(0)

  binaryOp_add3422.io.RightIO <> Loop_3.io.OutLiveIn.elements("field8")(1)

  icmp_exitcond9024.io.RightIO <> Loop_3.io.OutLiveIn.elements("field12")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */

  Loop_0.io.InLiveOut(0) <> binaryOp_add2265.io.Out(0)

  Loop_1.io.InLiveOut(0) <> phival_1_lcssa50.io.Out(0)



  /* ================================================================== *
   *                   LOOP LIVE OUT DEPENDENCIES                       *
   * ================================================================== */

  phiadd22_lcssa46.io.InData(0) <> Loop_0.io.OutLiveOut.elements("field0")(0)

  phival_0_lcssa31.io.InData(1) <> Loop_1.io.OutLiveOut.elements("field0")(0)



  /* ================================================================== *
   *                   LOOP CARRY DEPENDENCIES                          *
   * ================================================================== */

  Loop_0.io.CarryDepenIn(0) <> binaryOp_inc58.io.Out(0)

  Loop_0.io.CarryDepenIn(1) <> binaryOp_inc2366.io.Out(0)

  Loop_0.io.CarryDepenIn(2) <> binaryOp_add2265.io.Out(1)

  Loop_1.io.CarryDepenIn(0) <> binaryOp_add2451.io.Out(0)

  Loop_1.io.CarryDepenIn(1) <> phival_1_lcssa50.io.Out(1)

  Loop_1.io.CarryDepenIn(2) <> binaryOp_inc2652.io.Out(0)

  Loop_1.io.CarryDepenIn(3) <> phic_1_lcssa49.io.Out(0)

  Loop_2.io.CarryDepenIn(0) <> binaryOp_inc3236.io.Out(0)

  Loop_3.io.CarryDepenIn(0) <> binaryOp_add3422.io.Out(0)

  Loop_3.io.CarryDepenIn(1) <> binaryOp_inc3623.io.Out(0)



  /* ================================================================== *
   *                   LOOP DATA CARRY DEPENDENCIES                     *
   * ================================================================== */

  phic_17157.io.InData(1) <> Loop_0.io.CarryDepenOut.elements("field0")(0)

  phix_07355.io.InData(1) <> Loop_0.io.CarryDepenOut.elements("field1")(0)

  phival_17256.io.InData(1) <> Loop_0.io.CarryDepenOut.elements("field2")(0)

  phiindex2_07642.io.InData(1) <> Loop_1.io.CarryDepenOut.elements("field0")(0)

  phival_07840.io.InData(1) <> Loop_1.io.CarryDepenOut.elements("field1")(0)

  phiy_07939.io.InData(1) <> Loop_1.io.CarryDepenOut.elements("field2")(0)

  phic_07741.io.InData(1) <> Loop_1.io.CarryDepenOut.elements("field3")(0)

  phii_08226.io.InData(1) <> Loop_2.io.CarryDepenOut.elements("field0")(0)

  phiindex_08417.io.InData(1) <> Loop_3.io.CarryDepenOut.elements("field0")(0)

  phij_08616.io.InData(1) <> Loop_3.io.CarryDepenOut.elements("field1")(0)



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  const0.io.enable <> bb_entry0.io.Out(0)

  binaryOp_shr0.io.enable <> bb_entry0.io.Out(1)


  binaryOp_mul1.io.enable <> bb_entry0.io.Out(2)


  binaryOp_sub2.io.enable <> bb_entry0.io.Out(3)


  icmp_cmp833.io.enable <> bb_entry0.io.Out(4)


  br_4.io.enable <> bb_entry0.io.Out(5)


  const1.io.enable <> bb_for_body_lr_ph1.io.Out(0)

  const2.io.enable <> bb_for_body_lr_ph1.io.Out(1)

  const3.io.enable <> bb_for_body_lr_ph1.io.Out(2)

  const4.io.enable <> bb_for_body_lr_ph1.io.Out(3)

  binaryOp_sub25.io.enable <> bb_for_body_lr_ph1.io.Out(4)


  icmp_cmp3816.io.enable <> bb_for_body_lr_ph1.io.Out(5)


  binaryOp_mul97.io.enable <> bb_for_body_lr_ph1.io.Out(6)


  icmp_cmp10758.io.enable <> bb_for_body_lr_ph1.io.Out(7)


  icmp_cmp15709.io.enable <> bb_for_body_lr_ph1.io.Out(8)


  binaryOp_10.io.enable <> bb_for_body_lr_ph1.io.Out(9)


  br_11.io.enable <> bb_for_body_lr_ph1.io.Out(10)


  binaryOp_12.io.enable <> bb_for_cond_cleanup_loopexit2.io.Out(0)


  br_13.io.enable <> bb_for_cond_cleanup_loopexit2.io.Out(1)


  phiindex_0_lcssa14.io.enable <> bb_for_cond_cleanup3.io.Out(0)


  ret_15.io.In.enable <> bb_for_cond_cleanup3.io.Out(1)


  phij_08616.io.enable <> bb_for_body4.io.Out(0)


  phiindex_08417.io.enable <> bb_for_body4.io.Out(1)


  br_18.io.enable <> bb_for_body4.io.Out(2)


  binaryOp_sub719.io.enable <> bb_for_body5_lr_ph5.io.Out(0)


  br_20.io.enable <> bb_for_body5_lr_ph5.io.Out(1)


  br_21.io.enable <> bb_for_cond_cleanup4_loopexit6.io.Out(0)


  const5.io.enable <> bb_for_cond_cleanup47.io.Out(0)

  binaryOp_add3422.io.enable <> bb_for_cond_cleanup47.io.Out(1)


  binaryOp_inc3623.io.enable <> bb_for_cond_cleanup47.io.Out(2)


  icmp_exitcond9024.io.enable <> bb_for_cond_cleanup47.io.Out(3)


  br_25.io.enable <> bb_for_cond_cleanup47.io.Out(4)


  phii_08226.io.enable <> bb_for_body58.io.Out(0)


  br_27.io.enable <> bb_for_body58.io.Out(1)


  const6.io.enable <> bb_for_body12_lr_ph9.io.Out(0)

  binaryOp_sub1828.io.enable <> bb_for_body12_lr_ph9.io.Out(1)


  br_29.io.enable <> bb_for_body12_lr_ph9.io.Out(2)


  br_30.io.enable <> bb_for_cond_cleanup11_loopexit10.io.Out(0)


  const7.io.enable <> bb_for_cond_cleanup1111.io.Out(0)

  const8.io.enable <> bb_for_cond_cleanup1111.io.Out(1)

  phival_0_lcssa31.io.enable <> bb_for_cond_cleanup1111.io.Out(2)


  binaryOp_shr2832.io.enable <> bb_for_cond_cleanup1111.io.Out(3)


  binaryOp_add2933.io.enable <> bb_for_cond_cleanup1111.io.Out(4)


  Gep_arrayidx3034.io.enable <> bb_for_cond_cleanup1111.io.Out(5)


  st_35.io.enable <> bb_for_cond_cleanup1111.io.Out(6)


  binaryOp_inc3236.io.enable <> bb_for_cond_cleanup1111.io.Out(7)


  icmp_exitcond8937.io.enable <> bb_for_cond_cleanup1111.io.Out(8)


  br_38.io.enable <> bb_for_cond_cleanup1111.io.Out(9)


  const9.io.enable <> bb_for_body1212.io.Out(0)

  const10.io.enable <> bb_for_body1212.io.Out(1)

  const11.io.enable <> bb_for_body1212.io.Out(2)

  phiy_07939.io.enable <> bb_for_body1212.io.Out(3)


  phival_07840.io.enable <> bb_for_body1212.io.Out(4)


  phic_07741.io.enable <> bb_for_body1212.io.Out(5)


  phiindex2_07642.io.enable <> bb_for_body1212.io.Out(6)


  br_43.io.enable <> bb_for_body1212.io.Out(7)


  binaryOp_add44.io.enable <> bb_for_body17_lr_ph13.io.Out(0)


  br_45.io.enable <> bb_for_body17_lr_ph13.io.Out(1)


  phiadd22_lcssa46.io.enable <> bb_for_cond_cleanup16_loopexit14.io.Out(0)


  binaryOp_47.io.enable <> bb_for_cond_cleanup16_loopexit14.io.Out(1)


  br_48.io.enable <> bb_for_cond_cleanup16_loopexit14.io.Out(2)


  const12.io.enable <> bb_for_cond_cleanup1615.io.Out(0)

  phic_1_lcssa49.io.enable <> bb_for_cond_cleanup1615.io.Out(1)


  phival_1_lcssa50.io.enable <> bb_for_cond_cleanup1615.io.Out(2)


  binaryOp_add2451.io.enable <> bb_for_cond_cleanup1615.io.Out(3)


  binaryOp_inc2652.io.enable <> bb_for_cond_cleanup1615.io.Out(4)


  icmp_exitcond8853.io.enable <> bb_for_cond_cleanup1615.io.Out(5)


  br_54.io.enable <> bb_for_cond_cleanup1615.io.Out(6)


  const13.io.enable <> bb_for_body1716.io.Out(0)

  const14.io.enable <> bb_for_body1716.io.Out(1)

  const15.io.enable <> bb_for_body1716.io.Out(2)

  phix_07355.io.enable <> bb_for_body1716.io.Out(3)


  phival_17256.io.enable <> bb_for_body1716.io.Out(4)


  phic_17157.io.enable <> bb_for_body1716.io.Out(5)


  binaryOp_inc58.io.enable <> bb_for_body1716.io.Out(6)


  Gep_arrayidx59.io.enable <> bb_for_body1716.io.Out(7)


  ld_60.io.enable <> bb_for_body1716.io.Out(8)


  binaryOp_add1961.io.enable <> bb_for_body1716.io.Out(9)


  Gep_arrayidx2062.io.enable <> bb_for_body1716.io.Out(10)


  ld_63.io.enable <> bb_for_body1716.io.Out(11)


  binaryOp_mul2164.io.enable <> bb_for_body1716.io.Out(12)


  binaryOp_add2265.io.enable <> bb_for_body1716.io.Out(13)


  binaryOp_inc2366.io.enable <> bb_for_body1716.io.Out(14)


  icmp_exitcond67.io.enable <> bb_for_body1716.io.Out(15)


  br_68.io.enable <> bb_for_body1716.io.Out(16)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  phiindex_0_lcssa14.io.Mask <> bb_for_cond_cleanup3.io.MaskBB(0)

  phij_08616.io.Mask <> bb_for_body4.io.MaskBB(0)

  phiindex_08417.io.Mask <> bb_for_body4.io.MaskBB(1)

  phii_08226.io.Mask <> bb_for_body58.io.MaskBB(0)

  phival_0_lcssa31.io.Mask <> bb_for_cond_cleanup1111.io.MaskBB(0)

  phiy_07939.io.Mask <> bb_for_body1212.io.MaskBB(0)

  phival_07840.io.Mask <> bb_for_body1212.io.MaskBB(1)

  phic_07741.io.Mask <> bb_for_body1212.io.MaskBB(2)

  phiindex2_07642.io.Mask <> bb_for_body1212.io.MaskBB(3)

  phiadd22_lcssa46.io.Mask <> bb_for_cond_cleanup16_loopexit14.io.MaskBB(0)

  phic_1_lcssa49.io.Mask <> bb_for_cond_cleanup1615.io.MaskBB(0)

  phival_1_lcssa50.io.Mask <> bb_for_cond_cleanup1615.io.MaskBB(1)

  phix_07355.io.Mask <> bb_for_body1716.io.MaskBB(0)

  phival_17256.io.Mask <> bb_for_body1716.io.MaskBB(1)

  phic_17157.io.Mask <> bb_for_body1716.io.MaskBB(2)



  /* ================================================================== *
   *                   PRINT ALLOCA OFFSET                              *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */

  MemCtrl.io.WriteIn(0) <> st_35.io.memReq

  st_35.io.memResp <> MemCtrl.io.WriteOut(0)

  MemCtrl.io.ReadIn(0) <> ld_60.io.memReq

  ld_60.io.memResp <> MemCtrl.io.ReadOut(0)

  MemCtrl.io.ReadIn(1) <> ld_63.io.memReq

  ld_63.io.memResp <> MemCtrl.io.ReadOut(1)



  /* ================================================================== *
   *                   PRINT SHARED CONNECTIONS                         *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  binaryOp_shr0.io.RightIO <> const0.io.Out

  binaryOp_mul97.io.RightIO <> const1.io.Out

  icmp_cmp10758.io.RightIO <> const2.io.Out

  icmp_cmp15709.io.RightIO <> const3.io.Out

  binaryOp_10.io.RightIO <> const4.io.Out

  binaryOp_inc3623.io.RightIO <> const5.io.Out

  binaryOp_sub1828.io.RightIO <> const6.io.Out

  phival_0_lcssa31.io.InData(0) <> const7.io.Out

  binaryOp_inc3236.io.RightIO <> const8.io.Out

  phiy_07939.io.InData(0) <> const9.io.Out

  phival_07840.io.InData(0) <> const10.io.Out

  phic_07741.io.InData(0) <> const11.io.Out

  binaryOp_inc2652.io.RightIO <> const12.io.Out

  phix_07355.io.InData(0) <> const13.io.Out

  binaryOp_inc58.io.RightIO <> const14.io.Out

  binaryOp_inc2366.io.RightIO <> const15.io.Out

  binaryOp_mul1.io.LeftIO <> binaryOp_shr0.io.Out(1)

  binaryOp_sub2.io.RightIO <> binaryOp_shr0.io.Out(2)

  icmp_cmp833.io.LeftIO <> binaryOp_shr0.io.Out(3)

  binaryOp_sub25.io.RightIO <> binaryOp_shr0.io.Out(4)

  icmp_cmp3816.io.LeftIO <> binaryOp_shr0.io.Out(5)

  phiindex_0_lcssa14.io.InData(0) <> binaryOp_mul1.io.Out(1)

  icmp_cmp833.io.RightIO <> binaryOp_sub2.io.Out(1)

  binaryOp_12.io.LeftIO <> binaryOp_sub2.io.Out(2)

  br_4.io.CmpIO <> icmp_cmp833.io.Out(0)

  icmp_cmp3816.io.RightIO <> binaryOp_sub25.io.Out(1)

  icmp_cmp10758.io.LeftIO <> binaryOp_mul97.io.Out(0)

  icmp_cmp15709.io.LeftIO <> binaryOp_mul97.io.Out(1)

  phiindex_0_lcssa14.io.InData(1) <> binaryOp_12.io.Out(0)

  ret_15.io.In.data("field0") <> phiindex_0_lcssa14.io.Out(0)

  binaryOp_inc3623.io.LeftIO <> phij_08616.io.Out(0)

  binaryOp_sub719.io.LeftIO <> phiindex_08417.io.Out(1)

  binaryOp_add3422.io.LeftIO <> phiindex_08417.io.Out(2)

  icmp_exitcond9024.io.LeftIO <> binaryOp_inc3623.io.Out(1)

  br_25.io.CmpIO <> icmp_exitcond9024.io.Out(0)

  binaryOp_sub1828.io.LeftIO <> phii_08226.io.Out(0)

  binaryOp_add2933.io.LeftIO <> phii_08226.io.Out(1)

  binaryOp_inc3236.io.LeftIO <> phii_08226.io.Out(2)

  binaryOp_shr2832.io.LeftIO <> phival_0_lcssa31.io.Out(0)

  st_35.io.inData <> binaryOp_shr2832.io.Out(0)

  Gep_arrayidx3034.io.idx(0) <> binaryOp_add2933.io.Out(0)

  st_35.io.GepAddr <> Gep_arrayidx3034.io.Out(0)

  icmp_exitcond8937.io.LeftIO <> binaryOp_inc3236.io.Out(1)

  br_38.io.CmpIO <> icmp_exitcond8937.io.Out(0)

  binaryOp_inc2652.io.LeftIO <> phiy_07939.io.Out(0)

  phival_1_lcssa50.io.InData(0) <> phival_07840.io.Out(1)

  binaryOp_47.io.RightIO <> phic_07741.io.Out(1)

  phic_1_lcssa49.io.InData(0) <> phic_07741.io.Out(2)

  binaryOp_add44.io.RightIO <> phiindex2_07642.io.Out(0)

  binaryOp_add2451.io.LeftIO <> phiindex2_07642.io.Out(1)

  phival_1_lcssa50.io.InData(1) <> phiadd22_lcssa46.io.Out(0)

  phic_1_lcssa49.io.InData(1) <> binaryOp_47.io.Out(0)

  icmp_exitcond8853.io.LeftIO <> binaryOp_inc2652.io.Out(1)

  br_54.io.CmpIO <> icmp_exitcond8853.io.Out(0)

  binaryOp_add1961.io.RightIO <> phix_07355.io.Out(0)

  binaryOp_inc2366.io.LeftIO <> phix_07355.io.Out(1)

  binaryOp_add2265.io.RightIO <> phival_17256.io.Out(0)

  binaryOp_inc58.io.LeftIO <> phic_17157.io.Out(0)

  Gep_arrayidx59.io.idx(0) <> phic_17157.io.Out(1)

  ld_60.io.GepAddr <> Gep_arrayidx59.io.Out(0)

  binaryOp_mul2164.io.RightIO <> ld_60.io.Out(0)

  Gep_arrayidx2062.io.idx(0) <> binaryOp_add1961.io.Out(0)

  ld_63.io.GepAddr <> Gep_arrayidx2062.io.Out(0)

  binaryOp_mul2164.io.LeftIO <> ld_63.io.Out(0)

  binaryOp_add2265.io.LeftIO <> binaryOp_mul2164.io.Out(0)

  icmp_exitcond67.io.LeftIO <> binaryOp_inc2366.io.Out(1)

  br_68.io.CmpIO <> icmp_exitcond67.io.Out(0)

  binaryOp_mul1.io.RightIO <> InputSplitter.io.Out.data.elements("field3")(1)

  binaryOp_sub25.io.LeftIO <> InputSplitter.io.Out.data.elements("field3")(2)

  binaryOp_12.io.RightIO <> InputSplitter.io.Out.data.elements("field3")(3)

  binaryOp_sub2.io.LeftIO <> InputSplitter.io.Out.data.elements("field4")(0)

  binaryOp_shr0.io.LeftIO <> InputSplitter.io.Out.data.elements("field5")(0)

  binaryOp_mul97.io.LeftIO <> InputSplitter.io.Out.data.elements("field5")(1)

  binaryOp_10.io.LeftIO <> InputSplitter.io.Out.data.elements("field5")(2)

  st_35.io.Out(0).ready := true.B



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_15.io.Out

}

import java.io.{File, FileWriter}

object conv2dSerialTop extends App {
  val dir = new File("RTL/conv2dSerialTop");
  dir.mkdirs
  implicit val p = Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new conv2dSerialDF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}
