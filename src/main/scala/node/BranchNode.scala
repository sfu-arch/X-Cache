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


/**
  * @note
  * For Conditional Branch output is always equal to two!
  * Since your branch output wire to two different basic block only
  */

class CBranchNodeIO(NumOuts: Int = 2)
                   (implicit p: Parameters)
  extends HandShakingIONPS(NumOuts)(new ControlBundle) {

  // RightIO: Right input data for computation
  val CmpIO = Flipped(Decoupled(new DataBundle))
}

class CBranchNode(ID: Int, Desc : String = "CBranchNode")
                 (implicit p: Parameters)
  extends HandShakingCtrlNPS(2, ID)(p) {
  override lazy val io = IO(new CBranchNodeIO())
  // Printf debugging
  override val printfSigil = "Node (CBR) ID: " + ID + " "
  val (cycleCount,_) = Counter(true.B,32*1024)

  /*===========================================*
   *            Registers                      *
   *===========================================*/
  // OP Inputs
  val cmp_R = RegInit(DataBundle.default)
  val cmp_valid_R = RegInit(false.B)


  // Output register
  val data_out_w = WireInit(VecInit(Seq.fill(2)(false.B)))

  val s_idle :: s_LATCH :: s_COMPUTE :: Nil = Enum(3)
  val state = RegInit(s_idle)

  /*==========================================*
   *           Predicate Evaluation           *
   *==========================================*/

  val start = cmp_valid_R & IsEnableValid()

  /*===============================================*
   *            Latch inputs. Wire up output       *
   *===============================================*/

  // Predicate register
  //val pred_R = RegInit(init = false.B)


  io.CmpIO.ready := ~cmp_valid_R
  when(io.CmpIO.fire()) {
    state := s_LATCH
    cmp_R <> io.CmpIO.bits
    cmp_valid_R := true.B
  }

  // Wire up Outputs
  io.Out(0).bits.control := data_out_w(0)
  io.Out(0).bits.taskID := 0.U
  io.Out(1).bits.control := data_out_w(1)
  io.Out(1).bits.taskID := 0.U

  /*============================================*
   *            ACTIONS (possibly dangerous)    *
   *============================================*/

  /**
    * Combination of bits and valid signal from CmpIn whill result the output value:
    * valid == 0  ->  output = 0
    * valid == 1  ->  cmp = true  then 1
    * valid == 1  ->  cmp = false then 2
    */

  data_out_w(0) := cmp_R.data.asUInt.orR
  data_out_w(1) := ~cmp_R.data.asUInt.orR

  when(start & state =/= s_COMPUTE) {
    state := s_COMPUTE
    ValidOut()
  }

  /*==========================================*
   *            Output Handshaking and Reset  *
   *==========================================*/


  when(IsOutReady() & (state === s_COMPUTE)){
    //printfInfo("Start restarting output \n")
    // Reset data
    cmp_R := DataBundle.default
    cmp_valid_R := false.B

    // Reset output
    data_out_w := VecInit(Seq.fill(2)(false.B))
    //Reset state
    state := s_idle

    Reset()
    printf("[LOG] " + Desc+": Output fired @ %d\n",cycleCount)

  }

  //printfInfo(" State: %x\n", state)


}

class UBranchNode(ID: Int, Desc : String = "UBranchNode", NumOuts: Int = 1)
                 (implicit p: Parameters)
  extends HandShakingCtrlNPS(NumOuts = NumOuts, ID)(p) {
  override lazy val io = IO(new HandShakingIONPS(NumOuts = NumOuts)(new ControlBundle)(p))
  // Printf debugging
  override val printfSigil = "Node (UBR) ID: " + ID + " "
  val (cycleCount,_) = Counter(true.B,32*1024)

  /*===========================================*
   *            Registers                      *
   *===========================================*/

  // Output register
  val data_R = RegInit(0.U(xlen.W))

  val s_idle :: s_OUTPUT :: Nil = Enum(2)
  val state = RegInit(s_idle)

  /*==========================================*
   *           Predicate Evaluation           *
   *==========================================*/

  val predicate = IsEnable()
  val start = IsEnableValid()

  /*===============================================*
   *            Latch inputs. Wire up output       *
   *===============================================*/

  /**
    * Combination of bits and valid signal from CmpIn whill result the output value:
    * valid == 0  ->  output = 0
    * valid == 1  ->  cmp = true  then 1
    * valid == 1  ->  cmp = false then 2
    * @note data_R value is equale to predicate bit
    */
  // Wire up Outputs
  for( i <- 0 until NumOuts){
    io.Out(i).bits.control := predicate
    io.Out(i).bits.taskID := 0.U

  }

  /*============================================*
   *            ACTIONS (possibly dangerous)    *
   *============================================*/

  when(start & (state === s_idle)) {
    state := s_OUTPUT
    ValidOut()
  }

  /*==========================================*
   *            Output Handshaking and Reset  *
   *==========================================*/


  val out_ready_W = out_ready_R.asUInt.andR
  val out_valid_W = out_valid_R.asUInt.andR

  when(out_ready_W & (state === s_OUTPUT)) {
    //printfInfo("Start restarting output \n")
    //
    Reset()

    //Output predication
    data_R := 0.U

    //Reset state
    state := s_idle
    when (predicate) {printf("[LOG] " + Desc+": Output fired @ %d\n",cycleCount)}



  }

}
