import numpy as np
import platform
import dsim

def test05(a):
    a_len = 10
    for i in range(0, a_len):
        a[i] = 2 * a[i]
    return a



if platform.system() == 'Linux':
    hw_lib_path = "./hardware/chisel/build/libhw.so"
elif platform.system() == 'Darwin':
    hw_lib_path = "./hardware/chisel/build/libhw.dylib"

val_a = list(range(1, 11))
print(val_a)
a_s = dsim.DArray(val_a)

events = dsim.sim(ptrs = [a_s], vars= [], numRets=0, numEvents=1, hwlib = hw_lib_path)

print("Cycle: " + str(events[0]))
print("Output array:\n\t")
print(a_s.getData())
print(test05(val_a))
