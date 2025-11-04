package com.example.weatherapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.model.WeatherResponse
import com.example.weatherapp.data.repository.WeatherRepository
import kotlinx.coroutines.launch

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {
    private val _weather = MutableLiveData<WeatherResponse>()
    val weather: LiveData<WeatherResponse> = _weather

    fun fetchWeather(city: String, apiKey: String) {
        viewModelScope.launch {
            try {
                val response = repository.getWeather(city, apiKey)
                _weather.postValue(response)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}