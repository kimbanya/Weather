package com.ban.weather.view_models

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ban.weather.SearchCityResponseModel
import com.ban.weather.WeatherResponseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel @ViewModelInject constructor(private val repository: MainRepository) : ViewModel() {

    private val TAG = javaClass.simpleName

    val weather = MutableLiveData<WeatherResponseModel>()
    val searchedCities = MutableLiveData<List<SearchCityResponseModel>>()
    val allCities = repository.allCities
    val numberOfCities = MutableLiveData<Int>()


    fun getWeather(woeid: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val response = repository.getCityWeather(woeid)

                if (response.isSuccessful) {
                    Log.d(TAG, "[getCityWeather]")
                    weather.postValue(response.body())

                } else {
                    Log.d(TAG, "[Fail to getCityWeather]")
                }
            }
        }
    }

    fun getCities(cityName: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val response = repository.getCityNames(cityName)

                if (response.isSuccessful) {
                    Log.d(TAG, "[getCities]")
                    searchedCities.postValue(response.body())

                } else {
                    Log.d(TAG, "[Fail to getCities]")
                }
            }
        }
    }

}

class MainViewModelFactory(private val repository: MainRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}