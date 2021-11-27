package ru.aklem.aktimer

data class Chart(
        var title: String = "Title",
        var headerPrepare: String = "Preparation",
        var prepare: Int = 0,
        var headerAction: String = "Action",
        var work: Int = 0,
        var headerRest: String = "Rest",
        var rest: Int = 0,
        var repeat: Int = 0
)