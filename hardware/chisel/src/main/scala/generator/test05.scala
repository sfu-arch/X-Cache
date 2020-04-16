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


class test05DF(PtrsIn: Seq[Int] = List(32), ValsIn: Seq[Int] = List(), Returns: Seq[Int] = List(32))
              (implicit p: Parameters) extends DandelionAccelDCRModule(PtrsIn, ValsIn, Returns) {


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  val MemCtrl = Module(new CacheMemoryEngine(ID = 0, NumRead = 2, NumWrite = 1))

  io.MemReq <> MemCtrl.io.cache.MemReq
  MemCtrl.io.cache.MemResp <> io.MemResp
  val ArgSplitter = Module(new SplitCallDCR(ptrsArgTypes = List(2), valsArgTypes = List()))
  ArgSplitter.io.In <> io.in


  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val Loop_0 = Module(new LoopBlockNode(NumIns = List(2), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 0))


  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_entry0 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 0))

  val bb_for_cond_cleanup1 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 4, BID = 1))

  val bb_for_body2 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 15, NumPhi = 1, BID = 2))


  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  br label %for.body, !dbg !19, !UID !20, !BB_UID !21
  val br_0 = Module(new UBranchNode(ID = 0))

  //  %arrayidx3 = getelementptr inbounds i32, i32* %a, i64 9, !dbg !22, !UID !23
  val Gep_arrayidx31 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 1)(ElementSize = 8, ArraySize = List()))

  //  %0 = load i32, i32* %arrayidx3, align 4, !dbg !22, !tbaa !24, !UID !28
  val ld_2 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 2, RouteID = 0))

  //  ret i32 %0, !dbg !29, !UID !30, !BB_UID !31
  val ret_3 = Module(new RetNode2(retTypes = List(32), ID = 3, Debug = true, NumBores = 4))

  //  %indvars.iv = phi i64 [ 0, %entry ], [ %indvars.iv.next, %for.body ], !UID !32
  val phiindvars_iv4 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 3, ID = 4, Res = true, Debug = true,
    GuardVals = List(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)))

  //  %arrayidx = getelementptr inbounds i32, i32* %a, i64 %indvars.iv, !dbg !33, !UID !36
  val Gep_arrayidx5 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 5)(ElementSize = 8, ArraySize = List()))

  //  %1 = load i32, i32* %arrayidx, align 4, !dbg !33, !tbaa !24, !UID !37
  val ld_6 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 6, RouteID = 1, Debug = true,
    GuardAddress = List(4096, 4104, 4112, 4120, 4128, 4136, 4144, 4152, 4160, 4168),
    GuardData = List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)))

  //  %mul = shl i32 %1, 1, !dbg !38, !UID !39
  val binaryOp_mul7 = Module(new ComputeNode(NumOuts = 1, ID = 7, opCode = "shl")(sign = false, Debug = false))

  //  %2 = add nuw nsw i64 %indvars.iv, 5, !dbg !40, !UID !41
  val binaryOp_8 = Module(new ComputeNode(NumOuts = 1, ID = 8, opCode = "add")(sign = false, Debug = true,
    GuardVals = List(5, 6, 7, 8, 9)))

  //  %arrayidx2 = getelementptr inbounds i32, i32* %a, i64 %2, !dbg !42, !UID !43
  val Gep_arrayidx29 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 9)(ElementSize = 8, ArraySize = List()))

  //  store i32 %mul, i32* %arrayidx2, align 4, !dbg !44, !tbaa !24, !UID !45
  val st_10 = Module(new UnTypStoreCache(NumPredOps = 0, NumSuccOps = 1, ID = 10, RouteID = 2, Debug = true,
    GuardAddress = List(4136, 4144, 4152, 4160, 4168)))

  //  %indvars.iv.next = add nuw nsw i64 %indvars.iv, 1, !dbg !46, !UID !47
  val binaryOp_indvars_iv_next11 = Module(new ComputeNode(NumOuts = 2, ID = 11, opCode = "add")(sign = false, Debug = false))

  //  %exitcond = icmp eq i64 %indvars.iv.next, 5, !dbg !48, !UID !49
  val icmp_exitcond12 = Module(new ComputeNode(NumOuts = 1, ID = 12, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond, label %for.cond.cleanup, label %for.body, !dbg !19, !llvm.loop !50, !UID !52, !BB_UID !53
  val br_13 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 1, ID = 13))


  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i64 9
  val const0 = Module(new ConstFastNode(value = 9, ID = 0))

  //i64 0
  val const1 = Module(new ConstFastNode(value = 0, ID = 1))

  //i32 1
  val const2 = Module(new ConstFastNode(value = 1, ID = 2))

  //i64 5
  val const3 = Module(new ConstFastNode(value = 5, ID = 3))

  //i64 1
  val const4 = Module(new ConstFastNode(value = 1, ID = 4))

  //i64 5
  val const5 = Module(new ConstFastNode(value = 5, ID = 5))


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

  Loop_0.io.loopBack(0) <> br_13.io.FalseOutput(0)

  Loop_0.io.loopFinish(0) <> br_13.io.TrueOutput(0)


  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */


  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

  Loop_0.io.InLiveIn(0) <> ArgSplitter.io.Out.dataPtrs.elements("field0")(0)


  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */

  Gep_arrayidx5.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field0")(0)

  Gep_arrayidx29.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field0")(1)


  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */


  /* ================================================================== *
   *                   LOOP LIVE OUT DEPENDENCIES                       *
   * ================================================================== */


  /* ================================================================== *
   *                   LOOP CARRY DEPENDENCIES                          *
   * ================================================================== */

  Loop_0.io.CarryDepenIn(0) <> binaryOp_indvars_iv_next11.io.Out(0)


  /* ================================================================== *
   *                   LOOP DATA CARRY DEPENDENCIES                     *
   * ================================================================== */

  phiindvars_iv4.io.InData(1) <> Loop_0.io.CarryDepenOut.elements("field0")(0)


  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  br_0.io.enable <> bb_entry0.io.Out(0)


  const0.io.enable <> bb_for_cond_cleanup1.io.Out(0)

  Gep_arrayidx31.io.enable <> bb_for_cond_cleanup1.io.Out(1)


  ld_2.io.enable <> bb_for_cond_cleanup1.io.Out(2)


  ret_3.io.In.enable <> bb_for_cond_cleanup1.io.Out(3)


  const1.io.enable <> bb_for_body2.io.Out(0)

  const2.io.enable <> bb_for_body2.io.Out(1)

  const3.io.enable <> bb_for_body2.io.Out(2)

  const4.io.enable <> bb_for_body2.io.Out(3)

  const5.io.enable <> bb_for_body2.io.Out(4)

  phiindvars_iv4.io.enable <> bb_for_body2.io.Out(5)


  Gep_arrayidx5.io.enable <> bb_for_body2.io.Out(6)


  ld_6.io.enable <> bb_for_body2.io.Out(7)


  binaryOp_mul7.io.enable <> bb_for_body2.io.Out(8)


  binaryOp_8.io.enable <> bb_for_body2.io.Out(9)


  Gep_arrayidx29.io.enable <> bb_for_body2.io.Out(10)


  st_10.io.enable <> bb_for_body2.io.Out(11)


  binaryOp_indvars_iv_next11.io.enable <> bb_for_body2.io.Out(12)


  icmp_exitcond12.io.enable <> bb_for_body2.io.Out(13)


  br_13.io.enable <> bb_for_body2.io.Out(14)


  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  phiindvars_iv4.io.Mask <> bb_for_body2.io.MaskBB(0)


  /* ================================================================== *
   *                   PRINT ALLOCA OFFSET                              *
   * ================================================================== */


  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */

  MemCtrl.io.rd.mem(0).MemReq <> ld_2.io.MemReq

  ld_2.io.MemResp <> MemCtrl.io.rd.mem(0).MemResp

  MemCtrl.io.rd.mem(1).MemReq <> ld_6.io.MemReq

  ld_6.io.MemResp <> MemCtrl.io.rd.mem(1).MemResp

  MemCtrl.io.wr.mem(0).MemReq <> st_10.io.MemReq

  st_10.io.MemResp <> MemCtrl.io.wr.mem(0).MemResp


  /* ================================================================== *
   *                   PRINT SHARED CONNECTIONS                         *
   * ================================================================== */


  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  Gep_arrayidx31.io.idx(0) <> const0.io.Out

  phiindvars_iv4.io.InData(0) <> const1.io.Out

  binaryOp_mul7.io.RightIO <> const2.io.Out

  binaryOp_8.io.RightIO <> const3.io.Out

  binaryOp_indvars_iv_next11.io.RightIO <> const4.io.Out

  icmp_exitcond12.io.RightIO <> const5.io.Out

  ld_2.io.GepAddr <> Gep_arrayidx31.io.Out(0)

  ret_3.io.In.data("field0") <> ld_2.io.Out(0)

  Gep_arrayidx5.io.idx(0) <> phiindvars_iv4.io.Out(0)

  binaryOp_8.io.LeftIO <> phiindvars_iv4.io.Out(1)

  binaryOp_indvars_iv_next11.io.LeftIO <> phiindvars_iv4.io.Out(2)

  ld_6.io.GepAddr <> Gep_arrayidx5.io.Out(0)

  binaryOp_mul7.io.LeftIO <> ld_6.io.Out(0)

  st_10.io.inData <> binaryOp_mul7.io.Out(0)

  Gep_arrayidx29.io.idx(0) <> binaryOp_8.io.Out(0)

  st_10.io.GepAddr <> Gep_arrayidx29.io.Out(0)

  icmp_exitcond12.io.LeftIO <> binaryOp_indvars_iv_next11.io.Out(1)

  br_13.io.CmpIO <> icmp_exitcond12.io.Out(0)

  Gep_arrayidx31.io.baseAddress <> ArgSplitter.io.Out.dataPtrs.elements("field0")(1)

  st_10.io.Out(0).ready := true.B


  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  br_13.io.PredOp(0) <> st_10.io.SuccOp(0)


  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_3.io.Out

}

