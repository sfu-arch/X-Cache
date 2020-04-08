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


class fftDF(PtrsIn: Seq[Int] = List(32, 32, 32, 32), ValsIn: Seq[Int] = List(), Returns: Seq[Int] = List())
			(implicit p: Parameters) extends DandelionAccelDCRModule(PtrsIn, ValsIn, Returns){


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  val MemCtrl = Module(new CacheMemoryEngine(ID = 0, NumRead = 8, NumWrite = 6))

  io.MemReq <> MemCtrl.io.cache.MemReq
  MemCtrl.io.cache.MemResp <> io.MemResp
  val ArgSplitter = Module(new SplitCallDCR(ptrsArgTypes = List(1, 1, 1, 1), valsArgTypes = List()))
  ArgSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val Loop_0 = Module(new LoopBlockNode(NumIns = List(3, 1, 2, 2, 1, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 0))

  val Loop_1 = Module(new LoopBlockNode(NumIns = List(1, 1, 1, 1), NumOuts = List(), NumCarry = List(1, 1), NumExits = 1, ID = 1))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_entry0 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 0))

  val bb_inner1 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 7, NumPhi = 2, BID = 1))

  val bb_for_body2_preheader2 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 2))

  val bb_for_body23 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 27, NumPhi = 1, BID = 3))

  val bb_if_then4 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 16, BID = 4))

  val bb_for_inc5 = Module(new BasicBlockNoMaskFastNode(NumInputs = 2, NumOuts = 5, BID = 5))

  val bb_for_inc53_loopexit6 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 6))

  val bb_for_inc537 = Module(new BasicBlockNoMaskFastNode(NumInputs = 2, NumOuts = 7, BID = 7))

  val bb_for_end558 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 8))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  br label %inner, !UID !3, !BB_UID !4
  val br_0 = Module(new UBranchNode(ID = 0))

  //  %log.0115 = phi i32 [ 0, %entry ], [ %inc54, %for.inc53 ], !UID !5
  val philog_01151 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 1, Res = true))

  //  %span.0113 = phi i32 [ 512, %entry ], [ %shr, %for.inc53 ], !UID !6
  val phispan_01132 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 3, ID = 2, Res = true))

  //  %cmp111 = icmp slt i32 %span.0113, 1024, !UID !7
  val icmp_cmp1113 = Module(new ComputeNode(NumOuts = 1, ID = 3, opCode = "slt")(sign = true, Debug = false))

  //  br i1 %cmp111, label %for.body2.preheader, label %for.inc53, !UID !8, !BB_UID !9
  val br_4 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 4))

  //  br label %for.body2, !UID !10, !BB_UID !11
  val br_5 = Module(new UBranchNode(ID = 5))

  //  %odd.0112 = phi i32 [ %inc, %for.inc ], [ %span.0113, %for.body2.preheader ], !UID !12
  val phiodd_01126 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 1, ID = 6, Res = false))

  //  %or = or i32 %odd.0112, %span.0113, !UID !13
  val binaryOp_or7 = Module(new ComputeNode(NumOuts = 4, ID = 7, opCode = "or")(sign = false, Debug = false))

  //  %xor = xor i32 %or, %span.0113, !UID !14
  val binaryOp_xor8 = Module(new ComputeNode(NumOuts = 2, ID = 8, opCode = "xor")(sign = false, Debug = false))

  //  %idxprom = sext i32 %xor to i64, !UID !15
  val sextidxprom9 = Module(new SextNode(NumOuts = 2))

  //  %arrayidx = getelementptr inbounds double, double* %real, i64 %idxprom, !UID !16
  val Gep_arrayidx10 = Module(new GepNode(NumIns = 1, NumOuts = 2, ID = 10)(ElementSize = 8, ArraySize = List()))

  //  %0 = load double, double* %arrayidx, align 8, !tbaa !17, !UID !21
  val ld_11 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 2, ID = 11, RouteID = 0))

  //  %idxprom3 = sext i32 %or to i64, !UID !22
  val sextidxprom312 = Module(new SextNode(NumOuts = 2))

  //  %arrayidx4 = getelementptr inbounds double, double* %real, i64 %idxprom3, !UID !23
  val Gep_arrayidx413 = Module(new GepNode(NumIns = 1, NumOuts = 4, ID = 13)(ElementSize = 8, ArraySize = List()))

  //  %1 = load double, double* %arrayidx4, align 8, !tbaa !17, !UID !24
  val ld_14 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 2, ID = 14, RouteID = 1))

  //  %add = fadd double %0, %1, !UID !25
  val FP_add15 = Module(new FPComputeNode(NumOuts = 1, ID = 15, opCode = "fadd")(fType))

  //  %sub = fsub double %0, %1, !UID !26
  val FP_sub16 = Module(new FPComputeNode(NumOuts = 1, ID = 16, opCode = "fsub")(fType))

  //  store double %sub, double* %arrayidx4, align 8, !tbaa !17, !UID !27
  val st_17 = Module(new UnTypStoreCache(NumPredOps = 0, NumSuccOps = 1, ID = 17, RouteID = 8))

  //  store double %add, double* %arrayidx, align 8, !tbaa !17, !UID !28
  val st_18 = Module(new UnTypStoreCache(NumPredOps = 0, NumSuccOps = 1, ID = 18, RouteID = 9))

  //  %arrayidx14 = getelementptr inbounds double, double* %img, i64 %idxprom, !UID !29
  val Gep_arrayidx1419 = Module(new GepNode(NumIns = 1, NumOuts = 2, ID = 19)(ElementSize = 8, ArraySize = List()))

  //  %2 = load double, double* %arrayidx14, align 8, !tbaa !17, !UID !30
  val ld_20 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 2, ID = 20, RouteID = 2))

  //  %arrayidx16 = getelementptr inbounds double, double* %img, i64 %idxprom3, !UID !31
  val Gep_arrayidx1621 = Module(new GepNode(NumIns = 1, NumOuts = 4, ID = 21)(ElementSize = 8, ArraySize = List()))

  //  %3 = load double, double* %arrayidx16, align 8, !tbaa !17, !UID !32
  val ld_22 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 2, ID = 22, RouteID = 3))

  //  %add17 = fadd double %2, %3, !UID !33
  val FP_add1723 = Module(new FPComputeNode(NumOuts = 1, ID = 23, opCode = "fadd")(fType))

  //  %sub22 = fsub double %2, %3, !UID !34
  val FP_sub2224 = Module(new FPComputeNode(NumOuts = 1, ID = 24, opCode = "fsub")(fType))

  //  store double %sub22, double* %arrayidx16, align 8, !tbaa !17, !UID !35
  val st_25 = Module(new UnTypStoreCache(NumPredOps = 0, NumSuccOps = 1, ID = 25, RouteID = 10))

  //  store double %add17, double* %arrayidx14, align 8, !tbaa !17, !UID !36
  val st_26 = Module(new UnTypStoreCache(NumPredOps = 0, NumSuccOps = 1, ID = 26, RouteID = 11))

  //  %shl = shl i32 %xor, %log.0115, !UID !37
  val binaryOp_shl27 = Module(new ComputeNode(NumOuts = 1, ID = 27, opCode = "shl")(sign = false, Debug = false))

  //  %and = and i32 %shl, 1023, !UID !38
  val binaryOp_and28 = Module(new ComputeNode(NumOuts = 2, ID = 28, opCode = "and")(sign = false, Debug = false))

  //  %tobool27 = icmp eq i32 %and, 0, !UID !39
  val icmp_tobool2729 = Module(new ComputeNode(NumOuts = 1, ID = 29, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %tobool27, label %for.inc, label %if.then, !UID !40, !BB_UID !41
  val br_30 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 4, ID = 30))

  //  %4 = zext i32 %and to i64, !UID !42
  val sext31 = Module(new ZextNode(NumOuts = 2))

  //  %arrayidx29 = getelementptr inbounds double, double* %real_twid, i64 %4, !UID !43
  val Gep_arrayidx2932 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 32)(ElementSize = 8, ArraySize = List()))

  //  %5 = load double, double* %arrayidx29, align 8, !tbaa !17, !UID !44
  val ld_33 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 2, ID = 33, RouteID = 4))

  //  %6 = load double, double* %arrayidx4, align 8, !tbaa !17, !UID !45
  val ld_34 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 2, ID = 34, RouteID = 5))

  //  %mul = fmul double %5, %6, !UID !46
  val FP_mul35 = Module(new FPComputeNode(NumOuts = 1, ID = 35, opCode = "fmul")(fType))

  //  %arrayidx33 = getelementptr inbounds double, double* %img_twid, i64 %4, !UID !47
  val Gep_arrayidx3336 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 36)(ElementSize = 8, ArraySize = List()))

  //  %7 = load double, double* %arrayidx33, align 8, !tbaa !17, !UID !48
  val ld_37 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 2, ID = 37, RouteID = 6))

  //  %8 = load double, double* %arrayidx16, align 8, !tbaa !17, !UID !49
  val ld_38 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 2, ID = 38, RouteID = 7))

  //  %mul36 = fmul double %7, %8, !UID !50
  val FP_mul3639 = Module(new FPComputeNode(NumOuts = 1, ID = 39, opCode = "fmul")(fType))

  //  %sub37 = fsub double %mul, %mul36, !UID !51
  val FP_sub3740 = Module(new FPComputeNode(NumOuts = 1, ID = 40, opCode = "fsub")(fType))

  //  %mul42 = fmul double %5, %8, !UID !52
  val FP_mul4241 = Module(new FPComputeNode(NumOuts = 1, ID = 41, opCode = "fmul")(fType))

  //  %mul47 = fmul double %6, %7, !UID !53
  val FP_mul4742 = Module(new FPComputeNode(NumOuts = 1, ID = 42, opCode = "fmul")(fType))

  //  %add48 = fadd double %mul47, %mul42, !UID !54
  val FP_add4843 = Module(new FPComputeNode(NumOuts = 1, ID = 43, opCode = "fadd")(fType))

  //  store double %add48, double* %arrayidx16, align 8, !tbaa !17, !UID !55
  val st_44 = Module(new UnTypStoreCache(NumPredOps = 0, NumSuccOps = 1, ID = 44, RouteID = 12))

  //  store double %sub37, double* %arrayidx4, align 8, !tbaa !17, !UID !56
  val st_45 = Module(new UnTypStoreCache(NumPredOps = 0, NumSuccOps = 1, ID = 45, RouteID = 13))

  //  br label %for.inc, !UID !57, !BB_UID !58
  val br_46 = Module(new UBranchNode(NumPredOps=2, ID = 46))

  //  %inc = add nsw i32 %or, 1, !UID !59
  val binaryOp_inc47 = Module(new ComputeNode(NumOuts = 1, ID = 47, opCode = "add")(sign = false, Debug = false))

  //  %cmp = icmp slt i32 %or, 1023, !UID !60
  val icmp_cmp48 = Module(new ComputeNode(NumOuts = 1, ID = 48, opCode = "slt")(sign = true, Debug = false))

  //  br i1 %cmp, label %for.body2, label %for.inc53.loopexit, !UID !61, !BB_UID !62
  val br_49 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 49))

  //  br label %for.inc53
  val br_50 = Module(new UBranchNode(ID = 50))

  //  %shr = ashr i32 %span.0113, 1, !UID !63
  val binaryOp_shr51 = Module(new ComputeNode(NumOuts = 1, ID = 51, opCode = "ashr")(sign = false, Debug = false))

  //  %inc54 = add nuw nsw i32 %log.0115, 1, !UID !64
  val binaryOp_inc5452 = Module(new ComputeNode(NumOuts = 2, ID = 52, opCode = "add")(sign = false, Debug = false))

  //  %exitcond = icmp eq i32 %inc54, 10, !UID !65
  val icmp_exitcond53 = Module(new ComputeNode(NumOuts = 1, ID = 53, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond, label %for.end55, label %inner, !UID !66, !BB_UID !67
  val br_54 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 54))

  //  ret void, !UID !68, !BB_UID !69
  val ret_55 = Module(new RetNode2(retTypes = List(), ID = 55))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i32 0
  val const0 = Module(new ConstFastNode(value = 0, ID = 0))

  //i32 512
  val const1 = Module(new ConstFastNode(value = 512, ID = 1))

  //i32 1024
  val const2 = Module(new ConstFastNode(value = 1024, ID = 2))

  //i32 1023
  val const3 = Module(new ConstFastNode(value = 1023, ID = 3))

  //i32 0
  val const4 = Module(new ConstFastNode(value = 0, ID = 4))

  //i32 1
  val const5 = Module(new ConstFastNode(value = 1, ID = 5))

  //i32 1023
  val const6 = Module(new ConstFastNode(value = 1023, ID = 6))

  //i32 1
  val const7 = Module(new ConstFastNode(value = 1, ID = 7))

  //i32 1
  val const8 = Module(new ConstFastNode(value = 1, ID = 8))

  //i32 10
  val const9 = Module(new ConstFastNode(value = 10, ID = 9))



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_entry0.io.predicateIn(0) <> ArgSplitter.io.Out.enable

  bb_for_body2_preheader2.io.predicateIn(0) <> br_4.io.TrueOutput(0)

  bb_if_then4.io.predicateIn(0) <> br_30.io.FalseOutput(0)

  bb_for_inc5.io.predicateIn(1) <> br_30.io.TrueOutput(0)

  bb_for_inc5.io.predicateIn(0) <> br_46.io.Out(0)

  bb_for_inc537.io.predicateIn(1) <> br_4.io.FalseOutput(0)

  bb_for_inc537.io.predicateIn(0) <> br_50.io.Out(0)



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE LOOP                     *
   * ================================================================== */

  bb_inner1.io.predicateIn(1) <> Loop_1.io.activate_loop_start

  bb_inner1.io.predicateIn(0) <> Loop_1.io.activate_loop_back

  bb_for_body23.io.predicateIn(1) <> Loop_0.io.activate_loop_start

  bb_for_body23.io.predicateIn(0) <> Loop_0.io.activate_loop_back

  bb_for_inc53_loopexit6.io.predicateIn(0) <> Loop_0.io.loopExit(0)

  bb_for_end558.io.predicateIn(0) <> Loop_1.io.loopExit(0)



  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */

  Loop_0.io.enable <> br_5.io.Out(0)

  Loop_0.io.loopBack(0) <> br_49.io.TrueOutput(0)

  Loop_0.io.loopFinish(0) <> br_49.io.FalseOutput(0)

  Loop_1.io.enable <> br_0.io.Out(0)

  Loop_1.io.loopBack(0) <> br_54.io.FalseOutput(0)

  Loop_1.io.loopFinish(0) <> br_54.io.TrueOutput(0)



  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

  Loop_0.io.InLiveIn(0) <> phispan_01132.io.Out(0)

  Loop_0.io.InLiveIn(1) <> philog_01151.io.Out(0)

  Loop_0.io.InLiveIn(2) <> Loop_1.io.OutLiveIn.elements("field1")(0)

  Loop_0.io.InLiveIn(3) <> Loop_1.io.OutLiveIn.elements("field0")(0)

  Loop_0.io.InLiveIn(4) <> Loop_1.io.OutLiveIn.elements("field2")(0)

  Loop_0.io.InLiveIn(5) <> Loop_1.io.OutLiveIn.elements("field3")(0)

  Loop_1.io.InLiveIn(0) <> ArgSplitter.io.Out.dataPtrs.elements("field0")(0)

  Loop_1.io.InLiveIn(1) <> ArgSplitter.io.Out.dataPtrs.elements("field1")(0)

  Loop_1.io.InLiveIn(2) <> ArgSplitter.io.Out.dataPtrs.elements("field2")(0)

  Loop_1.io.InLiveIn(3) <> ArgSplitter.io.Out.dataPtrs.elements("field3")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */

  phiodd_01126.io.InData(1) <> Loop_0.io.OutLiveIn.elements("field0")(0)

  binaryOp_or7.io.RightIO <> Loop_0.io.OutLiveIn.elements("field0")(1)

  binaryOp_xor8.io.RightIO <> Loop_0.io.OutLiveIn.elements("field0")(2)

  binaryOp_shl27.io.RightIO <> Loop_0.io.OutLiveIn.elements("field1")(0)

  Gep_arrayidx1419.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field2")(0)

  Gep_arrayidx1621.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field2")(1)

  Gep_arrayidx10.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field3")(0)

  Gep_arrayidx413.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field3")(1)

  Gep_arrayidx2932.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field4")(0)

  Gep_arrayidx3336.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field5")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP LIVE OUT DEPENDENCIES                       *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP CARRY DEPENDENCIES                          *
   * ================================================================== */

  Loop_0.io.CarryDepenIn(0) <> binaryOp_inc47.io.Out(0)

  Loop_1.io.CarryDepenIn(0) <> binaryOp_shr51.io.Out(0)

  Loop_1.io.CarryDepenIn(1) <> binaryOp_inc5452.io.Out(0)



  /* ================================================================== *
   *                   LOOP DATA CARRY DEPENDENCIES                     *
   * ================================================================== */

  phiodd_01126.io.InData(0) <> Loop_0.io.CarryDepenOut.elements("field0")(0)

  phispan_01132.io.InData(1) <> Loop_1.io.CarryDepenOut.elements("field0")(0)

  philog_01151.io.InData(1) <> Loop_1.io.CarryDepenOut.elements("field1")(0)



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  br_0.io.enable <> bb_entry0.io.Out(0)


  const0.io.enable <> bb_inner1.io.Out(0)

  const1.io.enable <> bb_inner1.io.Out(1)

  const2.io.enable <> bb_inner1.io.Out(2)

  philog_01151.io.enable <> bb_inner1.io.Out(3)


  phispan_01132.io.enable <> bb_inner1.io.Out(4)


  icmp_cmp1113.io.enable <> bb_inner1.io.Out(5)


  br_4.io.enable <> bb_inner1.io.Out(6)


  br_5.io.enable <> bb_for_body2_preheader2.io.Out(0)


  const3.io.enable <> bb_for_body23.io.Out(0)

  const4.io.enable <> bb_for_body23.io.Out(1)

  phiodd_01126.io.enable <> bb_for_body23.io.Out(2)


  binaryOp_or7.io.enable <> bb_for_body23.io.Out(3)


  binaryOp_xor8.io.enable <> bb_for_body23.io.Out(4)


  sextidxprom9.io.enable <> bb_for_body23.io.Out(5)


  Gep_arrayidx10.io.enable <> bb_for_body23.io.Out(6)


  ld_11.io.enable <> bb_for_body23.io.Out(7)


  sextidxprom312.io.enable <> bb_for_body23.io.Out(8)


  Gep_arrayidx413.io.enable <> bb_for_body23.io.Out(9)


  ld_14.io.enable <> bb_for_body23.io.Out(10)


  FP_add15.io.enable <> bb_for_body23.io.Out(11)


  FP_sub16.io.enable <> bb_for_body23.io.Out(12)


  st_17.io.enable <> bb_for_body23.io.Out(13)


  st_18.io.enable <> bb_for_body23.io.Out(14)


  Gep_arrayidx1419.io.enable <> bb_for_body23.io.Out(15)


  ld_20.io.enable <> bb_for_body23.io.Out(16)


  Gep_arrayidx1621.io.enable <> bb_for_body23.io.Out(17)


  ld_22.io.enable <> bb_for_body23.io.Out(18)


  FP_add1723.io.enable <> bb_for_body23.io.Out(19)


  FP_sub2224.io.enable <> bb_for_body23.io.Out(20)


  st_25.io.enable <> bb_for_body23.io.Out(21)


  st_26.io.enable <> bb_for_body23.io.Out(22)


  binaryOp_shl27.io.enable <> bb_for_body23.io.Out(23)


  binaryOp_and28.io.enable <> bb_for_body23.io.Out(24)


  icmp_tobool2729.io.enable <> bb_for_body23.io.Out(25)


  br_30.io.enable <> bb_for_body23.io.Out(26)


  sext31.io.enable <> bb_if_then4.io.Out(0)


  Gep_arrayidx2932.io.enable <> bb_if_then4.io.Out(1)


  ld_33.io.enable <> bb_if_then4.io.Out(2)


  ld_34.io.enable <> bb_if_then4.io.Out(3)


  FP_mul35.io.enable <> bb_if_then4.io.Out(4)


  Gep_arrayidx3336.io.enable <> bb_if_then4.io.Out(5)


  ld_37.io.enable <> bb_if_then4.io.Out(6)


  ld_38.io.enable <> bb_if_then4.io.Out(7)


  FP_mul3639.io.enable <> bb_if_then4.io.Out(8)


  FP_sub3740.io.enable <> bb_if_then4.io.Out(9)


  FP_mul4241.io.enable <> bb_if_then4.io.Out(10)


  FP_mul4742.io.enable <> bb_if_then4.io.Out(11)


  FP_add4843.io.enable <> bb_if_then4.io.Out(12)


  st_44.io.enable <> bb_if_then4.io.Out(13)


  st_45.io.enable <> bb_if_then4.io.Out(14)


  br_46.io.enable <> bb_if_then4.io.Out(15)


  const5.io.enable <> bb_for_inc5.io.Out(0)

  const6.io.enable <> bb_for_inc5.io.Out(1)

  binaryOp_inc47.io.enable <> bb_for_inc5.io.Out(2)


  icmp_cmp48.io.enable <> bb_for_inc5.io.Out(3)


  br_49.io.enable <> bb_for_inc5.io.Out(4)


  br_50.io.enable <> bb_for_inc53_loopexit6.io.Out(0)


  const7.io.enable <> bb_for_inc537.io.Out(0)

  const8.io.enable <> bb_for_inc537.io.Out(1)

  const9.io.enable <> bb_for_inc537.io.Out(2)

  binaryOp_shr51.io.enable <> bb_for_inc537.io.Out(3)


  binaryOp_inc5452.io.enable <> bb_for_inc537.io.Out(4)


  icmp_exitcond53.io.enable <> bb_for_inc537.io.Out(5)


  br_54.io.enable <> bb_for_inc537.io.Out(6)


  ret_55.io.In.enable <> bb_for_end558.io.Out(0)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  philog_01151.io.Mask <> bb_inner1.io.MaskBB(0)

  phispan_01132.io.Mask <> bb_inner1.io.MaskBB(1)

  phiodd_01126.io.Mask <> bb_for_body23.io.MaskBB(0)



  /* ================================================================== *
   *                   PRINT ALLOCA OFFSET                              *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */

  MemCtrl.io.rd.mem(0).MemReq <> ld_11.io.MemReq

  ld_11.io.MemResp <> MemCtrl.io.rd.mem(0).MemResp

  MemCtrl.io.rd.mem(1).MemReq <> ld_14.io.MemReq

  ld_14.io.MemResp <> MemCtrl.io.rd.mem(1).MemResp

  MemCtrl.io.wr.mem(0).MemReq <> st_17.io.MemReq

  st_17.io.MemResp <> MemCtrl.io.wr.mem(0).MemResp

  MemCtrl.io.wr.mem(1).MemReq <> st_18.io.MemReq

  st_18.io.MemResp <> MemCtrl.io.wr.mem(1).MemResp

  MemCtrl.io.rd.mem(2).MemReq <> ld_20.io.MemReq

  ld_20.io.MemResp <> MemCtrl.io.rd.mem(2).MemResp

  MemCtrl.io.rd.mem(3).MemReq <> ld_22.io.MemReq

  ld_22.io.MemResp <> MemCtrl.io.rd.mem(3).MemResp

  MemCtrl.io.wr.mem(2).MemReq <> st_25.io.MemReq

  st_25.io.MemResp <> MemCtrl.io.wr.mem(2).MemResp

  MemCtrl.io.wr.mem(3).MemReq <> st_26.io.MemReq

  st_26.io.MemResp <> MemCtrl.io.wr.mem(3).MemResp

  MemCtrl.io.rd.mem(4).MemReq <> ld_33.io.MemReq

  ld_33.io.MemResp <> MemCtrl.io.rd.mem(4).MemResp

  MemCtrl.io.rd.mem(5).MemReq <> ld_34.io.MemReq

  ld_34.io.MemResp <> MemCtrl.io.rd.mem(5).MemResp

  MemCtrl.io.rd.mem(6).MemReq <> ld_37.io.MemReq

  ld_37.io.MemResp <> MemCtrl.io.rd.mem(6).MemResp

  MemCtrl.io.rd.mem(7).MemReq <> ld_38.io.MemReq

  ld_38.io.MemResp <> MemCtrl.io.rd.mem(7).MemResp

  MemCtrl.io.wr.mem(4).MemReq <> st_44.io.MemReq

  st_44.io.MemResp <> MemCtrl.io.wr.mem(4).MemResp

  MemCtrl.io.wr.mem(5).MemReq <> st_45.io.MemReq

  st_45.io.MemResp <> MemCtrl.io.wr.mem(5).MemResp



  /* ================================================================== *
   *                   PRINT SHARED CONNECTIONS                         *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  philog_01151.io.InData(0) <> const0.io.Out

  phispan_01132.io.InData(0) <> const1.io.Out

  icmp_cmp1113.io.RightIO <> const2.io.Out

  binaryOp_and28.io.RightIO <> const3.io.Out

  icmp_tobool2729.io.RightIO <> const4.io.Out

  binaryOp_inc47.io.RightIO <> const5.io.Out

  icmp_cmp48.io.RightIO <> const6.io.Out

  binaryOp_shr51.io.RightIO <> const7.io.Out

  binaryOp_inc5452.io.RightIO <> const8.io.Out

  icmp_exitcond53.io.RightIO <> const9.io.Out

  binaryOp_inc5452.io.LeftIO <> philog_01151.io.Out(1)

  icmp_cmp1113.io.LeftIO <> phispan_01132.io.Out(1)

  binaryOp_shr51.io.LeftIO <> phispan_01132.io.Out(2)

  br_4.io.CmpIO <> icmp_cmp1113.io.Out(0)

  binaryOp_or7.io.LeftIO <> phiodd_01126.io.Out(0)

  binaryOp_xor8.io.LeftIO <> binaryOp_or7.io.Out(0)

  sextidxprom312.io.Input <> binaryOp_or7.io.Out(1)

  binaryOp_inc47.io.LeftIO <> binaryOp_or7.io.Out(2)

  icmp_cmp48.io.LeftIO <> binaryOp_or7.io.Out(3)

  sextidxprom9.io.Input <> binaryOp_xor8.io.Out(0)

  binaryOp_shl27.io.LeftIO <> binaryOp_xor8.io.Out(1)

  Gep_arrayidx10.io.idx(0) <> sextidxprom9.io.Out(0)

  Gep_arrayidx1419.io.idx(0) <> sextidxprom9.io.Out(1)

  ld_11.io.GepAddr <> Gep_arrayidx10.io.Out(0)

  st_18.io.GepAddr <> Gep_arrayidx10.io.Out(1)

  FP_add15.io.LeftIO <> ld_11.io.Out(0)

  FP_sub16.io.LeftIO <> ld_11.io.Out(1)

  Gep_arrayidx413.io.idx(0) <> sextidxprom312.io.Out(0)

  Gep_arrayidx1621.io.idx(0) <> sextidxprom312.io.Out(1)

  ld_14.io.GepAddr <> Gep_arrayidx413.io.Out(0)

  st_17.io.GepAddr <> Gep_arrayidx413.io.Out(1)

  ld_34.io.GepAddr <> Gep_arrayidx413.io.Out(2)

  st_45.io.GepAddr <> Gep_arrayidx413.io.Out(3)

  FP_add15.io.RightIO <> ld_14.io.Out(0)

  FP_sub16.io.RightIO <> ld_14.io.Out(1)

  st_18.io.inData <> FP_add15.io.Out(0)

  st_17.io.inData <> FP_sub16.io.Out(0)

  ld_20.io.GepAddr <> Gep_arrayidx1419.io.Out(0)

  st_26.io.GepAddr <> Gep_arrayidx1419.io.Out(1)

  FP_add1723.io.LeftIO <> ld_20.io.Out(0)

  FP_sub2224.io.LeftIO <> ld_20.io.Out(1)

  ld_22.io.GepAddr <> Gep_arrayidx1621.io.Out(0)

  st_25.io.GepAddr <> Gep_arrayidx1621.io.Out(1)

  ld_38.io.GepAddr <> Gep_arrayidx1621.io.Out(2)

  st_44.io.GepAddr <> Gep_arrayidx1621.io.Out(3)

  FP_add1723.io.RightIO <> ld_22.io.Out(0)

  FP_sub2224.io.RightIO <> ld_22.io.Out(1)

  st_26.io.inData <> FP_add1723.io.Out(0)

  st_25.io.inData <> FP_sub2224.io.Out(0)

  binaryOp_and28.io.LeftIO <> binaryOp_shl27.io.Out(0)

  icmp_tobool2729.io.LeftIO <> binaryOp_and28.io.Out(0)

  sext31.io.Input <> binaryOp_and28.io.Out(1)

  br_30.io.CmpIO <> icmp_tobool2729.io.Out(0)

  Gep_arrayidx2932.io.idx(0) <> sext31.io.Out(0)

  Gep_arrayidx3336.io.idx(0) <> sext31.io.Out(1)

  ld_33.io.GepAddr <> Gep_arrayidx2932.io.Out(0)

  FP_mul35.io.LeftIO <> ld_33.io.Out(0)

  FP_mul4241.io.LeftIO <> ld_33.io.Out(1)

  FP_mul35.io.RightIO <> ld_34.io.Out(0)

  FP_mul4742.io.LeftIO <> ld_34.io.Out(1)

  FP_sub3740.io.LeftIO <> FP_mul35.io.Out(0)

  ld_37.io.GepAddr <> Gep_arrayidx3336.io.Out(0)

  FP_mul3639.io.LeftIO <> ld_37.io.Out(0)

  FP_mul4742.io.RightIO <> ld_37.io.Out(1)

  FP_mul3639.io.RightIO <> ld_38.io.Out(0)

  FP_mul4241.io.RightIO <> ld_38.io.Out(1)

  FP_sub3740.io.RightIO <> FP_mul3639.io.Out(0)

  st_45.io.inData <> FP_sub3740.io.Out(0)

  FP_add4843.io.RightIO <> FP_mul4241.io.Out(0)

  FP_add4843.io.LeftIO <> FP_mul4742.io.Out(0)

  st_44.io.inData <> FP_add4843.io.Out(0)

  br_49.io.CmpIO <> icmp_cmp48.io.Out(0)

  icmp_exitcond53.io.LeftIO <> binaryOp_inc5452.io.Out(1)

  br_54.io.CmpIO <> icmp_exitcond53.io.Out(0)

  st_17.io.Out(0).ready := true.B

  st_18.io.Out(0).ready := true.B

  st_25.io.Out(0).ready := true.B

  st_26.io.Out(0).ready := true.B

  st_44.io.Out(0).ready := true.B

  st_45.io.Out(0).ready := true.B



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  br_30.io.PredOp(0) <> st_17.io.SuccOp(0)

  br_30.io.PredOp(1) <> st_18.io.SuccOp(0)

  br_30.io.PredOp(2) <> st_25.io.SuccOp(0)

  br_30.io.PredOp(3) <> st_26.io.SuccOp(0)

  br_46.io.PredOp(0) <> st_44.io.SuccOp(0)

  br_46.io.PredOp(1) <> st_45.io.SuccOp(0)



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_55.io.Out

}

