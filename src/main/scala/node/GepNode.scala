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
import utility._

class GepNodeOneIO(NumOuts: Int)
                  (implicit p: Parameters)
  extends HandShakingIONPS(NumOuts)(new DataBundle) {

  // Inputs should be fed only when Ready is HIGH
  // Inputs are always latched.
  // If Ready is LOW; Do not change the inputs as this will cause a bug
  val baseAddress = Flipped(Decoupled(new DataBundle()))
  val idx1        = Flipped(Decoupled(new DataBundle()))

}

class GepNodeTwoIO(NumOuts: Int)
               (implicit p: Parameters)
  extends HandShakingIONPS(NumOuts)(new DataBundle) {

  // Inputs should be fed only when Ready is HIGH
  // Inputs are always latched.
  // If Ready is LOW; Do not change the inputs as this will cause a bug
  val baseAddress = Flipped(Decoupled(new DataBundle()))
  val idx1 = Flipped(Decoupled(new DataBundle()))
  val idx2 = Flipped(Decoupled(new DataBundle()))

}

class GepOneNode(NumOuts: Int, ID: Int)
             (numByte1: Int)
             (implicit p: Parameters,
              name: sourcecode.Name,
              file: sourcecode.File)
  extends HandShakingNPS(NumOuts, ID)(new DataBundle)(p) {
  override lazy val io = IO(new GepNodeOneIO(NumOuts))
  // Printf debugging
  val node_name = name.value
  val module_name = file.value.split("/").tail.last.split("\\.").head.capitalize
  override val printfSigil =  "[" + module_name + "] " + node_name + ": " + ID + " "
  val (cycleCount,_) = Counter(true.B,32*1024)

  /*===========================================*
   *            Registers                      *
   *===========================================*/
  // Addr Inputs
  val base_addr_R = RegInit(DataBundle.default)
  val base_addr_valid_R = RegInit(false.B)

  // Index 1 input
  val idx1_R = RegInit(DataBundle.default)
  val idx1_valid_R = RegInit(false.B)

  val s_idle :: s_LATCH :: s_COMPUTE :: Nil = Enum(3)
  val state = RegInit(s_idle)

  /*==========================================*
   *           Predicate Evaluation           *
   *==========================================*/

  val predicate = base_addr_R.predicate & idx1_R.predicate & IsEnable()
  val start = base_addr_valid_R & idx1_valid_R & IsEnableValid()

  /*===============================================*
   *            Latch inputs. Wire up output       *
   *===============================================*/

  // Output register
  val data_W = base_addr_R.data +
    (idx1_R.data * numByte1.U)

  io.baseAddress.ready := ~base_addr_valid_R
  when(io.baseAddress.fire()) {
    //printfInfo("Latch left data\n")
    state := s_LATCH
    base_addr_R.data := io.baseAddress.bits.data
    base_addr_R.predicate := true.B
    //base_addr_R.valid := true.B

    base_addr_valid_R := true.B
  }

  io.idx1.ready := ~idx1_valid_R
  when(io.idx1.fire()) {
    //printfInfo("Latch right data\n")
    state := s_LATCH
    idx1_R.data :=  io.idx1.bits.data
    idx1_R.predicate := true.B
    //idx1_R.valid := true.B

    idx1_valid_R := true.B
  }

  // Wire up Outputs
  for (i <- 0 until NumOuts) {
    io.Out(i).bits.data := data_W
    io.Out(i).bits.predicate := predicate
    io.Out(i).bits.taskID := base_addr_R.taskID
  }


  /*============================================*
   *            ACTIONS (possibly dangerous)    *
   *============================================*/

  //Compute the address


  when(start & state =/= s_COMPUTE) {
    state := s_COMPUTE
    ValidOut()
  }

  /*==========================================*
   *            Output Handshaking and Reset  *
   *==========================================*/

  when(IsOutReady() & state === s_COMPUTE) {
    //printfInfo("Start restarting output \n")
    // Reset data
    base_addr_R := DataBundle.default
    base_addr_valid_R := false.B

    idx1_R := DataBundle.default
    idx1_valid_R := false.B

    state := s_idle
    when (predicate) {printf("[LOG] " + "[" + module_name + "] " + node_name +  ": Output fired @ %d\n", cycleCount)}

    //Reset output
    Reset()
  }

  //printfInfo(" State: %x\n", state)

}


class GepTwoNode(NumOuts: Int, ID: Int)
             (numByte1: Int,
              numByte2: Int)
             (implicit p: Parameters,
              name: sourcecode.Name,
              file: sourcecode.File)
  extends HandShakingNPS(NumOuts, ID)(new DataBundle)(p) {
  override lazy val io = IO(new GepNodeTwoIO(NumOuts))
  // Printf debugging
  val node_name = name.value
  val module_name = file.value.split("/").tail.last.split("\\.").head.capitalize
  override val printfSigil =  "[" + module_name + "] " + node_name + ": " + ID + " "
  val (cycleCount,_) = Counter(true.B,32*1024)

  /*===========================================*
   *            Registers                      *
   *===========================================*/
  // Addr Inputs
  val base_addr_R = RegInit(DataBundle.default)
  val base_addr_valid_R = RegInit(false.B)

  // Index 1 input
  val idx1_R = RegInit(DataBundle.default)
  val idx1_valid_R = RegInit(false.B)

  // Index 2 input
  val idx2_R = RegInit(DataBundle.default)
  val idx2_valid_R = RegInit(false.B)

  // Output register
  val data_W = WireInit(0.U(xlen.W))

  val s_idle :: s_LATCH :: s_COMPUTE :: Nil = Enum(3)
  val state = RegInit(s_idle)

  /*==========================================*
   *           Predicate Evaluation           *
   *==========================================*/

  val predicate = base_addr_R.predicate & idx1_R.predicate & idx2_R.predicate & IsEnable()
  val start = base_addr_valid_R & idx1_valid_R & idx2_valid_R & IsEnableValid()

  /*===============================================*
   *            Latch inputs. Wire up output       *
   *===============================================*/

  io.baseAddress.ready := ~base_addr_valid_R
  when(io.baseAddress.fire()) {
    //printfInfo("Latch left data\n")
    state := s_LATCH
    base_addr_R.data := io.baseAddress.bits.data
    base_addr_R.predicate := true.B
    //base_addr_R.valid := true.B

    base_addr_valid_R := true.B
  }

  io.idx1.ready := ~idx1_valid_R
  when(io.idx1.fire()) {
    //printfInfo("Latch right data\n")
    state := s_LATCH
    idx1_R.data :=  io.idx1.bits.data
    idx1_R.predicate := true.B
    //idx1_R.valid := true.B

    idx1_valid_R := true.B
  }

  io.idx2.ready := ~idx2_valid_R
  when(io.idx2.fire()) {
    //printfInfo("Latch right data\n")
    state := s_LATCH
    idx2_R.data := io.idx2.bits.data
    idx2_R.predicate := true.B
    //idx2_R.valid := true.B

    idx2_valid_R := true.B
  }

  // Wire up Outputs
  for (i <- 0 until NumOuts) {
    io.Out(i).bits.data := data_W
    io.Out(i).bits.predicate := predicate
    io.Out(i).bits.taskID := base_addr_R.taskID
  }


  /*============================================*
   *            ACTIONS (possibly dangerous)    *
   *============================================*/

  //Compute the address

  data_W := base_addr_R.data +
    (idx1_R.data * numByte1.U) + (idx2_R.data * numByte2.U)

  when(start & state =/= s_COMPUTE) {
    state := s_COMPUTE
    ValidOut()
  }



  /*==========================================*
   *            Output Handshaking and Reset  *
   *==========================================*/


  when(IsOutReady() & state === s_COMPUTE) {
    //printfInfo("Start restarting output \n")
    // Reset data

    base_addr_R := DataBundle.default
    base_addr_valid_R := false.B

    idx1_R := DataBundle.default
    idx1_valid_R := false.B

    idx2_R := DataBundle.default
    idx2_valid_R := false.B

    state := s_idle
    when (predicate) {
      printf("[LOG] " + "[" + module_name + "] " + node_name +  ": Output fired @ %d\n", cycleCount)
    }

    //Reset output
    Reset()
  }

//  printfInfo(" State: %x\n", state)
}
