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
  val idx1 = Flipped(Decoupled(new DataBundle()))

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

class GepNodeStackIO(NumOuts: Int)
                    (implicit p: Parameters)
  extends HandShakingIONPS(NumOuts)(new DataBundle) {

  // Inputs should be fed only when Ready is HIGH
  // Inputs are always latched.
  // If Ready is LOW; Do not change the inputs as this will cause a bug
  val baseAddress = Flipped(Decoupled(new DataBundle()))

}

class GepNodeIO(NumIns: Int, NumOuts: Int)
               (implicit p: Parameters)
  extends HandShakingIONPS(NumOuts)(new DataBundle) {

  // Inputs should be fed only when Ready is HIGH
  // Inputs are always latched.
  // If Ready is LOW; Do not change the inputs as this will cause a bug
  val baseAddress = Flipped(Decoupled(new DataBundle()))
  val idx = Vec(NumIns, Flipped(Decoupled(new DataBundle())))

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
  val (cycleCount, _) = Counter(true.B, 32 * 1024)
  override val printfSigil = "[" + module_name + "] " + node_name + ": " + ID + " "

  /*===========================================*
   *            Registers                      *
   *===========================================*/
  // Addr Inputs
  val base_addr_R = RegInit(DataBundle.default)
  val base_addr_valid_R = RegInit(false.B)

  // Index 1 input
  val idx1_R = RegInit(DataBundle.default)
  val idx1_valid_R = RegInit(false.B)

  val s_IDLE :: s_COMPUTE :: Nil = Enum(2)
  val state = RegInit(s_IDLE)

  /*==========================================*
   *           Predicate Evaluation           *
   *==========================================*/

  val predicate = base_addr_R.predicate & idx1_R.predicate & IsEnable()

  /*===============================================*
   *            Latch inputs. Wire up output       *
   *===============================================*/

  io.baseAddress.ready := ~base_addr_valid_R
  when(io.baseAddress.fire()) {
    base_addr_R <> io.baseAddress.bits
    base_addr_valid_R := true.B
  }

  io.idx1.ready := ~idx1_valid_R
  when(io.idx1.fire()) {
    idx1_R <> io.idx1.bits
    idx1_valid_R := true.B
  }

  // Output
  val data_W = base_addr_R.data +
    (idx1_R.data * numByte1.U)

  // Wire up Outputs
  for (i <- 0 until NumOuts) {
    io.Out(i).bits.data := data_W
    io.Out(i).bits.predicate := predicate
    io.Out(i).bits.taskID := base_addr_R.taskID
  }


  /*============================================*
   *            STATES                          *
   *============================================*/

  switch(state) {
    is(s_IDLE) {
      when(enable_valid_R) {
        /*
                when((~enable_R.control).toBool) {
                  idx1_R := DataBundle.default
                  base_addr_R := DataBundle.default

                  idx1_valid_R := false.B
                  base_addr_valid_R := false.B

                  Reset()
                  printf("[LOG] " + "[" + module_name + "] [TID-> %d]" + node_name + ": Not predicated value -> reset\n", enable_R.taskID)
                }.elsewhen((idx1_valid_R) && (base_addr_valid_R)) {
        */
        when((idx1_valid_R) && (base_addr_valid_R)) {
          ValidOut()
          state := s_COMPUTE
        }
      }

    }
    is(s_COMPUTE) {
      when(IsOutReady()) {
        // Reset output
        idx1_R := DataBundle.default
        base_addr_R := DataBundle.default

        idx1_valid_R := false.B
        base_addr_valid_R := false.B

        // Reset state
        state := s_IDLE

        // Reset output
        Reset()
        if (log) {
          printf("[LOG] " + "[" + module_name + "] [TID->%d] " +
            node_name + ": Output fired @ %d, Value: %d\n", enable_R.taskID, cycleCount, data_W)
        }
      }
    }
  }


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
  val (cycleCount, _) = Counter(true.B, 32 * 1024)
  override val printfSigil = "[" + module_name + "] " + node_name + ": " + ID + " "

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

  val s_IDLE :: s_COMPUTE :: Nil = Enum(2)
  val state = RegInit(s_IDLE)

  /*==========================================*
   *           Predicate Evaluation           *
   *==========================================*/

  val predicate = IsEnable()

  /*===============================================*
   *            Latch inputs. Wire up output       *
   *===============================================*/

  io.baseAddress.ready := ~base_addr_valid_R
  when(io.baseAddress.fire()) {
    base_addr_R <> io.baseAddress.bits
    base_addr_valid_R := true.B
  }

  io.idx1.ready := ~idx1_valid_R
  when(io.idx1.fire()) {
    idx1_R <> io.idx1.bits
    idx1_valid_R := true.B
  }

  io.idx2.ready := ~idx2_valid_R
  when(io.idx2.fire()) {
    idx2_R <> io.idx2.bits
    idx2_valid_R := true.B
  }

  val data_W = base_addr_R.data +
    (idx1_R.data * numByte1.U) + (idx2_R.data * numByte2.U)

  // Wire up Outputs
  for (i <- 0 until NumOuts) {
    io.Out(i).bits.data := data_W
    io.Out(i).bits.predicate := predicate
    io.Out(i).bits.taskID := base_addr_R.taskID
  }


  /*============================================*
     *            STATES                          *
     *============================================*/

  switch(state) {
    is(s_IDLE) {
      when(enable_valid_R) {
        when(idx1_valid_R && idx2_valid_R && base_addr_valid_R) {
          ValidOut()
          state := s_COMPUTE
        }
      }

    }
    is(s_COMPUTE) {
      when(IsOutReady()) {
        // Reset output
        idx1_R := DataBundle.default
        idx2_R := DataBundle.default
        base_addr_R := DataBundle.default

        idx1_valid_R := false.B
        idx2_valid_R := false.B
        base_addr_valid_R := false.B

        // Reset state
        state := s_IDLE

        // Reset output
        Reset()
        printf("[LOG] " + "[" + module_name + "] [TID->%d] " + node_name + ": Output fired @ %d, Value: %d\n", enable_R.taskID, cycleCount, data_W)
      }
    }
  }
}

class GepNodeStack(NumOuts: Int, ID: Int)
                  (numByte1: Int)
                  (implicit p: Parameters,
                   name: sourcecode.Name,
                   file: sourcecode.File)
  extends HandShakingNPS(NumOuts, ID)(new DataBundle)(p) {
  override lazy val io = IO(new GepNodeStackIO(NumOuts))
  // Printf debugging
  val node_name = name.value
  val module_name = file.value.split("/").tail.last.split("\\.").head.capitalize
  val (cycleCount, _) = Counter(true.B, 32 * 1024)
  override val printfSigil = "[" + module_name + "] " + node_name + ": " + ID + " "

  /*===========================================*
   *            Registers                      *
   *===========================================*/
  // Addr Inputs
  val base_addr_R = RegInit(DataBundle.default)
  val base_addr_valid_R = RegInit(false.B)

  val s_IDLE :: s_COMPUTE :: Nil = Enum(2)
  val state = RegInit(s_IDLE)

  /*==========================================*
   *           Predicate Evaluation           *
   *==========================================*/

  val predicate = base_addr_R.predicate & IsEnable()

  /*===============================================*
   *            Latch inputs. Wire up output       *
   *===============================================*/

  io.baseAddress.ready := ~base_addr_valid_R
  when(io.baseAddress.fire()) {
    base_addr_R <> io.baseAddress.bits
    base_addr_valid_R := true.B
  }

  // Output
  val data_W = base_addr_R.data +
    (enable_R.taskID * numByte1.U)

  // Wire up Outputs
  for (i <- 0 until NumOuts) {
    io.Out(i).bits.data := data_W
    io.Out(i).bits.predicate := predicate
    io.Out(i).bits.taskID := base_addr_R.taskID | enable_R.taskID
  }

  /*============================================*
   *            STATES                          *
   *============================================*/

  switch(state) {
    is(s_IDLE) {
      when(enable_valid_R) {
        when(base_addr_valid_R) {
          ValidOut()
          state := s_COMPUTE
        }
      }

    }
    is(s_COMPUTE) {
      when(IsOutReady()) {
        // Reset output
        base_addr_valid_R := false.B

        // Reset state
        state := s_IDLE

        // Reset output
        Reset()
        printf("[LOG] " + "[" + module_name + "] [TID->%d] " + node_name + ": Output fired @ %d, Value: %d\n", enable_R.taskID, cycleCount, data_W)
      }
    }
  }


}


/**
  * GepStructNode
  * Contains list of size of the element types of the structure and the input index
  * will pick the correct offset.
  *
  * @param NumOuts Number of outputs
  * @param ID      Node id
  * @param numByte
  * @param p
  * @param name
  * @param file
  */
class GepStructOneNode(NumOuts: Int, ID: Int)
                      (numByte: List[Int])
                      (implicit p: Parameters,
                       name: sourcecode.Name,
                       file: sourcecode.File)
  extends HandShakingNPS(NumOuts, ID)(new DataBundle)(p) {
  override lazy val io = IO(new GepNodeOneIO(NumOuts))
  // Printf debugging
  val node_name = name.value
  val module_name = file.value.split("/").tail.last.split("\\.").head.capitalize
  val (cycleCount, _) = Counter(true.B, 32 * 1024)
  override val printfSigil = "[" + module_name + "] " + node_name + ": " + ID + " "

  /*===========================================*
   *            Registers                      *
   *===========================================*/
  // Addr Inputs
  val base_addr_R = RegInit(DataBundle.default)
  val base_addr_valid_R = RegInit(false.B)

  // Index 1 input
  val idx1_R = RegInit(DataBundle.default)
  val idx1_valid_R = RegInit(false.B)

  val s_IDLE :: s_COMPUTE :: Nil = Enum(2)
  val state = RegInit(s_IDLE)

  // Lookup table
  val look_up_table = VecInit(numByte.map(_.U))

  /*==========================================*
   *           Predicate Evaluation           *
   *==========================================*/

  val predicate = IsEnable()

  /*===============================================*
   *            Latch inputs. Wire up output       *
   *===============================================*/

  io.baseAddress.ready := ~base_addr_valid_R
  when(io.baseAddress.fire()) {
    base_addr_R <> io.baseAddress.bits
    base_addr_valid_R := true.B
  }

  io.idx1.ready := ~idx1_valid_R
  when(io.idx1.fire()) {
    idx1_R <> io.idx1.bits
    idx1_valid_R := true.B
  }

  val data_W = base_addr_R.data +
    (idx1_R.data * numByte.last.U)

  // Wire up Outputs
  for (i <- 0 until NumOuts) {
    io.Out(i).bits.data := data_W
    io.Out(i).bits.predicate := predicate
    io.Out(i).bits.taskID := base_addr_R.taskID
  }


  /*============================================*
   *            STATES                          *
   *============================================*/

  switch(state) {
    is(s_IDLE) {
      when(enable_valid_R) {
        when(idx1_valid_R && base_addr_valid_R) {
          ValidOut()
          state := s_COMPUTE
        }
      }

    }
    is(s_COMPUTE) {
      when(IsOutReady()) {
        // Reset output
        idx1_R := DataBundle.default
        base_addr_R := DataBundle.default

        idx1_valid_R := false.B
        base_addr_valid_R := false.B

        // Reset state
        state := s_IDLE

        // Reset output
        Reset()
        printf("[LOG] " + "[" + module_name + "] [TID->%d] " + node_name + ": Output fired @ %d, Value: %d\n", enable_R.taskID, cycleCount, data_W)
      }
    }
  }
}

/**
  * GepStructNode
  * Contains list of size of the element types of the structure and the input index
  * will pick the correct offset.
  *
  * @param NumOuts Number of outputs
  * @param ID      Node id
  * @param numByte
  * @param p
  * @param name
  * @param file
  */
class GepStructTwoNode(NumOuts: Int, ID: Int)
                      (numByte: List[Int])
                      (implicit p: Parameters,
                       name: sourcecode.Name,
                       file: sourcecode.File)
  extends HandShakingNPS(NumOuts, ID)(new DataBundle)(p) {
  override lazy val io = IO(new GepNodeTwoIO(NumOuts))
  // Printf debugging
  val node_name = name.value
  val module_name = file.value.split("/").tail.last.split("\\.").head.capitalize
  val (cycleCount, _) = Counter(true.B, 32 * 1024)
  override val printfSigil = "[" + module_name + "] " + node_name + ": " + ID + " "

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

  val s_IDLE :: s_COMPUTE :: Nil = Enum(2)
  val state = RegInit(s_IDLE)

  // Lookup table
  val look_up_table = VecInit(numByte.map(_.U))

  /*==========================================*
   *           Predicate Evaluation           *
   *==========================================*/

  val predicate = IsEnable()

  /*===============================================*
   *            Latch inputs. Wire up output       *
   *===============================================*/

  io.baseAddress.ready := ~base_addr_valid_R
  when(io.baseAddress.fire()) {
    base_addr_R <> io.baseAddress.bits
    base_addr_valid_R := true.B
  }

  io.idx1.ready := ~idx1_valid_R
  when(io.idx1.fire()) {
    idx1_R <> io.idx1.bits
    idx1_valid_R := true.B
  }

  io.idx2.ready := ~idx2_valid_R
  when(io.idx2.fire()) {
    idx2_R <> io.idx2.bits
    idx2_valid_R := true.B
  }

  val data_W = base_addr_R.data +
    (idx1_R.data * numByte.last.U) + look_up_table(idx2_R.data)

  // Wire up Outputs
  for (i <- 0 until NumOuts) {
    io.Out(i).bits.data := data_W
    io.Out(i).bits.predicate := predicate
    io.Out(i).bits.taskID := base_addr_R.taskID
  }


  /*============================================*
   *            STATES                          *
   *============================================*/

  switch(state) {
    is(s_IDLE) {
      when(enable_valid_R) {
        when(idx1_valid_R && idx2_valid_R && base_addr_valid_R) {
          ValidOut()
          state := s_COMPUTE
        }
      }

    }
    is(s_COMPUTE) {
      when(IsOutReady()) {
        // Reset output
        idx1_R := DataBundle.default
        idx2_R := DataBundle.default
        base_addr_R := DataBundle.default

        idx1_valid_R := false.B
        idx2_valid_R := false.B
        base_addr_valid_R := false.B

        // Reset state
        state := s_IDLE

        // Reset output
        Reset()
        printf("[LOG] " + "[" + module_name + "] [TID->%d] " + node_name + ": Output fired @ %d, Value: %d\n", enable_R.taskID, cycleCount, data_W)
      }
    }
  }
}

/**
  * GepArrayNode
  * Contains list of size of the element types of the structure and the input index
  * will pick the correct offset.
  *
  * @param NumOuts Number of outputs
  * @param ID      Node id
  * @param numByte
  * @param p
  * @param name
  * @param file
  */
class GepArrayOneNode(NumOuts: Int, ID: Int)
                     (numByte: Int)
                     (size: Int)
                     (implicit p: Parameters,
                      name: sourcecode.Name,
                      file: sourcecode.File)
  extends HandShakingNPS(NumOuts, ID)(new DataBundle)(p) {
  override lazy val io = IO(new GepNodeOneIO(NumOuts))
  // Printf debugging
  val node_name = name.value
  val module_name = file.value.split("/").tail.last.split("\\.").head.capitalize
  val (cycleCount, _) = Counter(true.B, 32 * 1024)
  override val printfSigil = "[" + module_name + "] " + node_name + ": " + ID + " "

  /*===========================================*
   *            Registers                      *
   *===========================================*/
  // Addr Inputs
  val base_addr_R = RegInit(DataBundle.default)
  val base_addr_valid_R = RegInit(false.B)

  // Index 1 input
  val idx1_R = RegInit(DataBundle.default)
  val idx1_valid_R = RegInit(false.B)


  val s_IDLE :: s_COMPUTE :: Nil = Enum(2)
  val state = RegInit(s_IDLE)

  /*==========================================*
   *           Predicate Evaluation           *
   *==========================================*/

  val predicate = IsEnable()

  /*===============================================*
   *            Latch inputs. Wire up output       *
   *===============================================*/

  io.baseAddress.ready := ~base_addr_valid_R
  when(io.baseAddress.fire()) {
    base_addr_R <> io.baseAddress.bits
    base_addr_valid_R := true.B
  }

  io.idx1.ready := ~idx1_valid_R
  when(io.idx1.fire()) {
    idx1_R <> io.idx1.bits
    idx1_valid_R := true.B
  }

  val data_W = base_addr_R.data +
    (idx1_R.data * (numByte * size).U)

  // Wire up Outputs
  for (i <- 0 until NumOuts) {
    io.Out(i).bits.data := data_W
    io.Out(i).bits.predicate := predicate
    io.Out(i).bits.taskID := base_addr_R.taskID
  }


  /*============================================*
   *            STATES                          *
   *============================================*/

  switch(state) {
    is(s_IDLE) {
      when(enable_valid_R) {
        when(idx1_valid_R && base_addr_valid_R) {
          ValidOut()
          state := s_COMPUTE
        }
      }

    }
    is(s_COMPUTE) {
      when(IsOutReady()) {
        // Reset output
        idx1_R := DataBundle.default
        base_addr_R := DataBundle.default

        idx1_valid_R := false.B
        base_addr_valid_R := false.B

        // Reset state
        state := s_IDLE

        // Reset output
        Reset()
        printf("[LOG] " + "[" + module_name + "] [TID->%d] " + node_name + ": Output fired @ %d, Value: %d\n", enable_R.taskID, cycleCount, data_W)
      }
    }
  }
}

/**
  * GepArrayTwoNode
  * Contains list of size of the element types of the structure and the input index
  * will pick the correct offset.
  *
  * @param NumOuts Number of outputs
  * @param ID      Node id
  * @param numByte
  * @param p
  * @param name
  * @param file
  */
class GepArrayTwoNode(NumOuts: Int, ID: Int)
                     (numByte: Int)
                     (size: Int)
                     (implicit p: Parameters,
                      name: sourcecode.Name,
                      file: sourcecode.File)
  extends HandShakingNPS(NumOuts, ID)(new DataBundle)(p) {
  override lazy val io = IO(new GepNodeTwoIO(NumOuts))
  // Printf debugging
  val node_name = name.value
  val module_name = file.value.split("/").tail.last.split("\\.").head.capitalize
  val (cycleCount, _) = Counter(true.B, 32 * 1024)
  override val printfSigil = "[" + module_name + "] " + node_name + ": " + ID + " "

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

  val s_IDLE :: s_COMPUTE :: Nil = Enum(2)
  val state = RegInit(s_IDLE)

  // Lookup table
  //  val look_up_table = VecInit(numByte.map(_.U))

  /*==========================================*
   *           Predicate Evaluation           *
   *==========================================*/

  val predicate = IsEnable()

  /*===============================================*
   *            Latch inputs. Wire up output       *
   *===============================================*/

  io.baseAddress.ready := ~base_addr_valid_R
  when(io.baseAddress.fire()) {
    base_addr_R <> io.baseAddress.bits
    base_addr_valid_R := true.B
  }

  io.idx1.ready := ~idx1_valid_R
  when(io.idx1.fire()) {
    idx1_R <> io.idx1.bits
    idx1_valid_R := true.B
  }

  io.idx2.ready := ~idx2_valid_R
  when(io.idx2.fire()) {
    idx2_R <> io.idx2.bits
    idx2_valid_R := true.B
  }

  val data_W = base_addr_R.data +
    (idx1_R.data * (numByte * size).U) + (idx2_R.data * numByte.U)

  // Wire up Outputs
  for (i <- 0 until NumOuts) {
    io.Out(i).bits.data := data_W
    io.Out(i).bits.predicate := predicate
    io.Out(i).bits.taskID := base_addr_R.taskID
  }


  /*============================================*
   *            STATES                          *
   *============================================*/

  switch(state) {
    is(s_IDLE) {
      when(enable_valid_R) {
        when(idx1_valid_R && idx2_valid_R && base_addr_valid_R) {
          ValidOut()
          state := s_COMPUTE
        }
      }

    }
    is(s_COMPUTE) {
      when(IsOutReady()) {
        // Reset output
        idx1_R := DataBundle.default
        idx2_R := DataBundle.default
        base_addr_R := DataBundle.default

        idx1_valid_R := false.B
        idx2_valid_R := false.B
        base_addr_valid_R := false.B

        // Reset state
        state := s_IDLE

        // Reset output
        Reset()
        printf("[LOG] " + "[" + module_name + "] [TID->%d] " + node_name + ": Output fired @ %d, Value: %d\n", enable_R.taskID, cycleCount, data_W)
      }
    }
  }
}


/**
  * GetElementPtrNodeIO
  * GEP node calculates memory addresses for Load and Store instructions
  * Features:
  * It has a list of sizes -> compile
  * It has a list of indexes -> runtime
  */

class GepIO(NumIns: Int, NumOuts: Int)
           (implicit p: Parameters)
  extends CoreBundle {

  // Input indexes
  // Indexes can be either constant our coming from other nodes
  val BaseAddress = Flipped(Decoupled(new DataBundle))

  val InIndex = Vec(NumIns, Flipped((Decoupled(new DataBundle))))

  val enable = Flipped(Decoupled(new ControlBundle))

  val Out = Vec(NumOuts, Decoupled(new DataBundle))
}



class GepNode(NumIns: Int, NumOuts: Int, ID: Int)
             (ElementSize: Int, ArraySize: List[Int])
             (implicit p: Parameters,
              name: sourcecode.Name,
              file: sourcecode.File)
  extends HandShakingNPS(NumOuts, ID)(new DataBundle)(p) {
  override lazy val io = IO(new GepNodeIO(NumIns, NumOuts))
  // Printf debugging
  val node_name = name.value
  val module_name = file.value.split("/").tail.last.split("\\.").head.capitalize
  val (cycleCount, _) = Counter(true.B, 32 * 1024)
  override val printfSigil = "[" + module_name + "] " + node_name + ": " + ID + " "

  /*===========================================*
   *            Registers                      *
   *===========================================*/
  // Addr Inputs
  val base_addr_R = RegInit(DataBundle.default)
  val base_addr_valid_R = RegInit(false.B)

  // Index 1 input
  val idx_R = Seq.fill(NumIns)(RegInit(DataBundle.default))
  val idx_valid_R = Seq.fill(NumIns)(RegInit(false.B))


  val s_IDLE :: s_COMPUTE :: Nil = Enum(2)
  val state = RegInit(s_IDLE)

  //We support only geps with 1 or 2 inputs
  assert(NumIns <= 2)

  /*==========================================*
   *           Predicate Evaluation           *
   *==========================================*/

  val predicate = IsEnable()

  /*===============================================*
   *            Latch inputs. Wire up output       *
   *===============================================*/

  io.baseAddress.ready := ~base_addr_valid_R
  when(io.baseAddress.fire()) {
    base_addr_R <> io.baseAddress.bits
    base_addr_valid_R := true.B
  }

  for (i <- 0 until NumIns) {
    io.idx(i).ready := ~idx_valid_R(i)
    when(io.idx(i).fire()) {
      idx_R(i) <> io.idx(i).bits
      idx_valid_R(i) := true.B
    }
  }

  // Calculating the address
  val seek_value =
    if (ArraySize.isEmpty) {
      0.U
    }
    else if (ArraySize.length == 1) {
      idx_R(1).data * ArraySize(0).U
    }
    else {
      val table = VecInit(ArraySize.map(_.U))
      table(idx_R(1).data)
    }

  val data_out = base_addr_R.data +
    (idx_R(0).data * ElementSize.U) + seek_value

  //  val data_out = base_addr_R.data +
  //    (idx_R.map {
  //      _.data
  //    } zip ArraySize).map {
  //      case (a, b) =>
  //        if (b.length == 1) {
  //          a * b(0).U
  //        }
  //        else {
  //          val table = VecInit(b.map(_.U))
  //          table(a)
  //        }
  //    }.reduce(_ + _)


  // Wire up Outputs
  for (i <- 0 until NumOuts) {
    io.Out(i).bits.data := data_out
    io.Out(i).bits.predicate := predicate
    io.Out(i).bits.taskID := base_addr_R.taskID
  }


  /*============================================*
   *            STATES                          *
   *============================================*/

  switch(state) {
    is(s_IDLE) {
      when(enable_valid_R) {
        when(enable_R.control) {
          when(idx_valid_R.reduce(_ & _) && base_addr_valid_R) {
            ValidOut()
            state := s_COMPUTE
          }
        }.otherwise {
          ValidOut()
          state := s_COMPUTE
        }
      }

    }
    is(s_COMPUTE) {
      when(IsOutReady()) {
        // Reset output
        idx_R.foreach(_ := DataBundle.default)
        base_addr_R := DataBundle.default

        idx_valid_R.foreach(_ := false.B)
        base_addr_valid_R := false.B

        // Reset state
        state := s_IDLE

        // Reset output
        Reset()
        if (log) {
          printf("[LOG] " + "[" + module_name + "] [TID->%d] " +
            node_name + ": Output fired @ %d, Value: %d\n", enable_R.taskID, cycleCount, data_out)
        }
      }
    }
  }
}



/**
  * GetElementPtrNode
  * GEP node calculates memory addresses for Load and Store instructions
  * Features:
  * It has a list of sizes -> compile
  * It has a list of indexes -> runtime
  *
  * @ArraySize: Is a list of list containig integer values for each element of a datastructure.
  *             The first element of ArraySize is always the overall size of the type.
  */

class GepFastNode(NumIns: Int, NumOuts: Int, ArraySize: List[List[Int]], ID: Int)
                 (implicit val p: Parameters,
                  name: sourcecode.Name,
                  file: sourcecode.File)
  extends Module with CoreParams with UniformPrintfs {

  lazy val io = IO(new GepIO(NumIns, NumOuts))

  // Printf debugging
  val node_name = name.value
  val module_name = file.value.split("/").tail.last.split("\\.").head.capitalize

  override val printfSigil = "[" + module_name + "] " + node_name + ": " + ID + " "
  val (cycleCount, _) = Counter(true.B, 32 * 1024)


  // Latching inputs
  val badd_data_R = RegInit(DataBundle.default)
  val badd_valid_R = RegInit(false.B)

  val inputidx_data_R = Seq.fill(NumIns)(RegInit(DataBundle.default))
  val inputidx_valid_R = Seq.fill(NumIns)(RegInit(false.B))

  val enable_control_R = RegInit(ControlBundle.default)
  val enable_valid_R = RegInit(false.B)

  val output_R = RegInit(DataBundle.default)
  val output_valid_R = Seq.fill(NumOuts)(RegInit(false.B))

  val fire_R = Seq.fill(NumOuts)(RegInit(false.B))
  val task_input = (io.enable.bits.taskID | enable_control_R.taskID)


  // Predicating inputs

  val badd_data_predicate = (io.BaseAddress.bits.data & Fill(xlen, io.BaseAddress.valid)) | (badd_data_R.data & Fill(xlen, badd_valid_R))
  val badd_predicate = (io.BaseAddress.bits.predicate & Fill(xlen, io.BaseAddress.valid)) | (badd_data_R.predicate & Fill(xlen, badd_valid_R))

  val input_data_predicated = for (i <- 0 until NumIns) yield {
    val input_pred = (io.InIndex(i).bits.data & Fill(xlen, io.InIndex(i).valid)) |
      (inputidx_data_R(i).data & Fill(xlen, inputidx_valid_R(i)))
    input_pred
  }

  val input_pred_predicated = for (i <- 0 until NumIns) yield {
    val input_pred = (io.InIndex(i).bits.predicate & Fill(xlen, io.InIndex(i).valid)) |
      (inputidx_data_R(i).predicate & Fill(xlen, inputidx_valid_R(i)))
    input_pred
  }


  // Calculating the address
  val data_out = badd_data_predicate +
    (input_data_predicated zip ArraySize).map {
      case (a, b) =>
        if (b.length == 1) {
          a * b(0).U
        }
        else {
          val table = VecInit(b.map(_.U))
          table(a)
        }
    }.reduce(_ + _)

  // Handshaking protocol
  io.BaseAddress.ready := ~badd_valid_R
  when(io.BaseAddress.fire() && io.BaseAddress.bits.predicate) {
    badd_data_R <> io.BaseAddress.bits
    badd_valid_R := true.B
  }


  for (i <- 0 until NumIns) {
    io.InIndex(i).ready := ~inputidx_valid_R(i)
    when(io.InIndex(i).fire() && io.InIndex(i).bits.predicate) {
      inputidx_data_R(i) <> io.InIndex(i).bits
      inputidx_valid_R(i) := true.B
    }
  }

  io.enable.ready := ~enable_valid_R
  when(io.enable.fire()) {
    enable_control_R <> io.enable.bits
    enable_valid_R := true.B
  }


  // Defalut values for output
  val predicate = enable_control_R.control & input_pred_predicated.reduce(_ & _)

  output_R.data := data_out
  output_R.predicate := predicate
  output_R.taskID := task_input

  for (i <- 0 until NumOuts) {
    io.Out(i).bits <> output_R
    io.Out(i).valid <> output_valid_R(i)
  }

  for (i <- 0 until NumOuts) {
    when(io.Out(i).fire) {
      fire_R(i) := true.B
    }
  }

  val fire_mask = (fire_R zip io.Out.map(_.fire)).map { case (a, b) => a | b }


  // State Machine
  val s_idle :: s_fire :: Nil = Enum(2)
  val state = RegInit(s_idle)

  switch(state) {
    is(s_idle) {

      val input_fire = (inputidx_valid_R zip io.InIndex.map(_.fire)).map { case (a, b) => a || b }

      when((enable_valid_R || io.enable.fire) && input_fire.reduce(_ || _)) {

        output_valid_R.foreach(_ := true.B)

        state := s_fire

        if (log) {
          printf("[LOG] " + "[" + module_name + "] " + "[TID->%d] "
            + node_name + ": Output fired @ %d, Value: %d \n",
            task_input, cycleCount, data_out.asUInt())
        }
      }
    }

    is(s_fire) {

      when(fire_mask.reduce(_ & _)) {

        badd_data_R := DataBundle.default
        badd_valid_R := false.B

        inputidx_data_R.foreach(_ := DataBundle.default)
        inputidx_valid_R.foreach(_ := false.B)

        enable_control_R := ControlBundle.default
        enable_valid_R := false.B

        output_R := DataBundle.default
        output_valid_R.foreach(_ := false.B)

        fire_R.foreach(_ := false.B)

        state := s_idle
      }
    }
  }
}















