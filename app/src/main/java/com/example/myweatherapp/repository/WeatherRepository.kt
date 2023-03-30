package com.example.myweatherapp.repository

import androidx.annotation.WorkerThread
import com.example.myweatherapp.dao.WeatherDao
import com.example.myweatherapp.entity.WeatherEntity
import kotlinx.coroutines.flow.Flow

class WeatherRepository(private val weatherDao: WeatherDao) {
    val allCities: Flow<List<WeatherEntity>> = weatherDao.getAllWeather()

    @WorkerThread
    fun insert(weatherEntity: WeatherEntity) {
        weatherDao.insert(weatherEntity)
    }

    fun getWeather(id: Int): WeatherEntity {
        return weatherDao.getWeather(id)
    }
}