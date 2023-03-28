package com.example.myweatherapp
import com.example.myweatherapp.model.Weather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryName

interface WeatherApi {
        @GET("forecast")
        suspend fun forecast(
            @Query("latitude") latitude: Float,
            @Query("longitude") longitude: Float,
            @Query("hourly") hourly: String = "temperature_2m,apparent_temperature"
        ) : Response<Weather>
}