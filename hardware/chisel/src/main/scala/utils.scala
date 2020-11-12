package dandelion
import scala.io.Source

class GuardReader (fileName:String) {
  val bufferedSource = Source.fromResource("guards/" + fileName)
  val guard_values = (for (line <- bufferedSource.getLines) yield{
    val guards = line.split(",").map(_.trim).toList.map(_.toInt)
    (guards(0), guards.tail)
  }).toMap

  bufferedSource.close
}


object GuardReader{
  def apply(fileName:String) = {
    val greader = new GuardReader(fileName)
    greader.guard_values
  }
}