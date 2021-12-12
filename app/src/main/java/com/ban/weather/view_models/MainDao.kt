package com.ban.weather.view_models

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.ban.weather.models.CityInfo

@Dao
interface MainDao {

    @Query("SELECT * from city_info")
    fun getAllCities() : LiveData<List<CityInfo>>

    @Insert
    fun insertCity(cityInfo: CityInfo)

    @Delete
    fun deleteCity(cityInfo: CityInfo)

//    @Query("DELETE from city_info WHERE woeid = :woeid")
//    fun deleteCity(woeid: Int)

}