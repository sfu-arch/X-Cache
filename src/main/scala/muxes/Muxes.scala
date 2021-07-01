//package muxes
//
//import scala.math._
//import memGen.interfaces._
//import chisel3._
//import chisel3.util._
//import chisel3.Module
//import chipsalliance.rocketchip.config._
//import memGen.config._
//import memGen.interfaces._
//import memGen.config._
//
//class Demux[T <: ValidT](gen: T, Nops: Int) extends Module {
//  val io = IO(new Bundle {
//    val en      = Input(Bool( ))
//    val input   = Input(gen)
//    val sel     = Input(UInt(max(1, log2Ceil(Nops)).W))
//    val outputs = Output(Vec(Nops, gen))
//    // val outputvalid = Output(Bool())
//  })
//
//  val x = io.sel
//
//  for (i <- 0 until Nops) {
//    io.outputs(i) := io.input
//  }
//  when(io.en) {
//    for (i <- 0 until Nops) {
//      io.outputs(i).valid := false.B
//    }
//    io.outputs(x).valid := true.B
//  }.otherwise {
//    for (i <- 0 until Nops) {
//      io.outputs(i).valid := false.B
//    }
//  }
//  // io.outputvalid := io.valids.asUInt.andR
//}
//
//
//class DemuxGen[T <: Data](gen: T, Nops: Int) extends Module {
//  val io = IO(new Bundle {
//    val en      = Input(Bool( ))
//    val input   = Input(gen)
//    val sel     = Input(UInt(max(1, log2Ceil(Nops)).W))
//    val outputs = Output(Vec(Nops, gen))
//    val valids  = Output(Vec(Nops, Bool( )))
//  })
//
//  val x = io.sel
//
//  for (i <- 0 until Nops) {
//    io.outputs(i) := io.input
//  }
//  when(io.en) {
//    for (i <- 0 until Nops) {
//      io.valids(i) := false.B
//    }
//    io.valids(x) := true.B
//  }.otherwise {
//    for (i <- 0 until Nops) {
//      io.valids(i) := false.B
//    }
//  }
//}
//
//class Mux[T <: ValidT](gen: T, Nops: Int) extends Module {
//  val io = IO(new Bundle {
//    val en     = Input(Bool( ))
//    val output = Output(gen)
//    val sel    = Input(UInt(max(1, log2Ceil(Nops)).W))
//    val inputs = Input(Vec(Nops, gen))
//  })
//
//  val x = io.sel
//
//  io.output := io.inputs(x)
//  when(!io.en) {
//    io.output.valid := false.B
//  }
//}
