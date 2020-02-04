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

abstract class conv3x3HalideDFIO(implicit val p: Parameters) extends Module with HasAccelParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32, 32, 32, 32, 32, 32))))
    val MemResp = Flipped(Valid(new MemResp))
    val MemReq = Decoupled(new MemReq)
    val out = Decoupled(new Call(List()))
  })
}

class conv3x3HalideDF(implicit p: Parameters) extends conv3x3HalideDFIO()(p) {


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  val MemCtrl = Module(new UnifiedController(ID = 0, Size = 32, NReads = 3, NWrites = 1)
  (WControl = new WriteMemoryController(NumOps = 1, BaseSize = 2, NumEntries = 2))
  (RControl = new ReadMemoryController(NumOps = 3, BaseSize = 2, NumEntries = 2))
  (RWArbiter = new ReadWriteArbiter()))

  io.MemReq <> MemCtrl.io.MemReq
  MemCtrl.io.MemResp <> io.MemResp

  val InputSplitter = Module(new SplitCallNew(List(1, 1, 1, 1, 1, 1)))
  InputSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val Loop_0 = Module(new LoopBlockNode(NumIns = List(1, 1, 1, 1, 2, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 0))

  val Loop_1 = Module(new LoopBlockNode(NumIns = List(1, 1, 1, 1, 1, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 1))

  val Loop_2 = Module(new LoopBlockNode(NumIns = List(1, 1, 1, 1, 1, 1, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 2))

  val Loop_3 = Module(new LoopBlockNode(NumIns = List(1, 1, 1, 1, 1, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 3))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_entry0 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 0))

  val bb_for_cond_cleanup1 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 1))

  val bb_for_body2 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 6, NumPhi = 1, BID = 2))

  val bb_for_cond_cleanup33 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 5, BID = 3))

  val bb_for_body44 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 6, NumPhi = 1, BID = 4))

  val bb_for_cond_cleanup95 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 5, BID = 5))

  val bb_for_body106 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 7, NumPhi = 1, BID = 6))

  val bb_for_cond_cleanup177 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 5, BID = 7))

  val bb_for_body188 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 19, NumPhi = 1, BID = 8))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  br label %for.body, !dbg !11601, !UID !11602, !BB_UID !11603
  val br_0 = Module(new UBranchNode(ID = 0))

  //  ret void, !dbg !11604, !UID !11605, !BB_UID !11606
  val ret_1 = Module(new RetNode2(retTypes = List(), ID = 1))

  //  %_conv_s1_y.073 = phi i32 [ 0, %entry ], [ %inc33, %for.cond.cleanup3 ], !UID !11607
  val phi_conv_s1_y_0732 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 3, ID = 2, Res = true))

  //  %mul5 = mul nuw nsw i32 %_conv_s1_y.073, 62, !UID !11609
  val binaryOp_mul53 = Module(new ComputeNode(NumOuts = 1, ID = 3, opCode = "mul")(sign = false, Debug = false))

  //  %add12 = sub i32 %_conv_s1_y.073, %_16, !UID !11610
  val binaryOp_add124 = Module(new ComputeNode(NumOuts = 1, ID = 4, opCode = "sub")(sign = false, Debug = false))

  //  br label %for.body4, !dbg !11611, !UID !11612, !BB_UID !11613
  val br_5 = Module(new UBranchNode(ID = 5))

  //  %inc33 = add nuw nsw i32 %_conv_s1_y.073, 1, !dbg !11614, !UID !11615
  val binaryOp_inc336 = Module(new ComputeNode(NumOuts = 2, ID = 6, opCode = "add")(sign = false, Debug = false))

  //  %exitcond76 = icmp eq i32 %inc33, 62, !dbg !11616, !UID !11617
  val icmp_exitcond767 = Module(new ComputeNode(NumOuts = 1, ID = 7, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond76, label %for.cond.cleanup, label %for.body, !dbg !11601, !llvm.loop !11618, !UID !11620, !BB_UID !11621
  val br_8 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 8))

  //  %_conv_s1_x.072 = phi i32 [ 0, %for.body ], [ %inc30, %for.cond.cleanup9 ], !UID !11622
  val phi_conv_s1_x_0729 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 3, ID = 9, Res = true))

  //  %add6 = add nuw nsw i32 %_conv_s1_x.072, %mul5, !dbg !11625, !UID !11626
  val binaryOp_add610 = Module(new ComputeNode(NumOuts = 1, ID = 10, opCode = "add")(sign = false, Debug = false))

  //  %arrayidx = getelementptr inbounds i32, i32* %_conv, i32 %add6, !UID !11629
  val Gep_arrayidx11 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 11)(ElementSize = 4, ArraySize = List()))

  //  %sub = sub i32 %_conv_s1_x.072, %_13, !UID !11630
  val binaryOp_sub12 = Module(new ComputeNode(NumOuts = 1, ID = 12, opCode = "sub")(sign = false, Debug = false))

  //  br label %for.body10, !dbg !11631, !UID !11632, !BB_UID !11633
  val br_13 = Module(new UBranchNode(ID = 13))

  //  %inc30 = add nuw nsw i32 %_conv_s1_x.072, 1, !dbg !11634, !UID !11635
  val binaryOp_inc3014 = Module(new ComputeNode(NumOuts = 2, ID = 14, opCode = "add")(sign = false, Debug = false))

  //  %exitcond75 = icmp eq i32 %inc30, 62, !dbg !11636, !UID !11637
  val icmp_exitcond7515 = Module(new ComputeNode(NumOuts = 1, ID = 15, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond75, label %for.cond.cleanup3, label %for.body4, !dbg !11611, !llvm.loop !11638, !UID !11640, !BB_UID !11641
  val br_16 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 16))

  //  %_conv_s1_r__y.071 = phi i32 [ 0, %for.body4 ], [ %inc27, %for.cond.cleanup17 ], !UID !11642
  val phi_conv_s1_r__y_07117 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 3, ID = 17, Res = true))

  //  %mul11 = mul nuw nsw i32 %_conv_s1_r__y.071, 3, !dbg !11643, !UID !11644
  val binaryOp_mul1118 = Module(new ComputeNode(NumOuts = 1, ID = 18, opCode = "mul")(sign = false, Debug = false))

  //  %reass.add = add i32 %add12, %_conv_s1_r__y.071, !UID !11649
  val binaryOp_reass_add19 = Module(new ComputeNode(NumOuts = 1, ID = 19, opCode = "add")(sign = false, Debug = false))

  //  %reass.mul = mul i32 %reass.add, %_18, !UID !11650
  val binaryOp_reass_mul20 = Module(new ComputeNode(NumOuts = 1, ID = 20, opCode = "mul")(sign = false, Debug = false))

  //  br label %for.body18, !dbg !11651, !UID !11652, !BB_UID !11653
  val br_21 = Module(new UBranchNode(ID = 21))

  //  %inc27 = add nuw nsw i32 %_conv_s1_r__y.071, 1, !dbg !11654, !UID !11655
  val binaryOp_inc2722 = Module(new ComputeNode(NumOuts = 2, ID = 22, opCode = "add")(sign = false, Debug = false))

  //  %exitcond74 = icmp eq i32 %inc27, 3, !dbg !11656, !UID !11657
  val icmp_exitcond7423 = Module(new ComputeNode(NumOuts = 1, ID = 23, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond74, label %for.cond.cleanup9, label %for.body10, !dbg !11631, !llvm.loop !11658, !UID !11660, !BB_UID !11661
  val br_24 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 24))

  //  %_conv_s1_r__x.070 = phi i32 [ 0, %for.body10 ], [ %inc, %for.body18 ], !UID !11662
  val phi_conv_s1_r__x_07025 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 3, ID = 25, Res = true))

  //  %0 = load i32, i32* %arrayidx, align 4, !dbg !11663, !tbaa !11664, !UID !11668
  val ld_26 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 26, RouteID = 0))

  //  %add19 = add nuw nsw i32 %_conv_s1_r__x.070, %mul11, !dbg !11670, !UID !11671
  val binaryOp_add1927 = Module(new ComputeNode(NumOuts = 1, ID = 27, opCode = "add")(sign = false, Debug = false))

  //  %arrayidx20 = getelementptr inbounds i32, i32* %_kernel, i32 %add19, !dbg !11673, !UID !11674
  val Gep_arrayidx2028 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 28)(ElementSize = 4, ArraySize = List()))

  //  %1 = load i32, i32* %arrayidx20, align 4, !dbg !11673, !tbaa !11664, !UID !11675
  val ld_29 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 29, RouteID = 1))

  //  %add14 = add i32 %sub, %_conv_s1_r__x.070, !dbg !11677, !UID !11678
  val binaryOp_add1430 = Module(new ComputeNode(NumOuts = 1, ID = 30, opCode = "add")(sign = false, Debug = false))

  //  %add21 = add i32 %add14, %reass.mul, !dbg !11679, !UID !11680
  val binaryOp_add2131 = Module(new ComputeNode(NumOuts = 1, ID = 31, opCode = "add")(sign = false, Debug = false))

  //  %arrayidx22 = getelementptr inbounds i8, i8* %_input, i32 %add21, !dbg !11682, !UID !11683
  val Gep_arrayidx2232 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 32)(ElementSize = 1, ArraySize = List()))

  //  %2 = load i8, i8* %arrayidx22, align 1, !dbg !11682, !tbaa !11684, !UID !11685
  val ld_33 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 33, RouteID = 2))

  //  %conv = zext i8 %2 to i32, !dbg !11687, !UID !11688
  val sextconv34 = Module(new ZextNode(NumOuts = 1))

  //  %mul23 = mul nsw i32 %1, %conv, !dbg !11690, !UID !11691
  val binaryOp_mul2335 = Module(new ComputeNode(NumOuts = 1, ID = 35, opCode = "mul")(sign = false, Debug = false))

  //  %add24 = add nsw i32 %mul23, %0, !dbg !11693, !UID !11694
  val binaryOp_add2436 = Module(new ComputeNode(NumOuts = 1, ID = 36, opCode = "add")(sign = false, Debug = false))

  //  store i32 %add24, i32* %arrayidx, align 4, !dbg !11696, !tbaa !11664, !UID !11697
  val st_37 = Module(new UnTypStore(NumPredOps = 0, NumSuccOps = 0, ID = 37, RouteID = 0))

  //  %inc = add nuw nsw i32 %_conv_s1_r__x.070, 1, !dbg !11698, !UID !11699
  val binaryOp_inc38 = Module(new ComputeNode(NumOuts = 2, ID = 38, opCode = "add")(sign = false, Debug = false))

  //  %exitcond = icmp eq i32 %inc, 3, !dbg !11700, !UID !11701
  val icmp_exitcond39 = Module(new ComputeNode(NumOuts = 1, ID = 39, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond, label %for.cond.cleanup17, label %for.body18, !dbg !11651, !llvm.loop !11702, !UID !11704, !BB_UID !11705
  val br_40 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 40))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i32 0
  val const0 = Module(new ConstFastNode(value = 0, ID = 0))

  //i32 62
  val const1 = Module(new ConstFastNode(value = 62, ID = 1))

  //i32 1
  val const2 = Module(new ConstFastNode(value = 1, ID = 2))

  //i32 62
  val const3 = Module(new ConstFastNode(value = 62, ID = 3))

  //i32 0
  val const4 = Module(new ConstFastNode(value = 0, ID = 4))

  //i32 1
  val const5 = Module(new ConstFastNode(value = 1, ID = 5))

  //i32 62
  val const6 = Module(new ConstFastNode(value = 62, ID = 6))

  //i32 0
  val const7 = Module(new ConstFastNode(value = 0, ID = 7))

  //i32 3
  val const8 = Module(new ConstFastNode(value = 3, ID = 8))

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



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE LOOP                     *
   * ================================================================== */

  bb_for_cond_cleanup1.io.predicateIn(0) <> Loop_3.io.loopExit(0)

  bb_for_body2.io.predicateIn(1) <> Loop_3.io.activate_loop_start

  bb_for_body2.io.predicateIn(0) <> Loop_3.io.activate_loop_back

  bb_for_cond_cleanup33.io.predicateIn(0) <> Loop_2.io.loopExit(0)

  bb_for_body44.io.predicateIn(1) <> Loop_2.io.activate_loop_start

  bb_for_body44.io.predicateIn(0) <> Loop_2.io.activate_loop_back

  bb_for_cond_cleanup95.io.predicateIn(0) <> Loop_1.io.loopExit(0)

  bb_for_body106.io.predicateIn(1) <> Loop_1.io.activate_loop_start

  bb_for_body106.io.predicateIn(0) <> Loop_1.io.activate_loop_back

  bb_for_cond_cleanup177.io.predicateIn(0) <> Loop_0.io.loopExit(0)

  bb_for_body188.io.predicateIn(1) <> Loop_0.io.activate_loop_start

  bb_for_body188.io.predicateIn(0) <> Loop_0.io.activate_loop_back



  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */

  Loop_0.io.enable <> br_21.io.Out(0)

  Loop_0.io.loopBack(0) <> br_40.io.FalseOutput(0)

  Loop_0.io.loopFinish(0) <> br_40.io.TrueOutput(0)

  Loop_1.io.enable <> br_13.io.Out(0)

  Loop_1.io.loopBack(0) <> br_24.io.FalseOutput(0)

  Loop_1.io.loopFinish(0) <> br_24.io.TrueOutput(0)

  Loop_2.io.enable <> br_5.io.Out(0)

  Loop_2.io.loopBack(0) <> br_16.io.FalseOutput(0)

  Loop_2.io.loopFinish(0) <> br_16.io.TrueOutput(0)

  Loop_3.io.enable <> br_0.io.Out(0)

  Loop_3.io.loopBack(0) <> br_8.io.FalseOutput(0)

  Loop_3.io.loopFinish(0) <> br_8.io.TrueOutput(0)



  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

  Loop_0.io.InLiveIn(0) <> binaryOp_mul1118.io.Out(0)

  Loop_0.io.InLiveIn(1) <> binaryOp_reass_mul20.io.Out(0)

  Loop_0.io.InLiveIn(2) <> Loop_1.io.OutLiveIn.elements("field2")(0)

  Loop_0.io.InLiveIn(3) <> Loop_1.io.OutLiveIn.elements("field3")(0)

  Loop_0.io.InLiveIn(4) <> Loop_1.io.OutLiveIn.elements("field0")(0)

  Loop_0.io.InLiveIn(5) <> Loop_1.io.OutLiveIn.elements("field1")(0)

  Loop_1.io.InLiveIn(0) <> Gep_arrayidx11.io.Out(0)

  Loop_1.io.InLiveIn(1) <> binaryOp_sub12.io.Out(0)

  Loop_1.io.InLiveIn(2) <> Loop_2.io.OutLiveIn.elements("field2")(0)

  Loop_1.io.InLiveIn(3) <> Loop_2.io.OutLiveIn.elements("field3")(0)

  Loop_1.io.InLiveIn(4) <> Loop_2.io.OutLiveIn.elements("field4")(0)

  Loop_1.io.InLiveIn(5) <> Loop_2.io.OutLiveIn.elements("field1")(0)

  Loop_2.io.InLiveIn(0) <> binaryOp_mul53.io.Out(0)

  Loop_2.io.InLiveIn(1) <> binaryOp_add124.io.Out(0)

  Loop_2.io.InLiveIn(2) <> Loop_3.io.OutLiveIn.elements("field4")(0)

  Loop_2.io.InLiveIn(3) <> Loop_3.io.OutLiveIn.elements("field5")(0)

  Loop_2.io.InLiveIn(4) <> Loop_3.io.OutLiveIn.elements("field3")(0)

  Loop_2.io.InLiveIn(5) <> Loop_3.io.OutLiveIn.elements("field1")(0)

  Loop_2.io.InLiveIn(6) <> Loop_3.io.OutLiveIn.elements("field2")(0)

  Loop_3.io.InLiveIn(0) <> InputSplitter.io.Out.data.elements("field4")(0)

  Loop_3.io.InLiveIn(1) <> InputSplitter.io.Out.data.elements("field2")(0)

  Loop_3.io.InLiveIn(2) <> InputSplitter.io.Out.data.elements("field3")(0)

  Loop_3.io.InLiveIn(3) <> InputSplitter.io.Out.data.elements("field5")(0)

  Loop_3.io.InLiveIn(4) <> InputSplitter.io.Out.data.elements("field1")(0)

  Loop_3.io.InLiveIn(5) <> InputSplitter.io.Out.data.elements("field0")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */

  binaryOp_add1927.io.RightIO <> Loop_0.io.OutLiveIn.elements("field0")(0)

  binaryOp_add2131.io.RightIO <> Loop_0.io.OutLiveIn.elements("field1")(0)

  Gep_arrayidx2028.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field2")(0)

  Gep_arrayidx2232.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field3")(0)

  ld_26.io.GepAddr <> Loop_0.io.OutLiveIn.elements("field4")(0)

  st_37.io.GepAddr <> Loop_0.io.OutLiveIn.elements("field4")(1)

  binaryOp_add1430.io.LeftIO <> Loop_0.io.OutLiveIn.elements("field5")(0)

  binaryOp_reass_mul20.io.RightIO <> Loop_1.io.OutLiveIn.elements("field4")(0)

  binaryOp_reass_add19.io.LeftIO <> Loop_1.io.OutLiveIn.elements("field5")(0)

  binaryOp_add610.io.RightIO <> Loop_2.io.OutLiveIn.elements("field0")(0)

  Gep_arrayidx11.io.baseAddress <> Loop_2.io.OutLiveIn.elements("field5")(0)

  binaryOp_sub12.io.RightIO <> Loop_2.io.OutLiveIn.elements("field6")(0)

  binaryOp_add124.io.RightIO <> Loop_3.io.OutLiveIn.elements("field0")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP LIVE OUT DEPENDENCIES                       *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP CARRY DEPENDENCIES                          *
   * ================================================================== */

  Loop_0.io.CarryDepenIn(0) <> binaryOp_inc38.io.Out(0)

  Loop_1.io.CarryDepenIn(0) <> binaryOp_inc2722.io.Out(0)

  Loop_2.io.CarryDepenIn(0) <> binaryOp_inc3014.io.Out(0)

  Loop_3.io.CarryDepenIn(0) <> binaryOp_inc336.io.Out(0)



  /* ================================================================== *
   *                   LOOP DATA CARRY DEPENDENCIES                     *
   * ================================================================== */

  phi_conv_s1_r__x_07025.io.InData(1) <> Loop_0.io.CarryDepenOut.elements("field0")(0)

  phi_conv_s1_r__y_07117.io.InData(1) <> Loop_1.io.CarryDepenOut.elements("field0")(0)

  phi_conv_s1_x_0729.io.InData(1) <> Loop_2.io.CarryDepenOut.elements("field0")(0)

  phi_conv_s1_y_0732.io.InData(1) <> Loop_3.io.CarryDepenOut.elements("field0")(0)



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  br_0.io.enable <> bb_entry0.io.Out(0)


  ret_1.io.In.enable <> bb_for_cond_cleanup1.io.Out(0)


  const0.io.enable <> bb_for_body2.io.Out(0)

  const1.io.enable <> bb_for_body2.io.Out(1)

  phi_conv_s1_y_0732.io.enable <> bb_for_body2.io.Out(2)


  binaryOp_mul53.io.enable <> bb_for_body2.io.Out(3)


  binaryOp_add124.io.enable <> bb_for_body2.io.Out(4)


  br_5.io.enable <> bb_for_body2.io.Out(5)


  const2.io.enable <> bb_for_cond_cleanup33.io.Out(0)

  const3.io.enable <> bb_for_cond_cleanup33.io.Out(1)

  binaryOp_inc336.io.enable <> bb_for_cond_cleanup33.io.Out(2)


  icmp_exitcond767.io.enable <> bb_for_cond_cleanup33.io.Out(3)


  br_8.io.enable <> bb_for_cond_cleanup33.io.Out(4)


  const4.io.enable <> bb_for_body44.io.Out(0)

  phi_conv_s1_x_0729.io.enable <> bb_for_body44.io.Out(1)


  binaryOp_add610.io.enable <> bb_for_body44.io.Out(2)


  Gep_arrayidx11.io.enable <> bb_for_body44.io.Out(3)


  binaryOp_sub12.io.enable <> bb_for_body44.io.Out(4)


  br_13.io.enable <> bb_for_body44.io.Out(5)


  const5.io.enable <> bb_for_cond_cleanup95.io.Out(0)

  const6.io.enable <> bb_for_cond_cleanup95.io.Out(1)

  binaryOp_inc3014.io.enable <> bb_for_cond_cleanup95.io.Out(2)


  icmp_exitcond7515.io.enable <> bb_for_cond_cleanup95.io.Out(3)


  br_16.io.enable <> bb_for_cond_cleanup95.io.Out(4)


  const7.io.enable <> bb_for_body106.io.Out(0)

  const8.io.enable <> bb_for_body106.io.Out(1)

  phi_conv_s1_r__y_07117.io.enable <> bb_for_body106.io.Out(2)


  binaryOp_mul1118.io.enable <> bb_for_body106.io.Out(3)


  binaryOp_reass_add19.io.enable <> bb_for_body106.io.Out(4)


  binaryOp_reass_mul20.io.enable <> bb_for_body106.io.Out(5)


  br_21.io.enable <> bb_for_body106.io.Out(6)


  const9.io.enable <> bb_for_cond_cleanup177.io.Out(0)

  const10.io.enable <> bb_for_cond_cleanup177.io.Out(1)

  binaryOp_inc2722.io.enable <> bb_for_cond_cleanup177.io.Out(2)


  icmp_exitcond7423.io.enable <> bb_for_cond_cleanup177.io.Out(3)


  br_24.io.enable <> bb_for_cond_cleanup177.io.Out(4)


  const11.io.enable <> bb_for_body188.io.Out(0)

  const12.io.enable <> bb_for_body188.io.Out(1)

  const13.io.enable <> bb_for_body188.io.Out(2)

  phi_conv_s1_r__x_07025.io.enable <> bb_for_body188.io.Out(3)


  ld_26.io.enable <> bb_for_body188.io.Out(4)


  binaryOp_add1927.io.enable <> bb_for_body188.io.Out(5)


  Gep_arrayidx2028.io.enable <> bb_for_body188.io.Out(6)


  ld_29.io.enable <> bb_for_body188.io.Out(7)


  binaryOp_add1430.io.enable <> bb_for_body188.io.Out(8)


  binaryOp_add2131.io.enable <> bb_for_body188.io.Out(9)


  Gep_arrayidx2232.io.enable <> bb_for_body188.io.Out(10)


  ld_33.io.enable <> bb_for_body188.io.Out(11)


  sextconv34.io.enable <> bb_for_body188.io.Out(12)


  binaryOp_mul2335.io.enable <> bb_for_body188.io.Out(13)


  binaryOp_add2436.io.enable <> bb_for_body188.io.Out(14)


  st_37.io.enable <> bb_for_body188.io.Out(15)


  binaryOp_inc38.io.enable <> bb_for_body188.io.Out(16)


  icmp_exitcond39.io.enable <> bb_for_body188.io.Out(17)


  br_40.io.enable <> bb_for_body188.io.Out(18)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  phi_conv_s1_y_0732.io.Mask <> bb_for_body2.io.MaskBB(0)

  phi_conv_s1_x_0729.io.Mask <> bb_for_body44.io.MaskBB(0)

  phi_conv_s1_r__y_07117.io.Mask <> bb_for_body106.io.MaskBB(0)

  phi_conv_s1_r__x_07025.io.Mask <> bb_for_body188.io.MaskBB(0)



  /* ================================================================== *
   *                   PRINT ALLOCA OFFSET                              *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */

  MemCtrl.io.ReadIn(0) <> ld_26.io.memReq

  ld_26.io.memResp <> MemCtrl.io.ReadOut(0)

  MemCtrl.io.ReadIn(1) <> ld_29.io.memReq

  ld_29.io.memResp <> MemCtrl.io.ReadOut(1)

  MemCtrl.io.ReadIn(2) <> ld_33.io.memReq

  ld_33.io.memResp <> MemCtrl.io.ReadOut(2)

  /**
   * @todo this is a dirty fix for the cache bug
   *       We need to investigate more and fix the bug
   */

  //  MemCtrl.io.WriteIn(0) <> st_37.io.memReq
//
//  st_37.io.memResp <> MemCtrl.io.WriteOut(0)


  MemCtrl.io.WriteIn(0) <> DontCare
  MemCtrl.io.WriteOut(0) <> DontCare

  st_37.io.memReq.ready := true.B
  st_37.io.memResp.valid := true.B
  st_37.io.memResp.RouteID := 0.U
  st_37.io.memResp.done := true.B



  /* ================================================================== *
   *                   PRINT SHARED CONNECTIONS                         *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  phi_conv_s1_y_0732.io.InData(0) <> const0.io.Out

  binaryOp_mul53.io.RightIO <> const1.io.Out

  binaryOp_inc336.io.RightIO <> const2.io.Out

  icmp_exitcond767.io.RightIO <> const3.io.Out

  phi_conv_s1_x_0729.io.InData(0) <> const4.io.Out

  binaryOp_inc3014.io.RightIO <> const5.io.Out

  icmp_exitcond7515.io.RightIO <> const6.io.Out

  phi_conv_s1_r__y_07117.io.InData(0) <> const7.io.Out

  binaryOp_mul1118.io.RightIO <> const8.io.Out

  binaryOp_inc2722.io.RightIO <> const9.io.Out

  icmp_exitcond7423.io.RightIO <> const10.io.Out

  phi_conv_s1_r__x_07025.io.InData(0) <> const11.io.Out

  binaryOp_inc38.io.RightIO <> const12.io.Out

  icmp_exitcond39.io.RightIO <> const13.io.Out

  binaryOp_mul53.io.LeftIO <> phi_conv_s1_y_0732.io.Out(0)

  binaryOp_add124.io.LeftIO <> phi_conv_s1_y_0732.io.Out(1)

  binaryOp_inc336.io.LeftIO <> phi_conv_s1_y_0732.io.Out(2)

  icmp_exitcond767.io.LeftIO <> binaryOp_inc336.io.Out(1)

  br_8.io.CmpIO <> icmp_exitcond767.io.Out(0)

  binaryOp_add610.io.LeftIO <> phi_conv_s1_x_0729.io.Out(0)

  binaryOp_sub12.io.LeftIO <> phi_conv_s1_x_0729.io.Out(1)

  binaryOp_inc3014.io.LeftIO <> phi_conv_s1_x_0729.io.Out(2)

  Gep_arrayidx11.io.idx(0) <> binaryOp_add610.io.Out(0)

  icmp_exitcond7515.io.LeftIO <> binaryOp_inc3014.io.Out(1)

  br_16.io.CmpIO <> icmp_exitcond7515.io.Out(0)

  binaryOp_mul1118.io.LeftIO <> phi_conv_s1_r__y_07117.io.Out(0)

  binaryOp_reass_add19.io.RightIO <> phi_conv_s1_r__y_07117.io.Out(1)

  binaryOp_inc2722.io.LeftIO <> phi_conv_s1_r__y_07117.io.Out(2)

  binaryOp_reass_mul20.io.LeftIO <> binaryOp_reass_add19.io.Out(0)

  icmp_exitcond7423.io.LeftIO <> binaryOp_inc2722.io.Out(1)

  br_24.io.CmpIO <> icmp_exitcond7423.io.Out(0)

  binaryOp_add1927.io.LeftIO <> phi_conv_s1_r__x_07025.io.Out(0)

  binaryOp_add1430.io.RightIO <> phi_conv_s1_r__x_07025.io.Out(1)

  binaryOp_inc38.io.LeftIO <> phi_conv_s1_r__x_07025.io.Out(2)

  binaryOp_add2436.io.RightIO <> ld_26.io.Out(0)

  Gep_arrayidx2028.io.idx(0) <> binaryOp_add1927.io.Out(0)

  ld_29.io.GepAddr <> Gep_arrayidx2028.io.Out(0)

  binaryOp_mul2335.io.LeftIO <> ld_29.io.Out(0)

  binaryOp_add2131.io.LeftIO <> binaryOp_add1430.io.Out(0)

  Gep_arrayidx2232.io.idx(0) <> binaryOp_add2131.io.Out(0)

  ld_33.io.GepAddr <> Gep_arrayidx2232.io.Out(0)

  sextconv34.io.Input <> ld_33.io.Out(0)

  binaryOp_mul2335.io.RightIO <> sextconv34.io.Out(0)

  binaryOp_add2436.io.LeftIO <> binaryOp_mul2335.io.Out(0)

  st_37.io.inData <> binaryOp_add2436.io.Out(0)

  icmp_exitcond39.io.LeftIO <> binaryOp_inc38.io.Out(1)

  br_40.io.CmpIO <> icmp_exitcond39.io.Out(0)

  st_37.io.Out(0).ready := true.B



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_1.io.Out

}

import java.io.{File, FileWriter}

object conv3x3HalideTop extends App {
  val dir = new File("RTL/conv3x3HalideTop");
  dir.mkdirs
  implicit val p = new WithAccelConfig
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new conv3x3HalideDF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}
