package control

import chisel3._
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
import chisel3.Module
import chisel3.testers._
import chisel3.util._
import org.scalatest.{Matchers, FlatSpec}

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
  // LeftIO: Left input data for computation
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
                     BID: Int, Desc: String = "BasicBlock")
                    (implicit p: Parameters)
  extends HandShakingCtrlMask(NumInputs, NumOuts, NumPhi, BID)(p) {

  override lazy val io = IO(new BasicBlockIO(NumInputs, NumOuts, NumPhi))

  // Printf debugging
  override val printfSigil = Desc + BID + " "
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

  //val start = predicate_valid_R.asUInt().andR

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
      //fire_W := true.B
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
    state := s_COMPUTE
    when(predicate) {
      pred_R.control := predicate
      ValidOut()
    }
  }

  //Assertion

  //At each interation only on preds can be activated
  val pred_tem = predicate_in_R.asUInt

  assert(((pred_tem & pred_tem - 1.U) === 0.U),
    "BasicBlock can not have multiple active preds")

  /*==========================================*
   *            Output Handshaking and Reset  *
   *==========================================*/


  val out_ready_W = out_ready_R.asUInt.andR
  val out_valid_W = out_valid_R.asUInt.andR

  val mask_ready_W = mask_ready_R.asUInt.andR
  val mask_valid_W = mask_valid_R.asUInt.andR


  when(out_ready_W & mask_ready_W & (state === s_COMPUTE)) {
    //printfInfo("Start restarting output \n")
    // Reset data
    predicate_in_R := Vec(Seq.fill(NumInputs) {
      //ControlBundle.default
      false.B
    })
    predicate_valid_R := false.B
    //predicate_valid_R := Vec(Seq.fill(NumInputs) {
    //false.B
    //})

    // Reset output
    out_ready_R := Vec(Seq.fill(NumOuts) {
      false.B
    })

    //Reset state
    state := s_idle
    when(predicate) {
      printf("[LOG] " + Desc + ": Output fired @ %d\n", cycleCount)
    }

    //Restart predicate bit
    pred_R.control := false.B
  }

  //printfInfo(" State: %x\n", state)
}

/**
  * @brief BasicBlockIO class definition
  * @details Implimentation of BasickBlockIO
  * @param NumInputs Number of predecessors
  * @param NumOuts   Number of successor instructions
  */

class BasicBlockNoMaskIO(NumInputs: Int,
                         NumOuts: Int)
                        (implicit p: Parameters)
  extends HandShakingCtrlNoMaskIO(NumInputs, NumOuts) {
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
                           BID: Int, Desc: String = "BasicBlock")
                          (implicit p: Parameters)
  extends HandShakingCtrlNoMask(NumInputs, NumOuts, BID)(p) {

  override lazy val io = IO(new BasicBlockNoMaskIO(NumInputs, NumOuts))

  // Printf debugging
  override val printfSigil = Desc + BID + " "
  val (cycleCount, _) = Counter(true.B, 32 * 1024)

  /*===========================================*
   *            Registers                      *
   *===========================================*/
  // OP Inputs
  //  val predicate_in_R    = RegInit(VecInit(Seq.fill(NumInputs)(false.B)))
  //  val predicate_valid_R = RegInit(VecInit(Seq.fill(NumInputs)(false.B)))

  val predicate_in_R = RegInit(false.B)
  val predicate_valid_R = RegInit(false.B)

  val s_IDLE :: s_LATCH :: s_COMPUTE :: Nil = Enum(3)
  val state = RegInit(s_IDLE)

  /*===============================================*
   *            Latch inputs. Wire up output       *
   *===============================================*/

  io.predicateIn.ready := ~predicate_valid_R
  when(io.predicateIn.fire()) {
    predicate_in_R := io.predicateIn.bits.control
    predicate_valid_R := true.B
  }

  // Wire up Outputs
  for (i <- 0 until NumOuts) {
    io.Out(i).bits.control := predicate_in_R
    io.Out(i).bits.taskID := 0.U
  }


  /*============================================*
   *            ACTIONS (possibly dangerous)    *
   *============================================*/

  switch(state) {
    is(s_IDLE) {
      when(io.predicateIn.fire()) {
        state := s_LATCH
      }
    }
    is(s_LATCH) {
      when(predicate_valid_R) {
        ValidOut()
        state := s_COMPUTE
      }
    }
    is(s_COMPUTE) {
      when(IsOutReady()) {
        predicate_in_R := false.B
        predicate_valid_R := false.B

        state := s_IDLE

        Reset()
        when(predicate_in_R) {
          printf("[LOG] " + Desc + ": Output [T] fired @ %d\n", cycleCount)
        }.otherwise{
          printf("[LOG] " + Desc + ": Output [F] fired @ %d\n", cycleCount)
        }
      }
    }

  }

  //At each iteration only on preds can be activated
  val pred_tem = predicate_in_R.asUInt

  assert(((pred_tem & pred_tem - 1.U) === 0.U),
    "BasicBlock can not have multiple active preds")

}
