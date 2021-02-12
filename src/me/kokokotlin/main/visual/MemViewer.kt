package me.kokokotlin.main.visual

import me.kokokotlin.main.*
import me.kokokotlin.main.cpu.CPU
import me.kokokotlin.main.drawing.Drawable
import me.kokokotlin.main.drawing.WindowHandler
import me.kokokotlin.main.geometry.Rectangle
import me.kokokotlin.main.mem.MEM_SIZE
import java.awt.Color
import java.awt.Font
import java.awt.Graphics
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.lang.Exception
import javax.swing.JOptionPane

class MemViewer : Drawable {
    private val width = 1280
    private val height = 720
    private val fontSize = 24

    private var startAddr = 0

    private var widthOfOneChar = 0
    private var heightOfOneChar = 0
    private var cols = 0
    private var rows = 0
    private var memValuePerRow = 0

    private val window = WindowHandler(width, height, "Memory View")

    init {
        window.drawables.add(this)
        window.requestFocus()

        window.addMouseWheelListener {
            if (startAddr == 0x0 && it.wheelRotation == -1) return@addMouseWheelListener

            startAddr += it.wheelRotation
        }

        window.addKeyListener(object : KeyAdapter() {
            override fun keyPressed(e: KeyEvent?) {
                if (e == null) return

                when (e.keyCode) {
                    KeyEvent.VK_A -> {
                        val res = JOptionPane.showInputDialog(
                            "Set new start address",
                            "0"
                        )

                        try {
                            startAddr = res.toInt(16) / memValuePerRow
                        } catch (e: Exception) {
                            printError("Couldn't parse int from input: $res")
                        }
                    }
                }
            }
        })
    }

    private fun drawMemView(g: Graphics, bounds: Rectangle) {
        g.color = Color.BLUE
        g.fillRect(bounds.rI.x, bounds.rI.y, bounds.rI.width, bounds.rI.height)

        widthOfOneChar = g.fontMetrics.stringWidth("0")
        heightOfOneChar = g.fontMetrics.height
        cols = bounds.rI.width / widthOfOneChar - 5     // -5 for addr
        rows = bounds.rI.height / heightOfOneChar
        memValuePerRow = cols / 3

        // draw mem addr
        for (i in 0 until rows) {
            val x = widthOfOneChar + bounds.rI.x
            val y = (i + 1) * heightOfOneChar + bounds.rI.y

            g.color = Color.BLACK
            g.drawString(((i + startAddr) * memValuePerRow).toString(16).padStart(4, '0') + ":", x, y)
        }

        // draw mem (highlight pc)
        for (i in 0 until memValuePerRow * rows) {
            val x = (i % memValuePerRow) * 3 * widthOfOneChar + widthOfOneChar * 7 + bounds.rI.x
            val y = (i / memValuePerRow + 1) * heightOfOneChar + bounds.rI.y

            val addr = i + (startAddr * memValuePerRow)
            if (addr >= MEM_SIZE) continue

            try {
                val memValue = mem.read(addr.toWord())

                if (cpu.pc == addr.toWord()) {
                    g.color = Color.RED
                    g.fillRect(x, y - heightOfOneChar, widthOfOneChar * 2, heightOfOneChar)
                } else if (memValue != 0x0.toByte()) {
                    g.color = Color.YELLOW
                    g.fillRect(x, y - heightOfOneChar, widthOfOneChar * 2, heightOfOneChar)
                }
                g.color = Color.BLACK
                g.drawString("${memValue.repr(16)} ", x, y)
            } catch (e: Exception) {
            }
        }

        g.color = Color.BLACK
    }

    private fun drawRegView(g: Graphics, bounds: Rectangle) {
        g.color = Color.BLUE
        g.fillRect(bounds.rI.x, bounds.rI.y, bounds.rI.width, bounds.rI.height)

        // draw regs
        g.color = Color.BLACK
        val a = "A: ${cpu.A.repr(2)} (${cpu.A.repr(10)})"
        val x = "X: ${cpu.X.repr(2)} (${cpu.X.repr(10)})"
        val y = "Y: ${cpu.Y.repr(2)} (${cpu.Y.repr(10)})"
        val pc = "pc: ${cpu.pc.toString(16).padStart(4, '0')}"
        val status = "status: [ " +
                (if (cpu.getFlag(CPU.Flags.CARRY).isZero()) "0" else "C") +
                (if (cpu.getFlag(CPU.Flags.ZERO).isZero()) "0" else "Z") +
                (if (cpu.getFlag(CPU.Flags.INTERRUPT_DISABLE).isZero()) "0" else "I") +
                (if (cpu.getFlag(CPU.Flags.DECIMAL).isZero()) "0" else "D") +
                (if (cpu.getFlag(CPU.Flags.OVERFLOW).isZero()) "0" else "O") +
                (if (cpu.getFlag(CPU.Flags.NEGATIVE).isZero()) "0" else "N") +
                " ]"

        g.drawString(a, widthOfOneChar * 2, heightOfOneChar * 1)
        g.drawString(x, widthOfOneChar * 2, heightOfOneChar * 2)
        g.drawString(y, widthOfOneChar * 2, heightOfOneChar * 3)
        g.drawString(pc, widthOfOneChar * 2, heightOfOneChar * 5)
        g.drawString(status, widthOfOneChar * 2, heightOfOneChar * 7)

        val separatorWidth = 10
        g.fillRect(bounds.rI.width - separatorWidth, bounds.rI.y, separatorWidth, bounds.rI.height)
    }

    override fun draw(g: Graphics) {
        g.font = Font("Courier New", Font.BOLD, fontSize)

        drawRegView(g, Rectangle(0.0, 0.0, 300.0, height.toDouble()))
        drawMemView(g, Rectangle(300.0, 0.0, width.toDouble(), height.toDouble()))
    }
}