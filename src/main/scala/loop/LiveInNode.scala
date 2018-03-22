package node

import chisel3._
import chisel3.util._

import config._
import interfaces._
import util._

class LiveInNodeIO(NumOuts: Int)
                  (implicit p: Parameters)
  extends HandShakingIONPS(NumOuts)(new DataBundle) {

  //Input data for live in
  val InData = Flipped(Decoupled(new DataBundle()))

}

class LiveInNode(NumOuts: Int, ID: Int)
                (implicit p: Parameters,
                 name: sourcecode.Name,
                 file: sourcecode.File)
  extends HandShakingNPS(NumOuts, ID)(new DataBundle())(p) {
  override lazy val io = IO(new LiveInNodeIO(NumOuts))

  val node_name = name.value
  val module_name = file.value.split("/").tail.last.split("\\.").head.capitalize

  // Printf debugging
  override val printfSigil = module_name + ": " + node_name + ID + " "
  val (cycleCount,_) = Counter(true.B,32*1024)

  /*===========================================*
   *            Registers                      *
   *===========================================*/
  // In data Input
  val indata_R = RegInit(DataBundle.default)
  val indata_valid_R = RegInit(false.B)

  val s_IDLE :: s_VALIDOUT :: s_LATCH :: Nil = Enum(3)

  val state = RegInit(s_IDLE)

  /*===============================================*
   *            LATCHING INPUTS                    *
   *===============================================*/

  io.InData.ready := ~indata_valid_R
  when(io.InData.fire()) {
    indata_R <> io.InData.bits
    indata_valid_R := true.B
  }

  /*===============================================*
   *            DEFINING STATES                    *
   *===============================================*/

  switch(state){
    is(s_IDLE){
      when(io.InData.fire()){
        state := s_VALIDOUT
        ValidOut()
        printf("[LOG] " + "[" + module_name + "] " + node_name + ": Latch fired @ %d, Value:%d\n",cycleCount, io.InData.bits.data.asUInt())
      }
    }
    is(s_VALIDOUT){
      when(IsOutReady()){
        state := s_LATCH
      }
    }
    is(s_LATCH){
      when(enable_valid_R){
        when(enable_R){
          printf("[LOG] " + "[" + module_name + "] " + node_name + ": Latch invalidate @ %d\n",cycleCount)
          state := s_IDLE
          indata_R <> DataBundle.default
          indata_valid_R := false.B
          Reset()
        }.otherwise{
          state := s_VALIDOUT
          ValidOut()
          Reset()
        }
      }
    }
  }

  /*===============================================*
   *            CONNECTING OUTPUTS                 *
   *===============================================*/

  for (i <- 0 until NumOuts) {
    io.Out(i).bits <> indata_R
  }
}

class LiveInAckNodeIO(NumOuts: Int)
                  (implicit p: Parameters)
  extends HandShakingIONPS(NumOuts)(new AckBundle) {

  //Input data for live in
  val InData = Flipped(Decoupled(new AckBundle()))

}

class LiveInAckNode(NumOuts: Int, ID: Int)
                (implicit p: Parameters,
                 name: sourcecode.Name,
                 file: sourcecode.File)
  extends HandShakingNPS(NumOuts, ID)(new DataBundle())(p) {
  override lazy val io = IO(new LiveInNodeIO(NumOuts))

  val node_name = name.value
  val module_name = file.value.split("/").tail.last.split("\\.").head.capitalize

  // Printf debugging
  override val printfSigil = module_name + ": " + node_name + ID + " "
  val (cycleCount,_) = Counter(true.B,32*1024)

  /*===========================================*
   *            Registers                      *
   *===========================================*/
  // In data Input
  val indata_R = RegInit(DataBundle.default)
  val indata_valid_R = RegInit(false.B)

  val s_IDLE :: s_VALIDOUT :: s_LATCH :: Nil = Enum(3)

  val state = RegInit(s_IDLE)

  /*===============================================*
   *            LATCHING INPUTS                    *
   *===============================================*/

  io.InData.ready := ~indata_valid_R
  when(io.InData.fire()) {
    indata_R <> io.InData.bits
    indata_valid_R := true.B
  }

  /*===============================================*
   *            DEFINING STATES                    *
   *===============================================*/

  switch(state){
    is(s_IDLE){
      when(io.InData.fire()){
        state := s_VALIDOUT
        ValidOut()
        printf("[LOG] " + "[" + module_name + "] " + node_name + ": Latch fired @ %d, Value:%d\n",cycleCount, io.InData.bits.data.asUInt())
      }
    }
    is(s_VALIDOUT){
      when(IsOutReady()){
        state := s_LATCH
      }
    }
    is(s_LATCH){
      when(enable_valid_R){
        when(enable_R){
          printf("[LOG] " + "[" + module_name + "] " + node_name + ": Latch invalidate @ %d\n",cycleCount)
          state := s_IDLE
          indata_R <> DataBundle.default
          indata_valid_R := false.B
          Reset()
        }.otherwise{
          state := s_VALIDOUT
          ValidOut()
          Reset()
        }
      }
    }
  }

  /*===============================================*
   *            CONNECTING OUTPUTS                 *
   *===============================================*/

  for (i <- 0 until NumOuts) {
    io.Out(i).bits <> indata_R
  }
}
