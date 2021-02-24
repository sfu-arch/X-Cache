package memGen.memory.cache

object RoutineROM {

  val routineActions = Array [RoutinePC](


    // @todo should be fixed
    Routine ("LOAD_I") , Actions(Seq( "AllocateTBE","Allocate", "DataRQ", "SetState")),DstState("ID"),
    Routine ("LOAD_M"), Actions(Seq ("DataRQ", "SetState")),  DstState("M"),
    Routine("STORE_I"), Actions(Seq("AllocateTBE","Allocate", "WrInt", "SetState")),DstState("M"),
    Routine ("LOAD_ID") , Actions(Seq( "ReadInt", "DeallocateTBE", "SetState")), DstState("D"),
    Routine ("LOAD_IM") , Actions(Seq( "ReadInt","DeallocateTBE", "SetState")), DstState("M"),
    Routine ("STORE_IS") , Actions(Seq( "WrInt", "SetState")), DstState("S"),
    Routine ("DATA_ID") , Actions(Seq( "WrInt","DeallocateTBE", "SetState")), DstState("D")






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
