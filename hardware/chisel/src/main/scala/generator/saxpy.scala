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


class saxpyDF(PtrsIn: Seq[Int] = List(64, 64), ValsIn: Seq[Int] = List(64, 64), Returns: Seq[Int] = List(64))
			(implicit p: Parameters) extends DandelionAccelDCRModule(PtrsIn, ValsIn, Returns){


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  //Cache
  val mem_ctrl_cache = Module(new CacheMemoryEngine(ID = 0, NumRead = 2, NumWrite = 1))

  io.MemReq <> mem_ctrl_cache.io.cache.MemReq
  mem_ctrl_cache.io.cache.MemResp <> io.MemResp

  val ArgSplitter = Module(new SplitCallDCR(ptrsArgTypes = List(1, 1), valsArgTypes = List(2, 1)))
  ArgSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val Loop_0 = Module(new LoopBlockNode(NumIns = List(1, 1, 1, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 0))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_entry1 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 3, BID = 1))

  val bb_for_body_lr_ph4 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 2, BID = 4))

  val bb_for_cond_cleanup_loopexit7 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 7))

  val bb_for_cond_cleanup9 = Module(new BasicBlockNoMaskFastNode(NumInputs = 2, NumOuts = 2, BID = 9))

  val bb_for_body11 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 13, NumPhi = 1, BID = 11))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  %cmp11 = icmp sgt i32 %n, 0, !dbg !30, !UID !32
  val icmp_cmp110 = Module(new ComputeNode(NumOuts = 1, ID = 0, opCode = "sgt")(sign = true, Debug = false))

  //  br i1 %cmp11, label %for.body.lr.ph, label %for.cond.cleanup, !dbg !33, !UID !34, !BB_UID !35
  val br_3 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 3))

  //  %wide.trip.count = zext i32 %n to i64, !UID !36
  val sextwide_trip_count5 = Module(new ZextNode(NumOuts = 1))

  //  br label %for.body, !dbg !33, !UID !37, !BB_UID !38
  val br_6 = Module(new UBranchNode(ID = 6))

  //  br label %for.cond.cleanup, !dbg !39, !UID !40, !BB_UID !41
  val br_8 = Module(new UBranchNode(ID = 8))

  //  ret i32 1, !dbg !39, !UID !42, !BB_UID !43
  val ret_10 = Module(new RetNode2(retTypes = List(32), ID = 10))

  //  %indvars.iv = phi i64 [ 0, %for.body.lr.ph ], [ %indvars.iv.next, %for.body ], !UID !44
  val phiindvars_iv12 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 3, ID = 12, Res = true))

  //  %arrayidx = getelementptr inbounds i32, i32* %x, i64 %indvars.iv, !dbg !45, !UID !47
  val Gep_arrayidx13 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 13)(ElementSize = 8, ArraySize = List()))

  //  %0 = load i32, i32* %arrayidx, align 4, !dbg !45, !tbaa !48, !UID !52
  val ld_14 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 14, RouteID = 0))

  //  %mul = mul nsw i32 %0, %a, !dbg !53, !UID !54
  val binaryOp_mul15 = Module(new ComputeNode(NumOuts = 1, ID = 15, opCode = "mul")(sign = false, Debug = false))

  //  %arrayidx2 = getelementptr inbounds i32, i32* %y, i64 %indvars.iv, !dbg !55, !UID !56
  val Gep_arrayidx216 = Module(new GepNode(NumIns = 1, NumOuts = 2, ID = 16)(ElementSize = 8, ArraySize = List()))

  //  %1 = load i32, i32* %arrayidx2, align 4, !dbg !55, !tbaa !48, !UID !57
  val ld_17 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 17, RouteID = 1))

  //  %add = add nsw i32 %mul, %1, !dbg !58, !UID !59
  val binaryOp_add18 = Module(new ComputeNode(NumOuts = 1, ID = 18, opCode = "add")(sign = false, Debug = false))

  //  store i32 %add, i32* %arrayidx2, align 4, !dbg !60, !tbaa !48, !UID !61
  val st_19 = Module(new UnTypStoreCache(NumPredOps = 0, NumSuccOps = 1, ID = 19, RouteID = 2))

  //  %indvars.iv.next = add nuw nsw i64 %indvars.iv, 1, !dbg !62, !UID !63
  val binaryOp_indvars_iv_next20 = Module(new ComputeNode(NumOuts = 2, ID = 20, opCode = "add")(sign = false, Debug = false))

  //  %exitcond = icmp eq i64 %indvars.iv.next, %wide.trip.count, !dbg !30, !UID !64
  val icmp_exitcond15 = Module(new ComputeNode(NumOuts = 1, ID = 15, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond, label %for.cond.cleanup.loopexit, label %for.body, !dbg !33, !llvm.loop !65, !UID !67, !BB_UID !68
  val br_22 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 1, ID = 22))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i32 0
  val const0 = Module(new ConstFastNode(value = 0, ID = 0))

  //i32 1
  val const1 = Module(new ConstFastNode(value = 1, ID = 1))

  //i64 0
  val const2 = Module(new ConstFastNode(value = 0, ID = 2))

  //i64 1
  val const3 = Module(new ConstFastNode(value = 1, ID = 3))



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_entry1.io.predicateIn(0) <> ArgSplitter.io.Out.enable

  bb_for_body_lr_ph4.io.predicateIn(0) <> br_3.io.TrueOutput(0)

  bb_for_cond_cleanup9.io.predicateIn(1) <> br_3.io.FalseOutput(0)

  bb_for_cond_cleanup9.io.predicateIn(0) <> br_8.io.Out(0)



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE LOOP                     *
   * ================================================================== */

  bb_for_cond_cleanup_loopexit7.io.predicateIn(0) <> Loop_0.io.loopExit(0)

  bb_for_body11.io.predicateIn(1) <> Loop_0.io.activate_loop_start

  bb_for_body11.io.predicateIn(0) <> Loop_0.io.activate_loop_back



  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */

  Loop_0.io.enable <> br_6.io.Out(0)

  Loop_0.io.loopBack(0) <> br_22.io.FalseOutput(0)

  Loop_0.io.loopFinish(0) <> br_22.io.TrueOutput(0)



  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

  Loop_0.io.InLiveIn(0) <> ArgSplitter.io.Out.dataPtrs.elements("field0")(0)

  Loop_0.io.InLiveIn(1) <> ArgSplitter.io.Out.dataVals.elements("field1")(0)

  Loop_0.io.InLiveIn(2) <> ArgSplitter.io.Out.dataPtrs.elements("field1")(0)

  Loop_0.io.InLiveIn(3) <> sextwide_trip_count5.io.Out(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */

  Gep_arrayidx13.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field0")(0)

  binaryOp_mul15.io.RightIO <> Loop_0.io.OutLiveIn.elements("field1")(0)

  Gep_arrayidx216.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field2")(0)

  icmp_exitcond15.io.RightIO <> Loop_0.io.OutLiveIn.elements("field3")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP LIVE OUT DEPENDENCIES                       *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP CARRY DEPENDENCIES                          *
   * ================================================================== */

  Loop_0.io.CarryDepenIn(0) <> binaryOp_indvars_iv_next20.io.Out(0)



  /* ================================================================== *
   *                   LOOP DATA CARRY DEPENDENCIES                     *
   * ================================================================== */

  phiindvars_iv12.io.InData(1) <> Loop_0.io.CarryDepenOut.elements("field0")(0)



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  const0.io.enable <> bb_entry1.io.Out(0)

  icmp_cmp110.io.enable <> bb_entry1.io.Out(1)


  br_3.io.enable <> bb_entry1.io.Out(2)


  sextwide_trip_count5.io.enable <> bb_for_body_lr_ph4.io.Out(0)


  br_6.io.enable <> bb_for_body_lr_ph4.io.Out(1)


  br_8.io.enable <> bb_for_cond_cleanup_loopexit7.io.Out(0)


  const1.io.enable <> bb_for_cond_cleanup9.io.Out(0)

  ret_10.io.In.enable <> bb_for_cond_cleanup9.io.Out(1)


  const2.io.enable <> bb_for_body11.io.Out(0)

  const3.io.enable <> bb_for_body11.io.Out(1)

  phiindvars_iv12.io.enable <> bb_for_body11.io.Out(2)


  Gep_arrayidx13.io.enable <> bb_for_body11.io.Out(3)


  ld_14.io.enable <> bb_for_body11.io.Out(4)


  binaryOp_mul15.io.enable <> bb_for_body11.io.Out(5)


  Gep_arrayidx216.io.enable <> bb_for_body11.io.Out(6)


  ld_17.io.enable <> bb_for_body11.io.Out(7)


  binaryOp_add18.io.enable <> bb_for_body11.io.Out(8)


  st_19.io.enable <> bb_for_body11.io.Out(9)


  binaryOp_indvars_iv_next20.io.enable <> bb_for_body11.io.Out(10)


  icmp_exitcond15.io.enable <> bb_for_body11.io.Out(11)


  br_22.io.enable <> bb_for_body11.io.Out(12)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  phiindvars_iv12.io.Mask <> bb_for_body11.io.MaskBB(0)



  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */

  mem_ctrl_cache.io.rd.mem(0).MemReq <> ld_14.io.MemReq
  ld_14.io.MemResp <> mem_ctrl_cache.io.rd.mem(0).MemResp
  mem_ctrl_cache.io.rd.mem(1).MemReq <> ld_17.io.MemReq
  ld_17.io.MemResp <> mem_ctrl_cache.io.rd.mem(1).MemResp
  mem_ctrl_cache.io.wr.mem(0).MemReq <> st_19.io.MemReq
  st_19.io.MemResp <> mem_ctrl_cache.io.wr.mem(0).MemResp



  /* ================================================================== *
   *                   PRINT SHARED CONNECTIONS                         *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  icmp_cmp110.io.RightIO <> const0.io.Out

  ret_10.io.In.data("field0") <> const1.io.Out

  phiindvars_iv12.io.InData(0) <> const2.io.Out

  binaryOp_indvars_iv_next20.io.RightIO <> const3.io.Out

  br_3.io.CmpIO <> icmp_cmp110.io.Out(0)

  Gep_arrayidx13.io.idx(0) <> phiindvars_iv12.io.Out(0)

  Gep_arrayidx216.io.idx(0) <> phiindvars_iv12.io.Out(1)

  binaryOp_indvars_iv_next20.io.LeftIO <> phiindvars_iv12.io.Out(2)

  ld_14.io.GepAddr <> Gep_arrayidx13.io.Out(0)

  binaryOp_mul15.io.LeftIO <> ld_14.io.Out(0)

  binaryOp_add18.io.LeftIO <> binaryOp_mul15.io.Out(0)

  ld_17.io.GepAddr <> Gep_arrayidx216.io.Out(0)

  st_19.io.GepAddr <> Gep_arrayidx216.io.Out(1)

  binaryOp_add18.io.RightIO <> ld_17.io.Out(0)

  st_19.io.inData <> binaryOp_add18.io.Out(0)

  icmp_exitcond15.io.LeftIO <> binaryOp_indvars_iv_next20.io.Out(1)

  br_22.io.CmpIO <> icmp_exitcond15.io.Out(0)

  icmp_cmp110.io.LeftIO <> ArgSplitter.io.Out.dataVals.elements("field0")(0)

  sextwide_trip_count5.io.Input <> ArgSplitter.io.Out.dataVals.elements("field0")(1)

  st_19.io.Out(0).ready := true.B



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  br_22.io.PredOp(0) <> st_19.io.SuccOp(0)



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_10.io.Out

}

