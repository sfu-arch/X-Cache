import numpy as np
import platform
import dsim

def initData(test_size):
    data = np.random.randint(255, size = test_size)
    return [x - 127 for x in data]

def relu(in_mat):
    out_mat = list(map(lambda x : x if x > 0 else 0, in_mat))
    return out_mat



if platform.system() == 'Linux':
    hw_lib_path = "./hardware/chisel/build/libhw.so"
elif platform.system() == 'Darwin':
    hw_lib_path = "./hardware/chisel/build/libhw.dylib"

# Input Val
N = 8

# Input matrix
a = initData(N)
a_s = dsim.DArray(a, dsim.DArray.DType.DWord)

# Output matrix
b = np.zeros(N)
b_s = dsim.DArray(b, dsim.DArray.DType.DWord)


events = dsim.sim(ptrs = [a_s, b_s], vars= [N], debugs=[], numRets=0, numEvents=1, hwlib = hw_lib_path)

print("Cycle: " + str(events[0]))
if list(b_s.getData()) == relu(a):
    print("Input mat:")
    print(a)
    print("Relu's output:")
    print(relu(a))
    print("Success!")
else:
    print("Failed!")