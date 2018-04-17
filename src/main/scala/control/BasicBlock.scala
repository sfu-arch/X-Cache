package control

import chisel3._
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import chisel3.Module
import chisel3.testers._
import chisel3.util._
import org.scalatest.{Matchers, FlatSpec}
import utility.UniformPrintfs

import node._
import config._
import interfaces._
import muxes._
import util._


/**
  * @brief BasicBlockIO class definition
  * @details Implimentation of BasickBlockIO
  * @param NumInputs Number of predecessors
  * @param NumOuts   Number of successor instructions
  * @param NumPhi    Number existing phi nodes
  */

class BasicBlockIO(NumInputs: Int,
                   NumOuts: Int,
                   NumPhi: Int)
                  (implicit p: Parameters)
  extends HandShakingCtrlMaskIO(NumInputs, NumOuts, NumPhi) {

  val predicateIn = Vec(NumInputs, Flipped(Decoupled(new ControlBundle())))
}


/**
  * @brief BasicBlockIO class definition
  * @details Implimentation of BasickBlockIO
  * @param NumInputs Number of predecessors
  * @param NumOuts   Number of successor instructions
  * @param NumPhi    Number existing phi nodes
  * @param BID       BasicBlock ID
  * @note The logic for BasicBlock nodes differs from Compute nodes.
  *       In the BasicBlock nodes, as soon as one of the input signals get fires
  *       all the inputs should get not ready, since we don't need to wait for other
  *       inputs.
  */

class BasicBlockNode(NumInputs: Int,
                     NumOuts: Int,
                     NumPhi: Int,
                     BID: Int)
                    (implicit p: Parameters,
                     name: sourcecode.Name,
                     file: sourcecode.File)
  extends HandShakingCtrlMask(NumInputs, NumOuts, NumPhi, BID)(p) {

  override lazy val io = IO(new BasicBlockIO(NumInputs, NumOuts, NumPhi))
  // Printf debugging
  val node_name = name.value
  val module_name = file.value.split("/").tail.last.split("\\.").head.capitalize
  override val printfSigil = node_name + BID + " "
  val (cycleCount, _) = Counter(true.B, 32 * 1024)

  //Assertion
  assert(NumPhi >= 1, "NumPhi Cannot be zero")
  /*===========================================*
   *            Registers                      *
   *===========================================*/
  // OP Inputs
  val predicate_in_R = RegInit(VecInit(Seq.fill(NumInputs)(ControlBundle.default)))
  val predicate_valid_R = RegInit(VecInit(Seq.fill(NumInputs)(false.B)))

  val s_IDLE :: s_LATCH :: Nil = Enum(2)
  val state = RegInit(s_IDLE)

  /*===========================================*
   *            Valids                         *
   *===========================================*/

  val predicate = predicate_in_R.map(_.control).reduceLeft(_ || _)
  val start = predicate_valid_R.asUInt().andR()

  /*===============================================*
   *            Latch inputs. Wire up output       *
   *===============================================*/

  val pred_R = RegInit(ControlBundle.default)


  for (i <- 0 until NumInputs) {
    io.predicateIn(i).ready := ~predicate_valid_R(i)
    when(io.predicateIn(i).fire()) {
      predicate_in_R(i) <> io.predicateIn(i).bits
      predicate_valid_R(i) := true.B
    }
  }

  // Wire up Outputs
  for (i <- 0 until NumOuts) {
    io.Out(i).bits := predicate_in_R
  }

  // Wire up mask output
  for (i <- 0 until NumPhi) {
    io.MaskBB(i).bits := Reverse(predicate_in_R.asUInt())
  }


  /*============================================*
   *            ACTIONS (possibly dangerous)    *
   *============================================*/

  switch(state) {
    is(s_IDLE) {
      when(predicate_valid_R.asUInt.andR) {
        pred_R.control := predicate
        ValidOut()
        state := s_LATCH
      }
    }
    is(s_LATCH) {
      when(IsOutReady()) {
        predicate_valid_R := VecInit(Seq.fill(NumInputs)(false.B))
        predicate_in_R := VecInit(Seq.fill(NumInputs)(ControlBundle.default))

        Reset()

        state := s_IDLE

        when(predicate) {
          printf("[LOG] " + "[" + module_name + "] " + node_name + ": Output fired @ %d, Mask: %d\n", cycleCount, predicate_in_R.asUInt())
        }.otherwise {
          printf("[LOG] " + "[" + module_name + "] " + node_name + ": Output fired @ %d -> 0 predicate\n", cycleCount)
        }
        //Restart predicate bit
        pred_R.control := false.B
      }
    }

  }


}


/**
  * @brief BasicBlockIO class definition
  * @details Implimentation of BasickBlockIO
  * @param NumInputs Number of predecessors
  * @param NumOuts   Number of successor instructions
  * @param NumPhi    Number existing phi nodes
  * @param BID       BasicBlock ID
  * @note The logic for BasicBlock nodes differs from Compute nodes.
  *       In the BasicBlock nodes, as soon as one of the input signals get fires
  *       all the inputs should get not ready, since we don't need to wait for other
  *       inputs.
  */

class BasicBlockLoopHeadNode(NumInputs: Int,
                             NumOuts: Int,
                             NumPhi: Int,
                             BID: Int)
                            (implicit p: Parameters,
                             name: sourcecode.Name,
                             file: sourcecode.File)
  extends HandShakingCtrlMask(NumInputs, NumOuts, NumPhi, BID)(p) {

  override lazy val io = IO(new BasicBlockIO(NumInputs, NumOuts, NumPhi))
  // Printf debugging
  val node_name = name.value
  val module_name = file.value.split("/").tail.last.split("\\.").head.capitalize
  override val printfSigil = node_name + BID + " "
  val (cycleCount, _) = Counter(true.B, 32 * 1024)

  //Assertion
  assert(NumPhi >= 1, "NumPhi Cannot be zero")
  /*===========================================*
   *            Registers                      *
   *===========================================*/
  // OP Inputs
  val predicate_in_R = RegInit(VecInit(Seq.fill(NumInputs)(false.B)))

  val predicate_valid_R = RegInit(false.B)
  val predicate_valid_W = WireInit(VecInit(Seq.fill(NumInputs)(false.B)))

  val s_idle :: s_LATCH :: s_COMPUTE :: Nil = Enum(3)
  val state = RegInit(s_idle)

  /*===========================================*
   *            Valids                         *
   *===========================================*/

  val predicate = predicate_in_R.asUInt().orR
  val start = predicate_valid_R.asUInt().orR

  /*===============================================*
   *            Latch inputs. Wire up output       *
   *===============================================*/

  val pred_R = RegInit(ControlBundle.default)
  val fire_W = WireInit(false.B)


  //Make all the inputs invalid if one of the inputs
  //gets fire
  //
  when(state === s_idle) {
    predicate_valid_W := VecInit(Seq.fill(NumInputs)(false.B))
  }

  fire_W := predicate_valid_W.asUInt.orR

  when(fire_W & state === s_idle) {
    predicate_valid_R := true.B
  }

  for (i <- 0 until NumInputs) {
    io.predicateIn(i).ready := ~predicate_valid_R
    when(io.predicateIn(i).fire()) {
      state := s_LATCH
      predicate_in_R(i) <> io.predicateIn(i).bits.control
      predicate_valid_W(i) := true.B
    }
  }

  // Wire up Outputs
  for (i <- 0 until NumOuts) {
    io.Out(i).bits.control := pred_R.control
    io.Out(i).bits.taskID := 0.U
  }

  // Wire up mask output
  for (i <- 0 until NumPhi) {
    io.MaskBB(i).bits := predicate_in_R.asUInt
  }


  /*============================================*
   *            ACTIONS (possibly dangerous)    *
   *============================================*/

  when(start & state =/= s_COMPUTE) {
    when(predicate) {
      state := s_COMPUTE
      pred_R.control := predicate
      ValidOut()
    }.otherwise {
      state := s_idle
      predicate_valid_R := false.B
    }
  }

  /*==========================================*
   *      Output Handshaking and Reset        *
   *==========================================*/


  val out_ready_W = out_ready_R.asUInt.andR
  val out_valid_W = out_valid_R.asUInt.andR

  val mask_ready_W = mask_ready_R.asUInt.andR
  val mask_valid_W = mask_valid_R.asUInt.andR


  // Reseting all the latches
  when(out_ready_W & mask_ready_W & (state === s_COMPUTE)) {
    predicate_in_R := VecInit(Seq.fill(NumInputs)(false.B))
    predicate_valid_R := false.B

    // Reset output
    out_ready_R := VecInit(Seq.fill(NumOuts)(false.B))

    //Reset state
    state := s_idle
    when(predicate) {
      printf("[LOG] " + "[" + module_name + "] " + node_name + ": Output fired @ %d, Mask: %d\n", cycleCount, predicate_in_R.asUInt())
    }.otherwise {
      printf("[LOG] " + "[" + module_name + "] " + node_name + ": Output fired @ %d -> 0 predicate\n", cycleCount)
    }
    //Restart predicate bit
    pred_R.control := false.B
  }

}

/**
  * @brief BasicBlockIO class definition
  * @details Implimentation of BasickBlockIO
  * @param NumOuts   Number of successor instructions
  */

class BasicBlockNoMaskIO(NumOuts: Int)
                        (implicit p: Parameters)
  extends HandShakingCtrlNoMaskIO(NumOuts) {
  // LeftIO: Left input data for computation
  //  val predicateIn = Vec(NumInputs, Flipped(Decoupled(new ControlBundle())))
  val predicateIn = Flipped(Decoupled(new ControlBundle()))
}


/**
  * @brief BasicBlockIO class definition
  * @details Implimentation of BasickBlockIO
  * @param NumInputs Number of predecessors
  * @param NumOuts   Number of successor instructions
  * @param BID       BasicBlock ID
  */

class BasicBlockNoMaskNode(NumInputs: Int,
                           NumOuts: Int,
                           BID: Int)
                          (implicit p: Parameters,
                           name: sourcecode.Name,
                           file: sourcecode.File)
  extends HandShakingCtrlNoMask(NumInputs, NumOuts, BID)(p) {

  override lazy val io = IO(new BasicBlockNoMaskIO(NumOuts))
  // Printf debugging
  val node_name = name.value
  val module_name = file.value.split("/").tail.last.split("\\.").head.capitalize
  val (cycleCount, _) = Counter(true.B, 32 * 1024)
  override val printfSigil = node_name + BID + " "

  /*===========================================*
   *            Registers                      *
   *===========================================*/
  val predicate_in_R = RegInit(ControlBundle.default)
  val predicate_valid_R = RegInit(false.B)

  val s_IDLE :: s_COMPUTE :: Nil = Enum(2)
  val state = RegInit(s_IDLE)

  /*===============================================*
   *            Latch inputs. Wire up output       *
   *===============================================*/

  io.predicateIn.ready := ~predicate_valid_R
  when(io.predicateIn.fire()) {
    predicate_in_R <> io.predicateIn.bits
    predicate_valid_R := true.B
  }

  // Wire up Outputs
  for (i <- 0 until NumOuts) {
    io.Out(i).bits <> predicate_in_R
  }


  /*============================================*
   *            ACTIONS (possibly dangerous)    *
   *============================================*/

  switch(state) {
    is(s_IDLE) {
      when(io.predicateIn.fire()) {
        ValidOut()
        state := s_COMPUTE
      }
    }
    is(s_COMPUTE) {
      when(IsOutReady()) {
        predicate_in_R <> ControlBundle.default
        predicate_valid_R := false.B
        state := s_IDLE

        Reset()

        printf("[LOG] " + "[" + module_name + "] [TID->%d] " + node_name + ": Output [T] fired @ %d\n",
          predicate_in_R.taskID, cycleCount)
      }
    }

  }

}

class BasicBlockNoMaskFastIO(val NumOuts: Int)(implicit p: Parameters)
  extends CoreBundle()(p) {
  // Output IO
  val predicateIn = Flipped(Decoupled(new ControlBundle()))
  val Out = Vec(NumOuts, Decoupled(new ControlBundle))
}

class BasicBlockNoMaskFastNode(BID: Int, val NumInputs: Int, val NumOuts: Int)(implicit val p: Parameters,
                                                                               name: sourcecode.Name,
                                                                               file: sourcecode.File)
  extends Module with CoreParams with UniformPrintfs {

  val io = IO(new BasicBlockNoMaskFastIO(NumOuts)(p))
  // Printf debugging
  val node_name = name.value
  val module_name = file.value.split("/").tail.last.split("\\.").head.capitalize
  val (cycleCount, _) = Counter(true.B, 32 * 1024)
  override val printfSigil = "[" + module_name + "] " + node_name + ": " + BID + " "
  /*===========================================*
   *            Registers                      *
   *===========================================*/
  val allReady = io.Out.map(_.ready).reduceLeft(_ && _)

  io.Out.foreach(_.bits := io.predicateIn.bits)
  io.Out.foreach(_.valid := io.predicateIn.valid)

  io.predicateIn.ready := allReady // || allFired

}


/**
  * @note
  * For Conditional Branch output is always equal to two!
  * Since your branch output wire to two different basic block only
  */

class LoopHeadNodeIO(val NumOuts: Int, val NumPhi: Int)(implicit p: Parameters) extends CoreBundle {

  // Predicate enable
  val activate = Flipped(Decoupled(new ControlBundle))
  val loopBack = Flipped(Decoupled(new ControlBundle))

  // Output IO
  val Out = Vec(NumOuts, Decoupled(new ControlBundle))
  val MaskBB = Vec(NumPhi, Decoupled(UInt(2.W)))
}

class LoopHead(val BID: Int, val NumOuts: Int, val NumPhi: Int)
              (implicit val p: Parameters,
               name: sourcecode.Name,
               file: sourcecode.File) extends Module with CoreParams with UniformPrintfs {
  // Defining IOs
  val io = IO(new LoopHeadNodeIO(NumOuts, NumPhi))
  // Printf debugging
  val node_name = name.value
  val module_name = file.value.split("/").tail.last.split("\\.").head.capitalize
  override val printfSigil = "[" + module_name + "] " + node_name + ": " + BID + " "
  val (cycleCount, _) = Counter(true.B, 32 * 1024)

  /*===========================================*
   *            Registers                      *
   *===========================================*/
  // Enable Input
  val active_R = RegInit(ControlBundle.default)
  val active_valid_R = RegInit(false.B)

  val loop_back_R = RegInit(ControlBundle.default)
  val loop_back_valid_R = RegInit(false.B)

  val end_loop_R = RegInit(ControlBundle.default)
  val end_loop_valid_R = RegInit(false.B)

  // Output Handshaking
  val out_value = RegInit(ControlBundle.default)
  val out_val_R = RegInit(VecInit(Seq.fill(NumOuts)(false.B)))
  val out_ready_R = RegInit(VecInit(Seq.fill(NumOuts)(false.B)))
  val out_valid_R = RegInit(VecInit(Seq.fill(NumOuts)(false.B)))

  val mask_ready_R = RegInit(VecInit(Seq.fill(NumPhi)(false.B)))
  val mask_valid_R = RegInit(VecInit(Seq.fill(NumPhi)(false.B)))
  val mask_value_R = RegInit(0.U(2.W))
  val mask_ready_W = mask_ready_R.asUInt.andR

  val s_START :: s_FEED :: s_END :: Nil = Enum(3)
  val state = RegInit(s_START)
  val prev_state = RegInit(s_START)

  io.activate.ready := ~active_valid_R
  when(io.activate.fire()) {
    active_R <> io.activate.bits
    active_valid_R := true.B
  }

  io.loopBack.ready := ~loop_back_valid_R
  when(io.loopBack.fire()) {
    loop_back_R <> io.loopBack.bits
    loop_back_valid_R := true.B
  }

  for (i <- 0 until NumOuts) {
    io.Out(i).bits <> out_value
  }

  // Wire up OUT READYs and VALIDs
  for (i <- 0 until NumOuts) {
    io.Out(i).valid := out_valid_R(i)
    when(io.Out(i).fire()) {
      // Detecting when to reset
      out_ready_R(i) := io.Out(i).ready
      // Propagating output
      out_valid_R(i) := false.B
    }
  }

  // Wire up MASK Readys and Valids
  for (i <- 0 until NumPhi) {
    io.MaskBB(i).bits := mask_value_R
    io.MaskBB(i).valid := mask_valid_R(i)
    when(io.MaskBB(i).fire()) {
      // Detecting when to reset
      mask_ready_R(i) := io.MaskBB(i).ready
      // Propagating mask
      mask_valid_R(i) := false.B
    }

  }


  /*=================
   * States
   ==================*/

  switch(state) {
    is(s_START) {
      mask_value_R := 1.U
    }
    is(s_END) {
      mask_value_R := 2.U
    }
  }

  switch(state) {
    is(s_START) {
      when(active_valid_R) {
        when(active_R.control) {
          //Valid the output
          out_value <> active_R
          out_valid_R := VecInit(Seq.fill(NumOuts)(true.B))
          mask_valid_R := VecInit(Seq.fill(NumPhi)(true.B))
          state := s_FEED
          prev_state := s_START
          printf("[LOG] " + "[" + module_name + "] [TID->%d] " + node_name + ": Active fired @ %d, Mask: %d\n",active_R.taskID, cycleCount, 1.U)
        }.otherwise {
          active_R := ControlBundle.default
          active_valid_R := false.B
          state := s_START
        }
      }
    }
    is(s_FEED) {
      when(out_ready_R.asUInt().andR()) {
        out_ready_R := VecInit(Seq.fill(NumOuts)(false.B))
        out_valid_R := VecInit(Seq.fill(NumOuts)(false.B))

        mask_ready_R := VecInit(Seq.fill(NumPhi)(false.B))
        mask_valid_R := VecInit(Seq.fill(NumPhi)(false.B))

        state := s_END
      }
    }
    is(s_END) {
      when(loop_back_valid_R) {

//        loop_back_R := ControlBundle.default
        loop_back_valid_R := false.B

        when(loop_back_R.control) {
          out_value <> loop_back_R
          out_valid_R := VecInit(Seq.fill(NumOuts)(true.B))
          mask_valid_R := VecInit(Seq.fill(NumPhi)(true.B))

          printf("[LOG] " + "[" + module_name + "] [TID->%d] " + node_name + ": LoopBack fired @ %d, Mask: %d\n",loop_back_R.taskID, cycleCount, 2.U)

          state := s_FEED

        }.otherwise {
//          active_R := ControlBundle.default
          active_valid_R := false.B
          state := s_START

        }
      }
    }


  }


}









