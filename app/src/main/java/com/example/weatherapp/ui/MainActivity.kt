package com.example.weatherapp.ui

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.data.model.WeatherResponse
import com.example.weatherapp.data.network.WeatherApi
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.databinding.ActivityMainBinding
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: WeatherViewModel

    private val apiKey =BuildConfig.OPEN_WEATHER_API_KEY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(WeatherApi::class.java)
        val repository = WeatherRepository(api)
        viewModel = ViewModelProvider(this,
            WeatherViewModelFactory(repository))[WeatherViewModel::class.java]

        binding.btnSearch.setOnClickListener {
            val city = binding.etCity.text.toString().trim()
            if (city.isNotEmpty()) {
                hideKeyboard()
                viewModel.fetchWeather(city, apiKey)
            }
        }

        viewModel.weather.observe(this) { weather : WeatherResponse ->
            binding.weatherCard.visibility = View.VISIBLE

            binding.tvCity.text = weather.name
            binding.tvTemp.text = "${weather.main.temp}°C"
            binding.tvDescription.text = weather.weather[0].description.replaceFirstChar { it.uppercase() }
            binding.tvHumidity.text = "Humidity: ${weather.main.humidity}%"

            val iconUrl = "https://openweathermap.org/img/wn/${weather.weather[0].icon}@4x.png"
            Glide.with(this).load(iconUrl).into(binding.imgWeather)
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        currentFocus?.let {
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
}
