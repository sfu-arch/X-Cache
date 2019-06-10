package dandelion.node

import chisel3._
import dandelion.config._
import dandelion.interfaces._
import util._

class JumpNodeIO(NumOuts: Int)
                   (implicit p: Parameters)
  extends HandShakingIONPS(NumOuts)(new DataBundle) {
  val Input = Flipped(Decoupled(new DataBundle()))

}

class JumpNode(NumOuts: Int, ID: Int, Desc : String = "JumpNode")
                 (implicit p: Parameters)
  extends HandShakingNPS(NumOuts, ID)(new DataBundle())(p) {
  override lazy val io = IO(new JumpNodeIO(NumOuts))
  // Printf debugging
  override val printfSigil = "Jump Node ID: " + ID + " "
  val (cycleCount,_) = Counter(true.B,32*1024)

  /*===========================================*
   *            Registers                      *
   *===========================================*/
  // Left Input
  val input_R = RegInit(DataBundle.default)
  val input_valid_R = RegInit(false.B)

  //Output register
  val data_R = RegInit(0.U(xlen.W))

  val s_idle :: s_LATCH :: s_COMPUTE :: Nil = Enum(3)
  val state = RegInit(s_idle)

  /*==========================================*
   *           Predicate Evaluation           *
   *==========================================*/

  val predicate = input_R.predicate & IsEnable()
  val start = input_valid_R & IsEnableValid()

  /*===============================================*
   *            Latch inputs. Wire up output       *
   *===============================================*/

  //Instantiate ALU with selected code
  //val FU = Module(new UALU(xlen, opCode))
  //FU.io.in1 := left_R.data
  //FU.io.in2 := right_R.data

  io.Input.ready := ~input_valid_R
  when(io.Input.fire()) {
    //printfInfo("Latch left data\n")
    state := s_LATCH
    input_R <> io.Input.bits
    input_valid_R := true.B
  }

  // Wire up Outputs
  for (i <- 0 until NumOuts) {
    io.Out(i).bits.data := input_R.data
    io.Out(i).bits.predicate := predicate
//     io.Out(i).bits.valid := true.B
  }

  /*============================================*
   *            ACTIONS (possibly dangerous)    *
   *============================================*/

  when(start & state =/= s_COMPUTE) {
    state := s_COMPUTE
    ValidOut()
  }

  /*==========================================*
   *            Output Handshaking and Reset  *
   *==========================================*/

  when(IsOutReady() & (state === s_COMPUTE)) {

    // Reset data
    input_R  := DataBundle.default

    input_valid_R  := false.B

    //Reset state
    state := s_idle
    //Reset output
    Reset()
    printf("[LOG] " + Desc+": Output fired @ %d\n",cycleCount)
  }
  //printfInfo("State: %x", state)

}


