package com.ban.weather.api_utils

import com.ban.weather.BuildConfig
import com.ban.weather.models.SearchCityResponseModel
import com.ban.weather.models.WeatherResponseModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    companion object {

        var BASE_URL = "https://www.metaweather.com/api/"

        fun create(): ApiInterface {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(builderHttpClient())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(ApiInterface::class.java)
        }

        private fun builderHttpClient() : OkHttpClient {
            val client = OkHttpClient.Builder()
            if (BuildConfig.DEBUG) {
                val logging = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.BODY
                client.addInterceptor(logging)
            }
            return client.build()
        }

    }

    @GET("location/{woeid}")
    suspend fun getWeatherById(@Path("woeid") woeid: Int): Response<WeatherResponseModel>

    @GET("location/search/")
    suspend fun getCityNameByName(@Query("query") cityName: String): Response<List<SearchCityResponseModel>>

    @GET("location/search/")
    suspend fun getWeatherByLattLong(@Query("lattlong") lattLong: String) : Response<List<SearchCityResponseModel>>
}