package com.ban.weather.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

data class SearchCityResponseModel(
    val title : String,
    val woeid : Int,
    @SerializedName("latt_long")
    val lattLong : String,
    @SerializedName("location_type")
    val locationType : String
)

@Parcelize
data class WeatherResponseModel(
    @SerializedName("consolidated_weather")
    @IgnoredOnParcel
    val consolidatedWeather: List<ConsolidatedWeatherModel>,

    val time: String,

    @SerializedName("sun_rise")
    val sunRise: String,

    @SerializedName("sun_set")
    val sunSet: String,

    @SerializedName("timezone_name")
    val timezoneName: String,

    @IgnoredOnParcel
    val parent: Parent,

    @IgnoredOnParcel
    val sources: List<Source>,

    val title: String,

    @SerializedName("location_type")
    val locationType: String,

    val woeid: Int,

    @SerializedName("latt_long")
    val lattLong: String,

    val timezone: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        TODO("consolidatedWeather"),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        TODO("parent"),
        TODO("sources"),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(time)
        parcel.writeString(sunRise)
        parcel.writeString(sunSet)
        parcel.writeString(timezoneName)
        parcel.writeString(title)
        parcel.writeString(locationType)
        parcel.writeInt(woeid)
        parcel.writeString(lattLong)
        parcel.writeString(timezone)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WeatherResponseModel> {
        override fun createFromParcel(parcel: Parcel): WeatherResponseModel {
            return WeatherResponseModel(parcel)
        }

        override fun newArray(size: Int): Array<WeatherResponseModel?> {
            return arrayOfNulls(size)
        }
    }
}

@Parcelize
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
