package ru.aklem.aktimer

data class Chart(
    var title: String = "Title",
    var headerPrepare: String = "Preparation",
    var prepareTime: Int = 0,
    var headerAction: String = "Action",
    var actionTime: Int = 0,
    var headerRest: String = "Rest",
    var restTime: Int = 0,
    var repeat: Int = 0
)