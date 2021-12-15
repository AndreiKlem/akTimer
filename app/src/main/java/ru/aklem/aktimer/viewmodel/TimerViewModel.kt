package ru.aklem.aktimer.viewmodel

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.aklem.aktimer.R
import ru.aklem.aktimer.data.Chart
import ru.aklem.aktimer.misc.Period
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(@ApplicationContext context: Context) : ViewModel() {

    private var job: Job? = null
    private var soundPool: SoundPool? = null
    private var sound: Int?

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
        val audioAttributes: AudioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        soundPool = SoundPool.Builder().setAudioAttributes(audioAttributes).build()
        sound = soundPool?.load(context, R.raw.double_beep, 1)
    }

    override fun onCleared() {
        super.onCleared()
        soundPool?.release()
        soundPool = null
    }

    fun toggleStartPause() {
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
        }
    }

    fun stop() {
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
        for (i in 0 until (chart.repeat)) {
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
}