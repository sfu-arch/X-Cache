package dandelion.generator

import chipsalliance.rocketchip.config._
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


class test11DF(PtrsIn: Seq[Int] = List(), ValsIn: Seq[Int] = List(64), Returns: Seq[Int] = List(64))
			(implicit p: Parameters) extends DandelionAccelDCRModule(PtrsIn, ValsIn, Returns){

  /**
    * Call Interfaces
    */
  val call_5_out_io = IO(Decoupled(new CallDCR(ptrsArgTypes = List(), valsArgTypes = List(64))))
  val call_5_in_io  = IO(Flipped(Decoupled(new Call(List(64)))))

  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  //Remember if there is no mem operation io memreq/memresp should be grounded
  io.MemReq <> DontCare
  io.MemResp <> DontCare

  val ArgSplitter = Module(new SplitCallDCR(ptrsArgTypes = List(), valsArgTypes = List(1)))
  ArgSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val Loop_0 = Module(new LoopBlockNode(NumIns = List(1), NumOuts = List(1), NumCarry = List(1, 1), NumExits = 1, ID = 0))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_entry0 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 1, BID = 0))

  val bb_for_cond_cleanup1 = Module(new BasicBlockNode(NumInputs = 1, NumOuts = 2, NumPhi = 1, BID = 1))

  val bb_for_body2 = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 11, NumPhi = 2, BID = 2))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  br label %for.body, !dbg !20, !UID !21, !BB_UID !22
  val br_0 = Module(new UBranchNode(ID = 0))

  //  %add.lcssa = phi i32 [ %add, %for.body ], !UID !23
  val phiadd_lcssa1 = Module(new PhiFastNode(NumInputs = 1, NumOutputs = 1, ID = 1, Res = false))

  //  ret i32 %add.lcssa, !dbg !24, !UID !25, !BB_UID !26
  val ret_2 = Module(new RetNode2(retTypes = List(64), ID = 2))

  //  %i.07 = phi i32 [ 0, %entry ], [ %inc, %for.body ], !UID !27
  val phii_073 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 1, ID = 3, Res = true))

  //  %foo.06 = phi i32 [ %j, %entry ], [ %add, %for.body ], !UID !28
  val phifoo_064 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 4, Res = true))

  //  %call = tail call i32 @test11_inner(i32 %foo.06), !dbg !29, !UID !32
  val call_5_out = Module(new CallOutDCRNode(ID = 5, NumSuccOps = 0, PtrsTypes = List(), ValsTypes = List(64)))

  val call_5_in = Module(new CallInNode(ID = 5, argTypes = List(64)))

  //  %add = add i32 %call, %foo.06, !dbg !33, !UID !34
  val binaryOp_add6 = Module(new ComputeNode(NumOuts = 2, ID = 6, opCode = "add")(sign = false, Debug = false))

  //  %inc = add nuw nsw i32 %i.07, 1, !dbg !35, !UID !36
  val binaryOp_inc7 = Module(new ComputeNode(NumOuts = 2, ID = 7, opCode = "add")(sign = false, Debug = false))

  //  %exitcond = icmp eq i32 %inc, 5, !dbg !37, !UID !38
  val icmp_exitcond8 = Module(new ComputeNode(NumOuts = 1, ID = 8, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %exitcond, label %for.cond.cleanup, label %for.body, !dbg !20, !llvm.loop !39, !UID !41, !BB_UID !42
  val br_9 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 1, ID = 9))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i32 0
  val const0 = Module(new ConstFastNode(value = 0, ID = 0))

  //i32 1
  val const1 = Module(new ConstFastNode(value = 1, ID = 1))

  //i32 5
  val const2 = Module(new ConstFastNode(value = 5, ID = 2))



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_entry0.io.predicateIn(0) <> ArgSplitter.io.Out.enable



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

  Loop_0.io.loopBack(0) <> br_9.io.FalseOutput(0)

  Loop_0.io.loopFinish(0) <> br_9.io.TrueOutput(0)



  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

  Loop_0.io.InLiveIn(0) <> ArgSplitter.io.Out.dataVals.elements("field0")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */

  phifoo_064.io.InData(0) <> Loop_0.io.OutLiveIn.elements("field0")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */

  Loop_0.io.InLiveOut(0) <> binaryOp_add6.io.Out(0)



  /* ================================================================== *
   *                   LOOP LIVE OUT DEPENDENCIES                       *
   * ================================================================== */

  phiadd_lcssa1.io.InData(0) <> Loop_0.io.OutLiveOut.elements("field0")(0)



  /* ================================================================== *
   *                   LOOP CARRY DEPENDENCIES                          *
   * ================================================================== */

  Loop_0.io.CarryDepenIn(0) <> binaryOp_inc7.io.Out(0)

  Loop_0.io.CarryDepenIn(1) <> binaryOp_add6.io.Out(1)



  /* ================================================================== *
   *                   LOOP DATA CARRY DEPENDENCIES                     *
   * ================================================================== */

  phii_073.io.InData(1) <> Loop_0.io.CarryDepenOut.elements("field0")(0)

  phifoo_064.io.InData(1) <> Loop_0.io.CarryDepenOut.elements("field1")(0)



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  br_0.io.enable <> bb_entry0.io.Out(0)


  phiadd_lcssa1.io.enable <> bb_for_cond_cleanup1.io.Out(0)


  ret_2.io.In.enable <> bb_for_cond_cleanup1.io.Out(1)


  const0.io.enable <> bb_for_body2.io.Out(0)

  const1.io.enable <> bb_for_body2.io.Out(1)

  const2.io.enable <> bb_for_body2.io.Out(2)

  phii_073.io.enable <> bb_for_body2.io.Out(3)


  phifoo_064.io.enable <> bb_for_body2.io.Out(4)


  call_5_in.io.enable <> bb_for_body2.io.Out(6)

  call_5_out.io.enable <> bb_for_body2.io.Out(5)


  binaryOp_add6.io.enable <> bb_for_body2.io.Out(7)


  binaryOp_inc7.io.enable <> bb_for_body2.io.Out(8)


  icmp_exitcond8.io.enable <> bb_for_body2.io.Out(9)


  br_9.io.enable <> bb_for_body2.io.Out(10)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  phiadd_lcssa1.io.Mask <> bb_for_cond_cleanup1.io.MaskBB(0)

  phii_073.io.Mask <> bb_for_body2.io.MaskBB(0)

  phifoo_064.io.Mask <> bb_for_body2.io.MaskBB(1)



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

  phii_073.io.InData(0) <> const0.io.Out

  binaryOp_inc7.io.RightIO <> const1.io.Out

  icmp_exitcond8.io.RightIO <> const2.io.Out

  ret_2.io.In.data("field0") <> phiadd_lcssa1.io.Out(0)

  binaryOp_inc7.io.LeftIO <> phii_073.io.Out(0)

  call_5_out.io.inVals.elements("field0") <> phifoo_064.io.Out(0)

  binaryOp_add6.io.RightIO <> phifoo_064.io.Out(1)

  binaryOp_add6.io.LeftIO <> call_5_in.io.Out.data("field0")

  icmp_exitcond8.io.LeftIO <> binaryOp_inc7.io.Out(1)

  br_9.io.CmpIO <> icmp_exitcond8.io.Out(0)



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */



  /* ================================================================== *
   *                   PRINTING CALLIN AND CALLOUT INTERFACE            *
   * ================================================================== */

  call_5_out_io <> call_5_out.io.Out(0)

  call_5_in.io.In <> call_5_in_io

  br_9.io.PredOp(0) <> call_5_in.io.Out.enable



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_2.io.Out

}

