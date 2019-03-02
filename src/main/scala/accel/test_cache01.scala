package cache

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

abstract class test_cache01DFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32, 32))))
    val MemResp = Flipped(Valid(new MemResp))
    val MemReq = Decoupled(new MemReq)
    val out = Decoupled(new Call(List(32)))
  })
}

class test_cache01DF(implicit p: Parameters) extends test_cache01DFIO()(p) {


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  val MemCtrl = Module(new UnifiedController(ID = 0, Size = 32, NReads = 5, NWrites = 4)
  (WControl = new WriteMemoryController(NumOps = 4, BaseSize = 2, NumEntries = 2))
  (RControl = new ReadMemoryController(NumOps = 5, BaseSize = 2, NumEntries = 2))
  (RWArbiter = new ReadWriteArbiter()))

  io.MemReq <> MemCtrl.io.MemReq
  MemCtrl.io.MemResp <> io.MemResp

  val InputSplitter = Module(new SplitCallNew(List(2, 2)))
  InputSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val Loop_0 = Module(new LoopBlockNode(NumIns = List(1, 4), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 0))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_entry0 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 3, BID = 0))

  val bb_for_body_preheader1 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 1))

  val bb_for_cond_cleanup_loopexit2 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 2))

  val bb_for_cond_cleanup3 = Module(new BasicBlockNoMaskFastNode(NumInputs = 2, NumOuts = 4, BID = 3))

  val bb_for_body4 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 32, NumPhi = 1, BID = 4))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  %cmp36 = icmp eq i32 %n, 0, !UID !10
  val icmp_cmp360 = Module(new IcmpNode(NumOuts = 1, ID = 0, opCode = "eq")(sign = false))

  //  br i1 %cmp36, label %for.cond.cleanup, label %for.body.preheader, !UID !11, !BB_UID !12
  val br_1 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 1))

  //  br label %for.body, !UID !13, !BB_UID !14
  val br_2 = Module(new UBranchNode(ID = 2))

  //  br label %for.cond.cleanup
  val br_3 = Module(new UBranchNode(ID = 3))

  //  %arrayidx17 = getelementptr inbounds i32, i32* %a, i32 20, !UID !15
  val Gep_arrayidx174 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 4)(ElementSize = 4, ArraySize = List()))

  //  %0 = load i32, i32* %arrayidx17, align 4, !tbaa !16, !UID !20
  val ld_5 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 5, RouteID = 0))

  //  ret i32 %0, !UID !21, !BB_UID !22
  val ret_6 = Module(new RetNode2(retTypes = List(32), ID = 6))

  //  %i.037 = phi i32 [ %add16, %for.body ], [ 0, %for.body.preheader ], !UID !23
  val phii_0377 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 5, ID = 7, Res = false))

  //  %arrayidx = getelementptr inbounds i32, i32* %a, i32 %i.037, !UID !24
  val Gep_arrayidx8 = Module(new GepNode(NumIns = 1, NumOuts = 2, ID = 8)(ElementSize = 4, ArraySize = List()))

  //  %1 = load i32, i32* %arrayidx, align 4, !tbaa !16, !UID !25
  val ld_9 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 9, RouteID = 1))

  //  %mul = shl i32 %1, 1, !UID !26
  val binaryOp_mul10 = Module(new ComputeNode(NumOuts = 1, ID = 10, opCode = "shl")(sign = false))

  //  store i32 %mul, i32* %arrayidx, align 4, !tbaa !16, !UID !27
  val st_11 = Module(new UnTypStore(NumPredOps = 0, NumSuccOps = 0, ID = 11, RouteID = 0))

  //  %add = or i32 %i.037, 1, !UID !28
  val binaryOp_add12 = Module(new ComputeNode(NumOuts = 1, ID = 12, opCode = "or")(sign = false))

  //  %arrayidx2 = getelementptr inbounds i32, i32* %a, i32 %add, !UID !29
  val Gep_arrayidx213 = Module(new GepNode(NumIns = 1, NumOuts = 2, ID = 13)(ElementSize = 4, ArraySize = List()))

  //  %2 = load i32, i32* %arrayidx2, align 4, !tbaa !16, !UID !30
  val ld_14 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 14, RouteID = 2))

  //  %mul3 = shl i32 %2, 2, !UID !31
  val binaryOp_mul315 = Module(new ComputeNode(NumOuts = 1, ID = 15, opCode = "shl")(sign = false))

  //  store i32 %mul3, i32* %arrayidx2, align 4, !tbaa !16, !UID !32
  val st_16 = Module(new UnTypStore(NumPredOps = 0, NumSuccOps = 0, ID = 16, RouteID = 1))

  //  %add6 = or i32 %i.037, 2, !UID !33
  val binaryOp_add617 = Module(new ComputeNode(NumOuts = 1, ID = 17, opCode = "or")(sign = false))

  //  %arrayidx7 = getelementptr inbounds i32, i32* %a, i32 %add6, !UID !34
  val Gep_arrayidx718 = Module(new GepNode(NumIns = 1, NumOuts = 2, ID = 18)(ElementSize = 4, ArraySize = List()))

  //  %3 = load i32, i32* %arrayidx7, align 4, !tbaa !16, !UID !35
  val ld_19 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 19, RouteID = 3))

  //  %mul8 = mul i32 %3, 6, !UID !36
  val binaryOp_mul820 = Module(new ComputeNode(NumOuts = 1, ID = 20, opCode = "mul")(sign = false))

  //  store i32 %mul8, i32* %arrayidx7, align 4, !tbaa !16, !UID !37
  val st_21 = Module(new UnTypStore(NumPredOps = 0, NumSuccOps = 0, ID = 21, RouteID = 2))

  //  %add11 = or i32 %i.037, 3, !UID !38
  val binaryOp_add1122 = Module(new ComputeNode(NumOuts = 1, ID = 22, opCode = "or")(sign = false))

  //  %arrayidx12 = getelementptr inbounds i32, i32* %a, i32 %add11, !UID !39
  val Gep_arrayidx1223 = Module(new GepNode(NumIns = 1, NumOuts = 2, ID = 23)(ElementSize = 4, ArraySize = List()))

  //  %4 = load i32, i32* %arrayidx12, align 4, !tbaa !16, !UID !40
  val ld_24 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 24, RouteID = 4))

  //  %mul13 = shl i32 %4, 3, !UID !41
  val binaryOp_mul1325 = Module(new ComputeNode(NumOuts = 1, ID = 25, opCode = "shl")(sign = false))

  //  store i32 %mul13, i32* %arrayidx12, align 4, !tbaa !16, !UID !42
  val st_26 = Module(new UnTypStore(NumPredOps = 0, NumSuccOps = 0, ID = 26, RouteID = 3))

  //  %add16 = add i32 %i.037, 4, !UID !43
  val binaryOp_add1627 = Module(new ComputeNode(NumOuts = 2, ID = 27, opCode = "add")(sign = false))

  //  %cmp = icmp ult i32 %add16, %n, !UID !44
  val icmp_cmp28 = Module(new IcmpNode(NumOuts = 1, ID = 28, opCode = "ult")(sign = false))

  //  br i1 %cmp, label %for.body, label %for.cond.cleanup.loopexit, !UID !45, !BB_UID !46
  val br_29 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 29))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i32 0
  val const0 = Module(new ConstFastNode(value = 0, ID = 0))

  //i32 20
  val const1 = Module(new ConstFastNode(value = 20, ID = 1))

  //i32 0
  val const2 = Module(new ConstFastNode(value = 0, ID = 2))

  //i32 1
  val const3 = Module(new ConstFastNode(value = 1, ID = 3))

  //i32 1
  val const4 = Module(new ConstFastNode(value = 1, ID = 4))

  //i32 2
  val const5 = Module(new ConstFastNode(value = 2, ID = 5))

  //i32 2
  val const6 = Module(new ConstFastNode(value = 2, ID = 6))

  //i32 6
  val const7 = Module(new ConstFastNode(value = 6, ID = 7))

  //i32 3
  val const8 = Module(new ConstFastNode(value = 3, ID = 8))

  //i32 3
  val const9 = Module(new ConstFastNode(value = 3, ID = 9))

  //i32 4
  val const10 = Module(new ConstFastNode(value = 4, ID = 10))



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_entry0.io.predicateIn(0) <> InputSplitter.io.Out.enable

  bb_for_body_preheader1.io.predicateIn(0) <> br_1.io.FalseOutput(0)

  bb_for_cond_cleanup3.io.predicateIn(1) <> br_1.io.TrueOutput(0)

  bb_for_cond_cleanup3.io.predicateIn(0) <> br_3.io.Out(0)



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE LOOP                     *
   * ================================================================== */

  bb_for_cond_cleanup_loopexit2.io.predicateIn(0) <> Loop_0.io.loopExit(0)

  bb_for_body4.io.predicateIn(1) <> Loop_0.io.activate_loop_start

  bb_for_body4.io.predicateIn(0) <> Loop_0.io.activate_loop_back



  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */

  Loop_0.io.enable <> br_2.io.Out(0)

  Loop_0.io.loopBack(0) <> br_29.io.TrueOutput(0)

  Loop_0.io.loopFinish(0) <> br_29.io.FalseOutput(0)



  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

  Loop_0.io.InLiveIn(0) <> InputSplitter.io.Out.data.elements("field1")(1)

  Loop_0.io.InLiveIn(1) <> InputSplitter.io.Out.data.elements("field0")(1)



  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */

  icmp_cmp28.io.RightIO <> Loop_0.io.OutLiveIn.elements("field0")(0)

  Gep_arrayidx8.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field1")(0)

  Gep_arrayidx213.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field1")(1)

  Gep_arrayidx718.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field1")(2)

  Gep_arrayidx1223.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field1")(3)



  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP LIVE OUT DEPENDENCIES                       *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP CARRY DEPENDENCIES                          *
   * ================================================================== */

  Loop_0.io.CarryDepenIn(0) <> binaryOp_add1627.io.Out(0)



  /* ================================================================== *
   *                   LOOP DATA CARRY DEPENDENCIES                     *
   * ================================================================== */

  phii_0377.io.InData(0) <> Loop_0.io.CarryDepenOut.elements("field0")(0)



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  const0.io.enable <> bb_entry0.io.Out(0)

  icmp_cmp360.io.enable <> bb_entry0.io.Out(1)

  br_1.io.enable <> bb_entry0.io.Out(2)


  br_2.io.enable <> bb_for_body_preheader1.io.Out(0)


  br_3.io.enable <> bb_for_cond_cleanup_loopexit2.io.Out(0)


  const1.io.enable <> bb_for_cond_cleanup3.io.Out(0)

  Gep_arrayidx174.io.enable <> bb_for_cond_cleanup3.io.Out(1)

  ld_5.io.enable <> bb_for_cond_cleanup3.io.Out(2)

  ret_6.io.In.enable <> bb_for_cond_cleanup3.io.Out(3)


  const2.io.enable <> bb_for_body4.io.Out(0)

  const3.io.enable <> bb_for_body4.io.Out(1)

  const4.io.enable <> bb_for_body4.io.Out(2)

  const5.io.enable <> bb_for_body4.io.Out(3)

  const6.io.enable <> bb_for_body4.io.Out(4)

  const7.io.enable <> bb_for_body4.io.Out(5)

  const8.io.enable <> bb_for_body4.io.Out(6)

  const9.io.enable <> bb_for_body4.io.Out(7)

  const10.io.enable <> bb_for_body4.io.Out(8)

  phii_0377.io.enable <> bb_for_body4.io.Out(9)

  Gep_arrayidx8.io.enable <> bb_for_body4.io.Out(10)

  ld_9.io.enable <> bb_for_body4.io.Out(11)

  binaryOp_mul10.io.enable <> bb_for_body4.io.Out(12)

  st_11.io.enable <> bb_for_body4.io.Out(13)

  binaryOp_add12.io.enable <> bb_for_body4.io.Out(14)

  Gep_arrayidx213.io.enable <> bb_for_body4.io.Out(15)

  ld_14.io.enable <> bb_for_body4.io.Out(16)

  binaryOp_mul315.io.enable <> bb_for_body4.io.Out(17)

  st_16.io.enable <> bb_for_body4.io.Out(18)

  binaryOp_add617.io.enable <> bb_for_body4.io.Out(19)

  Gep_arrayidx718.io.enable <> bb_for_body4.io.Out(20)

  ld_19.io.enable <> bb_for_body4.io.Out(21)

  binaryOp_mul820.io.enable <> bb_for_body4.io.Out(22)

  st_21.io.enable <> bb_for_body4.io.Out(23)

  binaryOp_add1122.io.enable <> bb_for_body4.io.Out(24)

  Gep_arrayidx1223.io.enable <> bb_for_body4.io.Out(25)

  ld_24.io.enable <> bb_for_body4.io.Out(26)

  binaryOp_mul1325.io.enable <> bb_for_body4.io.Out(27)

  st_26.io.enable <> bb_for_body4.io.Out(28)

  binaryOp_add1627.io.enable <> bb_for_body4.io.Out(29)

  icmp_cmp28.io.enable <> bb_for_body4.io.Out(30)

  br_29.io.enable <> bb_for_body4.io.Out(31)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  phii_0377.io.Mask <> bb_for_body4.io.MaskBB(0)



  /* ================================================================== *
   *                   PRINT ALLOCA OFFSET                              *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */

  MemCtrl.io.ReadIn(0) <> ld_5.io.memReq

  ld_5.io.memResp <> MemCtrl.io.ReadOut(0)

  MemCtrl.io.ReadIn(1) <> ld_9.io.memReq

  ld_9.io.memResp <> MemCtrl.io.ReadOut(1)

  MemCtrl.io.WriteIn(0) <> st_11.io.memReq

  st_11.io.memResp <> MemCtrl.io.WriteOut(0)

  MemCtrl.io.ReadIn(2) <> ld_14.io.memReq

  ld_14.io.memResp <> MemCtrl.io.ReadOut(2)

  MemCtrl.io.WriteIn(1) <> st_16.io.memReq

  st_16.io.memResp <> MemCtrl.io.WriteOut(1)

  MemCtrl.io.ReadIn(3) <> ld_19.io.memReq

  ld_19.io.memResp <> MemCtrl.io.ReadOut(3)

  MemCtrl.io.WriteIn(2) <> st_21.io.memReq

  st_21.io.memResp <> MemCtrl.io.WriteOut(2)

  MemCtrl.io.ReadIn(4) <> ld_24.io.memReq

  ld_24.io.memResp <> MemCtrl.io.ReadOut(4)

  MemCtrl.io.WriteIn(3) <> st_26.io.memReq

  st_26.io.memResp <> MemCtrl.io.WriteOut(3)



  /* ================================================================== *
   *                   PRINT SHARED CONNECTIONS                         *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  icmp_cmp360.io.RightIO <> const0.io.Out

  Gep_arrayidx174.io.idx(0) <> const1.io.Out

  phii_0377.io.InData(1) <> const2.io.Out

  binaryOp_mul10.io.RightIO <> const3.io.Out

  binaryOp_add12.io.RightIO <> const4.io.Out

  binaryOp_mul315.io.RightIO <> const5.io.Out

  binaryOp_add617.io.RightIO <> const6.io.Out

  binaryOp_mul820.io.RightIO <> const7.io.Out

  binaryOp_add1122.io.RightIO <> const8.io.Out

  binaryOp_mul1325.io.RightIO <> const9.io.Out

  binaryOp_add1627.io.RightIO <> const10.io.Out

  br_1.io.CmpIO <> icmp_cmp360.io.Out(0)

  ld_5.io.GepAddr <> Gep_arrayidx174.io.Out(0)

  ret_6.io.In.data("field0") <> ld_5.io.Out(0)

  Gep_arrayidx8.io.idx(0) <> phii_0377.io.Out(0)

  binaryOp_add12.io.LeftIO <> phii_0377.io.Out(1)

  binaryOp_add617.io.LeftIO <> phii_0377.io.Out(2)

  binaryOp_add1122.io.LeftIO <> phii_0377.io.Out(3)

  binaryOp_add1627.io.LeftIO <> phii_0377.io.Out(4)

  ld_9.io.GepAddr <> Gep_arrayidx8.io.Out(0)

  st_11.io.GepAddr <> Gep_arrayidx8.io.Out(1)

  binaryOp_mul10.io.LeftIO <> ld_9.io.Out(0)

  st_11.io.inData <> binaryOp_mul10.io.Out(0)

  Gep_arrayidx213.io.idx(0) <> binaryOp_add12.io.Out(0)

  ld_14.io.GepAddr <> Gep_arrayidx213.io.Out(0)

  st_16.io.GepAddr <> Gep_arrayidx213.io.Out(1)

  binaryOp_mul315.io.LeftIO <> ld_14.io.Out(0)

  st_16.io.inData <> binaryOp_mul315.io.Out(0)

  Gep_arrayidx718.io.idx(0) <> binaryOp_add617.io.Out(0)

  ld_19.io.GepAddr <> Gep_arrayidx718.io.Out(0)

  st_21.io.GepAddr <> Gep_arrayidx718.io.Out(1)

  binaryOp_mul820.io.LeftIO <> ld_19.io.Out(0)

  st_21.io.inData <> binaryOp_mul820.io.Out(0)

  Gep_arrayidx1223.io.idx(0) <> binaryOp_add1122.io.Out(0)

  ld_24.io.GepAddr <> Gep_arrayidx1223.io.Out(0)

  st_26.io.GepAddr <> Gep_arrayidx1223.io.Out(1)

  binaryOp_mul1325.io.LeftIO <> ld_24.io.Out(0)

  st_26.io.inData <> binaryOp_mul1325.io.Out(0)

  icmp_cmp28.io.LeftIO <> binaryOp_add1627.io.Out(1)

  br_29.io.CmpIO <> icmp_cmp28.io.Out(0)

  Gep_arrayidx174.io.baseAddress <> InputSplitter.io.Out.data.elements("field0")(0)

  icmp_cmp360.io.LeftIO <> InputSplitter.io.Out.data.elements("field1")(0)

  st_11.io.Out(0).ready := true.B

  st_16.io.Out(0).ready := true.B

  st_21.io.Out(0).ready := true.B

  st_26.io.Out(0).ready := true.B



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_6.io.Out

}

import java.io.{File, FileWriter}

object test_cache01Top extends App {
  val dir = new File("RTL/test_cache01Top");
  dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new test_cache01DF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}
