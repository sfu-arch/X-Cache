package node

import chisel3._
import chisel3.iotesters.{ChiselFlatSpec, Driver, OrderedDecoupledHWIOTester, PeekPokeTester}
import chisel3.Module
import chisel3.testers._
import chisel3.util._
import org.scalatest.{FlatSpec, Matchers}
import config._
import interfaces._
import muxes._
import util._
import utility.UniformPrintfs


class SelectNodeIO(NumOuts: Int)
                  (implicit p: Parameters)
  extends HandShakingIONPS(NumOuts)(new DataBundle) {

  // Input data 1
  val InData1 = Flipped(Decoupled(new DataBundle()))

  // Input data 2
  val InData2 = Flipped(Decoupled(new DataBundle()))

  // Select input data
  val Select = Flipped(Decoupled(new DataBundle()))
}

class SelectNode(NumOuts: Int, ID: Int)
                (implicit p: Parameters,
                 name: sourcecode.Name,
                 file: sourcecode.File)
  extends HandShakingNPS(NumOuts, ID)(new DataBundle())(p) {
  override lazy val io = IO(new SelectNodeIO(NumOuts))

  // Printf debugging
  val node_name = name.value
  val module_name = file.value.split("/").tail.last.split("\\.").head.capitalize

  override val printfSigil = "[" + module_name + "] " + node_name + ": " + ID + " "
  //  override val printfSigil = "Node (COMP - " + opCode + ") ID: " + ID + " "
  val (cycleCount, _) = Counter(true.B, 32 * 1024)

  /*===========================================*
   *            Registers                      *
   *===========================================*/
  // Indata1 Input
  val indata1_R = RegInit(DataBundle.default)
  val indata1_valid_R = RegInit(false.B)

  // Indata2 Input
  val indata2_R = RegInit(DataBundle.default)
  val indata2_valid_R = RegInit(false.B)

  // Select Input
  val select_R = RegInit(DataBundle.default)
  val select_valid_R = RegInit(false.B)

  val task_ID_R = RegNext(next = enable_R.taskID)

  //Output register
  val out_data_R = RegInit(DataBundle.default)

  val s_IDLE :: s_COMPUTE :: Nil = Enum(2)
  val state = RegInit(s_IDLE)


  //  val predicate = indata1_R.predicate & indata2_R.predicate
  val predicate = select_R.predicate

  /*===============================================*
   *            Latch inputs. Wire up output       *
   *===============================================*/
  val output_data = Mux(select_R.data(0).toBool(), indata1_R, indata2_R)

  io.InData1.ready := ~indata1_valid_R
  when(io.InData1.fire()) {
    indata1_R <> io.InData1.bits
    indata1_valid_R := true.B
  }

  io.InData2.ready := ~indata2_valid_R
  when(io.InData2.fire()) {
    indata2_R <> io.InData2.bits
    indata2_valid_R := true.B
  }

  io.Select.ready := ~select_valid_R
  when(io.Select.fire()) {
    select_R <> io.Select.bits
    select_valid_R := true.B
  }


  // Wire up Outputs
  for (i <- 0 until NumOuts) {
    //io.Out(i).bits.data := FU.io.out
    //io.Out(i).bits.predicate := predicate
    // The taskID's should be identical except in the case
    // when one input is tied to a constant.  In that case
    // the taskID will be zero.  Logical OR'ing the IDs
    // Should produce a valid ID in either case regardless of
    // which input is constant.
    //io.Out(i).bits.taskID := left_R.taskID | right_R.taskID
    io.Out(i).bits := out_data_R
  }

  /*============================================*
   *            State Machine                   *
   *============================================*/
  switch(state) {
    is(s_IDLE) {
      when(enable_valid_R) {
        when(indata1_valid_R && indata2_valid_R & select_valid_R) {
          ValidOut()
          when(enable_R.control) {
            out_data_R.data := output_data.data
            out_data_R.predicate := predicate
            out_data_R.taskID := indata1_R.taskID | indata2_R.taskID | enable_R.taskID
          }
          state := s_COMPUTE
        }
      }
    }
    is(s_COMPUTE) {
      when(IsOutReady()) {
        // Reset data
        //left_R := DataBundle.default
        //right_R := DataBundle.default
        indata1_valid_R := false.B
        indata2_valid_R := false.B
        select_valid_R := false.B
        //Reset state
        state := s_IDLE
        //Reset output
        out_data_R.predicate := false.B
        Reset()
        printf("[LOG] " + "[" + module_name + "] " + "[TID->%d] " + node_name + ": Output fired @ %d, Value: %d\n", task_ID_R, cycleCount, output_data.data)
      }
    }
  }

}


class SelectFastNode(NumOuts: Int, ID: Int)
                    (implicit val p: Parameters,
                     name: sourcecode.Name,
                     file: sourcecode.File)
  extends Module with CoreParams with UniformPrintfs {

  val io = IO(new Bundle {
    // Input data 1
    val InData1 = Flipped(Decoupled(new DataBundle))

    // Input data 2
    val InData2 = Flipped(Decoupled(new DataBundle))

    // Select input data
    val Select = Flipped(Decoupled(new CustomDataBundle(Bool())))

    // Enable signal
    val enable = Flipped(Decoupled(new ControlBundle))

    val Out = Decoupled(new DataBundle)

  })

  // Printf debugging
  val node_name = name.value
  val module_name = file.value.split("/").tail.last.split("\\.").head.capitalize

  override val printfSigil = "[" + module_name + "] " + node_name + ": " + ID + " "
  //  override val printfSigil = "Node (COMP - " + opCode + ") ID: " + ID + " "
  val (cycleCount, _) = Counter(true.B, 32 * 1024)

  /*===========================================*
   *            Registers                      *
   *===========================================*/
  // Indata1 Input
  val indata1_R = RegInit(DataBundle.default)
  val indata1_valid_R = RegInit(false.B)

  // Indata2 Input
  val indata2_R = RegInit(DataBundle.default)
  val indata2_valid_R = RegInit(false.B)

  // Select Input
  val select_R = RegInit(CustomDataBundle.default(Bool()))
  val select_valid_R = RegInit(false.B)

  val enable_R = RegInit(ControlBundle.default)
  val enable_valid_R = RegInit(false.B)


  /*===============================================*
   *            Latch inputs. Wire up output       *
   *===============================================*/

  io.InData1.ready := ~indata1_valid_R
  when(io.InData1.fire()) {
    indata1_R <> io.InData1.bits
    indata1_valid_R := true.B
  }

  io.InData2.ready := ~indata2_valid_R
  when(io.InData2.fire()) {
    indata2_R <> io.InData2.bits
    indata2_valid_R := true.B
  }

  io.Select.ready := ~select_valid_R
  when(io.Select.fire()) {
    select_R <> io.Select.bits
    select_valid_R := true.B
  }

  io.enable.ready := ~enable_valid_R
  when(io.enable.fire()) {
    enable_R <> io.enable.bits
    enable_valid_R := true.B
  }

  val in1_input = (io.InData1.bits.data & Fill(xlen, io.InData1.valid)) | (indata1_R.data & Fill(xlen, indata1_valid_R))
  val in2_input = (io.InData2.bits.data & Fill(xlen, io.InData2.valid)) | (indata2_R.data & Fill(xlen, indata2_valid_R))

  val select_input = (io.Select.bits.data.asTypeOf(Bool()) & io.Select.valid) | (select_R.data & select_valid_R)

  val enable_input = (io.enable.bits.control & Fill(xlen, io.enable.valid)) | (enable_R.control & Fill(xlen, enable_valid_R))

  val output_data = Mux(select_input, in1_input, in2_input)
  val predicate = enable_input

  val task_ID_R = RegNext(next = enable_R.taskID)

  io.Out.valid := false.B
  io.Out.bits.taskID := 0.U
  io.Out.bits.data := output_data
  io.Out.bits.predicate := predicate

  /*============================================*
   *            State Machine                   *
   *============================================*/
  val s_idle :: s_fire :: Nil = Enum(2)
  val state = RegInit(s_idle)

  switch(state) {
    is(s_idle) {
      when(enable_valid_R || io.enable.fire) {
        when((indata1_valid_R || io.InData1.fire) &&
          (indata2_valid_R || io.InData2.fire) &&
          (select_valid_R || io.Select.fire)) {

          io.Out.valid := true.B

          when(io.Out.fire) {
            indata1_R := DataBundle.default
            indata1_valid_R := false.B

            indata2_R := DataBundle.default
            indata2_valid_R := false.B

            select_R := DataBundle.default
            select_valid_R := false.B

            enable_R := ControlBundle.default
            enable_valid_R := false.B

            state := s_idle

          }.otherwise {
            state := s_fire
          }

          printf("[LOG] " + "[" + module_name + "] " + "[TID->%d] "
            + node_name + ": Output fired @ %d, Value: %d : %d ? %d (S:%d)\n",
            task_ID_R, cycleCount, output_data, in1_input, in2_input, select_input)

        }
      }
    }
    is(s_fire) {
      io.Out.valid := true.B
      when(io.Out.fire) {
        indata1_R := DataBundle.default
        indata1_valid_R := false.B

        indata2_R := DataBundle.default
        indata2_valid_R := false.B

        select_R := DataBundle.default
        select_valid_R := false.B

        enable_R := ControlBundle.default
        enable_valid_R := false.B

        state := s_idle


      }
    }
  }

}
