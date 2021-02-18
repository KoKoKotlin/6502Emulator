package me.kokokotlin.test.instruction

import me.kokokotlin.main.cpu
import me.kokokotlin.main.toWord
import org.junit.Assert.*
import org.junit.Test

class BVCTest {

    @Test
    fun testBVCOverflowNotSetForward() {
        initMemTestProgram(byteArrayOf("69 10 69 10 50 03"))
        cpu.resetCPU()

        cpu.run()

        assertEquals((0x1006 + 4).toWord(), cpu.pc)
    }

    @Test
    fun testBVCOverflowNotSetBackward() {
        initMemTestProgram(byteArrayOf("69 10 69 10 50 F0"))
        cpu.resetCPU()

        cpu.run()

        assertEquals((0x1006 - 15).toWord(), cpu.pc)
    }


    @Test
    fun testBVCOverflowSet() {
        initMemTestProgram(byteArrayOf("69 80 69 FF 50 03"))
        cpu.resetCPU()

        cpu.run()

        assertEquals((0x1007).toWord(), cpu.pc)
    }
}