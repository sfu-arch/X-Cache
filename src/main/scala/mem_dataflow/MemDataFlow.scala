package mem_dataflow

/**
  * Created by nvedula on 15/5/17.
  */

import chisel3._
import chisel3.util._
import amin._
import dummy_nodes._


class MemDataFlow(xLen: Int) extends Module{

  val io = IO(new Bundle {
    val Out1_GEP   =  Decoupled(UInt(xLen.W))
    val Out2_Data   =  Decoupled(UInt(xLen.W))
    val In1_St_out  =  Flipped(Decoupled(UInt(1.W)))

    val Out3_Ld = Decoupled(UInt(0.W))

    //Decoupled = ready(I), valid(O), bits(O)
  })



  val m0_gep = Module(new DummyGEP(xLen))
  val m1_data = Module(new DummyGEP(xLen))
  val m2_load = Module(new LoadNode(xLen))
  val m3_store = Module(new StoreNode(xLen))


  //IO Connections
  io.Out1_GEP <> m0_gep.io.In1
  io.Out2_Data <> m1_data.io.In1
  io.In1_St_out <> m3_store.io.Out1

  //Internal Connections
  m0_gep.io.Out1  <> m3_store.io.In1
  m1_data.io.Out1  <> m3_store.io.In2

  m0_gep.io.Out1 <> m2_load.io.In1
  m2_load.io.Out1 <> m3_store.io.In3
  io.Out3_Ld <> m2_load.io.In3

  //Memory connections

}
