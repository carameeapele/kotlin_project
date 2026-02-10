package com.example.domain.service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TimerService(
    private val scope: CoroutineScope
) {
    private var timerJob: Job? = null

    var remainingSeconds: Int = 0
        private set

    var isRunning: Boolean = false
        private set

    fun start(
        durationSeconds: Int,
        onTick: (Int) -> Unit,
        onFinished: () -> Unit
    ) {
        stop()
        remainingSeconds = durationSeconds
        isRunning = true

        timerJob = scope.launch {
            while (remainingSeconds > 0) {
                delay(1000)
                remainingSeconds--
                onTick(remainingSeconds)
            }
            isRunning = false
            onFinished()
        }
    }

    fun stop() {
        timerJob?.cancel()
        timerJob = null
        isRunning = false
    }

    fun reset() {
        stop()
        remainingSeconds = 0
    }
}