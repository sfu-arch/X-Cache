package dandelion.memory.cache

import chisel3._
import chisel3.util._
import chipsalliance.rocketchip.config._
import dandelion.config._
import dandelion.util._
import dandelion.interfaces._
import dandelion.memory.TBE
import dandelion.interfaces.axi._
import chisel3.util.experimental.loadMemoryFromFile
import dandelion.memory.TBE.{ TBETable}



class programmableCacheIO (implicit val p:Parameters) extends Bundle
with HasCacheAccelParams
with HasAccelShellParams {

    val instruction = Flipped(Decoupled(new InstBundle))
}

class programmableCache (implicit val p:Parameters) extends Module
with HasCacheAccelParams
with HasAccelShellParams{

    val io = IO(new programmableCacheIO())

    val cache = new Gem5Cache()
    val tbe = new TBETable()
    val state = UInt(stateLen.W)

    val routineAddr = RoutinePtr.generateRoutineAddrMap(RoutineROM.routineActions)
    val rombits = RoutinePtr.generateTriggerRoutineBit(routineAddr, nextRoutine.routineTriggerList)
    val uCodedNextPtr = VecInit(rombits)
    val actions = RoutinePtr.generateActionRom(RoutineROM.routineActions, ActionList.actions)
    val actionRom = VecInit(actions)
//    val actionRom = VecInit()


    val addr = RegInit(0.U(addrLen.W))
    val event = RegInit(0.U(eventLen.W))

    val tbeAction = UInt(nCom.W)
    val cacheAction = UInt(nCom.W)
    val isCacheAction = Bool()

    val readTBE = Wire(Bool())
    val pc = UInt()
    val action = UInt(nCom.W)

    when(io.instruction.fire()){
        // latching
        addr := io.instruction.bits.addr
        event := io.instruction.bits.event
    }


    readTBE := io.instruction.fire()
    tbe.io.command := Mux (readTBE, tbe.read, tbeAction )
    tbe.io.addr := addr

    state := Mux(tbe.io.outputTBE.valid, tbe.io.outputTBE.bits.state.state, State.default)

    val routine = WireInit( Cat (event, state))

    pc := uCodedNextPtr(routine)
    action := actionRom(pc)

    isCacheAction := (action(nCom-1, nCom-2) === true.B)

    tbeAction := Mux(!isCacheAction, action, tbe.idle )
    cacheAction := Mux(isCacheAction, action, 0.U(nCom.W))


    cache.io.cpu.req.bits.command := cacheAction
    cache.io.cpu.req.bits.addr := addr
    cache.io.cpu.req.valid := true.B



}