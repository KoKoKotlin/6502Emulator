package me.kokokotlin.test.instruction

import me.kokokotlin.main.cpu
import me.kokokotlin.main.mem
import me.kokokotlin.main.toWord
import org.junit.Assert.*
import org.junit.Test

class JSRTest {

    @Test
    fun testJSRForward() {
        initMemTestProgram(byteArrayOf("20 00 FF"))
        cpu.resetCPU()

        cpu.run()

        assertEquals(0xFD.toByte(), cpu.sp)
        assertEquals(0x10.toByte(), mem.read(0x01FF.toWord()))
        assertEquals(0x02.toByte(), mem.read(0x01FE.toWord()))
        assertEquals(0xFF01.toWord(), cpu.pc)
    }

    @Test
    fun testJSRBackward() {
        initMemTestProgram(byteArrayOf("20 00 04"))
        cpu.resetCPU()

        cpu.run()

        assertEquals(0xFD.toByte(), cpu.sp)
        assertEquals(0x10.toByte(), mem.read(0x01FF.toWord()))
        assertEquals(0x02.toByte(), mem.read(0x01FE.toWord()))
        assertEquals(0x0401.toWord(), cpu.pc)
    }
}