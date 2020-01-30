package dandelion.node

import chisel3._
import chisel3.iotesters.{ChiselFlatSpec, Driver, OrderedDecoupledHWIOTester, PeekPokeTester}
import chisel3.Module
import chisel3.testers._
import chisel3.util._
import org.scalatest.{FlatSpec, Matchers}
import chipsalliance.rocketchip.config._
import dandelion.config._
import dandelion.interfaces._
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

  override def cloneType = new SelectNodeIO(NumOuts).asInstanceOf[this.type]
}

class SelectNode(NumOuts: Int, ID: Int)
                (fast: Boolean)
                (implicit p: Parameters,
                 name: sourcecode.Name,
                 file: sourcecode.File)
  extends HandShakingNPS(NumOuts, ID)(new DataBundle())(p) {
  override lazy val io = IO(new SelectNodeIO(NumOuts))

  // Printf debugging
  val node_name = name.value
  val module_name = file.value.split("/").tail.last.split("\\.").head.capitalize

  override val printfSigil = "[" + module_name + "] " + node_name + ": " + ID + " "
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

  val s_IDLE :: s_COMPUTE :: Nil = Enum(2)
  val state = RegInit(s_IDLE)


  val predicate = enable_R.control | io.enable.bits.control
  val taskID = Mux(enable_valid_R, enable_R.taskID ,io.enable.bits.taskID)

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

  // Wire up Outputs
  val output_data = Mux(select_R.data.orR, indata1_R.data, indata2_R.data)

    // The taskID's should be identical except in the case
    // when one input is tied to a constant.  In that case
    // the taskID will be zero.  Logical OR'ing the IDs
    // Should produce a valid ID in either case regardless of
    // which input is constant.
    io.Out.foreach(_.bits := DataBundle(output_data, taskID, predicate))

  /*============================================*
   *            State Machine                   *
   *============================================*/
  switch(state) {
    is(s_IDLE) {
        when(enable_valid_R && indata1_valid_R && indata2_valid_R && select_valid_R) {
          io.Out.foreach( _.valid := true.B)
          ValidOut()
          state := s_COMPUTE
          if(log){
            printf("[LOG] " + "[" + module_name + "] " + "[TID->%d] [SELECT] " +
              node_name + ": Output fired @ %d, Value: %d\n", taskID, cycleCount, output_data)
          }
        }
    }
    is(s_COMPUTE) {
      when(IsOutReady()) {
        // Reset data
        indata1_valid_R := false.B
        indata2_valid_R := false.B
        select_valid_R := false.B

        indata1_R := DataBundle.default
        indata2_R := DataBundle.default
        //Reset state
        state := s_IDLE
        //Reset output
        Reset()
      }
    }
  }

}
