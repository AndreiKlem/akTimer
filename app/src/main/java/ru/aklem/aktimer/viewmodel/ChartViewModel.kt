package ru.aklem.aktimer.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.aklem.aktimer.Chart

class ChartViewModel : ViewModel() {
    private val chart = Chart()
    private val _charts = mutableListOf(
        Chart(
            title = "Pancakes",
            headerPrepare = "Warming up a pan",
            prepareTime = 150,
            headerAction = "Cooking",
            actionTime = 120,
            headerRest = "Flipping",
            restTime = 10,
            repeat = 30
        ),
        Chart(
            title = "Squats",
            headerPrepare = "Getting ready",
            prepareTime = 10,
            headerAction = "Squatting",
            actionTime = 60,
            headerRest = "Resting",
            restTime = 30,
            repeat = 5
        ),
        Chart(
            title = "Push-ups",
            headerPrepare = "Getting ready",
            prepareTime = 10,
            headerAction = "Pushing-up",
            actionTime = 30,
            headerRest = "Resting",
            restTime = 30,
            repeat = 5
        ),
        Chart(
            title = "Running",
            headerAction = "RUN!",
            actionTime = 1800
        ),
        Chart(
            title = "Fast check",
            headerPrepare = "Preparing!",
            prepareTime = 5,
            headerAction = "Buzzing!",
            actionTime = 3,
            headerRest = "Resting!",
            restTime = 2,
            repeat = 3
        )
    )
    val charts = _charts
    var selectedChart = charts[0]

    private var _title = MutableStateFlow(chart.title)
    val title = _title.asStateFlow()
    private var _headerPrepare = MutableStateFlow(chart.headerPrepare)
    val headerPrepare = _headerPrepare.asStateFlow()
    private var _prepareTime = MutableStateFlow(chart.prepareTime)
    val prepareTime = _prepareTime.asStateFlow()
    private var _headerAction = MutableStateFlow(chart.headerAction)
    val headerAction = _headerAction.asStateFlow()
    private var _actionTime = MutableStateFlow(chart.actionTime)
    val actionTime = _actionTime.asStateFlow()
    private var _headerRest = MutableStateFlow(chart.headerRest)
    val headerRest = _headerRest.asStateFlow()
    private var _restTime = MutableStateFlow(chart.restTime)
    val restTime = _restTime.asStateFlow()
    private var _repeat = MutableStateFlow(chart.repeat)
    val repeat = _repeat.asStateFlow()

    fun onTitleChange(newTitle: String) {
        _title.value = newTitle
    }

    fun onHeaderPrepareChange(newHeader: String) {
        _headerPrepare.value = newHeader
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

    fun onRepeatChange(sets: String) {
        _repeat.value = sets.toInt()
    }

    fun createChart() {
        charts.add(chart)
        resetChart()
    }

    fun selectChart(index: Int) {
        selectedChart = charts[index]
    }

    private fun resetChart() {
        _title.value = ""
        _headerPrepare.value = ""
        _prepareTime.value = 0
        _headerAction.value = ""
        _actionTime.value = 0
        _headerRest.value = ""
        _restTime.value = 0
        _repeat.value = 0
    }
}