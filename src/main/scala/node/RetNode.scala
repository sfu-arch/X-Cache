package dandelion.node

import chisel3._
import chisel3.MultiIOModule
import chisel3.Module
import utility.UniformPrintfs
import dandelion.config._
import dandelion.interfaces.{VariableDecoupledData, _}
import util._

class RetNodeIO(val retTypes: Seq[Int])(implicit p: Parameters)
  extends Bundle {
  val enable = Flipped(Decoupled(new ControlBundle()))
  val In = Flipped(new VariableDecoupledData(retTypes)) // Data to be returned
  val Out = Decoupled(new Call(retTypes)) // Returns to calling block(s)
}

class RetNode(retTypes: Seq[Int], ID: Int)
             (implicit val p: Parameters,
              name: sourcecode.Name,
              file: sourcecode.File) extends Module
  with CoreParams with UniformPrintfs {

  val node_name = name.value
  val module_name = file.value.split("/").tail.last.split("\\.").head.capitalize

  override lazy val io = IO(new RetNodeIO(retTypes)(p))
  override val printfSigil = module_name + ": " + node_name + ID + " "


  val (cycleCount, _) = Counter(true.B, 32 * 1024)

  // Defining states
  val s_IDLE :: s_COMPUTE :: Nil = Enum(2)
  val state = RegInit(s_IDLE)

  // Enable signals
  //  val enable_R = RegInit(ControlBundle.default)
  val enable_valid_R = RegInit(false.B)

  // Data Inputs
  val in_data_valid_R = RegInit(VecInit(Seq.fill(retTypes.length)(false.B)))

  // Output registers
  val output_R = RegInit(0.U.asTypeOf(io.Out.bits))
  val out_ready_R = RegInit(false.B)
  val out_valid_R = RegInit(false.B)


  // Latching enable signal
  io.enable.ready := ~enable_valid_R
  when(io.enable.fire()) {
    enable_valid_R := io.enable.valid
    //    enable_R := io.enable.bits
    output_R.enable := io.enable.bits
  }

  // Latching input data
  for (i <- retTypes.indices) {
    io.In.elements(s"field$i").ready := ~in_data_valid_R(i)
    when(io.In.elements(s"field$i").fire()) {
      output_R.data(s"field$i") := io.In.elements(s"field$i").bits
      in_data_valid_R(i) := true.B
    }
  }

  // Connecting outputs
  io.Out.bits := output_R
  io.Out.valid := out_valid_R

  when(io.Out.fire()) {
    out_ready_R := io.Out.ready
    out_valid_R := false.B
  }

  switch(state) {
    is(s_IDLE) {
      when(enable_valid_R) {
        when(in_data_valid_R.reduceLeft(_ && _)) {
          out_valid_R := true.B
          state := s_COMPUTE
        }
      }
    }
    is(s_COMPUTE) {
      when(out_ready_R) {
        for (i <- retTypes.indices) {
          in_data_valid_R(i) := false.B
        }

        out_valid_R := false.B
        enable_valid_R := false.B
        out_ready_R := false.B

        state := s_IDLE
        if (log) {
          printf("[LOG] " + "[" + module_name + "] " + "[TID->%d] "
            + node_name + ": Output fired @ %d, Value: %d\n",
            output_R.enable.taskID, cycleCount, output_R.data(s"field0").data)
        }
      }
    }
  }


}

class RetNode2IO(val retTypes: Seq[Int])(implicit p: Parameters)
  extends Bundle {
  val In = Flipped(new CallDecoupled(retTypes))
  val Out = Decoupled(new Call(retTypes)) // Returns to calling block(s)
}

class RetNode2(retTypes: Seq[Int], ID: Int)
              (implicit val p: Parameters,
               name: sourcecode.Name,
               file: sourcecode.File) extends Module
  with CoreParams with UniformPrintfs {

  val node_name = name.value
  val module_name = file.value.split("/").tail.last.split("\\.").head.capitalize

  override lazy val io = IO(new RetNode2IO(retTypes)(p))
  override val printfSigil = module_name + ": " + node_name + ID + " "


  val (cycleCount, _) = Counter(true.B, 32 * 1024)

  // Defining states
  val s_IDLE :: s_COMPUTE :: Nil = Enum(2)
  val state = RegInit(s_IDLE)

  // Enable signals
  val enable_valid_R = RegInit(false.B)

  // Data Inputs
  val in_data_valid_R = Seq.fill(retTypes.length)(RegInit(false.B))

  // Output registers
  val output_R = RegInit(0.U.asTypeOf(io.Out.bits))
  val out_ready_R = RegInit(false.B)
  val out_valid_R = RegInit(false.B)

  def IsInValid(): Bool = {
    if (retTypes.length == 0) {
      return true.B
    } else {
      in_data_valid_R.reduceLeft(_ && _)
    }
  }

  // Latching enable signal
  io.In.enable.ready := ~enable_valid_R
  when(io.In.enable.fire()) {
    enable_valid_R := io.In.enable.valid
    output_R.enable := io.In.enable.bits
  }

  // Latching input data
  for (i <- retTypes.indices) {
    io.In.data(s"field$i").ready := ~in_data_valid_R(i)
    when(io.In.data(s"field$i").fire()) {
      output_R.data(s"field$i") := io.In.data(s"field$i").bits
      in_data_valid_R(i) := true.B
    }
  }

  // Connecting outputs
  io.Out.bits := output_R
  io.Out.valid := out_valid_R

  when(io.Out.fire()) {
    out_ready_R := io.Out.ready
    out_valid_R := false.B
  }

  switch(state) {
    is(s_IDLE) {
      when(enable_valid_R) {
        when(IsInValid()) {
          out_valid_R := true.B
          state := s_COMPUTE
        }
      }
    }
    is(s_COMPUTE) {
      when(out_ready_R) {
        for (i <- retTypes.indices) {
          in_data_valid_R(i) := false.B
        }

        out_valid_R := false.B
        enable_valid_R := false.B
        out_ready_R := false.B

        state := s_IDLE
        if (log) {
          printf("[LOG] " + "[" + module_name + "] "
            + "[TID->%d] [RET] " + node_name +
            ": Output fired @ %d\n", output_R.enable.taskID, cycleCount)
        }
      }
    }
  }
}

class RetNodeMultiIO(retTypes: Seq[Int], ID: Int)
                    (implicit val p: Parameters,
                     name: sourcecode.Name,
                     file: sourcecode.File) extends MultiIOModule
  with CoreParams with UniformPrintfs {

  val In = IO(new Bundle {
    val enable = Flipped(Decoupled(new ControlBundle()))
    val data = if (retTypes.size != 0)
      Some(Vec(retTypes.size, Flipped(Decoupled(new DataBundle()))))
    else
      None
  })
  val Out = IO(
    Decoupled(new Bundle {
      val enable = new ControlBundle()
      val ret = if (retTypes.size != 0) Some(new VariableData(retTypes)) else None
    }))

  val node_name = name.value
  val module_name = file.value.split("/").tail.last.split("\\.").head.capitalize

  override val printfSigil = module_name + ": " + node_name + ID + " "

  val (cycleCount, _) = Counter(true.B, 32 * 1024)

  val s_idle :: s_wait :: Nil = Enum(2)
  val state = RegInit(s_idle)

  val in_data = In.data.get
  val out_data = Out.bits.ret.get
  val out_valid = RegInit(false.B)

  val enable_r = RegInit(ControlBundle.default)
  val enable_v = RegInit(false.B)

  val input_r = Seq.fill(retTypes.size)(RegInit(DataBundle.default))
  val input_v = Seq.fill(retTypes.size)(RegInit(false.B))

  val output_r = RegInit(0.U.asTypeOf(Out.bits))


  val input_value = for (i <- 0 until retTypes.size) yield {
    val value = Mux(in_data(i).fire, in_data(i).bits, input_r(i))
    value
  }

  In.enable.ready := ~enable_v
  when(In.enable.fire) {
    enable_v := true.B
    enable_r := In.enable.bits
  }

  for (i <- 0 until retTypes.size) {
    in_data(i).ready := ~input_v(i)
    when(in_data(i).fire) {
      input_v(i) := true.B
      input_r(i) := in_data(i).bits
    }
  }

  val in_enable_valid = enable_v | In.enable.fire
  val in_data_valid = (in_data.map(_.fire) zip input_v).map {
    case (a, b) => a | b
  }

  //Initilizing the values
  //  for(i <- 0 until retTypes.size){
  //    out_data.elements(s"field$i") := input_value(i)
  //  }
  (out_data.elements.map(_._2) zip input_value).foreach { case (a, b) => a := b }
  Out.bits.enable := Mux(In.enable.fire, In.enable.bits, enable_r)

  Out.valid := out_valid

  switch(state) {
    is(s_idle) {
      when(in_enable_valid && in_data_valid.reduceLeft(_ & _)) {
        //when(in_enable_valid && in_data_valid.reduceLeft(_ & _)) {
        //We can fire the output
        Out.valid := true.B
        state := s_wait

        if (log) {
          printf("[LOG] " + "[" + module_name + "] "
            + "[TID->%d] [RET] " + node_name +
            ": Output fired @ %d\n", 0.U, cycleCount)
        }
      }
    }

    is(s_wait) {
      when(Out.fire) {
        enable_v := false.B
        input_v.foreach(_ := false.B)
        state := s_idle
      }
    }
  }

}










