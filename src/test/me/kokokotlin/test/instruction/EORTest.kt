package me.kokokotlin.test.instruction

import me.kokokotlin.main.cpu

import me.kokokotlin.main.cpu.CPU
import org.junit.Assert.*
import org.junit.Test

class EORTest {
    // Flags N Z

    @Test
    fun testEOR() {
        initMemTestProgram(byteArrayOf("69 7F 49 12"))
        cpu.resetCPU()

        cpu.run()
        assertEquals((0x12 xor 0x7F).toByte(), cpu.A)
    }

    @Test
    fun testEORZeroSet() {
        initMemTestProgram(byteArrayOf("69 FF 49 FF"))
        cpu.resetCPU()

        cpu.run()
        assertEquals(generateCpuStatus(listOf(CPU.Flags.ZERO)), cpu.status)
        assertEquals(0.toByte(), cpu.A)
    }

    @Test
    fun testEORNegativeSet() {
        initMemTestProgram(byteArrayOf("69 FF 49 7F"))
        cpu.resetCPU()

        cpu.run()
        assertEquals(generateCpuStatus(listOf(CPU.Flags.NEGATIVE)), cpu.status)
        assertEquals(0x80.toByte(), cpu.A)
    }


}