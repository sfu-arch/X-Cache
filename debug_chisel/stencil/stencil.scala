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


class stencilDF(PtrsIn: Seq[Int] = List(64, 64), ValsIn: Seq[Int] = List(), Returns: Seq[Int] = List())
			(implicit p: Parameters) extends DandelionAccelDCRModule(PtrsIn, ValsIn, Returns){


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  //Cache
  val mem_ctrl_cache = Module(new CacheMemoryEngine(ID = 0, NumRead = 3, NumWrite = 2))

  io.MemReq <> mem_ctrl_cache.io.cache.MemReq
  mem_ctrl_cache.io.cache.MemResp <> io.MemResp

  val ArgSplitter = Module(new SplitCallDCR(ptrsArgTypes = List(1, 1), valsArgTypes = List()))
  ArgSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val Loop_0 = Module(new LoopBlockNode(NumIns = List(1, 1, 1, 2, 2), NumOuts = List(), NumCarry = List(1, 1), NumExits = 1, ID = 0))

  val Loop_1 = Module(new LoopBlockNode(NumIns = List(1, 1, 1, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 1))

  val Loop_2 = Module(new LoopBlockNode(NumIns = List(2, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 2))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_entry1 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 1))

  val bb_for_cond_cleanup3 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 3))

  val bb_for_body5 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 14, NumPhi = 1, BID = 5))

  val bb_for_cond_cleanup315 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 12, BID = 15))

  val bb_for_body424 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 8, NumPhi = 1, BID = 24))

  val bb_for_cond_cleanup730 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 5, BID = 30))

  val bb_for_body834 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 10, NumPhi = 2, BID = 34))

  val bb_if_then1342 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 9, BID = 42))

  val bb_if_end2052 = Module(new BasicBlockNoMaskFastNode(NumInputs = 2, NumOuts = 7, BID = 52))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  br label %for.body, !dbg !35, !UID !36, !BB_UID !37
  val br_2 = Module(new UBranchNode(ID = 2))

  //  ret void, !dbg !38, !UID !39, !BB_UID !40
  val ret_4 = Module(new RetNode2(retTypes = List(), ID = 4))

  //  %indvars.iv66 = phi i64 [ 0, %entry ], [ %indvars.iv.next67, %for.cond.cleanup3 ], !UID !41
  val phiindvars_iv666 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 5, ID = 6, Res = true))

  //  %0 = trunc i64 %indvars.iv66 to i32, !dbg !42, !UID !43
  val trunc7 = Module(new TruncNode(NumOuts = 1))

  //  %div = lshr i32 %0, 6, !dbg !42, !UID !44
  val binaryOp_div8 = Module(new ComputeNode(NumOuts = 1, ID = 8, opCode = "lshr")(sign = false, Debug = false))

  //  %1 = trunc i64 %indvars.iv66 to i32, !dbg !46, !UID !47
  val trunc9 = Module(new TruncNode(NumOuts = 1))

  //  %and = and i32 %1, 63, !dbg !46, !UID !48
  val binaryOp_and10 = Module(new ComputeNode(NumOuts = 1, ID = 10, opCode = "and")(sign = false, Debug = false))

  //  %add = add nsw i32 %div, -1, !UID !51
  val binaryOp_add11 = Module(new ComputeNode(NumOuts = 1, ID = 11, opCode = "add")(sign = false, Debug = false))

  //  %add9 = add nsw i32 %and, -1, !UID !52
  val binaryOp_add912 = Module(new ComputeNode(NumOuts = 1, ID = 12, opCode = "add")(sign = false, Debug = false))

  //  %arrayidx18 = getelementptr inbounds i32, i32* %out, i64 %indvars.iv66, !UID !53
  val Gep_arrayidx1813 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 13)(ElementSize = 8, ArraySize = List()))

  //  br label %for.body4, !dbg !54, !UID !55, !BB_UID !56
  val br_14 = Module(new UBranchNode(ID = 14))

  //  %arrayidx27 = getelementptr inbounds i32, i32* %out, i64 %indvars.iv66, !dbg !57, !UID !58
  val Gep_arrayidx2716 = Module(new GepNode(NumIns = 1, NumOuts = 2, ID = 16)(ElementSize = 8, ArraySize = List()))

  //  %2 = load i32, i32* %arrayidx27, align 4, !dbg !57, !tbaa !59, !UID !63
  val ld_17 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 17, RouteID = 0))

  //  %add28 = add i32 %2, 9, !dbg !64, !UID !65
  val binaryOp_add2818 = Module(new ComputeNode(NumOuts = 1, ID = 18, opCode = "add")(sign = false, Debug = false))

  //  %div29 = udiv i32 %add28, 9, !dbg !66, !UID !67
  val binaryOp_div2919 = Module(new ComputeNode(NumOuts = 1, ID = 19, opCode = "udiv")(sign = false, Debug = false))

  //  store i32 %div29, i32* %arrayidx27, align 4, !dbg !68, !tbaa !59, !UID !69
  val st_20 = Module(new UnTypStoreCache(NumPredOps = 0, NumSuccOps = 1, ID = 20, RouteID = 3))

  //  %indvars.iv.next67 = add nuw nsw i64 %indvars.iv66, 1, !dbg !70, !UID !71
  val binaryOp_indvars_iv_next6721 = Module(new ComputeNode(NumOuts = 2, ID = 21, opCode = "add")(sign = false, Debug = false))

  //  %exitcond68 = icmp eq i64 %indvars.iv.next67, 8192, !dbg !72, !UID !73
  val icmp_exitcond6822 = Module(new ComputeNode(NumOuts = 1, ID = 22, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond68, label %for.cond.cleanup, label %for.body, !dbg !35, !llvm.loop !74, !UID !76, !BB_UID !77
  val br_23 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 1, ID = 23))

  //  %nr.062 = phi i32 [ 0, %for.body ], [ %inc22, %for.cond.cleanup7 ], !UID !78
  val phinr_06225 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 25, Res = true))

  //  %sub = add nsw i32 %add, %nr.062, !UID !80
  val binaryOp_sub26 = Module(new ComputeNode(NumOuts = 2, ID = 26, opCode = "add")(sign = false, Debug = false))

  //  %cmp11 = icmp ult i32 %sub, 128, !UID !81
  val icmp_cmp1127 = Module(new ComputeNode(NumOuts = 1, ID = 27, opCode = "slt")(sign = false, Debug = false))

  //  %mul = shl i32 %sub, 6, !UID !82
  val binaryOp_mul28 = Module(new ComputeNode(NumOuts = 1, ID = 28, opCode = "shl")(sign = false, Debug = false))

  //  br label %for.body8, !dbg !83, !UID !84, !BB_UID !85
  val br_29 = Module(new UBranchNode(ID = 29))

  //  %inc22 = add nuw nsw i32 %nr.062, 1, !dbg !86, !UID !87
  val binaryOp_inc2231 = Module(new ComputeNode(NumOuts = 2, ID = 31, opCode = "add")(sign = false, Debug = false))

  //  %exitcond65 = icmp eq i32 %inc22, 3, !dbg !88, !UID !89
  val icmp_exitcond6532 = Module(new ComputeNode(NumOuts = 1, ID = 32, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond65, label %for.cond.cleanup3, label %for.body4, !dbg !54, !llvm.loop !90, !UID !92, !BB_UID !93
  val br_33 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 33))

  //  %indvars.iv = phi i64 [ 0, %for.body4 ], [ %indvars.iv.next, %if.end20 ], !UID !94
  val phiindvars_iv35 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 35, Res = true))

  //  %nc.061 = phi i32 [ 0, %for.body4 ], [ %inc, %if.end20 ], !UID !95
  val phinc_06136 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 36, Res = true))

  //  %3 = trunc i64 %indvars.iv to i32, !dbg !98, !UID !102
  val trunc37 = Module(new TruncNode(NumOuts = 1))

  //  %4 = add i32 %add9, %3, !dbg !98, !UID !103
  val binaryOp_38 = Module(new ComputeNode(NumOuts = 1, ID = 38, opCode = "add")(sign = false, Debug = false))

  //  %cmp12 = icmp ult i32 %4, 64, !dbg !98, !UID !104
  val icmp_cmp1239 = Module(new ComputeNode(NumOuts = 1, ID = 39, opCode = "slt")(sign = false, Debug = false))

  //  %or.cond = and i1 %cmp11, %cmp12, !dbg !105, !UID !106
  val binaryOp_or_cond40 = Module(new ComputeNode(NumOuts = 1, ID = 40, opCode = "and")(sign = false, Debug = false))

  //  br i1 %or.cond, label %if.then13, label %if.end20, !dbg !105, !UID !107, !BB_UID !108
  val br_41 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 41))

  //  %sub10 = add nsw i32 %add9, %nc.061, !dbg !109, !UID !110
  val binaryOp_sub1043 = Module(new ComputeNode(NumOuts = 1, ID = 43, opCode = "add")(sign = false, Debug = false))

  //  %add14 = add i32 %sub10, %mul, !dbg !111, !UID !113
  val binaryOp_add1444 = Module(new ComputeNode(NumOuts = 1, ID = 44, opCode = "add")(sign = false, Debug = false))

  //  %idxprom = zext i32 %add14 to i64, !dbg !114, !UID !115
  val sextidxprom45 = Module(new ZextNode(NumOuts = 1))

  //  %arrayidx = getelementptr inbounds i32, i32* %in, i64 %idxprom, !dbg !114, !UID !116
  val Gep_arrayidx46 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 46)(ElementSize = 8, ArraySize = List()))

  //  %5 = load i32, i32* %arrayidx, align 4, !dbg !114, !tbaa !59, !UID !117
  val ld_47 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 47, RouteID = 1))

  //  %6 = load i32, i32* %arrayidx18, align 4, !dbg !118, !tbaa !59, !UID !119
  val ld_48 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 48, RouteID = 2))

  //  %add19 = add i32 %6, %5, !dbg !118, !UID !120
  val binaryOp_add1949 = Module(new ComputeNode(NumOuts = 1, ID = 49, opCode = "add")(sign = false, Debug = false))

  //  store i32 %add19, i32* %arrayidx18, align 4, !dbg !118, !tbaa !59, !UID !121
  val st_50 = Module(new UnTypStoreCache(NumPredOps = 0, NumSuccOps = 1, ID = 50, RouteID = 4))

  //  br label %if.end20, !dbg !122, !UID !123, !BB_UID !124
  val br_51 = Module(new UBranchNode(NumPredOps=1, ID = 51))

  //  %indvars.iv.next = add nuw nsw i64 %indvars.iv, 1, !dbg !125, !UID !126
  val binaryOp_indvars_iv_next53 = Module(new ComputeNode(NumOuts = 2, ID = 53, opCode = "add")(sign = false, Debug = false))

  //  %inc = add nuw nsw i32 %nc.061, 1, !dbg !125, !UID !127
  val binaryOp_inc54 = Module(new ComputeNode(NumOuts = 1, ID = 54, opCode = "add")(sign = false, Debug = false))

  //  %exitcond = icmp eq i64 %indvars.iv.next, 3, !dbg !128, !UID !129
  val icmp_exitcond55 = Module(new ComputeNode(NumOuts = 1, ID = 55, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond, label %for.cond.cleanup7, label %for.body8, !dbg !83, !llvm.loop !130, !UID !132, !BB_UID !133
  val br_56 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 56))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i64 0
  val const0 = Module(new ConstFastNode(value = 0, ID = 0))

  //i32 6
  val const1 = Module(new ConstFastNode(value = 6, ID = 1))

  //i32 63
  val const2 = Module(new ConstFastNode(value = 63, ID = 2))

  //i32 -1
  val const3 = Module(new ConstFastNode(value = -1, ID = 3))

  //i32 -1
  val const4 = Module(new ConstFastNode(value = -1, ID = 4))

  //i32 9
  val const5 = Module(new ConstFastNode(value = 9, ID = 5))

  //i32 9
  val const6 = Module(new ConstFastNode(value = 9, ID = 6))

  //i64 1
  val const7 = Module(new ConstFastNode(value = 1, ID = 7))

  //i64 8192
  val const8 = Module(new ConstFastNode(value = 8192, ID = 8))

  //i32 0
  val const9 = Module(new ConstFastNode(value = 0, ID = 9))

  //i32 128
  val const10 = Module(new ConstFastNode(value = 128, ID = 10))

  //i32 6
  val const11 = Module(new ConstFastNode(value = 6, ID = 11))

  //i32 1
  val const12 = Module(new ConstFastNode(value = 1, ID = 12))

  //i32 3
  val const13 = Module(new ConstFastNode(value = 3, ID = 13))

  //i64 0
  val const14 = Module(new ConstFastNode(value = 0, ID = 14))

  //i32 0
  val const15 = Module(new ConstFastNode(value = 0, ID = 15))

  //i32 64
  val const16 = Module(new ConstFastNode(value = 64, ID = 16))

  //i64 1
  val const17 = Module(new ConstFastNode(value = 1, ID = 17))

  //i32 1
  val const18 = Module(new ConstFastNode(value = 1, ID = 18))

  //i64 3
  val const19 = Module(new ConstFastNode(value = 3, ID = 19))



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_entry1.io.predicateIn(0) <> ArgSplitter.io.Out.enable

  bb_if_then1342.io.predicateIn(0) <> br_41.io.TrueOutput(0)

  bb_if_end2052.io.predicateIn(1) <> br_41.io.FalseOutput(0)

  bb_if_end2052.io.predicateIn(0) <> br_51.io.Out(0)



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE LOOP                     *
   * ================================================================== */

  bb_for_cond_cleanup3.io.predicateIn(0) <> Loop_2.io.loopExit(0)

  bb_for_body5.io.predicateIn(1) <> Loop_2.io.activate_loop_start

  bb_for_body5.io.predicateIn(0) <> Loop_2.io.activate_loop_back

  bb_for_cond_cleanup315.io.predicateIn(0) <> Loop_1.io.loopExit(0)

  bb_for_body424.io.predicateIn(1) <> Loop_1.io.activate_loop_start

  bb_for_body424.io.predicateIn(0) <> Loop_1.io.activate_loop_back

  bb_for_cond_cleanup730.io.predicateIn(0) <> Loop_0.io.loopExit(0)

  bb_for_body834.io.predicateIn(1) <> Loop_0.io.activate_loop_start

  bb_for_body834.io.predicateIn(0) <> Loop_0.io.activate_loop_back



  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */

  Loop_0.io.enable <> br_29.io.Out(0)

  Loop_0.io.loopBack(0) <> br_56.io.FalseOutput(0)

  Loop_0.io.loopFinish(0) <> br_56.io.TrueOutput(0)

  Loop_1.io.enable <> br_14.io.Out(0)

  Loop_1.io.loopBack(0) <> br_33.io.FalseOutput(0)

  Loop_1.io.loopFinish(0) <> br_33.io.TrueOutput(0)

  Loop_2.io.enable <> br_2.io.Out(0)

  Loop_2.io.loopBack(0) <> br_23.io.FalseOutput(0)

  Loop_2.io.loopFinish(0) <> br_23.io.TrueOutput(0)



  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

  Loop_0.io.InLiveIn(0) <> icmp_cmp1127.io.Out(0)

  Loop_0.io.InLiveIn(1) <> binaryOp_mul28.io.Out(0)

  Loop_0.io.InLiveIn(2) <> Loop_1.io.OutLiveIn.elements("field3")(0)

  Loop_0.io.InLiveIn(3) <> Loop_1.io.OutLiveIn.elements("field2")(0)

  Loop_0.io.InLiveIn(4) <> Loop_1.io.OutLiveIn.elements("field1")(0)

  Loop_1.io.InLiveIn(0) <> binaryOp_add11.io.Out(0)

  Loop_1.io.InLiveIn(1) <> binaryOp_add912.io.Out(0)

  Loop_1.io.InLiveIn(2) <> Gep_arrayidx1813.io.Out(0)

  Loop_1.io.InLiveIn(3) <> Loop_2.io.OutLiveIn.elements("field1")(0)

  Loop_2.io.InLiveIn(0) <> ArgSplitter.io.Out.dataPtrs.elements("field1")(0)

  Loop_2.io.InLiveIn(1) <> ArgSplitter.io.Out.dataPtrs.elements("field0")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */

  binaryOp_or_cond40.io.LeftIO <> Loop_0.io.OutLiveIn.elements("field0")(0)

  binaryOp_add1444.io.RightIO <> Loop_0.io.OutLiveIn.elements("field1")(0)

  Gep_arrayidx46.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field2")(0)

  ld_48.io.GepAddr <> Loop_0.io.OutLiveIn.elements("field3")(0)

  st_50.io.GepAddr <> Loop_0.io.OutLiveIn.elements("field3")(1)

  binaryOp_38.io.LeftIO <> Loop_0.io.OutLiveIn.elements("field4")(0)

  binaryOp_sub1043.io.LeftIO <> Loop_0.io.OutLiveIn.elements("field4")(1)

  binaryOp_sub26.io.LeftIO <> Loop_1.io.OutLiveIn.elements("field0")(0)

  Gep_arrayidx1813.io.baseAddress <> Loop_2.io.OutLiveIn.elements("field0")(0)

  Gep_arrayidx2716.io.baseAddress <> Loop_2.io.OutLiveIn.elements("field0")(1)



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

  Loop_0.io.CarryDepenIn(1) <> binaryOp_indvars_iv_next53.io.Out(0)

  Loop_1.io.CarryDepenIn(0) <> binaryOp_inc2231.io.Out(0)

  Loop_2.io.CarryDepenIn(0) <> binaryOp_indvars_iv_next6721.io.Out(0)



  /* ================================================================== *
   *                   LOOP DATA CARRY DEPENDENCIES                     *
   * ================================================================== */

  phinc_06136.io.InData(1) <> Loop_0.io.CarryDepenOut.elements("field0")(0)

  phiindvars_iv35.io.InData(1) <> Loop_0.io.CarryDepenOut.elements("field1")(0)

  phinr_06225.io.InData(1) <> Loop_1.io.CarryDepenOut.elements("field0")(0)

  phiindvars_iv666.io.InData(1) <> Loop_2.io.CarryDepenOut.elements("field0")(0)



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  br_2.io.enable <> bb_entry1.io.Out(0)


  ret_4.io.In.enable <> bb_for_cond_cleanup3.io.Out(0)


  const0.io.enable <> bb_for_body5.io.Out(0)

  const1.io.enable <> bb_for_body5.io.Out(1)

  const2.io.enable <> bb_for_body5.io.Out(2)

  const3.io.enable <> bb_for_body5.io.Out(3)

  const4.io.enable <> bb_for_body5.io.Out(4)

  phiindvars_iv666.io.enable <> bb_for_body5.io.Out(5)


  trunc7.io.enable <> bb_for_body5.io.Out(6)


  binaryOp_div8.io.enable <> bb_for_body5.io.Out(7)


  trunc9.io.enable <> bb_for_body5.io.Out(8)


  binaryOp_and10.io.enable <> bb_for_body5.io.Out(9)


  binaryOp_add11.io.enable <> bb_for_body5.io.Out(10)


  binaryOp_add912.io.enable <> bb_for_body5.io.Out(11)


  Gep_arrayidx1813.io.enable <> bb_for_body5.io.Out(12)


  br_14.io.enable <> bb_for_body5.io.Out(13)


  const5.io.enable <> bb_for_cond_cleanup315.io.Out(0)

  const6.io.enable <> bb_for_cond_cleanup315.io.Out(1)

  const7.io.enable <> bb_for_cond_cleanup315.io.Out(2)

  const8.io.enable <> bb_for_cond_cleanup315.io.Out(3)

  Gep_arrayidx2716.io.enable <> bb_for_cond_cleanup315.io.Out(4)


  ld_17.io.enable <> bb_for_cond_cleanup315.io.Out(5)


  binaryOp_add2818.io.enable <> bb_for_cond_cleanup315.io.Out(6)


  binaryOp_div2919.io.enable <> bb_for_cond_cleanup315.io.Out(7)


  st_20.io.enable <> bb_for_cond_cleanup315.io.Out(8)


  binaryOp_indvars_iv_next6721.io.enable <> bb_for_cond_cleanup315.io.Out(9)


  icmp_exitcond6822.io.enable <> bb_for_cond_cleanup315.io.Out(10)


  br_23.io.enable <> bb_for_cond_cleanup315.io.Out(11)


  const9.io.enable <> bb_for_body424.io.Out(0)

  const10.io.enable <> bb_for_body424.io.Out(1)

  const11.io.enable <> bb_for_body424.io.Out(2)

  phinr_06225.io.enable <> bb_for_body424.io.Out(3)


  binaryOp_sub26.io.enable <> bb_for_body424.io.Out(4)


  icmp_cmp1127.io.enable <> bb_for_body424.io.Out(5)


  binaryOp_mul28.io.enable <> bb_for_body424.io.Out(6)


  br_29.io.enable <> bb_for_body424.io.Out(7)


  const12.io.enable <> bb_for_cond_cleanup730.io.Out(0)

  const13.io.enable <> bb_for_cond_cleanup730.io.Out(1)

  binaryOp_inc2231.io.enable <> bb_for_cond_cleanup730.io.Out(2)


  icmp_exitcond6532.io.enable <> bb_for_cond_cleanup730.io.Out(3)


  br_33.io.enable <> bb_for_cond_cleanup730.io.Out(4)


  const14.io.enable <> bb_for_body834.io.Out(0)

  const15.io.enable <> bb_for_body834.io.Out(1)

  const16.io.enable <> bb_for_body834.io.Out(2)

  phiindvars_iv35.io.enable <> bb_for_body834.io.Out(3)


  phinc_06136.io.enable <> bb_for_body834.io.Out(4)


  trunc37.io.enable <> bb_for_body834.io.Out(5)


  binaryOp_38.io.enable <> bb_for_body834.io.Out(6)


  icmp_cmp1239.io.enable <> bb_for_body834.io.Out(7)


  binaryOp_or_cond40.io.enable <> bb_for_body834.io.Out(8)


  br_41.io.enable <> bb_for_body834.io.Out(9)


  binaryOp_sub1043.io.enable <> bb_if_then1342.io.Out(0)


  binaryOp_add1444.io.enable <> bb_if_then1342.io.Out(1)


  sextidxprom45.io.enable <> bb_if_then1342.io.Out(2)


  Gep_arrayidx46.io.enable <> bb_if_then1342.io.Out(3)


  ld_47.io.enable <> bb_if_then1342.io.Out(4)


  ld_48.io.enable <> bb_if_then1342.io.Out(5)


  binaryOp_add1949.io.enable <> bb_if_then1342.io.Out(6)


  st_50.io.enable <> bb_if_then1342.io.Out(7)


  br_51.io.enable <> bb_if_then1342.io.Out(8)


  const17.io.enable <> bb_if_end2052.io.Out(0)

  const18.io.enable <> bb_if_end2052.io.Out(1)

  const19.io.enable <> bb_if_end2052.io.Out(2)

  binaryOp_indvars_iv_next53.io.enable <> bb_if_end2052.io.Out(3)


  binaryOp_inc54.io.enable <> bb_if_end2052.io.Out(4)


  icmp_exitcond55.io.enable <> bb_if_end2052.io.Out(5)


  br_56.io.enable <> bb_if_end2052.io.Out(6)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  phiindvars_iv666.io.Mask <> bb_for_body5.io.MaskBB(0)

  phinr_06225.io.Mask <> bb_for_body424.io.MaskBB(0)

  phiindvars_iv35.io.Mask <> bb_for_body834.io.MaskBB(0)

  phinc_06136.io.Mask <> bb_for_body834.io.MaskBB(1)



  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */

  mem_ctrl_cache.io.rd.mem(0).MemReq <> ld_17.io.MemReq
  ld_17.io.MemResp <> mem_ctrl_cache.io.rd.mem(0).MemResp
  mem_ctrl_cache.io.rd.mem(1).MemReq <> ld_47.io.MemReq
  ld_47.io.MemResp <> mem_ctrl_cache.io.rd.mem(1).MemResp
  mem_ctrl_cache.io.rd.mem(2).MemReq <> ld_48.io.MemReq
  ld_48.io.MemResp <> mem_ctrl_cache.io.rd.mem(2).MemResp
  mem_ctrl_cache.io.wr.mem(0).MemReq <> st_20.io.MemReq
  st_20.io.MemResp <> mem_ctrl_cache.io.wr.mem(0).MemResp

  mem_ctrl_cache.io.wr.mem(1).MemReq <> st_50.io.MemReq
  st_50.io.MemResp <> mem_ctrl_cache.io.wr.mem(1).MemResp



  /* ================================================================== *
   *                   PRINT SHARED CONNECTIONS                         *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  phiindvars_iv666.io.InData(0) <> const0.io.Out

  binaryOp_div8.io.RightIO <> const1.io.Out

  binaryOp_and10.io.RightIO <> const2.io.Out

  binaryOp_add11.io.RightIO <> const3.io.Out

  binaryOp_add912.io.RightIO <> const4.io.Out

  binaryOp_add2818.io.RightIO <> const5.io.Out

  binaryOp_div2919.io.RightIO <> const6.io.Out

  binaryOp_indvars_iv_next6721.io.RightIO <> const7.io.Out

  icmp_exitcond6822.io.RightIO <> const8.io.Out

  phinr_06225.io.InData(0) <> const9.io.Out

  icmp_cmp1127.io.RightIO <> const10.io.Out

  binaryOp_mul28.io.RightIO <> const11.io.Out

  binaryOp_inc2231.io.RightIO <> const12.io.Out

  icmp_exitcond6532.io.RightIO <> const13.io.Out

  phiindvars_iv35.io.InData(0) <> const14.io.Out

  phinc_06136.io.InData(0) <> const15.io.Out

  icmp_cmp1239.io.RightIO <> const16.io.Out

  binaryOp_indvars_iv_next53.io.RightIO <> const17.io.Out

  binaryOp_inc54.io.RightIO <> const18.io.Out

  icmp_exitcond55.io.RightIO <> const19.io.Out

  trunc7.io.Input <> phiindvars_iv666.io.Out(0)

  trunc9.io.Input <> phiindvars_iv666.io.Out(1)

  Gep_arrayidx1813.io.idx(0) <> phiindvars_iv666.io.Out(2)

  Gep_arrayidx2716.io.idx(0) <> phiindvars_iv666.io.Out(3)

  binaryOp_indvars_iv_next6721.io.LeftIO <> phiindvars_iv666.io.Out(4)

  binaryOp_div8.io.LeftIO <> trunc7.io.Out(0)

  binaryOp_add11.io.LeftIO <> binaryOp_div8.io.Out(0)

  binaryOp_and10.io.LeftIO <> trunc9.io.Out(0)

  binaryOp_add912.io.LeftIO <> binaryOp_and10.io.Out(0)

  ld_17.io.GepAddr <> Gep_arrayidx2716.io.Out(0)

  st_20.io.GepAddr <> Gep_arrayidx2716.io.Out(1)

  binaryOp_add2818.io.LeftIO <> ld_17.io.Out(0)

  binaryOp_div2919.io.LeftIO <> binaryOp_add2818.io.Out(0)

  st_20.io.inData <> binaryOp_div2919.io.Out(0)

  icmp_exitcond6822.io.LeftIO <> binaryOp_indvars_iv_next6721.io.Out(1)

  br_23.io.CmpIO <> icmp_exitcond6822.io.Out(0)

  binaryOp_sub26.io.RightIO <> phinr_06225.io.Out(0)

  binaryOp_inc2231.io.LeftIO <> phinr_06225.io.Out(1)

  icmp_cmp1127.io.LeftIO <> binaryOp_sub26.io.Out(0)

  binaryOp_mul28.io.LeftIO <> binaryOp_sub26.io.Out(1)

  icmp_exitcond6532.io.LeftIO <> binaryOp_inc2231.io.Out(1)

  br_33.io.CmpIO <> icmp_exitcond6532.io.Out(0)

  trunc37.io.Input <> phiindvars_iv35.io.Out(0)

  binaryOp_indvars_iv_next53.io.LeftIO <> phiindvars_iv35.io.Out(1)

  binaryOp_sub1043.io.RightIO <> phinc_06136.io.Out(0)

  binaryOp_inc54.io.LeftIO <> phinc_06136.io.Out(1)

  binaryOp_38.io.RightIO <> trunc37.io.Out(0)

  icmp_cmp1239.io.LeftIO <> binaryOp_38.io.Out(0)

  binaryOp_or_cond40.io.RightIO <> icmp_cmp1239.io.Out(0)

  br_41.io.CmpIO <> binaryOp_or_cond40.io.Out(0)

  binaryOp_add1444.io.LeftIO <> binaryOp_sub1043.io.Out(0)

  sextidxprom45.io.Input <> binaryOp_add1444.io.Out(0)

  Gep_arrayidx46.io.idx(0) <> sextidxprom45.io.Out(0)

  ld_47.io.GepAddr <> Gep_arrayidx46.io.Out(0)

  binaryOp_add1949.io.RightIO <> ld_47.io.Out(0)

  binaryOp_add1949.io.LeftIO <> ld_48.io.Out(0)

  st_50.io.inData <> binaryOp_add1949.io.Out(0)

  icmp_exitcond55.io.LeftIO <> binaryOp_indvars_iv_next53.io.Out(1)

  br_56.io.CmpIO <> icmp_exitcond55.io.Out(0)

  st_20.io.Out(0).ready := true.B

  st_50.io.Out(0).ready := true.B



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  br_23.io.PredOp(0) <> st_20.io.SuccOp(0)

  br_51.io.PredOp(0) <> st_50.io.SuccOp(0)



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_4.io.Out

}

