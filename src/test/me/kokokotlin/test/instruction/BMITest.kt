package me.kokokotlin.test.instruction

import me.kokokotlin.main.toWord
import org.junit.Assert.*
import org.junit.Test

import me.kokokotlin.main.cpu
import me.kokokotlin.main.cpu.CPU

class BMITest {


    @Test
    fun testBMINegativeSetForward() {
        initMemTestProgram(byteArrayOf("69 F0 30 03"))
        cpu.resetCPU()

        cpu.run()

        assertEquals((0x1004 + 4).toWord(), cpu.pc)
    }

    @Test
    fun testBMINegativeSetBackward() {
        initMemTestProgram(byteArrayOf("69 F0 30 F0"))
        cpu.resetCPU()

        cpu.run()

        assertEquals((0x1004 - 15).toWord(), cpu.pc)
    }

    @Test
    fun testBMINegativeNotSet() {
        initMemTestProgram(byteArrayOf("30 03"))
        cpu.resetCPU()

        cpu.setFlag(CPU.Flags.NEGATIVE, false)
        cpu.run()

        assertEquals((0x1003).toWord(), cpu.pc)
    }

}