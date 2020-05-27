import numpy as np
import platform
import dsim

Type = dsim.DArray.DType
data_size = 5

def test15(a, b, c):
    for i in range(data_size):
        c[i] = a[i] * b[i]

    sum = 0
    for i in range(data_size):
        sum += c[i]

    return sum



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

events = dsim.sim(ptrs = [a_s, b_s, c_s], vars= [], debugs=[], numRets=1, numEvents=1, hwlib = hw_lib_path)

print(a)
print(b)
print(c)

print(a_s.getData_UInt64())
print(b_s.getData_UInt64())
print(c_s.getData_UInt64())

print("Cycle: " + str(events[0]))

if events[1] == test15(a, b, c):
    print("Success!\nRet: " + str(events[1]))
else:
    print("Failed!\nRet: " + str(events[1]) + " --- Expected: " + str(test15(a, b, c)))

