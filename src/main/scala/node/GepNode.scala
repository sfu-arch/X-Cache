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
        printf("[LOG] " + "[" + module_name + "] [TID->%d] " + node_name + ": Output fired @ %d, Value: %d\n", enable_R.taskID, cycleCount, data_W)
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
        /*
                when((~enable_R.control).toBool) {
                  idx1_R := DataBundle.default
                  idx2_R := DataBundle.default
                  base_addr_R := DataBundle.default

                  idx1_valid_R := false.B
                  idx2_valid_R := false.B
                  base_addr_valid_R := false.B

                  Reset()
                  printf("[LOG] " + "[" + module_name + "] [TID->%d] " + node_name + ": Not predicated value -> reset\n", enable_R.taskID)
                }.elsewhen(idx1_valid_R &&
        */
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
    (idx1_R.data * (numByte*size).U) + (idx2_R.data * numByte.U)

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


