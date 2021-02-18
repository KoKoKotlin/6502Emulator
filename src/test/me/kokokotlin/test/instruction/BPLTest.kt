package me.kokokotlin.test.instruction

import me.kokokotlin.main.cpu
import me.kokokotlin.main.toWord
import org.junit.Assert.*
import org.junit.Test

class BPLTest {

    @Test
    fun testBPLNegativeNotSetForward() {
        initMemTestProgram(byteArrayOf("69 01 10 03"))
        cpu.resetCPU()

        cpu.run()

        assertEquals((0x1004 + 4).toWord(), cpu.pc)
    }

    @Test
    fun testBPLNegativeNotSetBackward() {
        initMemTestProgram(byteArrayOf("69 01 10 F0"))
        cpu.resetCPU()

        cpu.run()

        assertEquals((0x1004 - 15).toWord(), cpu.pc)
    }


    @Test
    fun testBPLNegativeSet() {
        initMemTestProgram(byteArrayOf("69 F0 10 F0"))
        cpu.resetCPU()

        cpu.run()

        assertEquals((0x1005).toWord(), cpu.pc)
    }
}