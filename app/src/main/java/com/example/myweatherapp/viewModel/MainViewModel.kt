package com.example.myweatherapp.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myweatherapp.RetrofitClient
import com.example.myweatherapp.database.WeatherDatabase
import com.example.myweatherapp.entity.WeatherEntity
import com.example.myweatherapp.entity.toWeather
import com.example.myweatherapp.model.DailyWeather
import com.example.myweatherapp.model.Weather
import com.example.myweatherapp.repository.WeatherRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private lateinit var db: WeatherDatabase
    private val _weather: MutableLiveData<Weather> = MutableLiveData()
    val weather: MutableLiveData<Weather> = _weather

    fun setupDatabase(context: Context) {
        val applicationScope = CoroutineScope(SupervisorJob())
        db = WeatherDatabase.getDatabase(context, applicationScope)
        val repository = WeatherRepository(db.weatherDao())

        applicationScope.launch(Dispatchers.IO) {
            val response = RetrofitClient.apiService.forecast(44.84f, -0.58f)
            if (response.isSuccessful) {
                response.body()?.let { weather ->
                    _weather.postValue(weather)
                    Log.d("MainViewModel", weather.toString())
                    for (i in 0 until weather.daily.time.size) {
                        val entity = WeatherEntity(
                            weather.daily.time[i],
                            weather.daily.weathercode[i],
                            weather.daily.temperature_2m_max[i],
                            weather.daily.temperature_2m_min[i],
                            weather.daily.precipitation_sum[i],
                            weather.daily.windspeed_10m_max[i],
                        )
                        repository.insert(entity)
                    }
                }
            } else {
                throw Exception("Error")
            }
        }
    }

    fun getDailyWeather(date: Int): DailyWeather {
        return WeatherRepository(db.weatherDao()).getWeather(date).toWeather()
    }
}
