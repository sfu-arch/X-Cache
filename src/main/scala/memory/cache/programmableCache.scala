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
    val lockMem = Module(new lockVector())
    val stateMem = Module (new stateMem())

    stateMem.io <> DontCare

    val state = Wire(UInt(stateLen.W))

    cache.io.cpu <> DontCare
    cache.io.mem <> DontCare

    val inputTBE = WireInit(TBE.default)

    val (routineAddr,setStateDst) = RoutinePtr.generateRoutineAddrMap(RoutineROM.routineActions)
    val (rombits, dstStateBits) = RoutinePtr.generateTriggerRoutineBit(routineAddr, nextRoutine.routineTriggerList)

    val uCodedNextPtr = VecInit(rombits)
    val dstStateRom = VecInit(dstStateBits)

    val actions = RoutinePtr.generateActionRom(RoutineROM.routineActions, ActionList.actions, setStateDst)
    val actionRom = VecInit(actions)

    val addr = RegInit(0.U(addrLen.W))
    val event = RegInit(0.U(eventLen.W))

    val tbeAction = Wire(UInt(nSigs.W))
    val cacheAction = Wire(UInt(nSigs.W))
    val stateAction = Wire(Bool())

    val isTBEAction = Wire(Bool())
    val isStateAction = Wire(Bool())
    val isCacheAction = Wire(Bool())

    val readTBE = Reg(Bool())
    val checkLock = Reg(Bool())
    val isLocked = Wire(Bool())

    val pc = Reg(UInt())
    val action = Wire(new ActionBundle())

    val tbeResValid = Wire(Bool())
    val routineAddrResValid = Reg(Bool())
    val actionResValid = Reg(Bool())

    val routineStart = Reg(Bool())

    val defaultState = Wire(new State())
//    val dstState = Reg(new State())

    val startOfRoutine = Wire(Bool())
    val endOfRoutine = Wire(Bool())

    val isProbe = Wire(Bool())
    val routine = WireInit( Cat (event, state))

    val (probeHandled, _) = Counter(isProbe, 2)

    val cacheWayReg = RegInit(nWays.U((wayLen+1).W))
    val cacheWayWire = Wire(UInt((wayLen+1).W))

    val wayInputCache = Reg(UInt((wayLen+1).W))
    val tbeWay = RegInit(nWays.U((wayLen+1).W))

    val routineReg = RegInit(0.U((eventLen + stateLen).W))
    val actionReg = RegInit(0.U((nSigs + 2).W))

    val dstOfSetState = Wire(new State())
    val getState = Reg(Bool())
    val stateMemOutput = Wire((new State()))

    val probeStart = WireInit(io.instruction.fire())

    val addrWire   = WireInit(io.instruction.bits.addr)
    val addrCapturedReg  = RegNext(RegNext(addr))

    val updateTBEWay = Wire(Bool())
    val updateTBEState = Wire(Bool())

    defaultState := State.default
    io.instruction.ready := true.B

    cache.io.cpu.resp.ready := true.B

    //latching
    when(io.instruction.fire()){
        addr := io.instruction.bits.addr
        event := io.instruction.bits.event
    }

    when (cache.io.cpu.resp.fire()){
        cacheWayReg := cache.io.cpu.resp.bits.way
    }
    cacheWayWire := cache.io.cpu.resp.bits.way

    when(tbe.io.outputTBE.fire()){
        tbeWay := tbe.io.outputTBE.bits.way
    }

    readTBE := io.instruction.fire()
    checkLock := io.instruction.fire()
    getState := io.instruction.fire()



    lockMem.io.lock.in.bits.data := DontCare
    lockMem.io.unLock.in.bits.data := DontCare

    lockMem.io.lock.in.bits.addr  := addr
    lockMem.io.lock.in.valid := checkLock
    lockMem.io.lock.in.bits.cmd := true.B // checking and locking
    isLocked := Mux(lockMem.io.lock.out.valid, lockMem.io.lock.out.bits, false.B)

    lockMem.io.unLock.in.bits.addr := addrCapturedReg
    lockMem.io.unLock.in.bits.cmd  := false.B   //unlock
    lockMem.io.unLock.in.valid     := endOfRoutine


    val tbeActionInRom = WireInit(actionResValid & isTBEAction)
    val updateTBEWay = WireInit(RegNext(cache.io.cpu.resp.fire()) & (cacheWayReg =/= nWays.U))

    dstState.state := dstStateRom(routine)
    inputTBE.state.state :=  Mux(tbeActionInRom, dstState.state, DontCare)
    inputTBE.way := Mux(updateTBEWay , cacheWayReg, DontCare)

    tbe.io.command := Mux (readTBE, tbe.read, Mux( updateTBEWay, tbe.write, tbeAction))
    tbe.io.addr := addr
    tbe.io.inputTBE := inputTBE
    tbe.io.mask := Mux(updateTBEWay, tbe.maskWay, Mux(tbeActionInRom, tbe.maskState, tbe.maskAll))

    tbe.io.outputTBE.ready := true.B

    updateTBEWay   := (RegNext(cache.io.cpu.resp.fire()) & (cacheWayReg =/= nWays.U))
    updateTBEState := isStateAction
//    dstState.state := dstStateRom(routine)
    tbeResValid := (readTBE)
    routineAddrResValid := (tbeResValid)
    routineStart := tbeResValid

    isProbe := (action.signals === sigToAction(ActionList.actions("Probe")))

    actionResValid := Mux(routineAddrResValid, true.B , Mux (startOfRoutine, false.B, Mux(isProbe, (probeHandled.asBool()), true.B)))

    state := Mux(tbe.io.outputTBE.valid, tbe.io.outputTBE.bits.state.state, Mux(stateMem.io.out.valid, stateMem.io.out.bits.state, defaultState.state )) // stateMem should be added

    routineReg := uCodedNextPtr(routine)
    actionReg := actionRom(pc)

    pc := Mux(routineStart, routineReg, Mux(startOfRoutine, pc,  Mux(isProbe, pc + probeHandled.asUInt(), pc+ 1.U  )))

    wayInputCache := Mux( tbeWay === nWays.U , cacheWayReg, tbeWay )

    printf(p"way ${wayInputCache}\n")
    action.signals := sigToAction(actionReg)
    action.actionType := actionReg(nSigs+ 1, nSigs)


    startOfRoutine := (sigToAction(actionRom(pc)) === 0.U & actionRom(pc)(nSigs) === 0.U & actionRom(pc)(nSigs + 1) === 0.U)
    endOfRoutine   := isStateAction

    isTBEAction   := (action.actionType === 0.U)
    isStateAction := (action.actionType === 2.U | action.actionType === 3.U )
    isCacheAction := (action.actionType === 1.U)



    tbeAction := Mux(isTBEAction, action.signals, tbe.idle )
    cacheAction := Mux(isCacheAction, action.signals, 0.U(nSigs.W))
    stateAction := isStateAction

    actionState.state := sigToState (actionReg)

    stateMem.io.in.bits.isSet := Mux(getState, false.B, stateAction)
    stateMem.io.in.bits.addr := addr
    stateMem.io.in.bits.state := actionState
    stateMem.io.in.bits.way := cacheWayWire
    stateMem.io.in.valid := isStateAction | getState

    stateMemOutput := stateMem.io.out.bits
    
    cache.io.cpu.req.bits.way := RegNext(wayInputCache)
    cache.io.cpu.req.bits.command := Mux(probeStart, sigToAction(ActionList.actions("Probe")), cacheAction)
    cache.io.cpu.req.bits.addr    := Mux(probeStart, addrWire, RegNext(addrCapturedReg))
    cache.io.cpu.req.valid := actionResValid | io.instruction.fire()
}