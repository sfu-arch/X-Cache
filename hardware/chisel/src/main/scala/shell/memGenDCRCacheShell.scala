package dandelion.shell

import chisel3._
import chisel3.util._
import chipsalliance.rocketchip.config._
import dandelion.config._
import dandelion.interfaces.{ControlBundle, DataBundle}
import dandelion.interfaces.axi._
import dandelion.memory.cache._
import dandelion.accel._
import memGen.memory._
import memGen.shell._

class memGenDCRCacheShell [T <: memGenModule](accelModule: () => T)
                                             (numPtrs: Int = 0, numVals: Int = 6, numRets: Int = 0, numEvents: Int = 0, numCtrls: Int = 0)
                                             (implicit val p: Parameters) extends Module with HasAccelShellParams {
  val io = IO(new Bundle {
    val host = new AXILiteClient(hostParams)
    val mem = new AXIMaster(memParams)
  })


  val numInputs  = 3
  // comment test

  val regBits = dcrParams.regBits
  val ptrBits = regBits * 2

  val vcr = Module(new DCR)

  val accel = Module(accelModule())

  val sIdle :: sBusy :: sFlush :: sDone :: Nil = Enum(4)

  val state = RegInit(sIdle)
  val cycles = RegInit(0.U(regBits.W))
  val cnt = RegInit(0.U(regBits.W))
  val last = state === sDone
  val is_busy = state === sBusy

  when(state === sIdle) {
    cycles := 0.U
  }.elsewhen(state =/= sFlush & (state =/=sIdle)) {
    cycles := cycles + 1.U
  }

  /**
    * Connecting event controls and return values
    * Event zero always contains the cycle count
    */

  if (accel.RetsOut.size > 0) {
    for (i <- 1 to accel.RetsOut.size) {
      vcr.io.dcr.ecnt(i).bits := accel.io.out.bits.data(s"field${i - 1}").data
      vcr.io.dcr.ecnt(i).valid := accel.io.out.valid
    }
  }


 vcr.io.dcr.ecnt(0).valid := last
 vcr.io.dcr.ecnt(0).bits := cycles

  /**
    * @note This part needs to be changes for each function
    */
    val (nextChunk,_) = Counter(accel.io.in.fire, 1000)
    val DataReg = Reg(Vec(numVals, new DataBundle))
    val (cycle,stopSim) = Counter(true.B, 300)

  val vals = Seq.tabulate(numVals) { i => RegEnable(next = vcr.io.dcr.vals(i), init = 0.U(ptrBits.W), enable =  (state === sIdle)) }
  val ptrs = Seq.tabulate(2) { i => RegEnable(next = vcr.io.dcr.ptrs(i), init = 0.U(ptrBits.W), enable =  (state === sIdle)) }

  when(accel.io.out.fire()){
    // when(accel.io.out.bits.data("field0").data === 0.U){
      printf(p"addr ${accel.io.out.bits.data("field1").data} data ${accel.io.out.bits.data("field2").data} cycle ${cycles} \n") 
      // DataBundle(accel.io.out.bits.data("field1").data - ptrs(0) + ptrs(1)) := DataBundle(accel.io.out.bits.data("field2").data)
    //  }
  }


//  val DataQueue = for (i <- 0 until numInputs) yield {
//    val DQ = Module( new Queue( DataBundle(vals(i-numPtrs)), entries = numVals/numInputs))
//    DQ
//  }


  for (i <- 0 until numVals) {
    if( i % 3 == 1 )
      DataReg(i) := DataBundle(vals(i) + ptrs(0))
    else
     DataReg(i) := DataBundle(vals(i) )
  }

  for (i <- 0 until numInputs) {
    accel.io.in.bits.dataVals(s"field${i}") := DataReg(nextChunk * numInputs.U + i.U)
  }

  accel.io.in.bits.enable := ControlBundle.active()
  accel.io.in.valid := false.B
  accel.io.out.ready := is_busy | state === sDone


  switch(state) {
    is(sIdle) {
      when(vcr.io.dcr.launch) {
        printf(p"\nVals: ")
        vals.zipWithIndex.foreach(t => printf(p"val(${t._2}): ${t._1}, "))
        printf(p" \n")
        state := sBusy
      }
    }
    is(sBusy) {
      // when(accel.io.in.fire() ){
      //   printf(p"\nVals: ")
      //   vals.zipWithIndex.foreach(t => printf(p"val(${t._2}): ${t._1}, "))
      //   printf(p"\n")
      // }
      
        accel.io.in.valid := true.B
        when(nextChunk * numInputs.U > numVals.U ) {
          state := sDone
        }

    }

    is(sDone) {
      when(stopSim) {
        state := sIdle
      }
    }
  }

  vcr.io.dcr.finish := last & stopSim
  io.mem <> accel.io.mem
  io.host <> vcr.io.host

}

