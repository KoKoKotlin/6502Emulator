package me.kokokotlin.test.instruction

import me.kokokotlin.main.cpu
import me.kokokotlin.main.cpu.CPU

import org.junit.Assert.*
import org.junit.Test

class INXTest {
    // Flags N Z

    @Test
    fun testINX() {
        initMemTestProgram(byteArrayOf("A2 17 E8"))
        cpu.resetCPU()

        cpu.run()
        assertEquals((0x18).toByte(), cpu.X)
    }

    @Test
    fun testINXZeroSet() {
        initMemTestProgram(byteArrayOf("A2 FF E8"))
        cpu.resetCPU()

        cpu.run()
        assertEquals(generateCpuStatus(listOf(CPU.Flags.ZERO)), cpu.status)
        assertEquals(0.toByte(), cpu.X)
    }

    @Test
    fun testINXNegativeSet() {
        initMemTestProgram(byteArrayOf("A2 80 E8"))
        cpu.resetCPU()

        cpu.run()
        assertEquals(generateCpuStatus(listOf(CPU.Flags.NEGATIVE)), cpu.status)
        assertEquals((-127).toByte(), cpu.X)
    }
}