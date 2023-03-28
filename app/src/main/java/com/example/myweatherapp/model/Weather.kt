package com.example.myweatherapp.model

data class Weather(
    val longitude: Float,
    val latitude: Float,
    val hourly: Hourly,
)

data class Hourly(
    val time: List<String>,
    val apparent_temperature: List<Float>,
    val temperature_2m: List<Float>,
)
