package com.example.weatherapp.rest.models

import com.google.gson.annotations.SerializedName

data class Wind(
    @SerializedName("speed")
    var speed: Float = 0.toFloat(),
    @SerializedName("deg")
    var deg: Float = 0.toFloat()
)
