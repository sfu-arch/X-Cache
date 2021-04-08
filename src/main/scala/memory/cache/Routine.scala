package memGen.memory.cache

object RoutineROM {

  val routineActions = Array [RoutinePC](


    // @todo should be fixed
    Routine ("LOAD_I") , Actions(Seq("AllocateTBE","Allocate", "DataRQ", "SetState")),DstState("ID"),
    Routine ("LOAD_M"), Actions(Seq ("DataRQ", "SetState")),  DstState("M"),
    Routine ("STORE_I"), Actions(Seq("Allocate", "WrInt", "SetState")),DstState("E"),
    Routine ("STORE_M"), Actions(Seq("Allocate", "WrInt", "SetState")),DstState("M"),
    Routine ("LOAD_ID") , Actions(Seq( "SetState")), DstState("ID"),
    Routine ("LOAD_E") , Actions(Seq( "SetState")), DstState("E"),
    Routine ("STORE_IS") , Actions(Seq( "WrInt", "SetState")), DstState("S"),
    Routine ("DATA_ID") , Actions(Seq( "WrInt","ReadInt","DeallocateTBE","SetState")), DstState("E"),
    Routine ("STORE_E") , Actions(Seq( "WrInt","SetState")), DstState("E"),
    Routine ("NOP_E") , Actions(Seq("SetState")), DstState("E"),
    


    







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
