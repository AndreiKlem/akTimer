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
    val headerPreparation = _headerPreparation.asStateFlow()
    private var _headerAction = MutableStateFlow(chart.headerAction)
    val headerAction = _headerAction.asStateFlow()
    private var _headerRest = MutableStateFlow(chart.headerRest)
    val headerRest = _headerRest.asStateFlow()
    private var _prepareTime = MutableStateFlow(chart.preparationTime)
    val prepareTime = _prepareTime.asStateFlow()
    private var _actionTime = MutableStateFlow(chart.actionTime)
    val actionTime = _actionTime.asStateFlow()
    private var _restTime = MutableStateFlow(chart.restTime)
    val restTime = _restTime.asStateFlow()
    private var _repeat = MutableStateFlow(chart.repeat)
    private var _playPreparationSound = MutableStateFlow(chart.playPreparationSound)
    private var _playActionSound = MutableStateFlow(chart.playActionSound)
    private var _playRestSound = MutableStateFlow(chart.playRestSound)
    val repeat = _repeat.asStateFlow()

    fun onTitleChange(newTitle: String) {
        _title.value = newTitle
    }

    fun onHeaderPrepareChange(newHeader: String) {
        _headerPreparation.value = newHeader
    }

    fun onPrepareTimeChange(minutes: Int, seconds: Int) {
        _prepareTime.value = minutes * 60 + seconds
    }

    fun onHeaderActionChange(newHeader: String) {
        _headerAction.value = newHeader
    }

    fun onActionTimeChange(minutes: Int, seconds: Int) {
        _actionTime.value = minutes * 60 + seconds
    }

    fun onHeaderRestChange(newHeader: String) {
        _headerRest.value = newHeader
    }

    fun onRestTimeChange(minutes: Int, seconds: Int) {
        _restTime.value = minutes * 60 + seconds
    }

    fun onPlaySoundChange(key: ChartPeriods) {
        when(key) {
            ChartPeriods.PREPARATION -> _playPreparationSound.value = !_playPreparationSound.value
            ChartPeriods.ACTION -> _playActionSound.value = !_playActionSound.value
            ChartPeriods.REST -> _playRestSound.value = !_playRestSound.value
        }
    }

    fun getPlaySoundStatus(key: ChartPeriods): StateFlow<Boolean> {
        return when(key) {
            ChartPeriods.PREPARATION -> _playPreparationSound.asStateFlow()
            ChartPeriods.ACTION -> _playActionSound.asStateFlow()
            ChartPeriods.REST -> _playRestSound.asStateFlow()
        }
    }

    fun onRepeatChange(sets: String) {
        _repeat.value = sets.toInt()
    }

    fun createChart() {
        viewModelScope.launch {
            repository.addChart(Chart(
                title = title.value,
                headerPreparation = headerPreparation.value,
                preparationTime = prepareTime.value,
                headerAction = headerAction.value,
                actionTime = actionTime.value,
                headerRest = headerRest.value,
                restTime = restTime.value,
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

    fun resetChart() {
        _title.value = ""
        _headerPreparation.value = ""
        _headerAction.value = ""
        _headerRest.value = ""
        _prepareTime.value = 0
        _actionTime.value = 0
        _restTime.value = 0
        _repeat.value = 0
    }
}