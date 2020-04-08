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


class spmvCRSDF(PtrsIn: Seq[Int] = List(32, 32, 32, 32, 32), ValsIn: Seq[Int] = List(), Returns: Seq[Int] = List())
			(implicit p: Parameters) extends DandelionAccelDCRModule(PtrsIn, ValsIn, Returns){


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  val MemCtrl = Module(new CacheMemoryEngine(ID = 0, NumRead = 5, NumWrite = 1))

  io.MemReq <> MemCtrl.io.cache.MemReq
  MemCtrl.io.cache.MemResp <> io.MemResp
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

  val bb_entry0 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 0))

  val bb_for_body1 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 10, NumPhi = 1, BID = 1))

  val bb_for_body5_lr_ph2 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 3, BID = 2))

  val bb_for_body53 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 16, NumPhi = 2, BID = 3))

  val bb_for_end_loopexit4 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 4))

  val bb_for_end5 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 7, NumPhi = 1, BID = 5))

  val bb_for_end176 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 6))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  br label %for.body, !UID !3, !BB_UID !4
  val br_0 = Module(new UBranchNode(ID = 0))

  //  %indvars.iv37 = phi i64 [ 0, %entry ], [ %indvars.iv.next38, %for.end ], !UID !5
  val phiindvars_iv371 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 3, ID = 1, Res = true))

  //  %arrayidx = getelementptr inbounds i32, i32* %rowDelimiters, i64 %indvars.iv37, !UID !6
  val Gep_arrayidx2 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 2)(ElementSize = 8, ArraySize = List()))

  //  %0 = load i32, i32* %arrayidx, align 4, !tbaa !7, !UID !11
  val ld_3 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 2, ID = 3, RouteID = 0))

  //  %indvars.iv.next38 = add nuw nsw i64 %indvars.iv37, 1, !UID !12
  val binaryOp_indvars_iv_next384 = Module(new ComputeNode(NumOuts = 3, ID = 4, opCode = "add")(sign = false, Debug = false))

  //  %arrayidx2 = getelementptr inbounds i32, i32* %rowDelimiters, i64 %indvars.iv.next38, !UID !13
  val Gep_arrayidx25 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 5)(ElementSize = 8, ArraySize = List()))

  //  %1 = load i32, i32* %arrayidx2, align 4, !tbaa !7, !UID !14
  val ld_6 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 2, ID = 6, RouteID = 1))

  //  %cmp433 = icmp slt i32 %0, %1, !UID !15
  val icmp_cmp4337 = Module(new ComputeNode(NumOuts = 1, ID = 7, opCode = "slt")(sign = true, Debug = false))

  //  br i1 %cmp433, label %for.body5.lr.ph, label %for.end, !UID !16, !BB_UID !17
  val br_8 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 8))

  //  %2 = sext i32 %0 to i64, !UID !18
  val sext9 = Module(new SextNode(NumOuts = 1))

  //  %wide.trip.count = sext i32 %1 to i64, !UID !19
  val sextwide_trip_count10 = Module(new SextNode(NumOuts = 1))

  //  br label %for.body5, !UID !20, !BB_UID !21
  val br_11 = Module(new UBranchNode(ID = 11))

  //  %indvars.iv = phi i64 [ %2, %for.body5.lr.ph ], [ %indvars.iv.next, %for.body5 ], !UID !22
  val phiindvars_iv12 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 3, ID = 12, Res = true))

  //  %sum.034 = phi double [ 0.000000e+00, %for.body5.lr.ph ], [ %add12, %for.body5 ], !UID !23
  val phisum_03413 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 1, ID = 13, Res = true))

  //  %arrayidx7 = getelementptr inbounds double, double* %val, i64 %indvars.iv, !UID !24
  val Gep_arrayidx714 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 14)(ElementSize = 8, ArraySize = List()))

  //  %3 = load double, double* %arrayidx7, align 8, !tbaa !25, !UID !27
  val ld_15 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 15, RouteID = 2))

  //  %arrayidx9 = getelementptr inbounds i32, i32* %cols, i64 %indvars.iv, !UID !28
  val Gep_arrayidx916 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 16)(ElementSize = 8, ArraySize = List()))

  //  %4 = load i32, i32* %arrayidx9, align 4, !tbaa !7, !UID !29
  val ld_17 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 17, RouteID = 3))

  //  %idxprom10 = sext i32 %4 to i64, !UID !30
  val sextidxprom1018 = Module(new SextNode(NumOuts = 1))

  //  %arrayidx11 = getelementptr inbounds double, double* %vec, i64 %idxprom10, !UID !31
  val Gep_arrayidx1119 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 19)(ElementSize = 8, ArraySize = List()))

  //  %5 = load double, double* %arrayidx11, align 8, !tbaa !25, !UID !32
  val ld_20 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 20, RouteID = 4))

  //  %mul = fmul double %3, %5, !UID !33
  val FP_mul21 = Module(new FPComputeNode(NumOuts = 1, ID = 21, opCode = "fmul")(t = p(FTYP)))

  //  %add12 = fadd double %sum.034, %mul, !UID !34
  val FP_add1222 = Module(new FPComputeNode(NumOuts = 2, ID = 22, opCode = "fadd")(t = p(FTYP)))

  //  %indvars.iv.next = add nsw i64 %indvars.iv, 1, !UID !35
  val binaryOp_indvars_iv_next23 = Module(new ComputeNode(NumOuts = 2, ID = 23, opCode = "add")(sign = false, Debug = false))

  //  %exitcond = icmp eq i64 %indvars.iv.next, %wide.trip.count, !UID !36
  val icmp_exitcond24 = Module(new ComputeNode(NumOuts = 1, ID = 24, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond, label %for.end.loopexit, label %for.body5, !UID !37, !BB_UID !38
  val br_25 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 25))

  //  br label %for.end
  val br_26 = Module(new UBranchNode(ID = 26))

  //  %sum.0.lcssa = phi double [ 0.000000e+00, %for.body ], [ %add12, %for.end.loopexit ], !UID !39
  val phisum_0_lcssa27 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 1, ID = 27, Res = true))

  //  %arrayidx14 = getelementptr inbounds double, double* %out, i64 %indvars.iv37, !UID !40
  val Gep_arrayidx1428 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 28)(ElementSize = 8, ArraySize = List()))

  //  store double %sum.0.lcssa, double* %arrayidx14, align 8, !tbaa !25, !UID !41
  val st_29 = Module(new UnTypStoreCache(NumPredOps = 0, NumSuccOps = 1, ID = 29, RouteID = 5))

  //  %exitcond39 = icmp eq i64 %indvars.iv.next38, 494, !UID !42
  val icmp_exitcond3930 = Module(new ComputeNode(NumOuts = 1, ID = 30, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond39, label %for.end17, label %for.body, !UID !43, !BB_UID !44
  val br_31 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 1, ID = 31))

  //  ret void, !UID !45, !BB_UID !46
  val ret_32 = Module(new RetNode2(retTypes = List(), ID = 32))



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
  val constf0 = Module(new ConstNode(value = 0x0, ID = 0))

  //double 0.000000e+00
  val constf1 = Module(new ConstNode(value = 0x0, ID = 1))



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_entry0.io.predicateIn(0) <> ArgSplitter.io.Out.enable

  bb_for_body5_lr_ph2.io.predicateIn(0) <> br_8.io.TrueOutput(0)

  bb_for_end5.io.predicateIn(1) <> br_8.io.FalseOutput(0)

  bb_for_end5.io.predicateIn(0) <> br_26.io.Out(0)



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE LOOP                     *
   * ================================================================== */

  bb_for_body1.io.predicateIn(1) <> Loop_1.io.activate_loop_start

  bb_for_body1.io.predicateIn(0) <> Loop_1.io.activate_loop_back

  bb_for_body53.io.predicateIn(1) <> Loop_0.io.activate_loop_start

  bb_for_body53.io.predicateIn(0) <> Loop_0.io.activate_loop_back

  bb_for_end_loopexit4.io.predicateIn(0) <> Loop_0.io.loopExit(0)

  bb_for_end176.io.predicateIn(0) <> Loop_1.io.loopExit(0)



  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */

  Loop_0.io.enable <> br_11.io.Out(0)

  Loop_0.io.loopBack(0) <> br_25.io.FalseOutput(0)

  Loop_0.io.loopFinish(0) <> br_25.io.TrueOutput(0)

  Loop_1.io.enable <> br_0.io.Out(0)

  Loop_1.io.loopBack(0) <> br_31.io.FalseOutput(0)

  Loop_1.io.loopFinish(0) <> br_31.io.TrueOutput(0)



  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

  Loop_0.io.InLiveIn(0) <> sext9.io.Out(0)

  Loop_0.io.InLiveIn(1) <> sextwide_trip_count10.io.Out(0)

  Loop_0.io.InLiveIn(2) <> Loop_1.io.OutLiveIn.elements("field1")(0)

  Loop_0.io.InLiveIn(3) <> Loop_1.io.OutLiveIn.elements("field2")(0)

  Loop_0.io.InLiveIn(4) <> Loop_1.io.OutLiveIn.elements("field3")(0)

  Loop_1.io.InLiveIn(0) <> ArgSplitter.io.Out.dataPtrs.elements("field2")(0)

  Loop_1.io.InLiveIn(1) <> ArgSplitter.io.Out.dataPtrs.elements("field0")(0)

  Loop_1.io.InLiveIn(2) <> ArgSplitter.io.Out.dataPtrs.elements("field1")(0)

  Loop_1.io.InLiveIn(3) <> ArgSplitter.io.Out.dataPtrs.elements("field3")(0)

  Loop_1.io.InLiveIn(4) <> ArgSplitter.io.Out.dataPtrs.elements("field4")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */

  phiindvars_iv12.io.InData(0) <> Loop_0.io.OutLiveIn.elements("field0")(0)

  icmp_exitcond24.io.RightIO <> Loop_0.io.OutLiveIn.elements("field1")(0)

  Gep_arrayidx714.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field2")(0)

  Gep_arrayidx916.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field3")(0)

  Gep_arrayidx1119.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field4")(0)

  Gep_arrayidx2.io.baseAddress <> Loop_1.io.OutLiveIn.elements("field0")(0)

  Gep_arrayidx25.io.baseAddress <> Loop_1.io.OutLiveIn.elements("field0")(1)

  Gep_arrayidx1428.io.baseAddress <> Loop_1.io.OutLiveIn.elements("field4")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */

  Loop_0.io.InLiveOut(0) <> FP_add1222.io.Out(0)



  /* ================================================================== *
   *                   LOOP LIVE OUT DEPENDENCIES                       *
   * ================================================================== */

  phisum_0_lcssa27.io.InData(1) <> Loop_0.io.OutLiveOut.elements("field0")(0)



  /* ================================================================== *
   *                   LOOP CARRY DEPENDENCIES                          *
   * ================================================================== */

  Loop_0.io.CarryDepenIn(0) <> FP_add1222.io.Out(1)

  Loop_0.io.CarryDepenIn(1) <> binaryOp_indvars_iv_next23.io.Out(0)

  Loop_1.io.CarryDepenIn(0) <> binaryOp_indvars_iv_next384.io.Out(0)



  /* ================================================================== *
   *                   LOOP DATA CARRY DEPENDENCIES                     *
   * ================================================================== */

  phisum_03413.io.InData(1) <> Loop_0.io.CarryDepenOut.elements("field0")(0)

  phiindvars_iv12.io.InData(1) <> Loop_0.io.CarryDepenOut.elements("field1")(0)

  phiindvars_iv371.io.InData(1) <> Loop_1.io.CarryDepenOut.elements("field0")(0)



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  br_0.io.enable <> bb_entry0.io.Out(0)


  const0.io.enable <> bb_for_body1.io.Out(0)

  const1.io.enable <> bb_for_body1.io.Out(1)

  phiindvars_iv371.io.enable <> bb_for_body1.io.Out(2)


  Gep_arrayidx2.io.enable <> bb_for_body1.io.Out(3)


  ld_3.io.enable <> bb_for_body1.io.Out(4)


  binaryOp_indvars_iv_next384.io.enable <> bb_for_body1.io.Out(5)


  Gep_arrayidx25.io.enable <> bb_for_body1.io.Out(6)


  ld_6.io.enable <> bb_for_body1.io.Out(7)


  icmp_cmp4337.io.enable <> bb_for_body1.io.Out(8)


  br_8.io.enable <> bb_for_body1.io.Out(9)


  sext9.io.enable <> bb_for_body5_lr_ph2.io.Out(0)


  sextwide_trip_count10.io.enable <> bb_for_body5_lr_ph2.io.Out(1)


  br_11.io.enable <> bb_for_body5_lr_ph2.io.Out(2)


  constf0.io.enable <> bb_for_body53.io.Out(0)

  const2.io.enable <> bb_for_body53.io.Out(1)

  phiindvars_iv12.io.enable <> bb_for_body53.io.Out(2)


  phisum_03413.io.enable <> bb_for_body53.io.Out(3)


  Gep_arrayidx714.io.enable <> bb_for_body53.io.Out(4)


  ld_15.io.enable <> bb_for_body53.io.Out(5)


  Gep_arrayidx916.io.enable <> bb_for_body53.io.Out(6)


  ld_17.io.enable <> bb_for_body53.io.Out(7)


  sextidxprom1018.io.enable <> bb_for_body53.io.Out(8)


  Gep_arrayidx1119.io.enable <> bb_for_body53.io.Out(9)


  ld_20.io.enable <> bb_for_body53.io.Out(10)


  FP_mul21.io.enable <> bb_for_body53.io.Out(11)


  FP_add1222.io.enable <> bb_for_body53.io.Out(12)


  binaryOp_indvars_iv_next23.io.enable <> bb_for_body53.io.Out(13)


  icmp_exitcond24.io.enable <> bb_for_body53.io.Out(14)


  br_25.io.enable <> bb_for_body53.io.Out(15)


  br_26.io.enable <> bb_for_end_loopexit4.io.Out(0)


  constf1.io.enable <> bb_for_end5.io.Out(0)

  const3.io.enable <> bb_for_end5.io.Out(1)

  phisum_0_lcssa27.io.enable <> bb_for_end5.io.Out(2)


  Gep_arrayidx1428.io.enable <> bb_for_end5.io.Out(3)


  st_29.io.enable <> bb_for_end5.io.Out(4)


  icmp_exitcond3930.io.enable <> bb_for_end5.io.Out(5)


  br_31.io.enable <> bb_for_end5.io.Out(6)


  ret_32.io.In.enable <> bb_for_end176.io.Out(0)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  phiindvars_iv371.io.Mask <> bb_for_body1.io.MaskBB(0)

  phiindvars_iv12.io.Mask <> bb_for_body53.io.MaskBB(0)

  phisum_03413.io.Mask <> bb_for_body53.io.MaskBB(1)

  phisum_0_lcssa27.io.Mask <> bb_for_end5.io.MaskBB(0)



  /* ================================================================== *
   *                   PRINT ALLOCA OFFSET                              *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */

  MemCtrl.io.rd.mem(0).MemReq <> ld_3.io.MemReq

  ld_3.io.MemResp <> MemCtrl.io.rd.mem(0).MemResp

  MemCtrl.io.rd.mem(1).MemReq <> ld_6.io.MemReq

  ld_6.io.MemResp <> MemCtrl.io.rd.mem(1).MemResp

  MemCtrl.io.rd.mem(2).MemReq <> ld_15.io.MemReq

  ld_15.io.MemResp <> MemCtrl.io.rd.mem(2).MemResp

  MemCtrl.io.rd.mem(3).MemReq <> ld_17.io.MemReq

  ld_17.io.MemResp <> MemCtrl.io.rd.mem(3).MemResp

  MemCtrl.io.rd.mem(4).MemReq <> ld_20.io.MemReq

  ld_20.io.MemResp <> MemCtrl.io.rd.mem(4).MemResp

  MemCtrl.io.wr.mem(0).MemReq <> st_29.io.MemReq

  st_29.io.MemResp <> MemCtrl.io.wr.mem(0).MemResp



  /* ================================================================== *
   *                   PRINT SHARED CONNECTIONS                         *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  phiindvars_iv371.io.InData(0) <> const0.io.Out

  binaryOp_indvars_iv_next384.io.RightIO <> const1.io.Out

  binaryOp_indvars_iv_next23.io.RightIO <> const2.io.Out

  icmp_exitcond3930.io.RightIO <> const3.io.Out

  phisum_03413.io.InData(0) <> constf0.io.Out(0)

  phisum_0_lcssa27.io.InData(0) <> constf1.io.Out(0)

  Gep_arrayidx2.io.idx(0) <> phiindvars_iv371.io.Out(0)

  binaryOp_indvars_iv_next384.io.LeftIO <> phiindvars_iv371.io.Out(1)

  Gep_arrayidx1428.io.idx(0) <> phiindvars_iv371.io.Out(2)

  ld_3.io.GepAddr <> Gep_arrayidx2.io.Out(0)

  icmp_cmp4337.io.LeftIO <> ld_3.io.Out(0)

  sext9.io.Input <> ld_3.io.Out(1)

  Gep_arrayidx25.io.idx(0) <> binaryOp_indvars_iv_next384.io.Out(1)

  icmp_exitcond3930.io.LeftIO <> binaryOp_indvars_iv_next384.io.Out(2)

  ld_6.io.GepAddr <> Gep_arrayidx25.io.Out(0)

  icmp_cmp4337.io.RightIO <> ld_6.io.Out(0)

  sextwide_trip_count10.io.Input <> ld_6.io.Out(1)

  br_8.io.CmpIO <> icmp_cmp4337.io.Out(0)

  Gep_arrayidx714.io.idx(0) <> phiindvars_iv12.io.Out(0)

  Gep_arrayidx916.io.idx(0) <> phiindvars_iv12.io.Out(1)

  binaryOp_indvars_iv_next23.io.LeftIO <> phiindvars_iv12.io.Out(2)

  FP_add1222.io.LeftIO <> phisum_03413.io.Out(0)

  ld_15.io.GepAddr <> Gep_arrayidx714.io.Out(0)

  FP_mul21.io.LeftIO <> ld_15.io.Out(0)

  ld_17.io.GepAddr <> Gep_arrayidx916.io.Out(0)

  sextidxprom1018.io.Input <> ld_17.io.Out(0)

  Gep_arrayidx1119.io.idx(0) <> sextidxprom1018.io.Out(0)

  ld_20.io.GepAddr <> Gep_arrayidx1119.io.Out(0)

  FP_mul21.io.RightIO <> ld_20.io.Out(0)

  FP_add1222.io.RightIO <> FP_mul21.io.Out(0)

  icmp_exitcond24.io.LeftIO <> binaryOp_indvars_iv_next23.io.Out(1)

  br_25.io.CmpIO <> icmp_exitcond24.io.Out(0)

  st_29.io.inData <> phisum_0_lcssa27.io.Out(0)

  st_29.io.GepAddr <> Gep_arrayidx1428.io.Out(0)

  br_31.io.CmpIO <> icmp_exitcond3930.io.Out(0)

  st_29.io.Out(0).ready := true.B



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  br_31.io.PredOp(0) <> st_29.io.SuccOp(0)



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_32.io.Out

}

