package ru.aklem.aktimer

class Chart(
    val title: String = "Title",
    var headerPrepare: String = "Preparation",
    var prepare: Int = 0,
    var headerWork: String = "Work",
    var work: Int = 0,
    var headerRest: String = "Rest",
    var rest: Int = 0,
    var repeat: Int = 0
)