package me.kokokotlin.test.instruction

import me.kokokotlin.main.cpu
import me.kokokotlin.main.toWord
import org.junit.Assert
import org.junit.Test

class BCSTest {

    @Test
    fun testBCSCarrySetForward() {
        initMemTestProgram(byteArrayOf("38 B0 03"))
        cpu.resetCPU()

        cpu.run()

        Assert.assertEquals((0x1003 + 4).toWord(), cpu.pc)
    }

    @Test
    fun testBCSCarrySetBackward() {
        initMemTestProgram(byteArrayOf("38 B0 F0"))
        cpu.resetCPU()

        cpu.run()

        Assert.assertEquals((0x1003 - 15).toWord(), cpu.pc)
    }


    @Test
    fun testBCSNotCarrySet() {
        initMemTestProgram(byteArrayOf("38 90 F0"))
        cpu.resetCPU()

        cpu.run()

        Assert.assertEquals((0x1004).toWord(), cpu.pc)
    }
}