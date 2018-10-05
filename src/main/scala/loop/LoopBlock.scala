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

class LoopBlockIO(NumIns: Seq[Int], NumOuts : Int, NumExits : Int)(implicit p: Parameters)
  extends HandShakingIONPS(NumOuts)(new DataBundle())
{
  val In          = Flipped(Vec(NumIns.length, Decoupled(new DataBundle())))
  val activate    = Decoupled(new ControlBundle())
  val latchEnable = Flipped(Decoupled(new ControlBundle()))
  val liveIn      = new VariableDecoupledVec(NumIns)
  val loopExit    = Flipped(Vec(NumExits,Decoupled(new ControlBundle())))
  val liveOut     = Flipped(Vec(NumOuts, Decoupled(new DataBundle())))
  val endEnable   = Decoupled(new ControlBundle())
}

@deprecated("Please use LoopBlockO1 if you have compiled your program with O1 optmization", "1.0")
class LoopBlock(ID: Int, NumIns : Seq[Int], NumOuts : Int, NumExits : Int)
                    (implicit p: Parameters,
                     name: sourcecode.Name,
                     file: sourcecode.File) extends HandShakingNPS(ID=ID, NumOuts=NumOuts)(new DataBundle())(p) {

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

  val inputReady = Seq.fill(NumIns.length)(RegInit(true.B))             //RegInit(VecInit(Seq.fill(NumIns.length)(true.B)))
  val liveIn_R   = Seq.fill(NumIns.length)(RegInit(DataBundle.default)) //RegInit(VecInit(Seq.fill(NumIns.length){DataBundle.default}))
  val liveIn_R_valid = for(i <- NumIns.indices) yield {
    val validReg = Seq.fill(NumIns(i)){RegInit(false.B)}
    validReg
  }
  val allValid = for(i <- NumIns.indices) yield {
    val allValid = liveIn_R_valid(i).reduceLeft(_ || _)
    allValid
  }

  val liveOut_R     =  Seq.fill(NumOuts)(RegInit(DataBundle.default))
  val liveOutFire_R = Seq.fill(NumOuts)(RegInit(false.B))

  val exit_R = RegInit(VecInit(Seq.fill(NumExits)(false.B)))
  val exitFire_R = RegInit(VecInit(Seq.fill(NumExits)(false.B)))

  val endEnable_R = RegInit(0.U.asTypeOf(io.endEnable))
  val endEnableFire_R = RegNext(init=false.B,next=io.endEnable.fire())

  def IsInputLatched(): Bool = {
    if (NumIns.length == 0) {
      return true.B
    } else {
      !inputReady.reduceLeft(_ || _)
    }
  }
  // Live outs are ready if all have fired
  def IsLiveOutReady(): Bool = {
    if (NumOuts == 0) {
      return true.B
    } else {
      liveOutFire_R.reduceLeft(_ && _)
    }
  }

  def IsAllValid():Bool = {
    if (NumIns.length == 0) {
      return true.B
    } else {
      !allValid.reduceLeft(_ || _)
    }
  }
  // Note about hidden signals from handshaking:
  //   enable_valid_R is enable.fire()
  //   enable_R is latched io.enable.bits.control

  // Activate signals
  when(io.activate.fire()) {
    activate_R_valid := false.B
  }

  // Latch the block inputs when they fire to drive the liveIn I/O.
  for (i <- 0 until NumIns.length) {
    when(io.In(i).fire()) {
      liveIn_R(i) := io.In(i).bits
      inputReady(i) := false.B
    }
  }

  // Latch the liveOut inputs when they fire to drive the Out I/O
  for (i <- 0 until NumOuts) {
    when(io.liveOut(i).fire()) {
      liveOutFire_R(i) := true.B
      liveOut_R(i) := io.liveOut(i).bits
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
      when (IsInputLatched && IsEnableValid()) {
        when (IsEnable) {
          // Set the loop liveIN data as valid
          liveIn_R_valid.foreach(_.foreach(_ := true.B))
          activate_R_valid := true.B
          state := s_active
        }.otherwise{
          // Jump to end skipping loop
          state := s_end
          // Ensure downstream isn't predicated
          endEnable_R.bits.control := false.B
          endEnable_R.bits.taskID := enable_R.taskID
          endEnable_R.valid := true.B
          liveOut_R.foreach(_.predicate := false.B)
          liveOut_R.foreach(_.taskID := enable_R.taskID)
          ValidOut()
        }
      }
    }
    is(s_active) {
      // Strobe the liveIn output valid signals when latch strobe valid and
      // active.  This indicates a new iteration.
      for(i <- 0 until NumIns.length) {
        for (j <- 0 until NumIns(i)) {
          when(io.latchEnable.fire() && io.latchEnable.bits.control) {
            // Re-enable the liveIn latches to valid for next iteration
            liveIn_R_valid(i).foreach(_ := true.B)
          }.elsewhen(io.liveIn.data(s"field$i")(j).fire()) {
            // clear liveIn valid when loop has grabbed the data
            liveIn_R_valid(i)(j) := false.B
          }
        }
      }
      // If
      //  a) our liveIn data has been accepted, and
      //  b) our live outs are ready, and
      //  c) we've seen a valid exit pulse,
      // then we can end.
      when(exitFire_R.asUInt().orR && IsAllValid() && IsLiveOutReady()) {
        exitFire_R.foreach(_ := false.B)
        liveOutFire_R.foreach(_ := false.B)
        // Only exit on final (control=true) exit pulse
        when(exit_R.asUInt().orR) {
          endEnable_R.bits.control := exit_R.asUInt().orR
          endEnable_R.valid := true.B
          ValidOut() // Set Out() valid
          state := s_end
        }
      }
    }
    is(s_end) {
      when(endEnableFire_R) {
        endEnable_R.valid := false.B
      }
      when(endEnableFire_R && IsOutReady()) {
        Reset()
        inputReady.foreach(_ := true.B)
        exitFire_R.foreach(_ := false.B)
        endEnableFire_R := false.B
        state := s_idle
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
  for (i <- NumIns.indices) {
    io.In(i).ready := inputReady(i)
    for (j <- 0 until NumIns(i)) {
      io.liveIn.data(s"field$i")(j).valid := liveIn_R_valid(i)(j)
      io.liveIn.data(s"field$i")(j).bits := liveIn_R(i)
    }
  }

  // Connect LiveOut registers to I/O
  for (i <- 0 until NumOuts) {
    // Don't backpressure with the live outs. We only care
    // about the values on the last iteration
    io.liveOut(i).ready := true.B
    io.Out(i).bits := liveOut_R(i)
  }

}

class LoopBlockO1(ID: Int, NumIns: Seq[Int], NumOuts: Int, NumExits: Int)
                 (implicit p: Parameters,
                  name: sourcecode.Name,
                  file: sourcecode.File) extends HandShakingNPS(ID = ID, NumOuts = NumOuts)(new DataBundle())(p) {

  // Instantiate TaskController I/O signals
  override lazy val io = IO(new LoopBlockIO(NumIns, NumOuts, NumExits))
  // Printf debugging
  val node_name = name.value
  val module_name = file.value.split("/").tail.last.split("\\.").head.capitalize
  val (cycleCount, _) = Counter(true.B, 32 * 1024)
  override val printfSigil = "[" + module_name + "] " + node_name + ": " + ID + " "

  val s_idle :: s_active :: s_end :: Nil = Enum(3)
  val state = RegInit(s_idle)

  val activate_R = RegInit(ControlBundle.default)
  val activate_R_valid = RegInit(false.B)

  val inputReady = Seq.fill(NumIns.length)(RegInit(true.B)) //RegInit(VecInit(Seq.fill(NumIns.length)(true.B)))
  val liveIn_R = Seq.fill(NumIns.length)(RegInit(DataBundle.default)) //RegInit(VecInit(Seq.fill(NumIns.length){DataBundle.default}))
  val liveIn_R_valid = for (i <- NumIns.indices) yield {
    val validReg = Seq.fill(NumIns(i)) {
      RegInit(false.B)
    }
    validReg
  }
  val allValid = for (i <- NumIns.indices) yield {
    val allValid = liveIn_R_valid(i).reduceLeft(_ || _)
    allValid
  }

  val liveOut_R = Seq.fill(NumOuts)(RegInit(DataBundle.default))
  val liveOutFire_R = Seq.fill(NumOuts)(RegInit(false.B))

  val exit_R = RegInit(VecInit(Seq.fill(NumExits)(false.B)))
  val exitFire_R = RegInit(VecInit(Seq.fill(NumExits)(false.B)))

  val endEnable_R = RegInit(0.U.asTypeOf(io.endEnable))
  val endEnableFire_R = RegNext(init = false.B, next = io.endEnable.fire())

  def IsInputLatched(): Bool = {
    if (NumIns.length == 0) {
      return true.B
    } else {
      !inputReady.reduceLeft(_ || _)
    }
  }

  // Live outs are ready if all have fired
  def IsLiveOutReady(): Bool = {
    if (NumOuts == 0) {
      return true.B
    } else {
      liveOutFire_R.reduceLeft(_ && _)
    }
  }

  def IsAllValid(): Bool = {
    if (NumIns.length == 0) {
      return true.B
    } else {
      !allValid.reduceLeft(_ || _)
    }
  }

  // Note about hidden signals from handshaking:
  //   enable_valid_R is enable.fire()
  //   enable_R is latched io.enable.bits.control

  // Activate signals
  when(io.activate.fire()) {
    activate_R_valid := false.B
  }

  // Latch the block inputs when they fire to drive the liveIn I/O.
  for (i <- 0 until NumIns.length) {
    when(io.In(i).fire()) {
      liveIn_R(i) := io.In(i).bits
      inputReady(i) := false.B
    }
  }

  // Latch the liveOut inputs when they fire to drive the Out I/O
  for (i <- 0 until NumOuts) {
    when(io.liveOut(i).fire()) {
      liveOutFire_R(i) := true.B
      liveOut_R(i) := io.liveOut(i).bits
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
      when(IsInputLatched && IsEnableValid()) {
        when(IsEnable) {
          // Set the loop liveIN data as valid
          liveIn_R_valid.foreach(_.foreach(_ := true.B))
          activate_R_valid := true.B
          state := s_active
        }.otherwise {
          // Jump to end skipping loop
          state := s_end
          // Ensure downstream isn't predicated
          endEnable_R.bits.control := false.B
          endEnable_R.bits.taskID := enable_R.taskID
          endEnable_R.valid := true.B
          liveOut_R.foreach(_.predicate := false.B)
          liveOut_R.foreach(_.taskID := enable_R.taskID)
          ValidOut()
        }
      }
    }
    is(s_active) {
      // Strobe the liveIn output valid signals when latch strobe valid and
      // active.  This indicates a new iteration.
      for (i <- 0 until NumIns.length) {
        for (j <- 0 until NumIns(i)) {
          when(io.latchEnable.fire()) {
            //            when(io.latchEnable.fire() && io.latchEnable.bits.control) {
            // Re-enable the liveIn latches to valid for next iteration
            liveIn_R_valid(i).foreach(_ := true.B)
          }.elsewhen(io.liveIn.data(s"field$i")(j).fire()) {
            // clear liveIn valid when loop has grabbed the data
            liveIn_R_valid(i)(j) := false.B
          }
        }
      }
      // If
      //  a) our liveIn data has been accepted, and
      //  b) our live outs are ready, and
      //  c) we've seen a valid exit pulse,
      // then we can end.
      when((exitFire_R.asUInt().orR) && IsAllValid() && IsLiveOutReady()) {
        exitFire_R.foreach(_ := false.B)
        liveOutFire_R.foreach(_ := false.B)
        // Only exit on final (control=true) exit pulse
        when(exit_R.asUInt().orR) {
          endEnable_R.bits.control := exit_R.asUInt().orR
          endEnable_R.valid := true.B
          ValidOut() // Set Out() valid
          state := s_end
        }
      }
    }
    is(s_end) {
      when(endEnableFire_R) {
        endEnable_R.valid := false.B
      }
      when(endEnableFire_R && IsOutReady()) {
        Reset()
        inputReady.foreach(_ := true.B)
        exitFire_R.foreach(_ := false.B)
        endEnableFire_R := false.B
        state := s_idle
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
  for (i <- NumIns.indices) {
    io.In(i).ready := inputReady(i)
    for (j <- 0 until NumIns(i)) {
      io.liveIn.data(s"field$i")(j).valid := liveIn_R_valid(i)(j)
      io.liveIn.data(s"field$i")(j).bits := liveIn_R(i)
    }
  }

  // Connect LiveOut registers to I/O
  for (i <- 0 until NumOuts) {
    // Don't backpressure with the live outs. We only care
    // about the values on the last iteration
    io.liveOut(i).ready := true.B
    io.Out(i).bits := liveOut_R(i)
  }

}



