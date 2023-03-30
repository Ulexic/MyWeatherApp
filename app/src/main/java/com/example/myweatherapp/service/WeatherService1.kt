package com.example.myweatherapp.service

import android.util.Log
import com.example.myweatherapp.RetrofitClient
import com.example.myweatherapp.model.Weather
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherService1 {

    companion object {
        private var client = RetrofitClient.getInstance().create(WeatherService::class.java)
    }

    fun getWeather(longitude: Float, latitude: Float): Flow<Weather> = flow {
        Log.d("WeatherService", "getWeather: $longitude, $latitude")
        val response = client.forecast(longitude, latitude)
        Log.d("WeatherService", response.toString())
        if (response.isSuccessful) {
            emit(response.body()!!)
        } else {
            throw Exception("Error")
        }
    }
}