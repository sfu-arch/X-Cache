package dataflow

import chisel3._
import chisel3.util._
import chisel3.Module
import chisel3.testers._
import chisel3.iotesters._
import org.scalatest.{FlatSpec, Matchers}
import muxes._
import config._
import control._
import util._
import interfaces._
import regfile._
import memory._
import stack._
import arbiters._
import loop._
import accel._
import node._
import junctions._


/**
  * This Object should be initialized at the first step
  * It contains all the transformation from indices to their module's name
  */

object Data_test02_FlowParam{

  val bb_entry_pred = Map(
    "active" -> 0
  )


  val bb_if_then_pred = Map(
    "br2" -> 0
  )


  val bb_if_end_pred = Map(
    "br3" -> 0,
    "br5" -> 1
  )


  val bb_entry_if_end_crit_edge_pred = Map(
    "br2" -> 0
  )


  val br2_brn_bb = Map(
    "bb_if_then" -> 0,
    "bb_entry_if_end_crit_edge" -> 1
  )


  val br3_brn_bb = Map(
    "bb_if_end" -> 0
  )


  val br5_brn_bb = Map(
    "bb_if_end" -> 0
  )


  val bb_entry_activate = Map(
    "udiv0" -> 0,
    "icmp1" -> 1,
    "br2" -> 2
  )


  val bb_entry_if_end_crit_edge_activate = Map(
    "br3" -> 0
  )


  val bb_if_then_activate = Map(
    "add4" -> 0,
    "br5" -> 1
  )


  val bb_if_end_activate = Map(
    "phi6" -> 0,
    "ret7" -> 1
  )


  val phi6_phi_in = Map(
    "add4" -> 0,
    "const_1" -> 1
  )


  //  %div = udiv i32 %a, 2, !UID !8, !ScalaLabel !9
  val udiv0_in = Map(
    "field0" -> 0
  )


  //  %cmp = icmp eq i32 %div, 4, !UID !10, !ScalaLabel !11
  val icmp1_in = Map(
    "udiv0" -> 0
  )


  //  br i1 %cmp, label %if.then, label %entry.if.end_crit_edge, !UID !12, !BB_UID !13, !ScalaLabel !14
  val br2_in = Map(
    "icmp1" -> 0
  )


  //  %add = add i32 %a, %b, !UID !16, !ScalaLabel !17
  val add4_in = Map(
    "field0" -> 1,
    "field1" -> 0
  )


  //  %sum.0 = phi i32 [ %add, %if.then ], [ 0, %entry.if.end_crit_edge ], !UID !21, !ScalaLabel !22
  val phi6_in = Map(
    "add4" -> 0
  )


  //  ret i32 %sum.0, !UID !23, !BB_UID !24, !ScalaLabel !25
  val ret7_in = Map(
    "phi6" -> 0
  )


}




  /* ================================================================== *
   *                   PRINTING PORTS DEFINITION                        *
   * ================================================================== */


abstract class test02DFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32,32))))
    val CacheResp = Flipped(Valid(new CacheRespT))
    val CacheReq = Decoupled(new CacheReq)
    val out = Decoupled(new Call(List(32)))
  })
}




  /* ================================================================== *
   *                   PRINTING MODULE DEFINITION                       *
   * ================================================================== */


class test02DF(implicit p: Parameters) extends test02DFIO()(p) {



  /* ================================================================== *
   *                   PRINTING MEMORY SYSTEM                           *
   * ================================================================== */


	val StackPointer = Module(new Stack(NumOps = 1))

	val RegisterFile = Module(new TypeStackFile(ID=0,Size=32,NReads=2,NWrites=2)
		            (WControl=new WriteMemoryController(NumOps=2,BaseSize=2,NumEntries=2))
		            (RControl=new ReadMemoryController(NumOps=2,BaseSize=2,NumEntries=2)))

	val CacheMem = Module(new UnifiedController(ID=0,Size=32,NReads=2,NWrites=2)
		            (WControl=new WriteMemoryController(NumOps=2,BaseSize=2,NumEntries=2))
		            (RControl=new ReadMemoryController(NumOps=2,BaseSize=2,NumEntries=2))
		            (RWArbiter=new ReadWriteArbiter()))

  io.CacheReq <> CacheMem.io.CacheReq
  CacheMem.io.CacheResp <> io.CacheResp

  val InputSplitter = Module(new SplitCall(List(32,32)))
  InputSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */


  //Function doesn't have any loop


  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */


  //Initializing BasicBlocks: 

  val bb_entry = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 3, BID = 0))

  val bb_entry_if_end_crit_edge = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 1))

  val bb_if_then = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 2, BID = 2))

  val bb_if_end = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 2, NumPhi = 1, BID = 3))






  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */


  //Initializing Instructions: 

  // [BasicBlock]  entry:

  //  %div = udiv i32 %a, 2, !UID !8, !ScalaLabel !9
  val udiv0 = Module (new ComputeNode(NumOuts = 1, ID = 0, opCode = "udiv")(sign=false))


  //  %cmp = icmp eq i32 %div, 4, !UID !10, !ScalaLabel !11
  val icmp1 = Module (new IcmpNode(NumOuts = 1, ID = 1, opCode = "EQ")(sign=false))


  //  br i1 %cmp, label %if.then, label %entry.if.end_crit_edge, !UID !12, !BB_UID !13, !ScalaLabel !14
  val br2 = Module (new CBranchNode(ID = 2))



  // [BasicBlock]  entry.if.end_crit_edge:

  //  br label %if.end, !ScalaLabel !15
  val br3 = Module (new UBranchNode(ID = 3))



  // [BasicBlock]  if.then:

  //  %add = add i32 %a, %b, !UID !16, !ScalaLabel !17
  val add4 = Module (new ComputeNode(NumOuts = 1, ID = 4, opCode = "add")(sign=false))


  //  br label %if.end, !UID !18, !BB_UID !19, !ScalaLabel !20
  val br5 = Module (new UBranchNode(ID = 5))



  // [BasicBlock]  if.end:

  //  %sum.0 = phi i32 [ %add, %if.then ], [ 0, %entry.if.end_crit_edge ], !UID !21, !ScalaLabel !22
  val phi6 = Module (new PhiNode(NumInputs = 2, NumOuts = 1, ID = 6))


  //  ret i32 %sum.0, !UID !23, !BB_UID !24, !ScalaLabel !25
  val ret7 = Module(new RetNode(NumPredIn=1, retTypes=List(32), ID=7))







  /* ================================================================== *
   *                   INITIALIZING PARAM                               *
   * ================================================================== */


  /**
    * Instantiating parameters
    */
  val param = Data_test02_FlowParam



  /* ================================================================== *
   *                   CONNECTING BASIC BLOCKS TO PREDICATE INSTRUCTIONS*
   * ================================================================== */


  /**
     * Connecting basic blocks to predicate instructions
     */


  bb_entry.io.predicateIn <> InputSplitter.io.Out.enable

  /**
    * Connecting basic blocks to predicate instructions
    */

  //Connecting br2 to bb_if_then
  bb_if_then.io.predicateIn(param.bb_if_then_pred("br2")) <> br2.io.Out(param.br2_brn_bb("bb_if_then"))


  //Connecting br2 to bb_entry_if_end_crit_edge
  bb_entry_if_end_crit_edge.io.predicateIn(param.bb_entry_if_end_crit_edge_pred("br2")) <> br2.io.Out(param.br2_brn_bb("bb_entry_if_end_crit_edge"))


  //Connecting br3 to bb_if_end
  bb_if_end.io.predicateIn(param.bb_if_end_pred("br3")) <> br3.io.Out(param.br3_brn_bb("bb_if_end"))


  //Connecting br5 to bb_if_end
  bb_if_end.io.predicateIn(param.bb_if_end_pred("br5")) <> br5.io.Out(param.br5_brn_bb("bb_if_end"))




  /* ================================================================== *
   *                   CONNECTING BASIC BLOCKS TO INSTRUCTIONS          *
   * ================================================================== */


  /**
    * Wiring enable signals to the instructions
    */

  udiv0.io.enable <> bb_entry.io.Out(param.bb_entry_activate("udiv0"))

  icmp1.io.enable <> bb_entry.io.Out(param.bb_entry_activate("icmp1"))

  br2.io.enable <> bb_entry.io.Out(param.bb_entry_activate("br2"))



  br3.io.enable <> bb_entry_if_end_crit_edge.io.Out(param.bb_entry_if_end_crit_edge_activate("br3"))



  add4.io.enable <> bb_if_then.io.Out(param.bb_if_then_activate("add4"))

  br5.io.enable <> bb_if_then.io.Out(param.bb_if_then_activate("br5"))



  phi6.io.enable <> bb_if_end.io.Out(param.bb_if_end_activate("phi6"))

  ret7.io.enable <> bb_if_end.io.Out(param.bb_if_end_activate("ret7"))





  /* ================================================================== *
   *                   CONNECTING LOOPHEADERS                           *
   * ================================================================== */


  //Function doesn't have any for loop


  /* ================================================================== *
   *                   DUMPING PHI NODES                                *
   * ================================================================== */


  /**
    * Connecting PHI Masks
    */
  //Connect PHI node

  phi6.io.InData(param.phi6_phi_in("add4")) <> add4.io.Out(param.phi6_in("add4"))

  phi6.io.InData(param.phi6_phi_in("const_1")).bits.data := 0.U
  phi6.io.InData(param.phi6_phi_in("const_1")).bits.predicate := true.B
  phi6.io.InData(param.phi6_phi_in("const_1")).valid := true.B

  /**
    * Connecting PHI Masks
    */
  //Connect PHI node

  phi6.io.Mask <> bb_if_end.io.MaskBB(0)



  /* ================================================================== *
   *                   DUMPING DATAFLOW                                 *
   * ================================================================== */


  /**
    * Connecting Dataflow signals
    */

  // Wiring Binary instruction to the function argument
  udiv0.io.LeftIO <> InputSplitter.io.Out.data("field0")

  // Wiring constant
  udiv0.io.RightIO.bits.data := 2.U
  udiv0.io.RightIO.bits.predicate := true.B
  udiv0.io.RightIO.valid := true.B

  // Wiring instructions
  icmp1.io.LeftIO <> udiv0.io.Out(param.icmp1_in("udiv0"))

  // Wiring constant
  icmp1.io.RightIO.bits.data := 4.U
  icmp1.io.RightIO.bits.predicate := true.B
  icmp1.io.RightIO.valid := true.B

  // Wiring Branch instruction
  br2.io.CmpIO <> icmp1.io.Out(param.br2_in("icmp1"))

  // Wiring Binary instruction to the function argument
  add4.io.LeftIO <> InputSplitter.io.Out.data("field0")

  // Wiring Binary instruction to the function argument
  add4.io.RightIO <> InputSplitter.io.Out.data("field1")

  // Wiring return instruction
  ret7.io.predicateIn(0).bits.control := true.B
  ret7.io.predicateIn(0).bits.taskID := 0.U
  ret7.io.predicateIn(0).valid := true.B
  ret7.io.In.data("field0") <> phi6.io.Out(param.ret7_in("phi6"))
  io.out <> ret7.io.Out


}

import java.io.{File, FileWriter}
object test02Main extends App {
  val dir = new File("RTL/test02") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new test02DF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}

