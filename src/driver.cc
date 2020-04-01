#include <algorithm>
#include <experimental/iterator>
#include <fstream>
#include <iterator>
#include <sstream>
#include <streambuf>
#include <string>
#include <vector>

#include "DPImodule.h"
#include "darray.h"
#include "dlpack/dlpack.h"
#include "runtime/module.h"
#include "runtime/registry.h"
#include "virtual_memory.h"

//#define EN_DEBUG

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
    Device(uint64_t npatrs, uint64_t ndebugs, uint64_t nvals) {
        ptrs_ = std::vector<void *>(npatrs, nullptr);
        debugs_ = std::vector<void *>(ndebugs, nullptr);
        vals_ = std::vector<uint64_t>(nvals, 0);

        loader_ = DPILoader::Global();
    }

    vector<int64_t> Run(
        std::vector<std::reference_wrapper<DArray>> &ptrs,
        std::vector<std::reference_wrapper<DArray>> &debugs,
                        vector<int64_t> &values, int64_t nrets,
                        int64_t nevents) {

        vector<int64_t> rets(nrets + nevents, 0);
        // if (ptrs.size() != this->ptrs_.size())
        //     throw std::runtime_error("Number of PTRS must be one");

        //cout << "DEBUG SIZE: " << debugs.size() << "\n";

        if (values.size() != this->vals_.size())
            throw std::runtime_error("Number of VLAS must be one");

        //Allocate memory for input data
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
                DEBUG(k);
            }
        }

        //Allocate memory for debug data
        for (uint64_t ind = 0; ind < debugs.size(); ++ind) {
            size_t a_size = (debugs[ind].get().getArray().dtype.bits >> 3) *
                            debugs[ind].get().getArray().shape[0];

            debugs_[ind] = this->MemAlloc(a_size);

            this->MemCopyFromHost(debugs_[ind], debugs[ind].get().getArray().data,
                                  a_size);

            DEBUG("Print debug data:");
            auto b = debugs[ind].get().getArray();
            for (auto index = 0; index < b.shape[0]; index++) {
                uint64_t k = *(((uint64_t *)(b.data)) + index * sizeof(char));
                DEBUG(k);
            }
        }


        this->Init();

        this->Launch(values, rets);

        this->WaitForCompletion(rets);

        //Reading data from device to host
        for (uint64_t ind = 0; ind < ptrs.size(); ++ind) {
            size_t a_size = (ptrs[ind].get().getArray().dtype.bits >> 3) *
                            ptrs[ind].get().getArray().shape[0];
            this->MemCopyToHost(ptrs[ind].get().getArray().data, ptrs_[ind],
                                a_size);
            auto b = ptrs[ind].get().getArray();
            DEBUG("Print output data:");
            for (auto index = 0; index < b.shape[0]; index++) {
                uint64_t k = *(((uint64_t *)(b.data)) + index * sizeof(char));
                DEBUG(k);
            }
            this->MemFree(ptrs_[ind]);
        }

        //Reading debug data from device to host
        for (uint64_t ind = 0; ind < debugs.size(); ++ind) {
            size_t a_size = (debugs[ind].get().getArray().dtype.bits >> 3) *
                            debugs[ind].get().getArray().shape[0];
            this->MemCopyToHost(debugs[ind].get().getArray().data, debugs_[ind],
                                a_size);
            auto b = debugs[ind].get().getArray();
            DEBUG("Print returned debug data:");
            for (auto index = 0; index < b.shape[0]; index++) {
                uint64_t k = *(((uint64_t *)(b.data)) + index * sizeof(char));
                DEBUG(k);
            }
            this->MemFree(debugs_[ind]);
        }

         return rets;
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
        address += rets.size() * 4 ;
        int32_t cnt = 0;
        for (auto val : values) {
            dpi_->WriteReg(address + (cnt * 4), val);  // arg
            cnt++;
        }
        for (auto ptr : ptrs_) {
            dpi_->WriteReg(address + (( cnt )*4),
                           this->MemGetPhyAddr(ptr));  // ptr

            cnt++;
        }

        for (auto dbg : debugs_) {
            dpi_->WriteReg(address + ((cnt)*4),
                           this->MemGetPhyAddr(dbg));  // ptr

            cnt++;
        }

        dpi_->WriteReg(0x00, 0x1);  // launch
    }

    void WaitForCompletion(vector<int64_t> &rets) {
        uint32_t i, val;
        bool flag = false;
        for (i = 0; i < wait_cycles_; i++) {
            val = dpi_->ReadReg(0x00);
            if (val == 2){
                flag = true;
                break;  // finish
            }
        }

        if(!flag){
            cout << "SIMULATION TIMEOUT\n";
        }

        // Read event counter
        for (int j = 0; j < rets.size(); j++) {
            uint32_t addr = 0x04 + (j * 0x04);
            rets[j] = dpi_->ReadReg(addr);
        }
        dpi_->SimWait();
    }

    // wait cycles
    // uint32_t wait_cycles_{100000000};
    uint32_t wait_cycles_{400};
    // DPI loader
    DPILoader *loader_{nullptr};
    // DPI Module
    DPIModuleNode *dpi_{nullptr};

    std::vector<void *> ptrs_;
    std::vector<void *> debugs_;
    std::vector<uint64_t> vals_;
    std::vector<uint64_t> events_;
};

std::vector<int64_t> RunSim(std::vector<std::reference_wrapper<DArray>> in_ptrs,
                            std::vector<std::reference_wrapper<DArray>> in_debugs,
                            std::vector<int64_t> vars, int64_t nrets,
                            int64_t nevents, std::string hw_lib) {
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
    driver::Device dev(in_ptrs.size(), in_debugs.size(), vars.size());
    dev.loader_->Init(mod);

    std::vector<int64_t> returns = dev.Run(in_ptrs, in_debugs, vars, nrets, nevents);

    return returns;
}

}  // namespace driver

/**
 * Pybind11 wrappers for Dandelion-sim
 */
PYBIND11_MODULE(dsim, m) {
    py::class_<driver::DArray> darray(m, "DArray");
    darray.def(py::init<
             py::array_t<int64_t, py::array::c_style | py::array::forcecast>, driver::DArray::DType>())
        .def("initData", &driver::DArray::initData)
        .def("getData", &driver::DArray::getData)
        .def("__repr__", [](const driver::DArray &a) {
            return "<DArray ['" + std::to_string(a.getDimenssion()) + "']>: [" +
                   a.print_array() + "]";
        });

    py::enum_<driver::DArray::DType>(darray, "DType")
        .value("Bit", driver::DArray::DType::Bit)
        .value("Byte", driver::DArray::DType::Byte)
        .value("Word", driver::DArray::DType::Word)
        .value("DWord", driver::DArray::DType::DWord)
        .export_values();


    m.def("sim", &driver::RunSim, "A function to run DSIM", py::arg("ptrs"), py::arg("debugs"),
          py::arg("vars"), py::arg("numRets"), py::arg("numEvents"),
          py::arg("hwlib"));
}
