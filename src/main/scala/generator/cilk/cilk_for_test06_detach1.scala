package dataflow

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

abstract class cilk_for_test06_detach1DFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32, 32, 32, 32))))
    val call_10_out = Decoupled(new Call(List(32, 32, 32, 32, 32)))
    val call_10_in = Flipped(Decoupled(new Call(List(32))))
    val MemResp = Flipped(Valid(new MemResp))
    val MemReq = Decoupled(new MemReq)
    val out = Decoupled(new Call(List()))
  })
}

class cilk_for_test06_detach1DF(implicit p: Parameters) extends cilk_for_test06_detach1DFIO()(p) {


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  val MemCtrl = Module(new UnifiedController(ID=0, Size=32, NReads=2, NWrites=2)
		 (WControl=new WriteMemoryController(NumOps=2, BaseSize=2, NumEntries=2))
		 (RControl=new ReadMemoryController(NumOps=2, BaseSize=2, NumEntries=2))
		 (RWArbiter=new ReadWriteArbiter()))

  io.MemReq <> MemCtrl.io.MemReq
  MemCtrl.io.MemResp <> io.MemResp

  val InputSplitter = Module(new SplitCallNew(List(1,1,1,1)))
  InputSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val Loop_0 = Module(new LoopBlock(NumIns=List(1,1,1,1), NumOuts = 0, NumExits=1, ID = 0))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_my_pfor_body0 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 0))

  val bb_my_pfor_cond21 = Module(new LoopHead(NumOuts = 5, NumPhi=1, BID = 1))

  val bb_my_pfor_detach42 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 2))

  val bb_my_pfor_inc3 = Module(new BasicBlockNode(NumInputs = 1, NumOuts = 3, NumPhi=0, BID = 3))

  val bb_my_pfor_end4 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 4))

  val bb_my_pfor_end_continue5 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 5))

  val bb_my_pfor_preattach116 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 6))

  val bb_my_offload_pfor_body57 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 7))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  br label %my_pfor.cond2, !UID !1, !BB_UID !2
  val br_0 = Module(new UBranchNode(ID = 0))

  //  %0 = phi i32 [ 0, %my_pfor.body ], [ %2, %my_pfor.inc ], !UID !3
  val phi_1 = Module(new PhiNode(NumInputs = 2, NumOuts = 3, ID = 1))

  //  %1 = icmp slt i32 %0, 5, !UID !4
  val icmp_2 = Module(new IcmpNode(NumOuts = 1, ID = 2, opCode = "ult")(sign=false))

  //  br i1 %1, label %my_pfor.detach4, label %my_pfor.end, !UID !5, !BB_UID !6
  val br_3 = Module(new CBranchNode(ID = 3))

  //  detach label %my_offload.pfor.body5, label %my_pfor.inc, !UID !7, !BB_UID !8
  val detach_4 = Module(new Detach(ID = 4))

  //  %2 = add nsw i32 %0, 1, !UID !9
  val binaryOp_5 = Module(new ComputeNode(NumOuts = 1, ID = 5, opCode = "add")(sign=false))

  //  br label %my_pfor.cond2, !llvm.loop !10, !UID !12, !BB_UID !13
  val br_6 = Module(new UBranchNode(NumOuts=2, ID = 6))

  //  sync label %my_pfor.end.continue, !UID !14, !BB_UID !15
  val sync_7 = Module(new SyncTC(ID = 7, NumInc=1, NumDec=1, NumOuts=1))

  //  br label %my_pfor.preattach11, !UID !16, !BB_UID !17
  val br_8 = Module(new UBranchNode(ID = 8))

  //  ret void
  val ret_9 = Module(new RetNode2(retTypes=List(), ID = 9))

  //  call void @cilk_for_test06_detach2([5 x i32]* %a.in, i32 %i.0.in, i32 %0, [5 x i32]* %b.in, [5 x i32]* %c.in)
  val call_10_out = Module(new CallOutNode(ID = 10, NumSuccOps = 0, argTypes = List(32,32,32,32,32)))

  val call_10_in = Module(new CallInNode(ID = 10, argTypes = List()))

  //  reattach label %my_pfor.inc
  val reattach_11 = Module(new Reattach(NumPredOps = 1, ID = 11))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i32 0
  val const0 = Module(new ConstNode(value = 0, NumOuts = 1, ID = 0))

  //i32 5
  val const1 = Module(new ConstNode(value = 5, NumOuts = 1, ID = 1))

  //i32 1
  val const2 = Module(new ConstNode(value = 1, NumOuts = 1, ID = 2))



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_my_pfor_body0.io.predicateIn <> InputSplitter.io.Out.enable

  bb_my_pfor_cond21.io.activate <> Loop_0.io.activate

  bb_my_pfor_cond21.io.loopBack <> br_6.io.Out(0)

  bb_my_pfor_detach42.io.predicateIn <> br_3.io.Out(0)

  bb_my_pfor_inc3.io.predicateIn(0) <> detach_4.io.Out(0)

  bb_my_pfor_end4.io.predicateIn <> Loop_0.io.endEnable

  bb_my_pfor_end_continue5.io.predicateIn <> sync_7.io.Out(0)

  bb_my_pfor_preattach116.io.predicateIn <> br_8.io.Out(0)

  bb_my_offload_pfor_body57.io.predicateIn <> detach_4.io.Out(1)



  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */

  sync_7.io.incIn(0) <> detach_4.io.Out(2)

  sync_7.io.decIn(0) <> reattach_11.io.Out(0)



  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */

  Loop_0.io.enable <> br_0.io.Out(0)

  Loop_0.io.latchEnable <> br_6.io.Out(1)

  Loop_0.io.loopExit(0) <> br_3.io.Out(1)



  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

  Loop_0.io.In(0) <> InputSplitter.io.Out.data("field0")(0)

  Loop_0.io.In(1) <> InputSplitter.io.Out.data("field1")(0)

  Loop_0.io.In(2) <> InputSplitter.io.Out.data("field2")(0)

  Loop_0.io.In(3) <> InputSplitter.io.Out.data("field3")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */

  call_10_out.io.In("field0") <> Loop_0.io.liveIn.data("field0")(0)

  call_10_out.io.In("field1") <> Loop_0.io.liveIn.data("field1")(0)

  call_10_out.io.In("field3") <> Loop_0.io.liveIn.data("field2")(0)

  call_10_out.io.In("field4") <> Loop_0.io.liveIn.data("field3")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  br_0.io.enable <> bb_my_pfor_body0.io.Out(0)


  const0.io.enable <> bb_my_pfor_cond21.io.Out(0)

  const1.io.enable <> bb_my_pfor_cond21.io.Out(1)

  phi_1.io.enable <> bb_my_pfor_cond21.io.Out(2)

  icmp_2.io.enable <> bb_my_pfor_cond21.io.Out(3)

  br_3.io.enable <> bb_my_pfor_cond21.io.Out(4)


  detach_4.io.enable <> bb_my_pfor_detach42.io.Out(0)


  const2.io.enable <> bb_my_pfor_inc3.io.Out(0)

  binaryOp_5.io.enable <> bb_my_pfor_inc3.io.Out(1)

  br_6.io.enable <> bb_my_pfor_inc3.io.Out(2)


  sync_7.io.enable <> bb_my_pfor_end4.io.Out(0)


  br_8.io.enable <> bb_my_pfor_end_continue5.io.Out(0)


  ret_9.io.In.enable <> bb_my_pfor_preattach116.io.Out(0)


  call_10_in.io.enable.enq(ControlBundle.active())

  call_10_out.io.enable <> bb_my_offload_pfor_body57.io.Out(0)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  phi_1.io.Mask <> bb_my_pfor_cond21.io.MaskBB(0)



  /* ================================================================== *
   *                   PRINT ALLOCA OFFSET                              *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   PRINT SHARED CONNECTIONS                         *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  phi_1.io.InData(0) <> const0.io.Out(0)

  icmp_2.io.RightIO <> const1.io.Out(0)

  binaryOp_5.io.RightIO <> const2.io.Out(0)

  icmp_2.io.LeftIO <> phi_1.io.Out(0)

  binaryOp_5.io.LeftIO <> phi_1.io.Out(1)

  call_10_out.io.In("field2") <> phi_1.io.Out(2)

  br_3.io.CmpIO <> icmp_2.io.Out(0)

  phi_1.io.InData(1) <> binaryOp_5.io.Out(0)

  //reattach_11.io.predicateIn(0) <> call_10_in.io.Out.data("field0")
  reattach_11.io.predicateIn(0).enq(DataBundle.active(1.U))


  /* ================================================================== *
   *                   PRINTING CALLIN AND CALLOUT INTERFACE            *
   * ================================================================== */

  call_10_in.io.In <> io.call_10_in

  io.call_10_out <> call_10_out.io.Out(0)

  reattach_11.io.enable <> call_10_in.io.Out.enable



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_9.io.Out

}

import java.io.{File, FileWriter}
object cilk_for_test06_detach1Main extends App {
  val dir = new File("RTL/cilk_for_test06_detach1") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new cilk_for_test06_detach1DF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}
