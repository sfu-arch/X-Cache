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
      val eventLen = if (events.size == 1) 1 else (log2Ceil(events.size))

      val stateLen =  if (states.size == 1) 1 else log2Ceil(states.size)

//      println (p"${eventLen}  ${stateLen} \r\n")

    var routineTriggerBit = new ArrayBuffer[Bits]
      for ( i <- 0 until eventLen * stateLen){
          routineTriggerBit += 0.U
      }


      var lineName = 0
      for (routine <- routineTrigger){
      routine match{
        case Routine(name) =>
            lineName = routineAddrMap(name)

        case Trigger(list) =>
            val event = events(list(0)).toString() + ""
            val state = states(list(1)).toString() + ""
//            val line = Cat(lineName.U, event.U ,state.U )

//            println(p"event, state ${event++state}\r\n")
//            printf(p"event, state ${(event ++ state).toInt}\r\n")
//            println(p"${routineTriggerBit(0)}\r\n")
            routineTriggerBit( (event ++ state).toInt) = (lineName.U)
//            println("Line " + lineName.U)

      }

    }
      routineTriggerBit.toArray

  }


    def generateActionRom(routineRom: Array[RoutinePC], actions: Map[String, Bits]): Array[Bits] ={
        var routineAddr = 0
        var actionBits = new ArrayBuffer[Bits]
        for (elem <- routineRom) {
            elem match {
                case Actions(actionList) =>  actionList.map{ case action => actionBits += actions(action)}
//                case Actions(actionList) => for (action <- actionList) actionBits += 0.U

                case Routine(name) => actionBits += (0.U)
            }
        }

        actionBits.toArray
    }



}