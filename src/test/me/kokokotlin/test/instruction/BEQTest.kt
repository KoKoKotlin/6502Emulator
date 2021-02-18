package me.kokokotlin.test.instruction

import me.kokokotlin.main.cpu
import me.kokokotlin.main.toWord
import org.junit.Assert.*
import org.junit.Test


class BEQTest {

    @Test
    fun testBEQZeroSetForward() {
        initMemTestProgram(byteArrayOf("6D 00 00 F0 03"))
        cpu.resetCPU()

        cpu.run()

        assertEquals((0x1005 + 4).toWord(), cpu.pc)
    }

    @Test
    fun testBEQZeroSetBackward() {
        initMemTestProgram(byteArrayOf("6D 00 00 F0 F0"))
        cpu.resetCPU()

        cpu.run()

        assertEquals((0x1005 - 15).toWord(), cpu.pc)
    }

    @Test
    fun testBEQZeroNotSet() {
        initMemTestProgram(byteArrayOf("F0 F0"))
        cpu.resetCPU()

        cpu.run()

        assertEquals((0x1003).toWord(), cpu.pc)
    }
}