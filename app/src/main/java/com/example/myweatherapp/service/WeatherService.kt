package com.example.myweatherapp.service

import com.example.myweatherapp.model.Weather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("forecast")
    suspend fun forecast(
        @Query("latitude") latitude: Float,
        @Query("longitude") longitude: Float,
        @Query("hourly") hourly: String = "apparent_temperature",
        @Query("daily") daily: String = "weathercode,temperature_2m_max,temperature_2m_min,apparent_temperature_max,apparent_temperature_min,precipitation_sum,windspeed_10m_max",
        @Query("current_weather") current_weather: String = "true",
        @Query("forecast_days") forecast_days: String = "7",
        @Query("timezone") timezone: String = "Europe/London",
    ): Response<Weather>
}