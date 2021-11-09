package com.example.weatherapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weatherapp.db.dao.WeatherDao
import com.example.weatherapp.db.models.WeatherData

@Database(entities = [
    WeatherData::class,
], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){

    abstract fun weatherDao() : WeatherDao

}