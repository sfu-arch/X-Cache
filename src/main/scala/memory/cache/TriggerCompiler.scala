package dandelion.memory.TBE

import chipsalliance.rocketchip.config._
import chisel3._
import dandelion.config._
import dandelion.memory.cache.{HasCacheAccelParams, State}
import chisel3.util._
import chisel3.util.Enum

abstract class RoutinePC ()
case class Routine (name : String) extends RoutinePC
case class Actions (actionList: Seq[String]) extends RoutinePC
case class Trigger (triggerList : Seq[String]) extends  RoutinePC



object RoutinePtr {

  def generateRoutineAddrMap (routineRom : Array[RoutinePC]  ) : Map[String,Int] = {

      var routineAddr = 0
      var mappedRoutine = Map[String, Int]()
      for (routine <- routineRom) {
        routine match {
          case Actions(actionList) => routineAddr += 1
          case Routine(name) => mappedRoutine += ((name, routineAddr))
        }
      }

    mappedRoutine
  }



  def gereateTable (triggerList : (Map[selBlock, String]) , ) :  = {

  }

}