package me.kokokotlin.test.instruction

import me.kokokotlin.main.cpu
import me.kokokotlin.main.cpu.CPU
import org.junit.Assert.*
import org.junit.Test

class CPYTest {
    // Flags N Z C

    @Test
    fun testCPYNegativeSet() {
        initMemTestProgram(byteArrayOf("A0 01 C0 02"))
        cpu.resetCPU()

        cpu.run()
        assertEquals(generateCpuStatus(listOf(CPU.Flags.NEGATIVE)), cpu.status)
    }

    @Test
    fun testCPYZeroWithCarrySet() {
        initMemTestProgram(byteArrayOf("A0 01 C0 01"))
        cpu.resetCPU()

        cpu.run()
        assertEquals(generateCpuStatus(listOf(CPU.Flags.ZERO, CPU.Flags.CARRY)), cpu.status)
    }

    @Test
    fun testCPYCarryWithNegativeSet() {
        initMemTestProgram(byteArrayOf("A0 FF C0 01"))
        cpu.resetCPU()

        cpu.run()
        assertEquals(generateCpuStatus(listOf(CPU.Flags.CARRY,  CPU.Flags.NEGATIVE)), cpu.status)
    }

}