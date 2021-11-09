package ru.aklem.aktimer.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ru.aklem.aktimer.ui.theme.AkTimerTheme

@Composable
fun CreateScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Create", style = MaterialTheme.typography.h2)
    }
}

@Composable
@Preview
fun CreateScreenPreview() {
    AkTimerTheme {
        CreateScreen()
    }
}