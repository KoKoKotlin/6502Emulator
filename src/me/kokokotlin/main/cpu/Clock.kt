package me.kokokotlin.main.cpu

class Clock {
    private var currentTick = 0U
    var instructionDuration = 0U

    fun tickOnce() {
        delay()
        ++currentTick
    }

    fun instructionFinished() = instructionDuration >= currentTick

    // 1 MHZ
    private fun delay() = Thread.sleep(1)
}