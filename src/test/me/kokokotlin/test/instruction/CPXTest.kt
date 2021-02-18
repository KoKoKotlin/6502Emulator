package me.kokokotlin.test.instruction

import me.kokokotlin.main.cpu
import me.kokokotlin.main.cpu.CPU
import org.junit.Assert.*
import org.junit.Test

class CPXTest {
    // Flags N Z C

    @Test
    fun testCPXNegativeSet() {
        initMemTestProgram(byteArrayOf("A2 01 E0 02"))
        cpu.resetCPU()

        cpu.run()
        assertEquals(generateCpuStatus(listOf(CPU.Flags.NEGATIVE)), cpu.status)
    }

    @Test
    fun testCPXZeroWithCarrySet() {
        initMemTestProgram(byteArrayOf("A2 01 E0 01"))
        cpu.resetCPU()

        cpu.run()
        assertEquals(generateCpuStatus(listOf(CPU.Flags.ZERO, CPU.Flags.CARRY)), cpu.status)
    }

    @Test
    fun testCPXCarryWithNegativeSet() {
        initMemTestProgram(byteArrayOf("A2 FF E0 01"))
        cpu.resetCPU()

        cpu.run()
        assertEquals(generateCpuStatus(listOf(CPU.Flags.CARRY,  CPU.Flags.NEGATIVE)), cpu.status)
    }

}