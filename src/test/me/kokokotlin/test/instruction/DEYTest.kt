package me.kokokotlin.test.instruction

import me.kokokotlin.main.cpu

import me.kokokotlin.main.cpu.CPU
import org.junit.Assert.*
import org.junit.Test

class DEYTest {
    // Flags N Z

    @Test
    fun testDEYNegativeSet() {
        initMemTestProgram(byteArrayOf("A0 00 88"))
        cpu.resetCPU()

        cpu.run()
        assertEquals(generateCpuStatus(listOf(CPU.Flags.NEGATIVE)), cpu.status)
        assertEquals(cpu.Y, (-1).toByte())
    }

    @Test
    fun testDEYZeroSet() {
        initMemTestProgram(byteArrayOf("A0 01 88"))
        cpu.resetCPU()

        cpu.run()
        assertEquals(generateCpuStatus(listOf(CPU.Flags.ZERO)), cpu.status)
        assertEquals(cpu.Y, 0.toByte())
    }

}