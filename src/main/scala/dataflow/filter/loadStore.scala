package dataflow

import chisel3._
import chisel3.util._
import chisel3.Module
import chisel3.testers._
import chisel3.iotesters.{ChiselFlatSpec, Driver, OrderedDecoupledHWIOTester, PeekPokeTester}
import org.scalatest.{FlatSpec, Matchers}
import muxes._
import config._
import control.{BasicBlockNoMaskNode, BasicBlockNode}
import util._
import interfaces._
import regfile._
import memory._
import arbiters._
import node._

/**
  * This Object should be initialize at the first step
  * It contains all the transformation from indecies to their module's name
  */

object Data_copy_FlowParam{
  val unkonw_0_pred = Map(
    "active" -> 0
  )

  val unkonw_0_activate = Map(
    "m_0" -> 0,
    "m_1" -> 1,
    "m_2" -> 2,
    "m_3" -> 3,
    "m_4" -> 4,
    "m_5" -> 5,
    "m_6" -> 6,
    "m_7" -> 7,
    "m_8" -> 8,
    "m_9" -> 9,
    "m_10" -> 10
  )

  val m_0_in = Map( 
    "data_0" -> 0
  )

  val m_1_in = Map( 
    "m_0" -> 0,
    "data_1" -> 1
  )

  val m_2_in = Map( 
    "data_0" -> 0
  )

  val m_3_in = Map( 
    "m_2" -> 0
  )

  val m_4_in = Map( 
    "data_1" -> 0
  )

  val m_5_in = Map( 
    "m_3" -> 0,
    "m_4" -> 0
  )

  val m_6_in = Map( 
    "data_0" -> 0,
    "data_2" -> 1
  )

  val m_7_in = Map( 
    "m_6" -> 0
  )

  val m_8_in = Map( 
    "data_1" -> 0,
    "data_2" -> 1
  )

  val m_9_in = Map( 
    "m_7" -> 0,
    "m_8" -> 0
  )

  val m_10_in = Map(
  )

}




  /* ================================================================== *
   *                   PRINTING PORTS DEFINITION                        *
   * ================================================================== */

abstract class copyDFIO(implicit val p: Parameters) extends Module with CoreParams {
  val io = IO(new Bundle {
    val data_0 = Flipped(Decoupled(new DataBundle))
    val data_1 = Flipped(Decoupled(new DataBundle))
    val data_2 = Flipped(Decoupled(new DataBundle))
    val pred = Decoupled(new Bool())
    val start = Input(new Bool())
  })
}

class copyDF(implicit p: Parameters) extends copyDFIO()(p) {



  /* ================================================================== *
   *                   PRINTING MODULE DEFINITION                       *
   * ================================================================== */




  /* ================================================================== *
   *                   PRINTING BASICBLOCKS                             *
   * ================================================================== */

  //Initializing BasicBlocks: 
  val unkonw_0 = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 11, BID = 0)(p))





  /* ================================================================== *
   *                   PRINTING STACKFILE                               *
   * ================================================================== */

	val StackFile = Module(new TypeStackFile(ID=0,Size=32,NReads=3,NWrites=3)
		            (WControl=new WriteMemoryController(NumOps=3,BaseSize=2,NumEntries=2))
		            (RControl=new ReadMemoryController(NumOps=3,BaseSize=2,NumEntries=2)))


  /* ================================================================== *
   *                   PRINTING INSTURCTIONS                            *
   * ================================================================== */

  //Initializing Instructions: 
  // : 

  //  %1 = load i32, i32* %s, align 4, !UID !2
  val m_0 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=0,RouteID=0))

  //  store i32 %1, i32* %d, align 4, !UID !3
  val m_1 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=1,RouteID=0))

  //  %2 = getelementptr inbounds i32, i32* %s, i64 1, !UID !4
  val m_2 = Module (new GepOneNode(NumOuts = 1, ID = 2)(numByte1 = 4)(p))

  //  %3 = load i32, i32* %2, align 4, !UID !5
  val m_3 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=3,RouteID=1))

  //  %4 = getelementptr inbounds i32, i32* %d, i64 1, !UID !6
  val m_4 = Module (new GepOneNode(NumOuts = 1, ID = 4)(numByte1 = 4)(p))

  //  store i32 %3, i32* %4, align 4, !UID !7
  val m_5 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=5,RouteID=1))

  //  %5 = getelementptr inbounds i32, i32* %s, i64 %step, !UID !8
  val m_6 = Module (new GepOneNode(NumOuts = 1, ID = 6)(numByte1 = 0)(p))

  //  %6 = load i32, i32* %5, align 4, !UID !9
  val m_7 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=7,RouteID=2))

  //  %7 = getelementptr inbounds i32, i32* %d, i64 %step, !UID !10
  val m_8 = Module (new GepOneNode(NumOuts = 1, ID = 8)(numByte1 = 0)(p))

  //  store i32 %6, i32* %7, align 4, !UID !11
  val m_9 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=9,RouteID=2))

  //  ret void, !UID !12, !BB_UID !13
  //val m_10 = Module (new RetNode(ID = 10)(p))






  /* ================================================================== *
   *                   INITIALIZING PARAM                               *
   * ================================================================== */

  /**
    * Instantiating parameters
    */
  val param = Data_copy_FlowParam




  /* ================================================================== *
   *                   CONNECTING BASICBLOCKS TO PREDICATE INSTRUCTIONS *
   * ================================================================== */

  /**
     * Connecting basic blocks to predicate instructions
     */

  //We always ground entry BasicBlock
  unkonw_0.io.predicateIn(param.unkonw_0_pred("active")).bits  := ControlBundle.Activate
  unkonw_0.io.predicateIn(param.unkonw_0_pred("active")).valid := true.B

  /**
    * Connecting basic blocks to predicate instructions
    */

  // There is no branch insruction




  /* ================================================================== *
   *                   CONNECTING BASICBLOCKS TO INSTRUCTIONS           *
   * ================================================================== */

  /**
    * Wireing enable signals to the instructions
    */
  //Wiring enable signals
  m_0.io.enable <> unkonw_0.io.Out(param.unkonw_0_activate("m_0"))
  m_1.io.enable <> unkonw_0.io.Out(param.unkonw_0_activate("m_1"))
  m_2.io.enable <> unkonw_0.io.Out(param.unkonw_0_activate("m_2"))
  m_3.io.enable <> unkonw_0.io.Out(param.unkonw_0_activate("m_3"))
  m_4.io.enable <> unkonw_0.io.Out(param.unkonw_0_activate("m_4"))
  m_5.io.enable <> unkonw_0.io.Out(param.unkonw_0_activate("m_5"))
  m_6.io.enable <> unkonw_0.io.Out(param.unkonw_0_activate("m_6"))
  m_7.io.enable <> unkonw_0.io.Out(param.unkonw_0_activate("m_7"))
  m_8.io.enable <> unkonw_0.io.Out(param.unkonw_0_activate("m_8"))
  m_9.io.enable <> unkonw_0.io.Out(param.unkonw_0_activate("m_9"))
  //m_10.io.enable <> unkonw_0.io.Out(param.unkonw_0_activate("m_10"))




  /* ================================================================== *
   *                   DUMPING PHI NODES                                *
   * ================================================================== */

  /**
    * Connecting PHI nodes
    */
  //Connect PHI node
  //There is no PHI node




  /* ================================================================== *
   *                   DUMPING DATAFLOW                                 *
   * ================================================================== */

  /**
    * Connecting Dataflow signals
    */
  // Wiring Load instruction to the function argument
  m_0.io.GepAddr <> io.data_0
  m_0.io.memResp <> StackFile.io.ReadOut(0)
  StackFile.io.ReadIn(0) <> m_0.io.memReq


  // Wiring Store instruction to the parent instruction
  m_1.io.GepAddr <> m_0.io.Out(param.m_1_in("m_0"))
  m_1.io.inData <> io.data_1
  m_1.io.memResp  <> StackFile.io.WriteOut(0)
  StackFile.io.WriteIn(0) <> m_1.io.memReq
  m_1.io.Out(0).ready := true.B


  // Wiring GEP instruction to the function argument
  m_2.io.baseAddress <> io.data_0

  // Wiring GEP instruction to the Constant
  m_2.io.idx1.valid :=  true.B
  m_2.io.idx1.bits.predicate :=  true.B
  m_2.io.idx1.bits.data :=  1.U

  // Wiring Load instruction to the parent instruction
  m_3.io.GepAddr <> m_2.io.Out(param.m_3_in("m_2"))
  m_3.io.memResp  <> StackFile.io.ReadOut(1)
  StackFile.io.ReadIn(1) <> m_3.io.memReq


  // Wiring GEP instruction to the function argument
  m_4.io.baseAddress <> io.data_1

  // Wiring GEP instruction to the Constant
  m_4.io.idx1.valid :=  true.B
  m_4.io.idx1.bits.predicate :=  true.B
  m_4.io.idx1.bits.data :=  1.U

  // Wiring Store instruction to the parent instruction
  m_5.io.GepAddr <> m_3.io.Out(param.m_5_in("m_3"))
  m_5.io.inData <> m_4.io.Out(param.m_5_in("m_4"))
  m_5.io.memResp  <> StackFile.io.WriteOut(1)
  StackFile.io.WriteIn(1) <> m_5.io.memReq
  m_5.io.Out(0).ready := true.B


  // Wiring GEP instruction to the function argument
  m_6.io.baseAddress <> io.data_0

  // Wiring GEP instruction to the function argument
  m_6.io.idx1 <> io.data_2

  // Wiring Load instruction to the parent instruction
  m_7.io.GepAddr <> m_6.io.Out(param.m_7_in("m_6"))
  m_7.io.memResp  <> StackFile.io.ReadOut(2)
  StackFile.io.ReadIn(2) <> m_7.io.memReq


  // Wiring GEP instruction to the function argument
  m_8.io.baseAddress <> io.data_1

  // Wiring GEP instruction to the function argument
  m_8.io.idx1 <> io.data_2

  // Wiring Store instruction to the parent instruction
  m_9.io.GepAddr <> m_7.io.Out(param.m_9_in("m_7"))
  m_9.io.inData <> m_8.io.Out(param.m_9_in("m_8"))
  m_9.io.memResp  <> StackFile.io.WriteOut(2)
  StackFile.io.WriteIn(2) <> m_9.io.memReq
  m_9.io.Out(0).ready := true.B


}
