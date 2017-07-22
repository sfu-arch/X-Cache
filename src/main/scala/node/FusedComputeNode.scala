// package node

// import chisel3._
// import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester}
// import chisel3.Module
// import chisel3.testers._
// import chisel3.util._
// import org.scalatest.{Matchers, FlatSpec}

// import config._
// import interfaces._
// import muxes._
// import util._

// class FusedComputeNodeIO(NumIns: Int, NumOuts: Int)
//                    (implicit p: Parameters)
// extends HandShakingFusedIO (NumIns, NumOuts)(new DataBundle) {

// }

// class FusedComputeNode(NumIns: Int, NumOuts: Int, ID: Int, opCode: Array[String])
//                  (sign: Boolean)
//                  (implicit p: Parameters)
//   extends HandShakingFused(NumIns, NumOuts, ID)(new DataBundle)(p) {
//   override lazy val io = IO(new FusedComputeNodeIO(NumIns, NumOuts))
//   // Printf debugging

//   //  val s_idle :: s_LATCH :: s_COMPUTE :: Nil = Enum(3)
//   // val state = RegInit(s_idle)

//   // /*==========================================*
//   //  *           Predicate Evaluation           *
//   //  *==========================================*/

//   // val predicate = IsInPredicate() & IsEnable()
//   // val start = IsInValid() & IsEnableValid()

//   // /*===============================================*
//   //  *            Latch inputs. Wire up output       *
//   //  *===============================================*/

//   // //printfInfo("start: %x\n", start)


//   // // Wire up Outputs
//   // for (i <- 0 until NumOuts) {
//   //   io.Out(i).bits.data := FU.io.out
//   //   io.Out(i).bits.predicate := predicate 
//   // }


//   // /*============================================*
//   //  *            ACTIONS (possibly dangerous)    *
//   //  *============================================*/

//   // //Instantiate ALU with selected code
//   // var FU = Module(new UALU(xlen, opCode(0)))

//   // FU.io.in1 := in_data_R(0).data
//   // FU.io.in2 := in_data_R(1).data
   

//   // when(start & predicate & state =/= s_COMPUTE) {
//   //   state := s_COMPUTE
//   //   ValidOut()
//   // }.elsewhen(start & ~predicate & state =/= s_COMPUTE) {
//   //   //printfInfo("Start sending data to output INVALID\n")
//   //   state := s_COMPUTE
//   //   ValidOut()
//   // }

//   // /*==========================================*
//   //  *            Output Handshaking and Reset  *
//   //  *==========================================*/

//   // when(IsOutReady() & (state === s_COMPUTE)) {
//   //   // Reset data
//   //   state := s_idle
//   //   //Reset output
//   //   Reset()
//   // }
//   // var signed = if (sign == true) "S" else "U"
//   // override val printfSigil = opCode(0) + xlen +  "_" + signed + "_" + ID + ":"

//   // if (log == true && (comp contains "OP")) {
//   //   val x = RegInit(0.U(xlen.W))
//   //   x     := x + 1.U
  
//   //   verb match {
//   //     case "high"  => { }
//   //     case "med"   => { }
//   //     case "low"   => {
//   //       printfInfo("Cycle %d : { \"Inputs\": {",x)
//   //       printInValid()
//   //       printf("}")
//   //       printf("\"State\": {\"State\": \"%x\"",state)
//   //       printInData()
//   //       // \"(L,R)\": \"%x,%x\",  \"O(V,D,P)\": \"%x,%x,%x\"
//   //       // printf("\"Outputs\": {\"Out\": %x}",io.Out(0).fire())
//   //       // printf("}")
//   //      }
//   //     case everythingElse => {}
//   //   }
//   // }
// }
