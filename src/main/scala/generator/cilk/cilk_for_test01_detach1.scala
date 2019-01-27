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

abstract class cilk_for_test01_detach1DFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32, 32, 32))))
    val MemResp = Flipped(Valid(new MemResp))
    val MemReq = Decoupled(new MemReq)
    val out = Decoupled(new Call(List(32)))
  })
}

class cilk_for_test01_detach1DF(implicit p: Parameters) extends cilk_for_test01_detach1DFIO()(p) {


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  val MemCtrl = Module(new UnifiedController(ID = 0, Size = 32, NReads = 1, NWrites = 1)
  (WControl = new WriteMemoryController(NumOps = 1, BaseSize = 2, NumEntries = 2))
  (RControl = new ReadMemoryController(NumOps = 1, BaseSize = 2, NumEntries = 2))
  (RWArbiter = new ReadWriteArbiter()))

  io.MemReq <> MemCtrl.io.MemReq
  MemCtrl.io.MemResp <> io.MemResp

  val InputSplitter = Module(new SplitCallNew(List(1, 2, 1)))
  InputSplitter.io.In <> io.in


  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */


  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_my_pfor_body0 = Module(new BasicBlockNoMaskFastNode3(NumOuts = 7, BID = 0))

  val bb_my_pfor_preattach1 = Module(new BasicBlockNoMaskFastNode3(NumOuts = 1, BID = 1))


  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  %0 = getelementptr inbounds i32, i32* %a.in, i32 %i.0.in, !UID !1
  val Gep_0 = Module(new GepArrayOneNode(NumOuts = 1, ID = 0)(numByte = 4)(size = 1))

  //  %1 = load i32, i32* %0, align 4, !UID !2
  val ld_1 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 1, NumOuts = 1, ID = 1, RouteID = 0))

  //  %2 = mul i32 %1, 2, !UID !3
  val binaryOp_2 = Module(new ComputeFastNode(NumOuts = 1, ID = 2, opCode = "mul")(sign = false))

  //  %3 = getelementptr inbounds i32, i32* %b.in, i32 %i.0.in, !UID !4
  val Gep_3 = Module(new GepArrayOneNode(NumOuts = 1, ID = 3)(numByte = 4)(size = 1))

  //  store i32 %2, i32* %3, align 4, !UID !5
  val st_4 = Module(new UnTypStore(NumPredOps = 1, NumSuccOps = 0, ID = 4, RouteID = 0))

  //  br label %my_pfor.preattach, !UID !6, !BB_UID !7
  val br_5 = Module(new UBranchFastNode(ID = 5))

  //  ret void
  val ret_6 = Module(new RetNode2(retTypes = List(32), ID = 6))


  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i32 2
  val const0 = Module(new ConstNode(value = 2, NumOuts = 1, ID = 0))


  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_my_pfor_body0.io.predicateIn <> InputSplitter.io.Out.enable

  bb_my_pfor_preattach1.io.predicateIn <> br_5.io.Out(0)


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

  binaryOp_2.io.enable <> bb_my_pfor_body0.io.Out(3)

  Gep_3.io.enable <> bb_my_pfor_body0.io.Out(4)

  st_4.io.enable <> bb_my_pfor_body0.io.Out(5)

  br_5.io.enable <> bb_my_pfor_body0.io.Out(6)


  ret_6.io.In.enable <> bb_my_pfor_preattach1.io.Out(0)


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

  MemCtrl.io.WriteIn(0) <> st_4.io.memReq

  st_4.io.memResp <> MemCtrl.io.WriteOut(0)


  /* ================================================================== *
   *                   PRINT SHARED CONNECTIONS                         *
   * ================================================================== */


  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  binaryOp_2.io.RightIO <> const0.io.Out(0)

  ld_1.io.GepAddr <> Gep_0.io.Out(0)

  binaryOp_2.io.LeftIO <> ld_1.io.Out(0)

  st_4.io.inData <> binaryOp_2.io.Out(0)

  st_4.io.GepAddr <> Gep_3.io.Out(0)

  Gep_0.io.baseAddress <> InputSplitter.io.Out.data.elements("field0")(0)

  Gep_0.io.idx1 <> InputSplitter.io.Out.data.elements("field1")(0)

  Gep_3.io.idx1 <> InputSplitter.io.Out.data.elements("field1")(1)

  Gep_3.io.baseAddress <> InputSplitter.io.Out.data.elements("field2")(0)

  //  st_4.io.Out(0).ready := true.B

  st_4.io.PredOp(0) <> ld_1.io.SuccOp(0) //manual


  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  ret_6.io.In.elements("field0") <> st_4.io.Out(0) // manual
  io.out <> ret_6.io.Out

}

import java.io.{File, FileWriter}

object cilk_for_test01_detach1Main extends App {
  val dir = new File("RTL/cilk_for_test01_detach1");
  dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new cilk_for_test01_detach1DF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}
