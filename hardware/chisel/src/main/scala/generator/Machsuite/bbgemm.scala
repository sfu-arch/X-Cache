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


class bbgemmDF(PtrsIn: Seq[Int] = List(32, 32, 32), ValsIn: Seq[Int] = List(), Returns: Seq[Int] = List())
			(implicit p: Parameters) extends DandelionAccelDCRModule(PtrsIn, ValsIn, Returns){


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  val MemCtrl = Module(new CacheMemoryEngine(ID = 0, NumRead = 3, NumWrite = 1))

  io.MemReq <> MemCtrl.io.cache.MemReq
  MemCtrl.io.cache.MemResp <> io.MemResp
  val ArgSplitter = Module(new SplitCallDCR(ptrsArgTypes = List(1, 1, 1), valsArgTypes = List()))
  ArgSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val Loop_0 = Module(new LoopBlockNode(NumIns = List(1, 1, 1, 1, 1, 2), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 0))

  val Loop_1 = Module(new LoopBlockNode(NumIns = List(2, 1, 1, 1, 2, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 1))

  val Loop_2 = Module(new LoopBlockNode(NumIns = List(1, 1, 1, 1, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 2))

  val Loop_3 = Module(new LoopBlockNode(NumIns = List(1, 1, 1, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 3))

  val Loop_4 = Module(new LoopBlockNode(NumIns = List(1, 1, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 4))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_entry0 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 0))

  val bb_loopkk1 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 3, NumPhi = 1, BID = 1))

  val bb_loopi2 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 3, NumPhi = 1, BID = 2))

  val bb_loopk3 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 5, NumPhi = 1, BID = 3))

  val bb_for_body94 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 10, NumPhi = 1, BID = 4))

  val bb_for_body165 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 18, NumPhi = 1, BID = 5))

  val bb_for_inc276 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 5, BID = 6))

  val bb_for_inc307 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 5, BID = 7))

  val bb_for_inc338 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 5, BID = 8))

  val bb_for_inc369 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 5, BID = 9))

  val bb_for_end3810 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 10))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  br label %loopkk, !UID !3, !BB_UID !4
  val br_0 = Module(new UBranchNode(ID = 0))

  //  %indvars.iv84 = phi i64 [ 0, %entry ], [ %indvars.iv.next85, %for.inc36 ], !UID !5
  val phiindvars_iv841 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 1, Res = false))

  //  br label %loopi, !UID !6, !BB_UID !7
  val br_2 = Module(new UBranchNode(ID = 2))

  //  %indvars.iv82 = phi i64 [ 0, %loopkk ], [ %indvars.iv.next83, %for.inc33 ], !UID !8
  val phiindvars_iv823 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 3, Res = false))

  //  br label %loopk, !UID !9, !BB_UID !10
  val br_4 = Module(new UBranchNode(ID = 4))

  //  %indvars.iv78 = phi i64 [ 0, %loopi ], [ %indvars.iv.next79, %for.inc30 ], !UID !11
  val phiindvars_iv785 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 5, Res = true))

  //  %0 = shl nsw i64 %indvars.iv78, 4, !UID !12
  val binaryOp_6 = Module(new ComputeNode(NumOuts = 1, ID = 6, opCode = "shl")(sign = false, Debug = true, GuardVals=Seq.tabulate(1000)(n => n)))

  //  br label %for.body9, !UID !13, !BB_UID !14
  val br_7 = Module(new UBranchNode(ID = 7))

  //  %indvars.iv71 = phi i64 [ 0, %loopk ], [ %indvars.iv.next72, %for.inc27 ], !UID !15
  val phiindvars_iv718 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 3, ID = 8, Res = true))

  //  %1 = add nuw nsw i64 %indvars.iv71, %indvars.iv82, !UID !16
  val binaryOp_9 = Module(new ComputeNode(NumOuts = 1, ID = 9, opCode = "add")(sign = false, Debug = true, GuardVals=Seq.tabulate(1000)(n => n)))

  //  %2 = shl i64 %1, 4, !UID !17
  val binaryOp_10 = Module(new ComputeNode(NumOuts = 1, ID = 10, opCode = "shl")(sign = false, Debug = true, GuardVals=Seq.tabulate(1000)(n => n)))

  //  %3 = add nuw nsw i64 %indvars.iv71, %indvars.iv82, !UID !18
  val binaryOp_11 = Module(new ComputeNode(NumOuts = 1, ID = 11, opCode = "add")(sign = false, Debug = true, GuardVals=Seq.tabulate(1000)(n => n)))

  //  %4 = add nuw nsw i64 %3, %0, !UID !19
  val binaryOp_12 = Module(new ComputeNode(NumOuts = 1, ID = 12, opCode = "add")(sign = false, Debug = true, GuardVals=Seq.tabulate(1000)(n => n)))

  //  %arrayidx = getelementptr inbounds double, double* %m1, i64 %4, !UID !20
  val Gep_arrayidx13 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 13)(ElementSize = 8, ArraySize = List()))

  //  %5 = load double, double* %arrayidx, align 8, !tbaa !21, !UID !25
  val ld_14 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 14, RouteID = 0))

  //  br label %for.body16, !UID !26, !BB_UID !27
  val br_15 = Module(new UBranchNode(ID = 15))

  //  %indvars.iv = phi i64 [ 0, %for.body9 ], [ %indvars.iv.next, %for.body16 ], !UID !28
  val phiindvars_iv16 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 3, ID = 16, Res = true))

  //  %6 = add nuw nsw i64 %indvars.iv, %indvars.iv84, !UID !29
  val binaryOp_17 = Module(new ComputeNode(NumOuts = 1, ID = 17, opCode = "add")(sign = false, Debug = true, GuardVals=Seq.tabulate(1000)(n => n)))

  //  %7 = add nuw nsw i64 %6, %2, !UID !30
  val binaryOp_18 = Module(new ComputeNode(NumOuts = 1, ID = 18, opCode = "add")(sign = false, Debug = true, GuardVals=Seq.tabulate(1000)(n => n)))

  //  %arrayidx20 = getelementptr inbounds double, double* %m2, i64 %7, !UID !31
  val Gep_arrayidx2019 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 19)(ElementSize = 8, ArraySize = List()))

  //  %8 = load double, double* %arrayidx20, align 8, !tbaa !21, !UID !32
  val ld_20 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 20, RouteID = 1))

  //  %mul21 = fmul double %5, %8, !UID !33
  val FP_mul2121 = Module(new FPComputeNode(NumOuts = 1, ID = 21, opCode = "fmul")(fType))

  //  %9 = add nuw nsw i64 %indvars.iv, %indvars.iv84, !UID !34
  val binaryOp_22 = Module(new ComputeNode(NumOuts = 1, ID = 22, opCode = "add")(sign = false, Debug = false))

  //  %10 = add nuw nsw i64 %9, %0, !UID !35
  val binaryOp_23 = Module(new ComputeNode(NumOuts = 1, ID = 23, opCode = "add")(sign = false, Debug = false))

  //  %arrayidx25 = getelementptr inbounds double, double* %prod, i64 %10, !UID !36
  val Gep_arrayidx2524 = Module(new GepNode(NumIns = 1, NumOuts = 2, ID = 24)(ElementSize = 8, ArraySize = List()))

  //  %11 = load double, double* %arrayidx25, align 8, !tbaa !21, !UID !37
  val ld_25 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 25, RouteID = 2))

  //  %add26 = fadd double %11, %mul21, !UID !38
  val FP_add2626 = Module(new FPComputeNode(NumOuts = 1, ID = 26, opCode = "fadd")(fType))

  //  store double %add26, double* %arrayidx25, align 8, !tbaa !21, !UID !39
  val st_27 = Module(new UnTypStoreCache(NumPredOps = 0, NumSuccOps = 1, ID = 27, RouteID = 3))

  //  %indvars.iv.next = add nuw nsw i64 %indvars.iv, 1, !UID !40
  val binaryOp_indvars_iv_next28 = Module(new ComputeNode(NumOuts = 2, ID = 28, opCode = "add")(sign = false, Debug = false))

  //  %exitcond = icmp eq i64 %indvars.iv.next, 2, !UID !41
  val icmp_exitcond29 = Module(new ComputeNode(NumOuts = 1, ID = 29, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond, label %for.inc27, label %for.body16, !UID !42, !BB_UID !43
  val br_30 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 1, ID = 30))

  //  %indvars.iv.next72 = add nuw nsw i64 %indvars.iv71, 1, !UID !44
  val binaryOp_indvars_iv_next7231 = Module(new ComputeNode(NumOuts = 2, ID = 31, opCode = "add")(sign = false, Debug = false))

  //  %exitcond77 = icmp eq i64 %indvars.iv.next72, 2, !UID !45
  val icmp_exitcond7732 = Module(new ComputeNode(NumOuts = 1, ID = 32, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond77, label %for.inc30, label %for.body9, !UID !46, !BB_UID !47
  val br_33 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 33))

  //  %indvars.iv.next79 = add nuw nsw i64 %indvars.iv78, 1, !UID !48
  val binaryOp_indvars_iv_next7934 = Module(new ComputeNode(NumOuts = 2, ID = 34, opCode = "add")(sign = false, Debug = false))

  //  %exitcond81 = icmp eq i64 %indvars.iv.next79, 16, !UID !49
  val icmp_exitcond8135 = Module(new ComputeNode(NumOuts = 1, ID = 35, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond81, label %for.inc33, label %loopk, !UID !50, !BB_UID !51
  val br_36 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 36))

  //  %indvars.iv.next83 = add nuw nsw i64 %indvars.iv82, 2, !UID !52
  val binaryOp_indvars_iv_next8337 = Module(new ComputeNode(NumOuts = 2, ID = 37, opCode = "add")(sign = false, Debug = false))

  //  %cmp2 = icmp ult i64 %indvars.iv.next83, 16, !UID !53
  val icmp_cmp238 = Module(new ComputeNode(NumOuts = 1, ID = 38, opCode = "slt")(sign = false, Debug = false))

  //  br i1 %cmp2, label %loopi, label %for.inc36, !UID !54, !BB_UID !55
  val br_39 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 39))

  //  %indvars.iv.next85 = add nuw nsw i64 %indvars.iv84, 2, !UID !56
  val binaryOp_indvars_iv_next8540 = Module(new ComputeNode(NumOuts = 2, ID = 40, opCode = "add")(sign = false, Debug = false))

  //  %cmp = icmp ult i64 %indvars.iv.next85, 16, !UID !57
  val icmp_cmp41 = Module(new ComputeNode(NumOuts = 1, ID = 41, opCode = "slt")(sign = false, Debug = false))

  //  br i1 %cmp, label %loopkk, label %for.end38, !UID !58, !BB_UID !59
  val br_42 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 42))

  //  ret void, !UID !60, !BB_UID !61
  val ret_43 = Module(new RetNode2(retTypes = List(), ID = 43))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i64 0
  val const0 = Module(new ConstFastNode(value = 0, ID = 0))

  //i64 0
  val const1 = Module(new ConstFastNode(value = 0, ID = 1))

  //i64 0
  val const2 = Module(new ConstFastNode(value = 0, ID = 2))

  //i64 4
  val const3 = Module(new ConstFastNode(value = 4, ID = 3))

  //i64 0
  val const4 = Module(new ConstFastNode(value = 0, ID = 4))

  //i64 4
  val const5 = Module(new ConstFastNode(value = 4, ID = 5))

  //i64 0
  val const6 = Module(new ConstFastNode(value = 0, ID = 6))

  //i64 1
  val const7 = Module(new ConstFastNode(value = 1, ID = 7))

  //i64 2
  val const8 = Module(new ConstFastNode(value = 2, ID = 8))

  //i64 1
  val const9 = Module(new ConstFastNode(value = 1, ID = 9))

  //i64 2
  val const10 = Module(new ConstFastNode(value = 2, ID = 10))

  //i64 1
  val const11 = Module(new ConstFastNode(value = 1, ID = 11))

  //i64 16
  val const12 = Module(new ConstFastNode(value = 16, ID = 12))

  //i64 2
  val const13 = Module(new ConstFastNode(value = 2, ID = 13))

  //i64 16
  val const14 = Module(new ConstFastNode(value = 16, ID = 14))

  //i64 2
  val const15 = Module(new ConstFastNode(value = 2, ID = 15))

  //i64 16
  val const16 = Module(new ConstFastNode(value = 16, ID = 16))



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_entry0.io.predicateIn(0) <> ArgSplitter.io.Out.enable



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE LOOP                     *
   * ================================================================== */

  bb_loopkk1.io.predicateIn(0) <> Loop_4.io.activate_loop_start

  bb_loopkk1.io.predicateIn(1) <> Loop_4.io.activate_loop_back

  bb_loopi2.io.predicateIn(0) <> Loop_3.io.activate_loop_start

  bb_loopi2.io.predicateIn(1) <> Loop_3.io.activate_loop_back

  bb_loopk3.io.predicateIn(1) <> Loop_2.io.activate_loop_start

  bb_loopk3.io.predicateIn(0) <> Loop_2.io.activate_loop_back

  bb_for_body94.io.predicateIn(1) <> Loop_1.io.activate_loop_start

  bb_for_body94.io.predicateIn(0) <> Loop_1.io.activate_loop_back

  bb_for_body165.io.predicateIn(1) <> Loop_0.io.activate_loop_start

  bb_for_body165.io.predicateIn(0) <> Loop_0.io.activate_loop_back

  bb_for_inc276.io.predicateIn(0) <> Loop_0.io.loopExit(0)

  bb_for_inc307.io.predicateIn(0) <> Loop_1.io.loopExit(0)

  bb_for_inc338.io.predicateIn(0) <> Loop_2.io.loopExit(0)

  bb_for_inc369.io.predicateIn(0) <> Loop_3.io.loopExit(0)

  bb_for_end3810.io.predicateIn(0) <> Loop_4.io.loopExit(0)



  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */

  Loop_0.io.enable <> br_15.io.Out(0)

  Loop_0.io.loopBack(0) <> br_30.io.FalseOutput(0)

  Loop_0.io.loopFinish(0) <> br_30.io.TrueOutput(0)

  Loop_1.io.enable <> br_7.io.Out(0)

  Loop_1.io.loopBack(0) <> br_33.io.FalseOutput(0)

  Loop_1.io.loopFinish(0) <> br_33.io.TrueOutput(0)

  Loop_2.io.enable <> br_4.io.Out(0)

  Loop_2.io.loopBack(0) <> br_36.io.FalseOutput(0)

  Loop_2.io.loopFinish(0) <> br_36.io.TrueOutput(0)

  Loop_3.io.enable <> br_2.io.Out(0)

  Loop_3.io.loopBack(0) <> br_39.io.TrueOutput(0)

  Loop_3.io.loopFinish(0) <> br_39.io.FalseOutput(0)

  Loop_4.io.enable <> br_0.io.Out(0)

  Loop_4.io.loopBack(0) <> br_42.io.TrueOutput(0)

  Loop_4.io.loopFinish(0) <> br_42.io.FalseOutput(0)



  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

  Loop_0.io.InLiveIn(0) <> binaryOp_10.io.Out(0)

  Loop_0.io.InLiveIn(1) <> ld_14.io.Out(0)

  Loop_0.io.InLiveIn(2) <> Loop_1.io.OutLiveIn.elements("field1")(0)

  Loop_0.io.InLiveIn(3) <> Loop_1.io.OutLiveIn.elements("field0")(0)

  Loop_0.io.InLiveIn(4) <> Loop_1.io.OutLiveIn.elements("field2")(0)

  Loop_0.io.InLiveIn(5) <> Loop_1.io.OutLiveIn.elements("field3")(0)

  Loop_1.io.InLiveIn(0) <> binaryOp_6.io.Out(0)

  Loop_1.io.InLiveIn(1) <> Loop_2.io.OutLiveIn.elements("field1")(0)

  Loop_1.io.InLiveIn(2) <> Loop_2.io.OutLiveIn.elements("field3")(0)

  Loop_1.io.InLiveIn(3) <> Loop_2.io.OutLiveIn.elements("field4")(0)

  Loop_1.io.InLiveIn(4) <> Loop_2.io.OutLiveIn.elements("field0")(0)

  Loop_1.io.InLiveIn(5) <> Loop_2.io.OutLiveIn.elements("field2")(0)

  Loop_2.io.InLiveIn(0) <> phiindvars_iv823.io.Out(0)

  Loop_2.io.InLiveIn(1) <> Loop_3.io.OutLiveIn.elements("field1")(0)

  Loop_2.io.InLiveIn(2) <> Loop_3.io.OutLiveIn.elements("field2")(0)

  Loop_2.io.InLiveIn(3) <> Loop_3.io.OutLiveIn.elements("field3")(0)

  Loop_2.io.InLiveIn(4) <> Loop_3.io.OutLiveIn.elements("field0")(0)

  Loop_3.io.InLiveIn(0) <> phiindvars_iv841.io.Out(0)

  Loop_3.io.InLiveIn(1) <> Loop_4.io.OutLiveIn.elements("field1")(0)

  Loop_3.io.InLiveIn(2) <> Loop_4.io.OutLiveIn.elements("field0")(0)

  Loop_3.io.InLiveIn(3) <> Loop_4.io.OutLiveIn.elements("field2")(0)

  Loop_4.io.InLiveIn(0) <> ArgSplitter.io.Out.dataPtrs.elements("field0")(0)

  Loop_4.io.InLiveIn(1) <> ArgSplitter.io.Out.dataPtrs.elements("field1")(0)

  Loop_4.io.InLiveIn(2) <> ArgSplitter.io.Out.dataPtrs.elements("field2")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */

  binaryOp_18.io.RightIO <> Loop_0.io.OutLiveIn.elements("field0")(0)

  FP_mul2121.io.LeftIO <> Loop_0.io.OutLiveIn.elements("field1")(0)

  Gep_arrayidx2019.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field2")(0)

  binaryOp_23.io.RightIO <> Loop_0.io.OutLiveIn.elements("field3")(0)

  Gep_arrayidx2524.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field4")(0)

  binaryOp_17.io.RightIO <> Loop_0.io.OutLiveIn.elements("field5")(0)

  binaryOp_22.io.RightIO <> Loop_0.io.OutLiveIn.elements("field5")(1)

  binaryOp_12.io.RightIO <> Loop_1.io.OutLiveIn.elements("field0")(1)

  binaryOp_9.io.RightIO <> Loop_1.io.OutLiveIn.elements("field4")(0)

  binaryOp_11.io.RightIO <> Loop_1.io.OutLiveIn.elements("field4")(1)

  Gep_arrayidx13.io.baseAddress <> Loop_1.io.OutLiveIn.elements("field5")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP LIVE OUT DEPENDENCIES                       *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP CARRY DEPENDENCIES                          *
   * ================================================================== */

  Loop_0.io.CarryDepenIn(0) <> binaryOp_indvars_iv_next28.io.Out(0)

  Loop_1.io.CarryDepenIn(0) <> binaryOp_indvars_iv_next7231.io.Out(0)

  Loop_2.io.CarryDepenIn(0) <> binaryOp_indvars_iv_next7934.io.Out(0)

  Loop_3.io.CarryDepenIn(0) <> binaryOp_indvars_iv_next8337.io.Out(0)

  Loop_4.io.CarryDepenIn(0) <> binaryOp_indvars_iv_next8540.io.Out(0)



  /* ================================================================== *
   *                   LOOP DATA CARRY DEPENDENCIES                     *
   * ================================================================== */

  phiindvars_iv16.io.InData(1) <> Loop_0.io.CarryDepenOut.elements("field0")(0)

  phiindvars_iv718.io.InData(1) <> Loop_1.io.CarryDepenOut.elements("field0")(0)

  phiindvars_iv785.io.InData(1) <> Loop_2.io.CarryDepenOut.elements("field0")(0)

  phiindvars_iv823.io.InData(1) <> Loop_3.io.CarryDepenOut.elements("field0")(0)

  phiindvars_iv841.io.InData(1) <> Loop_4.io.CarryDepenOut.elements("field0")(0)



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  br_0.io.enable <> bb_entry0.io.Out(0)


  const0.io.enable <> bb_loopkk1.io.Out(0)

  phiindvars_iv841.io.enable <> bb_loopkk1.io.Out(1)


  br_2.io.enable <> bb_loopkk1.io.Out(2)


  const1.io.enable <> bb_loopi2.io.Out(0)

  phiindvars_iv823.io.enable <> bb_loopi2.io.Out(1)


  br_4.io.enable <> bb_loopi2.io.Out(2)


  const2.io.enable <> bb_loopk3.io.Out(0)

  const3.io.enable <> bb_loopk3.io.Out(1)

  phiindvars_iv785.io.enable <> bb_loopk3.io.Out(2)


  binaryOp_6.io.enable <> bb_loopk3.io.Out(3)


  br_7.io.enable <> bb_loopk3.io.Out(4)


  const4.io.enable <> bb_for_body94.io.Out(0)

  const5.io.enable <> bb_for_body94.io.Out(1)

  phiindvars_iv718.io.enable <> bb_for_body94.io.Out(2)


  binaryOp_9.io.enable <> bb_for_body94.io.Out(3)


  binaryOp_10.io.enable <> bb_for_body94.io.Out(4)


  binaryOp_11.io.enable <> bb_for_body94.io.Out(5)


  binaryOp_12.io.enable <> bb_for_body94.io.Out(6)


  Gep_arrayidx13.io.enable <> bb_for_body94.io.Out(7)


  ld_14.io.enable <> bb_for_body94.io.Out(8)


  br_15.io.enable <> bb_for_body94.io.Out(9)


  const6.io.enable <> bb_for_body165.io.Out(0)

  const7.io.enable <> bb_for_body165.io.Out(1)

  const8.io.enable <> bb_for_body165.io.Out(2)

  phiindvars_iv16.io.enable <> bb_for_body165.io.Out(3)


  binaryOp_17.io.enable <> bb_for_body165.io.Out(4)


  binaryOp_18.io.enable <> bb_for_body165.io.Out(5)


  Gep_arrayidx2019.io.enable <> bb_for_body165.io.Out(6)


  ld_20.io.enable <> bb_for_body165.io.Out(7)


  FP_mul2121.io.enable <> bb_for_body165.io.Out(8)


  binaryOp_22.io.enable <> bb_for_body165.io.Out(9)


  binaryOp_23.io.enable <> bb_for_body165.io.Out(10)


  Gep_arrayidx2524.io.enable <> bb_for_body165.io.Out(11)


  ld_25.io.enable <> bb_for_body165.io.Out(12)


  FP_add2626.io.enable <> bb_for_body165.io.Out(13)


  st_27.io.enable <> bb_for_body165.io.Out(14)


  binaryOp_indvars_iv_next28.io.enable <> bb_for_body165.io.Out(15)


  icmp_exitcond29.io.enable <> bb_for_body165.io.Out(16)


  br_30.io.enable <> bb_for_body165.io.Out(17)


  const9.io.enable <> bb_for_inc276.io.Out(0)

  const10.io.enable <> bb_for_inc276.io.Out(1)

  binaryOp_indvars_iv_next7231.io.enable <> bb_for_inc276.io.Out(2)


  icmp_exitcond7732.io.enable <> bb_for_inc276.io.Out(3)


  br_33.io.enable <> bb_for_inc276.io.Out(4)


  const11.io.enable <> bb_for_inc307.io.Out(0)

  const12.io.enable <> bb_for_inc307.io.Out(1)

  binaryOp_indvars_iv_next7934.io.enable <> bb_for_inc307.io.Out(2)


  icmp_exitcond8135.io.enable <> bb_for_inc307.io.Out(3)


  br_36.io.enable <> bb_for_inc307.io.Out(4)


  const13.io.enable <> bb_for_inc338.io.Out(0)

  const14.io.enable <> bb_for_inc338.io.Out(1)

  binaryOp_indvars_iv_next8337.io.enable <> bb_for_inc338.io.Out(2)


  icmp_cmp238.io.enable <> bb_for_inc338.io.Out(3)


  br_39.io.enable <> bb_for_inc338.io.Out(4)


  const15.io.enable <> bb_for_inc369.io.Out(0)

  const16.io.enable <> bb_for_inc369.io.Out(1)

  binaryOp_indvars_iv_next8540.io.enable <> bb_for_inc369.io.Out(2)


  icmp_cmp41.io.enable <> bb_for_inc369.io.Out(3)


  br_42.io.enable <> bb_for_inc369.io.Out(4)


  ret_43.io.In.enable <> bb_for_end3810.io.Out(0)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  phiindvars_iv841.io.Mask <> bb_loopkk1.io.MaskBB(0)

  phiindvars_iv823.io.Mask <> bb_loopi2.io.MaskBB(0)

  phiindvars_iv785.io.Mask <> bb_loopk3.io.MaskBB(0)

  phiindvars_iv718.io.Mask <> bb_for_body94.io.MaskBB(0)

  phiindvars_iv16.io.Mask <> bb_for_body165.io.MaskBB(0)



  /* ================================================================== *
   *                   PRINT ALLOCA OFFSET                              *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */

  MemCtrl.io.rd.mem(0).MemReq <> ld_14.io.MemReq

  ld_14.io.MemResp <> MemCtrl.io.rd.mem(0).MemResp

  MemCtrl.io.rd.mem(1).MemReq <> ld_20.io.MemReq

  ld_20.io.MemResp <> MemCtrl.io.rd.mem(1).MemResp

  MemCtrl.io.rd.mem(2).MemReq <> ld_25.io.MemReq

  ld_25.io.MemResp <> MemCtrl.io.rd.mem(2).MemResp

  MemCtrl.io.wr.mem(0).MemReq <> st_27.io.MemReq

  st_27.io.MemResp <> MemCtrl.io.wr.mem(0).MemResp



  /* ================================================================== *
   *                   PRINT SHARED CONNECTIONS                         *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  phiindvars_iv841.io.InData(0) <> const0.io.Out

  phiindvars_iv823.io.InData(0) <> const1.io.Out

  phiindvars_iv785.io.InData(0) <> const2.io.Out

  binaryOp_6.io.RightIO <> const3.io.Out

  phiindvars_iv718.io.InData(0) <> const4.io.Out

  binaryOp_10.io.RightIO <> const5.io.Out

  phiindvars_iv16.io.InData(0) <> const6.io.Out

  binaryOp_indvars_iv_next28.io.RightIO <> const7.io.Out

  icmp_exitcond29.io.RightIO <> const8.io.Out

  binaryOp_indvars_iv_next7231.io.RightIO <> const9.io.Out

  icmp_exitcond7732.io.RightIO <> const10.io.Out

  binaryOp_indvars_iv_next7934.io.RightIO <> const11.io.Out

  icmp_exitcond8135.io.RightIO <> const12.io.Out

  binaryOp_indvars_iv_next8337.io.RightIO <> const13.io.Out

  icmp_cmp238.io.RightIO <> const14.io.Out

  binaryOp_indvars_iv_next8540.io.RightIO <> const15.io.Out

  icmp_cmp41.io.RightIO <> const16.io.Out

  binaryOp_indvars_iv_next8540.io.LeftIO <> phiindvars_iv841.io.Out(1)

  binaryOp_indvars_iv_next8337.io.LeftIO <> phiindvars_iv823.io.Out(1)

  binaryOp_6.io.LeftIO <> phiindvars_iv785.io.Out(0)

  binaryOp_indvars_iv_next7934.io.LeftIO <> phiindvars_iv785.io.Out(1)

  binaryOp_9.io.LeftIO <> phiindvars_iv718.io.Out(0)

  binaryOp_11.io.LeftIO <> phiindvars_iv718.io.Out(1)

  binaryOp_indvars_iv_next7231.io.LeftIO <> phiindvars_iv718.io.Out(2)

  binaryOp_10.io.LeftIO <> binaryOp_9.io.Out(0)

  binaryOp_12.io.LeftIO <> binaryOp_11.io.Out(0)

  Gep_arrayidx13.io.idx(0) <> binaryOp_12.io.Out(0)

  ld_14.io.GepAddr <> Gep_arrayidx13.io.Out(0)

  binaryOp_17.io.LeftIO <> phiindvars_iv16.io.Out(0)

  binaryOp_22.io.LeftIO <> phiindvars_iv16.io.Out(1)

  binaryOp_indvars_iv_next28.io.LeftIO <> phiindvars_iv16.io.Out(2)

  binaryOp_18.io.LeftIO <> binaryOp_17.io.Out(0)

  Gep_arrayidx2019.io.idx(0) <> binaryOp_18.io.Out(0)

  ld_20.io.GepAddr <> Gep_arrayidx2019.io.Out(0)

  FP_mul2121.io.RightIO <> ld_20.io.Out(0)

  FP_add2626.io.RightIO <> FP_mul2121.io.Out(0)

  binaryOp_23.io.LeftIO <> binaryOp_22.io.Out(0)

  Gep_arrayidx2524.io.idx(0) <> binaryOp_23.io.Out(0)

  ld_25.io.GepAddr <> Gep_arrayidx2524.io.Out(0)

  st_27.io.GepAddr <> Gep_arrayidx2524.io.Out(1)

  FP_add2626.io.LeftIO <> ld_25.io.Out(0)

  st_27.io.inData <> FP_add2626.io.Out(0)

  icmp_exitcond29.io.LeftIO <> binaryOp_indvars_iv_next28.io.Out(1)

  br_30.io.CmpIO <> icmp_exitcond29.io.Out(0)

  icmp_exitcond7732.io.LeftIO <> binaryOp_indvars_iv_next7231.io.Out(1)

  br_33.io.CmpIO <> icmp_exitcond7732.io.Out(0)

  icmp_exitcond8135.io.LeftIO <> binaryOp_indvars_iv_next7934.io.Out(1)

  br_36.io.CmpIO <> icmp_exitcond8135.io.Out(0)

  icmp_cmp238.io.LeftIO <> binaryOp_indvars_iv_next8337.io.Out(1)

  br_39.io.CmpIO <> icmp_cmp238.io.Out(0)

  icmp_cmp41.io.LeftIO <> binaryOp_indvars_iv_next8540.io.Out(1)

  br_42.io.CmpIO <> icmp_cmp41.io.Out(0)

  st_27.io.Out(0).ready := true.B



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  br_30.io.PredOp(0) <> st_27.io.SuccOp(0)



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_43.io.Out

}

