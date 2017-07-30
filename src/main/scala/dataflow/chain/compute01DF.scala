package dataflow.chain

import chisel3._
import chisel3.util._

import node._
import config._
import interfaces._
import arbiters._
import memory._

class Compute01DF(implicit val p: Parameters) extends Module with CoreParams {

  val io = IO(new Bundle {
    val data0 = Flipped(Decoupled(new DataBundle()))
    val data1 = Flipped(Decoupled(new DataBundle()))
    val data2 = Flipped(Decoupled(new DataBundle()))
    val data3 = Flipped(Decoupled(new DataBundle()))
    val enable = Flipped(Decoupled(Bool()))

    val dataOut = Decoupled(new DataBundle())

  })

  val m0 = Module(new ComputeNode(NumOuts = 1, ID = 0, opCode = "And")(sign = false)(p))
  val m1 = Module(new ComputeNode(NumOuts = 1, ID = 0, opCode = "Xor")(sign = false)(p))
  val m2 = Module(new ComputeNode(NumOuts = 1, ID = 0, opCode = "Add")(sign = false)(p))

  m0.io.LeftIO <> io.data0
  m0.io.RightIO <> io.data1

  m1.io.LeftIO <> m0.io.Out(0)
  m1.io.RightIO <> io.data2

  m2.io.LeftIO <> m1.io.Out(0)
  m2.io.RightIO <> io.data3

  m0.io.enable <> io.enable
  m1.io.enable <> io.enable
  m2.io.enable <> io.enable

  io.dataOut <> m2.io.Out(0)


}

