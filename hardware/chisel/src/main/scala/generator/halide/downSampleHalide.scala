package dandelion.generator

import chisel3._
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

abstract class downSampleHalideDFIO(implicit val p: Parameters) extends Module with HasAccelParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32, 32, 32, 32, 32, 32, 32, 32, 32))))
    val MemResp = Flipped(Valid(new MemResp))
    val MemReq = Decoupled(new MemReq)
    val out = Decoupled(new Call(List()))
  })
}

class downSampleHalideDF(implicit p: Parameters) extends downSampleHalideDFIO()(p) {


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  val MemCtrl = Module(new UnifiedController(ID = 0, Size = 32, NReads = 1, NWrites = 1)
  (WControl = new WriteMemoryController(NumOps = 1, BaseSize = 2, NumEntries = 2))
  (RControl = new ReadMemoryController(NumOps = 1, BaseSize = 2, NumEntries = 2))
  (RWArbiter = new ReadWriteArbiter()))

  io.MemReq <> MemCtrl.io.MemReq
  MemCtrl.io.MemResp <> io.MemResp

  val InputSplitter = Module(new SplitCallNew(List(1, 1, 1, 1, 1, 1, 1, 1, 1)))
  InputSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val Loop_0 = Module(new LoopBlockNode(NumIns = List(1, 1, 1, 1, 1), NumOuts = List(1), NumCarry = List(1, 1), NumExits = 1, ID = 0))

  val Loop_1 = Module(new LoopBlockNode(NumIns = List(1, 1, 1, 1, 1), NumOuts = List(1), NumCarry = List(1, 1), NumExits = 1, ID = 1))

  val Loop_2 = Module(new LoopBlockNode(NumIns = List(1, 1, 1, 1, 1, 1, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 2))

  val Loop_3 = Module(new LoopBlockNode(NumIns = List(1, 1, 1, 1, 1, 1, 1, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 3))

  val Loop_4 = Module(new LoopBlockNode(NumIns = List(1, 1, 1, 1, 1, 1, 1, 1, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 4))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_entry0 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 0))

  val bb_for_cond_cleanup1 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 1))

  val bb_for_body2 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 6, NumPhi = 1, BID = 2))

  val bb_for_cond_cleanup73 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 5, BID = 3))

  val bb_for_body84 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 8, NumPhi = 1, BID = 4))

  val bb_for_cond_cleanup145 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 5, BID = 5))

  val bb_for_body156 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 6, NumPhi = 1, BID = 6))

  val bb_for_cond_cleanup207 = Module(new BasicBlockNode(NumInputs = 1, NumOuts = 12, NumPhi = 1, BID = 7))

  val bb_for_body218 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 7, NumPhi = 2, BID = 8))

  val bb_for_cond_cleanup279 = Module(new BasicBlockNode(NumInputs = 1, NumOuts = 6, NumPhi = 1, BID = 9))

  val bb_for_body2810 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 15, NumPhi = 2, BID = 10))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  br label %for.body, !dbg !11620, !UID !11621, !BB_UID !11622
  val br_0 = Module(new UBranchNode(ID = 0))

  //  ret void, !dbg !11623, !UID !11624, !BB_UID !11625
  val ret_1 = Module(new RetNode2(retTypes = List(), ID = 1))

  //  %_output_s0_z.098 = phi i32 [ 0, %entry ], [ %inc44, %for.cond.cleanup7 ], !UID !11626
  val phi_output_s0_z_0982 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 3, ID = 2, Res = true))

  //  %mul3 = mul nsw i32 %_output_s0_z.098, %_35, !dbg !11627, !UID !11628
  val binaryOp_mul33 = Module(new ComputeNode(NumOuts = 1, ID = 3, opCode = "mul")(sign = false, Debug = false))

  //  %reass.add = sub i32 %_output_s0_z.098, %_19, !UID !11632
  val binaryOp_reass_add4 = Module(new ComputeNode(NumOuts = 1, ID = 4, opCode = "sub")(sign = false, Debug = false))

  //  %reass.mul = mul i32 %reass.add, %_21, !UID !11633
  val binaryOp_reass_mul5 = Module(new ComputeNode(NumOuts = 1, ID = 5, opCode = "mul")(sign = false, Debug = false))

  //  br label %for.body8, !dbg !11634, !UID !11635, !BB_UID !11636
  val br_6 = Module(new UBranchNode(ID = 6))

  //  %inc44 = add nuw nsw i32 %_output_s0_z.098, 1, !dbg !11637, !UID !11638
  val binaryOp_inc447 = Module(new ComputeNode(NumOuts = 2, ID = 7, opCode = "add")(sign = false, Debug = false))

  //  %exitcond102 = icmp eq i32 %inc44, 4, !dbg !11639, !UID !11640
  val icmp_exitcond1028 = Module(new ComputeNode(NumOuts = 1, ID = 8, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond102, label %for.cond.cleanup, label %for.body, !dbg !11620, !llvm.loop !11641, !UID !11643, !BB_UID !11644
  val br_9 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 9))

  //  %_output_s0_y.097 = phi i32 [ 0, %for.body ], [ %inc41, %for.cond.cleanup14 ], !UID !11645
  val phi_output_s0_y_09710 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 3, ID = 10, Res = true))

  //  %mul9 = shl nuw nsw i32 %_output_s0_y.097, 1, !dbg !11646, !UID !11647
  val binaryOp_mul911 = Module(new ComputeNode(NumOuts = 1, ID = 11, opCode = "shl")(sign = false, Debug = false))

  //  %mul10 = mul nsw i32 %_output_s0_y.097, %_32, !dbg !11649, !UID !11650
  val binaryOp_mul1012 = Module(new ComputeNode(NumOuts = 1, ID = 12, opCode = "mul")(sign = false, Debug = false))

  //  %add11 = add nsw i32 %mul10, %mul3, !dbg !11652, !UID !11653
  val binaryOp_add1113 = Module(new ComputeNode(NumOuts = 1, ID = 13, opCode = "add")(sign = false, Debug = false))

  //  %add22 = sub i32 %mul9, %_16, !UID !11656
  val binaryOp_add2214 = Module(new ComputeNode(NumOuts = 1, ID = 14, opCode = "sub")(sign = false, Debug = false))

  //  br label %for.body15, !dbg !11657, !UID !11658, !BB_UID !11659
  val br_15 = Module(new UBranchNode(ID = 15))

  //  %inc41 = add nuw nsw i32 %_output_s0_y.097, 1, !dbg !11660, !UID !11661
  val binaryOp_inc4116 = Module(new ComputeNode(NumOuts = 2, ID = 16, opCode = "add")(sign = false, Debug = false))

  //  %exitcond101 = icmp eq i32 %inc41, 32, !dbg !11662, !UID !11663
  val icmp_exitcond10117 = Module(new ComputeNode(NumOuts = 1, ID = 17, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond101, label %for.cond.cleanup7, label %for.body8, !dbg !11634, !llvm.loop !11664, !UID !11666, !BB_UID !11667
  val br_18 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 18))

  //  %_output_s0_x.096 = phi i32 [ 0, %for.body8 ], [ %inc38, %for.cond.cleanup20 ], !UID !11668
  val phi_output_s0_x_09619 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 3, ID = 19, Res = true))

  //  %mul16 = shl nuw nsw i32 %_output_s0_x.096, 1, !dbg !11670, !UID !11671
  val binaryOp_mul1620 = Module(new ComputeNode(NumOuts = 1, ID = 20, opCode = "shl")(sign = false, Debug = false))

  //  %sub = sub i32 %mul16, %_13, !UID !11675
  val binaryOp_sub21 = Module(new ComputeNode(NumOuts = 1, ID = 21, opCode = "sub")(sign = false, Debug = false))

  //  br label %for.body21, !dbg !11676, !UID !11677, !BB_UID !11678
  val br_22 = Module(new UBranchNode(ID = 22))

  //  %add30.lcssa.lcssa = phi i32 [ %add30.lcssa, %for.cond.cleanup27 ], !UID !11679
  val phiadd30_lcssa_lcssa23 = Module(new PhiFastNode(NumInputs = 1, NumOutputs = 1, ID = 23, Res = false))

  //  %0 = lshr i32 %add30.lcssa.lcssa, 2, !dbg !11681, !UID !11682
  val binaryOp_24 = Module(new ComputeNode(NumOuts = 1, ID = 24, opCode = "lshr")(sign = false, Debug = false))

  //  %conv34 = trunc i32 %0 to i8, !dbg !11683, !UID !11684
  val truncconv3425 = Module(new TruncNode(NumOuts = 1))

  //  %add35 = add nsw i32 %add11, %_output_s0_x.096, !dbg !11686, !UID !11687
  val binaryOp_add3526 = Module(new ComputeNode(NumOuts = 1, ID = 26, opCode = "add")(sign = false, Debug = false))

  //  %arrayidx36 = getelementptr inbounds i8, i8* %_output, i32 %add35, !dbg !11689, !UID !11690
  val Gep_arrayidx3627 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 27)(ElementSize = 1, ArraySize = List()))

  //  store i8 %conv34, i8* %arrayidx36, align 1, !dbg !11691, !tbaa !11692, !UID !11695
  val st_28 = Module(new UnTypStore(NumPredOps = 0, NumSuccOps = 0, ID = 28, RouteID = 0))

  //  %inc38 = add nuw nsw i32 %_output_s0_x.096, 1, !dbg !11696, !UID !11697
  val binaryOp_inc3829 = Module(new ComputeNode(NumOuts = 2, ID = 29, opCode = "add")(sign = false, Debug = false))

  //  %exitcond100 = icmp eq i32 %inc38, 32, !dbg !11698, !UID !11699
  val icmp_exitcond10030 = Module(new ComputeNode(NumOuts = 1, ID = 30, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond100, label %for.cond.cleanup14, label %for.body15, !dbg !11657, !llvm.loop !11700, !UID !11702, !BB_UID !11703
  val br_31 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 31))

  //  %_avg_pool.095 = phi i32 [ 0, %for.body15 ], [ %add30.lcssa, %for.cond.cleanup27 ], !UID !11704
  val phi_avg_pool_09532 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 1, ID = 32, Res = true))

  //  %_avg_pool_s1_r__y.094 = phi i32 [ 0, %for.body15 ], [ %inc32, %for.cond.cleanup27 ], !UID !11705
  val phi_avg_pool_s1_r__y_09433 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 33, Res = true))

  //  %reass.add90 = add i32 %add22, %_avg_pool_s1_r__y.094, !UID !11709
  val binaryOp_reass_add9034 = Module(new ComputeNode(NumOuts = 1, ID = 34, opCode = "add")(sign = false, Debug = false))

  //  %reass.mul91 = mul i32 %reass.add90, %_18, !UID !11710
  val binaryOp_reass_mul9135 = Module(new ComputeNode(NumOuts = 1, ID = 35, opCode = "mul")(sign = false, Debug = false))

  //  br label %for.body28, !dbg !11711, !UID !11712, !BB_UID !11713
  val br_36 = Module(new UBranchNode(ID = 36))

  //  %add30.lcssa = phi i32 [ %add30, %for.body28 ], !UID !11714
  val phiadd30_lcssa37 = Module(new PhiFastNode(NumInputs = 1, NumOutputs = 2, ID = 37, Res = false))

  //  %inc32 = add nuw nsw i32 %_avg_pool_s1_r__y.094, 1, !dbg !11715, !UID !11716
  val binaryOp_inc3238 = Module(new ComputeNode(NumOuts = 2, ID = 38, opCode = "add")(sign = false, Debug = false))

  //  %exitcond99 = icmp eq i32 %inc32, 2, !dbg !11717, !UID !11718
  val icmp_exitcond9939 = Module(new ComputeNode(NumOuts = 1, ID = 39, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond99, label %for.cond.cleanup20, label %for.body21, !dbg !11676, !llvm.loop !11719, !UID !11721, !BB_UID !11722
  val br_40 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 40))

  //  %_avg_pool.193 = phi i32 [ %_avg_pool.095, %for.body21 ], [ %add30, %for.body28 ], !UID !11723
  val phi_avg_pool_19341 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 1, ID = 41, Res = true))

  //  %_avg_pool_s1_r__x.092 = phi i32 [ 0, %for.body21 ], [ %inc, %for.body28 ], !UID !11724
  val phi_avg_pool_s1_r__x_09242 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 42, Res = true))

  //  %add17 = add i32 %sub, %_avg_pool_s1_r__x.092, !dbg !11726, !UID !11727
  val binaryOp_add1743 = Module(new ComputeNode(NumOuts = 1, ID = 43, opCode = "add")(sign = false, Debug = false))

  //  %add24 = add i32 %add17, %reass.mul, !dbg !11728, !UID !11729
  val binaryOp_add2444 = Module(new ComputeNode(NumOuts = 1, ID = 44, opCode = "add")(sign = false, Debug = false))

  //  %add29 = add i32 %add24, %reass.mul91, !dbg !11730, !UID !11731
  val binaryOp_add2945 = Module(new ComputeNode(NumOuts = 1, ID = 45, opCode = "add")(sign = false, Debug = false))

  //  %arrayidx = getelementptr inbounds i8, i8* %_input, i32 %add29, !dbg !11733, !UID !11734
  val Gep_arrayidx46 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 46)(ElementSize = 1, ArraySize = List()))

  //  %1 = load i8, i8* %arrayidx, align 1, !dbg !11733, !tbaa !11692, !UID !11735
  val ld_47 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 47, RouteID = 0))

  //  %conv = zext i8 %1 to i32, !dbg !11737, !UID !11738
  val sextconv48 = Module(new ZextNode(NumOuts = 1))

  //  %add30 = add nsw i32 %_avg_pool.193, %conv, !dbg !11740, !UID !11741
  val binaryOp_add3049 = Module(new ComputeNode(NumOuts = 2, ID = 49, opCode = "add")(sign = false, Debug = false))

  //  %inc = add nuw nsw i32 %_avg_pool_s1_r__x.092, 1, !dbg !11743, !UID !11744
  val binaryOp_inc50 = Module(new ComputeNode(NumOuts = 2, ID = 50, opCode = "add")(sign = false, Debug = false))

  //  %exitcond = icmp eq i32 %inc, 2, !dbg !11745, !UID !11746
  val icmp_exitcond51 = Module(new ComputeNode(NumOuts = 1, ID = 51, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond, label %for.cond.cleanup27, label %for.body28, !dbg !11711, !llvm.loop !11747, !UID !11749, !BB_UID !11750
  val br_52 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 52))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i32 0
  val const0 = Module(new ConstFastNode(value = 0, ID = 0))

  //i32 1
  val const1 = Module(new ConstFastNode(value = 1, ID = 1))

  //i32 4
  val const2 = Module(new ConstFastNode(value = 4, ID = 2))

  //i32 0
  val const3 = Module(new ConstFastNode(value = 0, ID = 3))

  //i32 1
  val const4 = Module(new ConstFastNode(value = 1, ID = 4))

  //i32 1
  val const5 = Module(new ConstFastNode(value = 1, ID = 5))

  //i32 32
  val const6 = Module(new ConstFastNode(value = 32, ID = 6))

  //i32 0
  val const7 = Module(new ConstFastNode(value = 0, ID = 7))

  //i32 1
  val const8 = Module(new ConstFastNode(value = 1, ID = 8))

  //i32 2
  val const9 = Module(new ConstFastNode(value = 2, ID = 9))

  //i32 1
  val const10 = Module(new ConstFastNode(value = 1, ID = 10))

  //i32 32
  val const11 = Module(new ConstFastNode(value = 32, ID = 11))

  //i32 0
  val const12 = Module(new ConstFastNode(value = 0, ID = 12))

  //i32 0
  val const13 = Module(new ConstFastNode(value = 0, ID = 13))

  //i32 1
  val const14 = Module(new ConstFastNode(value = 1, ID = 14))

  //i32 2
  val const15 = Module(new ConstFastNode(value = 2, ID = 15))

  //i32 0
  val const16 = Module(new ConstFastNode(value = 0, ID = 16))

  //i32 1
  val const17 = Module(new ConstFastNode(value = 1, ID = 17))

  //i32 2
  val const18 = Module(new ConstFastNode(value = 2, ID = 18))



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_entry0.io.predicateIn(0) <> InputSplitter.io.Out.enable



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE LOOP                     *
   * ================================================================== */

  bb_for_cond_cleanup1.io.predicateIn(0) <> Loop_4.io.loopExit(0)

  bb_for_body2.io.predicateIn(1) <> Loop_4.io.activate_loop_start

  bb_for_body2.io.predicateIn(0) <> Loop_4.io.activate_loop_back

  bb_for_cond_cleanup73.io.predicateIn(0) <> Loop_3.io.loopExit(0)

  bb_for_body84.io.predicateIn(1) <> Loop_3.io.activate_loop_start

  bb_for_body84.io.predicateIn(0) <> Loop_3.io.activate_loop_back

  bb_for_cond_cleanup145.io.predicateIn(0) <> Loop_2.io.loopExit(0)

  bb_for_body156.io.predicateIn(1) <> Loop_2.io.activate_loop_start

  bb_for_body156.io.predicateIn(0) <> Loop_2.io.activate_loop_back

  bb_for_cond_cleanup207.io.predicateIn(0) <> Loop_1.io.loopExit(0)

  bb_for_body218.io.predicateIn(1) <> Loop_1.io.activate_loop_start

  bb_for_body218.io.predicateIn(0) <> Loop_1.io.activate_loop_back

  bb_for_cond_cleanup279.io.predicateIn(0) <> Loop_0.io.loopExit(0)

  bb_for_body2810.io.predicateIn(1) <> Loop_0.io.activate_loop_start

  bb_for_body2810.io.predicateIn(0) <> Loop_0.io.activate_loop_back



  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */

  Loop_0.io.enable <> br_36.io.Out(0)

  Loop_0.io.loopBack(0) <> br_52.io.FalseOutput(0)

  Loop_0.io.loopFinish(0) <> br_52.io.TrueOutput(0)

  Loop_1.io.enable <> br_22.io.Out(0)

  Loop_1.io.loopBack(0) <> br_40.io.FalseOutput(0)

  Loop_1.io.loopFinish(0) <> br_40.io.TrueOutput(0)

  Loop_2.io.enable <> br_15.io.Out(0)

  Loop_2.io.loopBack(0) <> br_31.io.FalseOutput(0)

  Loop_2.io.loopFinish(0) <> br_31.io.TrueOutput(0)

  Loop_3.io.enable <> br_6.io.Out(0)

  Loop_3.io.loopBack(0) <> br_18.io.FalseOutput(0)

  Loop_3.io.loopFinish(0) <> br_18.io.TrueOutput(0)

  Loop_4.io.enable <> br_0.io.Out(0)

  Loop_4.io.loopBack(0) <> br_9.io.FalseOutput(0)

  Loop_4.io.loopFinish(0) <> br_9.io.TrueOutput(0)



  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

  Loop_0.io.InLiveIn(0) <> phi_avg_pool_09532.io.Out(0)

  Loop_0.io.InLiveIn(1) <> binaryOp_reass_mul9135.io.Out(0)

  Loop_0.io.InLiveIn(2) <> Loop_1.io.OutLiveIn.elements("field0")(0)

  Loop_0.io.InLiveIn(3) <> Loop_1.io.OutLiveIn.elements("field1")(0)

  Loop_0.io.InLiveIn(4) <> Loop_1.io.OutLiveIn.elements("field2")(0)

  Loop_1.io.InLiveIn(0) <> binaryOp_sub21.io.Out(0)

  Loop_1.io.InLiveIn(1) <> Loop_2.io.OutLiveIn.elements("field2")(0)

  Loop_1.io.InLiveIn(2) <> Loop_2.io.OutLiveIn.elements("field3")(0)

  Loop_1.io.InLiveIn(3) <> Loop_2.io.OutLiveIn.elements("field0")(0)

  Loop_1.io.InLiveIn(4) <> Loop_2.io.OutLiveIn.elements("field4")(0)

  Loop_2.io.InLiveIn(0) <> binaryOp_add2214.io.Out(0)

  Loop_2.io.InLiveIn(1) <> binaryOp_add1113.io.Out(0)

  Loop_2.io.InLiveIn(2) <> Loop_3.io.OutLiveIn.elements("field4")(0)

  Loop_2.io.InLiveIn(3) <> Loop_3.io.OutLiveIn.elements("field1")(0)

  Loop_2.io.InLiveIn(4) <> Loop_3.io.OutLiveIn.elements("field5")(0)

  Loop_2.io.InLiveIn(5) <> Loop_3.io.OutLiveIn.elements("field2")(0)

  Loop_2.io.InLiveIn(6) <> Loop_3.io.OutLiveIn.elements("field3")(0)

  Loop_3.io.InLiveIn(0) <> binaryOp_mul33.io.Out(0)

  Loop_3.io.InLiveIn(1) <> binaryOp_reass_mul5.io.Out(0)

  Loop_3.io.InLiveIn(2) <> Loop_4.io.OutLiveIn.elements("field5")(0)

  Loop_3.io.InLiveIn(3) <> Loop_4.io.OutLiveIn.elements("field8")(0)

  Loop_3.io.InLiveIn(4) <> Loop_4.io.OutLiveIn.elements("field7")(0)

  Loop_3.io.InLiveIn(5) <> Loop_4.io.OutLiveIn.elements("field6")(0)

  Loop_3.io.InLiveIn(6) <> Loop_4.io.OutLiveIn.elements("field3")(0)

  Loop_3.io.InLiveIn(7) <> Loop_4.io.OutLiveIn.elements("field4")(0)

  Loop_4.io.InLiveIn(0) <> InputSplitter.io.Out.data.elements("field8")(0)

  Loop_4.io.InLiveIn(1) <> InputSplitter.io.Out.data.elements("field5")(0)

  Loop_4.io.InLiveIn(2) <> InputSplitter.io.Out.data.elements("field6")(0)

  Loop_4.io.InLiveIn(3) <> InputSplitter.io.Out.data.elements("field7")(0)

  Loop_4.io.InLiveIn(4) <> InputSplitter.io.Out.data.elements("field3")(0)

  Loop_4.io.InLiveIn(5) <> InputSplitter.io.Out.data.elements("field2")(0)

  Loop_4.io.InLiveIn(6) <> InputSplitter.io.Out.data.elements("field4")(0)

  Loop_4.io.InLiveIn(7) <> InputSplitter.io.Out.data.elements("field0")(0)

  Loop_4.io.InLiveIn(8) <> InputSplitter.io.Out.data.elements("field1")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */

  phi_avg_pool_19341.io.InData(0) <> Loop_0.io.OutLiveIn.elements("field0")(0)

  binaryOp_add2945.io.RightIO <> Loop_0.io.OutLiveIn.elements("field1")(0)

  binaryOp_add1743.io.LeftIO <> Loop_0.io.OutLiveIn.elements("field2")(0)

  Gep_arrayidx46.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field3")(0)

  binaryOp_add2444.io.RightIO <> Loop_0.io.OutLiveIn.elements("field4")(0)

  binaryOp_reass_add9034.io.LeftIO <> Loop_1.io.OutLiveIn.elements("field3")(0)

  binaryOp_reass_mul9135.io.RightIO <> Loop_1.io.OutLiveIn.elements("field4")(0)

  binaryOp_add3526.io.LeftIO <> Loop_2.io.OutLiveIn.elements("field1")(0)

  binaryOp_sub21.io.RightIO <> Loop_2.io.OutLiveIn.elements("field5")(0)

  Gep_arrayidx3627.io.baseAddress <> Loop_2.io.OutLiveIn.elements("field6")(0)

  binaryOp_add1113.io.RightIO <> Loop_3.io.OutLiveIn.elements("field0")(0)

  binaryOp_mul1012.io.RightIO <> Loop_3.io.OutLiveIn.elements("field6")(0)

  binaryOp_add2214.io.RightIO <> Loop_3.io.OutLiveIn.elements("field7")(0)

  binaryOp_mul33.io.RightIO <> Loop_4.io.OutLiveIn.elements("field0")(0)

  binaryOp_reass_add4.io.RightIO <> Loop_4.io.OutLiveIn.elements("field1")(0)

  binaryOp_reass_mul5.io.RightIO <> Loop_4.io.OutLiveIn.elements("field2")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */

  Loop_0.io.InLiveOut(0) <> binaryOp_add3049.io.Out(0)

  Loop_1.io.InLiveOut(0) <> phiadd30_lcssa37.io.Out(0)



  /* ================================================================== *
   *                   LOOP LIVE OUT DEPENDENCIES                       *
   * ================================================================== */

  phiadd30_lcssa37.io.InData(0) <> Loop_0.io.OutLiveOut.elements("field0")(0)

  phiadd30_lcssa_lcssa23.io.InData(0) <> Loop_1.io.OutLiveOut.elements("field0")(0)



  /* ================================================================== *
   *                   LOOP CARRY DEPENDENCIES                          *
   * ================================================================== */

  Loop_0.io.CarryDepenIn(0) <> binaryOp_add3049.io.Out(1)

  Loop_0.io.CarryDepenIn(1) <> binaryOp_inc50.io.Out(0)

  Loop_1.io.CarryDepenIn(0) <> phiadd30_lcssa37.io.Out(1)

  Loop_1.io.CarryDepenIn(1) <> binaryOp_inc3238.io.Out(0)

  Loop_2.io.CarryDepenIn(0) <> binaryOp_inc3829.io.Out(0)

  Loop_3.io.CarryDepenIn(0) <> binaryOp_inc4116.io.Out(0)

  Loop_4.io.CarryDepenIn(0) <> binaryOp_inc447.io.Out(0)



  /* ================================================================== *
   *                   LOOP DATA CARRY DEPENDENCIES                     *
   * ================================================================== */

  phi_avg_pool_19341.io.InData(1) <> Loop_0.io.CarryDepenOut.elements("field0")(0)

  phi_avg_pool_s1_r__x_09242.io.InData(1) <> Loop_0.io.CarryDepenOut.elements("field1")(0)

  phi_avg_pool_09532.io.InData(1) <> Loop_1.io.CarryDepenOut.elements("field0")(0)

  phi_avg_pool_s1_r__y_09433.io.InData(1) <> Loop_1.io.CarryDepenOut.elements("field1")(0)

  phi_output_s0_x_09619.io.InData(1) <> Loop_2.io.CarryDepenOut.elements("field0")(0)

  phi_output_s0_y_09710.io.InData(1) <> Loop_3.io.CarryDepenOut.elements("field0")(0)

  phi_output_s0_z_0982.io.InData(1) <> Loop_4.io.CarryDepenOut.elements("field0")(0)



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  br_0.io.enable <> bb_entry0.io.Out(0)


  ret_1.io.In.enable <> bb_for_cond_cleanup1.io.Out(0)


  const0.io.enable <> bb_for_body2.io.Out(0)

  phi_output_s0_z_0982.io.enable <> bb_for_body2.io.Out(1)


  binaryOp_mul33.io.enable <> bb_for_body2.io.Out(2)


  binaryOp_reass_add4.io.enable <> bb_for_body2.io.Out(3)


  binaryOp_reass_mul5.io.enable <> bb_for_body2.io.Out(4)


  br_6.io.enable <> bb_for_body2.io.Out(5)


  const1.io.enable <> bb_for_cond_cleanup73.io.Out(0)

  const2.io.enable <> bb_for_cond_cleanup73.io.Out(1)

  binaryOp_inc447.io.enable <> bb_for_cond_cleanup73.io.Out(2)


  icmp_exitcond1028.io.enable <> bb_for_cond_cleanup73.io.Out(3)


  br_9.io.enable <> bb_for_cond_cleanup73.io.Out(4)


  const3.io.enable <> bb_for_body84.io.Out(0)

  const4.io.enable <> bb_for_body84.io.Out(1)

  phi_output_s0_y_09710.io.enable <> bb_for_body84.io.Out(2)


  binaryOp_mul911.io.enable <> bb_for_body84.io.Out(3)


  binaryOp_mul1012.io.enable <> bb_for_body84.io.Out(4)


  binaryOp_add1113.io.enable <> bb_for_body84.io.Out(5)


  binaryOp_add2214.io.enable <> bb_for_body84.io.Out(6)


  br_15.io.enable <> bb_for_body84.io.Out(7)


  const5.io.enable <> bb_for_cond_cleanup145.io.Out(0)

  const6.io.enable <> bb_for_cond_cleanup145.io.Out(1)

  binaryOp_inc4116.io.enable <> bb_for_cond_cleanup145.io.Out(2)


  icmp_exitcond10117.io.enable <> bb_for_cond_cleanup145.io.Out(3)


  br_18.io.enable <> bb_for_cond_cleanup145.io.Out(4)


  const7.io.enable <> bb_for_body156.io.Out(0)

  const8.io.enable <> bb_for_body156.io.Out(1)

  phi_output_s0_x_09619.io.enable <> bb_for_body156.io.Out(2)


  binaryOp_mul1620.io.enable <> bb_for_body156.io.Out(3)


  binaryOp_sub21.io.enable <> bb_for_body156.io.Out(4)


  br_22.io.enable <> bb_for_body156.io.Out(5)


  const9.io.enable <> bb_for_cond_cleanup207.io.Out(0)

  const10.io.enable <> bb_for_cond_cleanup207.io.Out(1)

  const11.io.enable <> bb_for_cond_cleanup207.io.Out(2)

  phiadd30_lcssa_lcssa23.io.enable <> bb_for_cond_cleanup207.io.Out(3)


  binaryOp_24.io.enable <> bb_for_cond_cleanup207.io.Out(4)


  truncconv3425.io.enable <> bb_for_cond_cleanup207.io.Out(5)


  binaryOp_add3526.io.enable <> bb_for_cond_cleanup207.io.Out(6)


  Gep_arrayidx3627.io.enable <> bb_for_cond_cleanup207.io.Out(7)


  st_28.io.enable <> bb_for_cond_cleanup207.io.Out(8)


  binaryOp_inc3829.io.enable <> bb_for_cond_cleanup207.io.Out(9)


  icmp_exitcond10030.io.enable <> bb_for_cond_cleanup207.io.Out(10)


  br_31.io.enable <> bb_for_cond_cleanup207.io.Out(11)


  const12.io.enable <> bb_for_body218.io.Out(0)

  const13.io.enable <> bb_for_body218.io.Out(1)

  phi_avg_pool_09532.io.enable <> bb_for_body218.io.Out(2)


  phi_avg_pool_s1_r__y_09433.io.enable <> bb_for_body218.io.Out(3)


  binaryOp_reass_add9034.io.enable <> bb_for_body218.io.Out(4)


  binaryOp_reass_mul9135.io.enable <> bb_for_body218.io.Out(5)


  br_36.io.enable <> bb_for_body218.io.Out(6)


  const14.io.enable <> bb_for_cond_cleanup279.io.Out(0)

  const15.io.enable <> bb_for_cond_cleanup279.io.Out(1)

  phiadd30_lcssa37.io.enable <> bb_for_cond_cleanup279.io.Out(2)


  binaryOp_inc3238.io.enable <> bb_for_cond_cleanup279.io.Out(3)


  icmp_exitcond9939.io.enable <> bb_for_cond_cleanup279.io.Out(4)


  br_40.io.enable <> bb_for_cond_cleanup279.io.Out(5)


  const16.io.enable <> bb_for_body2810.io.Out(0)

  const17.io.enable <> bb_for_body2810.io.Out(1)

  const18.io.enable <> bb_for_body2810.io.Out(2)

  phi_avg_pool_19341.io.enable <> bb_for_body2810.io.Out(3)


  phi_avg_pool_s1_r__x_09242.io.enable <> bb_for_body2810.io.Out(4)


  binaryOp_add1743.io.enable <> bb_for_body2810.io.Out(5)


  binaryOp_add2444.io.enable <> bb_for_body2810.io.Out(6)


  binaryOp_add2945.io.enable <> bb_for_body2810.io.Out(7)


  Gep_arrayidx46.io.enable <> bb_for_body2810.io.Out(8)


  ld_47.io.enable <> bb_for_body2810.io.Out(9)


  sextconv48.io.enable <> bb_for_body2810.io.Out(10)


  binaryOp_add3049.io.enable <> bb_for_body2810.io.Out(11)


  binaryOp_inc50.io.enable <> bb_for_body2810.io.Out(12)


  icmp_exitcond51.io.enable <> bb_for_body2810.io.Out(13)


  br_52.io.enable <> bb_for_body2810.io.Out(14)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  phi_output_s0_z_0982.io.Mask <> bb_for_body2.io.MaskBB(0)

  phi_output_s0_y_09710.io.Mask <> bb_for_body84.io.MaskBB(0)

  phi_output_s0_x_09619.io.Mask <> bb_for_body156.io.MaskBB(0)

  phiadd30_lcssa_lcssa23.io.Mask <> bb_for_cond_cleanup207.io.MaskBB(0)

  phi_avg_pool_09532.io.Mask <> bb_for_body218.io.MaskBB(0)

  phi_avg_pool_s1_r__y_09433.io.Mask <> bb_for_body218.io.MaskBB(1)

  phiadd30_lcssa37.io.Mask <> bb_for_cond_cleanup279.io.MaskBB(0)

  phi_avg_pool_19341.io.Mask <> bb_for_body2810.io.MaskBB(0)

  phi_avg_pool_s1_r__x_09242.io.Mask <> bb_for_body2810.io.MaskBB(1)



  /* ================================================================== *
   *                   PRINT ALLOCA OFFSET                              *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */

  //MemCtrl.io.WriteIn(0) <> st_28.io.memReq

  //st_28.io.memResp <> MemCtrl.io.WriteOut(0)
  MemCtrl.io.WriteIn(0) <> DontCare
  MemCtrl.io.WriteOut(0) <> DontCare

  st_28.io.memReq.ready := true.B
  st_28.io.memResp.valid := true.B
  st_28.io.memResp.RouteID := 0.U
  st_28.io.memResp.done := true.B


  MemCtrl.io.ReadIn(0) <> ld_47.io.memReq

  ld_47.io.memResp <> MemCtrl.io.ReadOut(0)



  /* ================================================================== *
   *                   PRINT SHARED CONNECTIONS                         *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  phi_output_s0_z_0982.io.InData(0) <> const0.io.Out

  binaryOp_inc447.io.RightIO <> const1.io.Out

  icmp_exitcond1028.io.RightIO <> const2.io.Out

  phi_output_s0_y_09710.io.InData(0) <> const3.io.Out

  binaryOp_mul911.io.RightIO <> const4.io.Out

  binaryOp_inc4116.io.RightIO <> const5.io.Out

  icmp_exitcond10117.io.RightIO <> const6.io.Out

  phi_output_s0_x_09619.io.InData(0) <> const7.io.Out

  binaryOp_mul1620.io.RightIO <> const8.io.Out

  binaryOp_24.io.RightIO <> const9.io.Out

  binaryOp_inc3829.io.RightIO <> const10.io.Out

  icmp_exitcond10030.io.RightIO <> const11.io.Out

  phi_avg_pool_09532.io.InData(0) <> const12.io.Out

  phi_avg_pool_s1_r__y_09433.io.InData(0) <> const13.io.Out

  binaryOp_inc3238.io.RightIO <> const14.io.Out

  icmp_exitcond9939.io.RightIO <> const15.io.Out

  phi_avg_pool_s1_r__x_09242.io.InData(0) <> const16.io.Out

  binaryOp_inc50.io.RightIO <> const17.io.Out

  icmp_exitcond51.io.RightIO <> const18.io.Out

  binaryOp_mul33.io.LeftIO <> phi_output_s0_z_0982.io.Out(0)

  binaryOp_reass_add4.io.LeftIO <> phi_output_s0_z_0982.io.Out(1)

  binaryOp_inc447.io.LeftIO <> phi_output_s0_z_0982.io.Out(2)

  binaryOp_reass_mul5.io.LeftIO <> binaryOp_reass_add4.io.Out(0)

  icmp_exitcond1028.io.LeftIO <> binaryOp_inc447.io.Out(1)

  br_9.io.CmpIO <> icmp_exitcond1028.io.Out(0)

  binaryOp_mul911.io.LeftIO <> phi_output_s0_y_09710.io.Out(0)

  binaryOp_mul1012.io.LeftIO <> phi_output_s0_y_09710.io.Out(1)

  binaryOp_inc4116.io.LeftIO <> phi_output_s0_y_09710.io.Out(2)

  binaryOp_add2214.io.LeftIO <> binaryOp_mul911.io.Out(0)

  binaryOp_add1113.io.LeftIO <> binaryOp_mul1012.io.Out(0)

  icmp_exitcond10117.io.LeftIO <> binaryOp_inc4116.io.Out(1)

  br_18.io.CmpIO <> icmp_exitcond10117.io.Out(0)

  binaryOp_mul1620.io.LeftIO <> phi_output_s0_x_09619.io.Out(0)

  binaryOp_add3526.io.RightIO <> phi_output_s0_x_09619.io.Out(1)

  binaryOp_inc3829.io.LeftIO <> phi_output_s0_x_09619.io.Out(2)

  binaryOp_sub21.io.LeftIO <> binaryOp_mul1620.io.Out(0)

  binaryOp_24.io.LeftIO <> phiadd30_lcssa_lcssa23.io.Out(0)

  truncconv3425.io.Input <> binaryOp_24.io.Out(0)

  st_28.io.inData <> truncconv3425.io.Out(0)

  Gep_arrayidx3627.io.idx(0) <> binaryOp_add3526.io.Out(0)

  st_28.io.GepAddr <> Gep_arrayidx3627.io.Out(0)

  icmp_exitcond10030.io.LeftIO <> binaryOp_inc3829.io.Out(1)

  br_31.io.CmpIO <> icmp_exitcond10030.io.Out(0)

  binaryOp_reass_add9034.io.RightIO <> phi_avg_pool_s1_r__y_09433.io.Out(0)

  binaryOp_inc3238.io.LeftIO <> phi_avg_pool_s1_r__y_09433.io.Out(1)

  binaryOp_reass_mul9135.io.LeftIO <> binaryOp_reass_add9034.io.Out(0)

  icmp_exitcond9939.io.LeftIO <> binaryOp_inc3238.io.Out(1)

  br_40.io.CmpIO <> icmp_exitcond9939.io.Out(0)

  binaryOp_add3049.io.LeftIO <> phi_avg_pool_19341.io.Out(0)

  binaryOp_add1743.io.RightIO <> phi_avg_pool_s1_r__x_09242.io.Out(0)

  binaryOp_inc50.io.LeftIO <> phi_avg_pool_s1_r__x_09242.io.Out(1)

  binaryOp_add2444.io.LeftIO <> binaryOp_add1743.io.Out(0)

  binaryOp_add2945.io.LeftIO <> binaryOp_add2444.io.Out(0)

  Gep_arrayidx46.io.idx(0) <> binaryOp_add2945.io.Out(0)

  ld_47.io.GepAddr <> Gep_arrayidx46.io.Out(0)

  sextconv48.io.Input <> ld_47.io.Out(0)

  binaryOp_add3049.io.RightIO <> sextconv48.io.Out(0)

  icmp_exitcond51.io.LeftIO <> binaryOp_inc50.io.Out(1)

  br_52.io.CmpIO <> icmp_exitcond51.io.Out(0)

  st_28.io.Out(0).ready := true.B



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_1.io.Out

}

import java.io.{File, FileWriter}

object downSampleHalideTop extends App {
  val dir = new File("RTL/downSampleHalideTop");
  dir.mkdirs
  implicit val p = new WithAccelConfig
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new downSampleHalideDF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}
