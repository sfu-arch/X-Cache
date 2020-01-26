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

abstract class stridedConvHalideDFIO(implicit val p: Parameters) extends Module with HasAccelParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32, 32, 32, 32, 32, 32))))
    val MemResp = Flipped(Valid(new MemResp))
    val MemReq = Decoupled(new MemReq)
    val out = Decoupled(new Call(List()))
  })
}

class stridedConvHalideDF(implicit p: Parameters) extends stridedConvHalideDFIO()(p) {


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  val MemCtrl = Module(new UnifiedController(ID = 0, Size = 32, NReads = 19, NWrites = 9)
  (WControl = new WriteMemoryController(NumOps = 9, BaseSize = 2, NumEntries = 2))
  (RControl = new ReadMemoryController(NumOps = 19, BaseSize = 2, NumEntries = 2))
  (RWArbiter = new ReadWriteArbiter()))

  io.MemReq <> MemCtrl.io.MemReq
  MemCtrl.io.MemResp <> io.MemResp

  val InputSplitter = Module(new SplitCallNew(List(1, 9, 1, 1, 1, 2)))
  InputSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val Loop_0 = Module(new LoopBlockNode(NumIns = List(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 9, 1, 1, 1, 1, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 0))

  val Loop_1 = Module(new LoopBlockNode(NumIns = List(3, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 1))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_entry0 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 19, BID = 0))

  val bb_for_cond_cleanup1 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 1))

  val bb_for_body2 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 16, NumPhi = 1, BID = 2))

  val bb_for_cond_cleanup113 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 5, BID = 3))

  val bb_for_body124 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 93, NumPhi = 1, BID = 4))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  %mul = mul nsw i32 %_18, %_16, !dbg !11678, !UID !11679
  val binaryOp_mul0 = Module(new ComputeNode(NumOuts = 1, ID = 0, opCode = "mul")(sign = false, Debug = false))

  //  %add = add nsw i32 %mul, %_13, !dbg !11681, !UID !11682
  val binaryOp_add1 = Module(new ComputeNode(NumOuts = 1, ID = 1, opCode = "add")(sign = false, Debug = false))

  //  %arrayidx24 = getelementptr inbounds i32, i32* %_kernel, i32 1, !UID !11685
  val Gep_arrayidx242 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 2)(ElementSize = 4, ArraySize = List()))

  //  %arrayidx36 = getelementptr inbounds i32, i32* %_kernel, i32 2, !UID !11686
  val Gep_arrayidx363 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 3)(ElementSize = 4, ArraySize = List()))

  //  %arrayidx48 = getelementptr inbounds i32, i32* %_kernel, i32 3, !UID !11687
  val Gep_arrayidx484 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 4)(ElementSize = 4, ArraySize = List()))

  //  %arrayidx58 = getelementptr inbounds i32, i32* %_kernel, i32 4, !UID !11688
  val Gep_arrayidx585 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 5)(ElementSize = 4, ArraySize = List()))

  //  %arrayidx69 = getelementptr inbounds i32, i32* %_kernel, i32 5, !UID !11689
  val Gep_arrayidx696 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 6)(ElementSize = 4, ArraySize = List()))

  //  %arrayidx80 = getelementptr inbounds i32, i32* %_kernel, i32 6, !UID !11690
  val Gep_arrayidx807 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 7)(ElementSize = 4, ArraySize = List()))

  //  %arrayidx90 = getelementptr inbounds i32, i32* %_kernel, i32 7, !UID !11691
  val Gep_arrayidx908 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 8)(ElementSize = 4, ArraySize = List()))

  //  %arrayidx101 = getelementptr inbounds i32, i32* %_kernel, i32 8, !UID !11692
  val Gep_arrayidx1019 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 9)(ElementSize = 4, ArraySize = List()))

  //  br label %for.body, !dbg !11693, !UID !11694, !BB_UID !11695
  val br_10 = Module(new UBranchNode(ID = 10))

  //  ret void, !dbg !11696, !UID !11697, !BB_UID !11698
  val ret_11 = Module(new RetNode2(retTypes = List(), ID = 11))

  //  %_conv_s1_y.0295 = phi i32 [ 0, %entry ], [ %inc111, %for.cond.cleanup11 ], !UID !11699
  val phi_conv_s1_y_029512 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 4, ID = 12, Res = true))

  //  %mul1 = shl nuw nsw i32 %_conv_s1_y.0295, 1, !dbg !11700, !UID !11701
  val binaryOp_mul113 = Module(new ComputeNode(NumOuts = 2, ID = 13, opCode = "shl")(sign = false, Debug = false))

  //  %add2 = or i32 %mul1, 1, !dbg !11703, !UID !11704
  val binaryOp_add214 = Module(new ComputeNode(NumOuts = 1, ID = 14, opCode = "or")(sign = false, Debug = false))

  //  %mul3 = mul nsw i32 %add2, %_18, !dbg !11706, !UID !11707
  val binaryOp_mul315 = Module(new ComputeNode(NumOuts = 1, ID = 15, opCode = "mul")(sign = false, Debug = false))

  //  %sub = sub nsw i32 %mul3, %add, !dbg !11709, !UID !11710
  val binaryOp_sub16 = Module(new ComputeNode(NumOuts = 1, ID = 16, opCode = "sub")(sign = false, Debug = false))

  //  %add4 = add nuw nsw i32 %mul1, 2, !dbg !11712, !UID !11713
  val binaryOp_add417 = Module(new ComputeNode(NumOuts = 1, ID = 17, opCode = "add")(sign = false, Debug = false))

  //  %mul5 = mul nsw i32 %add4, %_18, !dbg !11715, !UID !11716
  val binaryOp_mul518 = Module(new ComputeNode(NumOuts = 1, ID = 18, opCode = "mul")(sign = false, Debug = false))

  //  %sub6 = sub nsw i32 %mul5, %add, !dbg !11718, !UID !11719
  val binaryOp_sub619 = Module(new ComputeNode(NumOuts = 1, ID = 19, opCode = "sub")(sign = false, Debug = false))

  //  %mul7 = mul nsw i32 %_conv_s1_y.0295, %_18, !dbg !11721, !UID !11722
  val binaryOp_mul720 = Module(new ComputeNode(NumOuts = 1, ID = 20, opCode = "mul")(sign = false, Debug = false))

  //  %mul8 = mul nuw nsw i32 %_conv_s1_y.0295, 31, !dbg !11724, !UID !11725
  val binaryOp_mul821 = Module(new ComputeNode(NumOuts = 1, ID = 21, opCode = "mul")(sign = false, Debug = false))

  //  br label %for.body12, !dbg !11728, !UID !11729, !BB_UID !11730
  val br_22 = Module(new UBranchNode(ID = 22))

  //  %inc111 = add nuw nsw i32 %_conv_s1_y.0295, 1, !dbg !11731, !UID !11732
  val binaryOp_inc11123 = Module(new ComputeNode(NumOuts = 2, ID = 23, opCode = "add")(sign = false, Debug = false))

  //  %exitcond296 = icmp eq i32 %inc111, 31, !dbg !11733, !UID !11734
  val icmp_exitcond29624 = Module(new ComputeNode(NumOuts = 1, ID = 24, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond296, label %for.cond.cleanup, label %for.body, !dbg !11693, !llvm.loop !11735, !UID !11737, !BB_UID !11738
  val br_25 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 25))

  //  %_conv_s1_x.0294 = phi i32 [ 0, %for.body ], [ %inc, %for.body12 ], !UID !11739
  val phi_conv_s1_x_029426 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 4, ID = 26, Res = true))

  //  %add13 = add nuw nsw i32 %_conv_s1_x.0294, %mul8, !dbg !11740, !UID !11741
  val binaryOp_add1327 = Module(new ComputeNode(NumOuts = 1, ID = 27, opCode = "add")(sign = false, Debug = false))

  //  %arrayidx = getelementptr inbounds i32, i32* %_conv, i32 %add13, !dbg !11743, !UID !11744
  val Gep_arrayidx28 = Module(new GepNode(NumIns = 1, NumOuts = 10, ID = 28)(ElementSize = 4, ArraySize = List()))

  //  %0 = load i32, i32* %arrayidx, align 4, !dbg !11743, !tbaa !11745, !UID !11749
  val ld_29 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 29, RouteID = 0))

  //  %1 = load i32, i32* %_kernel, align 4, !dbg !11751, !tbaa !11745, !UID !11752
  val ld_30 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 30, RouteID = 1))

  //  %add15 = add nsw i32 %_conv_s1_x.0294, %mul7, !dbg !11754, !UID !11755
  val binaryOp_add1531 = Module(new ComputeNode(NumOuts = 1, ID = 31, opCode = "add")(sign = false, Debug = false))

  //  %mul16 = shl nsw i32 %add15, 1, !dbg !11757, !UID !11758
  val binaryOp_mul1632 = Module(new ComputeNode(NumOuts = 1, ID = 32, opCode = "shl")(sign = false, Debug = false))

  //  %sub17 = sub nsw i32 %mul16, %add, !dbg !11760, !UID !11761
  val binaryOp_sub1733 = Module(new ComputeNode(NumOuts = 3, ID = 33, opCode = "sub")(sign = false, Debug = false))

  //  %arrayidx18 = getelementptr inbounds i8, i8* %_input, i32 %sub17, !dbg !11763, !UID !11764
  val Gep_arrayidx1834 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 34)(ElementSize = 1, ArraySize = List()))

  //  %2 = load i8, i8* %arrayidx18, align 1, !dbg !11763, !tbaa !11765, !UID !11766
  val ld_35 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 35, RouteID = 2))

  //  %conv = zext i8 %2 to i32, !dbg !11768, !UID !11769
  val sextconv36 = Module(new ZextNode(NumOuts = 1))

  //  %mul19 = mul nsw i32 %1, %conv, !dbg !11771, !UID !11772
  val binaryOp_mul1937 = Module(new ComputeNode(NumOuts = 1, ID = 37, opCode = "mul")(sign = false, Debug = false))

  //  %add20 = add nsw i32 %mul19, %0, !dbg !11774, !UID !11775
  val binaryOp_add2038 = Module(new ComputeNode(NumOuts = 2, ID = 38, opCode = "add")(sign = false, Debug = false))

  //  store i32 %add20, i32* %arrayidx, align 4, !dbg !11777, !tbaa !11745, !UID !11778
  val st_39 = Module(new UnTypStore(NumPredOps = 0, NumSuccOps = 0, ID = 39, RouteID = 0))

  //  %3 = load i32, i32* %arrayidx24, align 4, !dbg !11781, !tbaa !11745, !UID !11782
  val ld_40 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 40, RouteID = 3))

  //  %add28 = add nsw i32 %sub17, 1, !dbg !11787, !UID !11788
  val binaryOp_add2841 = Module(new ComputeNode(NumOuts = 1, ID = 41, opCode = "add")(sign = false, Debug = false))

  //  %arrayidx29 = getelementptr inbounds i8, i8* %_input, i32 %add28, !dbg !11790, !UID !11791
  val Gep_arrayidx2942 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 42)(ElementSize = 1, ArraySize = List()))

  //  %4 = load i8, i8* %arrayidx29, align 1, !dbg !11790, !tbaa !11765, !UID !11792
  val ld_43 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 43, RouteID = 4))

  //  %conv30 = zext i8 %4 to i32, !dbg !11794, !UID !11795
  val sextconv3044 = Module(new ZextNode(NumOuts = 1))

  //  %mul31 = mul nsw i32 %3, %conv30, !dbg !11797, !UID !11798
  val binaryOp_mul3145 = Module(new ComputeNode(NumOuts = 1, ID = 45, opCode = "mul")(sign = false, Debug = false))

  //  %add32 = add nsw i32 %mul31, %add20, !dbg !11800, !UID !11801
  val binaryOp_add3246 = Module(new ComputeNode(NumOuts = 2, ID = 46, opCode = "add")(sign = false, Debug = false))

  //  store i32 %add32, i32* %arrayidx, align 4, !dbg !11803, !tbaa !11745, !UID !11804
  val st_47 = Module(new UnTypStore(NumPredOps = 0, NumSuccOps = 0, ID = 47, RouteID = 1))

  //  %5 = load i32, i32* %arrayidx36, align 4, !dbg !11807, !tbaa !11745, !UID !11808
  val ld_48 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 48, RouteID = 5))

  //  %add40 = add nsw i32 %sub17, 2, !dbg !11813, !UID !11814
  val binaryOp_add4049 = Module(new ComputeNode(NumOuts = 1, ID = 49, opCode = "add")(sign = false, Debug = false))

  //  %arrayidx41 = getelementptr inbounds i8, i8* %_input, i32 %add40, !dbg !11816, !UID !11817
  val Gep_arrayidx4150 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 50)(ElementSize = 1, ArraySize = List()))

  //  %6 = load i8, i8* %arrayidx41, align 1, !dbg !11816, !tbaa !11765, !UID !11818
  val ld_51 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 51, RouteID = 6))

  //  %conv42 = zext i8 %6 to i32, !dbg !11820, !UID !11821
  val sextconv4252 = Module(new ZextNode(NumOuts = 1))

  //  %mul43 = mul nsw i32 %5, %conv42, !dbg !11823, !UID !11824
  val binaryOp_mul4353 = Module(new ComputeNode(NumOuts = 1, ID = 53, opCode = "mul")(sign = false, Debug = false))

  //  %add44 = add nsw i32 %mul43, %add32, !dbg !11826, !UID !11827
  val binaryOp_add4454 = Module(new ComputeNode(NumOuts = 2, ID = 54, opCode = "add")(sign = false, Debug = false))

  //  store i32 %add44, i32* %arrayidx, align 4, !dbg !11829, !tbaa !11745, !UID !11830
  val st_55 = Module(new UnTypStore(NumPredOps = 0, NumSuccOps = 0, ID = 55, RouteID = 2))

  //  %7 = load i32, i32* %arrayidx48, align 4, !dbg !11833, !tbaa !11745, !UID !11834
  val ld_56 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 56, RouteID = 7))

  //  %mul49 = shl nuw nsw i32 %_conv_s1_x.0294, 1, !dbg !11836, !UID !11837
  val binaryOp_mul4957 = Module(new ComputeNode(NumOuts = 2, ID = 57, opCode = "shl")(sign = false, Debug = false))

  //  %add50 = add nsw i32 %mul49, %sub, !dbg !11839, !UID !11840
  val binaryOp_add5058 = Module(new ComputeNode(NumOuts = 3, ID = 58, opCode = "add")(sign = false, Debug = false))

  //  %arrayidx51 = getelementptr inbounds i8, i8* %_input, i32 %add50, !dbg !11842, !UID !11843
  val Gep_arrayidx5159 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 59)(ElementSize = 1, ArraySize = List()))

  //  %8 = load i8, i8* %arrayidx51, align 1, !dbg !11842, !tbaa !11765, !UID !11844
  val ld_60 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 60, RouteID = 8))

  //  %conv52 = zext i8 %8 to i32, !dbg !11846, !UID !11847
  val sextconv5261 = Module(new ZextNode(NumOuts = 1))

  //  %mul53 = mul nsw i32 %7, %conv52, !dbg !11849, !UID !11850
  val binaryOp_mul5362 = Module(new ComputeNode(NumOuts = 1, ID = 62, opCode = "mul")(sign = false, Debug = false))

  //  %add54 = add nsw i32 %mul53, %add44, !dbg !11852, !UID !11853
  val binaryOp_add5463 = Module(new ComputeNode(NumOuts = 2, ID = 63, opCode = "add")(sign = false, Debug = false))

  //  store i32 %add54, i32* %arrayidx, align 4, !dbg !11855, !tbaa !11745, !UID !11856
  val st_64 = Module(new UnTypStore(NumPredOps = 0, NumSuccOps = 0, ID = 64, RouteID = 3))

  //  %9 = load i32, i32* %arrayidx58, align 4, !dbg !11859, !tbaa !11745, !UID !11860
  val ld_65 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 65, RouteID = 9))

  //  %add61 = add nsw i32 %add50, 1, !dbg !11864, !UID !11865
  val binaryOp_add6166 = Module(new ComputeNode(NumOuts = 1, ID = 66, opCode = "add")(sign = false, Debug = false))

  //  %arrayidx62 = getelementptr inbounds i8, i8* %_input, i32 %add61, !dbg !11867, !UID !11868
  val Gep_arrayidx6267 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 67)(ElementSize = 1, ArraySize = List()))

  //  %10 = load i8, i8* %arrayidx62, align 1, !dbg !11867, !tbaa !11765, !UID !11869
  val ld_68 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 68, RouteID = 10))

  //  %conv63 = zext i8 %10 to i32, !dbg !11871, !UID !11872
  val sextconv6369 = Module(new ZextNode(NumOuts = 1))

  //  %mul64 = mul nsw i32 %9, %conv63, !dbg !11874, !UID !11875
  val binaryOp_mul6470 = Module(new ComputeNode(NumOuts = 1, ID = 70, opCode = "mul")(sign = false, Debug = false))

  //  %add65 = add nsw i32 %mul64, %add54, !dbg !11877, !UID !11878
  val binaryOp_add6571 = Module(new ComputeNode(NumOuts = 2, ID = 71, opCode = "add")(sign = false, Debug = false))

  //  store i32 %add65, i32* %arrayidx, align 4, !dbg !11880, !tbaa !11745, !UID !11881
  val st_72 = Module(new UnTypStore(NumPredOps = 0, NumSuccOps = 0, ID = 72, RouteID = 4))

  //  %11 = load i32, i32* %arrayidx69, align 4, !dbg !11884, !tbaa !11745, !UID !11885
  val ld_73 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 73, RouteID = 11))

  //  %add72 = add nsw i32 %add50, 2, !dbg !11889, !UID !11890
  val binaryOp_add7274 = Module(new ComputeNode(NumOuts = 1, ID = 74, opCode = "add")(sign = false, Debug = false))

  //  %arrayidx73 = getelementptr inbounds i8, i8* %_input, i32 %add72, !dbg !11892, !UID !11893
  val Gep_arrayidx7375 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 75)(ElementSize = 1, ArraySize = List()))

  //  %12 = load i8, i8* %arrayidx73, align 1, !dbg !11892, !tbaa !11765, !UID !11894
  val ld_76 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 76, RouteID = 12))

  //  %conv74 = zext i8 %12 to i32, !dbg !11896, !UID !11897
  val sextconv7477 = Module(new ZextNode(NumOuts = 1))

  //  %mul75 = mul nsw i32 %11, %conv74, !dbg !11899, !UID !11900
  val binaryOp_mul7578 = Module(new ComputeNode(NumOuts = 1, ID = 78, opCode = "mul")(sign = false, Debug = false))

  //  %add76 = add nsw i32 %mul75, %add65, !dbg !11902, !UID !11903
  val binaryOp_add7679 = Module(new ComputeNode(NumOuts = 2, ID = 79, opCode = "add")(sign = false, Debug = false))

  //  store i32 %add76, i32* %arrayidx, align 4, !dbg !11905, !tbaa !11745, !UID !11906
  val st_80 = Module(new UnTypStore(NumPredOps = 0, NumSuccOps = 0, ID = 80, RouteID = 5))

  //  %13 = load i32, i32* %arrayidx80, align 4, !dbg !11909, !tbaa !11745, !UID !11910
  val ld_81 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 81, RouteID = 13))

  //  %add82 = add nsw i32 %mul49, %sub6, !dbg !11913, !UID !11914
  val binaryOp_add8282 = Module(new ComputeNode(NumOuts = 3, ID = 82, opCode = "add")(sign = false, Debug = false))

  //  %arrayidx83 = getelementptr inbounds i8, i8* %_input, i32 %add82, !dbg !11916, !UID !11917
  val Gep_arrayidx8383 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 83)(ElementSize = 1, ArraySize = List()))

  //  %14 = load i8, i8* %arrayidx83, align 1, !dbg !11916, !tbaa !11765, !UID !11918
  val ld_84 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 84, RouteID = 14))

  //  %conv84 = zext i8 %14 to i32, !dbg !11920, !UID !11921
  val sextconv8485 = Module(new ZextNode(NumOuts = 1))

  //  %mul85 = mul nsw i32 %13, %conv84, !dbg !11923, !UID !11924
  val binaryOp_mul8586 = Module(new ComputeNode(NumOuts = 1, ID = 86, opCode = "mul")(sign = false, Debug = false))

  //  %add86 = add nsw i32 %mul85, %add76, !dbg !11926, !UID !11927
  val binaryOp_add8687 = Module(new ComputeNode(NumOuts = 2, ID = 87, opCode = "add")(sign = false, Debug = false))

  //  store i32 %add86, i32* %arrayidx, align 4, !dbg !11929, !tbaa !11745, !UID !11930
  val st_88 = Module(new UnTypStore(NumPredOps = 0, NumSuccOps = 0, ID = 88, RouteID = 6))

  //  %15 = load i32, i32* %arrayidx90, align 4, !dbg !11933, !tbaa !11745, !UID !11934
  val ld_89 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 89, RouteID = 15))

  //  %add93 = add nsw i32 %add82, 1, !dbg !11938, !UID !11939
  val binaryOp_add9390 = Module(new ComputeNode(NumOuts = 1, ID = 90, opCode = "add")(sign = false, Debug = false))

  //  %arrayidx94 = getelementptr inbounds i8, i8* %_input, i32 %add93, !dbg !11941, !UID !11942
  val Gep_arrayidx9491 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 91)(ElementSize = 1, ArraySize = List()))

  //  %16 = load i8, i8* %arrayidx94, align 1, !dbg !11941, !tbaa !11765, !UID !11943
  val ld_92 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 92, RouteID = 16))

  //  %conv95 = zext i8 %16 to i32, !dbg !11945, !UID !11946
  val sextconv9593 = Module(new ZextNode(NumOuts = 1))

  //  %mul96 = mul nsw i32 %15, %conv95, !dbg !11948, !UID !11949
  val binaryOp_mul9694 = Module(new ComputeNode(NumOuts = 1, ID = 94, opCode = "mul")(sign = false, Debug = false))

  //  %add97 = add nsw i32 %mul96, %add86, !dbg !11951, !UID !11952
  val binaryOp_add9795 = Module(new ComputeNode(NumOuts = 2, ID = 95, opCode = "add")(sign = false, Debug = false))

  //  store i32 %add97, i32* %arrayidx, align 4, !dbg !11954, !tbaa !11745, !UID !11955
  val st_96 = Module(new UnTypStore(NumPredOps = 0, NumSuccOps = 0, ID = 96, RouteID = 7))

  //  %17 = load i32, i32* %arrayidx101, align 4, !dbg !11958, !tbaa !11745, !UID !11959
  val ld_97 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 97, RouteID = 17))

  //  %add104 = add nsw i32 %add82, 2, !dbg !11963, !UID !11964
  val binaryOp_add10498 = Module(new ComputeNode(NumOuts = 1, ID = 98, opCode = "add")(sign = false, Debug = false))

  //  %arrayidx105 = getelementptr inbounds i8, i8* %_input, i32 %add104, !dbg !11966, !UID !11967
  val Gep_arrayidx10599 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 99)(ElementSize = 1, ArraySize = List()))

  //  %18 = load i8, i8* %arrayidx105, align 1, !dbg !11966, !tbaa !11765, !UID !11968
  val ld_100 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 100, RouteID = 18))

  //  %conv106 = zext i8 %18 to i32, !dbg !11970, !UID !11971
  val sextconv106101 = Module(new ZextNode(NumOuts = 1))

  //  %mul107 = mul nsw i32 %17, %conv106, !dbg !11973, !UID !11974
  val binaryOp_mul107102 = Module(new ComputeNode(NumOuts = 1, ID = 102, opCode = "mul")(sign = false, Debug = false))

  //  %add108 = add nsw i32 %mul107, %add97, !dbg !11976, !UID !11977
  val binaryOp_add108103 = Module(new ComputeNode(NumOuts = 1, ID = 103, opCode = "add")(sign = false, Debug = false))

  //  store i32 %add108, i32* %arrayidx, align 4, !dbg !11979, !tbaa !11745, !UID !11980
  val st_104 = Module(new UnTypStore(NumPredOps = 0, NumSuccOps = 0, ID = 104, RouteID = 8))

  //  %inc = add nuw nsw i32 %_conv_s1_x.0294, 1, !dbg !11981, !UID !11982
  val binaryOp_inc105 = Module(new ComputeNode(NumOuts = 2, ID = 105, opCode = "add")(sign = false, Debug = false))

  //  %exitcond = icmp eq i32 %inc, 31, !dbg !11983, !UID !11984
  val icmp_exitcond106 = Module(new ComputeNode(NumOuts = 1, ID = 106, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond, label %for.cond.cleanup11, label %for.body12, !dbg !11728, !llvm.loop !11985, !UID !11987, !BB_UID !11988
  val br_107 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 107))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i32 1
  val const0 = Module(new ConstFastNode(value = 1, ID = 0))

  //i32 2
  val const1 = Module(new ConstFastNode(value = 2, ID = 1))

  //i32 3
  val const2 = Module(new ConstFastNode(value = 3, ID = 2))

  //i32 4
  val const3 = Module(new ConstFastNode(value = 4, ID = 3))

  //i32 5
  val const4 = Module(new ConstFastNode(value = 5, ID = 4))

  //i32 6
  val const5 = Module(new ConstFastNode(value = 6, ID = 5))

  //i32 7
  val const6 = Module(new ConstFastNode(value = 7, ID = 6))

  //i32 8
  val const7 = Module(new ConstFastNode(value = 8, ID = 7))

  //i32 0
  val const8 = Module(new ConstFastNode(value = 0, ID = 8))

  //i32 1
  val const9 = Module(new ConstFastNode(value = 1, ID = 9))

  //i32 1
  val const10 = Module(new ConstFastNode(value = 1, ID = 10))

  //i32 2
  val const11 = Module(new ConstFastNode(value = 2, ID = 11))

  //i32 31
  val const12 = Module(new ConstFastNode(value = 31, ID = 12))

  //i32 1
  val const13 = Module(new ConstFastNode(value = 1, ID = 13))

  //i32 31
  val const14 = Module(new ConstFastNode(value = 31, ID = 14))

  //i32 0
  val const15 = Module(new ConstFastNode(value = 0, ID = 15))

  //i32 1
  val const16 = Module(new ConstFastNode(value = 1, ID = 16))

  //i32 1
  val const17 = Module(new ConstFastNode(value = 1, ID = 17))

  //i32 2
  val const18 = Module(new ConstFastNode(value = 2, ID = 18))

  //i32 1
  val const19 = Module(new ConstFastNode(value = 1, ID = 19))

  //i32 1
  val const20 = Module(new ConstFastNode(value = 1, ID = 20))

  //i32 2
  val const21 = Module(new ConstFastNode(value = 2, ID = 21))

  //i32 1
  val const22 = Module(new ConstFastNode(value = 1, ID = 22))

  //i32 2
  val const23 = Module(new ConstFastNode(value = 2, ID = 23))

  //i32 1
  val const24 = Module(new ConstFastNode(value = 1, ID = 24))

  //i32 31
  val const25 = Module(new ConstFastNode(value = 31, ID = 25))



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_entry0.io.predicateIn(0) <> InputSplitter.io.Out.enable



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE LOOP                     *
   * ================================================================== */

  bb_for_cond_cleanup1.io.predicateIn(0) <> Loop_1.io.loopExit(0)

  bb_for_body2.io.predicateIn(1) <> Loop_1.io.activate_loop_start

  bb_for_body2.io.predicateIn(0) <> Loop_1.io.activate_loop_back

  bb_for_cond_cleanup113.io.predicateIn(0) <> Loop_0.io.loopExit(0)

  bb_for_body124.io.predicateIn(1) <> Loop_0.io.activate_loop_start

  bb_for_body124.io.predicateIn(0) <> Loop_0.io.activate_loop_back



  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */

  Loop_0.io.enable <> br_22.io.Out(0)

  Loop_0.io.loopBack(0) <> br_107.io.FalseOutput(0)

  Loop_0.io.loopFinish(0) <> br_107.io.TrueOutput(0)

  Loop_1.io.enable <> br_10.io.Out(0)

  Loop_1.io.loopBack(0) <> br_25.io.FalseOutput(0)

  Loop_1.io.loopFinish(0) <> br_25.io.TrueOutput(0)



  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

  Loop_0.io.InLiveIn(0) <> binaryOp_mul821.io.Out(0)

  Loop_0.io.InLiveIn(1) <> binaryOp_mul720.io.Out(0)

  Loop_0.io.InLiveIn(2) <> binaryOp_sub16.io.Out(0)

  Loop_0.io.InLiveIn(3) <> binaryOp_sub619.io.Out(0)

  Loop_0.io.InLiveIn(4) <> Loop_1.io.OutLiveIn.elements("field7")(0)

  Loop_0.io.InLiveIn(5) <> Loop_1.io.OutLiveIn.elements("field1")(0)

  Loop_0.io.InLiveIn(6) <> Loop_1.io.OutLiveIn.elements("field6")(0)

  Loop_0.io.InLiveIn(7) <> Loop_1.io.OutLiveIn.elements("field2")(0)

  Loop_0.io.InLiveIn(8) <> Loop_1.io.OutLiveIn.elements("field5")(0)

  Loop_0.io.InLiveIn(9) <> Loop_1.io.OutLiveIn.elements("field3")(0)

  Loop_0.io.InLiveIn(10) <> Loop_1.io.OutLiveIn.elements("field4")(0)

  Loop_0.io.InLiveIn(11) <> Loop_1.io.OutLiveIn.elements("field12")(0)

  Loop_0.io.InLiveIn(12) <> Loop_1.io.OutLiveIn.elements("field11")(0)

  Loop_0.io.InLiveIn(13) <> Loop_1.io.OutLiveIn.elements("field10")(0)

  Loop_0.io.InLiveIn(14) <> Loop_1.io.OutLiveIn.elements("field9")(0)

  Loop_0.io.InLiveIn(15) <> Loop_1.io.OutLiveIn.elements("field8")(0)

  Loop_1.io.InLiveIn(0) <> InputSplitter.io.Out.data.elements("field5")(0)

  Loop_1.io.InLiveIn(1) <> binaryOp_add1.io.Out(0)

  Loop_1.io.InLiveIn(2) <> InputSplitter.io.Out.data.elements("field2")(0)

  Loop_1.io.InLiveIn(3) <> InputSplitter.io.Out.data.elements("field1")(0)

  Loop_1.io.InLiveIn(4) <> InputSplitter.io.Out.data.elements("field0")(0)

  Loop_1.io.InLiveIn(5) <> Gep_arrayidx242.io.Out(0)

  Loop_1.io.InLiveIn(6) <> Gep_arrayidx363.io.Out(0)

  Loop_1.io.InLiveIn(7) <> Gep_arrayidx484.io.Out(0)

  Loop_1.io.InLiveIn(8) <> Gep_arrayidx585.io.Out(0)

  Loop_1.io.InLiveIn(9) <> Gep_arrayidx696.io.Out(0)

  Loop_1.io.InLiveIn(10) <> Gep_arrayidx807.io.Out(0)

  Loop_1.io.InLiveIn(11) <> Gep_arrayidx908.io.Out(0)

  Loop_1.io.InLiveIn(12) <> Gep_arrayidx1019.io.Out(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */

  binaryOp_add1327.io.RightIO <> Loop_0.io.OutLiveIn.elements("field0")(0)

  binaryOp_add1531.io.RightIO <> Loop_0.io.OutLiveIn.elements("field1")(0)

  binaryOp_add5058.io.RightIO <> Loop_0.io.OutLiveIn.elements("field2")(0)

  binaryOp_add8282.io.RightIO <> Loop_0.io.OutLiveIn.elements("field3")(0)

  ld_56.io.GepAddr <> Loop_0.io.OutLiveIn.elements("field4")(0)

  binaryOp_sub1733.io.RightIO <> Loop_0.io.OutLiveIn.elements("field5")(0)

  ld_48.io.GepAddr <> Loop_0.io.OutLiveIn.elements("field6")(0)

  Gep_arrayidx28.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field7")(0)

  ld_40.io.GepAddr <> Loop_0.io.OutLiveIn.elements("field8")(0)

  ld_30.io.GepAddr <> Loop_0.io.OutLiveIn.elements("field9")(0)

  Gep_arrayidx1834.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field10")(0)

  Gep_arrayidx2942.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field10")(1)

  Gep_arrayidx4150.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field10")(2)

  Gep_arrayidx5159.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field10")(3)

  Gep_arrayidx6267.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field10")(4)

  Gep_arrayidx7375.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field10")(5)

  Gep_arrayidx8383.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field10")(6)

  Gep_arrayidx9491.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field10")(7)

  Gep_arrayidx10599.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field10")(8)

  ld_97.io.GepAddr <> Loop_0.io.OutLiveIn.elements("field11")(0)

  ld_89.io.GepAddr <> Loop_0.io.OutLiveIn.elements("field12")(0)

  ld_81.io.GepAddr <> Loop_0.io.OutLiveIn.elements("field13")(0)

  ld_73.io.GepAddr <> Loop_0.io.OutLiveIn.elements("field14")(0)

  ld_65.io.GepAddr <> Loop_0.io.OutLiveIn.elements("field15")(0)

  binaryOp_mul315.io.RightIO <> Loop_1.io.OutLiveIn.elements("field0")(0)

  binaryOp_mul518.io.RightIO <> Loop_1.io.OutLiveIn.elements("field0")(1)

  binaryOp_mul720.io.RightIO <> Loop_1.io.OutLiveIn.elements("field0")(2)

  binaryOp_sub16.io.RightIO <> Loop_1.io.OutLiveIn.elements("field1")(1)

  binaryOp_sub619.io.RightIO <> Loop_1.io.OutLiveIn.elements("field1")(2)



  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP LIVE OUT DEPENDENCIES                       *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP CARRY DEPENDENCIES                          *
   * ================================================================== */

  Loop_0.io.CarryDepenIn(0) <> binaryOp_inc105.io.Out(0)

  Loop_1.io.CarryDepenIn(0) <> binaryOp_inc11123.io.Out(0)



  /* ================================================================== *
   *                   LOOP DATA CARRY DEPENDENCIES                     *
   * ================================================================== */

  phi_conv_s1_x_029426.io.InData(1) <> Loop_0.io.CarryDepenOut.elements("field0")(0)

  phi_conv_s1_y_029512.io.InData(1) <> Loop_1.io.CarryDepenOut.elements("field0")(0)



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  const0.io.enable <> bb_entry0.io.Out(0)

  const1.io.enable <> bb_entry0.io.Out(1)

  const2.io.enable <> bb_entry0.io.Out(2)

  const3.io.enable <> bb_entry0.io.Out(3)

  const4.io.enable <> bb_entry0.io.Out(4)

  const5.io.enable <> bb_entry0.io.Out(5)

  const6.io.enable <> bb_entry0.io.Out(6)

  const7.io.enable <> bb_entry0.io.Out(7)

  binaryOp_mul0.io.enable <> bb_entry0.io.Out(8)


  binaryOp_add1.io.enable <> bb_entry0.io.Out(9)


  Gep_arrayidx242.io.enable <> bb_entry0.io.Out(10)


  Gep_arrayidx363.io.enable <> bb_entry0.io.Out(11)


  Gep_arrayidx484.io.enable <> bb_entry0.io.Out(12)


  Gep_arrayidx585.io.enable <> bb_entry0.io.Out(13)


  Gep_arrayidx696.io.enable <> bb_entry0.io.Out(14)


  Gep_arrayidx807.io.enable <> bb_entry0.io.Out(15)


  Gep_arrayidx908.io.enable <> bb_entry0.io.Out(16)


  Gep_arrayidx1019.io.enable <> bb_entry0.io.Out(17)


  br_10.io.enable <> bb_entry0.io.Out(18)


  ret_11.io.In.enable <> bb_for_cond_cleanup1.io.Out(0)


  const8.io.enable <> bb_for_body2.io.Out(0)

  const9.io.enable <> bb_for_body2.io.Out(1)

  const10.io.enable <> bb_for_body2.io.Out(2)

  const11.io.enable <> bb_for_body2.io.Out(3)

  const12.io.enable <> bb_for_body2.io.Out(4)

  phi_conv_s1_y_029512.io.enable <> bb_for_body2.io.Out(5)


  binaryOp_mul113.io.enable <> bb_for_body2.io.Out(6)


  binaryOp_add214.io.enable <> bb_for_body2.io.Out(7)


  binaryOp_mul315.io.enable <> bb_for_body2.io.Out(8)


  binaryOp_sub16.io.enable <> bb_for_body2.io.Out(9)


  binaryOp_add417.io.enable <> bb_for_body2.io.Out(10)


  binaryOp_mul518.io.enable <> bb_for_body2.io.Out(11)


  binaryOp_sub619.io.enable <> bb_for_body2.io.Out(12)


  binaryOp_mul720.io.enable <> bb_for_body2.io.Out(13)


  binaryOp_mul821.io.enable <> bb_for_body2.io.Out(14)


  br_22.io.enable <> bb_for_body2.io.Out(15)


  const13.io.enable <> bb_for_cond_cleanup113.io.Out(0)

  const14.io.enable <> bb_for_cond_cleanup113.io.Out(1)

  binaryOp_inc11123.io.enable <> bb_for_cond_cleanup113.io.Out(2)


  icmp_exitcond29624.io.enable <> bb_for_cond_cleanup113.io.Out(3)


  br_25.io.enable <> bb_for_cond_cleanup113.io.Out(4)


  const15.io.enable <> bb_for_body124.io.Out(0)

  const16.io.enable <> bb_for_body124.io.Out(1)

  const17.io.enable <> bb_for_body124.io.Out(2)

  const18.io.enable <> bb_for_body124.io.Out(3)

  const19.io.enable <> bb_for_body124.io.Out(4)

  const20.io.enable <> bb_for_body124.io.Out(5)

  const21.io.enable <> bb_for_body124.io.Out(6)

  const22.io.enable <> bb_for_body124.io.Out(7)

  const23.io.enable <> bb_for_body124.io.Out(8)

  const24.io.enable <> bb_for_body124.io.Out(9)

  const25.io.enable <> bb_for_body124.io.Out(10)

  phi_conv_s1_x_029426.io.enable <> bb_for_body124.io.Out(11)


  binaryOp_add1327.io.enable <> bb_for_body124.io.Out(12)


  Gep_arrayidx28.io.enable <> bb_for_body124.io.Out(13)


  ld_29.io.enable <> bb_for_body124.io.Out(14)


  ld_30.io.enable <> bb_for_body124.io.Out(15)


  binaryOp_add1531.io.enable <> bb_for_body124.io.Out(16)


  binaryOp_mul1632.io.enable <> bb_for_body124.io.Out(17)


  binaryOp_sub1733.io.enable <> bb_for_body124.io.Out(18)


  Gep_arrayidx1834.io.enable <> bb_for_body124.io.Out(19)


  ld_35.io.enable <> bb_for_body124.io.Out(20)


  sextconv36.io.enable <> bb_for_body124.io.Out(21)


  binaryOp_mul1937.io.enable <> bb_for_body124.io.Out(22)


  binaryOp_add2038.io.enable <> bb_for_body124.io.Out(23)


  st_39.io.enable <> bb_for_body124.io.Out(24)


  ld_40.io.enable <> bb_for_body124.io.Out(25)


  binaryOp_add2841.io.enable <> bb_for_body124.io.Out(26)


  Gep_arrayidx2942.io.enable <> bb_for_body124.io.Out(27)


  ld_43.io.enable <> bb_for_body124.io.Out(28)


  sextconv3044.io.enable <> bb_for_body124.io.Out(29)


  binaryOp_mul3145.io.enable <> bb_for_body124.io.Out(30)


  binaryOp_add3246.io.enable <> bb_for_body124.io.Out(31)


  st_47.io.enable <> bb_for_body124.io.Out(32)


  ld_48.io.enable <> bb_for_body124.io.Out(33)


  binaryOp_add4049.io.enable <> bb_for_body124.io.Out(34)


  Gep_arrayidx4150.io.enable <> bb_for_body124.io.Out(35)


  ld_51.io.enable <> bb_for_body124.io.Out(36)


  sextconv4252.io.enable <> bb_for_body124.io.Out(37)


  binaryOp_mul4353.io.enable <> bb_for_body124.io.Out(38)


  binaryOp_add4454.io.enable <> bb_for_body124.io.Out(39)


  st_55.io.enable <> bb_for_body124.io.Out(40)


  ld_56.io.enable <> bb_for_body124.io.Out(41)


  binaryOp_mul4957.io.enable <> bb_for_body124.io.Out(42)


  binaryOp_add5058.io.enable <> bb_for_body124.io.Out(43)


  Gep_arrayidx5159.io.enable <> bb_for_body124.io.Out(44)


  ld_60.io.enable <> bb_for_body124.io.Out(45)


  sextconv5261.io.enable <> bb_for_body124.io.Out(46)


  binaryOp_mul5362.io.enable <> bb_for_body124.io.Out(47)


  binaryOp_add5463.io.enable <> bb_for_body124.io.Out(48)


  st_64.io.enable <> bb_for_body124.io.Out(49)


  ld_65.io.enable <> bb_for_body124.io.Out(50)


  binaryOp_add6166.io.enable <> bb_for_body124.io.Out(51)


  Gep_arrayidx6267.io.enable <> bb_for_body124.io.Out(52)


  ld_68.io.enable <> bb_for_body124.io.Out(53)


  sextconv6369.io.enable <> bb_for_body124.io.Out(54)


  binaryOp_mul6470.io.enable <> bb_for_body124.io.Out(55)


  binaryOp_add6571.io.enable <> bb_for_body124.io.Out(56)


  st_72.io.enable <> bb_for_body124.io.Out(57)


  ld_73.io.enable <> bb_for_body124.io.Out(58)


  binaryOp_add7274.io.enable <> bb_for_body124.io.Out(59)


  Gep_arrayidx7375.io.enable <> bb_for_body124.io.Out(60)


  ld_76.io.enable <> bb_for_body124.io.Out(61)


  sextconv7477.io.enable <> bb_for_body124.io.Out(62)


  binaryOp_mul7578.io.enable <> bb_for_body124.io.Out(63)


  binaryOp_add7679.io.enable <> bb_for_body124.io.Out(64)


  st_80.io.enable <> bb_for_body124.io.Out(65)


  ld_81.io.enable <> bb_for_body124.io.Out(66)


  binaryOp_add8282.io.enable <> bb_for_body124.io.Out(67)


  Gep_arrayidx8383.io.enable <> bb_for_body124.io.Out(68)


  ld_84.io.enable <> bb_for_body124.io.Out(69)


  sextconv8485.io.enable <> bb_for_body124.io.Out(70)


  binaryOp_mul8586.io.enable <> bb_for_body124.io.Out(71)


  binaryOp_add8687.io.enable <> bb_for_body124.io.Out(72)


  st_88.io.enable <> bb_for_body124.io.Out(73)


  ld_89.io.enable <> bb_for_body124.io.Out(74)


  binaryOp_add9390.io.enable <> bb_for_body124.io.Out(75)


  Gep_arrayidx9491.io.enable <> bb_for_body124.io.Out(76)


  ld_92.io.enable <> bb_for_body124.io.Out(77)


  sextconv9593.io.enable <> bb_for_body124.io.Out(78)


  binaryOp_mul9694.io.enable <> bb_for_body124.io.Out(79)


  binaryOp_add9795.io.enable <> bb_for_body124.io.Out(80)


  st_96.io.enable <> bb_for_body124.io.Out(81)


  ld_97.io.enable <> bb_for_body124.io.Out(82)


  binaryOp_add10498.io.enable <> bb_for_body124.io.Out(83)


  Gep_arrayidx10599.io.enable <> bb_for_body124.io.Out(84)


  ld_100.io.enable <> bb_for_body124.io.Out(85)


  sextconv106101.io.enable <> bb_for_body124.io.Out(86)


  binaryOp_mul107102.io.enable <> bb_for_body124.io.Out(87)


  binaryOp_add108103.io.enable <> bb_for_body124.io.Out(88)


  st_104.io.enable <> bb_for_body124.io.Out(89)


  binaryOp_inc105.io.enable <> bb_for_body124.io.Out(90)


  icmp_exitcond106.io.enable <> bb_for_body124.io.Out(91)


  br_107.io.enable <> bb_for_body124.io.Out(92)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  phi_conv_s1_y_029512.io.Mask <> bb_for_body2.io.MaskBB(0)

  phi_conv_s1_x_029426.io.Mask <> bb_for_body124.io.MaskBB(0)



  /* ================================================================== *
   *                   PRINT ALLOCA OFFSET                              *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */

  MemCtrl.io.ReadIn(0) <> ld_29.io.memReq

  ld_29.io.memResp <> MemCtrl.io.ReadOut(0)

  MemCtrl.io.ReadIn(1) <> ld_30.io.memReq

  ld_30.io.memResp <> MemCtrl.io.ReadOut(1)

  MemCtrl.io.ReadIn(2) <> ld_35.io.memReq

  ld_35.io.memResp <> MemCtrl.io.ReadOut(2)

//  MemCtrl.io.WriteIn(0) <> st_39.io.memReq
//
//  st_39.io.memResp <> MemCtrl.io.WriteOut(0)

  MemCtrl.io.WriteIn(0) <> DontCare
  MemCtrl.io.WriteOut(0) <> DontCare

  st_39.io.memReq.ready := true.B
  st_39.io.memResp.valid := true.B
  st_39.io.memResp.RouteID := 0.U
  st_39.io.memResp.done := true.B

  MemCtrl.io.ReadIn(3) <> ld_40.io.memReq

  ld_40.io.memResp <> MemCtrl.io.ReadOut(3)

  MemCtrl.io.ReadIn(4) <> ld_43.io.memReq

  ld_43.io.memResp <> MemCtrl.io.ReadOut(4)

//  MemCtrl.io.WriteIn(1) <> st_47.io.memReq
//
//  st_47.io.memResp <> MemCtrl.io.WriteOut(1)

  MemCtrl.io.WriteIn(1) <> DontCare
  MemCtrl.io.WriteOut(1) <> DontCare

  st_47.io.memReq.ready := true.B
  st_47.io.memResp.valid := true.B
  st_47.io.memResp.RouteID := 0.U
  st_47.io.memResp.done := true.B

  MemCtrl.io.ReadIn(5) <> ld_48.io.memReq

  ld_48.io.memResp <> MemCtrl.io.ReadOut(5)

  MemCtrl.io.ReadIn(6) <> ld_51.io.memReq

  ld_51.io.memResp <> MemCtrl.io.ReadOut(6)

//  MemCtrl.io.WriteIn(2) <> st_55.io.memReq
//
//  st_55.io.memResp <> MemCtrl.io.WriteOut(2)

  MemCtrl.io.WriteIn(2) <> DontCare
  MemCtrl.io.WriteOut(2) <> DontCare

  st_55.io.memReq.ready := true.B
  st_55.io.memResp.valid := true.B
  st_55.io.memResp.RouteID := 0.U
  st_55.io.memResp.done := true.B

  MemCtrl.io.ReadIn(7) <> ld_56.io.memReq

  ld_56.io.memResp <> MemCtrl.io.ReadOut(7)

  MemCtrl.io.ReadIn(8) <> ld_60.io.memReq

  ld_60.io.memResp <> MemCtrl.io.ReadOut(8)

//  MemCtrl.io.WriteIn(3) <> st_64.io.memReq
//
//  st_64.io.memResp <> MemCtrl.io.WriteOut(3)

  MemCtrl.io.WriteIn(3) <> DontCare
  MemCtrl.io.WriteOut(3) <> DontCare

  st_64.io.memReq.ready := true.B
  st_64.io.memResp.valid := true.B
  st_64.io.memResp.RouteID := 0.U
  st_64.io.memResp.done := true.B

  MemCtrl.io.ReadIn(9) <> ld_65.io.memReq

  ld_65.io.memResp <> MemCtrl.io.ReadOut(9)

  MemCtrl.io.ReadIn(10) <> ld_68.io.memReq

  ld_68.io.memResp <> MemCtrl.io.ReadOut(10)

//  MemCtrl.io.WriteIn(4) <> st_72.io.memReq
//
//  st_72.io.memResp <> MemCtrl.io.WriteOut(4)
  MemCtrl.io.WriteIn(4) <> DontCare
  MemCtrl.io.WriteOut(4) <> DontCare

  st_72.io.memReq.ready := true.B
  st_72.io.memResp.valid := true.B
  st_72.io.memResp.RouteID := 0.U
  st_72.io.memResp.done := true.B

  MemCtrl.io.ReadIn(11) <> ld_73.io.memReq

  ld_73.io.memResp <> MemCtrl.io.ReadOut(11)

  MemCtrl.io.ReadIn(12) <> ld_76.io.memReq

  ld_76.io.memResp <> MemCtrl.io.ReadOut(12)

//  MemCtrl.io.WriteIn(5) <> st_80.io.memReq
//
//  st_80.io.memResp <> MemCtrl.io.WriteOut(5)

  MemCtrl.io.WriteIn(5) <> DontCare
  MemCtrl.io.WriteOut(5) <> DontCare

  st_80.io.memReq.ready := true.B
  st_80.io.memResp.valid := true.B
  st_80.io.memResp.RouteID := 0.U
  st_80.io.memResp.done := true.B

  MemCtrl.io.ReadIn(13) <> ld_81.io.memReq

  ld_81.io.memResp <> MemCtrl.io.ReadOut(13)

  MemCtrl.io.ReadIn(14) <> ld_84.io.memReq

  ld_84.io.memResp <> MemCtrl.io.ReadOut(14)

//  MemCtrl.io.WriteIn(6) <> st_88.io.memReq
//
//  st_88.io.memResp <> MemCtrl.io.WriteOut(6)
  MemCtrl.io.WriteIn(6) <> DontCare
  MemCtrl.io.WriteOut(6) <> DontCare

  st_88.io.memReq.ready := true.B
  st_88.io.memResp.valid := true.B
  st_88.io.memResp.RouteID := 0.U
  st_88.io.memResp.done := true.B

  MemCtrl.io.ReadIn(15) <> ld_89.io.memReq

  ld_89.io.memResp <> MemCtrl.io.ReadOut(15)

  MemCtrl.io.ReadIn(16) <> ld_92.io.memReq

  ld_92.io.memResp <> MemCtrl.io.ReadOut(16)

//  MemCtrl.io.WriteIn(7) <> st_96.io.memReq
//
//  st_96.io.memResp <> MemCtrl.io.WriteOut(7)

  MemCtrl.io.WriteIn(7) <> DontCare
  MemCtrl.io.WriteOut(7) <> DontCare

  st_96.io.memReq.ready := true.B
  st_96.io.memResp.valid := true.B
  st_96.io.memResp.RouteID := 0.U
  st_96.io.memResp.done := true.B

  MemCtrl.io.ReadIn(17) <> ld_97.io.memReq

  ld_97.io.memResp <> MemCtrl.io.ReadOut(17)

  MemCtrl.io.ReadIn(18) <> ld_100.io.memReq

  ld_100.io.memResp <> MemCtrl.io.ReadOut(18)

//  MemCtrl.io.WriteIn(8) <> st_104.io.memReq
//
//  st_104.io.memResp <> MemCtrl.io.WriteOut(8)

  MemCtrl.io.WriteIn(8) <> DontCare
  MemCtrl.io.WriteOut(8) <> DontCare

  st_104.io.memReq.ready := true.B
  st_104.io.memResp.valid := true.B
  st_104.io.memResp.RouteID := 0.U
  st_104.io.memResp.done := true.B



  /* ================================================================== *
   *                   PRINT SHARED CONNECTIONS                         *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  Gep_arrayidx242.io.idx(0) <> const0.io.Out

  Gep_arrayidx363.io.idx(0) <> const1.io.Out

  Gep_arrayidx484.io.idx(0) <> const2.io.Out

  Gep_arrayidx585.io.idx(0) <> const3.io.Out

  Gep_arrayidx696.io.idx(0) <> const4.io.Out

  Gep_arrayidx807.io.idx(0) <> const5.io.Out

  Gep_arrayidx908.io.idx(0) <> const6.io.Out

  Gep_arrayidx1019.io.idx(0) <> const7.io.Out

  phi_conv_s1_y_029512.io.InData(0) <> const8.io.Out

  binaryOp_mul113.io.RightIO <> const9.io.Out

  binaryOp_add214.io.RightIO <> const10.io.Out

  binaryOp_add417.io.RightIO <> const11.io.Out

  binaryOp_mul821.io.RightIO <> const12.io.Out

  binaryOp_inc11123.io.RightIO <> const13.io.Out

  icmp_exitcond29624.io.RightIO <> const14.io.Out

  phi_conv_s1_x_029426.io.InData(0) <> const15.io.Out

  binaryOp_mul1632.io.RightIO <> const16.io.Out

  binaryOp_add2841.io.RightIO <> const17.io.Out

  binaryOp_add4049.io.RightIO <> const18.io.Out

  binaryOp_mul4957.io.RightIO <> const19.io.Out

  binaryOp_add6166.io.RightIO <> const20.io.Out

  binaryOp_add7274.io.RightIO <> const21.io.Out

  binaryOp_add9390.io.RightIO <> const22.io.Out

  binaryOp_add10498.io.RightIO <> const23.io.Out

  binaryOp_inc105.io.RightIO <> const24.io.Out

  icmp_exitcond106.io.RightIO <> const25.io.Out

  binaryOp_add1.io.LeftIO <> binaryOp_mul0.io.Out(0)

  binaryOp_mul113.io.LeftIO <> phi_conv_s1_y_029512.io.Out(0)

  binaryOp_mul720.io.LeftIO <> phi_conv_s1_y_029512.io.Out(1)

  binaryOp_mul821.io.LeftIO <> phi_conv_s1_y_029512.io.Out(2)

  binaryOp_inc11123.io.LeftIO <> phi_conv_s1_y_029512.io.Out(3)

  binaryOp_add214.io.LeftIO <> binaryOp_mul113.io.Out(0)

  binaryOp_add417.io.LeftIO <> binaryOp_mul113.io.Out(1)

  binaryOp_mul315.io.LeftIO <> binaryOp_add214.io.Out(0)

  binaryOp_sub16.io.LeftIO <> binaryOp_mul315.io.Out(0)

  binaryOp_mul518.io.LeftIO <> binaryOp_add417.io.Out(0)

  binaryOp_sub619.io.LeftIO <> binaryOp_mul518.io.Out(0)

  icmp_exitcond29624.io.LeftIO <> binaryOp_inc11123.io.Out(1)

  br_25.io.CmpIO <> icmp_exitcond29624.io.Out(0)

  binaryOp_add1327.io.LeftIO <> phi_conv_s1_x_029426.io.Out(0)

  binaryOp_add1531.io.LeftIO <> phi_conv_s1_x_029426.io.Out(1)

  binaryOp_mul4957.io.LeftIO <> phi_conv_s1_x_029426.io.Out(2)

  binaryOp_inc105.io.LeftIO <> phi_conv_s1_x_029426.io.Out(3)

  Gep_arrayidx28.io.idx(0) <> binaryOp_add1327.io.Out(0)

  ld_29.io.GepAddr <> Gep_arrayidx28.io.Out(0)

  st_39.io.GepAddr <> Gep_arrayidx28.io.Out(1)

  st_47.io.GepAddr <> Gep_arrayidx28.io.Out(2)

  st_55.io.GepAddr <> Gep_arrayidx28.io.Out(3)

  st_64.io.GepAddr <> Gep_arrayidx28.io.Out(4)

  st_72.io.GepAddr <> Gep_arrayidx28.io.Out(5)

  st_80.io.GepAddr <> Gep_arrayidx28.io.Out(6)

  st_88.io.GepAddr <> Gep_arrayidx28.io.Out(7)

  st_96.io.GepAddr <> Gep_arrayidx28.io.Out(8)

  st_104.io.GepAddr <> Gep_arrayidx28.io.Out(9)

  binaryOp_add2038.io.RightIO <> ld_29.io.Out(0)

  binaryOp_mul1937.io.LeftIO <> ld_30.io.Out(0)

  binaryOp_mul1632.io.LeftIO <> binaryOp_add1531.io.Out(0)

  binaryOp_sub1733.io.LeftIO <> binaryOp_mul1632.io.Out(0)

  Gep_arrayidx1834.io.idx(0) <> binaryOp_sub1733.io.Out(0)

  binaryOp_add2841.io.LeftIO <> binaryOp_sub1733.io.Out(1)

  binaryOp_add4049.io.LeftIO <> binaryOp_sub1733.io.Out(2)

  ld_35.io.GepAddr <> Gep_arrayidx1834.io.Out(0)

  sextconv36.io.Input <> ld_35.io.Out(0)

  binaryOp_mul1937.io.RightIO <> sextconv36.io.Out(0)

  binaryOp_add2038.io.LeftIO <> binaryOp_mul1937.io.Out(0)

  st_39.io.inData <> binaryOp_add2038.io.Out(0)

  binaryOp_add3246.io.RightIO <> binaryOp_add2038.io.Out(1)

  binaryOp_mul3145.io.LeftIO <> ld_40.io.Out(0)

  Gep_arrayidx2942.io.idx(0) <> binaryOp_add2841.io.Out(0)

  ld_43.io.GepAddr <> Gep_arrayidx2942.io.Out(0)

  sextconv3044.io.Input <> ld_43.io.Out(0)

  binaryOp_mul3145.io.RightIO <> sextconv3044.io.Out(0)

  binaryOp_add3246.io.LeftIO <> binaryOp_mul3145.io.Out(0)

  st_47.io.inData <> binaryOp_add3246.io.Out(0)

  binaryOp_add4454.io.RightIO <> binaryOp_add3246.io.Out(1)

  binaryOp_mul4353.io.LeftIO <> ld_48.io.Out(0)

  Gep_arrayidx4150.io.idx(0) <> binaryOp_add4049.io.Out(0)

  ld_51.io.GepAddr <> Gep_arrayidx4150.io.Out(0)

  sextconv4252.io.Input <> ld_51.io.Out(0)

  binaryOp_mul4353.io.RightIO <> sextconv4252.io.Out(0)

  binaryOp_add4454.io.LeftIO <> binaryOp_mul4353.io.Out(0)

  st_55.io.inData <> binaryOp_add4454.io.Out(0)

  binaryOp_add5463.io.RightIO <> binaryOp_add4454.io.Out(1)

  binaryOp_mul5362.io.LeftIO <> ld_56.io.Out(0)

  binaryOp_add5058.io.LeftIO <> binaryOp_mul4957.io.Out(0)

  binaryOp_add8282.io.LeftIO <> binaryOp_mul4957.io.Out(1)

  Gep_arrayidx5159.io.idx(0) <> binaryOp_add5058.io.Out(0)

  binaryOp_add6166.io.LeftIO <> binaryOp_add5058.io.Out(1)

  binaryOp_add7274.io.LeftIO <> binaryOp_add5058.io.Out(2)

  ld_60.io.GepAddr <> Gep_arrayidx5159.io.Out(0)

  sextconv5261.io.Input <> ld_60.io.Out(0)

  binaryOp_mul5362.io.RightIO <> sextconv5261.io.Out(0)

  binaryOp_add5463.io.LeftIO <> binaryOp_mul5362.io.Out(0)

  st_64.io.inData <> binaryOp_add5463.io.Out(0)

  binaryOp_add6571.io.RightIO <> binaryOp_add5463.io.Out(1)

  binaryOp_mul6470.io.LeftIO <> ld_65.io.Out(0)

  Gep_arrayidx6267.io.idx(0) <> binaryOp_add6166.io.Out(0)

  ld_68.io.GepAddr <> Gep_arrayidx6267.io.Out(0)

  sextconv6369.io.Input <> ld_68.io.Out(0)

  binaryOp_mul6470.io.RightIO <> sextconv6369.io.Out(0)

  binaryOp_add6571.io.LeftIO <> binaryOp_mul6470.io.Out(0)

  st_72.io.inData <> binaryOp_add6571.io.Out(0)

  binaryOp_add7679.io.RightIO <> binaryOp_add6571.io.Out(1)

  binaryOp_mul7578.io.LeftIO <> ld_73.io.Out(0)

  Gep_arrayidx7375.io.idx(0) <> binaryOp_add7274.io.Out(0)

  ld_76.io.GepAddr <> Gep_arrayidx7375.io.Out(0)

  sextconv7477.io.Input <> ld_76.io.Out(0)

  binaryOp_mul7578.io.RightIO <> sextconv7477.io.Out(0)

  binaryOp_add7679.io.LeftIO <> binaryOp_mul7578.io.Out(0)

  st_80.io.inData <> binaryOp_add7679.io.Out(0)

  binaryOp_add8687.io.RightIO <> binaryOp_add7679.io.Out(1)

  binaryOp_mul8586.io.LeftIO <> ld_81.io.Out(0)

  Gep_arrayidx8383.io.idx(0) <> binaryOp_add8282.io.Out(0)

  binaryOp_add9390.io.LeftIO <> binaryOp_add8282.io.Out(1)

  binaryOp_add10498.io.LeftIO <> binaryOp_add8282.io.Out(2)

  ld_84.io.GepAddr <> Gep_arrayidx8383.io.Out(0)

  sextconv8485.io.Input <> ld_84.io.Out(0)

  binaryOp_mul8586.io.RightIO <> sextconv8485.io.Out(0)

  binaryOp_add8687.io.LeftIO <> binaryOp_mul8586.io.Out(0)

  st_88.io.inData <> binaryOp_add8687.io.Out(0)

  binaryOp_add9795.io.RightIO <> binaryOp_add8687.io.Out(1)

  binaryOp_mul9694.io.LeftIO <> ld_89.io.Out(0)

  Gep_arrayidx9491.io.idx(0) <> binaryOp_add9390.io.Out(0)

  ld_92.io.GepAddr <> Gep_arrayidx9491.io.Out(0)

  sextconv9593.io.Input <> ld_92.io.Out(0)

  binaryOp_mul9694.io.RightIO <> sextconv9593.io.Out(0)

  binaryOp_add9795.io.LeftIO <> binaryOp_mul9694.io.Out(0)

  st_96.io.inData <> binaryOp_add9795.io.Out(0)

  binaryOp_add108103.io.RightIO <> binaryOp_add9795.io.Out(1)

  binaryOp_mul107102.io.LeftIO <> ld_97.io.Out(0)

  Gep_arrayidx10599.io.idx(0) <> binaryOp_add10498.io.Out(0)

  ld_100.io.GepAddr <> Gep_arrayidx10599.io.Out(0)

  sextconv106101.io.Input <> ld_100.io.Out(0)

  binaryOp_mul107102.io.RightIO <> sextconv106101.io.Out(0)

  binaryOp_add108103.io.LeftIO <> binaryOp_mul107102.io.Out(0)

  st_104.io.inData <> binaryOp_add108103.io.Out(0)

  icmp_exitcond106.io.LeftIO <> binaryOp_inc105.io.Out(1)

  br_107.io.CmpIO <> icmp_exitcond106.io.Out(0)

  Gep_arrayidx242.io.baseAddress <> InputSplitter.io.Out.data.elements("field1")(1)

  Gep_arrayidx363.io.baseAddress <> InputSplitter.io.Out.data.elements("field1")(2)

  Gep_arrayidx484.io.baseAddress <> InputSplitter.io.Out.data.elements("field1")(3)

  Gep_arrayidx585.io.baseAddress <> InputSplitter.io.Out.data.elements("field1")(4)

  Gep_arrayidx696.io.baseAddress <> InputSplitter.io.Out.data.elements("field1")(5)

  Gep_arrayidx807.io.baseAddress <> InputSplitter.io.Out.data.elements("field1")(6)

  Gep_arrayidx908.io.baseAddress <> InputSplitter.io.Out.data.elements("field1")(7)

  Gep_arrayidx1019.io.baseAddress <> InputSplitter.io.Out.data.elements("field1")(8)

  binaryOp_add1.io.RightIO <> InputSplitter.io.Out.data.elements("field3")(0)

  binaryOp_mul0.io.RightIO <> InputSplitter.io.Out.data.elements("field4")(0)

  binaryOp_mul0.io.LeftIO <> InputSplitter.io.Out.data.elements("field5")(1)

  st_39.io.Out(0).ready := true.B

  st_47.io.Out(0).ready := true.B

  st_55.io.Out(0).ready := true.B

  st_64.io.Out(0).ready := true.B

  st_72.io.Out(0).ready := true.B

  st_80.io.Out(0).ready := true.B

  st_88.io.Out(0).ready := true.B

  st_96.io.Out(0).ready := true.B

  st_104.io.Out(0).ready := true.B



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_11.io.Out

}

import java.io.{File, FileWriter}

object stridedConvHalideTop extends App {
  val dir = new File("RTL/stridedConvHalideTop");
  dir.mkdirs
  implicit val p = new WithAccelConfig
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new stridedConvHalideDF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}
