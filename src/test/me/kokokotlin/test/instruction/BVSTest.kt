package me.kokokotlin.test.instruction

import me.kokokotlin.main.cpu
import me.kokokotlin.main.toWord
import org.junit.Assert.*
import org.junit.Test

class BVSTest {

    @Test
    fun testBVSOverflowSetForward() {
        initMemTestProgram(byteArrayOf("69 80 69 FF 70 03"))
        cpu.resetCPU()

        cpu.run()

        assertEquals((0x1006 + 4).toWord(), cpu.pc)
    }

    @Test
    fun testBVSOverflowSetBackward() {
        initMemTestProgram(byteArrayOf("69 80 69 FF 70 F0"))
        cpu.resetCPU()

        cpu.run()

        assertEquals((0x1006 - 15).toWord(), cpu.pc)
    }


    @Test
    fun testBVSOverflowNotSet() {
        initMemTestProgram(byteArrayOf("69 01 69 01 70 03"))
        cpu.resetCPU()

        cpu.run()

        assertEquals((0x1007).toWord(), cpu.pc)
    }
}