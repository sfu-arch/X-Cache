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


class test15_reduceDF(PtrsIn: Seq[Int] = List(64), ValsIn: Seq[Int] = List(), Returns: Seq[Int] = List(64))
			(implicit p: Parameters) extends DandelionAccelDCRModule(PtrsIn, ValsIn, Returns){


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  val MemCtrl = Module(new CacheMemoryEngine(ID = 0, NumRead = 1, NumWrite = 0))

  io.MemReq <> MemCtrl.io.cache.MemReq
  MemCtrl.io.cache.MemResp <> io.MemResp
  val ArgSplitter = Module(new SplitCallDCR(ptrsArgTypes = List(1), valsArgTypes = List()))
  ArgSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val Loop_0 = Module(new LoopBlockNode(NumIns = List(1), NumOuts = List(1), NumCarry = List(1, 1), NumExits = 1, ID = 0))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_entry0 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 0))

  val bb_for_cond_cleanup1 = Module(new BasicBlockNode(NumInputs = 1, NumOuts = 2, NumPhi = 1, BID = 1))

  val bb_for_body2 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 12, NumPhi = 2, BID = 2))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  br label %for.body, !dbg !22, !UID !23, !BB_UID !24
  val br_0 = Module(new UBranchNode(ID = 0))

  //  %add.lcssa = phi i32 [ %add, %for.body ], !UID !25
  val phiadd_lcssa1 = Module(new PhiFastNode(NumInputs = 1, NumOutputs = 1, ID = 1, Res = false))

  //  ret i32 %add.lcssa, !dbg !26, !UID !27, !BB_UID !28
  val ret_2 = Module(new RetNode2(retTypes = List(32), ID = 2))

  //  %indvars.iv = phi i64 [ 0, %entry ], [ %indvars.iv.next, %for.body ], !UID !29
  val phiindvars_iv3 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 3, Res = true))

  //  %sum.06 = phi i32 [ 0, %entry ], [ %add, %for.body ], !UID !30
  val phisum_064 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 1, ID = 4, Res = true))

  //  %arrayidx = getelementptr inbounds i32, i32* %c, i64 %indvars.iv, !dbg !31, !UID !34
  val Gep_arrayidx5 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 5)(ElementSize = 8, ArraySize = List()))

  //  %0 = load i32, i32* %arrayidx, align 4, !dbg !31, !tbaa !35, !UID !39
  val ld_6 = Module(new UnTypLoadCache(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 6, RouteID = 0))

  //  %add = add i32 %0, %sum.06, !dbg !40, !UID !41
  val binaryOp_add7 = Module(new ComputeNode(NumOuts = 2, ID = 7, opCode = "add")(sign = false, Debug = false))

  //  %indvars.iv.next = add nuw nsw i64 %indvars.iv, 1, !dbg !42, !UID !43
  val binaryOp_indvars_iv_next8 = Module(new ComputeNode(NumOuts = 2, ID = 8, opCode = "add")(sign = false, Debug = false))

  //  %exitcond = icmp eq i64 %indvars.iv.next, 24, !dbg !44, !UID !45
  val icmp_exitcond9 = Module(new ComputeNode(NumOuts = 1, ID = 9, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond, label %for.cond.cleanup, label %for.body, !dbg !22, !llvm.loop !46, !UID !48, !BB_UID !49
  val br_10 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 10))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i64 0
  val const0 = Module(new ConstFastNode(value = 0, ID = 0))

  //i32 0
  val const1 = Module(new ConstFastNode(value = 0, ID = 1))

  //i64 1
  val const2 = Module(new ConstFastNode(value = 1, ID = 2))

  //i64 24
  val const3 = Module(new ConstFastNode(value = 5, ID = 3))



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

  Loop_0.io.loopBack(0) <> br_10.io.FalseOutput(0)

  Loop_0.io.loopFinish(0) <> br_10.io.TrueOutput(0)



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



  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */

  Loop_0.io.InLiveOut(0) <> binaryOp_add7.io.Out(0)



  /* ================================================================== *
   *                   LOOP LIVE OUT DEPENDENCIES                       *
   * ================================================================== */

  phiadd_lcssa1.io.InData(0) <> Loop_0.io.OutLiveOut.elements("field0")(0)



  /* ================================================================== *
   *                   LOOP CARRY DEPENDENCIES                          *
   * ================================================================== */

  Loop_0.io.CarryDepenIn(0) <> binaryOp_add7.io.Out(1)

  Loop_0.io.CarryDepenIn(1) <> binaryOp_indvars_iv_next8.io.Out(0)



  /* ================================================================== *
   *                   LOOP DATA CARRY DEPENDENCIES                     *
   * ================================================================== */

  phisum_064.io.InData(1) <> Loop_0.io.CarryDepenOut.elements("field0")(0)

  phiindvars_iv3.io.InData(1) <> Loop_0.io.CarryDepenOut.elements("field1")(0)



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  br_0.io.enable <> bb_entry0.io.Out(0)


  phiadd_lcssa1.io.enable <> bb_for_cond_cleanup1.io.Out(0)


  ret_2.io.In.enable <> bb_for_cond_cleanup1.io.Out(1)


  const0.io.enable <> bb_for_body2.io.Out(0)

  const1.io.enable <> bb_for_body2.io.Out(1)

  const2.io.enable <> bb_for_body2.io.Out(2)

  const3.io.enable <> bb_for_body2.io.Out(3)

  phiindvars_iv3.io.enable <> bb_for_body2.io.Out(4)


  phisum_064.io.enable <> bb_for_body2.io.Out(5)


  Gep_arrayidx5.io.enable <> bb_for_body2.io.Out(6)


  ld_6.io.enable <> bb_for_body2.io.Out(7)


  binaryOp_add7.io.enable <> bb_for_body2.io.Out(8)


  binaryOp_indvars_iv_next8.io.enable <> bb_for_body2.io.Out(9)


  icmp_exitcond9.io.enable <> bb_for_body2.io.Out(10)


  br_10.io.enable <> bb_for_body2.io.Out(11)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  phiadd_lcssa1.io.Mask <> bb_for_cond_cleanup1.io.MaskBB(0)

  phiindvars_iv3.io.Mask <> bb_for_body2.io.MaskBB(0)

  phisum_064.io.Mask <> bb_for_body2.io.MaskBB(1)



  /* ================================================================== *
   *                   PRINT ALLOCA OFFSET                              *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */

  MemCtrl.io.rd.mem(0).MemReq <> ld_6.io.MemReq

  ld_6.io.MemResp <> MemCtrl.io.rd.mem(0).MemResp



  /* ================================================================== *
   *                   PRINT SHARED CONNECTIONS                         *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  phiindvars_iv3.io.InData(0) <> const0.io.Out

  phisum_064.io.InData(0) <> const1.io.Out

  binaryOp_indvars_iv_next8.io.RightIO <> const2.io.Out

  icmp_exitcond9.io.RightIO <> const3.io.Out

  ret_2.io.In.data("field0") <> phiadd_lcssa1.io.Out(0)

  Gep_arrayidx5.io.idx(0) <> phiindvars_iv3.io.Out(0)

  binaryOp_indvars_iv_next8.io.LeftIO <> phiindvars_iv3.io.Out(1)

  binaryOp_add7.io.RightIO <> phisum_064.io.Out(0)

  ld_6.io.GepAddr <> Gep_arrayidx5.io.Out(0)

  binaryOp_add7.io.LeftIO <> ld_6.io.Out(0)

  icmp_exitcond9.io.LeftIO <> binaryOp_indvars_iv_next8.io.Out(1)

  br_10.io.CmpIO <> icmp_exitcond9.io.Out(0)



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_2.io.Out

}

