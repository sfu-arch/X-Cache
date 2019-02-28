#!/usr/local/bin/python3
import re
import pprint

#string = "class HandShakingIONPS[T <: Data](val NumOuts: Int, val NumIns: Int)(gen: T)(implicit p: Parameters)"
string = """ class NParallelCacheIO(val NumTiles: Int = 1, val NumBanks: Int = 1)(implicit val p: Parameters)
  extends Module with CoreParams with UniformPrintfs {
 """
string = string.replace('\n', '')

print(string)


def parseString():
    classname = re.search("class(.+?)[\[, \(]", string)
    params = re.findall("\((.+?)\)", string)
    decl = ""
    for all in params:
        variablenames = re.findall("val(.+?):", all)
        if "implicit" in all:
            continue
        if variablenames:
            decl = decl + \
                ('(' + ','.join(list(map(str.strip, variablenames))) + ')')
            continue
        if "gen" in all:
            decl = decl + ('(gen)')
            continue
    print("override def cloneType = new" +
          classname.group(1) + decl + ".asInstanceOf[this.type]")


if __name__ == "__main__":
    parseString()
