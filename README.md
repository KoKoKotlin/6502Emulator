# 6502 Emulator written in Kotlin

This is a 6502 emulator written in Kotlin.

It doesn't support interrupts because the cpu 
is "standalone" so there is no other hardware 
that could generate them.

For building it you need my [PseudoGameEngine](https://github.com/KoKoKotlin/PseudoGameEngine) as a lib. 

---
## Usage
The program is controlled via the command prompt.
Commands:

command | functionality
--------|----------------
a | Prints the accumulator.
x | Prints the x register.
y | Prints the y register.
pc | Prints the program counter.
status | Prints a human readable form of the status register.
load file | Loads a 6502 binary into memory.
mem start len | Prints len bytes from memory beginning at start.
set addr value | Writes value to memory at address addr.
r | Init cpu and run the program.
init | Init the cpu.
s | Execute a single instruction.
visual | Opens a register memory view window.
q | Quit program.