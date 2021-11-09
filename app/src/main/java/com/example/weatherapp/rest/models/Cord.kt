package com.example.weatherapp.rest.models

import com.google.gson.annotations.SerializedName

data class Cord(
    @SerializedName("lon")
    var lon: Float = 0.toFloat(),
    @SerializedName("lat")
    var lat: Float = 0.toFloat()
)
