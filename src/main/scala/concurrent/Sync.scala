package dataflow

import chisel3._
import chisel3.util._
import chisel3.Module
import config.{CoreParams, Parameters}
import control.BasicBlockNoMaskIO
import interfaces.{ControlBundle, DataBundle}
import node.{HandShakingCtrlNPS, HandShakingCtrlNoMaskIO, HandShakingIONPS}

class SyncIO(NumOuts : Int, NumInc : Int, NumDec : Int)(implicit p: Parameters)
extends HandShakingIONPS(NumOuts)(new ControlBundle)(p)
{
  val incIn = Flipped(Vec(NumInc, Decoupled(new ControlBundle())))
  val decIn = Flipped(Vec(NumDec, Decoupled(new ControlBundle())))
}

class Sync(NumOuts : Int,  NumInc : Int, NumDec : Int, ID: Int)
          (implicit p: Parameters,
           name: sourcecode.Name,
           file: sourcecode.File)
  extends HandShakingCtrlNPS(NumOuts, ID)(p) {
  override lazy val io = IO(new SyncIO(NumOuts, NumInc, NumDec)(p))
  // Printf debugging
  val node_name = name.value
  val module_name = file.value.split("/").tail.last.split("\\.").head.capitalize
  override val printfSigil =  "[" + module_name + "] " + node_name + ": " + ID + " "
  val (cycleCount,_) = Counter(true.B,32*1024)

  /*===========================================*
   *            Registers                      *
   *===========================================*/

  // State machine
  val s_IDLE :: s_COMPUTE :: s_DONE :: Nil = Enum(3)
  val state = RegInit(s_IDLE)
  val enableID = RegInit(0.U(1<<tlen))
  val syncCount = RegInit(0.U(8.W))

  /*==========================================*
   *           Predicate Evaluation           *
   *==========================================*/

  val predicate = IsEnable()
  val start     = IsEnableValid()

  io.enable.ready := (state === s_IDLE)
  when (io.enable.fire()){
    enableID := io.enable.bits.taskID
  }

  /*============================================*
   *            ACTIONS (possibly dangerous)    *
   *============================================*/
  val incArb = Module(new Arbiter(new ControlBundle, NumInc))
  val decArb = Module(new Arbiter(new ControlBundle, NumDec))

  incArb.io.in <> io.incIn
  decArb.io.in <> io.decIn

  incArb.io.out.ready := true.B
  decArb.io.out.ready := true.B
  val inc = incArb.io.out.fire() && incArb.io.out.bits.control
  val dec = decArb.io.out.fire() && decArb.io.out.bits.control
  when(inc && !dec) {
    syncCount := syncCount + 1.U
  }.elsewhen(!inc && dec) {
    syncCount := syncCount - 1.U
  }

  for (i <- 0 until NumOuts) {
    io.Out(i).bits.control := predicate
    io.Out(i).bits.taskID := enableID
  }
  switch (state) {
    is (s_IDLE) {
      when(start && predicate) {
        state := s_COMPUTE
      }
    }
    is (s_COMPUTE) {
      when (syncCount === 0.U) {
        ValidOut()
        state := s_DONE
      }
    }
    is (s_DONE) {
      when(IsOutReady()) {
        Reset()
        when (predicate) {printf("[LOG] " + "[" + module_name + "] " + node_name +  ": Output fired @ %d\n",cycleCount)}
        state := s_IDLE
      }
    }
  }

  io.Out(0).bits.control := predicate

}
/*
class Sync(NumOuts : Int,  NumInc : Int, NumDec : Int, ID: Int)
           (implicit p: Parameters,
            name: sourcecode.Name,
            file: sourcecode.File)
  extends HandShakingCtrlNPS(NumOuts, ID)(p) {
  override lazy val io = IO(new SyncIO(NumOuts, NumInc, NumDec)(p))
  // Printf debugging
  val node_name = name.value
  val module_name = file.value.split("/").tail.last.split("\\.").head.capitalize
  override val printfSigil =  "[" + module_name + "] " + node_name + ": " + ID + " "
  val (cycleCount,_) = Counter(true.B,32*1024)


  /*===========================================*
   *            Registers                      *
   *===========================================*/

  // idState machine
  val active = RegInit(false.B)
  val s_IDLE :: s_COMPUTE :: s_DONE :: Nil = Enum(3)
  val idState = Vec(1<<tlen, RegInit(s_IDLE))
  val enableID = RegInit(0.U(1<<tlen))
  val syncCount = Vec(1<<tlen, RegInit(0.U(8.W)))

  /*==========================================*
   *           Predicate Evaluation           *
   *==========================================*/

  val predicate = IsEnable()
  val start     = IsEnableValid()


  io.enable.ready := !active

  when (io.enable.fire()){
    enableID := io.enable.bits.taskID
  }

  /*============================================*
   *            ACTIONS (possibly dangerous)    *
   *============================================*/
  val incArb = Module(new Arbiter(new ControlBundle, NumInc))
  val decArb = Module(new Arbiter(new ControlBundle, NumDec))
  val updateArb = Module(new Arbiter(new ControlBundle, NumDec))

  incArb.io.in <> io.incIn
  decArb.io.in <> io.decIn
  updateArb.io.in(0) <> incArb.io.out // increments are higher priority
  updateArb.io.in(1) <> decArb.io.out // decrements lower priority
  updateArb.io.out.ready := true.B

  val update   = RegNext(init = false.B, next = updateArb.io.out.fire() && updateArb.io.out.bits.control)
  val inc      = RegNext(init = 0.U, next = updateArb.io.chosen)
  val updateID = RegNext(init = 0.U, next = incArb.io.out.bits.taskID)

  when(update) {
    when (inc =/= 0.U) {
      syncCount(updateID) := syncCount(updateID) + 1.U
    }.otherwise{
      syncCount(updateID) := syncCount(updateID) - 1.U
    }
  }

  for (i <- 0 until NumOuts) {
    io.Out(i).bits.control := predicate
    io.Out(i).bits.taskID  := updateID
  }

    switch(idState(updateID)) {
      is(s_IDLE) {
        when(start && predicate) {
          idState(updateID) := s_COMPUTE
        }
      }
      is(s_COMPUTE) {
        when(syncCount(updateID) === 0.U) {
          ValidOut()
          idState(updateID) := s_DONE
        }
      }
      is(s_DONE) {
        when(IsOutReady()) {
          when(predicate) {
            printf("[LOG] " + "[" + module_name + "] " + node_name + ": Output fired @ %d\n", cycleCount)
          }
          idState(updateID) := s_IDLE
        }
      }
    }

  io.Out(0).bits.control := predicate

}

*/