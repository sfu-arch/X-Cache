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


class test07DF(ArgsIn: Seq[Int] = List(32, 32), Returns: Seq[Int] = List(32))
			(implicit p: Parameters) extends DandelionAccelModule(ArgsIn, Returns){


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  val MemCtrl = Module(new UnifiedController(ID = 0, Size = 32, NReads = 1, NWrites = 1)
  (WControl = new WriteMemoryController(NumOps = 1, BaseSize = 2, NumEntries = 2))
  (RControl = new ReadMemoryController(NumOps = 1, BaseSize = 2, NumEntries = 2))
  (RWArbiter = new ReadWriteArbiter()))

  io.MemReq <> MemCtrl.io.MemReq
  MemCtrl.io.MemResp <> io.MemResp

  val InputSplitter = Module(new SplitCallNew(List(1, 3)))
  InputSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val Loop_0 = Module(new LoopBlockNode(NumIns = List(1, 1, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 0))

  val Loop_1 = Module(new LoopBlockNode(NumIns = List(2, 1, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 1))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_entry0 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 3, BID = 0))

  val bb_for_body_lr_ph1 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 2, BID = 1))

  val bb_for_cond_cleanup_loopexit2 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 2))

  val bb_for_cond_cleanup3 = Module(new BasicBlockNoMaskFastNode(NumInputs = 2, NumOuts = 2, BID = 3))

  val bb_for_body4_lr_ph4 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 4, NumPhi = 1, BID = 4))

  val bb_for_cond_cleanup35 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 4, BID = 5))

  val bb_for_body46 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 14, NumPhi = 1, BID = 6))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  %cmp25 = icmp eq i32 %n, 0, !dbg !31, !UID !32
  val icmp_cmp250 = Module(new ComputeNode(NumOuts = 1, ID = 0, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %cmp25, label %for.cond.cleanup, label %for.body.lr.ph, !dbg !33, !UID !34, !BB_UID !35
  val br_1 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 1))

  //  %wide.trip.count = zext i32 %n to i64, !UID !36
  val sextwide_trip_count2 = Module(new ZextNode(NumOuts = 1))

  //  br label %for.body4.lr.ph, !dbg !33, !UID !37, !BB_UID !38
  val br_3 = Module(new UBranchNode(ID = 3))

  //  br label %for.cond.cleanup, !dbg !39
  val br_4 = Module(new UBranchNode(ID = 4))

  //  ret i32 0, !dbg !39, !UID !40, !BB_UID !41
  val ret_5 = Module(new RetNode2(retTypes = List(32), ID = 5))

  //  %j.026 = phi i32 [ 0, %for.body.lr.ph ], [ %inc9, %for.cond.cleanup3 ], !UID !42
  val phij_0266 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 6, Res = true))

  //  %mul = mul i32 %j.026, %n, !UID !44
  val binaryOp_mul7 = Module(new ComputeNode(NumOuts = 1, ID = 7, opCode = "mul")(sign = false, Debug = false))

  //  br label %for.body4, !dbg !45, !UID !46, !BB_UID !47
  val br_8 = Module(new UBranchNode(ID = 8))

  //  %inc9 = add nuw i32 %j.026, 1, !dbg !48, !UID !49
  val binaryOp_inc99 = Module(new ComputeNode(NumOuts = 2, ID = 9, opCode = "add")(sign = false, Debug = false))

  //  %exitcond27 = icmp eq i32 %inc9, %n, !dbg !31, !UID !50
  val icmp_exitcond2710 = Module(new ComputeNode(NumOuts = 1, ID = 10, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond27, label %for.cond.cleanup.loopexit, label %for.body4.lr.ph, !dbg !33, !llvm.loop !51, !UID !53, !BB_UID !54
  val br_11 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 11))

  //  %indvars.iv = phi i64 [ 0, %for.body4.lr.ph ], [ %indvars.iv.next, %for.body4 ], !UID !55
  val phiindvars_iv12 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 12, Res = true))

  //  %0 = trunc i64 %indvars.iv to i32, !dbg !56, !UID !57
  val trunc13 = Module(new TruncNode(NumOuts = 1))

  //  %add = add i32 %mul, %0, !dbg !56, !UID !58
  val binaryOp_add14 = Module(new ComputeNode(NumOuts = 1, ID = 14, opCode = "add")(sign = false, Debug = false))

  //  %idxprom = zext i32 %add to i64, !dbg !60, !UID !61
  val sextidxprom15 = Module(new ZextNode(NumOuts = 1))

  //  %arrayidx = getelementptr inbounds i32, i32* %a, i64 %idxprom, !dbg !60, !UID !62
  val Gep_arrayidx16 = Module(new GepNode(NumIns = 1, NumOuts = 2, ID = 16)(ElementSize = 8, ArraySize = List()))

  //  %1 = load i32, i32* %arrayidx, align 4, !dbg !60, !tbaa !63, !UID !67
  val ld_17 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 17, RouteID = 0))

  //  %mul5 = shl i32 %1, 1, !dbg !68, !UID !69
  val binaryOp_mul518 = Module(new ComputeNode(NumOuts = 1, ID = 18, opCode = "shl")(sign = false, Debug = false))

  //  store i32 %mul5, i32* %arrayidx, align 4, !dbg !70, !tbaa !63, !UID !71
  val st_19 = Module(new UnTypStore(NumPredOps = 0, NumSuccOps = 0, ID = 19, RouteID = 0))

  //  %indvars.iv.next = add nuw nsw i64 %indvars.iv, 1, !dbg !72, !UID !73
  val binaryOp_indvars_iv_next20 = Module(new ComputeNode(NumOuts = 2, ID = 20, opCode = "add")(sign = false, Debug = false))

  //  %exitcond = icmp eq i64 %indvars.iv.next, %wide.trip.count, !dbg !74, !UID !75
  val icmp_exitcond21 = Module(new ComputeNode(NumOuts = 1, ID = 21, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond, label %for.cond.cleanup3, label %for.body4, !dbg !45, !llvm.loop !76, !UID !78, !BB_UID !79
  val br_22 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 22))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i32 0
  val const0 = Module(new ConstFastNode(value = 0, ID = 0))

  //i32 0
  val const1 = Module(new ConstFastNode(value = 0, ID = 1))

  //i32 0
  val const2 = Module(new ConstFastNode(value = 0, ID = 2))

  //i32 1
  val const3 = Module(new ConstFastNode(value = 1, ID = 3))

  //i64 0
  val const4 = Module(new ConstFastNode(value = 0, ID = 4))

  //i32 1
  val const5 = Module(new ConstFastNode(value = 1, ID = 5))

  //i64 1
  val const6 = Module(new ConstFastNode(value = 1, ID = 6))



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_entry0.io.predicateIn(0) <> InputSplitter.io.Out.enable

  bb_for_body_lr_ph1.io.predicateIn(0) <> br_1.io.FalseOutput(0)

  bb_for_cond_cleanup3.io.predicateIn(1) <> br_1.io.TrueOutput(0)

  bb_for_cond_cleanup3.io.predicateIn(0) <> br_4.io.Out(0)



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE LOOP                     *
   * ================================================================== */

  bb_for_cond_cleanup_loopexit2.io.predicateIn(0) <> Loop_1.io.loopExit(0)

  bb_for_body4_lr_ph4.io.predicateIn(1) <> Loop_1.io.activate_loop_start

  bb_for_body4_lr_ph4.io.predicateIn(0) <> Loop_1.io.activate_loop_back

  bb_for_cond_cleanup35.io.predicateIn(0) <> Loop_0.io.loopExit(0)

  bb_for_body46.io.predicateIn(1) <> Loop_0.io.activate_loop_start

  bb_for_body46.io.predicateIn(0) <> Loop_0.io.activate_loop_back



  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */

  Loop_0.io.enable <> br_8.io.Out(0)

  Loop_0.io.loopBack(0) <> br_22.io.FalseOutput(0)

  Loop_0.io.loopFinish(0) <> br_22.io.TrueOutput(0)

  Loop_1.io.enable <> br_3.io.Out(0)

  Loop_1.io.loopBack(0) <> br_11.io.FalseOutput(0)

  Loop_1.io.loopFinish(0) <> br_11.io.TrueOutput(0)



  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

  Loop_0.io.InLiveIn(0) <> binaryOp_mul7.io.Out(0)

  Loop_0.io.InLiveIn(1) <> Loop_1.io.OutLiveIn.elements("field1")(0)

  Loop_0.io.InLiveIn(2) <> Loop_1.io.OutLiveIn.elements("field2")(0)

  Loop_1.io.InLiveIn(0) <> InputSplitter.io.Out.data.elements("field1")(0)

  Loop_1.io.InLiveIn(1) <> InputSplitter.io.Out.data.elements("field0")(0)

  Loop_1.io.InLiveIn(2) <> sextwide_trip_count2.io.Out(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */

  binaryOp_add14.io.LeftIO <> Loop_0.io.OutLiveIn.elements("field0")(0)

  Gep_arrayidx16.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field1")(0)

  icmp_exitcond21.io.RightIO <> Loop_0.io.OutLiveIn.elements("field2")(0)

  binaryOp_mul7.io.RightIO <> Loop_1.io.OutLiveIn.elements("field0")(0)

  icmp_exitcond2710.io.RightIO <> Loop_1.io.OutLiveIn.elements("field0")(1)



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

  Loop_1.io.CarryDepenIn(0) <> binaryOp_inc99.io.Out(0)



  /* ================================================================== *
   *                   LOOP DATA CARRY DEPENDENCIES                     *
   * ================================================================== */

  phiindvars_iv12.io.InData(1) <> Loop_0.io.CarryDepenOut.elements("field0")(0)

  phij_0266.io.InData(1) <> Loop_1.io.CarryDepenOut.elements("field0")(0)



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  const0.io.enable <> bb_entry0.io.Out(0)

  icmp_cmp250.io.enable <> bb_entry0.io.Out(1)


  br_1.io.enable <> bb_entry0.io.Out(2)


  sextwide_trip_count2.io.enable <> bb_for_body_lr_ph1.io.Out(0)


  br_3.io.enable <> bb_for_body_lr_ph1.io.Out(1)


  br_4.io.enable <> bb_for_cond_cleanup_loopexit2.io.Out(0)


  const1.io.enable <> bb_for_cond_cleanup3.io.Out(0)

  ret_5.io.In.enable <> bb_for_cond_cleanup3.io.Out(1)


  const2.io.enable <> bb_for_body4_lr_ph4.io.Out(0)

  phij_0266.io.enable <> bb_for_body4_lr_ph4.io.Out(1)


  binaryOp_mul7.io.enable <> bb_for_body4_lr_ph4.io.Out(2)


  br_8.io.enable <> bb_for_body4_lr_ph4.io.Out(3)


  const3.io.enable <> bb_for_cond_cleanup35.io.Out(0)

  binaryOp_inc99.io.enable <> bb_for_cond_cleanup35.io.Out(1)


  icmp_exitcond2710.io.enable <> bb_for_cond_cleanup35.io.Out(2)


  br_11.io.enable <> bb_for_cond_cleanup35.io.Out(3)


  const4.io.enable <> bb_for_body46.io.Out(0)

  const5.io.enable <> bb_for_body46.io.Out(1)

  const6.io.enable <> bb_for_body46.io.Out(2)

  phiindvars_iv12.io.enable <> bb_for_body46.io.Out(3)


  trunc13.io.enable <> bb_for_body46.io.Out(4)


  binaryOp_add14.io.enable <> bb_for_body46.io.Out(5)


  sextidxprom15.io.enable <> bb_for_body46.io.Out(6)


  Gep_arrayidx16.io.enable <> bb_for_body46.io.Out(7)


  ld_17.io.enable <> bb_for_body46.io.Out(8)


  binaryOp_mul518.io.enable <> bb_for_body46.io.Out(9)


  st_19.io.enable <> bb_for_body46.io.Out(10)


  binaryOp_indvars_iv_next20.io.enable <> bb_for_body46.io.Out(11)


  icmp_exitcond21.io.enable <> bb_for_body46.io.Out(12)


  br_22.io.enable <> bb_for_body46.io.Out(13)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  phij_0266.io.Mask <> bb_for_body4_lr_ph4.io.MaskBB(0)

  phiindvars_iv12.io.Mask <> bb_for_body46.io.MaskBB(0)



  /* ================================================================== *
   *                   PRINT ALLOCA OFFSET                              *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */

  MemCtrl.io.ReadIn(0) <> ld_17.io.memReq

  ld_17.io.memResp <> MemCtrl.io.ReadOut(0)

  MemCtrl.io.WriteIn(0) <> st_19.io.memReq

  st_19.io.memResp <> MemCtrl.io.WriteOut(0)



  /* ================================================================== *
   *                   PRINT SHARED CONNECTIONS                         *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  icmp_cmp250.io.RightIO <> const0.io.Out

  ret_5.io.In.data("field0") <> const1.io.Out

  phij_0266.io.InData(0) <> const2.io.Out

  binaryOp_inc99.io.RightIO <> const3.io.Out

  phiindvars_iv12.io.InData(0) <> const4.io.Out

  binaryOp_mul518.io.RightIO <> const5.io.Out

  binaryOp_indvars_iv_next20.io.RightIO <> const6.io.Out

  br_1.io.CmpIO <> icmp_cmp250.io.Out(0)

  binaryOp_mul7.io.LeftIO <> phij_0266.io.Out(0)

  binaryOp_inc99.io.LeftIO <> phij_0266.io.Out(1)

  icmp_exitcond2710.io.LeftIO <> binaryOp_inc99.io.Out(1)

  br_11.io.CmpIO <> icmp_exitcond2710.io.Out(0)

  trunc13.io.Input <> phiindvars_iv12.io.Out(0)

  binaryOp_indvars_iv_next20.io.LeftIO <> phiindvars_iv12.io.Out(1)

  binaryOp_add14.io.RightIO <> trunc13.io.Out(0)

  sextidxprom15.io.Input <> binaryOp_add14.io.Out(0)

  Gep_arrayidx16.io.idx(0) <> sextidxprom15.io.Out(0)

  ld_17.io.GepAddr <> Gep_arrayidx16.io.Out(0)

  st_19.io.GepAddr <> Gep_arrayidx16.io.Out(1)

  binaryOp_mul518.io.LeftIO <> ld_17.io.Out(0)

  st_19.io.inData <> binaryOp_mul518.io.Out(0)

  icmp_exitcond21.io.LeftIO <> binaryOp_indvars_iv_next20.io.Out(1)

  br_22.io.CmpIO <> icmp_exitcond21.io.Out(0)

  icmp_cmp250.io.LeftIO <> InputSplitter.io.Out.data.elements("field1")(1)

  sextwide_trip_count2.io.Input <> InputSplitter.io.Out.data.elements("field1")(2)

  st_19.io.Out(0).ready := true.B



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_5.io.Out

}

