package me.kokokotlin.main.cpu

class Clock {
    private var currentTick = 0U
    var instructionDuration = 0U
    private var _delay = 1L

    var delay: Long
        get() = _delay
    set(value) {
        _delay = delay
    }

    fun tickOnce() {
        delay()
        ++currentTick
    }

    fun instructionFinished() = instructionDuration >= currentTick

    // 1 MHZ
    private fun delay() = Thread.sleep(_delay)
}