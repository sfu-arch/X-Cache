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

class IcmpNodeIO(NumOuts: Int)
                (implicit p: Parameters)
  extends HandShakingIONPS(NumOuts)(new DataBundle) {
  // LeftIO: Left input data for computation
  val LeftIO = Flipped(Decoupled(new DataBundle))

  // RightIO: Right input data for computation
  val RightIO = Flipped(Decoupled(new DataBundle))
}

class IcmpNode(NumOuts: Int, ID: Int, opCode: String, Desc: String = "IcmpNode")
              (sign: Boolean)
              (implicit p: Parameters)
  extends HandShakingNPS(NumOuts, ID)(new DataBundle)(p) {
  override lazy val io = IO(new ComputeNodeIO(NumOuts))
  // Printf debugging
  override val printfSigil = "Node (ICMP) ID: " + ID + " "
  val (cycleCount, _) = Counter(true.B, 32 * 1024)

  /*===========================================*
   *            Registers                      *
   *===========================================*/
  // Left Input
  val left_R = RegInit(DataBundle.default)
  val left_valid_R = RegInit(false.B)

  // Right Input
  val right_R = RegInit(DataBundle.default)
  val right_valid_R = RegInit(false.B)

  // Output register
  val data_R = RegInit(0.U(xlen.W))

  val s_IDLE :: s_LATCH :: s_COMPUTE :: Nil = Enum(3)
  val state = RegInit(s_IDLE)

  /*==========================================*
   *           Predicate Evaluation           *
   *==========================================*/

  val predicate = left_R.predicate & right_R.predicate & IsEnable()
  val start = left_valid_R & right_valid_R & IsEnableValid()

  /*===============================================*
   *            Latch inputs. Wire up output       *
   *===============================================*/

  val FU = Module(new UCMP(xlen, opCode))
  FU.io.in1 := left_R.data
  FU.io.in2 := right_R.data

  io.LeftIO.ready := ~left_valid_R
  when(io.LeftIO.fire()) {
    left_R <> io.LeftIO.bits
    left_valid_R := true.B
  }

  io.RightIO.ready := ~right_valid_R
  when(io.RightIO.fire()) {
    right_R <> io.RightIO.bits
    right_valid_R := true.B
  }

  // Wire up Outputs
  for (i <- 0 until NumOuts) {
    io.Out(i).bits.data := FU.io.out
    io.Out(i).bits.predicate := predicate
    io.Out(i).bits.taskID := left_R.taskID
  }


  /*============================================*
   *            ACTIONS (possibly dangerous)    *
   *============================================*/

  switch(state) {
    is(s_IDLE) {
      when(enable_valid_R && ~enable_R) {
        left_R := DataBundle.default
        right_R := DataBundle.default

        left_valid_R := false.B
        right_valid_R := false.B

        Reset()
        printf("[LOG] " + Desc + ": Not predicated value -> reset\n")
      }.elsewhen(io.LeftIO.fire() || io.RightIO.fire()) {
        state := s_LATCH
      }
    }
    is(s_LATCH) {
      when(enable_valid_R && ~enable_R) {
        state := s_IDLE
        left_R := DataBundle.default
        right_R := DataBundle.default

        left_valid_R := false.B
        right_valid_R := false.B

        Reset()
      }.elsewhen(start) {
        state := s_COMPUTE
        ValidOut()
      }
    }
    is(s_COMPUTE) {
      when(IsOutReady()) {
        // Reset data
        left_R := DataBundle.default
        right_R := DataBundle.default
        left_valid_R := false.B
        right_valid_R := false.B
        //Reset state
        state := s_IDLE
        //Reset output
        Reset()
        printf("[LOG] " + Desc + ": Output fired @ %d, (%d ? %d) ->  %d\n", cycleCount,left_R.data, right_R.data, FU.io.out)
      }
    }
  }


}
