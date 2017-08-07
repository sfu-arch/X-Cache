package dataflow.graph

import chisel3._
import chisel3.util._

import node._
import config._
import interfaces._
import arbiters._
import memory._

class Compute02DF(implicit val p: Parameters) extends Module with CoreParams {

  val io = IO(new Bundle {
    val data0 = Flipped(Decoupled(new DataBundle()))
    val data1 = Flipped(Decoupled(new DataBundle()))
    val data2 = Flipped(Decoupled(new DataBundle()))
    val data3 = Flipped(Decoupled(new DataBundle()))
    val data4 = Flipped(Decoupled(new DataBundle()))
    val data5 = Flipped(Decoupled(new DataBundle()))
    val data6 = Flipped(Decoupled(new DataBundle()))
    val enable = Flipped(Decoupled(Bool()))

    val dataOut0 = Decoupled(new DataBundle())
    val dataOut1 = Decoupled(new DataBundle())

  })

  val m0 = Module(new ComputeNode(NumOuts = 1, ID = 0, opCode = "Xor")(sign = false)(p))
  val m1 = Module(new ComputeNode(NumOuts = 1, ID = 0, opCode = "And")(sign = false)(p))
  val m2 = Module(new ComputeNode(NumOuts = 2, ID = 0, opCode = "Xor")(sign = false)(p))
  val m3 = Module(new ComputeNode(NumOuts = 1, ID = 0, opCode = "ShiftLeft")(sign = false)(p))
  val m4 = Module(new ComputeNode(NumOuts = 1, ID = 0, opCode = "ShiftRight")(sign = false)(p))
  val m5 = Module(new ComputeNode(NumOuts = 1, ID = 0, opCode = "Or")(sign = false)(p))

  m0.io.LeftIO <> io.data0
  m0.io.RightIO <> io.data1

  m1.io.LeftIO <> m0.io.Out(0)
  m1.io.RightIO <> io.data2

  m2.io.LeftIO <> m1.io.Out(0)
  m2.io.RightIO <> io.data3

  m3.io.LeftIO <> m2.io.Out(0)
  m3.io.RightIO <> io.data4

  m4.io.LeftIO <> m2.io.Out(1)
  m4.io.RightIO <> io.data5

  m5.io.LeftIO <> m3.io.Out(0)
  m5.io.RightIO <> io.data6

  m0.io.enable <> io.enable
  m1.io.enable <> io.enable
  m2.io.enable <> io.enable
  m3.io.enable <> io.enable
  m4.io.enable <> io.enable
  m5.io.enable <> io.enable

  io.dataOut0 <> m4.io.Out(0)
  io.dataOut1 <> m5.io.Out(0)


}

