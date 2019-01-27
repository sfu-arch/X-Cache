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

abstract class test05unDFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32))))
    val MemResp = Flipped(Valid(new MemResp))
    val MemReq = Decoupled(new MemReq)
    val out = Decoupled(new Call(List(32)))
  })
}

class test05unDF(implicit p: Parameters) extends test05unDFIO()(p) {


  /* ================================================================== *
   *                   PRINTING MEMORY MODULES                          *
   * ================================================================== */

  val MemCtrl = Module(new UnifiedController(ID=0, Size=32, NReads=5, NWrites=5)
		 (WControl=new WriteMemoryController(NumOps=5, BaseSize=2, NumEntries=2))
		 (RControl=new ReadMemoryController(NumOps=5, BaseSize=2, NumEntries=2))
		 (RWArbiter=new ReadWriteArbiter()))

  io.MemReq <> MemCtrl.io.MemReq
  MemCtrl.io.MemResp <> io.MemResp

  val InputSplitter = Module(new SplitCallNew(List(10)))
  InputSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */

  val bb_entry0 = Module(new BasicBlockNoMaskFastNode3(NumOuts = 39, BID = 0))



  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */

  //  %0 = load i32, i32* %a, align 4, !tbaa !2
  val ld_0 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=0, RouteID=0))

  //  %mul = shl i32 %0, 1
  val binaryOp_mul1 = Module(new ComputeFastNode(NumOuts = 1, ID = 1, opCode = "shl")(sign=false))

  //  %arrayidx1 = getelementptr inbounds i32, i32* %a, i32 5
  val Gep_arrayidx12 = Module(new GepNode(NumIns = 1, NumOuts=1, ID=2)(ElementSize = 4, ArraySize = List()))

  //  store i32 %mul, i32* %arrayidx1, align 4, !tbaa !2
  val st_3 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, ID=3, RouteID=0))

  //  %arrayidx.1 = getelementptr inbounds i32, i32* %a, i32 1
  val Gep_arrayidx_14 = Module(new GepNode(NumIns = 1, NumOuts=1, ID=4)(ElementSize = 4, ArraySize = List()))

  //  %1 = load i32, i32* %arrayidx.1, align 4, !tbaa !2
  val ld_5 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=5, RouteID=1))

  //  %mul.1 = shl i32 %1, 1
  val binaryOp_mul_16 = Module(new ComputeFastNode(NumOuts = 1, ID = 6, opCode = "shl")(sign=false))

  //  %arrayidx1.1 = getelementptr inbounds i32, i32* %a, i32 6
  val Gep_arrayidx1_17 = Module(new GepNode(NumIns = 1, NumOuts=1, ID=7)(ElementSize = 4, ArraySize = List()))

  //  store i32 %mul.1, i32* %arrayidx1.1, align 4, !tbaa !2
  val st_8 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, ID=8, RouteID=1))

  //  %arrayidx.2 = getelementptr inbounds i32, i32* %a, i32 2
  val Gep_arrayidx_29 = Module(new GepNode(NumIns = 1, NumOuts=1, ID=9)(ElementSize = 4, ArraySize = List()))

  //  %2 = load i32, i32* %arrayidx.2, align 4, !tbaa !2
  val ld_10 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=10, RouteID=2))

  //  %mul.2 = shl i32 %2, 1
  val binaryOp_mul_211 = Module(new ComputeFastNode(NumOuts = 1, ID = 11, opCode = "shl")(sign=false))

  //  %arrayidx1.2 = getelementptr inbounds i32, i32* %a, i32 7
  val Gep_arrayidx1_212 = Module(new GepNode(NumIns = 1, NumOuts=1, ID=12)(ElementSize = 4, ArraySize = List()))

  //  store i32 %mul.2, i32* %arrayidx1.2, align 4, !tbaa !2
  val st_13 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, ID=13, RouteID=2))

  //  %arrayidx.3 = getelementptr inbounds i32, i32* %a, i32 3
  val Gep_arrayidx_314 = Module(new GepNode(NumIns = 1, NumOuts=1, ID=14)(ElementSize = 4, ArraySize = List()))

  //  %3 = load i32, i32* %arrayidx.3, align 4, !tbaa !2
  val ld_15 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=15, RouteID=3))

  //  %mul.3 = shl i32 %3, 1
  val binaryOp_mul_316 = Module(new ComputeFastNode(NumOuts = 1, ID = 16, opCode = "shl")(sign=false))

  //  %arrayidx1.3 = getelementptr inbounds i32, i32* %a, i32 8
  val Gep_arrayidx1_317 = Module(new GepNode(NumIns = 1, NumOuts=1, ID=17)(ElementSize = 4, ArraySize = List()))

  //  store i32 %mul.3, i32* %arrayidx1.3, align 4, !tbaa !2
  val st_18 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, ID=18, RouteID=3))

  //  %arrayidx.4 = getelementptr inbounds i32, i32* %a, i32 4
  val Gep_arrayidx_419 = Module(new GepNode(NumIns = 1, NumOuts=1, ID=19)(ElementSize = 4, ArraySize = List()))

  //  %4 = load i32, i32* %arrayidx.4, align 4, !tbaa !2
  val ld_20 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=20, RouteID=4))

  //  %mul.4 = shl i32 %4, 1
  val binaryOp_mul_421 = Module(new ComputeFastNode(NumOuts = 2, ID = 21, opCode = "shl")(sign=false))

  //  %arrayidx1.4 = getelementptr inbounds i32, i32* %a, i32 9
  val Gep_arrayidx1_422 = Module(new GepNode(NumIns = 1, NumOuts=1, ID=22)(ElementSize = 4, ArraySize = List()))

  //  store i32 %mul.4, i32* %arrayidx1.4, align 4, !tbaa !2
  val st_23 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, ID=23, RouteID=4))

  //  ret i32 %mul.4
  val ret_24 = Module(new RetNode2(retTypes=List(32), ID = 24))



  /* ================================================================== *
   *                   PRINTING CONSTANTS NODES                         *
   * ================================================================== */

  //i32 1
  val const0 = Module(new ConstFastNode(value = 1, ID = 0))

  //i32 5
  val const1 = Module(new ConstFastNode(value = 5, ID = 1))

  //i32 1
  val const2 = Module(new ConstFastNode(value = 1, ID = 2))

  //i32 1
  val const3 = Module(new ConstFastNode(value = 1, ID = 3))

  //i32 6
  val const4 = Module(new ConstFastNode(value = 6, ID = 4))

  //i32 2
  val const5 = Module(new ConstFastNode(value = 2, ID = 5))

  //i32 1
  val const6 = Module(new ConstFastNode(value = 1, ID = 6))

  //i32 7
  val const7 = Module(new ConstFastNode(value = 7, ID = 7))

  //i32 3
  val const8 = Module(new ConstFastNode(value = 3, ID = 8))

  //i32 1
  val const9 = Module(new ConstFastNode(value = 1, ID = 9))

  //i32 8
  val const10 = Module(new ConstFastNode(value = 8, ID = 10))

  //i32 4
  val const11 = Module(new ConstFastNode(value = 4, ID = 11))

  //i32 1
  val const12 = Module(new ConstFastNode(value = 1, ID = 12))

  //i32 9
  val const13 = Module(new ConstFastNode(value = 9, ID = 13))



  /* ================================================================== *
   *                   BASICBLOCK -> PREDICATE INSTRUCTION              *
   * ================================================================== */

  bb_entry0.io.predicateIn <> InputSplitter.io.Out.enable



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
   *                   BASICBLOCK -> ENABLE INSTRUCTION                 *
   * ================================================================== */

  const0.io.enable <> bb_entry0.io.Out(0)

  const1.io.enable <> bb_entry0.io.Out(1)

  const2.io.enable <> bb_entry0.io.Out(2)

  const3.io.enable <> bb_entry0.io.Out(3)

  const4.io.enable <> bb_entry0.io.Out(4)

  const5.io.enable <> bb_entry0.io.Out(5)

  const6.io.enable <> bb_entry0.io.Out(6)

  const7.io.enable <> bb_entry0.io.Out(7)

  const8.io.enable <> bb_entry0.io.Out(8)

  const9.io.enable <> bb_entry0.io.Out(9)

  const10.io.enable <> bb_entry0.io.Out(10)

  const11.io.enable <> bb_entry0.io.Out(11)

  const12.io.enable <> bb_entry0.io.Out(12)

  const13.io.enable <> bb_entry0.io.Out(13)

  ld_0.io.enable <> bb_entry0.io.Out(14)

  binaryOp_mul1.io.enable <> bb_entry0.io.Out(15)

  Gep_arrayidx12.io.enable <> bb_entry0.io.Out(16)

  st_3.io.enable <> bb_entry0.io.Out(17)

  Gep_arrayidx_14.io.enable <> bb_entry0.io.Out(18)

  ld_5.io.enable <> bb_entry0.io.Out(19)

  binaryOp_mul_16.io.enable <> bb_entry0.io.Out(20)

  Gep_arrayidx1_17.io.enable <> bb_entry0.io.Out(21)

  st_8.io.enable <> bb_entry0.io.Out(22)

  Gep_arrayidx_29.io.enable <> bb_entry0.io.Out(23)

  ld_10.io.enable <> bb_entry0.io.Out(24)

  binaryOp_mul_211.io.enable <> bb_entry0.io.Out(25)

  Gep_arrayidx1_212.io.enable <> bb_entry0.io.Out(26)

  st_13.io.enable <> bb_entry0.io.Out(27)

  Gep_arrayidx_314.io.enable <> bb_entry0.io.Out(28)

  ld_15.io.enable <> bb_entry0.io.Out(29)

  binaryOp_mul_316.io.enable <> bb_entry0.io.Out(30)

  Gep_arrayidx1_317.io.enable <> bb_entry0.io.Out(31)

  st_18.io.enable <> bb_entry0.io.Out(32)

  Gep_arrayidx_419.io.enable <> bb_entry0.io.Out(33)

  ld_20.io.enable <> bb_entry0.io.Out(34)

  binaryOp_mul_421.io.enable <> bb_entry0.io.Out(35)

  Gep_arrayidx1_422.io.enable <> bb_entry0.io.Out(36)

  st_23.io.enable <> bb_entry0.io.Out(37)

  ret_24.io.In.enable <> bb_entry0.io.Out(38)




  /* ================================================================== *
   *                   CONNECTING PHI NODES                             *
   * ================================================================== */



  /* ================================================================== *
   *                   PRINT ALLOCA OFFSET                              *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING MEMORY CONNECTIONS                    *
   * ================================================================== */

  MemCtrl.io.ReadIn(0) <> ld_0.io.memReq

  ld_0.io.memResp <> MemCtrl.io.ReadOut(0)

  MemCtrl.io.WriteIn(0) <> st_3.io.memReq

  st_3.io.memResp <> MemCtrl.io.WriteOut(0)

  MemCtrl.io.ReadIn(1) <> ld_5.io.memReq

  ld_5.io.memResp <> MemCtrl.io.ReadOut(1)

  MemCtrl.io.WriteIn(1) <> st_8.io.memReq

  st_8.io.memResp <> MemCtrl.io.WriteOut(1)

  MemCtrl.io.ReadIn(2) <> ld_10.io.memReq

  ld_10.io.memResp <> MemCtrl.io.ReadOut(2)

  MemCtrl.io.WriteIn(2) <> st_13.io.memReq

  st_13.io.memResp <> MemCtrl.io.WriteOut(2)

  MemCtrl.io.ReadIn(3) <> ld_15.io.memReq

  ld_15.io.memResp <> MemCtrl.io.ReadOut(3)

  MemCtrl.io.WriteIn(3) <> st_18.io.memReq

  st_18.io.memResp <> MemCtrl.io.WriteOut(3)

  MemCtrl.io.ReadIn(4) <> ld_20.io.memReq

  ld_20.io.memResp <> MemCtrl.io.ReadOut(4)

  MemCtrl.io.WriteIn(4) <> st_23.io.memReq

  st_23.io.memResp <> MemCtrl.io.WriteOut(4)



  /* ================================================================== *
   *                   PRINT SHARED CONNECTIONS                         *
   * ================================================================== */



  /* ================================================================== *
   *                   CONNECTING DATA DEPENDENCIES                     *
   * ================================================================== */

  binaryOp_mul1.io.RightIO <> const0.io.Out

  Gep_arrayidx12.io.idx(0) <> const1.io.Out

  Gep_arrayidx_14.io.idx(0) <> const2.io.Out

  binaryOp_mul_16.io.RightIO <> const3.io.Out

  Gep_arrayidx1_17.io.idx(0) <> const4.io.Out

  Gep_arrayidx_29.io.idx(0) <> const5.io.Out

  binaryOp_mul_211.io.RightIO <> const6.io.Out

  Gep_arrayidx1_212.io.idx(0) <> const7.io.Out

  Gep_arrayidx_314.io.idx(0) <> const8.io.Out

  binaryOp_mul_316.io.RightIO <> const9.io.Out

  Gep_arrayidx1_317.io.idx(0) <> const10.io.Out

  Gep_arrayidx_419.io.idx(0) <> const11.io.Out

  binaryOp_mul_421.io.RightIO <> const12.io.Out

  Gep_arrayidx1_422.io.idx(0) <> const13.io.Out

  binaryOp_mul1.io.LeftIO <> ld_0.io.Out(0)

  st_3.io.inData <> binaryOp_mul1.io.Out(0)

  st_3.io.GepAddr <> Gep_arrayidx12.io.Out(0)

  ld_5.io.GepAddr <> Gep_arrayidx_14.io.Out(0)

  binaryOp_mul_16.io.LeftIO <> ld_5.io.Out(0)

  st_8.io.inData <> binaryOp_mul_16.io.Out(0)

  st_8.io.GepAddr <> Gep_arrayidx1_17.io.Out(0)

  ld_10.io.GepAddr <> Gep_arrayidx_29.io.Out(0)

  binaryOp_mul_211.io.LeftIO <> ld_10.io.Out(0)

  st_13.io.inData <> binaryOp_mul_211.io.Out(0)

  st_13.io.GepAddr <> Gep_arrayidx1_212.io.Out(0)

  ld_15.io.GepAddr <> Gep_arrayidx_314.io.Out(0)

  binaryOp_mul_316.io.LeftIO <> ld_15.io.Out(0)

  st_18.io.inData <> binaryOp_mul_316.io.Out(0)

  st_18.io.GepAddr <> Gep_arrayidx1_317.io.Out(0)

  ld_20.io.GepAddr <> Gep_arrayidx_419.io.Out(0)

  binaryOp_mul_421.io.LeftIO <> ld_20.io.Out(0)

  st_23.io.inData <> binaryOp_mul_421.io.Out(0)

  ret_24.io.In.data("field0") <> binaryOp_mul_421.io.Out(1)

  st_23.io.GepAddr <> Gep_arrayidx1_422.io.Out(0)

  ld_0.io.GepAddr <> InputSplitter.io.Out.data("field0")(0)

  Gep_arrayidx12.io.baseAddress <> InputSplitter.io.Out.data("field0")(1)

  Gep_arrayidx_14.io.baseAddress <> InputSplitter.io.Out.data("field0")(2)

  Gep_arrayidx1_17.io.baseAddress <> InputSplitter.io.Out.data("field0")(3)

  Gep_arrayidx_29.io.baseAddress <> InputSplitter.io.Out.data("field0")(4)

  Gep_arrayidx1_212.io.baseAddress <> InputSplitter.io.Out.data("field0")(5)

  Gep_arrayidx_314.io.baseAddress <> InputSplitter.io.Out.data("field0")(6)

  Gep_arrayidx1_317.io.baseAddress <> InputSplitter.io.Out.data("field0")(7)

  Gep_arrayidx_419.io.baseAddress <> InputSplitter.io.Out.data("field0")(8)

  Gep_arrayidx1_422.io.baseAddress <> InputSplitter.io.Out.data("field0")(9)

  st_3.io.Out(0).ready := true.B

  st_8.io.Out(0).ready := true.B

  st_13.io.Out(0).ready := true.B

  st_18.io.Out(0).ready := true.B

  st_23.io.Out(0).ready := true.B



  /* ================================================================== *
   *                   PRINTING OUTPUT INTERFACE                        *
   * ================================================================== */

  io.out <> ret_24.io.Out

}

import java.io.{File, FileWriter}
object test05unMain extends App {
  val dir = new File("RTL/test05") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new test05unDF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}
