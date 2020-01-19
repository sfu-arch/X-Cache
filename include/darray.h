#pragma once

#include <algorithm>
#include <experimental/iterator>
#include <fstream>
#include <iterator>
#include <sstream>
#include <streambuf>
#include <string>
#include <vector>

#include <pybind11/numpy.h>
#include <pybind11/pybind11.h>
#include <pybind11/stl.h>

namespace py = pybind11;

// DATA SIZES
#define ELEMENT_SIZE_BITS 64

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

std::string print_vector(std::vector<int64_t> const &input, std::string const dl = " ") {
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
    DArray(uint64_t shapes) {
        array.shape = (int64_t *)malloc(sizeof(int64_t));

        array.shape[0] = shapes;

        // Default values
        // TODO: replace default values with defined datatypes
        array.dtype.code = 0;
        array.dtype.bits = ELEMENT_SIZE_BITS;
        array.dtype.lanes = 1;

        array.ndim = 1;

        array.strides = nullptr;

        array.data = malloc(sizeof(uint64_t *) * array.shape[0]);
    }
    DArray(py::array_t<int64_t, py::array::c_style | py::array::forcecast>
               in_data) {
        std::vector<int64_t> shapes(in_data.size());
        std::memcpy(shapes.data(), in_data.data(),
                    in_data.size() * sizeof(int64_t));

        array.shape = (int64_t *)malloc(sizeof(int64_t));

        array.shape[0] = shapes.size();

        // Default values
        array.dtype.code = 0;
        array.dtype.bits = ELEMENT_SIZE_BITS;
        array.dtype.lanes = 1;

        array.ndim = 1;

        array.strides = nullptr;

        array.data = malloc(sizeof(uint64_t *) * shapes.size());

        for (auto i = 0; i < in_data.size(); ++i) {
            *(((uint64_t *)(array.data)) + i * sizeof(char)) = shapes[i];
        }
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
        std::vector<int64_t> values;
        for (auto index = 0; index < this->array.shape[0]; index++) {
            int64_t k =
                *(((uint64_t *)(this->array.data)) + index * sizeof(char));
            values.push_back(k);
        }

        return print_vector(values, ", ");
    }

    py::array_t<int64_t> getData() {
        py::array_t<int64_t> ret_data(this->array.shape[0]);
        auto buff = ret_data.request();
        int64_t *buff_ptr = (int64_t *)buff.ptr;
        for (auto index = 0; index < this->array.shape[0]; index++) {
            int64_t k =
                *(((uint64_t *)(this->array.data)) + index * sizeof(uint8_t));
            buff_ptr[index] = k;
        }

        return ret_data;
    }
};

}  // namespace driver

