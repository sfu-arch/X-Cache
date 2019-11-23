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

abstract class upSampleHalideDFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32))))
    val MemResp = Flipped(Valid(new MemResp))
    val MemReq = Decoupled(new MemReq)
    val out = Decoupled(new Call(List()))
  })
}

class upSampleHalideDF(implicit p: Parameters) extends upSampleHalideDFIO()(p) {


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  val MemCtrl = Module(new UnifiedController(ID = 0, Size = 32, NReads = 1, NWrites = 1)
  (WControl = new WriteMemoryController(NumOps = 1, BaseSize = 2, NumEntries = 2))
  (RControl = new ReadMemoryController(NumOps = 1, BaseSize = 2, NumEntries = 2))
  (RWArbiter = new ReadWriteArbiter()))

  io.MemReq <> MemCtrl.io.MemReq
  MemCtrl.io.MemResp <> io.MemResp

  val InputSplitter = Module(new SplitCallNew(List(1, 1, 1, 1, 1, 1, 1, 3, 1, 3, 1, 1, 3, 1, 1)))
  InputSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val Loop_0 = Module(new LoopBlockNode(NumIns = List(1, 1, 1, 1, 1, 1, 1, 2, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 0))

  val Loop_1 = Module(new LoopBlockNode(NumIns = List(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 1))

  val Loop_2 = Module(new LoopBlockNode(NumIns = List(2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 2))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_entry0 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 3, BID = 0))

  val bb_for_body_lr_ph1 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 5, BID = 1))

  val bb_for_cond_cleanup_loopexit2 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 2))

  val bb_for_cond_cleanup3 = Module(new BasicBlockNoMaskFastNode(NumInputs = 2, NumOuts = 1, BID = 3))

  val bb_for_body4 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 2, NumPhi = 1, BID = 4))

  val bb_for_body15_lr_ph5 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 5, BID = 5))

  val bb_for_cond_cleanup14_loopexit6 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 6))

  val bb_for_cond_cleanup147 = Module(new BasicBlockNoMaskFastNode(NumInputs = 2, NumOuts = 4, BID = 7))

  val bb_for_body158 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 2, NumPhi = 1, BID = 8))

  val bb_for_body24_lr_ph9 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 7, BID = 9))

  val bb_for_cond_cleanup23_loopexit10 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 10))

  val bb_for_cond_cleanup2311 = Module(new BasicBlockNoMaskFastNode(NumInputs = 2, NumOuts = 4, BID = 11))

  val bb_for_body2412 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 17, NumPhi = 1, BID = 12))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  %add7 = add i32 %_34, %_33, !dbg !11620, !UID !11621
  val binaryOp_add70 = Module(new ComputeNode(NumOuts = 2, ID = 0, opCode = "add")(sign = false, Debug = false))

  //  %cmp87 = icmp ugt i32 %add7, %_33, !dbg !11622, !UID !11623
  val icmp_cmp871 = Module(new ComputeNode(NumOuts = 1, ID = 1, opCode = "ugt")(sign = false, Debug = false))

  //  br i1 %cmp87, label %for.body.lr.ph, label %for.cond.cleanup, !dbg !11624, !UID !11625, !BB_UID !11626
  val br_2 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 2))

  //  %add12 = add i32 %_31, %_30, !UID !11627
  val binaryOp_add123 = Module(new ComputeNode(NumOuts = 2, ID = 3, opCode = "add")(sign = false, Debug = false))

  //  %cmp1385 = icmp ugt i32 %add12, %_30, !UID !11628
  val icmp_cmp13854 = Module(new ComputeNode(NumOuts = 1, ID = 4, opCode = "ugt")(sign = false, Debug = false))

  //  %add21 = add i32 %_28, %_27, !UID !11629
  val binaryOp_add215 = Module(new ComputeNode(NumOuts = 2, ID = 5, opCode = "add")(sign = false, Debug = false))

  //  %cmp2283 = icmp ugt i32 %add21, %_27, !UID !11630
  val icmp_cmp22836 = Module(new ComputeNode(NumOuts = 1, ID = 6, opCode = "ugt")(sign = false, Debug = false))

  //  br label %for.body, !dbg !11624, !UID !11631, !BB_UID !11632
  val br_7 = Module(new UBranchNode(ID = 7))

  //  br label %for.cond.cleanup, !dbg !11633
  val br_8 = Module(new UBranchNode(ID = 8))

  //  ret void, !dbg !11633, !UID !11634, !BB_UID !11635
  val ret_9 = Module(new RetNode2(retTypes = List(), ID = 9))

  //  %_output_s0_z.088 = phi i32 [ %_33, %for.body.lr.ph ], [ %inc33, %for.cond.cleanup14 ], !UID !11636
  val phi_output_s0_z_08810 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 3, ID = 10, Res = true))

  //  br i1 %cmp1385, label %for.body15.lr.ph, label %for.cond.cleanup14, !dbg !11640, !UID !11641, !BB_UID !11642
  val br_11 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 11))

  //  %reass.add = sub i32 %_output_s0_z.088, %_19, !UID !11643
  val binaryOp_reass_add12 = Module(new ComputeNode(NumOuts = 1, ID = 12, opCode = "sub")(sign = false, Debug = false))

  //  %reass.mul = mul i32 %reass.add, %_21, !UID !11644
  val binaryOp_reass_mul13 = Module(new ComputeNode(NumOuts = 1, ID = 13, opCode = "mul")(sign = false, Debug = false))

  //  %reass.add79 = sub nuw i32 %_output_s0_z.088, %_33, !UID !11645
  val binaryOp_reass_add7914 = Module(new ComputeNode(NumOuts = 1, ID = 14, opCode = "sub")(sign = false, Debug = false))

  //  %reass.mul80 = mul i32 %reass.add79, %_35, !UID !11646
  val binaryOp_reass_mul8015 = Module(new ComputeNode(NumOuts = 1, ID = 15, opCode = "mul")(sign = false, Debug = false))

  //  br label %for.body15, !dbg !11640, !UID !11647, !BB_UID !11648
  val br_16 = Module(new UBranchNode(ID = 16))

  //  br label %for.cond.cleanup14, !dbg !11649
  val br_17 = Module(new UBranchNode(ID = 17))

  //  %inc33 = add nuw nsw i32 %_output_s0_z.088, 1, !dbg !11649, !UID !11650
  val binaryOp_inc3318 = Module(new ComputeNode(NumOuts = 2, ID = 18, opCode = "add")(sign = false, Debug = false))

  //  %exitcond90 = icmp eq i32 %inc33, %add7, !dbg !11622, !UID !11651
  val icmp_exitcond9019 = Module(new ComputeNode(NumOuts = 1, ID = 19, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond90, label %for.cond.cleanup.loopexit, label %for.body, !dbg !11624, !llvm.loop !11652, !UID !11654, !BB_UID !11655
  val br_20 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 20))

  //  %_output_s0_y.086 = phi i32 [ %_30, %for.body15.lr.ph ], [ %inc30, %for.cond.cleanup23 ], !UID !11656
  val phi_output_s0_y_08621 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 3, ID = 21, Res = true))

  //  br i1 %cmp2283, label %for.body24.lr.ph, label %for.cond.cleanup23, !dbg !11661, !UID !11662, !BB_UID !11663
  val br_22 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 22))

  //  %shr = ashr i32 %_output_s0_y.086, 1, !dbg !11664, !UID !11665
  val binaryOp_shr23 = Module(new ComputeNode(NumOuts = 1, ID = 23, opCode = "ashr")(sign = false, Debug = false))

  //  %reass.add77 = sub i32 %shr, %_16, !UID !11666
  val binaryOp_reass_add7724 = Module(new ComputeNode(NumOuts = 1, ID = 24, opCode = "sub")(sign = false, Debug = false))

  //  %reass.mul78 = mul i32 %reass.add77, %_18, !UID !11667
  val binaryOp_reass_mul7825 = Module(new ComputeNode(NumOuts = 1, ID = 25, opCode = "mul")(sign = false, Debug = false))

  //  %reass.add81 = sub nuw i32 %_output_s0_y.086, %_30, !UID !11668
  val binaryOp_reass_add8126 = Module(new ComputeNode(NumOuts = 1, ID = 26, opCode = "sub")(sign = false, Debug = false))

  //  %reass.mul82 = mul i32 %reass.add81, %_32, !UID !11669
  val binaryOp_reass_mul8227 = Module(new ComputeNode(NumOuts = 1, ID = 27, opCode = "mul")(sign = false, Debug = false))

  //  br label %for.body24, !dbg !11661, !UID !11670, !BB_UID !11671
  val br_28 = Module(new UBranchNode(ID = 28))

  //  br label %for.cond.cleanup23, !dbg !11672
  val br_29 = Module(new UBranchNode(ID = 29))

  //  %inc30 = add nuw nsw i32 %_output_s0_y.086, 1, !dbg !11672, !UID !11673
  val binaryOp_inc3030 = Module(new ComputeNode(NumOuts = 2, ID = 30, opCode = "add")(sign = false, Debug = false))

  //  %exitcond89 = icmp eq i32 %inc30, %add12, !dbg !11674, !UID !11675
  val icmp_exitcond8931 = Module(new ComputeNode(NumOuts = 1, ID = 31, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond89, label %for.cond.cleanup14.loopexit, label %for.body15, !dbg !11640, !llvm.loop !11676, !UID !11678, !BB_UID !11679
  val br_32 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 32))

  //  %_output_s0_x.084 = phi i32 [ %_27, %for.body24.lr.ph ], [ %inc, %for.body24 ], !UID !11680
  val phi_output_s0_x_08433 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 3, ID = 33, Res = true))

  //  %shr25 = ashr i32 %_output_s0_x.084, 1, !dbg !11681, !UID !11682
  val binaryOp_shr2534 = Module(new ComputeNode(NumOuts = 1, ID = 34, opCode = "ashr")(sign = false, Debug = false))

  //  %sub10 = sub i32 %shr25, %_13, !dbg !11684, !UID !11685
  val binaryOp_sub1035 = Module(new ComputeNode(NumOuts = 1, ID = 35, opCode = "sub")(sign = false, Debug = false))

  //  %add17 = add i32 %sub10, %reass.mul, !dbg !11686, !UID !11687
  val binaryOp_add1736 = Module(new ComputeNode(NumOuts = 1, ID = 36, opCode = "add")(sign = false, Debug = false))

  //  %add26 = add i32 %add17, %reass.mul78, !dbg !11688, !UID !11689
  val binaryOp_add2637 = Module(new ComputeNode(NumOuts = 1, ID = 37, opCode = "add")(sign = false, Debug = false))

  //  %arrayidx = getelementptr inbounds i8, i8* %_input, i32 %add26, !dbg !11691, !UID !11692
  val Gep_arrayidx38 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 38)(ElementSize = 1, ArraySize = List()))

  //  %0 = load i8, i8* %arrayidx, align 1, !dbg !11691, !tbaa !11693, !UID !11696
  val ld_39 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 39, RouteID = 0))

  //  %sub = sub nuw i32 %_output_s0_x.084, %_27, !dbg !11698, !UID !11699
  val binaryOp_sub40 = Module(new ComputeNode(NumOuts = 1, ID = 40, opCode = "sub")(sign = false, Debug = false))

  //  %add19 = add i32 %sub, %reass.mul80, !dbg !11700, !UID !11701
  val binaryOp_add1941 = Module(new ComputeNode(NumOuts = 1, ID = 41, opCode = "add")(sign = false, Debug = false))

  //  %add27 = add i32 %add19, %reass.mul82, !dbg !11702, !UID !11703
  val binaryOp_add2742 = Module(new ComputeNode(NumOuts = 1, ID = 42, opCode = "add")(sign = false, Debug = false))

  //  %arrayidx28 = getelementptr inbounds i8, i8* %_output, i32 %add27, !dbg !11705, !UID !11706
  val Gep_arrayidx2843 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 43)(ElementSize = 1, ArraySize = List()))

  //  store i8 %0, i8* %arrayidx28, align 1, !dbg !11707, !tbaa !11693, !UID !11708
  val st_44 = Module(new UnTypStore(NumPredOps = 0, NumSuccOps = 0, ID = 44, RouteID = 0))

  //  %inc = add nuw nsw i32 %_output_s0_x.084, 1, !dbg !11709, !UID !11710
  val binaryOp_inc45 = Module(new ComputeNode(NumOuts = 2, ID = 45, opCode = "add")(sign = false, Debug = false))

  //  %exitcond = icmp eq i32 %inc, %add21, !dbg !11711, !UID !11712
  val icmp_exitcond46 = Module(new ComputeNode(NumOuts = 1, ID = 46, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond, label %for.cond.cleanup23.loopexit, label %for.body24, !dbg !11661, !llvm.loop !11713, !UID !11715, !BB_UID !11716
  val br_47 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 47))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i32 1
  val const0 = Module(new ConstFastNode(value = 1, ID = 0))

  //i32 1
  val const1 = Module(new ConstFastNode(value = 1, ID = 1))

  //i32 1
  val const2 = Module(new ConstFastNode(value = 1, ID = 2))

  //i32 1
  val const3 = Module(new ConstFastNode(value = 1, ID = 3))

  //i32 1
  val const4 = Module(new ConstFastNode(value = 1, ID = 4))



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_entry0.io.predicateIn(0) <> InputSplitter.io.Out.enable

  bb_for_body_lr_ph1.io.predicateIn(0) <> br_2.io.TrueOutput(0)

  bb_for_cond_cleanup3.io.predicateIn(1) <> br_2.io.FalseOutput(0)

  bb_for_cond_cleanup3.io.predicateIn(0) <> br_8.io.Out(0)

  bb_for_body15_lr_ph5.io.predicateIn(0) <> br_11.io.TrueOutput(0)

  bb_for_cond_cleanup147.io.predicateIn(1) <> br_11.io.FalseOutput(0)

  bb_for_cond_cleanup147.io.predicateIn(0) <> br_17.io.Out(0)

  bb_for_body24_lr_ph9.io.predicateIn(0) <> br_22.io.TrueOutput(0)

  bb_for_cond_cleanup2311.io.predicateIn(1) <> br_22.io.FalseOutput(0)

  bb_for_cond_cleanup2311.io.predicateIn(0) <> br_29.io.Out(0)



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE LOOP                     *
   * ================================================================== */

  bb_for_cond_cleanup_loopexit2.io.predicateIn(0) <> Loop_2.io.loopExit(0)

  bb_for_body4.io.predicateIn(1) <> Loop_2.io.activate_loop_start

  bb_for_body4.io.predicateIn(0) <> Loop_2.io.activate_loop_back

  bb_for_cond_cleanup14_loopexit6.io.predicateIn(0) <> Loop_1.io.loopExit(0)

  bb_for_body158.io.predicateIn(1) <> Loop_1.io.activate_loop_start

  bb_for_body158.io.predicateIn(0) <> Loop_1.io.activate_loop_back

  bb_for_cond_cleanup23_loopexit10.io.predicateIn(0) <> Loop_0.io.loopExit(0)

  bb_for_body2412.io.predicateIn(1) <> Loop_0.io.activate_loop_start

  bb_for_body2412.io.predicateIn(0) <> Loop_0.io.activate_loop_back



  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */

  Loop_0.io.enable <> br_28.io.Out(0)

  Loop_0.io.loopBack(0) <> br_47.io.FalseOutput(0)

  Loop_0.io.loopFinish(0) <> br_47.io.TrueOutput(0)

  Loop_1.io.enable <> br_16.io.Out(0)

  Loop_1.io.loopBack(0) <> br_32.io.FalseOutput(0)

  Loop_1.io.loopFinish(0) <> br_32.io.TrueOutput(0)

  Loop_2.io.enable <> br_7.io.Out(0)

  Loop_2.io.loopBack(0) <> br_20.io.FalseOutput(0)

  Loop_2.io.loopFinish(0) <> br_20.io.TrueOutput(0)



  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

  Loop_0.io.InLiveIn(0) <> binaryOp_reass_mul7825.io.Out(0)

  Loop_0.io.InLiveIn(1) <> binaryOp_reass_mul8227.io.Out(0)

  Loop_0.io.InLiveIn(2) <> Loop_1.io.OutLiveIn.elements("field0")(0)

  Loop_0.io.InLiveIn(3) <> Loop_1.io.OutLiveIn.elements("field2")(0)

  Loop_0.io.InLiveIn(4) <> Loop_1.io.OutLiveIn.elements("field3")(0)

  Loop_0.io.InLiveIn(5) <> Loop_1.io.OutLiveIn.elements("field4")(0)

  Loop_0.io.InLiveIn(6) <> Loop_1.io.OutLiveIn.elements("field5")(0)

  Loop_0.io.InLiveIn(7) <> Loop_1.io.OutLiveIn.elements("field6")(0)

  Loop_0.io.InLiveIn(8) <> Loop_1.io.OutLiveIn.elements("field1")(0)

  Loop_1.io.InLiveIn(0) <> binaryOp_reass_mul13.io.Out(0)

  Loop_1.io.InLiveIn(1) <> binaryOp_reass_mul8015.io.Out(0)

  Loop_1.io.InLiveIn(2) <> Loop_2.io.OutLiveIn.elements("field13")(0)

  Loop_1.io.InLiveIn(3) <> Loop_2.io.OutLiveIn.elements("field14")(0)

  Loop_1.io.InLiveIn(4) <> Loop_2.io.OutLiveIn.elements("field12")(0)

  Loop_1.io.InLiveIn(5) <> Loop_2.io.OutLiveIn.elements("field11")(0)

  Loop_1.io.InLiveIn(6) <> Loop_2.io.OutLiveIn.elements("field10")(0)

  Loop_1.io.InLiveIn(7) <> Loop_2.io.OutLiveIn.elements("field6")(0)

  Loop_1.io.InLiveIn(8) <> Loop_2.io.OutLiveIn.elements("field8")(0)

  Loop_1.io.InLiveIn(9) <> Loop_2.io.OutLiveIn.elements("field7")(0)

  Loop_1.io.InLiveIn(10) <> Loop_2.io.OutLiveIn.elements("field9")(0)

  Loop_1.io.InLiveIn(11) <> Loop_2.io.OutLiveIn.elements("field15")(0)

  Loop_1.io.InLiveIn(12) <> Loop_2.io.OutLiveIn.elements("field5")(0)

  Loop_2.io.InLiveIn(0) <> InputSplitter.io.Out.data.elements("field12")(0)

  Loop_2.io.InLiveIn(1) <> icmp_cmp13854.io.Out(0)

  Loop_2.io.InLiveIn(2) <> InputSplitter.io.Out.data.elements("field5")(0)

  Loop_2.io.InLiveIn(3) <> InputSplitter.io.Out.data.elements("field6")(0)

  Loop_2.io.InLiveIn(4) <> InputSplitter.io.Out.data.elements("field14")(0)

  Loop_2.io.InLiveIn(5) <> InputSplitter.io.Out.data.elements("field9")(0)

  Loop_2.io.InLiveIn(6) <> icmp_cmp22836.io.Out(0)

  Loop_2.io.InLiveIn(7) <> InputSplitter.io.Out.data.elements("field3")(0)

  Loop_2.io.InLiveIn(8) <> InputSplitter.io.Out.data.elements("field4")(0)

  Loop_2.io.InLiveIn(9) <> InputSplitter.io.Out.data.elements("field11")(0)

  Loop_2.io.InLiveIn(10) <> InputSplitter.io.Out.data.elements("field7")(0)

  Loop_2.io.InLiveIn(11) <> InputSplitter.io.Out.data.elements("field2")(0)

  Loop_2.io.InLiveIn(12) <> InputSplitter.io.Out.data.elements("field0")(0)

  Loop_2.io.InLiveIn(13) <> InputSplitter.io.Out.data.elements("field1")(0)

  Loop_2.io.InLiveIn(14) <> binaryOp_add215.io.Out(0)

  Loop_2.io.InLiveIn(15) <> binaryOp_add123.io.Out(0)

  Loop_2.io.InLiveIn(16) <> binaryOp_add70.io.Out(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */

  binaryOp_add2637.io.RightIO <> Loop_0.io.OutLiveIn.elements("field0")(0)

  binaryOp_add2742.io.RightIO <> Loop_0.io.OutLiveIn.elements("field1")(0)

  binaryOp_add1736.io.RightIO <> Loop_0.io.OutLiveIn.elements("field2")(0)

  Gep_arrayidx2843.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field3")(0)

  icmp_exitcond46.io.RightIO <> Loop_0.io.OutLiveIn.elements("field4")(0)

  Gep_arrayidx38.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field5")(0)

  binaryOp_sub1035.io.RightIO <> Loop_0.io.OutLiveIn.elements("field6")(0)

  phi_output_s0_x_08433.io.InData(0) <> Loop_0.io.OutLiveIn.elements("field7")(0)

  binaryOp_sub40.io.RightIO <> Loop_0.io.OutLiveIn.elements("field7")(1)

  binaryOp_add1941.io.RightIO <> Loop_0.io.OutLiveIn.elements("field8")(0)

  br_22.io.CmpIO <> Loop_1.io.OutLiveIn.elements("field7")(0)

  binaryOp_reass_mul7825.io.RightIO <> Loop_1.io.OutLiveIn.elements("field8")(0)

  binaryOp_reass_add7724.io.RightIO <> Loop_1.io.OutLiveIn.elements("field9")(0)

  binaryOp_reass_mul8227.io.RightIO <> Loop_1.io.OutLiveIn.elements("field10")(0)

  icmp_exitcond8931.io.RightIO <> Loop_1.io.OutLiveIn.elements("field11")(0)

  phi_output_s0_y_08621.io.InData(0) <> Loop_1.io.OutLiveIn.elements("field12")(0)

  binaryOp_reass_add8126.io.RightIO <> Loop_1.io.OutLiveIn.elements("field12")(1)

  phi_output_s0_z_08810.io.InData(0) <> Loop_2.io.OutLiveIn.elements("field0")(0)

  binaryOp_reass_add7914.io.RightIO <> Loop_2.io.OutLiveIn.elements("field0")(1)

  br_11.io.CmpIO <> Loop_2.io.OutLiveIn.elements("field1")(0)

  binaryOp_reass_add12.io.RightIO <> Loop_2.io.OutLiveIn.elements("field2")(0)

  binaryOp_reass_mul13.io.RightIO <> Loop_2.io.OutLiveIn.elements("field3")(0)

  binaryOp_reass_mul8015.io.RightIO <> Loop_2.io.OutLiveIn.elements("field4")(0)

  icmp_exitcond9019.io.RightIO <> Loop_2.io.OutLiveIn.elements("field16")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP LIVE OUT DEPENDENCIES                       *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP CARRY DEPENDENCIES                          *
   * ================================================================== */

  Loop_0.io.CarryDepenIn(0) <> binaryOp_inc45.io.Out(0)

  Loop_1.io.CarryDepenIn(0) <> binaryOp_inc3030.io.Out(0)

  Loop_2.io.CarryDepenIn(0) <> binaryOp_inc3318.io.Out(0)



  /* ================================================================== *
   *                   LOOP DATA CARRY DEPENDENCIES                     *
   * ================================================================== */

  phi_output_s0_x_08433.io.InData(1) <> Loop_0.io.CarryDepenOut.elements("field0")(0)

  phi_output_s0_y_08621.io.InData(1) <> Loop_1.io.CarryDepenOut.elements("field0")(0)

  phi_output_s0_z_08810.io.InData(1) <> Loop_2.io.CarryDepenOut.elements("field0")(0)



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  binaryOp_add70.io.enable <> bb_entry0.io.Out(0)


  icmp_cmp871.io.enable <> bb_entry0.io.Out(1)


  br_2.io.enable <> bb_entry0.io.Out(2)


  binaryOp_add123.io.enable <> bb_for_body_lr_ph1.io.Out(0)


  icmp_cmp13854.io.enable <> bb_for_body_lr_ph1.io.Out(1)


  binaryOp_add215.io.enable <> bb_for_body_lr_ph1.io.Out(2)


  icmp_cmp22836.io.enable <> bb_for_body_lr_ph1.io.Out(3)


  br_7.io.enable <> bb_for_body_lr_ph1.io.Out(4)


  br_8.io.enable <> bb_for_cond_cleanup_loopexit2.io.Out(0)


  ret_9.io.In.enable <> bb_for_cond_cleanup3.io.Out(0)


  phi_output_s0_z_08810.io.enable <> bb_for_body4.io.Out(0)


  br_11.io.enable <> bb_for_body4.io.Out(1)


  binaryOp_reass_add12.io.enable <> bb_for_body15_lr_ph5.io.Out(0)


  binaryOp_reass_mul13.io.enable <> bb_for_body15_lr_ph5.io.Out(1)


  binaryOp_reass_add7914.io.enable <> bb_for_body15_lr_ph5.io.Out(2)


  binaryOp_reass_mul8015.io.enable <> bb_for_body15_lr_ph5.io.Out(3)


  br_16.io.enable <> bb_for_body15_lr_ph5.io.Out(4)


  br_17.io.enable <> bb_for_cond_cleanup14_loopexit6.io.Out(0)


  const0.io.enable <> bb_for_cond_cleanup147.io.Out(0)

  binaryOp_inc3318.io.enable <> bb_for_cond_cleanup147.io.Out(1)


  icmp_exitcond9019.io.enable <> bb_for_cond_cleanup147.io.Out(2)


  br_20.io.enable <> bb_for_cond_cleanup147.io.Out(3)


  phi_output_s0_y_08621.io.enable <> bb_for_body158.io.Out(0)


  br_22.io.enable <> bb_for_body158.io.Out(1)


  const1.io.enable <> bb_for_body24_lr_ph9.io.Out(0)

  binaryOp_shr23.io.enable <> bb_for_body24_lr_ph9.io.Out(1)


  binaryOp_reass_add7724.io.enable <> bb_for_body24_lr_ph9.io.Out(2)


  binaryOp_reass_mul7825.io.enable <> bb_for_body24_lr_ph9.io.Out(3)


  binaryOp_reass_add8126.io.enable <> bb_for_body24_lr_ph9.io.Out(4)


  binaryOp_reass_mul8227.io.enable <> bb_for_body24_lr_ph9.io.Out(5)


  br_28.io.enable <> bb_for_body24_lr_ph9.io.Out(6)


  br_29.io.enable <> bb_for_cond_cleanup23_loopexit10.io.Out(0)


  const2.io.enable <> bb_for_cond_cleanup2311.io.Out(0)

  binaryOp_inc3030.io.enable <> bb_for_cond_cleanup2311.io.Out(1)


  icmp_exitcond8931.io.enable <> bb_for_cond_cleanup2311.io.Out(2)


  br_32.io.enable <> bb_for_cond_cleanup2311.io.Out(3)


  const3.io.enable <> bb_for_body2412.io.Out(0)

  const4.io.enable <> bb_for_body2412.io.Out(1)

  phi_output_s0_x_08433.io.enable <> bb_for_body2412.io.Out(2)


  binaryOp_shr2534.io.enable <> bb_for_body2412.io.Out(3)


  binaryOp_sub1035.io.enable <> bb_for_body2412.io.Out(4)


  binaryOp_add1736.io.enable <> bb_for_body2412.io.Out(5)


  binaryOp_add2637.io.enable <> bb_for_body2412.io.Out(6)


  Gep_arrayidx38.io.enable <> bb_for_body2412.io.Out(7)


  ld_39.io.enable <> bb_for_body2412.io.Out(8)


  binaryOp_sub40.io.enable <> bb_for_body2412.io.Out(9)


  binaryOp_add1941.io.enable <> bb_for_body2412.io.Out(10)


  binaryOp_add2742.io.enable <> bb_for_body2412.io.Out(11)


  Gep_arrayidx2843.io.enable <> bb_for_body2412.io.Out(12)


  st_44.io.enable <> bb_for_body2412.io.Out(13)


  binaryOp_inc45.io.enable <> bb_for_body2412.io.Out(14)


  icmp_exitcond46.io.enable <> bb_for_body2412.io.Out(15)


  br_47.io.enable <> bb_for_body2412.io.Out(16)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  phi_output_s0_z_08810.io.Mask <> bb_for_body4.io.MaskBB(0)

  phi_output_s0_y_08621.io.Mask <> bb_for_body158.io.MaskBB(0)

  phi_output_s0_x_08433.io.Mask <> bb_for_body2412.io.MaskBB(0)



  /* ================================================================== *
   *                   PRINT ALLOCA OFFSET                              *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */

  MemCtrl.io.ReadIn(0) <> ld_39.io.memReq

  ld_39.io.memResp <> MemCtrl.io.ReadOut(0)

//  MemCtrl.io.WriteIn(0) <> st_44.io.memReq
//
//  st_44.io.memResp <> MemCtrl.io.WriteOut(0)

  MemCtrl.io.WriteIn(0) <> DontCare
  MemCtrl.io.WriteOut(0) <> DontCare

  st_44.io.memReq.ready := true.B
  st_44.io.memResp.valid := true.B
  st_44.io.memResp.RouteID := 0.U
  st_44.io.memResp.done := true.B



  /* ================================================================== *
   *                   PRINT SHARED CONNECTIONS                         *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  binaryOp_inc3318.io.RightIO <> const0.io.Out

  binaryOp_shr23.io.RightIO <> const1.io.Out

  binaryOp_inc3030.io.RightIO <> const2.io.Out

  binaryOp_shr2534.io.RightIO <> const3.io.Out

  binaryOp_inc45.io.RightIO <> const4.io.Out

  icmp_cmp871.io.LeftIO <> binaryOp_add70.io.Out(1)

  br_2.io.CmpIO <> icmp_cmp871.io.Out(0)

  icmp_cmp13854.io.LeftIO <> binaryOp_add123.io.Out(1)

  icmp_cmp22836.io.LeftIO <> binaryOp_add215.io.Out(1)

  binaryOp_reass_add12.io.LeftIO <> phi_output_s0_z_08810.io.Out(0)

  binaryOp_reass_add7914.io.LeftIO <> phi_output_s0_z_08810.io.Out(1)

  binaryOp_inc3318.io.LeftIO <> phi_output_s0_z_08810.io.Out(2)

  binaryOp_reass_mul13.io.LeftIO <> binaryOp_reass_add12.io.Out(0)

  binaryOp_reass_mul8015.io.LeftIO <> binaryOp_reass_add7914.io.Out(0)

  icmp_exitcond9019.io.LeftIO <> binaryOp_inc3318.io.Out(1)

  br_20.io.CmpIO <> icmp_exitcond9019.io.Out(0)

  binaryOp_shr23.io.LeftIO <> phi_output_s0_y_08621.io.Out(0)

  binaryOp_reass_add8126.io.LeftIO <> phi_output_s0_y_08621.io.Out(1)

  binaryOp_inc3030.io.LeftIO <> phi_output_s0_y_08621.io.Out(2)

  binaryOp_reass_add7724.io.LeftIO <> binaryOp_shr23.io.Out(0)

  binaryOp_reass_mul7825.io.LeftIO <> binaryOp_reass_add7724.io.Out(0)

  binaryOp_reass_mul8227.io.LeftIO <> binaryOp_reass_add8126.io.Out(0)

  icmp_exitcond8931.io.LeftIO <> binaryOp_inc3030.io.Out(1)

  br_32.io.CmpIO <> icmp_exitcond8931.io.Out(0)

  binaryOp_shr2534.io.LeftIO <> phi_output_s0_x_08433.io.Out(0)

  binaryOp_sub40.io.LeftIO <> phi_output_s0_x_08433.io.Out(1)

  binaryOp_inc45.io.LeftIO <> phi_output_s0_x_08433.io.Out(2)

  binaryOp_sub1035.io.LeftIO <> binaryOp_shr2534.io.Out(0)

  binaryOp_add1736.io.LeftIO <> binaryOp_sub1035.io.Out(0)

  binaryOp_add2637.io.LeftIO <> binaryOp_add1736.io.Out(0)

  Gep_arrayidx38.io.idx(0) <> binaryOp_add2637.io.Out(0)

  ld_39.io.GepAddr <> Gep_arrayidx38.io.Out(0)

  st_44.io.inData <> ld_39.io.Out(0)

  binaryOp_add1941.io.LeftIO <> binaryOp_sub40.io.Out(0)

  binaryOp_add2742.io.LeftIO <> binaryOp_add1941.io.Out(0)

  Gep_arrayidx2843.io.idx(0) <> binaryOp_add2742.io.Out(0)

  st_44.io.GepAddr <> Gep_arrayidx2843.io.Out(0)

  icmp_exitcond46.io.LeftIO <> binaryOp_inc45.io.Out(1)

  br_47.io.CmpIO <> icmp_exitcond46.io.Out(0)

  binaryOp_add215.io.RightIO <> InputSplitter.io.Out.data.elements("field7")(1)

  icmp_cmp22836.io.RightIO <> InputSplitter.io.Out.data.elements("field7")(2)

  binaryOp_add215.io.LeftIO <> InputSplitter.io.Out.data.elements("field8")(0)

  binaryOp_add123.io.RightIO <> InputSplitter.io.Out.data.elements("field9")(1)

  icmp_cmp13854.io.RightIO <> InputSplitter.io.Out.data.elements("field9")(2)

  binaryOp_add123.io.LeftIO <> InputSplitter.io.Out.data.elements("field10")(0)

  binaryOp_add70.io.RightIO <> InputSplitter.io.Out.data.elements("field12")(1)

  icmp_cmp871.io.RightIO <> InputSplitter.io.Out.data.elements("field12")(2)

  binaryOp_add70.io.LeftIO <> InputSplitter.io.Out.data.elements("field13")(0)

  st_44.io.Out(0).ready := true.B



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_9.io.Out

}

import java.io.{File, FileWriter}

object upSampleHalideTop extends App {
  val dir = new File("RTL/upSampleHalideTop");
  dir.mkdirs
  implicit val p = Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new upSampleHalideDF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}
