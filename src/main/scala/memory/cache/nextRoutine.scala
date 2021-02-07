package memGen.memory.cache


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
