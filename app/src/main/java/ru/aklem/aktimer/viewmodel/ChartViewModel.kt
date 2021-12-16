package ru.aklem.aktimer.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.aklem.aktimer.data.Chart
import ru.aklem.aktimer.data.ChartDatabase
import ru.aklem.aktimer.data.ChartRepository
import ru.aklem.aktimer.misc.ChartPeriods

@InternalCoroutinesApi
class ChartViewModel(application: Application) : AndroidViewModel(application) {

    private val chart = Chart()
    private val _charts: LiveData<List<Chart>>
    private val repository: ChartRepository

    init {
        val chartDao = ChartDatabase.getInstance(application).chartDao()
        repository = ChartRepository(chartDao)
        _charts = repository.readAllData
    }

    val charts = _charts
    var selectedChart: Chart? = null

    private var _title = MutableStateFlow(chart.title)
    val title = _title.asStateFlow()
    private var _headerPreparation = MutableStateFlow(chart.headerPreparation)
    private var _headerAction = MutableStateFlow(chart.headerAction)
    private var _headerRest = MutableStateFlow(chart.headerRest)
    private var _preparationTime = MutableStateFlow(chart.preparationTime)
    private var _actionTime = MutableStateFlow(chart.actionTime)
    private var _restTime = MutableStateFlow(chart.restTime)
    private var _playPreparationSound = MutableStateFlow(chart.playPreparationSound)
    private var _playActionSound = MutableStateFlow(chart.playActionSound)
    private var _playRestSound = MutableStateFlow(chart.playRestSound)
    private var _repeat = MutableStateFlow(chart.repeat)
    val repeat = _repeat.asStateFlow()

    fun onSetTitle(newTitle: String) {
        _title.value = newTitle
    }

    fun onSetHeader(key: ChartPeriods, header: String) {
        when(key) {
            ChartPeriods.PREPARATION -> _headerPreparation.value = header
            ChartPeriods.ACTION -> _headerAction.value = header
            ChartPeriods.REST -> _headerRest.value = header
        }
    }

    fun getHeader(key: ChartPeriods): StateFlow<String> {
        return when(key) {
            ChartPeriods.PREPARATION -> _headerPreparation.asStateFlow()
            ChartPeriods.ACTION -> _headerAction.asStateFlow()
            ChartPeriods.REST -> _headerRest.asStateFlow()
        }
    }

    fun onSetTime(key: ChartPeriods, minutes: Int, seconds: Int) {
        when(key) {
            ChartPeriods.PREPARATION -> _preparationTime.value = minutes * 60 + seconds
            ChartPeriods.ACTION -> _actionTime.value = minutes * 60 + seconds
            ChartPeriods.REST -> _restTime.value = minutes * 60 + seconds
        }
    }

    fun getTime(key: ChartPeriods): StateFlow<Int> {
        return when(key) {
            ChartPeriods.PREPARATION -> _preparationTime.asStateFlow()
            ChartPeriods.ACTION -> _actionTime.asStateFlow()
            ChartPeriods.REST -> _restTime.asStateFlow()
        }
    }

    fun onSetPlaySound(key: ChartPeriods) {
        when(key) {
            ChartPeriods.PREPARATION -> _playPreparationSound.value = !_playPreparationSound.value
            ChartPeriods.ACTION -> _playActionSound.value = !_playActionSound.value
            ChartPeriods.REST -> _playRestSound.value = !_playRestSound.value
        }
    }

    fun getPlaySound(key: ChartPeriods): StateFlow<Boolean> {
        return when(key) {
            ChartPeriods.PREPARATION -> _playPreparationSound.asStateFlow()
            ChartPeriods.ACTION -> _playActionSound.asStateFlow()
            ChartPeriods.REST -> _playRestSound.asStateFlow()
        }
    }

    fun onSetRepeat(sets: String) {
        _repeat.value = sets.toInt()
    }

    fun createChart() {
        viewModelScope.launch {
            repository.addChart(Chart(
                title = title.value,
                headerPreparation = _headerPreparation.value,
                headerAction = _headerAction.value,
                headerRest = _headerRest.value,
                preparationTime = _preparationTime.value,
                actionTime = _actionTime.value,
                restTime = _restTime.value,
                playPreparationSound = _playPreparationSound.value,
                playActionSound = _playActionSound.value,
                playRestSound = _playRestSound.value,
                repeat = if (repeat.value > 1) repeat.value else 1
            ))
        }
    }

    fun insertTestChartsToDatabase(chartList: List<Chart>) {
        viewModelScope.launch {
            for (item in chartList) {
                repository.addChart(item)
            }
        }
    }

    fun onSelectChart(chart: Chart) {
        selectedChart = chart
    }
}