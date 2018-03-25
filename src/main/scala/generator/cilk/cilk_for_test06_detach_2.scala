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

object Data_cilk_for_test06_detach_2_FlowParam {

  val bb_my_pfor_body5_pred = Map(
    "active" -> 0
  )


  val bb_my_pfor_preattach_pred = Map(
    "br10" -> 0
  )


  val br10_brn_bb = Map(
    "bb_my_pfor_preattach" -> 0
  )


  val bb_my_pfor_body5_activate = Map(
    "getelementptr0" -> 0,
    "getelementptr1" -> 1,
    "load2" -> 2,
    "getelementptr3" -> 3,
    "getelementptr4" -> 4,
    "load5" -> 5,
    "add6" -> 6,
    "getelementptr7" -> 7,
    "getelementptr8" -> 8,
    "store9" -> 9,
    "br10" -> 10
  )


  val bb_my_pfor_preattach_activate = Map(
    "ret11" -> 0
  )


  //  %0 = getelementptr inbounds [5 x i32], [5 x i32]* %a.in, i32 %i.0.in, !UID !7, !ScalaLabel !8
  val getelementptr0_in = Map(
    "field0" -> 0,
    "field1" -> 0
  )


  //  %1 = getelementptr inbounds [5 x i32], [5 x i32]* %0, i32 0, i32 %j.0.in, !UID !9, !ScalaLabel !10
  val getelementptr1_in = Map(
    "getelementptr0" -> 0,
    "field2" -> 0
  )


  //  %2 = load i32, i32* %1, align 4, !UID !11, !ScalaLabel !12
  val load2_in = Map(
    "getelementptr1" -> 0
  )


  //  %3 = getelementptr inbounds [5 x i32], [5 x i32]* %b.in, i32 %i.0.in, !UID !13, !ScalaLabel !14
  val getelementptr3_in = Map(
    "field3" -> 0,
    "field1" -> 1
  )


  //  %4 = getelementptr inbounds [5 x i32], [5 x i32]* %3, i32 0, i32 %j.0.in, !UID !15, !ScalaLabel !16
  val getelementptr4_in = Map(
    "getelementptr3" -> 0,
    "field2" -> 1
  )


  //  %5 = load i32, i32* %4, align 4, !UID !17, !ScalaLabel !18
  val load5_in = Map(
    "getelementptr4" -> 0
  )


  //  %6 = add nsw i32 %2, %5, !UID !19, !ScalaLabel !20
  val add6_in = Map(
    "load2" -> 0,
    "load5" -> 0
  )


  //  %7 = getelementptr inbounds [5 x i32], [5 x i32]* %c.in, i32 %i.0.in, !UID !21, !ScalaLabel !22
  val getelementptr7_in = Map(
    "field4" -> 0,
    "field1" -> 2
  )


  //  %8 = getelementptr inbounds [5 x i32], [5 x i32]* %7, i32 0, i32 %j.0.in, !UID !23, !ScalaLabel !24
  val getelementptr8_in = Map(
    "getelementptr7" -> 0,
    "field2" -> 2
  )


  //  store i32 %6, i32* %8, align 4, !UID !25, !ScalaLabel !26
  val store9_in = Map(
    "add6" -> 0,
    "getelementptr8" -> 0
  )


  //  ret void, !UID !30, !BB_UID !31, !ScalaLabel !32
  val ret11_in = Map(

  )


}


/* ================================================================== *
 *                   PRINTING PORTS DEFINITION                        *
 * ================================================================== */


abstract class cilk_for_test06_detach_2DFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32, 32, 32, 32, 32))))
    val CacheResp = Flipped(Valid(new CacheRespT))
    val CacheReq = Decoupled(new CacheReq)
    val out = Decoupled(new Call(List(32)))
  })
}


/* ================================================================== *
 *                   PRINTING MODULE DEFINITION                       *
 * ================================================================== */


class cilk_for_test06_detach_2DF(implicit p: Parameters) extends cilk_for_test06_detach_2DFIO()(p) {


  /* ================================================================== *
   *                   PRINTING MEMORY SYSTEM                           *
   * ================================================================== */


  val StackPointer = Module(new Stack(NumOps = 1))

  val RegisterFile = Module(new TypeStackFile(ID = 0, Size = 32, NReads = 2, NWrites = 1)
  (WControl = new WriteMemoryController(NumOps = 1, BaseSize = 2, NumEntries = 2))
  (RControl = new ReadMemoryController(NumOps = 2, BaseSize = 2, NumEntries = 2)))

  val CacheMem = Module(new UnifiedController(ID = 0, Size = 32, NReads = 2, NWrites = 1)
  (WControl = new WriteMemoryController(NumOps = 1, BaseSize = 2, NumEntries = 2))
  (RControl = new ReadMemoryController(NumOps = 2, BaseSize = 2, NumEntries = 2))
  (RWArbiter = new ReadWriteArbiter()))

  io.CacheReq <> CacheMem.io.CacheReq
  CacheMem.io.CacheResp <> io.CacheResp

  val InputSplitter = Module(new SplitCall(List(32, 32, 32, 32, 32)))
  InputSplitter.io.In <> io.in

  // Manually added
  val field1_expand = Module(new ExpandNode(NumOuts = 3, ID = 100)(new DataBundle))
  field1_expand.io.enable.valid := true.B
  field1_expand.io.enable.bits.control := true.B
  field1_expand.io.InData <> InputSplitter.io.Out.data("field1")

  // Manually added
  val field2_expand = Module(new ExpandNode(NumOuts = 3, ID = 101)(new DataBundle))
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

  //  val bb_my_pfor_body5 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 11, BID = 0))
  val bb_my_pfor_body5 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 11, BID = 0))

  val bb_my_pfor_preattach = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 1))


  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */


  //Initializing Instructions: 

  // [BasicBlock]  my_pfor.body5:

  //  %0 = getelementptr inbounds [5 x i32], [5 x i32]* %a.in, i32 %i.0.in, !UID !7, !ScalaLabel !8
  val getelementptr0 = Module(new GepOneNode(NumOuts = 1, ID = 0)(numByte1 = 20))


  //  %1 = getelementptr inbounds [5 x i32], [5 x i32]* %0, i32 0, i32 %j.0.in, !UID !9, !ScalaLabel !10
  val getelementptr1 = Module(new GepTwoNode(NumOuts = 1, ID = 1)(numByte1 = 0, numByte2 = 4))


  //  %2 = load i32, i32* %1, align 4, !UID !11, !ScalaLabel !12
  val load2 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 2, RouteID = 0))


  //  %3 = getelementptr inbounds [5 x i32], [5 x i32]* %b.in, i32 %i.0.in, !UID !13, !ScalaLabel !14
  val getelementptr3 = Module(new GepOneNode(NumOuts = 1, ID = 3)(numByte1 = 20))


  //  %4 = getelementptr inbounds [5 x i32], [5 x i32]* %3, i32 0, i32 %j.0.in, !UID !15, !ScalaLabel !16
  val getelementptr4 = Module(new GepTwoNode(NumOuts = 1, ID = 4)(numByte1 = 0, numByte2 = 4))


  //  %5 = load i32, i32* %4, align 4, !UID !17, !ScalaLabel !18
  val load5 = Module(new UnTypLoad(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 5, RouteID = 1))


  //  %6 = add nsw i32 %2, %5, !UID !19, !ScalaLabel !20
  val add6 = Module(new ComputeNode(NumOuts = 1, ID = 6, opCode = "add")(sign = false))


  //  %7 = getelementptr inbounds [5 x i32], [5 x i32]* %c.in, i32 %i.0.in, !UID !21, !ScalaLabel !22
  val getelementptr7 = Module(new GepOneNode(NumOuts = 1, ID = 7)(numByte1 = 20))


  //  %8 = getelementptr inbounds [5 x i32], [5 x i32]* %7, i32 0, i32 %j.0.in, !UID !23, !ScalaLabel !24
  val getelementptr8 = Module(new GepTwoNode(NumOuts = 1, ID = 8)(numByte1 = 0, numByte2 = 4))


  //  store i32 %6, i32* %8, align 4, !UID !25, !ScalaLabel !26
  val store9 = Module(new UnTypStore(NumPredOps = 0, NumSuccOps = 0, NumOuts = 1, ID = 9, RouteID = 0))


  //  br label %my_pfor.preattach, !UID !27, !BB_UID !28, !ScalaLabel !29
  val br10 = Module(new UBranchNode(ID = 10))

  // [BasicBlock]  my_pfor.preattach:

  //  ret void, !UID !30, !BB_UID !31, !ScalaLabel !32
  val ret11 = Module(new RetNodeNew(retTypes = List(32), ID = 11))


  /* ================================================================== *
   *                   INITIALIZING PARAM                               *
   * ================================================================== */


  /**
    * Instantiating parameters
    */
  val param = Data_cilk_for_test06_detach_2_FlowParam


  /* ================================================================== *
   *                   CONNECTING BASIC BLOCKS TO PREDICATE INSTRUCTIONS*
   * ================================================================== */


  /**
    * Connecting basic blocks to predicate instructions
    */


  bb_my_pfor_body5.io.predicateIn <> InputSplitter.io.Out.enable

  /**
    * Connecting basic blocks to predicate instructions
    */

  //Connecting br10 to bb_my_pfor_preattach
  bb_my_pfor_preattach.io.predicateIn <> br10.io.Out(param.br10_brn_bb("bb_my_pfor_preattach"))


  // There is no detach instruction


  /* ================================================================== *
   *                   CONNECTING BASIC BLOCKS TO INSTRUCTIONS          *
   * ================================================================== */


  /**
    * Wiring enable signals to the instructions
    */

  getelementptr0.io.enable <> bb_my_pfor_body5.io.Out(param.bb_my_pfor_body5_activate("getelementptr0"))

  getelementptr1.io.enable <> bb_my_pfor_body5.io.Out(param.bb_my_pfor_body5_activate("getelementptr1"))

  load2.io.enable <> bb_my_pfor_body5.io.Out(param.bb_my_pfor_body5_activate("load2"))

  getelementptr3.io.enable <> bb_my_pfor_body5.io.Out(param.bb_my_pfor_body5_activate("getelementptr3"))

  getelementptr4.io.enable <> bb_my_pfor_body5.io.Out(param.bb_my_pfor_body5_activate("getelementptr4"))

  load5.io.enable <> bb_my_pfor_body5.io.Out(param.bb_my_pfor_body5_activate("load5"))

  add6.io.enable <> bb_my_pfor_body5.io.Out(param.bb_my_pfor_body5_activate("add6"))

  getelementptr7.io.enable <> bb_my_pfor_body5.io.Out(param.bb_my_pfor_body5_activate("getelementptr7"))

  getelementptr8.io.enable <> bb_my_pfor_body5.io.Out(param.bb_my_pfor_body5_activate("getelementptr8"))

  store9.io.enable <> bb_my_pfor_body5.io.Out(param.bb_my_pfor_body5_activate("store9"))

  br10.io.enable <> bb_my_pfor_body5.io.Out(param.bb_my_pfor_body5_activate("br10"))


  ret11.io.enable <> bb_my_pfor_preattach.io.Out(param.bb_my_pfor_preattach_activate("ret11"))


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

  /**
    * Connecting PHI Masks
    */
  //Connect PHI node

  /**
    * Connecting PHI Masks
    */
  //Connect PHI node
  // There is no PHI node


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


  // Wiring GEP instruction to the parent instruction
  getelementptr1.io.baseAddress <> getelementptr0.io.Out(param.getelementptr1_in("getelementptr0"))


  // Wiring GEP instruction to the Constant
  getelementptr1.io.idx1.valid := true.B
  getelementptr1.io.idx1.bits.predicate := true.B
  getelementptr1.io.idx1.bits.data := 0.U


  // Wiring GEP instruction to the function argument
  getelementptr1.io.idx2 <> field2_expand.io.Out(0)


  // Wiring Load instruction to the parent instruction
  load2.io.GepAddr <> getelementptr1.io.Out(param.load2_in("getelementptr1"))
  load2.io.memResp <> CacheMem.io.ReadOut(0)
  CacheMem.io.ReadIn(0) <> load2.io.memReq


  // Wiring GEP instruction to the function argument
  getelementptr3.io.baseAddress <> InputSplitter.io.Out.data("field3")


  // Wiring GEP instruction to the function argument
  getelementptr3.io.idx1 <> field1_expand.io.Out(1)


  // Wiring GEP instruction to the parent instruction
  getelementptr4.io.baseAddress <> getelementptr3.io.Out(param.getelementptr4_in("getelementptr3"))


  // Wiring GEP instruction to the Constant
  getelementptr4.io.idx1.valid := true.B
  getelementptr4.io.idx1.bits.predicate := true.B
  getelementptr4.io.idx1.bits.data := 0.U


  // Wiring GEP instruction to the function argument
  getelementptr4.io.idx2 <> field2_expand.io.Out(1)


  // Wiring Load instruction to the parent instruction
  load5.io.GepAddr <> getelementptr4.io.Out(param.load5_in("getelementptr4"))
  load5.io.memResp <> CacheMem.io.ReadOut(1)
  CacheMem.io.ReadIn(1) <> load5.io.memReq


  // Wiring instructions
  add6.io.LeftIO <> load2.io.Out(param.add6_in("load2"))

  // Wiring instructions
  add6.io.RightIO <> load5.io.Out(param.add6_in("load5"))

  // Wiring GEP instruction to the function argument
  getelementptr7.io.baseAddress <> InputSplitter.io.Out.data("field4")


  // Wiring GEP instruction to the function argument
  getelementptr7.io.idx1 <> field1_expand.io.Out(2)


  // Wiring GEP instruction to the parent instruction
  getelementptr8.io.baseAddress <> getelementptr7.io.Out(param.getelementptr8_in("getelementptr7"))


  // Wiring GEP instruction to the Constant
  getelementptr8.io.idx1.valid := true.B
  getelementptr8.io.idx1.bits.predicate := true.B
  getelementptr8.io.idx1.bits.data := 0.U


  // Wiring GEP instruction to the function argument
  getelementptr8.io.idx2 <> field2_expand.io.Out(2)


  store9.io.inData <> add6.io.Out(param.store9_in("add6"))


  // Wiring Store instruction to the parent instruction
  store9.io.GepAddr <> getelementptr8.io.Out(param.store9_in("getelementptr8"))
  store9.io.memResp <> CacheMem.io.WriteOut(0)
  CacheMem.io.WriteIn(0) <> store9.io.memReq
  // store9.io.Out(0).ready := true.B // Manually commented out


  /**
    * Connecting Dataflow signals
    */
  ret11.io.In.data("field0") <> store9.io.Out(0) // manually connected
  io.out <> ret11.io.Out


}

import java.io.{File, FileWriter}

object cilk_for_test06_detach_2Main extends App {
  val dir = new File("RTL/cilk_for_test06_detach_2");
  dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new cilk_for_test06_detach_2DF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}

