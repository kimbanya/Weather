package com.ban.weather

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import com.ban.weather.databinding.ActivityMainBinding
import com.ban.weather.view_models.MainViewModel
import java.time.LocalDate

class MainActivity : AppCompatActivity() {

    private val TAG = javaClass.simpleName

    private lateinit var binding : ActivityMainBinding

//    private lateinit var viewModel : MainViewModel
    private val viewModel = MainViewModel()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        viewModel.weather.observe(this, {
            Log.d(TAG, "[observe] ${it.title}")
            updateView(it)
        })

        viewModel.getWeather(1118370)

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
        theTemp.text = "Temperature : ${result!!.consolidatedWeather[0].theTemp.toInt().toString()}'"
        weatherState.text = "Weather: ${result!!.consolidatedWeather[0].weatherStateName}"
        maxTemp.text = "High temp : ${result!!.consolidatedWeather[0].maxTemp.toInt().toString()}'"
        minTemp.text = "Low temp : ${result!!.consolidatedWeather[0].minTemp.toInt().toString()}'"

        tomorrowDay.text = "${localDate.plusDays(1)}"
//        tomorrowWeatherIcon. = result!!.consolidatedWeather[1].weatherStateAbbr
        tomorrowWeatherStatus.text = result!!.consolidatedWeather[1].weatherStateName
        tomorrowHighLow.text = "${result!!.consolidatedWeather[1].maxTemp.toInt().toString()}'/${result!!.consolidatedWeather[1].minTemp.toInt().toString()}'"

    }

}


