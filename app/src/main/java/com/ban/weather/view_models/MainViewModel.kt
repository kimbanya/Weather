package com.ban.weather.view_models

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ban.weather.WeatherResponseModel
import com.ban.weather.api_utils.ApiInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {

    private val TAG = javaClass.simpleName

    val weather = MutableLiveData<WeatherResponseModel>()

    // lateinit 과 차이?
    private val retrofit: ApiInterface by lazy {
        ApiInterface.create()
    }

    fun getWeather(woeid: Long) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val response = retrofit.getWeatherById(woeid) // woeid.toString()
                if (response.isSuccessful) {
                    Log.d(TAG, "[getCityWeather]")
                    weather.postValue(response.body())
                } else {
                    Log.d(TAG, "[Fail to getCityWeather]")

                }
            }
        }
    }

}