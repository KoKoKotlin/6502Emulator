package me.kokokotlin.test.instruction

import me.kokokotlin.main.cpu
import me.kokokotlin.main.cpu.CPU
import me.kokokotlin.main.mem
import me.kokokotlin.main.toWord
import org.junit.Assert.*
import org.junit.Test

class BITTest {
    // Flags N Z V

    @Test
    fun testBITNoFlags() {
        initMemTestProgram(byteArrayOf("A9 FF 2C F0 FF"))
        mem.write(0xFFF0.toWord(), 12)

        cpu.run()

        assertEquals(generateCpuStatus(listOf()), cpu.status)
    }

    @Test
    fun testBITZero() {
        initMemTestProgram(byteArrayOf("A9 00 2C F0 FF"))
        mem.write(0xFFF0.toWord(), 0b00001111)

        cpu.run()

        assertEquals(generateCpuStatus(listOf(CPU.Flags.ZERO)), cpu.status)
    }

    @Test
    fun testBITNegative() {
        initMemTestProgram(byteArrayOf("A9 FF 2C F0 FF"))
        mem.write(0xFFF0.toWord(), 0b10000000.toByte())

        cpu.run()

        assertEquals(generateCpuStatus(listOf(CPU.Flags.NEGATIVE)), cpu.status)
    }

    @Test
    fun testBITOverflow() {
        initMemTestProgram(byteArrayOf("A9 FF 2C F0 FF"))
        mem.write(0xFFF0.toWord(), 0b01000000.toByte())

        cpu.run()

        assertEquals(generateCpuStatus(listOf(CPU.Flags.OVERFLOW)), cpu.status)
    }

    @Test
    fun testBITNegativeOverflow() {
        initMemTestProgram(byteArrayOf("A9 FF 2C F0 FF"))
        mem.write(0xFFF0.toWord(), 0b11000000.toByte())

        cpu.run()

        assertEquals(generateCpuStatus(
            listOf(CPU.Flags.NEGATIVE, CPU.Flags.OVERFLOW))
            , cpu.status)
    }
}