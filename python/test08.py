import numpy as np
import platform
import dsim

def test08(a, n):
    i = 0
    j = 0
    k = 0
    result = 0
    while i < 3:
        while j < n:
            while k < n:
                a[k] = 2 * a[k]
                k += 1
            a[n - 1] += 1
            j += 1
        result += a[n - 1]
        i += 1

    result = result / 2
    return result



if platform.system() == 'Linux':
    hw_lib_path = "./hardware/chisel/build/libhw.so"
elif platform.system() == 'Darwin':
    hw_lib_path = "./hardware/chisel/build/libhw.dylib"

val_a = list(range(0, 8))
a_s = dsim.DArray(val_a, dsim.DArray.DType.DWord)
val_n = 8

events = dsim.sim(ptrs = [a_s], vars= [val_n], numRets=1, numEvents=1, hwlib = hw_lib_path)

print("Cycle: " + str(events[0]))
print("Ret: " + str(events[1]))
print(a_s.getData())
print(test08(val_a, val_n))
