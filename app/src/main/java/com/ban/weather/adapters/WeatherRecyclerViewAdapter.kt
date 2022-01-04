package com.ban.weather.adapters

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.ban.weather.WeatherFragment
import com.ban.weather.databinding.ItemWeatherBinding
import com.ban.weather.models.ConsolidatedWeatherModel
import com.bumptech.glide.Glide

class WeatherRecyclerViewAdapter(private val context: WeatherFragment)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = javaClass.simpleName

    private var futureWeatherInfoList: List<ConsolidatedWeatherModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding : ItemWeatherBinding = ItemWeatherBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return WeatherViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as WeatherViewHolder).bind(futureWeatherInfoList[position])

        // Space out among items
        val layoutParams = holder.itemView.layoutParams
        layoutParams.height = 130
        holder.itemView.requestLayout()
    }

    override fun getItemCount(): Int = futureWeatherInfoList.size

    inner class WeatherViewHolder(private val binding : ItemWeatherBinding) : RecyclerView.ViewHolder(binding.root) {
        private val nextDay = binding.tvNextDate
        private val nextWeatherIcon = binding.ivNextWeatherIcon
        private val nextWeatherStatus = binding.tvNextWeatherStatus
        private val nextHighLow = binding.tvNextHighLow

        @RequiresApi(Build.VERSION_CODES.O)
        @SuppressLint("SetTextI18n")
        fun bind(position: ConsolidatedWeatherModel) {
            nextDay.text = position.applicableDate
            nextWeatherStatus.text = position.weatherStateName
            nextHighLow.text = position.maxTemp.toInt().toString() + "°C / " + position.minTemp.toInt().toString() + "°C"
            val tomorrowWeatherAbbrString = position.weatherStateAbbr
            setImageResource(nextWeatherIcon, tomorrowWeatherAbbrString)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(data: List<ConsolidatedWeatherModel>) {
        futureWeatherInfoList = data
        notifyDataSetChanged()
    }

    fun setImageResource(imageView: ImageView, category:String?) {
        var url = "https://www.metaweather.com/static/img/weather/png/%s.png"
        url = when (category) {
            "sn" -> { java.lang.String.format(url, "sn") }
            "sl" -> { java.lang.String.format(url, "sl") }
            "h" -> { java.lang.String.format(url, "h") }
            "t" -> { java.lang.String.format(url, "t") }
            "hr" -> { java.lang.String.format(url, "hr") }
            "lr" -> { java.lang.String.format(url, "lr") }
            "s" -> { java.lang.String.format(url, "s") }
            "hc" -> { java.lang.String.format(url, "hc") }
            "lc" -> { java.lang.String.format(url, "lc") }
            else -> {
                java.lang.String.format(url, "c")
            }
        }
        Glide.with(imageView.context).load(url).into(imageView)
    }

}