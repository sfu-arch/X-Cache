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

#include <algorithm>
#include <fstream>
#include <iterator>
#include <sstream>
#include <streambuf>
#include <string>
#include <vector>

using namespace std;

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

// DATA SIZES
#define ELEMENT_SIZE_BITS 32
#define ARRAY_DATA_TYPE int64_t

namespace vta {
namespace driver {

using tvm::runtime::Module;
using vta::dpi::DPIModule;
using vta::dpi::DPIModuleNode;

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
    Device() { loader_ = DPILoader::Global(); }

    uint32_t Run(DLTensor *a, DLTensor *b, vector<int32_t>& arguments) {
        uint32_t cycles;
        uint32_t a_len = a->shape[0];
        size_t a_size = (a->dtype.bits >> 3) * a_len;

        uint32_t b_len = b->shape[0];
        size_t b_size = (b->dtype.bits >> 3) * b_len;

        a_ = this->MemAlloc(a_size);
        b_ = this->MemAlloc(b_size);

        this->MemCopyFromHost(a_, a->data, a->shape[0]);
        this->MemCopyFromHost(b_, b->data, b->shape[0]);

        this->Init();

        this->Launch(arguments);

        cycles = this->WaitForCompletion();

        this->MemCopyToHost(b->data, b_, b_size);

        this->MemFree(a_);
        this->MemFree(b_);
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

    void Launch(vector<int32_t> &arguments) {

        int32_t address = 0x8;
        int32_t cnt = 0;
        for(auto arg : arguments){
            dpi_->WriteReg(address + (cnt * 4), arg);  // arg
            cnt++;
        }
        //std::cout << "a: " << this->MemGetPhyAddr(a_);
        dpi_->WriteReg(address + ((cnt + 0) * 4), this->MemGetPhyAddr(a_));  // ptr(0)
        //cnt++;
        std::cout << "b: " << this->MemGetPhyAddr(b_);
        dpi_->WriteReg(address + ((cnt + 1) * 4), this->MemGetPhyAddr(b_));  // ptr(1)
        //dpi_->WriteReg(0x18, 0);

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
    // input vm ptr
    void *a_{nullptr};
    // output vm ptr
    void *b_{nullptr};
};

using tvm::runtime::TVMArgs;
using tvm::runtime::TVMRetValue;

TVM_REGISTER_GLOBAL("tvm.vta.tsim.init")
    .set_body([](TVMArgs args, TVMRetValue *rv) {
        Module m = args[0];
        DPILoader::Global()->Init(m);
    });

TVM_REGISTER_GLOBAL("tvm.vta.driver")
    .set_body([](TVMArgs args, TVMRetValue *rv) {
        Device dev_;
        DLTensor *A = args[0];
        DLTensor *B = args[1];
        //uint32_t cc = static_cast<int>(args[4]);
        vector<int32_t> args_in = {0,0,64};
        uint32_t cycles = dev_.Run(A, B, args_in);
        *rv = static_cast<int>(cycles);
    });

}  // namespace driver
}  // namespace vta

#include <iostream>
int main(int argc, const char **argv) {
    std::cout << "Stating Tsim" << std::endl;
    std::shared_ptr<vta::dpi::DPIModule> n =
        std::make_shared<vta::dpi::DPIModule>();
    n->Init("./libhw.dylib");
    tvm::runtime::Module mod = tvm::runtime::Module(n);

    // tvm::runtime::Module m(dhw);

    // Init the simulator
    vta::driver::Device dev;
    dev.loader_->Init(mod);
    // dev.Init();
    // dev.Launch()

    std::ifstream input_file("../data/input_data.data");
    if (input_file.fail()) {
        cout << "Input file desn't exist -- EXIT\n";
        return 0;
    }
    std::string str_file((std::istreambuf_iterator<char>(input_file)),
                         std::istreambuf_iterator<char>());
    vector<string> input_str = split(str_file, ',');
    vector<int64_t> input_value;
    std::transform(input_str.begin(), input_str.end(),
                   std::back_inserter(input_value),
                   [](const string in_str) { return std::stol(in_str); });

    // Define the variables
    DLTensor input_image;
    DLTensor output_image;

    uint32_t input_size  = input_value.size();
    uint32_t output_size = 1024;

    //vector<int32_t> arguments = {0, 0, 64, 0, 4096, 0, 128, 0, 128, 128, 0, 4, 16384};
    vector<int32_t> arguments = {0, 0, 64, 0, 4096, 32, 1024};

    // Defining variables
    input_image.shape = (ARRAY_DATA_TYPE *)malloc(sizeof(ARRAY_DATA_TYPE));
    input_image.shape[0] = input_size;
    input_image.dtype.code = 0;
    input_image.dtype.bits = ELEMENT_SIZE_BITS;
    input_image.dtype.lanes = 1;
    input_image.ndim = 1;
    input_image.strides = nullptr;
    input_image.data = malloc(sizeof(ARRAY_DATA_TYPE) * input_image.shape[0]);

    output_image.shape = (ARRAY_DATA_TYPE *)malloc(sizeof(ARRAY_DATA_TYPE));
    output_image.shape[0] = output_size;  // 64;
    output_image.dtype.code = 0;
    output_image.dtype.bits = ELEMENT_SIZE_BITS;
    output_image.dtype.lanes = 1;
    output_image.ndim = 1;
    output_image.strides = nullptr;
    output_image.data = malloc(sizeof(ARRAY_DATA_TYPE) * output_image.shape[0]);

    // Initilizing data

    for (auto index = 0; index < input_image.shape[0]; index++) {
        *(((uint8_t *)(input_image.data)) + index * sizeof(ARRAY_DATA_TYPE)) = input_value[index];
    }

    for (auto index = 0; index < output_image.shape[0]; index++) {
        *(((uint8_t *)(output_image.data)) + index * sizeof(ARRAY_DATA_TYPE)) = 0;
    }

    //for (auto index = 0; index < input_image.shape[0]; index++) {
        //uint8_t val_c = *(((uint8_t *)(input_image.data)) + index * sizeof(ARRAY_DATA_TYPE));
        //std::cout << "index = " << (int)index << "\tval = " << (int)val_c << "\n";
    //}

    auto cycles = dev.Run(&input_image, &output_image, arguments);
    std::cout << "\n Output \n";
    // for (auto index = 0; index < a.shape[0]; index++)
    //{
    // uint8_t k = *(((uint8_t *)(c.data)) + index * sizeof(uint8_t));
    // std::cout << "index = " << (int)index << "\tres = " << (int)k << "\n";
    //}
    std::cout << "\n Cycles " << cycles << "\n";
    return 0;
}
