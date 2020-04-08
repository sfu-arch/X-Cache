import numpy as np
import platform
import dsim


num_rows = 4
num_cols = 4
num_rrows = 1
num_rcols = 1
filt_rows = (1 + 2 * num_rrows)
filt_cols = (1 + 2 * num_rcols)

flatten = lambda l: [item for sublist in l for item in sublist]

def initData(size_row, size_col):
    data = np.random.randint(10, size = (size_row, size_col))
    return data


def stencil(in_mat, out_mat):
    for pos in range(0, num_rows * num_cols):
        i = pos // num_cols
        j = pos & (num_cols - 1)
        for nr in range(0, 2 * num_rrows + 1):
            for nc in range(0, 2 * num_rcols + 1):
                row = i + nr - num_rrows
                col = j + nc - num_rcols
                if row < num_rows and row >= 0:
                    if col < num_cols and col >= 0:
                        out_mat[i * num_cols + j] = out_mat[i * num_cols + j] + in_mat[row * num_cols + col]

        out_mat[i * num_cols + j] = (out_mat[i * num_cols + j]  + (filt_cols * filt_rows)) // (filt_cols * filt_rows)

    return out_mat


if platform.system() == 'Linux':
    hw_lib_path = "./hardware/chisel/build/libhw.so"
elif platform.system() == 'Darwin':
    hw_lib_path = "./hardware/chisel/build/libhw.dylib"

# Input matrix
a = initData(num_rows, num_cols)
a_s = dsim.DArray(a, dsim.DArray.DType.UInt64)

# Output matrix
b = np.zeros((num_rows, num_cols), dtype = int)
b_s = dsim.DArray(b, dsim.DArray.DType.UInt64)



events = dsim.sim(ptrs = [a_s, b_s], vars= [], debugs=[], numRets=0, numEvents=1, hwlib = hw_lib_path)

# aa = [7 ,1 ,7 ,6 ,2 ,9 ,9 ,8 ,2 ,9 ,8 ,7 ,2 ,9 ,0 ,2]
# bb = [0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0]
# correct_res = stencil(aa, bb)
# print(bb)
correct_res = stencil(flatten(a.tolist()), flatten(b.tolist()))

print("Cycle: " + str(events[0]))
if list(b_s.getData_UInt64()) == correct_res:
    print("Input mat:")
    print(a)
    print("stencil's output:")
    print(correct_res)
    print("Success!")
else:
    print(a_s.getData_UInt64())
    print(b_s.getData_UInt64())
    print("Failed!")
