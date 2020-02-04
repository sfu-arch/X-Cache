// See LICENSE for license details.
//package dandelion.cache
//
//import chisel3._
//import chisel3.util._
//import dandelion.junctions.{NastiReadAddressChannel, NastiWriteAddressChannel, NastiWriteDataChannel}
//import vta.interface.axi.AXIMaster
////import dandelion.junctions._
//import chipsalliance.rocketchip.config._
import dandelion.config._
//import dandelion.interfaces._
////import NastiConstants._
//import vta.interface.axi.{AXILiteMaster, AXIParams}
//
//case object NWays extends Field[Int]
//
//case object NSets extends Field[Int]
//
//case object CacheBlockBytes extends Field[Int]
//
//class CacheIO(implicit p: Parameters) extends AccelBundle {
//  val abort = Input(Bool())
//  val req = Flipped(Decoupled(new MemReq))
//  val resp = Output(Valid(new MemResp))
//
//  override def cloneType = new CacheIO().asInstanceOf[this.type]
//}
//
//abstract class CacheModuleIO(axi_p: AXIParams)(implicit val p: Parameters) extends Module with HasAccelParams {
//  val io = IO(new Bundle {
//    val cpu = new CacheIO
//    val axi = new AXIMaster(axi_p)
//    val stat = Output(UInt(xlen.W))
//  })
//
//}
//
//class CacheParams(axi_p: AXIParams)(implicit p: Parameters) extends AccelBundle {
//  val nWays = p(NWays) // Not used...
//  val nSets = p(NSets)
//  val bBytes = p(CacheBlockBytes)
//  val bBits = bBytes << 3
//  val blen = log2Ceil(bBytes)
//  val slen = log2Ceil(nSets)
//  val taglen = xlen - (slen + blen)
//  val nWords = bBits / xlen
//  val wBytes = xlen / 8
//  val byteOffsetBits = log2Ceil(wBytes)
//  val dataBeats = bBits / axi_p.dataBits
//}
//
//class MetaData(cache_params: CacheParams)(implicit p: Parameters) extends AccelBundle {
//  val tag = UInt(cache_params.taglen.W)
//
//  override def cloneType = new MetaData(cache_params).asInstanceOf[this.type]
//}
//
//class Cache(val ID: Int = 0, val axi_param: AXIParams, val cache_params: CacheParams)(implicit p: Parameters) extends CacheModuleIO(axi_param)(p) {
//  //  val io = IO(new CacheModuleIO(axi_param))
//
//  import cache_params._
//
//  // cache states
//  val (s_IDLE :: s_READ_CACHE :: s_WRITE_CACHE :: s_WRITE_BACK :: s_WRITE_ACK :: s_REFILL_READY :: s_REFILL :: Nil) = Enum(7)
//  val state = RegInit(s_IDLE)
//  // memory
//  val valid = RegInit(0.U(nSets.W))
//  val dirty = RegInit(0.U(nSets.W))
//  val metaMem = SyncReadMem(nSets, new MetaData(cache_params))
//  val dataMem = Seq.fill(nWords)(Mem(nSets, Vec(wBytes, UInt(8.W))))
//
//  val addr_reg = Reg(io.cpu.req.bits.addr.cloneType)
//  val cpu_data = Reg(io.cpu.req.bits.data.cloneType)
//  val cpu_mask = Reg(io.cpu.req.bits.mask.cloneType)
//  val cpu_tag = Reg(io.cpu.req.bits.tag.cloneType)
//  val cpu_tile = Reg(io.cpu.req.bits.tile.cloneType)
//  val cpu_iswrite = Reg(io.cpu.req.bits.iswrite.cloneType)
//
//  // Counters
//  require(dataBeats > 0)
//  val (read_count, read_wrap_out) = Counter(io.axi.r.fire(), dataBeats)
//  val (write_count, write_wrap_out) = Counter(io.axi.w.fire(), dataBeats)
//
//  val is_idle = state === s_IDLE
//  val is_read = state === s_READ_CACHE
//  val is_write = state === s_WRITE_CACHE
//  val is_alloc = state === s_REFILL && read_wrap_out
//  val is_alloc_reg = RegNext(is_alloc)
//
//  val hit = Wire(Bool())
//  val wen = is_write && (hit || is_alloc_reg) && !io.cpu.abort || is_alloc
//  val ren = !wen && (is_idle || is_read) && io.cpu.req.valid
//  val ren_reg = RegNext(ren)
//
//  val addr = io.cpu.req.bits.addr
//  val idx = addr(slen + blen - 1, blen)
//  val tag_reg = addr_reg(xlen - 1, slen + blen)
//  val idx_reg = addr_reg(slen + blen - 1, blen)
//  val off_reg = addr_reg(blen - 1, byteOffsetBits)
//
//  val rmeta = Reg(new MetaData(cache_params))
//  rmeta := metaMem.read(idx)
//  val rdata = RegInit(0.U(128.W))
//  rdata := Cat((dataMem map (_.read(idx).asUInt)).reverse)
//  val rdata_buf = RegEnable(rdata, ren_reg)
//  val refill_buf = Reg(Vec(dataBeats, UInt(axi_param.dataBits.W)))
//  val read = Mux(is_alloc_reg, refill_buf.asUInt, Mux(ren_reg, rdata, rdata_buf))
//
//  hit := valid(idx_reg) && rmeta.tag === tag_reg
//
//  // Read Mux
//  io.cpu.resp.bits.data := VecInit.tabulate(nWords)(i => read((i + 1) * xlen - 1, i * xlen))(off_reg)
//  io.cpu.resp.bits.tag := cpu_tag
//  io.cpu.resp.bits.valid := (is_write && hit) || (is_read && hit) || (is_alloc_reg && !cpu_iswrite)
//  io.cpu.resp.bits.iswrite := cpu_iswrite
//  io.cpu.resp.valid := (is_write && hit) || (is_read && hit) || (is_alloc_reg && !cpu_iswrite)
//  io.cpu.req.ready := is_idle || (state === s_READ_CACHE && hit)
//
//  when(io.cpu.req.valid && io.cpu.req.ready) {
//    addr_reg := addr
//    cpu_tag := io.cpu.req.bits.tag
//    cpu_data := io.cpu.req.bits.data
//    cpu_mask := io.cpu.req.bits.mask
//    cpu_iswrite := io.cpu.req.bits.iswrite
//
//  }
//
//  val wmeta = Wire(new MetaData(cache_params))
//  wmeta.tag := tag_reg
//
//  val wmask = Mux(!is_alloc, (cpu_mask << Cat(off_reg, 0.U(byteOffsetBits.W))).asUInt.zext, (-1).S).asUInt()
//  val wdata = Mux(!is_alloc, Fill(nWords, cpu_data),
//    if (refill_buf.size == 1) io.axi.r.bits.data
//    else Cat(io.axi.r.bits.data, Cat(refill_buf.init.reverse)))
//  when(wen) {
//    valid := valid.bitSet(idx_reg, true.B)
//    dirty := dirty.bitSet(idx_reg, !is_alloc)
//    when(is_alloc) {
//      metaMem.write(idx_reg, wmeta)
//    }
//    dataMem.zipWithIndex foreach { case (mem, i) =>
//      val data = VecInit.tabulate(wBytes)(k => wdata(i * xlen + (k + 1) * 8 - 1, i * xlen + k * 8))
//      mem.write(idx_reg, data, (wmask((i + 1) * wBytes - 1, i * wBytes)).asBools())
//      mem suggestName s"dataMem_${i}"
//    }
//  }
//
//  //First elemt is ID and it looks like ID tags a memory request
//  // ID : Is AXI request ID
//  // @todo: Change @id value for each cache
//  io.axi.ar.bits := NastiReadAddressChannel(
//    id = 0.U, (Cat(tag_reg, idx_reg) << blen.U).asUInt(), log2Ceil(axi_param.dataBits / 8).U, (dataBeats - 1).U)
//  io.axi.ar.valid := false.B
//  io.axi.ar.bits.prot := AXPROT(false, false, true)
//  io.axi.ar.bits.user := 0x1f.U
//  io.axi.ar.bits.cache := 0xf.U
//
//  // read data
//  io.axi.r.ready := state === s_REFILL
//  when(io.axi.r.fire()) {
//    refill_buf(read_count) := io.axi.r.bits.data
//  }
//
//
//  // Dump state
//  io.stat := state.asUInt()
//
//  // write addr
//  io.axi.aw.bits := NastiWriteAddressChannel(
//    id = ID.U, (Cat(rmeta.tag, idx_reg) << blen.U).asUInt(), log2Ceil(cache_params.dataBeats / 8).U, (dataBeats - 1).U)
//  io.axi.aw.valid := false.B
//  io.axi.aw.bits.prot := AXPROT(false, false, true)
//  io.axi.aw.bits.user := 0x1f.U
//  io.axi.aw.bits.cache := 0xf.U
//  // write data
//  io.axi.w.bits := NastiWriteDataChannel(
//    VecInit.tabulate(dataBeats)(i => read((i + 1) * cache_params.dataBeats - 1, i * cache_params.dataBeats))(write_count),
//    None, write_wrap_out, id = ID.U)
//  io.axi.w.valid := false.B
//  // write resp
//  io.axi.b.ready := false.B
//
//  // Cache FSM
//  val countOn = true.B // increment counter every clock cycle
//  val (counterValue, counterWrap) = Counter(countOn, 64 * 1024)
//  //clockCycles := clockCycles + 1.U
//
//
//  val is_dirty = valid(idx_reg) && dirty(idx_reg)
//  switch(state) {
//    is(s_IDLE) {
//      when(io.cpu.req.valid) {
//        //state := Mux(io.cpu.req.bits.iswrite, s_WRITE_CACHE, s_READ_CACHE)
//        when(io.cpu.req.bits.iswrite) {
//          state := s_WRITE_CACHE
//          //printf("\nSTORE START: %d\n", counterValue)
//        }.otherwise {
//          state := s_READ_CACHE
//          //printf("\nLOAD START:  %d\n", counterValue)
//        }
//      }
//    }
//    is(s_READ_CACHE) {
//      when(hit) {
//        when(io.cpu.req.valid) {
//          state := Mux(io.cpu.req.bits.iswrite, s_WRITE_CACHE, s_READ_CACHE)
//        }.otherwise {
//          state := s_IDLE
//          //          printf("\nLOAD END: %d\n", counterValue)
//        }
//      }.otherwise {
//        io.axi.aw.valid := is_dirty
//        io.axi.ar.valid := !is_dirty
//        when(io.axi.aw.fire()) {
//          state := s_WRITE_BACK
//        }.elsewhen(io.axi.ar.fire()) {
//          state := s_REFILL
//        }
//      }
//    }
//    is(s_WRITE_CACHE) {
//      when(hit || is_alloc_reg || io.cpu.abort) {
//        state := s_IDLE
//        //      printf("\nSTORE END: %d\n", counterValue)
//      }.otherwise {
//        io.axi.aw.valid := is_dirty
//        io.axi.ar.valid := !is_dirty
//        when(io.axi.aw.fire()) {
//          state := s_WRITE_BACK
//        }.elsewhen(io.axi.ar.fire()) {
//          state := s_REFILL
//        }
//      }
//    }
//    is(s_WRITE_BACK) {
//      io.axi.w.valid := true.B
//      when(write_wrap_out) {
//        state := s_WRITE_ACK
//      }
//    }
//    is(s_WRITE_ACK) {
//      io.axi.b.ready := true.B
//      when(io.axi.b.fire()) {
//        state := s_REFILL_READY
//      }
//    }
//    is(s_REFILL_READY) {
//      io.axi.ar.valid := true.B
//      when(io.axi.ar.fire()) {
//        state := s_REFILL
//      }
//    }
//    is(s_REFILL) {
//      when(read_wrap_out) {
//        state := Mux(cpu_iswrite, s_WRITE_CACHE, s_IDLE)
//        when(!cpu_iswrite) {
//        }
//      }
//    }
//  }
//  // Info
//
//}
