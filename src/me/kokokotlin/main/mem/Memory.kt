package me.kokokotlin.main.mem

import me.kokokotlin.main.Word

const val MEM_SIZE: Int = 0x10000

class Memory {

    private val storage = ByteArray(MEM_SIZE) { 0 }

    fun reset() = storage.fill(0, 0, MEM_SIZE)

    fun read(addr: Word) = storage[addr.toInt()]
    fun write(addr: Word, value: Byte) {
        storage[addr.toInt()] = value
    }

    fun read(addr: Byte) = storage[addr.toInt()]
    fun write(addr: Byte, value: Byte) {
        storage[addr.toInt()] = value
    }

    fun write(startAddr: Word, byteArray: ByteArray) {
        byteArray.copyInto(storage, startAddr.toInt())
    }
}