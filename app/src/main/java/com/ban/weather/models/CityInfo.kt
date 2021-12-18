package com.ban.weather.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "city_info")
data class CityInfo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val cityName: String?,
    val woeid: Int?
) {
    @Ignore constructor(cityName: String, woeid: Int) : this(
        id = 0,
        cityName = cityName,
        woeid = woeid
    )
}