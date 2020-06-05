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


class md_kernelDF(PtrsIn: Seq[Int] = List(64, 64, 64, 64, 64, 64, 64), ValsIn: Seq[Int] = List(), Returns: Seq[Int] = List())
			(implicit p: Parameters) extends DandelionAccelDCRModule(PtrsIn, ValsIn, Returns){


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  //Cache
  val mem_ctrl_cache = Module(new CacheMemoryEngine(ID = 0, NumRead = 7, NumWrite = 3))

  io.MemReq <> mem_ctrl_cache.io.cache.MemReq
  mem_ctrl_cache.io.cache.MemResp <> io.MemResp

  val SharedFPU = Module(new SharedFPU(NumOps = 1, PipeDepth = 32)(fType))

  val ArgSplitter = Module(new SplitCallDCR(ptrsArgTypes = List(1, 1, 1, 1, 1, 1, 1), valsArgTypes = List()))
  ArgSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val Loop_0 = Module(new LoopBlockNode(NumIns = List(1, 1, 1, 1, 1, 1, 1, 1), NumOuts = List(1, 1, 1), NumCarry = List(1, 1, 1, 1), NumExits = 1, ID = 0))

  val Loop_1 = Module(new LoopBlockNode(NumIns = List(2, 2, 2, 1, 1, 1, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 1))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_entry0 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 0))

  val bb_for_body1 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 11, NumPhi = 1, BID = 1))

  val bb_for_body72 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 47, NumPhi = 4, BID = 2))

  val bb_for_end3 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 11, BID = 3))

  val bb_for_end434 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 4))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  br label %for.body, !UID !3, !BB_UID !4
  val br_0 = Module(new UBranchNode(ID = 0))

  //  %indvars.iv99 = phi i64 [ 0, %entry ], [ %indvars.iv.next100, %for.end ], !UID !5
  val phiindvars_iv991 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 8, ID = 1, Res = true))

  //  %arrayidx = getelementptr inbounds double, double* %position_x, i64 %indvars.iv99, !UID !6
  val Gep_arrayidx2 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 2)(ElementSize = 8, ArraySize = List()))

  //  %0 = load double, double* %arrayidx, align 8, !tbaa !7, !UID !11
  val ld_3 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 3, RouteID = 0))

  //  %arrayidx2 = getelementptr inbounds double, double* %position_y, i64 %indvars.iv99, !UID !12
  val Gep_arrayidx24 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 4)(ElementSize = 8, ArraySize = List()))

  //  %1 = load double, double* %arrayidx2, align 8, !tbaa !7, !UID !13
  val ld_5 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 5, RouteID = 0))

  //  %arrayidx4 = getelementptr inbounds double, double* %position_z, i64 %indvars.iv99, !UID !14
  val Gep_arrayidx46 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 6)(ElementSize = 8, ArraySize = List()))

  //  %2 = load double, double* %arrayidx4, align 8, !tbaa !7, !UID !15
  val ld_7 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 7, RouteID = 0))

  //  %3 = shl i64 %indvars.iv99, 4, !UID !16
  val binaryOp_8 = Module(new ComputeNode(NumOuts = 1, ID = 8, opCode = "shl")(sign = false, Debug = false))

  //  br label %for.body7, !UID !17, !BB_UID !18
  val br_9 = Module(new UBranchNode(ID = 9))

  //  %indvars.iv = phi i64 [ 0, %for.body ], [ %indvars.iv.next, %for.body7 ], !UID !19
  val phiindvars_iv10 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 10, Res = true))

  //  %fx.096 = phi double [ 0.000000e+00, %for.body ], [ %add30, %for.body7 ], !UID !20
  val phifx_09611 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 1, ID = 11, Res = true))

  //  %fz.094 = phi double [ 0.000000e+00, %for.body ], [ %add34, %for.body7 ], !UID !21
  val phifz_09412 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 1, ID = 12, Res = true))

  //  %fy.093 = phi double [ 0.000000e+00, %for.body ], [ %add32, %for.body7 ], !UID !22
  val phify_09313 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 1, ID = 13, Res = true))

  //  %4 = add nuw nsw i64 %indvars.iv, %3, !UID !23
  val binaryOp_14 = Module(new ComputeNode(NumOuts = 1, ID = 14, opCode = "add")(sign = false, Debug = false))

  //  %arrayidx9 = getelementptr inbounds i32, i32* %NL, i64 %4, !UID !24
  val Gep_arrayidx915 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 15)(ElementSize = 8, ArraySize = List()))

  //  %5 = load i32, i32* %arrayidx9, align 4, !tbaa !25, !UID !27
  val ld_16 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 16, RouteID = 0))

  //  %idxprom10 = sext i32 %5 to i64, !UID !28
  val sextidxprom1017 = Module(new SextNode(NumOuts = 3))

  //  %arrayidx11 = getelementptr inbounds double, double* %position_x, i64 %idxprom10, !UID !29
  val Gep_arrayidx1118 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 18)(ElementSize = 8, ArraySize = List()))

  //  %6 = load double, double* %arrayidx11, align 8, !tbaa !7, !UID !30
  val ld_19 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 19, RouteID = 0))

  //  %arrayidx13 = getelementptr inbounds double, double* %position_y, i64 %idxprom10, !UID !31
  val Gep_arrayidx1320 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 20)(ElementSize = 8, ArraySize = List()))

  //  %7 = load double, double* %arrayidx13, align 8, !tbaa !7, !UID !32
  val ld_21 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 21, RouteID = 0))

  //  %arrayidx15 = getelementptr inbounds double, double* %position_z, i64 %idxprom10, !UID !33
  val Gep_arrayidx1522 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 22)(ElementSize = 8, ArraySize = List()))

  //  %8 = load double, double* %arrayidx15, align 8, !tbaa !7, !UID !34
  val ld_23 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 23, RouteID = 0))

  //  %sub = fsub double %0, %6, !UID !35
  val FP_sub24 = Module(new FPComputeNode(NumOuts = 3, ID = 24, opCode = "fsub")(fType))

  //  %sub16 = fsub double %1, %7, !UID !36
  val FP_sub1625 = Module(new FPComputeNode(NumOuts = 3, ID = 25, opCode = "fsub")(fType))

  //  %sub17 = fsub double %2, %8, !UID !37
  val FP_sub1726 = Module(new FPComputeNode(NumOuts = 3, ID = 26, opCode = "fsub")(fType))

  //  %mul18 = fmul double %sub, %sub, !UID !38
  val FP_mul1827 = Module(new FPComputeNode(NumOuts = 1, ID = 27, opCode = "fmul")(fType))

  //  %mul19 = fmul double %sub16, %sub16, !UID !39
  val FP_mul1928 = Module(new FPComputeNode(NumOuts = 1, ID = 28, opCode = "fmul")(fType))

  //  %add20 = fadd double %mul18, %mul19, !UID !40
  val FP_add2029 = Module(new FPComputeNode(NumOuts = 1, ID = 29, opCode = "fadd")(fType))

  //  %mul21 = fmul double %sub17, %sub17, !UID !41
  val FP_mul2130 = Module(new FPComputeNode(NumOuts = 1, ID = 30, opCode = "fmul")(fType))

  //  %add22 = fadd double %add20, %mul21, !UID !42
  val FP_add2231 = Module(new FPComputeNode(NumOuts = 1, ID = 31, opCode = "fadd")(fType))

  //  %div = fdiv double 1.000000e+00, %add22, !UID !43
  val FP_div32 = Module(new FPDivSqrtNode(NumOuts = 4, ID = 32, RouteID = 0, opCode = "fdiv")(fType))

  //  %mul23 = fmul double %div, %div, !UID !44
  val FP_mul2333 = Module(new FPComputeNode(NumOuts = 1, ID = 33, opCode = "fmul")(fType))

  //  %mul24 = fmul double %div, %mul23, !UID !45
  val FP_mul2434 = Module(new FPComputeNode(NumOuts = 2, ID = 34, opCode = "fmul")(fType))

  //  %mul25 = fmul double %mul24, 1.500000e+00, !UID !46
  val FP_mul2535 = Module(new FPComputeNode(NumOuts = 1, ID = 35, opCode = "fmul")(fType))

  //  %sub26 = fadd double %mul25, -2.000000e+00, !UID !47
  val FP_sub2636 = Module(new FPComputeNode(NumOuts = 1, ID = 36, opCode = "fadd")(fType))

  //  %mul27 = fmul double %mul24, %sub26, !UID !48
  val FP_mul2737 = Module(new FPComputeNode(NumOuts = 1, ID = 37, opCode = "fmul")(fType))

  //  %mul28 = fmul double %div, %mul27, !UID !49
  val FP_mul2838 = Module(new FPComputeNode(NumOuts = 3, ID = 38, opCode = "fmul")(fType))

  //  %mul29 = fmul double %sub, %mul28, !UID !50
  val FP_mul2939 = Module(new FPComputeNode(NumOuts = 1, ID = 39, opCode = "fmul")(fType))

  //  %add30 = fadd double %fx.096, %mul29, !UID !51
  val FP_add3040 = Module(new FPComputeNode(NumOuts = 2, ID = 40, opCode = "fadd")(fType))

  //  %mul31 = fmul double %sub16, %mul28, !UID !52
  val FP_mul3141 = Module(new FPComputeNode(NumOuts = 1, ID = 41, opCode = "fmul")(fType))

  //  %add32 = fadd double %fy.093, %mul31, !UID !53
  val FP_add3242 = Module(new FPComputeNode(NumOuts = 2, ID = 42, opCode = "fadd")(fType))

  //  %mul33 = fmul double %sub17, %mul28, !UID !54
  val FP_mul3343 = Module(new FPComputeNode(NumOuts = 1, ID = 43, opCode = "fmul")(fType))

  //  %add34 = fadd double %fz.094, %mul33, !UID !55
  val FP_add3444 = Module(new FPComputeNode(NumOuts = 2, ID = 44, opCode = "fadd")(fType))

  //  %indvars.iv.next = add nuw nsw i64 %indvars.iv, 1, !UID !56
  val binaryOp_indvars_iv_next45 = Module(new ComputeNode(NumOuts = 2, ID = 45, opCode = "add")(sign = false, Debug = false))

  //  %exitcond = icmp eq i64 %indvars.iv.next, 16, !UID !57
  val icmp_exitcond46 = Module(new ComputeNode(NumOuts = 1, ID = 46, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond, label %for.end, label %for.body7, !UID !58, !BB_UID !59
  val br_47 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 47))

  //  %arrayidx36 = getelementptr inbounds double, double* %force_x, i64 %indvars.iv99, !UID !60
  val Gep_arrayidx3648 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 48)(ElementSize = 8, ArraySize = List()))

  //  store double %add30, double* %arrayidx36, align 8, !tbaa !7, !UID !61
  val st_49 = Module(new UnTypStoreCache(NumPredOps = 0, NumSuccOps = 1, ID = 49, RouteID = 0))

  //  %arrayidx38 = getelementptr inbounds double, double* %force_y, i64 %indvars.iv99, !UID !62
  val Gep_arrayidx3850 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 50)(ElementSize = 8, ArraySize = List()))

  //  store double %add32, double* %arrayidx38, align 8, !tbaa !7, !UID !63
  val st_51 = Module(new UnTypStoreCache(NumPredOps = 0, NumSuccOps = 1, ID = 51, RouteID = 0))

  //  %arrayidx40 = getelementptr inbounds double, double* %force_z, i64 %indvars.iv99, !UID !64
  val Gep_arrayidx4052 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 52)(ElementSize = 8, ArraySize = List()))

  //  store double %add34, double* %arrayidx40, align 8, !tbaa !7, !UID !65
  val st_53 = Module(new UnTypStoreCache(NumPredOps = 0, NumSuccOps = 1, ID = 53, RouteID = 0))

  //  %indvars.iv.next100 = add nuw nsw i64 %indvars.iv99, 1, !UID !66
  val binaryOp_indvars_iv_next10054 = Module(new ComputeNode(NumOuts = 2, ID = 54, opCode = "add")(sign = false, Debug = false))

  //  %exitcond102 = icmp eq i64 %indvars.iv.next100, 256, !UID !67
  val icmp_exitcond10255 = Module(new ComputeNode(NumOuts = 1, ID = 55, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond102, label %for.end43, label %for.body, !UID !68, !BB_UID !69
  val br_56 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 3, ID = 56))

  //  ret void, !UID !70, !BB_UID !71
  val ret_57 = Module(new RetNode2(retTypes = List(), ID = 57))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i64 0
  val const0 = Module(new ConstFastNode(value = 0, ID = 0))

  //i64 4
  val const1 = Module(new ConstFastNode(value = 4, ID = 1))

  //i64 0
  val const2 = Module(new ConstFastNode(value = 0, ID = 2))

  //i64 1
  val const3 = Module(new ConstFastNode(value = 1, ID = 3))

  //i64 16
  val const4 = Module(new ConstFastNode(value = 16, ID = 4))

  //i64 1
  val const5 = Module(new ConstFastNode(value = 1, ID = 5))

  //i64 256
  val const6 = Module(new ConstFastNode(value = 256, ID = 6))

  //double 0.000000e+00
  val constf0 = Module(new ConstFastNode(value = 0x0L, ID = 0))

  //double 0.000000e+00
  val constf1 = Module(new ConstFastNode(value = 0x0L, ID = 1))

  //double 0.000000e+00
  val constf2 = Module(new ConstFastNode(value = 0x0L, ID = 2))

  //double 1.000000e+00
  val constf3 = Module(new ConstFastNode(value = 0x3ff0000000000000L, ID = 3))

  //double 1.500000e+00
  val constf4 = Module(new ConstFastNode(value = 0x3ff8000000000000L, ID = 4))

  //double -2.000000e+00
  val constf5 = Module(new ConstFastNode(value = 0x0L, ID = 5))



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_entry0.io.predicateIn(0) <> ArgSplitter.io.Out.enable



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE LOOP                     *
   * ================================================================== */

  bb_for_body1.io.predicateIn(1) <> Loop_1.io.activate_loop_start

  bb_for_body1.io.predicateIn(0) <> Loop_1.io.activate_loop_back

  bb_for_body72.io.predicateIn(1) <> Loop_0.io.activate_loop_start

  bb_for_body72.io.predicateIn(0) <> Loop_0.io.activate_loop_back

  bb_for_end3.io.predicateIn(0) <> Loop_0.io.loopExit(0)

  bb_for_end434.io.predicateIn(0) <> Loop_1.io.loopExit(0)



  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */

  Loop_0.io.enable <> br_9.io.Out(0)

  Loop_0.io.loopBack(0) <> br_47.io.FalseOutput(0)

  Loop_0.io.loopFinish(0) <> br_47.io.TrueOutput(0)

  Loop_1.io.enable <> br_0.io.Out(0)

  Loop_1.io.loopBack(0) <> br_56.io.FalseOutput(0)

  Loop_1.io.loopFinish(0) <> br_56.io.TrueOutput(0)



  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

  Loop_0.io.InLiveIn(0) <> binaryOp_8.io.Out(0)

  Loop_0.io.InLiveIn(1) <> ld_3.io.Out(0)

  Loop_0.io.InLiveIn(2) <> ld_5.io.Out(0)

  Loop_0.io.InLiveIn(3) <> ld_7.io.Out(0)

  Loop_0.io.InLiveIn(4) <> Loop_1.io.OutLiveIn.elements("field1")(0)

  Loop_0.io.InLiveIn(5) <> Loop_1.io.OutLiveIn.elements("field0")(0)

  Loop_0.io.InLiveIn(6) <> Loop_1.io.OutLiveIn.elements("field3")(0)

  Loop_0.io.InLiveIn(7) <> Loop_1.io.OutLiveIn.elements("field2")(0)

  Loop_1.io.InLiveIn(0) <> ArgSplitter.io.Out.dataPtrs.elements("field3")(0)

  Loop_1.io.InLiveIn(1) <> ArgSplitter.io.Out.dataPtrs.elements("field4")(0)

  Loop_1.io.InLiveIn(2) <> ArgSplitter.io.Out.dataPtrs.elements("field5")(0)

  Loop_1.io.InLiveIn(3) <> ArgSplitter.io.Out.dataPtrs.elements("field6")(0)

  Loop_1.io.InLiveIn(4) <> ArgSplitter.io.Out.dataPtrs.elements("field0")(0)

  Loop_1.io.InLiveIn(5) <> ArgSplitter.io.Out.dataPtrs.elements("field1")(0)

  Loop_1.io.InLiveIn(6) <> ArgSplitter.io.Out.dataPtrs.elements("field2")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */

  binaryOp_14.io.RightIO <> Loop_0.io.OutLiveIn.elements("field0")(0)

  FP_sub24.io.LeftIO <> Loop_0.io.OutLiveIn.elements("field1")(0)

  FP_sub1625.io.LeftIO <> Loop_0.io.OutLiveIn.elements("field2")(0)

  FP_sub1726.io.LeftIO <> Loop_0.io.OutLiveIn.elements("field3")(0)

  Gep_arrayidx1320.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field4")(0)

  Gep_arrayidx1118.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field5")(0)

  Gep_arrayidx915.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field6")(0)

  Gep_arrayidx1522.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field7")(0)

  Gep_arrayidx2.io.baseAddress <> Loop_1.io.OutLiveIn.elements("field0")(1)

  Gep_arrayidx24.io.baseAddress <> Loop_1.io.OutLiveIn.elements("field1")(1)

  Gep_arrayidx46.io.baseAddress <> Loop_1.io.OutLiveIn.elements("field2")(1)

  Gep_arrayidx3648.io.baseAddress <> Loop_1.io.OutLiveIn.elements("field4")(0)

  Gep_arrayidx3850.io.baseAddress <> Loop_1.io.OutLiveIn.elements("field5")(0)

  Gep_arrayidx4052.io.baseAddress <> Loop_1.io.OutLiveIn.elements("field6")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */

  Loop_0.io.InLiveOut(0) <> FP_add3040.io.Out(0)

  Loop_0.io.InLiveOut(1) <> FP_add3242.io.Out(0)

  Loop_0.io.InLiveOut(2) <> FP_add3444.io.Out(0)



  /* ================================================================== *
   *                   LOOP LIVE OUT DEPENDENCIES                       *
   * ================================================================== */

  st_49.io.inData <> Loop_0.io.OutLiveOut.elements("field0")(0)

  st_51.io.inData <> Loop_0.io.OutLiveOut.elements("field1")(0)

  st_53.io.inData <> Loop_0.io.OutLiveOut.elements("field2")(0)



  /* ================================================================== *
   *                   LOOP CARRY DEPENDENCIES                          *
   * ================================================================== */

  Loop_0.io.CarryDepenIn(0) <> FP_add3040.io.Out(1)

  Loop_0.io.CarryDepenIn(1) <> FP_add3242.io.Out(1)

  Loop_0.io.CarryDepenIn(2) <> FP_add3444.io.Out(1)

  Loop_0.io.CarryDepenIn(3) <> binaryOp_indvars_iv_next45.io.Out(0)

  Loop_1.io.CarryDepenIn(0) <> binaryOp_indvars_iv_next10054.io.Out(0)



  /* ================================================================== *
   *                   LOOP DATA CARRY DEPENDENCIES                     *
   * ================================================================== */

  phifx_09611.io.InData(1) <> Loop_0.io.CarryDepenOut.elements("field0")(0)

  phify_09313.io.InData(1) <> Loop_0.io.CarryDepenOut.elements("field1")(0)

  phifz_09412.io.InData(1) <> Loop_0.io.CarryDepenOut.elements("field2")(0)

  phiindvars_iv10.io.InData(1) <> Loop_0.io.CarryDepenOut.elements("field3")(0)

  phiindvars_iv991.io.InData(1) <> Loop_1.io.CarryDepenOut.elements("field0")(0)



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  br_0.io.enable <> bb_entry0.io.Out(0)


  const0.io.enable <> bb_for_body1.io.Out(0)

  const1.io.enable <> bb_for_body1.io.Out(1)

  phiindvars_iv991.io.enable <> bb_for_body1.io.Out(2)


  Gep_arrayidx2.io.enable <> bb_for_body1.io.Out(3)


  ld_3.io.enable <> bb_for_body1.io.Out(4)


  Gep_arrayidx24.io.enable <> bb_for_body1.io.Out(5)


  ld_5.io.enable <> bb_for_body1.io.Out(6)


  Gep_arrayidx46.io.enable <> bb_for_body1.io.Out(7)


  ld_7.io.enable <> bb_for_body1.io.Out(8)


  binaryOp_8.io.enable <> bb_for_body1.io.Out(9)


  br_9.io.enable <> bb_for_body1.io.Out(10)


  constf0.io.enable <> bb_for_body72.io.Out(1)

  constf1.io.enable <> bb_for_body72.io.Out(2)

  constf2.io.enable <> bb_for_body72.io.Out(3)

  constf3.io.enable <> bb_for_body72.io.Out(4)

  constf4.io.enable <> bb_for_body72.io.Out(5)

  constf5.io.enable <> bb_for_body72.io.Out(6)

  const2.io.enable <> bb_for_body72.io.Out(0)

  const3.io.enable <> bb_for_body72.io.Out(7)

  const4.io.enable <> bb_for_body72.io.Out(8)

  phiindvars_iv10.io.enable <> bb_for_body72.io.Out(9)


  phifx_09611.io.enable <> bb_for_body72.io.Out(10)


  phifz_09412.io.enable <> bb_for_body72.io.Out(11)


  phify_09313.io.enable <> bb_for_body72.io.Out(12)


  binaryOp_14.io.enable <> bb_for_body72.io.Out(13)


  Gep_arrayidx915.io.enable <> bb_for_body72.io.Out(14)


  ld_16.io.enable <> bb_for_body72.io.Out(15)


  sextidxprom1017.io.enable <> bb_for_body72.io.Out(16)


  Gep_arrayidx1118.io.enable <> bb_for_body72.io.Out(17)


  ld_19.io.enable <> bb_for_body72.io.Out(18)


  Gep_arrayidx1320.io.enable <> bb_for_body72.io.Out(19)


  ld_21.io.enable <> bb_for_body72.io.Out(20)


  Gep_arrayidx1522.io.enable <> bb_for_body72.io.Out(21)


  ld_23.io.enable <> bb_for_body72.io.Out(22)


  FP_sub24.io.enable <> bb_for_body72.io.Out(23)


  FP_sub1625.io.enable <> bb_for_body72.io.Out(24)


  FP_sub1726.io.enable <> bb_for_body72.io.Out(25)


  FP_mul1827.io.enable <> bb_for_body72.io.Out(26)


  FP_mul1928.io.enable <> bb_for_body72.io.Out(27)


  FP_add2029.io.enable <> bb_for_body72.io.Out(28)


  FP_mul2130.io.enable <> bb_for_body72.io.Out(29)


  FP_add2231.io.enable <> bb_for_body72.io.Out(30)


  FP_div32.io.enable <> bb_for_body72.io.Out(31)


  FP_mul2333.io.enable <> bb_for_body72.io.Out(32)


  FP_mul2434.io.enable <> bb_for_body72.io.Out(33)


  FP_mul2535.io.enable <> bb_for_body72.io.Out(34)


  FP_sub2636.io.enable <> bb_for_body72.io.Out(35)


  FP_mul2737.io.enable <> bb_for_body72.io.Out(36)


  FP_mul2838.io.enable <> bb_for_body72.io.Out(37)


  FP_mul2939.io.enable <> bb_for_body72.io.Out(38)


  FP_add3040.io.enable <> bb_for_body72.io.Out(39)


  FP_mul3141.io.enable <> bb_for_body72.io.Out(40)


  FP_add3242.io.enable <> bb_for_body72.io.Out(41)


  FP_mul3343.io.enable <> bb_for_body72.io.Out(42)


  FP_add3444.io.enable <> bb_for_body72.io.Out(43)


  binaryOp_indvars_iv_next45.io.enable <> bb_for_body72.io.Out(44)


  icmp_exitcond46.io.enable <> bb_for_body72.io.Out(45)


  br_47.io.enable <> bb_for_body72.io.Out(46)


  const5.io.enable <> bb_for_end3.io.Out(0)

  const6.io.enable <> bb_for_end3.io.Out(1)

  Gep_arrayidx3648.io.enable <> bb_for_end3.io.Out(2)


  st_49.io.enable <> bb_for_end3.io.Out(3)


  Gep_arrayidx3850.io.enable <> bb_for_end3.io.Out(4)


  st_51.io.enable <> bb_for_end3.io.Out(5)


  Gep_arrayidx4052.io.enable <> bb_for_end3.io.Out(6)


  st_53.io.enable <> bb_for_end3.io.Out(7)


  binaryOp_indvars_iv_next10054.io.enable <> bb_for_end3.io.Out(8)


  icmp_exitcond10255.io.enable <> bb_for_end3.io.Out(9)


  br_56.io.enable <> bb_for_end3.io.Out(10)


  ret_57.io.In.enable <> bb_for_end434.io.Out(0)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  phiindvars_iv991.io.Mask <> bb_for_body1.io.MaskBB(0)

  phiindvars_iv10.io.Mask <> bb_for_body72.io.MaskBB(0)

  phifx_09611.io.Mask <> bb_for_body72.io.MaskBB(1)

  phifz_09412.io.Mask <> bb_for_body72.io.MaskBB(2)

  phify_09313.io.Mask <> bb_for_body72.io.MaskBB(3)



  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */

  mem_ctrl_cache.io.rd.mem(0).MemReq <> ld_3.io.MemReq
  ld_3.io.MemResp <> mem_ctrl_cache.io.rd.mem(0).MemResp
  mem_ctrl_cache.io.rd.mem(1).MemReq <> ld_5.io.MemReq
  ld_5.io.MemResp <> mem_ctrl_cache.io.rd.mem(1).MemResp
  mem_ctrl_cache.io.rd.mem(2).MemReq <> ld_7.io.MemReq
  ld_7.io.MemResp <> mem_ctrl_cache.io.rd.mem(2).MemResp
  mem_ctrl_cache.io.rd.mem(3).MemReq <> ld_16.io.MemReq
  ld_16.io.MemResp <> mem_ctrl_cache.io.rd.mem(3).MemResp
  mem_ctrl_cache.io.rd.mem(4).MemReq <> ld_19.io.MemReq
  ld_19.io.MemResp <> mem_ctrl_cache.io.rd.mem(4).MemResp
  mem_ctrl_cache.io.rd.mem(5).MemReq <> ld_21.io.MemReq
  ld_21.io.MemResp <> mem_ctrl_cache.io.rd.mem(5).MemResp
  mem_ctrl_cache.io.rd.mem(6).MemReq <> ld_23.io.MemReq
  ld_23.io.MemResp <> mem_ctrl_cache.io.rd.mem(6).MemResp
  mem_ctrl_cache.io.wr.mem(0).MemReq <> st_49.io.MemReq
  st_49.io.MemResp <> mem_ctrl_cache.io.wr.mem(0).MemResp

  mem_ctrl_cache.io.wr.mem(1).MemReq <> st_51.io.MemReq
  st_51.io.MemResp <> mem_ctrl_cache.io.wr.mem(1).MemResp

  mem_ctrl_cache.io.wr.mem(2).MemReq <> st_53.io.MemReq
  st_53.io.MemResp <> mem_ctrl_cache.io.wr.mem(2).MemResp



  /* ================================================================== *
   *                   PRINT SHARED CONNECTIONS                         *
   * ================================================================== */

  SharedFPU.io.InData(0) <> FP_div32.io.FUReq
  FP_div32.io.FUResp <> SharedFPU.io.OutData(0)



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  phiindvars_iv991.io.InData(0) <> const0.io.Out

  binaryOp_8.io.RightIO <> const1.io.Out

  phiindvars_iv10.io.InData(0) <> const2.io.Out

  binaryOp_indvars_iv_next45.io.RightIO <> const3.io.Out

  icmp_exitcond46.io.RightIO <> const4.io.Out

  binaryOp_indvars_iv_next10054.io.RightIO <> const5.io.Out

  icmp_exitcond10255.io.RightIO <> const6.io.Out

  phifx_09611.io.InData(0) <> constf0.io.Out

  phifz_09412.io.InData(0) <> constf1.io.Out

  phify_09313.io.InData(0) <> constf2.io.Out

  FP_div32.io.a <> constf3.io.Out

  FP_mul2535.io.RightIO <> constf4.io.Out

  FP_sub2636.io.RightIO <> constf5.io.Out

  Gep_arrayidx2.io.idx(0) <> phiindvars_iv991.io.Out(0)

  Gep_arrayidx24.io.idx(0) <> phiindvars_iv991.io.Out(1)

  Gep_arrayidx46.io.idx(0) <> phiindvars_iv991.io.Out(2)

  binaryOp_8.io.LeftIO <> phiindvars_iv991.io.Out(3)

  Gep_arrayidx3648.io.idx(0) <> phiindvars_iv991.io.Out(4)

  Gep_arrayidx3850.io.idx(0) <> phiindvars_iv991.io.Out(5)

  Gep_arrayidx4052.io.idx(0) <> phiindvars_iv991.io.Out(6)

  binaryOp_indvars_iv_next10054.io.LeftIO <> phiindvars_iv991.io.Out(7)

  ld_3.io.GepAddr <> Gep_arrayidx2.io.Out(0)

  ld_5.io.GepAddr <> Gep_arrayidx24.io.Out(0)

  ld_7.io.GepAddr <> Gep_arrayidx46.io.Out(0)

  binaryOp_14.io.LeftIO <> phiindvars_iv10.io.Out(0)

  binaryOp_indvars_iv_next45.io.LeftIO <> phiindvars_iv10.io.Out(1)

  FP_add3040.io.LeftIO <> phifx_09611.io.Out(0)

  FP_add3444.io.LeftIO <> phifz_09412.io.Out(0)

  FP_add3242.io.LeftIO <> phify_09313.io.Out(0)

  Gep_arrayidx915.io.idx(0) <> binaryOp_14.io.Out(0)

  ld_16.io.GepAddr <> Gep_arrayidx915.io.Out(0)

  sextidxprom1017.io.Input <> ld_16.io.Out(0)

  Gep_arrayidx1118.io.idx(0) <> sextidxprom1017.io.Out(0)

  Gep_arrayidx1320.io.idx(0) <> sextidxprom1017.io.Out(1)

  Gep_arrayidx1522.io.idx(0) <> sextidxprom1017.io.Out(2)

  ld_19.io.GepAddr <> Gep_arrayidx1118.io.Out(0)

  FP_sub24.io.RightIO <> ld_19.io.Out(0)

  ld_21.io.GepAddr <> Gep_arrayidx1320.io.Out(0)

  FP_sub1625.io.RightIO <> ld_21.io.Out(0)

  ld_23.io.GepAddr <> Gep_arrayidx1522.io.Out(0)

  FP_sub1726.io.RightIO <> ld_23.io.Out(0)

  FP_mul1827.io.LeftIO <> FP_sub24.io.Out(0)

  FP_mul1827.io.RightIO<> FP_sub24.io.Out(1)

  FP_mul2939.io.LeftIO <> FP_sub24.io.Out(2)

  FP_mul1928.io.LeftIO <> FP_sub1625.io.Out(0)

  FP_mul1928.io.RightIO <> FP_sub1625.io.Out(1)

  FP_mul3141.io.LeftIO <> FP_sub1625.io.Out(2)

  FP_mul2130.io.LeftIO <> FP_sub1726.io.Out(0)

  FP_mul2130.io.RightIO <> FP_sub1726.io.Out(1)

  FP_mul3343.io.LeftIO <> FP_sub1726.io.Out(2)

  FP_add2029.io.LeftIO <> FP_mul1827.io.Out(0)

  FP_add2029.io.RightIO <> FP_mul1928.io.Out(0)

  FP_add2231.io.LeftIO <> FP_add2029.io.Out(0)

  FP_add2231.io.RightIO <> FP_mul2130.io.Out(0)

  FP_div32.io.b <> FP_add2231.io.Out(0)

  FP_mul2333.io.LeftIO <> FP_div32.io.Out(0)

  FP_mul2333.io.RightIO <> FP_div32.io.Out(1)

  FP_mul2434.io.LeftIO <> FP_div32.io.Out(2)

  FP_mul2838.io.LeftIO <> FP_div32.io.Out(3)

  FP_mul2434.io.RightIO <> FP_mul2333.io.Out(0)

  FP_mul2535.io.LeftIO <> FP_mul2434.io.Out(0)

  FP_mul2737.io.LeftIO <> FP_mul2434.io.Out(1)

  FP_sub2636.io.LeftIO <> FP_mul2535.io.Out(0)

  FP_mul2737.io.RightIO <> FP_sub2636.io.Out(0)

  FP_mul2838.io.RightIO <> FP_mul2737.io.Out(0)

  FP_mul2939.io.RightIO <> FP_mul2838.io.Out(0)

  FP_mul3141.io.RightIO <> FP_mul2838.io.Out(1)

  FP_mul3343.io.RightIO <> FP_mul2838.io.Out(2)

  FP_add3040.io.RightIO <> FP_mul2939.io.Out(0)

  FP_add3242.io.RightIO <> FP_mul3141.io.Out(0)

  FP_add3444.io.RightIO <> FP_mul3343.io.Out(0)

  icmp_exitcond46.io.LeftIO <> binaryOp_indvars_iv_next45.io.Out(1)

  br_47.io.CmpIO <> icmp_exitcond46.io.Out(0)

  st_49.io.GepAddr <> Gep_arrayidx3648.io.Out(0)

  st_51.io.GepAddr <> Gep_arrayidx3850.io.Out(0)

  st_53.io.GepAddr <> Gep_arrayidx4052.io.Out(0)

  icmp_exitcond10255.io.LeftIO <> binaryOp_indvars_iv_next10054.io.Out(1)

  br_56.io.CmpIO <> icmp_exitcond10255.io.Out(0)

  st_49.io.Out(0).ready := true.B

  st_51.io.Out(0).ready := true.B

  st_53.io.Out(0).ready := true.B



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  br_56.io.PredOp(0) <> st_49.io.SuccOp(0)

  br_56.io.PredOp(1) <> st_51.io.SuccOp(0)

  br_56.io.PredOp(2) <> st_53.io.SuccOp(0)



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_57.io.Out

}

