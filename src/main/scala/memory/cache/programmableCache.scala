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
    cache.io.cpu <> DontCare
    cache.io.mem <> DontCare

    /********************************************************************************/
    // Building ROMs
    val (routineAddr,setStateDst) = RoutinePtr.generateRoutineAddrMap(RoutineROM.routineActions)
    val (rombits, dstStateBits) = RoutinePtr.generateTriggerRoutineBit(routineAddr, nextRoutine.routineTriggerList)

    val uCodedNextPtr = VecInit(rombits)
    val dstStateRom = VecInit(dstStateBits)

    val actions = RoutinePtr.generateActionRom(RoutineROM.routineActions, ActionList.actions, setStateDst)
    val actionRom = VecInit(actions)
    /********************************************************************************/

    val state    = Wire(UInt(stateLen.W))
    val inputTBE = WireInit(TBE.default)

    val addr = RegInit(0.U(addrLen.W))
    val event = RegInit(0.U(eventLen.W))
    val pc = Reg(UInt())

    val tbeAction   = Wire(UInt(nSigs.W))
    val cacheAction = Wire(UInt(nSigs.W))
    val stateAction = Wire(Bool())

    val isTBEAction   = Wire(Bool())
    val isStateAction = Wire(Bool())
    val isCacheAction = Wire(Bool())
    val isLocked      = Wire(Bool())
//    val tbeResValid   = Wire(Bool())

    val updateTBEWay  = Wire(Bool())
    val updateTBEState = Wire(Bool())

    val stall = WireInit(false.B)

    val readTBE   = Wire(Bool())
    val checkLock = Wire(Bool())
    val getState  = Wire(Bool())

    val action = Wire(new ActionBundle())

    val routineAddrResValid = Wire(Bool())
    val actionResValid      = Wire(Bool())
    val startRoutine = Wire(Bool())

    val defaultState = Wire(new State())
//    val dstState = Reg(new State())

    val firstLineNextRoutine = Wire(Bool())
    val endOfRoutine   = Wire(Bool())
//    val isProbe = Wire(Bool())

    val routine = WireInit( Cat (event, state))
//    val (probeHandled, _) = Counter(isProbe, 2)
    val cacheWayReg  = RegInit(nWays.U((wayLen+1).W))
    val cacheWayWire = Wire(UInt((wayLen+1).W))

    val wayInputCache = Reg(UInt((wayLen+1).W))
    val tbeWay        = RegInit(nWays.U((wayLen+1).W))

    val routineReg = Wire(UInt((eventLen + stateLen).W))
    val actionReg  = Wire(UInt((nSigs + 2).W))

    val dstOfSetState = Wire(new State())
    val stateMemOutput = Wire((new State()))

    val probeStart = WireInit(io.instruction.fire())

    val tbeActionInRom = Wire(Bool())

    val addrWire   = WireInit(io.instruction.bits.addr)

    val addrCapturedReg  = RegNext(RegNext(addr))

    io.instruction.ready := true.B // @todo should be changed for stalled situations
//    cache.io.cpu.resp.ready := true.B

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

    /*************************************************************************/
    // control signals

    readTBE     := RegEnable(io.instruction.fire(), false.B, !stall)
    checkLock   := RegEnable(io.instruction.fire(), false.B, !stall)
    getState    := RegEnable(io.instruction.fire(), false.B, !stall)

    routineAddrResValid := RegEnable(readTBE, false.B, !stall)
    startRoutine        := RegEnable(readTBE, false.B, !stall)

//    actionResValid := RegEnable(Mux(routineAddrResValid, true.B , Mux (firstLineRoutine, false.B,  true.B)), false.B , !stall)
    actionResValid := RegEnable(routineAddrResValid | (!routineAddrResValid & !firstLineNextRoutine) , false.B , !stall)
    firstLineNextRoutine := (actionRom(pc).asUInt() === 0.U )
    tbeActionInRom := (actionResValid & isTBEAction)
    updateTBEState := isStateAction
    endOfRoutine   := isStateAction

    isTBEAction   := (action.actionType === 0.U)
    isCacheAction := (action.actionType === 1.U)
    isStateAction := (action.actionType === 2.U | action.actionType === 3.U )

    updateTBEWay   := (RegNext(cache.io.cpu.resp.fire()) & (cacheWayReg =/= nWays.U))

    //    dstState.state := dstStateRom(routine)
//    isProbe := (action.signals === sigToAction(ActionList.actions("Probe")))

    /*************************************************************************/

    /*************************************************************************/
    // Elements

    defaultState := State.default

    state := Mux(tbe.io.outputTBE.valid, tbe.io.outputTBE.bits.state.state, Mux(stateMem.io.out.valid, stateMem.io.out.bits.state, defaultState.state ))

    routineReg := RegEnable(uCodedNextPtr(routine), 0.U, !stall)
    actionReg  := RegEnable(actionRom(pc) , 0.U, !stall)

    pc := Mux(startRoutine, routineReg, Mux(firstLineNextRoutine, pc, pc+ 1.U  ))

    wayInputCache := Mux( tbeWay === nWays.U , cacheWayReg, tbeWay )

    printf(p"way ${wayInputCache}\n")

    action.signals := sigToAction(actionReg)
    action.actionType := actionReg(nSigs+ 1, nSigs)

    tbeAction   := Mux(isTBEAction, action.signals, tbe.idle )
    cacheAction := Mux(isCacheAction, action.signals, 0.U(nSigs.W))
    stateAction := isStateAction

    dstOfSetState.state := Mux( isStateAction, sigToState (actionReg), State.default.state)
    /*************************************************************************************/

    // TBE
    inputTBE.state.state := Mux(updateTBEState, dstOfSetState.state, DontCare)
    inputTBE.way         := Mux(updateTBEWay , cacheWayReg, DontCare)

    tbe.io.command := Mux (readTBE, tbe.read, Mux( updateTBEWay | updateTBEState, tbe.write, tbeAction))
    tbe.io.addr := addr
    tbe.io.inputTBE := inputTBE

    tbe.io.mask := tbe.maskAll
    when (updateTBEState & updateTBEWay){
        tbe.io.mask := tbe.maskAll
    }.elsewhen(updateTBEWay & !updateTBEState){
        tbe.io.mask := tbe.maskWay
    }.elsewhen(updateTBEState & !updateTBEWay){
        tbe.io.mask := tbe.maskState
    }
    tbe.io.outputTBE.ready := true.B


    // lock Mem
    lockMem.io.lock.in.bits.data := DontCare
    lockMem.io.unLock.in.bits.data := DontCare
    lockMem.io.lock.in.bits.addr  := addr
    lockMem.io.lock.in.valid := checkLock
    lockMem.io.lock.in.bits.cmd := true.B // checking and locking
    isLocked := Mux(lockMem.io.lock.out.valid, lockMem.io.lock.out.bits, false.B)
    lockMem.io.unLock.in.bits.addr := addrCapturedReg
    lockMem.io.unLock.in.bits.cmd  := false.B   //unlock
    lockMem.io.unLock.in.valid     := endOfRoutine


    // @todo tbe should have a higher priority for saving dst state
    // State Memory
    stateMem.io.in.bits.isSet := Mux(getState, false.B, stateAction) // have conflict when an inst comes in and prev one is ending
    stateMem.io.in.bits.addr := addr
    stateMem.io.in.bits.state := dstOfSetState
    stateMem.io.in.bits.way := Mux (isStateAction, cacheWayReg, cacheWayWire)
    stateMem.io.in.valid := isStateAction | getState
    stateMemOutput := stateMem.io.out.bits


    // Cache Logic
    cache.io.cpu.req.bits.way := RegNext(wayInputCache)
    cache.io.cpu.req.bits.command := Mux(probeStart, sigToAction(ActionList.actions("Probe")), cacheAction)
    cache.io.cpu.req.bits.addr    := Mux(probeStart, addrWire, RegNext(addrCapturedReg))
    cache.io.cpu.req.valid := actionResValid | io.instruction.fire()
}