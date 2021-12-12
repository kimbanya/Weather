package com.ban.weather.view_models

import com.ban.weather.api_utils.ApiInterface

class MainRepository {

    private val TAG = javaClass.simpleName

    private val retrofit: ApiInterface by lazy {
        ApiInterface.create()
    }

    suspend fun getCityWeather(woeid: Long) = retrofit.getWeatherById(woeid)

}