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

abstract class bbgemmDFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32, 32, 32))))
    val MemResp = Flipped(Valid(new MemResp))
    val MemReq = Decoupled(new MemReq)
    val out = Decoupled(new Call(List()))
  })
}

class bbgemmDF(implicit p: Parameters) extends bbgemmDFIO()(p) {


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  val MemCtrl = Module(new UnifiedController(ID=0, Size=32, NReads=3, NWrites=2)
		 (WControl=new WriteMemoryController(NumOps=2, BaseSize=2, NumEntries=2))
		 (RControl=new ReadMemoryController(NumOps=3, BaseSize=2, NumEntries=2))
		 (RWArbiter=new ReadWriteArbiter()))

  io.MemReq <> MemCtrl.io.MemReq
  MemCtrl.io.MemResp <> io.MemResp

  val InputSplitter = Module(new SplitCallNew(List(1,1,1)))
  InputSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val Loop_0 = Module(new LoopBlock(NumIns=List(1,1,1,1,1), NumOuts = 0, NumExits=1, ID = 0))

  val Loop_1 = Module(new LoopBlock(NumIns=List(1,1,1,1,1,1,1), NumOuts = 0, NumExits=1, ID = 1))

  val Loop_2 = Module(new LoopBlock(NumIns=List(2,2,1,1,1), NumOuts = 0, NumExits=1, ID = 2))

  val Loop_3 = Module(new LoopBlock(NumIns=List(1,1,1,1), NumOuts = 0, NumExits=1, ID = 3))

  val Loop_4 = Module(new LoopBlock(NumIns=List(1,1,1), NumOuts = 0, NumExits=1, ID = 4))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_entry0 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 0))

  val bb_for_cond1_preheader1 = Module(new LoopHead(NumOuts = 3, NumPhi=1, BID = 1))

  val bb_for_cond4_preheader2 = Module(new LoopHead(NumOuts = 3, NumPhi=1, BID = 2))

  val bb_for_cond7_preheader3 = Module(new LoopHead(NumOuts = 7, NumPhi=1, BID = 3))

  val bb_for_body94 = Module(new LoopHead(NumOuts = 10, NumPhi=1, BID = 4))

  val bb_for_body165 = Module(new LoopHead(NumOuts = 16, NumPhi=1, BID = 5))

  val bb_for_inc256 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 5, BID = 6))

  val bb_for_inc287 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 5, BID = 7))

  val bb_for_inc318 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 5, BID = 8))

  val bb_for_inc349 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 5, BID = 9))

  val bb_for_end3610 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 10))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  br label %for.cond1.preheader
  val br_0 = Module(new UBranchNode(ID = 0))

  //  %jj.064 = phi i32 [ 0, %entry ], [ %add35, %for.inc34 ]
  val phi_jj_0641 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 1))

  //  br label %for.cond4.preheader
  val br_2 = Module(new UBranchNode(ID = 2))

  //  %kk.063 = phi i32 [ 0, %for.cond1.preheader ], [ %add32, %for.inc31 ]
  val phi_kk_0633 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 3))

  //  br label %for.cond7.preheader
  val br_4 = Module(new UBranchNode(ID = 4))

  //  %i.062 = phi i32 [ 0, %for.cond4.preheader ], [ %inc29, %for.inc28 ]
  val phi_i_0625 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 5))

  //  %mul10 = shl nsw i32 %i.062, 6
  val binaryOp_mul106 = Module(new ComputeNode(NumOuts = 2, ID = 6, opCode = "shl")(sign=false))

  //  %add12 = add nuw nsw i32 %mul10, %kk.063
  val binaryOp_add127 = Module(new ComputeNode(NumOuts = 1, ID = 7, opCode = "add")(sign=false))

  //  %add21 = add nuw nsw i32 %mul10, %jj.064
  val binaryOp_add218 = Module(new ComputeNode(NumOuts = 1, ID = 8, opCode = "add")(sign=false))

  //  br label %for.body9
  val br_9 = Module(new UBranchNode(ID = 9))

  //  %k.061 = phi i32 [ 0, %for.cond7.preheader ], [ %inc26, %for.inc25 ]
  val phi_k_06110 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 3, ID = 10))

  //  %add = add nuw nsw i32 %k.061, %kk.063
  val binaryOp_add11 = Module(new ComputeNode(NumOuts = 1, ID = 11, opCode = "add")(sign=false))

  //  %mul11 = shl i32 %add, 6
  val binaryOp_mul1112 = Module(new ComputeNode(NumOuts = 1, ID = 12, opCode = "shl")(sign=false))

  //  %add13 = add nuw nsw i32 %add12, %k.061
  val binaryOp_add1313 = Module(new ComputeNode(NumOuts = 1, ID = 13, opCode = "add")(sign=false))

  //  %arrayidx = getelementptr inbounds double, double* %m1, i32 %add13
  val Gep_arrayidx14 = Module(new GepNode(NumIns = 1, NumOuts=1, ID=14)(ElementSize = 8, ArraySize = List()))

  //  %0 = load double, double* %arrayidx, align 4, !tbaa !2
  val ld_15 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=15, RouteID=0))

  //  %add17 = add nuw nsw i32 %mul11, %jj.064
  val binaryOp_add1716 = Module(new ComputeNode(NumOuts = 1, ID = 16, opCode = "add")(sign=false))

  //  br label %for.body16
  val br_17 = Module(new UBranchNode(ID = 17))

  //  %j.060 = phi i32 [ 0, %for.body9 ], [ %inc, %for.body16 ]
  val phi_j_06018 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 3, ID = 18))

  //  %add18 = add nuw nsw i32 %add17, %j.060
  val binaryOp_add1819 = Module(new ComputeNode(NumOuts = 1, ID = 19, opCode = "add")(sign=false))

  //  %arrayidx19 = getelementptr inbounds double, double* %m2, i32 %add18
  val Gep_arrayidx1920 = Module(new GepNode(NumIns = 1, NumOuts=1, ID=20)(ElementSize = 8, ArraySize = List()))

  //  %1 = load double, double* %arrayidx19, align 4, !tbaa !2
  val ld_21 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=21, RouteID=1))

  //  %mul20 = fmul double %0, %1
  val binaryOp_mul2022 = Module(new FPComputeNode(NumOuts = 1, ID = 22, opCode = "mul")(t = p(FTYP)))

  //  %add22 = add nuw nsw i32 %add21, %j.060
  val binaryOp_add2223 = Module(new ComputeNode(NumOuts = 1, ID = 23, opCode = "add")(sign=false))

  //  %arrayidx23 = getelementptr inbounds double, double* %prod, i32 %add22
  val Gep_arrayidx2324 = Module(new GepNode(NumIns = 1, NumOuts=2, ID=24)(ElementSize = 8, ArraySize = List()))

  //  %2 = load double, double* %arrayidx23, align 4, !tbaa !2
  val ld_25 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=25, RouteID=2))

  //  %add24 = fadd double %2, %mul20
  val FP_add2426 = Module(new FPComputeNode(NumOuts = 1, ID = 26, opCode = "add")(t = p(FTYP)))

  //  store double %add24, double* %arrayidx23, align 4, !tbaa !2
  val st_27 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, ID=27, RouteID=0))

  //  %inc = add nuw nsw i32 %j.060, 1
  val binaryOp_inc28 = Module(new ComputeNode(NumOuts = 2, ID = 28, opCode = "add")(sign=false))

  //  %exitcond = icmp eq i32 %inc, 8
  val icmp_exitcond29 = Module(new IcmpNode(NumOuts = 1, ID = 29, opCode = "eq")(sign=false))

  //  br i1 %exitcond, label %for.inc25, label %for.body16
  val br_30 = Module(new CBranchNode(ID = 30))
  val tmp = Module(new UBranchNode(NumOuts = 2, ID = 30))

  //  %inc26 = add nuw nsw i32 %k.061, 1
  val binaryOp_inc2631 = Module(new ComputeNode(NumOuts = 2, ID = 31, opCode = "add")(sign=false))

  //  %exitcond65 = icmp eq i32 %inc26, 8
  val icmp_exitcond6532 = Module(new IcmpNode(NumOuts = 1, ID = 32, opCode = "eq")(sign=false))

  //  br i1 %exitcond65, label %for.inc28, label %for.body9
  //  val br_33 = Module(new CBranchFastNodeVariable(NumTrue = 1, NumFalse = 2, ID = 33))
  val br_33 = Module(new CBranchNode(ID = 33))
  val tmp2 = Module(new UBranchNode(NumOuts = 2, ID = 30))

  //  %inc29 = add nuw nsw i32 %i.062, 1
  val binaryOp_inc2934 = Module(new ComputeNode(NumOuts = 2, ID = 34, opCode = "add")(sign=false))

  //  %exitcond66 = icmp eq i32 %inc29, 64
  val icmp_exitcond6635 = Module(new IcmpNode(NumOuts = 1, ID = 35, opCode = "eq")(sign=false))

  //  br i1 %exitcond66, label %for.inc31, label %for.cond7.preheader
  val br_36 = Module(new CBranchNode(ID = 36))
  val tmp3 = Module(new UBranchNode(NumOuts = 2, ID = 30))

  //  %add32 = add nuw nsw i32 %kk.063, 8
  val binaryOp_add3237 = Module(new ComputeNode(NumOuts = 2, ID = 37, opCode = "add")(sign=false))

  //  %cmp2 = icmp slt i32 %add32, 64
  val icmp_cmp238 = Module(new IcmpNode(NumOuts = 1, ID = 38, opCode = "ult")(sign=false))

  //  br i1 %cmp2, label %for.cond4.preheader, label %for.inc34
  //  val br_39 = Module(new CBranchFastNodeVariable(NumTrue = 2, NumFalse = 1, ID = 39))
  val br_39 = Module(new CBranchNode(ID = 39))
  val tmp4 = Module(new UBranchNode(NumOuts = 2, ID = 30))

  //  %add35 = add nuw nsw i32 %jj.064, 8
  val binaryOp_add3540 = Module(new ComputeNode(NumOuts = 2, ID = 40, opCode = "add")(sign=false))

  //  %cmp = icmp slt i32 %add35, 64
  val icmp_cmp41 = Module(new IcmpNode(NumOuts = 1, ID = 41, opCode = "ult")(sign=false))

  //  br i1 %cmp, label %for.cond1.preheader, label %for.end36
  val br_42 = Module(new CBranchNode(ID = 42))
  val tmp5 = Module(new UBranchNode(NumOuts = 2, ID = 30))

  //  ret void
  val ret_43 = Module(new RetNode2(retTypes=List(), ID = 43))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i32 0
  val const0 = Module(new ConstFastNode(value = 0, ID = 0))

  //i32 0
  val const1 = Module(new ConstFastNode(value = 0, ID = 1))

  //i32 0
  val const2 = Module(new ConstFastNode(value = 0, ID = 2))

  //i32 6
  val const3 = Module(new ConstFastNode(value = 6, ID = 3))

  //i32 0
  val const4 = Module(new ConstFastNode(value = 0, ID = 4))

  //i32 6
  val const5 = Module(new ConstFastNode(value = 6, ID = 5))

  //i32 0
  val const6 = Module(new ConstFastNode(value = 0, ID = 6))

  //i32 1
  val const7 = Module(new ConstFastNode(value = 1, ID = 7))

  //i32 8
  val const8 = Module(new ConstFastNode(value = 8, ID = 8))

  //i32 1
  val const9 = Module(new ConstFastNode(value = 1, ID = 9))

  //i32 8
  val const10 = Module(new ConstFastNode(value = 8, ID = 10))

  //i32 1
  val const11 = Module(new ConstFastNode(value = 1, ID = 11))

  //i32 64
  val const12 = Module(new ConstFastNode(value = 64, ID = 12))

  //i32 8
  val const13 = Module(new ConstFastNode(value = 8, ID = 13))

  //i32 64
  val const14 = Module(new ConstFastNode(value = 64, ID = 14))

  //i32 8
  val const15 = Module(new ConstFastNode(value = 8, ID = 15))

  //i32 64
  val const16 = Module(new ConstFastNode(value = 64, ID = 16))



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_entry0.io.predicateIn <> InputSplitter.io.Out.enable

  bb_for_cond1_preheader1.io.activate <> Loop_4.io.activate

  tmp5.io.enable <> br_42.io.Out(0)
  bb_for_cond1_preheader1.io.loopBack <> tmp5.io.Out(0)

  bb_for_cond4_preheader2.io.activate <> Loop_3.io.activate

  tmp4.io.enable <> br_39.io.Out(0)
  bb_for_cond4_preheader2.io.loopBack <> tmp4.io.Out(0)

  bb_for_cond7_preheader3.io.activate <> Loop_2.io.activate

  tmp3.io.enable <> br_36.io.Out(1)
  bb_for_cond7_preheader3.io.loopBack <> tmp3.io.Out(0)

  bb_for_body94.io.activate <> Loop_1.io.activate

  tmp2.io.enable <> br_33.io.Out(1)
  bb_for_body94.io.loopBack <> tmp2.io.Out(0)

  bb_for_body165.io.activate <> Loop_0.io.activate

  tmp.io.enable <> br_30.io.Out(1)
  bb_for_body165.io.loopBack <> tmp.io.Out(0)

  bb_for_inc256.io.predicateIn <> Loop_0.io.endEnable

  bb_for_inc287.io.predicateIn <> Loop_1.io.endEnable

  bb_for_inc318.io.predicateIn <> Loop_2.io.endEnable

  bb_for_inc349.io.predicateIn <> Loop_3.io.endEnable

  bb_for_end3610.io.predicateIn <> Loop_4.io.endEnable



  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */

  Loop_0.io.enable <> br_17.io.Out(0)

  Loop_0.io.latchEnable <> tmp.io.Out(1)

  Loop_0.io.loopExit(0) <> br_30.io.Out(0)

  Loop_1.io.enable <> br_9.io.Out(0)

  //  Loop_1.io.latchEnable <> br_33.io.FalseOutput(1)
  Loop_1.io.latchEnable <> tmp2.io.Out(1)

  Loop_1.io.loopExit(0) <> br_33.io.Out(0)

  Loop_2.io.enable <> br_4.io.Out(0)

  Loop_2.io.latchEnable <> tmp3.io.Out(1)

  Loop_2.io.loopExit(0) <> br_36.io.Out(0)

  Loop_3.io.enable <> br_2.io.Out(0)

  Loop_3.io.latchEnable <> tmp4.io.Out(1)

  Loop_3.io.loopExit(0) <> br_39.io.Out(1)

  Loop_4.io.enable <> br_0.io.Out(0)

  Loop_4.io.latchEnable <> tmp5.io.Out(1)

  Loop_4.io.loopExit(0) <> br_42.io.Out(1)



  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

  Loop_0.io.In(0) <> binaryOp_add1716.io.Out(0)

  Loop_0.io.In(1) <> Loop_1.io.liveIn.data("field4")(0)

  Loop_0.io.In(2) <> ld_15.io.Out.data(0)

  Loop_0.io.In(3) <> Loop_1.io.liveIn.data("field5")(0)

  Loop_0.io.In(4) <> Loop_1.io.liveIn.data("field6")(0)

  Loop_1.io.In(0) <> Loop_2.io.liveIn.data("field0")(1)

  Loop_1.io.In(1) <> binaryOp_add127.io.Out(0)

  Loop_1.io.In(2) <> Loop_2.io.liveIn.data("field2")(0)

  Loop_1.io.In(3) <> Loop_2.io.liveIn.data("field1")(1)

  Loop_1.io.In(4) <> Loop_2.io.liveIn.data("field3")(0)

  Loop_1.io.In(5) <> binaryOp_add218.io.Out(0)

  Loop_1.io.In(6) <> Loop_2.io.liveIn.data("field4")(0)

  Loop_2.io.In(0) <> phi_kk_0633.io.Out(0)

  Loop_2.io.In(1) <> Loop_3.io.liveIn.data("field0")(0)

  Loop_2.io.In(2) <> Loop_3.io.liveIn.data("field1")(0)

  Loop_2.io.In(3) <> Loop_3.io.liveIn.data("field2")(0)

  Loop_2.io.In(4) <> Loop_3.io.liveIn.data("field3")(0)

  Loop_3.io.In(0) <> phi_jj_0641.io.Out(0)

  Loop_3.io.In(1) <> Loop_4.io.liveIn.data("field0")(0)

  Loop_3.io.In(2) <> Loop_4.io.liveIn.data("field1")(0)

  Loop_3.io.In(3) <> Loop_4.io.liveIn.data("field2")(0)

  Loop_4.io.In(0) <> InputSplitter.io.Out.data("field0")(0)

  Loop_4.io.In(1) <> InputSplitter.io.Out.data("field1")(0)

  Loop_4.io.In(2) <> InputSplitter.io.Out.data("field2")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */

  binaryOp_add1819.io.LeftIO <> Loop_0.io.liveIn.data("field0")(0)

  Gep_arrayidx1920.io.baseAddress <> Loop_0.io.liveIn.data("field1")(0)

  binaryOp_mul2022.io.LeftIO <> Loop_0.io.liveIn.data("field2")(0)

  binaryOp_add2223.io.LeftIO <> Loop_0.io.liveIn.data("field3")(0)

  Gep_arrayidx2324.io.baseAddress <> Loop_0.io.liveIn.data("field4")(0)

  binaryOp_add11.io.RightIO <> Loop_1.io.liveIn.data("field0")(0)

  binaryOp_add1313.io.LeftIO <> Loop_1.io.liveIn.data("field1")(0)

  Gep_arrayidx14.io.baseAddress <> Loop_1.io.liveIn.data("field2")(0)

  binaryOp_add1716.io.RightIO <> Loop_1.io.liveIn.data("field3")(0)

  binaryOp_add127.io.RightIO <> Loop_2.io.liveIn.data("field0")(0)

  binaryOp_add218.io.RightIO <> Loop_2.io.liveIn.data("field1")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  br_0.io.enable <> bb_entry0.io.Out(0)


  const0.io.enable <> bb_for_cond1_preheader1.io.Out(0)

  phi_jj_0641.io.enable <> bb_for_cond1_preheader1.io.Out(1)

  br_2.io.enable <> bb_for_cond1_preheader1.io.Out(2)


  const1.io.enable <> bb_for_cond4_preheader2.io.Out(0)

  phi_kk_0633.io.enable <> bb_for_cond4_preheader2.io.Out(1)

  br_4.io.enable <> bb_for_cond4_preheader2.io.Out(2)


  const2.io.enable <> bb_for_cond7_preheader3.io.Out(0)

  const3.io.enable <> bb_for_cond7_preheader3.io.Out(1)

  phi_i_0625.io.enable <> bb_for_cond7_preheader3.io.Out(2)

  binaryOp_mul106.io.enable <> bb_for_cond7_preheader3.io.Out(3)

  binaryOp_add127.io.enable <> bb_for_cond7_preheader3.io.Out(4)

  binaryOp_add218.io.enable <> bb_for_cond7_preheader3.io.Out(5)

  br_9.io.enable <> bb_for_cond7_preheader3.io.Out(6)


  const4.io.enable <> bb_for_body94.io.Out(0)

  const5.io.enable <> bb_for_body94.io.Out(1)

  phi_k_06110.io.enable <> bb_for_body94.io.Out(2)

  binaryOp_add11.io.enable <> bb_for_body94.io.Out(3)

  binaryOp_mul1112.io.enable <> bb_for_body94.io.Out(4)

  binaryOp_add1313.io.enable <> bb_for_body94.io.Out(5)

  Gep_arrayidx14.io.enable <> bb_for_body94.io.Out(6)

  ld_15.io.enable <> bb_for_body94.io.Out(7)

  binaryOp_add1716.io.enable <> bb_for_body94.io.Out(8)

  br_17.io.enable <> bb_for_body94.io.Out(9)


  const6.io.enable <> bb_for_body165.io.Out(0)

  const7.io.enable <> bb_for_body165.io.Out(1)

  const8.io.enable <> bb_for_body165.io.Out(2)

  phi_j_06018.io.enable <> bb_for_body165.io.Out(3)

  binaryOp_add1819.io.enable <> bb_for_body165.io.Out(4)

  Gep_arrayidx1920.io.enable <> bb_for_body165.io.Out(5)

  ld_21.io.enable <> bb_for_body165.io.Out(6)

  binaryOp_mul2022.io.enable <> bb_for_body165.io.Out(7)

  binaryOp_add2223.io.enable <> bb_for_body165.io.Out(8)

  Gep_arrayidx2324.io.enable <> bb_for_body165.io.Out(9)

  ld_25.io.enable <> bb_for_body165.io.Out(10)

  FP_add2426.io.enable <> bb_for_body165.io.Out(11)

  st_27.io.enable <> bb_for_body165.io.Out(12)

  binaryOp_inc28.io.enable <> bb_for_body165.io.Out(13)

  icmp_exitcond29.io.enable <> bb_for_body165.io.Out(14)

  br_30.io.enable <> bb_for_body165.io.Out(15)


  const9.io.enable <> bb_for_inc256.io.Out(0)

  const10.io.enable <> bb_for_inc256.io.Out(1)

  binaryOp_inc2631.io.enable <> bb_for_inc256.io.Out(2)

  icmp_exitcond6532.io.enable <> bb_for_inc256.io.Out(3)

  br_33.io.enable <> bb_for_inc256.io.Out(4)


  const11.io.enable <> bb_for_inc287.io.Out(0)

  const12.io.enable <> bb_for_inc287.io.Out(1)

  binaryOp_inc2934.io.enable <> bb_for_inc287.io.Out(2)

  icmp_exitcond6635.io.enable <> bb_for_inc287.io.Out(3)

  br_36.io.enable <> bb_for_inc287.io.Out(4)


  const13.io.enable <> bb_for_inc318.io.Out(0)

  const14.io.enable <> bb_for_inc318.io.Out(1)

  binaryOp_add3237.io.enable <> bb_for_inc318.io.Out(2)

  icmp_cmp238.io.enable <> bb_for_inc318.io.Out(3)

  br_39.io.enable <> bb_for_inc318.io.Out(4)


  const15.io.enable <> bb_for_inc349.io.Out(0)

  const16.io.enable <> bb_for_inc349.io.Out(1)

  binaryOp_add3540.io.enable <> bb_for_inc349.io.Out(2)

  icmp_cmp41.io.enable <> bb_for_inc349.io.Out(3)

  br_42.io.enable <> bb_for_inc349.io.Out(4)


  ret_43.io.In.enable <> bb_for_end3610.io.Out(0)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  phi_jj_0641.io.Mask <> bb_for_cond1_preheader1.io.MaskBB(0)

  phi_kk_0633.io.Mask <> bb_for_cond4_preheader2.io.MaskBB(0)

  phi_i_0625.io.Mask <> bb_for_cond7_preheader3.io.MaskBB(0)

  phi_k_06110.io.Mask <> bb_for_body94.io.MaskBB(0)

    phi_j_06018.io.Mask <> bb_for_body165.io.MaskBB(0)
//  phi_j_06018.io.Mask.bits <> Reverse(bb_for_body165.io.MaskBB(0).bits)
//  phi_j_06018.io.Mask.valid <> bb_for_body165.io.MaskBB(0).valid
//  bb_for_body165.io.MaskBB(0).ready <> phi_j_06018.io.Mask.ready



  /* ================================================================== *
   *                   PRINT ALLOCA OFFSET                              *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */

  MemCtrl.io.ReadIn(0) <> ld_15.io.memReq

  ld_15.io.memResp <> MemCtrl.io.ReadOut(0)

  MemCtrl.io.ReadIn(1) <> ld_21.io.memReq

  ld_21.io.memResp <> MemCtrl.io.ReadOut(1)

  MemCtrl.io.ReadIn(2) <> ld_25.io.memReq

  ld_25.io.memResp <> MemCtrl.io.ReadOut(2)

  MemCtrl.io.WriteIn(0) <> st_27.io.memReq

  st_27.io.memResp <> MemCtrl.io.WriteOut(0)



  /* ================================================================== *
   *                   PRINT SHARED CONNECTIONS                         *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  phi_jj_0641.io.InData(0) <> const0.io.Out

  phi_kk_0633.io.InData(0) <> const1.io.Out

  phi_i_0625.io.InData(0) <> const2.io.Out

  binaryOp_mul106.io.RightIO <> const3.io.Out

  phi_k_06110.io.InData(0) <> const4.io.Out

  binaryOp_mul1112.io.RightIO <> const5.io.Out

  phi_j_06018.io.InData(0) <> const6.io.Out

  binaryOp_inc28.io.RightIO <> const7.io.Out

  icmp_exitcond29.io.RightIO <> const8.io.Out

  binaryOp_inc2631.io.RightIO <> const9.io.Out

  icmp_exitcond6532.io.RightIO <> const10.io.Out

  binaryOp_inc2934.io.RightIO <> const11.io.Out

  icmp_exitcond6635.io.RightIO <> const12.io.Out

  binaryOp_add3237.io.RightIO <> const13.io.Out

  icmp_cmp238.io.RightIO <> const14.io.Out

  binaryOp_add3540.io.RightIO <> const15.io.Out

  icmp_cmp41.io.RightIO <> const16.io.Out

  binaryOp_add3540.io.LeftIO <> phi_jj_0641.io.Out(1)

  binaryOp_add3237.io.LeftIO <> phi_kk_0633.io.Out(1)

  binaryOp_mul106.io.LeftIO <> phi_i_0625.io.Out(0)

  binaryOp_inc2934.io.LeftIO <> phi_i_0625.io.Out(1)

  binaryOp_add127.io.LeftIO <> binaryOp_mul106.io.Out(0)

  binaryOp_add218.io.LeftIO <> binaryOp_mul106.io.Out(1)

  binaryOp_add11.io.LeftIO <> phi_k_06110.io.Out(0)

  binaryOp_add1313.io.RightIO <> phi_k_06110.io.Out(1)

  binaryOp_inc2631.io.LeftIO <> phi_k_06110.io.Out(2)

  binaryOp_mul1112.io.LeftIO <> binaryOp_add11.io.Out(0)

  binaryOp_add1716.io.LeftIO <> binaryOp_mul1112.io.Out(0)

  Gep_arrayidx14.io.idx(0) <> binaryOp_add1313.io.Out(0)

  ld_15.io.GepAddr <> Gep_arrayidx14.io.Out(0)

  binaryOp_add1819.io.RightIO <> phi_j_06018.io.Out(0)

  binaryOp_add2223.io.RightIO <> phi_j_06018.io.Out(1)

  binaryOp_inc28.io.LeftIO <> phi_j_06018.io.Out(2)

  Gep_arrayidx1920.io.idx(0) <> binaryOp_add1819.io.Out(0)

  ld_21.io.GepAddr <> Gep_arrayidx1920.io.Out(0)

  binaryOp_mul2022.io.RightIO <> ld_21.io.Out(0)

  FP_add2426.io.RightIO <> binaryOp_mul2022.io.Out(0)

  Gep_arrayidx2324.io.idx(0) <> binaryOp_add2223.io.Out(0)

  ld_25.io.GepAddr <> Gep_arrayidx2324.io.Out(0)

  st_27.io.GepAddr <> Gep_arrayidx2324.io.Out(1)

  FP_add2426.io.LeftIO <> ld_25.io.Out(0)

  st_27.io.inData <> FP_add2426.io.Out(0)

  phi_j_06018.io.InData(1) <> binaryOp_inc28.io.Out(0)

  icmp_exitcond29.io.LeftIO <> binaryOp_inc28.io.Out(1)

  br_30.io.CmpIO <> icmp_exitcond29.io.Out(0)

  phi_k_06110.io.InData(1) <> binaryOp_inc2631.io.Out(0)

  icmp_exitcond6532.io.LeftIO <> binaryOp_inc2631.io.Out(1)

  br_33.io.CmpIO <> icmp_exitcond6532.io.Out(0)

  phi_i_0625.io.InData(1) <> binaryOp_inc2934.io.Out(0)

  icmp_exitcond6635.io.LeftIO <> binaryOp_inc2934.io.Out(1)

  br_36.io.CmpIO <> icmp_exitcond6635.io.Out(0)

  phi_kk_0633.io.InData(1) <> binaryOp_add3237.io.Out(0)

  icmp_cmp238.io.LeftIO <> binaryOp_add3237.io.Out(1)

  br_39.io.CmpIO <> icmp_cmp238.io.Out(0)

  phi_jj_0641.io.InData(1) <> binaryOp_add3540.io.Out(0)

  icmp_cmp41.io.LeftIO <> binaryOp_add3540.io.Out(1)

  br_42.io.CmpIO <> icmp_cmp41.io.Out(0)

  st_27.io.Out(0).ready := true.B



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_43.io.Out

}

import java.io.{File, FileWriter}
object bbgemmMain extends App {
  val dir = new File("RTL/bbgemm") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new bbgemmDF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}
