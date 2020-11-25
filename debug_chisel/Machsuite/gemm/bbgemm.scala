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


class bbgemmDF(PtrsIn: Seq[Int] = List(64, 64, 64), ValsIn: Seq[Int] = List(), Returns: Seq[Int] = List())
			(implicit p: Parameters) extends DandelionAccelDCRModule(PtrsIn, ValsIn, Returns){


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  //Cache
  val mem_ctrl_cache = Module(new CacheMemoryEngine(ID = 0, NumRead = 3, NumWrite = 1))

  io.MemReq <> mem_ctrl_cache.io.cache.MemReq
  mem_ctrl_cache.io.cache.MemResp <> io.MemResp

  val ArgSplitter = Module(new SplitCallDCR(ptrsArgTypes = List(1, 1, 1), valsArgTypes = List()))
  ArgSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val Loop_0 = Module(new LoopBlockNode(NumIns = List(1, 1, 1, 1, 2, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 0))

  val Loop_1 = Module(new LoopBlockNode(NumIns = List(2, 1, 1, 1, 1, 2), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 1))

  val Loop_2 = Module(new LoopBlockNode(NumIns = List(1, 1, 1, 1, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 2))

  val Loop_3 = Module(new LoopBlockNode(NumIns = List(1, 1, 1, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 3))

  val Loop_4 = Module(new LoopBlockNode(NumIns = List(1, 1, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 4))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_entry1 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 1))

  val bb_loopkk3 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 3, NumPhi = 1, BID = 3))

  val bb_loopi6 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 3, NumPhi = 1, BID = 6))

  val bb_loopk9 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 5, NumPhi = 1, BID = 9))

  val bb_for_body913 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 10, NumPhi = 1, BID = 13))

  val bb_for_body1622 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 18, NumPhi = 1, BID = 22))

  val bb_for_inc2738 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 5, BID = 38))

  val bb_for_inc3042 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 5, BID = 42))

  val bb_for_inc3346 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 5, BID = 46))

  val bb_for_inc3650 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 5, BID = 50))

  val bb_for_end3854 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 54))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  br label %loopkk, !UID !3, !BB_UID !4
  val br_2 = Module(new UBranchNode(ID = 2))

  //  %indvars.iv84 = phi i64 [ 0, %entry ], [ %indvars.iv.next85, %for.inc36 ], !UID !5
  val phiindvars_iv844 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 4, Res = false))

  //  br label %loopi, !UID !6, !BB_UID !7
  val br_5 = Module(new UBranchNode(ID = 5))

  //  %indvars.iv82 = phi i64 [ 0, %loopkk ], [ %indvars.iv.next83, %for.inc33 ], !UID !8
  val phiindvars_iv827 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 7, Res = false))

  //  br label %loopk, !UID !9, !BB_UID !10
  val br_8 = Module(new UBranchNode(ID = 8))

  //  %indvars.iv78 = phi i64 [ 0, %loopi ], [ %indvars.iv.next79, %for.inc30 ], !UID !11
  val phiindvars_iv7810 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 10, Res = true))

  //  %0 = shl nsw i64 %indvars.iv78, 6, !UID !12
  val binaryOp_11 = Module(new ComputeNode(NumOuts = 1, ID = 11, opCode = "shl")(sign = false, Debug = false))

  //  br label %for.body9, !UID !13, !BB_UID !14
  val br_12 = Module(new UBranchNode(ID = 12))

  //  %indvars.iv71 = phi i64 [ 0, %loopk ], [ %indvars.iv.next72, %for.inc27 ], !UID !15
  val phiindvars_iv7114 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 3, ID = 14, Res = true))

  //  %1 = add nuw nsw i64 %indvars.iv71, %indvars.iv82, !UID !16
  val binaryOp_15 = Module(new ComputeNode(NumOuts = 1, ID = 15, opCode = "add")(sign = false, Debug = false))

  //  %2 = shl i64 %1, 6, !UID !17
  val binaryOp_16 = Module(new ComputeNode(NumOuts = 1, ID = 16, opCode = "shl")(sign = false, Debug = false))

  //  %3 = add nuw nsw i64 %indvars.iv71, %indvars.iv82, !UID !18
  val binaryOp_17 = Module(new ComputeNode(NumOuts = 1, ID = 17, opCode = "add")(sign = false, Debug = false))

  //  %4 = add nuw nsw i64 %3, %0, !UID !19
  val binaryOp_18 = Module(new ComputeNode(NumOuts = 1, ID = 18, opCode = "add")(sign = false, Debug = false))

  //  %arrayidx = getelementptr inbounds double, double* %m1, i64 %4, !UID !20
  val Gep_arrayidx19 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 19)(ElementSize = 8, ArraySize = List()))

  //  %5 = load double, double* %arrayidx, align 8, !tbaa !21, !UID !25
  val ld_20 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 20, RouteID = 0))

  //  br label %for.body16, !UID !26, !BB_UID !27
  val br_21 = Module(new UBranchNode(ID = 21))

  //  %indvars.iv = phi i64 [ 0, %for.body9 ], [ %indvars.iv.next, %for.body16 ], !UID !28
  val phiindvars_iv23 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 3, ID = 23, Res = true))

  //  %6 = add nuw nsw i64 %indvars.iv, %indvars.iv84, !UID !29
  val binaryOp_24 = Module(new ComputeNode(NumOuts = 1, ID = 24, opCode = "add")(sign = false, Debug = false))

  //  %7 = add nuw nsw i64 %6, %2, !UID !30
  val binaryOp_25 = Module(new ComputeNode(NumOuts = 1, ID = 25, opCode = "add")(sign = false, Debug = false))

  //  %arrayidx20 = getelementptr inbounds double, double* %m2, i64 %7, !UID !31
  val Gep_arrayidx2026 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 26)(ElementSize = 8, ArraySize = List()))

  //  %8 = load double, double* %arrayidx20, align 8, !tbaa !21, !UID !32
  val ld_27 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 27, RouteID = 1))

  //  %mul21 = fmul double %5, %8, !UID !33
  val FP_mul2128 = Module(new FPComputeNode(NumOuts = 1, ID = 28, opCode = "fmul")(fType))

  //  %9 = add nuw nsw i64 %indvars.iv, %indvars.iv84, !UID !34
  val binaryOp_29 = Module(new ComputeNode(NumOuts = 1, ID = 29, opCode = "add")(sign = false, Debug = false))

  //  %10 = add nuw nsw i64 %9, %0, !UID !35
  val binaryOp_30 = Module(new ComputeNode(NumOuts = 1, ID = 30, opCode = "add")(sign = false, Debug = false))

  //  %arrayidx25 = getelementptr inbounds double, double* %prod, i64 %10, !UID !36
  val Gep_arrayidx2531 = Module(new GepNode(NumIns = 1, NumOuts = 2, ID = 31)(ElementSize = 8, ArraySize = List()))

  //  %11 = load double, double* %arrayidx25, align 8, !tbaa !21, !UID !37
  val ld_32 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 32, RouteID = 2))

  //  %add26 = fadd double %11, %mul21, !UID !38
  val FP_add2633 = Module(new FPComputeNode(NumOuts = 1, ID = 33, opCode = "fadd")(fType))

  //  store double %add26, double* %arrayidx25, align 8, !tbaa !21, !UID !39
  val st_34 = Module(new UnTypStoreCache(NumPredOps = 0, NumSuccOps = 1, ID = 34, RouteID = 3))

  //  %indvars.iv.next = add nuw nsw i64 %indvars.iv, 1, !UID !40
  val binaryOp_indvars_iv_next35 = Module(new ComputeNode(NumOuts = 2, ID = 35, opCode = "add")(sign = false, Debug = false))

  //  %exitcond = icmp eq i64 %indvars.iv.next, 8, !UID !41
  val icmp_exitcond36 = Module(new ComputeNode(NumOuts = 1, ID = 36, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond, label %for.inc27, label %for.body16, !UID !42, !BB_UID !43
  val br_37 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 1, ID = 37))

  //  %indvars.iv.next72 = add nuw nsw i64 %indvars.iv71, 1, !UID !44
  val binaryOp_indvars_iv_next7239 = Module(new ComputeNode(NumOuts = 2, ID = 39, opCode = "add")(sign = false, Debug = false))

  //  %exitcond77 = icmp eq i64 %indvars.iv.next72, 8, !UID !45
  val icmp_exitcond7740 = Module(new ComputeNode(NumOuts = 1, ID = 40, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond77, label %for.inc30, label %for.body9, !UID !46, !BB_UID !47
  val br_41 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 41))

  //  %indvars.iv.next79 = add nuw nsw i64 %indvars.iv78, 1, !UID !48
  val binaryOp_indvars_iv_next7943 = Module(new ComputeNode(NumOuts = 2, ID = 43, opCode = "add")(sign = false, Debug = false))

  //  %exitcond81 = icmp eq i64 %indvars.iv.next79, 64, !UID !49
  val icmp_exitcond8144 = Module(new ComputeNode(NumOuts = 1, ID = 44, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond81, label %for.inc33, label %loopk, !UID !50, !BB_UID !51
  val br_45 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 45))

  //  %indvars.iv.next83 = add nuw nsw i64 %indvars.iv82, 8, !UID !52
  val binaryOp_indvars_iv_next8347 = Module(new ComputeNode(NumOuts = 2, ID = 47, opCode = "add")(sign = false, Debug = false))

  //  %cmp2 = icmp ult i64 %indvars.iv.next83, 64, !UID !53
  val icmp_cmp248 = Module(new ComputeNode(NumOuts = 1, ID = 48, opCode = "slt")(sign = false, Debug = false))

  //  br i1 %cmp2, label %loopi, label %for.inc36, !UID !54, !BB_UID !55
  val br_49 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 49))

  //  %indvars.iv.next85 = add nuw nsw i64 %indvars.iv84, 8, !UID !56
  val binaryOp_indvars_iv_next8551 = Module(new ComputeNode(NumOuts = 2, ID = 51, opCode = "add")(sign = false, Debug = false))

  //  %cmp = icmp ult i64 %indvars.iv.next85, 64, !UID !57
  val icmp_cmp52 = Module(new ComputeNode(NumOuts = 1, ID = 52, opCode = "slt")(sign = false, Debug = false))

  //  br i1 %cmp, label %loopkk, label %for.end38, !UID !58, !BB_UID !59
  val br_53 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 53))

  //  ret void, !UID !60, !BB_UID !61
  val ret_55 = Module(new RetNode2(retTypes = List(), ID = 55))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i64 0
  val const0 = Module(new ConstFastNode(value = 0, ID = 0))

  //i64 0
  val const1 = Module(new ConstFastNode(value = 0, ID = 1))

  //i64 0
  val const2 = Module(new ConstFastNode(value = 0, ID = 2))

  //i64 6
  val const3 = Module(new ConstFastNode(value = 6, ID = 3))

  //i64 0
  val const4 = Module(new ConstFastNode(value = 0, ID = 4))

  //i64 6
  val const5 = Module(new ConstFastNode(value = 6, ID = 5))

  //i64 0
  val const6 = Module(new ConstFastNode(value = 0, ID = 6))

  //i64 1
  val const7 = Module(new ConstFastNode(value = 1, ID = 7))

  //i64 8
  val const8 = Module(new ConstFastNode(value = 8, ID = 8))

  //i64 1
  val const9 = Module(new ConstFastNode(value = 1, ID = 9))

  //i64 8
  val const10 = Module(new ConstFastNode(value = 8, ID = 10))

  //i64 1
  val const11 = Module(new ConstFastNode(value = 1, ID = 11))

  //i64 64
  val const12 = Module(new ConstFastNode(value = 64, ID = 12))

  //i64 8
  val const13 = Module(new ConstFastNode(value = 8, ID = 13))

  //i64 64
  val const14 = Module(new ConstFastNode(value = 64, ID = 14))

  //i64 8
  val const15 = Module(new ConstFastNode(value = 8, ID = 15))

  //i64 64
  val const16 = Module(new ConstFastNode(value = 64, ID = 16))



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_entry1.io.predicateIn(0) <> ArgSplitter.io.Out.enable



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE LOOP                     *
   * ================================================================== */

  bb_loopkk3.io.predicateIn(0) <> Loop_4.io.activate_loop_start

  bb_loopkk3.io.predicateIn(1) <> Loop_4.io.activate_loop_back

  bb_loopi6.io.predicateIn(0) <> Loop_3.io.activate_loop_start

  bb_loopi6.io.predicateIn(1) <> Loop_3.io.activate_loop_back

  bb_loopk9.io.predicateIn(1) <> Loop_2.io.activate_loop_start

  bb_loopk9.io.predicateIn(0) <> Loop_2.io.activate_loop_back

  bb_for_body913.io.predicateIn(1) <> Loop_1.io.activate_loop_start

  bb_for_body913.io.predicateIn(0) <> Loop_1.io.activate_loop_back

  bb_for_body1622.io.predicateIn(1) <> Loop_0.io.activate_loop_start

  bb_for_body1622.io.predicateIn(0) <> Loop_0.io.activate_loop_back

  bb_for_inc2738.io.predicateIn(0) <> Loop_0.io.loopExit(0)

  bb_for_inc3042.io.predicateIn(0) <> Loop_1.io.loopExit(0)

  bb_for_inc3346.io.predicateIn(0) <> Loop_2.io.loopExit(0)

  bb_for_inc3650.io.predicateIn(0) <> Loop_3.io.loopExit(0)

  bb_for_end3854.io.predicateIn(0) <> Loop_4.io.loopExit(0)



  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */

  Loop_0.io.enable <> br_21.io.Out(0)

  Loop_0.io.loopBack(0) <> br_37.io.FalseOutput(0)

  Loop_0.io.loopFinish(0) <> br_37.io.TrueOutput(0)

  Loop_1.io.enable <> br_12.io.Out(0)

  Loop_1.io.loopBack(0) <> br_41.io.FalseOutput(0)

  Loop_1.io.loopFinish(0) <> br_41.io.TrueOutput(0)

  Loop_2.io.enable <> br_8.io.Out(0)

  Loop_2.io.loopBack(0) <> br_45.io.FalseOutput(0)

  Loop_2.io.loopFinish(0) <> br_45.io.TrueOutput(0)

  Loop_3.io.enable <> br_5.io.Out(0)

  Loop_3.io.loopBack(0) <> br_49.io.TrueOutput(0)

  Loop_3.io.loopFinish(0) <> br_49.io.FalseOutput(0)

  Loop_4.io.enable <> br_2.io.Out(0)

  Loop_4.io.loopBack(0) <> br_53.io.TrueOutput(0)

  Loop_4.io.loopFinish(0) <> br_53.io.FalseOutput(0)



  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

  Loop_0.io.InLiveIn(0) <> binaryOp_16.io.Out(0)

  Loop_0.io.InLiveIn(1) <> ld_20.io.Out(0)

  Loop_0.io.InLiveIn(2) <> Loop_1.io.OutLiveIn.elements("field1")(0)

  Loop_0.io.InLiveIn(3) <> Loop_1.io.OutLiveIn.elements("field0")(0)

  Loop_0.io.InLiveIn(4) <> Loop_1.io.OutLiveIn.elements("field2")(0)

  Loop_0.io.InLiveIn(5) <> Loop_1.io.OutLiveIn.elements("field3")(0)

  Loop_1.io.InLiveIn(0) <> binaryOp_11.io.Out(0)

  Loop_1.io.InLiveIn(1) <> Loop_2.io.OutLiveIn.elements("field1")(0)

  Loop_1.io.InLiveIn(2) <> Loop_2.io.OutLiveIn.elements("field3")(0)

  Loop_1.io.InLiveIn(3) <> Loop_2.io.OutLiveIn.elements("field4")(0)

  Loop_1.io.InLiveIn(4) <> Loop_2.io.OutLiveIn.elements("field2")(0)

  Loop_1.io.InLiveIn(5) <> Loop_2.io.OutLiveIn.elements("field0")(0)

  Loop_2.io.InLiveIn(0) <> phiindvars_iv827.io.Out(0)

  Loop_2.io.InLiveIn(1) <> Loop_3.io.OutLiveIn.elements("field1")(0)

  Loop_2.io.InLiveIn(2) <> Loop_3.io.OutLiveIn.elements("field2")(0)

  Loop_2.io.InLiveIn(3) <> Loop_3.io.OutLiveIn.elements("field0")(0)

  Loop_2.io.InLiveIn(4) <> Loop_3.io.OutLiveIn.elements("field3")(0)

  Loop_3.io.InLiveIn(0) <> phiindvars_iv844.io.Out(0)

  Loop_3.io.InLiveIn(1) <> Loop_4.io.OutLiveIn.elements("field1")(0)

  Loop_3.io.InLiveIn(2) <> Loop_4.io.OutLiveIn.elements("field0")(0)

  Loop_3.io.InLiveIn(3) <> Loop_4.io.OutLiveIn.elements("field2")(0)

  Loop_4.io.InLiveIn(0) <> ArgSplitter.io.Out.dataPtrs.elements("field0")(0)

  Loop_4.io.InLiveIn(1) <> ArgSplitter.io.Out.dataPtrs.elements("field1")(0)

  Loop_4.io.InLiveIn(2) <> ArgSplitter.io.Out.dataPtrs.elements("field2")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */

  binaryOp_25.io.RightIO <> Loop_0.io.OutLiveIn.elements("field0")(0)

  FP_mul2128.io.LeftIO <> Loop_0.io.OutLiveIn.elements("field1")(0)

  Gep_arrayidx2026.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field2")(0)

  binaryOp_30.io.RightIO <> Loop_0.io.OutLiveIn.elements("field3")(0)

  binaryOp_24.io.RightIO <> Loop_0.io.OutLiveIn.elements("field4")(0)

  binaryOp_29.io.RightIO <> Loop_0.io.OutLiveIn.elements("field4")(1)

  Gep_arrayidx2531.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field5")(0)

  binaryOp_18.io.RightIO <> Loop_1.io.OutLiveIn.elements("field0")(1)

  Gep_arrayidx19.io.baseAddress <> Loop_1.io.OutLiveIn.elements("field4")(0)

  binaryOp_15.io.RightIO <> Loop_1.io.OutLiveIn.elements("field5")(0)

  binaryOp_17.io.RightIO <> Loop_1.io.OutLiveIn.elements("field5")(1)



  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP LIVE OUT DEPENDENCIES                       *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP CARRY DEPENDENCIES                          *
   * ================================================================== */

  Loop_0.io.CarryDepenIn(0) <> binaryOp_indvars_iv_next35.io.Out(0)

  Loop_1.io.CarryDepenIn(0) <> binaryOp_indvars_iv_next7239.io.Out(0)

  Loop_2.io.CarryDepenIn(0) <> binaryOp_indvars_iv_next7943.io.Out(0)

  Loop_3.io.CarryDepenIn(0) <> binaryOp_indvars_iv_next8347.io.Out(0)

  Loop_4.io.CarryDepenIn(0) <> binaryOp_indvars_iv_next8551.io.Out(0)



  /* ================================================================== *
   *                   LOOP DATA CARRY DEPENDENCIES                     *
   * ================================================================== */

  phiindvars_iv23.io.InData(1) <> Loop_0.io.CarryDepenOut.elements("field0")(0)

  phiindvars_iv7114.io.InData(1) <> Loop_1.io.CarryDepenOut.elements("field0")(0)

  phiindvars_iv7810.io.InData(1) <> Loop_2.io.CarryDepenOut.elements("field0")(0)

  phiindvars_iv827.io.InData(1) <> Loop_3.io.CarryDepenOut.elements("field0")(0)

  phiindvars_iv844.io.InData(1) <> Loop_4.io.CarryDepenOut.elements("field0")(0)



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  br_2.io.enable <> bb_entry1.io.Out(0)


  const0.io.enable <> bb_loopkk3.io.Out(0)

  phiindvars_iv844.io.enable <> bb_loopkk3.io.Out(1)


  br_5.io.enable <> bb_loopkk3.io.Out(2)


  const1.io.enable <> bb_loopi6.io.Out(0)

  phiindvars_iv827.io.enable <> bb_loopi6.io.Out(1)


  br_8.io.enable <> bb_loopi6.io.Out(2)


  const2.io.enable <> bb_loopk9.io.Out(0)

  const3.io.enable <> bb_loopk9.io.Out(1)

  phiindvars_iv7810.io.enable <> bb_loopk9.io.Out(2)


  binaryOp_11.io.enable <> bb_loopk9.io.Out(3)


  br_12.io.enable <> bb_loopk9.io.Out(4)


  const4.io.enable <> bb_for_body913.io.Out(0)

  const5.io.enable <> bb_for_body913.io.Out(1)

  phiindvars_iv7114.io.enable <> bb_for_body913.io.Out(2)


  binaryOp_15.io.enable <> bb_for_body913.io.Out(3)


  binaryOp_16.io.enable <> bb_for_body913.io.Out(4)


  binaryOp_17.io.enable <> bb_for_body913.io.Out(5)


  binaryOp_18.io.enable <> bb_for_body913.io.Out(6)


  Gep_arrayidx19.io.enable <> bb_for_body913.io.Out(7)


  ld_20.io.enable <> bb_for_body913.io.Out(8)


  br_21.io.enable <> bb_for_body913.io.Out(9)


  const6.io.enable <> bb_for_body1622.io.Out(0)

  const7.io.enable <> bb_for_body1622.io.Out(1)

  const8.io.enable <> bb_for_body1622.io.Out(2)

  phiindvars_iv23.io.enable <> bb_for_body1622.io.Out(3)


  binaryOp_24.io.enable <> bb_for_body1622.io.Out(4)


  binaryOp_25.io.enable <> bb_for_body1622.io.Out(5)


  Gep_arrayidx2026.io.enable <> bb_for_body1622.io.Out(6)


  ld_27.io.enable <> bb_for_body1622.io.Out(7)


  FP_mul2128.io.enable <> bb_for_body1622.io.Out(8)


  binaryOp_29.io.enable <> bb_for_body1622.io.Out(9)


  binaryOp_30.io.enable <> bb_for_body1622.io.Out(10)


  Gep_arrayidx2531.io.enable <> bb_for_body1622.io.Out(11)


  ld_32.io.enable <> bb_for_body1622.io.Out(12)


  FP_add2633.io.enable <> bb_for_body1622.io.Out(13)


  st_34.io.enable <> bb_for_body1622.io.Out(14)


  binaryOp_indvars_iv_next35.io.enable <> bb_for_body1622.io.Out(15)


  icmp_exitcond36.io.enable <> bb_for_body1622.io.Out(16)


  br_37.io.enable <> bb_for_body1622.io.Out(17)


  const9.io.enable <> bb_for_inc2738.io.Out(0)

  const10.io.enable <> bb_for_inc2738.io.Out(1)

  binaryOp_indvars_iv_next7239.io.enable <> bb_for_inc2738.io.Out(2)


  icmp_exitcond7740.io.enable <> bb_for_inc2738.io.Out(3)


  br_41.io.enable <> bb_for_inc2738.io.Out(4)


  const11.io.enable <> bb_for_inc3042.io.Out(0)

  const12.io.enable <> bb_for_inc3042.io.Out(1)

  binaryOp_indvars_iv_next7943.io.enable <> bb_for_inc3042.io.Out(2)


  icmp_exitcond8144.io.enable <> bb_for_inc3042.io.Out(3)


  br_45.io.enable <> bb_for_inc3042.io.Out(4)


  const13.io.enable <> bb_for_inc3346.io.Out(0)

  const14.io.enable <> bb_for_inc3346.io.Out(1)

  binaryOp_indvars_iv_next8347.io.enable <> bb_for_inc3346.io.Out(2)


  icmp_cmp248.io.enable <> bb_for_inc3346.io.Out(3)


  br_49.io.enable <> bb_for_inc3346.io.Out(4)


  const15.io.enable <> bb_for_inc3650.io.Out(0)

  const16.io.enable <> bb_for_inc3650.io.Out(1)

  binaryOp_indvars_iv_next8551.io.enable <> bb_for_inc3650.io.Out(2)


  icmp_cmp52.io.enable <> bb_for_inc3650.io.Out(3)


  br_53.io.enable <> bb_for_inc3650.io.Out(4)


  ret_55.io.In.enable <> bb_for_end3854.io.Out(0)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  phiindvars_iv844.io.Mask <> bb_loopkk3.io.MaskBB(0)

  phiindvars_iv827.io.Mask <> bb_loopi6.io.MaskBB(0)

  phiindvars_iv7810.io.Mask <> bb_loopk9.io.MaskBB(0)

  phiindvars_iv7114.io.Mask <> bb_for_body913.io.MaskBB(0)

  phiindvars_iv23.io.Mask <> bb_for_body1622.io.MaskBB(0)



  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */

  mem_ctrl_cache.io.rd.mem(0).MemReq <> ld_20.io.MemReq
  ld_20.io.MemResp <> mem_ctrl_cache.io.rd.mem(0).MemResp
  mem_ctrl_cache.io.rd.mem(1).MemReq <> ld_27.io.MemReq
  ld_27.io.MemResp <> mem_ctrl_cache.io.rd.mem(1).MemResp
  mem_ctrl_cache.io.rd.mem(2).MemReq <> ld_32.io.MemReq
  ld_32.io.MemResp <> mem_ctrl_cache.io.rd.mem(2).MemResp
  mem_ctrl_cache.io.wr.mem(0).MemReq <> st_34.io.MemReq
  st_34.io.MemResp <> mem_ctrl_cache.io.wr.mem(0).MemResp



  /* ================================================================== *
   *                   PRINT SHARED CONNECTIONS                         *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  phiindvars_iv844.io.InData(0) <> const0.io.Out

  phiindvars_iv827.io.InData(0) <> const1.io.Out

  phiindvars_iv7810.io.InData(0) <> const2.io.Out

  binaryOp_11.io.RightIO <> const3.io.Out

  phiindvars_iv7114.io.InData(0) <> const4.io.Out

  binaryOp_16.io.RightIO <> const5.io.Out

  phiindvars_iv23.io.InData(0) <> const6.io.Out

  binaryOp_indvars_iv_next35.io.RightIO <> const7.io.Out

  icmp_exitcond36.io.RightIO <> const8.io.Out

  binaryOp_indvars_iv_next7239.io.RightIO <> const9.io.Out

  icmp_exitcond7740.io.RightIO <> const10.io.Out

  binaryOp_indvars_iv_next7943.io.RightIO <> const11.io.Out

  icmp_exitcond8144.io.RightIO <> const12.io.Out

  binaryOp_indvars_iv_next8347.io.RightIO <> const13.io.Out

  icmp_cmp248.io.RightIO <> const14.io.Out

  binaryOp_indvars_iv_next8551.io.RightIO <> const15.io.Out

  icmp_cmp52.io.RightIO <> const16.io.Out

  binaryOp_indvars_iv_next8551.io.LeftIO <> phiindvars_iv844.io.Out(1)

  binaryOp_indvars_iv_next8347.io.LeftIO <> phiindvars_iv827.io.Out(1)

  binaryOp_11.io.LeftIO <> phiindvars_iv7810.io.Out(0)

  binaryOp_indvars_iv_next7943.io.LeftIO <> phiindvars_iv7810.io.Out(1)

  binaryOp_15.io.LeftIO <> phiindvars_iv7114.io.Out(0)

  binaryOp_17.io.LeftIO <> phiindvars_iv7114.io.Out(1)

  binaryOp_indvars_iv_next7239.io.LeftIO <> phiindvars_iv7114.io.Out(2)

  binaryOp_16.io.LeftIO <> binaryOp_15.io.Out(0)

  binaryOp_18.io.LeftIO <> binaryOp_17.io.Out(0)

  Gep_arrayidx19.io.idx(0) <> binaryOp_18.io.Out(0)

  ld_20.io.GepAddr <> Gep_arrayidx19.io.Out(0)

  binaryOp_24.io.LeftIO <> phiindvars_iv23.io.Out(0)

  binaryOp_29.io.LeftIO <> phiindvars_iv23.io.Out(1)

  binaryOp_indvars_iv_next35.io.LeftIO <> phiindvars_iv23.io.Out(2)

  binaryOp_25.io.LeftIO <> binaryOp_24.io.Out(0)

  Gep_arrayidx2026.io.idx(0) <> binaryOp_25.io.Out(0)

  ld_27.io.GepAddr <> Gep_arrayidx2026.io.Out(0)

  FP_mul2128.io.RightIO <> ld_27.io.Out(0)

  FP_add2633.io.RightIO <> FP_mul2128.io.Out(0)

  binaryOp_30.io.LeftIO <> binaryOp_29.io.Out(0)

  Gep_arrayidx2531.io.idx(0) <> binaryOp_30.io.Out(0)

  ld_32.io.GepAddr <> Gep_arrayidx2531.io.Out(0)

  st_34.io.GepAddr <> Gep_arrayidx2531.io.Out(1)

  FP_add2633.io.LeftIO <> ld_32.io.Out(0)

  st_34.io.inData <> FP_add2633.io.Out(0)

  icmp_exitcond36.io.LeftIO <> binaryOp_indvars_iv_next35.io.Out(1)

  br_37.io.CmpIO <> icmp_exitcond36.io.Out(0)

  icmp_exitcond7740.io.LeftIO <> binaryOp_indvars_iv_next7239.io.Out(1)

  br_41.io.CmpIO <> icmp_exitcond7740.io.Out(0)

  icmp_exitcond8144.io.LeftIO <> binaryOp_indvars_iv_next7943.io.Out(1)

  br_45.io.CmpIO <> icmp_exitcond8144.io.Out(0)

  icmp_cmp248.io.LeftIO <> binaryOp_indvars_iv_next8347.io.Out(1)

  br_49.io.CmpIO <> icmp_cmp248.io.Out(0)

  icmp_cmp52.io.LeftIO <> binaryOp_indvars_iv_next8551.io.Out(1)

  br_53.io.CmpIO <> icmp_cmp52.io.Out(0)

  st_34.io.Out(0).ready := true.B



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  br_37.io.PredOp(0) <> st_34.io.SuccOp(0)



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_55.io.Out

}

