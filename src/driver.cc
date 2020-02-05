#include "DPImodule.h"
#include "dlpack/dlpack.h"
#include "runtime/module.h"
#include "runtime/registry.h"

#include "virtual_memory.h"

#include "darray.h"

#include <algorithm>
#include <experimental/iterator>
#include <fstream>
#include <iterator>
#include <sstream>
#include <streambuf>
#include <string>
#include <vector>

#define EN_DEBUG 0

#ifdef EN_DEBUG
#define DEBUG(x)                                   \
    do {                                           \
        std::cerr << #x << ": " << x << std::endl; \
    } while (0)
#else
#define DEBUG(X)
#endif

using namespace std;
namespace py = pybind11;

using driver::DArray;

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
    Device(uint64_t npatrs, uint64_t nvals) {
        ptrs_ = std::vector<void *>(npatrs, nullptr);
        vals_ = std::vector<uint64_t>(nvals, 0);

        loader_ = DPILoader::Global();
    }

    void Run(std::vector<std::reference_wrapper<DArray>> &ptrs,
                 vector<int64_t> &values, vector<int64_t> &rets) {
        if (ptrs.size() != this->ptrs_.size())
            throw std::runtime_error("Number of PTRS must be one");

        if (values.size() != this->vals_.size())
            throw std::runtime_error("Number of VLAS must be one");

        //uint32_t cycles;

        for (uint64_t ind = 0; ind < ptrs.size(); ++ind) {
            size_t a_size = (ptrs[ind].get().getArray().dtype.bits >> 3) *
                            ptrs[ind].get().getArray().shape[0];

            ptrs_[ind] = this->MemAlloc(a_size);

            this->MemCopyFromHost(ptrs_[ind], ptrs[ind].get().getArray().data,
                                  a_size);

            DEBUG("Print input data:");
            auto b = ptrs[ind].get().getArray();
            for (auto index = 0; index < b.shape[0]; index++) {
                uint64_t k = *(((uint64_t *)(b.data)) + index * sizeof(char));
                DEBUG((int)k);
            }
        }

        this->Init();

        this->Launch(values, rets);

        this->WaitForCompletion(rets);

        for (uint64_t ind = 0; ind < ptrs.size(); ++ind) {
            size_t a_size = (ptrs[ind].get().getArray().dtype.bits >> 3) *
                            ptrs[ind].get().getArray().shape[0];
            this->MemCopyToHost(ptrs[ind].get().getArray().data, ptrs_[ind],
                                a_size);
            auto b = ptrs[ind].get().getArray();
            DEBUG("Print output data:");
            for (auto index = 0; index < b.shape[0]; index++) {
                uint64_t k = *(((uint64_t *)(b.data)) + index * sizeof(char));
                DEBUG((int)k);
            }
            this->MemFree(ptrs_[ind]);
        }

        //return cycles;
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

    void Launch(vector<int64_t> &values, vector<int64_t> &rets) {

        /**
         * Address 0x00 is for control
         */
        int32_t address = 0x04;

        /**
         * We have event counters after control addresses
         */
        address += rets.size() * 0x04;

        int32_t cnt = 0;
        for (auto val : values) {
            dpi_->WriteReg(address + (cnt * 4), val);  // arg
            cnt++;
        }

        for (auto ptr : ptrs_) {
            dpi_->WriteReg(address + ((cnt)*4),
                           this->MemGetPhyAddr(ptr));  // ptr(0)

            cnt++;
        }

        dpi_->WriteReg(0x00, 0x1);  // launch
    }

    void WaitForCompletion(vector<int64_t> &rets) {
        uint32_t i, val;
        for (i = 0; i < wait_cycles_; i++) {
            val = dpi_->ReadReg(0x00);
            if (val == 2) break;  // finish
        }
        // Read event counter
        for(int j = 0; j < rets.size(); j++){
            uint32_t addr = 0x04 + (j * 0x04);
            rets[j] = dpi_->ReadReg(addr);
        }
        dpi_->SimWait();
    }

    // wait cycles
    uint32_t wait_cycles_{100000000};
    // DPI loader
    DPILoader *loader_{nullptr};
    // DPI Module
    DPIModuleNode *dpi_{nullptr};

    std::vector<void *> ptrs_;
    std::vector<uint64_t> vals_;
    std::vector<uint64_t> events_;
};

std::vector<int64_t> RunSim(std::vector<std::reference_wrapper<DArray>> in_ptrs,
                std::vector<int64_t> vars, std::vector<int64_t> returns, std::string hw_lib) {

    std::cout << "Stating MuIRSim..." << std::endl;

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

    dev.Run(in_ptrs, vars, returns);

    return returns;
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
          py::arg("vars"), py::arg("rets"), py::arg("hwlib"));
}
