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

abstract class test15DFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32, 32))))
    val MemResp = Flipped(Valid(new MemResp))
    val MemReq = Decoupled(new MemReq)
    val out = Decoupled(new Call(List(32)))
  })
}

class test15DF(implicit p: Parameters) extends test15DFIO()(p) {


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  val MemCtrl = Module(new UnifiedController(ID=0, Size=32, NReads=3, NWrites=4)
		 (WControl=new WriteMemoryController(NumOps=4, BaseSize=2, NumEntries=2))
		 (RControl=new ReadMemoryController(NumOps=3, BaseSize=2, NumEntries=2))
		 (RWArbiter=new ReadWriteArbiter()))

  io.MemReq <> MemCtrl.io.MemReq
  MemCtrl.io.MemResp <> io.MemResp

  val InputSplitter = Module(new SplitCallNew(List(2,3)))
  InputSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val Loop_0 = Module(new LoopBlock(NumIns=List(1), NumOuts = 2, NumExits=1, ID = 0))

  val Loop_1 = Module(new LoopBlock(NumIns=List(1,1), NumOuts = 0, NumExits=1, ID = 1))

  val Loop_2 = Module(new LoopBlock(NumIns=List(1,2,2), NumOuts = 2, NumExits=1, ID = 2))

  val Loop_3 = Module(new LoopBlock(NumIns=List(1,1,2), NumOuts = 1, NumExits=1, ID = 3))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_entry0 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 6, BID = 0))

  val bb_for_cond5_preheader_us_us_preheader_preheader1 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 1))

  val bb_for_cond1_preheader_preheader2 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 2, BID = 2))

  val bb_for_cond5_preheader_us_us_preheader3 = Module(new LoopHead(NumOuts = 5, NumPhi=2, BID = 3))

  val bb_for_cond1_for_cond_cleanup3_crit_edge_us4 = Module(new BasicBlockNode(NumInputs = 1, NumOuts = 11, NumPhi=2, BID = 4))

  val bb_for_cond5_preheader_us_us5 = Module(new LoopHead(NumOuts = 3, NumPhi=1, BID = 5))

  val bb_for_cond5_for_cond_cleanup7_crit_edge_us_us6 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 8, BID = 6))

  val bb_for_body8_us_us7 = Module(new LoopHead(NumOuts = 11, NumPhi=1, BID = 7))

  val bb_for_cond1_preheader8 = Module(new LoopHead(NumOuts = 13, NumPhi=3, BID = 8))

  val bb_for_cond_cleanup_loopexit9 = Module(new BasicBlockNode(NumInputs = 1, NumOuts = 4, NumPhi=2, BID = 9))

  val bb_for_cond_cleanup_loopexit6810 = Module(new BasicBlockNode(NumInputs = 1, NumOuts = 2, NumPhi=1, BID = 10))

  val bb_for_cond_cleanup11 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 4, NumPhi=1, BID = 11))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  %cmp240 = icmp eq i32 %n, 0
  val icmp_cmp2400 = Module(new IcmpNode(NumOuts = 1, ID = 0, opCode = "eq")(sign=false))

  //  %sub = add i32 %n, -1
  val binaryOp_sub1 = Module(new ComputeNode(NumOuts = 1, ID = 1, opCode = "add")(sign=false))

  //  %arrayidx10 = getelementptr inbounds i32, i32* %a, i32 %sub
  val Gep_arrayidx102 = Module(new GepNode(NumIns = 1, NumOuts=3, ID=2)(ElementSize = 4, ArraySize = List()))

  //  br i1 %cmp240, label %for.cond1.preheader.preheader, label %for.cond5.preheader.us.us.preheader.preheader
  val br_3 = Module(new CBranchNode(ID = 3))

  //  br label %for.cond5.preheader.us.us.preheader
  val br_4 = Module(new UBranchNode(ID = 4))

  //  %.pre = load i32, i32* %arrayidx10, align 4, !tbaa !2
  val ld_5 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=5, RouteID=0))

  //  br label %for.cond1.preheader
  val br_6 = Module(new UBranchNode(ID = 6))

  //  %i.043.us = phi i32 [ %inc19.us, %for.cond1.for.cond.cleanup3_crit_edge.us ], [ 0, %for.cond5.preheader.us.us.preheader.preheader ]
  val phi_i_043_us7 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 1, ID = 7))

  //  %result.042.us = phi i32 [ %add.us, %for.cond1.for.cond.cleanup3_crit_edge.us ], [ 0, %for.cond5.preheader.us.us.preheader.preheader ]
  val phi_result_042_us8 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 1, ID = 8))

  //  br label %for.cond5.preheader.us.us
  val br_9 = Module(new UBranchNode(ID = 9))

  //  %.lcssa = phi i32 [ %0, %for.cond5.for.cond.cleanup7_crit_edge.us.us ]
  val phi__lcssa10 = Module(new PhiFastNode(NumInputs = 1, NumOutputs = 1, ID = 10))

  //  %inc11.us.us.lcssa = phi i32 [ %inc11.us.us, %for.cond5.for.cond.cleanup7_crit_edge.us.us ]
  val phi_inc11_us_us_lcssa11 = Module(new PhiFastNode(NumInputs = 1, NumOutputs = 1, ID = 11))

  //  %inc17.us = add i32 %.lcssa, 2
  val binaryOp_inc17_us12 = Module(new ComputeNode(NumOuts = 1, ID = 12, opCode = "add")(sign=false))

  //  store i32 %inc17.us, i32* %arrayidx10, align 4, !tbaa !2
  val st_13 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, ID=13, RouteID=0))

  //  %add.us = add i32 %inc11.us.us.lcssa, %result.042.us
  val binaryOp_add_us14 = Module(new ComputeNode(NumOuts = 2, ID = 14, opCode = "add")(sign=false))

  //  %inc19.us = add nuw nsw i32 %i.043.us, 1
  val binaryOp_inc19_us15 = Module(new ComputeNode(NumOuts = 2, ID = 15, opCode = "add")(sign=false))

  //  %exitcond65 = icmp eq i32 %inc19.us, 3
  val icmp_exitcond6516 = Module(new IcmpNode(NumOuts = 1, ID = 16, opCode = "eq")(sign=false))

  //  br i1 %exitcond65, label %for.cond.cleanup.loopexit68, label %for.cond5.preheader.us.us.preheader
  val br_17 = Module(new CBranchNode(ID = 17))

  //  %j.041.us.us = phi i32 [ %inc13.us.us, %for.cond5.for.cond.cleanup7_crit_edge.us.us ], [ 0, %for.cond5.preheader.us.us.preheader ]
  val phi_j_041_us_us18 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 1, ID = 18))

  //  br label %for.body8.us.us
  val br_19 = Module(new UBranchNode(ID = 19))

  //  %0 = load i32, i32* %arrayidx10, align 4, !tbaa !2
  val ld_20 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=2, ID=20, RouteID=1))

  //  %inc11.us.us = add i32 %0, 1
  val binaryOp_inc11_us_us21 = Module(new ComputeNode(NumOuts = 2, ID = 21, opCode = "add")(sign=false))

  //  store i32 %inc11.us.us, i32* %arrayidx10, align 4, !tbaa !2
  val st_22 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, ID=22, RouteID=1))

  //  %inc13.us.us = add nuw i32 %j.041.us.us, 1
  val binaryOp_inc13_us_us23 = Module(new ComputeNode(NumOuts = 2, ID = 23, opCode = "add")(sign=false))

  //  %exitcond63 = icmp eq i32 %inc13.us.us, %n
  val icmp_exitcond6324 = Module(new IcmpNode(NumOuts = 1, ID = 24, opCode = "eq")(sign=false))

  //  br i1 %exitcond63, label %for.cond1.for.cond.cleanup3_crit_edge.us, label %for.cond5.preheader.us.us
  val br_25 = Module(new CBranchNode(ID = 25))

  //  %k.039.us.us = phi i32 [ 0, %for.cond5.preheader.us.us ], [ %inc.us.us, %for.body8.us.us ]
  val phi_k_039_us_us26 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 26))

  //  %arrayidx.us.us = getelementptr inbounds i32, i32* %a, i32 %k.039.us.us
  val Gep_arrayidx_us_us27 = Module(new GepNode(NumIns = 1, NumOuts=2, ID=27)(ElementSize = 4, ArraySize = List()))

  //  %1 = load i32, i32* %arrayidx.us.us, align 4, !tbaa !2
  val ld_28 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=28, RouteID=2))

  //  %mul.us.us = shl i32 %1, 1
  val binaryOp_mul_us_us29 = Module(new ComputeNode(NumOuts = 1, ID = 29, opCode = "shl")(sign=false))

  //  store i32 %mul.us.us, i32* %arrayidx.us.us, align 4, !tbaa !2
  val st_30 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, ID=30, RouteID=2))

  //  %inc.us.us = add nuw i32 %k.039.us.us, 1
  val binaryOp_inc_us_us31 = Module(new ComputeNode(NumOuts = 2, ID = 31, opCode = "add")(sign=false))

  //  %exitcond62 = icmp eq i32 %inc.us.us, %n
  val icmp_exitcond6232 = Module(new IcmpNode(NumOuts = 1, ID = 32, opCode = "eq")(sign=false))

  //  br i1 %exitcond62, label %for.cond5.for.cond.cleanup7_crit_edge.us.us, label %for.body8.us.us
  val br_33 = Module(new CBranchNode(ID = 33))

  //  %2 = phi i32 [ %inc17, %for.cond1.preheader ], [ %.pre, %for.cond1.preheader.preheader ]
  val phi_34 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 34))

  //  %i.043 = phi i32 [ %inc19, %for.cond1.preheader ], [ 0, %for.cond1.preheader.preheader ]
  val phi_i_04335 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 1, ID = 35))

  //  %result.042 = phi i32 [ %add, %for.cond1.preheader ], [ 0, %for.cond1.preheader.preheader ]
  val phi_result_04236 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 1, ID = 36))

  //  %inc17 = add i32 %2, 1
  val binaryOp_inc1737 = Module(new ComputeNode(NumOuts = 2, ID = 37, opCode = "add")(sign=false))

  //  %add = add i32 %2, %result.042
  val binaryOp_add38 = Module(new ComputeNode(NumOuts = 2, ID = 38, opCode = "add")(sign=false))

  //  %inc19 = add nuw nsw i32 %i.043, 1
  val binaryOp_inc1939 = Module(new ComputeNode(NumOuts = 2, ID = 39, opCode = "add")(sign=false))

  //  %exitcond = icmp eq i32 %inc19, 3
  val icmp_exitcond40 = Module(new IcmpNode(NumOuts = 1, ID = 40, opCode = "eq")(sign=false))

  //  br i1 %exitcond, label %for.cond.cleanup.loopexit, label %for.cond1.preheader
  val br_41 = Module(new CBranchNode(ID = 41))

  //  %inc17.lcssa = phi i32 [ %inc17, %for.cond1.preheader ]
  val phi_inc17_lcssa42 = Module(new PhiFastNode(NumInputs = 1, NumOutputs = 1, ID = 42))

  //  %add.lcssa = phi i32 [ %add, %for.cond1.preheader ]
  val phi_add_lcssa43 = Module(new PhiFastNode(NumInputs = 1, NumOutputs = 1, ID = 43))

  //  store i32 %inc17.lcssa, i32* %arrayidx10, align 4, !tbaa !2
  val st_44 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, ID=44, RouteID=3))

  //  br label %for.cond.cleanup
  val br_45 = Module(new UBranchNode(ID = 45))

  //  %add.us.lcssa = phi i32 [ %add.us, %for.cond1.for.cond.cleanup3_crit_edge.us ]
  val phi_add_us_lcssa46 = Module(new PhiFastNode(NumInputs = 1, NumOutputs = 1, ID = 46))

  //  br label %for.cond.cleanup
  val br_47 = Module(new UBranchNode(ID = 47))

  //  %result.0.lcssa = phi i32 [ %add.lcssa, %for.cond.cleanup.loopexit ], [ %add.us.lcssa, %for.cond.cleanup.loopexit68 ]
  val phi_result_0_lcssa48 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 1, ID = 48))

  //  %div = sdiv i32 %result.0.lcssa, 2
  val binaryOp_div49 = Module(new ComputeNode(NumOuts = 1, ID = 49, opCode = "udiv")(sign=false))

  //  ret i32 %div
  val ret_50 = Module(new RetNode2(retTypes=List(32), ID = 50))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i32 0
  val const0 = Module(new ConstFastNode(value = 0, ID = 0))

  //i32 -1
  val const1 = Module(new ConstFastNode(value = -1, ID = 1))

  //i32 0
  val const2 = Module(new ConstFastNode(value = 0, ID = 2))

  //i32 0
  val const3 = Module(new ConstFastNode(value = 0, ID = 3))

  //i32 2
  val const4 = Module(new ConstFastNode(value = 2, ID = 4))

  //i32 1
  val const5 = Module(new ConstFastNode(value = 1, ID = 5))

  //i32 3
  val const6 = Module(new ConstFastNode(value = 3, ID = 6))

  //i32 0
  val const7 = Module(new ConstFastNode(value = 0, ID = 7))

  //i32 1
  val const8 = Module(new ConstFastNode(value = 1, ID = 8))

  //i32 1
  val const9 = Module(new ConstFastNode(value = 1, ID = 9))

  //i32 0
  val const10 = Module(new ConstFastNode(value = 0, ID = 10))

  //i32 1
  val const11 = Module(new ConstFastNode(value = 1, ID = 11))

  //i32 1
  val const12 = Module(new ConstFastNode(value = 1, ID = 12))

  //i32 0
  val const13 = Module(new ConstFastNode(value = 0, ID = 13))

  //i32 0
  val const14 = Module(new ConstFastNode(value = 0, ID = 14))

  //i32 1
  val const15 = Module(new ConstFastNode(value = 1, ID = 15))

  //i32 1
  val const16 = Module(new ConstFastNode(value = 1, ID = 16))

  //i32 3
  val const17 = Module(new ConstFastNode(value = 3, ID = 17))

  //i32 2
  val const18 = Module(new ConstFastNode(value = 2, ID = 18))



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_entry0.io.predicateIn <> InputSplitter.io.Out.enable

  bb_for_cond5_preheader_us_us_preheader_preheader1.io.predicateIn <> br_3.io.Out(0)

  bb_for_cond1_preheader_preheader2.io.predicateIn <> br_3.io.Out(1)

  bb_for_cond5_preheader_us_us_preheader3.io.activate <> Loop_3.io.endEnable

  bb_for_cond5_preheader_us_us_preheader3.io.loopBack <> br_17.io.Out(0)

  bb_for_cond1_for_cond_cleanup3_crit_edge_us4.io.predicateIn(0) <> Loop_2.io.endEnable

  bb_for_cond5_preheader_us_us5.io.activate <> Loop_2.io.endEnable

  bb_for_cond5_preheader_us_us5.io.loopBack <> br_25.io.Out(0)

  bb_for_cond5_for_cond_cleanup7_crit_edge_us_us6.io.predicateIn <> Loop_1.io.endEnable

  bb_for_body8_us_us7.io.activate <> Loop_1.io.endEnable

  bb_for_body8_us_us7.io.loopBack <> br_33.io.Out(0)

  bb_for_cond1_preheader8.io.activate <> Loop_0.io.endEnable

  bb_for_cond1_preheader8.io.loopBack <> br_41.io.Out(0)

  bb_for_cond_cleanup_loopexit9.io.predicateIn(0) <> Loop_0.io.endEnable

  bb_for_cond_cleanup_loopexit6810.io.predicateIn(0) <> Loop_3.io.endEnable

  bb_for_cond_cleanup11.io.predicateIn(0) <> br_45.io.Out(0)

  bb_for_cond_cleanup11.io.predicateIn(1) <> br_47.io.Out(0)



  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */

  Loop_0.io.enable <> br_6.io.Out(0)

  Loop_0.io.latchEnable <> br_41.io.Out(1)

  Loop_0.io.loopExit(0) <> br_41.io.Out(1)

  Loop_1.io.enable <> br_19.io.Out(0)

  Loop_1.io.latchEnable <> br_33.io.Out(1)

  Loop_1.io.loopExit(0) <> br_33.io.Out(1)

  Loop_2.io.enable <> br_9.io.Out(0)

  Loop_2.io.latchEnable <> br_25.io.Out(1)

  Loop_2.io.loopExit(0) <> br_25.io.Out(1)

  Loop_3.io.enable <> br_4.io.Out(0)

  Loop_3.io.latchEnable <> br_17.io.Out(1)

  Loop_3.io.loopExit(0) <> br_17.io.Out(1)



  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

  Loop_0.io.In(0) <> ld_5.io.Out.data(0)

  Loop_1.io.In(0) <> Loop_2.io.liveIn.data("field0")(0)

  Loop_1.io.In(1) <> Loop_2.io.liveIn.data("field1")(0)

  Loop_2.io.In(0) <> Loop_3.io.liveIn.data("field0")(0)

  Loop_2.io.In(1) <> Loop_3.io.liveIn.data("field1")(0)

  Loop_2.io.In(2) <> Loop_3.io.liveIn.data("field2")(0)

  Loop_3.io.In(0) <> InputSplitter.io.Out.data("field0")(1)

  Loop_3.io.In(1) <> InputSplitter.io.Out.data("field1")(2)

  Loop_3.io.In(2) <> Gep_arrayidx102.io.Out(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */

  phi_34.io.InData(1) <> Loop_0.io.liveIn.data("field0")(0)

  Gep_arrayidx_us_us27.io.baseAddress <> Loop_1.io.liveIn.data("field0")(0)

  icmp_exitcond6232.io.RightIO <> Loop_1.io.liveIn.data("field1")(0)

  icmp_exitcond6324.io.RightIO <> Loop_2.io.liveIn.data("field1")(1)

  ld_20.io.GepAddr <> Loop_2.io.liveIn.data("field2")(0)

  st_22.io.GepAddr <> Loop_2.io.liveIn.data("field2")(1)

  st_13.io.GepAddr <> Loop_3.io.liveIn.data("field2")(1)



  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */

  Loop_0.io.liveOut(0) <> binaryOp_inc1737.io.Out(1)

  Loop_0.io.liveOut(0) <> binaryOp_add38.io.Out(1)

  Loop_2.io.liveOut(0) <> ld_20.io.Out.data(0)

  Loop_2.io.liveOut(0) <> binaryOp_inc11_us_us21.io.Out(0)

  Loop_3.io.liveOut(0) <> binaryOp_add_us14.io.Out(1)



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  const0.io.enable <> bb_entry0.io.Out(0)

  const1.io.enable <> bb_entry0.io.Out(1)

  icmp_cmp2400.io.enable <> bb_entry0.io.Out(2)

  binaryOp_sub1.io.enable <> bb_entry0.io.Out(3)

  Gep_arrayidx102.io.enable <> bb_entry0.io.Out(4)

  br_3.io.enable <> bb_entry0.io.Out(5)


  br_4.io.enable <> bb_for_cond5_preheader_us_us_preheader_preheader1.io.Out(0)


  ld_5.io.enable <> bb_for_cond1_preheader_preheader2.io.Out(0)

  br_6.io.enable <> bb_for_cond1_preheader_preheader2.io.Out(1)


  const2.io.enable <> bb_for_cond5_preheader_us_us_preheader3.io.Out(0)

  const3.io.enable <> bb_for_cond5_preheader_us_us_preheader3.io.Out(1)

  phi_i_043_us7.io.enable <> bb_for_cond5_preheader_us_us_preheader3.io.Out(2)

  phi_result_042_us8.io.enable <> bb_for_cond5_preheader_us_us_preheader3.io.Out(3)

  br_9.io.enable <> bb_for_cond5_preheader_us_us_preheader3.io.Out(4)


  const4.io.enable <> bb_for_cond1_for_cond_cleanup3_crit_edge_us4.io.Out(0)

  const5.io.enable <> bb_for_cond1_for_cond_cleanup3_crit_edge_us4.io.Out(1)

  const6.io.enable <> bb_for_cond1_for_cond_cleanup3_crit_edge_us4.io.Out(2)

  phi__lcssa10.io.enable <> bb_for_cond1_for_cond_cleanup3_crit_edge_us4.io.Out(3)

  phi_inc11_us_us_lcssa11.io.enable <> bb_for_cond1_for_cond_cleanup3_crit_edge_us4.io.Out(4)

  binaryOp_inc17_us12.io.enable <> bb_for_cond1_for_cond_cleanup3_crit_edge_us4.io.Out(5)

  st_13.io.enable <> bb_for_cond1_for_cond_cleanup3_crit_edge_us4.io.Out(6)

  binaryOp_add_us14.io.enable <> bb_for_cond1_for_cond_cleanup3_crit_edge_us4.io.Out(7)

  binaryOp_inc19_us15.io.enable <> bb_for_cond1_for_cond_cleanup3_crit_edge_us4.io.Out(8)

  icmp_exitcond6516.io.enable <> bb_for_cond1_for_cond_cleanup3_crit_edge_us4.io.Out(9)

  br_17.io.enable <> bb_for_cond1_for_cond_cleanup3_crit_edge_us4.io.Out(10)


  const7.io.enable <> bb_for_cond5_preheader_us_us5.io.Out(0)

  phi_j_041_us_us18.io.enable <> bb_for_cond5_preheader_us_us5.io.Out(1)

  br_19.io.enable <> bb_for_cond5_preheader_us_us5.io.Out(2)


  const8.io.enable <> bb_for_cond5_for_cond_cleanup7_crit_edge_us_us6.io.Out(0)

  const9.io.enable <> bb_for_cond5_for_cond_cleanup7_crit_edge_us_us6.io.Out(1)

  ld_20.io.enable <> bb_for_cond5_for_cond_cleanup7_crit_edge_us_us6.io.Out(2)

  binaryOp_inc11_us_us21.io.enable <> bb_for_cond5_for_cond_cleanup7_crit_edge_us_us6.io.Out(3)

  st_22.io.enable <> bb_for_cond5_for_cond_cleanup7_crit_edge_us_us6.io.Out(4)

  binaryOp_inc13_us_us23.io.enable <> bb_for_cond5_for_cond_cleanup7_crit_edge_us_us6.io.Out(5)

  icmp_exitcond6324.io.enable <> bb_for_cond5_for_cond_cleanup7_crit_edge_us_us6.io.Out(6)

  br_25.io.enable <> bb_for_cond5_for_cond_cleanup7_crit_edge_us_us6.io.Out(7)


  const10.io.enable <> bb_for_body8_us_us7.io.Out(0)

  const11.io.enable <> bb_for_body8_us_us7.io.Out(1)

  const12.io.enable <> bb_for_body8_us_us7.io.Out(2)

  phi_k_039_us_us26.io.enable <> bb_for_body8_us_us7.io.Out(3)

  Gep_arrayidx_us_us27.io.enable <> bb_for_body8_us_us7.io.Out(4)

  ld_28.io.enable <> bb_for_body8_us_us7.io.Out(5)

  binaryOp_mul_us_us29.io.enable <> bb_for_body8_us_us7.io.Out(6)

  st_30.io.enable <> bb_for_body8_us_us7.io.Out(7)

  binaryOp_inc_us_us31.io.enable <> bb_for_body8_us_us7.io.Out(8)

  icmp_exitcond6232.io.enable <> bb_for_body8_us_us7.io.Out(9)

  br_33.io.enable <> bb_for_body8_us_us7.io.Out(10)


  const13.io.enable <> bb_for_cond1_preheader8.io.Out(0)

  const14.io.enable <> bb_for_cond1_preheader8.io.Out(1)

  const15.io.enable <> bb_for_cond1_preheader8.io.Out(2)

  const16.io.enable <> bb_for_cond1_preheader8.io.Out(3)

  const17.io.enable <> bb_for_cond1_preheader8.io.Out(4)

  phi_34.io.enable <> bb_for_cond1_preheader8.io.Out(5)

  phi_i_04335.io.enable <> bb_for_cond1_preheader8.io.Out(6)

  phi_result_04236.io.enable <> bb_for_cond1_preheader8.io.Out(7)

  binaryOp_inc1737.io.enable <> bb_for_cond1_preheader8.io.Out(8)

  binaryOp_add38.io.enable <> bb_for_cond1_preheader8.io.Out(9)

  binaryOp_inc1939.io.enable <> bb_for_cond1_preheader8.io.Out(10)

  icmp_exitcond40.io.enable <> bb_for_cond1_preheader8.io.Out(11)

  br_41.io.enable <> bb_for_cond1_preheader8.io.Out(12)


  phi_inc17_lcssa42.io.enable <> bb_for_cond_cleanup_loopexit9.io.Out(0)

  phi_add_lcssa43.io.enable <> bb_for_cond_cleanup_loopexit9.io.Out(1)

  st_44.io.enable <> bb_for_cond_cleanup_loopexit9.io.Out(2)

  br_45.io.enable <> bb_for_cond_cleanup_loopexit9.io.Out(3)


  phi_add_us_lcssa46.io.enable <> bb_for_cond_cleanup_loopexit6810.io.Out(0)

  br_47.io.enable <> bb_for_cond_cleanup_loopexit6810.io.Out(1)


  const18.io.enable <> bb_for_cond_cleanup11.io.Out(0)

  phi_result_0_lcssa48.io.enable <> bb_for_cond_cleanup11.io.Out(1)

  binaryOp_div49.io.enable <> bb_for_cond_cleanup11.io.Out(2)

  ret_50.io.In.enable <> bb_for_cond_cleanup11.io.Out(3)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  phi_i_043_us7.io.Mask <> bb_for_cond5_preheader_us_us_preheader3.io.MaskBB(0)

  phi_result_042_us8.io.Mask <> bb_for_cond5_preheader_us_us_preheader3.io.MaskBB(1)

  phi__lcssa10.io.Mask <> bb_for_cond1_for_cond_cleanup3_crit_edge_us4.io.MaskBB(0)

  phi_inc11_us_us_lcssa11.io.Mask <> bb_for_cond1_for_cond_cleanup3_crit_edge_us4.io.MaskBB(1)

  phi_j_041_us_us18.io.Mask <> bb_for_cond5_preheader_us_us5.io.MaskBB(0)

  phi_k_039_us_us26.io.Mask <> bb_for_body8_us_us7.io.MaskBB(0)

  phi_34.io.Mask <> bb_for_cond1_preheader8.io.MaskBB(0)

  phi_i_04335.io.Mask <> bb_for_cond1_preheader8.io.MaskBB(1)

  phi_result_04236.io.Mask <> bb_for_cond1_preheader8.io.MaskBB(2)

  phi_inc17_lcssa42.io.Mask <> bb_for_cond_cleanup_loopexit9.io.MaskBB(0)

  phi_add_lcssa43.io.Mask <> bb_for_cond_cleanup_loopexit9.io.MaskBB(1)

  phi_add_us_lcssa46.io.Mask <> bb_for_cond_cleanup_loopexit6810.io.MaskBB(0)

  phi_result_0_lcssa48.io.Mask <> bb_for_cond_cleanup11.io.MaskBB(0)



  /* ================================================================== *
   *                   PRINT ALLOCA OFFSET                              *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */

  MemCtrl.io.ReadIn(0) <> ld_5.io.memReq

  ld_5.io.memResp <> MemCtrl.io.ReadOut(0)

  MemCtrl.io.WriteIn(0) <> st_13.io.memReq

  st_13.io.memResp <> MemCtrl.io.WriteOut(0)

  MemCtrl.io.ReadIn(1) <> ld_20.io.memReq

  ld_20.io.memResp <> MemCtrl.io.ReadOut(1)

  MemCtrl.io.WriteIn(1) <> st_22.io.memReq

  st_22.io.memResp <> MemCtrl.io.WriteOut(1)

  MemCtrl.io.ReadIn(2) <> ld_28.io.memReq

  ld_28.io.memResp <> MemCtrl.io.ReadOut(2)

  MemCtrl.io.WriteIn(2) <> st_30.io.memReq

  st_30.io.memResp <> MemCtrl.io.WriteOut(2)

  MemCtrl.io.WriteIn(3) <> st_44.io.memReq

  st_44.io.memResp <> MemCtrl.io.WriteOut(3)



  /* ================================================================== *
   *                   PRINT SHARED CONNECTIONS                         *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  icmp_cmp2400.io.RightIO <> const0.io.Out

  binaryOp_sub1.io.RightIO <> const1.io.Out

  phi_i_043_us7.io.InData(1) <> const2.io.Out

  phi_result_042_us8.io.InData(1) <> const3.io.Out

  binaryOp_inc17_us12.io.RightIO <> const4.io.Out

  binaryOp_inc19_us15.io.RightIO <> const5.io.Out

  icmp_exitcond6516.io.RightIO <> const6.io.Out

  phi_j_041_us_us18.io.InData(1) <> const7.io.Out

  binaryOp_inc11_us_us21.io.RightIO <> const8.io.Out

  binaryOp_inc13_us_us23.io.RightIO <> const9.io.Out

  phi_k_039_us_us26.io.InData(0) <> const10.io.Out

  binaryOp_mul_us_us29.io.RightIO <> const11.io.Out

  binaryOp_inc_us_us31.io.RightIO <> const12.io.Out

  phi_i_04335.io.InData(1) <> const13.io.Out

  phi_result_04236.io.InData(1) <> const14.io.Out

  binaryOp_inc1737.io.RightIO <> const15.io.Out

  binaryOp_inc1939.io.RightIO <> const16.io.Out

  icmp_exitcond40.io.RightIO <> const17.io.Out

  binaryOp_div49.io.RightIO <> const18.io.Out

  br_3.io.CmpIO <> icmp_cmp2400.io.Out(0)

  Gep_arrayidx102.io.idx(0) <> binaryOp_sub1.io.Out(0)

  ld_5.io.GepAddr <> Gep_arrayidx102.io.Out(1)

  st_44.io.GepAddr <> Gep_arrayidx102.io.Out(2)

  binaryOp_inc19_us15.io.LeftIO <> phi_i_043_us7.io.Out(0)

  binaryOp_add_us14.io.RightIO <> phi_result_042_us8.io.Out(0)

  binaryOp_inc17_us12.io.LeftIO <> phi__lcssa10.io.Out(0)

  binaryOp_add_us14.io.LeftIO <> phi_inc11_us_us_lcssa11.io.Out(0)

  st_13.io.inData <> binaryOp_inc17_us12.io.Out(0)

  phi_result_042_us8.io.InData(0) <> binaryOp_add_us14.io.Out(0)

  phi_i_043_us7.io.InData(0) <> binaryOp_inc19_us15.io.Out(0)

  icmp_exitcond6516.io.LeftIO <> binaryOp_inc19_us15.io.Out(1)

  br_17.io.CmpIO <> icmp_exitcond6516.io.Out(0)

  binaryOp_inc13_us_us23.io.LeftIO <> phi_j_041_us_us18.io.Out(0)

  binaryOp_inc11_us_us21.io.LeftIO <> ld_20.io.Out.data(1)

  st_22.io.inData <> binaryOp_inc11_us_us21.io.Out(1)

  phi_j_041_us_us18.io.InData(0) <> binaryOp_inc13_us_us23.io.Out(0)

  icmp_exitcond6324.io.LeftIO <> binaryOp_inc13_us_us23.io.Out(1)

  br_25.io.CmpIO <> icmp_exitcond6324.io.Out(0)

  Gep_arrayidx_us_us27.io.idx(0) <> phi_k_039_us_us26.io.Out(0)

  binaryOp_inc_us_us31.io.LeftIO <> phi_k_039_us_us26.io.Out(1)

  ld_28.io.GepAddr <> Gep_arrayidx_us_us27.io.Out.data(0)

  st_30.io.GepAddr <> Gep_arrayidx_us_us27.io.Out.data(1)

  binaryOp_mul_us_us29.io.LeftIO <> ld_28.io.Out.data(0)

  st_30.io.inData <> binaryOp_mul_us_us29.io.Out(0)

  phi_k_039_us_us26.io.InData(1) <> binaryOp_inc_us_us31.io.Out(0)

  icmp_exitcond6232.io.LeftIO <> binaryOp_inc_us_us31.io.Out(1)

  br_33.io.CmpIO <> icmp_exitcond6232.io.Out(0)

  binaryOp_inc1737.io.LeftIO <> phi_34.io.Out(0)

  binaryOp_add38.io.LeftIO <> phi_34.io.Out(1)

  binaryOp_inc1939.io.LeftIO <> phi_i_04335.io.Out(0)

  binaryOp_add38.io.RightIO <> phi_result_04236.io.Out(0)

  phi_34.io.InData(0) <> binaryOp_inc1737.io.Out(0)

  phi_result_04236.io.InData(0) <> binaryOp_add38.io.Out(0)

  phi_i_04335.io.InData(0) <> binaryOp_inc1939.io.Out(0)

  icmp_exitcond40.io.LeftIO <> binaryOp_inc1939.io.Out(1)

  br_41.io.CmpIO <> icmp_exitcond40.io.Out(0)

  st_44.io.inData <> phi_inc17_lcssa42.io.Out(0)

  phi_result_0_lcssa48.io.InData(0) <> phi_add_lcssa43.io.Out(0)

  phi_result_0_lcssa48.io.InData(1) <> phi_add_us_lcssa46.io.Out(0)

  binaryOp_div49.io.LeftIO <> phi_result_0_lcssa48.io.Out(0)

  ret_50.io.In.data("field0") <> binaryOp_div49.io.Out(0)

  phi_inc17_lcssa42.io.InData(0) <> Loop_0.io.Out(0)

  phi_add_lcssa43.io.InData(0) <> Loop_0.io.Out(0)

  phi__lcssa10.io.InData(0) <> Loop_2.io.Out(0)

  phi_inc11_us_us_lcssa11.io.InData(0) <> Loop_2.io.Out(0)

  phi_add_us_lcssa46.io.InData(0) <> Loop_3.io.Out(0)

  Gep_arrayidx102.io.baseAddress <> InputSplitter.io.Out.data("field0")(0)

  icmp_cmp2400.io.LeftIO <> InputSplitter.io.Out.data("field1")(0)

  binaryOp_sub1.io.LeftIO <> InputSplitter.io.Out.data("field1")(1)

  st_13.io.Out(0).ready := true.B

  st_22.io.Out(0).ready := true.B

  st_30.io.Out(0).ready := true.B

  st_44.io.Out(0).ready := true.B



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_50.io.Out

}

import java.io.{File, FileWriter}
object test15Main extends App {
  val dir = new File("RTL/test15") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new test15DF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}
