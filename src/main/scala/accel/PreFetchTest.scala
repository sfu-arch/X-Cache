package accel

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

abstract class prefetchDFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32))))
    val MemResp = Flipped(Valid(new MemResp))
    val MemReq = Decoupled(new MemReq)
    val out = Decoupled(new Call(List(32)))
  })
}

class prefetchDF(implicit p: Parameters) extends prefetchDFIO()(p) {


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

  val bb_entry0 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 2, BID = 0))

  //  %0 = load i32, i32* %arrayidx, align 4, !tbaa !2
  val ld_2 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1, ID=2, RouteID=0))

  //  ret i32 %value.0
  val ret_7 = Module(new RetNode2(retTypes=List(32), ID = 7))

  bb_entry0.io.predicateIn <> InputSplitter.io.Out.enable

  ld_2.io.enable <> bb_entry0.io.Out(0)
  ret_7.io.In.enable <> bb_entry0.io.Out(1)

  MemCtrl.io.ReadIn(0) <> ld_2.io.memReq
  ld_2.io.memResp <> MemCtrl.io.ReadOut(0)

  ld_2.io.GepAddr <> InputSplitter.io.Out.data("field0")(0)
  ret_7.io.In.data("field0") <> ld_2.io.Out(0)

  io.out <> ret_7.io.Out

}

import java.io.{File, FileWriter}
object prefetchMain extends App {
  val dir = new File("RTL/prefetchTest") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new prefetchDF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}
