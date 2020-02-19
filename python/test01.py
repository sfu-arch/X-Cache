import numpy as np
import platform
import dsim

def test01(a, b):
    return a * b


if platform.system() == 'Linux':
    hw_lib_path = "./hardware/chisel/build/libhw.so"
elif platform.system() == 'Darwin':
    hw_lib_path = "./hardware/chisel/build/libhw.dylib"


events = dsim.sim(ptrs = [  ], vars= [5, 3], numRets=1, numEvents=1, hwlib = hw_lib_path)

print("Cycle: " + str(events[0]))

if events[1] == test01(5,3):
    print("Success!\nRet: " + str(events[1]))
else:
    print("Failed!\nRet: " + str(events[1]))
