package memGen.noc

import chipsalliance.rocketchip.config.Parameters
import chisel3._
import chisel3.util._
import memGen.config.HasAccelParams
import memGen.memory.message.Flit


class Router( ID: Int = 0,  Queue_depth: Int = 2)(implicit val p: Parameters) extends Module with HasAccelParams {

  val io = IO(new Bundle {
    val cacheIn = Flipped(Decoupled(new Flit()))
    val cacheOut = Decoupled(new Flit())
    val in = Flipped(Decoupled(new Flit()))
    val out = Decoupled(new Flit())
  })

  val cache_in_Q = Module(new Queue(new Flit(), entries = Queue_depth, pipe = true))
  val cache_out_Q = Module(new Queue(new Flit(), entries = Queue_depth, pipe = true))
  val in_Q = Module(new Queue(new Flit(), entries = Queue_depth, pipe = false))

  val arbiter = Module(new RRArbiter(new Flit(), 2))

  cache_in_Q.io.enq <> io.cacheIn
  io.cacheOut <> cache_out_Q.io.deq
  in_Q.io.enq <> io.in

  io.out <> arbiter.io.out

  arbiter.io.in(0) <> cache_in_Q.io.deq

  cache_out_Q.io.enq.bits := in_Q.io.deq.bits
  cache_out_Q.io.enq.valid := in_Q.io.deq.valid && (in_Q.io.deq.bits.dst === ID.U)

  arbiter.io.in(1).bits := in_Q.io.deq.bits
  arbiter.io.in(1).valid := in_Q.io.deq.valid && (in_Q.io.deq.bits.dst =/= ID.U)
  in_Q.io.deq.ready := (arbiter.io.in(1).ready && (in_Q.io.deq.bits.dst =/= ID.U)) || (cache_out_Q.io.enq.ready && (in_Q.io.deq.bits.dst === ID.U))

}