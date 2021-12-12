package com.ban.weather.view_models

import androidx.annotation.WorkerThread
import com.ban.weather.api_utils.ApiInterface
import com.ban.weather.models.CityInfo

class MainRepository(private val mainDao: MainDao) {

    private val TAG = javaClass.simpleName
    private val apiInterface by lazy { ApiInterface.create()}


    val allCities = mainDao.getAllCities()


    // Server
    suspend fun getCityWeather(woeid: Int) = apiInterface.getWeatherById(woeid)


    // DB
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertCity(cityInfo: CityInfo) = mainDao.insertCity(cityInfo)

}