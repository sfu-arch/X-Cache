package memGen.memory.cache

import chisel3._

object ActionList {

    val typeLen   = 4
    val nSigs = 28

    val actionLen = typeLen + nSigs

    val actions = Map[String, Bits](

        ("Allocate" ,     "b0000_000000000000000000_0000110100".U(actionLen.W)) ,
        ("ReadInt" ,      "b0000_000000000000000000_0100000000".U),
        ("Probe",         "b0000_000000000000000000_0000001011".U),
        ("Aloc" ,         "b0000_000000000000000000_0000110100".U),
        ("DAloc",         "b0000_000000000000000000_0001000000".U),
        ("WrInt",         "b0000_000000000000000000_0010000000".U),
        ("RdInt",         "b0000_000000000000000000_0100000000".U),

        ("WAIT",          "b0001_000000000000000000_0000000000".U),
        ("NOP",           "b0000_000000000000000000_0000000000".U),

        ("AllocateTBE",   "b0001_000000000000000000_00000000_01".U),
        ("DeallocateTBE", "b0001_000000000000000000_00000000_10".U),
        ("UpdateTBE",     "b0001_000000000000000000_00000000_11".U), // should update field 0
        ("SetState",      "b0011_000000000000000000_0000000000".U),
        ("Feedback",      "b0010_000000000000000000_0000000000".U ),

        ("DataRQ",        "b0100_000000000000000000_1000000_00_0".U),  // addrSrcForMemReq = action(0,0) ? src(action(2,1)) : addrOfTheInputInstToXMU
        ("DataRQWalker",  "b0100_000000000000000000_1000000_01_1".U),  // addrSrcForMemReq = action(0,0) ? src(action(2,1)) : addrOfTheInputInstToXMU

//        functions: add: 0, sub: 1, mult: 2, shift_r: 3, shift_l: 4, xor: 5
//        |  tbe_field/imm/ operand_2 -> 0: Reg, 1:TBE, 2: Data, 3: hardcoded | addr/ operand_1 | write_addr | function |

        ("AddWalker",       "b1_10_0_000000000000000000000_00_01_000".U ), // add with op1: reg op2: data

        ("ShiftWalker7",    "b1_11_1_000000000000000000111_00_00_011".U ),  // Xor with op1: reg op2: hardcode = 0
        ("ShiftWalker13",   "b1_11_1_000000000000000001101_00_01_011".U ),  // Xor with op1: reg op2: hardcode = 0
        ("ShiftWalker21",   "b1_11_1_000000000000000010101_00_01_011".U ),  // Xor with op1: reg op2: hardcode = 0
        ("XorWalker",       "b1_00_0_000000000000000000000_01_00_101".U ),  // Xor with op1: reg op2: hardcode = 0
        ("XorWalkerAddr",   "b1_00_1_000000000000000000000_00_00_101".U ),  // Xor with op1: reg op2: hardcode = 0

        ("AndWalker1023",   "b1_11_0_000000000001111111111_00_00_001".U ),  // Xor with op1: reg op2: hardcode = 0
        ("AndWalker2047",   "b1_11_0_000000000011111111111_00_00_001".U ),  // Xor with op1: reg op2: hardcode = 0
        ("BLTWalker1714",   "b1_11_0_000000000011010110010_00_00_110".U ),  // Xor with op1: reg op2: hardcode = 0









    )


    // fill the input table
}
