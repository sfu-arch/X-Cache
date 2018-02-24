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

object Data_test06_FlowParam{

  val bb_entry_pred = Map(
    "active" -> 0
  )


  val bb_entry_activate = Map(
    "alloca0" -> 0,
    "alloca1" -> 1,
    "getelementptr2" -> 2,
    "store3" -> 3,
    "getelementptr4" -> 4,
    "store5" -> 5,
    "getelementptr6" -> 6,
    "load7" -> 7,
    "getelementptr8" -> 8,
    "load9" -> 9,
    "add10" -> 10,
    "getelementptr11" -> 11,
    "store12" -> 12,
    "getelementptr13" -> 13,
    "load14" -> 14,
    "ret15" -> 15
  )


  //  %alloc0 = alloca [2 x i32], align 4, !UID !8, !ScalaLabel !9
  val alloca0_in = Map(

  )


  //  %alloc1 = alloca [1 x i32], align 4, !UID !10, !ScalaLabel !11
  val alloca1_in = Map(

  )


  //  %arrayidx = getelementptr inbounds [2 x i32], [2 x i32]* %alloc0, i32 0, i32 0, !UID !12, !ScalaLabel !13
  val getelementptr2_in = Map(
    "alloca0" -> 0
  )


  //  store i32 %a, i32* %arrayidx, align 4, !UID !14, !ScalaLabel !15
  val store3_in = Map(
    "field0" -> 0,
    "getelementptr2" -> 0
  )


  //  %arrayidx1 = getelementptr inbounds [2 x i32], [2 x i32]* %alloc0, i32 0, i32 1, !UID !16, !ScalaLabel !17
  val getelementptr4_in = Map(
    "alloca0" -> 1
  )


  //  store i32 %b, i32* %arrayidx1, align 4, !UID !18, !ScalaLabel !19
  val store5_in = Map(
    "field1" -> 0,
    "getelementptr4" -> 0
  )


  //  %arrayidx2 = getelementptr inbounds [2 x i32], [2 x i32]* %alloc0, i32 0, i32 0, !UID !20, !ScalaLabel !21
  val getelementptr6_in = Map(
    "alloca0" -> 2
  )


  //  %0 = load i32, i32* %arrayidx2, align 4, !UID !22, !ScalaLabel !23
  val load7_in = Map(
    "getelementptr6" -> 0
  )


  //  %arrayidx3 = getelementptr inbounds [2 x i32], [2 x i32]* %alloc0, i32 0, i32 1, !UID !24, !ScalaLabel !25
  val getelementptr8_in = Map(
    "alloca0" -> 3
  )


  //  %1 = load i32, i32* %arrayidx3, align 4, !UID !26, !ScalaLabel !27
  val load9_in = Map(
    "getelementptr8" -> 0
  )


  //  %add = add i32 %0, %1, !UID !28, !ScalaLabel !29
  val add10_in = Map(
    "load7" -> 0,
    "load9" -> 0
  )


  //  %arrayidx4 = getelementptr inbounds [1 x i32], [1 x i32]* %alloc1, i32 0, i32 0, !UID !30, !ScalaLabel !31
  val getelementptr11_in = Map(
    "alloca1" -> 0
  )


  //  store i32 %add, i32* %arrayidx4, align 4, !UID !32, !ScalaLabel !33
  val store12_in = Map(
    "add10" -> 0,
    "getelementptr11" -> 0
  )


  //  %arrayidx5 = getelementptr inbounds [1 x i32], [1 x i32]* %alloc1, i32 0, i32 0, !UID !34, !ScalaLabel !35
  val getelementptr13_in = Map(
    "alloca1" -> 1
  )


  //  %2 = load i32, i32* %arrayidx5, align 4, !UID !36, !ScalaLabel !37
  val load14_in = Map(
    "getelementptr13" -> 0
  )


  //  ret i32 %2, !UID !38, !BB_UID !39, !ScalaLabel !40
  val ret15_in = Map(
    "load14" -> 0
  )


}




  /* ================================================================== *
   *                   PRINTING PORTS DEFINITION                        *
   * ================================================================== */


abstract class test06DFIO(implicit val p: Parameters) extends Module with CoreParams {
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


class test06DF(implicit p: Parameters) extends test06DFIO()(p) {



  /* ================================================================== *
   *                   PRINTING MEMORY SYSTEM                           *
   * ================================================================== */


	val StackPointer = Module(new Stack(NumOps = 2))

	val RegisterFile = Module(new TypeStackFile(ID=0,Size=64,NReads=3,NWrites=3)
		            (WControl=new WriteMemoryController(NumOps=3,BaseSize=2,NumEntries=2))
		            (RControl=new ReadMemoryController(NumOps=3,BaseSize=2,NumEntries=2)))

	val CacheMem = Module(new UnifiedController(ID=0,Size=32,NReads=3,NWrites=3)
		            (WControl=new WriteMemoryController(NumOps=3,BaseSize=2,NumEntries=2))
		            (RControl=new ReadMemoryController(NumOps=3,BaseSize=2,NumEntries=2))
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

  val bb_entry = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 16, BID = 0, Desc = "bb_entry")(p))






  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */


  //Initializing Instructions: 

  // [BasicBlock]  entry:

  //  %alloc0 = alloca [2 x i32], align 4, !UID !8, !ScalaLabel !9
  val alloca0 = Module(new AllocaNode(NumOuts=4, RouteID=0, ID=0, FrameSize=12))


  //  %alloc1 = alloca [1 x i32], align 4, !UID !10, !ScalaLabel !11
  val alloca1 = Module(new AllocaNode(NumOuts=2, RouteID=1, ID=1, FrameSize=12))


  //  %arrayidx = getelementptr inbounds [2 x i32], [2 x i32]* %alloc0, i32 0, i32 0, !UID !12, !ScalaLabel !13
  val getelementptr2 = Module (new GepTwoNode(NumOuts = 1, ID = 2, Desc = "getelementptr2")(numByte1 = 8, numByte2 = 0)(p))


  //  store i32 %a, i32* %arrayidx, align 4, !UID !14, !ScalaLabel !15
  val store3 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=1, NumOuts=1,ID=3,RouteID=0,Desc="store3"))


  //  %arrayidx1 = getelementptr inbounds [2 x i32], [2 x i32]* %alloc0, i32 0, i32 1, !UID !16, !ScalaLabel !17
  val getelementptr4 = Module (new GepTwoNode(NumOuts = 1, ID = 4, Desc = "getelementptr4")(numByte1 = 8, numByte2 = 4)(p))


  //  store i32 %b, i32* %arrayidx1, align 4, !UID !18, !ScalaLabel !19
  val store5 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=1, NumOuts=1,ID=5,RouteID=1,Desc="store5"))


  //  %arrayidx2 = getelementptr inbounds [2 x i32], [2 x i32]* %alloc0, i32 0, i32 0, !UID !20, !ScalaLabel !21
  val getelementptr6 = Module (new GepTwoNode(NumOuts = 1, ID = 6, Desc = "getelementptr6")(numByte1 = 8, numByte2 = 0)(p))


  //  %0 = load i32, i32* %arrayidx2, align 4, !UID !22, !ScalaLabel !23
  val load7 = Module(new UnTypLoad(NumPredOps=1, NumSuccOps=0, NumOuts=1,ID=7,RouteID=0,Desc="load7"))


  //  %arrayidx3 = getelementptr inbounds [2 x i32], [2 x i32]* %alloc0, i32 0, i32 1, !UID !24, !ScalaLabel !25
  val getelementptr8 = Module (new GepTwoNode(NumOuts = 1, ID = 8, Desc = "getelementptr8")(numByte1 = 8, numByte2 = 4)(p))


  //  %1 = load i32, i32* %arrayidx3, align 4, !UID !26, !ScalaLabel !27
  val load9 = Module(new UnTypLoad(NumPredOps=1, NumSuccOps=0, NumOuts=1,ID=9,RouteID=1,Desc="load9"))


  //  %add = add i32 %0, %1, !UID !28, !ScalaLabel !29
  val add10 = Module (new ComputeNode(NumOuts = 1, ID = 10, opCode = "add", Desc = "add10")(sign=false)(p))


  //  %arrayidx4 = getelementptr inbounds [1 x i32], [1 x i32]* %alloc1, i32 0, i32 0, !UID !30, !ScalaLabel !31
  val getelementptr11 = Module (new GepTwoNode(NumOuts = 1, ID = 11, Desc = "getelementptr11")(numByte1 = 4, numByte2 = 0)(p))


  //  store i32 %add, i32* %arrayidx4, align 4, !UID !32, !ScalaLabel !33
  val store12 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=1, NumOuts=1,ID=12,RouteID=2,Desc="store12"))


  //  %arrayidx5 = getelementptr inbounds [1 x i32], [1 x i32]* %alloc1, i32 0, i32 0, !UID !34, !ScalaLabel !35
  val getelementptr13 = Module (new GepTwoNode(NumOuts = 1, ID = 13, Desc = "getelementptr13")(numByte1 = 4, numByte2 = 0)(p))


  //  %2 = load i32, i32* %arrayidx5, align 4, !UID !36, !ScalaLabel !37
  val load14 = Module(new UnTypLoad(NumPredOps=1, NumSuccOps=0, NumOuts=1,ID=14,RouteID=2,Desc="load14"))


  //  ret i32 %2, !UID !38, !BB_UID !39, !ScalaLabel !40
  val ret15 = Module(new RetNode(NumPredIn=1, retTypes=List(32), ID=15, Desc="ret15"))







  /* ================================================================== *
   *                   INITIALIZING PARAM                               *
   * ================================================================== */


  /**
    * Instantiating parameters
    */
  val param = Data_test06_FlowParam



  /* ================================================================== *
   *                   CONNECTING BASIC BLOCKS TO PREDICATE INSTRUCTIONS*
   * ================================================================== */


  /**
     * Connecting basic blocks to predicate instructions
     */


  bb_entry.io.predicateIn(0) <> InputSplitter.io.Out.enable

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

  alloca0.io.enable <> bb_entry.io.Out(param.bb_entry_activate("alloca0"))

  alloca1.io.enable <> bb_entry.io.Out(param.bb_entry_activate("alloca1"))

  getelementptr2.io.enable <> bb_entry.io.Out(param.bb_entry_activate("getelementptr2"))

  store3.io.enable <> bb_entry.io.Out(param.bb_entry_activate("store3"))

  getelementptr4.io.enable <> bb_entry.io.Out(param.bb_entry_activate("getelementptr4"))

  store5.io.enable <> bb_entry.io.Out(param.bb_entry_activate("store5"))

  getelementptr6.io.enable <> bb_entry.io.Out(param.bb_entry_activate("getelementptr6"))

  load7.io.enable <> bb_entry.io.Out(param.bb_entry_activate("load7"))

  getelementptr8.io.enable <> bb_entry.io.Out(param.bb_entry_activate("getelementptr8"))

  load9.io.enable <> bb_entry.io.Out(param.bb_entry_activate("load9"))

  add10.io.enable <> bb_entry.io.Out(param.bb_entry_activate("add10"))

  getelementptr11.io.enable <> bb_entry.io.Out(param.bb_entry_activate("getelementptr11"))

  store12.io.enable <> bb_entry.io.Out(param.bb_entry_activate("store12"))

  getelementptr13.io.enable <> bb_entry.io.Out(param.bb_entry_activate("getelementptr13"))

  load14.io.enable <> bb_entry.io.Out(param.bb_entry_activate("load14"))

  ret15.io.enable <> bb_entry.io.Out(param.bb_entry_activate("ret15"))





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
   *                   CONNECTING LOOPHEADERS                           *
   * ================================================================== */


  //Function doesn't have any for loop


  /* ================================================================== *
   *                   DUMPING DATAFLOW                                 *
   * ================================================================== */


  /**
    * Connecting Dataflow signals
    */

  // Wiring Alloca instructions with Static inputs
  alloca0.io.allocaInputIO.bits.size      := 1.U
  alloca0.io.allocaInputIO.bits.numByte   := 8.U
  alloca0.io.allocaInputIO.bits.predicate := true.B
  alloca0.io.allocaInputIO.bits.valid     := true.B
  alloca0.io.allocaInputIO.valid          := true.B

  // Connecting Alloca to Stack
  StackPointer.io.InData(0) <> alloca0.io.allocaReqIO
  alloca0.io.allocaRespIO <> StackPointer.io.OutData(0)


  // Wiring Alloca instructions with Static inputs
  alloca1.io.allocaInputIO.bits.size      := 1.U
  alloca1.io.allocaInputIO.bits.numByte   := 4.U
  alloca1.io.allocaInputIO.bits.predicate := true.B
  alloca1.io.allocaInputIO.bits.valid     := true.B
  alloca1.io.allocaInputIO.valid          := true.B

  // Connecting Alloca to Stack
  StackPointer.io.InData(1) <> alloca1.io.allocaReqIO
  alloca1.io.allocaRespIO <> StackPointer.io.OutData(1)


  // Wiring GEP instruction to the parent instruction
  getelementptr2.io.baseAddress <> alloca0.io.Out(param.getelementptr2_in("alloca0"))


  // Wiring GEP instruction to the Constant
  getelementptr2.io.idx1.valid :=  true.B
  getelementptr2.io.idx1.bits.predicate :=  true.B
  getelementptr2.io.idx1.bits.data :=  0.U


  // Wiring GEP instruction to the Constant
  getelementptr2.io.idx2.valid :=  true.B
  getelementptr2.io.idx2.bits.predicate :=  true.B
  getelementptr2.io.idx2.bits.data :=  0.U


  // Wiring Store instruction to the function argument
  store3.io.inData <> InputSplitter.io.Out.data("field0")   // Manually fixed



  // Wiring Store instruction to the parent instruction
  store3.io.GepAddr <> getelementptr2.io.Out(param.store3_in("getelementptr2"))
  store3.io.memResp  <> RegisterFile.io.WriteOut(0)
  RegisterFile.io.WriteIn(0) <> store3.io.memReq
  store3.io.Out(0).ready := true.B



  // Wiring GEP instruction to the parent instruction
  getelementptr4.io.baseAddress <> alloca0.io.Out(param.getelementptr4_in("alloca0"))


  // Wiring GEP instruction to the Constant
  getelementptr4.io.idx1.valid :=  true.B
  getelementptr4.io.idx1.bits.predicate :=  true.B
  getelementptr4.io.idx1.bits.data :=  0.U


  // Wiring GEP instruction to the Constant
  getelementptr4.io.idx2.valid :=  true.B
  getelementptr4.io.idx2.bits.predicate :=  true.B
  getelementptr4.io.idx2.bits.data :=  1.U


  // Wiring Store instruction to the function argument
  store5.io.inData <> InputSplitter.io.Out.data("field1")   // Manually fixed



  // Wiring Store instruction to the parent instruction
  store5.io.GepAddr <> getelementptr4.io.Out(param.store5_in("getelementptr4"))
  store5.io.memResp  <> RegisterFile.io.WriteOut(1)
  RegisterFile.io.WriteIn(1) <> store5.io.memReq
  store5.io.Out(0).ready := true.B



  // Wiring GEP instruction to the parent instruction
  getelementptr6.io.baseAddress <> alloca0.io.Out(param.getelementptr6_in("alloca0"))


  // Wiring GEP instruction to the Constant
  getelementptr6.io.idx1.valid :=  true.B
  getelementptr6.io.idx1.bits.predicate :=  true.B
  getelementptr6.io.idx1.bits.data :=  0.U


  // Wiring GEP instruction to the Constant
  getelementptr6.io.idx2.valid :=  true.B
  getelementptr6.io.idx2.bits.predicate :=  true.B
  getelementptr6.io.idx2.bits.data :=  0.U


  // Wiring Load instruction to the parent instruction
  load7.io.GepAddr <> getelementptr6.io.Out(param.load7_in("getelementptr6"))
  load7.io.memResp <> RegisterFile.io.ReadOut(0)
  RegisterFile.io.ReadIn(0) <> load7.io.memReq
  load7.io.PredOp(0) <> store3.io.SuccOp(0)  // manually added




  // Wiring GEP instruction to the parent instruction
  getelementptr8.io.baseAddress <> alloca0.io.Out(param.getelementptr8_in("alloca0"))


  // Wiring GEP instruction to the Constant
  getelementptr8.io.idx1.valid :=  true.B
  getelementptr8.io.idx1.bits.predicate :=  true.B
  getelementptr8.io.idx1.bits.data :=  0.U


  // Wiring GEP instruction to the Constant
  getelementptr8.io.idx2.valid :=  true.B
  getelementptr8.io.idx2.bits.predicate :=  true.B
  getelementptr8.io.idx2.bits.data :=  1.U


  // Wiring Load instruction to the parent instruction
  load9.io.GepAddr <> getelementptr8.io.Out(param.load9_in("getelementptr8"))
  load9.io.memResp <> RegisterFile.io.ReadOut(1)
  RegisterFile.io.ReadIn(1) <> load9.io.memReq
  load9.io.PredOp(0) <> store5.io.SuccOp(0)  // manually added




  // Wiring instructions
  add10.io.LeftIO <> load7.io.Out(param.add10_in("load7"))

  // Wiring instructions
  add10.io.RightIO <> load9.io.Out(param.add10_in("load9"))

  // Wiring GEP instruction to the parent instruction
  getelementptr11.io.baseAddress <> alloca1.io.Out(param.getelementptr11_in("alloca1"))


  // Wiring GEP instruction to the Constant
  getelementptr11.io.idx1.valid :=  true.B
  getelementptr11.io.idx1.bits.predicate :=  true.B
  getelementptr11.io.idx1.bits.data :=  0.U


  // Wiring GEP instruction to the Constant
  getelementptr11.io.idx2.valid :=  true.B
  getelementptr11.io.idx2.bits.predicate :=  true.B
  getelementptr11.io.idx2.bits.data :=  0.U


  store12.io.inData <> add10.io.Out(param.store12_in("add10"))



  // Wiring Store instruction to the parent instruction
  store12.io.GepAddr <> getelementptr11.io.Out(param.store12_in("getelementptr11"))
  store12.io.memResp  <> RegisterFile.io.WriteOut(2)
  RegisterFile.io.WriteIn(2) <> store12.io.memReq
  store12.io.Out(0).ready := true.B



  // Wiring GEP instruction to the parent instruction
  getelementptr13.io.baseAddress <> alloca1.io.Out(param.getelementptr13_in("alloca1"))


  // Wiring GEP instruction to the Constant
  getelementptr13.io.idx1.valid :=  true.B
  getelementptr13.io.idx1.bits.predicate :=  true.B
  getelementptr13.io.idx1.bits.data :=  0.U


  // Wiring GEP instruction to the Constant
  getelementptr13.io.idx2.valid :=  true.B
  getelementptr13.io.idx2.bits.predicate :=  true.B
  getelementptr13.io.idx2.bits.data :=  0.U


  // Wiring Load instruction to the parent instruction
  load14.io.GepAddr <> getelementptr13.io.Out(param.load14_in("getelementptr13"))
  load14.io.memResp <> RegisterFile.io.ReadOut(2)
  RegisterFile.io.ReadIn(2) <> load14.io.memReq
  load14.io.PredOp(0) <> store12.io.SuccOp(0)  // manually added




  // Wiring return instruction
  ret15.io.predicateIn(0).bits.control := true.B
  ret15.io.predicateIn(0).bits.taskID := 0.U
  ret15.io.predicateIn(0).valid := true.B
  ret15.io.In.data("field0") <> load14.io.Out(param.ret15_in("load14"))
  io.out <> ret15.io.Out


}

import java.io.{File, FileWriter}
object test06Main extends App {
  val dir = new File("RTL/test06") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new test06DF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}

