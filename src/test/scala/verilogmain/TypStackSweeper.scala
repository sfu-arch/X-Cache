package verilogmain

// /**
//   * Created by vnaveen0 on 2/6/17.
//   */

import chisel3.iotesters.{ ChiselFlatSpec, Driver, PeekPokeTester, OrderedDecoupledHWIOTester }
import org.scalatest.{ Matchers, FlatSpec }
import scala.util.control.Breaks._

import config._
import arbiters._
import memory._
import utility._
import Constants._

class TypeStackPeekPoker(c: TypeStackFile, Writes: Int, Reads: Int, Activity: String)(implicit p: config.Parameters) extends PeekPokeTester(c) {

  var count = 0
  val J = Activity match {
  case "low" => 1
  case "med" => Reads/2
  case "high" => Reads
  case _ => Reads
	}
  // var readidx = 0
  for (i <- 0 until Writes) {
    if (i == 0) {
      poke(c.io.WriteIn(i).bits.address, 4)
      poke(c.io.WriteIn(i).bits.RouteID, i)
      poke(c.io.WriteIn(i).bits.Typ, MT_HU)
      poke(c.io.WriteIn(i).bits.mask, 15)
      poke(c.io.WriteIn(i).valid, 1)
      poke(c.io.WriteIn(i).bits.data, 0xbeef + i)
    } else {
      poke(c.io.WriteIn(i).valid, 0)
    }

  }

  for (i <- 0 until Reads) {
    poke(c.io.ReadIn(i).bits.address, 4)
    poke(c.io.ReadIn(i).valid, 1)
    poke(c.io.ReadIn(i).bits.RouteID, i)
    poke(c.io.ReadIn(i).bits.Typ, MT_HU)
    if (i >= J) {
    	poke(c.io.ReadIn(i).valid,0)
    }
  }
  var time = 0
  while (count < J) {
    step(1)
    for (i <- 0 until Reads) {
      if (peek(c.io.ReadOut(i).valid) == 1) {
        count = count + 1
      }
    }
    time = time + 1
  }
  printf("\n Time :" + t + "\n")
}

object TypeStackFileVerilog extends App {

  type OptionMap = Map[Symbol, Any]

  def optionsparse(designargs: Array[String], optionslist: Array[Symbol]): OptionMap = {

    val usage = optionslist.mkString(",")

    if (designargs.length == 0) {
      println("Usage:" + usage)
      sys.exit
    }

    val arglist = designargs.toList
    def nextOption(map: OptionMap, list: List[String]): OptionMap = {
      def isSwitch(s: String) = (s(0) == '-')
      list match {
        case Nil => map
        case "--OpSize" :: value :: tail =>
          nextOption(map ++ Map(optionslist(0) -> value.toInt), tail)
        case "--BaseSize" :: value :: tail =>
          nextOption(map ++ Map(optionslist(1) -> value.toInt), tail)
        case "--EntrySize" :: value :: tail =>
          nextOption(map ++ Map(optionslist(2) -> value.toInt), tail)
        case "--AF" :: value :: tail =>
          nextOption(map ++ Map(optionslist(3) -> value.toString), tail)
        case string :: opt2 :: tail if isSwitch(opt2) =>
          nextOption(map ++ Map('infile -> string), list.tail)
        case string :: Nil => nextOption(map ++ Map('infile -> string), list.tail)

        case option :: tail =>
          println("Unknown option " + option)
          sys.exit
      }
    }
    val options = nextOption(Map(), arglist)
    val G = optionslist.map(options.contains(_))
    val m = G.reduceLeft(_ & _)
    if (m == false) {
      println("Usage : " + usage)
      sys.exit(0)
    }
    return options
  }

  val (testargs, designargs) = args.splitAt(6)
  // println(testargs.deep.mkString("\n"))
  implicit val p = config.Parameters.root((new MiniConfig).toInstance)

  val optionslist = Array('OpSize, 'BaseSize, 'EntrySize, 'AF)

  val optionkv = optionsparse(designargs, optionslist)

  val ops = optionkv('OpSize).asInstanceOf[Int]
  val basesize = optionkv('BaseSize).asInstanceOf[Int]
  val entrysize = optionkv('EntrySize).asInstanceOf[Int]
  val AF        = optionkv('AF).asInstanceOf[String]
  chisel3.iotesters.Driver.execute(testargs, () => new TypeStackFile(ID=10,Size=4,NReads=ops,NWrites=1)(WControl=new WriteMemoryController(NumOps=1,BaseSize=2,NumEntries=1))(RControl=new ReadMemoryController(NumOps=ops,BaseSize=basesize,NumEntries=entrysize)))
  { c => new TypeStackPeekPoker(c,Writes = 1, Reads = ops, Activity = AF)  }
}