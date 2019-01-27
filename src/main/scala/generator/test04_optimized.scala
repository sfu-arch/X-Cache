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

abstract class test04_optimizedDFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32, 32))))
    val MemResp = Flipped(Valid(new MemResp))
    val MemReq = Decoupled(new MemReq)
    val out = Decoupled(new Call(List(32)))
  })
}

class test04_optimizedDF(implicit p: Parameters) extends test04_optimizedDFIO()(p) {


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  val MemCtrl = Module(new UnifiedController(ID = 0, Size = 32, NReads = 2, NWrites = 2)
  (WControl = new WriteMemoryController(NumOps = 2, BaseSize = 2, NumEntries = 2))
  (RControl = new ReadMemoryController(NumOps = 2, BaseSize = 2, NumEntries = 2))
  (RWArbiter = new ReadWriteArbiter()))

  io.MemReq <> MemCtrl.io.MemReq
  MemCtrl.io.MemResp <> io.MemResp

  val InputSplitter = Module(new SplitCallNew(List(2, 2)))
  InputSplitter.io.In <> io.in


  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val Loop_0 = Module(new LoopBlock(NumIns = List(1, 1), NumOuts = 0, NumExits = 1, ID = 0))


  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_entry0 = Module(new BasicBlockNoMaskFastNode3(NumOuts = 2, BID = 0))

  val bb_while_body_preheader1 = Module(new BasicBlockNoMaskFastNode3(NumOuts = 1, BID = 1))

  val bb_while_body2 = Module(new LoopFastHead(NumOuts = 11, NumPhi = 2, BID = 2))

  val bb_while_end_loopexit3 = Module(new BasicBlockNoMaskFastNode3(NumOuts = 1, BID = 3))

  val bb_while_end4 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 1, NumPhi = 0, BID = 4))


  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  %cmp9 = icmp eq i32 %a, %b
  val icmp_cmp90 = Module(new IcmpFastNode(NumOuts = 1, ID = 0, opCode = "eq")(sign = false))

  //  br i1 %cmp9, label %while.end, label %while.body.preheader
  val br_1 = Module(new CBranchFastNodeVariable(ID = 1))

  //  br label %while.body
  val br_2 = Module(new UBranchFastNode(ID = 2))

  //  %b.addr.011 = phi i32 [ %b.addr.1, %while.body ], [ %b, %while.body.preheader ]
  val phi_b_addr_0113 = Module(new PhiFastNode(NumOutputs = 3, ID = 3))

  //  %a.addr.010 = phi i32 [ %a.addr.1, %while.body ], [ %a, %while.body.preheader ]
  val phi_a_addr_0104 = Module(new PhiFastNode(NumOutputs = 3, ID = 4))

  //  %cmp1 = icmp slt i32 %b.addr.011, %a.addr.010
  val icmp_cmp15 = Module(new IcmpFastNode(NumOuts = 2, ID = 5, opCode = "ult")(sign = false))

  //  %sub = select i1 %cmp1, i32 %b.addr.011, i32 0
  val select_sub6 = Module(new SelectFastNode(NumOuts = 1, ID = 6))

  //  %a.addr.1 = sub nsw i32 %a.addr.010, %sub
  val binaryOp_a_addr_17 = Module(new ComputeFastNode(NumOuts = 2, ID = 7, opCode = "sub")(sign = false))

  //  %sub2 = select i1 %cmp1, i32 0, i32 %a.addr.010
  val select_sub28 = Module(new SelectFastNode(NumOuts = 1, ID = 8))

  //  %b.addr.1 = sub nsw i32 %b.addr.011, %sub2
  val binaryOp_b_addr_19 = Module(new ComputeFastNode(NumOuts = 2, ID = 9, opCode = "sub")(sign = false))

  //  %cmp = icmp eq i32 %a.addr.1, %b.addr.1
  val icmp_cmp10 = Module(new IcmpFastNode(NumOuts = 1, ID = 10, opCode = "eq")(sign = false))

  //  br i1 %cmp, label %while.end.loopexit, label %while.body
  val br_11 = Module(new CBranchFastNodeVariable(NumFalse = 2, ID = 11))

  //  br label %while.end
  val br_12 = Module(new UBranchFastNode(ID = 12))

  //  ret void
  val ret_13 = Module(new RetNode2(retTypes = List(), ID = 13))
  //  val ret_15 = Module(new RetNode(retTypes=List(32), ID = 15))


  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i32 0
  val const0 = Module(new ConstFastNode(value = 0, ID = 0))

  //i32 0
  val const1 = Module(new ConstFastNode(value = 0, ID = 1))

  val const2 = Module(new ConstFastNode(value = 1, ID = 1))


  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_entry0.io.predicateIn <> InputSplitter.io.Out.enable

  bb_while_body_preheader1.io.predicateIn <> br_1.io.FalseOutput(0)

  bb_while_body2.io.activate <> Loop_0.io.activate

  bb_while_body2.io.loopBack <> br_11.io.FalseOutput(0)

  bb_while_end_loopexit3.io.predicateIn <> Loop_0.io.endEnable

  bb_while_end4.io.predicateIn(0) <> br_1.io.TrueOutput(0)

  bb_while_end4.io.predicateIn(1) <> br_12.io.Out(0)


  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */


  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */

  Loop_0.io.enable <> br_2.io.Out(0)

  Loop_0.io.latchEnable <> br_11.io.FalseOutput(1)

  Loop_0.io.loopExit(0) <> br_11.io.TrueOutput(0)


  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */


  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

  Loop_0.io.In(0) <> InputSplitter.io.Out.data("field1")(1)

  Loop_0.io.In(1) <> InputSplitter.io.Out.data("field0")(1)


  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */

  phi_b_addr_0113.io.InData(0) <> Loop_0.io.liveIn.elements("field0")(0)

  phi_a_addr_0104.io.InData(0) <> Loop_0.io.liveIn.elements("field1")(0)


  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */


  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  icmp_cmp90.io.enable <> bb_entry0.io.Out(0)

  br_1.io.enable <> bb_entry0.io.Out(1)


  br_2.io.enable <> bb_while_body_preheader1.io.Out(0)


  const0.io.enable <> bb_while_body2.io.Out(0)

  const1.io.enable <> bb_while_body2.io.Out(1)

  phi_b_addr_0113.io.enable <> bb_while_body2.io.Out(2)

  phi_a_addr_0104.io.enable <> bb_while_body2.io.Out(3)

  icmp_cmp15.io.enable <> bb_while_body2.io.Out(4)

  select_sub6.io.enable <> bb_while_body2.io.Out(5)

  binaryOp_a_addr_17.io.enable <> bb_while_body2.io.Out(6)

  select_sub28.io.enable <> bb_while_body2.io.Out(7)

  binaryOp_b_addr_19.io.enable <> bb_while_body2.io.Out(8)

  icmp_cmp10.io.enable <> bb_while_body2.io.Out(9)

  br_11.io.enable <> bb_while_body2.io.Out(10)


  br_12.io.enable <> bb_while_end_loopexit3.io.Out(0)


  ret_13.io.In.enable <> bb_while_end4.io.Out(0)

  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  phi_b_addr_0113.io.Mask <> bb_while_body2.io.MaskBB(0)

  phi_a_addr_0104.io.Mask <> bb_while_body2.io.MaskBB(1)


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

  select_sub6.io.InData2 <> const0.io.Out

  select_sub28.io.InData1 <> const1.io.Out

  br_1.io.CmpIO <> icmp_cmp90.io.Out(0)

  icmp_cmp15.io.LeftIO <> phi_b_addr_0113.io.Out(0)

  select_sub6.io.InData1 <> phi_b_addr_0113.io.Out(1)

  binaryOp_b_addr_19.io.LeftIO <> phi_b_addr_0113.io.Out(2)

  icmp_cmp15.io.RightIO <> phi_a_addr_0104.io.Out(0)

  binaryOp_a_addr_17.io.LeftIO <> phi_a_addr_0104.io.Out(1)

  select_sub28.io.InData2 <> phi_a_addr_0104.io.Out(2)

  select_sub6.io.Select <> icmp_cmp15.io.Out(0)

  select_sub28.io.Select <> icmp_cmp15.io.Out(1)

  binaryOp_a_addr_17.io.RightIO <> select_sub6.io.Out

  phi_a_addr_0104.io.InData(1) <> binaryOp_a_addr_17.io.Out(0)

  icmp_cmp10.io.LeftIO <> binaryOp_a_addr_17.io.Out(1)

  binaryOp_b_addr_19.io.RightIO <> select_sub28.io.Out

  phi_b_addr_0113.io.InData(1) <> binaryOp_b_addr_19.io.Out(0)

  icmp_cmp10.io.RightIO <> binaryOp_b_addr_19.io.Out(1)

  br_11.io.CmpIO <> icmp_cmp10.io.Out(0)

  icmp_cmp90.io.LeftIO <> InputSplitter.io.Out.data("field0")(0)

  icmp_cmp90.io.RightIO <> InputSplitter.io.Out.data("field1")(0)


  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_13.io.Out

}

import java.io.{File, FileWriter}

object test04_optimizedMain extends App {
  val dir = new File("RTL/test04")
  dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new test04_optimizedDF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}
