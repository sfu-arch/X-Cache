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

abstract class cilk_saxpy_detach1DFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32, 32, 32, 32))))
    val MemResp = Flipped(Valid(new MemResp))
    val MemReq = Decoupled(new MemReq)
    val out = Decoupled(new Call(List()))
  })
}

class cilk_saxpy_detach1DF(implicit p: Parameters) extends cilk_saxpy_detach1DFIO()(p) {


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  val MemCtrl = Module(new UnifiedController(ID = 0, Size = 32, NReads = 2, NWrites = 2)
  (WControl = new WriteMemoryController(NumOps = 2, BaseSize = 2, NumEntries = 2))
  (RControl = new ReadMemoryController(NumOps = 2, BaseSize = 2, NumEntries = 2))
  (RWArbiter = new ReadWriteArbiter()))

  io.MemReq <> MemCtrl.io.MemReq
  MemCtrl.io.MemResp <> io.MemResp

  val InputSplitter = Module(new SplitCallNew(List(1, 3, 1, 2)))
  InputSplitter.io.In <> io.in


  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */


  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_my_pfor_body0 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 9, BID = 0))

  val bb_my_pfor_preattach1 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 1))


  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  %0 = getelementptr inbounds i32, i32* %x.in, i32 %i.0.in, !UID !1
  //    val Gep_0 = Module(new GepArrayOneNode(NumOuts = 1, ID = 0)(numByte = 4)(size = 1))
  //  val Gep_0 = Module(new GepNode(NumIns = 1, NumOuts = 1, ArraySize = List(List(4)), ID = 0))
  val Gep_0 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 0)(ElementSize = 4, ArraySize = List()))

  //  %1 = load i32, i32* %0, align 4, !UID !2
  val ld_1 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 1, RouteID = 0))

  //  %2 = mul nsw i32 %a.in, %1, !UID !3
  val binaryOp_2 = Module(new ComputeNode(NumOuts = 1, ID = 2, opCode = "mul")(sign = false))

  //  %3 = getelementptr inbounds i32, i32* %y.in, i32 %i.0.in, !UID !4
  val Gep_3 = Module(new GepArrayOneNode(NumOuts = 1, ID = 3)(numByte = 4)(size = 1))

  //  %4 = load i32, i32* %3, align 4, !UID !5
  val ld_4 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 4, RouteID = 1))

  //  %5 = add nsw i32 %2, %4, !UID !6
  val binaryOp_5 = Module(new ComputeNode(NumOuts = 1, ID = 5, opCode = "add")(sign = false))

  //  %6 = getelementptr inbounds i32, i32* %y.in, i32 %i.0.in, !UID !7
  val Gep_6 = Module(new GepArrayOneNode(NumOuts = 1, ID = 6)(numByte = 4)(size = 1))

  //  store i32 %5, i32* %6, align 4, !UID !8
  val st_7 = Module(new UnTypStore(NumPredOps = 0, NumSuccOps = 1, ID = 7, RouteID = 0))

  //  br label %my_pfor.preattach, !UID !9, !BB_UID !10
  val br_8 = Module(new UBranchNode(ID = 8, NumPredOps = 1))

  //  ret void
  val ret_9 = Module(new RetNode2(retTypes = List(), ID = 9))


  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */


  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_my_pfor_body0.io.predicateIn <> InputSplitter.io.Out.enable

  bb_my_pfor_preattach1.io.predicateIn <> br_8.io.Out(0)


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

  Gep_0.io.enable <> bb_my_pfor_body0.io.Out(0)

  ld_1.io.enable <> bb_my_pfor_body0.io.Out(1)

  binaryOp_2.io.enable <> bb_my_pfor_body0.io.Out(2)

  Gep_3.io.enable <> bb_my_pfor_body0.io.Out(3)

  ld_4.io.enable <> bb_my_pfor_body0.io.Out(4)

  binaryOp_5.io.enable <> bb_my_pfor_body0.io.Out(5)

  Gep_6.io.enable <> bb_my_pfor_body0.io.Out(6)

  st_7.io.enable <> bb_my_pfor_body0.io.Out(7)

  br_8.io.enable <> bb_my_pfor_body0.io.Out(8)
  br_8.io.PredOp(0) <> st_7.io.SuccOp(0)

  ret_9.io.In.enable <> bb_my_pfor_preattach1.io.Out(0)


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

  MemCtrl.io.ReadIn(1) <> ld_4.io.memReq

  ld_4.io.memResp <> MemCtrl.io.ReadOut(1)

  MemCtrl.io.WriteIn(0) <> st_7.io.memReq

  st_7.io.memResp <> MemCtrl.io.WriteOut(0)


  /* ================================================================== *
   *                   PRINT SHARED CONNECTIONS                         *
   * ================================================================== */


  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  ld_1.io.GepAddr <> Gep_0.io.Out(0)

  binaryOp_2.io.RightIO <> ld_1.io.Out(0)

  binaryOp_5.io.LeftIO <> binaryOp_2.io.Out(0)

  ld_4.io.GepAddr <> Gep_3.io.Out(0)

  binaryOp_5.io.RightIO <> ld_4.io.Out(0)

  st_7.io.inData <> binaryOp_5.io.Out(0)

  st_7.io.GepAddr <> Gep_6.io.Out(0)

  //  Gep_0.io.baseAddress <> InputSplitter.io.Out.data("field0")(0)
  Gep_0.io.baseAddress <> InputSplitter.io.Out.data("field0")(0)

  //  Gep_0.io.idx1 <> InputSplitter.io.Out.data("field1")(0)
  Gep_0.io.idx(0) <> InputSplitter.io.Out.data("field1")(0)

  Gep_3.io.idx1 <> InputSplitter.io.Out.data("field1")(1)

  Gep_6.io.idx1 <> InputSplitter.io.Out.data("field1")(2)

  binaryOp_2.io.LeftIO <> InputSplitter.io.Out.data("field2")(0)

  Gep_3.io.baseAddress <> InputSplitter.io.Out.data("field3")(0)

  Gep_6.io.baseAddress <> InputSplitter.io.Out.data("field3")(1)

  st_7.io.Out(0).ready := true.B


  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_9.io.Out

}

import java.io.{File, FileWriter}

object cilk_saxpy_detach1Main extends App {
  val dir = new File("RTL/cilk_saxpy_detach1");
  dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new cilk_saxpy_detach1DF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}
