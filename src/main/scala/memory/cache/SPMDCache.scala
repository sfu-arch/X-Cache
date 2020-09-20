package dandelion.memory.cache

import chisel3._
import chisel3.util._
import chipsalliance.rocketchip.config._
import dandelion.config._
import dandelion.util._
import dandelion.interfaces._
import dandelion.interfaces.axi._
import dandelion.junctions._
import dandelion.shell._

trait SPMDCacheParams extends CacheAccelParams{
  val wlen = log2Ceil(nWays)
  val timeLen = 32
}
class CacheMethod(cache: SPMDSPMDCache)(implicit p:Parameters) extends Module
  with SPMDCacheParams {

  def allocate ( address: UInt, time: UInt) {
    val numSet = UInt(slen.W)
    numSet = (adressToSet (address))
    for (numWay <- 0 until  nWays) {
      if (cache.metaMem[numSet][numWay].time === 0.U || cache.metaMem[numSet][numWay].time < globalTimer.getTime() ) {
        allocateWay(address, numSet, numWay.U, time)
        return
      }
    }

  }
  def allocateWay (address:UInt, numSet:UInt, numWay:UInt, time:UInt) {
    cache.metaMem[numSet][numWay].time = time
    cache.metaMem[numSet][numWay].tag = addressToTag(address)
    //cache[numSet].v.bitSet(numWay, true.B) = 0.U
  }
}
//class SPMDCacheModuleIO (cache:SPMDCache) (implicit p:Parameters) extends AccelBundle()(p)
//  with CacheAccelParams {
//  val ctrlCPUIO = new CacheIO
//  val ctrlMethod = new CacheMethod
//  val mem = new AXIMaster(memParams)
//}

class SPMDMetaData(implicit val p: Parameters) extends DandelionParameterizedBundle()(p) with SPMDCacheParams {
  val tag = UInt(taglen.W)
  val time = UInt (timeLen.W)
}

class SPMDCache(val ID: Int = 0, val debug: Boolean = false)(implicit val p: Parameters) extends Module
  with SPMDCacheParams
  with HasAccelShellParams {

  val io = IO(new Bundle {
    val cpu = new CacheCPUIO
    val mem = new DMEMaster
    //val controller = new SPMDCacheMethod
  })

  val Axi_param = memParams

  // cache states
  val (s_IDLE :: s_READ_CACHE :: s_WRITE_CACHE :: s_WRITE_BACK :: s_WRITE_ACK ::
    s_REFILL_READY :: s_REFILL :: Nil) = Enum(7)
  val state = RegInit(s_IDLE)

  val s_flush_IDLE :: s_flush_START :: s_flush_ADDR :: s_flush_WRITE :: s_flush_WRITE_BACK :: s_flush_WRITE_ACK :: Nil = Enum(6)
  val flush_state = RegInit(s_flush_IDLE)
  val flush_mode = RegInit(false.B)

  // memory
  val v = RegInit(0.U(nSets.W))
  val d = RegInit(0.U(nSets.W))
  val metaMem = SyncReadMem(nSets, new MetaData)
  val dataMem = Seq.fill(nWords)(SyncReadMem(nSets, Vec(wBytes, UInt(8.W))))

  val addr_reg = Reg(io.cpu.req.bits.addr.cloneType)
  val cpu_tag_reg = Reg(io.cpu.req.bits.tag.cloneType)
  val cpu_iswrite_reg = Reg(io.cpu.req.bits.iswrite.cloneType)
  val cpu_data = Reg(io.cpu.req.bits.data.cloneType)
  val cpu_mask = Reg(io.cpu.req.bits.mask.cloneType)

  // Counters
  require(dataBeats > 0)
  val (read_count, read_wrap_out) = Counter(io.mem.rd.data.fire(), dataBeats)
  val (write_count, write_wrap_out) = Counter(io.mem.wr.data.fire(), dataBeats)

  val (set_count, set_wrap) = Counter(flush_state === s_flush_START, nSets)
  val dirty_cache_block = Cat((dataMem map (_.read(set_count - 1.U).asUInt)).reverse)
  val block_rmeta = Reg(new MetaData)
  val read_rmeta = Wire(new MetaData)


  val is_idle = state === s_IDLE
  val is_read = state === s_READ_CACHE
  val is_write = state === s_WRITE_CACHE
  val is_alloc = state === s_REFILL && read_wrap_out
  val is_alloc_reg = RegNext(is_alloc)

  val hit = Wire(Bool())
  val wen = is_write && (hit || is_alloc_reg) && !io.cpu.abort || is_alloc
  val ren = !wen && (is_idle || is_read) && io.cpu.req.valid
  val ren_reg = RegNext(ren)

  val addr = io.cpu.req.bits.addr
  val idx = addr(slen + blen - 1, blen)
  val tag_reg = addr_reg(xlen - 1, slen + blen)
  val idx_reg = addr_reg(slen + blen - 1, blen)
  val off_reg = addr_reg(blen - 1, byteOffsetBits)

  val rmeta = metaMem.read(idx, ren)
  val rdata = Cat((dataMem map (_.read(idx, ren).asUInt)).reverse)
  val rdata_buf = RegEnable(rdata, ren_reg)
  val refill_buf = Reg(Vec(dataBeats, UInt(Axi_param.dataBits.W)))
  val read = Mux(is_alloc_reg, refill_buf.asUInt, Mux(ren_reg, rdata, rdata_buf))

  hit := v(idx_reg) && rmeta.tag === tag_reg

  io.cpu.req.ready := is_idle || (state === s_READ_CACHE && hit)

  // Read Mux
  io.cpu.resp.bits.data := VecInit.tabulate(nWords)(i => read((i + 1) * xlen - 1, i * xlen))(off_reg)
  io.cpu.resp.valid := is_idle || is_read && hit || is_alloc_reg && !cpu_mask.orR

  io.cpu.resp.bits.tag := cpu_tag_reg
  io.cpu.resp.bits.iswrite := cpu_iswrite_reg

  when(io.cpu.req.fire) {
    cpu_tag_reg := io.cpu.req.bits.tag
    cpu_iswrite_reg := io.cpu.req.bits.iswrite
  }

  when(io.cpu.resp.valid) {
    addr_reg := addr
    cpu_data := io.cpu.req.bits.data
    cpu_mask := io.cpu.req.bits.mask
  }

  val wmeta = Wire(new MetaData)
  wmeta.tag := tag_reg

  val wmask = Mux(!is_alloc, (cpu_mask << Cat(off_reg, 0.U(byteOffsetBits.W))).asUInt.zext(), (-1).S)
  val wdata = Mux(!is_alloc, Fill(nWords, cpu_data),
    if (refill_buf.size == 1) io.mem.rd.data.bits
    else Cat(io.mem.rd.data.bits, Cat(refill_buf.init.reverse)))
  when(wen) {
    v := v.bitSet(idx_reg, true.B)
    d := d.bitSet(idx_reg, !is_alloc)
    when(is_alloc) {
      metaMem.write(idx_reg, wmeta)
    }
    dataMem.zipWithIndex foreach { case (mem, i) =>
      val data = VecInit.tabulate(wBytes)(k => wdata(i * xlen + (k + 1) * 8 - 1, i * xlen + k * 8))
      mem.write(idx_reg, data, wmask((i + 1) * wBytes - 1, i * wBytes).asBools())
      mem suggestName s"dataMem_${i}"
    }
  }

  if (clog) {
    when(wen && is_alloc) {
      printf(p"Write meta: idx:${idx_reg.asUInt()} -- data:${wmeta.tag}\n")
      printf(p"Write meta: addr: ${addr_reg}\n")
    }
  }

  io.mem.rd.cmd.bits.addr := Cat(tag_reg, idx_reg) << blen.U
  io.mem.rd.cmd.bits.len := (dataBeats - 1).U
  io.mem.rd.cmd.valid := false.B

  // read data
  io.mem.rd.data.ready := state === s_REFILL
  when(io.mem.rd.data.fire()) {
    refill_buf(read_count) := io.mem.rd.data.bits
  }

  // Info
  val is_block_dirty = v(set_count) && d(set_count)
  val block_addr = Cat(block_rmeta.tag, set_count - 1.U) << blen.U
  read_rmeta := metaMem.read(set_count, (is_block_dirty) && (flush_state === s_flush_START))
  block_rmeta := read_rmeta

  if (clog) {
    when((is_block_dirty) && (flush_state === s_flush_START)) {
      printf(p"Read value of ${set_count} -- ${read_rmeta}\n")
    }
  }

  // write addr
  io.mem.wr.cmd.bits.addr := Mux(flush_mode, block_addr, Cat(rmeta.tag, idx_reg) << blen.U)
  io.mem.wr.cmd.bits.len := (dataBeats - 1).U
  io.mem.wr.cmd.valid := false.B

  // write data
  io.mem.wr.data.bits := Mux(flush_mode,
    VecInit.tabulate(dataBeats)(i => dirty_cache_block((i + 1) * Axi_param.dataBits - 1, i * Axi_param.dataBits))(write_count),
    VecInit.tabulate(dataBeats)(i => read((i + 1) * Axi_param.dataBits - 1, i * Axi_param.dataBits))(write_count))
  io.mem.wr.data.valid := false.B

  // write resp
  io.cpu.flush_done := false.B

  // Cache FSM
  val is_dirty = v(idx_reg) && d(idx_reg)
  switch(state) {
    is(s_IDLE) {
      when(io.cpu.req.valid) {
        state := Mux(io.cpu.req.bits.mask.orR, s_WRITE_CACHE, s_READ_CACHE)
      }
    }
    is(s_READ_CACHE) {
      when(hit) {
        when(io.cpu.req.valid) {
          state := Mux(io.cpu.req.bits.mask.orR, s_WRITE_CACHE, s_READ_CACHE)
        }.otherwise {
          state := s_IDLE
        }
      }.otherwise {
        io.mem.wr.cmd.valid := is_dirty
        io.mem.rd.cmd.valid := !is_dirty
        when(io.mem.wr.cmd.fire()) {
          state := s_WRITE_BACK
        }.elsewhen(io.mem.rd.cmd.fire()) {
          state := s_REFILL
        }
      }
    }
    is(s_WRITE_CACHE) {
      when(hit || is_alloc_reg || io.cpu.abort) {
        state := s_IDLE
      }.otherwise {
        io.mem.wr.cmd.valid := is_dirty
        io.mem.rd.cmd.valid := !is_dirty
        when(io.mem.wr.cmd.fire()) {
          state := s_WRITE_BACK
        }.elsewhen(io.mem.rd.cmd.fire()) {
          state := s_REFILL
        }
      }
    }
    is(s_WRITE_BACK) {
      io.mem.wr.data.valid := true.B
      when(write_wrap_out) {
        state := s_WRITE_ACK
      }
    }
    is(s_WRITE_ACK) {
      when(io.mem.wr.ack) {
        state := s_REFILL_READY
      }
    }
    is(s_REFILL_READY) {
      io.mem.rd.cmd.valid := true.B
      when(io.mem.rd.cmd.fire()) {
        state := s_REFILL
      }
    }
    is(s_REFILL) {
      when(read_wrap_out) {
        state := Mux(cpu_mask.orR, s_WRITE_CACHE, s_IDLE)
      }
    }
  }

  //Flush state machine
  switch(flush_state) {
    is(s_flush_IDLE) {
      when(io.cpu.flush) {
        flush_state := s_flush_START
        flush_mode := true.B
      }
    }
    is(s_flush_START) {
      when(set_wrap) {
        io.cpu.flush_done := true.B
        flush_mode := false.B
        flush_state := s_flush_IDLE
      }.elsewhen(is_block_dirty) {
        flush_state := s_flush_ADDR
        if (clog) {
          printf(p"[START] Read value of ${set_count} -- ${read_rmeta}\n")
          printf(p"[START] Reg -- Read value of ${set_count} -- ${block_rmeta.tag}\n")
        }
      }
    }
    is(s_flush_ADDR) {
      /**
       * There is a bug in sequential memory of chisel that doesn't provide data in the same cycle
       */
      if (clog) {
        printf(p"[ADDR] Read value of ${set_count} -- ${read_rmeta}\n")
        printf(p"[ADDR] Reg -- Read value of ${set_count} -- ${block_rmeta.tag}\n")
      }
      flush_state := s_flush_WRITE
    }
    is(s_flush_WRITE) {
      if (clog) {
        printf(p"[WRITE] Read value of ${set_count} -- ${read_rmeta}\n")
        printf(p"[WRITE] Reg -- Read value of ${set_count} -- ${block_rmeta.tag}\n")
        printf(p"SetCount:[${set_count - 1.U}] -- block_rmeta[${block_rmeta.tag}]\n")
        printf(p"Dirty block address: 0x${Hexadecimal(block_addr)}\n")
        printf(p"Dirty block data: 0x${Hexadecimal(dirty_cache_block)}\n")
        printf(p"Read value of ${set_count} -- ${read_rmeta.tag}\n")
      }

      io.mem.wr.cmd.valid := true.B
      io.mem.rd.cmd.valid := false.B

      when(io.mem.wr.cmd.fire()) {
        flush_state := s_flush_WRITE_BACK
      }
    }
    is(s_flush_WRITE_BACK) {
      if (clog) {
        printf(p"Write data: ${VecInit.tabulate(dataBeats)(i => dirty_cache_block((i + 1) * Axi_param.dataBits - 1, i * Axi_param.dataBits))(write_count)}\n")
      }
      io.mem.wr.data.valid := true.B
      when(write_wrap_out) {
        flush_state := s_flush_WRITE_ACK
      }
    }
    is(s_flush_WRITE_ACK) {
      when(io.mem.wr.ack) {
        flush_state := s_flush_START
      }
    }
  }
}