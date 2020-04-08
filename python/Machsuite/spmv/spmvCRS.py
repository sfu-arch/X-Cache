import os, sys
sys.path.append(os.path.dirname(os.path.dirname(os.path.dirname(os.path.abspath(__file__)))))

from math import isclose
import common as cm
import numpy as np
import platform
import dsim

input_data = cm.read_file('input.data')
check_data = cm.read_file('check.data')

"""
The implementaiton is taken from Machsuite benchmark
"""

NNZ = 1666
N = 494

def spmvCRS(val, cols, rowDelimiters, vec):
    out = [0] * N
    for i in range(0, N):
        sum = 0
        Si = 0
        tmp_begin = rowDelimiters[i]
        tmp_end = rowDelimiters[i+1]
        for j in range(tmp_begin, tmp_end):
            Si = val[j] * vec[cols[j]]
            sum = sum + Si
        out[i] = sum

    return out

res = spmvCRS(input_data[0], input_data[1], input_data[2], input_data[3])




if len(res) == sum([1 for i, j in zip(res, check_data[0]) if isclose(i,j)]):
    print("Sucess!")
else:
    print("Failed!")


# if platform.system() == 'Linux':
#     hw_lib_path = "./hardware/chisel/build/libhw.so"
# elif platform.system() == 'Darwin':
#     hw_lib_path = "./hardware/chisel/build/libhw.dylib"
# 
# # Input matrix
# a = initData(255, array_size)
# a_s = dsim.DArray(a, dsim.DArray.DType.DWord)
# 
# # Output matrix
# b = np.zeros(array_size, dtype = int)
# b_s = dsim.DArray(b, dsim.DArray.DType.DWord)
# 
# constant_val = 10
# 
# 
# events = dsim.sim(ptrs = [a_s, b_s], vars= [array_size, constant_val], debugs=[], numRets=0, numEvents=1, hwlib = hw_lib_path)
# 
# correct_res = saxpy(array_size, constant_val, a.tolist(), b.tolist())
# 
# print("Cycle: " + str(events[0]))
# 
# if list(b_s.getData()) == correct_res:
#     print("Input data:")
#     print(a)
#     print("stencil's output:")
#     print(correct_res)
#     print("Success!")
# else:
#     print(a_s.getData())
#     print(b_s.getData())
#     print("Failed!")
