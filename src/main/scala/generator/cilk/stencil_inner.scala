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
import org.scalatest.Matchers._
import regfile._
import stack._
import util._


  /* ================================================================== *
   *                   PRINTING PORTS DEFINITION                        *
   * ================================================================== */

abstract class stencil_innerDFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32, 32, 32, 32, 32))))
    val MemResp = Flipped(Valid(new MemResp))
    val MemReq = Decoupled(new MemReq)
    val out = Decoupled(new Call(List(32)))
  })
}

class stencil_innerDF(implicit p: Parameters) extends stencil_innerDFIO()(p) {


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  val MemCtrl = Module(new UnifiedController(ID=0, Size=32, NReads=2, NWrites=2)
		 (WControl=new WriteMemoryController(NumOps=2, BaseSize=2, NumEntries=2))
		 (RControl=new ReadMemoryController(NumOps=2, BaseSize=2, NumEntries=2))
		 (RWArbiter=new ReadWriteArbiter()))

  io.MemReq <> MemCtrl.io.MemReq
  MemCtrl.io.MemResp <> io.MemResp

  val InputSplitter = Module(new SplitCallNew(List(1,1,1,1,1)))
  InputSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val Loop_0 = Module(new LoopBlock(NumIns=List(2,1,2,1,1), NumOuts = 0, NumExits=1, ID = 0))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_entry0 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 0))

  val bb_for_cond1 = Module(new LoopHead(NumOuts = 5, NumPhi=1, BID = 1))

  val bb_for_body2 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 9, BID = 2))

  val bb_if_then3 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 3, BID = 3))

  val bb_if_then54 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 13, BID = 4))

  val bb_if_end5 = Module(new BasicBlockNoMaskNode(NumInputs = 2, NumOuts = 1, BID = 5))

  val bb_if_end116 = Module(new BasicBlockNoMaskNode(NumInputs = 2, NumOuts = 1, BID = 6))

  val bb_for_inc7 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 3, BID = 7))

  val bb_for_end8 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 8))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  br label %for.cond, !UID !2, !BB_UID !3
  val br_0 = Module(new UBranchNode(ID = 0))

  //  %nc.0 = phi i32 [ 0, %entry ], [ %inc, %for.inc ], !UID !4
  val phi_nc_01 = Module(new PhiNode(NumInputs = 2, NumOuts = 3, ID = 1))

  //  %cmp = icmp ule i32 %nc.0, 2, !UID !5
  val icmp_cmp2 = Module(new IcmpNode(NumOuts = 1, ID = 2, opCode = "ule")(sign=false))

  //  br i1 %cmp, label %for.body, label %for.end, !UID !6, !BB_UID !7
  val br_3 = Module(new CBranchNode(ID = 3))

  //  %add = add i32 %i, %nr, !UID !8
  val binaryOp_add4 = Module(new ComputeNode(NumOuts = 1, ID = 4, opCode = "add")(sign=false))

  //  %sub = sub i32 %add, 1, !UID !9
  val binaryOp_sub5 = Module(new ComputeNode(NumOuts = 2, ID = 5, opCode = "sub")(sign=false))

  //  %add1 = add i32 %j, %nc.0, !UID !10
  val binaryOp_add16 = Module(new ComputeNode(NumOuts = 1, ID = 6, opCode = "add")(sign=false))

  //  %sub2 = sub i32 %add1, 1, !UID !11
  val binaryOp_sub27 = Module(new ComputeNode(NumOuts = 2, ID = 7, opCode = "sub")(sign=false))

  //  %cmp3 = icmp ult i32 %sub, 4, !UID !12
  val icmp_cmp38 = Module(new IcmpNode(NumOuts = 1, ID = 8, opCode = "ult")(sign=false))

  //  br i1 %cmp3, label %if.then, label %if.end11, !UID !13, !BB_UID !14
  val br_9 = Module(new CBranchNode(ID = 9))

  //  %cmp4 = icmp ult i32 %sub2, 4, !UID !15
  val icmp_cmp410 = Module(new IcmpNode(NumOuts = 1, ID = 10, opCode = "ult")(sign=false))

  //  br i1 %cmp4, label %if.then5, label %if.end, !UID !16, !BB_UID !17
  val br_11 = Module(new CBranchNode(ID = 11))

  //  %mul = mul i32 %sub, 4, !UID !18
  val binaryOp_mul12 = Module(new ComputeNode(NumOuts = 1, ID = 12, opCode = "mul")(sign=false))

  //  %add6 = add i32 %mul, %sub2, !UID !19
  val binaryOp_add613 = Module(new ComputeNode(NumOuts = 1, ID = 13, opCode = "add")(sign=false))

  //  %arrayidx = getelementptr inbounds i32, i32* %in, i32 %add6, !UID !20
  val Gep_arrayidx14 = Module(new GepArrayOneNode(NumOuts=1, ID=14)(numByte=4)(size=1))

  //  %0 = load i32, i32* %arrayidx, align 4, !UID !21
  val ld_15 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=15, RouteID=0))

  //  %mul7 = mul i32 %i, 4, !UID !22
  val binaryOp_mul716 = Module(new ComputeNode(NumOuts = 1, ID = 16, opCode = "mul")(sign=false))

  //  %add8 = add i32 %mul7, %j, !UID !23
  val binaryOp_add817 = Module(new ComputeNode(NumOuts = 1, ID = 17, opCode = "add")(sign=false))

  //  %arrayidx9 = getelementptr inbounds i32, i32* %out, i32 %add8, !UID !24
  val Gep_arrayidx918 = Module(new GepArrayOneNode(NumOuts=2, ID=18)(numByte=4)(size=1))

  //  %1 = load i32, i32* %arrayidx9, align 4, !UID !25
  val ld_19 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=19, RouteID=1))

  //  %add10 = add i32 %1, %0, !UID !26
  val binaryOp_add1020 = Module(new ComputeNode(NumOuts = 1, ID = 20, opCode = "add")(sign=false))

  //  store i32 %add10, i32* %arrayidx9, align 4, !UID !27
  val st_21 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=1, ID=21, RouteID=0))

  //  br label %if.end, !UID !28, !BB_UID !29
  val br_22 = Module(new UBranchNode(NumPredOps=1, ID = 22))

  //  br label %if.end11, !UID !30, !BB_UID !31
  val br_23 = Module(new UBranchNode(ID = 23))

  //  br label %for.inc, !UID !32, !BB_UID !33
  val br_24 = Module(new UBranchNode(ID = 24))

  //  %inc = add i32 %nc.0, 1, !UID !34
  val binaryOp_inc25 = Module(new ComputeNode(NumOuts = 1, ID = 25, opCode = "add")(sign=false))

  //  br label %for.cond, !UID !35, !BB_UID !36
  val br_26 = Module(new UBranchNode(NumOuts=2, ID = 26))

  //  ret void, !UID !37, !BB_UID !38
  val ret_27 = Module(new RetNode(retTypes=List(32), ID = 27))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i32 0
  val const0 = Module(new ConstNode(value = 0, NumOuts = 1, ID = 0))

  //i32 2
  val const1 = Module(new ConstNode(value = 2, NumOuts = 1, ID = 1))

  //i32 1
  val const2 = Module(new ConstNode(value = 1, NumOuts = 1, ID = 2))

  //i32 1
  val const3 = Module(new ConstNode(value = 1, NumOuts = 1, ID = 3))

  //i32 4
  val const4 = Module(new ConstNode(value = 4, NumOuts = 1, ID = 4))

  //i32 4
  val const5 = Module(new ConstNode(value = 4, NumOuts = 1, ID = 5))

  //i32 4
  val const6 = Module(new ConstNode(value = 4, NumOuts = 1, ID = 6))

  //i32 4
  val const7 = Module(new ConstNode(value = 4, NumOuts = 1, ID = 7))

  //i32 1
  val const8 = Module(new ConstNode(value = 1, NumOuts = 1, ID = 8))



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_entry0.io.predicateIn <> InputSplitter.io.Out.enable

  bb_for_cond1.io.activate <> Loop_0.io.activate

  bb_for_cond1.io.loopBack <> br_26.io.Out(0)

  bb_for_body2.io.predicateIn <> br_3.io.Out(0)

  bb_if_then3.io.predicateIn <> br_9.io.Out(0)

  bb_if_then54.io.predicateIn <> br_11.io.Out(0)

  bb_if_end5.io.predicateIn <> br_11.io.Out(1)

  bb_if_end5.io.predicateIn <> br_22.io.Out(0)

  bb_if_end116.io.predicateIn <> br_9.io.Out(1)

  bb_if_end116.io.predicateIn <> br_23.io.Out(0)

  bb_for_inc7.io.predicateIn <> br_24.io.Out(0)

  bb_for_end8.io.predicateIn <> Loop_0.io.endEnable



  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */

  Loop_0.io.enable <> br_0.io.Out(0)

  Loop_0.io.latchEnable <> br_26.io.Out(1)

  Loop_0.io.loopExit(0) <> br_3.io.Out(1)



  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */

  br_22.io.PredOp(0) <> st_21.io.SuccOp(0)



  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

  Loop_0.io.In(0) <> InputSplitter.io.Out.data("field2")(0)

  Loop_0.io.In(1) <> InputSplitter.io.Out.data("field4")(0)

  Loop_0.io.In(2) <> InputSplitter.io.Out.data("field3")(0)

  Loop_0.io.In(3) <> InputSplitter.io.Out.data("field0")(0)

  Loop_0.io.In(4) <> InputSplitter.io.Out.data("field1")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */

  binaryOp_add4.io.LeftIO <> Loop_0.io.liveIn.data("field0")(0)

  binaryOp_mul716.io.LeftIO <> Loop_0.io.liveIn.data("field0")(1)

  binaryOp_add4.io.RightIO <> Loop_0.io.liveIn.data("field1")(0)

  binaryOp_add16.io.LeftIO <> Loop_0.io.liveIn.data("field2")(0)

  binaryOp_add817.io.RightIO <> Loop_0.io.liveIn.data("field2")(1)

  Gep_arrayidx14.io.baseAddress <> Loop_0.io.liveIn.data("field3")(0)

  Gep_arrayidx918.io.baseAddress <> Loop_0.io.liveIn.data("field4")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  br_0.io.enable <> bb_entry0.io.Out(0)


  const0.io.enable <> bb_for_cond1.io.Out(0)

  const1.io.enable <> bb_for_cond1.io.Out(1)

  phi_nc_01.io.enable <> bb_for_cond1.io.Out(2)

  icmp_cmp2.io.enable <> bb_for_cond1.io.Out(3)

  br_3.io.enable <> bb_for_cond1.io.Out(4)


  const2.io.enable <> bb_for_body2.io.Out(0)

  const3.io.enable <> bb_for_body2.io.Out(1)

  const4.io.enable <> bb_for_body2.io.Out(2)

  binaryOp_add4.io.enable <> bb_for_body2.io.Out(3)

  binaryOp_sub5.io.enable <> bb_for_body2.io.Out(4)

  binaryOp_add16.io.enable <> bb_for_body2.io.Out(5)

  binaryOp_sub27.io.enable <> bb_for_body2.io.Out(6)

  icmp_cmp38.io.enable <> bb_for_body2.io.Out(7)

  br_9.io.enable <> bb_for_body2.io.Out(8)


  const5.io.enable <> bb_if_then3.io.Out(0)

  icmp_cmp410.io.enable <> bb_if_then3.io.Out(1)

  br_11.io.enable <> bb_if_then3.io.Out(2)


  const6.io.enable <> bb_if_then54.io.Out(0)

  const7.io.enable <> bb_if_then54.io.Out(1)

  binaryOp_mul12.io.enable <> bb_if_then54.io.Out(2)

  binaryOp_add613.io.enable <> bb_if_then54.io.Out(3)

  Gep_arrayidx14.io.enable <> bb_if_then54.io.Out(4)

  ld_15.io.enable <> bb_if_then54.io.Out(5)

  binaryOp_mul716.io.enable <> bb_if_then54.io.Out(6)

  binaryOp_add817.io.enable <> bb_if_then54.io.Out(7)

  Gep_arrayidx918.io.enable <> bb_if_then54.io.Out(8)

  ld_19.io.enable <> bb_if_then54.io.Out(9)

  binaryOp_add1020.io.enable <> bb_if_then54.io.Out(10)

  st_21.io.enable <> bb_if_then54.io.Out(11)

  br_22.io.enable <> bb_if_then54.io.Out(12)


  br_23.io.enable <> bb_if_end5.io.Out(0)


  br_24.io.enable <> bb_if_end116.io.Out(0)


  const8.io.enable <> bb_for_inc7.io.Out(0)

  binaryOp_inc25.io.enable <> bb_for_inc7.io.Out(1)

  br_26.io.enable <> bb_for_inc7.io.Out(2)


  ret_27.io.enable <> bb_for_end8.io.Out(0)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

  phi_nc_01.io.Mask <> bb_for_cond1.io.MaskBB(0)



  /* ================================================================== *
   *                   PRINT ALLOCA OFFSET                              *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */

  MemCtrl.io.ReadIn(0) <> ld_15.io.memReq

  ld_15.io.memResp <> MemCtrl.io.ReadOut(0)

  MemCtrl.io.ReadIn(1) <> ld_19.io.memReq

  ld_19.io.memResp <> MemCtrl.io.ReadOut(1)

  MemCtrl.io.WriteIn(0) <> st_21.io.memReq

  st_21.io.memResp <> MemCtrl.io.WriteOut(0)



  /* ================================================================== *
   *                   PRINT SHARED CONNECTIONS                         *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  phi_nc_01.io.InData(0) <> const0.io.Out(0)

  icmp_cmp2.io.RightIO <> const1.io.Out(0)

  binaryOp_sub5.io.RightIO <> const2.io.Out(0)

  binaryOp_sub27.io.RightIO <> const3.io.Out(0)

  icmp_cmp38.io.RightIO <> const4.io.Out(0)

  icmp_cmp410.io.RightIO <> const5.io.Out(0)

  binaryOp_mul12.io.RightIO <> const6.io.Out(0)

  binaryOp_mul716.io.RightIO <> const7.io.Out(0)

  binaryOp_inc25.io.RightIO <> const8.io.Out(0)

  icmp_cmp2.io.LeftIO <> phi_nc_01.io.Out(0)

  binaryOp_add16.io.RightIO <> phi_nc_01.io.Out(1)

  binaryOp_inc25.io.LeftIO <> phi_nc_01.io.Out(2)

  br_3.io.CmpIO <> icmp_cmp2.io.Out(0)

  binaryOp_sub5.io.LeftIO <> binaryOp_add4.io.Out(0)

  icmp_cmp38.io.LeftIO <> binaryOp_sub5.io.Out(0)

  binaryOp_mul12.io.LeftIO <> binaryOp_sub5.io.Out(1)

  binaryOp_sub27.io.LeftIO <> binaryOp_add16.io.Out(0)

  icmp_cmp410.io.LeftIO <> binaryOp_sub27.io.Out(0)

  binaryOp_add613.io.RightIO <> binaryOp_sub27.io.Out(1)

  br_9.io.CmpIO <> icmp_cmp38.io.Out(0)

  br_11.io.CmpIO <> icmp_cmp410.io.Out(0)

  binaryOp_add613.io.LeftIO <> binaryOp_mul12.io.Out(0)

  Gep_arrayidx14.io.idx1 <> binaryOp_add613.io.Out(0)

  ld_15.io.GepAddr <> Gep_arrayidx14.io.Out.data(0)

  binaryOp_add1020.io.RightIO <> ld_15.io.Out.data(0)

  binaryOp_add817.io.LeftIO <> binaryOp_mul716.io.Out(0)

  Gep_arrayidx918.io.idx1 <> binaryOp_add817.io.Out(0)

  ld_19.io.GepAddr <> Gep_arrayidx918.io.Out.data(0)

  st_21.io.GepAddr <> Gep_arrayidx918.io.Out.data(1)

  binaryOp_add1020.io.LeftIO <> ld_19.io.Out.data(0)

  st_21.io.inData <> binaryOp_add1020.io.Out(0)

//  ret_27.io.In.data("field0") <> st_21.io.Out(0)
  st_21.io.Out(0).ready := true.B
  ret_27.io.In.data("field0") <> DataBundle.active(1.U)

  phi_nc_01.io.InData(1) <> binaryOp_inc25.io.Out(0)



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_27.io.Out

}

import java.io.{File, FileWriter}
object stencil_innerMain extends App {
  val dir = new File("RTL/stencil_inner") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new stencil_innerDF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}
