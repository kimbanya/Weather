package com.ban.weather

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.ban.weather.adapters.WeatherRecyclerViewAdapter
import com.ban.weather.databinding.FragmentWeatherBinding
import com.ban.weather.models.ConsolidatedWeatherModel
import com.ban.weather.models.WeatherResponseModel
import com.bumptech.glide.Glide

private const val mKEY = "WeatherFragment"

class WeatherFragment : Fragment() {

    private val TAG = javaClass.simpleName

    private lateinit var binding : FragmentWeatherBinding

    private lateinit var recyclerViewAdapter: WeatherRecyclerViewAdapter

    private lateinit var weatherData: WeatherResponseModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            weatherData = it.getParcelable(mKEY)!!
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentWeatherBinding.inflate(inflater, container, false)

        recyclerView()
        updateTodayView(weatherData)
        updateRecyclerView(weatherData?.consolidatedWeather!!)

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(weatherData: WeatherResponseModel) = WeatherFragment().apply {
            arguments = Bundle().apply {
                putParcelable(mKEY, weatherData)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateTodayView(result: WeatherResponseModel?) {
        val todayWeather = result!!.consolidatedWeather[0]
        val title = binding.tvLocationTitle
        val theTemp = binding.tvTheTemp
        val weatherState = binding.tvWeatherState
        val maxTemp = binding.tvMaxTemp
        val minTemp = binding.tvMinTemp
        val weatherIcon = binding.ivWeatherIcon
        val weatherAbbrString = todayWeather.weatherStateAbbr

        setImageResource(weatherIcon, weatherAbbrString)

        title.text = result!!.title
        theTemp.text = "Current: ${todayWeather.theTemp.toInt()}°C"
        weatherState.text = "${todayWeather.weatherStateName}"
        maxTemp.text = "High: ${todayWeather.maxTemp.toInt()}°C"
        minTemp.text = "Low: ${todayWeather.minTemp.toInt()}°C"
    }

    private fun setImageResource(imageView: ImageView, category:String?) {
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
            else -> { java.lang.String.format(url, "c") }
        }
        Glide.with(imageView.context).load(url).into(imageView)
    }

    private fun updateRecyclerView(dataList: List<ConsolidatedWeatherModel>) {
        val tempList: MutableList<ConsolidatedWeatherModel> = dataList as MutableList<ConsolidatedWeatherModel>
        tempList.removeAt(0)
        recyclerViewAdapter.updateList(tempList)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun recyclerView() {
        recyclerViewAdapter = WeatherRecyclerViewAdapter(this@WeatherFragment)
        binding.rvFutureWeatherInfo.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = recyclerViewAdapter
        }
        recyclerViewAdapter.notifyDataSetChanged()
    }
}