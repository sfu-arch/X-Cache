import numpy as np
import platform
import dsim


array_size = 100

flatten = lambda l: [item for sublist in l for item in sublist]

def initData(val, array_len):
    data = np.random.randint(val, size = array_len)
    return data

def saxpy(n , a, in_data, out_data):
    for i in range(0, n):
        out_data[i] = (a * in_data[i]) + out_data[i]
    return out_data


if platform.system() == 'Linux':
    hw_lib_path = "./hardware/chisel/build/libhw.so"
elif platform.system() == 'Darwin':
    hw_lib_path = "./hardware/chisel/build/libhw.dylib"

# Input matrix
a = initData(255, array_size)
a_s = dsim.DArray(a, dsim.DArray.DType.DWord)

# Output matrix
b = np.zeros(array_size, dtype = int)
b_s = dsim.DArray(b, dsim.DArray.DType.DWord)

constant_val = 10


events = dsim.sim(ptrs = [a_s, b_s], vars= [array_size, constant_val], debugs=[], numRets=0, numEvents=1, hwlib = hw_lib_path)

correct_res = saxpy(array_size, constant_val, a.tolist(), b.tolist())

print("Cycle: " + str(events[0]))

if list(b_s.getData()) == correct_res:
    print("Input data:")
    print(a)
    print("stencil's output:")
    print(correct_res)
    print("Success!")
else:
    print(a_s.getData())
    print(b_s.getData())
    print("Failed!")
