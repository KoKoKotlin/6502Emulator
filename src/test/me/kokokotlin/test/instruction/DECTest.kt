package me.kokokotlin.test.instruction

import me.kokokotlin.main.cpu
import me.kokokotlin.main.mem

import me.kokokotlin.main.cpu.CPU
import me.kokokotlin.main.toWord
import org.junit.Assert.*
import org.junit.Test

class DECTest {
    // Flags N Z

    @Test
    fun testDECNegativeSet() {
        initMemTestProgram(byteArrayOf("CE 00 FF"))
        cpu.resetCPU()

        cpu.run()
        assertEquals(generateCpuStatus(listOf(CPU.Flags.NEGATIVE)), cpu.status)
        assertEquals(mem.read(0xFF00.toWord()), (-1).toByte())
    }

    @Test
    fun testDECZeroSet() {
        initMemTestProgram(byteArrayOf("CE 00 FF"))
        cpu.resetCPU()
        mem.write(0xFF00.toWord(), 1)

        cpu.run()
        assertEquals(generateCpuStatus(listOf(CPU.Flags.ZERO)), cpu.status)
        assertEquals(mem.read(0xFF00.toWord()), 0.toByte())
    }

}