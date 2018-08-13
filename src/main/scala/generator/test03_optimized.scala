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

abstract class test03_optimizedDFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32, 32, 32))))
    val MemResp = Flipped(Valid(new MemResp))
    val MemReq = Decoupled(new MemReq)
    val out = Decoupled(new Call(List(32)))
  })
}

class test03_optimizedDF(implicit p: Parameters) extends test03DFIO()(p) {


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  val MemCtrl = Module(new UnifiedController(ID = 0, Size = 32, NReads = 2, NWrites = 2)
  (WControl = new WriteMemoryController(NumOps = 2, BaseSize = 2, NumEntries = 2))
  (RControl = new ReadMemoryController(NumOps = 2, BaseSize = 2, NumEntries = 2))
  (RWArbiter = new ReadWriteArbiter()))

  io.MemReq <> MemCtrl.io.MemReq
  MemCtrl.io.MemResp <> io.MemResp

  val InputSplitter = Module(new SplitCallNew(List(2, 1, 2)))
  InputSplitter.io.In <> io.in


  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val Loop_0 = Module(new LoopBlock(NumIns = List(2, 1, 1), NumOuts = 1, NumExits = 1, ID = 0))


  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_entry0 = Module(new BasicBlockNoMaskFastNode2(NumOuts = 3, BID = 0))

  val bb_for_body_preheader1 = Module(new BasicBlockNoMaskFastNode2(NumOuts = 1, BID = 1))

  val bb_for_body2 = Module(new LoopHead(NumOuts = 9, NumPhi = 2, BID = 2))

  val bb_for_end_loopexit3 = Module(new BasicBlockNoMaskFastNode2(NumOuts = 1, BID = 3))

  val bb_for_end4 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 2, NumPhi = 1, BID = 4))


  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  %cmp5 = icmp sgt i32 %n, 0
  val icmp_cmp50 = Module(new IcmpFastNode(NumOuts = 1, ID = 0, opCode = "ugt")(sign = false))

  //  br i1 %cmp5, label %for.body.preheader, label %for.end
  val br_1 = Module(new CBranchNode(ID = 1))

  //  br label %for.body
  val br_2 = Module(new UBranchFastNode(ID = 2))

  //  %i.07 = phi i32 [ %inc, %for.body ], [ 0, %for.body.preheader ]
  val phi_i_073 = Module(new PhiNode(NumInputs = 2, NumOuts = 1, ID = 3))

  //  %sum.06 = phi i32 [ %mul, %for.body ], [ %a, %for.body.preheader ]
  val phi_sum_064 = Module(new PhiNode(NumInputs = 2, NumOuts = 1, ID = 4))

  //  %add = add i32 %sum.06, %a
  val binaryOp_add5 = Module(new ComputeFastNode(NumOuts = 1, ID = 5, opCode = "add")(sign = false))

  //  %mul = mul i32 %add, %b
  val binaryOp_mul6 = Module(new ComputeFastNode(NumOuts = 2, ID = 6, opCode = "mul")(sign = false))

  //  %inc = add nuw nsw i32 %i.07, 1
  val binaryOp_inc7 = Module(new ComputeFastNode(NumOuts = 2, ID = 7, opCode = "add")(sign = false))

  //  %exitcond = icmp eq i32 %inc, %n
  val icmp_exitcond8 = Module(new IcmpFastNode(NumOuts = 1, ID = 8, opCode = "eq")(sign = false))

  //  br i1 %exitcond, label %for.end.loopexit, label %for.body
  val br_9 = Module(new CBranchNode(ID = 9))

  val tmp_br = Module(new UBranchNode(ID = 100, NumOuts = 2))

  //  br label %for.end
  val br_10 = Module(new UBranchFastNode(ID = 10))

  //  %sum.0.lcssa = phi i32 [ %a, %entry ], [ %mul, %for.end.loopexit ]
  val phi_sum_0_lcssa11 = Module(new PhiNode(NumInputs = 2, NumOuts = 1, ID = 11))

  //  ret i32 %sum.0.lcssa
  val ret_12 = Module(new RetNode2(retTypes = List(32), ID = 12))


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

  bb_entry0.io.predicateIn <> InputSplitter.io.Out.enable

  bb_for_body_preheader1.io.predicateIn <> br_1.io.Out(0)

  bb_for_body2.io.activate <> Loop_0.io.activate

  tmp_br.io.enable <> br_9.io.Out(1)
  bb_for_body2.io.loopBack <> tmp_br.io.Out(0)

  bb_for_end_loopexit3.io.predicateIn <> Loop_0.io.endEnable

  bb_for_end4.io.predicateIn(0) <> br_1.io.Out(1)

  bb_for_end4.io.predicateIn(1) <> br_10.io.Out(0)


  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */


  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */

  Loop_0.io.enable <> br_2.io.Out(0)

  Loop_0.io.latchEnable <> tmp_br.io.Out(1)

  Loop_0.io.loopExit(0) <> br_9.io.Out(0)


  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */


  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

  Loop_0.io.In(0) <> InputSplitter.io.Out.data("field0")(0)

  Loop_0.io.In(1) <> InputSplitter.io.Out.data("field1")(0)

  Loop_0.io.In(2) <> InputSplitter.io.Out.data("field2")(1)


  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */

  phi_sum_064.io.InData(0) <> Loop_0.io.liveIn.data("field0")(0)

  binaryOp_add5.io.RightIO <> Loop_0.io.liveIn.data("field0")(1)

  binaryOp_mul6.io.RightIO <> Loop_0.io.liveIn.data("field1")(0)

  icmp_exitcond8.io.RightIO <> Loop_0.io.liveIn.data("field2")(0)


  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */

  Loop_0.io.liveOut(0) <> binaryOp_mul6.io.Out(1)


  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  const0.io.enable <> bb_entry0.io.Out(0)

  icmp_cmp50.io.enable <> bb_entry0.io.Out(1)

  br_1.io.enable <> bb_entry0.io.Out(2)


  br_2.io.enable <> bb_for_body_preheader1.io.Out(0)


  const1.io.enable <> bb_for_body2.io.Out(0)

  const2.io.enable <> bb_for_body2.io.Out(1)

  phi_i_073.io.enable <> bb_for_body2.io.Out(2)

  phi_sum_064.io.enable <> bb_for_body2.io.Out(3)

  binaryOp_add5.io.enable <> bb_for_body2.io.Out(4)

  binaryOp_mul6.io.enable <> bb_for_body2.io.Out(5)

  binaryOp_inc7.io.enable <> bb_for_body2.io.Out(6)

  icmp_exitcond8.io.enable <> bb_for_body2.io.Out(7)

  br_9.io.enable <> bb_for_body2.io.Out(8)


  br_10.io.enable <> bb_for_end_loopexit3.io.Out(0)


  phi_sum_0_lcssa11.io.enable <> bb_for_end4.io.Out(0)

  ret_12.io.In.enable <> bb_for_end4.io.Out(1)


  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  phi_i_073.io.Mask <> bb_for_body2.io.MaskBB(0)

  phi_sum_064.io.Mask <> bb_for_body2.io.MaskBB(1)

  phi_sum_0_lcssa11.io.Mask <> bb_for_end4.io.MaskBB(0)


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

  icmp_cmp50.io.RightIO <> const0.io.Out

  phi_i_073.io.InData(0) <> const1.io.Out

  binaryOp_inc7.io.RightIO <> const2.io.Out

  br_1.io.CmpIO <> icmp_cmp50.io.Out

  binaryOp_inc7.io.LeftIO <> phi_i_073.io.Out(0)

  binaryOp_add5.io.LeftIO <> phi_sum_064.io.Out(0)

  binaryOp_mul6.io.LeftIO <> binaryOp_add5.io.Out(0)

  phi_sum_064.io.InData(1) <> binaryOp_mul6.io.Out(0)

  phi_i_073.io.InData(1) <> binaryOp_inc7.io.Out(0)

  icmp_exitcond8.io.LeftIO <> binaryOp_inc7.io.Out(1)

  br_9.io.CmpIO <> icmp_exitcond8.io.Out

  ret_12.io.In.data("field0") <> phi_sum_0_lcssa11.io.Out(0)

  phi_sum_0_lcssa11.io.InData(0) <> Loop_0.io.Out(0)

  phi_sum_0_lcssa11.io.InData(1) <> InputSplitter.io.Out.data("field0")(1)

  icmp_cmp50.io.LeftIO <> InputSplitter.io.Out.data("field2")(0)


  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_12.io.Out

}

import java.io.{File, FileWriter}

object test03_optimizedMain extends App {
  val dir = new File("RTL/test03");
  dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new test03_optimizedDF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}
