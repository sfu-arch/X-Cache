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


class fftDF(PtrsIn: Seq[Int] = List(64, 64, 64, 64), ValsIn: Seq[Int] = List(), Returns: Seq[Int] = List())
			(implicit p: Parameters) extends DandelionAccelDCRModule(PtrsIn, ValsIn, Returns){


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  //Cache
  val mem_ctrl_cache = Module(new CacheMemoryEngine(ID = 0, NumRead = 8, NumWrite = 6))

  io.MemReq <> mem_ctrl_cache.io.cache.MemReq
  mem_ctrl_cache.io.cache.MemResp <> io.MemResp

  val ArgSplitter = Module(new SplitCallDCR(ptrsArgTypes = List(1, 1, 1, 1), valsArgTypes = List()))
  ArgSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val Loop_0 = Module(new LoopBlockNode(NumIns = List(3, 1, 1, 1, 2, 2), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 0))

  val Loop_1 = Module(new LoopBlockNode(NumIns = List(1, 1, 1, 1), NumOuts = List(), NumCarry = List(1, 1), NumExits = 1, ID = 1))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_entry1 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 1))

  val bb_inner3 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 7, NumPhi = 2, BID = 3))

  val bb_for_body2_preheader8 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 8))

  val bb_for_body210 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 27, NumPhi = 1, BID = 10))

  val bb_if_then36 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 16, BID = 36))

  val bb_for_inc53 = Module(new BasicBlockNoMaskFastNode(NumInputs = 2, NumOuts = 5, BID = 53))

  val bb_for_inc53_loopexit57 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 57))

  val bb_for_inc5359 = Module(new BasicBlockNoMaskFastNode(NumInputs = 2, NumOuts = 7, BID = 59))

  val bb_for_end5564 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 64))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  br label %inner, !UID !3, !BB_UID !4
  val br_2 = Module(new UBranchNode(ID = 2))

  //  %log.0115 = phi i32 [ 0, %entry ], [ %inc54, %for.inc53 ], !UID !5
  val philog_01154 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 4, Res = false))

  //  %span.0113 = phi i32 [ 512, %entry ], [ %shr, %for.inc53 ], !UID !6
  val phispan_01135 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 3, ID = 5, Res = false))

  //  %cmp111 = icmp slt i32 %span.0113, 1024, !UID !7
  val icmp_cmp1116 = Module(new ComputeNode(NumOuts = 1, ID = 6, opCode = "slt")(sign = true, Debug = false))

  //  br i1 %cmp111, label %for.body2.preheader, label %for.inc53, !UID !8, !BB_UID !9
  val br_7 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 7))

  //  br label %for.body2, !UID !10, !BB_UID !11
  val br_9 = Module(new UBranchNode(ID = 9))

  //  %odd.0112 = phi i32 [ %inc, %for.inc ], [ %span.0113, %for.body2.preheader ], !UID !12
  val phiodd_011211 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 1, ID = 11, Res = true))

  //  %or = or i32 %odd.0112, %span.0113, !UID !13
  val binaryOp_or12 = Module(new ComputeNode(NumOuts = 4, ID = 12, opCode = "or")(sign = false, Debug = false))

  //  %xor = xor i32 %or, %span.0113, !UID !14
  val binaryOp_xor13 = Module(new ComputeNode(NumOuts = 2, ID = 13, opCode = "xor")(sign = false, Debug = false))

  //  %idxprom = sext i32 %xor to i64, !UID !15
  val sextidxprom14 = Module(new SextNode(NumOuts = 2))

  //  %arrayidx = getelementptr inbounds double, double* %real, i64 %idxprom, !UID !16
  val Gep_arrayidx15 = Module(new GepNode(NumIns = 1, NumOuts = 2, ID = 15)(ElementSize = 8, ArraySize = List()))

  //  %0 = load double, double* %arrayidx, align 8, !tbaa !17, !UID !21
  val ld_16 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 2, ID = 16, RouteID = 0))

  //  %idxprom3 = sext i32 %or to i64, !UID !22
  val sextidxprom317 = Module(new SextNode(NumOuts = 2))

  //  %arrayidx4 = getelementptr inbounds double, double* %real, i64 %idxprom3, !UID !23
  val Gep_arrayidx418 = Module(new GepNode(NumIns = 1, NumOuts = 4, ID = 18)(ElementSize = 8, ArraySize = List()))

  //  %1 = load double, double* %arrayidx4, align 8, !tbaa !17, !UID !24
  val ld_19 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 2, ID = 19, RouteID = 1))

  //  %add = fadd double %0, %1, !UID !25
  val FP_add20 = Module(new FPComputeNode(NumOuts = 1, ID = 20, opCode = "fadd")(fType))

  //  %sub = fsub double %0, %1, !UID !26
  val FP_sub21 = Module(new FPComputeNode(NumOuts = 1, ID = 21, opCode = "fsub")(fType))

  //  store double %sub, double* %arrayidx4, align 8, !tbaa !17, !UID !27
  val st_22 = Module(new UnTypStoreCache(NumPredOps = 0, NumSuccOps = 1, ID = 22, RouteID = 8))

  //  store double %add, double* %arrayidx, align 8, !tbaa !17, !UID !28
  val st_23 = Module(new UnTypStoreCache(NumPredOps = 0, NumSuccOps = 1, ID = 23, RouteID = 9))

  //  %arrayidx14 = getelementptr inbounds double, double* %img, i64 %idxprom, !UID !29
  val Gep_arrayidx1424 = Module(new GepNode(NumIns = 1, NumOuts = 2, ID = 24)(ElementSize = 8, ArraySize = List()))

  //  %2 = load double, double* %arrayidx14, align 8, !tbaa !17, !UID !30
  val ld_25 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 2, ID = 25, RouteID = 2))

  //  %arrayidx16 = getelementptr inbounds double, double* %img, i64 %idxprom3, !UID !31
  val Gep_arrayidx1626 = Module(new GepNode(NumIns = 1, NumOuts = 4, ID = 26)(ElementSize = 8, ArraySize = List()))

  //  %3 = load double, double* %arrayidx16, align 8, !tbaa !17, !UID !32
  val ld_27 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 2, ID = 27, RouteID = 3))

  //  %add17 = fadd double %2, %3, !UID !33
  val FP_add1728 = Module(new FPComputeNode(NumOuts = 1, ID = 28, opCode = "fadd")(fType))

  //  %sub22 = fsub double %2, %3, !UID !34
  val FP_sub2229 = Module(new FPComputeNode(NumOuts = 1, ID = 29, opCode = "fsub")(fType))

  //  store double %sub22, double* %arrayidx16, align 8, !tbaa !17, !UID !35
  val st_30 = Module(new UnTypStoreCache(NumPredOps = 0, NumSuccOps = 1, ID = 30, RouteID = 10))

  //  store double %add17, double* %arrayidx14, align 8, !tbaa !17, !UID !36
  val st_31 = Module(new UnTypStoreCache(NumPredOps = 0, NumSuccOps = 1, ID = 31, RouteID = 11))

  //  %shl = shl i32 %xor, %log.0115, !UID !37
  val binaryOp_shl32 = Module(new ComputeNode(NumOuts = 1, ID = 32, opCode = "shl")(sign = false, Debug = false))

  //  %and = and i32 %shl, 1023, !UID !38
  val binaryOp_and33 = Module(new ComputeNode(NumOuts = 2, ID = 33, opCode = "and")(sign = false, Debug = false))

  //  %tobool27 = icmp eq i32 %and, 0, !UID !39
  val icmp_tobool2734 = Module(new ComputeNode(NumOuts = 1, ID = 34, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %tobool27, label %for.inc, label %if.then, !UID !40, !BB_UID !41
  val br_35 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 4, ID = 35))

  //  %4 = zext i32 %and to i64, !UID !42
  val sext37 = Module(new ZextNode(NumOuts = 2))

  //  %arrayidx29 = getelementptr inbounds double, double* %real_twid, i64 %4, !UID !43
  val Gep_arrayidx2938 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 38)(ElementSize = 8, ArraySize = List()))

  //  %5 = load double, double* %arrayidx29, align 8, !tbaa !17, !UID !44
  val ld_39 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 2, ID = 39, RouteID = 4))

  //  %6 = load double, double* %arrayidx4, align 8, !tbaa !17, !UID !45
  val ld_40 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 2, ID = 40, RouteID = 5))

  //  %mul = fmul double %5, %6, !UID !46
  val FP_mul41 = Module(new FPComputeNode(NumOuts = 1, ID = 41, opCode = "fmul")(fType))

  //  %arrayidx33 = getelementptr inbounds double, double* %img_twid, i64 %4, !UID !47
  val Gep_arrayidx3342 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 42)(ElementSize = 8, ArraySize = List()))

  //  %7 = load double, double* %arrayidx33, align 8, !tbaa !17, !UID !48
  val ld_43 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 2, ID = 43, RouteID = 6))

  //  %8 = load double, double* %arrayidx16, align 8, !tbaa !17, !UID !49
  val ld_44 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 2, ID = 44, RouteID = 7))

  //  %mul36 = fmul double %7, %8, !UID !50
  val FP_mul3645 = Module(new FPComputeNode(NumOuts = 1, ID = 45, opCode = "fmul")(fType))

  //  %sub37 = fsub double %mul, %mul36, !UID !51
  val FP_sub3746 = Module(new FPComputeNode(NumOuts = 1, ID = 46, opCode = "fsub")(fType))

  //  %mul42 = fmul double %5, %8, !UID !52
  val FP_mul4247 = Module(new FPComputeNode(NumOuts = 1, ID = 47, opCode = "fmul")(fType))

  //  %mul47 = fmul double %6, %7, !UID !53
  val FP_mul4748 = Module(new FPComputeNode(NumOuts = 1, ID = 48, opCode = "fmul")(fType))

  //  %add48 = fadd double %mul47, %mul42, !UID !54
  val FP_add4849 = Module(new FPComputeNode(NumOuts = 1, ID = 49, opCode = "fadd")(fType))

  //  store double %add48, double* %arrayidx16, align 8, !tbaa !17, !UID !55
  val st_50 = Module(new UnTypStoreCache(NumPredOps = 0, NumSuccOps = 1, ID = 50, RouteID = 12))

  //  store double %sub37, double* %arrayidx4, align 8, !tbaa !17, !UID !56
  val st_51 = Module(new UnTypStoreCache(NumPredOps = 0, NumSuccOps = 1, ID = 51, RouteID = 13))

  //  br label %for.inc, !UID !57, !BB_UID !58
  val br_52 = Module(new UBranchNode(NumPredOps=2, ID = 52))

  //  %inc = add nsw i32 %or, 1, !UID !59
  val binaryOp_inc54 = Module(new ComputeNode(NumOuts = 1, ID = 54, opCode = "add")(sign = false, Debug = false))

  //  %cmp = icmp slt i32 %or, 1023, !UID !60
  val icmp_cmp55 = Module(new ComputeNode(NumOuts = 1, ID = 55, opCode = "slt")(sign = true, Debug = false))

  //  br i1 %cmp, label %for.body2, label %for.inc53.loopexit, !UID !61, !BB_UID !62
  val br_56 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 56))

  //  br label %for.inc53, !UID !63, !BB_UID !64
  val br_58 = Module(new UBranchNode(ID = 58))

  //  %shr = ashr i32 %span.0113, 1, !UID !65
  val binaryOp_shr60 = Module(new ComputeNode(NumOuts = 1, ID = 60, opCode = "ashr")(sign = false, Debug = false))

  //  %inc54 = add nuw nsw i32 %log.0115, 1, !UID !66
  val binaryOp_inc5461 = Module(new ComputeNode(NumOuts = 2, ID = 61, opCode = "add")(sign = false, Debug = false))

  //  %exitcond = icmp eq i32 %inc54, 10, !UID !67
  val icmp_exitcond62 = Module(new ComputeNode(NumOuts = 1, ID = 62, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond, label %for.end55, label %inner, !UID !68, !BB_UID !69
  val br_63 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 63))

  //  ret void, !UID !70, !BB_UID !71
  val ret_65 = Module(new RetNode2(retTypes = List(), ID = 65))



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

  bb_entry1.io.predicateIn(0) <> ArgSplitter.io.Out.enable

  bb_for_body2_preheader8.io.predicateIn(0) <> br_7.io.TrueOutput(0)

  bb_if_then36.io.predicateIn(0) <> br_35.io.FalseOutput(0)

  bb_for_inc53.io.predicateIn(0) <> br_35.io.TrueOutput(0)

  bb_for_inc53.io.predicateIn(1) <> br_52.io.Out(0)

  bb_for_inc5359.io.predicateIn(1) <> br_7.io.FalseOutput(0)

  bb_for_inc5359.io.predicateIn(0) <> br_58.io.Out(0)



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE LOOP                     *
   * ================================================================== */

  bb_inner3.io.predicateIn(0) <> Loop_1.io.activate_loop_start

  bb_inner3.io.predicateIn(1) <> Loop_1.io.activate_loop_back

  bb_for_body210.io.predicateIn(0) <> Loop_0.io.activate_loop_start

  bb_for_body210.io.predicateIn(1) <> Loop_0.io.activate_loop_back

  bb_for_inc53_loopexit57.io.predicateIn(0) <> Loop_0.io.loopExit(0)

  bb_for_end5564.io.predicateIn(0) <> Loop_1.io.loopExit(0)



  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */

  Loop_0.io.enable <> br_9.io.Out(0)

  Loop_0.io.loopBack(0) <> br_56.io.TrueOutput(0)

  Loop_0.io.loopFinish(0) <> br_56.io.FalseOutput(0)

  Loop_1.io.enable <> br_2.io.Out(0)

  Loop_1.io.loopBack(0) <> br_63.io.FalseOutput(0)

  Loop_1.io.loopFinish(0) <> br_63.io.TrueOutput(0)



  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

  Loop_0.io.InLiveIn(0) <> phispan_01135.io.Out(0)

  Loop_0.io.InLiveIn(1) <> philog_01154.io.Out(0)

  Loop_0.io.InLiveIn(2) <> Loop_1.io.OutLiveIn.elements("field3")(0)

  Loop_0.io.InLiveIn(3) <> Loop_1.io.OutLiveIn.elements("field2")(0)

  Loop_0.io.InLiveIn(4) <> Loop_1.io.OutLiveIn.elements("field1")(0)

  Loop_0.io.InLiveIn(5) <> Loop_1.io.OutLiveIn.elements("field0")(0)

  Loop_1.io.InLiveIn(0) <> ArgSplitter.io.Out.dataPtrs.elements("field0")(0)

  Loop_1.io.InLiveIn(1) <> ArgSplitter.io.Out.dataPtrs.elements("field1")(0)

  Loop_1.io.InLiveIn(2) <> ArgSplitter.io.Out.dataPtrs.elements("field2")(0)

  Loop_1.io.InLiveIn(3) <> ArgSplitter.io.Out.dataPtrs.elements("field3")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */

  phiodd_011211.io.InData(1) <> Loop_0.io.OutLiveIn.elements("field0")(0)

  binaryOp_or12.io.RightIO <> Loop_0.io.OutLiveIn.elements("field0")(1)

  binaryOp_xor13.io.RightIO <> Loop_0.io.OutLiveIn.elements("field0")(2)

  binaryOp_shl32.io.RightIO <> Loop_0.io.OutLiveIn.elements("field1")(0)

  Gep_arrayidx3342.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field2")(0)

  Gep_arrayidx2938.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field3")(0)

  Gep_arrayidx1424.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field4")(0)

  Gep_arrayidx1626.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field4")(1)

  Gep_arrayidx15.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field5")(0)

  Gep_arrayidx418.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field5")(1)



  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP LIVE OUT DEPENDENCIES                       *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP CARRY DEPENDENCIES                          *
   * ================================================================== */

  Loop_0.io.CarryDepenIn(0) <> binaryOp_inc54.io.Out(0)

  Loop_1.io.CarryDepenIn(0) <> binaryOp_inc5461.io.Out(0)

  Loop_1.io.CarryDepenIn(1) <> binaryOp_shr60.io.Out(0)



  /* ================================================================== *
   *                   LOOP DATA CARRY DEPENDENCIES                     *
   * ================================================================== */

  phiodd_011211.io.InData(0) <> Loop_0.io.CarryDepenOut.elements("field0")(0)

  philog_01154.io.InData(1) <> Loop_1.io.CarryDepenOut.elements("field0")(0)

  phispan_01135.io.InData(1) <> Loop_1.io.CarryDepenOut.elements("field1")(0)



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  br_2.io.enable <> bb_entry1.io.Out(0)


  const0.io.enable <> bb_inner3.io.Out(0)

  const1.io.enable <> bb_inner3.io.Out(1)

  const2.io.enable <> bb_inner3.io.Out(2)

  philog_01154.io.enable <> bb_inner3.io.Out(3)


  phispan_01135.io.enable <> bb_inner3.io.Out(4)


  icmp_cmp1116.io.enable <> bb_inner3.io.Out(5)


  br_7.io.enable <> bb_inner3.io.Out(6)


  br_9.io.enable <> bb_for_body2_preheader8.io.Out(0)


  const3.io.enable <> bb_for_body210.io.Out(0)

  const4.io.enable <> bb_for_body210.io.Out(1)

  phiodd_011211.io.enable <> bb_for_body210.io.Out(2)


  binaryOp_or12.io.enable <> bb_for_body210.io.Out(3)


  binaryOp_xor13.io.enable <> bb_for_body210.io.Out(4)


  sextidxprom14.io.enable <> bb_for_body210.io.Out(5)


  Gep_arrayidx15.io.enable <> bb_for_body210.io.Out(6)


  ld_16.io.enable <> bb_for_body210.io.Out(7)


  sextidxprom317.io.enable <> bb_for_body210.io.Out(8)


  Gep_arrayidx418.io.enable <> bb_for_body210.io.Out(9)


  ld_19.io.enable <> bb_for_body210.io.Out(10)


  FP_add20.io.enable <> bb_for_body210.io.Out(11)


  FP_sub21.io.enable <> bb_for_body210.io.Out(12)


  st_22.io.enable <> bb_for_body210.io.Out(13)


  st_23.io.enable <> bb_for_body210.io.Out(14)


  Gep_arrayidx1424.io.enable <> bb_for_body210.io.Out(15)


  ld_25.io.enable <> bb_for_body210.io.Out(16)


  Gep_arrayidx1626.io.enable <> bb_for_body210.io.Out(17)


  ld_27.io.enable <> bb_for_body210.io.Out(18)


  FP_add1728.io.enable <> bb_for_body210.io.Out(19)


  FP_sub2229.io.enable <> bb_for_body210.io.Out(20)


  st_30.io.enable <> bb_for_body210.io.Out(21)


  st_31.io.enable <> bb_for_body210.io.Out(22)


  binaryOp_shl32.io.enable <> bb_for_body210.io.Out(23)


  binaryOp_and33.io.enable <> bb_for_body210.io.Out(24)


  icmp_tobool2734.io.enable <> bb_for_body210.io.Out(25)


  br_35.io.enable <> bb_for_body210.io.Out(26)


  sext37.io.enable <> bb_if_then36.io.Out(0)


  Gep_arrayidx2938.io.enable <> bb_if_then36.io.Out(1)


  ld_39.io.enable <> bb_if_then36.io.Out(2)


  ld_40.io.enable <> bb_if_then36.io.Out(3)


  FP_mul41.io.enable <> bb_if_then36.io.Out(4)


  Gep_arrayidx3342.io.enable <> bb_if_then36.io.Out(5)


  ld_43.io.enable <> bb_if_then36.io.Out(6)


  ld_44.io.enable <> bb_if_then36.io.Out(7)


  FP_mul3645.io.enable <> bb_if_then36.io.Out(8)


  FP_sub3746.io.enable <> bb_if_then36.io.Out(9)


  FP_mul4247.io.enable <> bb_if_then36.io.Out(10)


  FP_mul4748.io.enable <> bb_if_then36.io.Out(11)


  FP_add4849.io.enable <> bb_if_then36.io.Out(12)


  st_50.io.enable <> bb_if_then36.io.Out(13)


  st_51.io.enable <> bb_if_then36.io.Out(14)


  br_52.io.enable <> bb_if_then36.io.Out(15)


  const5.io.enable <> bb_for_inc53.io.Out(0)

  const6.io.enable <> bb_for_inc53.io.Out(1)

  binaryOp_inc54.io.enable <> bb_for_inc53.io.Out(2)


  icmp_cmp55.io.enable <> bb_for_inc53.io.Out(3)


  br_56.io.enable <> bb_for_inc53.io.Out(4)


  br_58.io.enable <> bb_for_inc53_loopexit57.io.Out(0)


  const7.io.enable <> bb_for_inc5359.io.Out(0)

  const8.io.enable <> bb_for_inc5359.io.Out(1)

  const9.io.enable <> bb_for_inc5359.io.Out(2)

  binaryOp_shr60.io.enable <> bb_for_inc5359.io.Out(3)


  binaryOp_inc5461.io.enable <> bb_for_inc5359.io.Out(4)


  icmp_exitcond62.io.enable <> bb_for_inc5359.io.Out(5)


  br_63.io.enable <> bb_for_inc5359.io.Out(6)


  ret_65.io.In.enable <> bb_for_end5564.io.Out(0)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  philog_01154.io.Mask <> bb_inner3.io.MaskBB(0)

  phispan_01135.io.Mask <> bb_inner3.io.MaskBB(1)

  phiodd_011211.io.Mask <> bb_for_body210.io.MaskBB(0)



  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */

  mem_ctrl_cache.io.rd.mem(0).MemReq <> ld_16.io.MemReq
  ld_16.io.MemResp <> mem_ctrl_cache.io.rd.mem(0).MemResp
  mem_ctrl_cache.io.rd.mem(1).MemReq <> ld_19.io.MemReq
  ld_19.io.MemResp <> mem_ctrl_cache.io.rd.mem(1).MemResp
  mem_ctrl_cache.io.rd.mem(2).MemReq <> ld_25.io.MemReq
  ld_25.io.MemResp <> mem_ctrl_cache.io.rd.mem(2).MemResp
  mem_ctrl_cache.io.rd.mem(3).MemReq <> ld_27.io.MemReq
  ld_27.io.MemResp <> mem_ctrl_cache.io.rd.mem(3).MemResp
  mem_ctrl_cache.io.rd.mem(4).MemReq <> ld_39.io.MemReq
  ld_39.io.MemResp <> mem_ctrl_cache.io.rd.mem(4).MemResp
  mem_ctrl_cache.io.rd.mem(5).MemReq <> ld_40.io.MemReq
  ld_40.io.MemResp <> mem_ctrl_cache.io.rd.mem(5).MemResp
  mem_ctrl_cache.io.rd.mem(6).MemReq <> ld_43.io.MemReq
  ld_43.io.MemResp <> mem_ctrl_cache.io.rd.mem(6).MemResp
  mem_ctrl_cache.io.rd.mem(7).MemReq <> ld_44.io.MemReq
  ld_44.io.MemResp <> mem_ctrl_cache.io.rd.mem(7).MemResp
  mem_ctrl_cache.io.wr.mem(0).MemReq <> st_22.io.MemReq
  st_22.io.MemResp <> mem_ctrl_cache.io.wr.mem(0).MemResp

  mem_ctrl_cache.io.wr.mem(1).MemReq <> st_23.io.MemReq
  st_23.io.MemResp <> mem_ctrl_cache.io.wr.mem(1).MemResp

  mem_ctrl_cache.io.wr.mem(2).MemReq <> st_30.io.MemReq
  st_30.io.MemResp <> mem_ctrl_cache.io.wr.mem(2).MemResp

  mem_ctrl_cache.io.wr.mem(3).MemReq <> st_31.io.MemReq
  st_31.io.MemResp <> mem_ctrl_cache.io.wr.mem(3).MemResp

  mem_ctrl_cache.io.wr.mem(4).MemReq <> st_50.io.MemReq
  st_50.io.MemResp <> mem_ctrl_cache.io.wr.mem(4).MemResp

  mem_ctrl_cache.io.wr.mem(5).MemReq <> st_51.io.MemReq
  st_51.io.MemResp <> mem_ctrl_cache.io.wr.mem(5).MemResp



  /* ================================================================== *
   *                   PRINT SHARED CONNECTIONS                         *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  philog_01154.io.InData(0) <> const0.io.Out

  phispan_01135.io.InData(0) <> const1.io.Out

  icmp_cmp1116.io.RightIO <> const2.io.Out

  binaryOp_and33.io.RightIO <> const3.io.Out

  icmp_tobool2734.io.RightIO <> const4.io.Out

  binaryOp_inc54.io.RightIO <> const5.io.Out

  icmp_cmp55.io.RightIO <> const6.io.Out

  binaryOp_shr60.io.RightIO <> const7.io.Out

  binaryOp_inc5461.io.RightIO <> const8.io.Out

  icmp_exitcond62.io.RightIO <> const9.io.Out

  binaryOp_inc5461.io.LeftIO <> philog_01154.io.Out(1)

  icmp_cmp1116.io.LeftIO <> phispan_01135.io.Out(1)

  binaryOp_shr60.io.LeftIO <> phispan_01135.io.Out(2)

  br_7.io.CmpIO <> icmp_cmp1116.io.Out(0)

  binaryOp_or12.io.LeftIO <> phiodd_011211.io.Out(0)

  binaryOp_xor13.io.LeftIO <> binaryOp_or12.io.Out(0)

  sextidxprom317.io.Input <> binaryOp_or12.io.Out(1)

  binaryOp_inc54.io.LeftIO <> binaryOp_or12.io.Out(2)

  icmp_cmp55.io.LeftIO <> binaryOp_or12.io.Out(3)

  sextidxprom14.io.Input <> binaryOp_xor13.io.Out(0)

  binaryOp_shl32.io.LeftIO <> binaryOp_xor13.io.Out(1)

  Gep_arrayidx15.io.idx(0) <> sextidxprom14.io.Out(0)

  Gep_arrayidx1424.io.idx(0) <> sextidxprom14.io.Out(1)

  ld_16.io.GepAddr <> Gep_arrayidx15.io.Out(0)

  st_23.io.GepAddr <> Gep_arrayidx15.io.Out(1)

  FP_add20.io.LeftIO <> ld_16.io.Out(0)

  FP_sub21.io.LeftIO <> ld_16.io.Out(1)

  Gep_arrayidx418.io.idx(0) <> sextidxprom317.io.Out(0)

  Gep_arrayidx1626.io.idx(0) <> sextidxprom317.io.Out(1)

  ld_19.io.GepAddr <> Gep_arrayidx418.io.Out(0)

  st_22.io.GepAddr <> Gep_arrayidx418.io.Out(1)

  ld_40.io.GepAddr <> Gep_arrayidx418.io.Out(2)

  st_51.io.GepAddr <> Gep_arrayidx418.io.Out(3)

  FP_add20.io.RightIO <> ld_19.io.Out(0)

  FP_sub21.io.RightIO <> ld_19.io.Out(1)

  st_23.io.inData <> FP_add20.io.Out(0)

  st_22.io.inData <> FP_sub21.io.Out(0)

  ld_25.io.GepAddr <> Gep_arrayidx1424.io.Out(0)

  st_31.io.GepAddr <> Gep_arrayidx1424.io.Out(1)

  FP_add1728.io.LeftIO <> ld_25.io.Out(0)

  FP_sub2229.io.LeftIO <> ld_25.io.Out(1)

  ld_27.io.GepAddr <> Gep_arrayidx1626.io.Out(0)

  st_30.io.GepAddr <> Gep_arrayidx1626.io.Out(1)

  ld_44.io.GepAddr <> Gep_arrayidx1626.io.Out(2)

  st_50.io.GepAddr <> Gep_arrayidx1626.io.Out(3)

  FP_add1728.io.RightIO <> ld_27.io.Out(0)

  FP_sub2229.io.RightIO <> ld_27.io.Out(1)

  st_31.io.inData <> FP_add1728.io.Out(0)

  st_30.io.inData <> FP_sub2229.io.Out(0)

  binaryOp_and33.io.LeftIO <> binaryOp_shl32.io.Out(0)

  icmp_tobool2734.io.LeftIO <> binaryOp_and33.io.Out(0)

  sext37.io.Input <> binaryOp_and33.io.Out(1)

  br_35.io.CmpIO <> icmp_tobool2734.io.Out(0)

  Gep_arrayidx2938.io.idx(0) <> sext37.io.Out(0)

  Gep_arrayidx3342.io.idx(0) <> sext37.io.Out(1)

  ld_39.io.GepAddr <> Gep_arrayidx2938.io.Out(0)

  FP_mul41.io.LeftIO <> ld_39.io.Out(0)

  FP_mul4247.io.LeftIO <> ld_39.io.Out(1)

  FP_mul41.io.RightIO <> ld_40.io.Out(0)

  FP_mul4748.io.LeftIO <> ld_40.io.Out(1)

  FP_sub3746.io.LeftIO <> FP_mul41.io.Out(0)

  ld_43.io.GepAddr <> Gep_arrayidx3342.io.Out(0)

  FP_mul3645.io.LeftIO <> ld_43.io.Out(0)

  FP_mul4748.io.RightIO <> ld_43.io.Out(1)

  FP_mul3645.io.RightIO <> ld_44.io.Out(0)

  FP_mul4247.io.RightIO <> ld_44.io.Out(1)

  FP_sub3746.io.RightIO <> FP_mul3645.io.Out(0)

  st_51.io.inData <> FP_sub3746.io.Out(0)

  FP_add4849.io.RightIO <> FP_mul4247.io.Out(0)

  FP_add4849.io.LeftIO <> FP_mul4748.io.Out(0)

  st_50.io.inData <> FP_add4849.io.Out(0)

  br_56.io.CmpIO <> icmp_cmp55.io.Out(0)

  icmp_exitcond62.io.LeftIO <> binaryOp_inc5461.io.Out(1)

  br_63.io.CmpIO <> icmp_exitcond62.io.Out(0)

  st_22.io.Out(0).ready := true.B

  st_23.io.Out(0).ready := true.B

  st_30.io.Out(0).ready := true.B

  st_31.io.Out(0).ready := true.B

  st_50.io.Out(0).ready := true.B

  st_51.io.Out(0).ready := true.B



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  br_35.io.PredOp(0) <> st_22.io.SuccOp(0)

  br_35.io.PredOp(1) <> st_23.io.SuccOp(0)

  br_35.io.PredOp(2) <> st_30.io.SuccOp(0)

  br_35.io.PredOp(3) <> st_31.io.SuccOp(0)

  br_52.io.PredOp(0) <> st_50.io.SuccOp(0)

  br_52.io.PredOp(1) <> st_51.io.SuccOp(0)



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_65.io.Out

}

