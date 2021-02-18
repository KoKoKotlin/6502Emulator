package me.kokokotlin.test.instruction

import me.kokokotlin.main.cpu
import me.kokokotlin.main.cpu.CPU
import org.junit.Assert.*
import org.junit.Test

class CLDTest {

    @Test
    fun testCLD() {
        initMemTestProgram(byteArrayOf("F8 D8"))
        cpu.resetCPU()

        cpu.run()

        assertEquals(generateCpuStatus(listOf()), cpu.status)
    }
}