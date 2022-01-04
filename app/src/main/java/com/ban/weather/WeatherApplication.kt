package com.ban.weather

import android.app.Application
import com.ban.weather.view_models.MainRepository
import com.ban.weather.view_models.MainRoomDatabase

class WeatherApplication : Application() {

    private val database by lazy { MainRoomDatabase.getDatabase(this) }
    val repository by lazy { MainRepository(database.mainDao()) }
}