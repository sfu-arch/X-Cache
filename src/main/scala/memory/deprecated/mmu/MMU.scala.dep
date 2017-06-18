// package memory.mmu

// import chisel3._
// import chisel3.Module
// import config._
// import util._
// import interfaces._

// /**
//   * Data bundle between dataflow nodes.
//   * @note 2 fields
//   *  data : U(xlen.W)
//   *  predicate : Bool
//   *  chosen
//   * @param p : implicit
//   * @return
//   */
// class ReadReqDataBundle (NReads: Int) (implicit p: Parameters) extends CoreBundle()(p){

//   override def cloneType = new ReadReqDataBundle(NReads).asInstanceOf[this.type]
//   // Data packet
//   val bits = new ReadReq()
//   val valid     = Bool()
//   val chosen = UInt(log2Ceil(NReads).W)
// }
// object ReadReqDataBundle {
//   def default(NReads: Int)(implicit p: Parameters): ReadReqDataBundle = {
//     val wire = Wire(new ReadReqDataBundle(NReads))
//     //    wire.bits      := 0.U
//     wire.chosen  := 0.U
//     wire.valid     := false.B
//     wire
//   }
// }


// class MmuInIO[T <: Data](gen: T, n: Int) extends Bundle {

//   override def cloneType = new MmuInIO(gen,n).asInstanceOf[this.type]
//   val in = Flipped(Decoupled(gen))
//   val chosen = Input(UInt(log2Ceil(n).W))
// }

// class MmuOutIO[T<: Data](gen: T, n:Int ) extends Bundle {

//   override def cloneType = new MmuOutIO(gen,n).asInstanceOf[this.type]
//   val out = Decoupled(gen)
//   val chosen = Output(UInt(log2Ceil(n).W))
// }


// abstract class MmuIO(val NReads :Int, NWrites: Int
//                     )(implicit val p: Parameters) extends Module with CoreParams{

//   val io = IO(new Bundle {
//     val readReq   = new MmuInIO(new ReadReq(), NReads)
//     val readResp  = new MmuOutIO(new ReadResp(),NReads)

//     val writeReq  = Flipped(Decoupled(new MmuInIO(new WriteReq(),NWrites)))
//     val writeResp = Decoupled(new MmuOutIO(new WriteResp(), NWrites))

//   })
// }

// class MMU(NReads: Int, NWrites: Int)(implicit p: Parameters) extends MmuIO(NReads, NWrites)(p) {

//   //TODO
//   //declare registers for req and send appropriate values to responses

//   val read_req_R = RegInit(ReadReqDataBundle.default(NReads))

//   // Input Handshaking
//   io.readReq.in.ready := ~read_req_R.valid

//   when( io.readReq.in.fire()) {
//     read_req_R.valid := true.B
//     read_req_R.bits := io.readReq.in.bits
//     read_req_R.chosen := io.readReq.chosen
//   }

//   //Output Handshaking

//   //TODO Since no Memory connected yet
//   // passing address as data bask to ReadResp
//   //read_req_R.bits is of type ReadReq()
//   //io.readResp.out.bits is of type ReadResp()

//   //Todo -> remove exta valid from ReadResponse
//   io.readResp.out.bits.valid := read_req_R.valid

//   //Todo -> data <= address ; address is passed as data
//   io.readResp.out.bits.data := read_req_R.bits.address
//   //  io.readResp.out.bits.data := 12.U



//   io.readResp.chosen := read_req_R.chosen
//   io.readResp.out.valid := read_req_R.valid

//   when(io.readResp.out.fire()) {
//     read_req_R.valid := false.B
//   }

//   print(p" \n mem_contrl: ${read_req_R.valid} \n")


// }

