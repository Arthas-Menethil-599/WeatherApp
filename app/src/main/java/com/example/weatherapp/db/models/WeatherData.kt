package com.example.weatherapp.db.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class WeatherData(
    val temp: Double = 0.toDouble(),
    val pressure: Int = 0,
    val humidity: Int = 0,
    val windSpeed: Double = 0.toDouble()
) {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0



}
