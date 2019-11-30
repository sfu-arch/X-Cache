/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

#include "DPImodule.h"
#include "dlpack/dlpack.h"
#include "runtime/module.h"
#include "runtime/registry.h"

#include "virtual_memory.h"

#include <pybind11/numpy.h>
#include <pybind11/pybind11.h>
#include <pybind11/stl.h>

#include <algorithm>
#include <experimental/iterator>
#include <fstream>
#include <iterator>
#include <sstream>
#include <streambuf>
#include <string>
#include <vector>

#define DEBUG(x)                                   \
    do {                                           \
        std::cerr << #x << ": " << x << std::endl; \
    } while (0)

using namespace std;
namespace py = pybind11;

/**
 * Tokenizing input data
 */
vector<string> split(const string &s, char delimiter) {
    vector<string> tokens;
    string token;
    istringstream tokenStream(s);
    while (std::getline(tokenStream, token, delimiter)) {
        tokens.push_back(token);
    }
    return tokens;
}

string print_vector(std::vector<int64_t> const &input, string const dl = " ") {
    std::stringstream str_out;
    std::copy(input.begin(), input.end(),
              std::experimental::make_ostream_joiner(str_out, dl));
    return str_out.str();
}

// DATA SIZES
#define ELEMENT_SIZE_BITS 32
#define DLTensor_DATA_TYPE int64_t

namespace driver {

using tvm::runtime::Module;
using vta::dpi::DPIModule;
using vta::dpi::DPIModuleNode;

class DArray {
   private:
    DLTensor array;

   public:
    DArray(uint64_t shapes) {
        array.shape = new DLTensor_DATA_TYPE[shapes];

        array.shape[0] = shapes;

        // Default values
        // TODO: replace default values with defined datatypes
        array.dtype.code = 0;
        array.dtype.bits = ELEMENT_SIZE_BITS;
        array.dtype.lanes = 1;

        array.ndim = 1;

        array.strides = nullptr;

        array.data = malloc(sizeof(void *) * array.shape[0]);
    }
    DArray(py::array_t<int64_t, py::array::c_style | py::array::forcecast>
               in_data) {
        std::vector<int64_t> shapes(in_data.size());
        std::memcpy(shapes.data(), in_data.data(),
                    in_data.size() * sizeof(int64_t));

        array.shape = new DLTensor_DATA_TYPE[shapes.size()];

        array.shape[0] = shapes.size();

        // Default values
        array.dtype.code = 0;
        array.dtype.bits = ELEMENT_SIZE_BITS;
        array.dtype.lanes = 1;

        array.ndim = shapes.size();

        array.strides = nullptr;

        array.data = malloc(sizeof(void *) * shapes.size());

        for (auto i = 0; i < in_data.size(); ++i) {
            *(((uint8_t *)(array.data)) + i * sizeof(DLTensor_DATA_TYPE)) =
                shapes[i];
        }
    }

    ~DArray() {
        free(array.shape);
        free(array.data);
    }

    uint64_t getDimenssion() const { return this->array.ndim; }
    vector<uint64_t> getShapes() const {
        vector<uint64_t> shapes;
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
            *(((uint8_t *)(array.data)) + i * sizeof(DLTensor_DATA_TYPE)) =
                array_vec[i];
        }
    }

    const DLTensor &getArray() { return array; }

    string print_array() const {
        std::vector<int64_t> values;
        for (auto index = 0; index < this->array.shape[0]; index++) {
            int64_t k =
                *(((uint8_t *)(this->array.data)) + index * sizeof(uint8_t));
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
                *(((uint8_t *)(this->array.data)) + index * sizeof(uint8_t));
            buff_ptr[index] = k;
        }

        return ret_data;
    }
};

class DPILoader {
   public:
    ~DPILoader() {
        dpi_->SimResume();
        dpi_->SimFinish();
    }

    void Init(Module module) {
        mod_ = module;
        dpi_ = this->Get();
        dpi_->SimLaunch();
        dpi_->SimWait();
    }

    DPIModuleNode *Get() {
        return static_cast<DPIModuleNode *>(mod_.operator->());
    }

    static DPILoader *Global() {
        static DPILoader inst;
        return &inst;
    }

    // TVM module
    Module mod_;
    // DPI Module
    DPIModuleNode *dpi_{nullptr};
};

class Device {
   public:
    Device(uint64_t npatrs, uint64_t nargs) {
        ptrs_ = std::vector<void *>(npatrs, nullptr);
        args_ = std::vector<uint64_t>(nargs, 0);

        loader_ = DPILoader::Global();
    }

    uint32_t Run(std::vector<std::reference_wrapper<DArray>> &ptrs,
                 vector<int64_t> &arguments) {
        if (ptrs.size() != this->ptrs_.size())
            throw std::runtime_error("Number of PTRS must be one");

        if (arguments.size() != this->args_.size())
            throw std::runtime_error("Number of ARGS must be one");

        uint32_t cycles;

        for (uint64_t ind = 0; ind < ptrs.size(); ++ind) {
            size_t a_size = (ptrs[ind].get().getArray().dtype.bits >> 3) *
                            ptrs[ind].get().getArray().shape[0];
            ptrs_[ind] = this->MemAlloc(a_size);
            this->MemCopyFromHost(ptrs_[ind], ptrs[ind].get().getArray().data,
                                  ptrs[ind].get().getArray().shape[0]);
        }

        this->Init();

        this->Launch(arguments);

        cycles = this->WaitForCompletion();

        for (uint64_t ind = 0; ind < ptrs.size(); ++ind) {
            size_t a_size = (ptrs[ind].get().getArray().dtype.bits >> 3) *
                            ptrs[ind].get().getArray().shape[0];
            this->MemCopyToHost(ptrs[ind].get().getArray().data, ptrs_[ind],
                                a_size);
            this->MemFree(ptrs_[ind]);
        }

        return cycles;
    }

   public:
    void Init() {
        dpi_ = loader_->Get();
        dpi_->SimResume();
    }

    void *MemAlloc(size_t size) {
        void *addr = vta::vmem::VirtualMemoryManager::Global()->Alloc(size);
        return reinterpret_cast<void *>(
            vta::vmem::VirtualMemoryManager::Global()->GetPhyAddr(addr));
    }

    void MemFree(void *buf) {
        void *addr = vta::vmem::VirtualMemoryManager::Global()->GetAddr(
            reinterpret_cast<uint64_t>(buf));
        vta::vmem::VirtualMemoryManager::Global()->Free(addr);
    }

    vta_phy_addr_t MemGetPhyAddr(void *buf) {
        return reinterpret_cast<uint64_t>(reinterpret_cast<uint64_t *>(buf));
    }

    void MemCopyFromHost(void *dst, const void *src, size_t size) {
        vta::vmem::VirtualMemoryManager::Global()->MemCopyFromHost(dst, src,
                                                                   size);
    }

    void MemCopyToHost(void *dst, const void *src, size_t size) {
        vta::vmem::VirtualMemoryManager::Global()->MemCopyToHost(dst, src,
                                                                 size);
    }

    void Launch(vector<int64_t> &arguments) {
        int32_t address = 0x8;
        int32_t cnt = 0;
        for (auto arg : arguments) {
            dpi_->WriteReg(address + (cnt * 4), arg);  // arg
            cnt++;
        }

        for (auto ptr : ptrs_) {
            DEBUG("ptr: " << this->MemGetPhyAddr(ptr));
            dpi_->WriteReg(address + ((cnt + 0) * 4),
                           this->MemGetPhyAddr(ptr));  // ptr(0)

            cnt++;
        }

        dpi_->WriteReg(0x00, 0x1);  // launch
    }

    uint32_t WaitForCompletion() {
        uint32_t i, val;
        for (i = 0; i < wait_cycles_; i++) {
            val = dpi_->ReadReg(0x00);
            if (val == 2) break;  // finish
        }
        // Read event counter
        val = dpi_->ReadReg(0x04);
        dpi_->SimWait();
        return val;
    }

    // wait cycles
    uint32_t wait_cycles_{100000000};
    // DPI loader
    DPILoader *loader_{nullptr};
    // DPI Module
    DPIModuleNode *dpi_{nullptr};

    std::vector<void *> ptrs_;
    std::vector<uint64_t> args_;
    std::vector<uint64_t> events_;
};

uint64_t RunSim(std::vector<std::reference_wrapper<DArray>> in_ptrs,
                std::vector<int64_t> vars, std::string hw_lib) {
    std::cout << "Stating Dsim..." << std::endl;

    std::shared_ptr<vta::dpi::DPIModule> n =
        std::make_shared<vta::dpi::DPIModule>();

    if (hw_lib.empty())
        throw std::runtime_error("Hardware library path can not be empty!");
    else {
        ifstream ifile(hw_lib);
        if (!ifile)
            throw std::runtime_error(
                "Hardware library doesn't exist make sure the path is "
                "correct!");
        n->Init(hw_lib);
    }

    tvm::runtime::Module mod = tvm::runtime::Module(n);

    // Init the simulator
    driver::Device dev(in_ptrs.size(), vars.size());
    dev.loader_->Init(mod);

    auto cycles = dev.Run(in_ptrs, vars);

    return cycles;
}

}  // namespace driver

/**
 * Pybind11 wrappers for Dandelion-sim
 */
PYBIND11_MODULE(dsim, m) {
    py::class_<driver::DArray>(m, "DArray")
        .def(py::init<
             py::array_t<int64_t, py::array::c_style | py::array::forcecast>>())
        .def("initData", &driver::DArray::initData)
        .def("getData", &driver::DArray::getData)
        .def("__repr__", [](const driver::DArray &a) {
            return "<DArray ['" + std::to_string(a.getDimenssion()) + "']>: [" +
                   a.print_array() + "]";
        });

    m.def("sim", &driver::RunSim, "A function to run DSIM", py::arg("ptrs"),
          py::arg("vars"), py::arg("hwlib"));
}
