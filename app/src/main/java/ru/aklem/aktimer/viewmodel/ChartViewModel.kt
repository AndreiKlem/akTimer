package ru.aklem.aktimer.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.aklem.aktimer.Chart

class ChartViewModel : ViewModel() {
    private val chart = Chart()

    private var _title = MutableStateFlow(chart.title)
    val title = _title.asStateFlow()
    private var _headerPrepare = MutableStateFlow(chart.headerPrepare)
    val headerPrepare = _headerPrepare.asStateFlow()
    private var _headerAction = MutableStateFlow(chart.headerAction)
    val headerAction = _headerAction.asStateFlow()
    private var _headerRest = MutableStateFlow(chart.headerRest)
    val headerRest = _headerRest.asStateFlow()

    fun onTitleChange(newTitle: String) {
        _title.value = newTitle
    }

    fun onHeaderPrepareChange(newHeader: String) {
        _headerPrepare.value = newHeader
    }

    fun onHeaderActionChange(newHeader: String) {
        _headerAction.value = newHeader
    }

    fun onHeaderRestChange(newHeader: String) {
        _headerRest.value = newHeader
    }
}