package me.kokokotlin.test.instruction

import me.kokokotlin.main.cpu
import me.kokokotlin.main.toWord
import org.junit.Assert.*
import org.junit.Test

class BRKTest {

    @Test
    fun testBPLNegativeNotSetForward() {
        cpu.run()

        assertEquals(true, cpu.halt)
    }

}