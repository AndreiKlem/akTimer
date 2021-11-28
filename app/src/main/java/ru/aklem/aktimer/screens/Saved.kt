package ru.aklem.aktimer.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.aklem.aktimer.Chart

@Composable
fun SavedScreen(navController: NavController, charts: List<Chart>, onClick: (Int) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        itemsIndexed(items = charts) { index, chart ->
            ChartCard(
                navController = navController,
                chart = chart,
                index = index,
                onClick = onClick
            )
        }
    }
}

@Composable
fun ChartCard(navController: NavController, chart: Chart, index: Int, onClick: (Int) -> Unit) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            onClick(index)
            navController.navigate("timer")
        }) {
        Text(text = chart.title)
    }
}