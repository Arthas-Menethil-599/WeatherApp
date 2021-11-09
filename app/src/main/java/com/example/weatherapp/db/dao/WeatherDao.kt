package com.example.weatherapp.db.dao

import androidx.room.*
import com.example.weatherapp.db.models.WeatherData
import com.example.weatherapp.rest.models.Weather

@Dao
interface WeatherDao {

    @Query("SELECT * FROM WeatherData")
    fun getWeatherData() : List<WeatherData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeatherData(item: WeatherData)

    @Transaction
    suspend fun addWeatherData(item: WeatherData){
        insertWeatherData(item)
    }

}