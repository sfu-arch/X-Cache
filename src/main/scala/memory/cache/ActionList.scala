package memGen.memory.cache

import chisel3._

object ActionList {

    val typeLen   = 4
    val nSigs = 10

    val actionLen = typeLen + nSigs

    val actions = Map[String, Bits](

        ("Allocate" ,     "b0000_0000110100".U(actionLen.W)) ,
        ("ReadInt" ,      "b0000_0100000000".U),
        ("Probe",         "b0000_0000001011".U),
        ("Aloc" ,         "b0000_0000110100".U),
        ("DAloc",         "b0000_0001000000".U),
        ("WrInt",         "b0000_0010000000".U),
        ("RdInt",         "b0000_0100000000".U),
        ("DataRQ",        "b0000_1000000000".U), // sending data to mem ctrl should be added
        ("NOP",           "b0000_0000000000".U),
        ("AllocateTBE",   "b0001_0000000001".U),
        ("DeallocateTBE", "b0001_0000000010".U),
        ("UpdateTBE",     "b0001_0000000011".U), // should update field 0
        ("SetState",      "b0011_0000000000".U),
        ("Feedback",      "b0010_0000000000".U ),
        ("Add",           "b1000_0000000000".U ) // add with reg reg
//        ("ReadTBE",     "b00000000011".U)





    )


    // fill the input table
}
