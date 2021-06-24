package memGen.memory.cache

import chipsalliance.rocketchip.config._

import scala.collection.mutable.ArrayBuffer
import chisel3._
import chisel3.util._
import memGen.config._


abstract class RoutinePC ()
case class Routine (name : String) extends RoutinePC
case class Actions (actionList: Seq[String]) extends RoutinePC
case class Trigger (triggerList : Seq[String]) extends  RoutinePC
case class DstState(name :String) extends  RoutinePC



class RoutinePtr (implicit val p: Parameters) extends HasAccelParams {

  def generateRoutineAddrMap (routineRom : Array[RoutinePC]  ): (Map[String,Int], Array[Int])= {

      var routineAddr = 0
      var mappedRoutine = Map[String, Int]()
      var dstStateArray = ArrayBuffer[Int]()
      for (routine <- routineRom) {
          routine match {
              case Actions(actionList) => routineAddr += actionList.length
              case Routine(name) => {
                  routineAddr += 1
                  mappedRoutine += ((name, routineAddr))
              }
              case DstState(state) => dstStateArray += accelParams.States.StateArray(state)
          }

      }
      (mappedRoutine, dstStateArray.toArray)

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


  def generateTriggerRoutineBit (routineAddrMap : Map[String, Int], routineTrigger: Array[RoutinePC]) : (Array[Bits] , Array[Bits]) = {

    val events = accelParams.Events.EventArray
    val states = accelParams.States.StateArray
      val eventLen = if (events.size == 1) 1 else log2Ceil(events.size)
      val stateLen =  if (states.size == 1 ) 1 else log2Ceil(states.size)

    var routineTriggerBit = new ArrayBuffer[Bits]
      var routineDstStBit = new ArrayBuffer[Bits]
      for ( i <- 0 until math.pow(2, log2Ceil(states.size * events.size)).toInt){
          routineTriggerBit += 0.U
          routineDstStBit += 0.U

      }


      var routineStartAddr = 0
      var routineDstState = 0
      for (routine <- routineTrigger){
      routine match{
        case Routine(name) =>
            routineStartAddr = routineAddrMap(name)
        case DstState(name) =>
            routineDstState = states(name)

        case Trigger(list) =>
            val event = events(list(0)) << stateLen
            val state = states(list(1))
            val idx = event | state
            routineTriggerBit( idx) = (routineStartAddr.U)
            routineDstStBit (idx) = routineDstState.U
//            val line = Cat(lineName.U, event.U ,state.U )
//            println(p"event, state ${idx}\n")
//            printf(p"event, state ${(event ++ state).toInt}\n")
//            println(p"${routineTriggerBit(0)}\r\n")

//            println("Line " + lineName.U)

      }

    }
      (routineTriggerBit.toArray, routineDstStBit.toArray)

  }


    def generateActionRom(routineRom: Array[RoutinePC], actions: Map[String, Bits], dstState: Array[Int]): Array[Bits] ={
        var stateIdx = 0
        var actionBits = new ArrayBuffer[Bits]
        for (elem <- routineRom) {
            elem match {
                case Actions(actionList) =>
                    for (action <- actionList) {
                        action match {
                            case "SetState" => {
                                actionBits += (actions(action).asUInt() + dstState(stateIdx).asUInt())
                                stateIdx += 1
                            }

                            case default => actionBits += (actions(action))
                        }
                    }

//                case Actions(actionList) => for (action <- actionList) actionBits += 0.U

                case Routine(name) => actionBits += (0.U)
                case default => {}
            }
        }

        actionBits.toArray
    }



}