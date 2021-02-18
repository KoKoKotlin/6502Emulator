package me.kokokotlin.test.instruction

import me.kokokotlin.main.*
import me.kokokotlin.main.cpu.CPU
import org.junit.Assert.*
import org.junit.Test


class ADCTest {
    // flags: N Z C V

    @Test
    fun testADCWithoutFlags() {
        initMemTestProgram(byteArrayOf("AD F0 FF 6D F1 FF"))

        val operator1: Byte = 12
        val operator2: Byte = 12

        mem.write(0xFFF0.toWord(), operator1)
        mem.write(0xFFF1.toWord(), operator2)

        cpu.run()

        assertEquals(operator1 bytePlus operator2, cpu.A)
        assertEquals(generateCpuStatus(listOf()), cpu.status)
    }

    @Test
    fun testADCWithoutZero() {
        initMemTestProgram(byteArrayOf("AD F0 FF 6D F1 FF"))

        val operator1: Byte = 0
        val operator2: Byte = 0

        mem.write(0xFFF0.toWord(), operator1)
        mem.write(0xFFF1.toWord(), operator2)

        cpu.run()

        assertEquals(operator1 bytePlus operator2, cpu.A)
        assertEquals(generateCpuStatus(
            listOf(CPU.Flags.ZERO)
        ), cpu.status)
    }

    @Test
    fun testADCWithoutNeg() {
        initMemTestProgram(byteArrayOf("AD F0 FF 6D F1 FF"))

        val operator1: Byte = 127
        val operator2: Byte = 1

        mem.write(0xFFF0.toWord(), operator1)
        mem.write(0xFFF1.toWord(), operator2)

        cpu.run()

        assertEquals(operator1 bytePlus operator2, cpu.A)
        assertEquals(generateCpuStatus(
            listOf(CPU.Flags.NEGATIVE, CPU.Flags.OVERFLOW)
        ), cpu.status)
    }

    @Test
    fun testADCWithoutWithNegCarry() {
        initMemTestProgram(byteArrayOf("AD F0 FF 6D F1 FF"))

        val operator1: Byte = -10
        val operator2: Byte = 12

        println(operator1 bytePlus operator2)

        mem.write(0xFFF0.toWord(), operator1)
        mem.write(0xFFF1.toWord(), operator2)

        cpu.run()

        assertEquals(operator1 bytePlus operator2, cpu.A)
        assertEquals(generateCpuStatus(
            listOf(CPU.Flags.CARRY)
        ), cpu.status)
    }
}
