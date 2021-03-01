package memGen.memory.cache

import chisel3._
import chisel3.util._
import chipsalliance.rocketchip.config._
import memGen.config._
import memGen.memory.message._
import chisel3.util.Arbiter
import dsptools.counters.CounterWithReset
import memGen.util._
import memGen.interfaces._
import memGen.interfaces.Action
import memGen.interfaces.axi._


class programmableCacheIO (implicit val p:Parameters) extends Bundle
with HasCacheAccelParams
with HasAccelShellParams
with MessageParams{
    val in = new Bundle{
        val cpu     = Flipped(Decoupled((new InstBundle())))
        val memCtrl = Flipped(Decoupled((new InstBundle())))
    }
    val out = Valid(new Bundle{
        val dst = UInt(dstLen.W)
        val req = new IntraNodeBundle()
    })
}

class programmableCache (implicit val p:Parameters) extends Module
with HasCacheAccelParams
with HasAccelShellParams{

    val io = IO(new programmableCacheIO())

    val cache    = Module(new Gem5Cache())
    val tbe      = Module(new TBETable())
    val lockMem  = Module(new lockVector())
    val stateMem = Module (new stateMem())
    val pc       = Module(new PC())
    val arbiter  = Module (new Arbiter(new InstBundle(), n = 2))



    stateMem.io  <> DontCare
    cache.io.cpu <> DontCare

    /********************************************************************************/
    // Building ROMs
    val (routineAddr,setStateDst) = RoutinePtr.generateRoutineAddrMap(RoutineROM.routineActions)
    val (rombits, dstStateBits)   = RoutinePtr.generateTriggerRoutineBit(routineAddr, nextRoutine.routineTriggerList)

    val uCodedNextPtr = VecInit(rombits)
    val dstStateRom = VecInit(dstStateBits)

    val actions = RoutinePtr.generateActionRom(RoutineROM.routineActions, ActionList.actions, setStateDst)
    val actionRom = for (i <- 0 until nParal) yield {
        val actionRom = VecInit(actions)
        actionRom
    }
    /********************************************************************************/



    val instruction = Wire(Decoupled(new InstBundle()))

    val missLD = Wire(Bool())
    val missLDReg = Reg(Bool())

    val state    = Wire(UInt(stateLen.W))
    val inputTBE = Wire(Vec(nParal, new TBE))

    val addr = RegInit(0.U(addrLen.W))
    val event = RegInit(0.U(eventLen.W))
    val data = RegInit(0.U(dataLen.W))

    val pcWire = Wire(Vec(nParal, new PCBundle()))
    val updatedPC = Wire(Vec(nParal, UInt(pcLen.W)))
    val updatedPCValid = Wire(Vec(nParal, Bool()))
    val dataValid = Wire(Bool())


    val tbeAction   = Wire(Vec(nParal, UInt(nSigs.W)))
    val cacheAction = Wire(Vec(nParal, UInt(nSigs.W)))
    val stateAction = Wire(Vec(nParal, Bool()))
//    val memAction = Wire(Vec(nParal, Bool()))

    val isTBEAction   = Wire(Vec(nParal, Bool()))
    val isStateAction = Wire(Vec(nParal, Bool()))
    val isCacheAction = Wire(Vec(nParal, Bool()))
    val isMemAction =   Wire(Vec(nParal, Bool()))

    val isLocked      = Wire(Bool())
    val hitLD         = Wire(Bool())

    val updateTBEWay   = Wire(Vec(nParal, Bool()))
    val updateTBEState = Wire(Vec(nParal, Bool()))

    val stall = WireInit(false.B)
    val stallInput = WireInit(false.B)
    val bubble = WireInit(stallInput)

    val readTBE   = Wire(Bool())
    val checkLock = Wire(Bool())
    val getState  = Wire(Bool())

    val routineAddrResValid = Wire(Bool())
    val actionResValid      = Wire(Vec(nParal, Bool()))

    val defaultState = Wire(new State())

    val firstLineNextRoutine = Wire(Vec(nParal, Bool()))
    val endOfRoutine         = Wire(Vec(nParal, Bool()))

    val routine = WireInit( Cat (event, state))
    val cacheWayReg  = RegInit(VecInit(Seq.fill(nParal + 1)(nWays.U((wayLen+1).W))))
    val cacheWayWire = Wire(Vec(nParal + 1, (UInt((wayLen+1).W))))
    val updateWay = Wire(Vec(nParal, Bool()))

    val wayInputCache = Wire(UInt((wayLen+1).W))
    val replaceWayInputCache = Wire(UInt((wayLen+1).W))
    val tbeWay        = RegInit(nWays.U((wayLen+1).W))

    val routineQueue = Module( new Queue( UInt((eventLen + stateLen).W), pipe = true, entries = 1 ))

    val actionReg  = for (i <- 0 until nParal) yield {
        val ActionReg =  Module(new Queue( new ActionBundle(), pipe = true, entries = 1))
        ActionReg
    }

    val dstOfSetState = Wire(Vec(nParal, new State()))
    val stateMemOutput = Wire((new State()))

    val probeStart = WireInit(instruction.fire())

    val tbeActionInRom = Wire(Vec(nParal, Bool()))

    val addrWire   = WireInit(instruction.bits.addr)

    val addrInputCache  = Wire(UInt(addrLen.W))
    val dataInputCache = Wire(UInt(dataLen.W))

    //latching
    when(instruction.fire() ){
        addr := instruction.bits.addr
        event := instruction.bits.event
        data := instruction.bits.data
    }

    for (i <- 0 until (nParal + 1) ) {
        when(cache.io.cpu(i).resp.fire()) {
            cacheWayReg(i) := cache.io.cpu(i).resp.bits.way
        }
        cacheWayWire(i) := cache.io.cpu(i).resp.bits.way
    }

    when(tbe.io.outputTBE.fire()){
        tbeWay := tbe.io.outputTBE.bits.way
    }.otherwise{
        tbeWay := nWays.U
    }

    /*************************************************************************/
    // control signals

    val cpuPriority = 1

    /**************
    tempe
    **************/
      val (_, timeout) = CounterWithReset(true.B, 100, probeStart)


    arbiter.io.in(cpuPriority) <> io.in.cpu
    arbiter.io.in(0) <> io.in.memCtrl // priority is for lower producer
    instruction <> arbiter.io.out

    missLD := (cacheWayWire(nParal) === nWays.U) & probeStart & !isLocked & (arbiter.io.chosen === cpuPriority.U) & (instruction.bits.event === Events.EventArray("LOAD").U)

    when(missLD | !missLD & arbiter.io.chosen =/= cpuPriority.U){
        missLDReg := missLD
    }

    io.in.memCtrl.ready := !stallInput
    io.in.cpu.ready := !missLD & !missLDReg & instruction.ready
    io.out.valid := false.B

    stallInput := isLocked | pc.io.isFull

    instruction.ready := !stallInput & !(missLDReg & (arbiter.io.chosen === cpuPriority.U))   // @todo should be changed for stalled situations

    readTBE     := RegEnable(instruction.fire(), false.B, instruction.ready)
    checkLock   := RegEnable(instruction.fire(), false.B, true.B)
    getState    := RegEnable(instruction.fire(), false.B, instruction.ready)

    routineAddrResValid := RegEnable(readTBE , false.B, !stall)

    for (i <- 0 until nParal) {
        isTBEAction(i) :=   (actionReg(i).io.deq.bits.action.actionType === 1.U)
        isCacheAction(i) := (actionReg(i).io.deq.bits.action.actionType === 0.U)
        isStateAction(i) := (actionReg(i).io.deq.bits.action.actionType === 2.U | actionReg(i).io.deq.bits.action.actionType === 3.U)
        isMemAction(i) := isCacheAction(i) & (actionReg(i).io.deq.bits.action.signals === sigToAction(ActionList.actions("DataRQ")))
    }

    /*************************************************************************/


    /*************************************************************************/
    // Elements
    defaultState := State.default
    state := Mux(tbe.io.outputTBE.valid, tbe.io.outputTBE.bits.state.state, Mux(stateMem.io.out.valid, stateMem.io.out.bits.state, defaultState.state ))

    routineQueue.io.enq.bits := uCodedNextPtr(routine)
    routineQueue.io.enq.valid := !pc.io.isFull & readTBE

    val replacer  =  ReplacementPolicy.fromString(replacementPolicy, nWays)
    when(probeStart) {replacer.miss}

    for (i <- 0 until nParal) {

        updateTBEState(i) := isStateAction(i)
        updateTBEWay(i)   := isStateAction(i)
        endOfRoutine(i)   := isStateAction(i)

        actionResValid(i):= RegEnable((routineAddrResValid | (!routineAddrResValid & !firstLineNextRoutine(i))), false.B , !stall)
        tbeActionInRom(i) := (actionResValid(i) & isTBEAction(i))

        actionReg(i).io.enq.bits.action.signals := sigToAction(actionRom(i)(pcWire(i).pc))
        actionReg(i).io.enq.bits.action.actionType := sigToActType(actionRom(i)(pcWire(i).pc))
        actionReg(i).io.enq.bits.addr := pcWire(i).addr
        actionReg(i).io.enq.bits.way  := Mux(updateWay(i), cacheWayWire(i), pcWire(i).way)
        actionReg(i).io.enq.bits.data := pcWire(i).data
        actionReg(i).io.enq.bits.replaceWay := pcWire(i).replaceWay

    }

    for (i <- 0 until nParal) {
        firstLineNextRoutine(i) := (sigToAction(actionRom(i)(pcWire(i).pc)).asUInt() === 0.U ) // @todo all of them should be changed for stall
        updatedPC(i) := Mux(firstLineNextRoutine(i), pcWire(i).pc, pcWire(i).pc + 1.U  )
        updatedPCValid(i) := !firstLineNextRoutine(i)
    }

    pc.io.write.bits.addr := addrInputCache
    pc.io.write.bits.way := wayInputCache
    pc.io.write.bits.replaceWay := replaceWayInputCache
    pc.io.write.bits.pc := routineQueue.io.deq.bits
    pc.io.write.bits.data := dataInputCache
    pc.io.write.bits.valid := true.B
    pc.io.write.valid := routineQueue.io.deq.fire() & routineAddrResValid
    routineQueue.io.deq.ready := !pc.io.isFull


    for (i <- 0 until nParal) {
        updateWay(i) := pc.io.read(i).out.bits.way === nWays.U & cache.io.cpu(i).resp.fire()
        pc.io.read(i).in.bits.data.addr := DontCare //
        pc.io.read(i).in.bits.data.way := Mux(updateWay(i), cacheWayWire(i), pc.io.read(i).out.bits.way  )
        pc.io.read(i).in.bits.data.data := DontCare //
        pc.io.read(i).in.bits.data.pc := updatedPC(i)
        pc.io.read(i).in.bits.data.replaceWay := DontCare
        pc.io.read(i).in.bits.data.valid := updatedPCValid(i) // @todo should be changed for stall

        pc.io.read(i).in.valid := DontCare // @todo Should be changed probably

//        pcWire(i).pc   := pc.io.read(i).out.bits.pc
//        pcWire(i).way  := pc.io.read(i).out.bits.way
//        pcWire(i).addr  := addr
        pcWire(i) <> pc.io.read(i).out.bits

    }

    wayInputCache := (RegEnable(Mux( tbeWay === nWays.U , cacheWayReg(nParal), tbeWay ), nWays.U, !stallInput))
    replaceWayInputCache := RegEnable(replacer.get_replace_way(0.U), 0.U, !stallInput)
    addrInputCache := (RegEnable(addr, 0.U, !stallInput))
    dataInputCache := (RegEnable(data, 0.U, !stallInput))

    for (i <- 0 until nParal){
        tbeAction(i) := Mux(isTBEAction(i), actionReg(i).io.deq.bits.action.signals, tbe.idle)
        cacheAction(i) := Mux(isCacheAction(i), actionReg(i).io.deq.bits.action.signals, 0.U(nSigs.W))
        stateAction(i) := isStateAction(i)

        dstOfSetState(i).state := Mux( isStateAction(i), sigToState (actionReg(i).io.deq.bits.action.signals), State.default.state)

        actionReg(i).io.deq.ready := !stall
        actionReg(i).io.enq.valid := pcWire(i).valid  // @todo enq ready should be used for controlling  updated pc

    }
    /*************************************************************************************/


    // TBE
    tbe.io.read.valid := readTBE
    tbe.io.read.bits.addr := addr

    for (i <- 0 until nParal)  {

        inputTBE(i).state.state := dstOfSetState(i).state
        inputTBE(i).way         := actionReg(i).io.deq.bits.way // @todo WRONG

        tbe.io.write(i).bits.command := Mux(updateTBEWay(i) | updateTBEState(i), tbe.write, tbeAction(i)) // @todo Wrong
        tbe.io.write(i).bits.addr := actionReg(i).io.deq.bits.addr
        tbe.io.write(i).bits.inputTBE := inputTBE(i)
        tbe.io.write(i).bits.mask := tbe.maskAll // @todo Should be double-checked
        tbe.io.write(i).valid := DontCare
    }

    // lock Mem
    lockMem.io.lock.in.bits.data := DontCare
    lockMem.io.lock.in.bits.addr  := addr
    lockMem.io.lock.in.valid := checkLock
    lockMem.io.lock.in.bits.cmd := true.B // checking and locking
    isLocked := Mux(lockMem.io.lock.out.valid, lockMem.io.lock.out.bits, false.B)

    for (i <- 0 until nParal)  {
        lockMem.io.unLock(i).in.bits.data := DontCare
        lockMem.io.unLock(i).in.bits.addr := actionReg(i).io.deq.bits.addr
        lockMem.io.unLock(i).in.bits.cmd := false.B //unlock
        lockMem.io.unLock(i).in.valid := endOfRoutine(i)
    }

    // @todo tbe should have a higher priority for saving dst state
    // State Memory
    for (i <- 0 until nParal)  {
        stateMem.io.in(i).bits.isSet := true.B
        stateMem.io.in(i).bits.addr := actionReg(i).io.deq.bits.addr //
        stateMem.io.in(i).bits.state := dstOfSetState(i)
        stateMem.io.in(i).bits.way := actionReg(i).io.deq.bits.way
        stateMem.io.in(i).valid := isStateAction(i)
    }

    stateMem.io.in(nParal).bits.isSet := false.B // used for getting
    stateMem.io.in(nParal).bits.addr := addr
    stateMem.io.in(nParal).bits.state := DontCare
    stateMem.io.in(nParal).bits.way :=  cacheWayWire(nParal)
    stateMem.io.in(nParal).valid := getState
    stateMemOutput := stateMem.io.out.bits

    // Cache Logic
    for (i <- 0 until nParal) {
        cache.io.cpu(i).req.bits.way := actionReg(i).io.deq.bits.way
        cache.io.cpu(i).req.bits.command := cacheAction(i)
        cache.io.cpu(i).req.bits.addr := actionReg(i).io.deq.bits.addr
//        cache.io.cpu(i).req.valid := actionResValid(i) // todo  WRONG for stall
        cache.io.cpu(i).req.valid := actionReg(i).io.deq.fire()
        cache.io.cpu(i).req.bits.data := actionReg(i).io.deq.bits.data
        cache.io.cpu(i).req.bits.replaceWay := actionReg(i).io.deq.bits.replaceWay


    }
    cache.io.cpu(nParal).req.bits.way := DontCare
    cache.io.cpu(nParal).req.bits.command := Mux(probeStart, sigToAction(ActionList.actions("Probe")), 0.U)
    cache.io.cpu(nParal).req.bits.addr := Mux(probeStart, addrWire, 0.U)
    cache.io.cpu(nParal).req.valid := probeStart
    cache.io.cpu(nParal).req.bits.replaceWay := DontCare // should be changed

    hitLD :=  (cacheWayWire(nParal) =/= nWays.U) & probeStart & !isLocked
    cache.io.bipassLD.in.valid := hitLD
    cache.io.bipassLD.in.bits.addr  := addr
    cache.io.bipassLD.in.bits.way := cacheWayWire(nParal)
    data := cache.io.bipassLD.out.bits
    dataValid := cache.io.bipassLD.out.valid


    for (i <- 0 until nParal) {
        io.out.bits.req.inst := 0.U // for reading
        io.out.bits.req.data := DontCare
        io.out.bits.req.addr := actionReg(i).io.deq.bits.addr
        io.out.bits.dst := 0.U // 0 for memCtrl
        io.out.valid := isMemAction(i)
    }
}