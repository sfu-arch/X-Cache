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


class stencil2dDF(PtrsIn: Seq[Int] = List(64, 64, 64), ValsIn: Seq[Int] = List(), Returns: Seq[Int] = List())
			(implicit p: Parameters) extends DandelionAccelDCRModule(PtrsIn, ValsIn, Returns){


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  //Cache
  val mem_ctrl_cache = Module(new CacheMemoryEngine(ID = 0, NumRead = 2, NumWrite = 1))

  io.MemReq <> mem_ctrl_cache.io.cache.MemReq
  mem_ctrl_cache.io.cache.MemResp <> io.MemResp

  val ArgSplitter = Module(new SplitCallDCR(ptrsArgTypes = List(1, 1, 1), valsArgTypes = List()))
  ArgSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val Loop_0 = Module(new LoopBlockNode(NumIns = List(1, 1, 1, 1, 1), NumOuts = List(1), NumCarry = List(1, 1), NumExits = 1, ID = 0))

  val Loop_1 = Module(new LoopBlockNode(NumIns = List(1, 1, 1, 1), NumOuts = List(1), NumCarry = List(1, 0), NumExits = 1, ID = 1))

  val Loop_2 = Module(new LoopBlockNode(NumIns = List(1, 1, 1, 1, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 2))

  val Loop_3 = Module(new LoopBlockNode(NumIns = List(1, 1, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 3))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_entry1 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 1))

  val bb_stencil_label23 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 5, NumPhi = 1, BID = 3))

  val bb_for_body37 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 3, NumPhi = 1, BID = 7))

  val bb_stencil_label410 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 11, NumPhi = 2, BID = 10))

  val bb_for_body918 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 16, NumPhi = 2, BID = 18))

  val bb_for_inc1932 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 5, BID = 32))

  val bb_for_end2136 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 8, BID = 36))

  val bb_for_inc2943 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 5, BID = 43))

  val bb_for_end3147 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 47))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  br label %stencil_label2, !UID !3, !BB_UID !4
  val br_2 = Module(new UBranchNode(ID = 2))

  //  %indvars.iv70 = phi i64 [ 0, %entry ], [ %indvars.iv.next71, %for.inc29 ], !UID !5
  val phiindvars_iv704 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 3, ID = 4, Res = true))

  //  %0 = shl i64 %indvars.iv70, 6, !UID !6
  val binaryOp_5 = Module(new ComputeNode(NumOuts = 1, ID = 5, opCode = "shl")(sign = false, Debug = false))

  //  br label %for.body3, !UID !7, !BB_UID !8
  val br_6 = Module(new UBranchNode(ID = 6))

  //  %indvars.iv66 = phi i64 [ 0, %stencil_label2 ], [ %indvars.iv.next67, %for.end21 ], !UID !9
  val phiindvars_iv668 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 3, ID = 8, Res = true))

  //  br label %stencil_label4, !UID !10, !BB_UID !11
  val br_9 = Module(new UBranchNode(ID = 9))

  //  %indvars.iv59 = phi i64 [ 0, %for.body3 ], [ %indvars.iv.next60, %for.inc19 ], !UID !12
  val phiindvars_iv5911 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 3, ID = 11, Res = true))

  //  %temp.054 = phi i32 [ 0, %for.body3 ], [ %add18, %for.inc19 ], !UID !13
  val phitemp_05412 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 1, ID = 12, Res = true))

  //  %1 = mul nuw nsw i64 %indvars.iv59, 3, !UID !14
  val binaryOp_13 = Module(new ComputeNode(NumOuts = 1, ID = 13, opCode = "mul")(sign = false, Debug = false))

  //  %2 = add nuw nsw i64 %indvars.iv59, %indvars.iv70, !UID !15
  val binaryOp_14 = Module(new ComputeNode(NumOuts = 1, ID = 14, opCode = "add")(sign = false, Debug = false))

  //  %3 = shl i64 %2, 6, !UID !16
  val binaryOp_15 = Module(new ComputeNode(NumOuts = 1, ID = 15, opCode = "shl")(sign = false, Debug = false))

  //  %4 = add nuw nsw i64 %3, %indvars.iv66, !UID !17
  val binaryOp_16 = Module(new ComputeNode(NumOuts = 1, ID = 16, opCode = "add")(sign = false, Debug = false))

  //  br label %for.body9, !UID !18, !BB_UID !19
  val br_17 = Module(new UBranchNode(ID = 17))

  //  %indvars.iv = phi i64 [ 0, %stencil_label4 ], [ %indvars.iv.next, %for.body9 ], !UID !20
  val phiindvars_iv19 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 3, ID = 19, Res = true))

  //  %temp.152 = phi i32 [ %temp.054, %stencil_label4 ], [ %add18, %for.body9 ], !UID !21
  val phitemp_15220 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 1, ID = 20, Res = true))

  //  %5 = add nuw nsw i64 %indvars.iv, %1, !UID !22
  val binaryOp_21 = Module(new ComputeNode(NumOuts = 1, ID = 21, opCode = "add")(sign = false, Debug = false))

  //  %arrayidx = getelementptr inbounds i32, i32* %filter, i64 %5, !UID !23
  val Gep_arrayidx22 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 22)(ElementSize = 8, ArraySize = List()))

  //  %6 = load i32, i32* %arrayidx, align 4, !tbaa !24, !UID !28
  val ld_23 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 23, RouteID = 0))

  //  %7 = add nuw nsw i64 %4, %indvars.iv, !UID !29
  val binaryOp_24 = Module(new ComputeNode(NumOuts = 1, ID = 24, opCode = "add")(sign = false, Debug = false))

  //  %arrayidx16 = getelementptr inbounds i32, i32* %orig, i64 %7, !UID !30
  val Gep_arrayidx1625 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 25)(ElementSize = 8, ArraySize = List()))

  //  %8 = load i32, i32* %arrayidx16, align 4, !tbaa !24, !UID !31
  val ld_26 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 26, RouteID = 1))

  //  %mul17 = mul nsw i32 %8, %6, !UID !32
  val binaryOp_mul1727 = Module(new ComputeNode(NumOuts = 1, ID = 27, opCode = "mul")(sign = false, Debug = false))

  //  %add18 = add nsw i32 %mul17, %temp.152, !UID !33
  val binaryOp_add1828 = Module(new ComputeNode(NumOuts = 2, ID = 28, opCode = "add")(sign = false, Debug = false))

  //  %indvars.iv.next = add nuw nsw i64 %indvars.iv, 1, !UID !34
  val binaryOp_indvars_iv_next29 = Module(new ComputeNode(NumOuts = 2, ID = 29, opCode = "add")(sign = false, Debug = false))

  //  %exitcond = icmp eq i64 %indvars.iv.next, 3, !UID !35
  val icmp_exitcond30 = Module(new ComputeNode(NumOuts = 1, ID = 30, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond, label %for.inc19, label %for.body9, !UID !36, !BB_UID !37
  val br_31 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 31))

  //  %indvars.iv.next60 = add nuw nsw i64 %indvars.iv59, 1, !UID !38
  val binaryOp_indvars_iv_next6033 = Module(new ComputeNode(NumOuts = 2, ID = 33, opCode = "add")(sign = false, Debug = false))

  //  %exitcond65 = icmp eq i64 %indvars.iv.next60, 3, !UID !39
  val icmp_exitcond6534 = Module(new ComputeNode(NumOuts = 1, ID = 34, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond65, label %for.end21, label %stencil_label4, !UID !40, !BB_UID !41
  val br_35 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 35))

  //  %9 = add nuw nsw i64 %indvars.iv66, %0, !UID !42
  val binaryOp_37 = Module(new ComputeNode(NumOuts = 1, ID = 37, opCode = "add")(sign = false, Debug = false))

  //  %arrayidx25 = getelementptr inbounds i32, i32* %sol, i64 %9, !UID !43
  val Gep_arrayidx2538 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 38)(ElementSize = 8, ArraySize = List()))

  //  store i32 %add18, i32* %arrayidx25, align 4, !tbaa !24, !UID !44
  val st_39 = Module(new UnTypStoreCache(NumPredOps = 0, NumSuccOps = 1, ID = 39, RouteID = 2))

  //  %indvars.iv.next67 = add nuw nsw i64 %indvars.iv66, 1, !UID !45
  val binaryOp_indvars_iv_next6740 = Module(new ComputeNode(NumOuts = 2, ID = 40, opCode = "add")(sign = false, Debug = false))

  //  %exitcond69 = icmp eq i64 %indvars.iv.next67, 62, !UID !46
  val icmp_exitcond6941 = Module(new ComputeNode(NumOuts = 1, ID = 41, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond69, label %for.inc29, label %for.body3, !UID !47, !BB_UID !48
  val br_42 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 1, ID = 42))

  //  %indvars.iv.next71 = add nuw nsw i64 %indvars.iv70, 1, !UID !49
  val binaryOp_indvars_iv_next7144 = Module(new ComputeNode(NumOuts = 2, ID = 44, opCode = "add")(sign = false, Debug = false))

  //  %exitcond73 = icmp eq i64 %indvars.iv.next71, 126, !UID !50
  val icmp_exitcond7345 = Module(new ComputeNode(NumOuts = 1, ID = 45, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond73, label %for.end31, label %stencil_label2, !UID !51, !BB_UID !52
  val br_46 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 46))

  //  ret void, !UID !53, !BB_UID !54
  val ret_48 = Module(new RetNode2(retTypes = List(), ID = 48))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i64 0
  val const0 = Module(new ConstFastNode(value = 0, ID = 0))

  //i64 6
  val const1 = Module(new ConstFastNode(value = 6, ID = 1))

  //i64 0
  val const2 = Module(new ConstFastNode(value = 0, ID = 2))

  //i64 0
  val const3 = Module(new ConstFastNode(value = 0, ID = 3))

  //i32 0
  val const4 = Module(new ConstFastNode(value = 0, ID = 4))

  //i64 3
  val const5 = Module(new ConstFastNode(value = 3, ID = 5))

  //i64 6
  val const6 = Module(new ConstFastNode(value = 6, ID = 6))

  //i64 0
  val const7 = Module(new ConstFastNode(value = 0, ID = 7))

  //i64 1
  val const8 = Module(new ConstFastNode(value = 1, ID = 8))

  //i64 3
  val const9 = Module(new ConstFastNode(value = 3, ID = 9))

  //i64 1
  val const10 = Module(new ConstFastNode(value = 1, ID = 10))

  //i64 3
  val const11 = Module(new ConstFastNode(value = 3, ID = 11))

  //i64 1
  val const12 = Module(new ConstFastNode(value = 1, ID = 12))

  //i64 62
  val const13 = Module(new ConstFastNode(value = 62, ID = 13))

  //i64 1
  val const14 = Module(new ConstFastNode(value = 1, ID = 14))

  //i64 126
  val const15 = Module(new ConstFastNode(value = 126, ID = 15))



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_entry1.io.predicateIn(0) <> ArgSplitter.io.Out.enable



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE LOOP                     *
   * ================================================================== */

  bb_stencil_label23.io.predicateIn(1) <> Loop_3.io.activate_loop_start

  bb_stencil_label23.io.predicateIn(0) <> Loop_3.io.activate_loop_back

  bb_for_body37.io.predicateIn(1) <> Loop_2.io.activate_loop_start

  bb_for_body37.io.predicateIn(0) <> Loop_2.io.activate_loop_back

  bb_stencil_label410.io.predicateIn(1) <> Loop_1.io.activate_loop_start

  bb_stencil_label410.io.predicateIn(0) <> Loop_1.io.activate_loop_back

  bb_for_body918.io.predicateIn(1) <> Loop_0.io.activate_loop_start

  bb_for_body918.io.predicateIn(0) <> Loop_0.io.activate_loop_back

  bb_for_inc1932.io.predicateIn(0) <> Loop_0.io.loopExit(0)

  bb_for_end2136.io.predicateIn(0) <> Loop_1.io.loopExit(0)

  bb_for_inc2943.io.predicateIn(0) <> Loop_2.io.loopExit(0)

  bb_for_end3147.io.predicateIn(0) <> Loop_3.io.loopExit(0)



  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */

  Loop_0.io.enable <> br_17.io.Out(0)

  Loop_0.io.loopBack(0) <> br_31.io.FalseOutput(0)

  Loop_0.io.loopFinish(0) <> br_31.io.TrueOutput(0)

  Loop_1.io.enable <> br_9.io.Out(0)

  Loop_1.io.loopBack(0) <> br_35.io.FalseOutput(0)

  Loop_1.io.loopFinish(0) <> br_35.io.TrueOutput(0)

  Loop_2.io.enable <> br_6.io.Out(0)

  Loop_2.io.loopBack(0) <> br_42.io.FalseOutput(0)

  Loop_2.io.loopFinish(0) <> br_42.io.TrueOutput(0)

  Loop_3.io.enable <> br_2.io.Out(0)

  Loop_3.io.loopBack(0) <> br_46.io.FalseOutput(0)

  Loop_3.io.loopFinish(0) <> br_46.io.TrueOutput(0)



  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

  Loop_0.io.InLiveIn(0) <> phitemp_05412.io.Out(0)

  Loop_0.io.InLiveIn(1) <> binaryOp_13.io.Out(0)

  Loop_0.io.InLiveIn(2) <> binaryOp_16.io.Out(0)

  Loop_0.io.InLiveIn(3) <> Loop_1.io.OutLiveIn.elements("field1")(0)

  Loop_0.io.InLiveIn(4) <> Loop_1.io.OutLiveIn.elements("field2")(0)

  Loop_1.io.InLiveIn(0) <> phiindvars_iv668.io.Out(0)

  Loop_1.io.InLiveIn(1) <> Loop_2.io.OutLiveIn.elements("field2")(0)

  Loop_1.io.InLiveIn(2) <> Loop_2.io.OutLiveIn.elements("field3")(0)

  Loop_1.io.InLiveIn(3) <> Loop_2.io.OutLiveIn.elements("field0")(0)

  Loop_2.io.InLiveIn(0) <> phiindvars_iv704.io.Out(0)

  Loop_2.io.InLiveIn(1) <> binaryOp_5.io.Out(0)

  Loop_2.io.InLiveIn(2) <> Loop_3.io.OutLiveIn.elements("field1")(0)

  Loop_2.io.InLiveIn(3) <> Loop_3.io.OutLiveIn.elements("field0")(0)

  Loop_2.io.InLiveIn(4) <> Loop_3.io.OutLiveIn.elements("field2")(0)

  Loop_3.io.InLiveIn(0) <> ArgSplitter.io.Out.dataPtrs.elements("field2")(0)

  Loop_3.io.InLiveIn(1) <> ArgSplitter.io.Out.dataPtrs.elements("field0")(0)

  Loop_3.io.InLiveIn(2) <> ArgSplitter.io.Out.dataPtrs.elements("field1")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */

  phitemp_15220.io.InData(0) <> Loop_0.io.OutLiveIn.elements("field0")(0)

  binaryOp_21.io.RightIO <> Loop_0.io.OutLiveIn.elements("field1")(0)

  binaryOp_24.io.LeftIO <> Loop_0.io.OutLiveIn.elements("field2")(0)

  Gep_arrayidx1625.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field3")(0)

  Gep_arrayidx22.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field4")(0)

  binaryOp_16.io.RightIO <> Loop_1.io.OutLiveIn.elements("field0")(0)

  binaryOp_14.io.RightIO <> Loop_1.io.OutLiveIn.elements("field3")(0)

  binaryOp_37.io.RightIO <> Loop_2.io.OutLiveIn.elements("field1")(0)

  Gep_arrayidx2538.io.baseAddress <> Loop_2.io.OutLiveIn.elements("field4")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */

  Loop_0.io.InLiveOut(0) <> binaryOp_add1828.io.Out(0)



  /* ================================================================== *
   *                   LOOP LIVE OUT DEPENDENCIES                       *
   * ================================================================== */

  phitemp_05412.io.InData(1) <> Loop_0.io.OutLiveOut.elements("field0")(0)

  st_39.io.inData <> Loop_1.io.OutLiveOut.elements("field0")(0)



  /* ================================================================== *
   *                   LOOP CARRY DEPENDENCIES                          *
   * ================================================================== */

  Loop_0.io.CarryDepenIn(0) <> binaryOp_indvars_iv_next29.io.Out(0)

  Loop_0.io.CarryDepenIn(1) <> binaryOp_add1828.io.Out(1)

  Loop_1.io.CarryDepenIn(0) <> binaryOp_indvars_iv_next6033.io.Out(0)

  Loop_2.io.CarryDepenIn(0) <> binaryOp_indvars_iv_next6740.io.Out(0)

  Loop_3.io.CarryDepenIn(0) <> binaryOp_indvars_iv_next7144.io.Out(0)



  /* ================================================================== *
   *                   LOOP DATA CARRY DEPENDENCIES                     *
   * ================================================================== */

  phiindvars_iv19.io.InData(1) <> Loop_0.io.CarryDepenOut.elements("field0")(0)

  phitemp_15220.io.InData(1) <> Loop_0.io.CarryDepenOut.elements("field1")(0)

  phiindvars_iv5911.io.InData(1) <> Loop_1.io.CarryDepenOut.elements("field0")(0)

  phiindvars_iv668.io.InData(1) <> Loop_2.io.CarryDepenOut.elements("field0")(0)

  phiindvars_iv704.io.InData(1) <> Loop_3.io.CarryDepenOut.elements("field0")(0)



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  br_2.io.enable <> bb_entry1.io.Out(0)


  const0.io.enable <> bb_stencil_label23.io.Out(0)

  const1.io.enable <> bb_stencil_label23.io.Out(1)

  phiindvars_iv704.io.enable <> bb_stencil_label23.io.Out(2)


  binaryOp_5.io.enable <> bb_stencil_label23.io.Out(3)


  br_6.io.enable <> bb_stencil_label23.io.Out(4)


  const2.io.enable <> bb_for_body37.io.Out(0)

  phiindvars_iv668.io.enable <> bb_for_body37.io.Out(1)


  br_9.io.enable <> bb_for_body37.io.Out(2)


  const3.io.enable <> bb_stencil_label410.io.Out(0)

  const4.io.enable <> bb_stencil_label410.io.Out(1)

  const5.io.enable <> bb_stencil_label410.io.Out(2)

  const6.io.enable <> bb_stencil_label410.io.Out(3)

  phiindvars_iv5911.io.enable <> bb_stencil_label410.io.Out(4)


  phitemp_05412.io.enable <> bb_stencil_label410.io.Out(5)


  binaryOp_13.io.enable <> bb_stencil_label410.io.Out(6)


  binaryOp_14.io.enable <> bb_stencil_label410.io.Out(7)


  binaryOp_15.io.enable <> bb_stencil_label410.io.Out(8)


  binaryOp_16.io.enable <> bb_stencil_label410.io.Out(9)


  br_17.io.enable <> bb_stencil_label410.io.Out(10)


  const7.io.enable <> bb_for_body918.io.Out(0)

  const8.io.enable <> bb_for_body918.io.Out(1)

  const9.io.enable <> bb_for_body918.io.Out(2)

  phiindvars_iv19.io.enable <> bb_for_body918.io.Out(3)


  phitemp_15220.io.enable <> bb_for_body918.io.Out(4)


  binaryOp_21.io.enable <> bb_for_body918.io.Out(5)


  Gep_arrayidx22.io.enable <> bb_for_body918.io.Out(6)


  ld_23.io.enable <> bb_for_body918.io.Out(7)


  binaryOp_24.io.enable <> bb_for_body918.io.Out(8)


  Gep_arrayidx1625.io.enable <> bb_for_body918.io.Out(9)


  ld_26.io.enable <> bb_for_body918.io.Out(10)


  binaryOp_mul1727.io.enable <> bb_for_body918.io.Out(11)


  binaryOp_add1828.io.enable <> bb_for_body918.io.Out(12)


  binaryOp_indvars_iv_next29.io.enable <> bb_for_body918.io.Out(13)


  icmp_exitcond30.io.enable <> bb_for_body918.io.Out(14)


  br_31.io.enable <> bb_for_body918.io.Out(15)


  const10.io.enable <> bb_for_inc1932.io.Out(0)

  const11.io.enable <> bb_for_inc1932.io.Out(1)

  binaryOp_indvars_iv_next6033.io.enable <> bb_for_inc1932.io.Out(2)


  icmp_exitcond6534.io.enable <> bb_for_inc1932.io.Out(3)


  br_35.io.enable <> bb_for_inc1932.io.Out(4)


  const12.io.enable <> bb_for_end2136.io.Out(0)

  const13.io.enable <> bb_for_end2136.io.Out(1)

  binaryOp_37.io.enable <> bb_for_end2136.io.Out(2)


  Gep_arrayidx2538.io.enable <> bb_for_end2136.io.Out(3)


  st_39.io.enable <> bb_for_end2136.io.Out(4)


  binaryOp_indvars_iv_next6740.io.enable <> bb_for_end2136.io.Out(5)


  icmp_exitcond6941.io.enable <> bb_for_end2136.io.Out(6)


  br_42.io.enable <> bb_for_end2136.io.Out(7)


  const14.io.enable <> bb_for_inc2943.io.Out(0)

  const15.io.enable <> bb_for_inc2943.io.Out(1)

  binaryOp_indvars_iv_next7144.io.enable <> bb_for_inc2943.io.Out(2)


  icmp_exitcond7345.io.enable <> bb_for_inc2943.io.Out(3)


  br_46.io.enable <> bb_for_inc2943.io.Out(4)


  ret_48.io.In.enable <> bb_for_end3147.io.Out(0)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  phiindvars_iv704.io.Mask <> bb_stencil_label23.io.MaskBB(0)

  phiindvars_iv668.io.Mask <> bb_for_body37.io.MaskBB(0)

  phiindvars_iv5911.io.Mask <> bb_stencil_label410.io.MaskBB(0)

  phitemp_05412.io.Mask <> bb_stencil_label410.io.MaskBB(1)

  phiindvars_iv19.io.Mask <> bb_for_body918.io.MaskBB(0)

  phitemp_15220.io.Mask <> bb_for_body918.io.MaskBB(1)



  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */

  mem_ctrl_cache.io.rd.mem(0).MemReq <> ld_23.io.MemReq
  ld_23.io.MemResp <> mem_ctrl_cache.io.rd.mem(0).MemResp
  mem_ctrl_cache.io.rd.mem(1).MemReq <> ld_26.io.MemReq
  ld_26.io.MemResp <> mem_ctrl_cache.io.rd.mem(1).MemResp
  mem_ctrl_cache.io.wr.mem(0).MemReq <> st_39.io.MemReq
  st_39.io.MemResp <> mem_ctrl_cache.io.wr.mem(0).MemResp



  /* ================================================================== *
   *                   PRINT SHARED CONNECTIONS                         *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  phiindvars_iv704.io.InData(0) <> const0.io.Out

  binaryOp_5.io.RightIO <> const1.io.Out

  phiindvars_iv668.io.InData(0) <> const2.io.Out

  phiindvars_iv5911.io.InData(0) <> const3.io.Out

  phitemp_05412.io.InData(0) <> const4.io.Out

  binaryOp_13.io.RightIO <> const5.io.Out

  binaryOp_15.io.RightIO <> const6.io.Out

  phiindvars_iv19.io.InData(0) <> const7.io.Out

  binaryOp_indvars_iv_next29.io.RightIO <> const8.io.Out

  icmp_exitcond30.io.RightIO <> const9.io.Out

  binaryOp_indvars_iv_next6033.io.RightIO <> const10.io.Out

  icmp_exitcond6534.io.RightIO <> const11.io.Out

  binaryOp_indvars_iv_next6740.io.RightIO <> const12.io.Out

  icmp_exitcond6941.io.RightIO <> const13.io.Out

  binaryOp_indvars_iv_next7144.io.RightIO <> const14.io.Out

  icmp_exitcond7345.io.RightIO <> const15.io.Out

  binaryOp_5.io.LeftIO <> phiindvars_iv704.io.Out(1)

  binaryOp_indvars_iv_next7144.io.LeftIO <> phiindvars_iv704.io.Out(2)

  binaryOp_37.io.LeftIO <> phiindvars_iv668.io.Out(1)

  binaryOp_indvars_iv_next6740.io.LeftIO <> phiindvars_iv668.io.Out(2)

  binaryOp_13.io.LeftIO <> phiindvars_iv5911.io.Out(0)

  binaryOp_14.io.LeftIO <> phiindvars_iv5911.io.Out(1)

  binaryOp_indvars_iv_next6033.io.LeftIO <> phiindvars_iv5911.io.Out(2)

  binaryOp_15.io.LeftIO <> binaryOp_14.io.Out(0)

  binaryOp_16.io.LeftIO <> binaryOp_15.io.Out(0)

  binaryOp_21.io.LeftIO <> phiindvars_iv19.io.Out(0)

  binaryOp_24.io.RightIO <> phiindvars_iv19.io.Out(1)

  binaryOp_indvars_iv_next29.io.LeftIO <> phiindvars_iv19.io.Out(2)

  binaryOp_add1828.io.RightIO <> phitemp_15220.io.Out(0)

  Gep_arrayidx22.io.idx(0) <> binaryOp_21.io.Out(0)

  ld_23.io.GepAddr <> Gep_arrayidx22.io.Out(0)

  binaryOp_mul1727.io.RightIO <> ld_23.io.Out(0)

  Gep_arrayidx1625.io.idx(0) <> binaryOp_24.io.Out(0)

  ld_26.io.GepAddr <> Gep_arrayidx1625.io.Out(0)

  binaryOp_mul1727.io.LeftIO <> ld_26.io.Out(0)

  binaryOp_add1828.io.LeftIO <> binaryOp_mul1727.io.Out(0)

  icmp_exitcond30.io.LeftIO <> binaryOp_indvars_iv_next29.io.Out(1)

  br_31.io.CmpIO <> icmp_exitcond30.io.Out(0)

  icmp_exitcond6534.io.LeftIO <> binaryOp_indvars_iv_next6033.io.Out(1)

  br_35.io.CmpIO <> icmp_exitcond6534.io.Out(0)

  Gep_arrayidx2538.io.idx(0) <> binaryOp_37.io.Out(0)

  st_39.io.GepAddr <> Gep_arrayidx2538.io.Out(0)

  icmp_exitcond6941.io.LeftIO <> binaryOp_indvars_iv_next6740.io.Out(1)

  br_42.io.CmpIO <> icmp_exitcond6941.io.Out(0)

  icmp_exitcond7345.io.LeftIO <> binaryOp_indvars_iv_next7144.io.Out(1)

  br_46.io.CmpIO <> icmp_exitcond7345.io.Out(0)

  st_39.io.Out(0).ready := true.B



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  br_42.io.PredOp(0) <> st_39.io.SuccOp(0)



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_48.io.Out

}

