package me.kokokotlin.main.visual

import me.kokokotlin.main.*
import me.kokokotlin.main.mem.MEM_SIZE
import java.awt.Color
import java.awt.Graphics
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent

class VRamView : InfoWindow("VRamViewer", 1000, 1000) {
    private var startAddr = 0
    private var cellSize = 10

    init {
        window.drawables.add(this)
        window.requestFocus()

        window.addKeyListener(object : KeyAdapter() {
            override fun keyPressed(e: KeyEvent?) {
                if (e == null) return

                when (e.keyCode) {
                    KeyEvent.VK_A -> getNumInput("New start addr", 16, "0")?.also { startAddr = it }
                }
            }
        })
    }

    private fun rgbFromMem(value: Byte): Color {
        val r = (255.0 / 8 * (value.toInt() shr 5 and 0b111)).toInt()
        val g = (255.0 / 8 * (value.toInt() shr 2 and 0b111)).toInt()
        val b = (255.0 / 4 * (value.toInt() and 0b11)).toInt()

        return Color(r, g, b)
    }

    override fun draw(g: Graphics) {
        val cols = width / cellSize
        val rows = height / cellSize

        for(i in 0 until (rows * cols)) {
            val addr = startAddr + i
            if(addr >= MEM_SIZE) continue

            val memValue = mem.read(addr.toWord())

            val x = (i % cols) * cellSize
            val y = (i / cols) * cellSize

            val rgbColor = rgbFromMem(memValue)
            g.color = rgbColor
            g.fillRect(x, y, cellSize, cellSize)
        }
    }
}