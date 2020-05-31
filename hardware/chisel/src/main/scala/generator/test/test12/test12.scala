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


class test12DF(PtrsIn: Seq[Int] = List(), ValsIn: Seq[Int] = List(64, 64, 64), Returns: Seq[Int] = List(64))
			(implicit p: Parameters) extends DandelionAccelDCRModule(PtrsIn, ValsIn, Returns){


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  //Remember if there is no mem operation io memreq/memresp should be grounded
  io.MemReq <> DontCare
  io.MemResp <> DontCare

  val ArgSplitter = Module(new SplitCallDCR(ptrsArgTypes = List(), valsArgTypes = List(8, 4, 6)))
  ArgSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_entry0 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 5, BID = 0))

  val bb_if_then1 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 7, BID = 1))

  val bb_if_then42 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 3, BID = 2))

  val bb_if_else3 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 4, BID = 3))

  val bb_if_else104 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 5, BID = 4))

  val bb_if_then145 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 4, BID = 5))

  val bb_if_else186 = Module(new BasicBlockNoMaskFastNode(NumInputs = 1, NumOuts = 5, BID = 6))

  val bb_if_end237 = Module(new BasicBlockNode(NumInputs = 4, NumOuts = 2, NumPhi = 1, BID = 7))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  %div.mask = and i32 %a, -2, !dbg !22, !UID !24
  val binaryOp_div_mask0 = Module(new ComputeNode(NumOuts = 1, ID = 0, opCode = "and")(sign = false, Debug = false))

  //  %cmp = icmp eq i32 %div.mask, 8, !dbg !22, !UID !25
  val icmp_cmp1 = Module(new ComputeNode(NumOuts = 1, ID = 1, opCode = "eq")(sign = false, Debug = false))

  //  br i1 %cmp, label %if.then, label %if.else10, !dbg !26, !UID !27, !BB_UID !28
  val br_2 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 2))

  //  %add = add i32 %b, %a, !dbg !29, !UID !31
  val binaryOp_add3 = Module(new ComputeNode(NumOuts = 2, ID = 3, opCode = "add")(sign = false, Debug = false))

  //  %add1 = add i32 %add, %c, !dbg !32, !UID !33
  val binaryOp_add14 = Module(new ComputeNode(NumOuts = 2, ID = 4, opCode = "add")(sign = false, Debug = false))

  //  %a.off = add i32 %a, -3, !dbg !34, !UID !36
  val binaryOp_a_off5 = Module(new ComputeNode(NumOuts = 1, ID = 5, opCode = "add")(sign = false, Debug = false))

  //  %0 = icmp ult i32 %a.off, 3, !dbg !34, !UID !37
  val icmp_6 = Module(new ComputeNode(NumOuts = 1, ID = 6, opCode = "slt")(sign = false, Debug = false))

  //  br i1 %0, label %if.then4, label %if.else, !dbg !38, !UID !39, !BB_UID !40
  val br_7 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 7))

  //  %mul = mul i32 %add1, %c, !dbg !41, !UID !42
  val binaryOp_mul8 = Module(new ComputeNode(NumOuts = 1, ID = 8, opCode = "mul")(sign = false, Debug = false))

  //  %add6 = add i32 %mul, %add, !dbg !43, !UID !44
  val binaryOp_add69 = Module(new ComputeNode(NumOuts = 1, ID = 9, opCode = "add")(sign = false, Debug = false))

  //  br label %if.end23, !dbg !45, !UID !46, !BB_UID !47
  val br_10 = Module(new UBranchNode(ID = 10))

  //  %mul7 = mul i32 %b, %a, !dbg !48, !UID !49
  val binaryOp_mul711 = Module(new ComputeNode(NumOuts = 1, ID = 11, opCode = "mul")(sign = false, Debug = false))

  //  %mul8 = mul i32 %add1, %c, !dbg !50, !UID !51
  val binaryOp_mul812 = Module(new ComputeNode(NumOuts = 1, ID = 12, opCode = "mul")(sign = false, Debug = false))

  //  %add9 = add i32 %mul8, %mul7, !dbg !52, !UID !53
  val binaryOp_add913 = Module(new ComputeNode(NumOuts = 1, ID = 13, opCode = "add")(sign = false, Debug = false))

  //  br label %if.end23, !UID !54, !BB_UID !55
  val br_14 = Module(new UBranchNode(ID = 14))

  //  %sub = sub i32 %a, %b, !dbg !56, !UID !58
  val binaryOp_sub15 = Module(new ComputeNode(NumOuts = 1, ID = 15, opCode = "sub")(sign = false, Debug = false))

  //  %add11 = add i32 %sub, %c, !dbg !59, !UID !60
  val binaryOp_add1116 = Module(new ComputeNode(NumOuts = 2, ID = 16, opCode = "add")(sign = false, Debug = false))

  //  %1 = icmp ult i32 %a, 5, !dbg !61, !UID !63
  val icmp_17 = Module(new ComputeNode(NumOuts = 1, ID = 17, opCode = "slt")(sign = false, Debug = false))

  //  br i1 %1, label %if.then14, label %if.else18, !dbg !64, !UID !65, !BB_UID !66
  val br_18 = Module(new CBranchNodeVariable(NumTrue = 1, NumFalse = 1, NumPredecessor = 0, ID = 18))

  //  %mul15 = mul i32 %add11, %b, !dbg !67, !UID !68
  val binaryOp_mul1519 = Module(new ComputeNode(NumOuts = 1, ID = 19, opCode = "mul")(sign = false, Debug = false))

  //  %sub16 = add i32 %c, %a, !dbg !69, !UID !70
  val binaryOp_sub1620 = Module(new ComputeNode(NumOuts = 1, ID = 20, opCode = "add")(sign = false, Debug = false))

  //  %add17 = sub i32 %sub16, %mul15, !dbg !71, !UID !72
  val binaryOp_add1721 = Module(new ComputeNode(NumOuts = 1, ID = 21, opCode = "sub")(sign = false, Debug = false))

  //  br label %if.end23, !dbg !73, !UID !74, !BB_UID !75
  val br_22 = Module(new UBranchNode(ID = 22))

  //  %mul19 = mul i32 %a, 12, !dbg !76, !UID !77
  val binaryOp_mul1923 = Module(new ComputeNode(NumOuts = 1, ID = 23, opCode = "mul")(sign = false, Debug = false))

  //  %mul20 = mul i32 %mul19, %add11, !dbg !78, !UID !79
  val binaryOp_mul2024 = Module(new ComputeNode(NumOuts = 1, ID = 24, opCode = "mul")(sign = false, Debug = false))

  //  %add21 = add i32 %mul20, %c, !dbg !80, !UID !81
  val binaryOp_add2125 = Module(new ComputeNode(NumOuts = 1, ID = 25, opCode = "add")(sign = false, Debug = false))

  //  br label %if.end23, !UID !82, !BB_UID !83
  val br_26 = Module(new UBranchNode(ID = 26))

  //  %sum.0 = phi i32 [ %add6, %if.then4 ], [ %add9, %if.else ], [ %add17, %if.then14 ], [ %add21, %if.else18 ], !UID !84
  val phisum_027 = Module(new PhiFastNode(NumInputs = 4, NumOutputs = 1, ID = 27, Res = true))

  //  ret i32 %sum.0, !dbg !85, !UID !86, !BB_UID !87
  val ret_28 = Module(new RetNode2(retTypes = List(32), ID = 28))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i32 -2
  val const0 = Module(new ConstFastNode(value = -2, ID = 0))

  //i32 8
  val const1 = Module(new ConstFastNode(value = 8, ID = 1))

  //i32 -3
  val const2 = Module(new ConstFastNode(value = -3, ID = 2))

  //i32 3
  val const3 = Module(new ConstFastNode(value = 3, ID = 3))

  //i32 5
  val const4 = Module(new ConstFastNode(value = 5, ID = 4))

  //i32 12
  val const5 = Module(new ConstFastNode(value = 12, ID = 5))



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_entry0.io.predicateIn(0) <> ArgSplitter.io.Out.enable

  bb_if_then1.io.predicateIn(0) <> br_2.io.TrueOutput(0)

  bb_if_then42.io.predicateIn(0) <> br_7.io.TrueOutput(0)

  bb_if_else3.io.predicateIn(0) <> br_7.io.FalseOutput(0)

  bb_if_else104.io.predicateIn(0) <> br_2.io.FalseOutput(0)

  bb_if_then145.io.predicateIn(0) <> br_18.io.TrueOutput(0)

  bb_if_else186.io.predicateIn(0) <> br_18.io.FalseOutput(0)

  bb_if_end237.io.predicateIn(3) <> br_10.io.Out(0)

  bb_if_end237.io.predicateIn(2) <> br_14.io.Out(0)

  bb_if_end237.io.predicateIn(1) <> br_22.io.Out(0)

  bb_if_end237.io.predicateIn(0) <> br_26.io.Out(0)



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE LOOP                     *
   * ================================================================== */



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
   *                   LOOP LIVE OUT DEPENDENCIES                       *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP CARRY DEPENDENCIES                          *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP DATA CARRY DEPENDENCIES                     *
   * ================================================================== */



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  const0.io.enable <> bb_entry0.io.Out(0)

  const1.io.enable <> bb_entry0.io.Out(1)

  binaryOp_div_mask0.io.enable <> bb_entry0.io.Out(2)


  icmp_cmp1.io.enable <> bb_entry0.io.Out(3)


  br_2.io.enable <> bb_entry0.io.Out(4)


  const2.io.enable <> bb_if_then1.io.Out(0)

  const3.io.enable <> bb_if_then1.io.Out(1)

  binaryOp_add3.io.enable <> bb_if_then1.io.Out(2)


  binaryOp_add14.io.enable <> bb_if_then1.io.Out(3)


  binaryOp_a_off5.io.enable <> bb_if_then1.io.Out(4)


  icmp_6.io.enable <> bb_if_then1.io.Out(5)


  br_7.io.enable <> bb_if_then1.io.Out(6)


  binaryOp_mul8.io.enable <> bb_if_then42.io.Out(0)


  binaryOp_add69.io.enable <> bb_if_then42.io.Out(1)


  br_10.io.enable <> bb_if_then42.io.Out(2)


  binaryOp_mul711.io.enable <> bb_if_else3.io.Out(0)


  binaryOp_mul812.io.enable <> bb_if_else3.io.Out(1)


  binaryOp_add913.io.enable <> bb_if_else3.io.Out(2)


  br_14.io.enable <> bb_if_else3.io.Out(3)


  const4.io.enable <> bb_if_else104.io.Out(0)

  binaryOp_sub15.io.enable <> bb_if_else104.io.Out(1)


  binaryOp_add1116.io.enable <> bb_if_else104.io.Out(2)


  icmp_17.io.enable <> bb_if_else104.io.Out(3)


  br_18.io.enable <> bb_if_else104.io.Out(4)


  binaryOp_mul1519.io.enable <> bb_if_then145.io.Out(0)


  binaryOp_sub1620.io.enable <> bb_if_then145.io.Out(1)


  binaryOp_add1721.io.enable <> bb_if_then145.io.Out(2)


  br_22.io.enable <> bb_if_then145.io.Out(3)


  const5.io.enable <> bb_if_else186.io.Out(0)

  binaryOp_mul1923.io.enable <> bb_if_else186.io.Out(1)


  binaryOp_mul2024.io.enable <> bb_if_else186.io.Out(2)


  binaryOp_add2125.io.enable <> bb_if_else186.io.Out(3)


  br_26.io.enable <> bb_if_else186.io.Out(4)


  phisum_027.io.enable <> bb_if_end237.io.Out(0)


  ret_28.io.In.enable <> bb_if_end237.io.Out(1)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  phisum_027.io.Mask <> bb_if_end237.io.MaskBB(0)



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

  binaryOp_a_off5.io.RightIO <> const2.io.Out

  icmp_6.io.RightIO <> const3.io.Out

  icmp_17.io.RightIO <> const4.io.Out

  binaryOp_mul1923.io.RightIO <> const5.io.Out

  icmp_cmp1.io.LeftIO <> binaryOp_div_mask0.io.Out(0)

  br_2.io.CmpIO <> icmp_cmp1.io.Out(0)

  binaryOp_add14.io.LeftIO <> binaryOp_add3.io.Out(0)

  binaryOp_add69.io.RightIO <> binaryOp_add3.io.Out(1)

  binaryOp_mul8.io.LeftIO <> binaryOp_add14.io.Out(0)

  binaryOp_mul812.io.LeftIO <> binaryOp_add14.io.Out(1)

  icmp_6.io.LeftIO <> binaryOp_a_off5.io.Out(0)

  br_7.io.CmpIO <> icmp_6.io.Out(0)

  binaryOp_add69.io.LeftIO <> binaryOp_mul8.io.Out(0)

  phisum_027.io.InData(0) <> binaryOp_add69.io.Out(0)

  binaryOp_add913.io.RightIO <> binaryOp_mul711.io.Out(0)

  binaryOp_add913.io.LeftIO <> binaryOp_mul812.io.Out(0)

  phisum_027.io.InData(1) <> binaryOp_add913.io.Out(0)

  binaryOp_add1116.io.LeftIO <> binaryOp_sub15.io.Out(0)

  binaryOp_mul1519.io.LeftIO <> binaryOp_add1116.io.Out(0)

  binaryOp_mul2024.io.RightIO <> binaryOp_add1116.io.Out(1)

  br_18.io.CmpIO <> icmp_17.io.Out(0)

  binaryOp_add1721.io.RightIO <> binaryOp_mul1519.io.Out(0)

  binaryOp_add1721.io.LeftIO <> binaryOp_sub1620.io.Out(0)

  phisum_027.io.InData(2) <> binaryOp_add1721.io.Out(0)

  binaryOp_mul2024.io.LeftIO <> binaryOp_mul1923.io.Out(0)

  binaryOp_add2125.io.LeftIO <> binaryOp_mul2024.io.Out(0)

  phisum_027.io.InData(3) <> binaryOp_add2125.io.Out(0)

  ret_28.io.In.data("field0") <> phisum_027.io.Out(0)

  binaryOp_div_mask0.io.LeftIO <> ArgSplitter.io.Out.dataVals.elements("field0")(0)

  binaryOp_add3.io.RightIO <> ArgSplitter.io.Out.dataVals.elements("field0")(1)

  binaryOp_a_off5.io.LeftIO <> ArgSplitter.io.Out.dataVals.elements("field0")(2)

  binaryOp_mul711.io.RightIO <> ArgSplitter.io.Out.dataVals.elements("field0")(3)

  binaryOp_sub15.io.LeftIO <> ArgSplitter.io.Out.dataVals.elements("field0")(4)

  icmp_17.io.LeftIO <> ArgSplitter.io.Out.dataVals.elements("field0")(5)

  binaryOp_sub1620.io.RightIO <> ArgSplitter.io.Out.dataVals.elements("field0")(6)

  binaryOp_mul1923.io.LeftIO <> ArgSplitter.io.Out.dataVals.elements("field0")(7)

  binaryOp_add3.io.LeftIO <> ArgSplitter.io.Out.dataVals.elements("field1")(0)

  binaryOp_mul711.io.LeftIO <> ArgSplitter.io.Out.dataVals.elements("field1")(1)

  binaryOp_sub15.io.RightIO <> ArgSplitter.io.Out.dataVals.elements("field1")(2)

  binaryOp_mul1519.io.RightIO <> ArgSplitter.io.Out.dataVals.elements("field1")(3)

  binaryOp_add14.io.RightIO <> ArgSplitter.io.Out.dataVals.elements("field2")(0)

  binaryOp_mul8.io.RightIO <> ArgSplitter.io.Out.dataVals.elements("field2")(1)

  binaryOp_mul812.io.RightIO <> ArgSplitter.io.Out.dataVals.elements("field2")(2)

  binaryOp_add1116.io.RightIO <> ArgSplitter.io.Out.dataVals.elements("field2")(3)

  binaryOp_sub1620.io.LeftIO <> ArgSplitter.io.Out.dataVals.elements("field2")(4)

  binaryOp_add2125.io.RightIO <> ArgSplitter.io.Out.dataVals.elements("field2")(5)



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_28.io.Out

}

