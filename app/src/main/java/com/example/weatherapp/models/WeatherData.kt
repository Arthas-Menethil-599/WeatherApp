package com.example.weatherapp.models

data class WeatherData(
    val id: Long,
    val temp: Double,
    val pressure: Int,
    val humidity: Int,
    val windSpeed: Double
)
