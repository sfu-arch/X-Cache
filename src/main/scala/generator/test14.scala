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

object Data_test14_FlowParam{

  val bb_entry_pred = Map(
    "active" -> 0
  )


  val bb_for_cond_pred = Map(
    "br0" -> 0,
    "br9" -> 1
  )


  val bb_for_inc_pred = Map(
    "br7" -> 0
  )


  val bb_for_body_pred = Map(
    "br4" -> 0
  )


  val bb_for_end_pred = Map(
    "br4" -> 0
  )


  val br0_brn_bb = Map(
    "bb_for_cond" -> 0
  )


  val br4_brn_bb = Map(
    "bb_for_body" -> 0,
    "bb_for_end" -> 1
  )


  val br7_brn_bb = Map(
    "bb_for_inc" -> 0
  )


  val br9_brn_bb = Map(
    "bb_for_cond" -> 0
  )


  val bb_entry_activate = Map(
    "br0" -> 0
  )


  val bb_for_cond_activate = Map(
    "phi1" -> 0,
    "phi2" -> 1,
    "icmp3" -> 2,
    "br4" -> 3
  )


  val bb_for_body_activate = Map(
    "call5" -> 0,
    "add6" -> 1,
    "br7" -> 2
  )


  val bb_for_inc_activate = Map(
    "add8" -> 0,
    "br9" -> 1
  )


  val bb_for_end_activate = Map(
    "ret10" -> 0
  )


  val phi1_phi_in = Map(
    "const_0" -> 0,
    "add6" -> 1
  )


  val phi2_phi_in = Map(
    "const_0" -> 0,
    "add8" -> 1
  )


  //  %a.0 = phi i32 [ 0, %entry ], [ %add, %for.inc ], !UID !11, !ScalaLabel !12
  val phi1_in = Map(
    "add6" -> 0
  )


  //  %i.0 = phi i32 [ 0, %entry ], [ %inc, %for.inc ], !UID !13, !ScalaLabel !14
  val phi2_in = Map(
    "add8" -> 0
  )


  //  %cmp = icmp slt i32 %i.0, %m, !UID !15, !ScalaLabel !16
  val icmp3_in = Map(
    "phi2" -> 0,
    "field0" -> 0
  )


  //  br i1 %cmp, label %for.body, label %for.end, !UID !17, !BB_UID !18, !ScalaLabel !19
  val br4_in = Map(
    "icmp3" -> 0
  )


  //  %call = call i32 @test14_inner(i32 %n), !UID !20, !ScalaLabel !21
  val call5_in = Map(
    "field1" -> 0,
    "" -> 0
  )


  //  %add = add nsw i32 %a.0, %call, !UID !22, !ScalaLabel !23
  val add6_in = Map(
    "phi1" -> 0,
    "call5" -> 0
  )


  //  %inc = add nsw i32 %i.0, 1, !UID !27, !ScalaLabel !28
  val add8_in = Map(
    "phi2" -> 1
  )


  //  ret i32 %a.0, !UID !40, !BB_UID !41, !ScalaLabel !42
  val ret10_in = Map(
    "phi1" -> 1
  )


}




  /* ================================================================== *
   *                   PRINTING PORTS DEFINITION                        *
   * ================================================================== */


abstract class test14DFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32,32))))
    val call5_out = Decoupled(new Call(List(32)))
    val call5_in = Flipped(Decoupled(new Call(List(32))))
    val CacheResp = Flipped(Valid(new CacheRespT))
    val CacheReq = Decoupled(new CacheReq)
    val out = Decoupled(new Call(List(32)))
  })
}




  /* ================================================================== *
   *                   PRINTING MODULE DEFINITION                       *
   * ================================================================== */


class test14DF(implicit p: Parameters) extends test14DFIO()(p) {



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
   *                   SUB MODULES                                      *
   * ================================================================== */

  val sub_module_1 = Module(new test14_innerDF())


  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */


  val loop_L_0_liveIN_0 = Module(new LiveInNode(NumOuts = 1, ID = 0))
  val loop_L_0_liveIN_1 = Module(new LiveInNode(NumOuts = 1, ID = 0))

  val loop_L_0_LiveOut_0 = Module(new LiveOutNode(NumOuts = 1, ID = 0))



  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */


  //Initializing BasicBlocks: 

  val bb_entry = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 0))

  val bb_for_cond = Module(new LoopHead(NumOuts = 4, NumPhi = 2, BID = 1))

  val bb_for_body = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 3, BID = 2))

  val bb_for_inc = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 2, BID = 3))

  val bb_for_end = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 4, BID = 4))






  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */


  //Initializing Instructions: 

  // [BasicBlock]  entry:

  //  br label %for.cond, !UID !8, !BB_UID !9, !ScalaLabel !10
  val br0 = Module (new UBranchNode(ID = 0))

  // [BasicBlock]  for.cond:

  //  %a.0 = phi i32 [ 0, %entry ], [ %add, %for.inc ], !UID !11, !ScalaLabel !12
  val phi1 = Module (new PhiNode(NumInputs = 2, NumOuts = 2, ID = 1))


  //  %i.0 = phi i32 [ 0, %entry ], [ %inc, %for.inc ], !UID !13, !ScalaLabel !14
  val phi2 = Module (new PhiNode(NumInputs = 2, NumOuts = 2, ID = 2))


  //  %cmp = icmp slt i32 %i.0, %m, !UID !15, !ScalaLabel !16
  val icmp3 = Module (new IcmpNode(NumOuts = 1, ID = 3, opCode = "ULT")(sign=false))


  //  br i1 %cmp, label %for.body, label %for.end, !UID !17, !BB_UID !18, !ScalaLabel !19
  val br4 = Module (new CBranchNode(ID = 4))

  // [BasicBlock]  for.body:

  //  %call = call i32 @test14_inner(i32 %n), !UID !20, !ScalaLabel !21
  val call5 = Module(new CallNode(ID=5,argTypes=List(32),retTypes=List(32)))


  //  %add = add nsw i32 %a.0, %call, !UID !22, !ScalaLabel !23
  val add6 = Module (new ComputeNode(NumOuts = 1, ID = 6, opCode = "add")(sign=false))


  //  br label %for.inc, !UID !24, !BB_UID !25, !ScalaLabel !26
  val br7 = Module (new UBranchNode(ID = 7))

  // [BasicBlock]  for.inc:

  //  %inc = add nsw i32 %i.0, 1, !UID !27, !ScalaLabel !28
  val add8 = Module (new ComputeNode(NumOuts = 1, ID = 8, opCode = "add")(sign=false))


  //  br label %for.cond, !llvm.loop !29, !UID !37, !BB_UID !38, !ScalaLabel !39
  val br9 = Module (new UBranchNode(ID = 9))

  // [BasicBlock]  for.end:

  //  ret i32 %a.0, !UID !40, !BB_UID !41, !ScalaLabel !42
  val ret10 = Module(new RetNode(NumPredIn=1, retTypes=List(32), ID=10))





  /* ================================================================== *
   *                   INITIALIZING PARAM                               *
   * ================================================================== */


  /**
    * Instantiating parameters
    */
  val param = Data_test14_FlowParam



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

  //Connecting br0 to bb_for_cond
  bb_for_cond.io.activate <> br0.io.Out(param.br0_brn_bb("bb_for_cond"))


  //Connecting br4 to bb_for_body
  bb_for_body.io.predicateIn <> br4.io.Out(param.br4_brn_bb("bb_for_body"))


  //Connecting br4 to bb_for_end
  bb_for_end.io.predicateIn <> br4.io.Out(param.br4_brn_bb("bb_for_end"))


  //Connecting br7 to bb_for_inc
  bb_for_inc.io.predicateIn <> br7.io.Out(param.br7_brn_bb("bb_for_inc"))


  //Connecting br9 to bb_for_cond
  bb_for_cond.io.loopBack <> br9.io.Out(param.br9_brn_bb("bb_for_cond"))


  // There is no detach instruction




  /* ================================================================== *
   *                   CONNECTING BASIC BLOCKS TO INSTRUCTIONS          *
   * ================================================================== */


  /**
    * Wiring enable signals to the instructions
    */

  br0.io.enable <> bb_entry.io.Out(param.bb_entry_activate("br0"))



  phi1.io.enable <> bb_for_cond.io.Out(param.bb_for_cond_activate("phi1"))

  phi2.io.enable <> bb_for_cond.io.Out(param.bb_for_cond_activate("phi2"))

  icmp3.io.enable <> bb_for_cond.io.Out(param.bb_for_cond_activate("icmp3"))

  br4.io.enable <> bb_for_cond.io.Out(param.bb_for_cond_activate("br4"))



  call5.io.In.enable <> bb_for_body.io.Out(param.bb_for_body_activate("call5"))

  add6.io.enable <> bb_for_body.io.Out(param.bb_for_body_activate("add6"))

  br7.io.enable <> bb_for_body.io.Out(param.bb_for_body_activate("br7"))



  add8.io.enable <> bb_for_inc.io.Out(param.bb_for_inc_activate("add8"))

  br9.io.enable <> bb_for_inc.io.Out(param.bb_for_inc_activate("br9"))



  ret10.io.enable <> bb_for_end.io.Out(param.bb_for_end_activate("ret10"))

  loop_L_0_liveIN_0.io.enable <> bb_for_end.io.Out(1)
  loop_L_0_liveIN_1.io.enable <> bb_for_end.io.Out(2)

  loop_L_0_LiveOut_0.io.enable <> bb_for_end.io.Out(3)





  /* ================================================================== *
   *                   CONNECTING LOOPHEADERS                           *
   * ================================================================== */


  // Connecting function argument to the loop header
  //i32 %m
  loop_L_0_liveIN_0.io.InData <> InputSplitter.io.Out.data("field0")

  // Connecting function argument to the loop header
  //i32 %n
  loop_L_0_liveIN_1.io.InData <> InputSplitter.io.Out.data("field1")



  /* ================================================================== *
   *                   DUMPING PHI NODES                                *
   * ================================================================== */


  /**
    * Connecting PHI Masks
    */
  //Connect PHI node

  phi1.io.InData(param.phi1_phi_in("const_0")).bits.data := 0.U
  phi1.io.InData(param.phi1_phi_in("const_0")).bits.predicate := true.B
  phi1.io.InData(param.phi1_phi_in("const_0")).valid := true.B

  phi1.io.InData(param.phi1_phi_in("add6")) <> add6.io.Out(param.phi1_in("add6"))

  phi2.io.InData(param.phi2_phi_in("const_0")).bits.data := 0.U
  phi2.io.InData(param.phi2_phi_in("const_0")).bits.predicate := true.B
  phi2.io.InData(param.phi2_phi_in("const_0")).valid := true.B

  phi2.io.InData(param.phi2_phi_in("add8")) <> add8.io.Out(param.phi2_in("add8"))

  /**
    * Connecting PHI Masks
    */
  //Connect PHI node

  phi1.io.Mask <> bb_for_cond.io.MaskBB(0)

  phi2.io.Mask <> bb_for_cond.io.MaskBB(1)



  /* ================================================================== *
   *                   DUMPING DATAFLOW                                 *
   * ================================================================== */


  /**
    * Connecting Dataflow signals
    */

  // Wiring instructions
  icmp3.io.LeftIO <> phi2.io.Out(param.icmp3_in("phi2"))

  // Wiring Binary instruction to the loop header
  icmp3.io.RightIO <> loop_L_0_liveIN_0.io.Out(param.icmp3_in("field0"))

  // Wiring Branch instruction
  br4.io.CmpIO <> icmp3.io.Out(param.br4_in("icmp3"))

  // Wiring Call to I/O
  io.call5_out <> call5.io.callOut
  call5.io.retIn <> io.call5_in
  call5.io.Out.enable.ready := true.B
  // Wiring Call instruction to the loop header
  call5.io.In.data("field0") <>loop_L_0_liveIN_1.io.Out(param.call5_in("field1")) // manually fixed


  // Wiring instructions
  add6.io.LeftIO <> phi1.io.Out(param.add6_in("phi1"))

  // Wiring instructions
  add6.io.RightIO <> call5.io.Out.data("field0")

  // Wiring instructions
  add8.io.LeftIO <> phi2.io.Out(param.add8_in("phi2"))

  // Wiring constant
  add8.io.RightIO.bits.data := 1.U
  add8.io.RightIO.bits.predicate := true.B
  add8.io.RightIO.valid := true.B

  ret10.io.predicateIn(0).bits.control := true.B
  ret10.io.predicateIn(0).bits.taskID := 0.U
  ret10.io.predicateIn(0).valid := true.B

  loop_L_0_LiveOut_0.io.InData <>   phi1.io.Out(param.ret10_in("phi1"))
  ret10.io.In.data("field0") <> loop_L_0_LiveOut_0.io.Out(0)
  io.out <> ret10.io.Out

  /* ================================================================== *
    *                   CONNECTING SUBMODULES                            *
    * ================================================================== */
  sub_module_1.io.in <> call5.io.callOut
  call5.io.retIn <> sub_module_1.io.out

}

import java.io.{File, FileWriter}
object test14Main extends App {
  val dir = new File("RTL/test14") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new test14DF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}

