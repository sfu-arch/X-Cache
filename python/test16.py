import numpy as np
import platform
import dsim

Type = dsim.DArray.DType
data_size = 5

def init():
    init = np.round(np.random.uniform(1 ,10, data_size), 2)
    return init

def test16(a):
    b = np.zeros(data_size)
    for i in range(data_size):
        b[i] = a[i] / 2

    return b



if platform.system() == 'Linux':
    hw_lib_path = "./hardware/chisel/build/libhw.so"
elif platform.system() == 'Darwin':
    hw_lib_path = "./hardware/chisel/build/libhw.dylib"



a = init()
a_s = dsim.DArray(a, Type.Double)
b_s = dsim.DArray(np.zeros(data_size), Type.Double)


events = dsim.sim(ptrs = [a_s, b_s], vars= [data_size], debugs=[], numRets=0, numEvents=1, hwlib = hw_lib_path)


print("Cycle: " + str(events[0]))
res = test16(a)
if list(b_s.getData_Double()) == list(res):
    print("Success!\nRet: ")
    print(b_s.getData_Double())
else:
    print("Failed!\nRet: ")
    print(b_s.getData_Double())
    print(" --- Expected: " + str(res))
    print(res)

