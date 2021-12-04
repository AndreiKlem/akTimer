package ru.aklem.aktimer.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chart_database")
data class Chart(

    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "title") val title: String = "Title",
    @ColumnInfo(name = "header_preparation") val headerPreparation: String = "Preparation",
    @ColumnInfo(name = "preparation_time") val preparationTime: Int = 0,
    @ColumnInfo(name = "header_action") val headerAction: String = "Action",
    @ColumnInfo(name = "action_time") val actionTime: Int = 0,
    @ColumnInfo(name = "header_rest") val headerRest: String = "Rest",
    @ColumnInfo(name = "rest_time") val restTime: Int = 0,
    @ColumnInfo(name = "repetitions_amount") val repeat: Int = 0
)