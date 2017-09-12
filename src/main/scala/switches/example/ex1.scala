package switches.example

/**
  * Created by vnaveen0 on 11/9/17.
  */

import chisel3._
import chisel3.util._
import utility.UniformPrintfs
import interfaces._
import util._
import config._
import accel._
import dataflow.tests._
import node.HandShaking
import switches._
import node._

class Dyser1X1(implicit val p: Parameters) extends Module with CoreParams {

  val io = IO(new Bundle {
    val Data0 = Flipped(Decoupled(new DataBundle))
    val Data1 = Flipped(Decoupled(new DataBundle))
    val start = Input(new Bool())
    val result = Decoupled(new DataBundle)
  })



  val add = Module(new ComputeNode(NumOuts = 1, ID = 0, opCode = "Add")(sign = false)(p))
  val sw01 = Module(new Dyser(
    SelN = 1,
    SelE = 1,
    SelW = 1,
    SelS = 1,
    SelNE = 1,
    SelNW = 1,
    SelSE = 2,
    SelSW = 1,

    EnN = false.B,
    EnE = false.B,
    EnW = false.B,
    EnS = false.B,
    EnNE = false.B,
    EnNW = false.B,
    EnSE = true.B,
    EnSW = false.B
  )(p))

  val sw11 = Module(new Dyser(
    SelN = 1,
    SelE = 1,
    SelW = 1,
    SelS = 1,
    SelNE = 1,
    SelNW = 1,
    SelSE = 1,
    SelSW = 1,

    EnN = false.B,
    EnE = false.B,
    EnW = false.B,
    EnS = false.B,
    EnNE = false.B,
    EnNW = false.B,
    EnSE = false.B,
    EnSW = true.B
  )(p))


  val sw02 = Module(new Dyser(
    SelN = 1,
    SelE = 1,
    SelW = 3,
    SelS = 1,
    SelNE = 1,
    SelNW = 1,
    SelSE = 1,
    SelSW = 1,

    EnN = false.B,
    EnE = false.B,
    EnW = true.B,
    EnS = false.B,
    EnNE = false.B,
    EnNW = false.B,
    EnSE = false.B,
    EnSW = false.B
  )(p))


  //IO connections

  //  sw01.io.inW := io.Data0
  io.Data0.ready := sw01.io.inW.ready
  sw01.io.inW.valid  := io.Data0.valid
  sw01.io.inW.bits   := io.Data0.bits

  //  sw11.io.inE <> io.Data1
  io.Data1.ready := sw11.io.inE.ready
  sw11.io.inE.valid  := io.Data1.valid
  sw11.io.inE.bits   := io.Data1.bits


  //  io.result  := sw02.io.outW
  sw02.io.outW.ready := io.result.ready
  io.result.valid  :=  sw02.io.outW.valid
  io.result.bits   :=  sw02.io.outW.bits


  //Add
  add.io.LeftIO  <> sw01.io.outSE
  add.io.RightIO <> sw11.io.outSW

  //TODO FIX Due to Problems in Add this connection is not possible
  //  sw02.io.inNE   <> add.io.Out(0)
  sw02.io.inNE.valid   <> add.io.Out(0).valid
  sw02.io.inNE.ready   <> add.io.Out(0).ready
  // This valid is the issue
  sw02.io.inNE.bits.valid   <> add.io.Out(0).valid
  sw02.io.inNE.bits.predicate   <> add.io.Out(0).bits.predicate
  sw02.io.inNE.bits.data <> add.io.Out(0).bits.data


  //  when(add.io.enable.ready) {
//    add.io.enable.valid := io.start
//    add.io.enable.bits := io.start
//  }

  add.io.enable.valid := true.B
  add.io.enable.bits := true.B

  printf("\"SW01\": {\"InW (R,V): %x,%x   bits:(Pr,V,D): %x,%x,%x \n",
    sw01.io.inW.ready,sw01.io.inW.valid,sw01.io.inW.bits.predicate,
    sw01.io.inW.bits.valid, sw01.io.inW.bits.data )

  printf("\"SW01\": {\"OutSE (R,V): %x,%x   bits:(Pr,V,D): %x,%x,%x \n",
    sw01.io.outSE.ready,sw01.io.outSE.valid,sw01.io.outSE.bits.predicate,
    sw01.io.outSE.bits.valid, sw01.io.outSE.bits.data )


  printf("\"SW11\": {\"InE (R,V): %x,%x   bits:(Pr,V,D): %x,%x,%x \n",
    sw11.io.inE.ready,sw11.io.inE.valid,sw11.io.inE.bits.predicate,
    sw11.io.inE.bits.valid, sw11.io.inE.bits.data )

  printf("\"SW11\": {\"OutSW (R,V): %x,%x   bits:(Pr,V,D): %x,%x,%x \n",
    sw11.io.outSW.ready,sw11.io.outSW.valid,sw11.io.outSW.bits.predicate,
    sw11.io.outSW.bits.valid, sw11.io.outSW.bits.data )


  printf("\"ADD\": {\"Out (R,V): %x,%x   bits:(Pr,V,D): %x,%x,%x \n",
    add.io.Out(0).ready, add.io.Out(0).valid, add.io.Out(0).bits.predicate,
    add.io.Out(0).bits.valid, add.io.Out(0).bits.data)


 //----
  printf("\"SW02\": {\"InE (R,V): %x,%x   bits:(Pr,V,D): %x,%x,%x \n",
    sw02.io.inNE.ready,sw02.io.inNE.valid,sw02.io.inNE.bits.predicate,
    sw02.io.inNE.bits.valid, sw02.io.inNE.bits.data )

  printf("\"SW02\": {\"OutSW (R,V): %x,%x   bits:(Pr,V,D): %x,%x,%x \n",
    sw02.io.outW.ready,sw02.io.outW.valid,sw02.io.outW.bits.predicate,
    sw02.io.outW.bits.valid, sw02.io.outW.bits.data )


}



// Testing Add
//  when(add.io.enable.ready) {
//    add.io.enable.valid := io.start
//    add.io.enable.bits := io.start
//  }
//
//  io.Data0.ready := add.io.LeftIO.ready
//  add.io.LeftIO.valid := io.Data0.valid
//  add.io.LeftIO.bits  := io.Data0.bits
//
//
//  io.Data1.ready := add.io.LeftIO.ready
//  add.io.RightIO.valid := io.Data1.valid
//  add.io.RightIO.bits := io.Data1.bits
//
//  io.result <> add.io.Out(0)

