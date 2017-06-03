package muxes

import chisel3._
import chisel3.util._
import chisel3.Module

class Demux[T <: Data](gen: T, n: Int) extends Module {
  val io = IO(new Bundle {
    val en = Input(Bool())
    val input = Input(gen)
    val sel = Input(UInt(width = log2Up(n)))
    val outputs = Output(Vec(n,gen))
    val valids  = Output(Vec(n,Bool()))
  })

  val x = io.sel

  when(io.en) {

    for (i <- 0 until n) {
      io.valids(i) := false.B
    }

    io.outputs(x) := io.input
    //TODO Fix the hack, Chisel overwrites the last statement
    io.valids(x) := true.B
  }
    .otherwise {
      io.valids := Vec(Seq.fill(n){false.B})
    }
}
