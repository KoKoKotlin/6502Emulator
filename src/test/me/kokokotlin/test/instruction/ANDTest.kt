package me.kokokotlin.test.instruction

import me.kokokotlin.main.toWord
import org.junit.Assert.*
import org.junit.Test

import me.kokokotlin.main.mem
import me.kokokotlin.main.cpu
import me.kokokotlin.main.cpu.CPU
import kotlin.experimental.and

class ANDTest {
    // Flags N Z

    @Test
    fun testAndWithNeg() {
        initMemTestProgram(byteArrayOf("AD F0 FF 2D F1 FF"))

        val operator1: Byte = 0xF0U.toByte()
        val operator2: Byte = 0X8FU.toByte()

        mem.write(0xFFF0.toWord(), operator1)
        mem.write(0xFFF1.toWord(), operator2)

        cpu.run()

        assertEquals(operator1 and operator2, cpu.A)
        assertEquals(generateCpuStatus(listOf(CPU.Flags.NEGATIVE)), cpu.status)
    }

    @Test
    fun testAndWithZero() {
        initMemTestProgram(byteArrayOf("AD F0 FF 2D F1 FF"))

        val operator1: Byte = 0xF0U.toByte()
        val operator2: Byte = 0X0F

        mem.write(0xFFF0.toWord(), operator1)
        mem.write(0xFFF1.toWord(), operator2)

        cpu.run()

        assertEquals(operator1 and operator2, cpu.A)
        assertEquals(generateCpuStatus(listOf(CPU.Flags.ZERO)), cpu.status)
    }

}