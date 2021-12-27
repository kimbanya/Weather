package com.ban.weather

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.ban.weather.databinding.ActivityMainBinding
import com.ban.weather.view_models.MainViewModel
import com.ban.weather.view_models.MainViewModelFactory
import com.bumptech.glide.Glide
import com.google.android.gms.location.*
import java.time.LocalDate

class MainActivity : AppCompatActivity() {

    private val TAG = javaClass.simpleName

    private lateinit var binding : ActivityMainBinding

    // View Model
    private val mainViewModel : MainViewModel by viewModels { MainViewModelFactory((application as WeatherApplication).repository) }

    // Current Location
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    lateinit var mLastLocation: Location
    internal lateinit var mLocationRequest: LocationRequest
    private val REQUEST_PERMISSION_LOCATION = 10


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mLocationRequest =  LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        if (requestGpsPermission(this)) {
            startLocationUpdates()
        }

        // Get Weather Info from Api
//        mainViewModel.getWeather(1118370) // Example:Tokyo

        // Move to a Acticity of Search City
        binding.fbaAddCityButton.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java).apply {
                putExtra("test", "TEST MESSAGE using Intent")
            }
            startActivity(intent)
        }

        addObservers()

    }

    private fun startLocationUpdates() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        mFusedLocationProviderClient!!.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation
            onLocationChanged(locationResult.lastLocation)
        }
    }

    fun onLocationChanged(location: Location) {
        val latitude = Math.round(location.latitude.toFloat() * 1000) / 1000f
        val longitude = Math.round(location.longitude.toFloat() * 1000) / 1000f

        val sampleText = "위도 + 경도 : $latitude,$longitude"
        binding.tvLocationTest.text = sampleText

        val arr = arrayOf(latitude, longitude)
        mainViewModel.getWeatherByLattLong(sampleText)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addObservers() {
        mainViewModel.weather.observe(this, {
            Log.d(TAG, "[observe] ${it.title}")
            updateView(it)
        })
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun requestGpsPermission(context: Context) : Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                true
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_PERMISSION_LOCATION)
                false
            }
        } else {
            true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates()
            } else {
                Log.d(TAG, "onRequestPermissionsResult")
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


