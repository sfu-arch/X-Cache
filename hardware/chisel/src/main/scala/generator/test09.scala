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


class test09DF(PtrsIn: Seq[Int] = List(32, 32, 32), ValsIn: Seq[Int] = List(), Returns: Seq[Int] = List(32))
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

  val PtrsInSplitter= Module(new SplitCallDCR(ptrsArgTypes = List(1, 1, 1), valsArgTypes = List()))
  PtrsInSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val Loop_0 = Module(new LoopBlockNode(NumIns = List(1, 1, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 0))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_entry0 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 0))

  val bb_for_cond_cleanup1 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 2, BID = 1))

  val bb_for_body2 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 14, NumPhi = 1, BID = 2))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  br label %for.body, !dbg !23, !UID !24, !BB_UID !25
  val br_0 = Module(new UBranchNode(ID = 0))

  //  ret i32 1, !dbg !26, !UID !27, !BB_UID !28
  val ret_1 = Module(new RetNode2(retTypes = List(32), ID = 1))

  //  %indvars.iv = phi i64 [ 0, %entry ], [ %indvars.iv.next, %for.body ], !UID !29
  val phiindvars_iv2 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 4, ID = 2, Res = true))

  //  %arrayidx = getelementptr inbounds i32, i32* %a, i64 %indvars.iv, !dbg !30, !UID !33
  val Gep_arrayidx3 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 3)(ElementSize = 8, ArraySize = List()))

  //  %0 = load i32, i32* %arrayidx, align 4, !dbg !30, !tbaa !34, !UID !38
  val ld_4 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 4, RouteID = 0))

  //  %arrayidx2 = getelementptr inbounds i32, i32* %b, i64 %indvars.iv, !dbg !39, !UID !40
  val Gep_arrayidx25 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 5)(ElementSize = 8, ArraySize = List()))

  //  %1 = load i32, i32* %arrayidx2, align 4, !dbg !39, !tbaa !34, !UID !41
  val ld_6 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 6, RouteID = 1))

  //  %add = add i32 %1, %0, !dbg !42, !UID !43
  val binaryOp_add7 = Module(new ComputeNode(NumOuts = 1, ID = 7, opCode = "add")(sign = false, Debug = false))

  //  %arrayidx4 = getelementptr inbounds i32, i32* %c, i64 %indvars.iv, !dbg !44, !UID !45
  val Gep_arrayidx48 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 8)(ElementSize = 8, ArraySize = List()))

  //  store i32 %add, i32* %arrayidx4, align 4, !dbg !46, !tbaa !34, !UID !47
  val st_9 = Module(new UnTypStore(NumPredOps = 0, NumSuccOps = 1, ID = 9, RouteID = 0))

  //  %indvars.iv.next = add nuw nsw i64 %indvars.iv, 1, !dbg !48, !UID !49
  val binaryOp_indvars_iv_next10 = Module(new ComputeNode(NumOuts = 2, ID = 10, opCode = "add")(sign = false, Debug = false))

  //  %exitcond = icmp eq i64 %indvars.iv.next, 5, !dbg !50, !UID !51
  val icmp_exitcond11 = Module(new ComputeNode(NumOuts = 1, ID = 11, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond, label %for.cond.cleanup, label %for.body, !dbg !23, !llvm.loop !52, !UID !54, !BB_UID !55
  val br_12 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 1, ID = 12))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i32 1
  val const0 = Module(new ConstFastNode(value = 1, ID = 0))

  //i64 0
  val const1 = Module(new ConstFastNode(value = 0, ID = 1))

  //i64 1
  val const2 = Module(new ConstFastNode(value = 1, ID = 2))

  //i64 5
  val const3 = Module(new ConstFastNode(value = 5, ID = 3))



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_entry0.io.predicateIn(0) <> PtrsInSplitter.io.Out.enable



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

  Loop_0.io.InLiveIn(0) <> PtrsInSplitter.io.Out.dataPtrs.elements("field0")(0)

  Loop_0.io.InLiveIn(1) <> PtrsInSplitter.io.Out.dataPtrs.elements("field1")(0)

  Loop_0.io.InLiveIn(2) <> PtrsInSplitter.io.Out.dataPtrs.elements("field2")(0)



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


  const0.io.enable <> bb_for_cond_cleanup1.io.Out(0)

  ret_1.io.In.enable <> bb_for_cond_cleanup1.io.Out(1)


  const1.io.enable <> bb_for_body2.io.Out(0)

  const2.io.enable <> bb_for_body2.io.Out(1)

  const3.io.enable <> bb_for_body2.io.Out(2)

  phiindvars_iv2.io.enable <> bb_for_body2.io.Out(3)


  Gep_arrayidx3.io.enable <> bb_for_body2.io.Out(4)


  ld_4.io.enable <> bb_for_body2.io.Out(5)


  Gep_arrayidx25.io.enable <> bb_for_body2.io.Out(6)


  ld_6.io.enable <> bb_for_body2.io.Out(7)


  binaryOp_add7.io.enable <> bb_for_body2.io.Out(8)


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

  MemCtrl.io.ReadIn(0) <> ld_4.io.memReq

  ld_4.io.memResp <> MemCtrl.io.ReadOut(0)

  MemCtrl.io.ReadIn(1) <> ld_6.io.memReq

  ld_6.io.memResp <> MemCtrl.io.ReadOut(1)

  MemCtrl.io.WriteIn(0) <> st_9.io.memReq

  st_9.io.memResp <> MemCtrl.io.WriteOut(0)



  /* ================================================================== *
   *                   PRINT SHARED CONNECTIONS                         *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  ret_1.io.In.data("field0") <> const0.io.Out

  phiindvars_iv2.io.InData(0) <> const1.io.Out

  binaryOp_indvars_iv_next10.io.RightIO <> const2.io.Out

  icmp_exitcond11.io.RightIO <> const3.io.Out

  Gep_arrayidx3.io.idx(0) <> phiindvars_iv2.io.Out(0)

  Gep_arrayidx25.io.idx(0) <> phiindvars_iv2.io.Out(1)

  Gep_arrayidx48.io.idx(0) <> phiindvars_iv2.io.Out(2)

  binaryOp_indvars_iv_next10.io.LeftIO <> phiindvars_iv2.io.Out(3)

  ld_4.io.GepAddr <> Gep_arrayidx3.io.Out(0)

  binaryOp_add7.io.RightIO <> ld_4.io.Out(0)

  ld_6.io.GepAddr <> Gep_arrayidx25.io.Out(0)

  binaryOp_add7.io.LeftIO <> ld_6.io.Out(0)

  st_9.io.inData <> binaryOp_add7.io.Out(0)

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

