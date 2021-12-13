package com.ban.weather

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.ban.weather.databinding.ActivityMainBinding
import com.ban.weather.view_models.MainViewModel
import com.ban.weather.view_models.MainViewModelFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import java.time.LocalDate

class MainActivity : AppCompatActivity() {

    private val TAG = javaClass.simpleName

    private lateinit var binding : ActivityMainBinding

    // View Model
    private val mainViewModel : MainViewModel by viewModels { MainViewModelFactory((application as WeatherApplication).repository) }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        mainViewModel.weather.observe(this, {
            Log.d(TAG, "[observe] ${it.title}")
            updateView(it)
        })

        mainViewModel.getWeather(1118370) // Example:Tokyo

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateView(result: WeatherResponseModel?) {
        val localDate: LocalDate = LocalDate.now()

        val title = binding.tvLocationTitle
        val theTemp = binding.tvTheTemp
        val weatherState = binding.tvWeatherState
        val maxTemp = binding.tvMaxTemp
        val minTemp = binding.tvMinTemp
        val weatherIcon = binding.ivWeatherIcon


        val tomorrowDay = binding.tvTomorrowDay
        val tomorrowWeatherIcon = binding.ivTomorrowWeatherIcon
        val tomorrowWeatherStatus = binding.tvTomorrowWeatherStatus
        val tomorrowHighLow = binding.tvTomorrowHighLow

        title.text = result!!.title
        theTemp.text = "Temperature : ${result!!.consolidatedWeather[0].theTemp.toInt().toString()}'"
        weatherState.text = "Weather: ${result!!.consolidatedWeather[0].weatherStateName}"
        maxTemp.text = "High temp : ${result!!.consolidatedWeather[0].maxTemp.toInt().toString()}'"
        minTemp.text = "Low temp : ${result!!.consolidatedWeather[0].minTemp.toInt().toString()}'"

        val weatherAbbrString = result!!.consolidatedWeather[0].weatherStateAbbr
        setImageResource(weatherIcon, weatherAbbrString)


        tomorrowDay.text = "${localDate.plusDays(1)}"
        tomorrowWeatherStatus.text = result!!.consolidatedWeather[1].weatherStateName
        tomorrowHighLow.text = "${result!!.consolidatedWeather[1].maxTemp.toInt().toString()}'/${result!!.consolidatedWeather[1].minTemp.toInt().toString()}'"

        val tomorrowWeatherAbbrString = result!!.consolidatedWeather[1].weatherStateAbbr
        setImageResource(tomorrowWeatherIcon, tomorrowWeatherAbbrString)
    }

    fun setImageResource(imageView: ImageView, category:String?) {
        var url = "https://www.metaweather.com/static/img/weather/png/%s.png"
        when (category) {
            "sn" -> { url = java.lang.String.format(url, "sn")}
            "sl" -> {url = java.lang.String.format(url, "sl")}
            "h" -> {url = java.lang.String.format(url, "h")}
            "t" -> {url = java.lang.String.format(url, "t")}
            "hr" -> {url = java.lang.String.format(url, "hr")}
            "lr" -> {url = java.lang.String.format(url, "lr")}
            "s" -> {url = java.lang.String.format(url, "s")}
            "hc" -> {url = java.lang.String.format(url, "hc")}
            "lc" -> {url = java.lang.String.format(url, "lc")}
            else -> {
                // "c" will fall under here
                url = java.lang.String.format(url, "c")
            }
        }
        Glide.with(imageView.context).load(url).into(imageView)
    }

}


