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
import org.scalatest.Matchers._
import regfile._
import stack._
import util._


  /* ================================================================== *
   *                   PRINTING PORTS DEFINITION                        *
   * ================================================================== */

abstract class vector_scale_detach1DFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32, 32, 32, 32))))
    val MemResp = Flipped(Valid(new MemResp))
    val MemReq = Decoupled(new MemReq)
    val out = Decoupled(new Call(List(32)))
  })
}

class vector_scale_detach1DF(implicit p: Parameters) extends vector_scale_detach1DFIO()(p) {


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  val MemCtrl = Module(new UnifiedController(ID=0, Size=32, NReads=3, NWrites=3)
		 (WControl=new WriteMemoryController(NumOps=3, BaseSize=2, NumEntries=2))
		 (RControl=new ReadMemoryController(NumOps=3, BaseSize=2, NumEntries=2))
		 (RWArbiter=new ReadWriteArbiter()))

  io.MemReq <> MemCtrl.io.MemReq
  MemCtrl.io.MemResp <> io.MemResp

  val InputSplitter = Module(new SplitCallNew(List(2,6,4,1)))
  InputSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_my_pfor_body0 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 5, BID = 0))

  val bb_my_if_then1 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 4, BID = 1))

  val bb_my_if_end92 = Module(new BasicBlockNoMaskNode(NumInputs = 2, NumOuts = 1, BID = 2))

  val bb_my_pfor_preattach3 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 2, BID = 3))

  val bb_my_if_else4 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 12, BID = 4))

  val bb_my_if_then75 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 4, BID = 5))

  val bb_my_if_end6 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 1, NumPhi = 0, BID = 6)) // manual



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  %0 = getelementptr inbounds i32, i32* %a.in, i32 %i.0.in, !UID !1
  val Gep_0 = Module(new GepArrayOneNode(NumOuts=1, ID=0)(numByte=4)(size=1))

  //  %1 = load i32, i32* %0, align 4, !UID !2
  val ld_1 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=1, RouteID=0))

  //  %2 = icmp slt i32 %1, 0, !UID !3
  val icmp_2 = Module(new IcmpNode(NumOuts = 1, ID = 2, opCode = "ult")(sign=false))

  //  br i1 %2, label %my_if.then, label %my_if.else, !UID !4, !BB_UID !5
  val br_3 = Module(new CBranchNode(ID = 3))

  //  %3 = getelementptr inbounds i32, i32* %c.in, i32 %i.0.in, !UID !6
  val Gep_4 = Module(new GepArrayOneNode(NumOuts=1, ID=4)(numByte=4)(size=1))

  //  store i32 0, i32* %3, align 4, !UID !7
  val st_5 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, ID=5, RouteID=0))

  //  br label %my_if.end9, !UID !8, !BB_UID !9
  val br_6 = Module(new UBranchNode(ID = 6))

  //  br label %my_pfor.preattach, !UID !10, !BB_UID !11
  val br_7 = Module(new UBranchNode(ID = 7))

  //  ret void
  val ret_8 = Module(new RetNode(retTypes=List(32), ID = 8))

  //  %4 = getelementptr inbounds i32, i32* %a.in, i32 %i.0.in, !UID !12
  val Gep_9 = Module(new GepArrayOneNode(NumOuts=1, ID=9)(numByte=4)(size=1))

  //  %5 = load i32, i32* %4, align 4, !UID !13
  val ld_10 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=10, RouteID=1))

  //  %6 = mul nsw i32 %5, %scale.in, !UID !14
  val binaryOp_11 = Module(new ComputeNode(NumOuts = 1, ID = 11, opCode = "mul")(sign=false))

  //  %7 = ashr i32 %6, 8, !UID !15
  val binaryOp_12 = Module(new ComputeNode(NumOuts = 1, ID = 12, opCode = "ashr")(sign=false))

  //  %8 = getelementptr inbounds i32, i32* %c.in, i32 %i.0.in, !UID !16
  val Gep_13 = Module(new GepArrayOneNode(NumOuts=1, ID=13)(numByte=4)(size=1))

  //  store i32 %7, i32* %8, align 4, !UID !17
  val st_14 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=1, ID=14, RouteID=1))

  //  %9 = getelementptr inbounds i32, i32* %c.in, i32 %i.0.in, !UID !18
  val Gep_15 = Module(new GepArrayOneNode(NumOuts=1, ID=15)(numByte=4)(size=1))

  //  %10 = load i32, i32* %9, align 4, !UID !19
  val ld_16 = Module(new UnTypLoad(NumPredOps=1, NumSuccOps=0, NumOuts=1, ID=16, RouteID=2))

  //  %11 = icmp sgt i32 %10, 255, !UID !20
  val icmp_17 = Module(new IcmpNode(NumOuts = 1, ID = 17, opCode = "ugt")(sign=false))

  //  br i1 %11, label %my_if.then7, label %my_if.end, !UID !21, !BB_UID !22
  val br_18 = Module(new CBranchNode(ID = 18))

  //  %12 = getelementptr inbounds i32, i32* %c.in, i32 %i.0.in, !UID !23
  val Gep_19 = Module(new GepArrayOneNode(NumOuts=1, ID=19)(numByte=4)(size=1))

  //  store i32 255, i32* %12, align 4, !UID !24
  val st_20 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, ID=20, RouteID=2))

  //  br label %my_if.end, !UID !25, !BB_UID !26
  val br_21 = Module(new UBranchNode(ID = 21))

  //  br label %my_if.end9, !UID !27, !BB_UID !28
  val br_22 = Module(new UBranchNode(ID = 22))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i32 0
  val const0 = Module(new ConstNode(value = 0, NumOuts = 1, ID = 0))

  //i32 0
  val const1 = Module(new ConstNode(value = 0, NumOuts = 1, ID = 1))

  //i32 8
  val const2 = Module(new ConstNode(value = 8, NumOuts = 1, ID = 2))

  //i32 255
  val const3 = Module(new ConstNode(value = 255, NumOuts = 1, ID = 3))

  //i32 255
  val const4 = Module(new ConstNode(value = 255, NumOuts = 1, ID = 4))

  //i32 1
  val const5 = Module(new ConstNode(value = 1, NumOuts = 1, ID = 5))


  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_my_pfor_body0.io.predicateIn <> InputSplitter.io.Out.enable

  bb_my_if_then1.io.predicateIn <> br_3.io.Out(0)

  bb_my_if_end92.io.predicateIn <> br_6.io.Out(0)

  bb_my_if_end92.io.predicateIn <> br_22.io.Out(0)

  bb_my_pfor_preattach3.io.predicateIn <> br_7.io.Out(0)

  bb_my_if_else4.io.predicateIn <> br_3.io.Out(1)

  bb_my_if_then75.io.predicateIn <> br_18.io.Out(0)

  bb_my_if_end6.io.predicateIn(0) <> br_18.io.Out(1)

  bb_my_if_end6.io.predicateIn(1) <> br_21.io.Out(0)



  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */



  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  const0.io.enable <> bb_my_pfor_body0.io.Out(0)

  Gep_0.io.enable <> bb_my_pfor_body0.io.Out(1)

  ld_1.io.enable <> bb_my_pfor_body0.io.Out(2)

  icmp_2.io.enable <> bb_my_pfor_body0.io.Out(3)

  br_3.io.enable <> bb_my_pfor_body0.io.Out(4)


  const1.io.enable <> bb_my_if_then1.io.Out(0)

  Gep_4.io.enable <> bb_my_if_then1.io.Out(1)

  st_5.io.enable <> bb_my_if_then1.io.Out(2)

  br_6.io.enable <> bb_my_if_then1.io.Out(3)


  br_7.io.enable <> bb_my_if_end92.io.Out(0)


  ret_8.io.enable <> bb_my_pfor_preattach3.io.Out(0)
  const5.io.enable <> bb_my_pfor_preattach3.io.Out(1)


  const2.io.enable <> bb_my_if_else4.io.Out(0)

  const3.io.enable <> bb_my_if_else4.io.Out(1)

  Gep_9.io.enable <> bb_my_if_else4.io.Out(2)

  ld_10.io.enable <> bb_my_if_else4.io.Out(3)

  binaryOp_11.io.enable <> bb_my_if_else4.io.Out(4)

  binaryOp_12.io.enable <> bb_my_if_else4.io.Out(5)

  Gep_13.io.enable <> bb_my_if_else4.io.Out(6)

  st_14.io.enable <> bb_my_if_else4.io.Out(7)

  Gep_15.io.enable <> bb_my_if_else4.io.Out(8)

  ld_16.io.PredOp(0) <> st_14.io.SuccOp(0)
  ld_16.io.enable <> bb_my_if_else4.io.Out(9)

  icmp_17.io.enable <> bb_my_if_else4.io.Out(10)

  br_18.io.enable <> bb_my_if_else4.io.Out(11)


  const4.io.enable <> bb_my_if_then75.io.Out(0)

  Gep_19.io.enable <> bb_my_if_then75.io.Out(1)

  st_20.io.enable <> bb_my_if_then75.io.Out(2)

  br_21.io.enable <> bb_my_if_then75.io.Out(3)


  br_22.io.enable <> bb_my_if_end6.io.Out(0)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */



  /* ================================================================== *
   *                   PRINT ALLOCA OFFSET                              *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */

  MemCtrl.io.ReadIn(0) <> ld_1.io.memReq

  ld_1.io.memResp <> MemCtrl.io.ReadOut(0)

  MemCtrl.io.WriteIn(0) <> st_5.io.memReq

  st_5.io.memResp <> MemCtrl.io.WriteOut(0)

  MemCtrl.io.ReadIn(1) <> ld_10.io.memReq

  ld_10.io.memResp <> MemCtrl.io.ReadOut(1)

  MemCtrl.io.WriteIn(1) <> st_14.io.memReq

  st_14.io.memResp <> MemCtrl.io.WriteOut(1)

  MemCtrl.io.ReadIn(2) <> ld_16.io.memReq

  ld_16.io.memResp <> MemCtrl.io.ReadOut(2)

  MemCtrl.io.WriteIn(2) <> st_20.io.memReq

  st_20.io.memResp <> MemCtrl.io.WriteOut(2)



  /* ================================================================== *
   *                   PRINT SHARED CONNECTIONS                         *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  icmp_2.io.RightIO <> const0.io.Out(0)

  st_5.io.inData <> const1.io.Out(0)

  binaryOp_12.io.RightIO <> const2.io.Out(0)

  icmp_17.io.RightIO <> const3.io.Out(0)

  st_20.io.inData <> const4.io.Out(0)

  ld_1.io.GepAddr <> Gep_0.io.Out.data(0)

  icmp_2.io.LeftIO <> ld_1.io.Out.data(0)

  br_3.io.CmpIO <> icmp_2.io.Out(0)

  st_5.io.GepAddr <> Gep_4.io.Out.data(0)

  ld_10.io.GepAddr <> Gep_9.io.Out.data(0)

  binaryOp_11.io.LeftIO <> ld_10.io.Out.data(0)

  binaryOp_12.io.LeftIO <> binaryOp_11.io.Out(0)

  st_14.io.inData <> binaryOp_12.io.Out(0)

  st_14.io.GepAddr <> Gep_13.io.Out.data(0)

  ld_16.io.GepAddr <> Gep_15.io.Out.data(0)

  icmp_17.io.LeftIO <> ld_16.io.Out.data(0)

  br_18.io.CmpIO <> icmp_17.io.Out(0)

  st_20.io.GepAddr <> Gep_19.io.Out.data(0)

  Gep_0.io.baseAddress <> InputSplitter.io.Out.data("field0")(0)

  Gep_9.io.baseAddress <> InputSplitter.io.Out.data("field0")(1)

  Gep_0.io.idx1 <> InputSplitter.io.Out.data("field1")(0)

  Gep_4.io.idx1 <> InputSplitter.io.Out.data("field1")(1)

  Gep_9.io.idx1 <> InputSplitter.io.Out.data("field1")(2)

  Gep_13.io.idx1 <> InputSplitter.io.Out.data("field1")(3)

  Gep_15.io.idx1 <> InputSplitter.io.Out.data("field1")(4)

  Gep_19.io.idx1 <> InputSplitter.io.Out.data("field1")(5)

  Gep_4.io.baseAddress <> InputSplitter.io.Out.data("field2")(0)

  Gep_13.io.baseAddress <> InputSplitter.io.Out.data("field2")(1)

  Gep_15.io.baseAddress <> InputSplitter.io.Out.data("field2")(2)

  Gep_19.io.baseAddress <> InputSplitter.io.Out.data("field2")(3)

  binaryOp_11.io.RightIO <> InputSplitter.io.Out.data("field3")(0)

  st_5.io.Out(0).ready := true.B

  st_14.io.Out(0).ready := true.B

  st_20.io.Out(0).ready := true.B

  ret_8.io.In.data("field0") <> const5.io.Out(0)


  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_8.io.Out

}

import java.io.{File, FileWriter}
object vector_scale_detach1Main extends App {
  val dir = new File("RTL/vector_scale_detach1") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new vector_scale_detach1DF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}
