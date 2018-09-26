package memory


import Chisel.experimental.chiselName
import accel.Cache
import chisel3._
import chisel3.Module
import chisel3.util._
import junctions._
import muxes.{Demux, DemuxGen}

import scala.collection.immutable


// Config
import config._
import utility._
import interfaces._
import scala.math._

class cacheserving(val NumTileBits: Int, val NumSlotBits: Int) extends Bundle {
  val tile_idx = UInt(NumTileBits.W)
  val slot_idx = UInt(NumSlotBits.W)

  override def cloneType = new cacheserving(NumTileBits, NumSlotBits).asInstanceOf[this.type]
}

object cacheserving {
  def default()(
    implicit p: Parameters
  ): cacheserving = {
    val wire = Wire(new cacheserving(32, 32))
    wire.slot_idx := 0.U
    wire.tile_idx := 0.U
    wire
  }

  def default(NumTileBits: Int,
              NumSlotBits: Int
             )(
               implicit p: Parameters
             ): cacheserving = {
    val wire = Wire(new cacheserving(32, 32))
    wire.slot_idx := 0.U
    wire.tile_idx := 0.U
    wire
  }

}


class NParallelCache(NumTiles: Int = 1, NumBanks: Int = 1)(implicit p: Parameters) extends NCacheIO(NumTiles, NumBanks)(p) {

  //  Declare a vector of cache objects
  val caches = for (i <- 0 until NumBanks) yield {
    val cache1 = Module(new Cache(i))
    io.stat(i) := cache1.io.stat
    cache1
  }

  /*============================*
   *    Wiring  Cache to CPU  *
   *============================*/
  assert(isPow2(NumBanks) && NumBanks != 0)
  //
  val NumBankBits = max(1, log2Ceil(NumBanks))
  val NumTileBits = max(1, log2Ceil(NumBanks))
  val NumSlots    = NumTiles * NumBanks * 20
  val NumSlotBits = max(1, log2Ceil(NumSlots))

  //  Per-Tile stateink

  val fetch_queues = for (i <- 0 until NumBanks) yield {
    val fq = Module(new PeekQueue(new MemReq( ), 4))
    fq
  }

  val fq_io_deq_bits = fetch_queues map {
    _.io.deq.bits
  }
  val slots          = RegInit(VecInit(Seq.fill(NumTiles)(VecInit(Seq.fill(NumBanks)(CacheSlotsBundle.default(NumTiles))))))

  // Per-Cache bank state
  //  val slots         = RegInit(VecInit(Seq.fill(NumTiles)(VecInit(Seq.fill(NumBanks)(CacheSlotsBundle.default(NumTiles))))))
  val cache_ready   = VecInit(caches.map(_.io.cpu.req.ready))
  val cache_req_io  = VecInit(caches.map(_.io.cpu.req))
  val cache_serving = RegInit(VecInit(Seq.fill(NumBanks)(cacheserving.default(NumTileBits, NumSlotBits))))


  var bankidxseq = Seq[UInt]( )

  //  Input to queue
  for (i <- 0 until NumTiles) {
    fetch_queues(i).io.enq.bits <> io.cpu.MemReq(i).bits.clone_and_set_tile_id(i.U)
    fetch_queues(i).io.enq.valid := io.cpu.MemReq(i).valid
    io.cpu.MemReq(i).ready := fetch_queues(i).io.enq.ready
    fetch_queues(i).io.recycle := false.B

    val bank_idx = if (NumBanks == 1) {
      0.U
    } else {
      fetch_queues(i).io.deq.bits.addr(caches(0).bankbitindex + NumBankBits - 1, caches(0).bankbitindex)
    }
    bankidxseq = bankidxseq :+ bank_idx
  }

  val picker_matrix = for (i <- 0 until NumBanks) yield {
    bankidxseq map {
      _ === i.U
    }
  }

  val picked_matrix = for (i <- 0 until NumBanks) yield {
    PriorityEncoderOH(picker_matrix(i))
  }

  val tile_picked = picked_matrix.map {
    VecInit(_).asUInt
  }.reduce {
    _ | _
  }

  val prioritymuxes = for (i <- 0 until NumBanks) yield {
    Mux1H(picked_matrix(i), fq_io_deq_bits)
  }


  for (i <- 0 until NumTiles) {
    val slot_arbiter = Module(new RRArbiter(new Bool, NumSlots))
    val slot_idx = slot_arbiter.io.chosen

    for (j <- 0 until NumBanks) {
      slot_arbiter.io.in(i).valid := ~(slots(i)(j).alloc)
    }

    //  Handshaking fetch queue with slot arbiter
    fetch_queues(i).io.deq.ready := slot_arbiter.io.out.valid
    slot_arbiter.io.out.ready := fetch_queues(i).io.deq.valid


    //  Queueing Logic.
    when(fetch_queues(i).io.deq.fire) {
      when(cache_ready(bankidxseq(i)) && tile_picked(i)) {
        //  Fetch queue fires only if slot is free.
        //  Slot is free and cache is ready

        slots(i)(slot_idx).tile := i.U
        slots(i)(slot_idx).alloc := true.B
        // Setting cache metadata before sending request request.
        cache_req_io(bankidxseq(i)).valid := true.B
        cache_serving(bankidxseq(i)).slot_idx := slot_idx
        cache_serving(bankidxseq(i)).tile_idx := i.U
      }.otherwise {
        //    Cache is not ready
        //      Recycling logic
        fetch_queues(i).io.recycle := true.B
      }
    }

    for (j <- 0 until NumBanks) {
      cache_req_io(j).bits <> prioritymuxes(j)
      cache_req_io(j).valid := false.B
      when(caches(j).io.cpu.resp.valid) {
        slots(cache_serving(j).tile_idx)(cache_serving(j).slot_idx).ready := true.B
        slots(cache_serving(j).tile_idx)(cache_serving(j).slot_idx).bits := caches(i).io.cpu.resp.bits
      }
    }

    val resp_arbiter = Module(new RRArbiter(
      new MemResp, NumSlots))

    for (j <- 0 until NumBanks) {
      resp_arbiter.io.in(j).bits := slots(i)(j).bits
      resp_arbiter.io.in(j).bits.tile := slots(i)(j).tile
      resp_arbiter.io.in(j).valid := slots(i)(j).fire
      when(resp_arbiter.io.in(j).fire) {
        slots(i)(j).alloc := false.B
        slots(i)(j).ready := false.B
      }
    }

    val resp_tile = resp_arbiter.io.out.bits.tile
    val resp_slot = resp_arbiter.io.chosen
    io.cpu.MemResp foreach {
      _.valid := false.B
    }

    io.cpu.MemResp(resp_tile).bits := slots(i)(resp_slot).bits
    when(resp_arbiter.io.out.fire( )) {
      io.cpu.MemResp(resp_tile).valid := true.B
    }

  }

  //  cache_req_io foreach { rq =>
  //    rq.bits <> fetch_queue.io.deq.bits
  //  }


  //  Wiring up queue with appropriate cache


  //  Debug statements
  //  printf(p"\nRecycle: ${fetch_queue.io.recycle} \n Arbiter : ${fetch_arbiter.io.out} \n Queue: ${fetch_queue.io.enq}")

  /*============================*
   *    Wiring  Cache to NASTI  *
   *============================*/

  val nasti_ar_arbiter = Module(new Arbiter(new NastiReadAddressChannel( ), NumBanks))
  //  This is a twin channel arbiter (arbitrates for both the W and AW channels at once)
  //  AW and W go together
  val nasti_aw_arbiter = Module(new Arbiter(new NastiWriteAddressChannel( ), NumBanks))
  val nasti_r_demux    = Module(new DemuxGen(new NastiReadDataChannel( ), NumBanks))
  val nasti_b_demux    = Module(new DemuxGen(new NastiWriteResponseChannel( ), NumBanks))

  //  Multiplex cache objects to NASTI interface.
  nasti_ar_arbiter.io.in zip caches.map {
    _.io.nasti.ar
  } foreach {
    case (a, b) => a <> b
  }

  //  Multiplex cache objects to NASTI interface.
  nasti_aw_arbiter.io.in zip caches.map {
    _.io.nasti.aw
  } foreach {
    case (a, b) => a <> b
  }


  //  Cache->NASTI AR
  io.nasti.ar <> nasti_ar_arbiter.io.out

  //  Cache->NASTI AW
  io.nasti.aw.bits <> nasti_aw_arbiter.io.out.bits
  io.nasti.aw.valid <> nasti_aw_arbiter.io.out.valid

  //  Vector of bools.
  val cache_wback = caches map {
    _.IsWBACK( )
  }
  nasti_aw_arbiter.io.out.ready := io.nasti.aw.ready & (~(cache_wback.reduceLeft(_ | _)))

  //  Cache->NASTI W . Sequence of cache io.nasti.w.bits
  val cache_nasti_w_bits = caches.map {
    _.io.nasti.w.bits
  }
  //  Feed into MUX and select one of these for the NASTI io w
  val w_mux              = Mux1H(cache_wback, cache_nasti_w_bits)
  //  Mux -> NASTI
  io.nasti.w.bits <> w_mux
  //  NASTI w valid  = true if one of the caches are writing back.
  io.nasti.w.valid := cache_wback.reduceLeft(_ | _)
  // Fanout io nasti w ready to all the caches. NASTI slave to all cache ready
  caches foreach {
    _.io.nasti.w.ready := io.nasti.w.ready
  }

  // NASTI->Cache b and r; ready.
  val b_ready = caches map {
    _.io.nasti.b.ready
  }

  val r_ready = caches map {
    _.io.nasti.r.ready
  }

  io.nasti.b.ready := b_ready.reduceLeft(_ | _)
  io.nasti.r.ready := r_ready.reduceLeft(_ | _)

  //  NASTI->Cache r
  nasti_r_demux.io.en := io.nasti.r.fire
  nasti_r_demux.io.input := io.nasti.r.bits
  nasti_r_demux.io.sel := io.nasti.r.bits.id


  for (i <- 0 until NumBanks) {
    caches(i).io.nasti.r.valid := nasti_r_demux.io.valids(i)
    caches(i).io.nasti.r.bits := nasti_r_demux.io.outputs(i)
  }


  //  NASTI->Cache b
  nasti_b_demux.io.en := io.nasti.b.fire
  nasti_b_demux.io.input := io.nasti.b.bits
  nasti_b_demux.io.sel := io.nasti.b.bits.id

  for (i <- 0 until NumBanks) {
    caches(i).io.nasti.b.valid := nasti_b_demux.io.valids(i)
    caches(i).io.nasti.b.bits := nasti_b_demux.io.outputs(i)
  }

}
