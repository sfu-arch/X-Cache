package memGen.memory.cache

object RoutineROM {

  val routineActions = Array [RoutinePC](


    // @todo should be fixed
    // 0-5
    Routine ("LOAD_I") , Actions(Seq("AllocateTBE","Allocate", "DataRQ", "SetState")),DstState("ID"),
    //6-9
    Routine ("STORE_I"), Actions(Seq("Allocate", "WrInt", "SetState")),DstState("E"),
    //10-11
    Routine ("LOAD_ID") , Actions(Seq( "SetState")), DstState("ID"),
    Routine ("LOAD_E") , Actions(Seq( "SetState")), DstState("E"),
    Routine ("DATA_ID") , Actions(Seq( "WrInt","ReadInt","DeallocateTBE","SetState")), DstState("E"),
    Routine ("STORE_E") , Actions(Seq( "WrInt","SetState")), DstState("E"),
    Routine ("NOP_E") , Actions(Seq("SetState")), DstState("E"),
    Routine ("NOP_I") , Actions(Seq("SetState")), DstState("I"),
    Routine ("NOP_ID") , Actions(Seq("SetState")), DstState("ID"),
    Routine ("STORE_IS") , Actions(Seq( "WrInt", "SetState")), DstState("S"),


    


    







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
