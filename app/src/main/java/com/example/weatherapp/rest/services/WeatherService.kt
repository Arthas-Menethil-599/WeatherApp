package com.example.weatherapp.rest.services

import com.example.weatherapp.models.WeatherData
import com.example.weatherapp.rest.models.WeatherResponse
import io.reactivex.Observable
import retrofit2.http.*

interface WeatherService {

    @GET("weather")
    suspend fun getData(@Query("q") cityName: String, @Query("appid") apiKey : String) : WeatherResponse

}