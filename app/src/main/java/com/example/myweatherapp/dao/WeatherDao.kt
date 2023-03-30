package com.example.myweatherapp.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myweatherapp.entity.WeatherEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Query("DELETE FROM weather")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(weather: WeatherEntity)

    @Query("SELECT * FROM weather")
    fun getAllWeather(): Flow<List<WeatherEntity>>

    @Query("SELECT * FROM weather WHERE time = :time")
    fun getWeather(time: Int): WeatherEntity
}