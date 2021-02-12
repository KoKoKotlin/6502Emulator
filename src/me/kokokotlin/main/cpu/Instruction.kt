package me.kokokotlin.main.cpu

import me.kokokotlin.main.*
import me.kokokotlin.main.mem.*
import java.lang.Exception
import kotlin.experimental.and
import kotlin.experimental.or

open class Instruction(val name: String,
                       val operation: (CPU, Instruction) -> Unit,
                       val addressMode: AddressMode,
                       val param: Word) {
}

open class Branch(name: String, operation: (CPU, Instruction) -> Unit, param: Word) :
        Instruction(
                name,
                operation,
                BranchRelative,
                param) {
    var taken = false
}

/**
 * A = Accumulator
 * M = Memory
 *
 * C = Carry Flag
 * */

// add mem with carry to acc
// A + M + C -> A, C
class ADC(addressMode: AddressMode, param: Word)
    : Instruction(
        "adc",
        { cpu, _ ->
            val memValue = addressMode.read(param)
            val resByte = cpu.A bytePlus memValue bytePlus cpu.getFlag(CPU.Flags.CARRY)
            val resInt = cpu.A + memValue + cpu.getFlag(CPU.Flags.CARRY)

            cpu.setFlag(CPU.Flags.NEGATIVE, resByte.isNeg())
            cpu.setFlag(CPU.Flags.ZERO, resByte.isZero())
            cpu.setFlag(CPU.Flags.CARRY, resInt.hasCarried())
            cpu.setFlag(CPU.Flags.OVERFLOW, isOverflowedMinus(cpu.A, memValue, resByte))

            cpu.A = resByte
        },
        addressMode,
        param
)

// and mem with acc
// A && M -> A
class AND(addressMode: AddressMode, param: Word) :
        Instruction(
                "and",
                { cpu, _ ->
                    val memValue = addressMode.read(param)
                    val resByte = cpu.A and memValue

                    cpu.setFlag(CPU.Flags.NEGATIVE, resByte.isNeg())
                    cpu.setFlag(CPU.Flags.ZERO, resByte.isZero())

                    cpu.A = resByte
                },
                addressMode,
                param
        )

// arithmetic shift left
// C <- [76543210] <- 0
class ASL(addressMode: AddressMode, param: Word) :
        Instruction(
                "asl",
                { cpu, _ ->
                    val memValue = addressMode.read(param)
                    val resInt = memValue.toInt() shl 1
                    val resByte = resInt.toByte()

                    cpu.setFlag(CPU.Flags.NEGATIVE, resByte.isNeg())
                    cpu.setFlag(CPU.Flags.ZERO, resByte.isZero())
                    cpu.setFlag(CPU.Flags.CARRY, resInt.hasCarried())

                    addressMode.write(param, resByte)
                },
                addressMode,
                param
        )

// needed for signed arithmetic
fun calcBranchAddr(offset: Byte) = (cpu.pc.toShort() + offset.toShort()).toWord()

// Branch on Carry Clear
// Cycles +1 when taken and +1 when page crossed
class BCC(param: Word) :
        Branch(
                "bcc",
                { cpu, bcc_ ->
                    if (cpu.getFlag(CPU.Flags.CARRY) == 0.toByte()) {
                        cpu.pc = calcBranchAddr(bcc_.addressMode.read(param))
                        val bcc = bcc_ as? Branch ?: throw Exception("Not a branch instruction!")
                        bcc.taken = true
                    }
                },
                param
        )

// Branch on Result Zero
// Cycles +1 when taken and +1 when page crossed
class BEQ(param: Word) :
        Branch(
                "beq",
                { cpu, beq_ ->
                    if (!cpu.getFlag(CPU.Flags.ZERO).isZero()) {
                        cpu.pc = calcBranchAddr(beq_.addressMode.read(param))
                        val beq = beq_ as? Branch ?: throw Exception("Not a branch instruction!")
                        beq.taken = true
                    }
                },
                param
        )

// Test Bits in Memory with Accumulator
// A AND M, M7 -> N, M6 -> V
class BIT(addressMode: AddressMode, param: Word) :
        Instruction(
                "bit",
                { cpu, _ ->
                    val memValue = addressMode.read(param)
                    val resByte = cpu.A and memValue

                    cpu.setFlag(CPU.Flags.NEGATIVE, memValue[7] == 1.toByte())
                    cpu.setFlag(CPU.Flags.OVERFLOW, memValue[6] == 1.toByte())
                    cpu.setFlag(CPU.Flags.ZERO, resByte.isZero())
                },
                addressMode,
                param
        )

// Branch on Result Minus
// Cycles +1 when taken and +1 when page crossed
class BMI(param: Word) :
        Branch(
                "bmi",
                { cpu, bmi_ ->
                    if (cpu.getFlag(CPU.Flags.NEGATIVE) == 1.toByte()) {
                        cpu.pc = calcBranchAddr(bmi_.addressMode.read(param))
                        val bmi = bmi_ as? Branch ?: throw Exception("Not a branch instruction!")
                        bmi.taken = true
                    }
                },
                param
        )

// Branch on Result not Zero
// Cycles +1 when taken and +1 when page crossed
class BNE(param: Word) :
        Branch(
                "bne",
                { cpu, bne_ ->
                    if (cpu.getFlag(CPU.Flags.ZERO).isZero()) {
                        cpu.pc = calcBranchAddr(bne_.addressMode.read(param))
                        val bne = bne_ as? Branch ?: throw Exception("Not a branch instruction!")
                        bne.taken = true
                    }
                },
                param
        )

// Branch on Result Plus
// Cycles +1 when taken and +1 when page crossed
class BPL(param: Word) :
        Branch(
                "bpl",
                { cpu, bpl_ ->
                    if (cpu.getFlag(CPU.Flags.NEGATIVE) != 1.toByte()) {
                        cpu.pc = calcBranchAddr(bpl_.addressMode.read(param))
                        val bpl = bpl_ as? Branch ?: throw Exception("Not a branch instruction!")
                        bpl.taken = true
                    }
                },
                param
        )

// Force Break
class BRK
    : Instruction(
        "brk",
        {   cpu, _ ->
            cpu.halt = true
        },
        Implied,
        0x00U
    )

// Branch on Overflow Clear
// Cycles +1 when taken and +1 when page crossed
class BVC(param: Word) :
        Branch(
                "bvc",
                { cpu, bvc_ ->
                    if (cpu.getFlag(CPU.Flags.OVERFLOW) != 1.toByte()) {
                        cpu.pc = calcBranchAddr(bvc_.addressMode.read(param))
                        val bvc = bvc_ as? Branch ?: throw Exception("Not a branch instruction!")
                        bvc.taken = true
                    }
                },
                param
        )

// Branch on Overflow Set
// Cycles +1 when taken and +1 when page crossed
class BVS(param: Word) :
        Branch(
                "bvs",
                { cpu, bvs_ ->
                    if (cpu.getFlag(CPU.Flags.OVERFLOW) == 1.toByte()) {
                        cpu.pc = calcBranchAddr(bvs_.addressMode.read(param))
                        val bvs = bvs_ as? Branch ?: throw Exception("Not a branch instruction!")
                        bvs.taken = true
                    }
                },
                param
        )

// Clear Carry
// 0 -> C
class CLC :
        Instruction(
                "clc",
                { cpu, _ ->
                    cpu.setFlag(CPU.Flags.CARRY, false)
                },
                Implied,
                0x00U
        )

// clear decimal mode
// 0 -> D
class CLD :
        Instruction(
                "clc",
                { cpu, _ ->
                    cpu.setFlag(CPU.Flags.DECIMAL, false)
                },
                Implied,
                0x00U
        )

// clear interrupt disable
// 0 -> I
class CLI :
        Instruction(
                "cli",
                { cpu, _ ->
                    cpu.setFlag(CPU.Flags.INTERRUPT_DISABLE, false)
                },
                Implied,
                0x00U
        )

// clear overflow
// 0 -> V
class CLV :
        Instruction(
                "clc",
                { cpu, _ ->
                    cpu.setFlag(CPU.Flags.OVERFLOW, false)
                },
                Implied,
                0x00U
        )

// compare mem with acc
// A - M
class CMP(addressMode: AddressMode, param: Word)
    : Instruction(
        "cmp",
        { cpu, _ ->
            val memValue = addressMode.read(param)
            val resByte = cpu.A byteMinus memValue
            val resInt = cpu.A - memValue

            cpu.setFlag(CPU.Flags.NEGATIVE, resByte.isNeg())
            cpu.setFlag(CPU.Flags.ZERO, resByte.isZero())
            cpu.setFlag(CPU.Flags.CARRY, resInt.hasCarried())
        },
        addressMode,
        param
)

// compare mem with x
// X - M
class CPX(addressMode: AddressMode, param: Word) :
        Instruction(
                "cpx",
                {   cpu, _ ->
                    val memValue = addressMode.read(param)
                    val resByte = cpu.X byteMinus memValue
                    val resInt = cpu.X - memValue

                    cpu.setFlag(CPU.Flags.NEGATIVE, resByte.isNeg())
                    cpu.setFlag(CPU.Flags.ZERO, resByte.isZero())
                    cpu.setFlag(CPU.Flags.CARRY, resInt.hasCarried())
                },
                addressMode,
                param
        )

// compare mem with y
// Y - M
class CPY(addressMode: AddressMode, param: Word) :
        Instruction(
                "cpy",
                {   cpu, _ ->
                    val memValue = addressMode.read(param)
                    val resByte = cpu.Y byteMinus memValue
                    val resInt = cpu.Y - memValue

                    cpu.setFlag(CPU.Flags.NEGATIVE, resByte.isNeg())
                    cpu.setFlag(CPU.Flags.ZERO, resByte.isZero())
                    cpu.setFlag(CPU.Flags.CARRY, resInt.hasCarried())
                },
                addressMode,
                param
        )


// Decrement mem by one
// M - 1 -> M
class DEC(addressMode: AddressMode, param: Word) :
        Instruction(
                "dec",
                {   cpu, _ ->
                    val memValue = addressMode.read(param)
                    val resByte = memValue byteMinus 0x1

                    cpu.setFlag(CPU.Flags.NEGATIVE, resByte.isNeg())
                    cpu.setFlag(CPU.Flags.ZERO, resByte.isZero())

                    addressMode.write(param, resByte)
                },
                addressMode,
                param
        )

// Decrement x by one
// X - 1 -> X
class DEX :
        Instruction(
                "dex",
                {   cpu, _ ->
                    val resByte = cpu.X byteMinus 0x1

                    cpu.setFlag(CPU.Flags.NEGATIVE, resByte.isNeg())
                    cpu.setFlag(CPU.Flags.ZERO, resByte.isZero())

                    cpu.X = resByte
                },
                Implied,
                0x00U
        )

// Decrement y by one
// Y - 1 -> Y
class DEY :
        Instruction(
                "dey",
                {   cpu, _ ->
                    val resByte = cpu.Y byteMinus 0x1

                    cpu.setFlag(CPU.Flags.NEGATIVE, resByte.isNeg())
                    cpu.setFlag(CPU.Flags.ZERO, resByte.isZero())

                    cpu.Y = resByte
                },
                Implied,
                0x00U
        )

// xor mem with acc
// A xor M -> A
class EOR(addressMode: AddressMode, param: Word) :
        Instruction(
                "eor",
                {   cpu, _ ->
                    val memValue = addressMode.read(param)
                    val resByte = cpu.A and memValue

                    cpu.setFlag(CPU.Flags.NEGATIVE, resByte.isNeg())
                    cpu.setFlag(CPU.Flags.ZERO, resByte.isZero())

                    cpu.A = resByte
                },
                addressMode,
                param
        )

// Increment mem by one
// M + 1 -> M
class INC(addressMode: AddressMode, param: Word) :
        Instruction(
                "inc",
                {   cpu, _ ->
                    val memValue = addressMode.read(param)
                    val resByte = memValue bytePlus 0x1

                    cpu.setFlag(CPU.Flags.NEGATIVE, resByte.isNeg())
                    cpu.setFlag(CPU.Flags.ZERO, resByte.isZero())

                    addressMode.write(param, resByte)
                },
                addressMode,
                param
        )

// Increment x by one
// X + 1 -> X
class INX :
        Instruction(
                "inx",
                {   cpu, _ ->
                    val resByte = cpu.X bytePlus 0x1

                    cpu.setFlag(CPU.Flags.NEGATIVE, resByte.isNeg())
                    cpu.setFlag(CPU.Flags.ZERO, resByte.isZero())

                    cpu.X = resByte
                },
                Implied,
                0x00U
        )

// Increment y by one
// Y + 1 -> Y
class INY :
        Instruction(
                "iny",
                {   cpu, _ ->
                    val resByte = cpu.Y bytePlus 0x1

                    cpu.setFlag(CPU.Flags.NEGATIVE, resByte.isNeg())
                    cpu.setFlag(CPU.Flags.ZERO, resByte.isZero())

                    cpu.Y = resByte
                },
                Implied,
                0x00U
        )

// Jump to new Location
class JMP(addressMode: AddressMode, param: Word) :
        Instruction(
                "jmp",
                {   cpu, _ ->
                    cpu.pc = addressMode.getAddr(param)
                },
                addressMode,
                param
        )

// Jump to Subroutine pushing return addr to stack
class JSR(param: Word) :
        Instruction(
                "jsr",
                {   cpu, jsr ->
                    // push return addr to the stack
                    val returnAddr = cpu.pc
                    cpu.pushStack(returnAddr.upper)
                    cpu.pushStack(returnAddr.lower)

                    // jump to new location
                    cpu.pc = jsr.addressMode.getAddr(param)
                },
                JumpAbsolute,
                param
        )

// load acc from mem
// M -> A
class LDA(addressMode: AddressMode, param: Word) :
        Instruction(
                "lda",
                {   cpu, _ ->
                    val memValue = addressMode.read(param)

                    cpu.setFlag(CPU.Flags.NEGATIVE, memValue.isNeg())
                    cpu.setFlag(CPU.Flags.ZERO, memValue.isZero())

                    cpu.A = memValue
                },
                addressMode,
                param
        )

// load x from mem
// M -> X
class LDX(addressMode: AddressMode, param: Word) :
        Instruction(
                "ldx",
                {   cpu, _ ->
                    val memValue = addressMode.read(param)

                    cpu.setFlag(CPU.Flags.NEGATIVE, memValue.isNeg())
                    cpu.setFlag(CPU.Flags.ZERO, memValue.isZero())

                    cpu.X = memValue
                },
                addressMode,
                param
        )

// load y from mem
// M -> Y
class LDY(addressMode: AddressMode, param: Word) :
        Instruction(
                "ldy",
                {   cpu, _ ->
                    val memValue = addressMode.read(param)

                    cpu.setFlag(CPU.Flags.NEGATIVE, memValue.isNeg())
                    cpu.setFlag(CPU.Flags.ZERO, memValue.isZero())

                    cpu.Y = memValue
                },
                addressMode,
                param
        )

// Logic shift right (mem or acc)
// 0 -> [76543210] -> C
class LSR(addressMode: AddressMode, param: Word) :
        Instruction(
                "lsr",
                {   cpu, _ ->
                    val memValue = addressMode.read(param)

                    cpu.setFlag(CPU.Flags.CARRY, memValue and 1 == 1.toByte())

                    addressMode.write(param, (memValue.toInt() shr 1).toByte())
                },
                addressMode,
                param
        )

// No operation
class NOP :
    Instruction(
            "nop",
            {_, _ -> },
            Implied,
            0x00U
    )

// Or memory with acc
// A or M -> A
class ORA(addressMode: AddressMode, param: Word) :
        Instruction(
                "ora",
                {   cpu, _ ->
                    val memValue = addressMode.read(param)
                    val resByte = cpu.A or memValue

                    cpu.setFlag(CPU.Flags.NEGATIVE, resByte.isNeg())
                    cpu.setFlag(CPU.Flags.ZERO, resByte.isZero())

                    cpu.A = resByte
                },
                addressMode,
                param
        )

// Push acc to stack
class PHA :
        Instruction(
                "pha",
                {   cpu, _ ->
                    cpu.pushStack(cpu.A)
                },
                Implied,
                0x00U
        )

// push cpu status onto stack
class PHP :
        Instruction(
                "php",
                {   cpu, _ ->
                    cpu.pushStack(cpu.status)
                },
                Implied,
                0x00U
        )

// pull acc from stack
class PLA :
        Instruction(
                "pla",
                {   cpu, _ ->
                    val stackVal = cpu.popStack()

                    cpu.setFlag(CPU.Flags.NEGATIVE, stackVal.isNeg())
                    cpu.setFlag(CPU.Flags.ZERO, stackVal.isZero())

                    cpu.A = stackVal
                },
                Implied,
                0x00U
        )

// pull cpu status from stack
class PLP :
        Instruction(
                "plp",
                {   cpu, _ ->
                    cpu.status = cpu.popStack()
                },
                Implied,
                0x00U
        )

// rotate on bit left (mem or acc)
// C <- [76543210] <- C
class ROL(addressMode: AddressMode, param: Word) :
        Instruction(
                "rol",
                {   cpu, _ ->
                    val memValue = addressMode.read(param)
                    val resByte = (memValue.toInt() shl 1).toByte() or cpu.getFlag(CPU.Flags.CARRY)
                    val resInt = memValue.toInt() shl 1

                    cpu.setFlag(CPU.Flags.NEGATIVE, resByte.isNeg())
                    cpu.setFlag(CPU.Flags.ZERO, resByte.isZero())
                    cpu.setFlag(CPU.Flags.CARRY, resInt.hasCarried())

                    addressMode.write(param, resByte)
                },
                addressMode,
                param
        )

// rotate on bit right (mem or acc)
// C -> [76543210] -> C
class ROR(addressMode: AddressMode, param: Word) :
        Instruction(
                "ror",
                {   cpu, _ ->  
                    val memValue = addressMode.read(param)
                    val resByte = ((memValue.toInt() shr 1) or (cpu.getFlag(CPU.Flags.CARRY).toInt() shl 8)).toByte()

                    cpu.setFlag(CPU.Flags.NEGATIVE, resByte.isNeg())
                    cpu.setFlag(CPU.Flags.ZERO, resByte.isZero())
                    cpu.setFlag(CPU.Flags.CARRY, memValue[0] == 1.toByte())

                    addressMode.write(param, resByte)
                },
                addressMode,
                param
        )

/**
 * RTI  Return from Interrupt

pull SR, pull PC                 N Z C I D V
from stack

addressing    assembler    opc  bytes  cyles
--------------------------------------------
implied       RTI           40    1     6*/

// return from subroutine
class RTS :
        Instruction(
                "rts",
                {   cpu, _ ->
                    val lower = cpu.popStack()
                    val upper = cpu.popStack()

                    cpu.pc = wordFromBytes(lower, upper)
                },
                Implied,
                0x00U
        )


// subtract mem from acc with borrow
// A - M - ~C -> A
class SBC(addressMode: AddressMode, param: Word) :
        Instruction(
                "sbc",
                {   cpu, _ ->  
                    val memValue = addressMode.read(param)
                    val resByte = cpu.A byteMinus memValue byteMinus (cpu.getFlag(CPU.Flags.CARRY) - 1)

                    cpu.setFlag(CPU.Flags.NEGATIVE, resByte.isNeg())
                    cpu.setFlag(CPU.Flags.ZERO, resByte.isZero())
                    cpu.setFlag(CPU.Flags.CARRY, cpu.A > memValue ||
                            cpu.A == memValue && cpu.getFlag(CPU.Flags.CARRY) == 1.toByte())
                    cpu.setFlag(CPU.Flags.OVERFLOW, isOverflowedMinus(cpu.A, memValue, resByte))

                    cpu.A = resByte
                },
                addressMode,
                param
        )

// set carry
class SEC :
        Instruction(
                "sec",
                {   cpu, _ ->
                    cpu.setFlag(CPU.Flags.CARRY, true)
                },
                Implied,
                0x00U
        )

// set decimal mode
class SED :
        Instruction(
                "sed",
                {   cpu, _ ->
                    cpu.setFlag(CPU.Flags.DECIMAL, true)
                },
                Implied,
                0x00U
        )

// set decimal mode
class SEI :
        Instruction(
                "sei",
                {   cpu, _ ->
                    cpu.setFlag(CPU.Flags.INTERRUPT_DISABLE, true)
                },
                Implied,
                0x00U
        )

// store acc in mem
// A -> M
class STA(addressMode: AddressMode, param: Word) :
        Instruction(
                "sta",
                {   cpu, _ ->
                    addressMode.write(param, cpu.A)
                },
                addressMode,
                param
        )

// store x in mem
// X -> M
class STX(addressMode: AddressMode, param: Word) :
        Instruction(
                "stx",
                {   cpu, _ ->
                    addressMode.write(param, cpu.X)
                },
                addressMode,
                param
        )

// store y in mem
// Y -> M
class STY(addressMode: AddressMode, param: Word) :
        Instruction(
                "sty",
                {   cpu, _ ->
                    addressMode.write(param, cpu.Y)
                },
                addressMode,
                param
        )

// transfer acc to x
// A -> X
class TAX :
        Instruction(
                "tax",
                {   cpu, _ ->
                    cpu.X = cpu.A
                },
                Implied,
                0x00U
        )

// transfer acc to y
// A -> Y
class TAY :
        Instruction(
                "tay",
                {   cpu, _ ->
                    cpu.setFlag(CPU.Flags.NEGATIVE, cpu.A.isNeg())
                    cpu.setFlag(CPU.Flags.ZERO, cpu.A.isZero())

                    cpu.Y = cpu.A
                },
                Implied,
                0x00U
        )

// transfer stack pointer to x
// SP -> X
class TSX :
        Instruction(
                "tsx",
                {   cpu, _ ->
                    cpu.setFlag(CPU.Flags.NEGATIVE, cpu.sp.isNeg())
                    cpu.setFlag(CPU.Flags.ZERO, cpu.sp.isZero())

                    cpu.X = cpu.sp
                },
                Implied,
                0x00U
        )

// transfer x to acc
// X -> A
class TXA :
        Instruction(
                "txa",
                {   cpu, _ ->
                    cpu.setFlag(CPU.Flags.NEGATIVE, cpu.X.isNeg())
                    cpu.setFlag(CPU.Flags.ZERO, cpu.X.isZero())

                    cpu.A = cpu.X
                },
                Implied,
                0x00U
        )

// transfer X to stack pointer
// X -> SP
class TXS :
        Instruction(
                "txs",
                {   cpu, _ ->
                    cpu.sp = cpu.X
                },
                Implied,
                0x00U
        )

// transfer y to acc
// Y -> A
class TYA :
        Instruction(
                "tya",
                {   cpu, _ ->
                    cpu.setFlag(CPU.Flags.NEGATIVE, cpu.Y.isNeg())
                    cpu.setFlag(CPU.Flags.ZERO, cpu.Y.isZero())

                    cpu.A = cpu.Y
                },
                Implied,
                0x00U
        )