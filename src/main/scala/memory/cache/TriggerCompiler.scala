package dandelion.memory.cache

import scala.collection.mutable.ArrayBuffer
import chipsalliance.rocketchip.config._
import chisel3._
import dandelion.config._
import dandelion.memory.cache.{HasCacheAccelParams, State}
import chisel3.util._
import chisel3.util.Enum
//import dandelion.memory.TBE.Events._
import shapeless.ops.hlist.Length


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



//  def gereateEventList () : Map[String, Int] = {
//
//    var eventList = Map[String,Int]()
//    val events = Events.EventArray
//    val IntValue = 0
//    val IntClass = IntValue.getClass()
//
//    for (event <- events) {
//      event._1
//      val name = Event.getName()
//      val rtype = Event.getReturnType()
//      if (rtype == IntClass) {
////        val value = Event.
//      }
//    }
//    eventList
//  }


  def generateTriggerRoutineBit (routineAddrMap : Map[String, Int], routineTrigger: Array[RoutinePC]) : Array[Bits] = {

    val events = Events.EventArray
    val states = States.StateArray
      val eventLen = log2Ceil(events.size)
      val stateLen = log2Ceil(states.size)
    var routineTriggerBit = new ArrayBuffer[Bits]((eventLen * stateLen))

      var lineName = 0
      for (routine <- routineTrigger){
      routine match{
        case Routine(name) =>
            lineName = routineAddrMap(name)

        case Trigger(list) =>
            val event = events(list(0)).toString() + ""
            val state = states(list(1)).toString() + ""
//            val line = Cat(lineName.U, event.U ,state.U )

            routineTriggerBit( (event ++ state).toInt) = (lineName.U)
            println("Line " + lineName.U)

      }

    }
      routineTriggerBit.toArray

  }




}