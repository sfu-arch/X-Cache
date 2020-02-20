import numpy as np
import platform
import dsim

def test04(a, b, n):
    sum = a
    for i in range (0, n):
        sum =  (sum + a) * b
    return sum



if platform.system() == 'Linux':
    hw_lib_path = "./hardware/chisel/build/libhw.so"
elif platform.system() == 'Darwin':
    hw_lib_path = "./hardware/chisel/build/libhw.dylib"

val_a = 5
val_b = 3
val_n = 5

events = dsim.sim(ptrs = [  ], vars= [val_a, val_b, val_n], numRets=1, numEvents=1, hwlib = hw_lib_path)

print("Cycle: " + str(events[0]))

if events[1] == test04(val_a, val_b, val_n):
    print("Success!\nRet: " + str(events[1]))
else:
    print("Failed!\nExpected {0}, but Returned: {1}".format(str(test04(val_a, val_b, val_n)), str(events[1])))
