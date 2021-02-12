package me.kokokotlin.main.cpu

class Clock {
    private var currentTick = 0U
    var instructionDuration: UInt = 0U
        set(value) {
            field = value
            currentTick = 0U
        }

    private var _delay = 1L

    var delay: Long
        get() = _delay
    set(value) {
        _delay = value
    }

    fun tickOnce() {
        delay()
        ++currentTick
    }

    fun instructionFinished() = currentTick >= instructionDuration

    // 1 MHZ
    private fun delay() = Thread.sleep(_delay)
}