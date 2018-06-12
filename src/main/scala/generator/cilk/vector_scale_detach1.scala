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

object Data_vector_scale_detach1_FlowParam{

  val bb_my_pfor_body_pred = Map(
    "active" -> 0
  )


  val bb_my_if_else_pred = Map(
    "br3" -> 0
  )


  val bb_my_pfor_preattach_sink_split_pred = Map(
    "br5" -> 0,
    "br16" -> 1
  )


  val bb_my_if_then_pred = Map(
    "br3" -> 0
  )


  val bb_my_pfor_preattach_pred = Map(
    "br9" -> 0,
    "br16" -> 1
  )


  val br3_brn_bb = Map(
    "bb_my_if_then" -> 0,
    "bb_my_if_else" -> 1
  )


  val br5_brn_bb = Map(
    "bb_my_pfor_preattach_sink_split" -> 0
  )


  val br9_brn_bb = Map(
    "bb_my_pfor_preattach" -> 0
  )


  val br16_brn_bb = Map(
    "bb_my_pfor_preattach_sink_split" -> 0,
    "bb_my_pfor_preattach" -> 1
  )


  val bb_my_pfor_body_activate = Map(
    "getelementptr0" -> 0,
    "load1" -> 1,
    "icmp2" -> 2,
    "br3" -> 3
  )


  val bb_my_if_then_activate = Map(
    "getelementptr4" -> 0,
    "br5" -> 1
  )


  val bb_my_pfor_preattach_sink_split_activate = Map(
    "phi6" -> 0,
    "phi7" -> 1,
    "store8" -> 2,
    "br9" -> 3
  )


  val bb_my_pfor_preattach_activate = Map(
    "ret10" -> 0
  )


  val bb_my_if_else_activate = Map(
    "mul11" -> 0,
    "ashr12" -> 1,
    "getelementptr13" -> 2,
    "store14" -> 3,
    "icmp15" -> 4,
    "br16" -> 5
  )


  val phi6_phi_in = Map(
    "getelementptr4" -> 0,
    "getelementptr13" -> 1
  )


  val phi7_phi_in = Map(
    "const_0" -> 0,
    "const_1" -> 1
  )


  //  %0 = getelementptr inbounds i32, i32* %a.in, i32 %i.024.in, !UID !9, !ScalaLabel !10
  val getelementptr0_in = Map(
    "field0" -> 0,
    "field1" -> 0
  )


  //  %1 = load i32, i32* %0, align 4, !tbaa !11, !UID !15, !ScalaLabel !16
  val load1_in = Map(
    "getelementptr0" -> 0
  )


  //  %2 = icmp slt i32 %1, 0, !UID !17, !ScalaLabel !18
  val icmp2_in = Map(
    "load1" -> 0
  )


  //  br i1 %2, label %my_if.then, label %my_if.else, !UID !19, !BB_UID !20, !ScalaLabel !21
  val br3_in = Map(
    "icmp2" -> 0
  )


  //  %3 = getelementptr inbounds i32, i32* %c.in, i32 %i.024.in, !UID !22, !ScalaLabel !23
  val getelementptr4_in = Map(
    "field2" -> 0,
    "field1" -> 1
  )


  //  %4 = phi i32* [ %3, %my_if.then ], [ %8, %my_if.else ], !UID !27, !ScalaLabel !28
  val phi6_in = Map(
    "getelementptr4" -> 0,
    "getelementptr13" -> 0
  )


  //  %5 = phi i32 [ 0, %my_if.then ], [ 255, %my_if.else ], !UID !29, !ScalaLabel !30
  val phi7_in = Map(

  )


  //  store i32 %5, i32* %4, align 4, !tbaa !11, !UID !31, !ScalaLabel !32
  val store8_in = Map(
    "phi7" -> 0,
    "phi6" -> 0
  )


  //  ret void, !UID !36, !BB_UID !37, !ScalaLabel !38
  val ret10_in = Map(

  )


  //  %6 = mul nsw i32 %1, %scale.in, !UID !39, !ScalaLabel !40
  val mul11_in = Map(
    "load1" -> 1,
    "field3" -> 0
  )


  //  %7 = ashr i32 %6, 8, !UID !41, !ScalaLabel !42
  val ashr12_in = Map(
    "mul11" -> 0
  )


  //  %8 = getelementptr inbounds i32, i32* %c.in, i32 %i.024.in, !UID !43, !ScalaLabel !44
  val getelementptr13_in = Map(
    "field2" -> 1,
    "field1" -> 2
  )


  //  store i32 %7, i32* %8, align 4, !tbaa !11, !UID !45, !ScalaLabel !46
  val store14_in = Map(
    "ashr12" -> 0,
    "getelementptr13" -> 1
  )


  //  %9 = icmp sgt i32 %7, 255, !UID !47, !ScalaLabel !48
  val icmp15_in = Map(
    "ashr12" -> 1
  )


  //  br i1 %9, label %my_pfor.preattach.sink.split, label %my_pfor.preattach, !UID !49, !BB_UID !50, !ScalaLabel !51
  val br16_in = Map(
    "icmp15" -> 0
  )


}




  /* ================================================================== *
   *                   PRINTING PORTS DEFINITION                        *
   * ================================================================== */


abstract class vector_scale_detach1DFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32,32,32,32))))
    val CacheResp = Flipped(Valid(new CacheRespT))
    val CacheReq = Decoupled(new CacheReq)
    val out = Decoupled(new Call(List(32)))
  })
}




  /* ================================================================== *
   *                   PRINTING MODULE DEFINITION                       *
   * ================================================================== */


class vector_scale_detach1DF(implicit p: Parameters) extends vector_scale_detach1DFIO()(p) {



  /* ================================================================== *
   *                   PRINTING MEMORY SYSTEM                           *
   * ================================================================== */

/*
	val StackPointer = Module(new Stack(NumOps = 1))

	val RegisterFile = Module(new TypeStackFile(ID=0,Size=32,NReads=1,NWrites=2)
		            (WControl=new WriteMemoryController(NumOps=2,BaseSize=2,NumEntries=2))
		            (RControl=new ReadMemoryController(NumOps=1,BaseSize=2,NumEntries=2)))
*/
	val CacheMem = Module(new UnifiedController(ID=0,Size=32,NReads=1,NWrites=2)
		            (WControl=new WriteMemoryController(NumOps=2,BaseSize=2,NumEntries=2))
		            (RControl=new ReadMemoryController(NumOps=1,BaseSize=2,NumEntries=2))
		            (RWArbiter=new ReadWriteArbiter()))

  io.CacheReq <> CacheMem.io.CacheReq
  CacheMem.io.CacheResp <> io.CacheResp

  val InputSplitter = Module(new SplitCall(List(32,32,32,32)))
  InputSplitter.io.In <> io.in

  // Manually added

  val field1_expand = Module(new ExpandNode(NumOuts=3,ID=102)(new DataBundle))
  field1_expand.io.enable.valid := true.B
  field1_expand.io.enable.bits.control := true.B
  field1_expand.io.InData <> InputSplitter.io.Out.data("field1")

  val field2_expand = Module(new ExpandNode(NumOuts=2,ID=103)(new DataBundle))
  field2_expand.io.enable.valid := true.B
  field2_expand.io.enable.bits.control := true.B
  field2_expand.io.InData <> InputSplitter.io.Out.data("field2")


  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */


  //Function doesn't have any loop


  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */


  //Initializing BasicBlocks: 

  val bb_my_pfor_body = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 4, BID = 0))

  val bb_my_if_then = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 2, BID = 1))

  val bb_my_pfor_preattach_sink_split = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 4, NumPhi = 2, BID = 2))

  val bb_my_pfor_preattach = Module(new BasicBlockNode(NumInputs = 2, NumOuts = 1, NumPhi = 1, BID = 3))

  val bb_my_if_else = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 6, BID = 4))






  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */


  //Initializing Instructions: 

  // [BasicBlock]  my_pfor.body:

  //  %0 = getelementptr inbounds i32, i32* %a.in, i32 %i.024.in, !UID !9, !ScalaLabel !10
  val getelementptr0 = Module (new GepOneNode(NumOuts = 1, ID = 0)(numByte1 = 4))


  //  %1 = load i32, i32* %0, align 4, !tbaa !11, !UID !15, !ScalaLabel !16
  val load1 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=2,ID=1,RouteID=0))


  //  %2 = icmp slt i32 %1, 0, !UID !17, !ScalaLabel !18
  val icmp2 = Module (new IcmpNode(NumOuts = 1, ID = 2, opCode = "ULT")(sign=false))


  //  br i1 %2, label %my_if.then, label %my_if.else, !UID !19, !BB_UID !20, !ScalaLabel !21
  val br3 = Module (new CBranchNode(ID = 3))

  // [BasicBlock]  my_if.then:

  //  %3 = getelementptr inbounds i32, i32* %c.in, i32 %i.024.in, !UID !22, !ScalaLabel !23
  val getelementptr4 = Module (new GepOneNode(NumOuts = 1, ID = 4)(numByte1 = 4))


  //  br label %my_pfor.preattach.sink.split, !UID !24, !BB_UID !25, !ScalaLabel !26
  val br5 = Module (new UBranchNode(ID = 5))

  // [BasicBlock]  my_pfor.preattach.sink.split:

  //  %4 = phi i32* [ %3, %my_if.then ], [ %8, %my_if.else ], !UID !27, !ScalaLabel !28
  val phi6 = Module (new PhiNode(NumInputs = 2, NumOuts = 1, ID = 6))


  //  %5 = phi i32 [ 0, %my_if.then ], [ 255, %my_if.else ], !UID !29, !ScalaLabel !30
  val phi7 = Module (new PhiNode(NumInputs = 2, NumOuts = 1, ID = 7))


  //  store i32 %5, i32* %4, align 4, !tbaa !11, !UID !31, !ScalaLabel !32
  val store8 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=8,RouteID=0))


  //  br label %my_pfor.preattach, !UID !33, !BB_UID !34, !ScalaLabel !35
  val br9 = Module (new UBranchNode(ID = 9))

  // [BasicBlock]  my_pfor.preattach:

  //  ret void, !UID !36, !BB_UID !37, !ScalaLabel !38
  val ret10 = Module(new RetNode(NumPredIn=1, retTypes=List(32), ID=10))

  // [BasicBlock]  my_if.else:

  //  %6 = mul nsw i32 %1, %scale.in, !UID !39, !ScalaLabel !40
  val mul11 = Module (new ComputeNode(NumOuts = 1, ID = 11, opCode = "mul")(sign=false))


  //  %7 = ashr i32 %6, 8, !UID !41, !ScalaLabel !42
  val ashr12 = Module (new ComputeNode(NumOuts = 2, ID = 12, opCode = "ashr")(sign=false))


  //  %8 = getelementptr inbounds i32, i32* %c.in, i32 %i.024.in, !UID !43, !ScalaLabel !44
  val getelementptr13 = Module (new GepOneNode(NumOuts = 2, ID = 13)(numByte1 = 4))


  //  store i32 %7, i32* %8, align 4, !tbaa !11, !UID !45, !ScalaLabel !46
  val store14 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=14,RouteID=1))


  //  %9 = icmp sgt i32 %7, 255, !UID !47, !ScalaLabel !48
  val icmp15 = Module (new IcmpNode(NumOuts = 1, ID = 15, opCode = "UGT")(sign=false))


  //  br i1 %9, label %my_pfor.preattach.sink.split, label %my_pfor.preattach, !UID !49, !BB_UID !50, !ScalaLabel !51
  val br16 = Module (new CBranchNode(ID = 16))





  /* ================================================================== *
   *                   INITIALIZING PARAM                               *
   * ================================================================== */


  /**
    * Instantiating parameters
    */
  val param = Data_vector_scale_detach1_FlowParam



  /* ================================================================== *
   *                   CONNECTING BASIC BLOCKS TO PREDICATE INSTRUCTIONS*
   * ================================================================== */


  /**
     * Connecting basic blocks to predicate instructions
     */


  bb_my_pfor_body.io.predicateIn <> InputSplitter.io.Out.enable

  /**
    * Connecting basic blocks to predicate instructions
    */

  //Connecting br3 to bb_my_if_then
  bb_my_if_then.io.predicateIn <> br3.io.Out(param.br3_brn_bb("bb_my_if_then"))


  //Connecting br3 to bb_my_if_else
  bb_my_if_else.io.predicateIn <> br3.io.Out(param.br3_brn_bb("bb_my_if_else"))


  //Connecting br5 to bb_my_pfor_preattach_sink_split
  bb_my_pfor_preattach_sink_split.io.predicateIn(param.bb_my_pfor_preattach_sink_split_pred("br5")) <> br5.io.Out(param.br5_brn_bb("bb_my_pfor_preattach_sink_split"))


  //Connecting br9 to bb_my_pfor_preattach
  bb_my_pfor_preattach.io.predicateIn(0) <> br9.io.Out(param.br9_brn_bb("bb_my_pfor_preattach"))


  //Connecting br16 to bb_my_pfor_preattach_sink_split
  bb_my_pfor_preattach_sink_split.io.predicateIn(param.bb_my_pfor_preattach_sink_split_pred("br16")) <> br16.io.Out(param.br16_brn_bb("bb_my_pfor_preattach_sink_split"))


  //Connecting br16 to bb_my_pfor_preattach
  bb_my_pfor_preattach.io.predicateIn(1) <> br16.io.Out(param.br16_brn_bb("bb_my_pfor_preattach"))
  bb_my_pfor_preattach.io.MaskBB(0).ready := true.B


  // There is no detach instruction




  /* ================================================================== *
   *                   CONNECTING BASIC BLOCKS TO INSTRUCTIONS          *
   * ================================================================== */


  /**
    * Wiring enable signals to the instructions
    */

  getelementptr0.io.enable <> bb_my_pfor_body.io.Out(param.bb_my_pfor_body_activate("getelementptr0"))

  load1.io.enable <> bb_my_pfor_body.io.Out(param.bb_my_pfor_body_activate("load1"))

  icmp2.io.enable <> bb_my_pfor_body.io.Out(param.bb_my_pfor_body_activate("icmp2"))

  br3.io.enable <> bb_my_pfor_body.io.Out(param.bb_my_pfor_body_activate("br3"))



  getelementptr4.io.enable <> bb_my_if_then.io.Out(param.bb_my_if_then_activate("getelementptr4"))

  br5.io.enable <> bb_my_if_then.io.Out(param.bb_my_if_then_activate("br5"))



  phi6.io.enable <> bb_my_pfor_preattach_sink_split.io.Out(param.bb_my_pfor_preattach_sink_split_activate("phi6"))

  phi7.io.enable <> bb_my_pfor_preattach_sink_split.io.Out(param.bb_my_pfor_preattach_sink_split_activate("phi7"))

  store8.io.enable <> bb_my_pfor_preattach_sink_split.io.Out(param.bb_my_pfor_preattach_sink_split_activate("store8"))

  br9.io.enable <> bb_my_pfor_preattach_sink_split.io.Out(param.bb_my_pfor_preattach_sink_split_activate("br9"))



  ret10.io.enable <> bb_my_pfor_preattach.io.Out(param.bb_my_pfor_preattach_activate("ret10"))



  mul11.io.enable <> bb_my_if_else.io.Out(param.bb_my_if_else_activate("mul11"))

  ashr12.io.enable <> bb_my_if_else.io.Out(param.bb_my_if_else_activate("ashr12"))

  getelementptr13.io.enable <> bb_my_if_else.io.Out(param.bb_my_if_else_activate("getelementptr13"))

  store14.io.enable <> bb_my_if_else.io.Out(param.bb_my_if_else_activate("store14"))

  icmp15.io.enable <> bb_my_if_else.io.Out(param.bb_my_if_else_activate("icmp15"))

  br16.io.enable <> bb_my_if_else.io.Out(param.bb_my_if_else_activate("br16"))





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

  phi6.io.InData(param.phi6_phi_in("getelementptr13")) <> getelementptr4.io.Out(param.phi6_in("getelementptr4"))

  phi6.io.InData(param.phi6_phi_in("getelementptr4")) <> getelementptr13.io.Out(param.phi6_in("getelementptr13"))

  phi7.io.InData(param.phi7_phi_in("const_0")).bits.data := 0.U
  phi7.io.InData(param.phi7_phi_in("const_0")).bits.predicate := true.B
  phi7.io.InData(param.phi7_phi_in("const_0")).valid := true.B

  phi7.io.InData(param.phi7_phi_in("const_1")).bits.data := 255.U
  phi7.io.InData(param.phi7_phi_in("const_1")).bits.predicate := true.B
  phi7.io.InData(param.phi7_phi_in("const_1")).valid := true.B

  /**
    * Connecting PHI Masks
    */
  //Connect PHI node

  phi6.io.Mask <> bb_my_pfor_preattach_sink_split.io.MaskBB(0)

  phi7.io.Mask <> bb_my_pfor_preattach_sink_split.io.MaskBB(1)



  /* ================================================================== *
   *                   DUMPING DATAFLOW                                 *
   * ================================================================== */


  /**
    * Connecting Dataflow signals
    */

  // Wiring GEP instruction to the function argument
  getelementptr0.io.baseAddress <> InputSplitter.io.Out.data("field0")


  // Wiring GEP instruction to the function argument
  getelementptr0.io.idx1 <> field1_expand.io.Out(0)

  // Wiring Load instruction to the parent instruction
  load1.io.GepAddr <> getelementptr0.io.Out(param.load1_in("getelementptr0"))
  load1.io.memResp <> CacheMem.io.ReadOut(0)
  CacheMem.io.ReadIn(0) <> load1.io.memReq




  // Wiring instructions
  icmp2.io.LeftIO <> load1.io.Out(param.icmp2_in("load1"))

  // Wiring constant
  icmp2.io.RightIO.bits.data := 0.U
  icmp2.io.RightIO.bits.predicate := true.B
  icmp2.io.RightIO.valid := true.B

  // Wiring Branch instruction
  br3.io.CmpIO <> icmp2.io.Out(param.br3_in("icmp2"))

  // Wiring GEP instruction to the function argument
  getelementptr4.io.baseAddress <> field2_expand.io.Out(0)


  // Wiring GEP instruction to the function argument
  getelementptr4.io.idx1 <> field1_expand.io.Out(1)


  store8.io.inData <> phi7.io.Out(param.store8_in("phi7"))



  // Wiring Store instruction to the parent instruction
  store8.io.GepAddr <> phi6.io.Out(param.store8_in("phi6"))
  store8.io.memResp  <> CacheMem.io.WriteOut(0)
  CacheMem.io.WriteIn(0) <> store8.io.memReq
  store8.io.Out(0).ready := true.B



  /**
    * Connecting Dataflow signals
    */
  ret10.io.predicateIn(0).bits.control := true.B
  ret10.io.predicateIn(0).bits.taskID := 0.U
  ret10.io.predicateIn(0).valid := true.B
  //ret10.io.In.data("field0") <> store8.io.Out(0)

  ret10.io.In.data("field0").bits.data := 1.U
  ret10.io.In.data("field0").bits.predicate := true.B
  ret10.io.In.data("field0").valid := true.B

  io.out <> ret10.io.Out


  // Wiring instructions
  mul11.io.LeftIO <> load1.io.Out(param.mul11_in("load1"))

  // Wiring Binary instruction to the function argument
  mul11.io.RightIO <> InputSplitter.io.Out.data("field3")

  // Wiring instructions
  ashr12.io.LeftIO <> mul11.io.Out(param.ashr12_in("mul11"))

  // Wiring constant
  ashr12.io.RightIO.bits.data := 8.U
  ashr12.io.RightIO.bits.predicate := true.B
  ashr12.io.RightIO.valid := true.B

  // Wiring GEP instruction to the function argument
  getelementptr13.io.baseAddress <> field2_expand.io.Out(1)


  // Wiring GEP instruction to the function argument
  getelementptr13.io.idx1 <> field1_expand.io.Out(2)


  store14.io.inData <> ashr12.io.Out(param.store14_in("ashr12"))



  // Wiring Store instruction to the parent instruction
  store14.io.GepAddr <> getelementptr13.io.Out(param.store14_in("getelementptr13"))
  store14.io.memResp  <> CacheMem.io.WriteOut(1)
  CacheMem.io.WriteIn(1) <> store14.io.memReq
  store14.io.Out(0).ready := true.B



  // Wiring instructions
  icmp15.io.LeftIO <> ashr12.io.Out(param.icmp15_in("ashr12"))

  // Wiring constant
  icmp15.io.RightIO.bits.data := 255.U
  icmp15.io.RightIO.bits.predicate := true.B
  icmp15.io.RightIO.valid := true.B

  // Wiring Branch instruction
  br16.io.CmpIO <> icmp15.io.Out(param.br16_in("icmp15"))

}

import java.io.{File, FileWriter}
object vector_scale_detach1Main extends App {
  val dir = new File("RTL/vector_scale_detach1") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new vector_scale_detach1DF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}

