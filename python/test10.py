import numpy as np
import platform
import dsim

Type = dsim.DArray.DType
data_size = 5

def test10(a, b, c):
    for i in range(data_size):
        c[i] = a[i] + b[i]

    return c



if platform.system() == 'Linux':
    hw_lib_path = "./hardware/chisel/build/libhw.so"
elif platform.system() == 'Darwin':
    hw_lib_path = "./hardware/chisel/build/libhw.dylib"



a = np.random.randint(10, size = data_size)
b = np.random.randint(10, size = data_size)
c = np.zeros(data_size)

a_s = dsim.DArray(a, Type.UInt64)
b_s = dsim.DArray(b, Type.UInt64)
c_s = dsim.DArray(c, Type.UInt64)

events = dsim.sim(ptrs = [a_s, b_s, c_s], vars= [], debugs=[], numRets=0, numEvents=1, hwlib = hw_lib_path)

print(a)
print(b)
print(c)

print(a_s.getData_UInt64())
print(b_s.getData_UInt64())
print(c_s.getData_UInt64())

print("Cycle: " + str(events[0]))
res = test10(a, b, c)
mask = np.logical_and(c_s.getData_UInt64(), res)
if np.bitwise_and.reduce(mask):
    print("Success!\nRet: " + str(c_s.getData_UInt64()))
else:
    print("Failed!\nRet: " + str(c_s.getData_UInt64()) + " --- Expected: " + str(res))

