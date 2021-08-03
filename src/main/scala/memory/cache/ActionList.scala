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
        ("UpdateTBE",     "b0001_000000000000000000_00000_01_0_11".U), // should update field 0 with src(1)
        ("SetState",      "b0011_000000000000000000_0000000000".U),
        ("FeedbackPrep",  "b0010_0000000000000_100_1000_01000000".U ), // Feedback for Prep

        ("DataRQ",        "b0100_000000000000000000_1000000_00_0".U),  // addrSrcForMemReq = action(0,0) ? src(action(2,1)) : addrOfTheInputInstToXMU
        ("DataRQWalker",  "b0100_000000000000000000_1000000_01_1".U),  // addrSrcForMemReq = action(0,0) ? src(action(2,1)) : addrOfTheInputInstToXMU

//        functions: add: 0, sub: 1, mult: 2, shift_r: 3, shift_l: 4, xor: 5
//        |  tbe_field/imm/ operand_2 -> 0: Reg, 1:TBE, 2: Data, 3: hardcoded | addr/ operand_1 | write_addr | function |

        ("AddWalker",         "b1_10_0_00000000_00000000_00_000001_0000".U ), // add with op1: reg op2: data
        ("AddBucket256",      "b1_11_0_00001000_00000000_01_000001_0000".U ), // add with op1: reg op2: data

        ("AddWalkerWithTBE",  "b1_01_0_00000000_00000000_00_000001_0000".U ), // add with op1: reg op2: data

        ("ShiftWalker7",      "b1_11_1_00000000_00000111_00_000000_0011".U ),  // Xor with op1: reg op2: hardcode = 0
        ("ShiftWalker13",     "b1_11_1_00000000_00001101_00_000001_0011".U ),  // Xor with op1: reg op2: hardcode = 0
        ("ShiftWalker21",     "b1_11_1_00000000_00010101_00_000001_0011".U ),  // Xor with op1: reg op2: hardcode = 0
        ("ShiftLeftWalker",   "b1_11_0_00000000_00000011_01_000000_0100".U ),  // Xor with op1: reg op2: hardcode = 0

        ("ShiftRightWalker",  "b1_11_0_00000000_00011101_01_000000_0011".U ),  // Xor with op1: reg op2: hardcode = 0
        ("XorWalker",         "b1_00_0_00000000_00000000_01_000000_0101".U ),  // Xor with op1: reg op2: hardcode = 0
        ("XorWalkerAddr",     "b1_00_1_00000000_00000000_00_000000_0101".U ),  // Xor with op1: reg op2: hardcode = 0
        ("AndWalker1023",     "b1_11_0_00000011_11111111_00_000001_0001".U ),  // Xor with op1: reg op2: hardcode = 0
        ("AndWalker2047",     "b1_11_0_00000111_11111111_00_000001_0001".U ),  // Xor with op1: reg op2: hardcode = 0
        ("BLTWalker1714",     "b1_11_0_00000110_10110010_00_000001_0110".U ),  // Xor with op1: reg op2: hardcode = 0

        ("BNEQWalkerDataAddr","b1_00_1_00000000_00000001_00_000010_0111".U ),  // Xor with op1: reg op2: hardcode = 0

        ("OneOne",            "b1_11_0_00000000_00000001_10_000010_0000".U ),  // Add with op1: reg op2: hardcode = 0

        ("BLTIfZero",         "b1_00_0_00000000_00000010_01_000011_0110".U ),  // Xor with op1: reg op2: hardcode = 0

        ("BNEQIfDataNotZero", "b1_10_0_00000000_00000000_00_000011_0111".U ),  // Xor with op1: reg op2: hardcode = 0

        ("RightOnes",         "b1_11_0_11111111_11111111_00_000000_0000".U ),  // Add with op1: reg op2: hardcode = 0
        ("ShiftLeft16",       "b1_11_0_00000000_00010000_00_000001_0100".U ),  // Shift_l with op1: reg op2: hardcode = 0
        ("ShiftLeft32",       "b1_11_0_00000000_00100000_00_000000_0100".U ),  // Shift_l with op1: reg op2: hardcode = 0

        ("OrOnes",            "b1_00_0_00000000_00000001_00_000000_1000".U ),  // Or with op1: reg op2: hardcode = 0

        ("MaskWalkerData",    "b1_10_0_00000000_00000000_00_000001_0001".U ),  // And with op1: reg op2: hardcode = 0








        ("AddFive",            "b1_11_1_00000000_00000101_00_000001_0000".U ),  // Add with op1: reg op2: hardcode = 0
        ("BLTIfDataZero",      "b1_10_0_00000000_00000000_10_000011_0110".U ),  // Xor with op1: reg op2: hardcode = 0







          )


    // fill the input table
}
