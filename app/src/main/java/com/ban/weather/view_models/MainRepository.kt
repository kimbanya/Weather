package com.ban.weather.view_models

import androidx.annotation.WorkerThread
import com.ban.weather.api_utils.ApiInterface
import com.ban.weather.models.CityInfo

class MainRepository(private val mainDao: MainDao) {

    private val TAG = javaClass.simpleName
    private val apiInterface by lazy { ApiInterface.create()}

    val favoriteList = mainDao.getFavoriteList()

    // Server
    suspend fun getCityWeather(woeid: Int) = apiInterface.getWeatherById(woeid)
    suspend fun getCityNames(cityName: String) = apiInterface.getCityNameByName(cityName)
    suspend fun getCityWeatherByLattLong(lattLong : String) = apiInterface.getWeatherByLattLong(lattLong)

    // DB
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertCity(cityInfo: CityInfo) = mainDao.insertCity(cityInfo)

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteAll() = mainDao.deleteAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteCity(woeid: Int) = mainDao.deleteCity(woeid)

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun findCityById(woeid: Int) = mainDao.findCityById(woeid)
}