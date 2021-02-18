package me.kokokotlin.test.instruction

import me.kokokotlin.main.cpu
import me.kokokotlin.main.cpu.CPU

import org.junit.Assert.*
import org.junit.Test

class INYTest {
    // Flags N Z

    @Test
    fun testINY() {
        initMemTestProgram(byteArrayOf("A0 17 C8"))
        cpu.resetCPU()

        cpu.run()
        assertEquals((0x18).toByte(), cpu.Y)
    }

    @Test
    fun testINYZeroSet() {
        initMemTestProgram(byteArrayOf("A0 FF C8"))
        cpu.resetCPU()

        cpu.run()
        assertEquals(generateCpuStatus(listOf(CPU.Flags.ZERO)), cpu.status)
        assertEquals(0.toByte(), cpu.Y)
    }

    @Test
    fun testINYNegativeSet() {
        initMemTestProgram(byteArrayOf("A0 80 C8"))
        cpu.resetCPU()

        cpu.run()
        assertEquals(generateCpuStatus(listOf(CPU.Flags.NEGATIVE)), cpu.status)
        assertEquals((-127).toByte(), cpu.Y)
    }
}