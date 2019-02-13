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

abstract class test03DFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32, 32, 32))))
    val MemResp = Flipped(Valid(new MemResp))
    val MemReq = Decoupled(new MemReq)
    val out = Decoupled(new Call(List(32)))
  })
}

class test03DF(implicit p: Parameters) extends test03DFIO()(p) {


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  //Remember if there is no mem operation io memreq/memresp should be grounded
  io.MemReq <> DontCare
  io.MemResp <> DontCare

  val InputSplitter = Module(new SplitCallNew(List(2, 1, 2)))
  InputSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val Loop_0 = Module(new LoopBlockNode(NumIns = List(2, 1, 1), NumOuts = List(1), NumCarry = List(1, 1), NumExits = 1, ID = 0))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_0 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 3, BID = 0))

  val bb_1 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 9, NumPhi = 2, BID = 1))

  val bb_2 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 2, NumPhi = 1, BID = 2))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  %4 = icmp sgt i32 %2, 0, !UID !3
  val icmp_0 = Module(new IcmpNode(NumOuts = 1, ID = 0, opCode = "ugt")(sign = false))

  //  br i1 %4, label %5, label %12, !UID !4, !BB_UID !5
  val br_1 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 1))

  //  %6 = phi i32 [ %10, %5 ], [ 0, %3 ], !UID !6
  val phi2 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 1, ID = 2, Res = true))

  //  %7 = phi i32 [ %9, %5 ], [ %0, %3 ], !UID !7
  val phi3 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 1, ID = 3, Res = true))

  //  %8 = add i32 %7, %0, !UID !8
  val binaryOp_4 = Module(new ComputeNode(NumOuts = 1, ID = 4, opCode = "add")(sign = false))

  //  %9 = mul i32 %8, %1, !UID !9
  val binaryOp_5 = Module(new ComputeNode(NumOuts = 2, ID = 5, opCode = "mul")(sign = false))

  //  %10 = add nuw nsw i32 %6, 1, !UID !10
  val binaryOp_6 = Module(new ComputeNode(NumOuts = 2, ID = 6, opCode = "add")(sign = false))

  //  %11 = icmp eq i32 %10, %2, !UID !11
  val icmp_7 = Module(new IcmpNode(NumOuts = 1, ID = 7, opCode = "eq")(sign = false))

  //  br i1 %11, label %12, label %5, !UID !12, !BB_UID !13
  val br_8 = Module(new CBranchNodeVariableLoop(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 8))

  //  %13 = phi i32 [ %0, %3 ], [ %9, %5 ], !UID !14
  val phi9 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 1, ID = 9))

  //  ret i32 %13, !UID !15, !BB_UID !16
  val ret_10 = Module(new RetNode2(retTypes = List(32), ID = 10))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i32 0
  val const0 = Module(new ConstFastNode(value = 0, ID = 0))

  //i32 0
  val const1 = Module(new ConstFastNode(value = 0, ID = 1))

  //i32 1
  val const2 = Module(new ConstFastNode(value = 1, ID = 2))



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_0.io.predicateIn(0) <> InputSplitter.io.Out.enable

  bb_1.io.predicateIn(0) <> Loop_0.io.activate_loop_start

  bb_1.io.predicateIn(1) <> Loop_0.io.activate_loop_back

  bb_2.io.predicateIn(0) <> br_1.io.FalseOutput(0)

  bb_2.io.predicateIn(1) <> Loop_0.io.loopExit(0)



  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */

  Loop_0.io.enable <> br_1.io.TrueOutput(0)

  Loop_0.io.loopBack(0) <> br_8.io.FalseOutput(0)

  Loop_0.io.loopFinish(0) <> br_8.io.TrueOutput(0)



  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

  Loop_0.io.InLiveIn(0) <> InputSplitter.io.Out.data.elements("field0")(0)

  Loop_0.io.InLiveIn(1) <> InputSplitter.io.Out.data.elements("field1")(0)

  Loop_0.io.InLiveIn(2) <> InputSplitter.io.Out.data.elements("field2")(1)



  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */

  phi3.io.InData(1) <> Loop_0.io.OutLiveIn.elements("field0")(0)

  binaryOp_4.io.RightIO <> Loop_0.io.OutLiveIn.elements("field0")(1)

  binaryOp_5.io.RightIO <> Loop_0.io.OutLiveIn.elements("field1")(0)

  icmp_7.io.RightIO <> Loop_0.io.OutLiveIn.elements("field2")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */

  Loop_0.io.CarryDepenIn(0) <> binaryOp_5.io.Out(0)

  Loop_0.io.CarryDepenIn(1) <> binaryOp_6.io.Out(0)

  Loop_0.io.InLiveOut(0) <> binaryOp_5.io.Out(1)


  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  const0.io.enable <> bb_0.io.Out(0)

  icmp_0.io.enable <> bb_0.io.Out(1)

  br_1.io.enable <> bb_0.io.Out(2)


  const1.io.enable <> bb_1.io.Out(0)

  const2.io.enable <> bb_1.io.Out(1)

  phi2.io.enable <> bb_1.io.Out(2)

  phi3.io.enable <> bb_1.io.Out(3)

  binaryOp_4.io.enable <> bb_1.io.Out(4)

  binaryOp_5.io.enable <> bb_1.io.Out(5)

  binaryOp_6.io.enable <> bb_1.io.Out(6)

  icmp_7.io.enable <> bb_1.io.Out(7)

  br_8.io.enable <> bb_1.io.Out(8)


  phi9.io.enable <> bb_2.io.Out(0)

  ret_10.io.In.enable <> bb_2.io.Out(1)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  phi2.io.Mask <> bb_1.io.MaskBB(0)

  phi3.io.Mask <> bb_1.io.MaskBB(1)

  phi9.io.Mask <> bb_2.io.MaskBB(0)



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

  icmp_0.io.RightIO <> const0.io.Out

  phi2.io.InData(1) <> const1.io.Out

  binaryOp_6.io.RightIO <> const2.io.Out

  br_1.io.CmpIO <> icmp_0.io.Out(0)

  binaryOp_6.io.LeftIO <> phi2.io.Out(0)

  binaryOp_4.io.LeftIO <> phi3.io.Out(0)

  binaryOp_5.io.LeftIO <> binaryOp_4.io.Out(0)

  phi3.io.InData(0) <> Loop_0.io.CarryDepenOut.elements("field0")(0)

  phi2.io.InData(0) <> Loop_0.io.CarryDepenOut.elements("field1")(0)

  icmp_7.io.LeftIO <> binaryOp_6.io.Out(1)

  br_8.io.CmpIO <> icmp_7.io.Out(0)

  ret_10.io.In.data("field0") <> phi9.io.Out(0)

  phi9.io.InData(1) <> Loop_0.io.OutLiveOut.elements("field0")(0)

  phi9.io.InData(0) <> InputSplitter.io.Out.data("field0")(1)

  icmp_0.io.LeftIO <> InputSplitter.io.Out.data("field2")(0)





  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_10.io.Out

}

import java.io.{File, FileWriter}

object test03Main extends App {
  val dir = new File("RTL/test03");
  dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new test03DF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}
