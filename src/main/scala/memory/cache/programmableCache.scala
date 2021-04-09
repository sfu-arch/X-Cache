package memGen.memory.cache

import chisel3._
import chisel3.util._
import chisel3.util.experimental._
import chipsalliance.rocketchip.config._
import memGen.config._
import memGen.memory.message._
import chisel3.util.Arbiter
import dsptools.counters.CounterWithReset
import memGen.util._
import memGen.interfaces._
import memGen.interfaces.Action
import memGen.interfaces.axi._

  /*                  sigLoadWays | sigFindInSet | sigAddrToWay | sigPrepMDRead | sigPrepMDWrite | sigAllocate | sigDeallocate | sigWrite |  sigRead  | dataReq
  Probe= 0b0000001011          1             1               0              1                0              0            0               0          0   |   0
  Aloc = 0b0000110100          0             0               1              0                1              1            0               0          0   |   0
  DAloc= 0b0001000000          0             0               0              0                0              0            1               0          0   |   0
  WrInt= 0b0010000000          0             0               0              0                0              0            0               1          0   |   0
  RdInt= 0b0100000000          0             0               0              0                0              0            0               0          1   |   0
  DataRQ=0b1000000000          0             0               0              0                0              0            0               0          0   |   1

     */


class programmableCacheIO (implicit val p:Parameters) extends Bundle
with HasCacheAccelParams
with HasAccelShellParams
with MessageParams{
    val in = new Bundle{
        val cpu     = Flipped(Decoupled((new InstBundle())))
        val memCtrl = Flipped(Decoupled((new InstBundle())))
        val otherNodes = Flipped(Decoupled((new InstBundle())))
    }
    val out = new Bundle{
        val req = Valid (new Bundle{
            val dst = UInt(dstLen.W)
            val req = new IntraNodeBundle()
        })
        val resp = Decoupled(new IntraNodeBundle())
    }
    
}

class programmableCache (val cacheID :Int = 0)(implicit val p:Parameters) extends Module
with HasCacheAccelParams
with HasAccelShellParams{

    val io = IO(new programmableCacheIO())

    val cache    = Module(new Gem5Cache(cacheID))
    val tbe      = Module(new TBETable())
    val lockMem  = Module(new lockVector())
    val stateMem = Module (new stateMem())
    val pc       = Module(new PC())
    val inputArbiter   = Module (new Arbiter(new InstBundle(), n = 3))
    val outReqArbiter  = Module (new Arbiter(io.out.req.cloneType.bits, n = nParal))
    val outRespArbiter = Module (new Arbiter(new InstBundle(), n = nParal + 1))


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

    val respPortQueue = for (i <- 0 until nParal + 1) yield{
        val queue = Module(new Queue(new InstBundle(), entries = 16, pipe = true))
        queue
    }


    val instruction = Wire(Decoupled(new InstBundle()))

    val missLD = Wire(Bool())

    val state    = Wire(UInt(stateLen.W))
    val inputTBE = Wire(Vec(nParal, new TBE))

    val addr = RegInit(0.U(addrLen.W))
    val event = RegInit(0.U(eventLen.W))
    val data = RegInit(0.U(bBits.W))

    val pcWire = Wire(Vec(nParal, new PCBundle()))
    val updatedPC = Wire(Vec(nParal, UInt(pcLen.W)))
    val updatedPCValid = Wire(Vec(nParal, Bool()))
    val dataValid = Wire(Bool())


    val tbeAction   = Wire(Vec(nParal, UInt(nSigs.W)))
    val cacheAction = Wire(Vec(nParal, UInt(nSigs.W)))
    val stateAction = Wire(Vec(nParal, Bool()))

    val isTBEAction   = Wire(Vec(nParal, Bool()))
    val isStateAction = Wire(Vec(nParal, Bool()))
    val isCacheAction = Wire(Vec(nParal, Bool()))
    val isMemAction =   Wire(Vec(nParal, Bool()))

    val isLocked      = Wire(Bool())
    val hit           = Wire(Bool())
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
    val tbeWay        = WireInit(nWays.U((wayLen+1).W))

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
    val dataInputCache = Wire(UInt(bBits.W))

    val probeHit = Wire(Bool())
    val recheckLock = Wire(Bool())

    //latching
    when(instruction.fire() ){
        addr := instruction.bits.addr
        data := instruction.bits.data
    }
    when(instruction.fire() ){
        event := instruction.bits.event
    }.elsewhen((!isLocked && RegNext(recheckLock))& hitLD){
        event := Events.EventArray("NOP").U
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

    val cpuPriority = 2
    val otherNodesPriority  = 1
    val memCtrlPriority = 0

    inputArbiter.io.in(otherNodesPriority) <> io.in.otherNodes
    inputArbiter.io.in(cpuPriority) <> io.in.cpu
    inputArbiter.io.in(memCtrlPriority) <> io.in.memCtrl // priority is for lower producer
    instruction <> inputArbiter.io.out

    recheckLock := RegNext(isLocked && endOfRoutine.reduce(_||_))

    probeHit := (RegNext(probeStart)  && !isLocked) || (!isLocked && RegNext(recheckLock)) 
    hit := probeHit && (cacheWayWire(nParal) =/= nWays.U) && (stateMem.io.out.bits.state === States.StateArray(s"E").U)
    hitLD :=   hit && event === Events.EventArray("LOAD").U
    missLD := probeHit &&  (event === Events.EventArray("LOAD").U) && ((stateMem.io.out.bits.state =/= States.StateArray(s"E").U) || (cacheWayWire(nParal) === nWays.U) )
    
    io.in.memCtrl.ready := !stallInput & inputArbiter.io.chosen === memCtrlPriority.U 
    io.in.cpu.ready      := !stallInput & inputArbiter.io.chosen === cpuPriority.U
    io.in.otherNodes.ready :=  !stallInput & inputArbiter.io.chosen === otherNodesPriority.U

    stallInput := isLocked | pc.io.isFull

    instruction.ready := !stallInput  // @todo should be changed for stalled situations

    checkLock :=  probeStart || recheckLock

    readTBE     := RegEnable(probeStart, false.B, !stallInput) || (RegNext(RegNext(stallInput && endOfRoutine.reduce(_||_) && !probeStart)&& !probeStart) && !stallInput)
    getState    := readTBE

    routineAddrResValid := RegEnable(readTBE , false.B, !stall)

    for (i <- 0 until nParal) {
        isTBEAction(i) :=   (actionReg(i).io.deq.bits.action.actionType === 1.U) && actionReg(i).io.deq.valid
        isCacheAction(i) := (actionReg(i).io.deq.bits.action.actionType === 0.U) && actionReg(i).io.deq.valid
        isStateAction(i) := (actionReg(i).io.deq.bits.action.actionType === 3.U) && actionReg(i).io.deq.valid
        isMemAction(i) := isCacheAction(i) & (actionReg(i).io.deq.bits.action.signals === sigToAction(ActionList.actions("DataRQ")))
    }

    /*************************************************************************/


    /*************************************************************************/
    // Elements
    defaultState := State.default
    state := Mux(tbe.io.outputTBE.valid, tbe.io.outputTBE.bits.state.state, Mux(stateMem.io.out.valid, stateMem.io.out.bits.state, defaultState.state ))

    routineQueue.io.enq.bits := uCodedNextPtr(routine)
    routineQueue.io.enq.valid := !pc.io.isFull & readTBE & !isLocked

    val replacer  =  ReplacementPolicy.fromString(replacementPolicy, nWays)

    val replStateReg = RegInit(VecInit(Seq.fill(nSets)(0.U(replacer.nBits.W))))
    val replacerWayWire = Wire(UInt(replacer.nBits.W))
    val replacerWayReg = Reg(UInt(replacer.nBits.W))
    val addrReplacer = Wire(UInt(addrLen.W))

    addrReplacer := addrToSet(addr)

    replacerWayWire := replacer.get_replace_way(replStateReg(addrReplacer))
    when(missLD & RegNext(probeStart)) { //when a miss happens
        replacerWayReg := replacerWayWire 
        replStateReg(addrReplacer) := replacer.get_next_state(replStateReg(addrReplacer), replacerWayWire)
    }

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
    pc.io.write.bits.way := (wayInputCache)
    pc.io.write.bits.replaceWay := replaceWayInputCache
    pc.io.write.bits.pc := routineQueue.io.deq.bits
    pc.io.write.bits.data := dataInputCache
    pc.io.write.bits.valid := true.B
    pc.io.write.valid := routineQueue.io.deq.fire() & routineAddrResValid
    routineQueue.io.deq.ready := (!pc.io.isFull)

    for (i <- 0 until nParal) {
        updateWay(i) := pc.io.read(i).out.bits.way === nWays.U & cache.io.cpu(i).resp.fire()
        pc.io.read(i).in.bits.data.addr := DontCare //
        pc.io.read(i).in.bits.data.way := Mux(updateWay(i), cacheWayWire(i), pc.io.read(i).out.bits.way  )
        pc.io.read(i).in.bits.data.data := DontCare //
        pc.io.read(i).in.bits.data.pc := updatedPC(i)
        pc.io.read(i).in.bits.data.replaceWay := DontCare
        pc.io.read(i).in.bits.data.valid := updatedPCValid(i) // @todo should be changed for stall

        pc.io.read(i).in.valid := DontCare // @todo Should be changed probably
        pcWire(i) <> pc.io.read(i).out.bits
    }

    wayInputCache := RegEnable(Mux( tbeWay === nWays.U , (cacheWayWire(nParal)), tbeWay ), nWays.U, !stallInput)
    replaceWayInputCache := replacerWayReg
    addrInputCache := ((RegEnable(addr, 0.U, !stallInput)))
    dataInputCache := ((RegEnable(data, 0.U, !stallInput)))

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
        inputTBE(i).way         := actionReg(i).io.deq.bits.way 

        tbe.io.write(i).bits.command := Mux(updateTBEWay(i) | updateTBEState(i), tbe.write, tbeAction(i)) // @todo Wrong
        tbe.io.write(i).bits.addr := actionReg(i).io.deq.bits.addr
        tbe.io.write(i).bits.inputTBE := inputTBE(i)
        tbe.io.write(i).bits.mask := tbe.maskAll 
        tbe.io.write(i).valid := DontCare
    }

    // lock Mem
    lockMem.io.lock.in.bits.data := DontCare
    lockMem.io.lock.in.bits.addr  := Mux(isLocked, addr, instruction.bits.addr)
    lockMem.io.lock.in.valid := checkLock
    lockMem.io.lock.in.bits.cmd := true.B // checking and locking

    isLocked := RegEnable(Mux(lockMem.io.lock.out.valid, lockMem.io.lock.out.bits, false.B), false.B, checkLock)

    for (i <- 0 until nParal)  {
        lockMem.io.unLock(i).in.bits.data := DontCare
        lockMem.io.unLock(i).in.bits.addr := actionReg(i).io.deq.bits.addr
        lockMem.io.unLock(i).in.bits.cmd := false.B //unlock
        lockMem.io.unLock(i).in.valid := endOfRoutine(i)
    }

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
        cache.io.cpu(i).req.valid := actionReg(i).io.deq.fire()
        cache.io.cpu(i).req.bits.data := actionReg(i).io.deq.bits.data
        cache.io.cpu(i).req.bits.replaceWay := actionReg(i).io.deq.bits.replaceWay
    }

    cache.io.cpu(nParal).req.bits.way := DontCare
    cache.io.cpu(nParal).req.bits.command := Mux(probeStart, sigToAction(ActionList.actions("Probe")), 0.U)
    cache.io.cpu(nParal).req.bits.addr := Mux(probeStart, addrWire, 0.U)
    cache.io.cpu(nParal).req.valid := probeStart
    cache.io.cpu(nParal).req.bits.replaceWay := DontCare 

    when( RegNext(probeStart)){
        printf(p"req from ${RegNext(inputArbiter.io.chosen)}\n")
        when(RegNext(inputArbiter.io.chosen) === cpuPriority.U){
            when(hitLD){
                printf(p" Load hit for addr ${addr}\n")
            }.elsewhen(isLocked){
                printf(p"addr ${addr} is locked\n")
            }.elsewhen(hit){
                printf(p"Hit (store probably) for addr ${addr}\n")
            }.otherwise{
                printf(p"miss for addr ${addr}\n")
            }
        }
    }
    cache.io.bipassLD.in.valid := hitLD
    cache.io.bipassLD.in.bits.addr  := addr
    cache.io.bipassLD.in.bits.way := cacheWayWire(nParal)
    dataValid := cache.io.bipassLD.out.valid
    
    for (i <- 0 until nParal) {
        outReqArbiter.io.in(i).bits.req.data:= 0.U // for reading
        outReqArbiter.io.in(i).bits.req.data:= DontCare
        outReqArbiter.io.in(i).bits.req.addr := actionReg(i).io.deq.bits.addr
        outReqArbiter.io.in(i).bits.req.inst:= 0.U // 0 for memCtrl
        outReqArbiter.io.in(i).bits.dst:= 1.U
        outReqArbiter.io.in(i).valid :=  isMemAction(i)
    }

    io.out.req.bits.req.inst := outReqArbiter.io.out.bits.req.inst// for reading
    io.out.req.bits.req.data := outReqArbiter.io.out.bits.req.data
    io.out.req.bits.req.addr := outReqArbiter.io.out.bits.req.addr
    io.out.req.bits.dst := outReqArbiter.io.out.bits.dst
    io.out.req.valid :=  outReqArbiter.io.out.valid
    outReqArbiter.io.out.ready := true.B // @todo Should be connected to NI

    for (i <- 0 until nParal) {
        respPortQueue(i).io.enq.bits.data  :=cache.io.cpu(i).resp.bits.data
        respPortQueue(i).io.enq.bits.event := 0.U
        respPortQueue(i).io.enq.bits.addr  := actionReg(i).io.deq.bits.addr
        respPortQueue(i).io.enq.valid      := cache.io.cpu(i).resp.valid & cache.io.cpu(i).resp.bits.iswrite
    }

    respPortQueue(nParal).io.enq.bits.data  := cache.io.bipassLD.out.bits.data
    respPortQueue(nParal).io.enq.bits.event := 0.U
    respPortQueue(nParal).io.enq.bits.addr  := RegNext(addr)
    respPortQueue(nParal).io.enq.valid      := cache.io.bipassLD.out.valid

    for (i <- 0 until nParal + 1) {
        outRespArbiter.io.in(i).bits.data  := respPortQueue(i).io.deq.bits.data
        outRespArbiter.io.in(i).bits.event := respPortQueue(i).io.deq.bits.event
        outRespArbiter.io.in(i).bits.addr  := respPortQueue(i).io.deq.bits.addr
        outRespArbiter.io.in(i).valid      := respPortQueue(i).io.deq.valid
        respPortQueue(i).io.deq.ready := true.B
    }

    io.out.resp.bits.inst := outRespArbiter.io.out.bits.event
    io.out.resp.bits.data := outRespArbiter.io.out.bits.data
    io.out.resp.bits.addr := outRespArbiter.io.out.bits.addr
    io.out.resp.valid     := outRespArbiter.io.out.valid
    outRespArbiter.io.out.ready := true.B

    // BoringUtils.addSource(missLD, "missLD")
    // BoringUtils.addSource(hitLD,  "hitLD" )
    // BoringUtils.addSource(instruction.fire, "instCount")
    // BoringUtils.addSource(instruction.fire && inputArbiter.io.chosen === cpuPriority.U, "cpuReq")
    // BoringUtils.addSource(instruction.fire && inputArbiter.io.chosen === memCtrlPriority.U, "memCtrlReq")
    // BoringUtils.addSource(instruction.fire && instruction.bits.event === Events.EventArray("LOAD").U , "ldReq")

}
