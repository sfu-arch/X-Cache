package dandelion.generator

import chisel3._
import chisel3.util._
import chisel3.Module._
import chisel3.testers._
import chisel3.iotesters._
import dandelion.accel._
import dandelion.arbiters._
import chipsalliance.rocketchip.config._
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

//abstract class test14DFIO(implicit val p: Parameters) extends Module with HasAccelParams {
//  val io = IO(new Bundle {
//    val in = Flipped(Decoupled(new Call(List(32, 32, 32))))
//    val MemResp = Flipped(Valid(new MemResp))
//    val MemReq = Decoupled(new MemReq)
//    val out = Decoupled(new Call(List(32)))
//  })
//}
//
//class test14DF(implicit p: Parameters) extends test14DFIO()(p) {

class test14DF(ArgsIn: Seq[Int] = List(32, 32, 32), Returns: Seq[Int] = List(32))
              (implicit p: Parameters) extends DandelionAccelModule(ArgsIn, Returns) {

  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  val MemCtrl = Module(new UnifiedController(ID = 0, Size = 32, NReads = 2, NWrites = 1)
  (WControl = new WriteMemoryController(NumOps = 1, BaseSize = 2, NumEntries = 2))
  (RControl = new ReadMemoryController(NumOps = 2, BaseSize = 2, NumEntries = 2))
  (RWArbiter = new ReadWriteArbiter()))

  io.MemReq <> MemCtrl.io.MemReq
  MemCtrl.io.MemResp <> io.MemResp

  val InputSplitter = Module(new SplitCallNew(List(2, 1, 1)))
  InputSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val Loop_0 = Module(new LoopBlockNode(NumIns = List(1, 1, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 0))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_entry0 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 0))

  val bb_for_cond_cleanup1 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 4, BID = 1))

  val bb_for_body2 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 12, NumPhi = 1, BID = 2))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  br label %for.body, !dbg !24, !UID !25, !BB_UID !26
  val br_0 = Module(new UBranchNode(ID = 0))

  //  %arrayidx2 = getelementptr inbounds i32, i32* %a, i32 4, !dbg !27, !UID !28
  val Gep_arrayidx21 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 1)(ElementSize = 4, ArraySize = List()))

  //  %0 = load i32, i32* %arrayidx2, align 4, !dbg !27, !tbaa !29, !UID !33
  val ld_2 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 2, RouteID = 0))

  //  ret i32 %0, !dbg !34, !UID !35, !BB_UID !36
  val ret_3 = Module(new RetNode2(retTypes = List(32), ID = 3))

  //  %i.08 = phi i32 [ 0, %entry ], [ %inc, %for.body ], !UID !37
  val phii_084 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 3, ID = 4, Res = true))

  //  %arrayidx = getelementptr inbounds i32, i32* %a, i32 %i.08, !dbg !38, !UID !41
  val Gep_arrayidx5 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 5)(ElementSize = 4, ArraySize = List()))

  //  %1 = load i32, i32* %arrayidx, align 4, !dbg !38, !tbaa !29, !UID !42
  val ld_6 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 6, RouteID = 1))

  //  %add = add i32 %1, %c, !dbg !43, !UID !44
  val binaryOp_add7 = Module(new ComputeNode(NumOuts = 1, ID = 7, opCode = "add")(sign = false, Debug = false))

  //  %arrayidx1 = getelementptr inbounds i32, i32* %b, i32 %i.08, !dbg !45, !UID !46
  val Gep_arrayidx18 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 8)(ElementSize = 4, ArraySize = List()))

  //  store i32 %add, i32* %arrayidx1, align 4, !dbg !47, !tbaa !29, !UID !48
  val st_9 = Module(new UnTypStore(NumPredOps = 0, NumSuccOps = 1, ID = 9, RouteID = 0))

  //  %inc = add nuw nsw i32 %i.08, 1, !dbg !49, !UID !50
  val binaryOp_inc10 = Module(new ComputeNode(NumOuts = 2, ID = 10, opCode = "add")(sign = false, Debug = false))

  //  %exitcond = icmp eq i32 %inc, 5, !dbg !51, !UID !52
  val icmp_exitcond11 = Module(new ComputeNode(NumOuts = 1, ID = 11, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond, label %for.cond.cleanup, label %for.body, !dbg !24, !llvm.loop !53, !UID !55, !BB_UID !56
  val br_12 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 1, ID = 12))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i32 4
  val const0 = Module(new ConstFastNode(value = 4, ID = 0))

  //i32 0
  val const1 = Module(new ConstFastNode(value = 0, ID = 1))

  //i32 1
  val const2 = Module(new ConstFastNode(value = 1, ID = 2))

  //i32 5
  val const3 = Module(new ConstFastNode(value = 5, ID = 3))



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_entry0.io.predicateIn(0) <> InputSplitter.io.Out.enable



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE LOOP                     *
   * ================================================================== */

  bb_for_cond_cleanup1.io.predicateIn(0) <> Loop_0.io.loopExit(0)

  bb_for_body2.io.predicateIn(1) <> Loop_0.io.activate_loop_start

  bb_for_body2.io.predicateIn(0) <> Loop_0.io.activate_loop_back



  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */

  Loop_0.io.enable <> br_0.io.Out(0)

  Loop_0.io.loopBack(0) <> br_12.io.FalseOutput(0)

  Loop_0.io.loopFinish(0) <> br_12.io.TrueOutput(0)



  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

  Loop_0.io.InLiveIn(0) <> InputSplitter.io.Out.data.elements("field0")(0)

  Loop_0.io.InLiveIn(1) <> InputSplitter.io.Out.data.elements("field2")(0)

  Loop_0.io.InLiveIn(2) <> InputSplitter.io.Out.data.elements("field1")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */

  Gep_arrayidx5.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field0")(0)

  binaryOp_add7.io.RightIO <> Loop_0.io.OutLiveIn.elements("field1")(0)

  Gep_arrayidx18.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field2")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP LIVE OUT DEPENDENCIES                       *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP CARRY DEPENDENCIES                          *
   * ================================================================== */

  Loop_0.io.CarryDepenIn(0) <> binaryOp_inc10.io.Out(0)



  /* ================================================================== *
   *                   LOOP DATA CARRY DEPENDENCIES                     *
   * ================================================================== */

  phii_084.io.InData(1) <> Loop_0.io.CarryDepenOut.elements("field0")(0)



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  br_0.io.enable <> bb_entry0.io.Out(0)


  const0.io.enable <> bb_for_cond_cleanup1.io.Out(0)

  Gep_arrayidx21.io.enable <> bb_for_cond_cleanup1.io.Out(1)


  ld_2.io.enable <> bb_for_cond_cleanup1.io.Out(2)


  ret_3.io.In.enable <> bb_for_cond_cleanup1.io.Out(3)


  const1.io.enable <> bb_for_body2.io.Out(0)

  const2.io.enable <> bb_for_body2.io.Out(1)

  const3.io.enable <> bb_for_body2.io.Out(2)

  phii_084.io.enable <> bb_for_body2.io.Out(3)


  Gep_arrayidx5.io.enable <> bb_for_body2.io.Out(4)


  ld_6.io.enable <> bb_for_body2.io.Out(5)


  binaryOp_add7.io.enable <> bb_for_body2.io.Out(6)


  Gep_arrayidx18.io.enable <> bb_for_body2.io.Out(7)


  st_9.io.enable <> bb_for_body2.io.Out(8)


  binaryOp_inc10.io.enable <> bb_for_body2.io.Out(9)


  icmp_exitcond11.io.enable <> bb_for_body2.io.Out(10)


  br_12.io.enable <> bb_for_body2.io.Out(11)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  phii_084.io.Mask <> bb_for_body2.io.MaskBB(0)



  /* ================================================================== *
   *                   PRINT ALLOCA OFFSET                              *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */

  MemCtrl.io.ReadIn(0) <> ld_2.io.memReq

  ld_2.io.memResp <> MemCtrl.io.ReadOut(0)

  MemCtrl.io.ReadIn(1) <> ld_6.io.memReq

  ld_6.io.memResp <> MemCtrl.io.ReadOut(1)

  MemCtrl.io.WriteIn(0) <> st_9.io.memReq

  st_9.io.memResp <> MemCtrl.io.WriteOut(0)



  /* ================================================================== *
   *                   PRINT SHARED CONNECTIONS                         *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  Gep_arrayidx21.io.idx(0) <> const0.io.Out

  phii_084.io.InData(0) <> const1.io.Out

  binaryOp_inc10.io.RightIO <> const2.io.Out

  icmp_exitcond11.io.RightIO <> const3.io.Out

  ld_2.io.GepAddr <> Gep_arrayidx21.io.Out(0)

  ret_3.io.In.data("field0") <> ld_2.io.Out(0)

  Gep_arrayidx5.io.idx(0) <> phii_084.io.Out(0)

  Gep_arrayidx18.io.idx(0) <> phii_084.io.Out(1)

  binaryOp_inc10.io.LeftIO <> phii_084.io.Out(2)

  ld_6.io.GepAddr <> Gep_arrayidx5.io.Out(0)

  binaryOp_add7.io.LeftIO <> ld_6.io.Out(0)

  st_9.io.inData <> binaryOp_add7.io.Out(0)

  st_9.io.GepAddr <> Gep_arrayidx18.io.Out(0)

  icmp_exitcond11.io.LeftIO <> binaryOp_inc10.io.Out(1)

  br_12.io.CmpIO <> icmp_exitcond11.io.Out(0)

  Gep_arrayidx21.io.baseAddress <> InputSplitter.io.Out.data.elements("field0")(1)

  st_9.io.Out(0).ready := true.B

  br_12.io.PredOp(0) <> st_9.io.SuccOp(0)



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_3.io.Out

}

import java.io.{File, FileWriter}

object test14Top extends App {
  val dir = new File("RTL/test14Top");
  dir.mkdirs
  implicit val p = new WithAccelConfig
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new test14DF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}
