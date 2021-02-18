package me.kokokotlin.test.instruction

import me.kokokotlin.main.cpu

import me.kokokotlin.main.cpu.CPU
import org.junit.Assert.*
import org.junit.Test

class DEXTest {
    // Flags N Z

    @Test
    fun testDEXNegativeSet() {
        initMemTestProgram(byteArrayOf("A2 00 CA"))
        cpu.resetCPU()

        cpu.run()
        assertEquals(generateCpuStatus(listOf(CPU.Flags.NEGATIVE)), cpu.status)
        assertEquals(cpu.X, (-1).toByte())
    }

    @Test
    fun testDEXZeroSet() {
        initMemTestProgram(byteArrayOf("A2 01 CA"))
        cpu.resetCPU()

        cpu.run()
        assertEquals(generateCpuStatus(listOf(CPU.Flags.ZERO)), cpu.status)
        assertEquals(cpu.X, 0.toByte())
    }

}