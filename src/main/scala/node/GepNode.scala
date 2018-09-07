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
  * GetElementPtrNode
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
  val In = Vec(NumIns, Flipped((Decoupled(new DataBundle))))

  val enable = Flipped(Decoupled(new ControlBundle))

  val Out = Vec(NumOuts, Decoupled(new DataBundle))
}

class GepNode(NumIns: Int, NumOuts: Int, AraySize: Seq[Int], ID: Int)
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
  val input_data = Seq.fill(NumIns)(RegInit(DataBundle.default))
  val input_valid = Seq.fill(NumIns)(RegInit(false.B))

  val enable_control = RegInit(ControlBundle.default)
  val enable_valid = RegInit(false.B)

  val output_R = RegInit(DataBundle.default)
  val output_valid_R = Seq.fill(NumOuts)(RegInit(false.B))

  val fire_R = Seq.fill(NumOuts)(RegInit(false.B))
  val task_input = (io.enable.bits.taskID | enable_control.taskID)


  // Predicating inputs
  val input_data_predicated = for (i <- 0 until NumIns) yield {
    val input_pred = (io.In(i).bits.data & Fill(xlen, io.In(i).valid)) |
      (input_data(i).data & Fill(xlen, input_valid(i)))
    input_pred
  }

  val input_pred_predicated = for (i <- 0 until NumIns) yield {
    val input_pred = (io.In(i).bits.predicate & Fill(xlen, io.In(i).valid)) |
      (input_data(i).predicate & Fill(xlen, input_valid(i)))
    input_pred
  }


  // Computing the address
  val data_out = (input_data_predicated zip AraySize).map { case (a, b) => a * b.U }.reduce(_ + _)

  // Handshaking protocol
  for (i <- 0 until NumIns) {
    io.In(i).ready := ~input_valid(i)
    when(io.In(i).fire() && io.In(i).bits.predicate) {
      input_data_predicated(i) <> io.In(i).bits
      input_valid(i) := true.B
    }

  }

  io.enable.ready := ~enable_valid
  when(io.enable.fire()) {
    enable_control <> io.enable.bits
    enable_valid := true.B
  }


  // Defalut values for output
  val predicate = enable_control.control & input_pred_predicated.reduce(_ & _)

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

      val input_fire = (input_valid zip io.In.map(_.fire)).map { case (a, b) => a || b }

      when((enable_valid || io.enable.fire) && input_fire.reduce(_ || _)) {

        output_valid_R.foreach(_ := true.B)

        state := s_fire

        printf("[LOG] " + "[" + module_name + "] " + "[TID->%d] "
          + node_name + ": Output fired @ %d, Value: %d \n",
          task_input, cycleCount, data_out.asUInt())
      }
    }

    is(s_fire) {

      when(fire_mask.reduce(_ & _)) {

        input_data.foreach(_ := DataBundle.default)
        input_valid.foreach(_ := false.B)

        enable_control := ControlBundle.default
        enable_valid := false.B

        output_R := DataBundle.default
        output_valid_R.foreach(_ := false.B)

        fire_R.foreach(_ := false.B)

        state := s_idle
      }
    }
  }


  //  printf("[LOG] " + "[" + module_name + "] " + "[TID->%d] "
  //    + node_name + ": Output fired @ %d, Value: %d (%d + %d)\n",
  //    task_input, cycleCount, FU.io.out.asUInt(), left_input.asUInt(), right_input.asUInt())
}






















