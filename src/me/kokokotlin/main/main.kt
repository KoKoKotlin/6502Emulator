package me.kokokotlin.main

import me.kokokotlin.main.asm.loadBytesFromPath
import me.kokokotlin.main.cpu.CPU
import me.kokokotlin.main.mem.Memory
import me.kokokotlin.main.visual.MemViewer
import me.kokokotlin.main.visual.VRamView
import java.lang.Exception
import java.nio.file.Paths

val cpu = CPU()
val mem = Memory()

fun main() {
    repl()
}

fun printError(msg: String) {
    println("ERROR: $msg")
}

fun repl() {
    while(true) {
        print("$>")
        val input = readLine()


        if (input != "") {
            val options = input!!.split(" ")
            when(input.split(" ")[0]) {
                "q" -> break
                "mem" -> {
                    if(options.size < 3) {
                        printError("mem needs 2 args: mem start len")
                        continue
                    }
                    try {
                        val start = options[1].toInt(16)
                        val len = options[2].toInt()

                        for (i in start..(start + len)) {
                            if ((i - start) % 32 == 0) println()
                            mem.read(i.toWord()).print(radix = 16)
                            print(" ")
                        }
                    } catch (e: Exception) {
                        printError("ERROR in mem: ${e.message}")
                    }

                    println()
                }
                "set" -> {
                    if(options.size < 3) {
                        printError("mem needs 2 args: mem start len")
                        continue
                    }

                    try {
                        val addr = options[1].toInt(16).toWord()
                        val value = options[2].toUByte(16).toByte()

                        mem.write(addr, value)
                    } catch (e: Exception) {
                        printError("ERROR in mem: ${e.message}")
                    }
                }
                "init" -> cpu.resetCPU()
                "r" -> cpu.run()
                "s" -> cpu.singleStep()
                "a" -> println("${cpu.A.repr()} (= ${cpu.A})")
                "x" -> println("${cpu.X.repr()} (= ${cpu.X})")
                "y" -> println("${cpu.Y.repr()} (= ${cpu.Y})")
                "sp" -> println(cpu.sp.repr())
                "status" -> {
                    print(cpu.status.toString(2).padStart(8, '0'))
                    println(" [ " +
                            (if(cpu.getFlag(CPU.Flags.CARRY).isZero()) "0" else "C") + " " +
                            (if(cpu.getFlag(CPU.Flags.ZERO).isZero()) "0" else "Z") + " " +
                            (if(cpu.getFlag(CPU.Flags.INTERRUPT_DISABLE).isZero()) "0" else "I") + " " +
                            (if(cpu.getFlag(CPU.Flags.DECIMAL).isZero()) "0" else "D") + " " +
                            (if(cpu.getFlag(CPU.Flags.OVERFLOW).isZero()) "0" else "O") + " " +
                            (if(cpu.getFlag(CPU.Flags.NEGATIVE).isZero()) "0" else "N") +
                            " ]")
                }
                "pc"  -> println("0x" + cpu.pc.toString(16).padStart(4, '0'))
                "load" -> {
                    if(options.size < 2) {
                        printError("load needs 1 args: load filePath")
                        continue
                    }
                    val data = try {
                        val path = Paths.get(options[1])
                        loadBytesFromPath(path)
                    } catch (e: Exception) {
                        printError("ERROR in load: Maybe your file does not exist?")
                        continue
                    }

                    mem.write(0U, data)
                    println("Written ${data.size} bytes to memory!")
                }
                "memview" -> MemViewer()
                "vram" -> VRamView()
                else -> {
                    printError("Command $input does not exists!")
                }
            }
        }
    }
}