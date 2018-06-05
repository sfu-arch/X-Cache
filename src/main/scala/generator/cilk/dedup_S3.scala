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

object Data_S3_FlowParam{

  val bb_entry_pred = Map(
    "active" -> 0
  )


  val bb_entry_activate = Map(
    "getelementptr0" -> 0,
    "store1" -> 1,
    "urem2" -> 2,
    "getelementptr3" -> 3,
    "store4" -> 4,
    "ret5" -> 5
  )


  //  %arrayidx = getelementptr inbounds i32, i32* %chunk, i32 1, !UID !16, !ScalaLabel !17
  val getelementptr0_in = Map(
    "field0" -> 0
  )


  //  store i32 50, i32* %arrayidx, align 4, !UID !18, !ScalaLabel !19
  val store1_in = Map(
    "getelementptr0" -> 0
  )


  //  %rem = urem i32 %iter, 100, !UID !20, !ScalaLabel !21
  val urem2_in = Map(
    "field2" -> 0
  )


  //  %arrayidx1 = getelementptr inbounds [100 x i32], [100 x i32]* @q, i32 0, i32 %rem, !UID !22, !ScalaLabel !23
  val getelementptr3_in = Map(
    "" -> 0,
    "urem2" -> 0
  )


  //  store i32 %pos, i32* %arrayidx1, align 4, !UID !24, !ScalaLabel !25
  val store4_in = Map(
    "field1" -> 0,
    "getelementptr3" -> 0
  )


  //  ret void, !UID !26, !BB_UID !27, !ScalaLabel !28
  val ret5_in = Map(

  )


}




  /* ================================================================== *
   *                   PRINTING PORTS DEFINITION                        *
   * ================================================================== */


abstract class S3DFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val in = Flipped(Decoupled(new Call(List(32,32,32))))
    val CacheResp = Flipped(Valid(new CacheRespT))
    val CacheReq = Decoupled(new CacheReq)
    val out = Decoupled(new Call(List(32)))
  })
}




  /* ================================================================== *
   *                   PRINTING MODULE DEFINITION                       *
   * ================================================================== */


class S3DF(implicit p: Parameters) extends S3DFIO()(p) {



  /* ================================================================== *
   *                   PRINTING MEMORY SYSTEM                           *
   * ================================================================== */


	val StackPointer = Module(new Stack(NumOps = 1))

	val RegisterFile = Module(new TypeStackFile(ID=0,Size=32,NReads=2,NWrites=2)
		            (WControl=new WriteMemoryController(NumOps=2,BaseSize=2,NumEntries=2))
		            (RControl=new ReadMemoryController(NumOps=2,BaseSize=2,NumEntries=2)))

	val CacheMem = Module(new UnifiedController(ID=0,Size=4096,NReads=2,NWrites=2)
		            (WControl=new WriteMemoryController(NumOps=2,BaseSize=2,NumEntries=2))
		            (RControl=new ReadMemoryController(NumOps=2,BaseSize=2,NumEntries=2))
		            (RWArbiter=new ReadWriteArbiter()))

  io.CacheReq <> CacheMem.io.CacheReq
  CacheMem.io.CacheResp <> io.CacheResp

  val InputSplitter = Module(new SplitCall(List(32,32,32)))
  InputSplitter.io.In <> io.in



  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */


  //Function doesn't have any loop


  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */


  //Initializing BasicBlocks: 

  val bb_entry = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 6, BID = 0))






  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */


  //Initializing Instructions: 

  // [BasicBlock]  entry:

  //  %arrayidx = getelementptr inbounds i32, i32* %chunk, i32 1, !UID !16, !ScalaLabel !17
  val getelementptr0 = Module (new GepOneNode(NumOuts = 1, ID = 0)(numByte1 = 4))


  //  store i32 50, i32* %arrayidx, align 4, !UID !18, !ScalaLabel !19
  val store1 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=1,RouteID=0))


  //  %rem = urem i32 %iter, 100, !UID !20, !ScalaLabel !21

//  val urem2 = Module (new ComputeNode(NumOuts = 1, ID = 2, opCode = "urem")(sign=false))
  val urem2 = for(i<- 0 until 70) yield {
    val foo =  Module (new ComputeNode(NumOuts = 1, ID = 2, opCode = "urem")(sign=false))
    foo
  }

  //  %arrayidx1 = getelementptr inbounds [100 x i32], [100 x i32]* @q, i32 0, i32 %rem, !UID !22, !ScalaLabel !23
  val getelementptr3 = Module (new GepTwoNode(NumOuts = 1, ID = 3)(numByte1 = 4, numByte2 = 4))


  //  store i32 %pos, i32* %arrayidx1, align 4, !UID !24, !ScalaLabel !25
  val store4 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=4,RouteID=1))


  //  ret void, !UID !26, !BB_UID !27, !ScalaLabel !28
  val ret5 = Module(new RetNode(NumPredIn=1, retTypes=List(32), ID=5))





  /* ================================================================== *
   *                   INITIALIZING PARAM                               *
   * ================================================================== */


  /**
    * Instantiating parameters
    */
  val param = Data_S3_FlowParam



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


  // There is no branch instruction



  // There is no detach instruction




  /* ================================================================== *
   *                   CONNECTING BASIC BLOCKS TO INSTRUCTIONS          *
   * ================================================================== */


  /**
    * Wiring enable signals to the instructions
    */

  getelementptr0.io.enable <> bb_entry.io.Out(param.bb_entry_activate("getelementptr0"))

  store1.io.enable <> bb_entry.io.Out(param.bb_entry_activate("store1"))

  urem2(70-1).io.enable <> bb_entry.io.Out(param.bb_entry_activate("urem2"))

  getelementptr3.io.enable <> bb_entry.io.Out(param.bb_entry_activate("getelementptr3"))

  store4.io.enable <> bb_entry.io.Out(param.bb_entry_activate("store4"))

  ret5.io.enable <> bb_entry.io.Out(param.bb_entry_activate("ret5"))





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


  // Wiring GEP instruction to the Constant
  getelementptr0.io.idx1.valid :=  true.B
  getelementptr0.io.idx1.bits.predicate :=  true.B
  getelementptr0.io.idx1.bits.data :=  1.U


  // Wiring constant instructions to store
  store1.io.inData.bits.data := 50.U
  store1.io.inData.bits.predicate := true.B
  store1.io.inData.valid := true.B



  // Wiring Store instruction to the parent instruction
  store1.io.GepAddr <> getelementptr0.io.Out(param.store1_in("getelementptr0"))
  store1.io.memResp  <> CacheMem.io.WriteOut(0)
  CacheMem.io.WriteIn(0) <> store1.io.memReq
  store1.io.Out(0).ready := true.B



  // Wiring Binary instruction to the function argument
  urem2(0).io.LeftIO <> InputSplitter.io.Out.data("field2")

  // Wiring constant
  urem2(0).io.RightIO.bits.data := 100.U
  urem2(0).io.RightIO.bits.predicate := true.B
  urem2(0).io.RightIO.valid := true.B

  for (i <- 1 until 70) {
    urem2(i).io.LeftIO <> urem2(i-1).io.Out(0)
    urem2(i-1).io.enable.enq(ControlBundle.active())
    // Wiring constant
    urem2(i).io.RightIO.bits.data := 100.U
    urem2(i).io.RightIO.bits.predicate := true.B
    urem2(i).io.RightIO.valid := true.B

  }
  // Manually added.  Pointer to q[] which is global
  getelementptr3.io.baseAddress.valid := true.B
  getelementptr3.io.baseAddress.bits.predicate :=  true.B
  getelementptr3.io.baseAddress.bits.data :=  0.U


  // Wiring GEP instruction to the Constant
  getelementptr3.io.idx1.valid :=  true.B
  getelementptr3.io.idx1.bits.predicate :=  true.B
  getelementptr3.io.idx1.bits.data :=  0.U


  // Wiring GEP instruction to the parent instruction
  getelementptr3.io.idx2 <> urem2(39).io.Out(param.getelementptr3_in("urem2"))


  // Wiring Store instruction to the function argument
  store4.io.inData <> InputSplitter.io.Out.data("field1")



  // Wiring Store instruction to the parent instruction
  store4.io.GepAddr <> getelementptr3.io.Out(param.store4_in("getelementptr3"))
  store4.io.memResp  <> CacheMem.io.WriteOut(1)
  CacheMem.io.WriteIn(1) <> store4.io.memReq
  //store4.io.Out(0).ready := true.B



  /**
    * Connecting Dataflow signals
    */
  ret5.io.predicateIn(0).bits.control := true.B
  ret5.io.predicateIn(0).bits.taskID := 0.U
  ret5.io.predicateIn(0).valid := true.B
  ret5.io.In.data("field0") <> store4.io.Out(0)
//  ret5.io.In.data("field0").bits.data := 1.U
//  ret5.io.In.data("field0").bits.predicate := true.B
//  ret5.io.In.data("field0").valid := true.B
  io.out <> ret5.io.Out


}

import java.io.{File, FileWriter}
object S3Main extends App {
  val dir = new File("RTL/S3") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new S3DF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}

