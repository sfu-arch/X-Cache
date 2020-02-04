package dandelion.generator

import chisel3._
import chipsalliance.rocketchip.config._
import dandelion.config._
import dandelion.control._
import dandelion.interfaces._
import dandelion.junctions._
import dandelion.loop._
import dandelion.memory._
import dandelion.node._
import util._


  /* ================================================================== *
   *                   PRINTING PORTS DEFINITION                        *
   * ================================================================== */

abstract class convLayerHalideDFIO(implicit val p: Parameters) extends Module with HasAccelParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32))))
    val MemResp = Flipped(Valid(new MemResp))
    val MemReq = Decoupled(new MemReq)
    val out = Decoupled(new Call(List()))
  })
}

class convLayerHalideDF(implicit p: Parameters) extends convLayerHalideDFIO()(p) {


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  val MemCtrl = Module(new UnifiedController(ID = 0, Size = 32, NReads = 1, NWrites = 1)
  (WControl = new WriteMemoryController(NumOps = 1, BaseSize = 2, NumEntries = 2))
  (RControl = new ReadMemoryController(NumOps = 1, BaseSize = 2, NumEntries = 2))
  (RWArbiter = new ReadWriteArbiter()))

  io.MemReq <> MemCtrl.io.MemReq
  MemCtrl.io.MemResp <> io.MemResp

  val InputSplitter = Module(new SplitCallNew(List(1, 1, 1, 1, 2, 1, 1, 3, 2, 2, 2, 1, 2, 2, 1)))
  InputSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val Loop_0 = Module(new LoopBlockNode(NumIns = List(1, 1, 1, 1), NumOuts = List(1), NumCarry = List(1, 1), NumExits = 1, ID = 0))

  val Loop_1 = Module(new LoopBlockNode(NumIns = List(1, 1, 1, 1, 1), NumOuts = List(1), NumCarry = List(1, 1), NumExits = 1, ID = 1))

  val Loop_2 = Module(new LoopBlockNode(NumIns = List(1, 1, 1, 1, 1, 1, 1, 2, 1, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 2))

  val Loop_3 = Module(new LoopBlockNode(NumIns = List(1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 3))

  val Loop_4 = Module(new LoopBlockNode(NumIns = List(2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 4))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_entry0 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 4, BID = 0))

  val bb_for_body_lr_ph1 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 11, BID = 1))

  val bb_for_cond_cleanup_loopexit2 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 2))

  val bb_for_cond_cleanup3 = Module(new BasicBlockNoMaskFastNode(NumInputs = 2, NumOuts = 1, BID = 3))

  val bb_for_body4 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 2, NumPhi = 1, BID = 4))

  val bb_for_body13_lr_ph5 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 3, BID = 5))

  val bb_for_cond_cleanup12_loopexit6 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 6))

  val bb_for_cond_cleanup127 = Module(new BasicBlockNoMaskFastNode(NumInputs = 2, NumOuts = 4, BID = 7))

  val bb_for_body138 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 2, NumPhi = 1, BID = 8))

  val bb_for_body20_lr_ph9 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 3, BID = 9))

  val bb_for_cond_cleanup19_loopexit10 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 10))

  val bb_for_cond_cleanup1911 = Module(new BasicBlockNoMaskFastNode(NumInputs = 2, NumOuts = 4, BID = 11))

  val bb_for_body2012 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 2, NumPhi = 1, BID = 12))

  val bb_for_cond_cleanup2513 = Module(new BasicBlockNode(NumInputs = 1, NumOuts = 13, NumPhi = 1, BID = 13))

  val bb_for_body2614 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 8, NumPhi = 2, BID = 14))

  val bb_for_cond_cleanup3215 = Module(new BasicBlockNode(NumInputs = 1, NumOuts = 6, NumPhi = 1, BID = 15))

  val bb_for_body3316 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 14, NumPhi = 2, BID = 16))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  %add7 = add nsw i32 %_34, %_33, !dbg !11640, !UID !11641
  val binaryOp_add70 = Module(new ComputeNode(NumOuts = 1, ID = 0, opCode = "add")(sign = false, Debug = false))

  //  %cmp144 = icmp sgt i32 %_34, 0, !dbg !11642, !UID !11643
  val icmp_cmp1441 = Module(new ComputeNode(NumOuts = 1, ID = 1, opCode = "ugt")(sign = false, Debug = false))

  //  br i1 %cmp144, label %for.body.lr.ph, label %for.cond.cleanup, !dbg !11644, !UID !11645, !BB_UID !11646
  val br_2 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 2))

  //  %add10 = add nsw i32 %_31, %_30, !UID !11647
  val binaryOp_add103 = Module(new ComputeNode(NumOuts = 1, ID = 3, opCode = "add")(sign = false, Debug = false))

  //  %cmp11141 = icmp sgt i32 %_31, 0, !UID !11648
  val icmp_cmp111414 = Module(new ComputeNode(NumOuts = 1, ID = 4, opCode = "ugt")(sign = false, Debug = false))

  //  %add17 = add nsw i32 %_28, %_27, !UID !11649
  val binaryOp_add175 = Module(new ComputeNode(NumOuts = 1, ID = 5, opCode = "add")(sign = false, Debug = false))

  //  %cmp18139 = icmp sgt i32 %_28, 0, !UID !11650
  val icmp_cmp181396 = Module(new ComputeNode(NumOuts = 1, ID = 6, opCode = "ugt")(sign = false, Debug = false))

  //  %mul4.neg = mul i32 %_18, %_16, !UID !11651
  val binaryOp_mul4_neg7 = Module(new ComputeNode(NumOuts = 1, ID = 7, opCode = "mul")(sign = false, Debug = false))

  //  %mul3.neg = mul i32 %_21, %_19, !UID !11652
  val binaryOp_mul3_neg8 = Module(new ComputeNode(NumOuts = 1, ID = 8, opCode = "mul")(sign = false, Debug = false))

  //  %reass.add133 = add i32 %mul3.neg, %mul4.neg, !UID !11653
  val binaryOp_reass_add1339 = Module(new ComputeNode(NumOuts = 1, ID = 9, opCode = "add")(sign = false, Debug = false))

  //  %add6.neg = sub i32 %_27, %_13, !UID !11654
  val binaryOp_add6_neg10 = Module(new ComputeNode(NumOuts = 1, ID = 10, opCode = "sub")(sign = false, Debug = false))

  //  br label %for.body, !dbg !11644, !UID !11655, !BB_UID !11656
  val br_11 = Module(new UBranchNode(ID = 11))

  //  br label %for.cond.cleanup, !dbg !11657
  val br_12 = Module(new UBranchNode(ID = 12))

  //  ret void, !dbg !11657, !UID !11658, !BB_UID !11659
  val ret_13 = Module(new RetNode2(retTypes = List(), ID = 13))

  //  %_output_s0_k.0145 = phi i32 [ %_33, %for.body.lr.ph ], [ %inc61, %for.cond.cleanup12 ], !UID !11660
  val phi_output_s0_k_014514 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 14, Res = false))

  //  br i1 %cmp11141, label %for.body13.lr.ph, label %for.cond.cleanup12, !dbg !11663, !UID !11664, !BB_UID !11665
  val br_15 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 15))

  //  %reass.add = sub i32 %_output_s0_k.0145, %_33, !UID !11666
  val binaryOp_reass_add16 = Module(new ComputeNode(NumOuts = 1, ID = 16, opCode = "sub")(sign = false, Debug = false))

  //  %reass.mul = mul i32 %reass.add, %_35, !UID !11667
  val binaryOp_reass_mul17 = Module(new ComputeNode(NumOuts = 1, ID = 17, opCode = "mul")(sign = false, Debug = false))

  //  br label %for.body13, !dbg !11663, !UID !11668, !BB_UID !11669
  val br_18 = Module(new UBranchNode(ID = 18))

  //  br label %for.cond.cleanup12, !dbg !11670
  val br_19 = Module(new UBranchNode(ID = 19))

  //  %inc61 = add nsw i32 %_output_s0_k.0145, 1, !dbg !11670, !UID !11671
  val binaryOp_inc6120 = Module(new ComputeNode(NumOuts = 2, ID = 20, opCode = "add")(sign = false, Debug = false))

  //  %cmp = icmp slt i32 %inc61, %add7, !dbg !11642, !UID !11672
  val icmp_cmp21 = Module(new ComputeNode(NumOuts = 1, ID = 21, opCode = "ult")(sign = false, Debug = false))

  //  br i1 %cmp, label %for.body, label %for.cond.cleanup.loopexit, !dbg !11644, !llvm.loop !11673, !UID !11675, !BB_UID !11676
  val br_22 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 22))

  //  %_output_s0_y.0142 = phi i32 [ %_30, %for.body13.lr.ph ], [ %inc58, %for.cond.cleanup19 ], !UID !11677
  val phi_output_s0_y_014223 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 3, ID = 23, Res = false))

  //  br i1 %cmp18139, label %for.body20.lr.ph, label %for.cond.cleanup19, !dbg !11680, !UID !11681, !BB_UID !11682
  val br_24 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 24))

  //  %reass.add131 = sub i32 %_output_s0_y.0142, %_30, !UID !11683
  val binaryOp_reass_add13125 = Module(new ComputeNode(NumOuts = 1, ID = 25, opCode = "sub")(sign = false, Debug = false))

  //  %reass.mul132 = mul i32 %reass.add131, %_32, !UID !11684
  val binaryOp_reass_mul13226 = Module(new ComputeNode(NumOuts = 1, ID = 26, opCode = "mul")(sign = false, Debug = false))

  //  br label %for.body20, !dbg !11680, !UID !11685, !BB_UID !11686
  val br_27 = Module(new UBranchNode(ID = 27))

  //  br label %for.cond.cleanup19, !dbg !11687
  val br_28 = Module(new UBranchNode(ID = 28))

  //  %inc58 = add nsw i32 %_output_s0_y.0142, 1, !dbg !11687, !UID !11688
  val binaryOp_inc5829 = Module(new ComputeNode(NumOuts = 2, ID = 29, opCode = "add")(sign = false, Debug = false))

  //  %cmp11 = icmp slt i32 %inc58, %add10, !dbg !11689, !UID !11690
  val icmp_cmp1130 = Module(new ComputeNode(NumOuts = 1, ID = 30, opCode = "ult")(sign = false, Debug = false))

  //  br i1 %cmp11, label %for.body13, label %for.cond.cleanup12.loopexit, !dbg !11663, !llvm.loop !11691, !UID !11693, !BB_UID !11694
  val br_31 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 31))

  //  %_output_s0_x.0140 = phi i32 [ %_27, %for.body20.lr.ph ], [ %inc55, %for.cond.cleanup25 ], !UID !11695
  val phi_output_s0_x_014032 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 32, Res = false))

  //  br label %for.body26, !dbg !11704, !UID !11705, !BB_UID !11706
  val br_33 = Module(new UBranchNode(ID = 33))

  //  %add37.lcssa.lcssa = phi i16 [ %add37.lcssa, %for.cond.cleanup32 ], !UID !11707
  val phiadd37_lcssa_lcssa34 = Module(new PhiFastNode(NumInputs = 1, NumOutputs = 1, ID = 34, Res = false))

  //  %0 = trunc i16 %add37.lcssa.lcssa to i8, !dbg !11711, !UID !11712
  val trunc35 = Module(new TruncNode(NumOuts = 1))

  //  %conv51 = mul i8 %0, 3, !dbg !11711, !UID !11713
  val binaryOp_conv5136 = Module(new ComputeNode(NumOuts = 1, ID = 36, opCode = "mul")(sign = false, Debug = false))

  //  %sub = sub i32 %_output_s0_x.0140, %_27, !dbg !11715, !UID !11716
  val binaryOp_sub37 = Module(new ComputeNode(NumOuts = 1, ID = 37, opCode = "sub")(sign = false, Debug = false))

  //  %add15 = add i32 %sub, %reass.mul, !dbg !11717, !UID !11718
  val binaryOp_add1538 = Module(new ComputeNode(NumOuts = 1, ID = 38, opCode = "add")(sign = false, Debug = false))

  //  %add52 = add i32 %add15, %reass.mul132, !dbg !11719, !UID !11720
  val binaryOp_add5239 = Module(new ComputeNode(NumOuts = 1, ID = 39, opCode = "add")(sign = false, Debug = false))

  //  %arrayidx53 = getelementptr inbounds i8, i8* %_output, i32 %add52, !dbg !11722, !UID !11723
  val Gep_arrayidx5340 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 40)(ElementSize = 1, ArraySize = List()))

  //  store i8 %conv51, i8* %arrayidx53, align 1, !dbg !11724, !tbaa !11725, !UID !11728
  val st_41 = Module(new UnTypStore(NumPredOps = 0, NumSuccOps = 0, ID = 41, RouteID = 0))

  //  %inc55 = add nsw i32 %_output_s0_x.0140, 1, !dbg !11729, !UID !11730
  val binaryOp_inc5542 = Module(new ComputeNode(NumOuts = 2, ID = 42, opCode = "add")(sign = false, Debug = false))

  //  %cmp18 = icmp slt i32 %inc55, %add17, !dbg !11731, !UID !11732
  val icmp_cmp1843 = Module(new ComputeNode(NumOuts = 1, ID = 43, opCode = "ult")(sign = false, Debug = false))

  //  br i1 %cmp18, label %for.body20, label %for.cond.cleanup19.loopexit, !dbg !11680, !llvm.loop !11733, !UID !11735, !BB_UID !11736
  val br_44 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 44))

  //  %_dw_conv.0138 = phi i16 [ 0, %for.body20 ], [ %add37.lcssa, %for.cond.cleanup32 ], !UID !11737
  val phi_dw_conv_013845 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 1, ID = 45, Res = true))

  //  %_dw_conv_s1_r_dw__y.0137 = phi i32 [ 0, %for.body20 ], [ %inc40, %for.cond.cleanup32 ], !UID !11738
  val phi_dw_conv_s1_r_dw__y_013746 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 46, Res = true))

  //  %add27 = add nsw i32 %_dw_conv_s1_r_dw__y.0137, %_output_s0_y.0142, !dbg !11739, !UID !11740
  val binaryOp_add2747 = Module(new ComputeNode(NumOuts = 1, ID = 47, opCode = "add")(sign = false, Debug = false))

  //  %mul28 = mul nsw i32 %add27, %_18, !dbg !11742, !UID !11743
  val binaryOp_mul2848 = Module(new ComputeNode(NumOuts = 1, ID = 48, opCode = "mul")(sign = false, Debug = false))

  //  %sub21 = add i32 %add6.neg, %mul28, !UID !11747
  val binaryOp_sub2149 = Module(new ComputeNode(NumOuts = 1, ID = 49, opCode = "add")(sign = false, Debug = false))

  //  br label %for.body33, !dbg !11748, !UID !11749, !BB_UID !11750
  val br_50 = Module(new UBranchNode(ID = 50))

  //  %add37.lcssa = phi i16 [ %add37, %for.body33 ], !UID !11751
  val phiadd37_lcssa51 = Module(new PhiFastNode(NumInputs = 1, NumOutputs = 2, ID = 51, Res = false))

  //  %inc40 = add nuw nsw i32 %_dw_conv_s1_r_dw__y.0137, 1, !dbg !11752, !UID !11753
  val binaryOp_inc4052 = Module(new ComputeNode(NumOuts = 2, ID = 52, opCode = "add")(sign = false, Debug = false))

  //  %exitcond146 = icmp eq i32 %inc40, 3, !dbg !11754, !UID !11755
  val icmp_exitcond14653 = Module(new ComputeNode(NumOuts = 1, ID = 53, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond146, label %for.cond.cleanup25, label %for.body26, !dbg !11704, !llvm.loop !11756, !UID !11758, !BB_UID !11759
  val br_54 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 54))

  //  %_dw_conv.1136 = phi i16 [ %_dw_conv.0138, %for.body26 ], [ %add37, %for.body33 ], !UID !11760
  val phi_dw_conv_113655 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 1, ID = 55, Res = true))

  //  %_dw_conv_s1_r_dw__x.0135 = phi i32 [ 0, %for.body26 ], [ %inc, %for.body33 ], !UID !11761
  val phi_dw_conv_s1_r_dw__x_013556 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 56, Res = true))

  //  %add29 = add i32 %sub21, %_dw_conv_s1_r_dw__x.0135, !dbg !11763, !UID !11764
  val binaryOp_add2957 = Module(new ComputeNode(NumOuts = 1, ID = 57, opCode = "add")(sign = false, Debug = false))

  //  %add34 = sub i32 %add29, %reass.add133, !dbg !11765, !UID !11766
  val binaryOp_add3458 = Module(new ComputeNode(NumOuts = 1, ID = 58, opCode = "sub")(sign = false, Debug = false))

  //  %arrayidx = getelementptr inbounds i8, i8* %_input, i32 %add34, !dbg !11768, !UID !11769
  val Gep_arrayidx59 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 59)(ElementSize = 1, ArraySize = List()))

  //  %1 = load i8, i8* %arrayidx, align 1, !dbg !11768, !tbaa !11725, !UID !11770
  val ld_60 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 60, RouteID = 0))

  //  %conv36 = sext i8 %1 to i16, !dbg !11772, !UID !11773
  val sextconv3661 = Module(new SextNode(NumOuts = 1))

  //  %add37 = add i16 %_dw_conv.1136, %conv36, !dbg !11774, !UID !11775
  val binaryOp_add3762 = Module(new ComputeNode(NumOuts = 2, ID = 62, opCode = "add")(sign = false, Debug = false))

  //  %inc = add nuw nsw i32 %_dw_conv_s1_r_dw__x.0135, 1, !dbg !11777, !UID !11778
  val binaryOp_inc63 = Module(new ComputeNode(NumOuts = 2, ID = 63, opCode = "add")(sign = false, Debug = false))

  //  %exitcond = icmp eq i32 %inc, 3, !dbg !11779, !UID !11780
  val icmp_exitcond64 = Module(new ComputeNode(NumOuts = 1, ID = 64, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond, label %for.cond.cleanup32, label %for.body33, !dbg !11748, !llvm.loop !11781, !UID !11783, !BB_UID !11784
  val br_65 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 65))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i32 0
  val const0 = Module(new ConstFastNode(value = 0, ID = 0))

  //i32 0
  val const1 = Module(new ConstFastNode(value = 0, ID = 1))

  //i32 0
  val const2 = Module(new ConstFastNode(value = 0, ID = 2))

  //i32 1
  val const3 = Module(new ConstFastNode(value = 1, ID = 3))

  //i32 1
  val const4 = Module(new ConstFastNode(value = 1, ID = 4))

  //i8 3
  val const5 = Module(new ConstFastNode(value = 3, ID = 5))

  //i32 1
  val const6 = Module(new ConstFastNode(value = 1, ID = 6))

  //i16 0
  val const7 = Module(new ConstFastNode(value = 0, ID = 7))

  //i32 0
  val const8 = Module(new ConstFastNode(value = 0, ID = 8))

  //i32 1
  val const9 = Module(new ConstFastNode(value = 1, ID = 9))

  //i32 3
  val const10 = Module(new ConstFastNode(value = 3, ID = 10))

  //i32 0
  val const11 = Module(new ConstFastNode(value = 0, ID = 11))

  //i32 1
  val const12 = Module(new ConstFastNode(value = 1, ID = 12))

  //i32 3
  val const13 = Module(new ConstFastNode(value = 3, ID = 13))



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_entry0.io.predicateIn(0) <> InputSplitter.io.Out.enable

  bb_for_body_lr_ph1.io.predicateIn(0) <> br_2.io.TrueOutput(0)

  bb_for_cond_cleanup3.io.predicateIn(1) <> br_2.io.FalseOutput(0)

  bb_for_cond_cleanup3.io.predicateIn(0) <> br_12.io.Out(0)

  bb_for_body13_lr_ph5.io.predicateIn(0) <> br_15.io.TrueOutput(0)

  bb_for_cond_cleanup127.io.predicateIn(1) <> br_15.io.FalseOutput(0)

  bb_for_cond_cleanup127.io.predicateIn(0) <> br_19.io.Out(0)

  bb_for_body20_lr_ph9.io.predicateIn(0) <> br_24.io.TrueOutput(0)

  bb_for_cond_cleanup1911.io.predicateIn(1) <> br_24.io.FalseOutput(0)

  bb_for_cond_cleanup1911.io.predicateIn(0) <> br_28.io.Out(0)



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE LOOP                     *
   * ================================================================== */

  bb_for_cond_cleanup_loopexit2.io.predicateIn(0) <> Loop_4.io.loopExit(0)

  bb_for_body4.io.predicateIn(0) <> Loop_4.io.activate_loop_start

  bb_for_body4.io.predicateIn(1) <> Loop_4.io.activate_loop_back

  bb_for_cond_cleanup12_loopexit6.io.predicateIn(0) <> Loop_3.io.loopExit(0)

  bb_for_body138.io.predicateIn(0) <> Loop_3.io.activate_loop_start

  bb_for_body138.io.predicateIn(1) <> Loop_3.io.activate_loop_back

  bb_for_cond_cleanup19_loopexit10.io.predicateIn(0) <> Loop_2.io.loopExit(0)

  bb_for_body2012.io.predicateIn(0) <> Loop_2.io.activate_loop_start

  bb_for_body2012.io.predicateIn(1) <> Loop_2.io.activate_loop_back

  bb_for_cond_cleanup2513.io.predicateIn(0) <> Loop_1.io.loopExit(0)

  bb_for_body2614.io.predicateIn(1) <> Loop_1.io.activate_loop_start

  bb_for_body2614.io.predicateIn(0) <> Loop_1.io.activate_loop_back

  bb_for_cond_cleanup3215.io.predicateIn(0) <> Loop_0.io.loopExit(0)

  bb_for_body3316.io.predicateIn(1) <> Loop_0.io.activate_loop_start

  bb_for_body3316.io.predicateIn(0) <> Loop_0.io.activate_loop_back



  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */

  Loop_0.io.enable <> br_50.io.Out(0)

  Loop_0.io.loopBack(0) <> br_65.io.FalseOutput(0)

  Loop_0.io.loopFinish(0) <> br_65.io.TrueOutput(0)

  Loop_1.io.enable <> br_33.io.Out(0)

  Loop_1.io.loopBack(0) <> br_54.io.FalseOutput(0)

  Loop_1.io.loopFinish(0) <> br_54.io.TrueOutput(0)

  Loop_2.io.enable <> br_27.io.Out(0)

  Loop_2.io.loopBack(0) <> br_44.io.TrueOutput(0)

  Loop_2.io.loopFinish(0) <> br_44.io.FalseOutput(0)

  Loop_3.io.enable <> br_18.io.Out(0)

  Loop_3.io.loopBack(0) <> br_31.io.TrueOutput(0)

  Loop_3.io.loopFinish(0) <> br_31.io.FalseOutput(0)

  Loop_4.io.enable <> br_11.io.Out(0)

  Loop_4.io.loopBack(0) <> br_22.io.TrueOutput(0)

  Loop_4.io.loopFinish(0) <> br_22.io.FalseOutput(0)



  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

  Loop_0.io.InLiveIn(0) <> phi_dw_conv_013845.io.Out(0)

  Loop_0.io.InLiveIn(1) <> binaryOp_sub2149.io.Out(0)

  Loop_0.io.InLiveIn(2) <> Loop_1.io.OutLiveIn.elements("field0")(0)

  Loop_0.io.InLiveIn(3) <> Loop_1.io.OutLiveIn.elements("field1")(0)

  Loop_1.io.InLiveIn(0) <> Loop_2.io.OutLiveIn.elements("field3")(0)

  Loop_1.io.InLiveIn(1) <> Loop_2.io.OutLiveIn.elements("field5")(0)

  Loop_1.io.InLiveIn(2) <> Loop_2.io.OutLiveIn.elements("field2")(0)

  Loop_1.io.InLiveIn(3) <> Loop_2.io.OutLiveIn.elements("field0")(0)

  Loop_1.io.InLiveIn(4) <> Loop_2.io.OutLiveIn.elements("field4")(0)

  Loop_2.io.InLiveIn(0) <> phi_output_s0_y_014223.io.Out(0)

  Loop_2.io.InLiveIn(1) <> binaryOp_reass_mul13226.io.Out(0)

  Loop_2.io.InLiveIn(2) <> Loop_3.io.OutLiveIn.elements("field1")(0)

  Loop_2.io.InLiveIn(3) <> Loop_3.io.OutLiveIn.elements("field3")(0)

  Loop_2.io.InLiveIn(4) <> Loop_3.io.OutLiveIn.elements("field4")(0)

  Loop_2.io.InLiveIn(5) <> Loop_3.io.OutLiveIn.elements("field5")(0)

  Loop_2.io.InLiveIn(6) <> Loop_3.io.OutLiveIn.elements("field0")(0)

  Loop_2.io.InLiveIn(7) <> Loop_3.io.OutLiveIn.elements("field2")(0)

  Loop_2.io.InLiveIn(8) <> Loop_3.io.OutLiveIn.elements("field6")(0)

  Loop_2.io.InLiveIn(9) <> Loop_3.io.OutLiveIn.elements("field7")(0)

  Loop_3.io.InLiveIn(0) <> binaryOp_reass_mul17.io.Out(0)

  Loop_3.io.InLiveIn(1) <> Loop_4.io.OutLiveIn.elements("field7")(0)

  Loop_3.io.InLiveIn(2) <> Loop_4.io.OutLiveIn.elements("field6")(0)

  Loop_3.io.InLiveIn(3) <> Loop_4.io.OutLiveIn.elements("field9")(0)

  Loop_3.io.InLiveIn(4) <> Loop_4.io.OutLiveIn.elements("field8")(0)

  Loop_3.io.InLiveIn(5) <> Loop_4.io.OutLiveIn.elements("field10")(0)

  Loop_3.io.InLiveIn(6) <> Loop_4.io.OutLiveIn.elements("field11")(0)

  Loop_3.io.InLiveIn(7) <> Loop_4.io.OutLiveIn.elements("field12")(0)

  Loop_3.io.InLiveIn(8) <> Loop_4.io.OutLiveIn.elements("field3")(0)

  Loop_3.io.InLiveIn(9) <> Loop_4.io.OutLiveIn.elements("field5")(0)

  Loop_3.io.InLiveIn(10) <> Loop_4.io.OutLiveIn.elements("field13")(0)

  Loop_3.io.InLiveIn(11) <> Loop_4.io.OutLiveIn.elements("field4")(0)

  Loop_4.io.InLiveIn(0) <> InputSplitter.io.Out.data.elements("field12")(0)

  Loop_4.io.InLiveIn(1) <> icmp_cmp111414.io.Out(0)

  Loop_4.io.InLiveIn(2) <> InputSplitter.io.Out.data.elements("field14")(0)

  Loop_4.io.InLiveIn(3) <> InputSplitter.io.Out.data.elements("field9")(0)

  Loop_4.io.InLiveIn(4) <> icmp_cmp181396.io.Out(0)

  Loop_4.io.InLiveIn(5) <> InputSplitter.io.Out.data.elements("field11")(0)

  Loop_4.io.InLiveIn(6) <> InputSplitter.io.Out.data.elements("field7")(0)

  Loop_4.io.InLiveIn(7) <> InputSplitter.io.Out.data.elements("field4")(0)

  Loop_4.io.InLiveIn(8) <> binaryOp_add6_neg10.io.Out(0)

  Loop_4.io.InLiveIn(9) <> binaryOp_reass_add1339.io.Out(0)

  Loop_4.io.InLiveIn(10) <> InputSplitter.io.Out.data.elements("field0")(0)

  Loop_4.io.InLiveIn(11) <> InputSplitter.io.Out.data.elements("field1")(0)

  Loop_4.io.InLiveIn(12) <> binaryOp_add175.io.Out(0)

  Loop_4.io.InLiveIn(13) <> binaryOp_add103.io.Out(0)

  Loop_4.io.InLiveIn(14) <> binaryOp_add70.io.Out(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */

  phi_dw_conv_113655.io.InData(0) <> Loop_0.io.OutLiveIn.elements("field0")(0)

  binaryOp_add2957.io.LeftIO <> Loop_0.io.OutLiveIn.elements("field1")(0)

  binaryOp_add3458.io.RightIO <> Loop_0.io.OutLiveIn.elements("field2")(0)

  Gep_arrayidx59.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field3")(0)

  binaryOp_mul2848.io.RightIO <> Loop_1.io.OutLiveIn.elements("field2")(0)

  binaryOp_add2747.io.RightIO <> Loop_1.io.OutLiveIn.elements("field3")(0)

  binaryOp_sub2149.io.LeftIO <> Loop_1.io.OutLiveIn.elements("field4")(0)

  binaryOp_add5239.io.RightIO <> Loop_2.io.OutLiveIn.elements("field1")(0)

  binaryOp_add1538.io.RightIO <> Loop_2.io.OutLiveIn.elements("field6")(0)

  phi_output_s0_x_014032.io.InData(0) <> Loop_2.io.OutLiveIn.elements("field7")(0)

  binaryOp_sub37.io.RightIO <> Loop_2.io.OutLiveIn.elements("field7")(1)

  Gep_arrayidx5340.io.baseAddress <> Loop_2.io.OutLiveIn.elements("field8")(0)

  icmp_cmp1843.io.RightIO <> Loop_2.io.OutLiveIn.elements("field9")(0)

  phi_output_s0_y_014223.io.InData(0) <> Loop_3.io.OutLiveIn.elements("field8")(0)

  binaryOp_reass_add13125.io.RightIO <> Loop_3.io.OutLiveIn.elements("field8")(1)

  binaryOp_reass_mul13226.io.RightIO <> Loop_3.io.OutLiveIn.elements("field9")(0)

  icmp_cmp1130.io.RightIO <> Loop_3.io.OutLiveIn.elements("field10")(0)

  br_24.io.CmpIO <> Loop_3.io.OutLiveIn.elements("field11")(0)

  phi_output_s0_k_014514.io.InData(0) <> Loop_4.io.OutLiveIn.elements("field0")(0)

  binaryOp_reass_add16.io.RightIO <> Loop_4.io.OutLiveIn.elements("field0")(1)

  br_15.io.CmpIO <> Loop_4.io.OutLiveIn.elements("field1")(0)

  binaryOp_reass_mul17.io.RightIO <> Loop_4.io.OutLiveIn.elements("field2")(0)

  icmp_cmp21.io.RightIO <> Loop_4.io.OutLiveIn.elements("field14")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */

  Loop_0.io.InLiveOut(0) <> binaryOp_add3762.io.Out(0)

  Loop_1.io.InLiveOut(0) <> phiadd37_lcssa51.io.Out(0)



  /* ================================================================== *
   *                   LOOP LIVE OUT DEPENDENCIES                       *
   * ================================================================== */

  phiadd37_lcssa51.io.InData(0) <> Loop_0.io.OutLiveOut.elements("field0")(0)

  phiadd37_lcssa_lcssa34.io.InData(0) <> Loop_1.io.OutLiveOut.elements("field0")(0)



  /* ================================================================== *
   *                   LOOP CARRY DEPENDENCIES                          *
   * ================================================================== */

  Loop_0.io.CarryDepenIn(0) <> binaryOp_inc63.io.Out(0)

  Loop_0.io.CarryDepenIn(1) <> binaryOp_add3762.io.Out(1)

  Loop_1.io.CarryDepenIn(0) <> binaryOp_inc4052.io.Out(0)

  Loop_1.io.CarryDepenIn(1) <> phiadd37_lcssa51.io.Out(1)

  Loop_2.io.CarryDepenIn(0) <> binaryOp_inc5542.io.Out(0)

  Loop_3.io.CarryDepenIn(0) <> binaryOp_inc5829.io.Out(0)

  Loop_4.io.CarryDepenIn(0) <> binaryOp_inc6120.io.Out(0)



  /* ================================================================== *
   *                   LOOP DATA CARRY DEPENDENCIES                     *
   * ================================================================== */

  phi_dw_conv_s1_r_dw__x_013556.io.InData(1) <> Loop_0.io.CarryDepenOut.elements("field0")(0)

  phi_dw_conv_113655.io.InData(1) <> Loop_0.io.CarryDepenOut.elements("field1")(0)

  phi_dw_conv_s1_r_dw__y_013746.io.InData(1) <> Loop_1.io.CarryDepenOut.elements("field0")(0)

  phi_dw_conv_013845.io.InData(1) <> Loop_1.io.CarryDepenOut.elements("field1")(0)

  phi_output_s0_x_014032.io.InData(1) <> Loop_2.io.CarryDepenOut.elements("field0")(0)

  phi_output_s0_y_014223.io.InData(1) <> Loop_3.io.CarryDepenOut.elements("field0")(0)

  phi_output_s0_k_014514.io.InData(1) <> Loop_4.io.CarryDepenOut.elements("field0")(0)



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  const0.io.enable <> bb_entry0.io.Out(0)

  binaryOp_add70.io.enable <> bb_entry0.io.Out(1)


  icmp_cmp1441.io.enable <> bb_entry0.io.Out(2)


  br_2.io.enable <> bb_entry0.io.Out(3)


  const1.io.enable <> bb_for_body_lr_ph1.io.Out(0)

  const2.io.enable <> bb_for_body_lr_ph1.io.Out(1)

  binaryOp_add103.io.enable <> bb_for_body_lr_ph1.io.Out(2)


  icmp_cmp111414.io.enable <> bb_for_body_lr_ph1.io.Out(3)


  binaryOp_add175.io.enable <> bb_for_body_lr_ph1.io.Out(4)


  icmp_cmp181396.io.enable <> bb_for_body_lr_ph1.io.Out(5)


  binaryOp_mul4_neg7.io.enable <> bb_for_body_lr_ph1.io.Out(6)


  binaryOp_mul3_neg8.io.enable <> bb_for_body_lr_ph1.io.Out(7)


  binaryOp_reass_add1339.io.enable <> bb_for_body_lr_ph1.io.Out(8)


  binaryOp_add6_neg10.io.enable <> bb_for_body_lr_ph1.io.Out(9)


  br_11.io.enable <> bb_for_body_lr_ph1.io.Out(10)


  br_12.io.enable <> bb_for_cond_cleanup_loopexit2.io.Out(0)


  ret_13.io.In.enable <> bb_for_cond_cleanup3.io.Out(0)


  phi_output_s0_k_014514.io.enable <> bb_for_body4.io.Out(0)


  br_15.io.enable <> bb_for_body4.io.Out(1)


  binaryOp_reass_add16.io.enable <> bb_for_body13_lr_ph5.io.Out(0)


  binaryOp_reass_mul17.io.enable <> bb_for_body13_lr_ph5.io.Out(1)


  br_18.io.enable <> bb_for_body13_lr_ph5.io.Out(2)


  br_19.io.enable <> bb_for_cond_cleanup12_loopexit6.io.Out(0)


  const3.io.enable <> bb_for_cond_cleanup127.io.Out(0)

  binaryOp_inc6120.io.enable <> bb_for_cond_cleanup127.io.Out(1)


  icmp_cmp21.io.enable <> bb_for_cond_cleanup127.io.Out(2)


  br_22.io.enable <> bb_for_cond_cleanup127.io.Out(3)


  phi_output_s0_y_014223.io.enable <> bb_for_body138.io.Out(0)


  br_24.io.enable <> bb_for_body138.io.Out(1)


  binaryOp_reass_add13125.io.enable <> bb_for_body20_lr_ph9.io.Out(0)


  binaryOp_reass_mul13226.io.enable <> bb_for_body20_lr_ph9.io.Out(1)


  br_27.io.enable <> bb_for_body20_lr_ph9.io.Out(2)


  br_28.io.enable <> bb_for_cond_cleanup19_loopexit10.io.Out(0)


  const4.io.enable <> bb_for_cond_cleanup1911.io.Out(0)

  binaryOp_inc5829.io.enable <> bb_for_cond_cleanup1911.io.Out(1)


  icmp_cmp1130.io.enable <> bb_for_cond_cleanup1911.io.Out(2)


  br_31.io.enable <> bb_for_cond_cleanup1911.io.Out(3)


  phi_output_s0_x_014032.io.enable <> bb_for_body2012.io.Out(0)


  br_33.io.enable <> bb_for_body2012.io.Out(1)


  const5.io.enable <> bb_for_cond_cleanup2513.io.Out(0)

  const6.io.enable <> bb_for_cond_cleanup2513.io.Out(1)

  phiadd37_lcssa_lcssa34.io.enable <> bb_for_cond_cleanup2513.io.Out(2)


  trunc35.io.enable <> bb_for_cond_cleanup2513.io.Out(3)


  binaryOp_conv5136.io.enable <> bb_for_cond_cleanup2513.io.Out(4)


  binaryOp_sub37.io.enable <> bb_for_cond_cleanup2513.io.Out(5)


  binaryOp_add1538.io.enable <> bb_for_cond_cleanup2513.io.Out(6)


  binaryOp_add5239.io.enable <> bb_for_cond_cleanup2513.io.Out(7)


  Gep_arrayidx5340.io.enable <> bb_for_cond_cleanup2513.io.Out(8)


  st_41.io.enable <> bb_for_cond_cleanup2513.io.Out(9)


  binaryOp_inc5542.io.enable <> bb_for_cond_cleanup2513.io.Out(10)


  icmp_cmp1843.io.enable <> bb_for_cond_cleanup2513.io.Out(11)


  br_44.io.enable <> bb_for_cond_cleanup2513.io.Out(12)


  const7.io.enable <> bb_for_body2614.io.Out(0)

  const8.io.enable <> bb_for_body2614.io.Out(1)

  phi_dw_conv_013845.io.enable <> bb_for_body2614.io.Out(2)


  phi_dw_conv_s1_r_dw__y_013746.io.enable <> bb_for_body2614.io.Out(3)


  binaryOp_add2747.io.enable <> bb_for_body2614.io.Out(4)


  binaryOp_mul2848.io.enable <> bb_for_body2614.io.Out(5)


  binaryOp_sub2149.io.enable <> bb_for_body2614.io.Out(6)


  br_50.io.enable <> bb_for_body2614.io.Out(7)


  const9.io.enable <> bb_for_cond_cleanup3215.io.Out(0)

  const10.io.enable <> bb_for_cond_cleanup3215.io.Out(1)

  phiadd37_lcssa51.io.enable <> bb_for_cond_cleanup3215.io.Out(2)


  binaryOp_inc4052.io.enable <> bb_for_cond_cleanup3215.io.Out(3)


  icmp_exitcond14653.io.enable <> bb_for_cond_cleanup3215.io.Out(4)


  br_54.io.enable <> bb_for_cond_cleanup3215.io.Out(5)


  const11.io.enable <> bb_for_body3316.io.Out(0)

  const12.io.enable <> bb_for_body3316.io.Out(1)

  const13.io.enable <> bb_for_body3316.io.Out(2)

  phi_dw_conv_113655.io.enable <> bb_for_body3316.io.Out(3)


  phi_dw_conv_s1_r_dw__x_013556.io.enable <> bb_for_body3316.io.Out(4)


  binaryOp_add2957.io.enable <> bb_for_body3316.io.Out(5)


  binaryOp_add3458.io.enable <> bb_for_body3316.io.Out(6)


  Gep_arrayidx59.io.enable <> bb_for_body3316.io.Out(7)


  ld_60.io.enable <> bb_for_body3316.io.Out(8)


  sextconv3661.io.enable <> bb_for_body3316.io.Out(9)


  binaryOp_add3762.io.enable <> bb_for_body3316.io.Out(10)


  binaryOp_inc63.io.enable <> bb_for_body3316.io.Out(11)


  icmp_exitcond64.io.enable <> bb_for_body3316.io.Out(12)


  br_65.io.enable <> bb_for_body3316.io.Out(13)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  phi_output_s0_k_014514.io.Mask <> bb_for_body4.io.MaskBB(0)

  phi_output_s0_y_014223.io.Mask <> bb_for_body138.io.MaskBB(0)

  phi_output_s0_x_014032.io.Mask <> bb_for_body2012.io.MaskBB(0)

  phiadd37_lcssa_lcssa34.io.Mask <> bb_for_cond_cleanup2513.io.MaskBB(0)

  phi_dw_conv_013845.io.Mask <> bb_for_body2614.io.MaskBB(0)

  phi_dw_conv_s1_r_dw__y_013746.io.Mask <> bb_for_body2614.io.MaskBB(1)

  phiadd37_lcssa51.io.Mask <> bb_for_cond_cleanup3215.io.MaskBB(0)

  phi_dw_conv_113655.io.Mask <> bb_for_body3316.io.MaskBB(0)

  phi_dw_conv_s1_r_dw__x_013556.io.Mask <> bb_for_body3316.io.MaskBB(1)



  /* ================================================================== *
   *                   PRINT ALLOCA OFFSET                              *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */

//  MemCtrl.io.WriteIn(0) <> st_41.io.memReq
//
//  st_41.io.memResp <> MemCtrl.io.WriteOut(0)

  MemCtrl.io.WriteIn(0) <> DontCare
  MemCtrl.io.WriteOut(0) <> DontCare

  st_41.io.memReq.ready := true.B
  st_41.io.memResp.valid := true.B
  st_41.io.memResp.RouteID := 0.U
  st_41.io.memResp.done := true.B

  MemCtrl.io.ReadIn(0) <> ld_60.io.memReq

  ld_60.io.memResp <> MemCtrl.io.ReadOut(0)



  /* ================================================================== *
   *                   PRINT SHARED CONNECTIONS                         *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  icmp_cmp1441.io.RightIO <> const0.io.Out

  icmp_cmp111414.io.RightIO <> const1.io.Out

  icmp_cmp181396.io.RightIO <> const2.io.Out

  binaryOp_inc6120.io.RightIO <> const3.io.Out

  binaryOp_inc5829.io.RightIO <> const4.io.Out

  binaryOp_conv5136.io.RightIO <> const5.io.Out

  binaryOp_inc5542.io.RightIO <> const6.io.Out

  phi_dw_conv_013845.io.InData(0) <> const7.io.Out

  phi_dw_conv_s1_r_dw__y_013746.io.InData(0) <> const8.io.Out

  binaryOp_inc4052.io.RightIO <> const9.io.Out

  icmp_exitcond14653.io.RightIO <> const10.io.Out

  phi_dw_conv_s1_r_dw__x_013556.io.InData(0) <> const11.io.Out

  binaryOp_inc63.io.RightIO <> const12.io.Out

  icmp_exitcond64.io.RightIO <> const13.io.Out

  br_2.io.CmpIO <> icmp_cmp1441.io.Out(0)

  binaryOp_reass_add1339.io.RightIO <> binaryOp_mul4_neg7.io.Out(0)

  binaryOp_reass_add1339.io.LeftIO <> binaryOp_mul3_neg8.io.Out(0)

  binaryOp_reass_add16.io.LeftIO <> phi_output_s0_k_014514.io.Out(0)

  binaryOp_inc6120.io.LeftIO <> phi_output_s0_k_014514.io.Out(1)

  binaryOp_reass_mul17.io.LeftIO <> binaryOp_reass_add16.io.Out(0)

  icmp_cmp21.io.LeftIO <> binaryOp_inc6120.io.Out(1)

  br_22.io.CmpIO <> icmp_cmp21.io.Out(0)

  binaryOp_reass_add13125.io.LeftIO <> phi_output_s0_y_014223.io.Out(1)

  binaryOp_inc5829.io.LeftIO <> phi_output_s0_y_014223.io.Out(2)

  binaryOp_reass_mul13226.io.LeftIO <> binaryOp_reass_add13125.io.Out(0)

  icmp_cmp1130.io.LeftIO <> binaryOp_inc5829.io.Out(1)

  br_31.io.CmpIO <> icmp_cmp1130.io.Out(0)

  binaryOp_sub37.io.LeftIO <> phi_output_s0_x_014032.io.Out(0)

  binaryOp_inc5542.io.LeftIO <> phi_output_s0_x_014032.io.Out(1)

  trunc35.io.Input <> phiadd37_lcssa_lcssa34.io.Out(0)

  binaryOp_conv5136.io.LeftIO <> trunc35.io.Out(0)

  st_41.io.inData <> binaryOp_conv5136.io.Out(0)

  binaryOp_add1538.io.LeftIO <> binaryOp_sub37.io.Out(0)

  binaryOp_add5239.io.LeftIO <> binaryOp_add1538.io.Out(0)

  Gep_arrayidx5340.io.idx(0) <> binaryOp_add5239.io.Out(0)

  st_41.io.GepAddr <> Gep_arrayidx5340.io.Out(0)

  icmp_cmp1843.io.LeftIO <> binaryOp_inc5542.io.Out(1)

  br_44.io.CmpIO <> icmp_cmp1843.io.Out(0)

  binaryOp_add2747.io.LeftIO <> phi_dw_conv_s1_r_dw__y_013746.io.Out(0)

  binaryOp_inc4052.io.LeftIO <> phi_dw_conv_s1_r_dw__y_013746.io.Out(1)

  binaryOp_mul2848.io.LeftIO <> binaryOp_add2747.io.Out(0)

  binaryOp_sub2149.io.RightIO <> binaryOp_mul2848.io.Out(0)

  icmp_exitcond14653.io.LeftIO <> binaryOp_inc4052.io.Out(1)

  br_54.io.CmpIO <> icmp_exitcond14653.io.Out(0)

  binaryOp_add3762.io.LeftIO <> phi_dw_conv_113655.io.Out(0)

  binaryOp_add2957.io.RightIO <> phi_dw_conv_s1_r_dw__x_013556.io.Out(0)

  binaryOp_inc63.io.LeftIO <> phi_dw_conv_s1_r_dw__x_013556.io.Out(1)

  binaryOp_add3458.io.LeftIO <> binaryOp_add2957.io.Out(0)

  Gep_arrayidx59.io.idx(0) <> binaryOp_add3458.io.Out(0)

  ld_60.io.GepAddr <> Gep_arrayidx59.io.Out(0)

  sextconv3661.io.Input <> ld_60.io.Out(0)

  binaryOp_add3762.io.RightIO <> sextconv3661.io.Out(0)

  icmp_exitcond64.io.LeftIO <> binaryOp_inc63.io.Out(1)

  br_65.io.CmpIO <> icmp_exitcond64.io.Out(0)

  binaryOp_add6_neg10.io.RightIO <> InputSplitter.io.Out.data.elements("field2")(0)

  binaryOp_mul4_neg7.io.RightIO <> InputSplitter.io.Out.data.elements("field3")(0)

  binaryOp_mul4_neg7.io.LeftIO <> InputSplitter.io.Out.data.elements("field4")(1)

  binaryOp_mul3_neg8.io.RightIO <> InputSplitter.io.Out.data.elements("field5")(0)

  binaryOp_mul3_neg8.io.LeftIO <> InputSplitter.io.Out.data.elements("field6")(0)

  binaryOp_add175.io.RightIO <> InputSplitter.io.Out.data.elements("field7")(1)

  binaryOp_add6_neg10.io.LeftIO <> InputSplitter.io.Out.data.elements("field7")(2)

  binaryOp_add175.io.LeftIO <> InputSplitter.io.Out.data.elements("field8")(0)

  icmp_cmp181396.io.LeftIO <> InputSplitter.io.Out.data.elements("field8")(1)

  binaryOp_add103.io.RightIO <> InputSplitter.io.Out.data.elements("field9")(1)

  binaryOp_add103.io.LeftIO <> InputSplitter.io.Out.data.elements("field10")(0)

  icmp_cmp111414.io.LeftIO <> InputSplitter.io.Out.data.elements("field10")(1)

  binaryOp_add70.io.RightIO <> InputSplitter.io.Out.data.elements("field12")(1)

  binaryOp_add70.io.LeftIO <> InputSplitter.io.Out.data.elements("field13")(0)

  icmp_cmp1441.io.LeftIO <> InputSplitter.io.Out.data.elements("field13")(1)

  st_41.io.Out(0).ready := true.B



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_13.io.Out

}

import java.io.{File, FileWriter}

object convLayerHalideTop extends App {
  val dir = new File("RTL/convLayerHalideTop");
  dir.mkdirs
  implicit val p = new WithAccelConfig
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new convLayerHalideDF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}
