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

abstract class stencil_detach1DFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32, 32, 32))))
    val call_6_out = Decoupled(new Call(List(32, 32, 32, 32, 32)))
    val call_6_in = Flipped(Decoupled(new Call(List())))
    val MemResp = Flipped(Valid(new MemResp))
    val MemReq = Decoupled(new MemReq)
    val out = Decoupled(new Call(List()))
  })
}

class stencil_detach1DF(implicit p: Parameters) extends stencil_detach1DFIO()(p) {


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  val MemCtrl = Module(new UnifiedController(ID=0, Size=32, NReads=2, NWrites=2)
		 (WControl=new WriteMemoryController(NumOps=2, BaseSize=2, NumEntries=2))
		 (RControl=new ReadMemoryController(NumOps=2, BaseSize=2, NumEntries=2))
		 (RWArbiter=new ReadWriteArbiter()))

  io.MemReq <> MemCtrl.io.MemReq
  MemCtrl.io.MemResp <> io.MemResp

  val InputSplitter = Module(new SplitCallNew(List(2,1,3)))
  InputSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val Loop_0 = Module(new LoopBlock(NumIns=List(1,1,1,1), NumOuts = 0, NumExits=1, ID = 0))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_my_pfor_body0 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 5, BID = 0))

  val bb_my_for_cond1 = Module(new LoopHead(NumOuts = 5, NumPhi=1, BID = 1))

  val bb_my_for_body2 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 2, BID = 2))

  val bb_my_for_inc3 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 3, BID = 3))

  val bb_my_for_end4 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 13, BID = 4))

  val bb_my_pfor_preattach5 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 5))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  %0 = lshr i32 %pos.0.in, 2, !UID !1
  val binaryOp_0 = Module(new ComputeNode(NumOuts = 3, ID = 0, opCode = "lshr")(sign=false))

  //  %1 = and i32 %pos.0.in, 3, !UID !2
  val binaryOp_1 = Module(new ComputeNode(NumOuts = 3, ID = 1, opCode = "and")(sign=false))

  //  br label %my_for.cond, !UID !3, !BB_UID !4
  val br_2 = Module(new UBranchNode(ID = 2))

  //  %2 = phi i32 [ 0, %my_pfor.body ], [ %4, %my_for.inc ], !UID !5
  val phi_3 = Module(new PhiNode(NumInputs = 2, NumOuts = 3, ID = 3))

  //  %3 = icmp ule i32 %2, 2, !UID !6
  val icmp_4 = Module(new IcmpNode(NumOuts = 1, ID = 4, opCode = "ule")(sign=false))

  //  br i1 %3, label %my_for.body, label %my_for.end, !UID !7, !BB_UID !8
  val br_5 = Module(new CBranchNode(ID = 5))

  //  call void @stencil_inner(i32* %in.in, i32* %out.in, i32 %0, i32 %1, i32 %2), !UID !9
  val call_6_out = Module(new CallOutNode(ID = 6, NumSuccOps = 0, argTypes = List(32,32,32,32,32)))

  val call_6_in = Module(new CallInNode(ID = 6, argTypes = List()))

  //  br label %my_for.inc, !UID !10, !BB_UID !11
  val br_7 = Module(new UBranchNode(ID = 7, NumPredOps = 1))

  //  %4 = add i32 %2, 1, !UID !12
  val binaryOp_8 = Module(new ComputeNode(NumOuts = 1, ID = 8, opCode = "add")(sign=false))

  //  br label %my_for.cond, !UID !13, !BB_UID !14
  val br_9 = Module(new UBranchNode(NumOuts=2, ID = 9))

  //  %5 = mul i32 %0, 4, !UID !15
  val binaryOp_10 = Module(new ComputeNode(NumOuts = 1, ID = 10, opCode = "mul")(sign=false))

  //  %6 = add i32 %5, %1, !UID !16
  val binaryOp_11 = Module(new ComputeNode(NumOuts = 1, ID = 11, opCode = "add")(sign=false))

  //  %7 = getelementptr inbounds i32, i32* %out.in, i32 %6, !UID !17
  val Gep_12 = Module(new GepArrayOneNode(NumOuts=1, ID=12)(numByte=4)(size=1))

  //  %8 = load i32, i32* %7, align 4, !UID !18
  val ld_13 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=13, RouteID=0))

  //  %9 = add i32 %8, 9, !UID !19
  val binaryOp_14 = Module(new ComputeNode(NumOuts = 1, ID = 14, opCode = "add")(sign=false))

  //  %10 = mul i32 %0, 4, !UID !20
  val binaryOp_15 = Module(new ComputeNode(NumOuts = 1, ID = 15, opCode = "mul")(sign=false))

  //  %11 = add i32 %10, %1, !UID !21
  val binaryOp_16 = Module(new ComputeNode(NumOuts = 1, ID = 16, opCode = "add")(sign=false))

  //  %12 = getelementptr inbounds i32, i32* %out.in, i32 %11, !UID !22
  val Gep_17 = Module(new GepArrayOneNode(NumOuts=1, ID=17)(numByte=4)(size=1))

  //  store i32 %9, i32* %12, align 4, !UID !23
  val st_18 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, ID=18, RouteID=0))

  //  br label %my_pfor.preattach, !UID !24, !BB_UID !25
  val br_19 = Module(new UBranchNode(ID = 19))

  //  ret void
  val ret_20 = Module(new RetNode2(retTypes=List(), ID = 20))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i32 2
  val const0 = Module(new ConstNode(value = 2, NumOuts = 1, ID = 0))

  //i32 3
  val const1 = Module(new ConstNode(value = 3, NumOuts = 1, ID = 1))

  //i32 0
  val const2 = Module(new ConstNode(value = 0, NumOuts = 1, ID = 2))

  //i32 2
  val const3 = Module(new ConstNode(value = 2, NumOuts = 1, ID = 3))

  //i32 1
  val const4 = Module(new ConstNode(value = 1, NumOuts = 1, ID = 4))

  //i32 4
  val const5 = Module(new ConstNode(value = 4, NumOuts = 1, ID = 5))

  //i32 9
  val const6 = Module(new ConstNode(value = 9, NumOuts = 1, ID = 6))

  //i32 4
  val const7 = Module(new ConstNode(value = 4, NumOuts = 1, ID = 7))



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_my_pfor_body0.io.predicateIn <> InputSplitter.io.Out.enable

  bb_my_for_cond1.io.activate <> Loop_0.io.activate

  bb_my_for_cond1.io.loopBack <> br_9.io.Out(0)

  bb_my_for_body2.io.predicateIn <> br_5.io.Out(0)

  bb_my_for_inc3.io.predicateIn <> br_7.io.Out(0)

  bb_my_for_end4.io.predicateIn <> Loop_0.io.endEnable

  bb_my_pfor_preattach5.io.predicateIn <> br_19.io.Out(0)



  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */

  Loop_0.io.enable <> br_2.io.Out(0)

  Loop_0.io.latchEnable <> br_9.io.Out(1)

  Loop_0.io.loopExit(0) <> br_5.io.Out(1)



  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

  Loop_0.io.In(0) <> InputSplitter.io.Out.data.elements("field1")(0)

  Loop_0.io.In(1) <> InputSplitter.io.Out.data.elements("field2")(0)

  Loop_0.io.In(2) <> binaryOp_0.io.Out(0)

  Loop_0.io.In(3) <> binaryOp_1.io.Out(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */

  call_6_out.io.In("field0") <> Loop_0.io.liveIn.elements("field0")(0)

  call_6_out.io.In("field1") <> Loop_0.io.liveIn.elements("field1")(0)

  call_6_out.io.In("field2") <> Loop_0.io.liveIn.elements("field2")(0)

  call_6_out.io.In("field3") <> Loop_0.io.liveIn.elements("field3")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  const0.io.enable <> bb_my_pfor_body0.io.Out(0)

  const1.io.enable <> bb_my_pfor_body0.io.Out(1)

  binaryOp_0.io.enable <> bb_my_pfor_body0.io.Out(2)

  binaryOp_1.io.enable <> bb_my_pfor_body0.io.Out(3)

  br_2.io.enable <> bb_my_pfor_body0.io.Out(4)


  const2.io.enable <> bb_my_for_cond1.io.Out(0)

  const3.io.enable <> bb_my_for_cond1.io.Out(1)

  phi_3.io.enable <> bb_my_for_cond1.io.Out(2)

  icmp_4.io.enable <> bb_my_for_cond1.io.Out(3)

  br_5.io.enable <> bb_my_for_cond1.io.Out(4)


  call_6_in.io.enable.enq(ControlBundle.active())

  call_6_out.io.enable <> bb_my_for_body2.io.Out(0)

  br_7.io.enable <> bb_my_for_body2.io.Out(1)


  const4.io.enable <> bb_my_for_inc3.io.Out(0)

  binaryOp_8.io.enable <> bb_my_for_inc3.io.Out(1)

  br_9.io.enable <> bb_my_for_inc3.io.Out(2)


  const5.io.enable <> bb_my_for_end4.io.Out(0)

  const6.io.enable <> bb_my_for_end4.io.Out(1)

  const7.io.enable <> bb_my_for_end4.io.Out(2)

  binaryOp_10.io.enable <> bb_my_for_end4.io.Out(3)

  binaryOp_11.io.enable <> bb_my_for_end4.io.Out(4)

  Gep_12.io.enable <> bb_my_for_end4.io.Out(5)

  ld_13.io.enable <> bb_my_for_end4.io.Out(6)

  binaryOp_14.io.enable <> bb_my_for_end4.io.Out(7)

  binaryOp_15.io.enable <> bb_my_for_end4.io.Out(8)

  binaryOp_16.io.enable <> bb_my_for_end4.io.Out(9)

  Gep_17.io.enable <> bb_my_for_end4.io.Out(10)

  st_18.io.enable <> bb_my_for_end4.io.Out(11)

  br_19.io.enable <> bb_my_for_end4.io.Out(12)


  ret_20.io.In.enable <> bb_my_pfor_preattach5.io.Out(0)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  phi_3.io.Mask <> bb_my_for_cond1.io.MaskBB(0)



  /* ================================================================== *
   *                   PRINT ALLOCA OFFSET                              *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */

  MemCtrl.io.ReadIn(0) <> ld_13.io.memReq

  ld_13.io.memResp <> MemCtrl.io.ReadOut(0)

  MemCtrl.io.WriteIn(0) <> st_18.io.memReq

  st_18.io.memResp <> MemCtrl.io.WriteOut(0)



  /* ================================================================== *
   *                   PRINT SHARED CONNECTIONS                         *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  binaryOp_0.io.RightIO <> const0.io.Out(0)

  binaryOp_1.io.RightIO <> const1.io.Out(0)

  phi_3.io.InData(0) <> const2.io.Out(0)

  icmp_4.io.RightIO <> const3.io.Out(0)

  binaryOp_8.io.RightIO <> const4.io.Out(0)

  binaryOp_10.io.RightIO <> const5.io.Out(0)

  binaryOp_14.io.RightIO <> const6.io.Out(0)

  binaryOp_15.io.RightIO <> const7.io.Out(0)

  binaryOp_10.io.LeftIO <> binaryOp_0.io.Out(1)

  binaryOp_15.io.LeftIO <> binaryOp_0.io.Out(2)

  binaryOp_11.io.RightIO <> binaryOp_1.io.Out(1)

  binaryOp_16.io.RightIO <> binaryOp_1.io.Out(2)

  icmp_4.io.LeftIO <> phi_3.io.Out(0)

  call_6_out.io.In("field4") <> phi_3.io.Out(1)

  binaryOp_8.io.LeftIO <> phi_3.io.Out(2)

  br_5.io.CmpIO <> icmp_4.io.Out(0)

//  br_7.io.CmpIO <> call_6_in.io.Out.elements("field0")

  phi_3.io.InData(1) <> binaryOp_8.io.Out(0)

  binaryOp_11.io.LeftIO <> binaryOp_10.io.Out(0)

  Gep_12.io.idx1 <> binaryOp_11.io.Out(0)

  ld_13.io.GepAddr <> Gep_12.io.Out(0)

  binaryOp_14.io.LeftIO <> ld_13.io.Out(0)

  st_18.io.inData <> binaryOp_14.io.Out(0)

  binaryOp_16.io.LeftIO <> binaryOp_15.io.Out(0)

  Gep_17.io.idx1 <> binaryOp_16.io.Out(0)

  st_18.io.GepAddr <> Gep_17.io.Out(0)

  binaryOp_0.io.LeftIO <> InputSplitter.io.Out.data.elements("field0")(0)

  binaryOp_1.io.LeftIO <> InputSplitter.io.Out.data.elements("field0")(1)

  Gep_12.io.baseAddress <> InputSplitter.io.Out.data.elements("field2")(1)

  Gep_17.io.baseAddress <> InputSplitter.io.Out.data.elements("field2")(2)

  st_18.io.Out(0).ready := true.B



  /* ================================================================== *
   *                   PRINTING CALLIN AND CALLOUT INTERFACE            *
   * ================================================================== */

  call_6_in.io.In <> io.call_6_in

  io.call_6_out <> call_6_out.io.Out(0)

  br_7.io.PredOp(0) <> call_6_in.io.Out.enable



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_20.io.Out

}

import java.io.{File, FileWriter}
object stencil_detach1Main extends App {
  val dir = new File("RTL/stencil_detach1") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new stencil_detach1DF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}
