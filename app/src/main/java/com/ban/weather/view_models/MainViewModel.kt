package com.ban.weather.view_models

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.ban.weather.WeatherResponseModel
import com.ban.weather.models.CityInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel @ViewModelInject constructor(private val repository: MainRepository) : ViewModel() {

    private val TAG = javaClass.simpleName

    val weather = MutableLiveData<WeatherResponseModel>()
    val numberOfCitiesSearched = MutableLiveData<Int>()
    var favoriteList: LiveData<List<CityInfo>> = repository.favoriteList
    var presentingListMerged = MutableLiveData<List<CityInfo>>()

    init {
        if (favoriteList.value.isNullOrEmpty()) {
            Log.d(TAG, "[init] >> favoriteList is NULL" )
        }
        else {
            Log.d(TAG, "[init] >> favoriteList Size : ${favoriteList.value?.size}" )
            presentingListMerged.postValue(favoriteList.value)
        }
    }

    fun deleteCity(woeid: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.deleteCity(woeid)
                Log.d(TAG, "[deleteCity]")
            }
        }
    }

    fun saveCity(cityInfo: CityInfo) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val foundData = repository.findCityById(cityInfo.woeid)
                if (foundData.isNotEmpty()) {
                    if (foundData[0].woeid == cityInfo.woeid) {
                        Log.d(TAG, "[saveCity] : duplicated data ${cityInfo.cityName}")
                    }
                }
                if (foundData.isEmpty()) {
                    repository.insertCity(cityInfo)
                    Log.d(TAG, "[saveCity] : save successful ${cityInfo.cityName}")
                }
            }
        }
    }

    fun getWeather(woeid: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val response = repository.getCityWeather(woeid)

                if (response.isSuccessful) {
                    Log.d(TAG, "[getCityWeather] result: ${response.body()}")
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

//                repository.deleteAll()

                if (response.isSuccessful) {
                    Log.d(TAG, "[getCities] : API response sucess")
                    val tempList = mutableListOf<CityInfo>()

                    response.body()?.map {
                        var tempCityInfo : CityInfo
                        var isFavorited : Boolean = false

                        for (i in favoriteList.value!!) {
                            if (i.woeid == it.woeid) {
                                isFavorited = true
                                break
                                // Log.d(TAG, "i : ${i.woeid}, it : ${it.woeid}")
                                }
                            }
                        tempCityInfo = CityInfo(
                            woeid = it.woeid,
                            cityName = it.title,
                            isFavorite = isFavorited
                        )

                        tempList.add(tempCityInfo)
                    }

                    presentingListMerged.postValue(tempList)

                    val list = response.body()
                    numberOfCitiesSearched.postValue(list?.size)

                } else {
                    Log.d(TAG, "[Fail to getCities]")
                }
            }
        }
    }

    suspend fun deleteAll() {
        repository.deleteAll()
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