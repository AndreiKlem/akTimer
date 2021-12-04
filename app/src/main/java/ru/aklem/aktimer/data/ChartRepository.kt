package ru.aklem.aktimer.data

import androidx.lifecycle.LiveData

class ChartRepository(private val chartDao: ChartDao) {
    val readAllData: LiveData<List<Chart>> = chartDao.getAll()

    suspend fun addChart(chart: Chart) {
        chartDao.insert(chart)
    }

    suspend fun updateChart(chart: Chart) {
        chartDao.update(chart)
    }

    suspend fun deleteChart(chart: Chart) {
        chartDao.delete(chart)
    }

    suspend fun deleteAllCharts() {
        chartDao.deleteAllCharts()
    }
}