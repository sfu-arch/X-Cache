package memGen.memory.cache

import chisel3._
import chisel3.util._
import chipsalliance.rocketchip.config._
import memGen.config._
import memGen.util._
import memGen.interfaces._
import memGen.interfaces.axi._
import chisel3.util.experimental.loadMemoryFromFile

import memGen.junctions._
import memGen.shell._

trait HasCacheAccelParams extends HasAccelParams with HasAccelShellParams {
  val nWays = accelParams.nways
  val nSets = accelParams.nsets
  override val addrLen = accelParams.addrlen
  val dataLen = accelParams.dataLen
  val eventLen = Events.eventLen
  val stateLen = States.stateLen

  //  val nStates = States.stateLen
  val nCom = 8
  val nParal = 1
  val pcLen = 16

  val wBytes = xlen >> 3
  val nWords = accelParams.nWords // 4

  val bBytes = accelParams.cacheBlockBytes // 32 Bytes (  nWords * Xlen)
  val bBits = bSize
  val blen = log2Ceil(nWords) // 2

  val byteOffsetBits = log2Ceil(wBytes) //
  val wordLen = byteOffsetBits



  val nData = bBits / memParams.dataBits
  val dataBeats = nData

  val setLen = log2Ceil(nSets)
  val wayLen = log2Ceil(nWays) + 1
  val cacheLen = log2Ceil(nSets*nWays)

  val taglen = addrLen - (setLen + blen + byteOffsetBits ) // 3 + 2

  val replacementPolicy = "random"

  override val nSigs = accelParams.nSigs
  val actionLen = accelParams.actionLen


  def addrToSet(addr: UInt): UInt = {
    val set = addr(setLen + blen + byteOffsetBits, blen + byteOffsetBits)
    set.asUInt()
  }

  def addrNoOffset (addr: UInt) :UInt  ={
    val addrNoOff = addr >> (blen + byteOffsetBits)
    addrNoOff.asUInt()
  }

  def sigToAction(sigs : Bits) :UInt = sigs.asUInt()(nSigs - 1, 0)
  def sigToActType(sigs : Bits) :UInt = sigs.asUInt()(nSigs+ 1, nSigs)
  def sigToState (sigs :Bits) : UInt = sigs.asUInt()(States.stateLen - 1, 0)
}

class CacheCPUIO(implicit p: Parameters) extends DandelionGenericParameterizedBundle(p) {
  val abort = Input(Bool())
  val flush = Input(Bool())
  val flush_done = Output(Bool())
  val req = Flipped(Decoupled(new MemReq))
  val resp = (Valid(new MemResp))
}

// @todo bipass for reading
class Gem5CacheLogic(val ID:Int = 0)(implicit  val p: Parameters) extends Module
  with HasCacheAccelParams
  with HasAccelShellParams {

  val io = IO(new Bundle {

    val cpu = new CacheCPUIO

    val metaMem =  Flipped (new MemBankIO(new MetaData())(dataLen = xlen, addrLen = setLen, banks = nWays, bankDepth = nSets, bankLen = wayLen))
    val dataMem =  Flipped (new MemBankIO(UInt(xlen.W)) (dataLen = xlen, addrLen= addrLen, banks =nWords, bankDepth= nSets * nWays, bankLen = nWords))

    val validBits = new RegIO(Bool(), nRead = 1)
    val validTagBits = new RegIO(Bool(), nWays)
//    val dirtyBits = new RegIO(Bool(), nWays)

  })
  val decoder = Module(new Decoder)

  io.cpu <> DontCare
  io.metaMem <> DontCare
  io.dataMem <> DontCare

  val Axi_param = memParams

  // cache states

  val (sigLoadWays :: sigFindInSet ::sigAddrToWay :: sigPrepMDRead ::sigPrepMDWrite:: sigAllocate :: sigDeallocate :: sigWrite :: sigRead :: sigDataReq:: Nil) = Enum(nSigs)

  val (s_IDLE :: s_READ_CACHE :: s_WRITE_CACHE :: s_WRITE_BACK :: s_WRITE_ACK :: s_REFILL_READY :: s_REFILL :: Nil) = Enum(7)
  val st = RegInit(s_IDLE)

  val s_flush_IDLE :: s_flush_START :: s_flush_ADDR :: s_flush_WRITE :: s_flush_WRITE_BACK :: s_flush_WRITE_ACK :: Nil = Enum(6)
  val flush_state = RegInit(s_flush_IDLE)

  val cNone :: cProbe :: cAlloc :: cDeAlloc :: cIntRead :: cExtRead :: cSetState :: cIntWrite :: Nil = Enum(nCom)

  val (stIntRdIdle :: stIntRdLookMeta :: stIntRdPassData :: Nil) = Enum(3)
  val stIntRdReg = RegInit(stIntRdIdle)

  val (stIntWrIdle :: stIntWrAddr :: stIntWrData :: Nil) = Enum(3)
  val stIntWrReg = RegInit(stIntWrIdle)

  //Flush Logic
  val dirty_block = nSets

  val addr_reg = RegInit(0.U(io.cpu.req.bits.addr.getWidth.W))
  val cpu_data = RegInit(0.U(io.cpu.req.bits.data.getWidth.W))
  val cpu_mask = RegInit(0.U(io.cpu.req.bits.mask.getWidth.W))
  val cpu_tag = RegInit(0.U(io.cpu.req.bits.tag.getWidth.W))
  val cpu_iswrite = RegInit(0.U(io.cpu.req.bits.iswrite.getWidth.W))
  val cpu_command = RegInit(0.U(io.cpu.req.bits.command.getWidth.W))
  val count_set = RegInit(false.B)

  val tag = RegInit(0.U(taglen.W))
  val set = RegInit(0.U(setLen.W))
  val wayInput = RegInit(nWays.U((wayLen + 1).W))
  val replaceWayInput = RegInit(nWays.U((wayLen + 1).W))

  val offset = RegInit(0.U(byteOffsetBits.W))
  val way = WireInit(0.U((wayLen + 1).W))

  val dataBuffer = RegInit(VecInit(Seq.fill(nData)(0.U(xlen.W))))

  val findInSetSig = Wire(Bool())
  val addrToLocSig = Wire(Bool())
  val dataValidCheckSig = Wire(Bool())

  val start = WireInit(VecInit(Seq.fill(nCom)(false.B)))
  val res = WireInit(VecInit(Seq.fill(nCom)(false.B)))
  val done = WireInit(false.B)
  val dataValid = WireInit(false.B)

  // Counters
  require(nData > 0)

  val signals = Wire(Vec(nSigs, Bool()))

  val loadWaysMeta = Wire(Bool())
  val loadLineData = Wire(Bool())
  //  val loadFromMemSig = Wire(Bool())
  val loadDataBuffer = Wire(Bool())

  val waysInASet = Reg(Vec(nWays, new MetaData()))
//  val cacheLine = Reg(Vec(nWords, UInt(xlen.W)))

  val addrReadValid = Wire(Bool())
  val dataReadReady = Wire(Bool())

  val addrWriteValid = Wire(Bool())
  val dataWriteValid = Wire(Bool())

  val wayInvalid = Wire(Bool())

  val is_idle = st === s_IDLE
  val is_read = st === s_READ_CACHE
  val is_write = st === s_WRITE_CACHE
  val is_alloc = st === s_REFILL //&& read_wrap_out
  val is_alloc_reg = RegNext(is_alloc)

  val readMetaData = Wire(Bool())
  val targetWayReg = RegInit(nWays.U((wayLen + 1).W))
  val targetWayWire = WireInit(nWays.U((wayLen + 1).W))

  val hit = Wire(Bool())

  /********************************************************************************/
  // Signals
  val allocate = WireInit(signals(sigAllocate))
  val prepMDRead = WireInit(signals(sigPrepMDRead))
  val prepMDWrite = WireInit(signals(sigPrepMDWrite))
  val deallocate = WireInit(signals(sigDeallocate))
  val addrToWaySig = WireInit(signals(sigAddrToWay))
  val writeSig = WireInit(signals(sigWrite))
  val readSig = WireInit(signals(sigRead))
  val dataReq = WireInit(signals(sigDataReq))

  loadWaysMeta := signals(sigLoadWays)
  findInSetSig := signals(sigFindInSet)
  /********************************************************************************/

  /********************************************************************************/
  // Latching
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
    wayInput := io.cpu.req.bits.way
    replaceWayInput := io.cpu.req.bits.replaceWay
  }
//
//  when(loadLineData) {
//    cacheLine := io.dataMem.read.outputValue
//  }

//  when(dataValidCheckSig) {
//    dataValid := validBits(set * nSets.U + way)
//  }

  dataValid := io.validBits.read.out.asUInt()
  io.validBits.read.in.bits.addr := set * nSets.U + way
  io.validBits.read.in.valid := dataValidCheckSig

  /********************************************************************************/



  /********************************************************************************/
  //Decoder
  decoder.io.inAction := cpu_command
  signals := decoder.io.outSignals
  /********************************************************************************/

  io.cpu.req.ready := true.B

  loadLineData := false.B
  addrToLocSig := false.B
  dataValidCheckSig := false.B

  addrWriteValid := false.B
  dataWriteValid := false.B
  addrReadValid := false.B
  dataReadReady := false.B
  loadDataBuffer := false.B


  //  val (block_count, block_wrap) = Counter(flush_state === s_flush_BLOCK, nWords)
  //  val (way_count, way_wrap) = Counter(flush_state === s_flush_START, nWays)
  //  val (set_count, set_wrap) = Counter(flush_state === s_flush_START && way_wrap , nSets)
  //  val block_addr_tag_reg = RegInit(0.U(io.cpu.req.bits.addr.getWidth.W))
  //  val dirty_cache_block = Cat((dataMem map (_.read(set_count - 1.U).asUInt)).reverse)
  //  val block_rmeta = RegInit(init = MetaData.default)
  //  val flush_mode = RegInit(false.B)


  //  val wen = is_write && (hit || is_alloc_reg) && !io.cpu.abort || is_alloc
  //  val ren = !wen && (is_idle || is_read) && io.cpu.req.valid
  //  val ren_reg = RegNext(ren)
  //  val rmeta = RegNext(next = metaMem.read(addrToSet(io.cpu.req.bits.addr))(0), init = MetaData.default)
  //  val rdata = RegNext(next = cache_block, init = 0.U(cache_block_size.W))
  //  val rdata_buf = RegEnable(rdata, ren_reg)
  //  val refill_buf = RegInit(VecInit(Seq.fill(nData)(0.U(Axi_param.dataBits.W))))
  //  val read = Mux(is_alloc_reg, refill_buf.asUInt, Mux(ren_reg, rdata, rdata_buf))
  //  hit := (valid(set) & rmeta.tag === tag)

  def addrToOffset(addr: UInt): UInt = {
    val offset = addr(blen + byteOffsetBits - 1, byteOffsetBits)
    offset.asUInt()
  }

  def addrToTag(addr: UInt): UInt = {
    val tag = Wire(UInt(taglen.W))
    tag := (addr(addrLen - 1, setLen + blen + byteOffsetBits))
    tag.asUInt()
  }

//  def addrToWay(set: UInt): (UInt) = {
//    val way = Wire(UInt((nWays + 1).W))
//    way := nWays.U
//    for (i <- 0 until nWays) {
//      when(validTag(set * nWays.U + i.U) === false.B) {
//        way := i.asUInt()
//      }
//    }
//    (way.asUInt())
//  }


  def prepForRead[T <: Data](D: MemBankIO[T]): Unit = {
    D.read.in.valid := true.B

    D match {
      case io.dataMem => {
        D.read.in.bits.address := set * nSets.U + way
        D.read.in.bits.bank := 0.U
      }
      case io.metaMem => {
        D.read.in.bits.bank := 0.U
        D.read.in.bits.address := set
      }
    }
  }

  def prepForWrite[T <: Data](D: MemBankIO[T]): Unit = {
    D.write.valid := true.B

    D match {
      case io.dataMem => {
        D.write.bits.address := set * nSets.U + way
//        D.write.bits.bank :=
        D.write.bits.bank := 1.U << nWords.U - 1.U
      }
      case io.metaMem => {
        D.write.bits.bank := 1.U << way
        D.write.bits.address := set
      }
    }
  }

  val MD = Wire(new MetaData())
  MD.tag := tag
  io.metaMem.write.valid := false.B
  io.dataMem.write.valid := false.B
  io.metaMem.read.in.valid := false.B
  io.dataMem.read.in.valid := false.B

  wayInvalid := (wayInput === nWays.U)

  val (missCount, _) = Counter( !hit, 1000)
  val (hitCount,  _) = Counter( hit , 1000)
  hit := (readSig & !wayInvalid)

  readMetaData := !(sigAllocate | sigDeallocate)

  val emptyLine = Module(new FindEmptyLine(nWays, addrLen))
  emptyLine.io.data := io.validTagBits.read.out

  val tagFinder = Module(new Find(new MetaData(), new MetaData(),nWays, addrLen))
  tagFinder.io.key := MD
  tagFinder.io.data := io.metaMem.read.outputValue
  tagFinder.io.valid := io.validTagBits.read.out

  when(addrToWaySig | (findInSetSig & loadWaysMeta)){
    io.validTagBits.read.in.bits.addr := set * nWays.U
  }.otherwise{
    io.validTagBits.read.in.bits.addr := 0.U
  }

  io.validTagBits.read.in.valid := addrToWaySig  | (findInSetSig & loadWaysMeta) // probing and allocating


  when (!wayInvalid) {
    way := wayInput
  }.elsewhen(addrToWaySig){
    way := Mux(emptyLine.io.value.valid, emptyLine.io.value.bits, replaceWayInput)
  }.otherwise{
    way := nWays.U
  }
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
    io.dataMem.write.bits.inputValue := cpu_data.asTypeOf(Vec(nWords, UInt(xlen.W)))
  }.otherwise{
    io.dataMem.write.bits.inputValue := DontCare
  }

  io.validBits.write.valid := writeSig
  io.validBits.write.bits.addr := (set * nWays.U + way)
  io.validBits.write.bits.value := writeSig
//
//  when(writeSig){
//    io.validBits.write.bits.value := true.B
//  }

  when (readSig){
    dataBuffer := io.dataMem.read.outputValue
  }.otherwise{
    dataBuffer := dataBuffer
  }
  io.metaMem.write.bits.inputValue:= DontCare

  when(prepMDWrite){
      io.metaMem.write.bits.inputValue(way) := MD
  }.otherwise{
      io.metaMem.write.bits.inputValue := DontCare
  }

  when((findInSetSig & loadWaysMeta)) { // probing way
    targetWayWire := Mux(tagFinder.io.value.valid, tagFinder.io.value.bits, nWays.U)
  }.elsewhen(addrToWaySig) { // allocate
    targetWayWire := way
  }.otherwise{
    targetWayReg := targetWayReg // @todo no completed
  }
  targetWayReg := targetWayWire

//  when(allocate | deallocate){
//    io.metaMem.isRead := false.B
//  }.otherwise{
//    io.metaMem.isRead := true.B
//  }

  when(loadWaysMeta){
    waysInASet := io.metaMem.read.outputValue
  }.otherwise{
    waysInASet := waysInASet
  }

  io.validTagBits.write.bits.addr := (set * nWays.U + way)
  io.validTagBits.write.valid := allocate | deallocate
  io.validTagBits.write.bits.value := allocate | !deallocate
//  when(allocate ) {
//    validTagBits := true.B
//  }.elsewhen(deallocate){
//    validTagBits(set * nWays.U + way) := false.B
//  }

  io.cpu.resp.bits.way := targetWayWire
  io.cpu.resp.bits.data := Cat(dataBuffer)
  io.cpu.resp.bits.iswrite := RegNext(readSig) 
  io.cpu.resp.valid    := addrToWaySig | (findInSetSig & loadWaysMeta) | RegNext(readSig) // @todo should be changed to sth more generic

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

//  val wmeta = MetaData(tag)
//
//  //   is_alloc means cache hit, hence, if is_alloc is false, it means our mask should consider only the modified byte
//  //   and since wdata is masked with wmask we only duplicate the write data, otherwise, since mask doesn't do anything
//  //   with write data we make the proper wdata using refill_buf
//  val wmask = Mux(!is_alloc, (cpu_mask << Cat(offset, 0.U(byteOffsetBits.W))).asUInt.zext, (-1).asSInt()).asUInt()
//  val wdata = Mux(!is_alloc, Fill(nWords, cpu_data),
//    if (refill_buf.size == 1) io.mem.r.bits.data
//    else Cat(io.mem.r.bits.data, Cat(refill_buf.init.reverse)))

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