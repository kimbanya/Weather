package com.ban.weather

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.ban.weather.databinding.ItemWeatherBinding
import com.bumptech.glide.Glide

class WeatherRecyclerViewAdapter(private val context: Context)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = javaClass.simpleName

    private var futureWeatherInfoList: List<ConsolidatedWeatherModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding : ItemWeatherBinding = ItemWeatherBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return WeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as WeatherViewHolder).bind(futureWeatherInfoList[position], context)
    }

    override fun getItemCount(): Int {
        return futureWeatherInfoList.size
    }

    inner class WeatherViewHolder(private val binding : ItemWeatherBinding) : RecyclerView.ViewHolder(binding.root) {
//        val tomorrowDay = binding.tvTomorrowDay
//        val tomorrowWeatherIcon = binding.ivTomorrowWeatherIcon
//        val tomorrowWeatherStatus = binding.tvTomorrowWeatherStatus
//        val tomorrowHighLow = binding.tvTomorrowHighLow

        fun bind(position: ConsolidatedWeatherModel, context: Context) {
//            tomorrowDay.text = "${localDate.plusDays(1)}"
//            tomorrowWeatherStatus.text = result!!.consolidatedWeather[1].weatherStateName
//            tomorrowHighLow.text = "${result!!.consolidatedWeather[1].maxTemp.toInt().toString()}'/${result!!.consolidatedWeather[1].minTemp.toInt().toString()}'"
//            val tomorrowWeatherAbbrString = result!!.consolidatedWeather[1].weatherStateAbbr
//            setImageResource(tomorrowWeatherIcon, tomorrowWeatherAbbrString)
        }

    }

    fun updateList(data: List<ConsolidatedWeatherModel>) {
        futureWeatherInfoList = data
        notifyDataSetChanged()
    }

    fun setImageResource(imageView: ImageView, category:String?) {
        var url = "https://www.metaweather.com/static/img/weather/png/%s.png"
        when (category) {
            "sn" -> { url = java.lang.String.format(url, "sn")}
            "sl" -> {url = java.lang.String.format(url, "sl")}
            "h" -> {url = java.lang.String.format(url, "h")}
            "t" -> {url = java.lang.String.format(url, "t")}
            "hr" -> {url = java.lang.String.format(url, "hr")}
            "lr" -> {url = java.lang.String.format(url, "lr")}
            "s" -> {url = java.lang.String.format(url, "s")}
            "hc" -> {url = java.lang.String.format(url, "hc")}
            "lc" -> {url = java.lang.String.format(url, "lc")}
            else -> {
                url = java.lang.String.format(url, "c")
            }
        }
        Glide.with(imageView.context).load(url).into(imageView)
    }

}