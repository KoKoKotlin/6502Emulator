package me.kokokotlin.test.instruction

import me.kokokotlin.main.cpu
import me.kokokotlin.main.cpu.CPU
import org.junit.Assert.*
import org.junit.Test

class CMPTest {
    // Flags N Z C

    @Test
    fun testCMPNegativeSet() {
        initMemTestProgram(byteArrayOf("69 01 C9 02"))
        cpu.resetCPU()

        cpu.run()
        assertEquals(generateCpuStatus(listOf(CPU.Flags.NEGATIVE)), cpu.status)
    }

    @Test
    fun testCMPZeroWithCarrySet() {
        initMemTestProgram(byteArrayOf("69 01 C9 01"))
        cpu.resetCPU()

        cpu.run()
        assertEquals(generateCpuStatus(listOf(CPU.Flags.ZERO, CPU.Flags.CARRY)), cpu.status)
    }

    @Test
    fun testCMPCarryWithNegativeSet() {
        initMemTestProgram(byteArrayOf("69 FF C9 01"))
        cpu.resetCPU()

        cpu.run()
        assertEquals(generateCpuStatus(listOf(CPU.Flags.CARRY,  CPU.Flags.NEGATIVE)), cpu.status)
    }

}