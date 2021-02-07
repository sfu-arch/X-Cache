package dandelion.memory.cache

object RoutineROM {

  val routineActions = Array [RoutinePC](


    // @todo should be fixed
    Routine ("LOAD_I") , Actions(Seq( "AllocateTBE","Allocate", "DataRQ", "SetState")),DstState("ID"),
    Routine ("LOAD_M"), Actions(Seq ("DataRQ", "SetState")),  DstState("M"),
    Routine("STORE_I"), Actions(Seq("AllocateTBE","Allocate", "DataRQ", "SetState")),DstState("IM"),
    Routine ("LOAD_ID") , Actions(Seq( "ReadInt", "DeallocateTBE", "SetState")), DstState("D"),
    Routine ("LOAD_IM") , Actions(Seq( "ReadInt","DeallocateTBE", "SetState")), DstState("M"),
    Routine ("STORE_IS") , Actions(Seq( "WrInt", "SetState")), DstState("S"),
    Routine("STORE_IM"), Actions(Seq("Allocate", "DataRQ", "SetState")),DstState("M")





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
