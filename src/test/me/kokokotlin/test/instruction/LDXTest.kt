package me.kokokotlin.test.instruction

import me.kokokotlin.main.cpu
import me.kokokotlin.main.cpu.CPU
import org.junit.Assert.*
import org.junit.Test

class LDXTest {
    // flags: N Z

    @Test
    fun testLDX() {
        initMemTestProgram(byteArrayOf("A2 17"))
        val operator1: Byte = 0x17

        cpu.run()

        assertEquals(generateCpuStatus(listOf()), cpu.status)
        assertEquals(operator1, cpu.X)
    }

    @Test
    fun testLDXZeroSet() {
        initMemTestProgram(byteArrayOf("A2 00"))
        val operator1: Byte = 0x00

        cpu.run()

        assertEquals(generateCpuStatus(listOf(CPU.Flags.ZERO)), cpu.status)
        assertEquals(operator1, cpu.X)
    }

    @Test
    fun testLDXNegativeSet() {
        initMemTestProgram(byteArrayOf("A2 80"))
        val operator1: Byte = 0x80.toByte()

        cpu.run()

        assertEquals(generateCpuStatus(listOf(CPU.Flags.NEGATIVE)), cpu.status)
        assertEquals(operator1, cpu.X)
    }
}
