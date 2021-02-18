package me.kokokotlin.test.instruction

import me.kokokotlin.main.cpu
import me.kokokotlin.main.cpu.CPU
import me.kokokotlin.main.mem
import me.kokokotlin.main.toWord

import org.junit.Assert.*
import org.junit.Test

class INCTest {
    // Flags N Z

    @Test
    fun testINC() {
        initMemTestProgram(byteArrayOf("EE 00 FF"))
        cpu.resetCPU()
        mem.write(0xFF00.toWord(), 12)

        cpu.run()
        assertEquals(13.toByte(), mem.read(0xFF00.toWord()))
    }

    @Test
    fun testINCZeroSet() {
        initMemTestProgram(byteArrayOf("EE 00 FF"))
        cpu.resetCPU()
        mem.write(0xFF00.toWord(), -1)

        cpu.run()
        assertEquals(generateCpuStatus(listOf(CPU.Flags.ZERO)), cpu.status)
        assertEquals(0.toByte(), mem.read(0xFF00.toWord()))
    }

    @Test
    fun testINCNegativeSet() {
        initMemTestProgram(byteArrayOf("EE 00 FF"))
        cpu.resetCPU()
        mem.write(0xFF00.toWord(), -20)

        cpu.run()
        assertEquals(generateCpuStatus(listOf(CPU.Flags.NEGATIVE)), cpu.status)
        assertEquals((-19).toByte(), mem.read(0xFF00.toWord()))
    }
}