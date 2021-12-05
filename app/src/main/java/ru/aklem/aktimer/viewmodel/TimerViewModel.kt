package ru.aklem.aktimer.viewmodel

import android.app.Application
import android.content.ContentValues.TAG
import android.media.AudioAttributes
import android.media.SoundPool
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.aklem.aktimer.R
import ru.aklem.aktimer.misc.Period

class TimerViewModel(application: Application) : AndroidViewModel(application) {

    private var job: Job? = null
    private var soundPool: SoundPool? = null
    private var audioAttributes: AudioAttributes? = null
    private var sound: Int? = null

    private var _timerValue = MutableStateFlow(0)
    val timerValue = _timerValue.asStateFlow()

    private var _isRunning = MutableStateFlow(false)
    val isRunning = _isRunning.asStateFlow()

    private var index = 0

    init {
        audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
    }

    fun toggleStartPause(periods: List<Period>) {
        initSound()
        val amountOfPeriods = periods.size - 1
        if (!_isRunning.value && amountOfPeriods > 0) {
            _isRunning.value = true
            if (_timerValue.value == 0 && index == 0) _timerValue.value = periods[index].time
            job?.cancel()
            job = viewModelScope.launch(Dispatchers.Default) {
                while (isActive) {
                    if (_timerValue.value <= 0) {
                        if (index == amountOfPeriods) {
                            job?.cancel()
                            index = 0
                            _isRunning.value = false
                            releaseSoundPool()
                        } else {
                            index += 1
                            _timerValue.value = periods[index].time
                        }
                    }
                    if (_timerValue.value > 0) _timerValue.value--
                    if (_timerValue.value == 0) sound?.let {
                        soundPool?.play(it, 1f, 1f, 0, 0, 1f)
                    }
                    delay(1000L)
                }
            }
        } else {
            job?.cancel()
            _isRunning.value = false
            releaseSoundPool()
        }
    }

    private fun initSound() {
        if (soundPool == null) {
            soundPool = SoundPool.Builder().setAudioAttributes(audioAttributes).build()
            sound = soundPool?.load(getApplication(), R.raw.double_beep, 1)
        }
    }

    fun stop() {
        releaseSoundPool()
        job?.cancel()
        index = 0
        _timerValue.value = 0
        if (_isRunning.value) _isRunning.value = false
    }

    private fun releaseSoundPool() {
        soundPool?.release()
        soundPool = null
    }
}