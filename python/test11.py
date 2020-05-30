import numpy as np
import platform
import dsim

Type = dsim.DArray.DType
data_size = 5

def test11_inner(a):
    sum = a
    for i in range(data_size):
        sum += 1
    return sum

def test11(a):
    sum = a
    for i in range(data_size):
        sum += test11_inner(sum)
    return sum



if platform.system() == 'Linux':
    hw_lib_path = "./hardware/chisel/build/libhw.so"
elif platform.system() == 'Darwin':
    hw_lib_path = "./hardware/chisel/build/libhw.dylib"



# a = np.random.randint(10, size = data_size)
# b = np.random.randint(10, size = data_size)
# c = np.zeros(data_size)

# a_s = dsim.DArray(a, Type.UInt64)
# b_s = dsim.DArray(b, Type.UInt64)
# c_s = dsim.DArray(c, Type.UInt64)

events = dsim.sim(ptrs = [  ], vars= [5], debugs=[], numRets=1, numEvents=1, hwlib = hw_lib_path)


print("Cycle: " + str(events[0]))
res = test11(5)
if events[1] == res:
    print("Success!\nRet: " + str(events[1]))
else:
    print("Failed!\nRet: " + str(events[1]) + " --- Expected: " + str(res))

