package memGen.memory.cache


object nextRoutineLDST {

    val routineTriggerList = Array [RoutinePC](

                    Routine ("LOAD_I") ,DstState("ID"), Trigger( Seq ("LOAD" , "I")),
                    Routine ("STORE_I") , DstState("M"), Trigger( Seq("STORE", "I")),
                    Routine ("LOAD_ID") , DstState("ID"),Trigger( Seq ("LOAD" , "ID")),
                    Routine ("LOAD_E") ,  DstState("E"), Trigger( Seq("LOAD", "E")),
                    Routine ("DATA_ID"), DstState("D"), Trigger(Seq("DATA", "ID")),
                    Routine ("STORE_E"), DstState("E"), Trigger(Seq("STORE", "E")),
                    Routine("NOP_E"), DstState("E"), Trigger(Seq("NOP", "E")),
                    Routine("NOP_ID"), DstState("ID"), Trigger(Seq("NOP", "ID")),  
                    Routine("NOP_I"), DstState("I"), Trigger(Seq("NOP", "I")),
    )

}

object nextRoutineWalker {

  val routineTriggerList = Array [RoutinePC](

    Routine ("LOAD_I") ,DstState("ID"), Trigger( Seq ("LOAD" , "I")),
    Routine ("STORE_I") , DstState("M"), Trigger( Seq("STORE", "I")),
    Routine ("LOAD_ID") , DstState("ID"),Trigger( Seq ("LOAD" , "ID")),
    Routine ("LOAD_E") ,  DstState("E"), Trigger( Seq("LOAD", "E")),
    Routine ("DATA_ID"), DstState("D"), Trigger(Seq("DATA", "ID")),
    Routine ("STORE_E"), DstState("E"), Trigger(Seq("STORE", "E")),
    Routine("NOP_E"), DstState("E"), Trigger(Seq("NOP", "E")),
    Routine("NOP_ID"), DstState("ID"), Trigger(Seq("NOP", "ID")),
    Routine("NOP_I"), DstState("I"), Trigger(Seq("NOP", "I")),
  )

}
