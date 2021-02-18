package me.kokokotlin.test.instruction

import me.kokokotlin.main.cpu
import me.kokokotlin.main.cpu.CPU
import org.junit.Assert.*
import org.junit.Test

class LDATest {
    // flags: N Z

    @Test
    fun testLDA() {
        initMemTestProgram(byteArrayOf("A9 17"))
        val operator1: Byte = 0x17

        cpu.run()

        assertEquals(generateCpuStatus(listOf()), cpu.status)
        assertEquals(operator1, cpu.A)
    }

    @Test
    fun testLDAZeroSet() {
        initMemTestProgram(byteArrayOf("A9 00"))
        val operator1: Byte = 0x00

        cpu.run()

        assertEquals(generateCpuStatus(listOf(CPU.Flags.ZERO)), cpu.status)
        assertEquals(operator1, cpu.A)
    }

    @Test
    fun testLDANegativeSet() {
        initMemTestProgram(byteArrayOf("A9 80"))
        val operator1: Byte = 0x80.toByte()

        cpu.run()

        assertEquals(generateCpuStatus(listOf(CPU.Flags.NEGATIVE)), cpu.status)
        assertEquals(operator1, cpu.A)
    }
}
