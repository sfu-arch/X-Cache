import numpy as np
import platform
import dsim

def test02(a, b):
    sum = 0
    if a/2 == 4:
        sum =  a + b;
    return sum;



if platform.system() == 'Linux':
    hw_lib_path = "./hardware/chisel/build/libhw.so"
elif platform.system() == 'Darwin':
    hw_lib_path = "./hardware/chisel/build/libhw.dylib"


events = dsim.sim(ptrs = [  ], vars= [8, 3], numRets=1, numEvents=1, hwlib = hw_lib_path)

print("Cycle: " + str(events[0]))

if events[1] == test02(8,3):
    print("Success!\nRet: " + str(events[1]))
else:
    print("Failed!\nRet: " + str(events[1]))
