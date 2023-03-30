package com.example.myweatherapp.model

data class Weather(
    val longitude: Float,
    val latitude: Float,
    val daily: DailyWeather,
    val hourly: HourlyWeather,
    val current_weather: CurrentWeather,
)

data class CurrentWeather(
    val temperature: String,
    val windSpeed_10m: Float,
    val weathercode: WeatherCode,
    val time: String,
)

data class HourlyWeather(
    val time: List<String>,
    val apparent_temperature: List<Float>,
)

data class DailyWeather(
    val time: List<String>,
    val weathercode: List<WeatherCode>,
    val temperature_2m_max: List<Float>,
    val temperature_2m_min: List<Float>,
    val precipitation_sum: List<Float>,
    val windspeed_10m_max: List<Float>,
)
