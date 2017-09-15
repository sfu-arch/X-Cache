package loop

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
import node._

class LoopRegNodeIO(NumOuts: Int)
                  (implicit p: Parameters)
  extends HandShakingIONPS(NumOuts)(new DataBundle) {

  // Inputs should be fed only when Ready is HIGH
  // Inputs are always latched.
  // If Ready is LOW; Do not change the inputs as this will cause a bug
  val inData = Flipped(Decoupled(new DataBundle))

}

class LoopRegNode(NumOuts: Int, ID: Int)
             (numByte1: Int)
             (implicit p: Parameters)
  extends HandShakingNPS(NumOuts, ID)(new DataBundle)(p) {
  override lazy val io = IO(new LoopRegNodeIO(NumOuts))
  // Printf debugging
  override val printfSigil = "Node ID: " + ID + " "

  /*===========================================*
   *            Registers                      *
   *===========================================*/
  // Addr Inputs
  val in_data_R = RegInit(DataBundle.default)


  // Output register
  val data_R = RegInit(0.U(xlen.W))

  val s_idle :: s_LATCH :: s_COMPUTE :: Nil = Enum(3)
  val state = RegInit(s_idle)

  /*==========================================*
   *           Predicate Evaluation           *
   *==========================================*/

  val predicate = in_data_R.predicate & IsEnable()
  val start = in_data_R.valid & IsEnableValid()

  /*===============================================*
   *            Latch inputs. Wire up output       *
   *===============================================*/

  io.inData.ready := ~in_data_R.valid
  when(io.inData.fire()) {
    //printfInfo("Latch left data\n")
    state := s_LATCH
    in_data_R.data := io.inData.bits.data
    in_data_R.valid := true.B
    in_data_R.predicate := io.inData.bits.predicate
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

  data_R := in_data_R.data

  when(start & predicate & state =/= s_COMPUTE) {
    state := s_COMPUTE
    ValidOut()
  }.elsewhen(start & !predicate & state =/= s_COMPUTE) {
    state := s_COMPUTE
    ValidOut()
  }

  /*==========================================*
   *            Output Handshaking and Reset  *
   *==========================================*/

  when(IsOutReady() & state === s_COMPUTE) {
    //printfInfo("Start restarting output \n")
    // Reset data
    in_data_R := DataBundle.default

    // Reset output
    data_R := 0.U

    //Reset output
    Reset()
  }

  printfInfo(" State: %x\n", state)

}

