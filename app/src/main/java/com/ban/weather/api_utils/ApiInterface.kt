package com.ban.weather.api_utils

import com.ban.weather.WeatherResponseModel
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface {

    companion object {

        var BASE_URL = "https://www.metaweather.com/api/"

        fun create(): ApiInterface {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(ApiInterface::class.java)
        }
    }

    @GET("location/{woeid}")
    suspend fun getWeatherById(@Path("woeid") woeid: Long): Response<WeatherResponseModel>

}