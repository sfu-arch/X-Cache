package memGen.memory.cache

object RoutineROM {

  val routineActions = Array [RoutinePC](


    // @todo should be fixed
    // 0-5
    Routine ("LOAD_I") , Actions(Seq("AllocateTBE","Allocate", "DataRQ", "UpdateTBE","SetState")),DstState("ID"),
    //6-9
    Routine ("STORE_I"), Actions(Seq("Allocate", "WrInt", "SetState")),DstState("E"),
    //10-11
    Routine ("LOAD_ID") , Actions(Seq( "SetState")), DstState("ID"),
    Routine ("LOAD_E") , Actions(Seq( "SetState")), DstState("E"),
    Routine ("DATA_ID") , Actions(Seq( "DeallocateTBE","WrInt","RdInt","SetState")), DstState("E"),
    Routine ("STORE_E") , Actions(Seq( "WrInt","SetState")), DstState("E"),
    Routine ("NOP_E") , Actions(Seq("SetState")), DstState("E"),
    Routine ("NOP_I") , Actions(Seq("SetState")), DstState("I"),
    Routine ("NOP_ID") , Actions(Seq("SetState")), DstState("ID"),
    Routine ("STORE_IS") , Actions(Seq( "WrInt", "SetState")), DstState("S"),


    // Prefetch_I : Allocate, AllocateTBE, DataRQ 16, Feedback 1, SetState("PF_It")
    // Prefetch_It: Allocate, AllocateTBE, BRZ 1, Feedback 1, SetState("It") 
    // Data_PF_IT :

    // node: 0
    // WideLoad_I: AllocateTBE, BRNF 3, Allocate, DataRQ 2, SetState("WL"), SetState("WL_FWD")
    // DATA_WL: Write, DeallocateTBE
    // DATA_WL-FWD: PUSHRQ TOOthers Node_1, Deallocate TBE
    

    // node: 1
    // WideLoad_I : AllocateTBE, Allocate, SetState("WL")


    


    







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
