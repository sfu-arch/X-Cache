import numpy as np
import platform
import dsim

def test05(a):
    a_len = 10
    for i in range(0, a_len/2):
        a[i + a_len/2] = 2 * a[i]
    return a[9]



if platform.system() == 'Linux':
    hw_lib_path = "./hardware/chisel/build/libhw.so"
elif platform.system() == 'Darwin':
    hw_lib_path = "./hardware/chisel/build/libhw.dylib"

val_a = range(1, 11)
a_s = dsim.DArray(val_a)

events = dsim.sim(ptrs = [a_s], vars= [], numRets=0, numEvents=1, hwlib = hw_lib_path)

print("Cycle: " + str(events[0]))
print("Output array:\n\t")
print(a_s.getData())

# if events[1] == test05(val_a):
    # print("Success!\nRet: " + str(events[1]))
    # print("Output array:\n\t")
    # print(a_s.getData())
# else:
    # print("Failed!\nExpected {0}, but Returned: {1}".format(str(test05(val_a)), str(events[1])))
