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

abstract class test12DFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32))))
    val call_5_out = Decoupled(new Call(List(32)))
    val call_5_in = Flipped(Decoupled(new Call(List(32))))
    val MemResp = Flipped(Valid(new MemResp))
    val MemReq = Decoupled(new MemReq)
    val out = Decoupled(new Call(List(32)))
  })
}

class test12DF(implicit p: Parameters) extends test12DFIO()(p) {


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

  val Loop_0 = Module(new LoopBlock(NumIns=List(1), NumOuts = 1, NumExits=1, ID = 0))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_entry0 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 0))

  val bb_for_cond1 = Module(new LoopHead(NumOuts = 6, NumPhi=2, BID = 1))

  val bb_for_body2 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 3, BID = 2))

  val bb_for_inc3 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 3, BID = 3))

  val bb_for_end4 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 4))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  br label %for.cond
  val br_0 = Module(new UBranchNode(ID = 0))

  //  %foo.0 = phi i32 [ %j, %entry ], [ %add, %for.inc ]
  val phi_foo_01 = Module(new PhiNode(NumInputs = 2, NumOuts = 3, ID = 1))

  //  %i.0 = phi i32 [ 0, %entry ], [ %inc, %for.inc ]
  val phi_i_02 = Module(new PhiNode(NumInputs = 2, NumOuts = 2, ID = 2))

  //  %cmp = icmp ult i32 %i.0, 5
  val icmp_cmp3 = Module(new IcmpNode(NumOuts = 1, ID = 3, opCode = "ult")(sign=false))

  //  br i1 %cmp, label %for.body, label %for.end
  val br_4 = Module(new CBranchNode(ID = 4))

  //  %call = call i32 @test12_inner(i32 %foo.0)
  val call_5_out = Module(new CallOutNode(ID = 5, NumSuccOps = 0, argTypes = List(32)))

  val call_5_in = Module(new CallInNode(ID = 5, argTypes = List(32,32)))

  //  %add = add i32 %foo.0, %call
  val binaryOp_add6 = Module(new ComputeNode(NumOuts = 1, ID = 6, opCode = "add")(sign=false))

  //  br label %for.inc
  val br_7 = Module(new CBranchNode(ID = 7))

  //  %inc = add i32 %i.0, 1
  val binaryOp_inc8 = Module(new ComputeNode(NumOuts = 1, ID = 8, opCode = "add")(sign=false))

  //  br label %for.cond, !llvm.loop !7
  val br_9 = Module(new UBranchNode(NumOuts=2, ID = 9))

  //  ret i32 %foo.0
  val ret_10 = Module(new RetNode(retTypes=List(32), ID = 10))



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

  bb_for_cond1.io.loopBack <> br_9.io.Out(0)

  bb_for_body2.io.predicateIn <> br_4.io.Out(0)

  bb_for_inc3.io.predicateIn <> br_7.io.Out(0)

  bb_for_end4.io.predicateIn <> Loop_0.io.endEnable



  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */

  Loop_0.io.enable <> br_0.io.Out(0)

  Loop_0.io.latchEnable <> br_9.io.Out(1)

  Loop_0.io.loopExit(0) <> br_4.io.Out(1)



  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

  Loop_0.io.In(0) <> InputSplitter.io.Out.data("field0")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */

  phi_foo_01.io.InData(0) <> Loop_0.io.liveIn.data("field0")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */

  Loop_0.io.liveOut(0) <> phi_foo_01.io.Out(2)



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  br_0.io.enable <> bb_entry0.io.Out(0)


  const0.io.enable <> bb_for_cond1.io.Out(0)

  const1.io.enable <> bb_for_cond1.io.Out(1)

  phi_foo_01.io.enable <> bb_for_cond1.io.Out(2)

  phi_i_02.io.enable <> bb_for_cond1.io.Out(3)

  icmp_cmp3.io.enable <> bb_for_cond1.io.Out(4)

  br_4.io.enable <> bb_for_cond1.io.Out(5)


  call_5_in.io.enable.enq(ControlBundle.active())

  call_5_out.io.enable <> bb_for_body2.io.Out(0)

  binaryOp_add6.io.enable <> bb_for_body2.io.Out(1)

  br_7.io.enable <> bb_for_body2.io.Out(2)


  const2.io.enable <> bb_for_inc3.io.Out(0)

  binaryOp_inc8.io.enable <> bb_for_inc3.io.Out(1)

  br_9.io.enable <> bb_for_inc3.io.Out(2)


  ret_10.io.enable <> bb_for_end4.io.Out(0)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  phi_foo_01.io.Mask <> bb_for_cond1.io.MaskBB(0)

  phi_i_02.io.Mask <> bb_for_cond1.io.MaskBB(1)



  /* ================================================================== *
   *                   PRINT ALLOCA OFFSET                              *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  phi_i_02.io.InData(0) <> const0.io.Out(0)

  icmp_cmp3.io.RightIO <> const1.io.Out(0)

  binaryOp_inc8.io.RightIO <> const2.io.Out(0)

  call_5_out.io.In("field0") <> phi_foo_01.io.Out(0)

  binaryOp_add6.io.LeftIO <> phi_foo_01.io.Out(1)

  icmp_cmp3.io.LeftIO <> phi_i_02.io.Out(0)

  binaryOp_inc8.io.LeftIO <> phi_i_02.io.Out(1)

  br_4.io.CmpIO <> icmp_cmp3.io.Out(0)

  binaryOp_add6.io.RightIO <> call_5_in.io.Out.data("field0")

  br_7.io.CmpIO <> call_5_in.io.Out.data("field1")

  phi_foo_01.io.InData(1) <> binaryOp_add6.io.Out(0)

  phi_i_02.io.InData(1) <> binaryOp_inc8.io.Out(0)

  ret_10.io.In.data("field0") <> Loop_0.io.Out(0)



  /* ================================================================== *
   *                   PRINTING CALLIN AND CALLOUT INTERFACE            *
   * ================================================================== */

  call_5_in.io.In <> io.call_5_in

  io.call_5_out <> call_5_out.io.Out(0)

//  br_7.io.PredOp(0) <> call_5_in.io.Out.enable
  call_5_in.io.Out.enable.ready := true.B



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_10.io.Out

}

import java.io.{File, FileWriter}
object test12Main extends App {
  val dir = new File("RTL/test12") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new test12DF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}
