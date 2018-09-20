package dataflow

import FPU._
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

abstract class test18DFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32, 32))))
    val MemResp = Flipped(Valid(new MemResp))
    val MemReq = Decoupled(new MemReq)
    val out = Decoupled(new Call(List(32)))
  })
}

class test18DF(implicit p: Parameters) extends test18DFIO()(p) {


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  val MemCtrl = Module(new UnifiedController(ID=0, Size=32, NReads=2, NWrites=2)
		 (WControl=new WriteMemoryController(NumOps=2, BaseSize=2, NumEntries=2))
		 (RControl=new ReadMemoryController(NumOps=2, BaseSize=2, NumEntries=2))
		 (RWArbiter=new ReadWriteArbiter()))

  io.MemReq <> MemCtrl.io.MemReq
  MemCtrl.io.MemResp <> io.MemResp

  val InputSplitter = Module(new SplitCallNew(List(2,3)))
  InputSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val Loop_0 = Module(new LoopBlock(NumIns=List(1,1), NumOuts = 0, NumExits=1, ID = 0))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_entry0 = Module(new BasicBlockNoMaskFastNode3(NumOuts = 3, BID = 0))

  val bb_for_body_preheader1 = Module(new BasicBlockNoMaskFastNode3(NumOuts = 1, BID = 1))

  val bb_for_cond_cleanup_loopexit2 = Module(new BasicBlockNoMaskFastNode3(NumOuts = 1, BID = 2))

  val bb_for_cond_cleanup3 = Module(new BasicBlockNoMaskNode(NumInputs = 2, NumOuts = 9, BID = 3))

  val bb_for_body4 = Module(new LoopHead(NumOuts = 11, NumPhi=1, BID = 4))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  %cmp13 = icmp eq i32 %n, 0
  val icmp_cmp130 = Module(new IcmpFastNode(NumOuts = 1, ID = 0, opCode = "eq")(sign=false))

  //  br i1 %cmp13, label %for.cond.cleanup, label %for.body.preheader
  val br_1 = Module(new CBranchFastNodeVariable(ID = 1))

  //  br label %for.body
  val br_2 = Module(new UBranchFastNode(ID = 2))

  //  br label %for.cond.cleanup
  val br_3 = Module(new UBranchFastNode(ID = 3))

  //  %sub = add i32 %n, -1
  val binaryOp_sub4 = Module(new ComputeFastNode(NumOuts = 1, ID = 4, opCode = "add")(sign=false))

  //  %arrayidx2 = getelementptr inbounds i32, i32* %a, i32 %sub
  val Gep_arrayidx25 = Module(new GepNode(NumIns = 1, NumOuts=2, ID=5)(ElementSize = 4, ArraySize = List()))

  //  %0 = load i32, i32* %arrayidx2, align 4, !tbaa !2
  val ld_6 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=6, RouteID=0))

  //  %inc3 = add i32 %0, 1
  val binaryOp_inc37 = Module(new ComputeFastNode(NumOuts = 2, ID = 7, opCode = "add")(sign=false))

  //  store i32 %inc3, i32* %arrayidx2, align 4, !tbaa !2
  val st_8 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, ID=8, RouteID=0))

  //  ret i32 %inc3
  val ret_9 = Module(new RetNode2(retTypes=List(32), ID = 9))

  //  %k.014 = phi i32 [ %inc, %for.body ], [ 0, %for.body.preheader ]
  val phi_k_01410 = Module(new PhiFastNode(NumInputs = 2, NumOutputs = 2, ID = 10))

  //  %arrayidx = getelementptr inbounds i32, i32* %a, i32 %k.014
  val Gep_arrayidx11 = Module(new GepNode(NumIns = 1, NumOuts=2, ID=11)(ElementSize = 4, ArraySize = List()))

  //  %1 = load i32, i32* %arrayidx, align 4, !tbaa !2
  val ld_12 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=12, RouteID=1))

  //  %mul = shl i32 %1, 1
  val binaryOp_mul13 = Module(new ComputeFastNode(NumOuts = 1, ID = 13, opCode = "shl")(sign=false))

  //  store i32 %mul, i32* %arrayidx, align 4, !tbaa !2
  val st_14 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, ID=14, RouteID=1))

  //  %inc = add nuw i32 %k.014, 1
  val binaryOp_inc15 = Module(new ComputeFastNode(NumOuts = 2, ID = 15, opCode = "add")(sign=false))

  //  %exitcond = icmp eq i32 %inc, %n
  val icmp_exitcond16 = Module(new IcmpFastNode(NumOuts = 1, ID = 16, opCode = "eq")(sign=false))

  //  br i1 %exitcond, label %for.cond.cleanup.loopexit, label %for.body
  val br_17 = Module(new CBranchFastNodeVariable(NumFalse = 2, ID = 17))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i32 0
  val const0 = Module(new ConstFastNode(value = 0, ID = 0))

  //i32 -1
  val const1 = Module(new ConstFastNode(value = -1, ID = 1))

  //i32 1
  val const2 = Module(new ConstFastNode(value = 1, ID = 2))

  //i32 0
  val const3 = Module(new ConstFastNode(value = 0, ID = 3))

  //i32 0
  val const4 = Module(new ConstFastNode(value = 0, ID = 4))

  //i32 1
  val const5 = Module(new ConstFastNode(value = 1, ID = 5))

  //i32 1
  val const6 = Module(new ConstFastNode(value = 1, ID = 6))



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_entry0.io.predicateIn <> InputSplitter.io.Out.enable

  bb_for_body_preheader1.io.predicateIn <> br_1.io.FalseOutput(0)

  bb_for_cond_cleanup_loopexit2.io.predicateIn <> Loop_0.io.endEnable

  bb_for_cond_cleanup3.io.predicateIn <> br_1.io.TrueOutput(0)

  bb_for_cond_cleanup3.io.predicateIn <> br_3.io.Out(0)

  bb_for_body4.io.activate <> Loop_0.io.activate

  bb_for_body4.io.loopBack <> br_17.io.FalseOutput(0)



  /* ================================================================== *
   *                   PRINTING PARALLEL CONNECTIONS                    *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP -> PREDICATE INSTRUCTION                    *
   * ================================================================== */

  Loop_0.io.enable <> br_2.io.Out(0)

  Loop_0.io.latchEnable <> br_17.io.FalseOutput(1)

  Loop_0.io.loopExit(0) <> br_17.io.TrueOutput(0)



  /* ================================================================== *
   *                   ENDING INSTRUCTIONS                              *
   * ================================================================== */



  /* ================================================================== *
   *                   LOOP INPUT DATA DEPENDENCIES                     *
   * ================================================================== */

  Loop_0.io.In(0) <> InputSplitter.io.Out.data("field0")(1)

  Loop_0.io.In(1) <> InputSplitter.io.Out.data("field1")(2)



  /* ================================================================== *
   *                   LOOP DATA LIVE-IN DEPENDENCIES                   *
   * ================================================================== */

  Gep_arrayidx11.io.baseAddress <> Loop_0.io.liveIn.data("field0")(0)

  icmp_exitcond16.io.RightIO <> Loop_0.io.liveIn.data("field1")(0)



  /* ================================================================== *
   *                   LOOP DATA LIVE-OUT DEPENDENCIES                  *
   * ================================================================== */



  /* ================================================================== *
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  const0.io.enable <> bb_entry0.io.Out(0)

  icmp_cmp130.io.enable <> bb_entry0.io.Out(1)

  br_1.io.enable <> bb_entry0.io.Out(2)


  br_2.io.enable <> bb_for_body_preheader1.io.Out(0)


  br_3.io.enable <> bb_for_cond_cleanup_loopexit2.io.Out(0)


  const1.io.enable <> bb_for_cond_cleanup3.io.Out(0)

  const2.io.enable <> bb_for_cond_cleanup3.io.Out(1)

  const3.io.enable <> bb_for_cond_cleanup3.io.Out(2)

  binaryOp_sub4.io.enable <> bb_for_cond_cleanup3.io.Out(3)

  Gep_arrayidx25.io.enable <> bb_for_cond_cleanup3.io.Out(4)

  ld_6.io.enable <> bb_for_cond_cleanup3.io.Out(5)

  binaryOp_inc37.io.enable <> bb_for_cond_cleanup3.io.Out(6)

  st_8.io.enable <> bb_for_cond_cleanup3.io.Out(7)

  ret_9.io.In.enable <> bb_for_cond_cleanup3.io.Out(8)


  const4.io.enable <> bb_for_body4.io.Out(0)

  const5.io.enable <> bb_for_body4.io.Out(1)

  const6.io.enable <> bb_for_body4.io.Out(2)

  phi_k_01410.io.enable <> bb_for_body4.io.Out(3)

  Gep_arrayidx11.io.enable <> bb_for_body4.io.Out(4)

  ld_12.io.enable <> bb_for_body4.io.Out(5)

  binaryOp_mul13.io.enable <> bb_for_body4.io.Out(6)

  st_14.io.enable <> bb_for_body4.io.Out(7)

  binaryOp_inc15.io.enable <> bb_for_body4.io.Out(8)

  icmp_exitcond16.io.enable <> bb_for_body4.io.Out(9)

  br_17.io.enable <> bb_for_body4.io.Out(10)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */

//  phi_k_01410.io.Mask <> bb_for_body4.io.MaskBB(0)
  phi_k_01410.io.Mask.bits := Reverse(bb_for_body4.io.MaskBB(0).bits)
  phi_k_01410.io.Mask.valid := bb_for_body4.io.MaskBB(0).valid
  bb_for_body4.io.MaskBB(0).ready := phi_k_01410.io.Mask.ready



  /* ================================================================== *
   *                   PRINT ALLOCA OFFSET                              *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */

  MemCtrl.io.ReadIn(0) <> ld_6.io.memReq

  ld_6.io.memResp <> MemCtrl.io.ReadOut(0)

  MemCtrl.io.WriteIn(0) <> st_8.io.memReq

  st_8.io.memResp <> MemCtrl.io.WriteOut(0)

  MemCtrl.io.ReadIn(1) <> ld_12.io.memReq

  ld_12.io.memResp <> MemCtrl.io.ReadOut(1)

  MemCtrl.io.WriteIn(1) <> st_14.io.memReq

  st_14.io.memResp <> MemCtrl.io.WriteOut(1)



  /* ================================================================== *
   *                   PRINT SHARED CONNECTIONS                         *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  icmp_cmp130.io.RightIO <> const0.io.Out

  binaryOp_sub4.io.RightIO <> const1.io.Out

  binaryOp_inc37.io.RightIO <> const2.io.Out

  //  ret_9.io.In.data("field0") <> const3.io.Out
  const3.io.Out.ready := true.B
  ret_9.io.In.data("field0") <> binaryOp_inc37.io.Out(0)

  phi_k_01410.io.InData(1) <> const4.io.Out

  binaryOp_mul13.io.RightIO <> const5.io.Out

  binaryOp_inc15.io.RightIO <> const6.io.Out

  br_1.io.CmpIO <> icmp_cmp130.io.Out(0)

  Gep_arrayidx25.io.idx(0) <> binaryOp_sub4.io.Out(0)

  ld_6.io.GepAddr <> Gep_arrayidx25.io.Out(0)

  st_8.io.GepAddr <> Gep_arrayidx25.io.Out(1)

  binaryOp_inc37.io.LeftIO <> ld_6.io.Out(0)

  st_8.io.inData <> binaryOp_inc37.io.Out(0)

  Gep_arrayidx11.io.idx(0) <> phi_k_01410.io.Out(0)

  binaryOp_inc15.io.LeftIO <> phi_k_01410.io.Out(1)

  ld_12.io.GepAddr <> Gep_arrayidx11.io.Out(0)

  st_14.io.GepAddr <> Gep_arrayidx11.io.Out(1)

  binaryOp_mul13.io.LeftIO <> ld_12.io.Out(0)

  st_14.io.inData <> binaryOp_mul13.io.Out(0)

  phi_k_01410.io.InData(0) <> binaryOp_inc15.io.Out(0)

  icmp_exitcond16.io.LeftIO <> binaryOp_inc15.io.Out(1)

  br_17.io.CmpIO <> icmp_exitcond16.io.Out(0)

  Gep_arrayidx25.io.baseAddress <> InputSplitter.io.Out.data("field0")(0)

  icmp_cmp130.io.LeftIO <> InputSplitter.io.Out.data("field1")(0)

  binaryOp_sub4.io.LeftIO <> InputSplitter.io.Out.data("field1")(1)

  st_8.io.Out(0).ready := true.B

  st_14.io.Out(0).ready := true.B



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_9.io.Out

}

import java.io.{File, FileWriter}
object test18Main extends App {
  val dir = new File("RTL/test18") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new test18DF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}
