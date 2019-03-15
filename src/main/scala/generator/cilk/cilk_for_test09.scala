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

abstract class cilk_for_test09DFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32, 32))))
    val call_15_out = Decoupled(new Call(List(32, 32)))
    val call_15_in = Flipped(Decoupled(new Call(List())))
    val MemResp = Flipped(Valid(new MemResp))
    val MemReq = Decoupled(new MemReq)
    val out = Decoupled(new Call(List(32)))
  })
}

class cilk_for_test09DF(implicit p: Parameters) extends cilk_for_test09DFIO()(p) {


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  val MemCtrl = Module(new UnifiedController(ID = 0, Size = 32, NReads = 1, NWrites = 1)
  (WControl = new WriteMemoryController(NumOps = 1, BaseSize = 2, NumEntries = 2))
  (RControl = new ReadMemoryController(NumOps = 1, BaseSize = 2, NumEntries = 2))
  (RWArbiter = new ReadWriteArbiter()))

  io.MemReq <> MemCtrl.io.MemReq
  MemCtrl.io.MemResp <> io.MemResp

  val StackPointer = Module(new Stack(NumOps = 1))

  val InputSplitter = Module(new SplitCallNew(List(2, 1)))
  InputSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val Loop_0 = Module(new LoopBlockNode(NumIns = List(1, 1, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 0))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_entry0 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 7, BID = 0))

  val bb_pfor_detach_preheader1 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 1))

  val bb_pfor_cond_cleanup_loopexit2 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 2))

  val bb_pfor_cond_cleanup3 = Module(new BasicBlockNoMaskFastNode(NumInputs = 2, NumOuts = 1, BID = 3))

  val bb_pfor_detach4 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 3, NumPhi = 1, BID = 4))

  val bb_pfor_inc5 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 4, BID = 5))

  val bb_sync_continue6 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 2, BID = 6))

  val bb_offload_pfor_body7 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 2, BID = 7))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  %a = alloca i32, align 4, !UID !10
  val alloca_a0 = Module(new AllocaNode(NumOuts=4, ID = 0, RouteID=0))

  //  %a.0.a.0..sroa_cast = bitcast i32* %a to i8*, !UID !11
  val bitcast_a_0_a_0__sroa_cast1 = Module(new BitCastNode(NumOuts = 1, ID = 1))

  //  store i32 0, i32* %a, align 4, !UID !12
  val st_2 = Module(new UnTypStore(NumPredOps = 0, NumSuccOps = 0, ID = 2, RouteID = 0))

  //  %cmp16 = icmp sgt i32 %m, 0, !UID !13
  val icmp_cmp163 = Module(new IcmpNode(NumOuts = 1, ID = 3, opCode = "ugt")(sign = false))

  //  br i1 %cmp16, label %pfor.detach.preheader, label %pfor.cond.cleanup, !UID !14, !BB_UID !15
  val br_4 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 4))

  //  br label %pfor.detach, !UID !16, !BB_UID !17
  val br_5 = Module(new UBranchNode(ID = 5))

  //  br label %pfor.cond.cleanup, !UID !18, !BB_UID !19
  val br_6 = Module(new UBranchNode(ID = 6))

  //  sync within %syncreg, label %sync.continue, !UID !20, !BB_UID !21
  val sync_7 = Module(new SyncTC(ID = 7, NumInc=1, NumDec=1, NumOuts=1))

  //  %__begin.017 = phi i32 [ %inc, %pfor.inc ], [ 0, %pfor.detach.preheader ], !UID !22
  val phi__begin_0178 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 1, ID = 8, Res = false))

  //  detach within %syncreg, label %offload.pfor.body, label %pfor.inc, !UID !23, !BB_UID !24
  val detach_9 = Module(new Detach(ID = 9))

  //  %inc = add nuw nsw i32 %__begin.017, 1, !UID !25
  val binaryOp_inc10 = Module(new ComputeNode(NumOuts = 2, ID = 10, opCode = "add")(sign = false))

  //  %exitcond = icmp eq i32 %inc, %m, !UID !26
  val icmp_exitcond11 = Module(new IcmpNode(NumOuts = 1, ID = 11, opCode = "eq")(sign = false))

  //  br i1 %exitcond, label %pfor.cond.cleanup.loopexit, label %pfor.detach, !llvm.loop !27, !UID !29, !BB_UID !30
  val br_12 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 12))

  //  %a.0.load15 = load i32, i32* %a, align 4, !UID !31
  val ld_13 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 13, RouteID = 0))

  //  ret i32 %a.0.load15, !UID !32, !BB_UID !33
  val ret_14 = Module(new RetNode2(retTypes = List(32), ID = 14))

  //  call void @cilk_for_test09_detach1(i32* %a, i32 %n), !UID !34
  val call_15_out = Module(new CallOutNode(ID = 15, NumSuccOps = 0, argTypes = List(32,32)))

  val call_15_in = Module(new CallInNode(ID = 15, argTypes = List()))

  //  reattach within %syncreg, label %pfor.inc, !UID !35, !BB_UID !36
  val reattach_16 = Module(new Reattach(NumPredOps= 1, ID = 16))



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



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_entry0.io.predicateIn(0) <> InputSplitter.io.Out.enable

  bb_pfor_detach_preheader1.io.predicateIn(0) <> br_4.io.TrueOutput(0)

  bb_pfor_cond_cleanup3.io.predicateIn(1) <> br_4.io.FalseOutput(0)

  bb_pfor_cond_cleanup3.io.predicateIn(0) <> br_6.io.Out(0)

  bb_pfor_inc5.io.predicateIn(0) <> detach_9.io.Out(0)

  bb_sync_continue6.io.predicateIn(0) <> sync_7.io.Out(0)

  bb_offload_pfor_body7.io.predicateIn(0) <> detach_9.io.Out(1)



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE LOOP                     *
   * ================================================================== */

  bb_pfor_cond_cleanup_loopexit2.io.predicateIn(0) <> Loop_0.io.loopExit(0)

  bb_pfor_detach4.io.predicateIn(1) <> Loop_0.io.activate_loop_start

  bb_pfor_detach4.io.predicateIn(0) <> Loop_0.io.activate_loop_back



  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */

  sync_7.io.incIn(0) <> detach_9.io.Out(2)

  sync_7.io.decIn(0) <> reattach_16.io.Out(0)



  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */

  Loop_0.io.enable <> br_5.io.Out(0)

  Loop_0.io.loopBack(0) <> br_12.io.FalseOutput(0)

  Loop_0.io.loopFinish(0) <> br_12.io.TrueOutput(0)



  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

  Loop_0.io.InLiveIn(0) <> alloca_a0.io.Out(0)

  Loop_0.io.InLiveIn(1) <> InputSplitter.io.Out.data.elements("field1")(0)

  Loop_0.io.InLiveIn(2) <> InputSplitter.io.Out.data.elements("field0")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */

  call_15_out.io.In.elements("field0") <> Loop_0.io.OutLiveIn.elements("field0")(0)

  call_15_out.io.In.elements("field1") <> Loop_0.io.OutLiveIn.elements("field1")(0)

  icmp_exitcond11.io.RightIO <> Loop_0.io.OutLiveIn.elements("field2")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP LIVE OUT DEPENDENCIES                       *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP CARRY DEPENDENCIES                          *
   * ================================================================== */

  Loop_0.io.CarryDepenIn(0) <> binaryOp_inc10.io.Out(0)



  /* ================================================================== *
   *                   LOOP DATA CARRY DEPENDENCIES                     *
   * ================================================================== */

  phi__begin_0178.io.InData(0) <> Loop_0.io.CarryDepenOut.elements("field0")(0)



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  const0.io.enable <> bb_entry0.io.Out(0)

  const1.io.enable <> bb_entry0.io.Out(1)

  alloca_a0.io.enable <> bb_entry0.io.Out(2)


  bitcast_a_0_a_0__sroa_cast1.io.enable <> bb_entry0.io.Out(3)

  bitcast_a_0_a_0__sroa_cast1.io.Out(0).ready := true.B


  st_2.io.enable <> bb_entry0.io.Out(4)


  icmp_cmp163.io.enable <> bb_entry0.io.Out(5)


  br_4.io.enable <> bb_entry0.io.Out(6)


  br_5.io.enable <> bb_pfor_detach_preheader1.io.Out(0)


  br_6.io.enable <> bb_pfor_cond_cleanup_loopexit2.io.Out(0)


  sync_7.io.enable <> bb_pfor_cond_cleanup3.io.Out(0)


  const2.io.enable <> bb_pfor_detach4.io.Out(0)

  phi__begin_0178.io.enable <> bb_pfor_detach4.io.Out(1)


  detach_9.io.enable <> bb_pfor_detach4.io.Out(2)


  const3.io.enable <> bb_pfor_inc5.io.Out(0)

  binaryOp_inc10.io.enable <> bb_pfor_inc5.io.Out(1)


  icmp_exitcond11.io.enable <> bb_pfor_inc5.io.Out(2)


  br_12.io.enable <> bb_pfor_inc5.io.Out(3)


  ld_13.io.enable <> bb_sync_continue6.io.Out(0)


  ret_14.io.In.enable <> bb_sync_continue6.io.Out(1)


  call_15_in.io.enable <> bb_offload_pfor_body7.io.Out(1)

  call_15_out.io.enable <> bb_offload_pfor_body7.io.Out(0)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  phi__begin_0178.io.Mask <> bb_pfor_detach4.io.MaskBB(0)



  /* ================================================================== *
   *                   PRINT ALLOCA OFFSET                              *
   * ================================================================== */

  alloca_a0.io.allocaInputIO.bits.size      := 1.U
  alloca_a0.io.allocaInputIO.bits.numByte   := 4.U
  alloca_a0.io.allocaInputIO.bits.predicate := true.B
  alloca_a0.io.allocaInputIO.bits.valid     := true.B
  alloca_a0.io.allocaInputIO.valid          := true.B





  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */

  StackPointer.io.InData(0) <> alloca_a0.io.allocaReqIO

  alloca_a0.io.allocaRespIO <> StackPointer.io.OutData(0)

  MemCtrl.io.WriteIn(0) <> st_2.io.memReq

  st_2.io.memResp <> MemCtrl.io.WriteOut(0)

  MemCtrl.io.ReadIn(0) <> ld_13.io.memReq

  ld_13.io.memResp <> MemCtrl.io.ReadOut(0)



  /* ================================================================== *
   *                   PRINT SHARED CONNECTIONS                         *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  st_2.io.inData <> const0.io.Out

  icmp_cmp163.io.RightIO <> const1.io.Out

  phi__begin_0178.io.InData(1) <> const2.io.Out

  binaryOp_inc10.io.RightIO <> const3.io.Out

  bitcast_a_0_a_0__sroa_cast1.io.Input <> alloca_a0.io.Out(1)

  st_2.io.GepAddr <> alloca_a0.io.Out(2)

  ld_13.io.GepAddr <> alloca_a0.io.Out(3)

  br_4.io.CmpIO <> icmp_cmp163.io.Out(0)

  binaryOp_inc10.io.LeftIO <> phi__begin_0178.io.Out(0)

  icmp_exitcond11.io.LeftIO <> binaryOp_inc10.io.Out(1)

  br_12.io.CmpIO <> icmp_exitcond11.io.Out(0)

  ret_14.io.In.data("field0") <> ld_13.io.Out(0)

  icmp_cmp163.io.LeftIO <> InputSplitter.io.Out.data.elements("field0")(1)

  st_2.io.Out(0).ready := true.B

  reattach_16.io.predicateIn(0).enq(DataBundle.active(1.U))



  /* ================================================================== *
   *                   PRINTING CALLIN AND CALLOUT INTERFACE            *
   * ================================================================== */

  call_15_in.io.In <> io.call_15_in

  io.call_15_out <> call_15_out.io.Out(0)

  reattach_16.io.enable <> call_15_in.io.Out.enable



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_14.io.Out

}

import java.io.{File, FileWriter}

object cilk_for_test09Top extends App {
  val dir = new File("RTL/cilk_for_test09Top");
  dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new cilk_for_test09DF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}
