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

object Data_cilk_for_test05_detach_FlowParam{

  val bb_my_pfor_body_pred = Map(
    "active" -> 0
  )


  val bb_my_if_end_pred = Map(
    "br13" -> 0,
    "br23" -> 1
  )


  val bb_my_if_then_pred = Map(
    "br5" -> 0
  )


  val bb_my_if_else_pred = Map(
    "br5" -> 0
  )


  val bb_my_pfor_preattach_pred = Map(
    "br14" -> 0
  )


  val br5_brn_bb = Map(
    "bb_my_if_then" -> 0,
    "bb_my_if_else" -> 1
  )


  val br13_brn_bb = Map(
    "bb_my_if_end" -> 0
  )


  val br14_brn_bb = Map(
    "bb_my_pfor_preattach" -> 0
  )


  val br23_brn_bb = Map(
    "bb_my_if_end" -> 0
  )


  val bb_my_pfor_body_activate = Map(
    "getelementptr0" -> 0,
    "load1" -> 1,
    "getelementptr2" -> 2,
    "load3" -> 3,
    "icmp4" -> 4,
    "br5" -> 5
  )


  val bb_my_if_then_activate = Map(
    "getelementptr6" -> 0,
    "load7" -> 1,
    "getelementptr8" -> 2,
    "load9" -> 3,
    "sub10" -> 4,
    "getelementptr11" -> 5,
    "store12" -> 6,
    "br13" -> 7
  )


  val bb_my_if_end_activate = Map(
    "br14" -> 0
  )


  val bb_my_pfor_preattach_activate = Map(
    "ret15" -> 0
  )


  val bb_my_if_else_activate = Map(
    "getelementptr16" -> 0,
    "load17" -> 1,
    "getelementptr18" -> 2,
    "load19" -> 3,
    "sub20" -> 4,
    "getelementptr21" -> 5,
    "store22" -> 6,
    "br23" -> 7
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


  //  %4 = icmp ugt i32 %1, %3, !UID !15, !ScalaLabel !16
  val icmp4_in = Map(
    "load1" -> 0,
    "load3" -> 0
  )


  //  br i1 %4, label %my_if.then, label %my_if.else, !UID !17, !BB_UID !18, !ScalaLabel !19
  val br5_in = Map(
    "icmp4" -> 0
  )


  //  %5 = getelementptr inbounds i32, i32* %a.in, i32 %i.0.in, !UID !20, !ScalaLabel !21
  val getelementptr6_in = Map(
    "field0" -> 1,
    "field1" -> 2
  )


  //  %6 = load i32, i32* %5, align 4, !UID !22, !ScalaLabel !23
  val load7_in = Map(
    "getelementptr6" -> 0
  )


  //  %7 = getelementptr inbounds i32, i32* %b.in, i32 %i.0.in, !UID !24, !ScalaLabel !25
  val getelementptr8_in = Map(
    "field2" -> 1,
    "field1" -> 3
  )


  //  %8 = load i32, i32* %7, align 4, !UID !26, !ScalaLabel !27
  val load9_in = Map(
    "getelementptr8" -> 0
  )


  //  %9 = sub i32 %6, %8, !UID !28, !ScalaLabel !29
  val sub10_in = Map(
    "load7" -> 0,
    "load9" -> 0
  )


  //  %10 = getelementptr inbounds i32, i32* %c.in, i32 %i.0.in, !UID !30, !ScalaLabel !31
  val getelementptr11_in = Map(
    "field3" -> 0,
    "field1" -> 4
  )


  //  store i32 %9, i32* %10, align 4, !UID !32, !ScalaLabel !33
  val store12_in = Map(
    "sub10" -> 0,
    "getelementptr11" -> 0
  )


  //  ret void, !UID !40, !BB_UID !41, !ScalaLabel !42
  val ret15_in = Map(

  )


  //  %11 = getelementptr inbounds i32, i32* %b.in, i32 %i.0.in, !UID !43, !ScalaLabel !44
  val getelementptr16_in = Map(
    "field2" -> 2,
    "field1" -> 5
  )


  //  %12 = load i32, i32* %11, align 4, !UID !45, !ScalaLabel !46
  val load17_in = Map(
    "getelementptr16" -> 0
  )


  //  %13 = getelementptr inbounds i32, i32* %a.in, i32 %i.0.in, !UID !47, !ScalaLabel !48
  val getelementptr18_in = Map(
    "field0" -> 2,
    "field1" -> 6
  )


  //  %14 = load i32, i32* %13, align 4, !UID !49, !ScalaLabel !50
  val load19_in = Map(
    "getelementptr18" -> 0
  )


  //  %15 = sub i32 %12, %14, !UID !51, !ScalaLabel !52
  val sub20_in = Map(
    "load17" -> 0,
    "load19" -> 0
  )


  //  %16 = getelementptr inbounds i32, i32* %c.in, i32 %i.0.in, !UID !53, !ScalaLabel !54
  val getelementptr21_in = Map(
    "field3" -> 1,
    "field1" -> 7
  )


  //  store i32 %15, i32* %16, align 4, !UID !55, !ScalaLabel !56
  val store22_in = Map(
    "sub20" -> 0,
    "getelementptr21" -> 0
  )


}




  /* ================================================================== *
   *                   PRINTING PORTS DEFINITION                        *
   * ================================================================== */


abstract class cilk_for_test05_detachDFIO(implicit val p: Parameters) extends Module with CoreParams {
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


class cilk_for_test05_detachDF(implicit p: Parameters) extends cilk_for_test05_detachDFIO()(p) {



  /* ================================================================== *
   *                   PRINTING MEMORY SYSTEM                           *
   * ================================================================== */


	val StackPointer = Module(new Stack(NumOps = 1))

	val RegisterFile = Module(new TypeStackFile(ID=0,Size=32,NReads=6,NWrites=2)
		            (WControl=new WriteMemoryController(NumOps=2,BaseSize=2,NumEntries=2))
		            (RControl=new ReadMemoryController(NumOps=6,BaseSize=2,NumEntries=2)))

	val CacheMem = Module(new UnifiedController(ID=0,Size=32,NReads=6,NWrites=2)
		            (WControl=new WriteMemoryController(NumOps=2,BaseSize=2,NumEntries=2))
		            (RControl=new ReadMemoryController(NumOps=6,BaseSize=2,NumEntries=2))
		            (RWArbiter=new ReadWriteArbiter()))

  io.CacheReq <> CacheMem.io.CacheReq
  CacheMem.io.CacheResp <> io.CacheResp

  val InputSplitter = Module(new SplitCall(List(32,32,32,32)))
  InputSplitter.io.In <> io.in

  // Manually added
  val field0_expand = Module(new ExpandNode(NumOuts=3,ID=100)(new DataBundle))
  field0_expand.io.enable.valid := true.B
  field0_expand.io.enable.bits.control := true.B
  field0_expand.io.InData <> InputSplitter.io.Out.data("field0")

  // Manually added
  val field1_expand = Module(new ExpandNode(NumOuts=8,ID=101)(new DataBundle))
  field1_expand.io.enable.valid := true.B
  field1_expand.io.enable.bits.control := true.B
  field1_expand.io.InData <> InputSplitter.io.Out.data("field1")


  /* ================================================================== *
   *                   PRINTING LOOP HEADERS                            *
   * ================================================================== */


  //Function doesn't have any loop


  /* ================================================================== *
   *                   PRINTING BASICBLOCK NODES                        *
   * ================================================================== */


  //Initializing BasicBlocks: 

  val bb_my_pfor_body = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 2, BID = 0))

  val bb_my_if_then = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 3, BID = 1))

  val bb_my_if_end = Module(new BasicBlockNoMaskNode(NumInputs = 2, NumOuts = 1, BID = 2))

  val bb_my_pfor_preattach = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 1, BID = 3))

  val bb_my_if_else = Module(new BasicBlockNoMaskNode(NumInputs = 1, NumOuts = 3, BID = 4))






  /* ================================================================== *
   *                   PRINTING INSTRUCTION NODES                       *
   * ================================================================== */


  //Initializing Instructions: 

  // [BasicBlock]  my_pfor.body:

  //  %0 = getelementptr inbounds i32, i32* %a.in, i32 %i.0.in, !UID !7, !ScalaLabel !8
  val getelementptr0 = Module (new GepOneNode(NumOuts = 1, ID = 0)(numByte1 = 1))


  //  %1 = load i32, i32* %0, align 4, !UID !9, !ScalaLabel !10
  val load1 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=1,RouteID=0))


  //  %2 = getelementptr inbounds i32, i32* %b.in, i32 %i.0.in, !UID !11, !ScalaLabel !12
  val getelementptr2 = Module (new GepOneNode(NumOuts = 1, ID = 2)(numByte1 = 1))


  //  %3 = load i32, i32* %2, align 4, !UID !13, !ScalaLabel !14
  val load3 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=3,RouteID=1))


  //  %4 = icmp ugt i32 %1, %3, !UID !15, !ScalaLabel !16
  val icmp4 = Module (new IcmpNode(NumOuts = 1, ID = 4, opCode = "UGT")(sign=false))


  //  br i1 %4, label %my_if.then, label %my_if.else, !UID !17, !BB_UID !18, !ScalaLabel !19
  val br5 = Module (new CBranchNode(ID = 5))

  // [BasicBlock]  my_if.then:

  //  %5 = getelementptr inbounds i32, i32* %a.in, i32 %i.0.in, !UID !20, !ScalaLabel !21
  val getelementptr6 = Module (new GepOneNode(NumOuts = 1, ID = 6)(numByte1 = 1))


  //  %6 = load i32, i32* %5, align 4, !UID !22, !ScalaLabel !23
  val load7 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=7,RouteID=2))


  //  %7 = getelementptr inbounds i32, i32* %b.in, i32 %i.0.in, !UID !24, !ScalaLabel !25
  val getelementptr8 = Module (new GepOneNode(NumOuts = 1, ID = 8)(numByte1 = 1))


  //  %8 = load i32, i32* %7, align 4, !UID !26, !ScalaLabel !27
  val load9 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=9,RouteID=3))


  //  %9 = sub i32 %6, %8, !UID !28, !ScalaLabel !29
  val sub10 = Module (new ComputeNode(NumOuts = 1, ID = 10, opCode = "sub")(sign=false))


  //  %10 = getelementptr inbounds i32, i32* %c.in, i32 %i.0.in, !UID !30, !ScalaLabel !31
  val getelementptr11 = Module (new GepOneNode(NumOuts = 1, ID = 11)(numByte1 = 1))


  //  store i32 %9, i32* %10, align 4, !UID !32, !ScalaLabel !33
  val store12 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=12,RouteID=0))


  //  br label %my_if.end, !UID !34, !BB_UID !35, !ScalaLabel !36
  val br13 = Module (new UBranchFastNode(ID = 13))

  // [BasicBlock]  my_if.end:

  //  br label %my_pfor.preattach, !UID !37, !BB_UID !38, !ScalaLabel !39
  val br14 = Module (new UBranchFastNode(ID = 14))

  // [BasicBlock]  my_pfor.preattach:

  //  ret void, !UID !40, !BB_UID !41, !ScalaLabel !42
  val ret15 = Module(new RetNode(retTypes=List(32), ID=15))

  // [BasicBlock]  my_if.else:

  //  %11 = getelementptr inbounds i32, i32* %b.in, i32 %i.0.in, !UID !43, !ScalaLabel !44
  val getelementptr16 = Module (new GepOneNode(NumOuts = 1, ID = 16)(numByte1 = 1))


  //  %12 = load i32, i32* %11, align 4, !UID !45, !ScalaLabel !46
  val load17 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=17,RouteID=4))


  //  %13 = getelementptr inbounds i32, i32* %a.in, i32 %i.0.in, !UID !47, !ScalaLabel !48
  val getelementptr18 = Module (new GepOneNode(NumOuts = 1, ID = 18)(numByte1 = 1))


  //  %14 = load i32, i32* %13, align 4, !UID !49, !ScalaLabel !50
  val load19 = Module(new UnTypLoad(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=19,RouteID=5))


  //  %15 = sub i32 %12, %14, !UID !51, !ScalaLabel !52
  val sub20 = Module (new ComputeNode(NumOuts = 1, ID = 20, opCode = "sub")(sign=false))


  //  %16 = getelementptr inbounds i32, i32* %c.in, i32 %i.0.in, !UID !53, !ScalaLabel !54
  val getelementptr21 = Module (new GepOneNode(NumOuts = 1, ID = 21)(numByte1 = 1))


  //  store i32 %15, i32* %16, align 4, !UID !55, !ScalaLabel !56
  val store22 = Module(new UnTypStore(NumPredOps=0, NumSuccOps=0, NumOuts=1,ID=22,RouteID=1))


  //  br label %my_if.end, !UID !57, !BB_UID !58, !ScalaLabel !59
  val br23 = Module (new UBranchFastNode(ID = 23))





  /* ================================================================== *
   *                   INITIALIZING PARAM                               *
   * ================================================================== */


  /**
    * Instantiating parameters
    */
  val param = Data_cilk_for_test05_detach_FlowParam



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

  //Connecting br5 to bb_my_if_then
  bb_my_if_then.io.predicateIn <> br5.io.Out(param.br5_brn_bb("bb_my_if_then"))


  //Connecting br5 to bb_my_if_else
  bb_my_if_else.io.predicateIn <> br5.io.Out(param.br5_brn_bb("bb_my_if_else"))


  //Connecting br13 to bb_my_if_end
  bb_my_if_end.io.predicateIn <> br13.io.Out(param.br13_brn_bb("bb_my_if_end"))


  //Connecting br14 to bb_my_pfor_preattach
  bb_my_pfor_preattach.io.predicateIn <> br14.io.Out(param.br14_brn_bb("bb_my_pfor_preattach"))


  //Connecting br23 to bb_my_if_end
  bb_my_if_end.io.predicateIn <> br23.io.Out(param.br23_brn_bb("bb_my_if_end"))



  // There is no detach instruction




  /* ================================================================== *
   *                   CONNECTING BASIC BLOCKS TO INSTRUCTIONS          *
   * ================================================================== */


  /**
    * Wiring enable signals to the instructions
    */

  getelementptr0.io.enable <> bb_my_pfor_body.io.Out(0)
  getelementptr2.io.enable <> bb_my_pfor_body.io.Out(1)
  getelementptr6.io.enable <> bb_my_if_then.io.Out(0)
  getelementptr8.io.enable <> bb_my_if_then.io.Out(1)
  getelementptr11.io.enable <> bb_my_if_then.io.Out(2)
  getelementptr16.io.enable <> bb_my_if_else.io.Out(0)
  getelementptr18.io.enable <> bb_my_if_else.io.Out(1)
  getelementptr21.io.enable <> bb_my_if_else.io.Out(2)

  load1.io.enable.valid <> true.B
  load3.io.enable.valid <> true.B
  icmp4.io.enable.valid <> true.B
  br5.io.enable.valid <> true.B
  load1.io.enable.bits.control <> true.B
  load3.io.enable.bits.control <> true.B
  icmp4.io.enable.bits.control <> true.B
  br5.io.enable.bits.control <> true.B

  load7.io.enable.valid <> true.B
  load9.io.enable.valid <> true.B
  sub10.io.enable.valid <> true.B
  store12.io.enable.valid <> true.B
  br13.io.enable.valid <> true.B
  load7.io.enable.bits.control <> true.B
  load9.io.enable.bits.control <> true.B
  sub10.io.enable.bits.control <> true.B
  store12.io.enable.bits.control <> true.B
  br13.io.enable.bits.control <> true.B

  br14.io.enable.valid <> true.B
  ret15.io.enable.valid <> true.B
  load17.io.enable.valid <> true.B
  load19.io.enable.valid <> true.B
  sub20.io.enable.valid <> true.B
  store22.io.enable.valid <> true.B
  br23.io.enable.valid <> true.B
  br14.io.enable.bits.control <> true.B
  ret15.io.enable.bits.control <> true.B
  load17.io.enable.bits.control <> true.B
  load19.io.enable.bits.control <> true.B
  sub20.io.enable.bits.control <> true.B
  store22.io.enable.bits.control <> true.B
  br23.io.enable.bits.control <> true.B





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
  getelementptr0.io.baseAddress <> field0_expand.io.Out(0)


  // Wiring GEP instruction to the function argument
  getelementptr0.io.idx1 <> field1_expand.io.Out(0)


  // Wiring Load instruction to the parent instruction
  load1.io.GepAddr <> getelementptr0.io.Out(param.load1_in("getelementptr0"))
  load1.io.memResp <> RegisterFile.io.ReadOut(0)
  RegisterFile.io.ReadIn(0) <> load1.io.memReq




  // Wiring GEP instruction to the function argument
  getelementptr2.io.baseAddress <> InputSplitter.io.Out.data("field2")


  // Wiring GEP instruction to the function argument
  getelementptr2.io.idx1 <> field1_expand.io.Out(1)


  // Wiring Load instruction to the parent instruction
  load3.io.GepAddr <> getelementptr2.io.Out(param.load3_in("getelementptr2"))
  load3.io.memResp <> RegisterFile.io.ReadOut(1)
  RegisterFile.io.ReadIn(1) <> load3.io.memReq




  // Wiring instructions
  icmp4.io.LeftIO <> load1.io.Out(param.icmp4_in("load1"))

  // Wiring instructions
  icmp4.io.RightIO <> load3.io.Out(param.icmp4_in("load3"))

  // Wiring Branch instruction
  br5.io.CmpIO <> icmp4.io.Out(param.br5_in("icmp4"))

  // Wiring GEP instruction to the function argument
  getelementptr6.io.baseAddress <> field0_expand.io.Out(1)


  // Wiring GEP instruction to the function argument
  getelementptr6.io.idx1 <> field1_expand.io.Out(2)


  // Wiring Load instruction to the parent instruction
  load7.io.GepAddr <> getelementptr6.io.Out(param.load7_in("getelementptr6"))
  load7.io.memResp <> RegisterFile.io.ReadOut(2)
  RegisterFile.io.ReadIn(2) <> load7.io.memReq




  // Wiring GEP instruction to the function argument
  getelementptr8.io.baseAddress <> InputSplitter.io.Out.data("field2")


  // Wiring GEP instruction to the function argument
  getelementptr8.io.idx1 <> field1_expand.io.Out(3)


  // Wiring Load instruction to the parent instruction
  load9.io.GepAddr <> getelementptr8.io.Out(param.load9_in("getelementptr8"))
  load9.io.memResp <> RegisterFile.io.ReadOut(3)
  RegisterFile.io.ReadIn(3) <> load9.io.memReq




  // Wiring instructions
  sub10.io.LeftIO <> load7.io.Out(param.sub10_in("load7"))

  // Wiring instructions
  sub10.io.RightIO <> load9.io.Out(param.sub10_in("load9"))

  // Wiring GEP instruction to the function argument
  getelementptr11.io.baseAddress <> InputSplitter.io.Out.data("field3")


  // Wiring GEP instruction to the function argument
  getelementptr11.io.idx1 <> field1_expand.io.Out(4)


  store12.io.inData <> sub10.io.Out(param.store12_in("sub10"))



  // Wiring Store instruction to the parent instruction
  store12.io.GepAddr <> getelementptr11.io.Out(param.store12_in("getelementptr11"))
  store12.io.memResp  <> RegisterFile.io.WriteOut(0)
  RegisterFile.io.WriteIn(0) <> store12.io.memReq
  store12.io.Out(0).ready := true.B



  /**
    * Connecting Dataflow signals
    */
  
  
  
  ret15.io.In.data("field0").bits.data := 1.U
  ret15.io.In.data("field0").bits.predicate := true.B
  ret15.io.In.data("field0").valid := store12.io.Out(0).valid || store22.io.Out(0).valid  // manual hack
  io.out <> ret15.io.Out


  // Wiring GEP instruction to the function argument
  getelementptr16.io.baseAddress <> InputSplitter.io.Out.data("field2")


  // Wiring GEP instruction to the function argument
  getelementptr16.io.idx1 <> field1_expand.io.Out(5)


  // Wiring Load instruction to the parent instruction
  load17.io.GepAddr <> getelementptr16.io.Out(param.load17_in("getelementptr16"))
  load17.io.memResp <> RegisterFile.io.ReadOut(4)
  RegisterFile.io.ReadIn(4) <> load17.io.memReq




  // Wiring GEP instruction to the function argument
  getelementptr18.io.baseAddress <> field0_expand.io.Out(2)


  // Wiring GEP instruction to the function argument
  getelementptr18.io.idx1 <> field1_expand.io.Out(6)


  // Wiring Load instruction to the parent instruction
  load19.io.GepAddr <> getelementptr18.io.Out(param.load19_in("getelementptr18"))
  load19.io.memResp <> RegisterFile.io.ReadOut(5)
  RegisterFile.io.ReadIn(5) <> load19.io.memReq




  // Wiring instructions
  sub20.io.LeftIO <> load17.io.Out(param.sub20_in("load17"))

  // Wiring instructions
  sub20.io.RightIO <> load19.io.Out(param.sub20_in("load19"))

  // Wiring GEP instruction to the function argument
  getelementptr21.io.baseAddress <> InputSplitter.io.Out.data("field3")


  // Wiring GEP instruction to the function argument
  getelementptr21.io.idx1 <> field1_expand.io.Out(7)


  store22.io.inData <> sub20.io.Out(param.store22_in("sub20"))



  // Wiring Store instruction to the parent instruction
  store22.io.GepAddr <> getelementptr21.io.Out(param.store22_in("getelementptr21"))
  store22.io.memResp  <> RegisterFile.io.WriteOut(1)
  RegisterFile.io.WriteIn(1) <> store22.io.memReq
  store22.io.Out(0).ready := true.B



}

import java.io.{File, FileWriter}
object cilk_for_test05_detachMain extends App {
  val dir = new File("RTL/cilk_for_test05_detach") ; dir.mkdirs
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)
  val chirrtl = firrtl.Parser.parse(chisel3.Driver.emit(() => new cilk_for_test05_detachDF()))

  val verilogFile = new File(dir, s"${chirrtl.main}.v")
  val verilogWriter = new FileWriter(verilogFile)
  val compileResult = (new firrtl.VerilogCompiler).compileAndEmit(firrtl.CircuitState(chirrtl, firrtl.ChirrtlForm))
  val compiledStuff = compileResult.getEmittedCircuit
  verilogWriter.write(compiledStuff.value)
  verilogWriter.close()
}

