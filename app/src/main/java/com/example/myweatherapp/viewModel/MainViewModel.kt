package com.example.myweatherapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweatherapp.model.Weather
import com.example.myweatherapp.service.WeatherService
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val service = WeatherService()

    suspend fun displayWeather(): String {
            val weather = service.getWeather(44.84f, -0.58f)
            return "Weather: ${weather.hourly.temperature_2m[0]}°C"
    }
}