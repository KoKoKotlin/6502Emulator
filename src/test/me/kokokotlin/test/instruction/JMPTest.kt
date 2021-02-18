package me.kokokotlin.test.instruction

import me.kokokotlin.main.cpu
import me.kokokotlin.main.toWord
import org.junit.Assert
import org.junit.Test

class JMPTest {

    @Test
    fun testJMPForward() {
        initMemTestProgram(byteArrayOf("4C 00 FF"))
        cpu.resetCPU()

        cpu.run()

        Assert.assertEquals(0xFF01.toWord(), cpu.pc)
    }

    @Test
    fun testJMPBackward() {
        initMemTestProgram(byteArrayOf("4C 00 04"))
        cpu.resetCPU()

        cpu.run()

        Assert.assertEquals(0x0401.toWord(), cpu.pc)
    }


}