//package dandelion.memory.cache
//
//import chipsalliance.rocketchip.config._
//import chisel3._
//import dandelion.config._
//import dandelion.memory.cache.{HasCacheAccelParams, State}
//import chisel3.util._
//import chisel3.util.Enum
//
//
//class TBEControllerIO (implicit val p: Parameters) extends Bundle
//  with HasCacheAccelParams
//  with HasAccelShellParams {
//
//    val command = Input (UInt(nCom.W))
//    val inputTBE= Input(new TBE)
//    val outputTBE = Output(new TBE)
//
//}
//
//class TBEController(implicit  val p: Parameters) extends Module
//  with HasCacheAccelParams
//  with HasAccelShellParams {
//
//
//    val routineAddr = RoutinePtr.generateRoutineAddrMap(RoutineROM.routineActions)
//    val rombits = RoutinePtr.generateTriggerRoutineBit(routineAddr, nextRoutine.routineTriggerList)
//
//
//    val uCodedNextPtr = VecInit(rombits)
//
//
//    val cNone :: cAlloc :: cDeAlloc :: cGetState :: cSetState :: cIntRead :: cWrite::Nil = Enum(nCom)
//    val none :: wayFail :: metaFail :: Nil = Enum(3)
//
//    val io = IO(new TBEControllerIO())
//
//    val way = WireInit(io.inputTBE.way)
//    val addr = WireInit(io.inputTBE.addr)
//    val state = WireInit(io.inputTBE.state.state)
//    val cmd = WireInit(io.command)
//
//    val nextCmd = Wire(UInt(nCom.W))
//
//    io.outputTBE.way := way
//    io.outputTBE.addr := addr
//    io.outputTBE.state.state := state
//    io.outputTBE.data := io.inputTBE.data
//
//    io.outputTBE.cmdPtr := nextCmd
//
//    //  val switchVal = WireInit(Cat(cmd , state.state))
//
//    //  val allocFailed = Cat (cAlloc, wayFail)
//
//    //  switch(switchVal){
//    //    //@todo Should be completed
//    //    is (Cat (cAlloc, wayFail)){
//    //      nextCmd := replPolicy
//    //    }
//    //    is (Cat (cIntRead, metaFail))
//    //    {
//    //       nextCmd := cAlloc
//    //    }
//    //
//    ////    is (Cat (cAlloc, hit))
//    ////    {
//    ////      nextCmd :=
//    ////    }
//    //
//    //  }
//
//
//}