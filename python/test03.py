import numpy as np
import platform
import dsim

def test03(a, b):
    if a > b:
        a -= b;
    else:
        b -= a;
    return a * b



if platform.system() == 'Linux':
    hw_lib_path = "./hardware/chisel/build/libhw.so"
elif platform.system() == 'Darwin':
    hw_lib_path = "./hardware/chisel/build/libhw.dylib"

val_a = 50
val_b = 5

events = dsim.sim(ptrs = [  ], vars= [val_a, val_b], numRets=1, numEvents=1, hwlib = hw_lib_path)

print("Cycle: " + str(events[0]))

if events[1] == test03(val_a, val_b):
    print("Success!\nRet: " + str(events[1]))
else:
    print("Failed!\nExpected {0}, but Returned: {1}".format(str(test03(val_a, val_b)), str(events[1])))
