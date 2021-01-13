package dandelion.memory.cache

import chipsalliance.rocketchip.config._
import chisel3._
import dandelion.config._
import dandelion.memory.cache.{HasCacheAccelParams, State}
import chisel3.util._
import chisel3.util.Enum

object RoutineROM {

  val routineActions = Array [RoutinePC](


    // @todo should be fixed
    Routine ("LOAD_I") , Actions(Seq( "AllocateTBE","Allocate", "DataRQ")),
    Routine ("LOAD_M"), Actions(Seq ("DataRQ")),
    Routine("STORE_I"), Actions(Seq("Allocate", "DataRQ")),
    Routine ("LOAD_ID") , Actions(Seq( "ReadInt", "DeallocateTBE")),
    Routine ("LOAD_IM") , Actions(Seq( "ReadInt","DeallocateTBE")),
    Routine ("STORE_IS") , Actions(Seq( "WrInt"))




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
