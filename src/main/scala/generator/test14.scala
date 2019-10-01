package dandelion.generator

import chisel3._
import chisel3.util._
import chisel3.Module._
import chisel3.testers._
import chisel3.iotesters._
import dandelion.accel._
import dandelion.arbiters._
import dandelion.config._
import dandelion.control._
import dandelion.fpu._
import dandelion.interfaces._
import dandelion.junctions._
import dandelion.loop._
import dandelion.memory._
import dandelion.memory.stack._
import dandelion.node._
import muxes._
import org.scalatest._
import regfile._
import util._


  /* ================================================================== *
   *                   PRINTING PORTS DEFINITION                        *
   * ================================================================== */

abstract class test14DFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32, 32, 32, 32))))
    val MemResp = Flipped(Valid(new MemResp))
    val MemReq = Decoupled(new MemReq)
    val out = Decoupled(new Call(List(32)))
  })
}

class test14DF(implicit p: Parameters) extends test14DFIO()(p) {


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  val MemCtrl = Module(new UnifiedController(ID = 0, Size = 32, NReads = 1, NWrites = 1)
  (WControl = new WriteMemoryController(NumOps = 1, BaseSize = 2, NumEntries = 2))
  (RControl = new ReadMemoryController(NumOps = 1, BaseSize = 2, NumEntries = 2))
  (RWArbiter = new ReadWriteArbiter()))

  io.MemReq <> MemCtrl.io.MemReq
  MemCtrl.io.MemResp <> io.MemResp

  val InputSplitter = Module(new SplitCallNew(List(1, 1, 1, 1)))
  InputSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_entry0 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 4, BID = 0))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  %0 = load i32, i32* %a, align 4, !dbg !23, !tbaa !24, !UID !28
  val ld_0 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 0, RouteID = 0))

  //  %add = add i32 %n, %c, !dbg !29, !UID !30
  val binaryOp_add1 = Module(new ComputeNode(NumOuts = 1, ID = 1, opCode = "add")(sign = false))

  //  %add1 = add i32 %add, %0, !dbg !31, !UID !32
  val binaryOp_add12 = Module(new ComputeNode(NumOuts = 2, ID = 2, opCode = "add")(sign = false))

  //  store i32 %add1, i32* %b, align 4, !dbg !33, !tbaa !24, !UID !34
  val st_3 = Module(new UnTypStore(NumPredOps = 0, NumSuccOps = 1, ID = 3, RouteID = 0))

  //  ret i32 %add1, !dbg !35, !UID !36, !BB_UID !37
  val ret_4 = Module(new RetNode2(retTypes = List(32), ID = 4))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_entry0.io.predicateIn(0) <> InputSplitter.io.Out.enable



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE LOOP                     *
   * ================================================================== */



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
   *                   LOOP LIVE OUT DEPENDENCIES                       *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP CARRY DEPENDENCIES                          *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP DATA CARRY DEPENDENCIES                     *
   * ================================================================== */



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  ld_0.io.enable <> bb_entry0.io.Out(0)


  binaryOp_add1.io.enable <> bb_entry0.io.Out(1)


  binaryOp_add12.io.enable <> bb_entry0.io.Out(2)


  st_3.io.enable <> bb_entry0.io.Out(3)


  //ret_4.io.In.enable <> bb_entry0.io.Out(4)
  ret_4.io.In.enable <> st_3.io.SuccOp(0)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */



  /* ================================================================== *
   *                   PRINT ALLOCA OFFSET                              *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */

  MemCtrl.io.ReadIn(0) <> ld_0.io.memReq

  ld_0.io.memResp <> MemCtrl.io.ReadOut(0)

  MemCtrl.io.WriteIn(0) <> st_3.io.memReq

  st_3.io.memResp <> MemCtrl.io.WriteOut(0)



  /* ================================================================== *
   *                   PRINT SHARED CONNECTIONS                         *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  binaryOp_add12.io.RightIO <> ld_0.io.Out(0)

  binaryOp_add12.io.LeftIO <> binaryOp_add1.io.Out(0)

  st_3.io.inData <> binaryOp_add12.io.Out(0)

  ret_4.io.In.data("field0") <> binaryOp_add12.io.Out(1)

  ld_0.io.GepAddr <> InputSplitter.io.Out.data.elements("field0")(0)

  st_3.io.GepAddr <> InputSplitter.io.Out.data.elements("field1")(0)

  binaryOp_add1.io.RightIO <> InputSplitter.io.Out.data.elements("field2")(0)

  binaryOp_add1.io.LeftIO <> InputSplitter.io.Out.data.elements("field3")(0)

  st_3.io.Out(0).ready := true.B




  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_4.io.Out

}

import java.io.{File, FileWriter}

object test14Top extends App {
  val dir = new File("RTL/test14Top");
  dir.mkdirs
  implicit val p = Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new test14DF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}
