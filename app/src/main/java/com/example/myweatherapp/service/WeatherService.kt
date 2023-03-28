package com.example.myweatherapp.service

import com.example.myweatherapp.RetrofitClient
import com.example.myweatherapp.WeatherApi
import com.example.myweatherapp.model.Weather
import okhttp3.Request
import retrofit2.Retrofit

class WeatherService {

    companion object {
        private var client = RetrofitClient.getInstance().create(WeatherApi::class.java)
    }

    suspend fun getWeather(longitude: Float, latitude: Float): Weather {
        val response = client.forecast(longitude, latitude)
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw Exception("Error: ${response.code()}")
        }
    }
}