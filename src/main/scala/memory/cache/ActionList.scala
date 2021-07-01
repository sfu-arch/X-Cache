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

        ("NOP",           "b0000_0000000000".U),
        ("AllocateTBE",   "b0001_00000000_01".U),
        ("DeallocateTBE", "b0001_00000000_10".U),
        ("UpdateTBE",     "b0001_00000000_11".U), // should update field 0
        ("SetState",      "b0011_0000000000".U),
        ("Feedback",      "b0010_0000000000".U ),

        ("DataRQ",        "b0101_1000000_00_0".U),  // addrSrcForMemReq = action(0,0) ? action(2,1) : addrOfTheInputInstToXMU
        ("DataRQWalker",  "b0101_1000000_01_1".U),  // addrSrcForMemReq = action(0,0) ? action(2,1) : addrOfTheInputInstToXMU

//        functions: add: 0, sub: 1, mult: 2, shift_r: 3, shift_l: 4, xor: 5
//        |  tbe_field/imm/ operand_2 | tbe_field/ operand_1 | write_addr | function |

        ("AddWalker",     "b1_10_0_000_01_01_000".U ), // add with op1: reg op2: data
        ("XorWalker",     "b1_11_0_111_01_01_101".U )  // Xor with op1: reg op2: hardcode = 7




        )


    // fill the input table
}
