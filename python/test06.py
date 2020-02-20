import numpy as np
import platform
import dsim

def test06(a, b):
    i = 0
    while i < a:
        if a > b:
            a -= b
        else:
            b -= a
        i += 1

    return a * b



if platform.system() == 'Linux':
    hw_lib_path = "./hardware/chisel/build/libhw.so"
elif platform.system() == 'Darwin':
    hw_lib_path = "./hardware/chisel/build/libhw.dylib"

val_a = 50
val_b = 5

events = dsim.sim(ptrs = [  ], vars= [val_a, val_b], numRets=1, numEvents=1, hwlib = hw_lib_path)

print("Cycle: " + str(events[0]))

if events[1] == test06(val_a, val_b):
    print("Success!\nRet: " + str(events[1]))
else:
    print("Failed!\nExpected {0}, but Returned: {1}".format(str(test06(val_a, val_b)), str(events[1])))
