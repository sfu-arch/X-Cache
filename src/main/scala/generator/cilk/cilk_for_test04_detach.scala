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

object Data_cilk_for_test04_detach_FlowParam{

  val bb_my_pfor_body_pred = Map(
    "active" -> 0
  )


  val bb_my_pfor_preattach_pred = Map(
    "br7" -> 0
  )


  val br7_brn_bb = Map(
    "bb_my_pfor_preattach" -> 0
  )


  val bb_my_pfor_body_activate = Map(
    "getelementptr0" -> 0,
    "load1" -> 1,
    "getelementptr2" -> 2,
    "load3" -> 3,
    "add4" -> 4,
    "getelementptr5" -> 5,
    "store6" -> 6,
    "br7" -> 7
  )


  val bb_my_pfor_preattach_activate = Map(
    "ret8" -> 0
  )


  //  %0 = getelementptr inbounds i32, i32* %a.in, i32 %i.0.in, !UID !7, !ScalaLabel !8
  val getelementptr0_in = Map(
    "field0" -> 0,
    "field1" -> 0
  )


  //  %1 = load i32, i32* %0, align 4, !UID !9, !ScalaLabel !10
  val load1_in = Map(
    "getelementptr0" -> 0
  )


  //  %2 = getelementptr inbounds i32, i32* %b.in, i32 %i.0.in, !UID !11, !ScalaLabel !12
  val getelementptr2_in = Map(
    "field2" -> 0,
    "field1" -> 1
  )


  //  %3 = load i32, i32* %2, align 4, !UID !13, !ScalaLabel !14
  val load3_in = Map(
    "getelementptr2" -> 0
  )


  //  %4 = add i32 %1, %3, !UID !15, !ScalaLabel !16
  val add4_in = Map(
    "load1" -> 0,
    "load3" -> 0
  )


  //  %5 = getelementptr inbounds i32, i32* %c.in, i32 %i.0.in, !UID !17, !ScalaLabel !18
  val getelementptr5_in = Map(
    "field3" -> 0,
    "field1" -> 2
  )


  //  store i32 %4, i32* %5, align 4, !UID !19, !ScalaLabel !20
  val store6_in = Map(
    "add4" -> 0,
    "getelementptr5" -> 0
  )


  //  ret void, !UID !24, !BB_UID !25, !ScalaLabel !26
  val ret8_in = Map(

  )


}




  /* ================================================================== *
   *                   PRINTING PORTS DEFINITION                        *
   * ================================================================== */


abstract class cilk_for_test04_detachDFIO(implicit val p: Parameters) extends Module with CoreParams {
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


class cilk_for_test04_detachDF(implicit p: Parameters) extends cilk_for_test04_detachDFIO()(p) {



  /* ================================================================== *
   *                   PRINTING MEMORY SYSTEM                           *
   * ================================================================== */


	val StackPointer = Module(new Stack(NumOps = 1))

	val RegisterFile = Module(new TypeStackFile(ID=0,Size=32,NReads=2,NWrites=1)
		            (WControl=new WriteMemoryController(NumOps=1,BaseSize=2,NumEntries=2))
		            (RControl=new ReadMemoryController(NumOps=2,BaseSize=2,NumEntries=2)))

	val CacheMem = Module(new UnifiedController(ID=0,Size=32,NReads=2,NWrites=1)
		            (WControl=new WriteMemoryController(NumOps=1,BaseSize=2,NumEntries=2))
		            (RControl=new ReadMemoryController(NumOps=2,BaseSize=2,NumEntries=2))
		            (RWArbiter=new ReadWriteArbiter()))

  io.CacheReq <> CacheMem.io.CacheReq
  CacheMem.io.CacheResp <> io.CacheResp

  val InputSplitter = Module(new SplitCall(List(32,32,32,32)))
  InputSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */


  //Function doesn't have any loop


  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */


  //Initializing BasicBlocks: 

  val bb_my_pfor_body = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 3, BID = 0))

  val bb_my_pfor_preattach = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 1))






  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */


  //Initializing Instructions: 

  // [BasicBlock]  my_pfor.body:

  //  %0 = getelementptr inbounds i32, i32* %a.in, i32 %i.0.in, !UID !7, !ScalaLabel !8
  val getelementptr0 = Module (new GepOneNode(NumOuts = 1, ID = 0, Desc = "getelementptr0")(numByte1 = 1))


  //  %1 = load i32, i32* %0, align 4, !UID !9, !ScalaLabel !10
  val load1 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=1,RouteID=0,Desc="load1"))


  //  %2 = getelementptr inbounds i32, i32* %b.in, i32 %i.0.in, !UID !11, !ScalaLabel !12
  val getelementptr2 = Module (new GepOneNode(NumOuts = 1, ID = 2, Desc = "getelementptr2")(numByte1 = 1))


  //  %3 = load i32, i32* %2, align 4, !UID !13, !ScalaLabel !14
  val load3 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=3,RouteID=1,Desc="load3"))


  //  %4 = add i32 %1, %3, !UID !15, !ScalaLabel !16
  val add4 = Module (new ComputeNode(NumOuts = 1, ID = 4, opCode = "add")(sign=false))


  //  %5 = getelementptr inbounds i32, i32* %c.in, i32 %i.0.in, !UID !17, !ScalaLabel !18
  val getelementptr5 = Module (new GepOneNode(NumOuts = 1, ID = 5, Desc = "getelementptr5")(numByte1 = 1))


  //  store i32 %4, i32* %5, align 4, !UID !19, !ScalaLabel !20
  val store6 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=6,RouteID=0,Desc="store6"))


  //  br label %my_pfor.preattach, !UID !21, !BB_UID !22, !ScalaLabel !23
  val br7 = Module (new UBranchFastNode(ID = 7, Desc = "br7"))



  // [BasicBlock]  my_pfor.preattach:

  //  ret void, !UID !24, !BB_UID !25, !ScalaLabel !26
  val ret8 = Module(new RetNode(NumPredIn=1, retTypes=List(32), ID=8))







  /* ================================================================== *
   *                   INITIALIZING PARAM                               *
   * ================================================================== */


  /**
    * Instantiating parameters
    */
  val param = Data_cilk_for_test04_detach_FlowParam



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

  //Connecting br7 to bb_my_pfor_preattach
  bb_my_pfor_preattach.io.predicateIn <> br7.io.Out(param.br7_brn_bb("bb_my_pfor_preattach"))



  // There is no detach instruction




  /* ================================================================== *
   *                   CONNECTING BASIC BLOCKS TO INSTRUCTIONS          *
   * ================================================================== */


  /**
    * Wiring enable signals to the instructions
    */
/*
  getelementptr0.io.enable <> bb_my_pfor_body.io.Out(param.bb_my_pfor_body_activate("getelementptr0"))

  load1.io.enable <> bb_my_pfor_body.io.Out(param.bb_my_pfor_body_activate("load1"))

  getelementptr2.io.enable <> bb_my_pfor_body.io.Out(param.bb_my_pfor_body_activate("getelementptr2"))

  load3.io.enable <> bb_my_pfor_body.io.Out(param.bb_my_pfor_body_activate("load3"))

  add4.io.enable <> bb_my_pfor_body.io.Out(param.bb_my_pfor_body_activate("add4"))

  getelementptr5.io.enable <> bb_my_pfor_body.io.Out(param.bb_my_pfor_body_activate("getelementptr5"))

  store6.io.enable <> bb_my_pfor_body.io.Out(param.bb_my_pfor_body_activate("store6"))

  br7.io.enable <> bb_my_pfor_body.io.Out(param.bb_my_pfor_body_activate("br7"))



  ret8.io.enable <> bb_my_pfor_preattach.io.Out(param.bb_my_pfor_preattach_activate("ret8"))
*/
  getelementptr0.io.enable <> bb_my_pfor_body.io.Out(0)
  getelementptr2.io.enable <> bb_my_pfor_body.io.Out(1)
  getelementptr5.io.enable <> bb_my_pfor_body.io.Out(2)

  load1.io.enable.valid := true.B
  load1.io.enable.bits.control := true.B
  load3.io.enable.valid  := true.B
  load3.io.enable.bits.control  := true.B
  add4.io.enable.valid  := true.B
  add4.io.enable.bits.control  := true.B
  store6.io.enable.valid  := true.B
  store6.io.enable.bits.control  := true.B
  br7.io.enable.valid  := true.B
  br7.io.enable.bits.control  := true.B
  ret8.io.enable.valid  := true.B
  ret8.io.enable.bits.control  := true.B




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
  getelementptr0.io.idx1 <> InputSplitter.io.Out.data("field1")


  // Wiring Load instruction to the parent instruction
  load1.io.GepAddr <> getelementptr0.io.Out(param.load1_in("getelementptr0"))
  load1.io.memResp <> RegisterFile.io.ReadOut(0)
  RegisterFile.io.ReadIn(0) <> load1.io.memReq




  // Wiring GEP instruction to the function argument
  getelementptr2.io.baseAddress <> InputSplitter.io.Out.data("field2")


  // Wiring GEP instruction to the function argument
  getelementptr2.io.idx1 <> InputSplitter.io.Out.data("field1")


  // Wiring Load instruction to the parent instruction
  load3.io.GepAddr <> getelementptr2.io.Out(param.load3_in("getelementptr2"))
  load3.io.memResp <> RegisterFile.io.ReadOut(1)
  RegisterFile.io.ReadIn(1) <> load3.io.memReq




  // Wiring instructions
  add4.io.LeftIO <> load1.io.Out(param.add4_in("load1"))

  // Wiring instructions
  add4.io.RightIO <> load3.io.Out(param.add4_in("load3"))

  // Wiring GEP instruction to the function argument
  getelementptr5.io.baseAddress <> InputSplitter.io.Out.data("field3")


  // Wiring GEP instruction to the function argument
  getelementptr5.io.idx1 <> InputSplitter.io.Out.data("field1")


  store6.io.inData <> add4.io.Out(param.store6_in("add4"))



  // Wiring Store instruction to the parent instruction
  store6.io.GepAddr <> getelementptr5.io.Out(param.store6_in("getelementptr5"))
  store6.io.memResp  <> RegisterFile.io.WriteOut(0)
  RegisterFile.io.WriteIn(0) <> store6.io.memReq



  /**
    * Connecting Dataflow signals
    */
  ret8.io.predicateIn(0).bits.control := true.B
  ret8.io.predicateIn(0).bits.taskID := 0.U
  ret8.io.predicateIn(0).valid := true.B
  ret8.io.In.data("field0") <> store6.io.Out(0)  // Manually connected
  io.out <> ret8.io.Out


}

import java.io.{File, FileWriter}
object cilk_for_test04_detachMain extends App {
  val dir = new File("RTL/cilk_for_test04_detach") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new cilk_for_test04_detachDF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}

