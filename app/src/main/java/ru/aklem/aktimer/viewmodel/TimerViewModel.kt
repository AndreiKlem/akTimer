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

    private var index = 0

    fun toggleStartPause(periods: List<Int>) {
        val amountOfPeriods = periods.size - 1
        if (!_isRunning.value && amountOfPeriods > 0) {
            _isRunning.value = true
            if (_timerValue.value == 0 && index == 0) _timerValue.value = periods[index]
            job?.cancel()
            job = viewModelScope.launch(Dispatchers.Default) {
                while (isActive) {
                    if (_timerValue.value <= 0) {
                        if (index == amountOfPeriods) {
                            job?.cancel()
                            index = 0
                            _isRunning.value = false
                            Log.d(TAG, "Job cancelling...")
                        } else {
                            index += 1
                            _timerValue.value = periods[index]
                        }
                    }
                    delay(1000L)
                    _timerValue.value -= 1
                    Log.d(TAG, "_timerValue = ${_timerValue.value}")
                }
            }
        } else {
            job?.cancel()
            _isRunning.value = false
        }
    }

    fun stop() {
        job?.cancel()
        index = 0
        _timerValue.value = 0
        if (_isRunning.value) _isRunning.value = false
    }
}