package node

import chisel3._
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import chisel3.Module
import chisel3.testers._
import chisel3.util._
import org.scalatest.{Matchers, FlatSpec}

import config._
import interfaces._
import muxes._
import util._

class GepNodeOneIO(NumOuts: Int)
                  (implicit p: Parameters)
  extends HandShakingIONPS(NumOuts)(new DataBundle) {

  // Inputs should be fed only when Ready is HIGH
  // Inputs are always latched.
  // If Ready is LOW; Do not change the inputs as this will cause a bug
  val baseAddress = Flipped(Decoupled(new DataBundle))
  val idx1 = Flipped(Decoupled(new DataBundle))

}

class GepNodeTwoIO(NumOuts: Int)
               (implicit p: Parameters)
  extends HandShakingIONPS(NumOuts)(new DataBundle) {

  // Inputs should be fed only when Ready is HIGH
  // Inputs are always latched.
  // If Ready is LOW; Do not change the inputs as this will cause a bug
  val baseAddress = Flipped(Decoupled(new DataBundle))
  val idx1 = Flipped(Decoupled(new DataBundle))
  val idx2 = Flipped(Decoupled(new DataBundle))

}

class GepOneNode(NumOuts: Int, ID: Int, opCode: String)
             (numByte1: Int,
              numByte2: Int)
             (implicit p: Parameters)
  extends HandShakingNPS(NumOuts, ID)(new DataBundle)(p) {
  override lazy val io = IO(new GepNodeTwoIO(NumOuts))
  // Printf debugging
  override val printfSigil = "Node ID: " + ID + " "

  /*===========================================*
   *            Registers                      *
   *===========================================*/
  // Addr Inputs
  val base_addr_R = RegInit(DataBundle.default)

  // Index 1 input
  val idx1_R = RegInit(DataBundle.default)


  // Output register
  val data_R = RegInit(0.U(xlen.W))

  /*==========================================*
   *           Predicate Evaluation           *
   *==========================================*/

  val predicate = base_addr_R.predicate & idx1_R.predicate & IsEnable()
  val start = base_addr_R.valid & idx1_R.valid & IsEnableValid()

  /*===============================================*
   *            Latch inputs. Wire up output       *
   *===============================================*/

  io.baseAddress.ready := ~base_addr_R.valid
  when(io.baseAddress.fire()) {
    //printfInfo("Latch left data\n")
    base_addr_R.data := io.baseAddress.bits.data
    base_addr_R.valid := true.B
    base_addr_R.predicate := io.baseAddress.bits.predicate
  }

  io.idx1.ready := ~idx1_R.valid
  when(io.idx1.fire()) {
    //printfInfo("Latch right data\n")
    idx1_R.data := io.idx1.bits.data
    idx1_R.valid := true.B
    idx1_R.predicate := io.idx1.bits.predicate
  }

  // Wire up Outputs
  for (i <- 0 until NumOuts) {
    io.Out(i).bits.data := data_R
    io.Out(i).bits.predicate := predicate
  }


  /*============================================*
   *            ACTIONS (possibly dangerous)    *
   *============================================*/

  //Compute the address

  val comp_addr_W = base_addr_R.data +
    (idx1_R.data * numByte1.U)

  when(start & predicate) {
    data_R := comp_addr_W
    ValidOut()
  }.elsewhen(start & !predicate) {
    ValidOut()
  }

  /*==========================================*
   *            Output Handshaking and Reset  *
   *==========================================*/

  when(IsOutReady()) {
    //printfInfo("Start restarting output \n")
    // Reset data
    base_addr_R := DataBundle.default
    idx1_R := DataBundle.default

    // Reset output
    data_R := 0.U

    //Reset output
    Reset()
  }

}


class GepTwoNode(NumOuts: Int, ID: Int, opCode: String)
             (numByte1: Int,
              numByte2: Int)
             (implicit p: Parameters)
  extends HandShakingNPS(NumOuts, ID)(new DataBundle)(p) {
  override lazy val io = IO(new GepNodeTwoIO(NumOuts))
  // Printf debugging
  override val printfSigil = "Node ID: " + ID + " "

  /*===========================================*
   *            Registers                      *
   *===========================================*/
  // Addr Inputs
  val base_addr_R = RegInit(DataBundle.default)

  // Index 1 input
  val idx1_R = RegInit(DataBundle.default)

  // Index 2 input
  val idx2_R = RegInit(DataBundle.default)

  // Output register
  val data_R = RegInit(0.U(xlen.W))

  /*==========================================*
   *           Predicate Evaluation           *
   *==========================================*/

  val predicate = base_addr_R.predicate & idx1_R.predicate & idx2_R.predicate & IsEnable()
  val start = base_addr_R.valid & idx1_R.valid & idx2_R.valid & IsEnableValid()

  /*===============================================*
   *            Latch inputs. Wire up output       *
   *===============================================*/

  io.baseAddress.ready := ~base_addr_R.valid
  when(io.baseAddress.fire()) {
    //printfInfo("Latch left data\n")
    base_addr_R.data := io.baseAddress.bits.data
    base_addr_R.valid := true.B
    base_addr_R.predicate := io.baseAddress.bits.predicate
  }

  io.idx1.ready := ~idx1_R.valid
  when(io.idx1.fire()) {
    //printfInfo("Latch right data\n")
    idx1_R.data := io.idx1.bits.data
    idx1_R.valid := true.B
    idx1_R.predicate := io.idx1.bits.predicate
  }

  io.idx2.ready := ~idx2_R.valid
  when(io.idx2.fire()) {
    //printfInfo("Latch right data\n")
    idx2_R.data := io.idx2.bits.data
    idx2_R.valid := true.B
    idx2_R.predicate := io.idx2.bits.predicate
  }

  // Wire up Outputs
  for (i <- 0 until NumOuts) {
    io.Out(i).bits.data := data_R
    io.Out(i).bits.predicate := predicate
  }


  /*============================================*
   *            ACTIONS (possibly dangerous)    *
   *============================================*/

  //Compute the address

  val comp_addr_W = base_addr_R.data +
    (idx1_R.data * numByte1.U) + (idx2_R.data * numByte2.U)

  data_R := comp_addr_W
    
  ValidOut()

  /*==========================================*
   *            Output Handshaking and Reset  *
   *==========================================*/


  when(IsOutReady()) {
    //printfInfo("Start restarting output \n")
    // Reset data

    base_addr_R := DataBundle.default
    idx1_R := DataBundle.default
    idx2_R := DataBundle.default
    // Reset output
    data_R := 0.U
    //Reset output
    Reset()
  }

}
