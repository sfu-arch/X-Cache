package dataflow

import FPU._
import accel._
import arbiters._
import chisel3._
import chisel3.util._
import chisel3.Module._
import chisel3.testers._
import chisel3.iotesters._
import config._
import control._
import interfaces._
import junctions._
import loop._
import memory._
import muxes._
import node._
import org.scalatest._
import regfile._
import stack._
import util._


  /* ================================================================== *
   *                   PRINTING PORTS DEFINITION                        *
   * ================================================================== */

abstract class fftDFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32, 32, 32, 32))))
    val MemResp = Flipped(Valid(new MemResp))
    val MemReq = Decoupled(new MemReq)
    val out = Decoupled(new Call(List()))
  })
}

class fftDF(implicit p: Parameters) extends fftDFIO()(p) {


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  val MemCtrl = Module(new UnifiedController(ID = 0, Size = 32, NReads = 8, NWrites = 6)
  (WControl = new WriteMemoryController(NumOps = 6, BaseSize = 2, NumEntries = 2))
  (RControl = new ReadMemoryController(NumOps = 8, BaseSize = 2, NumEntries = 2))
  (RWArbiter = new ReadWriteArbiter()))

  io.MemReq <> MemCtrl.io.MemReq
  MemCtrl.io.MemResp <> io.MemResp

  val InputSplitter = Module(new SplitCallNew(List(1, 1, 1, 1)))
  InputSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val Loop_0 = Module(new LoopBlockO1(NumIns = List(3, 2, 2, 1, 1, 1), NumOuts = 0, NumExits = 1, ID = 0))

  val Loop_1 = Module(new LoopBlockO1(NumIns = List(1, 1, 1, 1), NumOuts = 0, NumExits = 1, ID = 1))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_entry0 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 3, BID = 0))

  val bb_for_cond1_preheader1 = Module(new LoopFastHead(NumOuts = 5, NumPhi = 2, BID = 1))

  val bb_for_body2_preheader2 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 2))

  val bb_for_body23 = Module(new LoopFastHead(NumOuts = 27, NumPhi = 1, BID = 3))

  val bb_if_then4 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 16, BID = 4))

  val bb_for_inc5 = Module(new BasicBlockNoMaskFastNode(NumInputs = 2, NumOuts = 5, BID = 5))

  val bb_for_inc53_loopexit6 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 6))

  val bb_for_inc537 = Module(new BasicBlockNoMaskFastNode(NumInputs = 2, NumOuts = 7, BID = 7))

  val bb_for_end558 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 8))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  br label %for.cond1.preheader
  val br_0 = Module(new UBranchNode(ID = 0))

  //  %log.0116 = phi i32 [ 0, %entry ], [ %inc54, %for.inc53 ]
  val philog_01161 = Module(new PhiFastNode2(NumInputs = 2, NumOutputs = 2, ID = 1))

  //  %span.0114 = phi i32 [ 512, %entry ], [ %shr, %for.inc53 ]
  val phispan_01142 = Module(new PhiFastNode2(NumInputs = 2, NumOutputs = 3, ID = 2))

  //  %cmp112 = icmp slt i32 %span.0114, 1024
  val icmp_cmp1123 = Module(new IcmpNode(NumOuts = 1, ID = 3, opCode = "ult")(sign = false))

  //  br i1 %cmp112, label %for.body2.preheader, label %for.inc53
  val br_4 = Module(new CBranchFastNodeVariable2(NumFalse = 1, NumTrue = 1, ID = 4))

  //  br label %for.body2
  val br_5 = Module(new UBranchNode(ID = 5))

  //  %odd.0113 = phi i32 [ %inc, %for.inc ], [ %span.0114, %for.body2.preheader ]
  val phiodd_01136 = Module(new PhiFastNode2(NumInputs = 2, NumOutputs = 1, ID = 6))

  //  %or = or i32 %odd.0113, %span.0114
  val binaryOp_or7 = Module(new ComputeNode(NumOuts = 3, ID = 7, opCode = "or")(sign = false))

  //  %xor = xor i32 %or, %span.0114
  val binaryOp_xor8 = Module(new ComputeNode(NumOuts = 2, ID = 8, opCode = "xor")(sign = false))

  //  %idxprom = sext i32 %xor to i64
  val sextidxprom9 = Module(new SextNode())

  //  %arrayidx = getelementptr inbounds double, double* %real, i64 %idxprom
  val Gep_arrayidx10 = Module(new GepNode(NumIns = 1, NumOuts = 2, ID = 10)(ElementSize = 8, ArraySize = List()))

  //  %0 = load double, double* %arrayidx, align 8, !tbaa !2
  val ld_11 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 2, ID = 11, RouteID = 0))

  //  %idxprom3 = sext i32 %or to i64
  val sextidxprom312 = Module(new SextNode())

  //  %arrayidx4 = getelementptr inbounds double, double* %real, i64 %idxprom3
  val Gep_arrayidx413 = Module(new GepNode(NumIns = 1, NumOuts = 4, ID = 13)(ElementSize = 8, ArraySize = List()))

  //  %1 = load double, double* %arrayidx4, align 8, !tbaa !2
  val ld_14 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 2, ID = 14, RouteID = 1))

  //  %add = fadd double %0, %1
  val FP_add15 = Module(new FPComputeNode(NumOuts = 1, ID = 15, opCode = "fadd")(t = p(FTYP)))

  //  %sub = fsub double %0, %1
  val FP_sub16 = Module(new FPComputeNode(NumOuts = 1, ID = 16, opCode = "fsub")(t = p(FTYP)))

  //  store double %sub, double* %arrayidx4, align 8, !tbaa !2
  val st_17 = Module(new UnTypStore(NumPredOps = 0, NumSuccOps = 0, ID = 17, RouteID = 0))

  //  store double %add, double* %arrayidx, align 8, !tbaa !2
  val st_18 = Module(new UnTypStore(NumPredOps = 0, NumSuccOps = 0, ID = 18, RouteID = 1))

  //  %arrayidx14 = getelementptr inbounds double, double* %img, i64 %idxprom
  val Gep_arrayidx1419 = Module(new GepNode(NumIns = 1, NumOuts = 2, ID = 19)(ElementSize = 8, ArraySize = List()))

  //  %2 = load double, double* %arrayidx14, align 8, !tbaa !2
  val ld_20 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 2, ID = 20, RouteID = 2))

  //  %arrayidx16 = getelementptr inbounds double, double* %img, i64 %idxprom3
  val Gep_arrayidx1621 = Module(new GepNode(NumIns = 1, NumOuts = 4, ID = 21)(ElementSize = 8, ArraySize = List()))

  //  %3 = load double, double* %arrayidx16, align 8, !tbaa !2
  val ld_22 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 2, ID = 22, RouteID = 3))

  //  %add17 = fadd double %2, %3
  val FP_add1723 = Module(new FPComputeNode(NumOuts = 1, ID = 23, opCode = "fadd")(t = p(FTYP)))

  //  %sub22 = fsub double %2, %3
  val FP_sub2224 = Module(new FPComputeNode(NumOuts = 1, ID = 24, opCode = "fsub")(t = p(FTYP)))

  //  store double %sub22, double* %arrayidx16, align 8, !tbaa !2
  val st_25 = Module(new UnTypStore(NumPredOps = 0, NumSuccOps = 0, ID = 25, RouteID = 2))

  //  store double %add17, double* %arrayidx14, align 8, !tbaa !2
  val st_26 = Module(new UnTypStore(NumPredOps = 0, NumSuccOps = 0, ID = 26, RouteID = 3))

  //  %shl = shl i32 %xor, %log.0116
  val binaryOp_shl27 = Module(new ComputeNode(NumOuts = 1, ID = 27, opCode = "shl")(sign = false))

  //  %and = and i32 %shl, 1023
  val binaryOp_and28 = Module(new ComputeNode(NumOuts = 2, ID = 28, opCode = "and")(sign = false))

  //  %tobool27 = icmp eq i32 %and, 0
  val icmp_tobool2729 = Module(new IcmpNode(NumOuts = 1, ID = 29, opCode = "eq")(sign = false))

  //  br i1 %tobool27, label %for.inc, label %if.then
  val br_30 = Module(new CBranchFastNodeVariable2(NumFalse = 1, NumTrue = 1, ID = 30))

  //  %idxprom28111 = zext i32 %and to i64
  val sextidxprom2811131 = Module(new ZextNode())

  //  %arrayidx29 = getelementptr inbounds double, double* %real_twid, i64 %idxprom28111
  val Gep_arrayidx2932 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 32)(ElementSize = 8, ArraySize = List()))

  //  %4 = load double, double* %arrayidx29, align 8, !tbaa !2
  val ld_33 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 2, ID = 33, RouteID = 4))

  //  %5 = load double, double* %arrayidx4, align 8, !tbaa !2
  val ld_34 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 2, ID = 34, RouteID = 5))

  //  %mul = fmul double %4, %5
  val FP_mul35 = Module(new FPComputeNode(NumOuts = 1, ID = 35, opCode = "fmul")(t = p(FTYP)))

  //  %arrayidx33 = getelementptr inbounds double, double* %img_twid, i64 %idxprom28111
  val Gep_arrayidx3336 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 36)(ElementSize = 8, ArraySize = List()))

  //  %6 = load double, double* %arrayidx33, align 8, !tbaa !2
  val ld_37 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 2, ID = 37, RouteID = 6))

  //  %7 = load double, double* %arrayidx16, align 8, !tbaa !2
  val ld_38 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 2, ID = 38, RouteID = 7))

  //  %mul36 = fmul double %6, %7
  val FP_mul3639 = Module(new FPComputeNode(NumOuts = 1, ID = 39, opCode = "fmul")(t = p(FTYP)))

  //  %sub37 = fsub double %mul, %mul36
  val FP_sub3740 = Module(new FPComputeNode(NumOuts = 1, ID = 40, opCode = "fsub")(t = p(FTYP)))

  //  %mul42 = fmul double %4, %7
  val FP_mul4241 = Module(new FPComputeNode(NumOuts = 1, ID = 41, opCode = "fmul")(t = p(FTYP)))

  //  %mul47 = fmul double %5, %6
  val FP_mul4742 = Module(new FPComputeNode(NumOuts = 1, ID = 42, opCode = "fmul")(t = p(FTYP)))

  //  %add48 = fadd double %mul47, %mul42
  val FP_add4843 = Module(new FPComputeNode(NumOuts = 1, ID = 43, opCode = "fadd")(t = p(FTYP)))

  //  store double %add48, double* %arrayidx16, align 8, !tbaa !2
  val st_44 = Module(new UnTypStore(NumPredOps = 0, NumSuccOps = 0, ID = 44, RouteID = 4))

  //  store double %sub37, double* %arrayidx4, align 8, !tbaa !2
  val st_45 = Module(new UnTypStore(NumPredOps = 0, NumSuccOps = 0, ID = 45, RouteID = 5))

  //  br label %for.inc
  val br_46 = Module(new UBranchNode(ID = 46))

  //  %inc = add nsw i32 %or, 1
  val binaryOp_inc47 = Module(new ComputeNode(NumOuts = 2, ID = 47, opCode = "add")(sign = false))

  //  %cmp = icmp slt i32 %inc, 1024
  val icmp_cmp48 = Module(new IcmpNode(NumOuts = 1, ID = 48, opCode = "ult")(sign = false))

  //  br i1 %cmp, label %for.body2, label %for.inc53.loopexit
  val br_49 = Module(new CBranchFastNodeVariable2(NumFalse = 1, NumTrue = 2, ID = 49))

  //  br label %for.inc53
  val br_50 = Module(new UBranchNode(ID = 50))

  //  %shr = ashr i32 %span.0114, 1
  val binaryOp_shr51 = Module(new ComputeNode(NumOuts = 1, ID = 51, opCode = "ashr")(sign = false))

  //  %inc54 = add nuw nsw i32 %log.0116, 1
  val binaryOp_inc5452 = Module(new ComputeNode(NumOuts = 2, ID = 52, opCode = "add")(sign = false))

  //  %exitcond = icmp eq i32 %inc54, 10
  val icmp_exitcond53 = Module(new IcmpNode(NumOuts = 1, ID = 53, opCode = "eq")(sign = false))

  //  br i1 %exitcond, label %for.end55, label %for.cond1.preheader
  val br_54 = Module(new CBranchFastNodeVariable2(NumFalse = 2, NumTrue = 1, ID = 54))

  //  ret void
  val ret_55 = Module(new RetNode2(retTypes = List(), ID = 55))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i32 0
  val const0 = Module(new ConstFastNode(value = 0, ID = 0))

  //i32 512
  val const1 = Module(new ConstFastNode(value = 512, ID = 1))

  //i32 1024
  val const2 = Module(new ConstFastNode(value = 1024, ID = 2))

  //i32 1023
  val const3 = Module(new ConstFastNode(value = 1023, ID = 3))

  //i32 0
  val const4 = Module(new ConstFastNode(value = 0, ID = 4))

  //i32 1
  val const5 = Module(new ConstFastNode(value = 1, ID = 5))

  //i32 1024
  val const6 = Module(new ConstFastNode(value = 1024, ID = 6))

  //i32 1
  val const7 = Module(new ConstFastNode(value = 1, ID = 7))

  //i32 1
  val const8 = Module(new ConstFastNode(value = 1, ID = 8))

  //i32 10
  val const9 = Module(new ConstFastNode(value = 10, ID = 9))



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_entry0.io.predicateIn(0) <> InputSplitter.io.Out.enable

  bb_for_cond1_preheader1.io.activate <> Loop_1.io.activate

  bb_for_cond1_preheader1.io.loopBack <> br_54.io.FalseOutput(0)

  bb_for_body2_preheader2.io.predicateIn(0) <> br_4.io.TrueOutput(0)

  bb_for_body23.io.activate <> Loop_0.io.activate

  bb_for_body23.io.loopBack <> br_49.io.TrueOutput(0)

  bb_if_then4.io.predicateIn(0) <> br_30.io.FalseOutput(0)

  bb_for_inc5.io.predicateIn(0) <> br_30.io.TrueOutput(0)

  bb_for_inc5.io.predicateIn(1) <> br_46.io.Out(0)

  bb_for_inc53_loopexit6.io.predicateIn(0) <> Loop_0.io.endEnable

  bb_for_inc537.io.predicateIn(0) <> br_4.io.FalseOutput(0)

  bb_for_inc537.io.predicateIn(1) <> br_50.io.Out(0)

  bb_for_end558.io.predicateIn(0) <> Loop_1.io.endEnable



  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */

  Loop_0.io.enable <> br_5.io.Out(0)

  Loop_0.io.latchEnable <> br_49.io.TrueOutput(1)

  Loop_0.io.loopExit(0) <> br_49.io.FalseOutput(0)

  Loop_1.io.enable <> br_0.io.Out(0)

  Loop_1.io.latchEnable <> br_54.io.FalseOutput(1)

  Loop_1.io.loopExit(0) <> br_54.io.TrueOutput(0)



  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

  Loop_0.io.In(0) <> phispan_01142.io.Out(1)

  Loop_0.io.In(1) <> Loop_1.io.liveIn.data("field0")(0)

  Loop_0.io.In(2) <> Loop_1.io.liveIn.data("field1")(0)

  Loop_0.io.In(3) <> philog_01161.io.Out(0)

  Loop_0.io.In(4) <> Loop_1.io.liveIn.data("field2")(0)

  Loop_0.io.In(5) <> Loop_1.io.liveIn.data("field3")(0)

  Loop_1.io.In(0) <> InputSplitter.io.Out.data("field0")(0)

  Loop_1.io.In(1) <> InputSplitter.io.Out.data("field1")(0)

  Loop_1.io.In(2) <> InputSplitter.io.Out.data("field2")(0)

  Loop_1.io.In(3) <> InputSplitter.io.Out.data("field3")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */

  phiodd_01136.io.InData(1) <> Loop_0.io.liveIn.data("field0")(0)

  binaryOp_or7.io.RightIO <> Loop_0.io.liveIn.data("field0")(1)

  binaryOp_xor8.io.RightIO <> Loop_0.io.liveIn.data("field0")(2)

  Gep_arrayidx10.io.baseAddress <> Loop_0.io.liveIn.data("field1")(0)

  Gep_arrayidx413.io.baseAddress <> Loop_0.io.liveIn.data("field1")(1)

  Gep_arrayidx1419.io.baseAddress <> Loop_0.io.liveIn.data("field2")(0)

  Gep_arrayidx1621.io.baseAddress <> Loop_0.io.liveIn.data("field2")(1)

  binaryOp_shl27.io.RightIO <> Loop_0.io.liveIn.data("field3")(0)

  Gep_arrayidx2932.io.baseAddress <> Loop_0.io.liveIn.data("field4")(0)

  Gep_arrayidx3336.io.baseAddress <> Loop_0.io.liveIn.data("field5")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  const0.io.enable <> bb_entry0.io.Out(0)

  const1.io.enable <> bb_entry0.io.Out(1)

  br_0.io.enable <> bb_entry0.io.Out(2)


  const2.io.enable <> bb_for_cond1_preheader1.io.Out(0)

  philog_01161.io.enable <> bb_for_cond1_preheader1.io.Out(1)

  phispan_01142.io.enable <> bb_for_cond1_preheader1.io.Out(2)

  icmp_cmp1123.io.enable <> bb_for_cond1_preheader1.io.Out(3)

  br_4.io.enable <> bb_for_cond1_preheader1.io.Out(4)


  br_5.io.enable <> bb_for_body2_preheader2.io.Out(0)


  const3.io.enable <> bb_for_body23.io.Out(0)

  const4.io.enable <> bb_for_body23.io.Out(1)

  phiodd_01136.io.enable <> bb_for_body23.io.Out(2)

  binaryOp_or7.io.enable <> bb_for_body23.io.Out(3)

  binaryOp_xor8.io.enable <> bb_for_body23.io.Out(4)

  sextidxprom9.io.enable <> bb_for_body23.io.Out(5)

  Gep_arrayidx10.io.enable <> bb_for_body23.io.Out(6)

  ld_11.io.enable <> bb_for_body23.io.Out(7)

  sextidxprom312.io.enable <> bb_for_body23.io.Out(8)

  Gep_arrayidx413.io.enable <> bb_for_body23.io.Out(9)

  ld_14.io.enable <> bb_for_body23.io.Out(10)

  FP_add15.io.enable <> bb_for_body23.io.Out(11)

  FP_sub16.io.enable <> bb_for_body23.io.Out(12)

  st_17.io.enable <> bb_for_body23.io.Out(13)

  st_18.io.enable <> bb_for_body23.io.Out(14)

  Gep_arrayidx1419.io.enable <> bb_for_body23.io.Out(15)

  ld_20.io.enable <> bb_for_body23.io.Out(16)

  Gep_arrayidx1621.io.enable <> bb_for_body23.io.Out(17)

  ld_22.io.enable <> bb_for_body23.io.Out(18)

  FP_add1723.io.enable <> bb_for_body23.io.Out(19)

  FP_sub2224.io.enable <> bb_for_body23.io.Out(20)

  st_25.io.enable <> bb_for_body23.io.Out(21)

  st_26.io.enable <> bb_for_body23.io.Out(22)

  binaryOp_shl27.io.enable <> bb_for_body23.io.Out(23)

  binaryOp_and28.io.enable <> bb_for_body23.io.Out(24)

  icmp_tobool2729.io.enable <> bb_for_body23.io.Out(25)

  br_30.io.enable <> bb_for_body23.io.Out(26)


  sextidxprom2811131.io.enable <> bb_if_then4.io.Out(0)

  Gep_arrayidx2932.io.enable <> bb_if_then4.io.Out(1)

  ld_33.io.enable <> bb_if_then4.io.Out(2)

  ld_34.io.enable <> bb_if_then4.io.Out(3)

  FP_mul35.io.enable <> bb_if_then4.io.Out(4)

  Gep_arrayidx3336.io.enable <> bb_if_then4.io.Out(5)

  ld_37.io.enable <> bb_if_then4.io.Out(6)

  ld_38.io.enable <> bb_if_then4.io.Out(7)

  FP_mul3639.io.enable <> bb_if_then4.io.Out(8)

  FP_sub3740.io.enable <> bb_if_then4.io.Out(9)

  FP_mul4241.io.enable <> bb_if_then4.io.Out(10)

  FP_mul4742.io.enable <> bb_if_then4.io.Out(11)

  FP_add4843.io.enable <> bb_if_then4.io.Out(12)

  st_44.io.enable <> bb_if_then4.io.Out(13)

  st_45.io.enable <> bb_if_then4.io.Out(14)

  br_46.io.enable <> bb_if_then4.io.Out(15)


  const5.io.enable <> bb_for_inc5.io.Out(0)

  const6.io.enable <> bb_for_inc5.io.Out(1)

  binaryOp_inc47.io.enable <> bb_for_inc5.io.Out(2)

  icmp_cmp48.io.enable <> bb_for_inc5.io.Out(3)

  br_49.io.enable <> bb_for_inc5.io.Out(4)


  br_50.io.enable <> bb_for_inc53_loopexit6.io.Out(0)


  const7.io.enable <> bb_for_inc537.io.Out(0)

  const8.io.enable <> bb_for_inc537.io.Out(1)

  const9.io.enable <> bb_for_inc537.io.Out(2)

  binaryOp_shr51.io.enable <> bb_for_inc537.io.Out(3)

  binaryOp_inc5452.io.enable <> bb_for_inc537.io.Out(4)

  icmp_exitcond53.io.enable <> bb_for_inc537.io.Out(5)

  br_54.io.enable <> bb_for_inc537.io.Out(6)


  ret_55.io.In.enable <> bb_for_end558.io.Out(0)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  philog_01161.io.Mask <> bb_for_cond1_preheader1.io.MaskBB(0)

  phispan_01142.io.Mask <> bb_for_cond1_preheader1.io.MaskBB(1)

  phiodd_01136.io.Mask <> bb_for_body23.io.MaskBB(0)



  /* ================================================================== *
   *                   PRINT ALLOCA OFFSET                              *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */

  MemCtrl.io.ReadIn(0) <> ld_11.io.memReq

  ld_11.io.memResp <> MemCtrl.io.ReadOut(0)

  MemCtrl.io.ReadIn(1) <> ld_14.io.memReq

  ld_14.io.memResp <> MemCtrl.io.ReadOut(1)

  MemCtrl.io.WriteIn(0) <> st_17.io.memReq

  st_17.io.memResp <> MemCtrl.io.WriteOut(0)

  MemCtrl.io.WriteIn(1) <> st_18.io.memReq

  st_18.io.memResp <> MemCtrl.io.WriteOut(1)

  MemCtrl.io.ReadIn(2) <> ld_20.io.memReq

  ld_20.io.memResp <> MemCtrl.io.ReadOut(2)

  MemCtrl.io.ReadIn(3) <> ld_22.io.memReq

  ld_22.io.memResp <> MemCtrl.io.ReadOut(3)

  MemCtrl.io.WriteIn(2) <> st_25.io.memReq

  st_25.io.memResp <> MemCtrl.io.WriteOut(2)

  MemCtrl.io.WriteIn(3) <> st_26.io.memReq

  st_26.io.memResp <> MemCtrl.io.WriteOut(3)

  MemCtrl.io.ReadIn(4) <> ld_33.io.memReq

  ld_33.io.memResp <> MemCtrl.io.ReadOut(4)

  MemCtrl.io.ReadIn(5) <> ld_34.io.memReq

  ld_34.io.memResp <> MemCtrl.io.ReadOut(5)

  MemCtrl.io.ReadIn(6) <> ld_37.io.memReq

  ld_37.io.memResp <> MemCtrl.io.ReadOut(6)

  MemCtrl.io.ReadIn(7) <> ld_38.io.memReq

  ld_38.io.memResp <> MemCtrl.io.ReadOut(7)

  MemCtrl.io.WriteIn(4) <> st_44.io.memReq

  st_44.io.memResp <> MemCtrl.io.WriteOut(4)

  MemCtrl.io.WriteIn(5) <> st_45.io.memReq

  st_45.io.memResp <> MemCtrl.io.WriteOut(5)



  /* ================================================================== *
   *                   PRINT SHARED CONNECTIONS                         *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  philog_01161.io.InData(0) <> const0.io.Out

  phispan_01142.io.InData(0) <> const1.io.Out

  icmp_cmp1123.io.RightIO <> const2.io.Out

  binaryOp_and28.io.RightIO <> const3.io.Out

  icmp_tobool2729.io.RightIO <> const4.io.Out

  binaryOp_inc47.io.RightIO <> const5.io.Out

  icmp_cmp48.io.RightIO <> const6.io.Out

  binaryOp_shr51.io.RightIO <> const7.io.Out

  binaryOp_inc5452.io.RightIO <> const8.io.Out

  icmp_exitcond53.io.RightIO <> const9.io.Out

  binaryOp_inc5452.io.LeftIO <> philog_01161.io.Out(1)

  icmp_cmp1123.io.LeftIO <> phispan_01142.io.Out(0)

  binaryOp_shr51.io.LeftIO <> phispan_01142.io.Out(2)

  br_4.io.CmpIO <> icmp_cmp1123.io.Out(0)

  binaryOp_or7.io.LeftIO <> phiodd_01136.io.Out(0)

  binaryOp_xor8.io.LeftIO <> binaryOp_or7.io.Out(0)

  sextidxprom312.io.Input <> binaryOp_or7.io.Out(1)

  binaryOp_inc47.io.LeftIO <> binaryOp_or7.io.Out(2)

  sextidxprom9.io.Input <> binaryOp_xor8.io.Out(0)

  binaryOp_shl27.io.LeftIO <> binaryOp_xor8.io.Out(1)

  Gep_arrayidx10.io.idx(0) <> sextidxprom9.io.Out

  Gep_arrayidx1419.io.idx(0) <> sextidxprom9.io.Out

  ld_11.io.GepAddr <> Gep_arrayidx10.io.Out(0)

  st_18.io.GepAddr <> Gep_arrayidx10.io.Out(1)

  FP_add15.io.LeftIO <> ld_11.io.Out(0)

  FP_sub16.io.LeftIO <> ld_11.io.Out(1)

  Gep_arrayidx413.io.idx(0) <> sextidxprom312.io.Out

  Gep_arrayidx1621.io.idx(0) <> sextidxprom312.io.Out

  ld_14.io.GepAddr <> Gep_arrayidx413.io.Out(0)

  st_17.io.GepAddr <> Gep_arrayidx413.io.Out(1)

  ld_34.io.GepAddr <> Gep_arrayidx413.io.Out(2)

  st_45.io.GepAddr <> Gep_arrayidx413.io.Out(3)

  FP_add15.io.RightIO <> ld_14.io.Out(0)

  FP_sub16.io.RightIO <> ld_14.io.Out(1)

  st_18.io.inData <> FP_add15.io.Out(0)

  st_17.io.inData <> FP_sub16.io.Out(0)

  ld_20.io.GepAddr <> Gep_arrayidx1419.io.Out(0)

  st_26.io.GepAddr <> Gep_arrayidx1419.io.Out(1)

  FP_add1723.io.LeftIO <> ld_20.io.Out(0)

  FP_sub2224.io.LeftIO <> ld_20.io.Out(1)

  ld_22.io.GepAddr <> Gep_arrayidx1621.io.Out(0)

  st_25.io.GepAddr <> Gep_arrayidx1621.io.Out(1)

  ld_38.io.GepAddr <> Gep_arrayidx1621.io.Out(2)

  st_44.io.GepAddr <> Gep_arrayidx1621.io.Out(3)

  FP_add1723.io.RightIO <> ld_22.io.Out(0)

  FP_sub2224.io.RightIO <> ld_22.io.Out(1)

  st_26.io.inData <> FP_add1723.io.Out(0)

  st_25.io.inData <> FP_sub2224.io.Out(0)

  binaryOp_and28.io.LeftIO <> binaryOp_shl27.io.Out(0)

  icmp_tobool2729.io.LeftIO <> binaryOp_and28.io.Out(0)

  sextidxprom2811131.io.Input <> binaryOp_and28.io.Out(1)

  br_30.io.CmpIO <> icmp_tobool2729.io.Out(0)

  Gep_arrayidx2932.io.idx(0) <> sextidxprom2811131.io.Out

  Gep_arrayidx3336.io.idx(0) <> sextidxprom2811131.io.Out

  ld_33.io.GepAddr <> Gep_arrayidx2932.io.Out(0)

  FP_mul35.io.LeftIO <> ld_33.io.Out(0)

  FP_mul4241.io.LeftIO <> ld_33.io.Out(1)

  FP_mul35.io.RightIO <> ld_34.io.Out(0)

  FP_mul4742.io.LeftIO <> ld_34.io.Out(1)

  FP_sub3740.io.LeftIO <> FP_mul35.io.Out(0)

  ld_37.io.GepAddr <> Gep_arrayidx3336.io.Out(0)

  FP_mul3639.io.LeftIO <> ld_37.io.Out(0)

  FP_mul4742.io.RightIO <> ld_37.io.Out(1)

  FP_mul3639.io.RightIO <> ld_38.io.Out(0)

  FP_mul4241.io.RightIO <> ld_38.io.Out(1)

  FP_sub3740.io.RightIO <> FP_mul3639.io.Out(0)

  st_45.io.inData <> FP_sub3740.io.Out(0)

  FP_add4843.io.RightIO <> FP_mul4241.io.Out(0)

  FP_add4843.io.LeftIO <> FP_mul4742.io.Out(0)

  st_44.io.inData <> FP_add4843.io.Out(0)

  phiodd_01136.io.InData(0) <> binaryOp_inc47.io.Out(0)

  icmp_cmp48.io.LeftIO <> binaryOp_inc47.io.Out(1)

  br_49.io.CmpIO <> icmp_cmp48.io.Out(0)

  phispan_01142.io.InData(1) <> binaryOp_shr51.io.Out(0)

  philog_01161.io.InData(1) <> binaryOp_inc5452.io.Out(0)

  icmp_exitcond53.io.LeftIO <> binaryOp_inc5452.io.Out(1)

  br_54.io.CmpIO <> icmp_exitcond53.io.Out(0)

  st_17.io.Out(0).ready := true.B

  st_18.io.Out(0).ready := true.B

  st_25.io.Out(0).ready := true.B

  st_26.io.Out(0).ready := true.B

  st_44.io.Out(0).ready := true.B

  st_45.io.Out(0).ready := true.B



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_55.io.Out

}

import java.io.{File, FileWriter}
object fftMain extends App {
  val dir = new File("RTL/fft") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new fftDF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}
