package com.example.myweatherapp.model

import android.graphics.Color
import com.example.myweatherapp.R
import com.google.gson.annotations.SerializedName

enum class WeatherCode {
    @SerializedName("0")
    CLEAR,

    @SerializedName(value = "1", alternate = ["2", "3"])
    CLOUDY,

    @SerializedName(value = "45", alternate = ["48"])
    FOG,

    @SerializedName(value = "51", alternate = ["53", "55"])
    DRIZZLE,

    @SerializedName(value = "61", alternate = ["63", "65"])
    RAIN,

    @SerializedName(value = "56", alternate = ["57"])
    FREEZING_DRIZZLE,

    @SerializedName(value = "66", alternate = ["67"])
    FREEZING_RAIN,

    @SerializedName(value = "71", alternate = ["73", "75"])
    SNOW_FALL,

    @SerializedName("77")
    SNOW,

    @SerializedName(value = "80", alternate = ["81", "82"])
    RAIN_SHOWERS,

    @SerializedName(value = "85", alternate = ["86"])
    SNOW_SHOWERS,

    @SerializedName("95")
    THUNDERSTORM,

    @SerializedName(value = "96", alternate = ["99"])
    HAIL;
}

fun Int.toWeatherCode(): WeatherCode {
    return when (this) {
        0 -> WeatherCode.CLEAR
        1, 2, 3 -> WeatherCode.CLOUDY
        45, 48 -> WeatherCode.FOG
        51, 53, 55 -> WeatherCode.DRIZZLE
        61, 63, 65 -> WeatherCode.RAIN
        56, 57 -> WeatherCode.FREEZING_DRIZZLE
        66, 67 -> WeatherCode.FREEZING_RAIN
        71, 73, 75 -> WeatherCode.SNOW_FALL
        77 -> WeatherCode.SNOW
        80, 81, 82 -> WeatherCode.RAIN_SHOWERS
        85, 86 -> WeatherCode.SNOW_SHOWERS
        95 -> WeatherCode.THUNDERSTORM
        96, 99 -> WeatherCode.HAIL
        else -> WeatherCode.CLEAR
    }
}

fun getDarkColorFromWeatherCode(weatherCode: WeatherCode): Int {
    return when (weatherCode) {
        WeatherCode.CLEAR -> Color.parseColor("#FFD700")
        WeatherCode.CLOUDY -> Color.parseColor("#808080")
        WeatherCode.FOG -> Color.parseColor("#808080")
        WeatherCode.DRIZZLE -> Color.parseColor("#808080")
        WeatherCode.RAIN -> Color.parseColor("#406187")
        WeatherCode.FREEZING_DRIZZLE -> Color.parseColor("#406187")
        WeatherCode.FREEZING_RAIN -> Color.parseColor("#406187")
        WeatherCode.SNOW_FALL -> Color.parseColor("#82b7f5")
        WeatherCode.SNOW -> Color.parseColor("#82b7f5")
        WeatherCode.RAIN_SHOWERS -> Color.parseColor("#406187")
        WeatherCode.SNOW_SHOWERS -> Color.parseColor("#82b7f5")
        WeatherCode.THUNDERSTORM -> Color.parseColor("#30302f")
        WeatherCode.HAIL -> Color.parseColor("#82b7f5")
    }
}

fun getLightColorFromWeatherCode(weatherCode: WeatherCode): Int {
    return when (weatherCode) {
        WeatherCode.CLEAR -> Color.parseColor("#ffd84d")
        WeatherCode.CLOUDY -> Color.parseColor("#a3a2a2")
        WeatherCode.FOG -> Color.parseColor("#a3a2a2")
        WeatherCode.DRIZZLE -> Color.parseColor("#a3a2a2")
        WeatherCode.RAIN -> Color.parseColor("#5f738a")
        WeatherCode.FREEZING_DRIZZLE -> Color.parseColor("#5f738a")
        WeatherCode.FREEZING_RAIN -> Color.parseColor("#5f738a")
        WeatherCode.SNOW_FALL -> Color.parseColor("#bbd9fc")
        WeatherCode.SNOW -> Color.parseColor("#bbd9fc")
        WeatherCode.RAIN_SHOWERS -> Color.parseColor("#5f738a")
        WeatherCode.SNOW_SHOWERS -> Color.parseColor("#bbd9fc")
        WeatherCode.THUNDERSTORM -> Color.parseColor("#5e5e5e")
        WeatherCode.HAIL -> Color.parseColor("#bbd9fc")
    }
}

fun getWeatherIconFromWeatherCode(weatherCode: WeatherCode): Int? {
    return when (weatherCode) {
        WeatherCode.CLEAR -> R.drawable.clear_day
        WeatherCode.CLOUDY -> R.drawable.cloudy_day
        WeatherCode.FOG -> null
        WeatherCode.DRIZZLE -> null
        WeatherCode.RAIN -> R.drawable.rainy_6
        WeatherCode.FREEZING_DRIZZLE -> null
        WeatherCode.FREEZING_RAIN -> null
        WeatherCode.SNOW_FALL -> R.drawable.snowy_6
        WeatherCode.SNOW -> R.drawable.snowy_6
        WeatherCode.RAIN_SHOWERS -> R.drawable.rainy_6
        WeatherCode.SNOW_SHOWERS -> R.drawable.snowy_6
        WeatherCode.THUNDERSTORM -> R.drawable.thunder
        WeatherCode.HAIL -> null
    }
}