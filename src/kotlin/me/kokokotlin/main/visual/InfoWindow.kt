package me.kokokotlin.main.visual

import me.kokokotlin.main.drawing.Drawable
import me.kokokotlin.main.drawing.WindowHandler
import me.kokokotlin.main.getNumInput
import me.kokokotlin.main.toWord
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent

abstract class InfoWindow(
    title: String,
    protected var width: Int,
    protected var height: Int
): Drawable {
    protected val window = WindowHandler(width, height, title)

    init {
        window.addKeyListener(object : KeyAdapter() {
            override fun keyPressed(e: KeyEvent?) {
                if (e == null) return

                // TODO: resize parent window
                when (e.keyCode) {
                    KeyEvent.VK_W -> getNumInput("New width", 10, "1000")?.also { width = it }
                    KeyEvent.VK_H -> getNumInput("New height", 10, "1000")?.also { height = it }
                    KeyEvent.VK_S -> getNumInput("New size", 10, "1000")
                        ?.also {
                            width = it
                            height = it
                        }
                }
            }
        })
    }
}