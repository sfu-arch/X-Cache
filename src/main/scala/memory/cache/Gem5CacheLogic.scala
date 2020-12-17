package dandelion.memory.cache

import chisel3._
import chisel3.util._
import chipsalliance.rocketchip.config._
import dandelion.config._
import dandelion.util._
import dandelion.interfaces._
import dandelion.interfaces.axi._
import chisel3.util.experimental.loadMemoryFromFile

import dandelion.junctions._
import dandelion.shell._


trait HasCacheAccelParams extends HasAccelParams with HasAccelShellParams {

  val nWays = accelParams.nways
  val nSets = accelParams.nsets
  val nStates = accelParams.nstates
  val addrLen = accelParams.addrlen
  val eventLen = 8
  val nCom = 8
  val dataLen = 512

  val stateLen = log2Ceil(nStates)
  val wBytes = xlen / 8
  val bBytes = accelParams.cacheBlockBytes
  val bBits = bBytes << 3
  val blen = log2Ceil(bBytes)
  val nData = bBits / memParams.dataBits
  val dataBeats = nData

  val setLen = log2Ceil(nSets)
  val wayLen = log2Ceil(nWays)

  val nWords = bBits / xlen
  val wordLen = log2Ceil(nWords)
  //val taglen = xlen - (setLen + blen)
  val taglen = addrLen - (setLen + blen + byteOffsetBits )

  val byteOffsetBits = log2Ceil(wBytes)
  override val nSigs = accelParams.nSigs
  //

}

class DecoderIO (nSigs: Int)(implicit val p:Parameters) extends Bundle {

    val inAction = Input(UInt(nSigs.W))
    val outSignals = Output(Vec(nSigs, Bool()))

}

class Decoder (implicit val p:Parameters) extends Module
with HasCacheAccelParams
with HasAccelShellParams {


    val io = IO(new DecoderIO(nSigs))

    io.outSignals := io.inAction.asBools()
}


class CacheCPUIO(implicit p: Parameters) extends DandelionGenericParameterizedBundle(p) {
  val abort = Input(Bool())
  val flush = Input(Bool())
  val flush_done = Output(Bool())
  val req = Flipped(Decoupled(new MemReq))
  val resp = Output(Valid(new MemResp))
}

class CacheBankedMemIO[T <: Data](D: T, nInput :Int) (implicit val p: Parameters) extends Bundle
  with HasCacheAccelParams
  with HasAccelShellParams {


  val bank = Output(UInt(wayLen.W))
  val address = Output(UInt(setLen.W))
  val isRead = Output(Bool())
  val outputValue = Output(D.cloneType)
  val inputValue = Input(Vec(nInput, D.cloneType))
  val valid = Input(Bool())
}

class StateMemIO() (implicit val p: Parameters) extends Bundle
  with HasCacheAccelParams
  with HasAccelShellParams {

  val isSet = Output(Bool())
  val stateIn = Input(new State)
  val stateOut = Output(new State)
  val addr = Output(UInt(addrLen.W))

}

// @todo bipass for reading
class Gem5CacheLogic(val ID:Int = 0)(implicit  val p: Parameters) extends Module
  with HasCacheAccelParams
  with HasAccelShellParams {

  val io = IO(new Bundle {

    val cpu = new CacheCPUIO
    val mem = new AXIMaster(memParams)
    val dataMem = new CacheBankedMemIO(UInt(xlen.W), nWords)
    val metaMem = new CacheBankedMemIO(new MetaData(), nWays)
    val stateMem = new StateMemIO()
  })


  val decoder = Module(new Decoder)

  io.cpu <> DontCare
  io.mem <> DontCare
  io.metaMem <> DontCare
  io.dataMem <> DontCare
  io.stateMem <> DontCare

  val Axi_param = memParams

  // cache states

  val (sigLoadWays :: sigFindInSet ::sigAddrToWay :: sigPrepMDRead ::sigPrepMDWrite:: sigAllocate :: sigDeallocate :: sigWrite :: sigRead :: sigDataReq:: Nil) = Enum(nSigs)

  val (s_IDLE :: s_READ_CACHE :: s_WRITE_CACHE :: s_WRITE_BACK :: s_WRITE_ACK :: s_REFILL_READY :: s_REFILL :: Nil) = Enum(7)
  val st = RegInit(s_IDLE)

  val s_flush_IDLE :: s_flush_START :: s_flush_ADDR :: s_flush_WRITE :: s_flush_WRITE_BACK :: s_flush_WRITE_ACK :: Nil = Enum(6)
  val flush_state = RegInit(s_flush_IDLE)

  val cNone :: cProbe :: cAlloc :: cDeAlloc :: cIntRead :: cExtRead :: cSetState :: cIntWrite :: Nil = Enum(nCom)

  val (stAlIdle :: stAlLookMeta :: stAlCreate :: Nil) = Enum(3)
  val stAlReg = RegInit(stAlIdle)

  val (stDeAlIdle :: stDeAlLookMeta :: stDeAlRemove :: Nil) = Enum(3)
  val stDeAlReg = RegInit(stDeAlIdle)

  val (stIntRdIdle :: stIntRdLookMeta :: stIntRdPassData :: Nil) = Enum(3)
  val stIntRdReg = RegInit(stIntRdIdle)

  val (stExtRdIdle :: stExtRdAddr :: stExtRdData :: Nil) = Enum(3)
  val stExtRdReg = RegInit(stExtRdIdle)

  val (stIntWrIdle :: stIntWrAddr :: stIntWrData :: Nil) = Enum(3)
  val stIntWrReg = RegInit(stIntWrIdle)

  //Flush Logic
  val dirty_block = nSets

  // memory
  val valid = RegInit(VecInit(Seq.fill(nSets * nWays)(false.B)))
  val validTag = RegInit(VecInit(Seq.fill(nSets * nWays)(false.B)))
  val dirty = VecInit(Seq.fill(nSets)(0.U(nWays.W)))

  val addr_reg = RegInit(0.U(io.cpu.req.bits.addr.getWidth.W))
  val cpu_data = RegInit(0.U(io.cpu.req.bits.data.getWidth.W))
  val cpu_mask = RegInit(0.U(io.cpu.req.bits.mask.getWidth.W))
  val cpu_tag = RegInit(0.U(io.cpu.req.bits.tag.getWidth.W))
  val cpu_iswrite = RegInit(0.U(io.cpu.req.bits.iswrite.getWidth.W))
  val cpu_command = RegInit(0.U(io.cpu.req.bits.command.getWidth.W))
//  println (s" \n ******* \n len ${io.cpu.req.bits.command.getWidth.W } \n")
  val count_set = RegInit(false.B)
  val inputState = RegInit(State.default)

  val tag = RegInit(0.U(taglen.W))
  val set = RegInit(0.U(setLen.W))
  val offset = RegInit(0.U(byteOffsetBits.W))
  val way = WireInit(0.U((nWays + 1).W))
  val state = RegInit(State.default)

  val dataBuffer = RegInit(VecInit(Seq.fill(nData)(0.U(xlen.W))))

  val findInSetSig = Reg(Bool())
  val addrToLocSig = Wire(Bool())
  val dataValidCheckSig = Wire(Bool())

  val start = WireInit(VecInit(Seq.fill(nCom)(false.B)))
  val res = WireInit(VecInit(Seq.fill(nCom)(false.B)))
  val done = WireInit(false.B)

  val dataValid = WireInit(false.B)

  val commandValid = Reg(Bool())

  // Counters
  require(nData > 0)
  val (read_count, read_wrap_out) = Counter(io.mem.r.fire(), nData)
  val (write_count, write_wrap_out) = Counter(io.mem.w.fire(), nData)





  commandValid := io.cpu.req.fire()

  when(io.cpu.req.fire()) {
    addr_reg := io.cpu.req.bits.addr
    cpu_tag := io.cpu.req.bits.tag
    cpu_data := io.cpu.req.bits.data
    cpu_mask := io.cpu.req.bits.mask
    cpu_command := io.cpu.req.bits.command
    cpu_iswrite := io.cpu.req.bits.iswrite
    tag := addrToTag(io.cpu.req.bits.addr)
    set := addrToSet(io.cpu.req.bits.addr)
    offset := addrToOffset(io.cpu.req.bits.addr)
    //    inputState := io.cpu.req.bits.state
  }

//  when (io.cpu.req.fire()){
//  }.otherwise{
//    cpu_command := 0.U(nSigs.W)
//  }

  val signals = Wire(Vec(nSigs, Bool()))

  decoder.io.inAction := cpu_command
  signals := decoder.io.outSignals



  val readyForCmd = RegInit(true.B)

  val loadWaysMeta = Wire(Bool())
  val loadLineData = Wire(Bool())
  val loadState = Wire(Bool())

  //  val loadFromMemSig = Wire(Bool())
  val loadDataBuffer = Wire(Bool())

  val waysInASet = Reg(Vec(nWays, new MetaData()))
  val cacheLine = Reg(Vec(nWords, UInt(xlen.W)))

  val addrReadValid = Wire(Bool())
  val dataReadReady = Wire(Bool())

  val addrWriteValid = Wire(Bool())
  val dataWriteValid = Wire(Bool())

  val wayInvalid = Wire(Bool())


  when(loadLineData) {
    cacheLine := io.dataMem.inputValue
  }

  when(loadState) {
    state := io.stateMem.stateIn
  }

  when(dataValidCheckSig) {
    dataValid := valid(set * nSets.U + way)
  }

//  when(done) {
//    readyForCmd := true.B
//  }.elsewhen(commandValid) {
//    readyForCmd := false.B
//  }

  io.mem.ar.bits.addr := addr_reg
  io.mem.ar.valid := addrReadValid
  io.mem.r.ready := dataReadReady

  io.mem.aw.bits.addr := addr_reg
  io.mem.aw.valid := addrWriteValid
  io.mem.w.valid := dataWriteValid


  when(loadDataBuffer) {
    dataBuffer(read_count) := io.mem.r.bits.data
  }

//  io.cpu.req.ready := readyForCmd
  io.cpu.req.ready := true.B

  //loadWaysMeta := false.B
  loadLineData := false.B
  //findInSetSig := false.B
  addrToLocSig := false.B
  dataValidCheckSig := false.B
  loadState := false.B

  addrWriteValid := false.B
  dataWriteValid := false.B
  addrReadValid := false.B
  dataReadReady := false.B
  loadDataBuffer := false.B

  //  val (block_count, block_wrap) = Counter(flush_state === s_flush_BLOCK, nWords)
  //  val (way_count, way_wrap) = Counter(flush_state === s_flush_START, nWays)
  //  val (set_count, set_wrap) = Counter(flush_state === s_flush_START && way_wrap , nSets)


  //val block_addr_tag_reg = RegInit(0.U(io.cpu.req.bits.addr.getWidth.W))
  //  val dirty_cache_block = Cat((dataMem map (_.read(set_count - 1.U).asUInt)).reverse)
  //  val block_rmeta = RegInit(init = MetaData.default)
  //  val flush_mode = RegInit(false.B)

  val is_idle = st === s_IDLE
  val is_read = st === s_READ_CACHE
  val is_write = st === s_WRITE_CACHE
  val is_alloc = st === s_REFILL && read_wrap_out
  val is_alloc_reg = RegNext(is_alloc)


  //  val wen = is_write && (hit || is_alloc_reg) && !io.cpu.abort || is_alloc
  //  val ren = !wen && (is_idle || is_read) && io.cpu.req.valid
  //  val ren_reg = RegNext(ren)

  //  val rmeta = RegNext(next = metaMem.read(addrToSet(io.cpu.req.bits.addr))(0), init = MetaData.default)
  //  val rdata = RegNext(next = cache_block, init = 0.U(cache_block_size.W))
  //  val rdata_buf = RegEnable(rdata, ren_reg)
  //  val refill_buf = RegInit(VecInit(Seq.fill(nData)(0.U(Axi_param.dataBits.W))))
  //  val read = Mux(is_alloc_reg, refill_buf.asUInt, Mux(ren_reg, rdata, rdata_buf))

  //  hit := (valid(set) & rmeta.tag === tag)

  def addrToSet(addr: UInt): UInt = {
    val set = addr(setLen + blen + byteOffsetBits, blen + byteOffsetBits)
    set.asUInt()
  }

  def addrToOffset(addr: UInt): UInt = {
    val offset = addr(blen - 1, byteOffsetBits)
    offset.asUInt()
  }

  def addrToTag(addr: UInt): UInt = {
    val tag = Wire(UInt(taglen.W))
    tag := (addr(addrLen - 1, setLen + blen + byteOffsetBits))
    tag.asUInt()
  }


  def addrToWay(set: UInt): (UInt) = {

    val way = Wire(UInt((nWays + 1).W))
    way := nWays.U
    for (i <- 0 until nWays) {
      when(validTag(set * nWays.U + i.U) === false.B) {
        way := i.asUInt()
      }
    }
    (way.asUInt())
  }

  def tagValidation(set: UInt, way: UInt): Bool = {
    validTag(set * nWays.U + way) := true.B
    //    printf(p"Valid Tag: ${validTag(set*nWays.U + way)}\n")
    true.B
  }

  def tagInvalidation(set: UInt, way: UInt): Bool = {
    validTag(set).bitSet(way, false.B)
    true.B
  }


  def prepForRead[T <: Data](D: CacheBankedMemIO[T]): Unit = {
    D.isRead := true.B

    D match {
      case io.dataMem => {
        //        loadLineData := true.B
        D.address := set * nSets.U + way
        D.bank := 0.U
      }
      case io.metaMem => {
        //        loadWaysMeta := true.B
        D.bank := 0.U
        D.address := set
      }
    }
  }
  def prepForWrite[T <: Data](D: CacheBankedMemIO[T]): Unit = {
    D.isRead := false.B

    D match {
      case io.dataMem => {
        //        loadLineData := true.B
        D.address := set * nSets.U + way
        D.bank := offset
      }
      case io.metaMem => {
        //        loadWaysMeta := true.B
        D.bank := way
        D.address := set
      }
    }
  }


  def findInSet(set: UInt, tag: UInt): UInt = {
    MD.tag := tag
    val wayWire = Wire(UInt((nWays + 1).W))
    wayWire := nWays.U
    for (i <- 0 until nWays) yield {
      when(validTag(set * nWays.U + i.U) === true.B && waysInASet(i).tag.asUInt() === MD.tag) {
        wayWire := i.asUInt()
      }
    }
    wayWire.asUInt()
  }

  def tagging(tag: UInt, set: UInt, way: UInt): Bool = {
    MD.tag := tag
    io.metaMem.bank := (way)
    io.metaMem.address := (set)
    io.metaMem.isRead := false.B
    io.metaMem.outputValue := MD
    tagValidation(set, way)
  }

  def detaggin(set: UInt, way: UInt): Bool = {
    tagInvalidation(set, way)
  }

  //  def Probing (addr:UInt): (UInt,UInt) ={
  //    val set = addrToSet(addr)
  //    val way = rplPolicy (set)
  //    (set, way)
  //  }
  def ReadInternal(set: UInt, way: UInt): Unit = {
    findInSetSig := true.B
    prepForRead(io.dataMem)
    //    io.dataMem.address := set*nSets.U + way
    //    io.dataMem.bank := 0.U
    //    io.dataMem.isRead := true.B
  }

  def SetState(state: State): Bool = {
    val addr = Wire(UInt(addrLen.W))
    addr := set * nSets.U + way
    io.stateMem.addr := addr
    io.stateMem.stateOut := state
    io.stateMem.isSet := true.B
    true.B
  }

  def GetState(): Bool = {
    val addr = Wire(UInt(addrLen.W))
    addr := set * nSets.U + way
    io.stateMem.addr := addr
    io.stateMem.addr := false.B
    loadState := true.B
    true.B
  }



  //  def Replace (set: UInt): UInt={
  //
  //  }
  // FSM for each command
  //  way :=


  val readMetaData = Wire(Bool())
  val targetWay = Reg(UInt((wayLen + 1).W))

  val MD = Wire(new MetaData())
  MD.tag := tag

  wayInvalid := (targetWay === nWays.U)


    //  val signals = WireInit(VecInit(Seq.fill(nSigs)(false.B)))
  val allocate = WireInit(signals(sigAllocate))
  val prepMDRead = WireInit(signals(sigPrepMDRead))
  val prepMDWrite = WireInit(signals(sigPrepMDWrite))
  val deallocate = WireInit(signals(sigDeallocate))
  val addrToWaySig = WireInit(signals(sigAddrToWay))
  val writeSig = WireInit(signals(sigWrite))
  val readSig = WireInit(signals(sigRead))
  loadWaysMeta := signals(sigLoadWays)
  findInSetSig := signals(sigFindInSet)

  val hit = Wire(Bool())
  hit := (readSig & !wayInvalid)

  val (missCount, _) = Counter(!hit, 1000)
  val (hitCount,  _) = Counter( hit , 1000)

  readMetaData := !(sigAllocate | sigDeallocate)




  when (!wayInvalid) {
    way := targetWay
  }.elsewhen(addrToWaySig){
    way := addrToWay(set)
  }.otherwise{
    way := nWays.U
  }

  printf(p"signals  ${signals}\n")
//  printf(p"loadMeta  ${loadWaysMeta}\n")
//  printf(p"findInSet ${findInSetSig}\n")
  printf(p"targetWay ${targetWay} \n")
//  printf(p"prepMDWrite ${prepMDWrite} \n")
  printf(p"way ${way}\n")

  when(prepMDRead){
    prepForRead(io.metaMem)
  }.elsewhen(prepMDWrite){
    prepForWrite(io.metaMem)
  }

  when(writeSig){
    prepForWrite(io.dataMem)
  }.elsewhen(readSig){
    prepForRead(io.dataMem)
  }

  when(writeSig){
    io.dataMem.outputValue := cpu_data
  }.otherwise{
    io.dataMem.outputValue := DontCare
  }

  when(writeSig){
    valid(set * nWays.U + way) := true.B
  }

  when (readSig){
    dataBuffer := io.dataMem.inputValue
  }.otherwise{
    dataBuffer := dataBuffer
  }
  when(prepMDWrite){
      io.metaMem.outputValue := MD
  }.elsewhen(writeSig){
    io.dataMem.outputValue := cpu_data
  } .otherwise{
      io.metaMem.outputValue := DontCare
  }

  when(findInSetSig & loadWaysMeta){
    targetWay := findInSet(set,tag)
  }.otherwise{
    targetWay := targetWay
  }


//  when(allocate | deallocate){
//    io.metaMem.isRead := false.B
//  }.otherwise{
//    io.metaMem.isRead := true.B
//  }


  when(loadWaysMeta){
    waysInASet := io.metaMem.inputValue
  }.otherwise{
    waysInASet := waysInASet
  }


  when(allocate ) {
    validTag(set * nWays.U + way) := true.B
  }.elsewhen(deallocate){
    validTag(set * nWays.U + way) := false.B
  }



  




//  switch(stAlReg){
//    is (stAlIdle){
//      when(start(cAlloc)){
//        stAlReg := stAlLookMeta
//        loadWaysMeta := true.B
//      }.otherwise{
//        stAlReg := stAlIdle
//      }
//    }
//    is(stAlLookMeta){
//      findInSetSig := true.B
//      when(way === nWays.U){ // it's not there
//        stAlReg := stAlCreate
//      }.otherwise{
//        stAlReg := stAlIdle
////        resAl := true.B
//        res(cAlloc) := true.B
//        done := true.B
//
//      }
//    }
//    is (stAlCreate){
//      addrToLocSig := true.B
//      when(way === nWays.U){
//        stAlReg := stAlIdle
//        done := true.B
//      }.otherwise{
////        resAl := true.B
//        res(cAlloc) := true.B
//        done := true.B
//        tagging(tag,set,way)
//        stAlReg := stAlIdle
//        printf(p" way: ${way} set: ${set}\n")
//
//      }
//    }
//  }
//
//  switch (stDeAlReg){
//
//    is (stDeAlIdle){
//      when(start(cDeAlloc)) {
//        stDeAlReg := stDeAlLookMeta
//        loadWaysMeta := true.B
//      }.otherwise{
//        stDeAlReg := stDeAlIdle
//      }
//    }
//    is(stDeAlLookMeta){
//      findInSetSig := true.B
//      when(way === nWays.U){
//        stDeAlReg := stDeAlIdle
//        res(cDeAlloc) := false.B
//        done := true.B
//      }.otherwise{
//        stDeAlReg := stDeAlIdle
//        detaggin(set,way)
//        res(cDeAlloc) := true.B
//        done := true.B
//      }
//    }
//  }
//
//  switch(stIntRdReg){
//    is (stIntRdIdle) {
//      when(start(cIntRead)) {
//        stIntRdReg := stIntRdLookMeta
//        loadWaysMeta := true.B
//      }.otherwise {
//        stIntRdReg := stIntRdIdle
//      }
//    }
//    is (stIntRdLookMeta){
//      findInSetSig := true.B
//      dataValidCheckSig := true.B
//      when(way === nWays.U){
//        res(cIntRead) := false.B
//        done := true.B
//        stIntRdReg := stIntRdIdle
//      }.otherwise{
//        when(dataValid){
//          prepForRead(io.dataMem)
//          stIntRdReg := stIntRdPassData
//        }.otherwise{
//          res(cIntRead) := false.B
//          done := true.B
//        }
//      }
//    }
//
//    is (stIntRdPassData){
//      loadLineData := true.B
//      stIntRdReg := stIntRdIdle
//      done := true.B
//      res(cIntRead) := true.B
//    }
//  }
//
//  switch(stExtRdReg){
//
//    is(stExtRdIdle){
//      when(start(cExtRead) ){
//        stExtRdReg := stExtRdAddr
//        addrReadValid := true.B
//      }.otherwise{
//        stExtRdReg := stExtRdIdle
//      }
//    }
//
//    is (stExtRdAddr){
//      when (io.mem.ar.fire()){
//        dataReadReady := true.B
//        stExtRdReg := stExtRdData
//      }.otherwise{
//        stExtRdReg := stExtRdAddr
//      }
//    }
//
//    is (stExtRdData){
//      when(io.mem.r.fire()){
//        loadDataBuffer := true.B
//        when(read_wrap_out){
//          stExtRdReg := stExtRdIdle
//          done := true.B
//          res(cExtRead) := true.B
//        }.otherwise {
//          stExtRdReg := stExtRdData
//        }
//      }
//    }
//  }

//  switch (stIntWrReg){
//    is (stIntWrIdle){
//      when(start(cIntWrite)){
//        stIntWrReg := stIntWrAddr
//      }.otherwise{
//        stIntWrReg := stIntWrIdle
//      }
//    }
//
//    is (stIntWrAddr){
//
//
//    }
//
//  }


  io.cpu.resp.valid := false.B
//
//  switch(cpu_command) {
//
//    is(cAlloc) {
//
//        start(cAlloc) := commandValid
//        io.cpu.resp.valid := done
//    }
//
//    is(cDeAlloc){
//      start(cDeAlloc) := commandValid
//      io.cpu.resp.valid := done
//    }
//    is (cIntRead){
//        start(cIntRead) := commandValid
//        io.cpu.resp.valid := done
//    }
//    is(cExtRead){
//      start(cExtRead) := commandValid
//      io.cpu.resp.valid := done
//    }

//    is (cGetState){
//      val res = GetState()
//      io.cpu.resp.valid := res
//
//    }
//    is (cSetState){
//      val res = SetState(inputState)
//      io.cpu.resp.valid := res
//
//    }
//
//    is (cIntWrite){
//      //@todo should be completed
//      start(cIntWrite) := commandValid
//      io.cpu.resp.valid := done
//    }

//  }
//
//
//
//  // Read Mux
//  io.cpu.resp.bits.data := VecInit.tabulate(nWords)(i => read((i + 1) * xlen - 1, i * xlen))(offset)
//  io.cpu.resp.bits.tag := cpu_tag
//  io.cpu.resp.bits.valid := (is_write && hit) || (is_read && hit) || (is_alloc_reg && !cpu_iswrite)
//  io.cpu.resp.bits.iswrite := cpu_iswrite
//
//  //Extra input needs to be removed
//  //Extra input needs to be remove
//  io.cpu.resp.bits.tile := 0.U
//
//  // Can be: 1)Write hit, 2)Read hit, 3)Read miss
//  //  io.cpu.resp.valid := (is_write && hit) || (is_read && hit) || (is_alloc_reg && !cpu_iswrite)
//  //  io.cpu.req.ready := is_idle || (state === s_READ_CACHE && hit)
//
//
//
//  val wmeta = MetaData(tag)
//
//  //   is_alloc means cache hit, hence, if is_alloc is false, it means our mask should consider only the modified byte
//  //   and since wdata is masked with wmask we only duplicate the write data, otherwise, since mask doesn't do anything
//  //   with write data we make the proper wdata using refill_buf
//  val wmask = Mux(!is_alloc, (cpu_mask << Cat(offset, 0.U(byteOffsetBits.W))).asUInt.zext, (-1).asSInt()).asUInt()
//  val wdata = Mux(!is_alloc, Fill(nWords, cpu_data),
//    if (refill_buf.size == 1) io.mem.r.bits.data
//    else Cat(io.mem.r.bits.data, Cat(refill_buf.init.reverse)))
//
//
//  when(wen) {
//    //      valid := valid.bitSet(set_reg, true.B)
//    //      dirty := dirty.bitSet(set_reg, !is_alloc)
//
//    //        dirty_mem(dirty_block) := dirty_mem(dirty_block).bitSet(dirty_offset, !is_alloc)
//
//    //    when(is_alloc) {
//    //      metaMem.write(set, wmeta)
//    //    }
//    dataMem.zipWithIndex foreach { case (mem, i) =>
//      val data = VecInit.tabulate(wBytes)(k => wdata(i * xlen + (k + 1) * 8 - 1, i * xlen + k * 8))
//      mem.write(set, data, (wmask((i + 1) * wBytes - 1, i * wBytes)).asBools)
//      mem suggestName s"dataMem_${i}"
//    }
//  }
//
//  //First elemt is ID and it looks like ID tags a memory request
//  // ID : Is AXI request ID
//  // @todo: Change @id value for each cache
//
//  // AXI constants - statically defined
//  io.mem.setConst()
//
//  // read request

//
//  //   read data
//  io.mem.r.ready := state === s_REFILL
//  when(io.mem.r.fire()) {
//    refill_buf(read_count) := io.mem.r.bits.data
//  }
//
//
//  //  // Info
//  val is_block_dirty = valid(set_count)(way_count) && dirty(set_count)(way_count)
//  val block_addr = Cat(block_rmeta.tag, set_count - 1.U) << blen.U
//
//  // write addr
//  io.mem.aw.bits.addr := Cat(rmeta.tag, set) << blen.U
//  io.mem.aw.bits.addr := Mux(flush_mode, block_addr, Cat(rmeta.tag, set) << blen.U)
//  io.mem.aw.bits.len := (nData - 1).U
//  io.mem.aw.valid := false.B
//
//  // Write resp
//  io.mem.w.bits.data := VecInit.tabulate(dataBeats)(i => read((i + 1) * Axi_param.dataBits - 1, i * Axi_param.dataBits))(write_count)
//  io.mem.w.bits.data :=
//    Mux(flush_mode,
//      VecInit.tabulate(nData)(i => dirty_cache_block((i + 1) * Axi_param.dataBits - 1, i * Axi_param.dataBits))(write_count),
//      VecInit.tabulate(nData)(i => read((i + 1) * Axi_param.dataBits - 1, i * Axi_param.dataBits))(write_count))
//
//  io.mem.w.bits.last := write_wrap_out
//  io.mem.w.valid := false.B
//  io.mem.b.ready := false.B
//
//  io.cpu.flush_done := false.B
//
//  /**
//    * Dumping cache state should add for debugging purpose
//    */
//
//  //   io.state := state.asUInt()
//  // Cache FSM
//  val countOn = true.B // increment counter every clock cycle
//  val (counterValue, counterWrap) = Counter(countOn, 64 * 1024)
//  //clockCycles := clockCycles + 1.U
//
//  //  val is_dirty = valid(set) && dirty(set)
//  val is_dirty = false.B
//  switch(state) {
//    is(s_IDLE) {
//      when(io.cpu.req.valid && false.B) {
//        //state := Mux(io.cpu.req.bits.iswrite, s_WRITE_CACHE, s_READ_CACHE)
//        when(io.cpu.req.bits.iswrite) {
//          state := s_WRITE_CACHE
//          if (debug) {
//            printf("\nSTORE START: %d\n", counterValue)
//          }
//        }.otherwise {
//          state := s_READ_CACHE
//          if (debug) {
//            printf("\nLOAD START:  %d\n", counterValue)
//          }
//        }
//      }
//    }
//    is(s_READ_CACHE) {
//      when(hit) {
//        when(io.cpu.req.valid) {
//          when(io.cpu.req.bits.iswrite) {
//            state := s_WRITE_CACHE
//          }.otherwise {
//            state := s_READ_CACHE
//          }
//        }.otherwise {
//          state := s_IDLE
//          if (debug) {
//            printf("\nLOAD END: %d\n", counterValue)
//          }
//        }
//      }.otherwise {
//        io.mem.aw.valid := is_dirty
//        io.mem.ar.valid := !is_dirty
//        when(io.mem.aw.fire()) {
//          state := s_WRITE_BACK
//        }.elsewhen(io.mem.ar.fire()) {
//          state := s_REFILL
//        }
//      }
//    }
//    is(s_WRITE_CACHE) {
//      when(hit || is_alloc_reg || io.cpu.abort) {
//        state := s_IDLE
//        io.cpu.resp.valid := true.B
//        if (debug) {
//          printf("\nSTORE END: %d\n", counterValue)
//        }
//      }.otherwise {
//        if (debug) {
//          printf("\nSTORE MISS: %d\n", counterValue)
//        }
//        io.mem.aw.valid := is_dirty
//        io.mem.ar.valid := !is_dirty
//        when(io.mem.aw.fire()) {
//          state := s_WRITE_BACK
//        }.elsewhen(io.mem.ar.fire()) {
//          state := s_REFILL
//        }
//      }
//    }
//    is(s_WRITE_BACK) {
//      io.mem.w.valid := true.B
//      when(write_wrap_out) {
//        state := s_WRITE_ACK
//      }
//    }
//    is(s_WRITE_ACK) {
//      io.mem.b.ready := true.B
//      when(io.mem.b.fire()) {
//        state := s_REFILL_READY
//      }
//    }
//    is(s_REFILL_READY) {
//      io.mem.ar.valid := true.B
//      when(io.mem.ar.fire()) {
//        state := s_REFILL
//      }
//    }
//    is(s_REFILL) {
//      if (debug) {
//        printf(p"state: Refill\n")
//      }
//      when(read_wrap_out) {
//        state := Mux(cpu_iswrite.asBool(), s_WRITE_CACHE, s_IDLE)
//        when(!cpu_iswrite) {
//          if (debug) {
//            printf("\nLOAD END: %d\n", counterValue)
//          }
//        }
//      }
//    }
//  }
//
//  //Flush state machine
//  switch(flush_state) {
//    is(s_flush_IDLE) {
//      when(io.cpu.flush) {
//        flush_state := s_flush_START
//        flush_mode := true.B
//      }
//    }
//    is(s_flush_START) {
//      when(set_wrap) {
//        io.cpu.flush_done := true.B
//        flush_mode := false.B
//        flush_state := s_flush_IDLE
//      }.elsewhen(is_block_dirty) {
//        flush_state := s_flush_ADDR
//        //        block_rmeta := metaMem.read(set_count)
//      }
//    }
//    is(s_flush_ADDR) {
//      /**
//        * When cycle delay to read from metaMem
//        */
//      flush_state := s_flush_WRITE
//    }
//    is(s_flush_WRITE) {
//      if (clog) {
//        printf(p"Dirty block address: 0x${Hexadecimal(block_addr)}\n")
//        printf(p"Dirty block data: 0x${Hexadecimal(dirty_cache_block)}\n")
//      }
//
//      io.mem.aw.valid := true.B
//      io.mem.ar.valid := false.B
//
//      when(io.mem.aw.fire()) {
//        flush_state := s_flush_WRITE_BACK
//      }
//    }
//    is(s_flush_WRITE_BACK) {
//      if (clog) {
//        printf(p"Write data: ${VecInit.tabulate(nData)(i => dirty_cache_block((i + 1) * Axi_param.dataBits - 1, i * Axi_param.dataBits))(write_count)}\n")
//      }
//      io.mem.w.valid := true.B
//      when(write_wrap_out) {
//        flush_state := s_flush_WRITE_ACK
//      }
//    }
//    is(s_flush_WRITE_ACK) {
//      io.mem.b.ready := true.B
//      when(io.mem.b.fire()) {
//        flush_state := s_flush_START
//      }
//    }
//  }
}