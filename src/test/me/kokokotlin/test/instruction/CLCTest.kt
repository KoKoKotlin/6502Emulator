package me.kokokotlin.test.instruction

import me.kokokotlin.main.cpu
import org.junit.Assert.*
import org.junit.Test

class CLCTest {

    @Test
    fun testCLC() {
        initMemTestProgram(byteArrayOf("69 F0 69 7F 18"))
        cpu.resetCPU()

        cpu.run()

        assertEquals(generateCpuStatus(listOf()), cpu.status)
    }
}