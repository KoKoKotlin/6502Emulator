package me.kokokotlin.test.utils

import me.kokokotlin.main.hasCarried
import org.junit.Assert.*
import org.junit.Test

class UtilsKtTest {
    @Test
    fun hasCarriedTestPositivePositiveFalse() {
        val arg1: Byte = 10
        val arg2: Byte = 12

        val res = hasCarried(arg1, arg2)

        assertEquals(false, res)
    }

    @Test
    fun hasCarriedTestPositivePositiveTrue() {
        val arg1: Byte = 127
        val arg2: Byte = 127

        val res = hasCarried(arg1, arg2)

        assertEquals(false, res)
    }

    @Test
    fun hasCarriedTestNegativeNegativeFalse() {
        val arg1: Byte = -10
        val arg2: Byte = -12

        val res = hasCarried(arg1, arg2)

        assertEquals(true, res)
    }

    @Test
    fun hasCarriedTestNegativeNegativeTrue() {
        val arg1: Byte = -127
        val arg2: Byte = -127

        val res = hasCarried(arg1, arg2)

        assertEquals(true, res)
    }

    @Test
    fun hasCarriedTestPositiveNegativeFalse() {
        val pos1: Byte = 10
        val pos2: Byte = -12

        val res = hasCarried(pos1, pos2)

        assertEquals(false, res)
    }

    @Test
    fun hasCarriedTestPositiveNegativeTrue() {
        val pos1: Byte = -10
        val pos2: Byte = 12

        val res = hasCarried(pos1, pos2)

        assertEquals(true, res)
    }
}