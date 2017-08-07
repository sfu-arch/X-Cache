package dataflow.filter

import chisel3._
import chisel3.util._

import node._
import config._
import interfaces._
import arbiters._
import memory._

class FilterDF(implicit val p: Parameters) extends Module with CoreParams {

  val FilterSize = 3*3
 
  val io = IO(new Bundle {
    val enable = Flipped(Decoupled(Bool()))
    val dptr   = Vec(FilterSize,Flipped(Decoupled(new DataBundle())))
    val kptr   = Vec(FilterSize,Flipped(Decoupled(new DataBundle())))
    val sum    = Decoupled(new DataBundle())
  })

  val DataLoader = Module(new BasicLoader()(p))
  val KernLoader = Module(new BasicLoader()(p))
  val Filt = Module(new BasicFilter()(p))
  
  DataLoader.io.enable <> io.enable
  DataLoader.io.ptr <> io.dptr;  

  KernLoader.io.enable <> io.enable
  KernLoader.io.ptr <> io.kptr;  

  Filt.io.enable <> io.enable
  Filt.io.data <> DataLoader.io.data
  Filt.io.kern <> KernLoader.io.data

  io.sum <> Filt.io.sum;

}

