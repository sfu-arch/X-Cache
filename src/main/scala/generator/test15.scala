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

  val MemCtrl = Module(new UnifiedController(ID=0, Size=32, NReads=3, NWrites=3)
		 (WControl=new WriteMemoryController(NumOps=3, BaseSize=2, NumEntries=2))
		 (RControl=new ReadMemoryController(NumOps=3, BaseSize=2, NumEntries=2))
		 (RWArbiter=new ReadWriteArbiter()))

  io.MemReq <> MemCtrl.io.MemReq
  MemCtrl.io.MemResp <> io.MemResp

  val InputSplitter = Module(new SplitCallNew(List(3,5)))
  InputSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val Loop_0 = Module(new LoopBlock(NumIns=List(1,1), NumOuts = 0, NumExits=1, ID = 0))

  val Loop_1 = Module(new LoopBlock(NumIns=List(1,1,2,2), NumOuts = 0, NumExits=1, ID = 1))

  val Loop_2 = Module(new LoopBlock(NumIns=List(1,1,1,1,1,2), NumOuts = 1, NumExits=1, ID = 2))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_entry0 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 11, BID = 0))

  val bb_for_cond1_preheader1 = Module(new LoopHead(NumOuts = 5, NumPhi=2, BID = 1))

  val bb_for_cond5_preheader_preheader2 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 2))

  val bb_for_cond_cleanup3 = Module(new BasicBlockNode(NumInputs = 1, NumOuts = 4, NumPhi=1, BID = 3))

  val bb_for_cond5_preheader4 = Module(new LoopHead(NumOuts = 3, NumPhi=1, BID = 4))

  val bb_for_body8_preheader5 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 5))

  val bb_for_cond_cleanup3_loopexit6 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 6))

  val bb_for_cond_cleanup37 = Module(new BasicBlockNoMaskNode(NumInputs = 2, NumOuts = 10, BID = 7))

  val bb_for_cond_cleanup7_loopexit8 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 8))

  val bb_for_cond_cleanup79 = Module(new BasicBlockNoMaskNode(NumInputs = 2, NumOuts = 8, BID = 9))

  val bb_for_body810 = Module(new LoopHead(NumOuts = 11, NumPhi=1, BID = 10))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  %cmp240 = icmp eq i32 %n, 0
  val icmp_cmp2400 = Module(new IcmpFastNode(NumOuts = 1, ID = 0, opCode = "eq")(sign=false))

  //  %sub15 = add i32 %n, -1
  val binaryOp_sub151 = Module(new ComputeFastNode(NumOuts = 1, ID = 1, opCode = "add")(sign=false))

  //  %arrayidx16 = getelementptr inbounds i32, i32* %a, i32 %sub15
  val Gep_arrayidx162 = Module(new GepNode(NumIns = 1, NumOuts=1, ID=2)(ElementSize = 4, ArraySize = List()))

  //  %cmp638 = icmp eq i32 %n, 0
  val icmp_cmp6383 = Module(new IcmpFastNode(NumOuts = 1, ID = 3, opCode = "eq")(sign=false))

  //  %sub = add i32 %n, -1
  val binaryOp_sub4 = Module(new ComputeFastNode(NumOuts = 1, ID = 4, opCode = "add")(sign=false))

  //  %arrayidx10 = getelementptr inbounds i32, i32* %a, i32 %sub
  val Gep_arrayidx105 = Module(new GepNode(NumIns = 1, NumOuts=1, ID=5)(ElementSize = 4, ArraySize = List()))

  //  br label %for.cond1.preheader
  val br_6 = Module(new UBranchNode(ID = 6))

  //  %i.043 = phi i32 [ 0, %entry ], [ %inc19, %for.cond.cleanup3 ]
  val phi_i_0437 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 1, ID = 7))

  //  %result.042 = phi i32 [ 0, %entry ], [ %add, %for.cond.cleanup3 ]
  val phi_result_0428 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 1, ID = 8))

  //  br i1 %cmp240, label %for.cond.cleanup3, label %for.cond5.preheader.preheader
  val br_9 = Module(new CBranchFastNodeVariable(ID = 9))

  //  br label %for.cond5.preheader
  val br_10 = Module(new UBranchNode(ID = 10))

  //  %add.lcssa = phi i32 [ %add, %for.cond.cleanup3 ]
  val phi_add_lcssa11 = Module(new PhiFastNode(NumInputs = 1, NumOutputs = 1, ID = 11))

  //  %div = sdiv i32 %add.lcssa, 2
  val binaryOp_div12 = Module(new ComputeNode(NumOuts = 1, ID = 12, opCode = "udiv")(sign=false))

  //  ret i32 %div
  val ret_13 = Module(new RetNode2(retTypes=List(32), ID = 13))

  //  %j.041 = phi i32 [ %inc13, %for.cond.cleanup7 ], [ 0, %for.cond5.preheader.preheader ]
  val phi_j_04114 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 1, ID = 14))

  //  br i1 %cmp638, label %for.cond.cleanup7, label %for.body8.preheader
  val br_15 = Module(new CBranchFastNodeVariable(ID = 15))

  //  br label %for.body8
  val br_16 = Module(new UBranchNode(ID = 16))

  //  br label %for.cond.cleanup3
  val br_17 = Module(new UBranchNode(ID = 17))

  //  %0 = load i32, i32* %arrayidx16, align 4, !tbaa !2
  val ld_18 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=2, ID=18, RouteID=0))

  //  %inc17 = add i32 %0, 1
  val binaryOp_inc1719 = Module(new ComputeNode(NumOuts = 1, ID = 19, opCode = "add")(sign=false))

  //  store i32 %inc17, i32* %arrayidx16, align 4, !tbaa !2
  val st_20 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, ID=20, RouteID=0))

  //  %add = add i32 %0, %result.042
  val binaryOp_add21 = Module(new ComputeNode(NumOuts = 2, ID = 21, opCode = "add")(sign=false))

  //  %inc19 = add nuw nsw i32 %i.043, 1
  val binaryOp_inc1922 = Module(new ComputeNode(NumOuts = 2, ID = 22, opCode = "add")(sign=false))

  //  %exitcond45 = icmp eq i32 %inc19, 3
  val icmp_exitcond4523 = Module(new IcmpNode(NumOuts = 1, ID = 23, opCode = "eq")(sign=false))

  //  br i1 %exitcond45, label %for.cond.cleanup, label %for.cond1.preheader
  val br_24 = Module(new CBranchFastNodeVariable(NumFalse = 2, ID = 24))

  //  br label %for.cond.cleanup7
  val br_25 = Module(new UBranchNode(ID = 25))

  //  %1 = load i32, i32* %arrayidx10, align 4, !tbaa !2
  val ld_26 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=26, RouteID=1))

  //  %inc11 = add i32 %1, 1
  val binaryOp_inc1127 = Module(new ComputeNode(NumOuts = 1, ID = 27, opCode = "add")(sign=false))

  //  store i32 %inc11, i32* %arrayidx10, align 4, !tbaa !2
  val st_28 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, ID=28, RouteID=1))

  //  %inc13 = add nuw i32 %j.041, 1
  val binaryOp_inc1329 = Module(new ComputeNode(NumOuts = 2, ID = 29, opCode = "add")(sign=false))

  //  %exitcond44 = icmp eq i32 %inc13, %n
  val icmp_exitcond4430 = Module(new IcmpNode(NumOuts = 1, ID = 30, opCode = "eq")(sign=false))

  //  br i1 %exitcond44, label %for.cond.cleanup3.loopexit, label %for.cond5.preheader
  val br_31 = Module(new CBranchNode(ID = 31))

  //  %k.039 = phi i32 [ %inc, %for.body8 ], [ 0, %for.body8.preheader ]
  val phi_k_03932 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 32))

  //  %arrayidx = getelementptr inbounds i32, i32* %a, i32 %k.039
  val Gep_arrayidx33 = Module(new GepNode(NumIns = 1, NumOuts=2, ID=33)(ElementSize = 4, ArraySize = List()))

  //  %2 = load i32, i32* %arrayidx, align 4, !tbaa !2
  val ld_34 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=34, RouteID=2))

  //  %mul = shl i32 %2, 1
  val binaryOp_mul35 = Module(new ComputeNode(NumOuts = 1, ID = 35, opCode = "shl")(sign=false))

  //  store i32 %mul, i32* %arrayidx, align 4, !tbaa !2
  val st_36 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, ID=36, RouteID=2))

  //  %inc = add nuw i32 %k.039, 1
  val binaryOp_inc37 = Module(new ComputeNode(NumOuts = 2, ID = 37, opCode = "add")(sign=false))

  //  %exitcond = icmp eq i32 %inc, %n
  val icmp_exitcond38 = Module(new IcmpNode(NumOuts = 1, ID = 38, opCode = "eq")(sign=false))

  //  br i1 %exitcond, label %for.cond.cleanup7.loopexit, label %for.body8
  val br_39 = Module(new CBranchNode(ID = 39))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i32 0
  val const0 = Module(new ConstFastNode(value = 0, ID = 0))

  //i32 -1
  val const1 = Module(new ConstFastNode(value = -1, ID = 1))

  //i32 0
  val const2 = Module(new ConstFastNode(value = 0, ID = 2))

  //i32 -1
  val const3 = Module(new ConstFastNode(value = -1, ID = 3))

  //i32 0
  val const4 = Module(new ConstFastNode(value = 0, ID = 4))

  //i32 0
  val const5 = Module(new ConstFastNode(value = 0, ID = 5))

  //i32 2
  val const6 = Module(new ConstFastNode(value = 2, ID = 6))

  //i32 0
  val const7 = Module(new ConstFastNode(value = 0, ID = 7))

  //i32 1
  val const8 = Module(new ConstFastNode(value = 1, ID = 8))

  //i32 1
  val const9 = Module(new ConstFastNode(value = 1, ID = 9))

  //i32 3
  val const10 = Module(new ConstFastNode(value = 3, ID = 10))

  //i32 1
  val const11 = Module(new ConstFastNode(value = 1, ID = 11))

  //i32 1
  val const12 = Module(new ConstFastNode(value = 1, ID = 12))

  //i32 0
  val const13 = Module(new ConstFastNode(value = 0, ID = 13))

  //i32 1
  val const14 = Module(new ConstFastNode(value = 1, ID = 14))

  //i32 1
  val const15 = Module(new ConstFastNode(value = 1, ID = 15))



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_entry0.io.predicateIn <> InputSplitter.io.Out.enable

  bb_for_cond1_preheader1.io.activate <> Loop_2.io.endEnable

  bb_for_cond1_preheader1.io.loopBack <> br_24.io.TrueOutput(0)

  bb_for_cond5_preheader_preheader2.io.predicateIn <> br_9.io.TrueOutput(0)

  bb_for_cond_cleanup3.io.predicateIn(0) <> Loop_2.io.endEnable

  bb_for_cond5_preheader4.io.activate <> Loop_1.io.endEnable

  bb_for_cond5_preheader4.io.loopBack <> br_31.io.Out(0)

  bb_for_body8_preheader5.io.predicateIn <> br_15.io.TrueOutput(0)

  bb_for_cond_cleanup3_loopexit6.io.predicateIn <> Loop_1.io.endEnable

  bb_for_cond_cleanup37.io.predicateIn <> br_9.io.FalseOutput(0)

  bb_for_cond_cleanup37.io.predicateIn <> br_17.io.Out(0)

  bb_for_cond_cleanup7_loopexit8.io.predicateIn <> Loop_0.io.endEnable

  bb_for_cond_cleanup79.io.predicateIn <> br_15.io.FalseOutput(0)

  bb_for_cond_cleanup79.io.predicateIn <> br_25.io.Out(0)

  bb_for_body810.io.activate <> Loop_0.io.endEnable

  bb_for_body810.io.loopBack <> br_39.io.Out(0)



  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */

  Loop_0.io.enable <> br_16.io.Out(0)

  Loop_0.io.latchEnable <> br_39.io.Out(1)

  Loop_0.io.loopExit(0) <> br_39.io.Out(1)

  Loop_1.io.enable <> br_10.io.Out(0)

  Loop_1.io.latchEnable <> br_31.io.Out(1)

  Loop_1.io.loopExit(0) <> br_31.io.Out(1)

  Loop_2.io.enable <> br_6.io.Out(0)

  Loop_2.io.latchEnable <> br_24.io.FalseOutput(0)

  Loop_2.io.loopExit(0) <> br_24.io.FalseOutput(1)



  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

  Loop_0.io.In(0) <> Loop_1.io.liveIn.data("field1")(0)

  Loop_0.io.In(1) <> Loop_1.io.liveIn.data("field2")(0)

  Loop_1.io.In(0) <> Loop_2.io.liveIn.data("field1")(0)

  Loop_1.io.In(1) <> Loop_2.io.liveIn.data("field2")(0)

  Loop_1.io.In(2) <> Loop_2.io.liveIn.data("field3")(0)

  Loop_1.io.In(3) <> Loop_2.io.liveIn.data("field4")(0)

  Loop_2.io.In(0) <> icmp_cmp2400.io.Out(0)

  Loop_2.io.In(1) <> icmp_cmp6383.io.Out(0)

  Loop_2.io.In(2) <> InputSplitter.io.Out.data("field0")(0)

  Loop_2.io.In(3) <> InputSplitter.io.Out.data("field1")(1)

  Loop_2.io.In(4) <> Gep_arrayidx105.io.Out.data(0)

  Loop_2.io.In(5) <> Gep_arrayidx162.io.Out.data(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */

  Gep_arrayidx33.io.baseAddress <> Loop_0.io.liveIn.data("field0")(0)

  icmp_exitcond38.io.RightIO <> Loop_0.io.liveIn.data("field1")(0)

  br_15.io.CmpIO <> Loop_1.io.liveIn.data("field0")(0)

  icmp_exitcond4430.io.RightIO <> Loop_1.io.liveIn.data("field2")(1)

  ld_26.io.GepAddr <> Loop_1.io.liveIn.data("field3")(0)

  st_28.io.GepAddr <> Loop_1.io.liveIn.data("field3")(1)

  br_9.io.CmpIO <> Loop_2.io.liveIn.data("field0")(0)

  ld_18.io.GepAddr <> Loop_2.io.liveIn.data("field5")(0)

  st_20.io.GepAddr <> Loop_2.io.liveIn.data("field5")(1)



  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */

  Loop_2.io.liveOut(0) <> binaryOp_add21.io.Out(0)



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  const0.io.enable <> bb_entry0.io.Out(0)

  const1.io.enable <> bb_entry0.io.Out(1)

  const2.io.enable <> bb_entry0.io.Out(2)

  const3.io.enable <> bb_entry0.io.Out(3)

  icmp_cmp2400.io.enable <> bb_entry0.io.Out(4)

  binaryOp_sub151.io.enable <> bb_entry0.io.Out(5)

  Gep_arrayidx162.io.enable <> bb_entry0.io.Out(6)

  icmp_cmp6383.io.enable <> bb_entry0.io.Out(7)

  binaryOp_sub4.io.enable <> bb_entry0.io.Out(8)

  Gep_arrayidx105.io.enable <> bb_entry0.io.Out(9)

  br_6.io.enable <> bb_entry0.io.Out(10)


  const4.io.enable <> bb_for_cond1_preheader1.io.Out(0)

  const5.io.enable <> bb_for_cond1_preheader1.io.Out(1)

  phi_i_0437.io.enable <> bb_for_cond1_preheader1.io.Out(2)

  phi_result_0428.io.enable <> bb_for_cond1_preheader1.io.Out(3)

  br_9.io.enable <> bb_for_cond1_preheader1.io.Out(4)


  br_10.io.enable <> bb_for_cond5_preheader_preheader2.io.Out(0)


  const6.io.enable <> bb_for_cond_cleanup3.io.Out(0)

  phi_add_lcssa11.io.enable <> bb_for_cond_cleanup3.io.Out(1)

  binaryOp_div12.io.enable <> bb_for_cond_cleanup3.io.Out(2)

  ret_13.io.In.enable <> bb_for_cond_cleanup3.io.Out(3)


  const7.io.enable <> bb_for_cond5_preheader4.io.Out(0)

  phi_j_04114.io.enable <> bb_for_cond5_preheader4.io.Out(1)

  br_15.io.enable <> bb_for_cond5_preheader4.io.Out(2)


  br_16.io.enable <> bb_for_body8_preheader5.io.Out(0)


  br_17.io.enable <> bb_for_cond_cleanup3_loopexit6.io.Out(0)


  const8.io.enable <> bb_for_cond_cleanup37.io.Out(0)

  const9.io.enable <> bb_for_cond_cleanup37.io.Out(1)

  const10.io.enable <> bb_for_cond_cleanup37.io.Out(2)

  ld_18.io.enable <> bb_for_cond_cleanup37.io.Out(3)

  binaryOp_inc1719.io.enable <> bb_for_cond_cleanup37.io.Out(4)

  st_20.io.enable <> bb_for_cond_cleanup37.io.Out(5)

  binaryOp_add21.io.enable <> bb_for_cond_cleanup37.io.Out(6)

  binaryOp_inc1922.io.enable <> bb_for_cond_cleanup37.io.Out(7)

  icmp_exitcond4523.io.enable <> bb_for_cond_cleanup37.io.Out(8)

  br_24.io.enable <> bb_for_cond_cleanup37.io.Out(9)


  br_25.io.enable <> bb_for_cond_cleanup7_loopexit8.io.Out(0)


  const11.io.enable <> bb_for_cond_cleanup79.io.Out(0)

  const12.io.enable <> bb_for_cond_cleanup79.io.Out(1)

  ld_26.io.enable <> bb_for_cond_cleanup79.io.Out(2)

  binaryOp_inc1127.io.enable <> bb_for_cond_cleanup79.io.Out(3)

  st_28.io.enable <> bb_for_cond_cleanup79.io.Out(4)

  binaryOp_inc1329.io.enable <> bb_for_cond_cleanup79.io.Out(5)

  icmp_exitcond4430.io.enable <> bb_for_cond_cleanup79.io.Out(6)

  br_31.io.enable <> bb_for_cond_cleanup79.io.Out(7)


  const13.io.enable <> bb_for_body810.io.Out(0)

  const14.io.enable <> bb_for_body810.io.Out(1)

  const15.io.enable <> bb_for_body810.io.Out(2)

  phi_k_03932.io.enable <> bb_for_body810.io.Out(3)

  Gep_arrayidx33.io.enable <> bb_for_body810.io.Out(4)

  ld_34.io.enable <> bb_for_body810.io.Out(5)

  binaryOp_mul35.io.enable <> bb_for_body810.io.Out(6)

  st_36.io.enable <> bb_for_body810.io.Out(7)

  binaryOp_inc37.io.enable <> bb_for_body810.io.Out(8)

  icmp_exitcond38.io.enable <> bb_for_body810.io.Out(9)

  br_39.io.enable <> bb_for_body810.io.Out(10)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  phi_i_0437.io.Mask <> bb_for_cond1_preheader1.io.MaskBB(0)

  phi_result_0428.io.Mask <> bb_for_cond1_preheader1.io.MaskBB(1)

  phi_add_lcssa11.io.Mask <> bb_for_cond_cleanup3.io.MaskBB(0)

  phi_j_04114.io.Mask <> bb_for_cond5_preheader4.io.MaskBB(0)

  phi_k_03932.io.Mask <> bb_for_body810.io.MaskBB(0)



  /* ================================================================== *
   *                   PRINT ALLOCA OFFSET                              *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */

  MemCtrl.io.ReadIn(0) <> ld_18.io.memReq

  ld_18.io.memResp <> MemCtrl.io.ReadOut(0)

  MemCtrl.io.WriteIn(0) <> st_20.io.memReq

  st_20.io.memResp <> MemCtrl.io.WriteOut(0)

  MemCtrl.io.ReadIn(1) <> ld_26.io.memReq

  ld_26.io.memResp <> MemCtrl.io.ReadOut(1)

  MemCtrl.io.WriteIn(1) <> st_28.io.memReq

  st_28.io.memResp <> MemCtrl.io.WriteOut(1)

  MemCtrl.io.ReadIn(2) <> ld_34.io.memReq

  ld_34.io.memResp <> MemCtrl.io.ReadOut(2)

  MemCtrl.io.WriteIn(2) <> st_36.io.memReq

  st_36.io.memResp <> MemCtrl.io.WriteOut(2)



  /* ================================================================== *
   *                   PRINT SHARED CONNECTIONS                         *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  icmp_cmp2400.io.RightIO <> const0.io.Out

  binaryOp_sub151.io.RightIO <> const1.io.Out

  icmp_cmp6383.io.RightIO <> const2.io.Out

  binaryOp_sub4.io.RightIO <> const3.io.Out

  phi_i_0437.io.InData(0) <> const4.io.Out

  phi_result_0428.io.InData(0) <> const5.io.Out

  binaryOp_div12.io.RightIO <> const6.io.Out

  phi_j_04114.io.InData(1) <> const7.io.Out

  binaryOp_inc1719.io.RightIO <> const8.io.Out

  binaryOp_inc1922.io.RightIO <> const9.io.Out

  icmp_exitcond4523.io.RightIO <> const10.io.Out

  binaryOp_inc1127.io.RightIO <> const11.io.Out

  binaryOp_inc1329.io.RightIO <> const12.io.Out

  phi_k_03932.io.InData(1) <> const13.io.Out

  binaryOp_mul35.io.RightIO <> const14.io.Out

  binaryOp_inc37.io.RightIO <> const15.io.Out

  Gep_arrayidx162.io.idx(0) <> binaryOp_sub151.io.Out(0)

  Gep_arrayidx105.io.idx(0) <> binaryOp_sub4.io.Out(0)

  binaryOp_inc1922.io.LeftIO <> phi_i_0437.io.Out(0)

  binaryOp_add21.io.RightIO <> phi_result_0428.io.Out(0)

  binaryOp_div12.io.LeftIO <> phi_add_lcssa11.io.Out(0)

  ret_13.io.In.data("field0") <> binaryOp_div12.io.Out(0)

  binaryOp_inc1329.io.LeftIO <> phi_j_04114.io.Out(0)

  binaryOp_inc1719.io.LeftIO <> ld_18.io.Out.data(0)

  binaryOp_add21.io.LeftIO <> ld_18.io.Out.data(0)

  st_20.io.inData <> binaryOp_inc1719.io.Out(0)

  phi_result_0428.io.InData(1) <> binaryOp_add21.io.Out(1)

  phi_i_0437.io.InData(1) <> binaryOp_inc1922.io.Out(1)

  icmp_exitcond4523.io.LeftIO <> binaryOp_inc1922.io.Out(0)

  br_24.io.CmpIO <> icmp_exitcond4523.io.Out(0)

  binaryOp_inc1127.io.LeftIO <> ld_26.io.Out.data(0)

  st_28.io.inData <> binaryOp_inc1127.io.Out(0)

  phi_j_04114.io.InData(0) <> binaryOp_inc1329.io.Out(0)

  icmp_exitcond4430.io.LeftIO <> binaryOp_inc1329.io.Out(0)

  br_31.io.CmpIO <> icmp_exitcond4430.io.Out(0)

  Gep_arrayidx33.io.idx(0) <> phi_k_03932.io.Out(1)

  binaryOp_inc37.io.LeftIO <> phi_k_03932.io.Out(0)

  ld_34.io.GepAddr <> Gep_arrayidx33.io.Out.data(0)

  st_36.io.GepAddr <> Gep_arrayidx33.io.Out.data(1)

  binaryOp_mul35.io.LeftIO <> ld_34.io.Out.data(0)

  st_36.io.inData <> binaryOp_mul35.io.Out(0)

  phi_k_03932.io.InData(0) <> binaryOp_inc37.io.Out(0)

  icmp_exitcond38.io.LeftIO <> binaryOp_inc37.io.Out(0)

  br_39.io.CmpIO <> icmp_exitcond38.io.Out(0)

  phi_add_lcssa11.io.InData(0) <> Loop_2.io.Out(0)

  Gep_arrayidx162.io.baseAddress <> InputSplitter.io.Out.data("field0")(0)

  Gep_arrayidx105.io.baseAddress <> InputSplitter.io.Out.data("field0")(0)

  icmp_cmp2400.io.LeftIO <> InputSplitter.io.Out.data("field1")(0)

  binaryOp_sub151.io.LeftIO <> InputSplitter.io.Out.data("field1")(0)

  icmp_cmp6383.io.LeftIO <> InputSplitter.io.Out.data("field1")(0)

  binaryOp_sub4.io.LeftIO <> InputSplitter.io.Out.data("field1")(0)

  st_20.io.Out(0).ready := true.B

  st_28.io.Out(0).ready := true.B

  st_36.io.Out(0).ready := true.B



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_13.io.Out

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
