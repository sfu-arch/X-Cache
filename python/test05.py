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
a_s = dsim.DArray(val_a, dsim.DArray.DType.UInt64)


# Debug container
b_s = dsim.DArray(np.zeros(20), dsim.DArray.DType.UInt64)
c_s = dsim.DArray(np.zeros(30), dsim.DArray.DType.UInt64)
d_s = dsim.DArray(np.zeros(30), dsim.DArray.DType.UInt64)

events = dsim.sim(ptrs = [a_s], debugs=[b_s, c_s, d_s], vars= [], numRets=1, numEvents=1, hwlib = hw_lib_path)

print("Cycle: " + str(events[0]))
print("Output array:\t")
print(list(a_s.getData_UInt64()))
print(test05(val_a))

print("Debug output:")
print([ hex(int(x) & ((1 << 64) - 1)) for x in b_s.getData_UInt64()])
print([ hex(int(x) & ((1 << 64) - 1)) for x in c_s.getData_UInt64()])
print([ hex(int(x) & ((1 << 64) - 1)) for x in d_s.getData_UInt64()])
