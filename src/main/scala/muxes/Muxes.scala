package muxes

import interfaces._
import chisel3._
import chisel3.util._
import chisel3.Module

class Demux[T <: ValidT](gen: T, n: Int) extends Module {
  val io = IO(new Bundle {
    val en = Input(Bool())
    val input = Input(gen)
    val sel = Input(UInt(width = log2Up(n)))
    val outputs = Output(Vec(n, gen))
    val valids = Output(Vec(n, Bool()))
  })

  val x = io.sel

  when(io.en) {
    for (i <- 0 until n) {
      io.outputs(i).valid := false.B
      io.valids(i) := false.B
    }

    io.outputs(x) := io.input
    io.outputs(x).valid := true.B
    io.valids(x) := true.B
  }.otherwise {
    io.valids := Vec(Seq.fill(n) { false.B })
    for (i <- 0 until n) {
      io.outputs(i).valid := false.B
      io.valids(i) := false.B
    }
  }
}

