package ru.aklem.aktimer.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.aklem.aktimer.Chart
import ru.aklem.aktimer.viewmodel.ChartViewModel

@Composable
fun SavedScreen(chartViewModel: ChartViewModel) {
    LazyColumn(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(items = chartViewModel.charts) { chart ->
            ChartCard(chart = chart)
        }
    }
}

@Composable
fun ChartCard(chart: Chart) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Text(text = chart.title)
    }
}