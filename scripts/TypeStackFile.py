import zipfile
import os, errno
import shutil
import csv
import json
import binascii
import codecs
import sqlite3
import datetime
import sys
import collections
from getpass import getpass
import argparse
from collections import OrderedDict
from stat import S_ISREG, ST_CTIME, ST_MODE
from glob import glob
import ntpath
from shutil import copyfile

# Parameters
OPSize = [2,8,16,32]


# Directory set up
BASEDIR = "/home/amiralis/git/dataflow-lib"
TARGETDIR = "/tmp/build-stuff/"
RESULT = "Out"
OUTDIR = "./Output16b/"

DESIGN =  "verilogmain.TypeStackFileVerilog16b" 

SBTCMD = "sbt \"test:run-main " + DESIGN + " -td "+TARGETDIR+" -tbn verilator -tts 1"

def parsenode(name,node):
	VCDSPLIT = os.path.splitext(name)
	return VCDSPLIT[0]+"_"+"_".join(str(x) for x in node)+VCDSPLIT[1]

def doubling_range(start, stop):
    while start <= stop:
        yield start
        start <<= 1


if __name__ == '__main__':
	print(DESIGN)
	os.popen('rm -rf '+TARGETDIR+"*")
	print("Running Build Size...")
#	print("cd " + BASEDIR + " && exec " + SBTCMD)
	#os.popen("cd " + BASEDIR + " && exec " + SBTCMD)
	# copyfile(src, dst)

	
	for ops in OPSize:
		for BaseSize in doubling_range(2, ops):
			for TableSize in doubling_range(2, ops):
				for AF in ("low","med","high"):
					Output = TARGETDIR + "/" + RESULT + "_"+str(ops)+"_"+str(BaseSize)+"_"+str(TableSize)+"_"+str(AF)
					params = " --OpSize " + str(ops) + " --BaseSize " + str(BaseSize) + " --EntrySize " + str(TableSize) + " --AF " + str(AF)
					EXE = SBTCMD + params + "\" > " + Output
					
					print(params)
                                        print(EXE)
					os.popen("cd " + BASEDIR + " && exec " + EXE)
					TOPVCD     = [y for x in sorted(os.walk(TARGETDIR)) for y in glob(os.path.join(x[0], '*.vcd'))]
					TOPVERILOG = [y for x in sorted(os.walk(TARGETDIR)) for y in glob(os.path.join(x[0], '*.v'))]
					OUTVCD     = OUTDIR+parsenode(os.path.basename(TOPVCD[-1]),[str(ops),str(BaseSize),str(TableSize),str(AF)])
					OUTVERILOG = OUTDIR+parsenode(os.path.basename(TOPVERILOG[-1]),[str(ops),str(BaseSize),str(TableSize),str(AF)])

					copyfile(TOPVCD[-1],OUTVCD)
					copyfile(TOPVERILOG[-1],OUTVERILOG)
					copyfile(Output,OUTDIR + "/" + RESULT + "_"+str(ops)+"_"+str(BaseSize)+"_"+str(TableSize)+"_"+str(AF))
					# print(TOPVCD)
					# print(OUTVCD)
					# print(OUTVERILOG)

