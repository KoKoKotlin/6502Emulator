package me.kokokotlin.test.instruction

import me.kokokotlin.main.cpu
import me.kokokotlin.main.toWord
import org.junit.Assert.*
import org.junit.Test

class BNETest {

    @Test
    fun testBNEZeroSet() {
        initMemTestProgram(byteArrayOf("69 01 D0 03"))
        cpu.resetCPU()

        cpu.run()

        assertEquals((0x1004 + 4).toWord(), cpu.pc)
    }

    @Test
    fun testBNEZeroNotSetForward() {
        initMemTestProgram(byteArrayOf("69 01 D0 F0"))
        cpu.resetCPU()

        cpu.run()

        assertEquals((0x1004 - 15).toWord(), cpu.pc)
    }


    @Test
    fun testBNEZeroNotSetBackward() {
        initMemTestProgram(byteArrayOf("69 00 D0 F0"))
        cpu.resetCPU()

        cpu.run()

        assertEquals((0x1005).toWord(), cpu.pc)
    }
}