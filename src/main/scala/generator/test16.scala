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

abstract class test16DFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32))))
    val MemResp = Flipped(Valid(new MemResp))
    val MemReq = Decoupled(new MemReq)
    val out = Decoupled(new Call(List()))
  })
}

class test16DF(implicit p: Parameters) extends test16DFIO()(p) {


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  val MemCtrl = Module(new UnifiedController(ID=0, Size=32, NReads=2, NWrites=2)
		 (WControl=new WriteMemoryController(NumOps=2, BaseSize=2, NumEntries=2))
		 (RControl=new ReadMemoryController(NumOps=2, BaseSize=2, NumEntries=2))
		 (RWArbiter=new ReadWriteArbiter()))

  io.MemReq <> MemCtrl.io.MemReq
  MemCtrl.io.MemResp <> io.MemResp

  val InputSplitter = Module(new SplitCallNew(List(1)))
  InputSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val Loop_0 = Module(new LoopBlock(NumIns=List(), NumOuts = 0, NumExits=1, ID = 0))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_entry0 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 0))

  val bb_for_cond1 = Module(new LoopHead(NumOuts = 5, NumPhi=1, BID = 1))

  val bb_for_body2 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 2))

  val bb_for_inc3 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 3, BID = 3))

  val bb_for_end4 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 4))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  br label %for.cond
  val br_0 = Module(new UBranchNode(ID = 0))

  //  %i.0 = phi i32 [ 0, %entry ], [ %inc, %for.inc ]
  val phi_i_01 = Module(new PhiNode(NumInputs = 2, NumOuts = 2, ID = 1))

  //  %cmp = icmp ult i32 %i.0, 5
  val icmp_cmp2 = Module(new IcmpNode(NumOuts = 1, ID = 2, opCode = "ult")(sign=false))

  //  br i1 %cmp, label %for.body, label %for.end
  val br_3 = Module(new CBranchNode(ID = 3))

  //  br label %for.inc
  val br_4 = Module(new UBranchNode(ID = 4))

  //  %inc = add i32 %i.0, 1
  val binaryOp_inc5 = Module(new ComputeNode(NumOuts = 1, ID = 5, opCode = "add")(sign=false))

  //  br label %for.cond
  val br_6 = Module(new UBranchNode(NumOuts=2, ID = 6))

  //  ret void
  val ret_7 = Module(new RetNode2(retTypes=List(), ID = 7))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i32 0
  val const0 = Module(new ConstNode(value = 0, NumOuts = 1, ID = 0))

  //i32 5
  val const1 = Module(new ConstNode(value = 5, NumOuts = 1, ID = 1))

  //i32 1
  val const2 = Module(new ConstNode(value = 1, NumOuts = 1, ID = 2))



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_entry0.io.predicateIn <> InputSplitter.io.Out.enable

  bb_for_cond1.io.activate <> Loop_0.io.activate

  bb_for_cond1.io.loopBack <> br_6.io.Out(0)

  bb_for_body2.io.predicateIn <> br_3.io.Out(0)

  bb_for_inc3.io.predicateIn <> br_4.io.Out(0)

  bb_for_end4.io.predicateIn <> Loop_0.io.endEnable



  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */

  Loop_0.io.enable <> br_0.io.Out(0)

  Loop_0.io.latchEnable <> br_6.io.Out(1)

  Loop_0.io.loopExit(0) <> br_3.io.Out(1)



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

  br_0.io.enable <> bb_entry0.io.Out(0)


  const0.io.enable <> bb_for_cond1.io.Out(0)

  const1.io.enable <> bb_for_cond1.io.Out(1)

  phi_i_01.io.enable <> bb_for_cond1.io.Out(2)

  icmp_cmp2.io.enable <> bb_for_cond1.io.Out(3)

  br_3.io.enable <> bb_for_cond1.io.Out(4)


  br_4.io.enable <> bb_for_body2.io.Out(0)


  const2.io.enable <> bb_for_inc3.io.Out(0)

  binaryOp_inc5.io.enable <> bb_for_inc3.io.Out(1)

  br_6.io.enable <> bb_for_inc3.io.Out(2)


  ret_7.io.In.enable <> bb_for_end4.io.Out(0)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  phi_i_01.io.Mask <> bb_for_cond1.io.MaskBB(0)



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

  phi_i_01.io.InData(0) <> const0.io.Out(0)

  icmp_cmp2.io.RightIO <> const1.io.Out(0)

  binaryOp_inc5.io.RightIO <> const2.io.Out(0)

  icmp_cmp2.io.LeftIO <> phi_i_01.io.Out(0)

  binaryOp_inc5.io.LeftIO <> phi_i_01.io.Out(1)

  br_3.io.CmpIO <> icmp_cmp2.io.Out(0)

  phi_i_01.io.InData(1) <> binaryOp_inc5.io.Out(0)



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_7.io.Out

}

import java.io.{File, FileWriter}
object test16Main extends App {
  val dir = new File("RTL/test16") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new test16DF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}
