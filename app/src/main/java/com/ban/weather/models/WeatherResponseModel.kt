package com.ban.weather

import com.google.gson.annotations.SerializedName


data class SearchCityResponseModel(
    val title : String,
    val woeid : Int,
    @SerializedName("latt_long")
    val lattLong : String,
    @SerializedName("location_type")
    val locationType : String
)

data class WeatherResponseModel(
    @SerializedName("consolidated_weather")
    val consolidatedWeather: List<ConsolidatedWeatherModel>,

    val time: String,

    @SerializedName("sun_rise")
    val sunRise: String,

    @SerializedName("sun_set")
    val sunSet: String,

    @SerializedName("timezone_name")
    val timezoneName: String,

    val parent: Parent,
    val sources: List<Source>,
    val title: String,

    @SerializedName("location_type")
    val locationType: String,

    val woeid: Int,

    @SerializedName("latt_long")
    val lattLong: String,

    val timezone: String
)

data class ConsolidatedWeatherModel(

    val id: Long,

    @SerializedName("weather_state_name")
    val weatherStateName: String,

    @SerializedName("weather_state_abbr")
    val weatherStateAbbr: String,

    @SerializedName("wind_direction_compass")
    val windDirectionCompass: String,

    val created: String,

    @SerializedName("applicable_date")
    val applicableDate: String,

    @SerializedName("min_temp")
    val minTemp: Double,

    @SerializedName("max_temp")
    val maxTemp: Double,

    @SerializedName("the_temp")
    val theTemp: Double,

    @SerializedName("wind_speed")
    val windSpeed: Double,

    @SerializedName("wind_direction")
    val windDirection: Double,

    @SerializedName("air_pressure")
    val airPressure: Double,

    val humidity: Long,
    val visibility: Double,
    val predictability: Long


)

data class Parent(
    val title: String,

    @SerializedName("location_type")
    val locationType: String,

    val woeid: Int,

    @SerializedName("latt_long")
    val lattLong: String
)

data class Source(
    val title: String,
    val slug: String,
    val url: String,

    @SerializedName("crawl_rate")
    val crawlRate: Long
)
