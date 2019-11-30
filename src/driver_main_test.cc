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

namespace vta
{
namespace driver
{

using tvm::runtime::Module;
using vta::dpi::DPIModule;
using vta::dpi::DPIModuleNode;

class DPILoader
{
public:
  ~DPILoader()
  {
    dpi_->SimResume();
    dpi_->SimFinish();
  }

  void Init(Module module)
  {
    mod_ = module;
    dpi_ = this->Get();
    dpi_->SimLaunch();
    dpi_->SimWait();
  }

  DPIModuleNode *Get()
  {
    return static_cast<DPIModuleNode *>(mod_.operator->());
  }

  static DPILoader *Global()
  {
    static DPILoader inst;
    return &inst;
  }

  // TVM module
  Module mod_;
  // DPI Module
  DPIModuleNode *dpi_{nullptr};
};

class Device
{
public:
  Device() { loader_ = DPILoader::Global(); }

  uint32_t Run(uint32_t c, DLTensor *a, DLTensor *b)
  {
    uint32_t cycles;
    uint32_t len = a->shape[0];
    size_t size = (a->dtype.bits >> 3) * len;
    a_ = this->MemAlloc(size);
    b_ = this->MemAlloc(size);
    this->MemCopyFromHost(a_, a->data, size);
    this->Init();
    this->Launch(c, len);
    cycles = this->WaitForCompletion();
    this->MemCopyToHost(b->data, b_, size);
    this->MemFree(a_);
    this->MemFree(b_);
    return cycles;
  }

public:
  void Init()
  {
    dpi_ = loader_->Get();
    dpi_->SimResume();
  }

  void *MemAlloc(size_t size)
  {
    void *addr = vta::vmem::VirtualMemoryManager::Global()->Alloc(size);
    return reinterpret_cast<void *>(
        vta::vmem::VirtualMemoryManager::Global()->GetPhyAddr(addr));
  }

  void MemFree(void *buf)
  {
    void *addr = vta::vmem::VirtualMemoryManager::Global()->GetAddr(
        reinterpret_cast<uint64_t>(buf));
    vta::vmem::VirtualMemoryManager::Global()->Free(addr);
  }

  vta_phy_addr_t MemGetPhyAddr(void *buf)
  {
    return reinterpret_cast<uint64_t>(reinterpret_cast<uint64_t *>(buf));
  }

  void MemCopyFromHost(void *dst, const void *src, size_t size)
  {
    vta::vmem::VirtualMemoryManager::Global()->MemCopyFromHost(dst, src, size);
  }

  void MemCopyToHost(void *dst, const void *src, size_t size)
  {
    vta::vmem::VirtualMemoryManager::Global()->MemCopyToHost(dst, src, size);
  }

  void Launch(uint32_t c, uint32_t len)
  {
    dpi_->WriteReg(0x08, c);
    dpi_->WriteReg(0x0c, len);
    dpi_->WriteReg(0x10, this->MemGetPhyAddr(a_));
    dpi_->WriteReg(0x14, 0);
    dpi_->WriteReg(0x18, this->MemGetPhyAddr(b_));
    dpi_->WriteReg(0x1c, 0);
    dpi_->WriteReg(0x00, 0x1); // launch
  }

  uint32_t WaitForCompletion()
  {
    uint32_t i, val;
    for (i = 0; i < wait_cycles_; i++)
    {
      val = dpi_->ReadReg(0x00);
      if (val == 2)
        break; // finish
    }
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
      uint32_t c = static_cast<int>(args[2]);
      uint32_t cycles = dev_.Run(c, A, B);
      *rv = static_cast<int>(cycles);
    });

} // namespace driver
} // namespace vta

#include <iostream>
int main(int argc, const char **argv)
{
  std::cout << "Stating Tsim" << std::endl;
  std::shared_ptr<vta::dpi::DPIModule> n =
      std::make_shared<vta::dpi::DPIModule>();
  n->Init("./libhw.dylib");
  tvm::runtime::Module mod = tvm::runtime::Module(n);

  // tvm::runtime::Module m(dhw);

  vta::driver::Device dev;
  dev.loader_->Init(mod);
  // dev.Init();
  // dev.Launch()
  DLTensor a;
  DLTensor b;
  a.shape = (int64_t *)malloc(sizeof(int64_t));
  a.shape[0] = 10;
  a.dtype.code = 0;
  a.dtype.bits = 64;
  a.dtype.lanes = 1;
  a.ndim = 1;
  a.strides = nullptr;
  a.data = malloc(sizeof(uint64_t) * a.shape[0]);
  std::cout << "Input (len has to < AXI beats) \n";
  for (auto index = 0; index < a.shape[0]; index++)
  {
    *(((uint64_t *)(a.data)) + index * sizeof(char)) = index;
    uint64_t k = *(((uint64_t *)(a.data)) + index * sizeof(char));
    std::cout << (int)k << "\n";
  }
  uint32_t constant = 10;
  std ::cout << "Adding Constant " << constant;

  b.shape = (int64_t *)malloc(sizeof(int64_t));
  b.shape[0] = 10;
  b.dtype.code = 0;
  b.dtype.bits = 64;
  b.dtype.lanes = 1;
  b.ndim = 1;
  b.strides = nullptr;
  b.data = malloc(sizeof(uint64_t) * b.shape[0]);
  auto cycles = dev.Run(constant, &a, &b);
  std::cout << "\n Cycles " << cycles << "\n";
  std::cout << "\n Output \n";
  for (auto index = 0; index < a.shape[0]; index++)
  {
    uint64_t k = *(((uint64_t *)(b.data)) + index * sizeof(char));
    std::cout << (int)k << "\n";
  }
  // tvm::runtime::Module m;
  //  m.LoadFromFile("./libsw.dylib", "dylib");
  return 0;
}