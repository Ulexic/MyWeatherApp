package com.example.myweatherapp.repository

//
//class WeatherCityRepository(private val wordDao: WeatherCityDao) {
//    val allCities: Flow<List<WeatherCityEntity>> = wordDao.getWeatherCities()
//
//    @WorkerThread
//    suspend fun insert(city: WeatherCityEntity) {
//        wordDao.insert(city)
//    }
//}