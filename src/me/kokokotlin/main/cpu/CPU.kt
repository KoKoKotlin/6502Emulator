package me.kokokotlin.main.cpu

import me.kokokotlin.main.*
import me.kokokotlin.main.asm.debugPrint
import me.kokokotlin.main.asm.instructionFromOpcode
import me.kokokotlin.main.asm.memoryModeFromOpcode
import me.kokokotlin.main.mem.argumentLen
import me.kokokotlin.main.mem.durations
import java.lang.IllegalStateException
import java.lang.UnsupportedOperationException


/** status register composition
    7  bit  0
    ---- ----
    NVss DIZC
    |||| ||||
    |||| |||+- Carry
    |||| ||+-- Zero
    |||| |+--- Interrupt Disable
    |||| +---- Decimal
    ||++------ No CPU effect
    |+-------- Overflow
    +--------- Negative
    src: https://wiki.nesdev.com/w/index.php/Status_flags
 */

class CPU {

    enum class Flags(val place: Int) {
        CARRY(0),
        ZERO(1),
        INTERRUPT_DISABLE(2),
        DECIMAL(3),
        OVERFLOW(6),
        NEGATIVE(7)
    }

    // general purpose registers
    var A: Byte = 0
    var X: Byte = 0
    var Y: Byte = 0

    var pc = 0.toWord()              // program counter
    var sp: Byte = 0xFF.toByte()     // stack pointer

    var status: Byte = 0   // status flags |N|V|s|s|D|I|Z|C|

    var halt = false

    private val clock = Clock()

    fun getFlag(flag: Flags) = status[flag.place]
    fun setFlag(flag: Flags, value: Boolean) {
        status = status.setBit(flag.place, value)
    }

    fun resetFlags() {
        status = 0
    }

    fun resetCPU() {
        resetFlags()

        A = 0
        X = 0
        Y = 0

        pc = wordFromBytes(mem.read(0xFFFCU), mem.read(0xFFFDU))
        sp = 0xFF.toByte()

        halt = false
    }

    fun pushStack(value: Byte) {
        if(sp == 0x00.toByte()) throw IllegalStateException("Stackoverflow!") // FIXME

        mem.write(0x100.toWord() wordPlus sp, value)
        sp--
    }

    fun popStack(): Byte {
        if(sp == 0xFF.toByte()) throw IllegalStateException("Stackunderflow!") // FIXME

        val memValue = mem.read(0x100.toWord() wordPlus sp)
        sp++
        return memValue
    }

    fun decodeNextInstruction() {
        val opcode = mem.read(pc)

        val addrMode = memoryModeFromOpcode(opcode.toUByte())
        if (addrMode == null) {
            println("Invalid Opcode 0x${opcode.toString(16)} at addr 0x${pc.toString(16)}")
            halt = true
            return
        }

        val paramLen = argumentLen[addrMode]

        val param =
            if(paramLen == 1U) {
                val byte = mem.read(++pc)
                byte.toWord()
            } else if (paramLen == 2U) {
                val lowByte = mem.read(++pc)
                val highByte = mem.read(++pc)

                wordFromBytes(lowByte, highByte)
            } else 0x00.toWord()

        val instructionName = instructionFromOpcode(opcode.toUByte())

        val instruction = when(instructionName) {
            "adc" -> ADC(addrMode, param)
            "and" -> AND(addrMode, param)
            "asl" -> ASL(addrMode, param)
            "bcc" -> BCC(param)
            "beq" -> BEQ(param)
            "bit" -> BIT(addrMode, param)
            "bmi" -> BMI(param)
            "bne" -> BNE(param)
            "bpl" -> BPL(param)
            "brk" -> BRK()
            "bvc" -> BVC(param)
            "bvs" -> BVS(param)
            "clc" -> CLC()
            "cld" -> CLD()
            "cli" -> CLI()
            "clv" -> CLV()
            "cmp" -> CMP(addrMode, param)
            "cpx" -> CPX(addrMode, param)
            "cpy" -> CPY(addrMode, param)
            "dec" -> DEC(addrMode, param)
            "dex" -> DEX()
            "dey" -> DEY()
            "eor" -> EOR(addrMode, param)
            "inc" -> INC(addrMode, param)
            "inx" -> INX()
            "iny" -> INY()
            "jmp" -> JMP(addrMode, param)
            "jsr" -> JSR(param)
            "lda" -> LDA(addrMode, param)
            "ldx" -> LDX(addrMode, param)
            "ldy" -> LDY(addrMode, param)
            "lsr" -> LSR(addrMode, param)
            "nop" -> NOP()
            "ora" -> ORA(addrMode, param)
            "pha" -> PHA()
            "php" -> PHP()
            "pla" -> PLA()
            "plp" -> PLP()
            "rol" -> ROL(addrMode, param)
            "ror" -> ROR(addrMode, param)
            "rti" -> throw UnsupportedOperationException("RTI not implemented yet")
            "rts" -> RTS()
            "sbc" -> SBC(addrMode, param)
            "sec" -> SEC()
            "sed" -> SED()
            "sei" -> SEI()
            "sta" -> STA(addrMode, param)
            "stx" -> STX(addrMode, param)
            "sty" -> STY(addrMode, param)
            "tax" -> TAX()
            "tay" -> TAY()
            "tsx" -> TSX()
            "txa" -> TXA()
            "txs" -> TXS()
            "tya" -> TYA()
            else -> throw UnsupportedOperationException("No instruction with opcode 0x${opcode.repr(16)}")
        }

        instruction.operation(cpu, instruction)

        debugPrint(opcode.toUByte(), instructionName, addrMode.javaClass.toString(), param, paramLen!!)

        val isWrite: Boolean = opcode in setOf("sta", "stx", "sty")
        val branchTaken = when(instruction) {
            is Branch -> instruction.taken
            else -> false
        }

        val baseCycles = durations[addrMode] ?: throw IllegalStateException("Couldn't find addrMode!")
        val cycles = baseCycles + if(addrMode.pageCrossed(param, isWrite)) 1U else 0U + if(branchTaken) 1U else 0U

        clock.instructionDuration = cycles

        while (!clock.instructionFinished()) clock.tickOnce()

        pc++
    }

    fun run() {
        resetCPU()
        while(!halt) {
            decodeNextInstruction()
        }
    }

    fun singleStep() {
        if(!halt) decodeNextInstruction()
    }
}