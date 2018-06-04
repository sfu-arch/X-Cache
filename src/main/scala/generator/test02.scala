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
    val in = Flipped(Decoupled(new Call(List(32,32))))
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

  val entry = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 5, BID = 0))

  val if_then = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 2, BID = 1))

  val if_end = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 3, NumPhi=1, BID = 2))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  %div = udiv i32 %a, 2
  val div = Module(new ComputeNode(NumOuts = 1, ID = 0, opCode = "udiv")(sign=false))

  //  %cmp = icmp eq i32 %div, 4
  val cmp = Module(new IcmpNode(NumOuts = 1, ID = 1, opCode = "eq")(sign=false))

  //  br i1 %cmp, label %if.then, label %if.end
  val br_2 = Module(new CBranchNode(ID = 2))

  //  %add = add i32 %a, %b
  val add = Module(new ComputeNode(NumOuts = 1, ID = 3, opCode = "add")(sign=false))

  //  br label %if.end
  val br_4 = Module(new UBranchNode(ID = 4))

  //  %sum.0 = phi i32 [ %add, %if.then ], [ 0, %entry ]
  val sum_0 = Module(new PhiNode(NumInputs = 2, NumOuts = 1, ID = 5))

  //  ret i32 %sum.0
  val ret_6 = Module(new RetNode(retTypes=List(32), ID = 6))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i32 2
  val const0 = Module(new ConstNode(value = 2, NumOuts = 1, ID = 0))

  //i32 4
  val const1 = Module(new ConstNode(value = 4, NumOuts = 1, ID = 1))

  //i32 0
  val const2 = Module(new ConstNode(value = 0, NumOuts = 1, ID = 2))



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  entry.io.predicateIn <> InputSplitter.io.Out.enable

  if_then.io.predicateIn <> br_2.io.Out(0)

  if_end.io.predicateIn(0) <> br_2.io.Out(1)

  if_end.io.predicateIn(1) <> br_4.io.Out(0)



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
   *                   LOOP DATA OUTPUT DEPENDENCIES                    *
   * ================================================================== */



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  const0.io.enable <> entry.io.Out(0)

  const1.io.enable <> entry.io.Out(1)

  div.io.enable <> entry.io.Out(2)

  cmp.io.enable <> entry.io.Out(3)

  br_2.io.enable <> entry.io.Out(4)


  add.io.enable <> if_then.io.Out(0)

  br_4.io.enable <> if_then.io.Out(1)


  const2.io.enable <> if_end.io.Out(0)

  sum_0.io.enable <> if_end.io.Out(1)

  ret_6.io.enable <> if_end.io.Out(2)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  sum_0.io.Mask <> if_end.io.MaskBB(0)



  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  div.io.RightIO <> const0.io.Out(0)

  cmp.io.RightIO <> const1.io.Out(0)

  sum_0.io.InData(1) <> const2.io.Out(0)

  cmp.io.LeftIO <> div.io.Out(0)

  br_2.io.CmpIO <> cmp.io.Out(0)

  sum_0.io.InData(0) <> add.io.Out(0)

  ret_6.io.In.data("field0") <> sum_0.io.Out(0)

  div.io.LeftIO <> InputSplitter.io.Out.data("field0")(0)

  add.io.LeftIO <> InputSplitter.io.Out.data("field0")(1)

  add.io.RightIO <> InputSplitter.io.Out.data("field1")(0)



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_6.io.Out

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
