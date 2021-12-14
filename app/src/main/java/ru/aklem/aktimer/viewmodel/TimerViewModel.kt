package ru.aklem.aktimer.viewmodel

import android.app.Application
import android.media.AudioAttributes
import android.media.SoundPool
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.aklem.aktimer.R
import ru.aklem.aktimer.data.Chart
import ru.aklem.aktimer.misc.Period

class TimerViewModel(application: Application) : AndroidViewModel(application) {

    private var job: Job? = null
    private var soundPool: SoundPool? = null
    private var audioAttributes: AudioAttributes? = null
    private var sound: Int? = null

    private var _timerValue = MutableStateFlow(0)
    val timerValue = _timerValue.asStateFlow()

    private var _progressTime = MutableStateFlow(0)
    val progressTime = _progressTime.asStateFlow()
    var progressStartTime = 0

    private var _isRunning = MutableStateFlow(false)
    val isRunning = _isRunning.asStateFlow()

    private var _periods = mutableListOf<Period>()
    private var _currentPeriod = MutableStateFlow<Period?>(null)
    val currentPeriod = _currentPeriod.asStateFlow()
    private var index = 0

    init {
        audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
    }

    fun toggleStartPause() {
        initSound()
        val amountOfPeriods = _periods.size
        if (!_isRunning.value && amountOfPeriods > 0) {
            _isRunning.value = true
            if (_timerValue.value == 0 && index == 0) {
                _timerValue.value = _periods[0].time
            }
            job?.cancel()
            job = viewModelScope.launch(Dispatchers.Default) {
                delay(1000L)
                while (isActive) {
                    if (_timerValue.value <= 0) {
                        if (index == amountOfPeriods - 1) {
                            job?.cancel()
                            index = 0
                            _isRunning.value = false
                            _currentPeriod.value = _periods[0]
                            releaseSoundPool()
                            setProgressTime()
                        } else {
                            index += 1
                            _timerValue.value = _periods[index].time
                            _currentPeriod.value = _periods[index]
                        }
                    }
                    if (_timerValue.value > 0) {
                        _timerValue.value--
                        _progressTime.value--
                    }
                    if (_timerValue.value == 0 && _currentPeriod.value?.playSound == true) sound?.let {
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

    fun stop() {
        releaseSoundPool()
        job?.cancel()
        index = 0
        _timerValue.value = 0
        if (_isRunning.value) _isRunning.value = false
        if (_periods.isNotEmpty()) _currentPeriod.value = _periods[0]
        setProgressTime()
    }

    fun setTimerPeriods(chart: Chart) {
        if (_periods.isNotEmpty()) _periods.clear()
        _timerValue.value =
            if (chart.preparationTime > 0) chart.preparationTime else chart.actionTime
        if (chart.preparationTime > 0) {
            _periods.add(
                Period(
                    name = chart.headerPreparation,
                    time = chart.preparationTime,
                    playSound = chart.playPreparationSound
                )
            )
        }
        for (i in 0 until(chart.repeat)) {
            _periods.add(
                Period(
                    name = chart.headerAction,
                    time = chart.actionTime,
                    playSound = chart.playActionSound
                )
            )
            if (chart.restTime > 0) _periods.add(
                Period(
                    name = chart.headerRest,
                    time = chart.restTime,
                    playSound = chart.playRestSound
                )
            )
        }
        _currentPeriod.value = _periods[0]
        setProgressTime()
    }

    private fun setProgressTime() {
        _progressTime.value = _periods.fold(0) { acc, period -> acc + period.time }
        progressStartTime = _progressTime.value
    }

    private fun initSound() {
        if (soundPool == null) {
            soundPool = SoundPool.Builder().setAudioAttributes(audioAttributes).build()
            sound = soundPool?.load(getApplication(), R.raw.double_beep, 1)
        }
    }

    private fun releaseSoundPool() {
        soundPool?.release()
        soundPool = null
    }
}