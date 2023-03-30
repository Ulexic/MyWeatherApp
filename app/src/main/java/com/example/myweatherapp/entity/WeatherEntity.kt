package com.example.myweatherapp.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myweatherapp.model.DailyWeather
import com.example.myweatherapp.model.WeatherCode

@Entity(tableName = "weather")
data class WeatherEntity(
    @PrimaryKey
    @ColumnInfo(name = "time")
    val time: String,
    @ColumnInfo(name = "weatherCode")
    val weathercode: WeatherCode,
    @ColumnInfo(name = "temperatureMax")
    val temperature_2m_max: Float,
    @ColumnInfo(name = "temperatureMin")
    val temperature_2m_min: Float,
    @ColumnInfo(name = "precipitation")
    val precipitation_sum: Float,
    @ColumnInfo(name = "windSpeed")
    val windspeed_10m_max: Float,
)

fun WeatherEntity.toWeather() = DailyWeather(
    time = listOf(time),
    weathercode = listOf(weathercode),
    temperature_2m_max = listOf(temperature_2m_max),
    temperature_2m_min = listOf(temperature_2m_min),
    precipitation_sum = listOf(precipitation_sum),
    windspeed_10m_max = listOf(windspeed_10m_max),
)