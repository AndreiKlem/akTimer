package ru.aklem.aktimer.screens

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ru.aklem.aktimer.data.Chart

@Composable
fun SavedScreen(navController: NavController, charts: List<Chart>?, onClick: (Int) -> Unit) {
    if (charts.isNullOrEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "No timers created yet",
                style = TextStyle(fontSize = 20.sp),
                textAlign = TextAlign.Center
            )
        }
    } else {
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
}

@Composable
fun ChartCard(navController: NavController, chart: Chart, index: Int, onClick: (Int) -> Unit) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            onClick(index)
            navController.navigate("timer")
        }) {
        Column {
            Text(text = "id = ${chart.id} title = ${chart.title}")
        }
    }
}