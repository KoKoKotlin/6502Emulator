package me.kokokotlin.test.utils

import me.kokokotlin.main.hasCarried
import me.kokokotlin.main.isOverflowedMinus
import me.kokokotlin.main.isOverflowedPlus
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
        val arg1: Byte = 10
        val arg2: Byte = -12

        val res = hasCarried(arg1, arg2)

        assertEquals(false, res)
    }

    @Test
    fun hasCarriedTestPositiveNegativeTrue() {
        val arg1: Byte = -10
        val arg2: Byte = 12

        val res = hasCarried(arg1, arg2)

        assertEquals(true, res)
    }

    @Test
    fun hasOverflowed1() {
        val arg1: Byte = 1
        val arg2: Byte = 1

        val res = isOverflowedPlus(arg1, arg2)

        assertEquals(false, res)
    }

    @Test
    fun hasOverflowed2() {
        val arg1: Byte = 1
        val arg2: Byte = -1

        val res = isOverflowedPlus(arg1, arg2)

        assertEquals(false, res)
    }

    @Test
    fun hasOverflowed3() {
        val arg1: Byte = 127
        val arg2: Byte = 1

        val res = isOverflowedPlus(arg1, arg2)

        assertEquals(true, res)
    }

    @Test
    fun hasOverflowed4() {
        val arg1: Byte = 1
        val arg2: Byte = 1

        val res = isOverflowedMinus(arg1, arg2)

        assertEquals(false, res)
    }

    @Test
    fun hasOverflowed5() {
        val arg1: Byte = 127
        val arg2: Byte = -1

        val res = isOverflowedMinus(arg1, arg2)

        assertEquals(true, res)
    }

    @Test
    fun hasOverflowed6() {
        val arg1: Byte = -128
        val arg2: Byte = 1

        val res = isOverflowedMinus(arg1, arg2)

        assertEquals(true, res)
    }
}