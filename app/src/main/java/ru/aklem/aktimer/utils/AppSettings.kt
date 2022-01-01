package ru.aklem.aktimer.utils

data class AppSettings(
    val showTitle: Boolean = true,
    val showPeriods: Boolean = true,
    val showProgressBar: Boolean = true,
    val showPreparation: Boolean = true,
    val showRest: Boolean = true,
    val userSound: String = ""
)
