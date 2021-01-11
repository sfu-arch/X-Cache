package dandelion.memory.cache

import chisel3._
import chisel3.util._
import chipsalliance.rocketchip.config._
import dandelion.config._
import dandelion.util._
import dandelion.interfaces._
import dandelion.interfaces.ActionBundle
//import dandelion.memory.TBE
import dandelion.interfaces.axi._
import chisel3.util.experimental.loadMemoryFromFile
//import dandelion.memory.TBE.{ TBETable, TBE}



class programmableCacheIO (implicit val p:Parameters) extends Bundle
with HasCacheAccelParams
with HasAccelShellParams {

    val instruction = Flipped(Decoupled(new InstBundle))
}

class programmableCache (implicit val p:Parameters) extends Module
with HasCacheAccelParams
with HasAccelShellParams{

    val io = IO(new programmableCacheIO())

    val cache = Module(new Gem5Cache())
    val tbe = Module(new TBETable())
    val state = Wire(UInt(stateLen.W))

    val inputTBE = WireInit(TBE.default)




    val routineAddr = RoutinePtr.generateRoutineAddrMap(RoutineROM.routineActions)
    val (rombits, dstStateBits) = RoutinePtr.generateTriggerRoutineBit(routineAddr, nextRoutine.routineTriggerList)

    val uCodedNextPtr = VecInit(rombits)
    val dstStateRom = VecInit(dstStateBits)

    val actions = RoutinePtr.generateActionRom(RoutineROM.routineActions, ActionList.actions)
    val actionRom = VecInit(actions)

    val addr = RegInit(0.U(addrLen.W))
    val event = RegInit(0.U(eventLen.W))

    val tbeAction = Wire(UInt(nSigs.W))
    val cacheAction = Wire(UInt(nSigs.W))
    val isCacheAction = Wire(Bool())

    val readTBE = Reg(Bool())
    val pc = Reg(UInt())
    val action = Wire(new ActionBundle())

//    val tbeRdRdy = Wire(Bool())
    val tbeResValid = Wire(Bool())
    val routineAddrResValid = Reg(Bool())
    val actionResValid = Reg(Bool())

    val routineStart = Wire(Bool())

    val defaultState = Wire(new State())
    val dstState = Wire(new State())

    val startOfRoutine = Wire(Bool())


    val isProbe = Wire(Bool())
    val routine = WireInit( Cat (event, state))

    val (probeHandled, _) = Counter(isProbe, 2)

    val targetWay = RegInit(nWays.U((wayLen+1).W))

    defaultState := State.default
    io.instruction.ready := true.B

    when(io.instruction.fire()){
        // latching
        addr := io.instruction.bits.addr
        event := io.instruction.bits.event
    }

    when (cache.io.cpu.resp.fire()){
        targetWay := cache.io.cpu.resp.bits.way
    }

    readTBE := io.instruction.fire()

    dstState.state := dstStateRom(routine)
    inputTBE.state.state :=  Mux(actionResValid & !isCacheAction, dstState.state, State.default.state)  // @todo should be changed
    inputTBE.way := targetWay //@todo should be changed to the way calculated by the same logic which is in charge of probing and resource chekcing

    tbe.io.command := Mux (readTBE, tbe.read, tbeAction )
    tbe.io.addr := addr
    tbe.io.inputTBE := inputTBE

    tbe.io.outputTBE.ready := true.B

    tbeResValid := (readTBE)
    routineAddrResValid := (tbeResValid)
    routineStart := tbeResValid

    isProbe := (action.signals === ActionList.actions("Probe").asUInt()(nSigs-1 , 0))

    actionResValid := Mux(routineAddrResValid, true.B , Mux (startOfRoutine, false.B, Mux(isProbe, (probeHandled.asBool()), true.B)))

    state := Mux(tbe.io.outputTBE.valid, tbe.io.outputTBE.bits.state.state, defaultState.state )

    pc := Mux(routineStart, uCodedNextPtr(routine), Mux(startOfRoutine, pc,  Mux(isProbe, pc + probeHandled.asUInt(), pc+ 1.U  )))

    action.signals := actionRom(pc)(nSigs -1,0)
    action.isCacheAction := actionRom(pc)(nSigs)

    startOfRoutine := (action.signals === 0.U & action.isCacheAction === 0.U)
    isCacheAction := (action.isCacheAction === true.B)

    tbeAction := Mux(!isCacheAction, action.signals, tbe.idle )
    cacheAction := Mux(isCacheAction, action.signals, 0.U(nSigs.W))

    cache.io.cpu <> DontCare
    cache.io.mem <> DontCare

    cache.io.cpu.req.bits.command := cacheAction
    cache.io.cpu.req.bits.addr := addr
    cache.io.cpu.req.valid := actionResValid



}