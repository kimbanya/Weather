package com.ban.weather.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "city_info")
data class CityInfo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val woeid: Int,
    val cityName: String,
    var isFavorite: Boolean
) {
    @Ignore constructor(woeid: Int, cityName: String, isFavorite: Boolean) : this(
        id = 0,
        woeid = woeid,
        cityName = cityName,
        isFavorite = false
    )
}