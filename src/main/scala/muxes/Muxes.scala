package muxes

import chisel3._
import chisel3.util._
import chisel3.Module

class DeMux[T <: Data](gen: T, n: Int) extends Module {
  val io = new Bundle {
    val input = Input(gen)
    val sel = Input(UInt(width = log2Up(n)))
    val outputs = Output(Vec(n,gen))
  }
  io.outputs(io.sel) := io.input
}
