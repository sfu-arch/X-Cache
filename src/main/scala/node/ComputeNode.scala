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

class ComputeNodeIO(NumOuts: Int)
                   (implicit p: Parameters)
extends HandShakingIONPS (NumOuts)(new DataBundle) {
// LeftIO: Left input data for computation
val LeftIO = Flipped (Decoupled (new DataBundle) )

// RightIO: Right input data for computation
val RightIO = Flipped (Decoupled (new DataBundle) )
}

class ComputeNode(NumOuts: Int, ID: Int, opCode: String)
                 (sign: Boolean)
                 (implicit p: Parameters)
  extends HandShakingNPS(NumOuts, ID)(new DataBundle)(p) {
  override lazy val io = IO(new ComputeNodeIO(NumOuts))
  // Printf debugging

  /*===========================================*
   *            Registers                      *
   *===========================================*/
  // Left Input
  val left_R = RegInit(DataBundle.default)

  // Right Input
  val right_R = RegInit(DataBundle.default)

  // Output register
  val data_R = RegInit(0.U(xlen.W))

  val s_idle :: s_LATCH :: s_COMPUTE :: Nil = Enum(3)
  val state = RegInit(s_idle)

  /*==========================================*
   *           Predicate Evaluation           *
   *==========================================*/

  val predicate = left_R.predicate & right_R.predicate & IsEnable()
  val start = left_R.valid & right_R.valid & IsEnableValid()

  /*===============================================*
   *            Latch inputs. Wire up output       *
   *===============================================*/

  //printfInfo("start: %x\n", start)

  io.LeftIO.ready := ~left_R.valid
  when(io.LeftIO.fire()) {
    //printfInfo("Latch left data\n")
    state := s_LATCH
    left_R.data := io.LeftIO.bits.data
    left_R.valid := true.B
    left_R.predicate := io.LeftIO.bits.predicate
  }

  io.RightIO.ready := ~right_R.valid
  when(io.RightIO.fire()) {
    //printfInfo("Latch right data\n")
    state := s_LATCH
    right_R.data := io.RightIO.bits.data
    right_R.valid := true.B
    right_R.predicate := io.RightIO.bits.predicate
  }

  // Wire up Outputs
  for (i <- 0 until NumOuts) {
    io.Out(i).bits.data := data_R
    io.Out(i).bits.predicate := predicate 
  }


  /*============================================*
   *            ACTIONS (possibly dangerous)    *
   *============================================*/

  //Instantiate ALU with selected code
  var FU = Module(new UALU(xlen, opCode))

  FU.io.in1 := left_R.data
  FU.io.in2 := right_R.data
  data_R := FU.io.out

  when(start & predicate & state =/= s_COMPUTE) {
    state := s_COMPUTE
    ValidOut()
  }.elsewhen(start & ~predicate & state =/= s_COMPUTE) {
    //printfInfo("Start sending data to output INVALID\n")
    state := s_COMPUTE
    ValidOut()
  }

  /*==========================================*
   *            Output Handshaking and Reset  *
   *==========================================*/

  when(IsOutReady() & (state === s_COMPUTE)) {
    // Reset data
    left_R := DataBundle.default
    right_R := DataBundle.default
    // Reset output
    data_R := 0.U
    out_ready_R := Vec(Seq.fill(NumOuts) {
      false.B
    })
    //Reset state
    state := s_idle
    //Reset output
    Reset()
  }
  var signed = if (sign == true) "S" else "U"
  override val printfSigil = opCode + xlen +  "_" + signed + "_" + ID + ":"

  if (log == true && (comp contains "OP")) {
    val x = RegInit(0.U(xlen.W))
    x     := x + 1.U
  
    verb match {
      case "high"  => { }
      case "med"   => { }
      case "low"   => {
        printfInfo("Cycle %d : { \"Inputs\": {\"Left\": %x, \"Right\": %x},",x,(left_R.valid),(right_R.valid))
        printf("\"State\": {\"State\": \"%x\", \"(L,R)\": \"%x,%x\",  \"O(V,D,P)\": \"%x,%x,%x\" },",state,left_R.data,right_R.data,io.Out(0).valid,data_R,io.Out(0).bits.predicate)
        printf("\"Outputs\": {\"Out\": %x}",io.Out(0).fire())
        printf("}")
       }
      case everythingElse => {}
    }
  }
}
