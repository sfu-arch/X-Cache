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


class vaddDF(PtrsIn: Seq[Int] = List(64, 64, 64), ValsIn: Seq[Int] = List(), Returns: Seq[Int] = List())
			(implicit p: Parameters) extends DandelionAccelDCRModule(PtrsIn, ValsIn, Returns){


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  //Cache
  val mem_ctrl_cache = Module(new CacheMemoryEngine(ID = 0, NumRead = 2, NumWrite = 1))

  io.MemReq <> mem_ctrl_cache.io.cache.MemReq
  mem_ctrl_cache.io.cache.MemResp <> io.MemResp

  val ArgSplitter = Module(new SplitCallDCR(ptrsArgTypes = List(1, 1, 1), valsArgTypes = List()))
  ArgSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val Loop_0 = Module(new LoopBlockNode(NumIns = List(1, 1, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 0))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_entry1 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 1))

  val bb_for_cond_cleanup3 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 3))

  val bb_for_body5 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 14, NumPhi = 1, BID = 5))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  br label %for.body, !dbg !23, !UID !24, !BB_UID !25
  val br_2 = Module(new UBranchNode(ID = 2))

  //  ret void, !dbg !26, !UID !27, !BB_UID !28
  val ret_4 = Module(new RetNode2(retTypes = List(), ID = 4))

  //  %indvars.iv = phi i64 [ 0, %entry ], [ %indvars.iv.next, %for.body ], !UID !29
  val phiindvars_iv6 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 4, ID = 6, Res = true))

  //  %arrayidx = getelementptr inbounds i32, i32* %a, i64 %indvars.iv, !dbg !30, !UID !33
  val Gep_arrayidx7 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 7)(ElementSize = 8, ArraySize = List()))

  //  %0 = load i32, i32* %arrayidx, align 4, !dbg !30, !tbaa !34, !UID !38
  val ld_8 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 8, RouteID = 0))

  //  %arrayidx2 = getelementptr inbounds i32, i32* %b, i64 %indvars.iv, !dbg !39, !UID !40
  val Gep_arrayidx29 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 9)(ElementSize = 8, ArraySize = List()))

  //  %1 = load i32, i32* %arrayidx2, align 4, !dbg !39, !tbaa !34, !UID !41
  val ld_10 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 10, RouteID = 1))

  //  %add = add i32 %1, %0, !dbg !42, !UID !43
  val binaryOp_add11 = Module(new ComputeNode(NumOuts = 1, ID = 11, opCode = "add")(sign = false, Debug = false))

  //  %arrayidx4 = getelementptr inbounds i32, i32* %c, i64 %indvars.iv, !dbg !44, !UID !45
  val Gep_arrayidx412 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 12)(ElementSize = 8, ArraySize = List()))

  //  store i32 %add, i32* %arrayidx4, align 4, !dbg !46, !tbaa !34, !UID !47
  val st_13 = Module(new UnTypStoreCache(NumPredOps = 0, NumSuccOps = 1, ID = 13, RouteID = 2))

  //  %indvars.iv.next = add nuw nsw i64 %indvars.iv, 1, !dbg !48, !UID !49
  val binaryOp_indvars_iv_next14 = Module(new ComputeNode(NumOuts = 2, ID = 14, opCode = "add")(sign = false, Debug = false))

  //  %exitcond = icmp eq i64 %indvars.iv.next, 2048, !dbg !50, !UID !51
  val icmp_exitcond15 = Module(new ComputeNode(NumOuts = 1, ID = 15, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond, label %for.cond.cleanup, label %for.body, !dbg !23, !llvm.loop !52, !UID !54, !BB_UID !55
  val br_16 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 1, ID = 16))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i64 0
  val const0 = Module(new ConstFastNode(value = 0, ID = 0))

  //i64 1
  val const1 = Module(new ConstFastNode(value = 1, ID = 1))

  //i64 2048
  val const2 = Module(new ConstFastNode(value = 2048, ID = 2))



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_entry1.io.predicateIn(0) <> ArgSplitter.io.Out.enable



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE LOOP                     *
   * ================================================================== */

  bb_for_cond_cleanup3.io.predicateIn(0) <> Loop_0.io.loopExit(0)

  bb_for_body5.io.predicateIn(1) <> Loop_0.io.activate_loop_start

  bb_for_body5.io.predicateIn(0) <> Loop_0.io.activate_loop_back



  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */

  Loop_0.io.enable <> br_2.io.Out(0)

  Loop_0.io.loopBack(0) <> br_16.io.FalseOutput(0)

  Loop_0.io.loopFinish(0) <> br_16.io.TrueOutput(0)



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

  Gep_arrayidx7.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field0")(0)

  Gep_arrayidx29.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field1")(0)

  Gep_arrayidx412.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field2")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP LIVE OUT DEPENDENCIES                       *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP CARRY DEPENDENCIES                          *
   * ================================================================== */

  Loop_0.io.CarryDepenIn(0) <> binaryOp_indvars_iv_next14.io.Out(0)



  /* ================================================================== *
   *                   LOOP DATA CARRY DEPENDENCIES                     *
   * ================================================================== */

  phiindvars_iv6.io.InData(1) <> Loop_0.io.CarryDepenOut.elements("field0")(0)



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  br_2.io.enable <> bb_entry1.io.Out(0)


  ret_4.io.In.enable <> bb_for_cond_cleanup3.io.Out(0)


  const0.io.enable <> bb_for_body5.io.Out(0)

  const1.io.enable <> bb_for_body5.io.Out(1)

  const2.io.enable <> bb_for_body5.io.Out(2)

  phiindvars_iv6.io.enable <> bb_for_body5.io.Out(3)


  Gep_arrayidx7.io.enable <> bb_for_body5.io.Out(4)


  ld_8.io.enable <> bb_for_body5.io.Out(5)


  Gep_arrayidx29.io.enable <> bb_for_body5.io.Out(6)


  ld_10.io.enable <> bb_for_body5.io.Out(7)


  binaryOp_add11.io.enable <> bb_for_body5.io.Out(8)


  Gep_arrayidx412.io.enable <> bb_for_body5.io.Out(9)


  st_13.io.enable <> bb_for_body5.io.Out(10)


  binaryOp_indvars_iv_next14.io.enable <> bb_for_body5.io.Out(11)


  icmp_exitcond15.io.enable <> bb_for_body5.io.Out(12)


  br_16.io.enable <> bb_for_body5.io.Out(13)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  phiindvars_iv6.io.Mask <> bb_for_body5.io.MaskBB(0)



  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */

  mem_ctrl_cache.io.rd.mem(0).MemReq <> ld_8.io.MemReq
  ld_8.io.MemResp <> mem_ctrl_cache.io.rd.mem(0).MemResp
  mem_ctrl_cache.io.rd.mem(1).MemReq <> ld_10.io.MemReq
  ld_10.io.MemResp <> mem_ctrl_cache.io.rd.mem(1).MemResp
  mem_ctrl_cache.io.wr.mem(0).MemReq <> st_13.io.MemReq
  st_13.io.MemResp <> mem_ctrl_cache.io.wr.mem(0).MemResp



  /* ================================================================== *
   *                   PRINT SHARED CONNECTIONS                         *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  phiindvars_iv6.io.InData(0) <> const0.io.Out

  binaryOp_indvars_iv_next14.io.RightIO <> const1.io.Out

  icmp_exitcond15.io.RightIO <> const2.io.Out

  Gep_arrayidx7.io.idx(0) <> phiindvars_iv6.io.Out(0)

  Gep_arrayidx29.io.idx(0) <> phiindvars_iv6.io.Out(1)

  Gep_arrayidx412.io.idx(0) <> phiindvars_iv6.io.Out(2)

  binaryOp_indvars_iv_next14.io.LeftIO <> phiindvars_iv6.io.Out(3)

  ld_8.io.GepAddr <> Gep_arrayidx7.io.Out(0)

  binaryOp_add11.io.RightIO <> ld_8.io.Out(0)

  ld_10.io.GepAddr <> Gep_arrayidx29.io.Out(0)

  binaryOp_add11.io.LeftIO <> ld_10.io.Out(0)

  st_13.io.inData <> binaryOp_add11.io.Out(0)

  st_13.io.GepAddr <> Gep_arrayidx412.io.Out(0)

  icmp_exitcond15.io.LeftIO <> binaryOp_indvars_iv_next14.io.Out(1)

  br_16.io.CmpIO <> icmp_exitcond15.io.Out(0)

  st_13.io.Out(0).ready := true.B



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  br_16.io.PredOp(0) <> st_13.io.SuccOp(0)



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_4.io.Out

}

