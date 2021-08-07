package memGen.memory.cache


abstract class RoutineRom{
    val routineActions = Array [RoutinePC]()
}

object RoutineROMLDST extends RoutineRom{

  override val routineActions = Array [RoutinePC](

    // 0-5
    Routine ("LOAD_I") , Actions(Seq("AllocateTBE","Allocate", "DataRQ", "UpdateTBE", "SetState")),DstState("ID"),
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

  )

}


object RoutineROMWalker extends RoutineRom{

  val MASK22 = Seq("AndWalker2047", "BLTWalker1714", "WAIT", "AndWalker1023")
  val MASK20 = Seq("AndWalker2047", "BLTWalker1714", "WAIT", "AndWalker1023")
  val BUCK256 = Seq("AddBucket256" )

  val MakeAddr = Seq("ShiftLeftWalker", "AddWalker")

  val HASH = Seq("ShiftWalker7", "ShiftWalker13", "XorWalker", "ShiftWalker21", "XorWalker", "XorWalkerAddr") ++
    MASK22 ++ MakeAddr ++ BUCK256

  val Make32Ones = Seq("RightOnes","ShiftLeft16", "OrOnes")
  val MASKData = Make32Ones ++ Seq("MaskWalkerData")
  val COMP = Seq ("BNEQWalkerDataAddr", "WAIT", "WrInt","RdInt")

  val GetPtr = Seq("ShiftLeft32", "MaskWalkerData", "OneOne" )
  val ReqNext = GetPtr ++ Seq("BLTIfZero", "WAIT" , "ShiftRightWalker", "AddWalkerWithTBE", "DataRQWalker")




  override val routineActions = Array [RoutinePC](

    // 0-5
    Routine ("FIND_I") , Actions(Seq("AllocateTBE","Allocate", "AddWalker" , "UpdateTBE") ++  HASH ++
                                 Seq("DataRQWalker", "SetState")),DstState("IB"),

    Routine ("DATA_IB") , Actions(Seq( "BNEQIfDataNotZero", "WAIT", "DeallocateTBE", "SetState","NOP", "AddWalker", "ShiftLeftWalker", "AddWalkerWithTBE", "DataRQWalker","SetState")), DstState("I"), DstState("ID"),
    Routine ("DATA_ID") , Actions(MASKData ++ COMP ++ ReqNext ++ Seq("DeallocateTBE", "SetState")), DstState("V"),
    Routine ("DATA_I") ,  Actions(Seq( "Allocate","WrInt" ,"RdInt","SetState")), DstState("ID"),


    Routine ("FIND_ID") , Actions(Seq("SetState")),DstState("ID"),
    Routine ("FIND_V") , Actions(Seq( "SetState")), DstState("V"),


  )
}


object RoutineROMDasxArray extends RoutineRom{

  val ReqForFiveLines = Seq("AddFive","DataRQWalker")

  override val routineActions = Array [RoutinePC](

    Routine ("COLLECT_I") , Actions(Seq("AllocateTBE","Allocate") ++ ReqForFiveLines ++ Seq("FeedbackPrep","SetState")), DstState("IC"),

    Routine ("DATA_IC") , Actions(Seq( "DeallocateTBE","WrInt","RdInt","SetState")), DstState("V"),
    Routine ("DATA_IP") , Actions(Seq( "DeallocateTBE","WrInt","SetState")), DstState("V"),

    Routine ("LOAD_E") , Actions(Seq( "SetState")), DstState("E"),
    Routine ("LOAD_V") , Actions(Seq( "SetState")), DstState("V"),


    Routine ("DATA_I") ,  Actions(Seq( "Allocate", "WrInt","RdInt","SetState")), DstState("V"),
    Routine ("PREP_I") , Actions(Seq("BIfDataNotZero", "WAIT","SetState","NOP", "AllocateTBE","Allocate")  ++ Seq("FeedbackPrep","SetState")), DstState("IC") ,DstState("IP"),

    Routine ("STORE_I"), Actions(Seq("Allocate", "WrInt", "SetState")),DstState("E"),
    Routine ("STORE_E") , Actions(Seq( "WrInt","SetState")), DstState("E"),

  )
}

object RoutineROMGraphPulse extends RoutineRom{

  override val routineActions = Array [RoutinePC](

    Routine ("INIT_I")  ,  Actions(Seq("Allocate") ++ Seq("WrInt","SetState")), DstState("V"),
    Routine ("UPDATE_V"),  Actions(Seq( "WrInt","RdInt","SetState")), DstState("V"),
    Routine ("DATA_I") ,  Actions(Seq( "Allocate", "WrInt","RdInt","SetState")), DstState("V"),
    Routine ("PREP_I") ,  Actions(Seq("BLTIfDataZero", "WAIT", "AllocateTBE","Allocate")  ++ Seq("FeedbackPrep","SetState")), DstState("IC"),

    Routine ("STORE_I"),  Actions(Seq("Allocate", "WrInt", "SetState")),DstState("E"),
    Routine ("STORE_E") , Actions(Seq( "WrInt","SetState")), DstState("E"),

  )
}


object RoutineROMSpArch extends RoutineRom{

  val ReqForMultiLines = Seq("AddWalker","DataRQWalker") // copy data to src 1

  override val routineActions = Array [RoutinePC](

    Routine ("COLLECT_I") , Actions(Seq("AllocateTBE","Allocate") ++ ReqForMultiLines ++ Seq("FeedbackSparch","SetState")), DstState("IC"),
    Routine ("PREP_I") , Actions(Seq("BIfDataNotZero", "WAIT","SetState","NOP", "AllocateTBE","Allocate")  ++ Seq("FeedbackSparch","SetState")), DstState("IC") ,DstState("IP"),

    Routine ("DATA_IC") , Actions(Seq( "DeallocateTBE","WrInt","RdInt","SetState")), DstState("V"),
    Routine ("DATA_IP") , Actions(Seq( "DeallocateTBE","WrInt","SetState")), DstState("V"),

    Routine ("COLLECT_V") , Actions(Seq("FeedbackCollect", "SetState")), DstState("V"),

    Routine ("DATA_I") ,  Actions(Seq( "Allocate", "WrInt","RdInt","SetState")), DstState("V"),


  )
}