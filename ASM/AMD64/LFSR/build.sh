#!/bin/sh

yasm -felf64 LFSR.ASM
gcc -o LFSR LFSR.o -no-pie
