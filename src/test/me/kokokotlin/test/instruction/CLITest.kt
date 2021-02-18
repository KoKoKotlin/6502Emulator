package me.kokokotlin.test.instruction

import me.kokokotlin.main.cpu
import org.junit.Assert.*
import org.junit.Test

class CLITest {

    @Test
    fun testCLI() {
        initMemTestProgram(byteArrayOf("78 58"))
        cpu.resetCPU()

        cpu.run()

        assertEquals(generateCpuStatus(listOf()), cpu.status)
    }
}