package com.ban.weather.view_models

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ban.weather.models.CityInfo

@Dao
interface MainDao {

    @Query("SELECT * FROM city_info WHERE isFavorite")
    fun getFavoriteList() : LiveData<List<CityInfo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCity(cityInfo: CityInfo)

    @Query("DELETE from city_info WHERE woeid is :woeid")
    fun deleteCity(woeid: Int)

    @Query("DELETE FROM city_info")
    fun deleteAll()

    @Query("SELECT * FROM city_info WHERE woeid is :woeid")
    fun findCityById(woeid: Int) : List<CityInfo>
}