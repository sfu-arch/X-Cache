package muxes

import scala.math._
import interfaces._
import chisel3._
import chisel3.util._
import chisel3.Module
import config._
import interfaces._

class Demux[T <: ValidT](gen: T, n: Int) extends Module {
  val io = IO(new Bundle {
    val en = Input(Bool())
    val input = Input(gen)
    val sel = Input(UInt(max(1, log2Ceil(n)).W))
    val outputs = Output(Vec(n, gen))
    val valids = Output(Vec(n, Bool()))
    val outputvalid = Output(Bool())
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
  io.outputvalid := io.valids.asUInt.andR
}

abstract class AbstractDeMuxTree[T <: RouteID](Nops: Int, gen: T)(implicit p: Parameters)
  extends Module with CoreParams {
  val io = IO(new Bundle {
    val outputs = Vec(Nops, Output(gen))
    val input = Input(gen)
    val enable = Input(Bool())
    val sel = Input(UInt(glen.W))
  })
}

class DeMuxTree[T <: RouteID with ValidT](BaseSize: Int, NumOps: Int, gen: T)(implicit val p: Parameters) extends AbstractDeMuxTree(NumOps, gen)(p) {
  require(isPow2(BaseSize))
  var prev = Seq.fill(0) { Module(new Demux(gen, 4)).io }
  var toplevel = Seq.fill(0) { Module(new Demux(gen, 4)).io }
  val SelBits = max(1, log2Ceil(BaseSize))
  var Level = (max(1, log2Ceil(NumOps)) + SelBits - 1) / SelBits
  var TopBits = Level * SelBits - 1
  var SelHIndex = 0
  var Muxes_Per_Level = (NumOps + BaseSize - 1) / BaseSize
  while (Muxes_Per_Level > 0) {
    val Demuxes = Seq.fill(Muxes_Per_Level) { Module(new Demux(gen, BaseSize)).io }

    if (prev.length != 0) {
      // val pipereg = Reg(Vec(prev.length,gen))
      //  for (k <- 0 until prev.length) {
      //   pipereg(k).valid := false.B
      // }
      for (i <- 0 until prev.length) {
        // println("Sink["+i+"]"+"Source Demux["+i/BaseSize+","+indexcalc(i,BaseSize)+"]")
        // println("z:"+z+","+(z+log2Ceil(BaseSize)-1))
        val demuxInputReg = RegNext(Demuxes(i / BaseSize).outputs(indexcalc(i, BaseSize)))
        val demuxvalidreg = RegNext(init = false.B, next = Demuxes(i / BaseSize).outputs(indexcalc(i, BaseSize)).valid)
        prev(i).input := demuxInputReg
        prev(i).en := demuxvalidreg
        prev(i).sel := demuxInputReg.RouteID(SelHIndex + log2Ceil(BaseSize) - 1, SelHIndex)
        // prev(i).input <> Demuxes(i/BaseSize).outputs(indexcalc(i,BaseSize)) 
        // prev(i).en    := Demuxes(i/BaseSize).outputs(indexcalc(i,BaseSize)).valid
        // prev(i).sel   := Demuxes(i/BaseSize).outputs(indexcalc(i,BaseSize)).RouteID(SelHIndex+log2Ceil(BaseSize)-1,SelHIndex)
      }
      SelHIndex = SelHIndex + log2Ceil(BaseSize)
    }

    if (prev.length == 0) {
      toplevel = Demuxes
      for (i <- 0 until Demuxes.length * BaseSize) {
        if (i < NumOps) {
          // println("Output["+i+"]"+"Source Demux["+i/BaseSize+","+indexcalc(i,BaseSize)+"]")
          io.outputs(i) <> Demuxes(i / BaseSize).outputs(indexcalc(i, BaseSize))
        }
      }
    }
    prev = Demuxes
    if (Muxes_Per_Level == 1) {
      Muxes_Per_Level = 0
    } else {
      Muxes_Per_Level = (Muxes_Per_Level + BaseSize - 1) / BaseSize
    }
  }
  // printf(p"Top Demuxes: ${toplevel(1).outputs}\n")
  // printf(p"Bottom Demuxes: ${prev(0).outputs}\n")

  // println("z:"+z+","+(z+log2Ceil(BaseSize)-1))
  prev(0).input <> io.input
  prev(0).en := io.enable
  prev(0).sel := io.input.RouteID(SelHIndex + log2Ceil(BaseSize) - 1, SelHIndex)

  object indexcalc {
    def apply(i: Int, BaseSize: Int): Int = {
      i - ((i / BaseSize) * BaseSize)
    }
  }
}