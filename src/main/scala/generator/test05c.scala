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

object Data_test05c_FlowParam{

  val bb_entry_pred = Map(
    "active" -> 0
  )


  val bb_for_body_pred = Map(
    "br4" -> 0
  )


  val bb_for_end_pred = Map(
    "br4" -> 0
  )


  val bb_for_cond_pred = Map(
    "br0" -> 0,
    "br9" -> 1
  )


  val bb_for_inc_pred = Map(
    "br7" -> 0
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
    "sdiv10" -> 0,
    "ret11" -> 1
  )


  val phi1_phi_in = Map(
    "const_0" -> 0,
    "add6" -> 1
  )


  val phi2_phi_in = Map(
    "const_0" -> 0,
    "add8" -> 1
  )


  //  %j.0 = phi i32 [ 0, %entry ], [ %add, %for.inc ], !UID !5, !ScalaLabel !6
  val phi1_in = Map(
    "add6" -> 0
  )


  //  %i.0 = phi i32 [ 0, %entry ], [ %inc, %for.inc ], !UID !7, !ScalaLabel !8
  val phi2_in = Map(
    "add8" -> 0
  )


  //  %cmp = icmp ult i32 %i.0, 3, !UID !9, !ScalaLabel !10
  val icmp3_in = Map(
    "phi2" -> 0
  )


  //  br i1 %cmp, label %for.body, label %for.end, !UID !11, !BB_UID !12, !ScalaLabel !13
  val br4_in = Map(
    "icmp3" -> 0
  )


  //  %call = call i32 @test05b(i32* %a, i32 %n), !UID !14, !ScalaLabel !15
  val call5_in = Map(
    "field0" -> 0,
    "field1" -> 0,
    "" -> 0
  )


  //  %add = add i32 %j.0, %call, !UID !16, !ScalaLabel !17
  val add6_in = Map(
    "phi1" -> 0,
    "call5" -> 0
  )


  //  %inc = add i32 %i.0, 1, !UID !21, !ScalaLabel !22
  val add8_in = Map(
    "phi2" -> 1
  )


  //  %div = sdiv i32 %j.0, 2, !UID !26, !ScalaLabel !27
  val sdiv10_in = Map(
    "phi1" -> 1
  )


  //  ret i32 %div, !UID !28, !BB_UID !29, !ScalaLabel !30
  val ret11_in = Map(
    "sdiv10" -> 0
  )


}




  /* ================================================================== *
   *                   PRINTING PORTS DEFINITION                        *
   * ================================================================== */


abstract class test05cDFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32,32))))
    val call5_out = Decoupled(new Call(List(32,32)))
    val call5_in = Flipped(Decoupled(new Call(List(32))))
    val MemResp = Flipped(Valid(new MemResp))
    val MemReq = Decoupled(new MemReq)
    val out = Decoupled(new Call(List(32)))
  })
}




  /* ================================================================== *
   *                   PRINTING MODULE DEFINITION                       *
   * ================================================================== */


class test05cDF(implicit p: Parameters) extends test05cDFIO()(p) {



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

  io.MemReq <> CacheMem.io.MemReq
  CacheMem.io.MemResp <> io.MemResp

  val InputSplitter = Module(new SplitCallNew(List(1,1)))
  InputSplitter.io.In <> io.in


  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */

  val lb_L_0 = Module(new LoopBlock(ID=999,NumIns=List(1,1),NumOuts=1,NumExits=1));
/*
  val loop_L_0_liveIN_0 = Module(new LiveInNode(NumOuts = 1, ID = 0))
  val loop_L_0_liveIN_1 = Module(new LiveInNode(NumOuts = 1, ID = 0))

  val loop_L_0_LiveOut_0 = Module(new LiveOutNode(NumOuts = 1, ID = 0))
*/


  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */


  //Initializing BasicBlocks: 

  val bb_entry = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 0))

  val bb_for_cond = Module(new LoopHead(NumOuts = 4, NumPhi = 2, BID = 1)) // Manual

  val bb_for_body = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 4, BID = 2))

  val bb_for_inc = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 2, BID = 3))

  val bb_for_end = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 5, BID = 4))






  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */


  //Initializing Instructions: 

  // [BasicBlock]  entry:

  //  br label %for.cond, !UID !2, !BB_UID !3, !ScalaLabel !4
  val br0 = Module (new UBranchNode(ID = 0))

  // [BasicBlock]  for.cond:

  //  %j.0 = phi i32 [ 0, %entry ], [ %add, %for.inc ], !UID !5, !ScalaLabel !6
  val phi1 = Module (new PhiNode(NumInputs = 2, NumOuts = 2, ID = 1))


  //  %i.0 = phi i32 [ 0, %entry ], [ %inc, %for.inc ], !UID !7, !ScalaLabel !8
  val phi2 = Module (new PhiNode(NumInputs = 2, NumOuts = 2, ID = 2))


  //  %cmp = icmp ult i32 %i.0, 3, !UID !9, !ScalaLabel !10
  val icmp3 = Module (new IcmpNode(NumOuts = 1, ID = 3, opCode = "ULT")(sign=false))


  //  br i1 %cmp, label %for.body, label %for.end, !UID !11, !BB_UID !12, !ScalaLabel !13
  val br4 = Module (new CBranchNode(ID = 4))

  // [BasicBlock]  for.body:

  //  %call = call i32 @test05b(i32* %a, i32 %n), !UID !14, !ScalaLabel !15
//  val call5 = Module(new CallNode(ID=5,argTypes=List(32,32),retTypes=List(32)))
  val callout5 = Module(new CallOutNode(ID=4,argTypes=List(32,32))) // Manually changed
  val callin5 = Module(new CallInNode(ID=499,argTypes=List(32)))


  //  %add = add i32 %j.0, %call, !UID !16, !ScalaLabel !17
  val add6 = Module (new ComputeNode(NumOuts = 1, ID = 6, opCode = "add")(sign=false))


  //  br label %for.inc, !UID !18, !BB_UID !19, !ScalaLabel !20
  val br7 = Module (new UBranchNode(ID = 7, NumPredOps=1))

  // [BasicBlock]  for.inc:

  //  %inc = add i32 %i.0, 1, !UID !21, !ScalaLabel !22
  val add8 = Module (new ComputeNode(NumOuts = 1, ID = 8, opCode = "add")(sign=false))


  //  br label %for.cond, !UID !23, !BB_UID !24, !ScalaLabel !25
  val br9 = Module (new UBranchNode(ID = 9, NumOuts=2)) // manually changed to 2 outs

  // [BasicBlock]  for.end:

  //  %div = sdiv i32 %j.0, 2, !UID !26, !ScalaLabel !27
  val sdiv10 = Module (new ComputeNode(NumOuts = 1, ID = 10, opCode = "udiv")(sign=false))


  //  ret i32 %div, !UID !28, !BB_UID !29, !ScalaLabel !30
  val ret11 = Module(new RetNode(retTypes=List(32), ID=11))





  /* ================================================================== *
   *                   INITIALIZING PARAM                               *
   * ================================================================== */


  /**
    * Instantiating parameters
    */
  val param = Data_test05c_FlowParam



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
//  bb_for_cond.io.activate <> br0.io.Out(param.br0_brn_bb("bb_for_cond"))
  lb_L_0.io.enable <> br0.io.Out(param.br0_brn_bb("bb_for_cond"))  // manually added

  bb_for_cond.io.activate <> lb_L_0.io.activate // manually corrected


  //Connecting br4 to bb_for_body
  bb_for_body.io.predicateIn <> br4.io.Out(param.br4_brn_bb("bb_for_body"))


  //Connecting br4 to bb_for_end
//  bb_for_end.io.predicateIn <> br4.io.Out(param.br4_brn_bb("bb_for_end"))
  lb_L_0.io.loopExit(0) <> br4.io.Out(param.br4_brn_bb("bb_for_end")) // Manual
  bb_for_end.io.predicateIn <> lb_L_0.io.endEnable



  //Connecting br7 to bb_for_inc
  bb_for_inc.io.predicateIn <> br7.io.Out(param.br7_brn_bb("bb_for_inc"))


  //Connecting br9 to bb_for_cond
  bb_for_cond.io.loopBack <> br9.io.Out(0)
  lb_L_0.io.latchEnable   <> br9.io.Out(1) // Manual



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



  callout5.io.enable <> bb_for_body.io.Out(param.bb_for_body_activate("call5"))
  callin5.io.enable <> bb_for_body.io.Out(3) // Manual

  add6.io.enable <> bb_for_body.io.Out(param.bb_for_body_activate("add6"))

  br7.io.enable <> bb_for_body.io.Out(param.bb_for_body_activate("br7"))
  br7.io.PredOp(0) <> callin5.io.Out.enable



  add8.io.enable <> bb_for_inc.io.Out(param.bb_for_inc_activate("add8"))

  br9.io.enable <> bb_for_inc.io.Out(param.bb_for_inc_activate("br9"))



  sdiv10.io.enable <> bb_for_end.io.Out(param.bb_for_end_activate("sdiv10"))

  ret11.io.enable <> bb_for_end.io.Out(param.bb_for_end_activate("ret11"))

/*
  loop_L_0_liveIN_0.io.enable <> bb_for_end.io.Out(2)
  loop_L_0_liveIN_1.io.enable <> bb_for_end.io.Out(3)

  loop_L_0_LiveOut_0.io.enable <> bb_for_end.io.Out(4)
*/




  /* ================================================================== *
   *                   CONNECTING LOOPHEADERS                           *
   * ================================================================== */


  // Connecting function argument to the loop header
  //i32* %a
  lb_L_0.io.In(0) <> InputSplitter.io.Out.data.elements("field0")(0) // manual

  // Connecting function argument to the loop header
  //i32 %n
  lb_L_0.io.In(1) <> InputSplitter.io.Out.data.elements("field1")(0) // manual

  lb_L_0.io.liveOut(0) <> phi1.io.Out(param.sdiv10_in("phi1"))


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

  // Wiring constant
  icmp3.io.RightIO.bits.data := 3.U
  icmp3.io.RightIO.bits.predicate := true.B
  icmp3.io.RightIO.valid := true.B

  // Wiring Branch instruction
  br4.io.CmpIO <> icmp3.io.Out(param.br4_in("icmp3"))

  // Wiring Call to I/O

  callout5.io.In("field0") <> lb_L_0.io.liveIn.elements("field0")(0) // manual
  callout5.io.In("field1") <> lb_L_0.io.liveIn.elements("field1")(0) // manual
  io.call5_out <> callout5.io.Out(0)

  callin5.io.In <> io.call5_in

  // Wiring instructions
  add6.io.LeftIO <> phi1.io.Out(param.add6_in("phi1"))

  // Wiring instructions
  add6.io.RightIO <> callin5.io.Out.data("field0")

  // Wiring instructions
  add8.io.LeftIO <> phi2.io.Out(param.add8_in("phi2"))

  // Wiring constant
  add8.io.RightIO.bits.data := 1.U
  add8.io.RightIO.bits.predicate := true.B
  add8.io.RightIO.valid := true.B

  // Wiring instructions
  sdiv10.io.LeftIO <> lb_L_0.io.Out(0)

  // Wiring constant
  sdiv10.io.RightIO.bits.data := 2.U
  sdiv10.io.RightIO.bits.predicate := true.B
  sdiv10.io.RightIO.valid := true.B

  // Wiring return instruction
  
  
  
  ret11.io.In.elements("field0") <> sdiv10.io.Out(param.ret11_in("sdiv10"))
  io.out <> ret11.io.Out


}

import java.io.{File, FileWriter}
object test05cMain extends App {
  val dir = new File("RTL/test05c") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new test05cDF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}

