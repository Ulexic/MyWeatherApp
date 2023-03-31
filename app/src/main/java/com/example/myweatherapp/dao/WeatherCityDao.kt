package com.example.weather.dao

//@Dao
//interface WeatherCityDao {
//    @Query("SELECT * FROM city_table")
//    fun getWeatherCities(): Flow<List<WeatherCityEntity>>
//
//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    suspend fun insert(city: WeatherCityEntity)
//
//    @Query("DELETE FROM city_table")
//    suspend fun deleteAll()
//}