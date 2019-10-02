cmake_minimum_required(VERSION 3.15)
project(tsim C CXX)

include(${CMAKE_ROOT}/Modules/ExternalProject.cmake)
message(STATUS "CMAKE_ROOT=${CMAKE_ROOT}")

set(EXTERNAL_INSTALL_LOCATION ${CMAKE_BINARY_DIR}/external)

ExternalProject_Add(tvm_runtime
    GIT_REPOSITORY git@github.com:sfu-arch/tvm_runtime.git
    CMAKE_ARGS -DCMAKE_INSTALL_PREFIX=${EXTERNAL_INSTALL_LOCATION}
    STEP_TARGETS build
    EXCLUDE_FROM_ALL TRUE
)

include_directories("${CMAKE_CURRENT_SOURCE_DIR}/include")
include_directories("${CMAKE_CURRENT_SOURCE_DIR}/3rdparty/dlpack/include")
include_directories("${CMAKE_CURRENT_SOURCE_DIR}/3rdparty/dmlc-core/include")

set(CMAKE_C_FLAGS "-O2 -Wall -fPIC -fvisibility=hidden")
set(CMAKE_CXX_FLAGS "-O2 -Wall -fPIC -fvisibility=hidden")
set(CMAKE_CXX_STANDARD 14)

if (CMAKE_CXX_COMPILER_ID MATCHES "GNU" AND
    CMAKE_CXX_COMPILER_VERSION VERSION_GREATER 7.0)
  set(CMAKE_CXX_FLAGS "-faligned-new ${CMAKE_CXX_FLAGS}")
endif()

file(GLOB TSIM_SW_SRC src/driver.cc)
list(APPEND TSIM_SW_SRC src/vmem/virtual_memory.cc)
list(APPEND TSIM_SW_SRC src/dpi/module.cc)

add_library(sw SHARED ${TSIM_SW_SRC})
add_dependencies(sw tvm_runtime-build)
#target_link_libraries(sw tvm_runtime)
target_include_directories(sw PRIVATE ${VTA_DIR}/include ${VTA_DIR}/src)

if(APPLE)
  set_target_properties(sw PROPERTIES LINK_FLAGS "-undefined dynamic_lookup")
endif(APPLE)
