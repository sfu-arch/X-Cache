package memGen.memory.cache

abstract class NextRoutine{
  val routineTriggerList = Array [RoutinePC] ()
}
object nextRoutineLDST extends NextRoutine{

    override val routineTriggerList = Array [RoutinePC](

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

object nextRoutineWalker extends NextRoutine{

  override val routineTriggerList = Array [RoutinePC](

    Routine ("FIND_I") ,DstState("ID"), Trigger(Seq("FIND" , "I")),
    Routine ("DATA_ID"), DstState("V"), Trigger(Seq("DATA", "ID")),
    Routine ("DATA_IB"), DstState("ID"), Trigger(Seq("DATA", "IB")),
    Routine ("DATA_I"), DstState("I"), Trigger(Seq("DATA", "I")),

    Routine ("FIND_V"), DstState("V"), Trigger(Seq("FIND", "V")),
    Routine ("FIND_ID"), DstState("ID"), Trigger(Seq("FIND", "ID")),



  )

}


object nextRoutineDASX extends NextRoutine{

  override val routineTriggerList = Array [RoutinePC](

    Routine ("COLLECT_I") ,DstState("IC"), Trigger(Seq("COLLECT" , "I")),

    Routine ("DATA_IC"), DstState("V"), Trigger(Seq("DATA", "IC")),
    Routine ("DATA_I"), DstState("V"), Trigger(Seq("DATA", "I")),
    Routine ("DATA_IP"), DstState("V"), Trigger(Seq("DATA", "IP")),

    Routine ("LOAD_I"), DstState("I"), Trigger(Seq("LOAD", "I")),
    Routine ("LOAD_V"), DstState("V"), Trigger(Seq("LOAD", "V")),
    Routine ("LOAD_E"), DstState("E"), Trigger(Seq("LOAD", "E")),
    Routine ("LOAD_IC"), DstState("I"), Trigger(Seq("LOAD", "IC")),
    Routine ("LOAD_IP"), DstState("I"), Trigger(Seq("LOAD", "IP")),


    Routine ("COLLECT_E") ,DstState("IC"), Trigger(Seq("COLLECT" , "E")),
    Routine ("COLLECT_V") ,DstState("IC"), Trigger(Seq("COLLECT" , "V")),

    Routine ("PREP_V"), DstState("IP"), Trigger(Seq("PREP", "V")),
    Routine ("PREP_E"), DstState("IP"), Trigger(Seq("PREP", "E")),


    Routine ("PREP_I"), DstState("IP"), Trigger(Seq("PREP", "I")),

    Routine ("STORE_I"), DstState("E"), Trigger(Seq("STORE", "I")),
    Routine ("STORE_E"), DstState("E"), Trigger(Seq("STORE", "E")),



  )

}

object nextRoutineSpArch extends NextRoutine{

  override val routineTriggerList = Array [RoutinePC](

    Routine ("COLLECT_I") ,DstState("IC"), Trigger(Seq("COLLECT" , "I")),

    Routine ("DATA_IC"), DstState("V"), Trigger(Seq("DATA", "IC")),
    Routine ("DATA_I"), DstState("V"), Trigger(Seq("DATA", "I")),
    Routine ("DATA_IP"), DstState("V"), Trigger(Seq("DATA", "IP")),

    Routine ("COLLECT_V") ,DstState("IC"), Trigger(Seq("COLLECT" , "V")),

//    Routine ("PREP_V"), DstState("IP"), Trigger(Seq("PREP", "V")),

    Routine ("PREP_I"), DstState("IP"), Trigger(Seq("PREP", "I")),


  )

}

object nextRoutineGP extends NextRoutine{

  override val routineTriggerList = Array [RoutinePC](

    Routine ("INIT_I") ,DstState("IU"), Trigger(Seq("INIT" , "I")),
    Routine ("UPDATE_I"), DstState("IU"), Trigger(Seq("UPDATE", "I")),

    Routine ("UPDATE_IU"), DstState("IU"), Trigger(Seq("UPDATE", "IU")),
    Routine ("DATA_IU"), DstState("IU"), Trigger(Seq("DATA", "IU")),



  )

}

object nextRoutineSyn extends NextRoutine{

  override val routineTriggerList = Array [RoutinePC](

    Routine ("IND_I") ,DstState("DReq"), Trigger(Seq("IND" , "I")),

    Routine ("DRAM_DReq"), DstState("DWait"), Trigger(Seq("DRAM", "DReq")),
    Routine ("DATA_DWait"), DstState("VD"), Trigger(Seq("DATA", "DWait")),

    Routine ("AGEN_DWait") ,DstState("DWait"), Trigger(Seq("AGEN" , "DWait")),
    Routine ("AGEN_VD") ,DstState("DReq"), Trigger(Seq("AGEN" , "VD")),

    Routine ("PROD_DWait"), DstState("DWait"), Trigger(Seq("PROD", "DWait")),

    Routine ("PROD_VD"), DstState("D"), Trigger(Seq("PROD", "VD")),


    Routine ("IND_D"), DstState("D"), Trigger(Seq("IND", "D")),
    Routine ("DRAM_I"), DstState("I"), Trigger(Seq("DRAM", "I")),
    Routine ("PROD_I"), DstState("I"), Trigger(Seq("PROD", "I")),
    Routine ("AGEN_I"), DstState("I"), Trigger(Seq("AGEN", "I")),

  )

}
