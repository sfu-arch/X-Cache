package dandelion.memory.cache

import chipsalliance.rocketchip.config._
import chisel3._
import dandelion.config._
import dandelion.memory.cache.{HasCacheAccelParams, State}
import chisel3.util._
import chisel3.util.Enum

object RoutineROM {

  val routineActions = Array [RoutinePC](

    Routine ("LOAD_I") , Actions(Seq("Allocate", "ReadInt"))

    /*
    Allocate
    Beq allocPassed PASSED
    replace
    PASSED :
    ReadExt

     */



  )


  // fill the input table
}
