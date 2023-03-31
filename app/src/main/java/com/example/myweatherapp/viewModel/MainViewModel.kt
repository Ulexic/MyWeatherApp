package com.example.myweatherapp.viewModel

import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationRequest
import android.util.Log
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweatherapp.RetrofitClient
import com.example.myweatherapp.database.WeatherDatabase
import com.example.myweatherapp.entity.WeatherEntity
import com.example.myweatherapp.model.Weather
import com.example.myweatherapp.repository.WeatherRepository
import com.example.myweatherapp.view.MainActivity
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel : ViewModel() {
    private lateinit var db: WeatherDatabase
    private val _weather: MutableLiveData<Weather> = MutableLiveData()
    val weather: MutableLiveData<Weather> = _weather
    private var city: Address? = null
    private var cityList: List<Address> = listOf()

    fun setupDatabase(context: Context) {
        val applicationScope = CoroutineScope(SupervisorJob())
        db = WeatherDatabase.getDatabase(context, applicationScope)
        val repository = WeatherRepository(db.weatherDao())

        applicationScope.launch(Dispatchers.IO) {
            val response =
                city?.let { RetrofitClient.apiService.forecast(it.latitude, it.longitude) }
            if (response != null && response.isSuccessful) {
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
            }
        }
    }

    fun setLocation(context: Context, cityName: String) {
        val locationList = getLocationList(context, cityName)
        if (locationList != null && locationList.isNotEmpty()) {
            city = locationList[0]
        }
    }

    private fun setLocation(context: Context, latitude: Double, longitude: Double) {
        val locationList = getLocationList(context, latitude, longitude)
        if (locationList != null && locationList.isNotEmpty()) {
            city = locationList[0]
        }
    }

    private fun getLocationList(context: Context, cityName: String): List<Address> {
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            geocoder.getFromLocationName(cityName, 5).let {
                Log.d("MainViewModel", "getLocationList: $it")
                if (it != null) {
                    if (it.isNotEmpty()) {
                        return it
                    }
                }
            }
        } catch (e: Exception) {
            Log.d("MainViewModel", "setLocation: ${e.message}")
        }
        return listOf()
    }

    private fun getLocationList(
        context: Context,
        latitude: Double,
        longitude: Double
    ): List<Address> {
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            geocoder.getFromLocation(latitude, longitude, 5).let {
                Log.d("MainViewModel", "getLocationList: $it")
                if (it != null) {
                    if (it.isNotEmpty()) {
                        return it
                    }
                }
            }
        } catch (e: Exception) {
            Log.d("MainViewModel", "setLocation: ${e.message}")
        }
        return listOf()
    }

    private fun getWeatherFromCity(context: Context, city: Address) {
        viewModelScope.launch {
            val response = RetrofitClient.apiService.forecast(city.latitude, city.longitude)
            if (response.isSuccessful) {
                response.body()?.let { weather ->
                    _weather.postValue(weather)
                }
            } else {
                throw Exception("Error")
            }
        }
    }

    private fun closeSearchView(searchView: SearchView, context: Context, searchItem: MenuItem) {
        getWeatherFromCity(context, city!!)
        searchView.isIconified = true
        val imm: InputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(searchView.windowToken, 0)
    }

    fun setSearchViewListener(
        searchView: SearchView,
        context: Context,
        popupMenu: PopupMenu,
        searchItem: MenuItem,

        ) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d("MainViewModel", "onQueryTextSubmit: $query")
                query?.let { setLocation(context, it) }
                city?.let { setupDatabase(context) }
                closeSearchView(searchView, context, searchItem)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText == null || newText.isEmpty()) {
                    popupMenu.menu.clear()
                    return true
                }

                viewModelScope.launch(Dispatchers.IO) {
                    cityList = getLocationList(context, newText!!)
                }
                if (cityList.isNotEmpty()) {
                    popupMenu.menu.clear()
                    for (i in cityList.indices) {
                        popupMenu.menu.add("${cityList[i].locality}, ${cityList[i].countryCode} ${cityList[i].adminArea}")
                    }
                    popupMenu.show()
                }
                return true
            }
        })
    }

    fun getCityName(context: Context): String? {
        return city?.locality
    }

    fun setPopupMenuListener(
        popupMenu: PopupMenu,
        mainActivity: MainActivity,
        searchView: SearchView,
        searchItem: MenuItem,
    ) {
        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
            setLocation(mainActivity, item.title.toString())
            closeSearchView(searchView, mainActivity, searchItem)
            true
        })
    }

    private fun checkPermission(mainActivity: MainActivity): Boolean {
        if (ActivityCompat.checkSelfPermission(
                mainActivity,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                mainActivity,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                mainActivity,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
            return false
        }
        return true
    }

    fun getUserLocation(mainActivity: MainActivity) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(mainActivity)
        if (checkPermission(mainActivity)) {
            fusedLocationClient.getCurrentLocation(
                LocationRequest.QUALITY_LOW_POWER,
                object : CancellationToken() {
                    override fun onCanceledRequested(p0: OnTokenCanceledListener) =
                        CancellationTokenSource().token

                    override fun isCancellationRequested() = false
                })
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        setLocation(mainActivity, location.latitude, location.longitude)
                        setupDatabase(mainActivity)
                    }
                }
        } else {
            setLocation(mainActivity, "Montauban")
            setupDatabase(mainActivity)
        }
    }
}
