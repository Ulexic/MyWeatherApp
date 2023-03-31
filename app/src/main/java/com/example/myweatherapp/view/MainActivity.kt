package com.example.myweatherapp.view

import android.app.SearchManager
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import com.example.myweatherapp.R
import com.example.myweatherapp.databinding.ActivityMainBinding
import com.example.myweatherapp.databinding.WeekTemperatureCardBinding
import com.example.myweatherapp.model.Weather
import com.example.myweatherapp.model.getDarkColorFromWeatherCode
import com.example.myweatherapp.model.getLightColorFromWeatherCode
import com.example.myweatherapp.model.getWeatherIconFromWeatherCode
import com.example.myweatherapp.viewModel.MainViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private var isFirstLoading: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        viewModel = MainViewModel()
        viewModel.getUserLocation(this)

        viewModel.weather.observe(this) { weather ->
            val lightColor = getLightColorFromWeatherCode(weather.current_weather.weathercode)

            setAllCards(weather, lightColor)
            setWeatherInfo(weather)
            setColors(weather, lightColor)

            if (isFirstLoading) {
                binding.loading.screen.visibility = android.view.View.GONE
                supportActionBar?.show()
                isFirstLoading = false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        supportActionBar?.title = "test"

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.search).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
        }

        val searchItem = menu.findItem(R.id.search)

        val searchView = searchItem.actionView as SearchView
        searchItem.setOnMenuItemClickListener { item ->
            searchView.isIconified = item.itemId != R.id.search

            false
        }
        val popupMenu: PopupMenu = PopupMenu(this, searchView)
        popupMenu.menuInflater.inflate(R.menu.popup, popupMenu.menu)

        viewModel.setPopupMenuListener(popupMenu, this, searchView, searchItem)
        viewModel.setSearchViewListener(searchView, this, popupMenu, searchItem)

        return true
    }

    private fun setColors(weather: Weather, lightColor: Int) {
        val darkColor = getDarkColorFromWeatherCode(weather.current_weather.weathercode)

        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = darkColor
        supportActionBar?.setBackgroundDrawable(ColorDrawable(darkColor))
        supportActionBar?.title = viewModel.getCityName(this)

        binding.root.setBackgroundColor(lightColor)
        binding.linearLayout.setBackgroundColor(darkColor)
        binding.wave.setColorFilter(darkColor)
    }

    private fun setWeatherInfo(weather: Weather) {
        val weatherIcon = getWeatherIconFromWeatherCode(weather.current_weather.weathercode)
        binding.weatherCard.weatherIcon.setImageResource(weatherIcon)
        binding.weatherCard.Temperature.text =
            "${weather.current_weather.temperature.toString()} °C"

        val hour = LocalTime.now().hour

        binding.weatherCard.Extras.text =
            "Feels like: ${weather.hourly.apparent_temperature[hour]} °C\n" +
                    "Wind speed: ${weather.current_weather.windSpeed_10m} m/s\n" +
                    "Precipitation: ${weather.daily.precipitation_sum[0]} mm"

    }

    private fun setAllCards(weather: Weather, lightColor: Int) {
        val day1 = LocalDate.parse(weather.daily.time[1])
        val day2 = LocalDate.parse(weather.daily.time[2])
        val day3 = LocalDate.parse(weather.daily.time[3])
        val day4 = LocalDate.parse(weather.daily.time[4])
        val day5 = LocalDate.parse(weather.daily.time[5])

        val formatter = DateTimeFormatter.ofPattern("EE")

        setCard(binding.day1, lightColor, weather, day1.format(formatter).uppercase(), 0)
        setCard(binding.day2, lightColor, weather, day2.format(formatter).uppercase(), 1)
        setCard(binding.day3, lightColor, weather, day3.format(formatter).uppercase(), 2)
        setCard(binding.day4, lightColor, weather, day4.format(formatter).uppercase(), 3)
        setCard(binding.day5, lightColor, weather, day5.format(formatter).uppercase(), 4)
    }

    private fun setCard(
        day: WeekTemperatureCardBinding,
        color: Int,
        weather: Weather,
        date: String,
        dayNumber: Int,
    ) {
        day.maxTemp.text = "${weather.daily.temperature_2m_max[dayNumber].toString()} °C"
        day.minTemp.text = "${weather.daily.temperature_2m_min[dayNumber].toString()} °C"
        day.date.text = date
        day.card.setCardBackgroundColor(color)
    }
}