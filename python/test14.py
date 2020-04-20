import numpy as np
import platform
import dsim

Type = dsim.DArray.DType

def test14(a, N):
    for i in range(0,N):
        a[i] = a[i] * N

    return a

# a = np.array([1, 2, 3, 4, 5])

N = 5

a_s = dsim.DArray(np.arange(N), Type.UInt64)

if platform.system() == 'Linux':
    hw_lib_path = "./hardware/chisel/build/libhw.so"
elif platform.system() == 'Darwin':
    hw_lib_path = "./hardware/chisel/build/libhw.dylib"


events = dsim.sim(ptrs = [a_s], debugs = [], vars= [N], numRets=0, numEvents=1, hwlib = hw_lib_path)

# print("Cycle: " + str(cycle))
print("Cycle: " + str(events[0]))
print(test14(np.arange(N), N))
print(a_s.getData_UInt64())
