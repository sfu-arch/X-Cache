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
			(implicit p: Parameters) extends DandelionAccelDCRModule(PtrsIn, ValsIn, Returns){


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  val MemCtrl = Module(new UnifiedController(ID = 0, Size = 32, NReads = 2, NWrites = 1)
  (WControl = new WriteMemoryController(NumOps = 1, BaseSize = 2, NumEntries = 2, Serialize = true))
  (RControl = new ReadMemoryController(NumOps = 2, BaseSize = 2, NumEntries = 2, Serialize = true))
  (RWArbiter = new ReadWriteArbiter()))

  io.MemReq <> MemCtrl.io.MemReq
  MemCtrl.io.MemResp <> io.MemResp

  val ArgSplitter = Module(new SplitCallDCR(ptrsArgTypes = List(2), valsArgTypes = List()))
  ArgSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val Loop_0 = Module(new LoopBlockNode(NumIns = List(1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 0))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_entry0 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 0))

  val bb_for_cond_cleanup1 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 4, BID = 1))

  val bb_for_body2 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 12, NumPhi = 1, BID = 2))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  br label %for.body, !dbg !19, !UID !20, !BB_UID !21
  val br_0 = Module(new UBranchNode(ID = 0))

  //  %arrayidx3 = getelementptr inbounds i32, i32* %a, i64 9, !dbg !22, !UID !23
  val Gep_arrayidx31 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 1)(ElementSize = 8, ArraySize = List()))

  //  %0 = load i32, i32* %arrayidx3, align 4, !dbg !22, !tbaa !24, !UID !28
  val ld_2 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 2, RouteID = 0))

  //  ret i32 %0, !dbg !29, !UID !30, !BB_UID !31
  val ret_3 = Module(new RetNode2(retTypes = List(32), ID = 3))

  //  %indvars.iv = phi i64 [ 0, %entry ], [ %indvars.iv.next, %for.body ], !UID !32
  val phiindvars_iv4 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 4, Res = true))

  //  %arrayidx = getelementptr inbounds i32, i32* %a, i64 %indvars.iv, !dbg !33, !UID !36
  val Gep_arrayidx5 = Module(new GepNode(NumIns = 1, NumOuts = 2, ID = 5)(ElementSize = 8, ArraySize = List()))

  //  %1 = load i32, i32* %arrayidx, align 4, !dbg !33, !tbaa !24, !UID !37
  val ld_6 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 6, RouteID = 1))

  //  %mul = shl i32 %1, 1, !dbg !38, !UID !39
  val binaryOp_mul7 = Module(new ComputeNode(NumOuts = 1, ID = 7, opCode = "shl")(sign = false, Debug = false))

  //  store i32 %mul, i32* %arrayidx, align 4, !dbg !40, !tbaa !24, !UID !41
  val st_8 = Module(new UnTypStore(NumPredOps = 0, NumSuccOps = 1, ID = 8, RouteID = 0))

  //  %indvars.iv.next = add nuw nsw i64 %indvars.iv, 1, !dbg !42, !UID !43
  val binaryOp_indvars_iv_next9 = Module(new ComputeNode(NumOuts = 2, ID = 9, opCode = "add")(sign = false, Debug = false))

  //  %exitcond = icmp eq i64 %indvars.iv.next, 10, !dbg !44, !UID !45
  val icmp_exitcond10 = Module(new ComputeNode(NumOuts = 1, ID = 10, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond, label %for.cond.cleanup, label %for.body, !dbg !19, !llvm.loop !46, !UID !48, !BB_UID !49
  val br_11 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 1, ID = 11))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i64 9
  val const0 = Module(new ConstFastNode(value = 9, ID = 0))

  //i64 0
  val const1 = Module(new ConstFastNode(value = 0, ID = 1))

  //i32 1
  val const2 = Module(new ConstFastNode(value = 1, ID = 2))

  //i64 1
  val const3 = Module(new ConstFastNode(value = 1, ID = 3))

  //i64 10
  val const4 = Module(new ConstFastNode(value = 10, ID = 4))



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

  Loop_0.io.loopBack(0) <> br_11.io.FalseOutput(0)

  Loop_0.io.loopFinish(0) <> br_11.io.TrueOutput(0)



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



  /* ================================================================== *
   *                   LOOP LIVE OUT DEPENDENCIES                       *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP CARRY DEPENDENCIES                          *
   * ================================================================== */

  Loop_0.io.CarryDepenIn(0) <> binaryOp_indvars_iv_next9.io.Out(0)



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

  phiindvars_iv4.io.enable <> bb_for_body2.io.Out(4)


  Gep_arrayidx5.io.enable <> bb_for_body2.io.Out(5)


  ld_6.io.enable <> bb_for_body2.io.Out(6)


  binaryOp_mul7.io.enable <> bb_for_body2.io.Out(7)


  st_8.io.enable <> bb_for_body2.io.Out(8)


  binaryOp_indvars_iv_next9.io.enable <> bb_for_body2.io.Out(9)


  icmp_exitcond10.io.enable <> bb_for_body2.io.Out(10)


  br_11.io.enable <> bb_for_body2.io.Out(11)




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

  MemCtrl.io.ReadIn(0) <> ld_2.io.memReq

  ld_2.io.memResp <> MemCtrl.io.ReadOut(0)

  MemCtrl.io.ReadIn(1) <> ld_6.io.memReq

  ld_6.io.memResp <> MemCtrl.io.ReadOut(1)

  MemCtrl.io.WriteIn(0) <> st_8.io.memReq

  st_8.io.memResp <> MemCtrl.io.WriteOut(0)



  /* ================================================================== *
   *                   PRINT SHARED CONNECTIONS                         *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  Gep_arrayidx31.io.idx(0) <> const0.io.Out

  phiindvars_iv4.io.InData(0) <> const1.io.Out

  binaryOp_mul7.io.RightIO <> const2.io.Out

  binaryOp_indvars_iv_next9.io.RightIO <> const3.io.Out

  icmp_exitcond10.io.RightIO <> const4.io.Out

  ld_2.io.GepAddr <> Gep_arrayidx31.io.Out(0)

  ret_3.io.In.data("field0") <> ld_2.io.Out(0)

  Gep_arrayidx5.io.idx(0) <> phiindvars_iv4.io.Out(0)

  binaryOp_indvars_iv_next9.io.LeftIO <> phiindvars_iv4.io.Out(1)

  ld_6.io.GepAddr <> Gep_arrayidx5.io.Out(0)

  st_8.io.GepAddr <> Gep_arrayidx5.io.Out(1)

  binaryOp_mul7.io.LeftIO <> ld_6.io.Out(0)

  st_8.io.inData <> binaryOp_mul7.io.Out(0)

  icmp_exitcond10.io.LeftIO <> binaryOp_indvars_iv_next9.io.Out(1)

  br_11.io.CmpIO <> icmp_exitcond10.io.Out(0)

  Gep_arrayidx31.io.baseAddress <> ArgSplitter.io.Out.dataPtrs.elements("field0")(1)

  st_8.io.Out(0).ready := true.B



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  br_11.io.PredOp(0) <> st_8.io.SuccOp(0)



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_3.io.Out

}

