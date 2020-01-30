package dandelion.generator

import chisel3._
import chipsalliance.rocketchip.config._
import dandelion.config._
import dandelion.control._
import dandelion.interfaces._
import dandelion.junctions._
import dandelion.loop._
import dandelion.memory._
import dandelion.node._
import util._


  /* ================================================================== *
   *                   PRINTING PORTS DEFINITION                        *
   * ================================================================== */

abstract class reluDFIO(implicit val p: Parameters) extends Module with HasAccelParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32, 32, 32))))
    val MemResp = Flipped(Valid(new MemResp))
    val MemReq = Decoupled(new MemReq)
    val out = Decoupled(new Call(List()))
  })
}

class reluDF(implicit p: Parameters) extends reluDFIO()(p) {


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  val MemCtrl = Module(new UnifiedController(ID = 0, Size = 32, NReads = 1, NWrites = 1)
  (WControl = new WriteMemoryController(NumOps = 1, BaseSize = 2, NumEntries = 2))
  (RControl = new ReadMemoryController(NumOps = 1, BaseSize = 2, NumEntries = 2))
  (RWArbiter = new ReadWriteArbiter()))

  io.MemReq <> MemCtrl.io.MemReq
  MemCtrl.io.MemResp <> io.MemResp

  val InputSplitter = Module(new SplitCallNew(List(1, 1, 2)))
  InputSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val Loop_0 = Module(new LoopBlockNode(NumIns = List(1, 1, 1, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 0))

  val Loop_1 = Module(new LoopBlockNode(NumIns = List(3, 1, 1), NumOuts = List(), NumCarry = List(1), NumExits = 1, ID = 1))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_entry0 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 3, BID = 0))

  val bb_for_body4_lr_ph_preheader1 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 1))

  val bb_for_cond_cleanup_loopexit2 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 2))

  val bb_for_cond_cleanup3 = Module(new BasicBlockNoMaskFastNode(NumInputs = 2, NumOuts = 1, BID = 3))

  val bb_for_body4_lr_ph4 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 4, NumPhi = 1, BID = 4))

  val bb_for_cond_cleanup35 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 4, BID = 5))

  val bb_for_body46 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 15, NumPhi = 1, BID = 6))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  %cmp28 = icmp eq i32 %N, 0, !dbg !37, !UID !38
  val icmp_cmp280 = Module(new ComputeNode(NumOuts = 1, ID = 0, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %cmp28, label %for.cond.cleanup, label %for.body4.lr.ph.preheader, !dbg !39, !UID !40, !BB_UID !41
  val br_1 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 1))

  //  br label %for.body4.lr.ph, !UID !42, !BB_UID !43
  val br_2 = Module(new UBranchNode(ID = 2))

  //  br label %for.cond.cleanup, !dbg !44
  val br_3 = Module(new UBranchNode(ID = 3))

  //  ret void, !dbg !44, !UID !45, !BB_UID !46
  val ret_4 = Module(new RetNode2(retTypes = List(), ID = 4))

  //  %j.029 = phi i32 [ %inc10, %for.cond.cleanup3 ], [ 0, %for.body4.lr.ph.preheader ], !UID !47
  val phij_0295 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 5, Res = false))

  //  %mul = mul i32 %j.029, %N, !UID !49
  val binaryOp_mul6 = Module(new ComputeNode(NumOuts = 1, ID = 6, opCode = "mul")(sign = false, Debug = false))

  //  br label %for.body4, !dbg !50, !UID !51, !BB_UID !52
  val br_7 = Module(new UBranchNode(ID = 7))

  //  %inc10 = add nuw i32 %j.029, 1, !dbg !53, !UID !54
  val binaryOp_inc108 = Module(new ComputeNode(NumOuts = 2, ID = 8, opCode = "add")(sign = false, Debug = false))

  //  %exitcond30 = icmp eq i32 %inc10, %N, !dbg !37, !UID !55
  val icmp_exitcond309 = Module(new ComputeNode(NumOuts = 1, ID = 9, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond30, label %for.cond.cleanup.loopexit, label %for.body4.lr.ph, !dbg !39, !llvm.loop !56, !UID !58, !BB_UID !59
  val br_10 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 10))

  //  %i.027 = phi i32 [ 0, %for.body4.lr.ph ], [ %inc, %for.body4 ], !UID !60
  val phii_02711 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 11, Res = true))

  //  %add = add i32 %i.027, %mul, !dbg !61, !UID !62
  val binaryOp_add12 = Module(new ComputeNode(NumOuts = 2, ID = 12, opCode = "add")(sign = false, Debug = false))

  //  %arrayidx = getelementptr inbounds i32, i32* %in, i32 %add, !dbg !64, !UID !66
  val Gep_arrayidx13 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 13)(ElementSize = 4, ArraySize = List()))

  //  %0 = load i32, i32* %arrayidx, align 4, !dbg !64, !tbaa !67, !UID !71
  val ld_14 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 2, ID = 14, RouteID = 0))

  //  %arrayidx6 = getelementptr inbounds i32, i32* %out, i32 %add, !UID !72
  val Gep_arrayidx615 = Module(new GepNode(NumIns = 1, NumOuts = 1, ID = 15)(ElementSize = 4, ArraySize = List()))

  //  %1 = icmp sgt i32 %0, 0, !dbg !73, !UID !74
  val icmp_16 = Module(new ComputeNode(NumOuts = 1, ID = 16, opCode = "gte")(sign = true, Debug = false))

  //  %. = select i1 %1, i32 %0, i32 0, !dbg !73, !UID !75
  val select__17 = Module(new SelectNode(NumOuts = 1, ID = 17)(fast = false))

  //  store i32 %., i32* %arrayidx6, align 4, !tbaa !67, !UID !76
  val st_18 = Module(new UnTypStore(NumPredOps = 0, NumSuccOps = 0, ID = 18, RouteID = 0))

  //  %inc = add nuw i32 %i.027, 1, !dbg !77, !UID !78
  val binaryOp_inc19 = Module(new ComputeNode(NumOuts = 2, ID = 19, opCode = "add")(sign = false, Debug = false))

  //  %exitcond = icmp eq i32 %inc, %N, !dbg !79, !UID !80
  val icmp_exitcond20 = Module(new ComputeNode(NumOuts = 1, ID = 20, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond, label %for.cond.cleanup3, label %for.body4, !dbg !50, !llvm.loop !81, !UID !83, !BB_UID !84
  val br_21 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 21))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i32 0
  val const0 = Module(new ConstFastNode(value = 0, ID = 0))

  //i32 0
  val const1 = Module(new ConstFastNode(value = 0, ID = 1))

  //i32 1
  val const2 = Module(new ConstFastNode(value = 1, ID = 2))

  //i32 0
  val const3 = Module(new ConstFastNode(value = 0, ID = 3))

  //i32 0
  val const4 = Module(new ConstFastNode(value = 0, ID = 4))

  //i32 0
  val const5 = Module(new ConstFastNode(value = 0, ID = 5))

  //i32 1
  val const6 = Module(new ConstFastNode(value = 1, ID = 6))



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_entry0.io.predicateIn(0) <> InputSplitter.io.Out.enable

  bb_for_body4_lr_ph_preheader1.io.predicateIn(0) <> br_1.io.FalseOutput(0)

  bb_for_cond_cleanup3.io.predicateIn(1) <> br_1.io.TrueOutput(0)

  bb_for_cond_cleanup3.io.predicateIn(0) <> br_3.io.Out(0)



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE LOOP                     *
   * ================================================================== */

  bb_for_cond_cleanup_loopexit2.io.predicateIn(0) <> Loop_1.io.loopExit(0)

  bb_for_body4_lr_ph4.io.predicateIn(1) <> Loop_1.io.activate_loop_start

  bb_for_body4_lr_ph4.io.predicateIn(0) <> Loop_1.io.activate_loop_back

  bb_for_cond_cleanup35.io.predicateIn(0) <> Loop_0.io.loopExit(0)

  bb_for_body46.io.predicateIn(1) <> Loop_0.io.activate_loop_start

  bb_for_body46.io.predicateIn(0) <> Loop_0.io.activate_loop_back



  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */

  Loop_0.io.enable <> br_7.io.Out(0)

  Loop_0.io.loopBack(0) <> br_21.io.FalseOutput(0)

  Loop_0.io.loopFinish(0) <> br_21.io.TrueOutput(0)

  Loop_1.io.enable <> br_2.io.Out(0)

  Loop_1.io.loopBack(0) <> br_10.io.FalseOutput(0)

  Loop_1.io.loopFinish(0) <> br_10.io.TrueOutput(0)



  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

  Loop_0.io.InLiveIn(0) <> binaryOp_mul6.io.Out(0)

  Loop_0.io.InLiveIn(1) <> Loop_1.io.OutLiveIn.elements("field1")(0)

  Loop_0.io.InLiveIn(2) <> Loop_1.io.OutLiveIn.elements("field2")(0)

  Loop_0.io.InLiveIn(3) <> Loop_1.io.OutLiveIn.elements("field0")(0)

  Loop_1.io.InLiveIn(0) <> InputSplitter.io.Out.data.elements("field2")(0)

  Loop_1.io.InLiveIn(1) <> InputSplitter.io.Out.data.elements("field0")(0)

  Loop_1.io.InLiveIn(2) <> InputSplitter.io.Out.data.elements("field1")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */

  binaryOp_add12.io.RightIO <> Loop_0.io.OutLiveIn.elements("field0")(0)

  Gep_arrayidx13.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field1")(0)

  Gep_arrayidx615.io.baseAddress <> Loop_0.io.OutLiveIn.elements("field2")(0)

  icmp_exitcond20.io.RightIO <> Loop_0.io.OutLiveIn.elements("field3")(0)

  binaryOp_mul6.io.RightIO <> Loop_1.io.OutLiveIn.elements("field0")(1)

  icmp_exitcond309.io.RightIO <> Loop_1.io.OutLiveIn.elements("field0")(2)



  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP LIVE OUT DEPENDENCIES                       *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP CARRY DEPENDENCIES                          *
   * ================================================================== */

  Loop_0.io.CarryDepenIn(0) <> binaryOp_inc19.io.Out(0)

  Loop_1.io.CarryDepenIn(0) <> binaryOp_inc108.io.Out(0)



  /* ================================================================== *
   *                   LOOP DATA CARRY DEPENDENCIES                     *
   * ================================================================== */

  phii_02711.io.InData(1) <> Loop_0.io.CarryDepenOut.elements("field0")(0)

  phij_0295.io.InData(0) <> Loop_1.io.CarryDepenOut.elements("field0")(0)



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  const0.io.enable <> bb_entry0.io.Out(0)

  icmp_cmp280.io.enable <> bb_entry0.io.Out(1)


  br_1.io.enable <> bb_entry0.io.Out(2)


  br_2.io.enable <> bb_for_body4_lr_ph_preheader1.io.Out(0)


  br_3.io.enable <> bb_for_cond_cleanup_loopexit2.io.Out(0)


  ret_4.io.In.enable <> bb_for_cond_cleanup3.io.Out(0)


  const1.io.enable <> bb_for_body4_lr_ph4.io.Out(0)

  phij_0295.io.enable <> bb_for_body4_lr_ph4.io.Out(1)


  binaryOp_mul6.io.enable <> bb_for_body4_lr_ph4.io.Out(2)


  br_7.io.enable <> bb_for_body4_lr_ph4.io.Out(3)


  const2.io.enable <> bb_for_cond_cleanup35.io.Out(0)

  binaryOp_inc108.io.enable <> bb_for_cond_cleanup35.io.Out(1)


  icmp_exitcond309.io.enable <> bb_for_cond_cleanup35.io.Out(2)


  br_10.io.enable <> bb_for_cond_cleanup35.io.Out(3)


  const3.io.enable <> bb_for_body46.io.Out(0)

  const4.io.enable <> bb_for_body46.io.Out(1)

  const5.io.enable <> bb_for_body46.io.Out(2)

  const6.io.enable <> bb_for_body46.io.Out(3)

  phii_02711.io.enable <> bb_for_body46.io.Out(4)


  binaryOp_add12.io.enable <> bb_for_body46.io.Out(5)


  Gep_arrayidx13.io.enable <> bb_for_body46.io.Out(6)


  ld_14.io.enable <> bb_for_body46.io.Out(7)


  Gep_arrayidx615.io.enable <> bb_for_body46.io.Out(8)


  icmp_16.io.enable <> bb_for_body46.io.Out(9)


  select__17.io.enable <> bb_for_body46.io.Out(10)


  st_18.io.enable <> bb_for_body46.io.Out(11)


  binaryOp_inc19.io.enable <> bb_for_body46.io.Out(12)


  icmp_exitcond20.io.enable <> bb_for_body46.io.Out(13)


  br_21.io.enable <> bb_for_body46.io.Out(14)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  phij_0295.io.Mask <> bb_for_body4_lr_ph4.io.MaskBB(0)

  phii_02711.io.Mask <> bb_for_body46.io.MaskBB(0)



  /* ================================================================== *
   *                   PRINT ALLOCA OFFSET                              *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */

  MemCtrl.io.ReadIn(0) <> ld_14.io.memReq

  ld_14.io.memResp <> MemCtrl.io.ReadOut(0)

  MemCtrl.io.WriteIn(0) <> st_18.io.memReq

  st_18.io.memResp <> MemCtrl.io.WriteOut(0)



  /* ================================================================== *
   *                   PRINT SHARED CONNECTIONS                         *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  icmp_cmp280.io.RightIO <> const0.io.Out

  phij_0295.io.InData(1) <> const1.io.Out

  binaryOp_inc108.io.RightIO <> const2.io.Out

  phii_02711.io.InData(0) <> const3.io.Out

  icmp_16.io.RightIO <> const4.io.Out

  select__17.io.InData2 <> const5.io.Out

  binaryOp_inc19.io.RightIO <> const6.io.Out

  br_1.io.CmpIO <> icmp_cmp280.io.Out(0)

  binaryOp_mul6.io.LeftIO <> phij_0295.io.Out(0)

  binaryOp_inc108.io.LeftIO <> phij_0295.io.Out(1)

  icmp_exitcond309.io.LeftIO <> binaryOp_inc108.io.Out(1)

  br_10.io.CmpIO <> icmp_exitcond309.io.Out(0)

  binaryOp_add12.io.LeftIO <> phii_02711.io.Out(0)

  binaryOp_inc19.io.LeftIO <> phii_02711.io.Out(1)

  Gep_arrayidx13.io.idx(0) <> binaryOp_add12.io.Out(0)

  Gep_arrayidx615.io.idx(0) <> binaryOp_add12.io.Out(1)

  ld_14.io.GepAddr <> Gep_arrayidx13.io.Out(0)

  icmp_16.io.LeftIO <> ld_14.io.Out(0)

  select__17.io.InData1 <> ld_14.io.Out(1)

  st_18.io.GepAddr <> Gep_arrayidx615.io.Out(0)

  select__17.io.Select <> icmp_16.io.Out(0)

  st_18.io.inData <> select__17.io.Out(0)

  icmp_exitcond20.io.LeftIO <> binaryOp_inc19.io.Out(1)

  br_21.io.CmpIO <> icmp_exitcond20.io.Out(0)

  icmp_cmp280.io.LeftIO <> InputSplitter.io.Out.data.elements("field2")(1)

  st_18.io.Out(0).ready := true.B



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_4.io.Out

}

import java.io.{File, FileWriter}

object reluTop extends App {
  val dir = new File("RTL/reluTop");
  dir.mkdirs
  implicit val p = new WithAccelConfig
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new reluDF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}
