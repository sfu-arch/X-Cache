import numpy as np
import platform
import dsim

Type = dsim.DArray.DType
data_size = 5

def test12(a, b, c):
    sum = 0
    if (a / 2) == 4:
        sum = a + b + c
        if ( a / 3 ) == 1:
            sum = a + b + sum * c
        else:
            sum = a * b + sum * c
    else:
        sum = a - b + c
        if (a / 5) == 0:
            sum = a - b * sum + c
        else:
            sum = a * 12 * sum + c

    return sum



if platform.system() == 'Linux':
    hw_lib_path = "./hardware/chisel/build/libhw.so"
elif platform.system() == 'Darwin':
    hw_lib_path = "./hardware/chisel/build/libhw.dylib"


a = 8
b = 3
c = 6

events = dsim.sim(ptrs = [  ], vars= [a,b,c], debugs=[], numRets=1, numEvents=1, hwlib = hw_lib_path)


print("Cycle: " + str(events[0]))
res = test12(a,b,c)
if events[1] == res:
    print("Success!\nRet: " + str(events[1]))
else:
    print("Failed!\nRet: " + str(events[1]) + " --- Expected: " + str(res))

