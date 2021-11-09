package ru.aklem.aktimer.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TimerViewModel : ViewModel() {

    private var job: Job? = null
    private var _timerValue = MutableStateFlow(0)
    val timerValue = _timerValue.asStateFlow()

    private var _isRunning = MutableStateFlow(false)
    val isRunning = _isRunning.asStateFlow()

    fun start(counter: Int) {
        if (!_isRunning.value) {
            _isRunning.value = true
            if (_timerValue.value == 0) _timerValue.value = counter
            job?.cancel()
            job = viewModelScope.launch(Dispatchers.Default) {
                while (isActive) {
                    if (_timerValue.value <= 0) {
                        job?.cancel()
                        _isRunning.value = false
                        Log.d(TAG, "Job cancelling...")
                    }
                    delay(1000L)
                    _timerValue.value -= 1
                    Log.d(TAG, "_timerValue = ${_timerValue.value}")
                }
            }
        }
    }

    fun stop() {
        job?.cancel()
        _timerValue.value = 0
        if (_isRunning.value) _isRunning.value = false
    }

    fun pause() {
        if (_isRunning.value) {
            job?.cancel()
            _isRunning.value = false
        }
    }
}