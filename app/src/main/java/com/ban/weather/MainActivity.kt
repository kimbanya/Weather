package com.ban.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

class MainActivity : AppCompatActivity() {

    private val TAG = javaClass.simpleName

    private val retrofit: ApiInterface by lazy {
        ApiInterface.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        getCityWeather(44418)

    }

    fun getCityWeather(woeid: Long) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                val response = retrofit.getWeatherById(woeid) // woeid.toString()
                if (response.isSuccessful) {
                    Log.d(TAG, "[getCityWeather]")
                    updateView(response.body())
                } else {
                    Log.d(TAG, "[Fail to getCityWeather]")

                }
            }
        }
    }

    private fun updateView(result: WeatherResponseModel?) {
        val title: TextView = findViewById(R.id.tv_location_title)
        title.text = result!!.title
    }

}

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

