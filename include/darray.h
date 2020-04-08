#pragma once

#include <algorithm>
#include <experimental/iterator>
#include <fstream>
#include <iterator>
#include <sstream>
#include <streambuf>
#include <string>
#include <vector>

#include "dlpack/dlpack.h"

#include <pybind11/numpy.h>
#include <pybind11/pybind11.h>
#include <pybind11/stl.h>

namespace py = pybind11;

namespace driver {

/**
 * Tokenizing input data */
std::vector<std::string> split(const std::string &s, char delimiter) {
    std::vector<std::string> tokens;
    std::string token;
    std::istringstream tokenStream(s);
    while (std::getline(tokenStream, token, delimiter)) {
        tokens.push_back(token);
    }
    return tokens;
}

std::string print_vector(std::vector<uint64_t> const &input, std::string const dl = " ") {
    std::stringstream str_out;
    std::copy(input.begin(), input.end(),
              std::experimental::make_ostream_joiner(str_out, dl));
    return str_out.str();
}

/**
 * Dandelion array to keep data
 */
class DArray {
   private:
    DLTensor array;

   public:

    enum DType{
        UInt8 = 0,
        UInt16,
        UInt32,
        UInt64,
        Float,
        Double
    };
    DArray(uint64_t shapes, DType type) {
        array.shape = (int64_t *)malloc(sizeof(int64_t));

        array.shape[0] = shapes;

        // Default values
        array.dtype.code = 0;
        array.dtype.lanes = 1;
        switch(type){
            case DType::UInt8:
                array.dtype.bits = 8;
            case DType::UInt16:
                array.dtype.bits = 16;
            case DType::UInt32:
                array.dtype.bits = 32;
            case DType::UInt64:
                array.dtype.bits = 64;
            case DType::Float:
                array.dtype.bits = 32;
            case DType::Double:
                array.dtype.bits = 64;
        };


        array.ndim = 1;

        array.strides = nullptr;

        array.data = malloc(sizeof(uint64_t *) * array.shape[0]);
    }
    template <typename T>
    DArray(py::array_t<T, py::array::c_style | py::array::forcecast>
               in_data, DType type) {
        std::vector<T> shapes(in_data.size());
        std::memcpy(shapes.data(), in_data.data(),
                    in_data.size() * sizeof(T));

        array.shape = (int64_t *)malloc(sizeof(T));

        array.shape[0] = shapes.size();

        // Default values
        array.dtype.code = 0;
    switch(type){
            case DType::UInt8:
                array.dtype.bits = 8;
            case DType::UInt16:
                array.dtype.bits = 16;
            case DType::UInt32:
                array.dtype.bits = 32;
            case DType::UInt64:
                array.dtype.bits = 64;
            case DType::Float:
                array.dtype.bits = 32;
            case DType::Double:
                array.dtype.bits = 64;
        };


        array.dtype.lanes = 1;

        array.ndim = 1;

        array.strides = nullptr;

        array.data = malloc(sizeof(T *) * shapes.size());

        switch (type){
            case DType::UInt8:
                for (auto i = 0; i < in_data.size(); ++i) {
                    *(((uint8_t *)(array.data)) + i * sizeof(uint8_t)) = shapes[i];
                }
                break;
            case DType::UInt16:
                for (auto i = 0; i < in_data.size(); ++i) {
                    *(((uint16_t *)(array.data)) + i * sizeof(uint8_t)) = shapes[i];
                }
                break;
            case DType::UInt32:
                for (auto i = 0; i < in_data.size(); ++i) {
                    *(((uint32_t *)(array.data)) + i * sizeof(uint8_t)) = shapes[i];
                }
                break;
            case DType::UInt64:
                for (auto i = 0; i < in_data.size(); ++i) {
                    *(((uint64_t *)(array.data)) + i * sizeof(uint8_t)) = shapes[i];
                }
                break;
            case DType::Float:
                for (auto i = 0; i < in_data.size(); ++i) {
                    *(((float *)(array.data)) + i * sizeof(uint8_t)) = shapes[i];
                }
                break;
            case DType::Double:
                for (auto i = 0; i < in_data.size(); ++i) {
                    *(((double *)(array.data)) + i * sizeof(uint8_t)) = shapes[i];
                }
                break;
        };
    }

    ~DArray() {
        free(array.shape);
        free(array.data);
    }

    uint64_t getDimenssion() const { return this->array.ndim; }
    std::vector<uint64_t> getShapes() const {
        std::vector<uint64_t> shapes;
        for (int i = 0; i < this->array.ndim; ++i) {
            shapes.push_back(this->array.shape[i]);
        }
        return shapes;
    }

    void initData(
        py::array_t<int64_t, py::array::c_style | py::array::forcecast>
            in_data) {
        // allocate std::vector (to pass to the C++ function)
        std::vector<int64_t> array_vec(in_data.size());
        std::memcpy(array_vec.data(), in_data.data(),
                    in_data.size() * sizeof(int64_t));

        for (auto i = 0; i < in_data.size(); ++i) {
            *(((uint64_t *)(array.data)) + i * sizeof(char)) = array_vec[i];
        }

        for (auto index = 0; index < array.shape[0]; index++) {
            uint64_t k = *(((uint64_t *)(array.data)) + index * sizeof(char));
            std::cout << (int)k << " , ";
        }
        std::cout << "\n";
    }

    const DLTensor &getArray() { return array; }

    std::string print_array() const {
        std::vector<uint64_t> values;
        for (auto index = 0; index < this->array.shape[0]; index++) {
            uint64_t k =
                *(((uint64_t *)(this->array.data)) + index * sizeof(uint8_t));
            values.push_back(k);
        }

        return print_vector(values, ", ");
    }

    template <typename T>
    py::array_t<T> getData() {
        py::array_t<T> ret_data(this->array.shape[0]);
        auto buff = ret_data.request();
        T *buff_ptr = (T *)buff.ptr;
        for (auto index = 0; index < this->array.shape[0]; index++) {
            T k =
                *(((T *)(this->array.data)) + index * sizeof(uint8_t));
            buff_ptr[index] = k;
        }

        return ret_data;
    }
};

}  // namespace driver

