package FPU

import chisel3._
import chisel3.iotesters.{ChiselFlatSpec, Driver, OrderedDecoupledHWIOTester, PeekPokeTester}
import chisel3.Module
import chisel3.testers._
import chisel3.util._
import org.scalatest.{FlatSpec, Matchers}
import config._
import hardfloat._
import interfaces._
import muxes._
import util._
import node._
import FType._

class FNtoFNNodeIO(Src: FType, Des: FType,NumOuts: Int)
                   (implicit p: Parameters)
  extends HandShakingIONPS(NumOuts)(new CustomDataBundle(UInt((Des.ieeeWidth).W))) {
  // LeftIO: Left input data for computation
  //Input for floating point width
    val Input = Flipped(Decoupled(new CustomDataBundle(UInt((Src.ieeeWidth).W))))
    // Output gets initialized as part of Handshaking.
}

class FNtoFNNode(Src: FType, Des: FType, NumOuts: Int, ID: Int)
                 (implicit p: Parameters,
                  name: sourcecode.Name,
                  file: sourcecode.File)
  extends HandShakingNPS(NumOuts, ID)(new CustomDataBundle(UInt((Des.ieeeWidth).W)))(p) {
  override lazy val io = IO(new FNtoFNNodeIO(Src,Des, NumOuts))


// Printf debugging
  val node_name = name.value
  val module_name = file.value.split("/").tail.last.split("\\.").head.capitalize

  override val printfSigil = "[" + module_name + "] " + node_name + ": " + ID + " "
  //  override val printfSigil = "Node (COMP - " + opCode + ") ID: " + ID + " "
  val (cycleCount, _) = Counter(true.B, 32 * 1024)

  /*===========================================*
   *            Registers                      *
   *===========================================*/
  // Left Input
  val Input_R = Reg(new CustomDataBundle((UInt((Src.ieeeWidth).W))))
  val Input_valid_R = RegInit(false.B)

  //Output register
  val data_R = Reg(new CustomDataBundle((UInt((Src.ieeeWidth).W))))

  val s_IDLE :: s_COMPUTE :: Nil = Enum(2)
  val state = RegInit(s_IDLE)


  val predicate = Input_R.predicate & IsEnable()

  /*===============================================*
   *            Latch inputs. Wire up output       *
   *===============================================*/

  //Instantiate FN resize with selected code
  val recFNsrc = recFNFromFN(Src.expWidth,Src.sigWidth,Input_R.data)
  val ResizeFU = Module(new RecFNToRecFN(Src.expWidth, Src.sigWidth, Des.expWidth, Des.sigWidth))
  ResizeFU.io.in             := recFNsrc
  ResizeFU.io.roundingMode   := "b110".U(3.W)
  ResizeFU.io.detectTininess := 0.U(1.W)

  io.Input.ready := ~Input_valid_R
  when(io.Input.fire()) {
    Input_R <> io.Input.bits
    Input_valid_R := true.B
  }

  // Wire up Outputs
  for (i <- 0 until NumOuts) {
    io.Out(i).bits.data := Des.ieee(ResizeFU.io.out)
    io.Out(i).bits.predicate := predicate
    io.Out(i).bits.taskID := Input_R.taskID
  }

  /*============================================*
   *            State Machine                   *
   *============================================*/
  switch(state) {
    is(s_IDLE) {
      when(enable_valid_R) {
        // when((~enable_R).toBool) {
        //   Input_R.predicate := false.B
        //   Input_valid_R := false.B
        //   Reset()
        //   printf("[LOG] " + "[" + module_name + "] " + node_name + ": Not predicated value -> reset\n")
        // }
        when((io.Input.fire() || Input_valid_R)) {
          ValidOut()
          state := s_COMPUTE
        }
      }

    }
    is(s_COMPUTE) {
      when(IsOutReady()) {
        // Reset data
        Input_R := DataBundle.default
        Input_valid_R := false.B
        //Reset state
        state := s_IDLE
        //Reset output
        Reset()
        printf("[LOG] " + "[" + module_name + Src.ieeeWidth + "to" + Des.ieeeWidth + "] " + node_name + ": Output fired @ %d, Value: %x\n", cycleCount, Des.ieee(ResizeFU.io.out))
      }
    }
  }
}

