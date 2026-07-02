# Desc 

This is an implementation of the Linear Feedback Shift Register in x86-64 architecture.
It generates 128 of pseudo random bits based on the tap sequence defined and the seed provided by the user
The input and output is done via syscalls, with the shifting being done using AND,XOR and POPCNT (to obtain the parity)

