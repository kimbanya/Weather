package com.ban.weather.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "city_info")
data class CityInfo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val cityName: String?,
    val woeid: Int?
)