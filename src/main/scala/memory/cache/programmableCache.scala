package memGen.memory.cache

import chisel3._
import chisel3.util._
import chisel3.util.experimental._
import chipsalliance.rocketchip.config._
import memGen.config._
import memGen.memory.message._
import chisel3.util.Arbiter
import dsptools.counters.CounterWithReset
import interfaces.MIMOQueue
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
    val ID = WireInit(cacheID.U)

    val cache    = Module(new Gem5Cache(cacheID))
    val tbe      = Module(new TBETable())
    val lockMem  = Module(new lockVector())
    val stateMem = Module (new stateMem())
    val pc       = Module(new PC())
    val inputArbiter   = Module (new Arbiter(new InstBundle(), n = 4))
    val outReqArbiter  = Module (new Arbiter(io.out.req.cloneType.bits, n = nParal))
    val outRespArbiter = Module (new Arbiter(new InstBundle(), n = nParal + 1))
    val feedbackArbiter = Module (new Arbiter(new InstBundle(), n = nParal))

    stateMem.io  <> DontCare
    cache.io.cpu <> DontCare
    cache.io.probe <> DontCare

    /********************************************************************************/
    // Building ROMs
    val RoutinePtr = new RoutinePtr
    val (routineAddr,setStateDst) = RoutinePtr.generateRoutineAddrMap(RoutineROMWalker.routineActions)
    val (rombits, dstStateBits)   = RoutinePtr.generateTriggerRoutineBit(routineAddr, nextRoutineWalker.routineTriggerList)


    val uCodedNextPtr = VecInit(rombits)
    val dstStateRom = VecInit(dstStateBits)

//    printf(p"rombits ${uCodedNextPtr} \n")


    val actions = RoutinePtr.generateActionRom(RoutineROMWalker.routineActions, ActionList.actions, setStateDst)
    val actionRom = for (i <- 0 until nParal) yield {
        val actionRom = VecInit(actions)
        actionRom
    }

    /********************************************************************************/
    val input = Module(new Queue(new Bundle{ val inst = new InstBundle(); val tbeOut = new TBE() }, entries = 1, pipe = true))

    val respPortQueue = for (i <- 0 until nParal + 1) yield{
        val queue = Module(new Queue(new InstBundle(), entries = 16, pipe = true))
        queue
    }

    val feedbackInQueue = for (i <- 0 until nParal) yield{
        val queue = Module(new Queue(new InstBundle(), entries = 16, pipe = true))
        queue
    }

    val probeWay = Module(new Queue( UInt(wayLen.W) , entries = 16, pipe =true , flow = true))

    val instruction = Wire(Decoupled(new InstBundle()))
    val missLD = Wire(Bool())

    val state    = Wire(UInt(stateLen.W))
    val inputTBE = Wire(Vec(nParal, new TBE))

    val pcWire = Wire(Vec(nParal, new PCBundle()))
    val updatedPC = Wire(Vec(nParal, UInt(pcLen.W)))
    val updatedPCValid = Wire(Vec(nParal, Bool()))
    val dataValid = Wire(Bool())

    val tbeAction   = Wire(Vec(nParal, UInt(TBE.default.cmdLen.W)))
    val cacheAction = Wire(Vec(nParal, UInt(nSigs.W)))
    val stateAction = Wire(Vec(nParal, Bool()))

    val isTBEAction   = Wire(Vec(nParal, Bool()))
    val isStateAction = Wire(isTBEAction.cloneType)
    val isFeedbackAction = Wire(isTBEAction.cloneType)
    val isCacheAction = Wire(isTBEAction.cloneType)
    val isMemAction =   Wire(isTBEAction.cloneType)
    val isCompAction = Wire(isTBEAction.cloneType)

    val isLocked      = Wire(Bool())
    val hit           = Wire(Bool())
    val hitLD         = Wire(Bool())

    val updateTBEFixedFields   = Wire(Vec(nParal, Bool()))
    val maskField = Wire(Vec(nParal, UInt(TBE.default.fieldLen.W)))
    val tbeFieldUpdateSrc = Wire(maskField.cloneType)

    val stall = WireInit(false.B)
    val stallInput = WireInit(false.B)

    val readTBE   = Wire(Bool())
    val checkLock = Wire(Bool())
    val setLock   = Wire(Bool())
    val getState  = Wire(Bool())

    val defaultState = Wire(new State())

    val firstLineNextRoutine = Wire(Vec(nParal, Bool()))
    val endOfRoutine         = Wire(Vec(nParal, Bool()))

    val routine = WireInit( Cat (input.io.deq.bits.inst.event, state))
    val cacheWayWire = Wire(Vec(nParal, (UInt((wayLen+1).W))))
    val updateWay = Wire(Vec(nParal, Bool()))

    val sets = Wire(Vec(nParal, UInt(32.W)))

    val wayInputCache = Wire(UInt((wayLen+1).W))
    val replaceWayInputCache = Wire(UInt((wayLen+1).W))
    val tbeWay        = WireInit(nWays.U((wayLen+1).W))

    val feedbackOutQueue = Module(new Queue(new InstBundle, entries = 16, pipe = true))
    val routineQueue = Module( new Queue( UInt(pcLen.W), pipe = true, entries = 1 ))

    val actionReg  = for (i <- 0 until nParal) yield {
        val ActionReg =  Module(new Queue( new ActionBundle(), pipe = true, entries = 1))
        ActionReg
    }

    val mimoQ = Module (new MIMOQueue(new Bundle { val way =UInt(wayLen.W); val addr = UInt(addrLen.W)}, entries = 64, NumOuts = 1, NumIns = nWays, pipe = true ))
    mimoQ.io.clear := false.B

    val compUnit  = for (i <- 0 until nParal) yield {
        val Comp =  Module(new Computation(UInt(32.W)))
        Comp
    }

    val compUnitInput1  = for (i <- 0 until nParal) yield {
        val CompIO1 =  Module(new Mux3(UInt(32.W)))

        CompIO1
    }

    val compUnitInput2  = for (i <- 0 until nParal) yield {
        val CompIO2 =  Module(new Mux3(UInt(32.W)))
        CompIO2
    }

    val dstOfSetState = Wire(Vec(nParal, new State()))
    val stateMemOutput = Wire((new State()))

    val probeStart = Wire(Bool())

    val instUsed = RegInit(false.B)

    val inputToPC  = Wire(new InstBundle())

    val probeHit = Wire(Bool())

    val replacer  =  ReplacementPolicy.fromString(replacementPolicy, nWays)
    val replStateReg = RegInit(VecInit(Seq.fill(nSets)(0.U(replacer.nBits.W))))
    val replacerWayWire = Wire(UInt(replacer.nBits.W))
    val replacerWayReg = Reg(UInt(replacer.nBits.W))
    val addrReplacer = Wire(UInt(addrLen.W))


    for (i <- 0 until nParal) {
        cacheWayWire(i) := cache.io.cpu(i).resp.bits.way
    }

      probeWay.io.enq.bits := cache.io.probe.resp.bits.way
      probeWay.io.enq.valid := cache.io.probe.resp.valid

    (0 until nWays).map (i => mimoQ.io.enq.bits(i).way := cache.io.probe.multiWay.bits.way(i))
    (0 until nWays).map (i => mimoQ.io.enq.bits(i).addr := (cache.io.probe.multiWay.bits.addr))

    mimoQ.io.enq.valid := cache.io.probe.multiWay.valid




      probeWay.io.deq.ready := getState



    /*************************************************************************/
    // control signals

    val cpuPriority = 3
    val otherNodesPriority  = 2
    val feedbackPriority  = 1
    val memCtrlPriority = 0

    inputArbiter.io.in(otherNodesPriority) <> io.in.otherNodes
    inputArbiter.io.in(cpuPriority) <> io.in.cpu
    inputArbiter.io.in(memCtrlPriority) <> io.in.memCtrl // priority is for lower producer
    inputArbiter.io.in(feedbackPriority) <> feedbackOutQueue.io.deq

    feedbackOutQueue.io.enq <> feedbackArbiter.io.out
    for ( i <- 0 until nParal){
        feedbackArbiter.io.in(i) <> feedbackInQueue(i).io.deq
        feedbackInQueue(i).io.enq.bits.addr := actionReg(i).io.deq.bits.addr
        feedbackInQueue(i).io.enq.bits.event := actionReg(i).io.deq.bits.event
        feedbackInQueue(i).io.enq.bits.data := actionReg(i).io.deq.bits.data
        feedbackInQueue(i).io.enq.valid := isFeedbackAction(i)
    }
    instruction <> inputArbiter.io.out

    /*************************************************************************/


    /*************************************************************************/
    // Elements

    stallInput := isLocked || tbe.io.isFull

    readTBE    := instruction.valid
    probeStart := instruction.fire() && instruction.bits.event === Events.HitEvent.U
    getState   := input.io.deq.fire()


    checkLock   := instruction.valid
    setLock     := instruction.fire()

    probeHit := input.io.deq.fire()
    hit := probeHit && (probeWay.io.deq.bits =/= nWays.U) && (stateMem.io.out.bits.state === States.ValidState.U)
    hitLD :=   hit && input.io.deq.bits.inst.event === Events.HitEvent.U
    missLD := probeHit &&  (input.io.deq.bits.inst.event === Events.HitEvent.U) && ((stateMem.io.out.bits.state === States.InvalidState.U) /*|| (cacheWayWire(nParal) === nWays.U)*/ )

    io.in.memCtrl.ready    :=  instruction.fire() && inputArbiter.io.chosen === memCtrlPriority.U
    io.in.cpu.ready        := instruction.fire() && inputArbiter.io.chosen === cpuPriority.U
    io.in.otherNodes.ready :=  instruction.fire() && inputArbiter.io.chosen === otherNodesPriority.U

    instUsed := (  !instruction.fire() & (input.io.enq.fire() | instUsed))
    instruction.ready := input.io.enq.ready && !tbe.io.isFull && !stallInput

    input.io.enq.valid := instruction.valid && !instUsed && !tbe.io.isFull && !stallInput
    tbe.io.outputTBE.ready := instruction.ready
    input.io.enq.bits.inst <> instruction.bits
    input.io.enq.bits.tbeOut :=  tbe.io.outputTBE.bits

    defaultState := State.default

    state := Mux(input.io.deq.bits.tbeOut.state.state =/= defaultState.state, input.io.deq.bits.tbeOut.state.state, Mux(stateMem.io.out.valid, stateMem.io.out.bits.state, defaultState.state ))
    tbeWay := input.io.deq.bits.tbeOut.way

    // TBE
    tbe.io.read.valid := readTBE
    tbe.io.read.bits.addr := instruction.bits.addr

    for (i <- 0 until nParal)  {
        inputTBE(i).state.state := dstOfSetState(i).state
        inputTBE(i).way         := actionReg(i).io.deq.bits.way
        (0 until nTBEFields ).map( n =>  inputTBE(i).fields(n) := Mux(maskField(i) === n.asUInt(), tbeFieldUpdateSrc(i), DontCare)) // @todo should be from of action

        tbe.io.write(i).bits.command := Mux(updateTBEFixedFields(i), tbe.write, tbeAction(i))
        tbe.io.write(i).bits.addr := actionReg(i).io.deq.bits.addr
        tbe.io.write(i).bits.inputTBE := inputTBE(i)
        tbe.io.write(i).bits.mask := Mux(updateTBEFixedFields(i), tbe.maskFixed.U , maskField(i))
        tbe.io.write(i).valid := isTBEAction(i) || updateTBEFixedFields(i)
    }

    // lock Mem
    lockMem.io.probe.in.bits.data := DontCare
    lockMem.io.probe.in.bits.addr := instruction.bits.addr
    lockMem.io.probe.in.valid     := checkLock
    lockMem.io.probe.in.bits.cmd  :=lockMem.PROBE.U // Checking

    lockMem.io.lock.in.bits.data  := DontCare
    lockMem.io.lock.in.bits.addr  := instruction.bits.addr
    lockMem.io.lock.in.valid      := setLock
    lockMem.io.lock.in.bits.cmd   :=lockMem.LOCK.U // Locking

    isLocked := (Mux(lockMem.io.probe.out.valid, lockMem.io.probe.out.bits, false.B))
    for (i <- 0 until nParal)  {
        lockMem.io.unLock(i).in.bits.data := DontCare
        lockMem.io.unLock(i).in.bits.addr := actionReg(i).io.deq.bits.addr
        lockMem.io.unLock(i).in.bits.cmd := lockMem.UNLOCK.U //unlock
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
    stateMem.io.in(nParal).bits.addr := input.io.deq.bits.inst.addr
    stateMem.io.in(nParal).bits.state := DontCare
    stateMem.io.in(nParal).bits.way :=  probeWay.io.deq.bits
    stateMem.io.in(nParal).valid := getState
    stateMemOutput := stateMem.io.out.bits


    routineQueue.io.enq.bits := uCodedNextPtr(routine)
    routineQueue.io.enq.valid := input.io.deq.valid  /*& !stallInput*/
    input.io.deq.ready := routineQueue.io.enq.ready  /*& !stallInput*/
    routineQueue.io.deq.ready := !pc.io.isFull
    // io.deq.bits to PC Vector

    // Replacer
    addrReplacer := addrToSet(input.io.deq.bits.inst.addr)
    replacerWayWire := replacer.get_replace_way(replStateReg(addrReplacer))

    when(missLD) { //when a miss happens
        replStateReg(addrReplacer) := replacer.get_next_state(replStateReg(addrReplacer), replacerWayWire)
    }

    wayInputCache := RegEnable(Mux( tbeWay === nWays.U , probeWay.io.deq.bits, tbeWay ), nWays.U, input.io.deq.fire())
    replaceWayInputCache := RegEnable(replacerWayWire, nWays.U, input.io.deq.fire())
    inputToPC := RegEnable(input.io.deq.bits.inst, InstBundle.default, input.io.deq.fire())


    for (i <- 0 until nParal) {
        isTBEAction(i) :=   (actionReg(i).io.deq.bits.action.actionType === 1.U) && actionReg(i).io.deq.valid
        isCacheAction(i) := (actionReg(i).io.deq.bits.action.actionType === 0.U) && actionReg(i).io.deq.valid
        isFeedbackAction(i) := ((actionReg(i).io.deq.bits.action.actionType === 2.U)) && actionReg(i).io.deq.valid
        isStateAction(i) := (actionReg(i).io.deq.bits.action.actionType === 3.U) && actionReg(i).io.deq.valid
        isMemAction(i) := (actionReg(i).io.deq.bits.action.actionType  === 4.U) && actionReg(i).io.deq.valid
        isCompAction(i) :=  (actionReg(i).io.deq.bits.action.actionType >= 8.U) && actionReg(i).io.deq.valid


        updateTBEFixedFields(i)   := isStateAction(i)
        endOfRoutine(i)   := isStateAction(i)

        compUnit(i).io.clear := endOfRoutine(i)

        actionReg(i).io.enq.bits.action.signals := sigToAction(actionRom(i)(pcWire(i).pc))
        actionReg(i).io.enq.bits.action.actionType := sigToActType(actionRom(i)(pcWire(i).pc))
        actionReg(i).io.enq.bits.addr := pcWire(i).addr
        actionReg(i).io.enq.bits.way  := Mux(updateWay(i), cacheWayWire(i), pcWire(i).way)
        actionReg(i).io.enq.bits.data := pcWire(i).data
        actionReg(i).io.enq.bits.replaceWay := pcWire(i).replaceWay
        actionReg(i).io.enq.bits.event := pcWire(i).event

        firstLineNextRoutine(i) := actionRom(i)(pcWire(i).pc).asUInt() === ActionList.actions("NOP").asUInt()// NOP
        updatedPC(i) := Mux(firstLineNextRoutine(i), pcWire(i).pc, pcWire(i).pc + 1.U + compUnit(i).io.pc )
        updatedPCValid(i) := !firstLineNextRoutine(i)

        updateWay(i) := pc.io.read(i).out.bits.way === nWays.U & cache.io.cpu(i).resp.fire()
        pc.io.read(i).in.bits.data.addr := DontCare //
        pc.io.read(i).in.bits.data.way := Mux(updateWay(i), cacheWayWire(i), pc.io.read(i).out.bits.way  )
        pc.io.read(i).in.bits.data.data := DontCare //
        pc.io.read(i).in.bits.data.pc := updatedPC(i)
        pc.io.read(i).in.bits.data.replaceWay := DontCare
        pc.io.read(i).in.bits.data.event := DontCare
        pc.io.read(i).in.bits.data.valid := updatedPCValid(i) // @todo should be changed for stall

        pc.io.read(i).in.valid := DontCare // @todo Should be changed probably
        pcWire(i) <> pc.io.read(i).out.bits

        tbeAction(i) := sigToTBECmd(actionReg(i).io.deq.bits.action.signals)
        cacheAction(i) := Mux(isCacheAction(i), actionReg(i).io.deq.bits.action.signals, 0.U(nSigs.W))
        stateAction(i) := isStateAction(i)

        dstOfSetState(i).state := Mux( isStateAction(i), sigToState (actionReg(i).io.deq.bits.action.signals), State.default.state)

        maskField(i) := sigToTBEOp1(actionReg(i).io.deq.bits.action.signals)// @todo from actions
//        tbeFieldUpdateSrc(i) := 1.U // @todo should be replaced with the one below
         tbeFieldUpdateSrc(i) := compUnit(i).io.reg_file(sigToTBEOp2(actionRom(i)(pcWire(i).pc)))


        /************Computation*****************************/
        compUnitInput1(i).io.in.data := DontCare //   Real Dont Care, no data for the first operand
        compUnitInput1(i).io.in.tbe  := DontCare // @todo should be connected to tbe mux
        compUnitInput1(i).io.in.hardCoded := DontCare // Real Dont Care, no hardcoded for the first operand
        compUnitInput1(i).io.in.select := sigToCompOpSel1(actionReg(i).io.deq.bits.action.actionType) // 0: Reg, 1:TBE
        compUnit(i).io.op1 <> compUnitInput1(i).io.out


        compUnitInput2(i).io.in.data :=  actionReg(i).io.deq.bits.data
        compUnitInput2(i).io.in.tbe  := DontCare // @todo should be connected to tbe?
        compUnitInput2(i).io.in.hardCoded := actionReg(i).io.deq.bits.action.signals(instructionWidth - 1, compUnit(i).Op1End())
        compUnitInput2(i).io.in.select := sigToCompOpSel2(actionReg(i).io.deq.bits.action.actionType) // 0: Reg, 1:TBE, 2: Data, 3: hardcoded
        compUnit(i).io.op2 <> compUnitInput2(i).io.out

        compUnit(i).io.instruction.bits := actionReg(i).io.deq.bits.action.signals
        compUnit(i).io.instruction.valid := isCompAction(i)

        actionReg(i).io.deq.ready := !stall
        actionReg(i).io.enq.valid := pcWire(i).valid  // @todo enq ready should be used for controlling  updated pc

        sets(i) := addrToSet(pc.io.read(i).out.bits.addr)
    }

    pc.io.write.bits.addr := inputToPC.addr
    pc.io.write.bits.way := wayInputCache
    pc.io.write.bits.replaceWay := replaceWayInputCache
    pc.io.write.bits.pc := routineQueue.io.deq.bits
    pc.io.write.bits.data := inputToPC.data
    pc.io.write.bits.event := inputToPC.event
    pc.io.write.bits.valid := true.B
    pc.io.write.valid := routineQueue.io.deq.fire()

    /*************************************************************************************/

    // Cache Logic
    for (i <- 0 until nParal) {
        cache.io.cpu(i).req.bits.way := actionReg(i).io.deq.bits.way
        cache.io.cpu(i).req.bits.command := cacheAction(i)
        cache.io.cpu(i).req.bits.addr := actionReg(i).io.deq.bits.addr
        cache.io.cpu(i).req.valid := actionReg(i).io.deq.fire()
        cache.io.cpu(i).req.bits.data := actionReg(i).io.deq.bits.data
        cache.io.cpu(i).req.bits.replaceWay := actionReg(i).io.deq.bits.replaceWay
    }

    cache.io.probe.req.bits.command := Mux(probeStart, sigToAction(ActionList.actions("Probe")), 0.U)
    cache.io.probe.req.bits.addr := Mux(probeStart, instruction.bits.addr, 0.U)
    cache.io.probe.req.bits.way := DontCare
    cache.io.probe.req.valid := probeStart
    cache.io.probe.req.bits.replaceWay := DontCare

    cache.io.bipassLD.in.valid := mimoQ.io.deq.valid && (mimoQ.io.deq.bits(0).way =/= nWays.U)
    cache.io.bipassLD.in.bits.addr  := mimoQ.io.deq.bits(0).addr
    cache.io.bipassLD.in.bits.way := mimoQ.io.deq.bits(0).way
    dataValid := cache.io.bipassLD.out.valid

    mimoQ.io.deq.ready := true.B

    for (i <- 0 until nParal) {
        outReqArbiter.io.in(i).bits.req.data:= Mux(sigToDQSrc(actionReg(i).io.deq.bits.action.signals).asBool(), compUnit(i).io.reg_file(sigToDQRegSrc(actionReg(i).io.deq.bits.action.signals)), actionReg(i).io.deq.bits.addr)
        outReqArbiter.io.in(i).bits.req.addr := actionReg(i).io.deq.bits.addr
        outReqArbiter.io.in(i).bits.req.inst:= 0.U // 0 for reading
        outReqArbiter.io.in(i).bits.dst:= nCache.U  // temp for 2 cache(0-nCache-1) and one memCtrl(2)
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
    respPortQueue(nParal).io.enq.bits.addr  := RegNext(cache.io.bipassLD.in.bits.addr)
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



    when( probeWay.io.deq.fire()){
        printf(p"Cache: ${ID} req from ${RegNext(inputArbiter.io.chosen)} Addr: ${RegNext(inputArbiter.io.out.bits.addr)}\n")
        when(RegNext(inputArbiter.io.chosen) === cpuPriority.U){
            printf(p"Cache: ${ID} ")
            when(hitLD){
                printf(p" Load hit for addr ${input.io.deq.bits.inst.addr}\n")
            }.elsewhen(isLocked){
                printf(p"addr ${input.io.deq.bits.inst.addr} is locked\n")
            }.elsewhen(RegNext(tbe.io.isFull)){
                printf(p"TBE is full addr ${input.io.deq.bits.inst.addr}\n")
            }.elsewhen(hit){
                printf(p"Hit (store probably) for addr ${input.io.deq.bits.inst.addr}\n")
            }.otherwise{
                printf(p"miss for addr ${input.io.deq.bits.inst.addr}\n")
            }
        }
    }

    if (cacheID == 0){
        BoringUtils.addSource(missLD, "missLD_0")
        BoringUtils.addSource(hitLD,  "hitLD_0" )
        BoringUtils.addSource(instruction.fire, "instCount_0")
        BoringUtils.addSource(instruction.fire && inputArbiter.io.chosen === cpuPriority.U, "cpuReq_0")
        BoringUtils.addSource(instruction.fire && inputArbiter.io.chosen === memCtrlPriority.U, "memCtrlReq_0")
        BoringUtils.addSource(instruction.fire && instruction.bits.event === Events.HitEvent.U , "ldReq_0")
    }

    // if (cacheID == 1){
    //     BoringUtils.addSource(missLD, "missLD_1")
    //     BoringUtils.addSource(hitLD,  "hitLD_1" )
    //     BoringUtils.addSource(instruction.fire, "instCount_1")
    //     BoringUtils.addSource(instruction.fire && inputArbiter.io.chosen === cpuPriority.U, "cpuReq_1")
    //     BoringUtils.addSource(instruction.fire && inputArbiter.io.chosen === memCtrlPriority.U, "memCtrlReq_1")
    //     BoringUtils.addSource(instruction.fire && instruction.bits.event === Events.HitEvent.U , "ldReq_1")
    // }

}
