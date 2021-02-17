package me.kokokotlin.main

import java.lang.Exception
import javax.swing.JOptionPane
import kotlin.experimental.and
import kotlin.experimental.or

// a word on the 6502 is 2 bytes large == Short
typealias Word = UShort

// ***************** Word utils *****************

fun Number.toWord() = this.toShort().toUShort()
fun UInt.toWord() = this.toUShort()

// operations that automatically cast to word
infix fun Word.wordPlus(other: Number)  = (this + other.toWord()).toWord()
infix fun Word.wordMinus(other: Number) = (this - other.toWord()).toWord()
infix fun Word.wordTimes(other: Number) = (this * other.toWord()).toWord()
infix fun Word.wordDiv(other: Number)   = (this / other.toWord()).toWord()
infix fun Word.wordMod(other: Number)   = (this % other.toWord()).toWord()

val Word.upper: Byte
    get() = (this.toInt() shr 8 and 0xFF).toByte()

val Word.lower: Byte
    get() = (this.toInt() and 0xFF).toByte()

fun Word.print(radix: Int = 16, newLine: Boolean = false) {
    if (radix == 2) {
        print(toString(radix).padStart(16, '0'))
    } else if (radix == 16) {
        print(toString(radix).padStart(4, '0'))
    }

    if (newLine) println()
}

fun wordFromBytes(lower: Byte, upper: Byte) = (upper.toInt() shl 8 or lower.toInt()).toWord()

// **********************************************


// ***************** Byte utils *****************

fun Byte.repr(radix: Int = 2) =
    when (radix) {
        2 -> this.toUByte().toString(radix).padStart(8, '0')
        16 -> this.toUByte().toString(radix).padStart(2, '0')
        else -> this.toUByte().toString(radix)
    }

fun Byte.print(radix: Int = 2, newLine: Boolean = false) {
    print(repr(radix))
    if (newLine) println()
}

infix fun Byte.bytePlus(other: Number)  = (this + other.toByte()).toByte()
infix fun Byte.byteMinus(other: Number) = (this - other.toByte()).toByte()
infix fun Byte.byteTimes(other: Number) = (this * other.toByte()).toByte()
infix fun Byte.byteDiv(other: Number)   = (this / other.toByte()).toByte()
infix fun Byte.byteMod(other: Number)   = (this % other.toByte()).toByte()

fun Byte.isNeg() = this < 0
fun Byte.isZero() = this == 0.toByte()
// **********************************************

fun Int.hasCarried() = this.toUInt() !in 0U..255U

fun hasCarried(b1: Byte, b2: Byte) = hasCarried(byteArrayOf(b1, b2))

fun hasCarried(bytes: ByteArray): Boolean {
    var res: UInt = 0U
    for(byte in bytes) res += byte.toUByte()

    return res !in 0U..255U
}

fun isOverflowedPlus(a: Byte, b: Byte) =
    a.toInt() + b.toInt() !in -128..127


fun isOverflowedMinus(a: Byte, b: Byte) =
    a.toInt() - b.toInt() !in -128..127

operator fun Byte.get(index: Int) = (this.toInt() shr index and 0x1).toByte()

fun Byte.setBit(index: Int, value: Boolean) =
    if(this[index].isZero() && !value || !this[index].isZero() && value)
        this
    else if(value)
        this or (0x1 shl index).toByte()
    else
        this and (0x1 shl index).toByte().not()

fun Byte.not(): Byte {
    var res: Byte = 0
    for (i in 0..7)
        if (this[i].isZero())
            res = res or (0x1 shl i).toByte()

    return res
}

// input Utils
fun getNumInput(msg: String, radix: Int, startValue: String): Int? {
    val res = JOptionPane.showInputDialog(msg, startValue)

    try {
        return res.toInt(radix)
    } catch (e: Exception) {
        printError("Couldn't parse int from input: $res")
    }

    return null
}