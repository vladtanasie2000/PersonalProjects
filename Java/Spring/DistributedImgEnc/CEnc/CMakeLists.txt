cmake_minimum_required(VERSION 3.16)

project(imgenc
    VERSION 1.0
    LANGUAGES C
)

set(CMAKE_C_STANDARD 11)
set(CMAKE_C_STANDARD_REQUIRED ON)
set(CMAKE_C_EXTENSIONS OFF)

find_package(OpenSSL REQUIRED COMPONENTS Crypto)
find_package(OpenMP REQUIRED COMPONENTS C)

add_executable(imgenc
    imgenc.c
)

target_link_libraries(imgenc
    PRIVATE
        OpenSSL::Crypto
        OpenMP::OpenMP_C
)

target_compile_options(imgenc PRIVATE -Wall -Wextra -Wpedantic)
