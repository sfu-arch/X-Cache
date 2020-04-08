import numpy as np
import platform
import dsim


def initData(val, array_len):
    data = np.random.randint(val, size = array_len)
    return data


def conv2d(mat, res, coeffs, W, H, K, scf):
    R = K >> 1
    index = R * W
    for j in range(R, H - R):
        for i in range(R, W - R):
            index2 = index - (R * W)
            c = 0
            val = 0
            for y in range (0, (2 * R) + 1):
                for x in range (0, (2 * R) + 1):
                    val += coeffs[c] * mat[index2 + i + (x - 2)]
                    c += 1
                index2 += W
            res[index + i] = val >> scf
        index += W
    return res


if platform.system() == 'Linux':
    hw_lib_path = "./hardware/chisel/build/libhw.so"
elif platform.system() == 'Darwin':
    hw_lib_path = "./hardware/chisel/build/libhw.dylib"



IMG_SIZE=10

img_in = range(0, IMG_SIZE * IMG_SIZE)
coeffs = [
        1, 4, 6, 4, 1,
        4,16,24,16, 4,
        6,24,36,24, 6,
        4,16,24,16, 4,
        1, 4, 6, 4, 1 ]
img_out = np.zeros(IMG_SIZE * IMG_SIZE, dtype = int)


# Input matrix
img_in_s = dsim.DArray(img_in, dsim.DArray.DType.UInt64)

# Coeffs
coeffs_s = dsim.DArray(coeffs, dsim.DArray.DType.UInt64)

# Output matrix
img_out_s = dsim.DArray(img_out, dsim.DArray.DType.UInt64)


events = dsim.sim(ptrs = [img_in_s, img_out_s, coeffs_s], vars= [IMG_SIZE, IMG_SIZE, 5, 8], debugs=[], numRets=0, numEvents=1, hwlib = hw_lib_path)

# Running software version
res = conv2d(img_in, img_out.tolist(), coeffs, IMG_SIZE, IMG_SIZE, 5, 8)


print("Cycle: " + str(events[0]))
if list(img_out_s.getData_UInt64()) == res:
    print("Input data:")
    print([*img_in])
    print("stencil's output:")
    print(res)
    print("Success!")
else:
    print(a_s.getData_UInt64())
    print(b_s.getData_UInt64())
    print("Failed!")
