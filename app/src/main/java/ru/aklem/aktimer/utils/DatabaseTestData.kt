package ru.aklem.aktimer.utils

import ru.aklem.aktimer.data.Chart

fun getTestCharts(): List<Chart> {
    return listOf(
        Chart(
            title = "Pancakes",
            headerPreparation = "Heating pan",
            headerAction = "Frying",
            headerRest = "Flipping",
            preparationTime = 120,
            actionTime = 30,
            restTime = 10,
            repeat = 30
        ),
        Chart(
            title = "Push-ups",
            headerPreparation = "Preparing",
            headerAction = "Pushing",
            headerRest = "Resting",
            preparationTime = 10,
            actionTime = 20,
            restTime = 20,
            playPreparationSound = false,
            repeat = 10
        ),
        Chart(
            title = "Squats",
            headerPreparation = "Preparing",
            headerAction = "Squatting",
            headerRest = "Resting",
            playRestSound = false,
            preparationTime = 10,
            actionTime = 60,
            restTime = 30,
            repeat = 5
        ),
        Chart(
            title = "Running",
            headerAction = "Run!",
            actionTime = 1800,
        ),
        Chart(
            title = "Sound test no sound",
            headerPreparation = "Preparing no sound",
            headerAction = "Acting no sound",
            headerRest = "Resting no sound",
            playPreparationSound = false,
            playActionSound = false,
            playRestSound = false,
            preparationTime = 5,
            actionTime = 5,
            restTime = 5,
            repeat = 3
        ),
        Chart(
            title = "Sound test with sound",
            headerPreparation = "Preparing with sound",
            headerAction = "Acting with sound",
            headerRest = "Resting with sound",
            preparationTime = 5,
            actionTime = 5,
            restTime = 5,
            repeat = 3
        ),
        Chart(
            title = "Really really really long headers test",
            headerPreparation = "Really really long preparation header",
            headerAction = "Really really really long action header",
            headerRest = "Really really really long rest header",
            preparationTime = 5,
            actionTime = 5,
            restTime = 5,
            repeat = 3
        ),
        Chart(
            title = "One repetition test",
            headerPreparation = "Preparing",
            headerAction = "Acting",
            headerRest = "Resting",
            preparationTime = 5,
            actionTime = 5,
            restTime = 5,
            repeat = 1
        ),
        Chart(
            title = "One repetition no sound",
            headerPreparation = "Preparing",
            headerAction = "Acting",
            headerRest = "Resting",
            playPreparationSound = false,
            playActionSound = false,
            playRestSound = false,
            preparationTime = 3,
            actionTime = 3,
            restTime = 3,
            repeat = 1
        ),
        Chart(
            title = "No repetitions test",
            headerPreparation = "Preparing",
            headerAction = "Acting",
            headerRest = "Resting",
            preparationTime = 5,
            actionTime = 5,
            restTime = 5,
            repeat = 1
        ),
        Chart(
            title = "Two repetitions test",
            headerPreparation = "Preparing",
            headerAction = "Acting",
            headerRest = "Resting",
            preparationTime = 5,
            actionTime = 5,
            restTime = 5,
            repeat = 2
        ),
    )
}