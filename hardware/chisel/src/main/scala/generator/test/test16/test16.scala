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


class test16DF(PtrsIn: Seq[Int] = List(64, 64), ValsIn: Seq[Int] = List(64), Returns: Seq[Int] = List())
			(implicit p: Parameters) extends DandelionAccelDCRModule(PtrsIn, ValsIn, Returns){


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  val MemCtrl = Module(new CacheMemoryEngine(ID = 0, NumRead = 1, NumWrite = 1))

  io.MemReq <> MemCtrl.io.cache.MemReq
  MemCtrl.io.cache.MemResp <> io.MemResp
  val ArgSplitter = Module(new SplitCallDCR(ptrsArgTypes = List(1, 1), valsArgTypes = List(2)))
  ArgSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val Loop_0 = Module(new LoopBlockNode(NumIns = List(1, 1, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 0))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_entry0 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 3, BID = 0))

  val bb_for_body_lr_ph1 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 2, BID = 1))

  val bb_for_cond_cleanup_loopexit2 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 2))

  val bb_for_cond_cleanup3 = Module(new BasicBlockNoMaskFastNode(NumInputs = 2, NumOuts = 1, BID = 3))

  val bb_for_body4 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 12, NumPhi = 1, BID = 4))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  %cmp7 = icmp sgt i32 %n, 0, !dbg !25, !UID !27
  val icmp_cmp70 = Module(new ComputeNode(NumOuts = 1, ID = 0, opCode = "sgt")(sign = true, Debug = false))

  //  br i1 %cmp7, label %for.body.lr.ph, label %for.cond.cleanup, !dbg !28, !UID !29, !BB_UID !30
  val br_1 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 1))

  //  %wide.trip.count = zext i32 %n to i64, !UID !31
  val sextwide_trip_count2 = Module(new ZextNode(NumOuts = 1))

  //  br label %for.body, !dbg !28, !UID !32, !BB_UID !33
  val br_3 = Module(new UBranchNode(ID = 3))

  //  br label %for.cond.cleanup, !dbg !34
  val br_4 = Module(new UBranchNode(ID = 4))

  //  ret void, !dbg !34, !UID !35, !BB_UID !36
  val ret_5 = Module(new RetNode2(retTypes = List(), ID = 5))

  //  %indvars.iv = phi i64 [ 0, %for.body.lr.ph ], [ %indvars.iv.next, %for.body ], !UID !37
  val phiindvars_iv6 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 3, ID = 6, Res = true))

  //  %arrayidx = getelementptr inbounds float, float* %a, i64 %indvars.iv, !dbg !38, !UID !40
  val Gep_arrayidx7 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 7)(ElementSize = 8, ArraySize = List()))

  //  %0 = load float, float* %arrayidx, align 4, !dbg !38, !tbaa !41, !UID !45
  val ld_8 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 8, RouteID = 0))

  //  %div = fmul float %0, 5.000000e-01, !dbg !46, !UID !47
  val FP_div9 = Module(new FPComputeNode(NumOuts = 1, ID = 9, opCode = "fmul")(fType))

  //  %arrayidx2 = getelementptr inbounds float, float* %b, i64 %indvars.iv, !dbg !48, !UID !49
  val Gep_arrayidx210 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 10)(ElementSize = 8, ArraySize = List()))

  //  store float %div, float* %arrayidx2, align 4, !dbg !50, !tbaa !41, !UID !51
  val st_11 = Module(new UnTypStoreCache(NumPredOps = 0, NumSuccOps = 1, ID = 11, RouteID = 1))

  //  %indvars.iv.next = add nuw nsw i64 %indvars.iv, 1, !dbg !52, !UID !53
  val binaryOp_indvars_iv_next12 = Module(new ComputeNode(NumOuts = 2, ID = 12, opCode = "add")(sign = false, Debug = false))

  //  %exitcond = icmp eq i64 %indvars.iv.next, %wide.trip.count, !dbg !25, !UID !54
  val icmp_exitcond13 = Module(new ComputeNode(NumOuts = 1, ID = 13, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond, label %for.cond.cleanup.loopexit, label %for.body, !dbg !28, !llvm.loop !55, !UID !57, !BB_UID !58
  val br_14 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 1, ID = 14))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i32 0
  val const0 = Module(new ConstFastNode(value = 0, ID = 0))

  //i64 0
  val const1 = Module(new ConstFastNode(value = 0, ID = 1))

  //i64 1
  val const2 = Module(new ConstFastNode(value = 1, ID = 2))

  //float 5.000000e-01
  val constf0 = Module(new ConstFastNode(value = 0x3fe0000000000000L, ID = 0))



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_entry0.io.predicateIn(0) <> ArgSplitter.io.Out.enable

  bb_for_body_lr_ph1.io.predicateIn(0) <> br_1.io.TrueOutput(0)

  bb_for_cond_cleanup3.io.predicateIn(1) <> br_1.io.FalseOutput(0)

  bb_for_cond_cleanup3.io.predicateIn(0) <> br_4.io.Out(0)



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE LOOP                     *
   * ================================================================== */

  bb_for_cond_cleanup_loopexit2.io.predicateIn(0) <> Loop_0.io.loopExit(0)

  bb_for_body4.io.predicateIn(1) <> Loop_0.io.activate_loop_start

  bb_for_body4.io.predicateIn(0) <> Loop_0.io.activate_loop_back



  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */

  Loop_0.io.enable <> br_3.io.Out(0)

  Loop_0.io.loopBack(0) <> br_14.io.FalseOutput(0)

  Loop_0.io.loopFinish(0) <> br_14.io.TrueOutput(0)



  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

  Loop_0.io.InLiveIn(0) <> ArgSplitter.io.Out.dataPtrs.elements("field0")(0)

  Loop_0.io.InLiveIn(1) <> ArgSplitter.io.Out.dataPtrs.elements("field1")(0)

  Loop_0.io.InLiveIn(2) <> sextwide_trip_count2.io.Out(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */

  Gep_arrayidx7.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field0")(0)

  Gep_arrayidx210.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field1")(0)

  icmp_exitcond13.io.RightIO <> Loop_0.io.OutLiveIn.elements("field2")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP LIVE OUT DEPENDENCIES                       *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP CARRY DEPENDENCIES                          *
   * ================================================================== */

  Loop_0.io.CarryDepenIn(0) <> binaryOp_indvars_iv_next12.io.Out(0)



  /* ================================================================== *
   *                   LOOP DATA CARRY DEPENDENCIES                     *
   * ================================================================== */

  phiindvars_iv6.io.InData(1) <> Loop_0.io.CarryDepenOut.elements("field0")(0)



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  const0.io.enable <> bb_entry0.io.Out(0)

  icmp_cmp70.io.enable <> bb_entry0.io.Out(1)


  br_1.io.enable <> bb_entry0.io.Out(2)


  sextwide_trip_count2.io.enable <> bb_for_body_lr_ph1.io.Out(0)


  br_3.io.enable <> bb_for_body_lr_ph1.io.Out(1)


  br_4.io.enable <> bb_for_cond_cleanup_loopexit2.io.Out(0)


  ret_5.io.In.enable <> bb_for_cond_cleanup3.io.Out(0)


  constf0.io.enable <> bb_for_body4.io.Out(1)

  const1.io.enable <> bb_for_body4.io.Out(0)

  const2.io.enable <> bb_for_body4.io.Out(2)

  phiindvars_iv6.io.enable <> bb_for_body4.io.Out(3)


  Gep_arrayidx7.io.enable <> bb_for_body4.io.Out(4)


  ld_8.io.enable <> bb_for_body4.io.Out(5)


  FP_div9.io.enable <> bb_for_body4.io.Out(6)


  Gep_arrayidx210.io.enable <> bb_for_body4.io.Out(7)


  st_11.io.enable <> bb_for_body4.io.Out(8)


  binaryOp_indvars_iv_next12.io.enable <> bb_for_body4.io.Out(9)


  icmp_exitcond13.io.enable <> bb_for_body4.io.Out(10)


  br_14.io.enable <> bb_for_body4.io.Out(11)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  phiindvars_iv6.io.Mask <> bb_for_body4.io.MaskBB(0)



  /* ================================================================== *
   *                   PRINT ALLOCA OFFSET                              *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */

  MemCtrl.io.rd.mem(0).MemReq <> ld_8.io.MemReq

  ld_8.io.MemResp <> MemCtrl.io.rd.mem(0).MemResp

  MemCtrl.io.wr.mem(0).MemReq <> st_11.io.MemReq

  st_11.io.MemResp <> MemCtrl.io.wr.mem(0).MemResp



  /* ================================================================== *
   *                   PRINT SHARED CONNECTIONS                         *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  icmp_cmp70.io.RightIO <> const0.io.Out

  phiindvars_iv6.io.InData(0) <> const1.io.Out

  binaryOp_indvars_iv_next12.io.RightIO <> const2.io.Out

  FP_div9.io.RightIO <> constf0.io.Out

  br_1.io.CmpIO <> icmp_cmp70.io.Out(0)

  Gep_arrayidx7.io.idx(0) <> phiindvars_iv6.io.Out(0)

  Gep_arrayidx210.io.idx(0) <> phiindvars_iv6.io.Out(1)

  binaryOp_indvars_iv_next12.io.LeftIO <> phiindvars_iv6.io.Out(2)

  ld_8.io.GepAddr <> Gep_arrayidx7.io.Out(0)

  FP_div9.io.LeftIO <> ld_8.io.Out(0)

  st_11.io.inData <> FP_div9.io.Out(0)

  st_11.io.GepAddr <> Gep_arrayidx210.io.Out(0)

  icmp_exitcond13.io.LeftIO <> binaryOp_indvars_iv_next12.io.Out(1)

  br_14.io.CmpIO <> icmp_exitcond13.io.Out(0)

  icmp_cmp70.io.LeftIO <> ArgSplitter.io.Out.dataVals.elements("field0")(0)

  sextwide_trip_count2.io.Input <> ArgSplitter.io.Out.dataVals.elements("field0")(1)

  st_11.io.Out(0).ready := true.B



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  br_14.io.PredOp(0) <> st_11.io.SuccOp(0)



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_5.io.Out

}

