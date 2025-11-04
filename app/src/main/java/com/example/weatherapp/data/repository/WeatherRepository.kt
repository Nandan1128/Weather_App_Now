package com.example.weatherapp.data.repository

import com.example.weatherapp.data.model.WeatherResponse
import com.example.weatherapp.data.network.WeatherApi

class WeatherRepository(private val api: WeatherApi) {
    suspend fun getWeather(city: String, apiKey: String): WeatherResponse {
        return api.getWeatherByCity(city, apiKey)
    }
}