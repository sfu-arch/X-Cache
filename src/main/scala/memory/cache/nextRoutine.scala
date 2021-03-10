package memGen.memory.cache


object nextRoutine {

    val routineTriggerList = Array [RoutinePC](

                    Routine ("LOAD_I") ,DstState("ID"), Trigger( Seq ("LOAD" , "I")),
                    Routine ("LOAD_M") ,  DstState("I"), Trigger( Seq("LOAD", "M")),
                    Routine ("STORE_I") , DstState("M"), Trigger( Seq("STORE", "I")),
                    Routine ("LOAD_ID") , DstState("ID"),Trigger( Seq ("LOAD" , "ID")),
                    Routine ("LOAD_E") ,  DstState("E"), Trigger( Seq("LOAD", "E")),
                    Routine ("STORE_IS") ,  DstState("IM"), Trigger( Seq("STORE", "IS")),
                    Routine ("DATA_ID"), DstState("D"), Trigger(Seq("DATA", "ID")),
                    Routine ("STORE_E"), DstState("E"), Trigger(Seq("STORE", "E")),


                    






    )


  // fill the input table
}
