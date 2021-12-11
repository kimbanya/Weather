package com.ban.weather

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.ban.weather.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.text.DateFormat
import java.time.LocalDate
import java.util.*

class MainActivity : AppCompatActivity() {

    private val TAG = javaClass.simpleName

    private val retrofit: ApiInterface by lazy {
        ApiInterface.create()
    }

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateView(result: WeatherResponseModel?) {
        val localDate: LocalDate = LocalDate.now()

        val title = binding.tvLocationTitle
        val theTemp = binding.tvTheTemp
        val weatherState = binding.tvWeatherState
        val maxTemp = binding.tvMaxTemp
        val minTemp = binding.tvMinTemp

        val tomorrowDay = binding.tvTomorrowDay
        val tomorrowWeatherIcon = binding.ivTomorrowWeatherIcon
        val tomorrowWeatherStatus = binding.tvTomorrowWeatherStatus
        val tomorrowHighLow = binding.tvTomorrowHighLow

        title.text = result!!.title
        theTemp.text = result!!.consolidatedWeather[0].theTemp.toString()
        weatherState.text = result!!.consolidatedWeather[0].weatherStateName
        maxTemp.text = result!!.consolidatedWeather[0].maxTemp.toString()
        minTemp.text = result!!.consolidatedWeather[0].minTemp.toString()

        tomorrowDay.text = "${localDate.monthValue}/${localDate.dayOfMonth}"
//        tomorrowWeatherIcon. = result!!.consolidatedWeather[1].weatherStateAbbr
        tomorrowWeatherStatus.text = result!!.consolidatedWeather[1].weatherStateName
        tomorrowHighLow.text = result!!.consolidatedWeather[1].maxTemp.toString() + "/" + result!!.consolidatedWeather[1].minTemp.toString()

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

