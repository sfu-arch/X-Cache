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

abstract class cilk_for_test04_detach1DFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32,32,32,32))))
    val MemResp = Flipped(Valid(new MemResp))
    val MemReq = Decoupled(new MemReq)
    val out = Decoupled(new Call(List(32)))
  })
}

class cilk_for_test04_detach1DF(implicit p: Parameters) extends cilk_for_test04_detach1DFIO()(p) {


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  val MemCtrl = Module(new UnifiedController(ID=0, Size=32, NReads=2, NWrites=2)
		 (WControl=new WriteMemoryController(NumOps=2, BaseSize=2, NumEntries=2))
		 (RControl=new ReadMemoryController(NumOps=2, BaseSize=2, NumEntries=2))
		 (RWArbiter=new ReadWriteArbiter()))

  io.MemReq <> MemCtrl.io.MemReq
  MemCtrl.io.MemResp <> io.MemResp

  val InputSplitter = Module(new SplitCallNew(List(1,3,1,1)))
  InputSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_my_pfor_body0 = Module(new BasicBlockNoMaskFastNode3(NumOuts = 8, BID = 0))

  val bb_my_pfor_preattach1 = Module(new BasicBlockNoMaskFastNode3(NumOuts = 1, BID = 1))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  %0 = getelementptr inbounds i32, i32* %a.in, i32 %i.0.in, !UID !6
  val Gep_0 = Module(new GepArrayOneNode(NumOuts=1, ID=0)(numByte=4)(size=1))

  //  %1 = load i32, i32* %0, align 4, !UID !7
  val ld_1 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=1, RouteID=0))

  //  %2 = getelementptr inbounds i32, i32* %b.in, i32 %i.0.in, !UID !8
  val Gep_2 = Module(new GepArrayOneNode(NumOuts=1, ID=2)(numByte=4)(size=1))

  //  %3 = load i32, i32* %2, align 4, !UID !9
  val ld_3 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=3, RouteID=1))

  //  %4 = add i32 %1, %3, !UID !10
  val binaryOp_4 = Module(new ComputeFastNode(NumOuts = 1, ID = 4, opCode = "add")(sign = false))

  //  %5 = getelementptr inbounds i32, i32* %c.in, i32 %i.0.in, !UID !11
  val Gep_5 = Module(new GepArrayOneNode(NumOuts=1, ID=5)(numByte=4)(size=1))

  //  store i32 %4, i32* %5, align 4, !UID !12
  val st_6 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, ID=6, RouteID=0))

  //  br label %my_pfor.preattach, !UID !13, !BB_UID !14
  val br_7 = Module(new UBranchFastNode(ID = 7))

  //  ret void
  val ret_8 = Module(new RetNode2(retTypes = List(32), ID = 8))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_my_pfor_body0.io.predicateIn <> InputSplitter.io.Out.enable

  bb_my_pfor_preattach1.io.predicateIn <> br_7.io.Out(0)



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

  Gep_2.io.enable <> bb_my_pfor_body0.io.Out(2)

  ld_3.io.enable <> bb_my_pfor_body0.io.Out(3)

  binaryOp_4.io.enable <> bb_my_pfor_body0.io.Out(4)

  Gep_5.io.enable <> bb_my_pfor_body0.io.Out(5)

  st_6.io.enable <> bb_my_pfor_body0.io.Out(6)

  br_7.io.enable <> bb_my_pfor_body0.io.Out(7)


  ret_8.io.In.enable <> bb_my_pfor_preattach1.io.Out(0)




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

  MemCtrl.io.ReadIn(1) <> ld_3.io.memReq

  ld_3.io.memResp <> MemCtrl.io.ReadOut(1)

  MemCtrl.io.WriteIn(0) <> st_6.io.memReq

  st_6.io.memResp <> MemCtrl.io.WriteOut(0)



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  ld_1.io.GepAddr <> Gep_0.io.Out(0)

  binaryOp_4.io.LeftIO <> ld_1.io.Out(0)

  ld_3.io.GepAddr <> Gep_2.io.Out(0)

  binaryOp_4.io.RightIO <> ld_3.io.Out(0)

  st_6.io.inData <> binaryOp_4.io.Out(0)

  st_6.io.GepAddr <> Gep_5.io.Out(0)

  Gep_0.io.baseAddress <> InputSplitter.io.Out.data("field0")(0)

  Gep_0.io.idx1 <> InputSplitter.io.Out.data.elements("field1")(0)

  Gep_2.io.idx1 <> InputSplitter.io.Out.data.elements("field1")(1)

  Gep_5.io.idx1 <> InputSplitter.io.Out.data.elements("field1")(2)

  Gep_2.io.baseAddress <> InputSplitter.io.Out.data("field2")(0)

  Gep_5.io.baseAddress <> InputSplitter.io.Out.data("field3")(0)

//  st_6.io.Out(0).ready := true.B



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  ret_8.io.In.data("field0") <> st_6.io.Out(0)  // manual
  io.out <> ret_8.io.Out

}

import java.io.{File, FileWriter}
object cilk_for_test04_detach1Main extends App {
  val dir = new File("RTL/cilk_for_test04_detach1") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new cilk_for_test04_detach1DF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}
