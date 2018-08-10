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

abstract class test02DFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32, 32))))
    val MemResp = Flipped(Valid(new MemResp))
    val MemReq = Decoupled(new MemReq)
    val out = Decoupled(new Call(List(32)))
  })
}

class test02DF(implicit p: Parameters) extends test02DFIO()(p) {


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  val MemCtrl = Module(new UnifiedController(ID=0, Size=32, NReads=2, NWrites=2)
		 (WControl=new WriteMemoryController(NumOps=2, BaseSize=2, NumEntries=2))
		 (RControl=new ReadMemoryController(NumOps=2, BaseSize=2, NumEntries=2))
		 (RWArbiter=new ReadWriteArbiter()))

  io.MemReq <> MemCtrl.io.MemReq
  MemCtrl.io.MemResp <> io.MemResp

  val InputSplitter = Module(new SplitCallNew(List(2,1)))
  InputSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_entry0 = Module(new BasicBlockNoMaskFastNode2(NumOuts = 8, BID = 0))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  %div.mask = and i32 %a, -2
  val binaryOp_div_mask0 = Module(new ComputeFastNode(NumOuts = 1, ID = 0, opCode = "and")(sign=false))

  //  %cmp = icmp eq i32 %div.mask, 8
  val icmp_cmp1 = Module(new IcmpFastNode(NumOuts = 1, ID = 1, opCode = "eq")(sign=false))

  //  %add = add i32 %b, %a
  val binaryOp_add2 = Module(new ComputeFastNode(NumOuts = 1, ID = 2, opCode = "add")(sign=false))

  //  %add. = select i1 %cmp, i32 %add, i32 0
  val select_add_3 = Module(new SelectFastNode(NumOuts = 1, ID = 3))

  //  ret i32 %add.
  val ret_4 = Module(new RetNode(retTypes=List(32), ID = 4))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i32 -2
  val const0 = Module(new ConstFastNode(value = -2, ID = 0))

  //i32 8
  val const1 = Module(new ConstFastNode(value = 8, ID = 1))

  //i32 0
  val const2 = Module(new ConstFastNode(value = 0, ID = 2))



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_entry0.io.predicateIn <> InputSplitter.io.Out.enable



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

  const2.io.enable <> bb_entry0.io.Out(2)

  binaryOp_div_mask0.io.enable <> bb_entry0.io.Out(3)

  icmp_cmp1.io.enable <> bb_entry0.io.Out(4)

  binaryOp_add2.io.enable <> bb_entry0.io.Out(5)

  select_add_3.io.enable <> bb_entry0.io.Out(6)

  ret_4.io.enable <> bb_entry0.io.Out(7)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */



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

  select_add_3.io.InData2 <> const2.io.Out

  icmp_cmp1.io.LeftIO <> binaryOp_div_mask0.io.Out

  select_add_3.io.Select <> icmp_cmp1.io.Out

  select_add_3.io.InData1 <> binaryOp_add2.io.Out

  ret_4.io.In.data("field0") <> select_add_3.io.Out

  binaryOp_div_mask0.io.LeftIO <> InputSplitter.io.Out.data("field0")(0)

  binaryOp_add2.io.RightIO <> InputSplitter.io.Out.data("field0")(1)

  binaryOp_add2.io.LeftIO <> InputSplitter.io.Out.data("field1")(0)



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_4.io.Out

}

import java.io.{File, FileWriter}
object test02Main extends App {
  val dir = new File("RTL/test02") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new test02DF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}
