package loop

import chisel3.{Flipped, Module, _}
import chisel3.util.{Decoupled, _}
import config._
import config.Parameters
import interfaces._
import utility.Constants._
import junctions._
import node._

/**
  * @brief LoopBlockIO class definition
  * @details Implimentation of BasickBlockIO
  * @param NumIns   Number of liveIn DataBundles
  * @param NumOuts  Number of liveOut DataBundles
  * @param NumExits Number exit control bundles for loop (e.g. number of exit points)
  * I/O:
  *   In          = Connect each element to an upstream liveIn source for the loop
  *   activate    = Connect to the activate input of the LoopHeader
  *   latchEnable = Connect to the enable for the loop feedback path
  *   liveIn      = Connect each element to the corresponding liveIn termination inside the loop
  *   loopExit    = Connect to the exit control bundle(s) for the loop
  *   liveOut     = Connect each element to the corresponding liveOut source inside the loop
  *   endEnable   = Connect to the enable input of the downstream loopEnd block
  *   Out         = Connect each element to a downstream liveOut termination
  * Operation:
  *   The In values are latched when they are valid.  Their values are connected to the
  *   liveIN outputs for the loop logic. The liveIN values will be validated at the start of a loop and each time
  *   the latchEnable signal is active and valid.
  *   The liveOut values are latched when an active and valid loopExit signal is asserted. They are connected
  *   the Out outputs for the downstream logic.  The endEnable is also driven to trigger the end block downstream
  *   from the loop.
  */


class LoopBlockIO(NumIns : Int, NumOuts : Int, NumExits : Int)(implicit p: Parameters)
  extends HandShakingIONPS(NumOuts)(new DataBundle())
{
  val In          = Flipped(Vec(NumIns, Decoupled(new DataBundle())))
  val activate    = Decoupled(new ControlBundle())
  val latchEnable = Flipped(Decoupled(new ControlBundle()))
  val liveIn      = Vec(NumIns, Decoupled(new DataBundle()))
  val loopExit    = Flipped(Vec(NumExits,Decoupled(new ControlBundle())))
  val liveOut     = Flipped(Vec(NumOuts, Decoupled(new DataBundle())))
  val endEnable   = Decoupled(new ControlBundle())
}

class LoopBlock(ID: Int, NumIns : Int, NumOuts : Int, NumExits : Int)
                    (implicit p: Parameters,
                     name: sourcecode.Name,
                     file: sourcecode.File) extends HandShakingNPS(1, ID)(new DataBundle())(p) {

  // Instantiate TaskController I/O signals
  override lazy val io = IO(new LoopBlockIO(NumIns, NumOuts, NumExits))
  // Printf debugging
  val node_name = name.value
  val module_name = file.value.split("/").tail.last.split("\\.").head.capitalize
  val (cycleCount, _) = Counter(true.B, 32 * 1024)
  override val printfSigil = "[" + module_name + "] " + node_name + ": " + ID + " "

  val s_idle :: s_active :: s_end :: Nil = Enum(3)
  val state = RegInit(s_idle)

  val activate_R  = RegInit(ControlBundle.default)
  val activate_R_valid = RegInit(false.B)

  val inputReady = RegInit(VecInit(Seq.fill(NumIns)(true.B)))
  val liveIn_R  = RegInit(VecInit(Seq.fill(NumIns){DataBundle.default}))
  val liveIn_R_valid = RegInit(VecInit(Seq.fill(NumIns)(false.B)))

  val liveOut_R  = RegInit(VecInit(Seq.fill(NumOuts){DataBundle.default}))
  val liveOutValid = RegInit(VecInit(Seq.fill(NumOuts){true.B}))

  val exit_R = RegInit(VecInit(Seq.fill(NumExits)(false.B)))
  val exitFire_R = RegInit(VecInit(Seq.fill(NumExits)(false.B)))

  val endEnable_R = RegInit(0.U.asTypeOf(io.endEnable))
  val endEnableFire_R = RegNext(init=false.B,next=io.endEnable.fire())


  // Note about hidden signals from handshaking:
  //   enable_valid_R is enable.fire()
  //   enable_R is latched io.enable.bits.control

  // Activate signals
  when(io.activate.fire()) {
    activate_R_valid := false.B
  }

  // Latch the block inputs when they fire to drive the liveIn I/O.
  for (i <- 0 until NumIns) {
    when(io.In(i).fire()) {
      liveIn_R(i) := io.In(i).bits
      inputReady(i) := false.B
    }
  }

//  val allReady = io.Out.map(_.ready).reduceLeft(_ && _)

  // Latch the liveOut inputs when they fire to drive the Out I/O
  for (i <- 0 until NumOuts) {
    when(io.liveOut(i).fire()) {
      liveOut_R(i) := io.liveOut(i).bits
      liveOutValid(i) := true.B
    }
  }

  // Latch the exit signals
  for (i <- 0 until NumExits) {
    io.loopExit(i).ready := ~exitFire_R(i)
    when(io.loopExit(i).fire()) {
      exit_R(i) <> io.loopExit(i).bits.control
      exitFire_R(i) := true.B
      endEnable_R.bits.taskID := io.loopExit(i).bits.taskID
    }
  }

  switch(state) {
    is(s_idle) {
      // Wait for input data to be latched and enable to have fired
      when (!inputReady.asUInt.orR && IsEnableValid()) {
        when (IsEnable) {
          // Set the loop liveIN data as valid
          liveIn_R_valid.foreach(_ := true.B)
          activate_R_valid := true.B
          state := s_active
        }.otherwise{
          // Jump to end skipping loop
          state := s_end
          endEnable_R.bits.control := false.B
          endEnable_R.valid := true.B
          liveOut_R.foreach(_.predicate := false.B)
          ValidOut()
        }
      }
    }
    is(s_active) {
      // Strobe the liveIn output valid signals when latch strobe is active
      // This indicates a new iteration
      for(i <- 0 until NumIns) {
        when (io.latchEnable.fire()) {
          // Re-enable the liveIn latches to valid for next iteration
          liveIn_R_valid(i) := true.B
        }.elsewhen(io.liveIn(i).fire()){
          // clear liveIn valid when loop has grabbed the data
          liveIn_R_valid(i) := false.B
        }
      }
      // If we've seen a valid exit pulse and we have valid liveOut data
      when(exitFire_R.asUInt().orR) {
        exitFire_R.foreach(_ := false.B)
        when(exit_R.asUInt().orR && liveOutValid.asUInt().andR) {
          endEnable_R.bits.control := exit_R.asUInt().orR
          endEnable_R.valid := true.B
          ValidOut() // Set Out() valid
          state := s_end
        }
      }
    }
    is(s_end) {
      when(io.endEnable.fire()) {
        endEnable_R.valid := false.B
      }
      when(IsOutReady) {
        // Reset output status and enable status
        Reset()
      }
      when(endEnableFire_R && IsOutReady()) {
        inputReady.foreach(_ := true.B)
        exitFire_R.foreach(_ := false.B)
        endEnableFire_R := false.B
      }
    }
  }

  // Connect up endEnable control bundle
  io.endEnable.bits := endEnable_R.bits
  io.endEnable.valid := endEnable_R.valid

  io.activate.valid := activate_R_valid
  io.activate.bits := enable_R

  io.latchEnable.ready := true.B

  // Connect LiveIn registers to I/O
  for (i <- 0 until NumIns) {
    io.In(i).ready := inputReady(i)
    io.liveIn(i).bits := liveIn_R(i)
    io.liveIn(i).valid := liveIn_R_valid(i)
  }

  // Connect LiveOut registers to I/O
  for (i <- 0 until NumOuts) {
    // Don't backpressure with the live outs. We only care
    // about the values on the last iteration
    io.liveOut(i).ready := true.B
    io.Out(i).bits := liveOut_R(i)
  }

}




