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

abstract class test17DFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32, 32, 32))))
    val MemResp = Flipped(Valid(new MemResp))
    val MemReq = Decoupled(new MemReq)
    val out = Decoupled(new Call(List(32)))
  })
}

class test17DF(implicit p: Parameters) extends test17DFIO()(p) {


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  val MemCtrl = Module(new UnifiedController(ID = 0, Size = 32, NReads = 2, NWrites = 2)
  (WControl = new WriteMemoryController(NumOps = 2, BaseSize = 2, NumEntries = 2))
  (RControl = new ReadMemoryController(NumOps = 2, BaseSize = 2, NumEntries = 2))
  (RWArbiter = new ReadWriteArbiter()))

  io.MemReq <> MemCtrl.io.MemReq
  MemCtrl.io.MemResp <> io.MemResp

  val InputSplitter = Module(new SplitCallNew(List(8, 4, 5)))
  InputSplitter.io.In <> io.in


  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */


  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_entry0 = Module(new BasicBlockNoMaskFastNode3(NumOuts = 5, BID = 0))

  val bb_if_then1 = Module(new BasicBlockNoMaskFastNode3(NumOuts = 8, BID = 1))

  val bb_if_then42 = Module(new BasicBlockNoMaskFastNode3(NumOuts = 2, BID = 2))

  val bb_if_else3 = Module(new BasicBlockNoMaskFastNode3(NumOuts = 3, BID = 3))

  val bb_if_else104 = Module(new BasicBlockNoMaskFastNode3(NumOuts = 5, BID = 4))

  val bb_if_then145 = Module(new BasicBlockNoMaskFastNode3(NumOuts = 4, BID = 5))

  val bb_if_else186 = Module(new BasicBlockNoMaskFastNode3(NumOuts = 5, BID = 6))

  val bb_if_end237 = Module(new BasicBlockNode(NumInputs = 4, NumOuts = 2, NumPhi = 1, BID = 7))


  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  %div.mask = and i32 %a, -2
  val binaryOp_div_mask0 = Module(new ComputeFastNode(NumOuts = 1, ID = 0, opCode = "and")(sign = false))

  //  %cmp = icmp eq i32 %div.mask, 8
  val icmp_cmp1 = Module(new IcmpFastNode(NumOuts = 1, ID = 1, opCode = "eq")(sign = false))

  //  br i1 %cmp, label %if.then, label %if.else10
  val br_2 = Module(new CBranchFastNodeVariable(ID = 2))

  //  %add = add i32 %b, %a
  val binaryOp_add3 = Module(new ComputeFastNode(NumOuts = 2, ID = 3, opCode = "add")(sign = false))

  //  %add1 = add i32 %add, %c
  val binaryOp_add14 = Module(new ComputeFastNode(NumOuts = 1, ID = 4, opCode = "add")(sign = false))

  //  %a.off = add i32 %a, -3
  val binaryOp_a_off5 = Module(new ComputeFastNode(NumOuts = 1, ID = 5, opCode = "add")(sign = false))

  //  %0 = icmp ult i32 %a.off, 3
  val icmp_6 = Module(new IcmpFastNode(NumOuts = 1, ID = 6, opCode = "ult")(sign = false))

  //  %mul = mul i32 %add1, %c
  val binaryOp_mul7 = Module(new ComputeFastNode(NumOuts = 2, ID = 7, opCode = "mul")(sign = false))

  //  br i1 %0, label %if.then4, label %if.else
  val br_8 = Module(new CBranchFastNodeVariable(ID = 8))

  //  %add6 = add i32 %mul, %add
  val binaryOp_add69 = Module(new ComputeFastNode(NumOuts = 1, ID = 9, opCode = "add")(sign = false))

  //  br label %if.end23
  val br_10 = Module(new UBranchFastNode(ID = 10))

  //  %mul7 = mul i32 %b, %a
  val binaryOp_mul711 = Module(new ComputeFastNode(NumOuts = 1, ID = 11, opCode = "mul")(sign = false))

  //  %add9 = add i32 %mul, %mul7
  val binaryOp_add912 = Module(new ComputeFastNode(NumOuts = 1, ID = 12, opCode = "add")(sign = false))

  //  br label %if.end23
  val br_13 = Module(new UBranchFastNode(ID = 13))

  //  %sub = sub i32 %a, %b
  val binaryOp_sub14 = Module(new ComputeFastNode(NumOuts = 1, ID = 14, opCode = "sub")(sign = false))

  //  %add11 = add i32 %sub, %c
  val binaryOp_add1115 = Module(new ComputeFastNode(NumOuts = 2, ID = 15, opCode = "add")(sign = false))

  //  %1 = icmp ult i32 %a, 5
  val icmp_16 = Module(new IcmpFastNode(NumOuts = 1, ID = 16, opCode = "ult")(sign = false))

  //  br i1 %1, label %if.then14, label %if.else18
  val br_17 = Module(new CBranchFastNodeVariable(ID = 17))

  //  %mul15 = mul i32 %add11, %b
  val binaryOp_mul1518 = Module(new ComputeFastNode(NumOuts = 1, ID = 18, opCode = "mul")(sign = false))

  //  %sub16 = add i32 %c, %a
  val binaryOp_sub1619 = Module(new ComputeFastNode(NumOuts = 1, ID = 19, opCode = "add")(sign = false))

  //  %add17 = sub i32 %sub16, %mul15
  val binaryOp_add1720 = Module(new ComputeFastNode(NumOuts = 1, ID = 20, opCode = "sub")(sign = false))

  //  br label %if.end23
  val br_21 = Module(new UBranchFastNode(ID = 21))

  //  %mul19 = mul i32 %a, 12
  val binaryOp_mul1922 = Module(new ComputeFastNode(NumOuts = 1, ID = 22, opCode = "mul")(sign = false))

  //  %mul20 = mul i32 %mul19, %add11
  val binaryOp_mul2023 = Module(new ComputeFastNode(NumOuts = 1, ID = 23, opCode = "mul")(sign = false))

  //  %add21 = add i32 %mul20, %c
  val binaryOp_add2124 = Module(new ComputeFastNode(NumOuts = 1, ID = 24, opCode = "add")(sign = false))

  //  br label %if.end23
  val br_25 = Module(new UBranchFastNode(ID = 25))

  //  %sum.0 = phi i32 [ %add6, %if.then4 ], [ %add9, %if.else ], [ %add17, %if.then14 ], [ %add21, %if.else18 ]
  val phi_sum_026 = Module(new PhiFastNode(NumInputs = 4, NumOutputs = 1, ID = 26))

  //  ret i32 %sum.0
  val ret_27 = Module(new RetNode2(retTypes = List(32), ID = 27))


  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i32 -2
  val const0 = Module(new ConstFastNode(value = -2, ID = 0))

  //i32 8
  val const1 = Module(new ConstFastNode(value = 8, ID = 1))

  //i32 -3
  val const2 = Module(new ConstFastNode(value = -3, ID = 2))

  //i32 3
  val const3 = Module(new ConstFastNode(value = 3, ID = 3))

  //i32 5
  val const4 = Module(new ConstFastNode(value = 5, ID = 4))

  //i32 12
  val const5 = Module(new ConstFastNode(value = 12, ID = 5))


  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_entry0.io.predicateIn <> InputSplitter.io.Out.enable

  //  bb_if_then1.io.predicateIn <> br_2.io.Out(0
  bb_if_then1.io.predicateIn <> br_2.io.TrueOutput(0)

  bb_if_then42.io.predicateIn <> br_8.io.TrueOutput(0)

  bb_if_else3.io.predicateIn <> br_8.io.FalseOutput(0)

  bb_if_else104.io.predicateIn <> br_2.io.FalseOutput(0)

  bb_if_then145.io.predicateIn <> br_17.io.TrueOutput(0)

  bb_if_else186.io.predicateIn <> br_17.io.FalseOutput(0)

  bb_if_end237.io.predicateIn(0) <> br_10.io.Out(0)

  bb_if_end237.io.predicateIn(1) <> br_13.io.Out(0)

  bb_if_end237.io.predicateIn(2) <> br_21.io.Out(0)

  bb_if_end237.io.predicateIn(3) <> br_25.io.Out(0)


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

  const0.io.enable <> bb_entry0.io.Out(0)

  const1.io.enable <> bb_entry0.io.Out(1)

  binaryOp_div_mask0.io.enable <> bb_entry0.io.Out(2)

  icmp_cmp1.io.enable <> bb_entry0.io.Out(3)

  br_2.io.enable <> bb_entry0.io.Out(4)


  const2.io.enable <> bb_if_then1.io.Out(0)

  const3.io.enable <> bb_if_then1.io.Out(1)

  binaryOp_add3.io.enable <> bb_if_then1.io.Out(2)

  binaryOp_add14.io.enable <> bb_if_then1.io.Out(3)

  binaryOp_a_off5.io.enable <> bb_if_then1.io.Out(4)

  icmp_6.io.enable <> bb_if_then1.io.Out(5)

  binaryOp_mul7.io.enable <> bb_if_then1.io.Out(6)

  br_8.io.enable <> bb_if_then1.io.Out(7)


  binaryOp_add69.io.enable <> bb_if_then42.io.Out(0)

  br_10.io.enable <> bb_if_then42.io.Out(1)


  binaryOp_mul711.io.enable <> bb_if_else3.io.Out(0)

  binaryOp_add912.io.enable <> bb_if_else3.io.Out(1)

  br_13.io.enable <> bb_if_else3.io.Out(2)


  const4.io.enable <> bb_if_else104.io.Out(0)

  binaryOp_sub14.io.enable <> bb_if_else104.io.Out(1)

  binaryOp_add1115.io.enable <> bb_if_else104.io.Out(2)

  icmp_16.io.enable <> bb_if_else104.io.Out(3)

  br_17.io.enable <> bb_if_else104.io.Out(4)


  binaryOp_mul1518.io.enable <> bb_if_then145.io.Out(0)

  binaryOp_sub1619.io.enable <> bb_if_then145.io.Out(1)

  binaryOp_add1720.io.enable <> bb_if_then145.io.Out(2)

  br_21.io.enable <> bb_if_then145.io.Out(3)


  const5.io.enable <> bb_if_else186.io.Out(0)

  binaryOp_mul1922.io.enable <> bb_if_else186.io.Out(1)

  binaryOp_mul2023.io.enable <> bb_if_else186.io.Out(2)

  binaryOp_add2124.io.enable <> bb_if_else186.io.Out(3)

  br_25.io.enable <> bb_if_else186.io.Out(4)


  phi_sum_026.io.enable <> bb_if_end237.io.Out(0)

  ret_27.io.In.enable <> bb_if_end237.io.Out(1)


  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  phi_sum_026.io.Mask <> bb_if_end237.io.MaskBB(0)


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

  binaryOp_div_mask0.io.RightIO <> const0.io.Out

  icmp_cmp1.io.RightIO <> const1.io.Out

  binaryOp_a_off5.io.RightIO <> const2.io.Out

  icmp_6.io.RightIO <> const3.io.Out

  icmp_16.io.RightIO <> const4.io.Out

  binaryOp_mul1922.io.RightIO <> const5.io.Out

  icmp_cmp1.io.LeftIO <> binaryOp_div_mask0.io.Out(0)

  br_2.io.CmpIO <> icmp_cmp1.io.Out(0)

  binaryOp_add14.io.LeftIO <> binaryOp_add3.io.Out(0)

  binaryOp_add69.io.RightIO <> binaryOp_add3.io.Out(1)

  binaryOp_mul7.io.LeftIO <> binaryOp_add14.io.Out(0)

  icmp_6.io.LeftIO <> binaryOp_a_off5.io.Out(0)

  br_8.io.CmpIO <> icmp_6.io.Out(0)

  binaryOp_add69.io.LeftIO <> binaryOp_mul7.io.Out(0)

  binaryOp_add912.io.LeftIO <> binaryOp_mul7.io.Out(1)

  phi_sum_026.io.InData(0) <> binaryOp_add69.io.Out(0)

  binaryOp_add912.io.RightIO <> binaryOp_mul711.io.Out(0)

  phi_sum_026.io.InData(2) <> binaryOp_add912.io.Out(0)

  binaryOp_add1115.io.LeftIO <> binaryOp_sub14.io.Out(0)

  binaryOp_mul1518.io.LeftIO <> binaryOp_add1115.io.Out(0)

  binaryOp_mul2023.io.RightIO <> binaryOp_add1115.io.Out(1)

  br_17.io.CmpIO <> icmp_16.io.Out(0)

  binaryOp_add1720.io.RightIO <> binaryOp_mul1518.io.Out(0)

  binaryOp_add1720.io.LeftIO <> binaryOp_sub1619.io.Out(0)

  phi_sum_026.io.InData(1) <> binaryOp_add1720.io.Out(0)

  binaryOp_mul2023.io.LeftIO <> binaryOp_mul1922.io.Out(0)

  binaryOp_add2124.io.LeftIO <> binaryOp_mul2023.io.Out(0)

  phi_sum_026.io.InData(3) <> binaryOp_add2124.io.Out(0)

  ret_27.io.In.data("field0") <> phi_sum_026.io.Out(0)

  binaryOp_div_mask0.io.LeftIO <> InputSplitter.io.Out.data("field0")(0)

  binaryOp_add3.io.RightIO <> InputSplitter.io.Out.data("field0")(1)

  binaryOp_a_off5.io.LeftIO <> InputSplitter.io.Out.data("field0")(2)

  binaryOp_mul711.io.RightIO <> InputSplitter.io.Out.data("field0")(3)

  binaryOp_sub14.io.LeftIO <> InputSplitter.io.Out.data("field0")(4)

  icmp_16.io.LeftIO <> InputSplitter.io.Out.data("field0")(5)

  binaryOp_sub1619.io.RightIO <> InputSplitter.io.Out.data("field0")(6)

  binaryOp_mul1922.io.LeftIO <> InputSplitter.io.Out.data("field0")(7)

  binaryOp_add3.io.LeftIO <> InputSplitter.io.Out.data("field1")(0)

  binaryOp_mul711.io.LeftIO <> InputSplitter.io.Out.data("field1")(1)

  binaryOp_sub14.io.RightIO <> InputSplitter.io.Out.data("field1")(2)

  binaryOp_mul1518.io.RightIO <> InputSplitter.io.Out.data("field1")(3)

  binaryOp_add14.io.RightIO <> InputSplitter.io.Out.data("field2")(0)

  binaryOp_mul7.io.RightIO <> InputSplitter.io.Out.data("field2")(1)

  binaryOp_add1115.io.RightIO <> InputSplitter.io.Out.data("field2")(2)

  binaryOp_sub1619.io.LeftIO <> InputSplitter.io.Out.data("field2")(3)

  binaryOp_add2124.io.RightIO <> InputSplitter.io.Out.data("field2")(4)


  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_27.io.Out

}

import java.io.{File, FileWriter}

object test17Main extends App {
  val dir = new File("RTL/test17");
  dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new test17DF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}
