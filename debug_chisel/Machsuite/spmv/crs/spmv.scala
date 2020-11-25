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


class spmvDF(PtrsIn: Seq[Int] = List(64, 64, 64, 64, 64), ValsIn: Seq[Int] = List(), Returns: Seq[Int] = List())
			(implicit p: Parameters) extends DandelionAccelDCRModule(PtrsIn, ValsIn, Returns){


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  //Cache
  val mem_ctrl_cache = Module(new CacheMemoryEngine(ID = 0, NumRead = 5, NumWrite = 1))

  io.MemReq <> mem_ctrl_cache.io.cache.MemReq
  mem_ctrl_cache.io.cache.MemResp <> io.MemResp

  val ArgSplitter = Module(new SplitCallDCR(ptrsArgTypes = List(1, 1, 1, 1, 1), valsArgTypes = List()))
  ArgSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val Loop_0 = Module(new LoopBlockNode(NumIns = List(1, 1, 1, 1, 1), NumOuts = List(1), NumCarry = List(1, 1), NumExits = 1, ID = 0))

  val Loop_1 = Module(new LoopBlockNode(NumIns = List(2, 1, 1, 1, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 1))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_entry1 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 1))

  val bb_for_body3 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 10, NumPhi = 1, BID = 3))

  val bb_for_body5_lr_ph12 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 3, BID = 12))

  val bb_for_body516 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 16, NumPhi = 2, BID = 16))

  val bb_for_end_loopexit31 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 31))

  val bb_for_end33 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 7, NumPhi = 1, BID = 33))

  val bb_for_end1739 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 39))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  br label %for.body, !UID !3, !BB_UID !4
  val br_2 = Module(new UBranchNode(ID = 2))

  //  %indvars.iv37 = phi i64 [ 0, %entry ], [ %indvars.iv.next38, %for.end ], !UID !5
  val phiindvars_iv374 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 3, ID = 4, Res = true))

  //  %arrayidx = getelementptr inbounds i32, i32* %rowDelimiters, i64 %indvars.iv37, !UID !6
  val Gep_arrayidx5 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 5)(ElementSize = 8, ArraySize = List()))

  //  %0 = load i32, i32* %arrayidx, align 4, !tbaa !7, !UID !11
  val ld_6 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 2, ID = 6, RouteID = 0))

  //  %indvars.iv.next38 = add nuw nsw i64 %indvars.iv37, 1, !UID !12
  val binaryOp_indvars_iv_next387 = Module(new ComputeNode(NumOuts = 3, ID = 7, opCode = "add")(sign = false, Debug = false))

  //  %arrayidx2 = getelementptr inbounds i32, i32* %rowDelimiters, i64 %indvars.iv.next38, !UID !13
  val Gep_arrayidx28 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 8)(ElementSize = 8, ArraySize = List()))

  //  %1 = load i32, i32* %arrayidx2, align 4, !tbaa !7, !UID !14
  val ld_9 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 2, ID = 9, RouteID = 1))

  //  %cmp433 = icmp slt i32 %0, %1, !UID !15
  val icmp_cmp43310 = Module(new ComputeNode(NumOuts = 1, ID = 10, opCode = "slt")(sign = true, Debug = false))

  //  br i1 %cmp433, label %for.body5.lr.ph, label %for.end, !UID !16, !BB_UID !17
  val br_11 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 11))

  //  %2 = sext i32 %0 to i64, !UID !18
  val sext13 = Module(new SextNode(NumOuts = 1))

  //  %wide.trip.count = sext i32 %1 to i64, !UID !19
  val sextwide_trip_count14 = Module(new SextNode(NumOuts = 1))

  //  br label %for.body5, !UID !20, !BB_UID !21
  val br_15 = Module(new UBranchNode(ID = 15))

  //  %indvars.iv = phi i64 [ %2, %for.body5.lr.ph ], [ %indvars.iv.next, %for.body5 ], !UID !22
  val phiindvars_iv17 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 3, ID = 17, Res = true))

  //  %sum.034 = phi double [ 0.000000e+00, %for.body5.lr.ph ], [ %add12, %for.body5 ], !UID !23
  val phisum_03418 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 1, ID = 18, Res = true))

  //  %arrayidx7 = getelementptr inbounds double, double* %val, i64 %indvars.iv, !UID !24
  val Gep_arrayidx719 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 19)(ElementSize = 8, ArraySize = List()))

  //  %3 = load double, double* %arrayidx7, align 8, !tbaa !25, !UID !27
  val ld_20 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 20, RouteID = 2))

  //  %arrayidx9 = getelementptr inbounds i32, i32* %cols, i64 %indvars.iv, !UID !28
  val Gep_arrayidx921 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 21)(ElementSize = 8, ArraySize = List()))

  //  %4 = load i32, i32* %arrayidx9, align 4, !tbaa !7, !UID !29
  val ld_22 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 22, RouteID = 3))

  //  %idxprom10 = sext i32 %4 to i64, !UID !30
  val sextidxprom1023 = Module(new SextNode(NumOuts = 1))

  //  %arrayidx11 = getelementptr inbounds double, double* %vec, i64 %idxprom10, !UID !31
  val Gep_arrayidx1124 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 24)(ElementSize = 8, ArraySize = List()))

  //  %5 = load double, double* %arrayidx11, align 8, !tbaa !25, !UID !32
  val ld_25 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 25, RouteID = 4))

  //  %mul = fmul double %3, %5, !UID !33
  val FP_mul26 = Module(new FPComputeNode(NumOuts = 1, ID = 26, opCode = "fmul")(fType))

  //  %add12 = fadd double %sum.034, %mul, !UID !34
  val FP_add1227 = Module(new FPComputeNode(NumOuts = 2, ID = 27, opCode = "fadd")(fType))

  //  %indvars.iv.next = add nsw i64 %indvars.iv, 1, !UID !35
  val binaryOp_indvars_iv_next28 = Module(new ComputeNode(NumOuts = 2, ID = 28, opCode = "add")(sign = false, Debug = false))

  //  %exitcond = icmp eq i64 %indvars.iv.next, %wide.trip.count, !UID !36
  val icmp_exitcond29 = Module(new ComputeNode(NumOuts = 1, ID = 29, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond, label %for.end.loopexit, label %for.body5, !UID !37, !BB_UID !38
  val br_30 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 30))

  //  br label %for.end, !UID !39, !BB_UID !40
  val br_32 = Module(new UBranchNode(ID = 32))

  //  %sum.0.lcssa = phi double [ 0.000000e+00, %for.body ], [ %add12, %for.end.loopexit ], !UID !41
  val phisum_0_lcssa34 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 1, ID = 34, Res = true))

  //  %arrayidx14 = getelementptr inbounds double, double* %out, i64 %indvars.iv37, !UID !42
  val Gep_arrayidx1435 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 35)(ElementSize = 8, ArraySize = List()))

  //  store double %sum.0.lcssa, double* %arrayidx14, align 8, !tbaa !25, !UID !43
  val st_36 = Module(new UnTypStoreCache(NumPredOps = 0, NumSuccOps = 1, ID = 36, RouteID = 5))

  //  %exitcond39 = icmp eq i64 %indvars.iv.next38, 494, !UID !44
  val icmp_exitcond3937 = Module(new ComputeNode(NumOuts = 1, ID = 37, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond39, label %for.end17, label %for.body, !UID !45, !BB_UID !46
  val br_38 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 1, ID = 38))

  //  ret void, !UID !47, !BB_UID !48
  val ret_40 = Module(new RetNode2(retTypes = List(), ID = 40))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i64 0
  val const0 = Module(new ConstFastNode(value = 0, ID = 0))

  //i64 1
  val const1 = Module(new ConstFastNode(value = 1, ID = 1))

  //i64 1
  val const2 = Module(new ConstFastNode(value = 1, ID = 2))

  //i64 494
  val const3 = Module(new ConstFastNode(value = 494, ID = 3))

  //double 0.000000e+00
  val constf0 = Module(new ConstFastNode(value = 0x0L, ID = 0))

  //double 0.000000e+00
  val constf1 = Module(new ConstFastNode(value = 0x0L, ID = 1))



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_entry1.io.predicateIn(0) <> ArgSplitter.io.Out.enable

  bb_for_body5_lr_ph12.io.predicateIn(0) <> br_11.io.TrueOutput(0)

  bb_for_end33.io.predicateIn(1) <> br_11.io.FalseOutput(0)

  bb_for_end33.io.predicateIn(0) <> br_32.io.Out(0)



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE LOOP                     *
   * ================================================================== */

  bb_for_body3.io.predicateIn(1) <> Loop_1.io.activate_loop_start

  bb_for_body3.io.predicateIn(0) <> Loop_1.io.activate_loop_back

  bb_for_body516.io.predicateIn(1) <> Loop_0.io.activate_loop_start

  bb_for_body516.io.predicateIn(0) <> Loop_0.io.activate_loop_back

  bb_for_end_loopexit31.io.predicateIn(0) <> Loop_0.io.loopExit(0)

  bb_for_end1739.io.predicateIn(0) <> Loop_1.io.loopExit(0)



  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */

  Loop_0.io.enable <> br_15.io.Out(0)

  Loop_0.io.loopBack(0) <> br_30.io.FalseOutput(0)

  Loop_0.io.loopFinish(0) <> br_30.io.TrueOutput(0)

  Loop_1.io.enable <> br_2.io.Out(0)

  Loop_1.io.loopBack(0) <> br_38.io.FalseOutput(0)

  Loop_1.io.loopFinish(0) <> br_38.io.TrueOutput(0)



  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

  Loop_0.io.InLiveIn(0) <> sext13.io.Out(0)

  Loop_0.io.InLiveIn(1) <> sextwide_trip_count14.io.Out(0)

  Loop_0.io.InLiveIn(2) <> Loop_1.io.OutLiveIn.elements("field2")(0)

  Loop_0.io.InLiveIn(3) <> Loop_1.io.OutLiveIn.elements("field1")(0)

  Loop_0.io.InLiveIn(4) <> Loop_1.io.OutLiveIn.elements("field3")(0)

  Loop_1.io.InLiveIn(0) <> ArgSplitter.io.Out.dataPtrs.elements("field2")(0)

  Loop_1.io.InLiveIn(1) <> ArgSplitter.io.Out.dataPtrs.elements("field0")(0)

  Loop_1.io.InLiveIn(2) <> ArgSplitter.io.Out.dataPtrs.elements("field1")(0)

  Loop_1.io.InLiveIn(3) <> ArgSplitter.io.Out.dataPtrs.elements("field3")(0)

  Loop_1.io.InLiveIn(4) <> ArgSplitter.io.Out.dataPtrs.elements("field4")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */

  phiindvars_iv17.io.InData(0) <> Loop_0.io.OutLiveIn.elements("field0")(0)

  icmp_exitcond29.io.RightIO <> Loop_0.io.OutLiveIn.elements("field1")(0)

  Gep_arrayidx921.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field2")(0)

  Gep_arrayidx719.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field3")(0)

  Gep_arrayidx1124.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field4")(0)

  Gep_arrayidx5.io.baseAddress <> Loop_1.io.OutLiveIn.elements("field0")(0)

  Gep_arrayidx28.io.baseAddress <> Loop_1.io.OutLiveIn.elements("field0")(1)

  Gep_arrayidx1435.io.baseAddress <> Loop_1.io.OutLiveIn.elements("field4")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */

  Loop_0.io.InLiveOut(0) <> FP_add1227.io.Out(0)



  /* ================================================================== *
   *                   LOOP LIVE OUT DEPENDENCIES                       *
   * ================================================================== */

  phisum_0_lcssa34.io.InData(1) <> Loop_0.io.OutLiveOut.elements("field0")(0)



  /* ================================================================== *
   *                   LOOP CARRY DEPENDENCIES                          *
   * ================================================================== */

  Loop_0.io.CarryDepenIn(0) <> FP_add1227.io.Out(1)

  Loop_0.io.CarryDepenIn(1) <> binaryOp_indvars_iv_next28.io.Out(0)

  Loop_1.io.CarryDepenIn(0) <> binaryOp_indvars_iv_next387.io.Out(0)



  /* ================================================================== *
   *                   LOOP DATA CARRY DEPENDENCIES                     *
   * ================================================================== */

  phisum_03418.io.InData(1) <> Loop_0.io.CarryDepenOut.elements("field0")(0)

  phiindvars_iv17.io.InData(1) <> Loop_0.io.CarryDepenOut.elements("field1")(0)

  phiindvars_iv374.io.InData(1) <> Loop_1.io.CarryDepenOut.elements("field0")(0)



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  br_2.io.enable <> bb_entry1.io.Out(0)


  const0.io.enable <> bb_for_body3.io.Out(0)

  const1.io.enable <> bb_for_body3.io.Out(1)

  phiindvars_iv374.io.enable <> bb_for_body3.io.Out(2)


  Gep_arrayidx5.io.enable <> bb_for_body3.io.Out(3)


  ld_6.io.enable <> bb_for_body3.io.Out(4)


  binaryOp_indvars_iv_next387.io.enable <> bb_for_body3.io.Out(5)


  Gep_arrayidx28.io.enable <> bb_for_body3.io.Out(6)


  ld_9.io.enable <> bb_for_body3.io.Out(7)


  icmp_cmp43310.io.enable <> bb_for_body3.io.Out(8)


  br_11.io.enable <> bb_for_body3.io.Out(9)


  sext13.io.enable <> bb_for_body5_lr_ph12.io.Out(0)


  sextwide_trip_count14.io.enable <> bb_for_body5_lr_ph12.io.Out(1)


  br_15.io.enable <> bb_for_body5_lr_ph12.io.Out(2)


  constf0.io.enable <> bb_for_body516.io.Out(0)

  const2.io.enable <> bb_for_body516.io.Out(1)

  phiindvars_iv17.io.enable <> bb_for_body516.io.Out(2)


  phisum_03418.io.enable <> bb_for_body516.io.Out(3)


  Gep_arrayidx719.io.enable <> bb_for_body516.io.Out(4)


  ld_20.io.enable <> bb_for_body516.io.Out(5)


  Gep_arrayidx921.io.enable <> bb_for_body516.io.Out(6)


  ld_22.io.enable <> bb_for_body516.io.Out(7)


  sextidxprom1023.io.enable <> bb_for_body516.io.Out(8)


  Gep_arrayidx1124.io.enable <> bb_for_body516.io.Out(9)


  ld_25.io.enable <> bb_for_body516.io.Out(10)


  FP_mul26.io.enable <> bb_for_body516.io.Out(11)


  FP_add1227.io.enable <> bb_for_body516.io.Out(12)


  binaryOp_indvars_iv_next28.io.enable <> bb_for_body516.io.Out(13)


  icmp_exitcond29.io.enable <> bb_for_body516.io.Out(14)


  br_30.io.enable <> bb_for_body516.io.Out(15)


  br_32.io.enable <> bb_for_end_loopexit31.io.Out(0)


  constf1.io.enable <> bb_for_end33.io.Out(0)

  const3.io.enable <> bb_for_end33.io.Out(1)

  phisum_0_lcssa34.io.enable <> bb_for_end33.io.Out(2)


  Gep_arrayidx1435.io.enable <> bb_for_end33.io.Out(3)


  st_36.io.enable <> bb_for_end33.io.Out(4)


  icmp_exitcond3937.io.enable <> bb_for_end33.io.Out(5)


  br_38.io.enable <> bb_for_end33.io.Out(6)


  ret_40.io.In.enable <> bb_for_end1739.io.Out(0)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  phiindvars_iv374.io.Mask <> bb_for_body3.io.MaskBB(0)

  phiindvars_iv17.io.Mask <> bb_for_body516.io.MaskBB(0)

  phisum_03418.io.Mask <> bb_for_body516.io.MaskBB(1)

  phisum_0_lcssa34.io.Mask <> bb_for_end33.io.MaskBB(0)



  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */

  mem_ctrl_cache.io.rd.mem(0).MemReq <> ld_6.io.MemReq
  ld_6.io.MemResp <> mem_ctrl_cache.io.rd.mem(0).MemResp
  mem_ctrl_cache.io.rd.mem(1).MemReq <> ld_9.io.MemReq
  ld_9.io.MemResp <> mem_ctrl_cache.io.rd.mem(1).MemResp
  mem_ctrl_cache.io.rd.mem(2).MemReq <> ld_20.io.MemReq
  ld_20.io.MemResp <> mem_ctrl_cache.io.rd.mem(2).MemResp
  mem_ctrl_cache.io.rd.mem(3).MemReq <> ld_22.io.MemReq
  ld_22.io.MemResp <> mem_ctrl_cache.io.rd.mem(3).MemResp
  mem_ctrl_cache.io.rd.mem(4).MemReq <> ld_25.io.MemReq
  ld_25.io.MemResp <> mem_ctrl_cache.io.rd.mem(4).MemResp
  mem_ctrl_cache.io.wr.mem(0).MemReq <> st_36.io.MemReq
  st_36.io.MemResp <> mem_ctrl_cache.io.wr.mem(0).MemResp



  /* ================================================================== *
   *                   PRINT SHARED CONNECTIONS                         *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  phiindvars_iv374.io.InData(0) <> const0.io.Out

  binaryOp_indvars_iv_next387.io.RightIO <> const1.io.Out

  binaryOp_indvars_iv_next28.io.RightIO <> const2.io.Out

  icmp_exitcond3937.io.RightIO <> const3.io.Out

  phisum_03418.io.InData(0) <> constf0.io.Out

  phisum_0_lcssa34.io.InData(0) <> constf1.io.Out

  Gep_arrayidx5.io.idx(0) <> phiindvars_iv374.io.Out(0)

  binaryOp_indvars_iv_next387.io.LeftIO <> phiindvars_iv374.io.Out(1)

  Gep_arrayidx1435.io.idx(0) <> phiindvars_iv374.io.Out(2)

  ld_6.io.GepAddr <> Gep_arrayidx5.io.Out(0)

  icmp_cmp43310.io.LeftIO <> ld_6.io.Out(0)

  sext13.io.Input <> ld_6.io.Out(1)

  Gep_arrayidx28.io.idx(0) <> binaryOp_indvars_iv_next387.io.Out(1)

  icmp_exitcond3937.io.LeftIO <> binaryOp_indvars_iv_next387.io.Out(2)

  ld_9.io.GepAddr <> Gep_arrayidx28.io.Out(0)

  icmp_cmp43310.io.RightIO <> ld_9.io.Out(0)

  sextwide_trip_count14.io.Input <> ld_9.io.Out(1)

  br_11.io.CmpIO <> icmp_cmp43310.io.Out(0)

  Gep_arrayidx719.io.idx(0) <> phiindvars_iv17.io.Out(0)

  Gep_arrayidx921.io.idx(0) <> phiindvars_iv17.io.Out(1)

  binaryOp_indvars_iv_next28.io.LeftIO <> phiindvars_iv17.io.Out(2)

  FP_add1227.io.LeftIO <> phisum_03418.io.Out(0)

  ld_20.io.GepAddr <> Gep_arrayidx719.io.Out(0)

  FP_mul26.io.LeftIO <> ld_20.io.Out(0)

  ld_22.io.GepAddr <> Gep_arrayidx921.io.Out(0)

  sextidxprom1023.io.Input <> ld_22.io.Out(0)

  Gep_arrayidx1124.io.idx(0) <> sextidxprom1023.io.Out(0)

  ld_25.io.GepAddr <> Gep_arrayidx1124.io.Out(0)

  FP_mul26.io.RightIO <> ld_25.io.Out(0)

  FP_add1227.io.RightIO <> FP_mul26.io.Out(0)

  icmp_exitcond29.io.LeftIO <> binaryOp_indvars_iv_next28.io.Out(1)

  br_30.io.CmpIO <> icmp_exitcond29.io.Out(0)

  st_36.io.inData <> phisum_0_lcssa34.io.Out(0)

  st_36.io.GepAddr <> Gep_arrayidx1435.io.Out(0)

  br_38.io.CmpIO <> icmp_exitcond3937.io.Out(0)

  st_36.io.Out(0).ready := true.B



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  br_38.io.PredOp(0) <> st_36.io.SuccOp(0)



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_40.io.Out

}

