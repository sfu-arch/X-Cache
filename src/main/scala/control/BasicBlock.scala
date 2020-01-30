package dandelion.control

import chisel3._
import chisel3.Module
import utility.UniformPrintfs
import chipsalliance.rocketchip.config._
import dandelion.config._
import dandelion.interfaces._
import util._


/**
  * @brief BasicBlockIO class definition
  * @note Implimentation of BasickBlockIO
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

  override def cloneType = new BasicBlockIO(NumInputs, NumOuts, NumPhi).asInstanceOf[this.type]
}

/**
  * @brief BasicBlockIO class definition
  * @note Implimentation of BasickBlockIO
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
  /*===========================================*
   *            Registers                      *
   *===========================================*/
  // OP Inputs
  val predicate_in_R = Seq.fill(NumInputs)(RegInit(ControlBundle.default))
  val predicate_control_R = RegInit(VecInit(Seq.fill(NumInputs)(false.B)))
  val predicate_valid_R = Seq.fill(NumInputs)(RegInit(false.B))

  val s_IDLE :: s_LATCH :: Nil = Enum(2)
  val state = RegInit(s_IDLE)

  /*===========================================*
   *            Valids                         *
   *===========================================*/

  val predicate = predicate_in_R.map(_.control).reduce(_ | _)
  val predicate_task = predicate_in_R.map(_.taskID).reduce(_ | _)

  val start = (io.predicateIn.map(_.fire()) zip predicate_valid_R) map { case (a, b) => a | b } reduce (_ & _)

  /*===============================================*
   *            Latch inputs. Wire up output       *
   *===============================================*/


  for (i <- 0 until NumInputs) {
    io.predicateIn(i).ready := ~predicate_valid_R(i)
    when(io.predicateIn(i).fire()) {
      predicate_in_R(i) <> io.predicateIn(i).bits
      predicate_control_R(i) <> io.predicateIn(i).bits.control
      predicate_valid_R(i) := true.B
    }
  }

  // Wire up Outputs
  for (i <- 0 until NumOuts) {
    io.Out(i).bits.control := predicate
    io.Out(i).bits.taskID := predicate_task
  }

  // Wire up mask output
  for (i <- 0 until NumPhi) {
    io.MaskBB(i).bits := predicate_control_R.asUInt()
  }


  /*============================================*
   *            ACTIONS (possibly dangerous)    *
   *============================================*/

  switch(state) {
    is(s_IDLE) {
      when(start) {
        ValidOut()
        state := s_LATCH
        //        assert(PopCount(predicate_control_R) < 2.U)
      }
    }
    is(s_LATCH) {
      when(IsOutReady()) {
        predicate_valid_R.foreach(_ := false.B)
        Reset()
        state := s_IDLE

        when(predicate) {
          if (log) {
            printf("[LOG] " + "[" + module_name + "] [TID->%d] [BB]   " +
              node_name + ": Output fired @ %d, Mask: %d\n", predicate_task
              , cycleCount, predicate_control_R.asUInt())
          }
        }.otherwise {
          if (log) {
            printf("[LOG] " + "[" + module_name + "] " + node_name + ": Output fired @ %d -> 0 predicate\n", cycleCount)
          }
        }
      }
    }

  }
}


/**
  * @brief BasicBlockIO class definition
  * @note Implimentation of BasickBlockIO
  * @param NumOuts Number of successor instructions
  */

class BasicBlockNoMaskDepIO(NumOuts: Int)
                           (implicit p: Parameters)
  extends HandShakingCtrlNoMaskIO(NumOuts) {
  //  val predicateIn = Vec(NumInputs, Flipped(Decoupled(new ControlBundle())))
  val predicateIn = Flipped(Decoupled(new ControlBundle()))

  override def cloneType = new BasicBlockNoMaskDepIO(NumOuts).asInstanceOf[this.type]
}


/**
  * BasicBlockNoMaskFastNode
  *
  * @param BID
  * @param NumOuts
  * @param p
  * @param name
  * @param file
  */

class BasicBlockNoMaskFastIO(val NumOuts: Int)(implicit p: Parameters)
  extends AccelBundle()(p) {
  // Output IO
  val predicateIn = Flipped(Decoupled(new ControlBundle()))
  val Out = Vec(NumOuts, Decoupled(new ControlBundle))

  override def cloneType = new BasicBlockNoMaskFastIO(NumOuts).asInstanceOf[this.type]
}


class BasicBlockNoMaskFastVecIO(val NumInputs: Int, val NumOuts: Int)(implicit p: Parameters)
  extends AccelBundle()(p) {
  // Output IO
  val predicateIn = Vec(NumInputs, Flipped(Decoupled(new ControlBundle())))
  val Out = Vec(NumOuts, Decoupled(new ControlBundle))

  override def cloneType = new BasicBlockNoMaskFastVecIO(NumInputs, NumOuts).asInstanceOf[this.type]
}

/**
  * BasicBLockNoMaskFastNode details:
  * 1) Node can one one or multiple predicates as input and their type is controlBundle
  * 2) State machine inside the node waits for all the inputs to arrive and then fire.
  * 3) The ouput value is OR of all the input values
  * 4) Node can fire outputs at the same cycle if all the inputs. Since, basic block node
  * is only for circuit simplification therefore, in case that we know output is valid
  * we don't want to waste one cycle for latching purpose. Therefore, output can be zero
  * cycle.
  *
  * @param BID
  * @param NumInputs
  * @param NumOuts
  * @param p
  * @param name
  * @param file
  */

class BasicBlockNoMaskFastNode(BID: Int, val NumInputs: Int = 1, val NumOuts: Int, val fast: Boolean = true)
                              (implicit val p: Parameters,
                               name: sourcecode.Name,
                               file: sourcecode.File)
  extends Module with HasAccelParams with UniformPrintfs {

  val io = IO(new BasicBlockNoMaskFastVecIO(NumInputs, NumOuts)(p))

  // Printf debugging
  val node_name = name.value
  val module_name = file.value.split("/").tail.last.split("\\.").head.capitalize
  val (cycleCount, _) = Counter(true.B, 32 * 1024)
  override val printfSigil = "[" + module_name + "] " + node_name + ": " + BID + " "

  // Defining IO latches

  // Data Inputs
  val in_data_R = Seq.fill(NumInputs)(RegInit(ControlBundle.default))
  val in_data_valid_R = Seq.fill(NumInputs)(RegInit(false.B))

  val output_R = RegInit(ControlBundle.default)
  val output_valid_R = Seq.fill(NumOuts)(RegInit(false.B))
  val output_fire_R = Seq.fill(NumOuts)(RegInit(false.B))

  //Make sure whenever output is fired we latch it
  for (i <- 0 until NumInputs) {
    io.predicateIn(i).ready := ~in_data_valid_R(i)
    when(io.predicateIn(i).fire()) {
      in_data_R(i) <> io.predicateIn(i).bits
      in_data_valid_R(i) := true.B
    }
  }

  val in_task_ID = (io.predicateIn zip in_data_R) map {
    case (a, b) => a.bits.taskID | b.taskID
  } reduce (_ | _)

  //Output connections
  for (i <- 0 until NumOuts) {
    when(io.Out(i).fire()) {
      output_fire_R(i) := true.B
      output_valid_R(i) := false.B
    }
  }


  val select_valid = (in_data_valid_R zip io.predicateIn.map(_.fire)) map {
    case (a, b) => a | b
  } reduce (_ & _)


  val out_fire_mask = (output_fire_R zip io.Out.map(_.fire)) map { case (a, b) => a | b }


  //Masking output value
  //  val output_value = (io.predicateIn.map(_.bits.control) zip in_data_R.map(_.control)) map {
  //    case (a, b) => a | b
  //  } reduce (_ | _)

  val predicate_val = in_data_R.map(_.control).reduce(_ | _)

  output_R := ControlBundle.default(predicate_val, in_task_ID)


  val output_valid_map = for (i <- 0 until NumInputs) yield {
    val ret = Mux(io.predicateIn(i).fire, true.B, in_data_valid_R(i))
    ret
  }

  val output_data_map = for (i <- 0 until NumInputs) yield {
    val ret = Mux(io.predicateIn(i).fire, io.predicateIn(i).bits.control, in_data_R(i).control)
    ret
  }

  //Connecting output signals
  for (i <- 0 until NumOuts) {
    if (fast) {
      io.Out(i).bits := ControlBundle.default(output_data_map.reduceLeft(_ | _), in_task_ID)
    }
    else {
      io.Out(i).bits <> output_R
    }
    io.Out(i).valid <> output_valid_R(i)
  }


  val s_idle :: s_fire :: Nil = Enum(2)
  val state = RegInit(s_idle)


  switch(state) {
    is(s_idle) {
      when(
        if (fast) {
          output_valid_map.reduceLeft(_ & _)
        }
        else {
          in_data_valid_R.reduceLeft(_ & _)
        }
      ){
        if (fast) {
          io.Out.foreach(_.valid := true.B)
        }

        (output_valid_R zip io.Out.map(_.fire)).foreach { case (a, b) => a := b ^ true.B }

        state := s_fire

        when(predicate_val) {
          if (log) {
            printf("[LOG] " + "[" + module_name + "] [TID->%d] [BB]   "
              + node_name + ": Output [T] fired @ %d\n", output_R.taskID, cycleCount)
          }
        }.otherwise {
          if (log) {
            printf("[LOG] " + "[" + module_name + "] [TID->%d] [BB]   "
              + node_name + ": Output [F] fired @ %d\n", output_R.taskID, cycleCount)
          }
        }
      }
    }
    is(s_fire) {
      //Restart the states
      when(out_fire_mask.reduce(_ & _)) {
        in_data_R foreach (_ := ControlBundle.default)
        in_data_valid_R foreach (_ := false.B)

        output_fire_R foreach (_ := false.B)

        state := s_idle
      }
    }
  }
}


class LoopHeadNodeIO(val NumOuts: Int, val NumPhi: Int)(implicit p: Parameters) extends AccelBundle {

  // Predicate enable
  val activate = Flipped(Decoupled(new ControlBundle))
  val loopBack = Flipped(Decoupled(new ControlBundle))

  // Output IO
  val Out = Vec(NumOuts, Decoupled(new ControlBundle))
  val MaskBB = Vec(NumPhi, Decoupled(UInt(2.W)))

  override def cloneType = new LoopHeadNodeIO(NumOuts, NumPhi).asInstanceOf[this.type]
}

@deprecated("Use new loop node design to capture live-in and live-outs, this is old version to handle loops, this node will be removed from next release", "dandelion-1.0")
class LoopHead(val BID: Int, val NumOuts: Int, val NumPhi: Int)
              (implicit val p: Parameters,
               name: sourcecode.Name,
               file: sourcecode.File) extends Module with HasAccelParams with UniformPrintfs {
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
  val out_R = RegInit(ControlBundle.default)
  val out_val_R = RegInit(VecInit(Seq.fill(NumOuts)(false.B)))
  val out_fired_R = RegInit(VecInit(Seq.fill(NumOuts)(false.B)))
  val out_valid_R = RegInit(VecInit(Seq.fill(NumOuts)(false.B)))

  val mask_fired_R = RegInit(VecInit(Seq.fill(NumPhi)(false.B)))
  val mask_valid_R = RegInit(VecInit(Seq.fill(NumPhi)(false.B)))
  val mask_value_R = RegInit(0.U(2.W))

  val s_START :: s_FEED :: s_END :: Nil = Enum(3)
  val state = RegInit(s_START)

  io.activate.ready := ~active_valid_R
  when(io.activate.fire()) {
    active_R <> io.activate.bits
    active_valid_R := true.B
  }

  io.loopBack.ready := true.B //~loop_back_valid_R
  when(io.loopBack.fire()) {
    loop_back_R <> io.loopBack.bits
    loop_back_valid_R := true.B
  }

  for (i <- 0 until NumOuts) {
    io.Out(i).bits := out_R
  }

  // Wire up OUT READYs and VALIDs
  for (i <- 0 until NumOuts) {
    io.Out(i).valid := out_valid_R(i)
    when(io.Out(i).fire()) {
      // Detecting when to reset
      out_fired_R(i) := true.B;
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
      mask_fired_R(i) := true.B
      // Propagating mask
      mask_valid_R(i) := false.B
    }

  }


  /*=================
   * States
   ==================*/

  switch(state) {
    is(s_START) { // First loop
      mask_value_R := 1.U
      when(active_valid_R) {
        when(active_R.control) {
          //Valid the output
          out_R := active_R
          out_valid_R := VecInit(Seq.fill(NumOuts)(true.B))
          mask_valid_R := VecInit(Seq.fill(NumPhi)(true.B))
          state := s_END
          if (log) {
            printf("[LOG] " + "[" + module_name + "] [TID->%d] " + node_name + ": Active fired @ %d, Mask: %d\n",
              active_R.taskID, cycleCount, 1.U)
          }
        }.otherwise {
          active_R := ControlBundle.default
          active_valid_R := false.B
          state := s_START
        }
      }
    }
    is(s_FEED) { // Wait for loop feedback signal.
      mask_value_R := 2.U
      when(loop_back_valid_R) {
        loop_back_valid_R := false.B
        when(loop_back_R.control) {
          out_R := loop_back_R
          out_valid_R := VecInit(Seq.fill(NumOuts)(true.B))
          mask_valid_R := VecInit(Seq.fill(NumPhi)(true.B))
          state := s_END
        }.otherwise {
          active_valid_R := false.B
          state := s_START
        }
      }
    }
    is(s_END) { // Wait until all outputs have fired
      when(out_fired_R.reduceLeft(_ && _) && mask_fired_R.reduceLeft(_ && _)) {
        mask_value_R := 2.U
        out_fired_R := VecInit(Seq.fill(NumOuts)(false.B))
        mask_fired_R := VecInit(Seq.fill(NumPhi)(false.B))
        state := s_FEED
      }
    }
  }
}

