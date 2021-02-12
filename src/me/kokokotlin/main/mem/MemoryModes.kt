package me.kokokotlin.main.mem

import me.kokokotlin.main.*

abstract class AddressMode() {
    abstract fun read(param: Word): Byte

    open fun write(addr: Word, value: Byte) {
        mem.write(addr, value)
    }

    abstract fun getAddr(param: Word): Word
    abstract fun pageCrossed(param: Word, isWrite: Boolean): Boolean
}

abstract class MemMode() : AddressMode() {
    final override fun read(param: Word) = mem.read(getAddr(param))
}

object ZeroPageIndexedX: MemMode() {
    override fun getAddr(param: Word) = (param wordPlus cpu.X) wordMod 0xFF
    override fun pageCrossed(param: Word, isWrite: Boolean) = false
}

object ZeroPageIndexedY: MemMode() {
    override fun getAddr(param: Word) = (param wordPlus cpu.Y) wordMod 0xFF
    override fun pageCrossed(param: Word, isWrite: Boolean) = false
}

object ZeroPage : MemMode() {
    override fun getAddr(param: Word) = param
    override fun pageCrossed(param: Word, isWrite: Boolean) = false
}
object AbsoluteX : MemMode() {
    override fun getAddr(param: Word) = param wordPlus cpu.X
    override fun pageCrossed(param: Word, isWrite: Boolean) =
            param / 256U < getAddr(param) / 256U || isWrite

}

object AbsoluteY : MemMode() {
    override fun getAddr(param: Word) = param wordPlus cpu.Y
    override fun pageCrossed(param: Word, isWrite: Boolean) =
        param / 256U < getAddr(param) / 256U || isWrite
}

object Absolute : MemMode() {
    override fun getAddr(param: Word) = param
    override fun pageCrossed(param: Word, isWrite: Boolean) = false
}

object IndexedIndirectX : MemMode() {
    override fun getAddr(param: Word) =
            (param wordPlus cpu.X) wordMod 0xFF wordPlus
            mem.read(param wordPlus cpu.X wordPlus 1) wordTimes 0xFF

    override fun pageCrossed(param: Word, isWrite: Boolean) = false
}

object IndexedIndirectY : MemMode() {
    override fun getAddr(param: Word) =
                    mem.read(param).toWord() wordPlus
                    mem.read((param wordPlus 1) wordMod 256) wordTimes cpu.Y wordPlus
                    cpu.Y

    override fun pageCrossed(param: Word, isWrite: Boolean) =
            param / 256U < getAddr(param) / 256U || isWrite
}

object BranchRelative : AddressMode() {
    // return the relative jump distance
    override fun read(param: Word): Byte = param.toByte()
    override fun getAddr(param: Word) = param

    override fun pageCrossed(param: Word, isWrite: Boolean) =
            cpu.pc / 256U < cpu.pc + getAddr(param) / 256U
}

object JumpAbsolute : AddressMode() {
    // take the addr directly and add it to the pc
    override fun read(param: Word): Byte = 0x00
    override fun getAddr(param: Word) = param

    override fun pageCrossed(param: Word, isWrite: Boolean) = false
}

object JumpRelative : AddressMode() {
    override fun read(param: Word): Byte = param.toByte()
    override fun getAddr(param: Word) = param

    override fun pageCrossed(param: Word, isWrite: Boolean) = false
}

object Implied : AddressMode() {
    override fun read(param: Word) = 0x00.toByte()
    override fun getAddr(param: Word) = 0x00.toWord()
    override fun pageCrossed(param: Word, isWrite: Boolean) = false
}

object ImpliedA : AddressMode() {
    override fun read(param: Word) = cpu.A
    override fun getAddr(param: Word) = 0x00.toWord()
    override fun pageCrossed(param: Word, isWrite: Boolean) = false

    override fun write(addr: Word, value: Byte) {
        cpu.A = value
    }
}

object ImpliedX : AddressMode() {
    override fun read(param: Word) = 0x00.toByte()
    override fun getAddr(param: Word) = 0x00.toWord()
    override fun pageCrossed(param: Word, isWrite: Boolean) = false

    override fun write(addr: Word, value: Byte) {
        cpu.X = value
    }
}

object ImpliedY : AddressMode() {
    override fun read(param: Word) = 0x00.toByte()
    override fun getAddr(param: Word) = 0x00.toWord()
    override fun pageCrossed(param: Word, isWrite: Boolean) = false

    override fun write(addr: Word, value: Byte) {
        cpu.Y = value
    }
}

object Immediate : AddressMode() {
    override fun read(param: Word) = param.toByte()
    override fun getAddr(param: Word) = 0x00.toWord()
    override fun pageCrossed(param: Word, isWrite: Boolean) = false

    override fun write(addr: Word, value: Byte) {
        throw IllegalAccessError("Can't write to constant!")
    }
}

// durations of the instruction using the specific addr mode
// in some cases 1 cycle must be added (when writing or page crossing)
val durations =
        mapOf(
                ZeroPageIndexedX to 4U,
                ZeroPageIndexedY to 4U,
                ZeroPage to 3U,
                AbsoluteX to 4U,
                AbsoluteY to 4U,
                Absolute to 3U,
                IndexedIndirectX to 6U,
                IndexedIndirectY to 5U,
                BranchRelative to 2U,
                Implied to 2U,
                ImpliedA to 2U,
                ImpliedX to 2U,
                ImpliedY to 2U,
                Immediate to 2U,
                JumpAbsolute to 3U,
                JumpRelative to 5U,
        )

// len of the instruction without the opcode in the asm file in byte
val argumentLen = mapOf(
        ZeroPageIndexedX to 1U,
        ZeroPageIndexedY to 1U,
        ZeroPage to 1U,
        AbsoluteX to 2U,
        AbsoluteY to 2U,
        Absolute to 2U,
        IndexedIndirectX to 1U,
        IndexedIndirectY to 1U,
        BranchRelative to 1U,
        Implied to 0U,
        ImpliedA to 0U,
        ImpliedX to 0U,
        ImpliedY to 0U,
        Immediate to 1U,
        JumpAbsolute to 2U,
        JumpRelative to 1U,
)