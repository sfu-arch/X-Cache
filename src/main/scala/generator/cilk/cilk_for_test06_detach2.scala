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

abstract class cilk_for_test06_detach2DFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32, 32, 32, 32, 32))))
    val MemResp = Flipped(Valid(new MemResp))
    val MemReq = Decoupled(new MemReq)
    val out = Decoupled(new Call(List()))
  })
}

class cilk_for_test06_detach2DF(implicit p: Parameters) extends cilk_for_test06_detach2DFIO()(p) {


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  val MemCtrl = Module(new UnifiedController(ID=0, Size=32, NReads=2, NWrites=2)
		 (WControl=new WriteMemoryController(NumOps=2, BaseSize=2, NumEntries=2))
		 (RControl=new ReadMemoryController(NumOps=2, BaseSize=2, NumEntries=2))
		 (RWArbiter=new ReadWriteArbiter()))

  io.MemReq <> MemCtrl.io.MemReq
  MemCtrl.io.MemResp <> io.MemResp

  val InputSplitter = Module(new SplitCallNew(List(1,3,3,1,1)))
  InputSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_my_pfor_body50 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 14, BID = 0))

  val bb_my_pfor_preattach1 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 1))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  %0 = getelementptr inbounds [5 x i32], [5 x i32]* %a.in, i32 %i.0.in, !UID !1
  val Gep_0 = Module(new GepArrayOneNode(NumOuts=1, ID=0)(numByte=4)(size=5))

  //  %1 = getelementptr inbounds [5 x i32], [5 x i32]* %0, i32 0, i32 %j.0.in, !UID !2
  val Gep_1 = Module(new GepArrayTwoNode(NumOuts=1, ID=1)(numByte=4)(size=5))

  //  %2 = load i32, i32* %1, align 4, !UID !3
  val ld_2 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=2, RouteID=0))

  //  %3 = getelementptr inbounds [5 x i32], [5 x i32]* %b.in, i32 %i.0.in, !UID !4
  val Gep_3 = Module(new GepArrayOneNode(NumOuts=1, ID=3)(numByte=4)(size=5))

  //  %4 = getelementptr inbounds [5 x i32], [5 x i32]* %3, i32 0, i32 %j.0.in, !UID !5
  val Gep_4 = Module(new GepArrayTwoNode(NumOuts=1, ID=4)(numByte=4)(size=5))

  //  %5 = load i32, i32* %4, align 4, !UID !6
  val ld_5 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=5, RouteID=1))

  //  %6 = add nsw i32 %2, %5, !UID !7
  val binaryOp_6 = Module(new ComputeNode(NumOuts = 1, ID = 6, opCode = "add")(sign=false))

  //  %7 = getelementptr inbounds [5 x i32], [5 x i32]* %c.in, i32 %i.0.in, !UID !8
  val Gep_7 = Module(new GepArrayOneNode(NumOuts=1, ID=7)(numByte=4)(size=5))

  //  %8 = getelementptr inbounds [5 x i32], [5 x i32]* %7, i32 0, i32 %j.0.in, !UID !9
  val Gep_8 = Module(new GepArrayTwoNode(NumOuts=1, ID=8)(numByte=4)(size=5))

  //  store i32 %6, i32* %8, align 4, !UID !10
  val st_9 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=1, ID=9, RouteID=0))

  //  br label %my_pfor.preattach, !UID !11, !BB_UID !12
  val br_10 = Module(new UBranchNode(ID = 10, NumPredOps = 1))

  //  ret void
  val ret_11 = Module(new RetNode2(retTypes=List(), ID = 11))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i32 0
  val const0 = Module(new ConstNode(value = 0, NumOuts = 1, ID = 0))

  //i32 0
  val const1 = Module(new ConstNode(value = 0, NumOuts = 1, ID = 1))

  //i32 0
  val const2 = Module(new ConstNode(value = 0, NumOuts = 1, ID = 2))



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_my_pfor_body50.io.predicateIn <> InputSplitter.io.Out.enable

  bb_my_pfor_preattach1.io.predicateIn <> br_10.io.Out(0)



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

  const0.io.enable <> bb_my_pfor_body50.io.Out(0)

  const1.io.enable <> bb_my_pfor_body50.io.Out(1)

  const2.io.enable <> bb_my_pfor_body50.io.Out(2)

  Gep_0.io.enable <> bb_my_pfor_body50.io.Out(3)

  Gep_1.io.enable <> bb_my_pfor_body50.io.Out(4)

  ld_2.io.enable <> bb_my_pfor_body50.io.Out(5)

  Gep_3.io.enable <> bb_my_pfor_body50.io.Out(6)

  Gep_4.io.enable <> bb_my_pfor_body50.io.Out(7)

  ld_5.io.enable <> bb_my_pfor_body50.io.Out(8)

  binaryOp_6.io.enable <> bb_my_pfor_body50.io.Out(9)

  Gep_7.io.enable <> bb_my_pfor_body50.io.Out(10)

  Gep_8.io.enable <> bb_my_pfor_body50.io.Out(11)

  st_9.io.enable <> bb_my_pfor_body50.io.Out(12)

  br_10.io.enable <> bb_my_pfor_body50.io.Out(13)
  br_10.io.PredOp(0) <> st_9.io.SuccOp(0)

  ret_11.io.In.enable <> bb_my_pfor_preattach1.io.Out(0)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */



  /* ================================================================== *
   *                   PRINT ALLOCA OFFSET                              *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */

  MemCtrl.io.ReadIn(0) <> ld_2.io.memReq

  ld_2.io.memResp <> MemCtrl.io.ReadOut(0)

  MemCtrl.io.ReadIn(1) <> ld_5.io.memReq

  ld_5.io.memResp <> MemCtrl.io.ReadOut(1)

  MemCtrl.io.WriteIn(0) <> st_9.io.memReq

  st_9.io.memResp <> MemCtrl.io.WriteOut(0)



  /* ================================================================== *
   *                   PRINT SHARED CONNECTIONS                         *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  Gep_1.io.idx1 <> const0.io.Out(0)

  Gep_4.io.idx1 <> const1.io.Out(0)

  Gep_8.io.idx1 <> const2.io.Out(0)

  Gep_1.io.baseAddress <> Gep_0.io.Out.data(0)

  ld_2.io.GepAddr <> Gep_1.io.Out.data(0)

  binaryOp_6.io.LeftIO <> ld_2.io.Out.data(0)

  Gep_4.io.baseAddress <> Gep_3.io.Out.data(0)

  ld_5.io.GepAddr <> Gep_4.io.Out.data(0)

  binaryOp_6.io.RightIO <> ld_5.io.Out.data(0)

  st_9.io.inData <> binaryOp_6.io.Out(0)

  Gep_8.io.baseAddress <> Gep_7.io.Out.data(0)

  st_9.io.GepAddr <> Gep_8.io.Out.data(0)

  Gep_0.io.baseAddress <> InputSplitter.io.Out.data("field0")(0)

  Gep_0.io.idx1 <> InputSplitter.io.Out.data("field1")(0)

  Gep_3.io.idx1 <> InputSplitter.io.Out.data("field1")(1)

  Gep_7.io.idx1 <> InputSplitter.io.Out.data("field1")(2)

  Gep_1.io.idx2 <> InputSplitter.io.Out.data("field2")(0)

  Gep_4.io.idx2 <> InputSplitter.io.Out.data("field2")(1)

  Gep_8.io.idx2 <> InputSplitter.io.Out.data("field2")(2)

  Gep_3.io.baseAddress <> InputSplitter.io.Out.data("field3")(0)

  Gep_7.io.baseAddress <> InputSplitter.io.Out.data("field4")(0)

  st_9.io.Out(0).ready := true.B



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_11.io.Out

}

import java.io.{File, FileWriter}
object cilk_for_test06_detach2Main extends App {
  val dir = new File("RTL/cilk_for_test06_detach2") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new cilk_for_test06_detach2DF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}
