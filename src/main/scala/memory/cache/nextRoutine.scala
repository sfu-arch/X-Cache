package dandelion.memory.cache

import chipsalliance.rocketchip.config._
import chisel3._
import dandelion.config._
import dandelion.memory.cache.{HasCacheAccelParams, State}
import chisel3.util._
import chisel3.util.Enum

object nextRoutine {

    val routineTriggerList = Array [RoutinePC](

                    Routine ("LOAD_I") ,Trigger( Seq ("LOAD" , "I")),
                    Routine ("LOAD_M") ,  DstState("I"), Trigger( Seq("LOAD", "M")),
                    Routine ("STORE_I") , Trigger( Seq("STORE", "I")),
                    Routine ("LOAD_ID") , DstState("M"),Trigger( Seq ("LOAD" , "ID")),
                    Routine ("LOAD_IM") ,  DstState("I"), Trigger( Seq("LOAD", "IM")),
                    Routine ("STORE_IS") ,  DstState("IM"), Trigger( Seq("STORE", "IS"))





    )


  // fill the input table
}
