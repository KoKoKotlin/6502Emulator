package me.kokokotlin.test.instruction

import me.kokokotlin.main.cpu.CPU
import me.kokokotlin.main.setBit
import me.kokokotlin.main.toWord

import me.kokokotlin.main.mem

// test program gets written to 0x1000
// reset vector always set to 0x1000
// cpu always reset
fun initMemTestProgram(testProgram: ByteArray) {
    // set reset vector
    mem.reset()

    mem.write(0xFFFD.toWord(), 0x10)
    // inject test program
    mem.write(0x1000.toWord(), testProgram)
}

// get bytearray from string of hex numbers
fun byteArrayOf(rawString: String) =
    rawString
        .split(" ")
        .map { it.toUByte(16).toByte() }
        .toByteArray()

// get status with correct bit set
fun generateCpuStatus(flags: List<CPU.Flags>): Byte {
    var res: Byte = 0

    for (flag in flags) {
        res = res.setBit(flag.place, true)
    }

    return res
}