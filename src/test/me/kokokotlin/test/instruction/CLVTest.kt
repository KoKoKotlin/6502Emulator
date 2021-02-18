package me.kokokotlin.test.instruction

import me.kokokotlin.main.cpu
import me.kokokotlin.main.cpu.CPU
import org.junit.Assert.*
import org.junit.Test

class CLVTest {

    @Test
    fun testCLV() {
        initMemTestProgram(byteArrayOf("69 7F 69 01 B8"))
        cpu.resetCPU()

        cpu.run()
        assertEquals(generateCpuStatus(listOf(CPU.Flags.NEGATIVE)), cpu.status)
    }
}