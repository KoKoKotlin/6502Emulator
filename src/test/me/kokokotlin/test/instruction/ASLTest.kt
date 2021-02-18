package me.kokokotlin.test.instruction

import me.kokokotlin.main.*
import org.junit.Assert.*
import org.junit.Test

import me.kokokotlin.main.cpu.CPU

class ASLTest {
    // Flags N Z C

    @Test
    fun testASLWithNeg() {
        initMemTestProgram(byteArrayOf("AD F0 FF 0A"))

        val operator1: Byte = 0b01101111

        mem.write(0xFFF0.toWord(), operator1)

        cpu.run()

        assertEquals((operator1.toInt() shl 1).toByte(), cpu.A)
        assertEquals(generateCpuStatus(listOf(CPU.Flags.NEGATIVE)), cpu.status)
    }

    @Test
    fun testASLWithZero() {
        initMemTestProgram(byteArrayOf("AD F0 FF 0A"))

        val operator1: Byte = 0b0

        mem.write(0xFFF0.toWord(), operator1)

        cpu.run()

        assertEquals((operator1.toInt() shl 1).toByte(), cpu.A)
        assertEquals(generateCpuStatus(listOf(CPU.Flags.ZERO)), cpu.status)
    }

    @Test
    fun testASLWithZeroCarry() {
        initMemTestProgram(byteArrayOf("AD F0 FF 0A"))

        val operator1: Byte = -128

        mem.write(0xFFF0.toWord(), operator1)

        cpu.run()

        assertEquals((operator1.toInt() shl 1).toByte(), cpu.A)
        assertEquals(generateCpuStatus(
            listOf(CPU.Flags.ZERO, CPU.Flags.CARRY)),
            cpu.status)
    }
}