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

abstract class vectorScaleSerialDFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32, 32, 32, 32))))
    val MemResp = Flipped(Valid(new MemResp))
    val MemReq = Decoupled(new MemReq)
    val out = Decoupled(new Call(List(32)))
  })
}

class vectorScaleSerialDF(implicit p: Parameters) extends vectorScaleSerialDFIO()(p) {


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  val MemCtrl = Module(new UnifiedController(ID = 0, Size = 32, NReads = 1, NWrites = 1)
  (WControl = new WriteMemoryController(NumOps = 1, BaseSize = 2, NumEntries = 2))
  (RControl = new ReadMemoryController(NumOps = 1, BaseSize = 2, NumEntries = 2))
  (RWArbiter = new ReadWriteArbiter()))

  io.MemReq <> MemCtrl.io.MemReq
  MemCtrl.io.MemResp <> io.MemResp

  val InputSplitter = Module(new SplitCallNew(List(1, 1, 1, 2)))
  InputSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val Loop_0 = Module(new LoopBlockNode(NumIns = List(1, 1, 1, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 0))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_entry0 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 3, BID = 0))

  val bb_if_else_preheader1 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 1))

  val bb_for_cond_cleanup_loopexit2 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 2))

  val bb_for_cond_cleanup3 = Module(new BasicBlockNoMaskFastNode(NumInputs = 2, NumOuts = 2, BID = 3))

  val bb_if_else4 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 17, NumPhi = 1, BID = 4))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  %cmp22 = icmp eq i32 %N, 0, !dbg !32, !UID !34
  val icmp_cmp220 = Module(new ComputeNode(NumOuts = 1, ID = 0, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %cmp22, label %for.cond.cleanup, label %if.else.preheader, !dbg !35, !UID !36, !BB_UID !37
  val br_1 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 1))

  //  br label %if.else, !dbg !38, !UID !41, !BB_UID !42
  val br_2 = Module(new UBranchNode(ID = 2))

  //  br label %for.cond.cleanup, !dbg !43
  val br_3 = Module(new UBranchNode(ID = 3))

  //  ret i32 1, !dbg !43, !UID !44, !BB_UID !45
  val ret_4 = Module(new RetNode2(retTypes = List(32), ID = 4))

  //  %i.023 = phi i32 [ %inc, %if.else ], [ 0, %if.else.preheader ], !UID !46
  val phii_0235 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 3, ID = 5, Res = false))

  //  %arrayidx = getelementptr inbounds i32, i32* %a, i32 %i.023, !dbg !38, !UID !47
  val Gep_arrayidx6 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 6)(ElementSize = 4, ArraySize = List()))

  //  %0 = load i32, i32* %arrayidx, align 4, !dbg !38, !tbaa !48, !UID !52
  val ld_7 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 7, RouteID = 0))

  //  %mul = mul i32 %0, %scale, !dbg !53, !UID !55
  val binaryOp_mul8 = Module(new ComputeNode(NumOuts = 2, ID = 8, opCode = "mul")(sign = false, Debug = false))

  //  %shr = lshr i32 %mul, 8, !dbg !56, !UID !57
  val binaryOp_shr9 = Module(new ComputeNode(NumOuts = 1, ID = 9, opCode = "lshr")(sign = false, Debug = false))

  //  %arrayidx4 = getelementptr inbounds i32, i32* %c, i32 %i.023, !dbg !58, !UID !59
  val Gep_arrayidx410 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 10)(ElementSize = 4, ArraySize = List()))

  //  %cmp6 = icmp ugt i32 %mul, 65535, !dbg !60, !UID !62
  val icmp_cmp611 = Module(new ComputeNode(NumOuts = 1, ID = 11, opCode = "ugt")(sign = false, Debug = false))

  //  %spec.select = select i1 %cmp6, i32 255, i32 %shr, !dbg !63, !UID !64
  val select_spec_select12 = Module(new SelectNode(NumOuts = 1, ID = 12)(fast = false))

  //  store i32 %spec.select, i32* %arrayidx4, align 4, !tbaa !48, !UID !65
  val st_13 = Module(new UnTypStore(NumPredOps = 0, NumSuccOps = 0, ID = 13, RouteID = 0))

  //  %inc = add nuw i32 %i.023, 1, !dbg !66, !UID !67
  val binaryOp_inc14 = Module(new ComputeNode(NumOuts = 2, ID = 14, opCode = "add")(sign = false, Debug = false))

  //  %exitcond = icmp eq i32 %inc, %N, !dbg !32, !UID !68
  val icmp_exitcond15 = Module(new ComputeNode(NumOuts = 1, ID = 15, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond, label %for.cond.cleanup.loopexit, label %if.else, !dbg !35, !llvm.loop !69, !UID !71, !BB_UID !72
  val br_16 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 16))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i32 0
  val const0 = Module(new ConstFastNode(value = 0, ID = 0))

  //i32 1
  val const1 = Module(new ConstFastNode(value = 1, ID = 1))

  //i32 0
  val const2 = Module(new ConstFastNode(value = 0, ID = 2))

  //i32 8
  val const3 = Module(new ConstFastNode(value = 8, ID = 3))

  //i32 65535
  val const4 = Module(new ConstFastNode(value = 65535, ID = 4))

  //i32 255
  val const5 = Module(new ConstFastNode(value = 255, ID = 5))

  //i32 1
  val const6 = Module(new ConstFastNode(value = 1, ID = 6))



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_entry0.io.predicateIn(0) <> InputSplitter.io.Out.enable

  bb_if_else_preheader1.io.predicateIn(0) <> br_1.io.FalseOutput(0)

  bb_for_cond_cleanup3.io.predicateIn(1) <> br_1.io.TrueOutput(0)

  bb_for_cond_cleanup3.io.predicateIn(0) <> br_3.io.Out(0)



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE LOOP                     *
   * ================================================================== */

  bb_for_cond_cleanup_loopexit2.io.predicateIn(0) <> Loop_0.io.loopExit(0)

  bb_if_else4.io.predicateIn(1) <> Loop_0.io.activate_loop_start

  bb_if_else4.io.predicateIn(0) <> Loop_0.io.activate_loop_back



  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */

  Loop_0.io.enable <> br_2.io.Out(0)

  Loop_0.io.loopBack(0) <> br_16.io.FalseOutput(0)

  Loop_0.io.loopFinish(0) <> br_16.io.TrueOutput(0)



  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

  Loop_0.io.InLiveIn(0) <> InputSplitter.io.Out.data.elements("field0")(0)

  Loop_0.io.InLiveIn(1) <> InputSplitter.io.Out.data.elements("field2")(0)

  Loop_0.io.InLiveIn(2) <> InputSplitter.io.Out.data.elements("field1")(0)

  Loop_0.io.InLiveIn(3) <> InputSplitter.io.Out.data.elements("field3")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */

  Gep_arrayidx6.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field0")(0)

  binaryOp_mul8.io.RightIO <> Loop_0.io.OutLiveIn.elements("field1")(0)

  Gep_arrayidx410.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field2")(0)

  icmp_exitcond15.io.RightIO <> Loop_0.io.OutLiveIn.elements("field3")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP LIVE OUT DEPENDENCIES                       *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP CARRY DEPENDENCIES                          *
   * ================================================================== */

  Loop_0.io.CarryDepenIn(0) <> binaryOp_inc14.io.Out(0)



  /* ================================================================== *
   *                   LOOP DATA CARRY DEPENDENCIES                     *
   * ================================================================== */

  phii_0235.io.InData(0) <> Loop_0.io.CarryDepenOut.elements("field0")(0)



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  const0.io.enable <> bb_entry0.io.Out(0)

  icmp_cmp220.io.enable <> bb_entry0.io.Out(1)


  br_1.io.enable <> bb_entry0.io.Out(2)


  br_2.io.enable <> bb_if_else_preheader1.io.Out(0)


  br_3.io.enable <> bb_for_cond_cleanup_loopexit2.io.Out(0)


  const1.io.enable <> bb_for_cond_cleanup3.io.Out(0)

  ret_4.io.In.enable <> bb_for_cond_cleanup3.io.Out(1)


  const2.io.enable <> bb_if_else4.io.Out(0)

  const3.io.enable <> bb_if_else4.io.Out(1)

  const4.io.enable <> bb_if_else4.io.Out(2)

  const5.io.enable <> bb_if_else4.io.Out(3)

  const6.io.enable <> bb_if_else4.io.Out(4)

  phii_0235.io.enable <> bb_if_else4.io.Out(5)


  Gep_arrayidx6.io.enable <> bb_if_else4.io.Out(6)


  ld_7.io.enable <> bb_if_else4.io.Out(7)


  binaryOp_mul8.io.enable <> bb_if_else4.io.Out(8)


  binaryOp_shr9.io.enable <> bb_if_else4.io.Out(9)


  Gep_arrayidx410.io.enable <> bb_if_else4.io.Out(10)


  icmp_cmp611.io.enable <> bb_if_else4.io.Out(11)


  select_spec_select12.io.enable <> bb_if_else4.io.Out(12)


  st_13.io.enable <> bb_if_else4.io.Out(13)


  binaryOp_inc14.io.enable <> bb_if_else4.io.Out(14)


  icmp_exitcond15.io.enable <> bb_if_else4.io.Out(15)


  br_16.io.enable <> bb_if_else4.io.Out(16)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  phii_0235.io.Mask <> bb_if_else4.io.MaskBB(0)



  /* ================================================================== *
   *                   PRINT ALLOCA OFFSET                              *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */

  MemCtrl.io.ReadIn(0) <> ld_7.io.memReq

  ld_7.io.memResp <> MemCtrl.io.ReadOut(0)

  MemCtrl.io.WriteIn(0) <> st_13.io.memReq

  st_13.io.memResp <> MemCtrl.io.WriteOut(0)



  /* ================================================================== *
   *                   PRINT SHARED CONNECTIONS                         *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  icmp_cmp220.io.RightIO <> const0.io.Out

  ret_4.io.In.data("field0") <> const1.io.Out

  phii_0235.io.InData(1) <> const2.io.Out

  binaryOp_shr9.io.RightIO <> const3.io.Out

  icmp_cmp611.io.RightIO <> const4.io.Out

  select_spec_select12.io.InData1 <> const5.io.Out

  binaryOp_inc14.io.RightIO <> const6.io.Out

  br_1.io.CmpIO <> icmp_cmp220.io.Out(0)

  Gep_arrayidx6.io.idx(0) <> phii_0235.io.Out(0)

  Gep_arrayidx410.io.idx(0) <> phii_0235.io.Out(1)

  binaryOp_inc14.io.LeftIO <> phii_0235.io.Out(2)

  ld_7.io.GepAddr <> Gep_arrayidx6.io.Out(0)

  binaryOp_mul8.io.LeftIO <> ld_7.io.Out(0)

  binaryOp_shr9.io.LeftIO <> binaryOp_mul8.io.Out(0)

  icmp_cmp611.io.LeftIO <> binaryOp_mul8.io.Out(1)

  select_spec_select12.io.InData2 <> binaryOp_shr9.io.Out(0)

  st_13.io.GepAddr <> Gep_arrayidx410.io.Out(0)

  select_spec_select12.io.Select <> icmp_cmp611.io.Out(0)

  st_13.io.inData <> select_spec_select12.io.Out(0)

  icmp_exitcond15.io.LeftIO <> binaryOp_inc14.io.Out(1)

  br_16.io.CmpIO <> icmp_exitcond15.io.Out(0)

  icmp_cmp220.io.LeftIO <> InputSplitter.io.Out.data.elements("field3")(1)

  st_13.io.Out(0).ready := true.B



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_4.io.Out

}

import java.io.{File, FileWriter}

object vectorScaleSerialTop extends App {
  val dir = new File("RTL/vectorScaleSerialTop");
  dir.mkdirs
  implicit val p = Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new vectorScaleSerialDF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}
