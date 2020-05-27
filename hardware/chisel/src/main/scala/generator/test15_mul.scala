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


class test15_mulDF(PtrsIn: Seq[Int] = List(32, 32, 32), ValsIn: Seq[Int] = List(), Returns: Seq[Int] = List())
			(implicit p: Parameters) extends DandelionAccelDCRModule(PtrsIn, ValsIn, Returns){


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  val MemCtrl = Module(new CacheMemoryEngine(ID = 0, NumRead = 2, NumWrite = 1))

  io.MemReq <> MemCtrl.io.cache.MemReq
  MemCtrl.io.cache.MemResp <> io.MemResp
  val ArgSplitter = Module(new SplitCallDCR(ptrsArgTypes = List(1, 1, 1), valsArgTypes = List()))
  ArgSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val Loop_0 = Module(new LoopBlockNode(NumIns = List(1, 1, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 0))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_entry0 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 0))

  val bb_for_cond_cleanup1 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 1))

  val bb_for_body2 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 14, NumPhi = 1, BID = 2))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  br label %for.body, !dbg !24, !UID !25, !BB_UID !26
  val br_0 = Module(new UBranchNode(ID = 0))

  //  ret void, !dbg !27, !UID !28, !BB_UID !29
  val ret_1 = Module(new RetNode2(retTypes = List(), ID = 1))

  //  %indvars.iv = phi i64 [ 0, %entry ], [ %indvars.iv.next, %for.body ], !UID !30
  val phiindvars_iv2 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 4, ID = 2, Res = true))

  //  %arrayidx = getelementptr inbounds i32, i32* %a, i64 %indvars.iv, !dbg !31, !UID !34
  val Gep_arrayidx3 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 3)(ElementSize = 8, ArraySize = List()))

  //  %0 = load i32, i32* %arrayidx, align 4, !dbg !31, !tbaa !35, !UID !39
  val ld_4 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 4, RouteID = 0))

  //  %arrayidx2 = getelementptr inbounds i32, i32* %b, i64 %indvars.iv, !dbg !40, !UID !41
  val Gep_arrayidx25 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 5)(ElementSize = 8, ArraySize = List()))

  //  %1 = load i32, i32* %arrayidx2, align 4, !dbg !40, !tbaa !35, !UID !42
  val ld_6 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 6, RouteID = 1))

  //  %mul = mul i32 %1, %0, !dbg !43, !UID !44
  val binaryOp_mul7 = Module(new ComputeNode(NumOuts = 1, ID = 7, opCode = "mul")(sign = false, Debug = false))

  //  %arrayidx4 = getelementptr inbounds i32, i32* %c, i64 %indvars.iv, !dbg !45, !UID !46
  val Gep_arrayidx48 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 8)(ElementSize = 8, ArraySize = List()))

  //  store i32 %mul, i32* %arrayidx4, align 4, !dbg !47, !tbaa !35, !UID !48
  val st_9 = Module(new UnTypStoreCache(NumPredOps = 0, NumSuccOps = 1, ID = 9, RouteID = 2))

  //  %indvars.iv.next = add nuw nsw i64 %indvars.iv, 1, !dbg !49, !UID !50
  val binaryOp_indvars_iv_next10 = Module(new ComputeNode(NumOuts = 2, ID = 10, opCode = "add")(sign = false, Debug = false))

  //  %exitcond = icmp eq i64 %indvars.iv.next, 24, !dbg !51, !UID !52
  val icmp_exitcond11 = Module(new ComputeNode(NumOuts = 1, ID = 11, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond, label %for.cond.cleanup, label %for.body, !dbg !24, !llvm.loop !53, !UID !55, !BB_UID !56
  val br_12 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 1, ID = 12))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i64 0
  val const0 = Module(new ConstFastNode(value = 0, ID = 0))

  //i64 1
  val const1 = Module(new ConstFastNode(value = 1, ID = 1))

  //i64 24
  val const2 = Module(new ConstFastNode(value = 5, ID = 2))



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_entry0.io.predicateIn(0) <> ArgSplitter.io.Out.enable



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE LOOP                     *
   * ================================================================== */

  bb_for_cond_cleanup1.io.predicateIn(0) <> Loop_0.io.loopExit(0)

  bb_for_body2.io.predicateIn(1) <> Loop_0.io.activate_loop_start

  bb_for_body2.io.predicateIn(0) <> Loop_0.io.activate_loop_back



  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */

  Loop_0.io.enable <> br_0.io.Out(0)

  Loop_0.io.loopBack(0) <> br_12.io.FalseOutput(0)

  Loop_0.io.loopFinish(0) <> br_12.io.TrueOutput(0)



  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

  Loop_0.io.InLiveIn(0) <> ArgSplitter.io.Out.dataPtrs.elements("field0")(0)

  Loop_0.io.InLiveIn(1) <> ArgSplitter.io.Out.dataPtrs.elements("field1")(0)

  Loop_0.io.InLiveIn(2) <> ArgSplitter.io.Out.dataPtrs.elements("field2")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */

  Gep_arrayidx3.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field0")(0)

  Gep_arrayidx25.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field1")(0)

  Gep_arrayidx48.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field2")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP LIVE OUT DEPENDENCIES                       *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP CARRY DEPENDENCIES                          *
   * ================================================================== */

  Loop_0.io.CarryDepenIn(0) <> binaryOp_indvars_iv_next10.io.Out(0)



  /* ================================================================== *
   *                   LOOP DATA CARRY DEPENDENCIES                     *
   * ================================================================== */

  phiindvars_iv2.io.InData(1) <> Loop_0.io.CarryDepenOut.elements("field0")(0)



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  br_0.io.enable <> bb_entry0.io.Out(0)


  ret_1.io.In.enable <> bb_for_cond_cleanup1.io.Out(0)


  const0.io.enable <> bb_for_body2.io.Out(0)

  const1.io.enable <> bb_for_body2.io.Out(1)

  const2.io.enable <> bb_for_body2.io.Out(2)

  phiindvars_iv2.io.enable <> bb_for_body2.io.Out(3)


  Gep_arrayidx3.io.enable <> bb_for_body2.io.Out(4)


  ld_4.io.enable <> bb_for_body2.io.Out(5)


  Gep_arrayidx25.io.enable <> bb_for_body2.io.Out(6)


  ld_6.io.enable <> bb_for_body2.io.Out(7)


  binaryOp_mul7.io.enable <> bb_for_body2.io.Out(8)


  Gep_arrayidx48.io.enable <> bb_for_body2.io.Out(9)


  st_9.io.enable <> bb_for_body2.io.Out(10)


  binaryOp_indvars_iv_next10.io.enable <> bb_for_body2.io.Out(11)


  icmp_exitcond11.io.enable <> bb_for_body2.io.Out(12)


  br_12.io.enable <> bb_for_body2.io.Out(13)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  phiindvars_iv2.io.Mask <> bb_for_body2.io.MaskBB(0)



  /* ================================================================== *
   *                   PRINT ALLOCA OFFSET                              *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */

  MemCtrl.io.rd.mem(0).MemReq <> ld_4.io.MemReq

  ld_4.io.MemResp <> MemCtrl.io.rd.mem(0).MemResp

  MemCtrl.io.rd.mem(1).MemReq <> ld_6.io.MemReq

  ld_6.io.MemResp <> MemCtrl.io.rd.mem(1).MemResp

  MemCtrl.io.wr.mem(0).MemReq <> st_9.io.MemReq

  st_9.io.MemResp <> MemCtrl.io.wr.mem(0).MemResp



  /* ================================================================== *
   *                   PRINT SHARED CONNECTIONS                         *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  phiindvars_iv2.io.InData(0) <> const0.io.Out

  binaryOp_indvars_iv_next10.io.RightIO <> const1.io.Out

  icmp_exitcond11.io.RightIO <> const2.io.Out

  Gep_arrayidx3.io.idx(0) <> phiindvars_iv2.io.Out(0)

  Gep_arrayidx25.io.idx(0) <> phiindvars_iv2.io.Out(1)

  Gep_arrayidx48.io.idx(0) <> phiindvars_iv2.io.Out(2)

  binaryOp_indvars_iv_next10.io.LeftIO <> phiindvars_iv2.io.Out(3)

  ld_4.io.GepAddr <> Gep_arrayidx3.io.Out(0)

  binaryOp_mul7.io.RightIO <> ld_4.io.Out(0)

  ld_6.io.GepAddr <> Gep_arrayidx25.io.Out(0)

  binaryOp_mul7.io.LeftIO <> ld_6.io.Out(0)

  st_9.io.inData <> binaryOp_mul7.io.Out(0)

  st_9.io.GepAddr <> Gep_arrayidx48.io.Out(0)

  icmp_exitcond11.io.LeftIO <> binaryOp_indvars_iv_next10.io.Out(1)

  br_12.io.CmpIO <> icmp_exitcond11.io.Out(0)

  st_9.io.Out(0).ready := true.B



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  br_12.io.PredOp(0) <> st_9.io.SuccOp(0)



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_1.io.Out

}

