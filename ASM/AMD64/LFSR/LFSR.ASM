; need this to tell yasm to compile for 64
bits 64

section .data
    msg db "Please insert initial seed: "
    msg_len equ $ - msg
    error_msg db "Error has been detected and execution halted ",10
    error_len equ $ - error_msg
    ;initial seed given as string (4 bytes)
    readBuffer times 32 db 0
    ;tap sequence for 32 bits 
    tapsequnce dd 01100111101011110001001111111101b
    ;bits obtained from lfsr  displayed as ascii
    outputBuffer times 128 db 0
    terminator_buffer db 10


section .text
  global main

  main:

  ;Print insert text
    mov rax,1 ;print
    mov rdi,1 ;stdout
    mov rsi,msg
    mov rdx,msg_len
    syscall
    cmp rax,0
    jl error

    ;read keyboard input 
    mov rax,0 ;read 
    mov rdi,0 ;stdin
    mov rsi, readBuffer
    mov rdx, 4
    syscall
    cmp rax,0
    jl error

    mov rcx,0
    lfsr_loop:
    cmp rcx,100
    je exit_loop

    mov eax,[tapsequnce]
    mov ebx, [readBuffer]
    ;saving output bit to buffer 
    mov edx,ebx
    and edx,1
    add dl,'0'
    mov [outputBuffer+rcx],dl

    ;calculating feedback bit 
    and eax,ebx
    ;now we get the partity
    popcnt eax,eax
    and eax,1

    shr ebx,1
    shl eax,31
    or ebx,eax 
    mov [readBuffer],ebx

    add rcx,1
    jmp lfsr_loop

    exit_loop:
    ;displays text
    mov rax,1
    mov rdi,1
    mov rsi,outputBuffer
    mov rdx,129
    syscall    
    cmp rax,0
    jl error
    jmp exit_program

    error:
    mov rax,1
    mov rdi,1
    mov rsi,error_msg
    mov rdx,error_len
    syscall    
    
    ;exit program 
    exit_program:
    mov rax,60
    mov rdi,0
    syscall
   
